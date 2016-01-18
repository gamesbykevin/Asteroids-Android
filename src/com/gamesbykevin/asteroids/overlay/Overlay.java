package com.gamesbykevin.asteroids.overlay;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.entity.asteroid.Asteroid;
import com.gamesbykevin.asteroids.entity.ship.Ship;
import com.gamesbykevin.asteroids.game.Game;
import com.gamesbykevin.asteroids.panel.GamePanel;
import com.gamesbykevin.asteroids.screen.OptionsScreen;
import com.gamesbykevin.asteroids.screen.ScreenManager;
import com.gamesbykevin.asteroids.screen.ScreenManager.State;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Vibrator;

public class Overlay implements IOverlay 
{
	//our game reference
	private final Game game;

    //where we draw the message
    private int messageX = 0, messageY = 0;
    
	/**
	 * Darken the background this amount when displaying the overlay 
	 */
	private static final int DARKEN_BACKGROUND_ALPHA = 75;
	
	/**
	 * The amount of time between each transition (milliseconds)
	 */
	private static final long TRANSITION_DELAY = 1500L;
	
    /**
     * The length to vibrate the phone
     */
    public static final long VIBRATION_DURATION = 500;
	
	//keep track of the time elapsed (milliseconds)
	private long elapsed;
	
	//keep track of current time passed (milliseconds)
	private long time;
	
	//keep track of the wave starting at 1
	private int wave = 1;
	
	//the message we render on screen
	private String message;
	
	public Overlay(final Game game) 
	{
		//store our game reference
		this.game = game;
		
		//reset
		reset();
	}
	
	/**
	 * Is the transition complete?
	 * @return true if the time elapsed is greater than the transition delay, false otherwise
	 */
	public boolean isComplete()
	{
		return (elapsed >= TRANSITION_DELAY);
	}
	
	/**
	 * Set the wave
	 * @param wave The desired wave number
	 */
	public void setWave(final int wave)
	{
		this.wave = wave;
	}
	
	/**
	 * Get the wave
	 * @return The current wave number
	 */
	public int getWave()
	{
		return this.wave;
	}
	
	/**
	 * Set the message
	 * @param message The message to be displayed to the user between transitions
	 */
	public void setMessage(final String message)
	{
		this.message = message;
		
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //get the rectangle around the message
        game.getScreen().getPaint().getTextBounds(message, 0, message.length(), tmp);
        
        //calculate the position of the message
        messageX = (GamePanel.WIDTH / 2) - (tmp.width() / 2);
        messageY = (GamePanel.HEIGHT / 2) - (tmp.height() / 2);
	}
	
	/**
	 * Get the message
	 * @return The message to be displayed to the user between transitions
	 */
	public String getMessage()
	{
		return this.message;
	}
	
	@Override
	public final void reset() 
	{
		//reset the time to the current
		this.time = System.currentTimeMillis();
		
		//reset elapsed
		this.elapsed = 0;
		
		//assign the appropriate message
		switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
		{
			//classic, coop
			case OptionsScreen.MODE_CLASSIC:
			case OptionsScreen.MODE_COOP:
			default:
				setMessage("Wave: " + getWave());
				break;
				
			case OptionsScreen.MODE_VERSUS:
				setMessage("Get Ready");
				break;
		}
	}

	@Override
	public void dispose() 
	{
		//no need to recycle anything here
	}

