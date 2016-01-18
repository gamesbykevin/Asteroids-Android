package com.gamesbykevin.asteroids.player;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.asteroid.Asteroid;
import com.gamesbykevin.asteroids.entity.ship.Cpu;
import com.gamesbykevin.asteroids.entity.ship.Ship;
import com.gamesbykevin.asteroids.game.Game;
import com.gamesbykevin.asteroids.overlay.Overlay;
import com.gamesbykevin.asteroids.panel.GamePanel;
import com.gamesbykevin.asteroids.screen.OptionsScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;

public class Player implements IPlayer
{
	//the player's ship
	private Ship ship;
	
	//the text name
	private String name;
	
	//this will contain the stats
	private String description;
	
	//keep track of the lives and the score
	private int lives, score;

	//the location where we render the info etc..
	private int x, y;
	
	//x-coordinate where the icon will be rendered
	private int iconX;
	
	//our paint reference object
	private final Paint paint;
	
	//the player's icon
	private final Bitmap icon;
	
	//temporary rectangle used to calculate coordinates
	private Rect tmp;
	
	/**
	 * The number of digits we expect the score to not exceed
	 */
	private static final int SCORE_LENGTH = 8;
	
	/**
	 * The number of digits we expect the lives to not exceed
	 */
	private static final int LIVES_LENGTH = 2;
	
	/**
	 * We can't let the number of lives get below this value
	 */
	private static final int LIVES_MIN = 0;
	
	//our game reference
	private final Game game;
	
	public Player(final Game game, final boolean human) 
	{
		//store our game reference
		this.game = game;
		
		//store paint reference
		this.paint = game.getPaint();
		
		if (human)
		{
			//set the description
			setName("Human");
			
			//store the human icon
			this.icon = Images.getImage(Assets.ImageGameKey.ShipHumanIcon);
			
			//create our ship
			this.ship = new Ship(Ship.Type.ShipHuman);
		}
		else
		{
			//set the description
			setName("Cpu");
			
			//store the cpu icon
			this.icon = Images.getImage(Assets.ImageGameKey.ShipCpuIcon);
			
			//create our ship
			this.ship = new Cpu(Ship.Type.ShipCpu, game);
		}
		
		//update the y coordinate
		this.y = (human) ? (int)(icon.getHeight() * .5) : (int)(icon.getHeight() * 2.0);
		
		//position the x-coordinate of the icon
		this.iconX = GamePanel.WIDTH - (int)(icon.getWidth() * 1.5);
		
		//the score will always start at 0
		setScore(0);
	}

	/**
	 * Get the ship
	 * @return The ship controller by this player
	 */
	public Ship getShip()
	{
		return this.ship;
	}
	
	@Override
	public void setName(String name) 
	{
		this.name = name;
	}

	@Override
	public String getName() 
	{
		return this.name;
	}
	
	@Override
	public final void updateDescription() 
	{
		String result = "";
		
		result += " Score: ";
		
		//count the missing characters for the score
		int extraScore = SCORE_LENGTH - String.valueOf(getScore()).length();
		
		//add the extra characters
		for (int index = 0; index < extraScore; index++)
		{
			result += "0";
		}
		
		//add the score afterwards
		result += getScore();
		
		//make some space
		result += "      ";
		
		//the missing characters for the lives
		int extraLives = LIVES_LENGTH - String.valueOf(getLives()).length();
		
		//add the extra characters
		for (int index = 0; index < extraLives; index++)
		{
			result += "0";
		}
		
		//add the lives afterwards
		result += getLives();
		
		//add the extra text
		result += " x ";
		
		//update the description
		this.description = result; 
	}

	@Override
	public String getDescription() 
	{
		return this.description;
	}

	@Override
	public void setLives(final int lives)
	{
		this.lives = lives;
		
		//make sure we don't go below the minimum
		if (getLives() < LIVES_MIN)
			this.lives = LIVES_MIN;
		
		//update the x-coordinate
		positionText();
	}

	@Override
	public int getLives() 
	{
		return this.lives;
	}

	@Override
	public void setScore(final int score) 
	{
		this.score = score;
		
		//update the x-coordinate
		positionText();
	}

	@Override
	public int getScore() 
	{
		return this.score;
	}

	@Override
	public void addScore(final int score)
	{
		setScore(getScore() + score);
	}
	
	/**
	 * Update the coordinates where we render the text.<br>
	 * We should call this method when updating the lives, or the player's score
	 */
	private void positionText()
	{
		//update the description
		updateDescription();
		
		//create object if null
		if (tmp == null)
			tmp = new Rect();
		
		//get the boundary of the rectangle for this text
		paint.getTextBounds(getDescription(), 0, getDescription().length(), tmp);
		
		//update the x coordinate
		x = iconX - tmp.width() - (int)(icon.getWidth() * .5);
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//render the ship
		getShip().render(canvas);
		
		//render the player score, lives, etc...
		canvas.drawText(getDescription(), x, y, paint);

		//render the players icon image
		canvas.drawBitmap(icon, iconX, y - (int)(icon.getHeight() * .5), null);
	}

	@Override
	public void update() throws Exception
	{
		//we can't continue if the ship is dead
		if (getShip().isDead())
			return;
		
		//update ship basics
		getShip().update();
		
		//check for collision with asteroids
		for (Asteroid asteroid : game.getAsteroids().get())
		{
			//if the asteroid has collision with the ship
			if (asteroid.hasCollision(getShip(), false))
			{
				//add effect
				game.getEffects().add(getShip());
				
				//ship was hit
				getShip().setDead(true);
				
				//deduct a life
				setLives(getLives() - 1);
				
				//if this player is human, we will vibrate
				if (getShip().getType() == Ship.Type.ShipHuman)
				{
	        		//make sure vibrate option is enabled
	        		if (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Vibrate) == 0)
	        		{
		        		//get our vibrate object
		        		Vibrator v = (Vibrator) game.getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		        		 
						//vibrate for a specified amount of milliseconds
						v.vibrate(Overlay.VIBRATION_DURATION);
	        		}
				}
				
				//no need to check additional
				break;
			}
		}
	}

	@Override
	public void dispose() 
	{
		if (ship != null)
		{
			ship.dispose();
			ship = null;
		}
		
		tmp = null;
	}
}