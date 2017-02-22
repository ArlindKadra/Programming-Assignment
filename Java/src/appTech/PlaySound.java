package appTech;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.sound.sampled.*;

// class that will play the Sound

public class PlaySound 
{
	
	// File object for the sound file
	
	File soundFile;
	// Constructor for the class which takes the name of the sound file 
	public PlaySound (String nameOfSound) throws FileNotFoundException
	{
		// Creates a file with the nameOfSound relative to the class Path 
		
		soundFile = new File("sounds" + File.separator + nameOfSound);
	
	}
	// Method that Plays our SoundFile
	public void play() throws LineUnavailableException, UnsupportedAudioFileException, IOException 
	{
	    
	    	// Creating a Clip object which will be used to play our sound file
	        final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

	        clip.addLineListener(new LineListener()
	        {
	            @Override
	            public void update(LineEvent event)
	            {
	                if (event.getType() == LineEvent.Type.STOP)
	                    clip.close();
	            }
	        });
	        // clip opens our sound file and then plays it
	        clip.open(AudioSystem.getAudioInputStream(soundFile));
	        clip.start();
	    
	    
	}
	
	// Writes to the java log if any problems show up
	public void writeLog(String errorlog)
	{
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt",true));
		    writer.write("Class PlaySound- " + errorlog + "\r\n");
		    writer.close();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	

}
