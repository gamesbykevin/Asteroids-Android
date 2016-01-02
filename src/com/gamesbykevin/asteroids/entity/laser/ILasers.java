package com.gamesbykevin.asteroids.entity.laser;

import java.util.ArrayList;

import com.gamesbykevin.asteroids.common.ICommon;
import com.gamesbykevin.asteroids.entity.ship.Ship;

public interface ILasers extends ICommon
{
	/**
	 * Get the list of lasers
	 * @return The current list of lasers in play
	 */
	public ArrayList<Laser> get();
	
	/**
	 * Add a laser
	 * @param ship The ship that fired the laser
	 */
	public void add(final Ship ship);
	
	/**
	 * Get the laser count
	 * @param type The source of the lasers we are looking for
	 * @return The total amount of lasers found
	 */
	public int getCount(Ship.Type type);
}