	@Override
	public void update() throws Exception 
	{
		//check if the transition is yet complete
		final boolean complete = isComplete(); 
		
		//add the time elapsed
		elapsed += (System.currentTimeMillis() - time);
		
		//make the time current
		time = System.currentTimeMillis();
		
		//if previously not completed, but now am complete
		if (!complete && isComplete())
		{
			//determine what to do next by the game mode
			switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
			{
				//classic, coop
				case OptionsScreen.MODE_CLASSIC:
				case OptionsScreen.MODE_COOP:
				default:
					
					//only add asteroids here if there are none
					if (game.getAsteroids().get().isEmpty())
					{
						//spawn a number of asteroids depending on the wave
						for (int count = 0; count < getWave(); count++)
						{
							//pick a random type of asteroid
							switch (GamePanel.RANDOM.nextInt(8))
							{
								case 0:
								default:
									game.getAsteroids().add(0, 0, Asteroid.Type.BrownBig1);
									break;
									
								case 1:
									game.getAsteroids().add(0, 0, Asteroid.Type.BrownBig2);
									break;
									
								case 2:
									game.getAsteroids().add(0, 0, Asteroid.Type.BrownBig3);
									break;
									
								case 3:
									game.getAsteroids().add(0, 0, Asteroid.Type.BrownBig4);
									break;
									
								case 4:
									game.getAsteroids().add(0, 0, Asteroid.Type.GreyBig1);
									break;
									
								case 5:
									game.getAsteroids().add(0, 0, Asteroid.Type.GreyBig2);
									break;
									
								case 6:
									game.getAsteroids().add(0, 0, Asteroid.Type.GreyBig3);
									break;
									
								case 7:
									game.getAsteroids().add(0, 0, Asteroid.Type.GreyBig4);
									break;
							}
						}
					}
					
					//place ships back in the middle of the screen
					placeMiddle();
					break;
					
				//versus
				case OptionsScreen.MODE_VERSUS:
            		//the ships will be positioned on opposite ends
					placeOppositeEnds();
					break;
			}
			
			//flag dead false for any existing ships
			resetDead();
			
			//stop ships from moving
			stopVelocity();
		}
		else if (isComplete())
		{
			//determine what to do next by the game mode
			switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
			{
				//classic, coop
				case OptionsScreen.MODE_CLASSIC:
				case OptionsScreen.MODE_COOP:
				default:
					
					//if there are no asteroids, the wave is complete
					if (game.getAsteroids().get().isEmpty())
					{
		        		//make sure vibrate is enabled
		        		if (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Vibrate) == 0)
		        		{
			        		//get our vibrate object
			        		Vibrator v = (Vibrator) game.getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			        		 
							//vibrate for a specified amount of milliseconds
							v.vibrate(VIBRATION_DURATION);
		        		}
						
						//increase the wave
						setWave(getWave() + 1);
						
						//place ships back in the middle of the screen
						placeMiddle();
						
						//reset
						reset();

						//flag dead false for any existing ships
						resetDead();
						
						//stop ships from moving
						stopVelocity();
					}
					else
					{
						//are all ships dead?
						boolean dead = true;
						
						//if the player exists and the ship is not flagged dead, we will flag false
						if (game.getHuman() != null && !game.getHuman().getShip().isDead())
							dead = false;
						//if the player exists and the ship is not flagged dead, we will flag false
						if (game.getCpu() != null && !game.getCpu().getShip().isDead())
							dead = false;
						
						//if all ships are dead
						if (dead)
						{
							//remove all lasers
							game.getLasers().get().clear();
							
							//stop ships from moving
							stopVelocity();
							
							//remove existing lasers
							game.getLasers().get().clear();
							
							//check if the game is over
							checkGameOver();
							
							//reset overlay counter
							reset();
							
							//if the game is over expire the elapsed time
							if (hasGameOver())
							{
								this.elapsed = TRANSITION_DELAY;
							}
							else
							{
								//place existing asteroids in the corner
								for (Asteroid asteroid : game.getAsteroids().get())
								{
									placeEntity(asteroid, 0, 0);
								}
								
								//place ships back in the middle of the screen
								placeMiddle();

								//flag dead false for any existing ships
								resetDead();
							}
						}
					}
					break;

				//versus
				case OptionsScreen.MODE_VERSUS:
					
					//get the human and cpu ships
					Ship human = game.getHuman().getShip();
					Ship cpu = game.getCpu().getShip();
					
					//if a ship is dead
					if (human.isDead() || cpu.isDead())
					{
						//remove all lasers
						game.getLasers().get().clear();
						
						//stop ships from moving
						stopVelocity();
						
						//remove existing lasers
						game.getLasers().get().clear();
						
						//check if the game is over
						checkGameOver();
						
						//reset overlay counter
						reset();
						
						//if the game is over expire the elapsed time
						if (hasGameOver())
						{
							this.elapsed = TRANSITION_DELAY;
						}
						else
						{
		            		//the ships will be positioned on opposite ends
							placeOppositeEnds();
							
							//reset dead
							resetDead();
						}
					}
					break;
			}
		}
	}
	
	/**
	 * Is the game over?
	 * @return true if the state is game over, false otherwise
	 */
	private boolean hasGameOver()
	{
		return (game.getScreen().getState() == ScreenManager.State.GameOver);
	}
	
	/**
	 * Check if the overall game has ended
	 */
	private void checkGameOver()
	{
		//determine what to do next by the game mode
		switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
		{
			//classic
			case OptionsScreen.MODE_CLASSIC:
				
				//if the human has no move lives
				if (game.getHuman().getLives() <= 0)
				{
					//flag game over
					game.getScreen().setState(State.GameOver);
					
					//set message
					game.getScreen().getScreenGameover().setMessage(
						"Game Over", 
						"Score: " + game.getHuman().getScore()
					);
					
					//remove any explosions
					game.getEffects().get().clear();
					
					//remove any existing lasers
					game.getLasers().get().clear();
					
					//play game over sound
					Audio.play(Assets.AudioGameKey.Gameover);
				}
				break;
		
			//coop
			case OptionsScreen.MODE_COOP:
				
				//if neither player has any lives
				if (game.getHuman().getLives() <= 0 && game.getCpu().getLives() <= 0)
				{
					//flag game over
					game.getScreen().setState(State.GameOver);
					
					//set message
					game.getScreen().getScreenGameover().setMessage(
						"Game Over", 
						"Score: " + game.getHuman().getScore()
					);
					
					//remove any explosions
					game.getEffects().get().clear();
					
					//remove any existing lasers
					game.getLasers().get().clear();
					
					//play game over sound
					Audio.play(Assets.AudioGameKey.Gameover);
				}
				break;
				
			//versus
			case OptionsScreen.MODE_VERSUS:
				
				//if anyone lost all their lives, set the message appropriately
				if (game.getHuman().getLives() <= 0)
				{
					//flag game over
					game.getScreen().setState(State.GameOver);
					
					//set message
					game.getScreen().getScreenGameover().setMessage(
						"You lose", 
						"Score: " + game.getHuman().getScore()
					);
					
					//remove any explosions
					game.getEffects().get().clear();
					
					//remove any existing lasers
					game.getLasers().get().clear();
					
					//play game over sound
					Audio.play(Assets.AudioGameKey.Gameover);
				}
				else if (game.getCpu().getLives() <= 0)
				{
					//flag game over
					game.getScreen().setState(State.GameOver);
					
					//set message
					game.getScreen().getScreenGameover().setMessage(
						"You win", 
						"Score: " + game.getHuman().getScore()
					);
					
					//remove any explosions
					game.getEffects().get().clear();
					
					//remove any existing lasers
					game.getLasers().get().clear();
					
					//play game over sound
					Audio.play(Assets.AudioGameKey.Gameover);
				}
				break;
		}
	}

	/**
	 * Mark dead false for any existing ships
	 */
	private void resetDead()
	{
		//make sure any existing ships are not dead
		if (game.getHuman() != null)
			game.getHuman().getShip().setDead(false);
		
		if (game.getCpu() != null)
			game.getCpu().getShip().setDead(false);
		
		//stop thrust sound
		Audio.stop(Assets.AudioGameKey.Thrust);
	}
	
	/**
	 * Stop all existing ships
	 */
	private void stopVelocity()
	{
		//make sure any existing ships are stopped
		if (game.getHuman() != null)
		{
			game.getHuman().getShip().setDX(0);
			game.getHuman().getShip().setDY(0);
			game.getHuman().getShip().setThrust(false);
			game.getHuman().getShip().rotateReset();
		}
		
		if (game.getCpu() != null)
		{
			game.getCpu().getShip().setDX(0);
			game.getCpu().getShip().setDY(0);
			game.getCpu().getShip().setThrust(false);
			game.getCpu().getShip().rotateReset();
		}
	}
	
	/**
	 * Place the existing ships in the middle and stop the velocity
	 */
	private void placeMiddle()
	{
		if (game.getHuman() != null)
			placeEntity(
				game.getHuman().getShip(), 
				(GamePanel.WIDTH / 2) - (game.getHuman().getShip().getWidth() / 2), 
				(GamePanel.HEIGHT / 2) - (game.getHuman().getShip().getHeight() / 2)
			);
		
		if (game.getCpu() != null)
			placeEntity(
				game.getCpu().getShip(), 
				(GamePanel.WIDTH / 2) - (game.getCpu().getShip().getWidth() / 2), 
				(GamePanel.HEIGHT / 2) - (game.getCpu().getShip().getHeight() / 2)
			);
		
		//stop any ships from moving
		stopVelocity();
	}
	
	/**
	 * Place both ships on opposite ends.<br>
	 * Used for versus mode
	 */
	private void placeOppositeEnds()
	{
		if (GamePanel.RANDOM.nextBoolean())
		{
			placeEntity(game.getHuman().getShip(), 0, 0);
			placeEntity(game.getCpu().getShip(), GamePanel.WIDTH - game.getCpu().getShip().getHeight(), GamePanel.HEIGHT - game.getCpu().getShip().getHeight());
		}
		else
		{
			placeEntity(game.getHuman().getShip(), 0, GamePanel.HEIGHT - game.getHuman().getShip().getWidth());
			placeEntity(game.getCpu().getShip(), GamePanel.WIDTH - game.getCpu().getShip().getHeight(), 0);
		}
	}
	
	/**
	 * Place the entity at the specified coordinate
	 * @param entity The entity we want to move
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	private void placeEntity(final Entity entity, final double x, final double y)
	{
		try
		{
			//store existing velocity
			final double dx =  entity.getDX();
			final double dy =  entity.getDY();
			
			//set the velocity
			entity.setDX(x - entity.getX());
			entity.setDY(y - entity.getY());
			
			//update so all coordinates are correct
			entity.update();
			
			//reset the velocity
			entity.setDX(dx);
			entity.setDY(dy);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(Canvas canvas) throws Exception 
	{
		//only render if the transition is not yet complete and the game is not over
		if (!isComplete() && game.getScreen().getState() != State.GameOver)
		{
			//darken background
			ScreenManager.darkenBackground(canvas, DARKEN_BACKGROUND_ALPHA);
			
			//render message
			canvas.drawText(getMessage(), messageX, messageY, game.getScreen().getPaint());
		}
	}
}