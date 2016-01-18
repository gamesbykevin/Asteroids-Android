package com.gamesbykevin.asteroids.entity.ship;

import com.gamesbykevin.asteroids.entity.Entity;
import com.gamesbykevin.asteroids.entity.asteroid.Asteroid;
import com.gamesbykevin.asteroids.game.Game;
import com.gamesbykevin.asteroids.screen.OptionsScreen;

public class Cpu extends Ship 
{
	public enum Action
	{
		//avoid the target object
		Avoid,
		
		//approach the target object
		Approach,
		
		//attack the target object
		Attack
	}
	
	//the target rotation
	private float rotation;
	
	//the entity we are targeting
	private Entity target;
	
	//the action we want to take
	private Action action;
	
	//our game reference
	private final Game game;
	
	/**
	 * If the distance from the entity is at least this value, we will want to approach the entity
	 */
	private static final double DISTANCE_APPROACH = HEIGHT * 5;
	
	/**
	 * If the distance from the entity is this value or less, we will want to avoid the entity
	 */
	private static final double DISTANCE_AVOID = HEIGHT * 2.5;
	
	/**
	 * Rotation of half a circle
	 */
	private static final double HALF_CIRCLE = 180.0;
	
	public Cpu(final Type type, final Game game) 
	{
		super(type);
		
		//store our game reference
		this.game = game;
	}

	@Override
	public void update() throws Exception
	{
		//update common
		super.update();
		
		//if we don't have a target and we don't have an action, lets decide what to do
		if (getAction() == null && getTarget() == null)
		{
			//store this ships original coordinates
			final double x = getX();
			final double y = getY();
			
			//move coordinate to the center to calculate the distance
			setX(x + (getWidth() / 2));
			setY(y + (getHeight() / 2));
			
			//keep track of the distance
			double distance = 0;
			
			//determine who our target is
			switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
			{
				//classic and coop
				case 0:
				case 1:
				default:
					//check all the asteroids for the closest one
					for (Asteroid asteroid : game.getAsteroids().get())
					{
						//store the asteroids original coordinates
						final double ax = asteroid.getX();
						final double ay = asteroid.getY();
						
						//move coordinates to the center
						asteroid.setX(ax + (asteroid.getWidth() / 2));
						asteroid.setY(ay + (asteroid.getHeight() / 2));
						
						//if we have not picked an entity yet or if the distance is shorter
						if (getTarget() == null || asteroid.getDistance(this) < distance)
						{
							//store the new target
							setTarget(asteroid);
							
							//store the new distance
							distance = getTarget().getDistance(this);
						}
						
						//restore coordinates
						asteroid.setX(ax);
						asteroid.setY(ay);
					}
					
					//restore coordinates
					setX(x);
					setY(y);
					break;
					
				//versus
				case 2:
					
					//we can only target the other ship if it is not dead
					if (!game.getHuman().getShip().isDead())
					{
						//target the human ship
						setTarget(game.getHuman().getShip());
						
						//calculate the distance
						distance = getTarget().getDistance(this);
					}
					break;
			}

			//restore coordinates
			setX(x);
			setY(y);
			
			/**
			 * If an entity was found, we need to determine what to do
			 * 1) If the entity is far away, we will approach
			 * 2) If the entity is too close, we will avoid
			 * 3) If the entity is just the right distance, we will attack
			 */
			if (getTarget() != null)
			{
				if (distance >= DISTANCE_APPROACH)
				{
					//here we will approach the entity
					setAction(Action.Approach);
				}
				else if (distance < DISTANCE_APPROACH && distance > DISTANCE_AVOID)
				{
					//here we will attack the entity
					setAction(Action.Attack);
				}
				else if (distance <= DISTANCE_AVOID)
				{
					//here we will avoid the entity
					setAction(Action.Avoid);
				}
				else
				{
					//this shouldn't happen
					throw new Exception("Don't know what to do here: " + distance);
				}
				
				//determine where to turn to
				calculateRotation();
			}
			else
			{
				//if no target was found then there are no more asteroids left
			}
		}
		
		//if we have a target and action
		if (getTarget() != null && getAction() != null)
		{
			//stop thrust for the moment
			setThrust(false);
			
			//make sure we are facing our target rotation
			if (getRotation() != getTargetRotation())
			{
				//find our how close we are
				float diff = (getRotation() > getTargetRotation() ? getRotation() - getTargetRotation(): getTargetRotation() - getRotation());
				
				//if we are less than our rotation speed
				if (diff <= ROTATION_SPEED)
				{
					//assign to match
					setRotation(getTargetRotation());
					
					//stop rotating
					rotateReset();
				}
				else
				{
					//determine the optimal turn direction
					if (getRotation() > getTargetRotation())
					{
						//if the difference is greater than half a circle 180, turn the other way
						if (getRotation() - getTargetRotation() > HALF_CIRCLE)
						{
							rotateRight();
						}
						else
						{
							rotateLeft();
						}
					}
					else
					{
						//if the difference is greater than half a circle 180, turn the other way
						if (getTargetRotation() - getRotation() > HALF_CIRCLE)
						{
							rotateLeft();
						}
						else
						{
							rotateRight();
						}
					}
				}
			}
			else
			{
				//we are at our rotation, lets execute the action
				switch (getAction())
				{
					case Approach:
						setThrust(true);
						break;
						
					case Attack:
						game.getLasers().add(this);
						break;
						
					case Avoid:
						setThrust(true);
						break;
						
					//this should not happen
					default:
						throw new Exception("Action not setup here: " + getAction());
				}
				
				//now remove the target and action
				setAction(null);
				setTarget(null);
			}
		}
	}
	
	/**
	 * Here we calculate the rotation we want to face
	 */
	private void calculateRotation()
	{
		//calculate facing angle (in radian's)
		double angle = Math.atan( (getTarget().getY() - getY()) / (getTarget().getX() - getX()) );
		
		//adjust if the target is east of the ship, to make sure we make the optimal turn
		if (getTarget().getX() - getX() < 0)
            angle += Math.PI;		
		
		//if we are avoiding the entity, we want to face the other way
		if (getAction() == Action.Avoid)
			angle += Math.toRadians(180);
		
		//make sure the angle stays within range
		if (angle > (2 * Math.PI))
            angle -= (2 * Math.PI);
        if (angle < 0)
            angle += (2 * Math.PI);
        
        //set our target rotation destination
        this.setTargetRotation((float)Math.toDegrees(angle));
	}
	
	/**
	 * Set the target rotation we want for the cpu
	 * @param rotation The rotation in degrees
	 */
	protected void setTargetRotation(final float rotation)
	{
		this.rotation = rotation;
	}
	
	/**
	 * Get the target rotation
	 * @return The target rotation in degrees
	 */
	protected float getTargetRotation()
	{
		return this.rotation;
	}
	
	/**
	 * Set the target
	 * @param target The entity we are avoiding or attacking
	 */
	protected void setTarget(final Entity target)
	{
		this.target = target;
	}
	
	/**
	 * Get the target
	 * @return The entity we are avoiding or attacking
	 */
	protected Entity getTarget()
	{
		return this.target;
	}
	
	/**
	 * Set the current action
	 * @param action The action for this ship (avoid, attack, etc...)
	 */
	protected void setAction(final Action action)
	{
		this.action = action;
	}
	
	/**
	 * Get the current action
	 * @return The action for this ship (avoid, attack, etc...)
	 */
	protected Action getAction()
	{
		return this.action;
	}
}