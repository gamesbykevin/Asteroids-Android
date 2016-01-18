package com.gamesbykevin.asteroids.player;

import com.gamesbykevin.androidframework.resources.Disposable;

import android.graphics.Canvas;

public interface IPlayer extends Disposable
{
	/**
	 * Assign the player's name
	 * @param name The name for the player
	 */
	public void setName(final String name);
	
	/**
	 * Get the player's name
	 * @return The assigned name to the player
	 */
	public String getName();
	
	/**
	 * Update the text description that is rendered to the user
	 */
	public void updateDescription();
	
	/**
	 * Get the text description of the player's statistics
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Set the players lives
	 * @param lives The desired number of lives for the player
	 */
	public void setLives(final int lives);
	
	/**
	 * Get the players lives
	 * @return The total number of lives remaining
	 */
	public int getLives();
	
	/**
	 * Set the players score
	 * @param score The desired players total point score
	 */
	public void setScore(final int score);
	
	/**
	 * Get the players score
	 * @return The players total point score
	 */
	public int getScore();
	
	/**
	 * Add to the players current score
	 * @param score The point score to add to the existing total
	 */
	public void addScore(final int score);
	
	/**
	 * General logic to update
	 */
	public void update() throws Exception;
	
	/**
	 * Render the player's statistics
	 * @param canvas Object used to render pixel data
	 */
	public void render(final Canvas canvas) throws Exception;
}