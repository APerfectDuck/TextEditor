package Text_formatter;

import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.*;
import java.util.*;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;




/* README!
 * CMM -
 * The code is functional and works for 4 basic test cases, feel free to try
 * to find test cases that break it and then fix the errors. This readme is a summary
 * of the structure to help you understand my code.  I'm sorry it isn't object
 * oriented...   There are no classes besides ui, but I made use of 
 * several static functions to make the main[] a little less
 * ugly & huge.  There's some long static function so if you're on eclipse I suggest you
 * minimize them all so you can look at all the names at the same time, it makes my messy code
 * look a little less messy :)
 * The only thing not really implemented is the save button, if you want to implement it, go for it.
 * Right now the file is created before the save button is pressed.  I'm not sure if save is supposed to create
 * the file or just let you decide a name/location for the file, if you want to figure that out, go for it. 
 * 
 *   The static functions fall into a couple main categories:
 * 
 * 0) "Error function"
 * 	errorprinter() just prints the error messages into the proper box.  It needs to have access to the GUI component that it adds text to
 *  which is why it has to take a JTextArea parameter in addition to it's String parameter
 * 
 * 
 * 1) The "sending functions" - these are called after group of lines are properly formatted
 * 		-sendline()
 * 				Sendline takes a line and adds it to a arraylist of every line inputted so far
 * 				Once the whole input file is read, this big ArrayList is written to the output file 
 * 				(the arraylist is written to the output file near the end of main[]) 
 * 
 * 		-sendgroup() & sendgroup2columns - these take a properly formatted group of lines and sends it line-by-line to sendline()
 * 				due to the weird nature of formatting 2-column text, every time a file switches from wanting 1 column to 2 columns
 * 				or vice versa, all the lines built up need to be sent off, this function does this.
 * 
 * 2) The "formatting functions" - they take care of formatting based on the current attributes (justify, line length)
 *   2.1) The "justify functions" - handle left right center formatting
 *   		defaultprinter() is left justified
 *   		smartprinter() looks at the current specified justification and calls the right justifier accordinly
 *			the rest are self explanatory
 * 	 2.2) "Blank line function" inserts blank lines whenever the command is given
 * 
 * 3) Defaulting functions - they reset things back to their default state
 * 	defaultcmd() - sets all the formatting back to default (80 char/line, left justified, etc.)
 * 		back_to_empty() - clears out the group of lines.  Called at the end of sendgroup() or sendgroup2columns()
 * 		so that lines sent off to be entered in the file can be cleared so the next group of lines can be formatted
 * 
 *  4) Attribute updating function
 *  	commandhandler() - whenever a command is entered, this function updates the array (cmds[]) that keeps track of the
 *  	desired formatting at any given moment.  There's a summary of the structure of the cmds[] array at the bottom of the program.
 */

public class ui
{

	
	static int[] dflt = {80, 0, 0, 1, 0, 0, 0, 1, 0};
	

// (0) Error functions	
	// Print error message in GUI
	static void errorprinter(String s, JTextArea err_output)
	{
		err_output.append(s);
		
	}
	

// (1) Sending functions
	// Puts the line in a linkedlist, the linkedlist is used to make the output file after the whole file is inputted
	static void sendline(String line, ArrayList<String> finaltxt)
	{
		line = line + "\n";
		finaltxt.add(line);
	}

	// Takes formatted lines and sends them one at a time to sendline(), used for 1-column format
	static int sendgroup(int[] cmds, String[] lines, int idx, ArrayList<String> finaltxt )
	{
		for(int i=0;i<=idx;i++)
			sendline(lines[i], finaltxt);
		return idx;
	}
	
	// Takes formatted lines and sends them one at a time to sendline(), used for 2-column format
	static int sendgroup2columns(int[] cmds, String[] lines, int idx, ArrayList<String> finaltxt)
	{
		String[] finished_lines = new String[100];
		int finished_lines_idx = 0;
		int rows = (idx+1)/2;
		for(int i=0;i<rows;i++)
		{
			while(lines[i].length()<45)
				defaultprinter(" ", lines, i); // Add the spaces needed to make 2 columns work
			finished_lines[finished_lines_idx] = lines[i] + lines[i+rows]; // Concatenate each line of left column with it's corresponding right column
			finished_lines_idx ++;
		}
		finished_lines_idx --;
		idx = back_to_empty(lines);
		finished_lines_idx = sendgroup(cmds, finished_lines, finished_lines_idx, finaltxt);
		return idx;
	}
	

	
// (2) Formatting functions
  // (2.1) Justify Functions

