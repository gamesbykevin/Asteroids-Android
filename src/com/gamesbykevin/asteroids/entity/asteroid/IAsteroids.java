package com.gamesbykevin.asteroids.entity.asteroid;

import java.util.ArrayList;

import com.gamesbykevin.asteroids.common.ICommon;

public interface IAsteroids extends ICommon
{
	/**
	 * Add an asteroid to our current list
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param type Type of asteroid we want to add
	 * @throws Exception if specified asteroid type is not setup here
	 */
	public void add(final int x, final int y, final Asteroid.Type type) throws Exception;
	
	/**
	 * Get the asteroids
	 * @return The list of current asteroids
	 */
	public ArrayList<Asteroid> get();
	
	/**
	 * Spawn 2 smaller children from the specified parent
	 * @param asteroid The asteroid to spawn children from
	 */
	public void spawnChildren(final Asteroid asteroid) throws Exception;
}
