/*
 * Author Arlind Kadra     Application Assignment
 */
package appTech;

import javax.swing.JFrame;

public class ApplicationStart 
{

	public static void main(String[] args)
	{
		
		ApplicationFrame app = new ApplicationFrame();
		app.setSize(600,450);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.setResizable(false);
		app.setLocationRelativeTo(null);
	}
}
