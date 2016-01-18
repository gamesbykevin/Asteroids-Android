package com.gamesbykevin.asteroids.entity.effect;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.asteroids.assets.Assets;
import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.panel.GamePanel;

import android.graphics.Canvas;

public class Effects implements IEffects
{
	//list of effects
	private ArrayList<Effect> effects;
	
	public Effects() 
	{
		//create new list of effects
		this.effects = new ArrayList<Effect>();
	}

	@Override
	public ArrayList<Effect> get() 
	{
		return this.effects;
	}
	

	@Override
	public void add(Entity entity) 
	{
		//add to the list
		get().add(new Explosion(entity));
		
		//play sound effect
		switch (GamePanel.RANDOM.nextInt(3))
		{
			case 0:
			default:
				Audio.play(Assets.AudioGameKey.Explosion1);
				break;
				
			case 1:
				Audio.play(Assets.AudioGameKey.Explosion2);
				break;
				
			case 2:
				Audio.play(Assets.AudioGameKey.Explosion3);
				break;
		}
	}
	
	@Override
	public void update() throws Exception 
	{
		for (int index = 0; index < effects.size(); index++)
		{
			//get the current effect
			final Effect effect = get().get(index);
			
			if (!effect.isDead())
			{
				effect.update();
			}
			else
			{
				//remove the current effect
				effects.remove(index);
				
				//move the index back
				index--;
			}
		}
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		//render all the effects
		for (Effect effect : get())
		{
			effect.render(canvas);
		}
	}
	
	@Override
	public void dispose() 
	{
		if (effects != null)
		{
			for (int index = 0; index < effects.size(); index++)
			{
				if (effects.get(index) != null)
				{
					effects.get(index).dispose();
					effects.set(index, null);
				}
			}
			
			effects.clear();
			effects = null;
		}
	}
}