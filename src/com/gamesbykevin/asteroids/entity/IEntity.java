package com.gamesbykevin.asteroids.entity;

import com.gamesbykevin.asteroids.common.ICommon;

public interface IEntity extends ICommon
{
	/**
	 * Assign the desired rotation
	 * @param rotation in degrees (0 - 360)
	 */
	public void setRotation(final float rotation);
	
	/**
	 * Get the current rotation
	 * @return The rotation of this entity in degrees (0 - 360)
	 */
	public float getRotation();
	
	/**
	 * Get the rotation speed
	 * @return The current rotation speed (degrees)
	 */
	public float getRotationSpeed();
	
	/**
	 * Set the rotation speed
	 * @param speed The speed we can rotate the entity (degrees)
	 */
	public void setRotationSpeed(final float speed);
}