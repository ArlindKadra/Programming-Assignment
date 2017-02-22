package appTech;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Date;

public class ApplicationFrame extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3931470465986244458L;
	private JButton fetchButton; 	 // Button that will fetch the response
	private JCheckBox debugCheckBox; // CheckBox to allow debugging or not
	private JTextArea logField; 	 // Field where all of the log messages will appear
	private JLabel urlLabel; 
	private JTextField urlField; 	 // Field where the url will be input
	private JLabel logMessages;
	private PlaySound sound1; 		 // declaring a variable for the sound player 1
	private PlaySound sound2; 		 // declaring a variable for the sound player 2
	private int debug = 1; 			 // declaring the initial debug value which is 1
	private JScrollPane scroll;		 // declaring a JScrollPane so we can add the text area on it
	
	public  ApplicationFrame()
	{
		
		// Giving a name to the window and choosing a null layout
		super("AppTech Assignment");
		setLayout(null);
		try
		{
			this.setIconImage((new ImageIcon(getClass().getResource("icon.png")).getImage()));
		}
		catch(NullPointerException e)
		{
			writeLog("Error at Constructor ApplicationFrame- " + "The image used for the icon was not found " + e.getMessage() );
			addlogField("Error at Constructor ApplicationFrame- " + "The image used for the icon was not found" + e.getMessage() );
		}
		// Creating all of the neccesary Gui objects
		fetchButton = new JButton ("Fetch");
		debugCheckBox = new JCheckBox("Debug Output");
		logField = new JTextArea();
		urlLabel = new JLabel("URL");
		urlField = new JTextField();
		logMessages = new JLabel("Log Messages");
		try
		{
			sound1 = new PlaySound("Gun.wav");
			sound2 = new PlaySound("Applause.wav");
		}
		catch(FileNotFoundException e)
		{
			writeLog("Constructor PlaySound- " + "Sound File not Found " + e.getMessage());
			addlogField("Constructor PlaySound- " + "Sound File not Found " + e.getMessage());
		}
		
		
		// Setting the size and the location of each component
		urlLabel.setSize(70, 30);
		urlLabel.setLocation(130, 60);
		urlLabel.setFont(new Font("Tahoma",Font.BOLD,18));
		urlField.setSize(200, 30);
		urlField.setLocation(210,60);
		fetchButton.setSize(80, 30);
		fetchButton.setLocation(430, 60);
		debugCheckBox.setSize(200, 50);
		debugCheckBox.setFont(new Font("Tahoma",Font.BOLD,12));
		debugCheckBox.setLocation(210, 120);
		debugCheckBox.setSelected(true);
		logMessages.setSize(110, 20);
		logMessages.setLocation(130, 190);
		logMessages.setFont(new Font("Tahoma",Font.BOLD,12));
		
		
		
		// Adding the text area inside the scroll, so we can see all of the info
		
		logField.setEditable(false);
		scroll = new JScrollPane(logField);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setSize(380, 150);
		scroll.setLocation(130, 230);
	    
		// Adding the Components into the main window
		
		add(urlLabel);
		add(urlField);
		add(debugCheckBox);
		add(fetchButton);
		add(logMessages);
		add(scroll);
		
		// Adding the necessary Listener to the fetch button
		
		ButtonListener fetchListener = new ButtonListener();
		fetchButton.addActionListener(fetchListener);
		
		// Adding the Listener for the debug Checkbox
		
		CheckBoxListener debugListen = new CheckBoxListener();
		debugCheckBox.addItemListener(debugListen);
	
	}
	
	
	// Function that executes an action from the json enconding which is replied from the script
	public void executeAction(String input)
	{
		JSONObject json = null;
		String command;	// String that will keep the command

		try 
		{
			// get the Json Object from the String
			json = (JSONObject)new JSONParser().parse(input);
			// get the object from the hash map with the cmd key and cast it to string
			command = (String)json.get("cmd");
			if ( debug == 1)
			{
				logField.append("The Server returned: " + input + "\n");
			
			}
			
			/*	Figure out what the command was and do the appropiate action
			 *  In case the command its text or color, we need to get the color or text
			 *  respectively. We use the keys text and color to find the appropiate objects in 
			 *  the hash Map. 
			 *  In case of text we cast the object to String
			 *  in case of color we use color.decode and cast the string to a certain integer
			 *  which  returns the corresponding opaque color.
			 *  If we get a command which we do not recognize from the script, we write in on the 
			 *  log.
			 *  
			 */
			switch(command)
			{
			
				case "soundOne": sound1.play();
								 
								 break;
				case "soundTwo": sound2.play();
								 break;
								 
				case "text":   	 if ( debug == 1)
								 {
								 logField.append((String)json.get("text") + "\n");
								 }
								 break;
				case "color":    fetchButton.setBackground(Color.decode((String)json.get("color")));		;
								 break;
				default:   		 writeLog("Method executeAction " + "No Valid command was returned from the server");
								 addlogField("Method executeAction " + "No Valid command was returned from the server");
			   					 break;	
			}
		}
		// We catch the exceptions and write about each specific one on the log
		catch (ParseException e) 
		{
			writeLog("Method executeAction- " + "Problems while Parsing JsonObject: " + e.getMessage());
			addlogField("Method executeAction- " + "Problems while Parsing JsonObject: " + e.getMessage());
		}
		catch(ClassCastException e1)
        {
            writeLog("Method executeAction- " + "Problems while Casting Json command to string: " + e1.getMessage());
            addlogField("Method executeAction- " + "Problems while Casting Json command to string: " + e1.getMessage());
        }
		catch(NumberFormatException e2)
		{
			writeLog("Method executeAction- " + "The string returned from the server as color, cannot be interpreted as a number: " + e2.getMessage());
			addlogField("Method executeAction- " + "The string returned from the server as color, cannot be interpreted as a number: " + e2.getMessage());
		}
		catch (NullPointerException e3)
		{
			writeLog("Method executeAction- " + "There was a null pointer Exception: " + e3.getMessage());
			addlogField("Method executeAction- " + "There was a null pointer Exception: " + e3.getMessage());
			
		}
		catch (LineUnavailableException e4)
		{
			writeLog("PlaySound - Method play- " + "Line is not avaible " + e4.getMessage());
			addlogField("PlaySound - Method play- " + "Line is not avaible " + e4.getMessage());
		}
		catch (UnsupportedAudioFileException e5)
		{
			writeLog("PlaySound - Method play- " + "Audio File is not supported " + e5.getMessage());
			addlogField("PlaySound - Method play- " + "Audio File is not supported " + e5.getMessage());
		}
		catch (IOException e6)
		{
			writeLog("PlaySound - Method play- " + "IOException " + e6.getMessage());
			addlogField("PlaySound - Method play- " + "IOException " + e6.getMessage());
		}
		catch (Exception e7)
		{
			writeLog("Method executeAction- " + e7.getMessage());
			addlogField("Method executeAction- " + e7.getMessage());
		}		
	}
	
	/*
	 *  The functions which does write to the log and is called by the other methods of the class
	 *  when we do want to write about what happened on the log. Only writes to the log if 
	 *  the variable is 1
	 */
	
	public void writeLog(String errorlog)
	{
		try 
		{
			Date datevar = new Date();
			// Create a BufferedWriter which encapsulates FileWriter and writes to the file 
			BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt",true));
			// Write the string which contains the error information along with the class name
			writer.write(datevar.toString() + " Class ApplicationFrame- " + errorlog + "\r\n");
			writer.close();
		} 
		catch (IOException e1) 
		{
			if (debug == 1)
			{
				addlogField("Method writeLog" + "The log file failed to be created and be written on: " + e1.getMessage());
			}
		}
		catch (Exception e) 
		{
			if (debug == 1)
			{
				addlogField("Method writeLog" + "The log file failed to be created and be written on: " + e.getMessage());
			}
		}
	}
	
	
	// Method that helps makes the connection, sends the required commads and receives the request
	
	public  String executeScript(String targetURL, String urlParameters) 
	{
	  HttpURLConnection connection = null;  
	  try 
	  {
	    //Create connection with url
		URL url = new URL(targetURL);
		// Set the necessary configurations
		connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", 
		    "application/x-www-form-urlencoded");
		
		connection.setRequestProperty("Content-Length", 
		    Integer.toString(urlParameters.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");  
		
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		
		/* Write to the script in the url after you execute the connection
		 * Close the object which writes after finishing
		 */
		DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.close();
		// Get Response code from the connection
		int statusCode = connection.getResponseCode();
		if ( debug == 1)
		{
			logField.append("Status returned by the Server: " + statusCode + "\n");
		}
		
		
		//Get Response into the string after creating an object which reads the reply
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		// Save the first String which comes as a reply
		String inputLine = in.readLine();
		
		
		// Close the input object
		in.close();
		// Return the String from the Script so it can be processed by another method
		return inputLine;
		  }
		  catch (MalformedURLException e1)
		  {	
			 writeLog("Method executeScript- " + "A MalformedUrlException occurred: " + e1.getMessage());
			 addlogField("Method executeScript- " + "A MalformedUrlException occurred: " + e1.getMessage());
			 return null;
		  }
		  catch (IOException e2)
		  {
			 writeLog("Method executeScript- " + "An IO Exception occurred: " + e2.getMessage());
			 addlogField("Method executeScript- " + "An IO Exception occurred: " + e2.getMessage());
			 return null;
		  }
		  catch (IllegalStateException e3)
		  {
			writeLog("Method executeScript- " + "An IllegalStateException: " + e3.getMessage());
			addlogField("Method executeScript- " + "An IllegalStateException: " + e3.getMessage());
			return null;
		  }
		  catch (NullPointerException e4)
		  {
			writeLog("Method executeScript- " + "A NullPointerException happened: " + e4.getMessage());
			addlogField("Method executeScript- " + "A NullPointerException happened: " + e4.getMessage());
			return null;
		  }
		  catch (Exception e) 
		  {
			writeLog("Method executeScript- " + "An error occurred: " + e.getMessage());
			addlogField("Method executeScript- " + "An error occurred: " + e.getMessage());
			return null;
		  } 
		  finally 
		  {
			// if a connection was established, disconnect from it
		    if(connection != null)  
		    {
		      connection.disconnect(); 
		    }
		    else
		    {
		    	writeLog("Method executeScript- " + "A connection was not created");
		    	addlogField("Method executeScript- " + "A connection was not created");
				
		    }
		  }
	}
	
	// Eventhandler for the ActionEvent, this is used for fetchButton
	private class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// Checking if the user has left the field empty, and notifying him
			if (urlField.getText().equals(""))
			{
				JOptionPane.showMessageDialog(ApplicationFrame.this, "Please do not leave the Url field empty", "URL field is empty", JOptionPane.ERROR_MESSAGE);
			}
			else 
			{
				// Create variable String for our url Parameters
				String urlParameters;
				try 
				{
					// Assign the necessary name and value pair to our String
					urlParameters = "getCommands=" + URLEncoder.encode("true", "UTF-8");
					/*
					 * Execute the request to the server with url given in the program and
					 * the url parameters using method executeScript. Get the returning String
					 * from the executeScript method ( Server's reply )  and give it to the
					 * executeAction method. 
					 * The executeAction method will do the required action corresponding to the
					 * servers reply. 
					 */
					executeAction(executeScript(urlField.getText(),urlParameters));
				}
				catch (UnsupportedEncodingException e1) 
				{
					writeLog("Method actionPerformed- " + "The encoding of the word true failed " + e1.getMessage());
					addlogField("Method actionPerformed- " + "The encoding of the word true failed " + e1.getMessage());
				}
			    catch (NullPointerException e2)
				{
			    	writeLog("Method actionPerformed- " + "There is no string urlParameters, or the urlField is unacessible " + e2.getMessage());
			    	addlogField("Method actionPerformed- " + "There is no string urlParameters, or the urlField is unacessible " + e2.getMessage());

				}
				catch (Exception e3)
				{
					writeLog("Method actionPerformed- " + e3.getMessage());
					addlogField("Method actionPerformed- " + e3.getMessage());
				}
			}
		}
	}
	// Listener Class for the debugCheckBox
	private class CheckBoxListener implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e) 
		{
			if(debugCheckBox.isSelected())
			{
				debug = 1;
			}
			else
			{
				debug = 0;
			}
		}
	}
	// adds the messages to the textarea of the program, when the JChecBox is checked
	private void addlogField(String errorDesc)
	{
		// Show the messages on the textarea if the debug variable is 1
		if ( debug == 1 )
		{
			logField.append("Class ApplicationFrame" + errorDesc + "\n");
		}
	}
}
