package com.gamesbykevin.asteroids.entity.effect;

import java.util.ArrayList;

import com.gamesbykevin.asteroids.entity.Entity;

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
		get().add(new Explosion(entity));
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