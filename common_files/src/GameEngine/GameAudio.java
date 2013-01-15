package GameEngine;

import java.nio.ByteBuffer;
import java.util.Vector;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALException;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

public class GameAudio
{
	// Variables to store the audio files
	private Vector<String> AudioPaths;
	int AudioFilesCount = 0;
	
	// The OpenAL handle
	private AL al;
	
	// Buffers hold sound data.
	private int[] buffer;
	
	// Sources are points emitting sound.
	private int[] source;
	
	// Position of the source sound.
	private float[] sourcePos = { 0.0f, 0.0f, 0.0f };
	
	// Velocity of the source sound.
	private float[] sourceVel = { 0.0f, 0.0f, 0.0f };
	
	// Position of the listener.
	private float[] listenerPos = { 0.0f, 0.0f, 0.0f };
	
	// Velocity of the listener.
	private float[] listenerVel = { 0.0f, 0.0f, 0.0f };
	
	// Orientation of the listener. (first 3 elems are "at", second 3 are "up")
	private float[] listenerOri = { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f };
	
	// The initialisation state (initially false...duh!!)
	private boolean initialised = false;
	
	/**
	 * Constructor (nothing needed here)
	 */
	public GameAudio()
	{
		// Create the audio vector
		AudioPaths = new Vector<String>();
	}
	
	/**
	 * Add a new Audio file to the system
	 * 
	 * @param path The path to the audio file
	 */
	public void AddNewAudioFile(String path)
	{
		AudioPaths.add(path);
		AudioFilesCount = AudioPaths.size();
	}
	
	/**
	 * Play the audio file at the given index
	 * 
	 * @param index The index of the audio source to play
	 */
	public void PlayAudioIndex(int index)
	{
		// Make sure we have a proper audio item to play!
		if (index + 1 > AudioFilesCount)
			return;
		
		// Make sure we are initialised!
		Initialise();
		
		al.alSourcePlay(source[index]);
	}
	
	/**
	 * Initialise and setup the sound system
	 * 
	 * @return The success of the initialisation process
	 */
	public boolean Initialise()
	{
		// Make sure we only initialise once
		if (initialised)
			return(true);
		
		// Make sure there are audio items to load else skip this
		if (AudioPaths.size() <= 0)
			return(false);
		
		// Initialize OpenAL and clear the error bit.
		try
		{
			ALut.alutInit();
			al = ALFactory.getAL();
			al.alGetError();
		}
		catch (ALException e)
		{
			e.printStackTrace();
			return(false);
		}
		
		// Create the fuffer items
		buffer = new int[AudioFilesCount];
		source = new int[AudioFilesCount];
		
		// Load the audio data
		try
		{
			if (LoadAudioLData() == AL.AL_FALSE)
				return(false);
		}
		catch (ALException e)
		{
			e.printStackTrace();
			return(false);
		}
		
		// Set the listener values
		al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
		al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
		al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
		
		// We are now initialised
		initialised = true;
		
		// Return success
		return(true);
	}
	
	/**
	 * Load the Audio data
	 * 
	 * @return The success of the process
	 */
	private int LoadAudioLData()
	{
		// variables to load into
		int[] format = new int[1];
		int[] size = new int[1];
		ByteBuffer[] data = new ByteBuffer[1];
		int[] freq = new int[1];
		int[] loop = new int[1];
		
		// Generate buffers
		al.alGenBuffers(AudioFilesCount, buffer, 0);
		if (al.alGetError() != AL.AL_NO_ERROR)
			throw new ALException("Error generating OpenAL buffers");
		
		// Generate sources
		al.alGenSources(AudioFilesCount, source, 0);
		if (al.alGetError() != AL.AL_NO_ERROR)
			throw new ALException("Error generating OpenAL source");
		
		// Loop over all the audio files specified by the programmer
		for (int i = 0; i < AudioFilesCount; i++)
		{
			// Load wav data into a buffer.
			ALut.alutLoadWAVFile(AudioPaths.elementAt(i), format, data, size, freq, loop);
			if (data[0] == null)
				throw new RuntimeException("Error loading WAV file");
			
			// Get the buffer data
			al.alBufferData(buffer[i], format[0], data[0], size[0], freq[0]);
			
			// Setup the source with the buffered data
			al.alSourcei(source[i], AL.AL_BUFFER, buffer[i]);
			al.alSourcef(source[i], AL.AL_PITCH, 1.0f);
			al.alSourcef(source[i], AL.AL_GAIN, 1.0f);
			al.alSourcei(source[i], AL.AL_LOOPING, loop[0]);
			al.alSourcefv(source[i], AL.AL_POSITION, sourcePos, 0);
			al.alSourcefv(source[i], AL.AL_VELOCITY, sourceVel, 0);
			
			// Do another error check
			if (al.alGetError() != AL.AL_NO_ERROR)
				throw new ALException("Error setting up OpenAL source");
		}
		
		// Return success
		return(AL.AL_TRUE);
	}
	
	/**
	 * Kill all the audio datas
	 */
	public void KillAllData()
	{
		// Only try kill if we initialised
		if (!initialised)
			return;
		
		al.alDeleteBuffers(AudioFilesCount, buffer, 0);
		al.alDeleteSources(AudioFilesCount, source, 0);
	}
}
