package com.gamesbykevin.asteroids.entity.laser;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.entity.ship.Ship;

public class Laser extends Entity 
{
	/**
	 * The different kinds of lasers
	 */
	protected enum Type
	{
		Blue, Green
	}
	
	/**
	 * The width dimension
	 */
	private static final int WIDTH = 13;
	
	/**
	 * The height dimension
	 */
	private static final int HEIGHT = 54;
	
	/**
	 * The velocity of the laser
	 */
	public static final float VELOCITY = 5.75f;
	
	//the amount of time this laser is valid
	private long duration;
	
	//the time of creation
	private final long time;
	
	/**
	 * The amount of time the laser is valid (milliseconds)
	 */
	private static final long DEFAULT_DURATION = 850L;
	
    /**
     * The x-coordinates that make up the outline of the laser, used for collision detection 
     */
    private static final int[] XPOINTS = new int[] {-HEIGHT / 2, HEIGHT / 2, HEIGHT / 2, -HEIGHT / 2};
    
    /**
     * The y-coordinates that make up the outline of the laser, used for collision detection
     */
    private static final int[] YPOINTS = new int[] {-WIDTH / 2, -WIDTH / 2, WIDTH / 2, WIDTH / 2};
	
    //the ship that fired the laser
    private final Ship.Type source;
    
	public Laser(final Ship ship) 
	{
		super(XPOINTS, YPOINTS);
		
		//store the ship that fired the laser
		this.source = ship.getType();
		
		//store the time creation
		this.time = System.currentTimeMillis();
		
		//set the life span of this laser
		setDuration(DEFAULT_DURATION);
		
		final int x;
		
		//the type of laser
		final Type type;
		
		//determine which laser we use
		switch (ship.getType())
		{
			case ShipHuman:
				type = Type.Blue;
				x = 0;
				break;
		
			case ShipCpu:
			default:
				type = Type.Green;
				x = WIDTH;
				break;
		}
		
		//create the animation
		Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Lasers), x, 0, WIDTH, HEIGHT); 
		
		//add animation to the sprite sheet
		super.getSpritesheet().add(type, animation);
		
		//set the current animation
		super.getSpritesheet().setKey(type);
		
		//set the dimensions
		super.setWidth(WIDTH);
		super.setHeight(HEIGHT);
		
		//assign the desired rotation
		super.setRotation(ship.getRotation());
		
		//convert from degrees to radian's
		final double radians = Math.toRadians(ship.getRotation());
		
		//set the velocity according to the ship
		super.setDX(VELOCITY * Math.cos(radians));
		super.setDY(VELOCITY * Math.sin(radians));
		
		//apply this lasers velocity
		super.setDX(VELOCITY * getDX());
		super.setDY(VELOCITY * getDY());
		
    	//position the laser accordingly
    	setX(ship.getX() + (ship.getWidth() / 2) - (getWidth() / 2));
    	setY(ship.getY() + (ship.getHeight() / 2) - (getHeight() / 2));
    }

	/**
	 * Get the source
	 * @return The ship that fired the laser
	 */
	public Ship.Type getSource()
	{
		return this.source;
	}
	
	/**
	 * Assign the duration
	 * @param duration The amount of time this laser is valid (milliseconds)
	 */
	private void setDuration(final long duration)
	{
		this.duration = duration;
	}
	
	/**
	 * Get the duration
	 * @return The amount of time this laser is valid (milliseconds)
	 */
	private long getDuration()
	{
		return this.duration;
	}
	
	@Override
	public void update() throws Exception 
	{
		//update the location
		updateLocation();
		
		//determine if the life span is over
		if (System.currentTimeMillis() - time >= getDuration())
			super.setDead(true);
	}
}