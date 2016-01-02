package com.gamesbykevin.asteroids.game.controller;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Each controller needs to have these methods
 * @author GOD
 */
public interface IController extends Disposable
{
    /**
     * Update logic when motion event occurs
     * @param action The action of the MotionEvent
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if a change occurred, false otherwise
     * @throws Exception
     * 
     */
    public boolean update(final int action, final float x, final float y) throws Exception;
    
    /**
     * Logic to update at runtime, separate from the motion event
     * @throws Exception
     */
    public void update() throws Exception;
    
    /**
     * Render our controller
     * @param canvas Object to write pixels to
     * @throws Exception
     */
    public void render(final Canvas canvas) throws Exception;
    
    /**
     * Reset the controller
     */
    public void reset();
}