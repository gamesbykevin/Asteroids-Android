package com.gamesbykevin.asteroids.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.ship.Ship;
import com.gamesbykevin.asteroids.game.Game;
import com.gamesbykevin.asteroids.panel.GamePanel;
import com.gamesbykevin.asteroids.screen.OptionsScreen;
import com.gamesbykevin.asteroids.screen.ScreenManager;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be our game controller
 * @author GOD
 */
public class Controller implements IController
{
    //all of the buttons for the player to control
    private HashMap<Assets.ImageGameKey, Button> buttons;
    
    //our game object reference
    private final Game game;
    
    /**
     * The dimensions of the buttons that the user will see
     */
    private final static int BUTTON_DIMENSION = 72;

    /**
     * The dimensions of the game control buttons that the user will see
     */
    private final static int BUTTON_GAME_DIMENSION = 120;
    
    //location of exit button
    private final static int EXIT_X = (int)(BUTTON_DIMENSION * .25);
    private final static int EXIT_Y = (int)(BUTTON_DIMENSION * .25);
    
    //location of sound button
    private final static int SOUND_X = EXIT_X + (int)(BUTTON_DIMENSION * 1.5);
    private final static int SOUND_Y = EXIT_Y;
    
    //location of pause button
    private final static int PAUSE_X = SOUND_X + (int)(BUTTON_DIMENSION * 1.5);
    private final static int PAUSE_Y = EXIT_Y;
    
    //location of the rotate left button
    private final static int ROTATE_L_X = (int)(BUTTON_GAME_DIMENSION * .25);
    private final static int ROTATE_L_Y = (int)(GamePanel.HEIGHT - BUTTON_GAME_DIMENSION - (BUTTON_GAME_DIMENSION * .25));
    
    //location of the rotate right button
    private final static int ROTATE_R_X = ROTATE_L_X + (int)(BUTTON_GAME_DIMENSION * 1.25);
    private final static int ROTATE_R_Y = ROTATE_L_Y;
    
    //location of thrust button
    private final static int THRUST_X = (int)(GamePanel.WIDTH - BUTTON_GAME_DIMENSION - (BUTTON_GAME_DIMENSION * .25));
    private final static int THRUST_Y = ROTATE_L_Y;
    
    //location of fire button
    private final static int FIRE_X = THRUST_X - (int)(BUTTON_GAME_DIMENSION * 1.25);
    private final static int FIRE_Y = ROTATE_L_Y;
    
    /**
     * Default Constructor
     * @param game Object game object reference
     */
    public Controller(final Game game)
    {
        //assign object reference
        this.game = game;
        
        //create temporary list
        List<Assets.ImageGameKey> tmp = new ArrayList<Assets.ImageGameKey>();

        //add button unique key to list
        tmp.add(Assets.ImageGameKey.Pause);
        tmp.add(Assets.ImageGameKey.AudioOff);
        tmp.add(Assets.ImageGameKey.AudioOn);
        tmp.add(Assets.ImageGameKey.Exit);
        tmp.add(Assets.ImageGameKey.Fire);
        tmp.add(Assets.ImageGameKey.Thrust);
        tmp.add(Assets.ImageGameKey.RotateL);
        tmp.add(Assets.ImageGameKey.RotateR);
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageGameKey, Button>();
        
        //add button
        for (Assets.ImageGameKey key : tmp)
        {
            this.buttons.put(key, new Button(Images.getImage(key)));
        }
        
        //update location of our menu buttons
        this.buttons.get(Assets.ImageGameKey.Pause).setX(PAUSE_X);
        this.buttons.get(Assets.ImageGameKey.Pause).setY(PAUSE_Y);
        this.buttons.get(Assets.ImageGameKey.Exit).setX(EXIT_X);
        this.buttons.get(Assets.ImageGameKey.Exit).setY(EXIT_Y);
        this.buttons.get(Assets.ImageGameKey.AudioOn).setX(SOUND_X);
        this.buttons.get(Assets.ImageGameKey.AudioOn).setY(SOUND_Y);
        this.buttons.get(Assets.ImageGameKey.AudioOff).setX(SOUND_X);
        this.buttons.get(Assets.ImageGameKey.AudioOff).setY(SOUND_Y);
        
        //update location of our game buttons
        this.buttons.get(Assets.ImageGameKey.Fire).setX(FIRE_X);
        this.buttons.get(Assets.ImageGameKey.Fire).setY(FIRE_Y);
        this.buttons.get(Assets.ImageGameKey.Thrust).setX(THRUST_X);
        this.buttons.get(Assets.ImageGameKey.Thrust).setY(THRUST_Y);
        this.buttons.get(Assets.ImageGameKey.RotateL).setX(ROTATE_L_X);
        this.buttons.get(Assets.ImageGameKey.RotateL).setY(ROTATE_L_Y);
        this.buttons.get(Assets.ImageGameKey.RotateR).setX(ROTATE_R_X);
        this.buttons.get(Assets.ImageGameKey.RotateR).setY(ROTATE_R_Y);
        
        for (Assets.ImageGameKey key : tmp)
        {
        	switch (key)
        	{
	        	case Pause:
	        	case Exit:
	        	case AudioOn:
	        	case AudioOff:
	                //set the dimensions of each button
	                this.buttons.get(key).setWidth(BUTTON_DIMENSION);
	                this.buttons.get(key).setHeight(BUTTON_DIMENSION);
	        		break;
        		
        		default:
                    //set the dimensions of each button
                    this.buttons.get(key).setWidth(BUTTON_GAME_DIMENSION);
                    this.buttons.get(key).setHeight(BUTTON_GAME_DIMENSION);
        			break;
        	}

            //update the boundary of all buttons
            this.buttons.get(key).updateBounds();
        }
    }
    
