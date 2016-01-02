package com.gamesbykevin.asteroids.entity.asteroid;

import java.util.ArrayList;

import com.gamesbykevin.asteroids.entity.ship.Ship;
import com.gamesbykevin.asteroids.game.Game;
import com.gamesbykevin.asteroids.panel.GamePanel;
import com.gamesbykevin.asteroids.screen.ScreenManager.State;

import android.graphics.Canvas;

public class Asteroids implements IAsteroids
{
	//list of asteroids in play
	private ArrayList<Asteroid> asteroids;

	//the coordinates for each asteroid, used for collision detection
    private static final int[] XPOINTS_BIG1 = new int[] {-42, -9, 42, 42, 2, -32, -28};
    private static final int[] YPOINTS_BIG1 = new int[] {-20, -50, -33, 25, 51, 36, 11};
    private static final int[] XPOINTS_BIG2 = new int[] {-49,-27,4,41,49,30,-18,-32};
    private static final int[] YPOINTS_BIG2 = new int[] {-26,-53,-60,-40,8,60,46,-9};
    private static final int[] XPOINTS_BIG3 = new int[] {-34, -15, 19, 41, 30, 0, -41};
    private static final int[] YPOINTS_BIG3 = new int[] {-28, -41, -44, -9, 30, 45, 22};
    private static final int[] XPOINTS_BIG4 = new int[] {-48, -40, 34, 48, 12, -42};
    private static final int[] YPOINTS_BIG4 = new int[] {-19, -49, -35, 17, 49, 31};
    private static final int[] XPOINTS_MED1 = new int[] {-21, -14, 5, 21, 20, -6};
    private static final int[] YPOINTS_MED1 = new int[] {1, -14, -21, -9, 18, 22};
    private static final int[] XPOINTS_MED2 = new int[] {-17, -4, 15, 19, 3, -20};
    private static final int[] YPOINTS_MED2 = new int[] {-8, -22, -15, 11, 23, 8};
    private static final int[] XPOINTS_SMALL1 = new int[] {-9, 3, 12, 13, -4, -14};
    private static final int[] YPOINTS_SMALL1 = new int[] {-9, -14, -7, 10, 14, 0};
    private static final int[] XPOINTS_SMALL2 = new int[] {-11, -2, 10, 12, 2, -11};
    private static final int[] YPOINTS_SMALL2 = new int[] {-5, -14, -9, 7, 15, 6};
    private static final int[] XPOINTS_TINY1 = new int[] {-7, 1, 9, 5, -7};
    private static final int[] YPOINTS_TINY1 = new int[] {-3, -9, -1, 9, 6};
    private static final int[] XPOINTS_TINY2 = new int[] {-7, 1, 8, 3};
    private static final int[] YPOINTS_TINY2 = new int[] {8, -8, 0, 8};
    
    //our game reference
    private final Game game;
    
	public Asteroids(final Game game)
	{
		//store our game reference
		this.game = game;
		
		//create new list for the asteroids
		this.asteroids = new ArrayList<Asteroid>();
	}
	
	@Override
	public void add(final int x, final int y, final Asteroid.Type type) throws Exception
	{
		final Asteroid asteroid;
		
		//create asteroid of type
		switch (type)
		{
			case BrownBig1:
			case GreyBig1:
				asteroid = new Asteroid(type, XPOINTS_BIG1, YPOINTS_BIG1);
				break;
				
			case BrownBig2:
			case GreyBig2:
				asteroid = new Asteroid(type, XPOINTS_BIG2, YPOINTS_BIG2);
				break;
				
			case BrownBig3:
			case GreyBig3:
				asteroid = new Asteroid(type, XPOINTS_BIG3, YPOINTS_BIG3);
				break;
				
			case BrownBig4:
			case GreyBig4:
				asteroid = new Asteroid(type, XPOINTS_BIG4, YPOINTS_BIG4);
				break;
				
			case BrownMed1:
			case GreyMed1:
				asteroid = new Asteroid(type, XPOINTS_MED1, YPOINTS_MED1);
				break;
				
			case BrownMed2:
			case GreyMed2:
				asteroid = new Asteroid(type, XPOINTS_MED2, YPOINTS_MED2);
				break;
				
			case BrownSmall1:
			case GreySmall1:
				asteroid = new Asteroid(type, XPOINTS_SMALL1, YPOINTS_SMALL1);
				break;
				
			case BrownSmall2:
			case GreySmall2:
				asteroid = new Asteroid(type, XPOINTS_SMALL2, YPOINTS_SMALL2);
				break;
				
			case BrownTiny1:
			case GreyTiny1:
				asteroid = new Asteroid(type, XPOINTS_TINY1, YPOINTS_TINY1);
				break;
				
			case BrownTiny2:
			case GreyTiny2:
				asteroid = new Asteroid(type, XPOINTS_TINY2, YPOINTS_TINY2);
				break;
				
			default:
				throw new Exception("Type not setup here " + type.toString());
		}
		
		//set the location
		asteroid.setX(x);
		asteroid.setY(y);
		
		//add to list
		get().add(asteroid);
	}
	