	// Chooses correct justification based on current format specifications
	static int smartprinter(int[] cmds, String s, String[] lines, int idx) // Print with correct left/right/center alignment
	{
		switch(cmds[1])
		{
		case 1: // Center justify printing
			idx = centerprinter(cmds, s, lines, idx);
			break;
		case 2: // Center justify printing
			idx = rightprinter(cmds, s, lines, idx);
			break;
		case 3: // Equal justify printing
			idx = equalprinter(cmds, s, lines, idx);
			break;
		default: // Left justify printing
			idx = defaultprinter(s, lines, idx);
		}
		return idx;
	}
	
	// Left justify printing
	static int defaultprinter(String s, String[] lines, int idx)
	{
		lines[idx] += s;
		return idx;
	}
	
	// Right justify printing
	static int rightprinter(int[] cmds, String s, String[] lines, int idx)
	{
		int spaces = (cmds[0] - s.length());
		for(int i=0;i<spaces;i++)
			idx = defaultprinter(" ", lines, idx); // Print spaces to right justify text
		idx = defaultprinter(s, lines, idx); // Print the text
		return idx;
	}
	
	// Center justify printing
	static int centerprinter(int[] cmds, String s, String[] lines, int idx)
	{
		int spaces = (cmds[0] - s.length())/2;
		for(int i=0;i<spaces;i++)
			idx = defaultprinter(" ", lines, idx); // Print spaces to center text
		idx = defaultprinter(s, lines, idx); // Print the text
		return idx;
	}
	
	// Equal spacing printer
	static int equalprinter(int[] cmds, String s, String[] lines, int idx)
	{
		String[] words = s.split("\\s+"); //split s into its words
		s = "";
		for(int i=0;i<words.length;i++)
			s = s+ words[i] + " ";
		s = s.trim();
		int num_words = words.length;
		int num_extra = cmds[0] - s.length();
		int extra_spaces_per = num_extra/(num_words-1); // spaces per word (in addition to the normal 1 to achieve equal spacing
		int uneven_fillers = num_extra % (num_words-1); // extra spaces to throw in to make it even
		for(int i=0;i<num_words;i++)
		{
			idx = defaultprinter(words[i], lines, idx); // print the next word
			if(i<(num_words-1)) // unless you made it to final word, print spaces
			{
				idx = defaultprinter(" ", lines, idx); // print the normal space
				for(int j=0;j<extra_spaces_per;j++)
					idx = defaultprinter(" ", lines, idx); // print any extra spaces
				if(uneven_fillers > 0)
				{
					idx = defaultprinter(" ", lines, idx); // if needed, print another filler space
					uneven_fillers--;
				}
			}
		}
		return idx;
	}

	// Title printer
	static int titleprinterln(int[] cmds, String s, String[] lines, int idx, JTextArea messages)
	{
		if(s.length() <= cmds[0]) //if title length is valid
		{
			int spaces = (cmds[0] - s.length())/2;
			for(int i=0;i<spaces;i++)
				defaultprinter(" ", lines, idx); // Print spaces to center text
			defaultprinter(s, lines, idx); // Print the text
			idx ++; // Go to the next line
			for(int i=0;i<spaces;i++)
				defaultprinter(" ",lines, idx); // Print spaces to center underline
			for(int i=0;i<s.length();i++)
				defaultprinter("-",lines,idx); // Print the underline
			idx++;
			if(cmds[3]==2)
				idx++;
			cmds[4] = 0; //Set the title attribute OFF
		}
		else
			errorprinter("Error: Title is too long", messages);
		return idx;
	}
	
	
	// (2.2) Blank line function
		static int blanklinehandler(int[] cmds, String[] lines, int idx)
		{
			if(lines[idx]!="")
				idx++;
			while(cmds[6]>0)
			{
				idx++;
				cmds[6]--;
			}
			cmds[8] = 0;
			return idx;
		}
		
	
// (3) Defaulting functions
	static void defaultcmd(int[] cmd){
		for(int i=0;i<9;i++)
			cmd[i] = dflt[i];
	}
	
