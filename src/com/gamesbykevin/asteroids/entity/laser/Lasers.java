package com.gamesbykevin.asteroids.entity.laser;

import java.util.ArrayList;

import com.gamesbykevin.asteroids.entity.asteroid.Asteroid;
import com.gamesbykevin.asteroids.entity.ship.Ship;
import com.gamesbykevin.asteroids.entity.ship.Ship.Type;
import com.gamesbykevin.asteroids.game.Game;

import android.graphics.Canvas;

public class Lasers implements ILasers
{
    //our game reference
    private final Game game;
    
    //the list of lasers fired
    private ArrayList<Laser> lasers;

	/**
	 * The limited number of lasers we can have per each ship
	 */
	private static final int LASER_LIMIT = 3;
    
	public Lasers(final Game game) 
	{
		//store our game reference
		this.game = game;
		
		//create a new list for our bullets
		this.lasers = new ArrayList<Laser>();
	}
	

	@Override
	public void add(Ship ship) 
	{
		//we can't add a laser if we reached the limit
		if (getCount(ship.getType()) >= LASER_LIMIT)
			return;
		
		//add the new laser to our list
		get().add(new Laser(ship));
	}

	@Override
	public int getCount(Type type) 
	{
		//the count
		int count = 0;
		
		//check each laser in our list
		for (Laser laser : get())
		{
			//if the type matches the source
			if (laser.getSource() == type)
				count++;
		}
		
		//return result
		return count;
	}
	
	@Override
	public ArrayList<Laser> get()
	{
		return this.lasers;
	}

	@Override
	public void update() throws Exception 
	{
        //update all the lasers
        for (int index = 0; index < get().size(); index++)
        {
        	//get the current laser
        	final Laser laser = get().get(index);
        	
        	//update the laser
        	laser.update();
        	
        	//determine if the laser needs to be removed
        	if (laser.isDead())
        	{
        		//remove from list
        		get().remove(index);
        		
        		//adjust index
        		index--;
        	}
        	else
        	{
        		//check for collision with asteroids
        		for (Asteroid asteroid : game.getAsteroids().get())
        		{
        			if (laser.hasCollision(asteroid, true))
        			{
                		//add effect to be displayed
                		game.getEffects().add(laser);
                		
                		//flag the asteroid dead
                		asteroid.setDead(true);
        				
                		//spawn smaller asteroids
                		game.getAsteroids().spawnChildren(asteroid);
                		
        				//flag this laser as dead
        				laser.setDead(true);
        				
        				System.out.println("Laser collision with asteroid");
        				
        				//no need to check the other asteroids
        				break;
        			}
        		}
        	}
        }
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		//render all the lasers
        for (Laser laser : get())
        {
        	laser.render(canvas);
        }
	}

	@Override
	public void dispose() 
	{
		if (lasers != null)
		{
	        for (int index = 0; index < lasers.size(); index++)
	        {
	        	if (lasers.get(index) != null)
	        	{
	        		lasers.get(index).dispose();
	        		lasers.set(index, null);
	        	}
	        }
	        
	        lasers.clear();
	        lasers = null;
		}
	}
}