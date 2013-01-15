package GameEngine;

import java.awt.event.*;

/**
 * Implements several control functions for mouse and keyboard input.
 * 
 * @author Richard Baxter
 * @author Justin Crause
 *
 */
class GameInterface implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener 
{
	protected Game game;
	
	//==============================================================================
	
	/**
	 * Constructor
	 *
	 * @param g Store a reference to the game
	 */
	public GameInterface(Game g)
	{
		game = g;
	}
	
	//==============================================================================
	
	public void keyPressed(KeyEvent e)
	{
		game.registerKeyDown(e.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent e)
	{
		game.registerKeyDown(e.getKeyCode(), false);
	}
	public void keyTyped(KeyEvent e)
	{
		game.registerKeyTyped(e.getKeyCode());
	}
	
	//==============================================================================
	
	/**
	 * The mouse has entered the game window
	 */
	public void mouseEntered(MouseEvent e)
	{
		game.mouseInWindow = false;
	}
	/**
	 * The mouse has exited the game window
	 */
	public void mouseExited(MouseEvent e)
	{
		game.mouseInWindow = true;
	}
	/**
	 * The mouse button is pressed and held down
	 */
	public void mousePressed(MouseEvent e)
	{
		game.updateMousePos(e.getX(), e.getY());
		
		game.registerMouseDown(e.getButton(), true);
	}
	/**
	 * The mouse button is released and let go
	 */
	public void mouseReleased(MouseEvent e)
	{
		game.updateMousePos(e.getX(), e.getY());
		
		game.registerMouseDown(e.getButton(), false);
	}
	/**
	 * The mouse button is clicked (pressed down and released)
	 */
	public void mouseClicked(MouseEvent e)
	{
		game.registerMouseClicked(e.getButton());
	}
	
	//==============================================================================
	
	/**
	 * The mouse is dragged
	 */
	public void mouseDragged(MouseEvent e)
	{
		game.updateMousePos(e.getX(), e.getY());
		game.registerMouseDragged();
	}
	/**
	 * The mouse is movied (not pressed)
	 */
	public void mouseMoved(MouseEvent e)
	{
		game.updateMousePos(e.getX(), e.getY());
		game.registerMouseMoved();
	}
	
	//==============================================================================
	
	/**
	 * The mouse wheel has registered ticks
	 */
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		game.registerScrollAmount(e.getScrollAmount());
		game.registerScrollType(e.getScrollType());
		game.registerUnitsToScroll(e.getUnitsToScroll());
		game.registerWheelRotation(e.getWheelRotation());
	}
}
