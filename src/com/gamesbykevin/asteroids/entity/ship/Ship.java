package com.gamesbykevin.asteroids.entity.ship;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.entity.IEntity;
import com.gamesbykevin.asteroids.panel.GamePanel;

import android.graphics.Canvas;

public final class Ship extends Entity implements IEntity
{
	/**
	 * The width of the ship
	 */
	private static final int WIDTH = 99;
	
	/**
	 * The height of the ship
	 */
	private static final int HEIGHT = 106;

	/**
	 * The rotation per update (in degrees)
	 */
	private static final float ROTATION_SPEED = 9;
	
	/**
	 * No rotation speed
	 */
	private static final float ROTATION_SPEED_NONE = 0;
	
	/**
	 * The default time delay between animations
	 */
	private static final long DEFAULT_ANIMATION_DELAY = 100L;
	
	/**
	 * Different animations for the ship
	 *
	 */
	public enum Key
	{
		Idle, Thrust
	}
	
	//are we speeding
	private boolean thrust = false;
	
    /**
     * The rate at which we can accelerate
     */
    private final double DEFAULT_SPEED_RATE = .25;
    
    /**
     * The current speed rate of the ship
     */
    private double speedRate;
    
    /**
     * The rate at which we slow down the ship when not thrusting
     */
    private final double SPEED_DECELERATE = .995;
    
    /**
     * The different kinds of a ship
     */
    public enum Type
    {
    	ShipA,
    	ShipB,
    }
    
    //store the kind of ship
    private final Type type;
    
    /**
     * The x-coordinates that make up the outline of the ship, used for collision detection 
     */
    private static final int[] XPOINTS = new int[] {-8,22,22,14,28,54,54,28,14,22,22,-9,-11,-21,-21,-11};
    
    /**
     * The y-coordinates that make up the outline of the ship, used for collision detection
     */
    private static final int[] YPOINTS = new int[] {-45,-49,-43,-35,-11,-7,9,13,37,46,50,47,15,7,-6,-12};
    
	/**
	 * Create a new ship
	 * @param key The key to access the ship animations
	 */ 
	protected Ship(final Type type)
	{
		super(XPOINTS, YPOINTS);
		
		//store the type of ship
		this.type = type;
		
		//set the speed rate when accelerating
		setSpeedRate(DEFAULT_SPEED_RATE);
		
		//the desired image key
		final Assets.ImageGameKey imageKey;
		
		//determine animation key
    	switch (type)
    	{
	    	case ShipA:
	    		imageKey = Assets.ImageGameKey.ShipA;
	    		break;
    		
	    	case ShipB:
			default:
				imageKey = Assets.ImageGameKey.ShipB;
	    		break;
    	}
    	
        //add default ship animation to sprite sheet
        getSpritesheet().add(Key.Idle, new Animation(Images.getImage(imageKey), 0, 0, WIDTH, HEIGHT));
		
        //create animation for thrust
        Animation animation = new Animation(Images.getImage(imageKey), WIDTH, 0, WIDTH, HEIGHT, 3, 1, 3);
        
        //the amount of time between each animation
        animation.setDelay(DEFAULT_ANIMATION_DELAY);
        
        //this animation will loop
        animation.setLoop(true);
        
        //add animation to sprite sheet
        getSpritesheet().add(Key.Thrust, animation);
        
		//set dimensions
		super.setWidth(WIDTH);
		super.setHeight(HEIGHT);
		
		//place in the middle
		super.setX((GamePanel.WIDTH / 2) - (WIDTH / 2));
		super.setY((GamePanel.HEIGHT / 2) - (HEIGHT / 2));
		
		//set default animation to idle
		getSpritesheet().setKey(Key.Idle);
	}

	/**
	 * Get the type
	 * @return The kind of ship
	 */
	public Type getType()
	{
		return this.type;
	}
	
	/**
	 * Are we thrusting?
	 * @return true = yes, false = no
	 */
	public boolean hasThrust()
	{
		return this.thrust;
	}
	
	/**
	 * Flag thrust if we want to apply speed.
	 * @param thrust true = yes, false = no
	 */
	public void setThrust(final boolean thrust)
	{
		this.thrust = thrust;
		
		//make sure the appropriate animation is set
		getSpritesheet().setKey(hasThrust() ? Key.Thrust : Key.Idle);
	}
	
	/**
	 * Start rotating right
	 */
	public void rotateRight()
	{
		super.setRotationSpeed(ROTATION_SPEED);
	}
	
	/**
	 * Start rotating left
	 */
	public void rotateLeft()
	{
		super.setRotationSpeed(-ROTATION_SPEED);
	}
	
	/**
	 * Stop rotating
	 */
	public void rotateReset()
	{
		super.setRotationSpeed(ROTATION_SPEED_NONE);
	}
	
	/**
	 * Assign the speed rate
	 * @param speedRate The desired speed of the ship
	 */
	private void setSpeedRate(final double speedRate)
    {
        this.speedRate = speedRate;
    }
    
	/**
	 * Get the speed rate
	 * @return The current speed of the ship
	 */
	private double getSpeedRate()
    {
        return this.speedRate;
    }
	
	@Override
	public void update() throws Exception 
	{
		//update the sprite sheet animation
		super.getSpritesheet().update();
		
		//update the rotation according to the current rotation speed
		super.setRotation(super.getRotation() + super.getRotationSpeed());
		
		//update the velocity accordingly
		if (hasThrust())
		{
			setDX(getDX() + (getSpeedRate() * Math.cos(Math.toRadians(getRotation()))));
            setDY(getDY() + (getSpeedRate() * Math.sin(Math.toRadians(getRotation()))));
        }
        
        //slow down the velocity speed always
        setDX(getDX() * SPEED_DECELERATE);
        setDY(getDY() * SPEED_DECELERATE);
        
        //update the location of the ship
        updateLocation();
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
        //render the ship
		super.render(canvas);
	}
}