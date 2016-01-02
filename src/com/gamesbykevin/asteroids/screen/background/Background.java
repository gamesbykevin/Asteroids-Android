package com.gamesbykevin.asteroids.screen.background;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.panel.GamePanel;
import com.gamesbykevin.asteroids.screen.ScreenManager;

import android.graphics.Canvas;

/**
 * The background in the game
 * @author GOD
 */
public class Background extends Entity 
{
	//our main screen reference
	private final ScreenManager screen;
	
	//the amount of time it will take to scroll across the screen once (milliseconds)
	private static final long DEFAULT_SCROLL_DELAY = 15000L;
	
	//scroll delay
	private long delay;
	
	//the last time update
	private long time;
	
	public Background(final ScreenManager screen) 
	{
		this.screen = screen;
		
        //assign position, size
        setWidth(GamePanel.WIDTH);
        setHeight(GamePanel.HEIGHT);
        
        //add animation to sprite sheet
        getSpritesheet().add(Assets.ImageMenuKey.Background, new Animation(Images.getImage(Assets.ImageMenuKey.Background)));
        
        //position at origin
        reset();
        
        //set the default scroll delay
        setDelay(DEFAULT_SCROLL_DELAY);
	}
	
	public final void setDelay(final long delay)
	{
		this.delay = delay;
	}
	
	private long getDelay()
	{
		return this.delay;
	}
	
	private void reset()
	{
		//reset at origin
        setX(0);
        setY(0);
		
        //store current time
        this.time = System.currentTimeMillis();
	}
	
	/**
	 * Update the scrolling background
	 */
	public void update()
	{
		//update the background for the game, as well as the menu/option screen
		switch (screen.getState())
		{
			case Running:
			case Ready:
			case Options:
				//figure out how much time has passed
				final long elapsed = System.currentTimeMillis() - this.time; 
				
				if (elapsed >= getDelay())
				{
					//if enough time has elapsed reset
					reset();
				}
				else
				{
					//update x-coordinate accordingly
					setX(((double)elapsed / (double)getDelay()) * -getWidth());
				}
				break;
			
			default:
				reset();
				break;
		}
	}
	
	public void render(final Canvas canvas) throws Exception
	{
		//store original location
		final double x = getX();
		final double y = getY();
		
		//render 3 times so the background is always rendered on screen
		setX(x - GamePanel.WIDTH);
		super.render(canvas);
		setX(x);
		super.render(canvas);
		setX(x + GamePanel.WIDTH);
		super.render(canvas);
		
		//restore location
		super.setX(x);
		super.setY(y);
	}
}