    private HashMap<Assets.ImageGameKey, Button> getButtons()
    {
    	return this.buttons;
    }
    
    /**
     * Get our game object reference
     * @return Our game object reference
     */
    private Game getGame()
    {
        return this.game;
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
    	//check if there was a change
    	boolean change = false;
    	
    	switch (action)
    	{
	    	case MotionEvent.ACTION_MOVE:
	    		//check each button in our list
	    		for (Button button : getButtons().values())
	    		{
	    			//if the x,y location is not within the button
	    			if (button != null && button.isVisible() && !button.contains(x, y))
	    			{
	    				//if the button is pressed, it will now be released
	    				if (button.isPressed())
	    				{
	    					button.setPressed(true);
	    					button.setReleased(true);
	    					
	    					//flag change true
	    					change = true;
	    				}
	    			}
	    		}
	    		break;
    	
	    	case MotionEvent.ACTION_UP:
	    	case MotionEvent.ACTION_POINTER_UP:
	    		//check each button in our list
	    		for (Button button : getButtons().values())
	    		{
	    			if (button != null && button.isVisible() && button.contains(x, y))
	    			{
	    				if (button.isPressed())
	    				{
		    				//if contained within the coordinates flag released true
							button.setReleased(true);
		    				
							//flag change true
							change = true;
	    				}
	    			}
	    		}
	    		break;
	    		
	    	case MotionEvent.ACTION_DOWN:
	    	case MotionEvent.ACTION_POINTER_DOWN:
	    		//check each button in our list
	    		for (Button button : getButtons().values())
	    		{
	    			if (button != null && button.isVisible() && button.contains(x, y))
	    			{
	    				//if contained within the coordinates flag pressed true
						button.setPressed(true);
						
						//if the button is pressed, it can't be released
						button.setReleased(false);
						
						//flag change true
						change = true;
	    			}
	    		}
	    		break;
    	}

    	//return if any change was made
    	return change;
    }
    
