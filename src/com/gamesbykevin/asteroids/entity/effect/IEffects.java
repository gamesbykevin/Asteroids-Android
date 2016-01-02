package com.gamesbykevin.asteroids.entity.effect;

import java.util.ArrayList;

import com.gamesbykevin.asteroids.common.ICommon;
import com.gamesbykevin.asteroids.entity.Entity;

public interface IEffects extends ICommon
{
	/**
	 * Get the list
	 * @return The list of current effects
	 */
	public ArrayList<Effect> get();
	
	/**
	 * Add an effect
	 * @param entity The entity were we want to add the entity
	 */
	public void add(final Entity entity);
}
