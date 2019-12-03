package uisrc;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.*;

import java.io.FileNotFoundException;

import java.util.*;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;

public class ui
{
	public static void main(String[] args)
	{
		//Creating the Frame
		JFrame frame = new JFrame("TextFormatterz");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(1024, 768);

		//Creating the panel at bottom and adding components
		
		JPanel spanel = new JPanel(); //holds button panel and messages box and label
		
		JPanel bpanel = new JPanel(); //holds the buttons
		
		JPanel mespan = new JPanel();
		
		JPanel outpan = new JPanel();
		
		JLabel label = new JLabel("Messages:");
		
		JTextArea output = new JTextArea(); // Text Area at the Center
		
		JTextArea messages = new JTextArea(); // accepts upto 10 characters
		
		JButton open = new JButton("Open");
		
		JButton save = new JButton("Save");

        
		mespan.setLayout(new BorderLayout());
		
		outpan.setLayout(new BorderLayout());
		
		spanel.setLayout(new BorderLayout());
		
		//spanel.add(label); // Components Added using Flow Layout
		
		//spanel.add(label); // Components Added using Flow Layout
		
		mespan.add(BorderLayout.CENTER, messages);
		
		mespan.add(BorderLayout.NORTH, label);
		
		spanel.add(BorderLayout.WEST, bpanel);
		
		spanel.add(BorderLayout.CENTER, mespan);
		
		bpanel.setLayout(new GridLayout(2,1));
		
		bpanel.add(open);
		
		bpanel.add(save);

		//Adding Components to the frame.
		
		frame.add(BorderLayout.SOUTH, spanel);
		
		frame.add(BorderLayout.CENTER, output);
		
		bpanel.setPreferredSize(new Dimension(200,100));
		
		spanel.setPreferredSize(new Dimension(500,100));

		frame.setVisible(true);

        //Adding Action Listener to Save Button
		
        save.addActionListener(new ActionListener()
        {
		
	        public void actionPerformed(ActionEvent e)
	        {
			
	        	messages.setText("Temporary Save Successful!");
			
	        }
		
        }
			       
			      );

        //Adding Action Listener to  Open Button
		
        JFileChooser chooser = new JFileChooser();
		
        //File f;
		
        open.addActionListener(new ActionListener()
        {
		
	        public void actionPerformed(ActionEvent e)
	        {

	            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files Only", "txt");
			
	            chooser.setFileFilter(filter);
			
	            int returnVal = chooser.showOpenDialog(null);
			
	            if(returnVal == JFileChooser.APPROVE_OPTION) 
	            {
			    
	              System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
			    
	              File f = chooser.getSelectedFile();
			    
	              System.out.println(f.getAbsolutePath());
			    
	              Scanner sc;
			    
					try 
					{
						
						sc = new Scanner(f);
						
						while (sc.hasNextLine()) 
						{
							
							String j =sc.nextLine();
				
							if (j.startsWith("-"))
							{
								
								switch(j.charAt(1)) 
								{
								
									case "r":
									
								}
									
							}
									
						System.out.println(sc.nextLine());
									
						}
								
					} 
							
							catch (FileNotFoundException e1) 
							{

						System.out.println("Error");
								
					} 
							
	            }      	
						
	        }
			    
        }
			
			);
			
	}
		
}
