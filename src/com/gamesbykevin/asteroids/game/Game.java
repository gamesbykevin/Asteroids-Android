package com.gamesbykevin.asteroids.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.asteroid.Asteroids;
import com.gamesbykevin.asteroids.entity.effect.Effects;
import com.gamesbykevin.asteroids.entity.laser.Lasers;
import com.gamesbykevin.asteroids.game.controller.Controller;
import com.gamesbykevin.asteroids.overlay.Overlay;
import com.gamesbykevin.asteroids.player.Player;
import com.gamesbykevin.asteroids.screen.OptionsScreen;
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
    
    //our collection of asteroids
    private Asteroids asteroids;
    
    //our collection of lasers
    private Lasers lasers;
    
    //our collection of effects
    private Effects effects;
    
    //the human and cpu players
    private Player human, cpu;
    
    //the overlay to display between games
    private Overlay overlay;
    
    /**
     * Create our game object
     * @param screen The main screen
     * @throws Exception
     */
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new controller
        this.controller = new Controller(this);
        
        //create new overlay
        this.overlay = new Overlay(this);
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
    	if (this.effects == null)
    		this.effects = new Effects();
    	
    	return this.effects;
    }
    
    /**
     * Get the lasers
     * @return Our collection of objects
     */
    public Lasers getLasers()
    {
    	if (this.lasers == null)
    		this.lasers = new Lasers(this);
    	
    	return this.lasers;
    }
    
    /**
     * Get the overlay
     * @return The overlay that will control the transition in the game
     */
    public Overlay getOverlay()
    {
    	return this.overlay;
    }
    
    /**
     * Get the asteroids
     * @return Our collection of objects
     */
    public Asteroids getAsteroids()
    {
    	if (this.asteroids == null)
    		this.asteroids = new Asteroids();
    	
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
    		setNotify(false);
    }
    
    /**
     * Do we have reset flagged?
     * @return true = yes, false = no
     */
    public boolean hasReset()
    {
    	return this.reset;
    }
    
    /**
     * Flag notify
     * @param notify True if we notified the user, false otherwise
     */
    private void setNotify(final boolean notify)
    {
    	this.notify = notify;
    }
    
    /**
     * Do we have notify?
     * @return true if we notified the user, false otherwise
     */
    protected boolean hasNotify()
    {
    	return this.notify;
    }
    
    /**
     * Get the human
     * @return The human player reference
     */
    public Player getHuman()
    {
    	return this.human;
    }
    
    /**
     * Get the cpu
     * @return The cpu player reference
     */
    public Player getCpu()
    {
    	return this.cpu;
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
    	//if the object has not been created yet
    	if (this.paint == null)
    	{
            //create new paint object
            this.paint = new Paint();
            this.paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
            this.paint.setTextSize(24f);
            this.paint.setColor(Color.WHITE);
            this.paint.setLinearText(false);
    	}
    	
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
        	if (hasNotify())
        	{
	        	//flag reset false
	        	setReset(false);
        		
        		//remove any existing asteroids
        		getAsteroids().get().clear();
        		
        		//remove any existing lasers
        		getLasers().get().clear();
        		
        		//remove any existing effects
        		getEffects().get().clear();
        		
        		//start at wave 1
        		getOverlay().setWave(1);
        		
        		//reset the overlay
        		getOverlay().reset();
        		
        		//create ships based on the game mode
        		switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
        		{
        			//classic
        			case OptionsScreen.MODE_CLASSIC:
        			default:
        				this.human = new Player(this, true);
        				this.cpu = null;
        				break;
        				
        			//coop and versus
        			case OptionsScreen.MODE_COOP:
        			case OptionsScreen.MODE_VERSUS:
        				this.human = new Player(this, true);
        				this.cpu = new Player(this, false);
        				break;
        		}
	        	
	        	//reset controller
	        	if (getController() != null)
	        		getController().reset();
	        	
	        	//the number of lives for the ship
	        	final int lives;
	        	
	        	//determine how many lives the ships will get
	    		switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Lives))
	    		{
		    		case 0:
	    			default:
		    			lives = 5;
		    			break;
		    			
		    		case 1:
		    			lives = 10;
		    			break;
		    			
		    		case 2:
		    			lives = 3;
		    			break;
	    		}
	    		
	    		//assign the lives to each player
	    		if (getHuman() != null)
	    			getHuman().setLives(lives);
	    		if (getCpu() != null)
	    			getCpu().setLives(lives);
	    		
	    		//play random song
	    		playSong();
        	}
        }
        else
        {
        	if (getOverlay().isComplete())
        	{
        		if (getHuman() != null)
        			getHuman().update();
        		
        		if (getCpu() != null)
        			getCpu().update();
        		
	        	getAsteroids().update();
	        	getLasers().update();
	        	getEffects().update();
	        	
	        	//update the game elements
	        	if (getController() != null)
	        		getController().update();
	        	
				//determine what to check for by game mode
				switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
				{
					//classic and coop
					case OptionsScreen.MODE_CLASSIC:
					case OptionsScreen.MODE_COOP:
					default:
						//if there are no more asteroids we can move to the next wave
						if (getAsteroids().get().isEmpty())
						{
							//move to the next wave
							getOverlay().setWave(getOverlay().getWave() + 1);
							
							//reset the wave
							getOverlay().reset();
						}
						break;
						
					//versus
					case OptionsScreen.MODE_VERSUS:
						break;
				}
        	}
        	
    		//update always
    		getOverlay().update();

        	//update effects always
        	getEffects().update();
        }
    }
    
    /**
     * Play random song dependent on game mode
     */
    public final void playSong()
    {
        //play different song dependent on game mode
    	switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
    	{
    		default:
        	case OptionsScreen.MODE_CLASSIC:
        		Audio.play(Assets.AudioGameKey.Music1, true);
        		break;
        		
        	case OptionsScreen.MODE_COOP:
        		Audio.play(Assets.AudioGameKey.Music2, true);
        		break;
        		
        	case OptionsScreen.MODE_VERSUS:
        		Audio.play(Assets.AudioGameKey.Music3, true);
        		break;
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
			setNotify(true);
    	}
    	else
    	{
    		//render these elements when the overlay is complete
    		if (getOverlay().isComplete())
    		{
	    		//render the lasers
	    		getLasers().render(canvas);
	    		
	    		//render the asteroids
	    		getAsteroids().render(canvas);
	    		
		    	//render the players
		    	if (getHuman() != null)
		    		getHuman().render(canvas);
		    	if (getCpu() != null)
		    		getCpu().render(canvas);
    		}
    		
    		//render the effects
    		getEffects().render(canvas);
	    	
    		//render these elements when the overlay is complete
    		if (getOverlay().isComplete())
    		{
    	    	//render the controller
    	    	if (getController() != null)
    	    		getController().render(canvas);
    		}
    		
	    	//render the overlay
	    	if (getOverlay() != null)
	    		getOverlay().render(canvas);
    	}
    }
    
    @Override
    public void dispose()
    {
        this.paint = null;
        
        if (human != null)
        {
        	human.dispose();
        	human = null;
        }

        if (cpu != null)
        {
        	cpu.dispose();
        	cpu = null;
        }
        
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