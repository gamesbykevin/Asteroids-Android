package com.gamesbykevin.asteroids.entity.ship;

import java.util.HashMap;

import com.gamesbykevin.asteroids.entity.asteroid.Asteroid;
import com.gamesbykevin.asteroids.game.Game;

import android.graphics.Canvas;

public class Ships implements IShips
{
	//the list of ships
	private HashMap<Ship.Type, Ship> ships;
	
    //our game reference
    private final Game game;
	
	public Ships(final Game game)
	{
		//store our game reference
		this.game = game;
		
		//create new list for the ships
		this.ships = new HashMap<Ship.Type, Ship>();
	}
	
	@Override
	public void add(final Ship.Type type) 
	{
		getShips().put(type, new Ship(type));
	}
	
	/**
	 * Get the list of ships
	 * @return The current list of ships
	 */
	public Ship getShip(Ship.Type type)
	{
		return this.ships.get(type);
	}
	
	/**
	 * Get the ships
	 * @return The list of ships
	 */
	private HashMap<Ship.Type, Ship> getShips()
	{
		return this.ships;
	}
	
	@Override
	public void dispose() 
	{
		if (ships != null)
		{
			for (Ship.Type key : ships.keySet())
			{
				if (ships.get(key) != null)
				{
					ships.get(key).dispose();
					ships.put(key, null);
				}
			}
			
			ships.clear();
			ships = null;
		}
	}
	
	@Override
	public void update() throws Exception 
	{
		if (getShips() == null)
			return;
		
		for (Ship ship : getShips().values())
		{
			if (ship != null)
			{
				ship.update();
				
				//check for collision with asteroids
				for (Asteroid asteroid : game.getAsteroids().get())
				{
					//if the asteroid has collision with the ship
					if (asteroid.hasCollision(ship, false))
					{
						//add effect
						game.getEffects().add(ship);
						
						ship.setDead(true);
						
						System.out.println("Asteroid collision with ship");
						
						//no need to check the other asteroids
						break;
					}
				}
			}
		}
	}

	@Override
	public void render(final Canvas canvas) throws Exception 
	{
		if (getShips() == null)
			return;
		
		for (Ship ship : getShips().values())
		{
			if (ship != null)
			{
				ship.render(canvas);
			}
		}
	}
}