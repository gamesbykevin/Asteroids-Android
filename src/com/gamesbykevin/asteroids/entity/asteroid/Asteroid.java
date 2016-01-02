package com.gamesbykevin.asteroids.entity.asteroid;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.panel.GamePanel;

public final class Asteroid extends Entity 
{
	/**
	 * Different types of asteroids and where they are mapped on the sprite sheet
	 */
	public enum Type
	{
		BrownBig1(0,0,101,84),
		BrownBig2(101,0,120,98),
		BrownBig3(221,0,89,82),
		BrownBig4(310,0,98,96),
		BrownMed1(0,98,43,43),
		BrownMed2(43,98,45,40),
		BrownSmall1(88,98,28,28),
		BrownSmall2(116,98,29,26),
		BrownTiny1(145,98,18,18),
		BrownTiny2(163,98,16,15),
		
		GreyBig1(0,200,101,84),
		GreyBig2(101,200,120,98),
		GreyBig3(221,200,89,82),
		GreyBig4(310,200,98,96),
		GreyMed1(0,298,43,43),
		GreyMed2(43,298,45,40),
		GreySmall1(88,298,28,28),
		GreySmall2(116,298,29,26),
		GreyTiny1(145,298,18,18),
		GreyTiny2(163,298,16,15);
		
		//the size of the animation
		private final int x, y, w, h;
		
		private Type(final int x, final int y, final int w, final int h)
		{
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		
		protected int getWidth()
		{
			return this.w;
		}
		
		protected int getHeight()
		{
			return this.h;
		}
		
		protected int getX()
		{
			return this.x;
		}
		
		protected int getY()
		{
			return this.y;
		}
	}
	
	//the type of asteroid
	private final Type type;
	
	/**
	 * The maximum velocity
	 */
	private static final double VELOCITY_MAX = 5;
	
	/**
	 * The maximum rotation speed
	 */
	private static final float ROTATE_SPEED_MAX = 9.0f;
	
	protected Asteroid(final Type type, final int[] xpoints, final int[] ypoints) 
	{
		super(xpoints, ypoints);
		
		//store the type of android
		this.type = type;
		
		//create animation
		Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Asteroids), type.getX(), type.getY(), type.getWidth(), type.getHeight());
		
		//add to the sprite sheet
		super.getSpritesheet().add(type, animation);
		
		//setup dimensions
		super.setWidth(type.getWidth());
		super.setHeight(type.getHeight());
		
		//calculate a random velocity
		final double dx = (GamePanel.RANDOM.nextFloat() * VELOCITY_MAX * 2) - VELOCITY_MAX;
		final double dy = (GamePanel.RANDOM.nextFloat() * VELOCITY_MAX * 2) - VELOCITY_MAX;
		
		//start moving in a random direction
		super.setDX(dx);
		super.setDY(dy);
		
		//pick a random rotation speed
		setRotationSpeed((GamePanel.RANDOM.nextFloat() * ROTATE_SPEED_MAX * 2) - ROTATE_SPEED_MAX);
	}
	
	/**
	 * Get the type
	 * @return The type of asteroid
	 */
	public Type getType()
	{
		return this.type;
	}
	
	@Override
	public void update() throws Exception 
	{
		//the asteroid will always be spinning
		super.setRotation(getRotation() + getRotationSpeed());
		
		//update the location
		updateLocation();
	}
}