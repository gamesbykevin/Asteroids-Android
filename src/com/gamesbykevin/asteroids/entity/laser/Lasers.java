package com.gamesbykevin.asteroids.entity.laser;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.entity.asteroid.Asteroid;
import com.gamesbykevin.asteroids.entity.ship.Ship;
import com.gamesbykevin.asteroids.entity.ship.Ship.Type;
import com.gamesbykevin.asteroids.game.Game;
import com.gamesbykevin.asteroids.panel.GamePanel;
import com.gamesbykevin.asteroids.player.Player;
import com.gamesbykevin.asteroids.screen.OptionsScreen;

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
	private static final int LASER_LIMIT = 1;
    
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
		//we can't add a laser if we reached the limit or are dead
		if (getCount(ship.getType()) >= LASER_LIMIT || ship.isDead())
			return;
		
		//add the new laser to our list
		get().add(new Laser(ship));
		
		//play random sound effect
		switch (GamePanel.RANDOM.nextInt(3))
		{
			case 0:
			default:
				Audio.play(Assets.AudioGameKey.Laser1);
				break;
				
			case 1:
				Audio.play(Assets.AudioGameKey.Laser2);
				break;
				
			case 2:
				Audio.play(Assets.AudioGameKey.Laser3);
				break;
		}
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
        		//check collision according to the game mode
    			switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
    			{
    				//classic and coop
    				case OptionsScreen.MODE_CLASSIC:
    				case OptionsScreen.MODE_COOP:
    				default:
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
    	        				
    	        				//add points to the appropriate player
    	        				switch (laser.getSource())
    	        				{
    		        				case ShipHuman:
    		        					game.getHuman().addScore(asteroid.getScore());
    		        					break;
    		        					
    		        				case ShipCpu:
    		        					game.getCpu().addScore(asteroid.getScore());
    		        					break;
    	        				}
    	        				
    	        				//no need to check the other asteroids
    	        				break;
    	        			}
    	        		}
    					break;
    				
    				//versus
    				case OptionsScreen.MODE_VERSUS:
    					
    					//the ship to check
    					Entity ship = null;
    					
    					//the player of the ship
    					Player player;
    					
    					//potential winner
    					Player winner;
    					
    					//we will check for the other ship depending on the source 
        				switch (laser.getSource())
        				{
	        				case ShipHuman:
	        					ship = game.getCpu().getShip();
	        					player = game.getCpu();
	        					
	        					//store the winner
	        					winner = game.getHuman();
	        					break;
	        					
	        				case ShipCpu:
	        				default:
	        					ship = game.getHuman().getShip();
	        					player = game.getHuman();
	        					
	        					//store the winner
	        					winner = game.getCpu();
	        					break;
        				}
        				
        				//make sure we found the ship and player
        				if (ship != null && player != null)
        				{
        					//make sure the ship is not dead, and has collision
        					if (!ship.isDead() && laser.hasCollision(ship, true))
        					{
        						//flag the ship dead
        						ship.setDead(true);
        						
        						//deduct a life
        						player.setLives(player.getLives() - 1);
        						
		                		//add effect to be displayed
		                		game.getEffects().add(ship);
		                		
		        				//flag this laser as dead
		        				laser.setDead(true);
		        				
		        				//add score to winner
		        				winner.addScore(1000);
		        				
		        				//in versus mode, remove all remaining lasers
		        				get().clear();
        					}
        				}
    					break;
    					
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