package GameEngine;

import javax.media.opengl.awt.*;

/**
 * This class is like the normal Canvas except is has Game attached to it and will run and render that game in it's drawing area.
 * 
 * @author Richard Baxter
 * @author Justin Crause
 *
 */
public class GameCanvas extends GLCanvas
{
	private static final long serialVersionUID = 1L;
	
	//==============================================================================
	
	/**
	 * Standard Constructor
	 * 
	 * @param g The Game that is to be run in this Canvas
	 */
	public GameCanvas(Game g)
	{
		GameRenderer renderer = new GameRenderer(g);
		addGLEventListener(renderer);
		
		GameInterface gameInterface = new GameInterface(g);
		addKeyListener(gameInterface);
		addMouseListener(gameInterface);
		addMouseMotionListener(gameInterface);
		addMouseWheelListener(gameInterface);
		g.startGame(this);
	}
}
