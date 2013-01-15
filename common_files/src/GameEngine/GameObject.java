package GameEngine;

import java.util.Vector;
import java.awt.*;
import java.awt.geom.*;
import javax.media.opengl.*;
import com.jogamp.opengl.util.texture.*;

/**
 * This is the base Object for anything in your game. Backgrounds, units, bullets, enemies 
 * all should be instances of GameObject.
 * <p>
 * To specify a GameObject you need a position and a texture. The texture will determine the
 * game object's width and height. The GameObject cannot be scaled. If you want a larger
 * or smaller image you need to edit it manually (using GIMP or some other editor).
 * <p>
 * You may load multiple images in order to do animation. You may also load a large e image
 * and specify a sub-image to render.
 * Each texture will have a specified center relative to the bottom left corner of the
 * texture. When rendering, the center of the image will be drawn at this GameObject's
 * position and the rest of the image drawn around it.
 * <p>
 * You can specify a sprite sheet for the object (only 1) which will be used then instead
 * of the added textures so make sure you know what you are going to need. The sprite sheet
 * can be animated over the entire row of images or over the entire sprite sheet.
 * There are controls for directly selecting a specific row & column. All animations will
 * loop.
 * <p>
 * You can rotate your sprites by a specific angle, this will however change the size of the
 * bounding box as a square rotated 45 degrees is larger in terms of its axis-aligned
 * bounding box. Please take this into account when doing your collision detection!
 * <p>
 * Objects can be marked as collidable (enabled for collision detection) or not for use as
 * background items or such.
 * <p>
 * 
 * @author Richard Baxter
 * @author Justin Crause
 *	
 */
public class GameObject
{
	protected Point2D.Float position; // In pixels
	protected float rotation = 0.0f; // In Degrees
	//protected float depth = 0;
	
	private boolean markedForDestruction = false;
	
	//TODO combine texture and center into a nested class
	private Vector<GameTexture> textures = new Vector<GameTexture>();
	private Vector<Point2D.Float> centers = new Vector<Point2D.Float>();
	int activeTexture = -1;
	
	TextureCoords tc;
	
	private Point2D.Float imgDim = new Point2D.Float(0.0f, 0.0f);
	
	// Sprite sheet settings
	private boolean useSpriteSheet = false;
	private GameTexture spriteSheet;
	private int subImageW;
	private int subImageH;
	private int subImageCX;
	private int subImageCY;
	private int spriteSheetCols;
	private int spriteSheetRows;
	private int spriteSheetCurCol;
	private int spriteSheetCurRow;
	
	// Animation controls (Need sprite-sheet!!)
	private boolean animate = false;
	private boolean animateAll = true;
	private int timerCur = 0;
	private int timerMax = 10;
	
	protected boolean collidable = true;
	
	//==============================================================================
	
	/**
	 * Basic Constructor for the GameObject
	 *
	 * @param x Position along the x coordinate
	 * @param y Position along the y coordinate
	 */
	public GameObject(float x, float y)
	{
		position = new Point2D.Float(x, y);
	}
	
	public void finalize()
	{
		while (!textures.isEmpty())
			removeTexture(0);
	}
	
	//==============================================================================
	
	/**
	 * Returns the angle between this GameObject and another
	 *
	 * @param o The GameObject to get the angle from
	 * @return Then angle in degrees
	 */
	public float getDegreesTo(GameObject o)
	{
		return(getDegreesTo(o.position));
	}
	
	/**
	 * Returns the angle between this GameObject and another
	 *
	 * @param o The GameObject to get the angle from
	 * @return Then angle in radians
	 */
	public float getRadiansTo(GameObject o)
	{
		return(getRadiansTo(o.position));
	}
	
	/**
	 * Returns the angle between this GameObject and a point
	 *
	 * @param o The point to get the angle from
	 * @return Then angle in degrees
	 */
	public float getDegreesTo(Point2D.Float o)
	{
		return((float)Math.toDegrees(getRadiansTo(o)));
	}
	