    @Override
    public void update() throws Exception
    {
    	//we can't continue if the list is null
    	if (getButtons() == null)
    		return;
    	
		//get the human controlled ship, if the ship is dead null will be assigned
		final Ship human = (getGame().getHuman().getShip() == null || getGame().getHuman().getShip().isDead()) ? null : getGame().getHuman().getShip();
    	
    	//check each button to see what changes
    	for (Assets.ImageGameKey key : getButtons().keySet())
    	{
    		//get the current button
    		Button button = getButtons().get(key);
    		
    		//if this button has been pressed and released
    		if (button.isPressed() && button.isReleased())
    		{
                //reset
        		button.setPressed(false);
        		button.setReleased(false);
        		
    			//determine next steps
    			switch (key)
    			{
    				//stop thrusting
	    			case Thrust:
	    				if (human != null)
	    				{
	    					//flag false
	    					human.setThrust(false);
	    					
	    					//stop sound
	    					Audio.stop(Assets.AudioGameKey.Thrust);
	    				}
	    				break;
	    				
	    			//do we need to to anything for these
	    			case Fire:
	    				if (human != null)
	    					getGame().getLasers().add(human);
	    				break;
	    				
	    			//stop rotating if released
	    			case RotateL:
	    			case RotateR:
	    				if (human != null)
	    					human.rotateReset();
	    				break;
    			
	    			case Pause:
	                    //change the state to paused
	                    getGame().getScreen().setState(ScreenManager.State.Paused);
	    				break;
	    				
	    			case Exit:
	                    //change to the exit confirm screen
	                    getGame().getScreen().setState(ScreenManager.State.Exit);
	    				break;
	    				
	    			case AudioOn:
	    			case AudioOff:
	                    //flip the audio setting
	                    Audio.setAudioEnabled(!Audio.isAudioEnabled());

	        	        //determine which button is displayed
	        	        buttons.get(Assets.ImageGameKey.AudioOn).setVisible(Audio.isAudioEnabled());
	        	        buttons.get(Assets.ImageGameKey.AudioOff).setVisible(!Audio.isAudioEnabled());
	                    
	        	        //to maintain consistency, update the options screen as well
	        	        getGame().getScreen().getScreenOptions().setIndex(OptionsScreen.Key.Sound, Audio.isAudioEnabled() ? 0 : 1);
	        	        
	                    //make sure the correct button is showing
	                    if (Audio.isAudioEnabled())
	                    {
	                    	//play the song again
	                    	getGame().playSong();
	                    }
	                    else
	                    {
	                        //if audio is not enabled, stop all sound
	                        Audio.stop();
	                    }
	    				break;
    			
	    			default:
	    				throw new Exception("Key is not handled here: " + key.toString());
    			}
    		}
    		else if (button.isPressed())
    		{
    			//if the button is just pressed and not released
    			switch (key)
    			{
    				//start thrusting
	    			case Thrust:
	    				if (human != null)
	    				{
	    					//if we weren't thrusting previous
	    					if (!human.hasThrust())
	    						Audio.play(Assets.AudioGameKey.Thrust, true);
	    					
	    					//flag thrusting true
	    					human.setThrust(true);
	    				}
	    				break;
	    				
	    			//don't do anything here
	    			case Fire:
	    				break;
	    				
	    			//set the rotate speed
	    			case RotateL:
	    				if (human != null)
	    					human.rotateLeft();
	    				break;
	    				
	    			//set the rotate speed
	    			case RotateR:
	    				if (human != null)
	    					human.rotateRight();
	    				break;
	    			
	    			//no need to do anything for these
	    			case Pause:
	    			case Exit:
	    			case AudioOn:
	    			case AudioOff:
	    				break;
	    				
	    			//no need to do anything here
	    			default:
	    				throw new Exception("Key is not handled here: " + key.toString());
    			}
    		}
    	}
    }
    
    @Override
    public void reset()
    {
    	if (getButtons() != null)
    	{
	        //determine which button is displayed
    		getButtons().get(Assets.ImageGameKey.AudioOn).setVisible(Audio.isAudioEnabled());
    		getButtons().get(Assets.ImageGameKey.AudioOff).setVisible(!Audio.isAudioEnabled());
	        
	        //reset all buttons
	        for (Button button : getButtons().values())
	        {
	        	if (button != null)
	        	{
	        		button.setPressed(false);
	        		button.setReleased(false);
	        	}
	        }
    	}
    }
    
    /**
     * Recycle objects
     */
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            buttons.clear();
            buttons = null;
        }
    }
    
    /**
     * Render the controller
     * @param canvas Write pixel data to this canvas
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the buttons
        if (getButtons() != null)
        {
        	//check each key in the hash map
        	for (Assets.ImageGameKey key : getButtons().keySet())
        	{
        		//get the current button
        		final Button button = getButtons().get(key);
        		
        		//don't continue if button does not exist
        		if (button == null)
        			continue;
        		
        		//render the button
        		button.render(canvas);
        	}
        }
    }
}