	@Override
	public void spawnChildren(Asteroid asteroid) throws Exception 
	{
		final Asteroid.Type child;
		
		switch (asteroid.getType())
		{
			case BrownBig1:
			case BrownBig2:
			case BrownBig3:
			case BrownBig4:
				child = (GamePanel.RANDOM.nextBoolean() ? Asteroid.Type.BrownMed1 : Asteroid.Type.BrownMed2);
				break;
				
			case GreyBig1:
			case GreyBig2:
			case GreyBig3:
			case GreyBig4:
				child = (GamePanel.RANDOM.nextBoolean() ? Asteroid.Type.GreyMed1 : Asteroid.Type.GreyMed2);
				break;
				
			case BrownMed1:
			case BrownMed2:
				child = (GamePanel.RANDOM.nextBoolean() ? Asteroid.Type.BrownSmall1 : Asteroid.Type.BrownSmall2);
				break;
				
			case GreyMed1:
			case GreyMed2:
				child = (GamePanel.RANDOM.nextBoolean() ? Asteroid.Type.GreySmall1 : Asteroid.Type.GreySmall2);
				break;
				
				
			case BrownSmall1:
			case BrownSmall2:
				child = (GamePanel.RANDOM.nextBoolean() ? Asteroid.Type.BrownTiny1 : Asteroid.Type.BrownTiny2);
				break;
				
			case GreySmall1:
			case GreySmall2:
				child = (GamePanel.RANDOM.nextBoolean() ? Asteroid.Type.GreyTiny1 : Asteroid.Type.GreyTiny2);
				break;
				
			case BrownTiny1:
			case BrownTiny2:
			case GreyTiny1:
			case GreyTiny2:
				child = null;
				break;
		
			default:
				throw new Exception("Type not setup here: " + asteroid.getType().toString());
		}
		
		//if there is a child add it
		if (child != null)
		{
			//place child in the middle
			int x = (int)(asteroid.getX() + (asteroid.getWidth() / 2) - (child.getWidth() / 2));
			int y = (int)(asteroid.getY() + (asteroid.getHeight() / 2) - (child.getHeight() / 2));
			
			//add 2 children
			add(x, y, child);
			add(x, y, child);
		}
	}
	
	@Override
	public ArrayList<Asteroid> get() 
	{
		return this.asteroids;
	}
	
	@Override
	public void update() throws Exception 
	{
		if (get() == null)
			return;
		
		for (int index = 0; index < get().size(); index++)
		{
			//get the current asteroid
			Asteroid asteroid = get().get(index);
			
			//if it is dead, remove it
			if (asteroid.isDead())
			{
				//remove from our list
				get().remove(index);
				
				//move the index back
				index--;
			}
			else
			{
				asteroid.update();
			}
		}
	}

	@Override
	public void render(final Canvas canvas) throws Exception 
	{
		if (get() == null)
			return;
		
		for (Asteroid asteroid : get())
		{
			if (asteroid != null)
				asteroid.render(canvas);
		}
	}

	@Override
	public void dispose() 
	{
		if (asteroids != null)
		{
			for (int index = 0; index < asteroids.size(); index++)
			{
				if (asteroids.get(index) != null)
				{
					asteroids.get(index).dispose();
					asteroids.set(index, null);
				}
			}
			
			asteroids.clear();
			asteroids = null;
		}
	}
}