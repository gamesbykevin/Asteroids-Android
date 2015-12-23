package com.gamesbykevin.asteroids.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.HashMap;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.asteroids.MainActivity;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.panel.GamePanel;
import com.gamesbykevin.asteroids.storage.settings.Settings;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons
    private HashMap<Key, Button> buttons;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //our storage settings object
    private Settings settings;
    
    //our paint object for this screen
    private Paint paint;
    
    //buttons to access each button list
    public enum Key
    {
    	Back, Sound, Vibrate, Colors, Instructions, Facebook, Twitter
    }
    
    public OptionsScreen(final ScreenManager screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);

        //create buttons hash map
        this.buttons = new HashMap<Key, Button>();

        //store our screen reference
        this.screen = screen;
        
        //create our paint object for this menu
        this.paint = new Paint(screen.getPaint());
        
        //change font size
        this.paint.setTextSize(ScreenManager.DEFAULT_FONT_SIZE);
        
        //start coordinates
        int y = ScreenManager.BUTTON_Y;
        int x = ScreenManager.BUTTON_X;
        
        //add sound option
        addButtonSound(x, y);
        
        //add vibrate option
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonVibrate(x, y);
        
        //add colors option
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonColors(x, y);
        
        //the back button
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonBack(x, y);
        
        //setup each button
        for (Key key : Key.values())
        {
        	final Button button = buttons.get(key);
        	button.setWidth(MenuScreen.BUTTON_WIDTH);
        	button.setHeight(MenuScreen.BUTTON_HEIGHT);
        	button.updateBounds();
        	button.positionText(paint);
        }
        
        //add social media icons after the above, because the dimensions are different
        addIcons();
        
        //create our settings object last, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    /**
     * Get the list of buttons.<br>
     * We typically use this list to help load/set the settings based on the index of each button.
     * @return The list of buttons on the options screen
     */
    public HashMap<Key, Button> getButtons()
    {
    	return this.buttons;
    }
    
    /**
     * Add icons, including links to social media
     */
    private void addIcons()
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Instructions));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 4.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
    	tmp.setWidth(MenuScreen.ICON_DIMENSION);
    	tmp.setHeight(MenuScreen.ICON_DIMENSION);
    	tmp.updateBounds();
        this.buttons.put(Key.Instructions, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Facebook));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 3));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
    	tmp.setWidth(MenuScreen.ICON_DIMENSION);
    	tmp.setHeight(MenuScreen.ICON_DIMENSION);
    	tmp.updateBounds();
        this.buttons.put(Key.Facebook, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Twitter));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 1.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
    	tmp.setWidth(MenuScreen.ICON_DIMENSION);
    	tmp.setHeight(MenuScreen.ICON_DIMENSION);
    	tmp.updateBounds();
        this.buttons.put(Key.Twitter, tmp);
    }
    
    private void addButtonColors(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Colors: 3");
        button.addDescription("Colors: 4");
        button.addDescription("Colors: 5");
        button.addDescription("Colors: 6");
        button.setX(x);
        button.setY(y);
        this.buttons.put(Key.Colors, button);
    }
    
    private void addButtonBack(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Go  Back");
        button.setX(x);
        button.setY(y);
        this.buttons.put(Key.Back, button);
    }
    
    private void addButtonSound(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Sound: On");
        button.addDescription("Sound: Off");
        button.setX(x);
        button.setY(y);
        this.buttons.put(Key.Sound, button);
    }

    private void addButtonVibrate(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Vibrate: On");
        button.addDescription("Vibrate: Off");
        button.setX(x);
        button.setY(y);
    	this.buttons.put(Key.Vibrate, button);
    }
    
    /**
     * Assign the index.
     * @param key The key of the button we want to change
     * @param index The desired index
     */
    public void setIndex(final Key key, final int index)
    {
    	buttons.get(key).setIndex(index);
    }
    
    /**
     * Get the index selection of the specified button
     * @param key The key of the button we want to check
     * @return The current selection for the specified button key
     */
    public int getIndex(final Key key)
    {
    	return buttons.get(key).getIndex();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        if (buttons != null)
        {
        	for (Key key : Key.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		try
        		{
	        		switch (key)
	        		{
						case Back:
						case Sound:
						case Colors:
						case Vibrate:
							button.positionText(paint);
							break;
							
						//do nothing for these
						case Instructions:
						case Facebook:
						case Twitter:
							break;
							
						default:
							throw new Exception("Key not handled here: " + key);
	        		}
        		}
        		catch (Exception e)
        		{
        			e.printStackTrace();
        		}
        	}
        }
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
    	//we only want motion event up
    	if (event.getAction() != MotionEvent.ACTION_UP)
    		return true;
    	
        if (buttons != null)
        {
        	for (Key key : Key.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//if the button does not exist skip to the next
        		if (button == null)
        			continue;
        		
    			//if we did not select this button, skip to the next
    			if (!button.contains(x, y))
    				continue;
				
				//determine which button
				switch (key)
				{
    				case Back:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    	                //store our settings
    	                settings.save();
    	                
    	                //set ready state
    	                screen.setState(ScreenManager.State.Ready);
    	                
    	                //play sound effect
    	                //Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //no need to continue
    	                return false;
    	                
    				case Vibrate:
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    					
    	                //play sound effect
    	                //Audio.play(Assets.AudioMenuKey.Selection);
    	                
                        //no need to continue
                        return false;
    					
    				case Sound:
    	    			
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    			        
                        //flip setting
                        Audio.setAudioEnabled(!Audio.isAudioEnabled());
                        
                        //we also want to update the audio button in the controller so the correct is displayed
                        if (screen.getScreenGame() != null && screen.getScreenGame().getGame() != null)
                        {
                        	//make sure the controller exists
                    		if (screen.getScreenGame().getGame().getController() != null)
                    			screen.getScreenGame().getGame().getController().reset();
                        }
                        
                        //play sound effect
                        //Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case Colors:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    					
                        //play sound effect
                        //Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //no need to continue
    					return false;
                        
    				case Instructions:
    					
    	                //play sound effect
    	                //Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case Facebook:
    					
    	                //play sound effect
    	                //Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case Twitter:
    					
    	                //play sound effect
    	                //Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    				
    				default:
                    	throw new Exception("Key not setup here: " + key);
				}
        	}
        }
    	
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw our main logo
        canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);
        
        //draw the menu buttons
    	for (Key key : Key.values())
    	{
    		if (buttons.get(key) != null)
    		{
    			switch (key)
    			{
	    			case Back:
	    			case Sound:
	    			case Colors:
	    			case Vibrate:
	    				buttons.get(key).render(canvas, paint);
	    				break;
	    				
	    			case Instructions:
	    			case Facebook:
	    			case Twitter:
	    				buttons.get(key).render(canvas);
	    				break;
	    				
	    			default:
	    				throw new Exception("Button with key not setup here: " + key);
    			}
    		}
    	}
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
        	paint = null;
    	
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (buttons != null)
        {
        	for (Key key : Key.values())
        	{
        		if (buttons.get(key) != null)
        		{
        			buttons.get(key).dispose();
        			buttons.put(key, null);
        		}
        	}
        	
        	buttons.clear();
        	buttons = null;
        }
    }
}