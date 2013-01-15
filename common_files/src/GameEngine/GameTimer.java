package GameEngine;

import javax.media.opengl.*;
import GameEngine.Game;

/**
 * A timer class that is used to provide accurate time intervals used by the game engine
 *
 * @author Richard Baxter
 * @author Justin Crause
 */
class GameTimer extends Thread
{
	private boolean running;
	private GLAutoDrawable drawer;
	private Game game;
	
	//==============================================================================
	
	/**
	 * Constructor that creates the timer object and stores the Game object
	 *
	 * @param g The Game item
	 * @param d The OpenGL Drawer object
	 */
	public GameTimer (Game g, GLAutoDrawable d)
	{
		game = g;
		drawer = d;
	}
	
	//==============================================================================
	
	/**
	 * Run method which creates the game timer object
	 */
	public void run()
	{
		final long TICKS_PER_SECOND = game.game_frames_per_second;
		final long SKIP_TICKS = 1000000000L / TICKS_PER_SECOND;
		final int MAX_FRAMESKIP = 15;
		long next_game_tick = System.nanoTime();
		int loops;
		
		running = true;
		while (running)
		{
			loops = 0;
			while(System.nanoTime() > next_game_tick && loops < MAX_FRAMESKIP)
			{
				game.logicGame();
				
				next_game_tick += SKIP_TICKS;
				loops++;
			}
			drawer.display();
		}
		
		// Cleanup
		System.exit(0);
	}
	
	//==============================================================================
	
	/**
	 * The end game function. Called when the game ends
	 */
	public void endGame()
	{
		System.out.println("GameTimer.endGame() called");
		running = false;
	}
}