	static int back_to_empty(String[] lines)
	{
		for(int i=0;i<lines.length;i++)
			lines[i] = "";
		return 0;
	}
	
// (4) Attribute Updating Function
	// Update attribute list whenever a command is entered
	static int commandhandler(int[] cmds, String j, Scanner sc, String[] lines, int idx, JTextArea messages, ArrayList<String> finaltxt)
	{
		switch(j.charAt(1)) {
		
		//line length
		case 'n':
			if(cmds[7] == 2)
				errorprinter("Error: Line length cannot be changed while in 2 column format", messages);
			else try
			{
			    // convert String to int
				int num = Integer.parseInt(j.substring(2));
			    if(num < 1 || num > 100)
			    	errorprinter("Error: Number of characters is not in valid range (1-100)", messages);
			    else{
			    cmds[0] = num; // set line length attribute
			    }
			} catch (NumberFormatException nfe){
			      errorprinter("Error: Please enter a valid number", messages);
			}
			break;
			
		// justification
		case 'l':
			cmds[1] = 0;
			break;
			
		case 'c':
			cmds[1] = 1;
			cmds[5] = 0; //no paragraph indentation when NOT left justified
			break;
			
		case 'r':
			cmds[1] = 2;
			cmds[5] = 0; //no paragraph indentation when NOT left justified
			break;
			
		case 'e':
			cmds[1] = 3;
			cmds[5] = 0; //no paragraph indentation when NOT left justified
			break;
			
		
		// wrap
		case 'w':
			if(j.charAt(2) == '+')
				cmds[2] = 1;
			else if(j.charAt(2) == '-')
				cmds[2] = 0;
			else
				errorprinter("Error: Wrap must be '+' or '-'.", messages);								
			break;
			
		// spacing
		case 's':
			cmds[3] = 1;
			break;
			
		case 'd':
			cmds[3] = 2;
			break;
			
		// title	
		case 't':
			cmds[4] = 1;
			break;
			
		// paragraph indentation
		case 'p':
			try
			{
			    // convert String to int
				int num = Integer.parseInt(j.substring(2));
			    if(num < 1 || num >= cmds[0]) // if it is bigger than line length
			    	errorprinter("Error: Number of spaces is not in valid range (Less than line length)", messages);
			    else{
			    cmds[5] = num; // set paragraph indentation
			    }
			} catch (NumberFormatException nfe){
			      errorprinter("Error: Please enter a valid number", messages);
			}
			break;
		
		// blank lines
		case 'b':
			try
			{
			    // convert String to int
				int num = Integer.parseInt(j.substring(2));
			    if(num <= 1 || num >= 10)
			    	errorprinter("Error: Number of blank lines is not in valid range (0-10)", messages);
			    else{
			    	cmds[6] = num;
			    	cmds[8] = 1;
			    }
			} catch (NumberFormatException nfe){
			      errorprinter("Error: Please enter a valid number", messages);
			}
			break;
		
			
		// Columns	
		case 'a':
			if(j.charAt(2) == '1')
			{
				if(cmds[7] == 2) // if 2 columns were specified and it was changed to 1, print everything in 2 column fashion, change line length back to 80
				{
					if(!lines[0].isEmpty())
					{
						idx = sendgroup2columns(cmds, lines, idx, finaltxt);
						back_to_empty(lines);
					}
					cmds[0] = 80;
				}
				cmds[7] = 1;
			}
			else if(j.charAt(2) == '2')
			{
				if(cmds[7] == 1) // if 1 column was specified and it was changed to 2, print everything so far in 1 column fashion, change line length to 35
				{	
					if(!lines[0].isEmpty())
					{
						idx = sendgroup(cmds, lines, idx, finaltxt);
						back_to_empty(lines);
					}

					cmds[0] = 35;
				}
				cmds[7] = 2;
			}
			else
				errorprinter("Error: Invalid number of columns.  Please specify 1 or 2 columns.", messages);								
			break;
		}
		return idx;
	}
	
	
	
	
	public static void main(String[] args)
	{

	//Creating the Frame
        JFrame frame = new JFrame("TextFormatterz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        
        //Creating the panel at bottom and adding components
        JPanel spanel = new JPanel(); //holds button panel and messages box and label
        JPanel bpanel = new JPanel(); //holds the buttons
        JPanel mespan = new JPanel(); //holds the messages text box
        JPanel outpan = new JPanel(); //holds the formatted text output box
        
        JLabel label = new JLabel("Messages:"); //label for the Messages text box
        
        JTextArea output = new JTextArea(); //Output text box
        output.setFont(new Font("monospaced", Font.PLAIN, 12)); // Make it monospaced characters
        output.setEditable(false); //turns off editability of formatted output text box
        
        JTextArea messages = new JTextArea(); //accepts up to 10 characters
        messages.setEditable(false);
        
        JButton open = new JButton("Open"); //Open file button
        JButton save = new JButton("Save"); //Save file button
        
        //Sets the layouts of each of the three panels to BorderLayout for formatting purposes
        mespan.setLayout(new BorderLayout());
        outpan.setLayout(new BorderLayout());
        spanel.setLayout(new BorderLayout());

        //Creates and sets up Vertical scroll bar to formatted output text box
        JScrollPane oVert = new JScrollPane(output);
        oVert.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //Creates and sets up Vertical scroll bar to messages text box
        JScrollPane mVert = new JScrollPane(messages);
        mVert.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //Adds Messages text box and messages label to the mespan Panel
        mespan.add(BorderLayout.CENTER, mVert);
        mespan.add(BorderLayout.NORTH, label);
        
        //Adds Open and Save buttons to the bpanel
        bpanel.setLayout(new GridLayout(2,1));
        bpanel.add(open);
        bpanel.add(save);
        
        //Adds the button and messages panels to one single spanel
        spanel.add(BorderLayout.WEST, bpanel);
        spanel.add(BorderLayout.CENTER, mespan);


        //Adding component panels to the JFrame
        frame.add(BorderLayout.SOUTH, spanel);
        frame.add(BorderLayout.CENTER, oVert);
        
        //Sets a size for the components to keep them at a decent viewing ratio
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
        });
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
	               // Clear out the error message box each time a new file is formatted.
	               messages.selectAll();
				   messages.replaceSelection("");
	               errorprinter("You chose to open this file: " + chooser.getSelectedFile().getName(), messages);
	               File f = chooser.getSelectedFile();
	               errorprinter(f.getAbsolutePath()+"\n", messages);     
	               ArrayList<String> finaltxt = new ArrayList<String>(); // ArrayList containing all the lines that are entered into the output file
	               Scanner sc;
	               String[] lines = new String[100];
	               int idx = back_to_empty(lines);
					try {
						sc = new Scanner(f);
						int[] cmds = new int[9];
						defaultcmd(cmds);
						while (sc.hasNextLine()) {
							String j =sc.nextLine();
							// Handle commands
							if (j.startsWith("-"))
									idx = commandhandler(cmds, j, sc, lines, idx, messages, finaltxt);
							// Handle printing text
							else
							{
								if(cmds[8]==1) // First handle blank lines
									idx = blanklinehandler(cmds, lines, idx);
								if (cmds[4] == 1) // Then handle titles to be printed
								{
									idx = titleprinterln(cmds,j, lines, idx, messages);
									if (cmds[3] == 2) // Print an extra new line if doublespaced
										idx ++;
								}
								else // Then handle all other formatting
								{
									if( (cmds[7] == 1 && cmds[5]<cmds[0]) || (cmds[7] == 2 && cmds[5]<35) ) // If paragraph indentation is valid, make the indentations
									{
										int count = cmds[5];
										while(count > 0)
										{
											j = " "+ j;
											count--;
										}
									}
									else
										errorprinter("Error: Paragraph indentation is too large", messages);
								    if(cmds[2] == 1) // Then develop lines with wrapping.
									{
										while(!j.isEmpty()) // While j still has characters to read, add them to the lines
										{
											j = j.trim();
											//lines[idx] = lines[idx].trim();
											int chars_remaining = cmds[0]-lines[idx].length();
											if(j.length()<chars_remaining) // If there's space to hold the whole line, add it
											{
												idx = smartprinter(cmds, j, lines, idx);
												idx++;
												if(cmds[3]==2)
													idx++;
												j = "";
											}
											else // If there isn't space for the whole line, add what fits
											{
												int index = j.indexOf(' ');
												String firstword;
											    if (index > -1) // if there's more than one word
											    {
											        firstword = j.substring(0, index).trim(); // get the first word
											        j = j.substring(index).trim(); // store the rest as j
											    } 
											    else 
											    {
											        firstword = j; // get the whole thing
											        j = ""; // store the rest as j
											    }
											    if (firstword.length()<chars_remaining)
											    {
													idx = smartprinter(cmds, firstword, lines, idx);
													idx++;
													if(cmds[3]==2)
														idx++;
													firstword = "";
											    }
											    else if(firstword.length()>cmds[0]) //If a word is too big, throw an error message.
											    {
											    	errorprinter("Error: A word is longer than the allowed line length.  Skipping line.", messages);
											    	break;
											    }
											    else // If the word doesn't fit, wrap up this line and move on to the next line in output
											    {
											    	idx++;
													if(cmds[3]==2)
														idx++;
											    	idx = smartprinter(cmds, firstword, lines, idx);
											    }
											}
										}
									}
								    else // Develop lines with no wrapping
								    {
										while(j.length()>0)
										{
											if(j.length()<=cmds[0])
											{
												idx = smartprinter(cmds, j, lines, idx);
												idx++;
												if(cmds[3]==2)
													idx++;
												j = "";
											}
											else
											{
												int char_idx = Math.min(cmds[0]-1,j.length()-1);
												while(j.charAt(char_idx)!=' ')
												{
													char_idx--;
													if(char_idx<1)
													{
														errorprinter("Error", messages);
														break;
													}
												}
												String first_part = j.substring(0, char_idx);
												j = j.substring(char_idx+1, j.length());
												idx = smartprinter(cmds, first_part, lines, idx);
												idx++;
												if(cmds[3]==2)
													idx++;
											}
										}
								    }
								}
							}
						}
						// Print any lines remaining.
						if(cmds[7] == 1)
						{
							sendgroup(cmds, lines, idx, finaltxt);
							back_to_empty(lines);
						}	
						else if(cmds[7] == 2)
						{
							sendgroup2columns(cmds, lines, idx, finaltxt);
							back_to_empty(lines);
						}
						else
							errorprinter("Error", messages);
						
						
						// Write the formatted text to the file
						try{
			                 PrintWriter writer = new PrintWriter("output.txt");
			                 int j = 0;
			                 while (finaltxt.size() > j) 
			                 {
			                    writer.print(finaltxt.get(j++));
			                 }
			                 writer.flush();
			                 writer.close();
			             } catch (FileNotFoundException e1) 
			                        {e1.printStackTrace();
			                        System.exit(0) ;         
			                        }
						
						
						// Show a preview of the formatted text
						output.selectAll();
					    output.replaceSelection(""); // Clear the preview window of leftovers
					    File file = new File("output.txt");
					    FileReader fr = new FileReader(file);
					    BufferedReader br = new BufferedReader(fr);
					    String line;
					    while((line = br.readLine()) != null){
					        output.append(line);
					        output.append("\n");
					    }

	
					}
					catch (FileNotFoundException e1) {
						errorprinter("Error, no file found", messages);     
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}      	
	        }
        });
	}

}
/*
attribute index <cmds[i]>
0 - [1-100] 			line length [length of line]
1 - [0-3]				justification [0->left , 1->center , 2-> right , 3-> equal]
2 - [0-1] 				wrap [0->off , 1->on]
3 - [1-2] 				spacing[1->single , 2->double]
4 - [0-1] 				title[0->off , 1-> on]
5 - [0-linelength-1] 	paragraph indentation [# of spaces]
6 - [0-10]				blank lines [# of lines]
7 - [1-2]				columns [1->1 column , 2->2 columns]
8 - [0-1]				is blank line > 0? [0->no , 1->yes)
*/
