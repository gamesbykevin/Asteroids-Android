package com.gamesbykevin.asteroids.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.asteroid.Asteroid;
import com.gamesbykevin.asteroids.entity.asteroid.Asteroids;
import com.gamesbykevin.asteroids.entity.effect.Effects;
import com.gamesbykevin.asteroids.entity.laser.Lasers;
import com.gamesbykevin.asteroids.entity.ship.Ship;
import com.gamesbykevin.asteroids.entity.ship.Ships;
import com.gamesbykevin.asteroids.game.controller.Controller;
import com.gamesbykevin.asteroids.screen.ScreenManager;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final ScreenManager screen;
    
    //paint object to draw text
    private Paint paint;
    
    //our controller object
    private Controller controller;
    
    //is the game being reset
    private boolean reset = false;
    
    //has the player been notified (has the user seen the loading screen)
    private boolean notify = false;
    
    //our collection of ships
    private Ships ships;
    
    //our collection of asteroids
    private Asteroids asteroids;
    
    //our collection of lasers
    private Lasers lasers;
    
    //our collection of effects
    private Effects effects;
    
    /**
     * The length to vibrate the phone when you beat a level
     */
    private static final long VIBRATION_DURATION = 500;
    
    /**
     * Create our game object
     * @param screen The main screen
     * @throws Exception
     */
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(24f);
        this.paint.setColor(Color.WHITE);
        this.paint.setLinearText(false);
        
        //create new controller
        this.controller = new Controller(this);
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public ScreenManager getScreen()
    {
        return this.screen;
    }
    
    /**
     * Get the controller object
     * @return The controller object reference
     */
    public Controller getController()
    {
    	return this.controller;
    }
    
    /**
     * Get the effects
     * @return Our collection of objects
     */
    public Effects getEffects()
    {
    	return this.effects;
    }
    
    /**
     * Get the lasers
     * @return Our collection of objects
     */
    public Lasers getLasers()
    {
    	return this.lasers;
    }
    
    /**
     * Get the ships
     * @return Our collection of objects
     */
    public Ships getShips()
    {
    	return this.ships;
    }
    
    /**
     * Get the asteroids
     * @return Our collection of objects
     */
    public Asteroids getAsteroids()
    {
    	return this.asteroids;
    }
    
    @Override
    public void reset() throws Exception
    {
        //flag reset
    	setReset(true);
    }
    
    /**
     * Flag reset, we also will flag notify to false if reset is true
     * @param reset true to reset the game, false otherwise
     */
    private void setReset(final boolean reset)
    {
    	this.reset = reset;
    	
    	//flag that the user has not been notified, since we are resetting
    	if (hasReset())
    		this.notify = false;
    }
    
    /**
     * Do we have reset flagged?
     * @return true = yes, false = no
     */
    protected boolean hasReset()
    {
    	return this.reset;
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
        return this.paint;
    }
    
    @Override
    public void update(final int action, final float x, final float y) throws Exception
    {
    	//if reset we can't continue
    	if (hasReset())
    		return;
    	
        //update the following
        if (getController() != null)
        	getController().update(action, x, y);
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //if we are to reset the game
        if (hasReset())
        {
        	//make sure we have notified first
        	if (notify)
        	{
        		//create a new collection of ships
        		this.ships = new Ships(this);

        		//add default ship
        		this.ships.add(Ship.Type.ShipA);
        		
        		//create a new collection of asteroids
        		this.asteroids = new Asteroids(this);
        		
        		//add a default asteroid
        		this.asteroids.add(0, 0, Asteroid.Type.GreyBig1);
        		
        		//create a new collection of lasers
        		this.lasers = new Lasers(this);
        		
        		//create a new collection of effects
        		this.effects = new Effects();
        		
	        	//flag reset false
	        	setReset(false);
	        	
	        	//reset controller
	        	if (getController() != null)
	        		getController().reset();
        	}
        }
        else
        {
        	getShips().update();
        	getAsteroids().update();
        	getLasers().update();
        	getEffects().update();
        	
        	//update the game elements
        	if (getController() != null)
        		getController().update();
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
    	if (hasReset())
    	{
			//render loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
			
			//flag that the user has been notified
			notify = true;
    	}
    	else
    	{
    		getLasers().render(canvas);
    		getShips().render(canvas);
    		getAsteroids().render(canvas);
    		getEffects().render(canvas);
    		
	    	//render the controller
	    	if (getController() != null)
	    		getController().render(canvas);
    	}
    }
    
    @Override
    public void dispose()
    {
        paint = null;
        
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (asteroids != null)
        {
        	asteroids.dispose();
        	asteroids = null;
        }
        
        if (ships != null)
        {
        	ships.dispose();
        	ships = null;
        }
        
        if (lasers != null)
        {
        	lasers.dispose();
        	lasers = null;
        }
        
        if (effects != null)
        {
        	effects.dispose();
        	effects = null;
        }
    }
}