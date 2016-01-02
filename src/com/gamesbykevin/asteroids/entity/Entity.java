package com.gamesbykevin.asteroids.entity;

import com.gamesbykevin.asteroids.panel.GamePanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;

public abstract class Entity extends com.gamesbykevin.androidframework.base.Entity implements IEntity
{
	//the angle to rotate the entity (degrees)
	private float rotation = 0;
	
	//the rotation speed
	private float speed;

	/**
	 * The minimum allowed rotation (degrees)
	 */
	public static float ROTATION_MIN = 0;
	
	/**
	 * The maximum allowed rotation (degrees)
	 */
	public static float ROTATION_MAX = 360;
	
	//is this entity dead
	private boolean dead = false;
	
	//this will be the outline of our entity used for collision detection
	private Path outline;
	
	//the base coordinates for this entity
	private final int[] xpoints, ypoints;
	
	/**
	 * The number of degrees to offset when rendering the image
	 */
	private static final float ROTATION_OFFSET = 90f;
	
	//the region used for collision detection
	private Region region, clip, other;
	
	protected Entity()
	{
		this(null, null);
		//default constructor
	}
	
	protected Entity(final int[] xpoints, final int[] ypoints)
	{
		//store the outline coordinates
		this.xpoints = xpoints;
		this.ypoints = ypoints;
		
		//update outline
		this.updateOutline();
	}
	
	/**
	 * Get the outline
	 * @return The outline for this entity that will be used for collision detection
	 */
	protected Path getOutline()
	{
		//create the outline if it does not exist
		if (this.outline == null)
			this.outline = new Path();
		
		return this.outline;
	}
	
	/**
	 * Do we have collision?
	 * @param entity The entity we want to check
	 * @param adjust If true, the entity (x,y) will be updated to match the center of collision (if collision has been detected here)
	 * @return true if this Entity intersects the entity's are not dead (isDead), false otherwise
	 */
	public boolean hasCollision(final Entity entity, final boolean adjust)
	{
		//we can't have collision if we are dead or if the entity is dead
		if (isDead() || entity.isDead())
			return false;
		
		//create the region if it is not created yet
		if (region == null)
			region = new Region();
		if (other == null)
			other = new Region();
		
		//the boundary of the screen
		if (clip == null)
		{
			//the boundary of the screen will be slightly larger than the visual screen
			final int left = (int)(-GamePanel.WIDTH * .25);
			final int top = (int)(-GamePanel.HEIGHT * .25);
			final int right = (int)(GamePanel.WIDTH * 1.25);
			final int bottom = (int)(GamePanel.HEIGHT * 1.25);
			
			//create the expanded region
			clip = new Region(left, top, right, bottom);
		}
		
		//set the region according to the current outline
		final boolean result1 = region.setPath(getOutline(), clip);
		
		//set the region for the entity's current outline
		final boolean result2 = other.setPath(entity.getOutline(), clip);
		
		//make sure the regions are not empty
		if (result1 && result2)
		{
			//make sure result is false, if true is the result, then the regions do not intersect
			if (!region.quickReject(other))
			{
				//if true, we have collision
				if (region.op(other, Region.Op.INTERSECT))
				{
					//if we are to adjust, place this entity at the center of collision
					if (adjust)
					{
						setX(region.getBounds().left + (region.getBounds().width() / 2));
						setY(region.getBounds().top + (region.getBounds().height() / 2));
					}
					
					//return true because we have collision
					return true;
				}
			}
		}
		
		//there is no collision
		return false;
	}
	
	/**
	 * Update the outline based on the facing rotation and the current position
	 */
	protected final void updateOutline()
	{
		//if the array is null, no need to continue
		if (xpoints == null || ypoints == null)
			return;
		
		//remove the previous coordinates
		getOutline().reset();
		
		//convert the rotation from degrees to radian's
		final double radians = Math.toRadians(getRotation());
		
		//we will offset from the center of the entity
		final double centerX = getX() + (getWidth() / 2);
		final double centerY = getY() + (getHeight() / 2);
		
		//update the coordinates of the outline
		for (int index = 0; index < xpoints.length; index++)
		{
            //determine the new coordinates for the outline based on the rotation
            final double newX = (xpoints[index] * Math.cos(radians)) - (ypoints[index] * Math.sin(radians));
            final double newY = (xpoints[index] * Math.sin(radians)) + (ypoints[index] * Math.cos(radians));
            
            //add the points to the path accordingly
            if (index == 0)
            {
            	getOutline().moveTo((int)(centerX + newX), (int)(centerY + newY));
            }
            else
            {
            	getOutline().lineTo((int)(centerX + newX), (int)(centerY + newY));
            }
		}
	}
	
	/**
	 * Update the location based on the velocity.<br>
	 * We will also make sure we stay on the screen
	 */
	protected void updateLocation()
	{
		//update location
		setX(getX() + getDX());
		setY(getY() + getDY());
		
		//make sure we stay in bounds
		if (getX() < -getWidth())
			setX(GamePanel.WIDTH);
		if (getX() > GamePanel.WIDTH)
			setX(-getWidth());
		if (getY() < -getHeight())
			setY(GamePanel.HEIGHT);
		if (getY() > GamePanel.HEIGHT)
			setY(-getHeight());
		
		//now that we updated the location, update the outline
		updateOutline();	
	}
	
	/**
	 * Is the entity dead?
	 * @return true = yes, false = no
	 */
	public boolean isDead()
	{
		return this.dead;
	}
	
	/**
	 * Flag the entity dead
	 * @param dead true = yes, false = no
	 */
	public void setDead(final boolean dead)
	{
		this.dead = dead;
	}
	
	@Override
	public void setRotation(final float rotation)
	{
		//assign the rotation
		this.rotation = rotation;
		
		//make sure we keep the rotation within range
		if (getRotation() < ROTATION_MIN)
			setRotation(ROTATION_MAX - 1);
		if (getRotation() > ROTATION_MAX)
			setRotation(ROTATION_MIN + 1);
	}
	
	@Override
	public float getRotation()
	{
		return this.rotation;
	}
	
	@Override
	public void setRotationSpeed(final float speed)
	{
		this.speed = speed;
	}
	
	/**
	 * Get the rotate speed
	 * @return The rotation speed while the asteroid is moving
	 */
	@Override
	public float getRotationSpeed()
	{
		return this.speed;
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//if the entity is flagged dead, we won't render it
		if (isDead())
			return;
		
		//save the canvas here so the rotation changes below only affect this object
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		
		//rotate the canvas
        canvas.rotate(getRotation() + ROTATION_OFFSET, (float)(getX() + (getWidth() / 2)), (float)(getY() + (getHeight() / 2)));
        
        //render the current animation
        super.render(canvas);
        
        //restore canvas to previous state so only this object is affected
        canvas.restore();
	}
}