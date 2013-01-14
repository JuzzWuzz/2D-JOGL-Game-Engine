

import java.awt.geom.Point2D;

import GameEngine.GameObject;

class PlayerObject extends GameObject
{
    float direction;
    
    Point2D.Float oldPosition;
    
	//==================================================================================================
    public PlayerObject (float x, float y)
    {
        super (x, y);
        oldPosition = getPosition();
    }
    
    //==================================================================================================
    
    public void setDirection(float direction)
    {
        while (direction < 0.0) direction += 360.0;
        while (direction >= 360.0) direction -= 360.0;
        
        this.direction = direction;
        
        // Set the rotation to be the same as the direction value
        rotation = direction;
    }
    
    public void revertPosition ()
    {
    	this.setPosition(oldPosition);
    }
    
    public float getDirection()
    {
		return direction;
	}


	public void moveInDirection(float direction)
	{
		oldPosition = this.getPosition();
		incrementPosition((float)Math.sin(Math.toRadians(direction))*2, -(float)Math.cos(Math.toRadians(direction))*2);
		setDirection(direction);
	}
}