	/**
	 * Returns the angle between this GameObject and a point
	 *
	 * @param o The point to get the angle from
	 * @return Then angle in radians
	 */
	public float getRadiansTo(Point2D.Float o)
	{
		return((float)Math.atan2((o.y - position.y),(o.x - position.x)));
	}
	
	//==============================================================================
	
	/**
	 * Sets the position of the object. Objects will be drawn with their center at this position
	 *
	 * @param p Position of the object
	 */
	public void setPosition(Point2D.Float p)
	{
		position.x = p.x;
		position.y = p.y;
	}
	
	/**
	 * Sets the position of the object. Objects will be drawn with their center at this position
	 *
	 * @param x x position of the object
	 * @param y y position of the object
	 */
	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}
	
	/**
	 * Sets the position of the object. Objects will be drawn with their center at this position
	 *
	 * @param p Position of the object
	 */
	public void incrementPosition(Point2D.Float p)
	{
		position.x += p.x;
		position.y += p.y;
	}
	
	/**
	 * Sets the position of the object. Objects will be drawn with their center at this position
	 *
	 * @param x x position of the object
	 * @param y y position of the object
	 */
	public void incrementPosition(float x, float y)
	{
		position.x += x;
		position.y += y;
	}
	
	/**
	 * Gets the position of this object
	 *
	 * @return Depth of this object
	 */
	public Point2D.Float getPosition()
	{
		return(new Point2D.Float(position.x, position.y));
	}
	
	//==============================================================================
	
	/**
	 * Sets the rotation of the object
	 * 
	 * @param angle The angle of rotation in degrees
	 */
	public void setRotation(float angle)
	{
		rotation = angle;
	}
	
	//==============================================================================
	
	/**
	 * Gets the Axis-Aligned Bounding Box of this object for use in collision detection/debugging
	 *
	 * @return Axis-Aligned Bounding Box
	 */
	public Rectangle2D.Float getAABoundingBox()
	{
		return(new Rectangle2D.Float(position.x - getCurrentCenter().x, position.y - getCurrentCenter().y, imgDim.x, imgDim.y));
	}
	
	/**
	 * Gets the Integer Value Axis-Aligned Bounding Box of this object for use in collision detection/debugging
	 *
	 * @return Integer Valued Axis-Aligned Bounding Box
	 */
	public Rectangle getIntAABoundingBox()
	{
		return(new Rectangle((int)(position.x - getCurrentCenter().x), (int)(position.y - getCurrentCenter().y), (int)imgDim.x, (int)imgDim.y));
	}
	
	//==============================================================================
	
	/**
	 * Get the center of the object (based on the texture / sprite sheet)
	 *
	 * @return Center of the object
	 */
	private Point2D.Float getCurrentCenter()
	{
		if (useSpriteSheet)
		{
			return(new Point2D.Float(subImageCX, subImageCY));
		}
		else
		{
			return(centers.elementAt(activeTexture));
		}
	}
	
	/**
	 * Get the objects active texture
	 *
	 * @return Objects active Texture
	 */
	public GameTexture getCurrentTexture()
	{
		if (useSpriteSheet)
			return(spriteSheet);
		else
			return(textures.elementAt(activeTexture));
	}
	
	//==============================================================================
	
	/**
	 * One can set a flag in this object to mark that it needs to be destroyed at the end of the game loop
	 *
	 * @param m Boolean value marking whether it should be destroyed (true, means is should be destroyed)
	 */
	public void setMarkedForDestruction(boolean m)
	{
		markedForDestruction = m;
	}
	
	/**
	 * Returns whether this unit has been marked for destruction (true, means is should be destroyed)
	 *
	 * @return Boolean value marking whether it should be destroyed
	 */
	public boolean isMarkedForDestruction()
	{
		return(markedForDestruction);
	}
	
	//==============================================================================
	
	/**
	 * Set the collidable state of the object
	 *
	 * @param enabled Make this item collidable
	 */
	 public void setCollidable(boolean enabled)
	 {
		collidable = enabled;
	 }
	 
	 /**
	 * Return the collidable state of the object
	 *
	 * @return Boolean value stating the collidable nature of the object
	 */
	 public boolean getCollidable()
	 {
		return(collidable);
	 }
	
	//==============================================================================
	
	/**
	 * Adds a texture to this object's list of textures. The corresponding Center position will be set to the center of the image as default
	 * 
	 * @param t The GameTexture containing the image
	 */
	public void addTexture(GameTexture t)
	{
		addTexture(t, (t.w + 1) / 2, (t.h + 1) / 2);
	}
	
	/**
	 * Adds a texture to this object's list of textures with specified center. 
	 * The center is relative to the bottom left hand corner of the image. 
	 * So (0,0) as center corresponds to the bottom left hand corner of the image.
	 * 
	 * @param t The GameTexture containing the image
	 * @param centerX The center of the image in the x direction relative to the image
	 * @param centerY The center of the image in the y direction relative to the image
	 */
	public void addTexture(GameTexture t, float centerX, float centerY)
	{
		textures.add(t);
		centers.add(new Point2D.Float(centerX, centerY));
		
		if (textures.size() == 1) // i.e. textures was empty before calling
			setActiveTexture(0);
	}
	
	/**
	 * Removes the texture at position i. All textures with index greater then i will be shifted down one position
	 * 
	 * @param index index of the texture to remove
	 */
	public void removeTexture(int index)
	{
		textures.remove(index);
		centers.remove(index);
		
		if (activeTexture == index)
			setActiveTexture(index);
	}
	
	/**
	 * Gets the number of textures this object currently holds
	 * 
	 * @return Number of textures
	 */
	public int getNumberOfTextures()
	{
		return(textures.size());
	}
	
	//==============================================================================
	
	/**
	* Sets which texture will be drawn during the render stage of the game loop
	* 
	* @param i The index of which texture should be set to as active
	*/
	public void setActiveTexture(int i)
	{
		if (textures.size() == 0)
		{
			activeTexture = -1;
			return;
		}
		activeTexture = i%textures.size();
		
		setTextureCoords();
	}
	
	/**
	 * Gets the index of the texture that will be drawn during the render stage of the game loop
	 * 
	 * @return The index of the active texture
	 */
	public int getActiveTexture()
	{
		return(activeTexture);
	}
	
	//==============================================================================
	
	/**
	 * Add sprite sheet
	 * 
	 * @param t The GameTexture containing the image
	 * @param subW The width of the subimage to use. Used to determine number of columns automatically
	 * @param subH The height of the subimage to use. Used to determine number of columns automatically
	 */
	public void addSpriteSheet(GameTexture t, int subW, int subH)
	{
		// We are now using a sprite sheet
		useSpriteSheet = true;
		
		// Set the sprite sheet
		spriteSheet = t;
		
		// Store the sizes of the 
		subImageW = subW;
		subImageH = subH;
		
		// Store the center pos
		subImageCX = subImageW / 2;
		subImageCY = subImageH / 2;
		
		// Calculate the rows and cols (-1 beccause
		spriteSheetCols = (spriteSheet.w / subImageW) - 1;
		spriteSheetRows = (spriteSheet.h / subImageH) - 1;
		
		// Setup tex coords
		setTextureCoords();
	}
	
	/**
	 * Set the column index in the sprite sheet to use
	 * 
	 * @param col The column index to change to in the sprite sheet
	 */
	public void setSpriteSheetCol(int col)
	{
		if (!useSpriteSheet)
			return;
		
		spriteSheetCurCol = col % spriteSheetCols;
		if (spriteSheetCurCol < 0)
			spriteSheetCurCol = 0;
		
		// Setup tex coords
		setTextureCoords();
	}
	/**
	 * Set the row index in the sprite sheet to use
	 * 
	 * @param row The row index to change to in the sprite sheet
	 */
	public void setSpriteSheetRow(int row)
	{
		if (!useSpriteSheet)
			return;
		
		spriteSheetCurRow = row % spriteSheetRows;
		if (spriteSheetCurRow < 0)
			spriteSheetCurRow = 0;
		
		// Setup tex coords
		setTextureCoords();
	}
	
	//==============================================================================
	
	/**
	 * Sets the texture coords
	 */
	private void setTextureCoords()
	{
		if (useSpriteSheet)
		{
			int x = spriteSheetCurCol * subImageW;
			int y = spriteSheet.h - ((spriteSheetCurRow + 1) * subImageH);
			tc = spriteSheet.t.getSubImageTexCoords(x, y, x + subImageW, y + subImageH);
			
			imgDim.x = subImageW;
			imgDim.y = subImageH;
		}
		else
		{
			tc = getCurrentTexture().t.getImageTexCoords();
			
			imgDim.x = getCurrentTexture().w;
			imgDim.y = getCurrentTexture().h;
		}
	}
	
	//==============================================================================
	
	void draw(GL gl, float offsetx, float offsety, float r, float g, float b, float a, float depth)
	{
		gl.getGL2().glColor4f(r, g, b, a);
		if (activeTexture != -1 | useSpriteSheet)
		{
			internalDraw (gl, offsetx, offsety, depth);
		}
	}
	void draw(GL gl, float offsetx, float offsety, float depth)
	{
		if (activeTexture != -1 | useSpriteSheet)
		{
			internalDraw (gl, offsetx, offsety, depth);
		}
	}
	
	private void internalDraw (GL gl, float offsetx, float offsety, float depth)
	{
		 gl.getGL2().glPushMatrix();
		 
		 gl.getGL2().glTranslatef(offsetx, offsety, 0);
		 gl.getGL2().glTranslatef(position.x, position.y, 0);
		 gl.getGL2().glRotatef(rotation, 0.0f, 0.0f, 1.0f);
		 gl.getGL2().glTranslatef(-getCurrentCenter().x, -getCurrentCenter().y, 0);
		 getCurrentTexture().t.enable(gl);
		 getCurrentTexture().t.bind(gl);
		 
		 gl.getGL2().glBegin(GL2.GL_QUADS);
		 {
			 gl.getGL2().glTexCoord2f(tc.left(),	tc.bottom());	gl.getGL2().glVertex3f(0,			0,			depth);
			 gl.getGL2().glTexCoord2f(tc.right(),	tc.bottom());	gl.getGL2().glVertex3f(imgDim.x,	0,			depth);
			 gl.getGL2().glTexCoord2f(tc.right(),	tc.top());		gl.getGL2().glVertex3f(imgDim.x,	imgDim.y,	depth);
			 gl.getGL2().glTexCoord2f(tc.left(),	tc.top());		gl.getGL2().glVertex3f(0,			imgDim.y,	depth);
		 }
		 gl.getGL2().glEnd();
		 getCurrentTexture().t.disable(gl);
		 
		 gl.getGL2().glPopMatrix();
	}
	
	//==============================================================================
	
	/**
	 * This should be called once per object per frame and should be over-ridden for more specific behaviour
	 */
	public void doTimeStep()
	{
		animate();
	}
	
	//==============================================================================
	
	/**
	 * Configure animation
	 * 
	 * @param enabled Enable or disable animation
	 */
	public void setupAnimation(boolean enabled)
	{
		animate = enabled;
	}
	/**
	 * Configure animation
	 * 
	 * @param enabled Enable or disable animation
	 * @param animateAll Animate over the entire sprite sheet or just the specific row
	 */
	public void setupAnimation(boolean enabled, boolean animateAll)
	{
		animate = enabled;
		this.animateAll = animateAll;
	}
	
	/**
	 * Animate the image
	 * 
	 * This only works with a sprite sheet!
	 * You can either animate over a specific row or entire sheet (Left -> Right) and (Top -> Bottom)
	 */
	public void animate()
	{
		// Make sure using sprite sheet and animation is enabled
		if (!useSpriteSheet | !animate)
			return;
		
		// Timing controls
		timerCur = (timerCur + 1) % timerMax;
		if (timerCur != 0)
			return;
		
		// Increment the current column
		spriteSheetCurCol++;
		
		if (spriteSheetCurCol > spriteSheetCols)
		{
			spriteSheetCurCol = 0;
			if (animateAll)
			{
				spriteSheetCurRow++;
				if (spriteSheetCurRow > spriteSheetRows)
				{
					spriteSheetCurRow = 0;
				}
			}
		}
		
		// Setup tex coords
		setTextureCoords();
	}
}
