package com.gamesbykevin.asteroids.entity.ship;

import com.gamesbykevin.asteroids.common.ICommon;

public interface IShips extends ICommon
{
	/**
	 * Logic to add ship to the list
	 * @param key The unique key to access the ship
	 */
	public void add(final Ship.Type type);
	
	/**
	 * Get the ship
	 * @param type The desired ship
	 * @return The desired ship, if it does not exist, null is returned
	 */
	public Ship getShip(Ship.Type type);
}
