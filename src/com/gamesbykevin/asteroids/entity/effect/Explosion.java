package com.gamesbykevin.asteroids.entity.effect;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.panel.GamePanel;

public final class Explosion extends Effect 
{
	/**
	 * The default key for this animation
	 */
	private static final String DEFAULT_KEY = "Default";
	
	/**
	 * The default time delay for each frame
	 */
	private static final long DEFAULT_DELAY = 100L;
	
	protected Explosion(final Entity entity)
	{
		super();
		
		//our animation object
		Animation animation;
		
		//pick a random animation
		switch (GamePanel.RANDOM.nextInt(3))
		{
			case 0:
				animation = new Animation(Images.getImage(Assets.ImageGameKey.Explosion), 0, 0, 190, 190, 13, 1, 13);
				break;
				
			case 1:
				animation = new Animation(Images.getImage(Assets.ImageGameKey.Explosion), 0, 190, 140, 140, 9, 1, 9);
				break;
			
			case 2:
			default:
				animation = new Animation(Images.getImage(Assets.ImageGameKey.Explosion), 0, 330, 96, 96, 12, 1, 12);
				break;
		}
		
		//we don't want this to loop
		animation.setLoop(false);
		
		//set the delay for each frame
		animation.setDelay(DEFAULT_DELAY);
		
		//add animation to sprite sheet
		super.getSpritesheet().add(DEFAULT_KEY, animation);
		
		//place at the specified entity
		setX(entity);
		setY(entity);
		
		//make sure all explosions have the same width / height
		if (entity.getWidth() > entity.getHeight())
		{
			setWidth(entity.getHeight());
			setHeight(entity.getHeight());
		}
		else
		{
			setWidth(entity.getWidth());
			setHeight(entity.getWidth());
		}
	}

	@Override
	public void update() throws Exception 
	{
		//if the animation has finished, flag this explosion as dead
		if (getSpritesheet().get().hasFinished())
			super.setDead(true);
		
		//update the sprite sheet
		super.getSpritesheet().update();
		
	}
}