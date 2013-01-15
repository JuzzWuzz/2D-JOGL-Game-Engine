package GameEngine;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.awt.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * The game render class that provides the various rendering options
 * <p>
 * Font classes are defined in here, a class to store the information about a font
 * as well as a stack object that is used to hold a collection of FontInfo items.
 * <p>
 * A renderer for text is provided which creates the text items to be rendered and
 * later renders them.
 * This is called by the main game renderer
 * <p>
 * The game renderer is responsible for handing the OpenGL calls. Can clear the
 * screen and then render the game and lastly draw the text.
 *
 * @author Richard Baxter
 * @author Justin Crause
 */
class GameRenderer implements GLEventListener
{
	/**
	 * Class that stores the information about a particular text item
	 */
	class FontInfo
	{
		String text;
		float x;
		float y;
		float r;
		float g;
		float b;
		float a;
		float depth;
		float scale;
		
		//==============================================================================
		
		/**
		 * Constructor for the font info class
		 *
		 * @param text Text The text to be displayed
		 * @param x The x-coordinate of the bottom left point to which to draw the text
		 * @param y The y-coordinate of the bottom left point to which to draw the text
		 * @param r Value of the red channel (between 0.0 and 1.0)
		 * @param g Value of the green channel (between 0.0 and 1.0)
		 * @param b Value of the blue channel (between 0.0 and 1.0)
		 * @param a Value of the alpha channel (between 0.0 and 1.0)
		 * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
		 * @param scale What percentage scaling this text should be rendered at (1.0 means the same size, 2.0 means double size etc)
		 */
		FontInfo (String text, float x, float y, float r, float g, float b, float a, float depth, float scale)
		{
			this.text = text;
			this.x = x;
			this.y = y;
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			this.depth = depth;
			this.scale = scale;
		}
	}
	
	//==============================================================================
	//==============================================================================
	
	/**
	 * Class for storing a stack of FontInfo items
	 */
	class FontStack
	{
		Stack<FontInfo> stack;
		GameFont gFont;
		TextRenderer tr;
		
		//==============================================================================
		
		/**
		 * Constructor for the font stack class
		 *
		 * @param gf The GameFont to use (represents an actual system font)
		 */
		FontStack (GameFont gf)
		{
			stack = new Stack<FontInfo>();
			tr = new TextRenderer(gf.font);
			gFont = gf;
		}
	}
	
	//==============================================================================
	//==============================================================================
	
	/**
	 * The renderer that draws the text items
	 */
	class FontRenderer
	{	 
		Vector<FontStack> textRenderers = new Vector<FontStack>();
		
		//==============================================================================
		
		/**
		 * Add the specific font to the vector of renderers
		 *
		 * @param f The new Font to add
		 */
		GameFont addGameFont(Font f)
		{
			GameFont gf = new GameFont(f, textRenderers.size());
			textRenderers.add(new FontStack(gf));
			return gf;
		}
		
		/**
		 * This adds the specific string with parameters to the renderer for later use
		 *
		 * @param gf The GameFont we want to render the text in
		 * @param text Text The text to be displayed
		 * @param x The x-coordinate of the bottom left point to which to draw the text
		 * @param y The y-coordinate of the bottom left point to which to draw the text
		 * @param r Value of the red channel (between 0.0 and 1.0)
		 * @param g Value of the green channel (between 0.0 and 1.0)
		 * @param b Value of the blue channel (between 0.0 and 1.0)
		 * @param a Value of the alpha channel (between 0.0 and 1.0)
		 * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
		 * @param scale What percentage scaling this text should be rendered at (1.0 means the same size, 2.0 means double size etc)
		 */
		void registerFontRender(GameFont gf, String text, float x, float y, float r, float g, float b, float a, float depth, float scale)
		{
			textRenderers.elementAt(gf.index).stack.add(new FontInfo(text, x, y, r, g, b, a, depth, scale));
		}
		
		/**
		 * Get the bounds that the text fits in based on the given font
		 *
		 * @param s Text string to test
		 * @param gf The GameFont that we are using to get size infor on for the text
		 */
		Rectangle2D.Float getBounds(String s, GameFont gf)
		{
			return (Rectangle2D.Float)textRenderers.elementAt(gf.index).tr.getBounds(s);
		}
			
		/* need to fix this up, i made the whole stack system so i would reduce the number of calls to 
		* fs.tr.begin3DRendering(); and fs.tr.end3DRendering();, however I've discovered that you can't 
		* change colour more then once inside of those two calls so now they are being called as many times as if I 
		* hadn't done the whole stack system... fail
		*/
		void processFontDraws (GL gl)
		{
			for (FontStack fs : textRenderers)
			{
				while(!fs.stack.isEmpty())
				{
					FontInfo fi = fs.stack.pop();

					fs.tr.begin3DRendering();
					gl.getGL2().glColor4f(fi.r, fi.g, fi.b, fi.a);
					fs.tr.draw3D(fi.text, fi.x, fi.y, fi.depth, fi.scale);
					fs.tr.end3DRendering();
				}
			}
		}
	}
	
	//==============================================================================
	//==============================================================================
	
	Game game;
	FontRenderer f;
	Dimension viewPortDimension = new Dimension(1,1);
	
	//==============================================================================
	
	/**
	 * Game renderer constructor
	 *
	 * @param g A reference for the Game object
	 */
	public GameRenderer (Game g)
	{
		game = g;
		
		f = new FontRenderer();
		g.fr = f; // game.FontRenderer = f;
	}
	
	//==============================================================================
	
	/**
	 * The display method that clears the screen and calls the game render method
	 */
	public void display(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		game.renderGame(gl);
		
		f.processFontDraws (gl);
	}
	
	//==============================================================================
	
	/**
	 * This calls the games logic method
	 */
	public void callLogic()
	{
		game.logicGame();
	}
	
	//==============================================================================
	
	/**
	 * Stub to handle things when the display is changed?????
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
	}
	
	//==============================================================================
	
	/**
	 * Initialise the game renderer class
	 */
	public void init(GLAutoDrawable drawable)
	{
		System.out.println("GameRenderer.init() called");
		GL gl = drawable.getGL(); 
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		game.initGame(gl, f);
	}
	
	//==============================================================================
	
	/**
	 * Reshape the drawing window (viewport)
	 *
	 * @param x The new X location
	 * @param y The new Y location
	 * @param width The new window width
	 * @param height the new window height
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		System.out.println("GameRenderer.reshape("+x+", "+y+", "+width+", "+height+") called");
		game.setViewPortDimension(new Dimension(width, height));
		GL gl = drawable.getGL(); 
		GLU glu = new GLU(); 
		
		gl.getGL2().glViewport( 0, 0, width, height ); 
		gl.getGL2().glMatrixMode( GL2.GL_PROJECTION );
		gl.getGL2().glLoadIdentity();
		glu.gluOrtho2D( 0.0, width, 0.0, height);
		gl.getGL2().glMatrixMode(GL2.GL_MODELVIEW);
		gl.getGL2().glLoadIdentity();
	}
	
	//==============================================================================
	
	/**
	 * Dispose method than cleans up when destroyed
	 */
	public void dispose(GLAutoDrawable drawable)
	{
	}
}
