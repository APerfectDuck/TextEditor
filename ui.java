import java.awt.*;

import java.awt.List;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.*;

import java.util.*;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @param s
 * @param err_output
 * @param line
 * @param finaltxt
 * @param cmds
 * @param lines
 * @param idx
 * @param cmds
 * @return
 * @param messages
 * @param j
 * @param sc
 * @param args
 * @author Jose Elenes, Albert Haddad, Caden Midkiff, Madhu Madhavan Sridhar
 * @Class_ID CSE 360: 85141
 * @TeamProject 15
 * @Description The program is a text formatter tool that takes strings as inputs
 * and changes the font and orientation according to user commands
*/

public class ui
{
	

	static int[] dflt = {80, 0, 0, 1, 0, 0, 0, 1, 0};
	
	/**
	 * @param s
	 * @param err_output
	 */
	
	static void errorprinter(String s, JTextArea err_output)
	{
		
		err_output.append(s);
		
	}
	
	/**
	 * @param line
	 * @param finaltxt
	 */
	
	static void sendline(String line, ArrayList<String> finaltxt)
	{
		/**
		 * The "sending functions" - these are called
		 * after group of lines are properly formatted
		 */
		
		line = line + "\n";
		
		finaltxt.add(line);
		
	}
	
	/**
	 * @param cmds
	 * @param lines
	 * @param idx
	 * @param finaltxt
	 * @return
	 */
	
	static int sendgroup(int[] cmds, String[] lines, int idx, ArrayList<String> finaltxt )
	{
		
		
		for(int i=0;i<=idx;i++)
		{
			
			sendline(lines[i], finaltxt);
			
		}
		
		idx = back_to_empty(lines);
		
		return idx;
		
	}
	
	/**
	 * @param cmds
	 * @param lines
	 * @param idx
	 * @param finaltxt
	 * @return
	 */
	
	static int sendgroup2columns(int[] cmds, String[] lines, int idx, ArrayList<String> finaltxt)
	{
		
		String[] finished_lines = new String[100];
		
		int finished_lines_idx = 0;
		
		int rows = (idx+1)/2;
		
		for(int i=0;i<rows;i++)
		{
			
			while(lines[i].length()<45)
			{
				
				/**
				 * Add the spaces needed to make 2 columns work
				 */
				
				defaultprinter(" ", lines, i);
				
			}
			
			/**
			 * Concatenate each line of left column with it's corresponding right column
			 */
			
			finished_lines[finished_lines_idx] = lines[i] + lines[i+rows];
			
			finished_lines_idx ++;
			
		}
		
		finished_lines_idx --;
		
		idx = back_to_empty(lines);
		
		finished_lines_idx = sendgroup(cmds, finished_lines, finished_lines_idx, finaltxt);
		
		return idx;
		
	}
	
	/**
	 * @param cmds
	 * @param s
	 * @param lines
	 * @param idx
	 * @return
	 */
	
	static int smartprinter(int[] cmds, String s, String[] lines, int idx) // Print with correct left/right/center alignment
	{
		
		switch(cmds[1])
		{
		
			/**
			 * Center justify printing
			 */
			
			case 1: 
				
				idx = centerprinter(cmds, s, lines, idx);
				
				break;
				
			/**
			 * Center justify printing
			 */
				
			case 2:  
				idx = rightprinter(cmds, s, lines, idx);
				
				break;
				
			/**
			 * Equal justify printing
			 */
				
			case 3:
				
				idx = equalprinter(cmds, s, lines, idx);
				
				break;
				
			/**
			 * Left justify printing
			 */
				
			default: 
				
				idx = defaultprinter(s, lines, idx);
				
		}
		
		return idx;
		
	}
	 
	/**
	 * Left justify printing
	 * @param s
	 * @param lines
	 * @param idx
	 * @return
	 */
	
	static int defaultprinter(String s, String[] lines, int idx)
	{
		
		lines[idx] += s;
		
		return idx;
		
	}
	 
	/**
	 * Right justify printing
	 * @param cmds
	 * @param s
	 * @param lines
	 * @param idx
	 * @return
	 */
	
	static int rightprinter(int[] cmds, String s, String[] lines, int idx)
	{
		
		int spaces = (cmds[0] - s.length());
		
		for(int i=0;i<spaces;i++)
		{
			/**
			 * Print spaces to right justify text
			 */
			
			idx = defaultprinter(" ", lines, idx); 
		}
		
		/**
		 * Print the text
		 */
		
		idx = defaultprinter(s, lines, idx); 
		
		return idx;
		
	}
	 
	/**
	 * Center justify printing
	 * @param cmds
	 * @param s
	 * @param lines
	 * @param idx
	 * @return
	 */
	
	static int centerprinter(int[] cmds, String s, String[] lines, int idx)
	{
		
		int spaces = (cmds[0] - s.length())/2;
		
		for(int i=0;i<spaces;i++)
		{
			/**
			 * Print spaces to center text
			 */
		
			idx = defaultprinter(" ", lines, idx);  
			
		}
		
		/**
		 * Print the text
		 */
		
		idx = defaultprinter(s, lines, idx); 
		
		return idx;
		
	}
	 
	/**
	 * Equal spacing printer
	 * @param cmds
	 * @param s
	 * @param lines
	 * @param idx
	 * @return
	 */
	
	static int equalprinter(int[] cmds, String s, String[] lines, int idx)
	{
		
		/**
		 * split s into its words
		 */
		
		String[] words = s.split("\\s+"); 
		
		s = "";
		
		for(int i=0;i<words.length;i++)
		{
			
			s = s+ words[i] + " ";
			
		}
		
		s = s.trim();
		
		int num_words = words.length;
		
		int num_extra = cmds[0] - s.length();
		
		/**
		 * spaces per word (in addition to the normal 1 to achieve equal spacing
		 */
		
		int extra_spaces_per = num_extra/(Math.max(1,num_words-1)); 
		
		/**
		 * extra spaces to throw in to make it even
		 */
		
		int uneven_fillers = num_extra % (Math.max(1,num_words-1));  
		
		for(int i=0;i<num_words;i++)
		{
			
			/**
			 * print the next word
			 */
			
			idx = defaultprinter(words[i], lines, idx); 
			
			/**
			 * unless you made it to final word, print spaces
			 */
			
			if(i<(num_words-1))  
			{
				
				/**
				 * print the normal space
				 */
				
				idx = defaultprinter(" ", lines, idx);  
				
				for(int j=0;j<extra_spaces_per;j++)
				{
					
					/**
					 * print any extra spaces
					 */
					
					idx = defaultprinter(" ", lines, idx);  
					
				}
				
				if(uneven_fillers > 0)
				{
					
					/**
					 * if needed, print another filler space
					 */
					
					idx = defaultprinter(" ", lines, idx);  
					
					uneven_fillers--;
					
				}
				
			}
			
		}
		
		return idx;
		
	}
 
	/**
	 * Title printer
	 * @param cmds
	 * @param s
	 * @param lines
	 * @param idx
	 * @param messages
	 * @return
	 */
	
	static int titleprinterln(int[] cmds, String s, String[] lines, int idx, JTextArea messages)
	{
		/**
		 * if title length is valid
		 */
		
		if(s.length() <= cmds[0]) 
		{
			
			int spaces = (cmds[0] - s.length())/2;
			
			for(int i=0;i<spaces;i++)
			{
				
				/**
				 * Print spaces to center text
				 */
				
				defaultprinter(" ", lines, idx);  
				
			}
			
			/**
			 * Print the text
			 */
			
			defaultprinter(s, lines, idx); 
			
			/**
			 * Go to the next line
			 */
			
			idx ++;  
			
			for(int i=0;i<spaces;i++)
			{
				
				/**
				 * Print spaces to center underline
				 */
				
				defaultprinter(" ",lines, idx);  
				
			}
			
			for(int i=0;i<s.length();i++)
			{
				/**
				 * Print the underline
				 */
				
				defaultprinter("-",lines,idx);  
				
			}
			
			idx++;
			
			if(cmds[3]==2)
			{
				idx++;
			}
			
			/**
			 * Set the title attribute OFF
			 */
			
			cmds[4] = 0; 
			
		}
		else
		{
			
			errorprinter("Error: Title is too long", messages);
			
		}
		return idx;
	}
	
		/**
		 * "Blank line function" inserts blank lines
		 * whenever the command is given
		 * @param cmds
		 * @param lines
		 * @param idx
		 * @return
		 */
	
		static int blanklinehandler(int[] cmds, String[] lines, int idx)
		{
			
			if(lines[idx]!="")
			{
				idx++;
			}
			
			while(cmds[6]>0)
			{
				idx++;
				
				cmds[6]--;
				
			}
			
			cmds[8] = 0;
			
			return idx;
			
		}
		
	/**
	 * @param cmd
	 */
		
	static void defaultcmd(int[] cmd)
	{
		
		for(int i=0;i<9;i++)
		{
			
			cmd[i] = dflt[i];
			
		}
		
	}
	
	/**
	 * 
	 * @param lines
	 * @return
	 */
	
	static int back_to_empty(String[] lines)
	{
		
		for(int i=0;i<lines.length;i++)
		{
			
			lines[i] = "";
			
		}
		
		return 0;
	}
	
	/**
	 * @param cmds
	 * @param j
	 * @param sc
	 * @param lines
	 * @param idx
	 * @param messages
	 * @param finaltxt
	 * @return
	 */
	
	static int commandhandler(int[] cmds, String j, Scanner sc, String[] lines, int idx, JTextArea messages, ArrayList<String> finaltxt)
	{
		
		switch(j.charAt(1)) 
		{
		
			/**
			 * line length
			 */
			
			case 'n':
				
				if(cmds[7] == 2)
				{
					
					errorprinter("Error: Line length cannot be changed while in 2 column format", messages);
					
				}
				else try
				{
					
					/**
					 * convert String to int
					 */
					 
					int num = Integer.parseInt(j.substring(2));
					
				    if(num < 1 || num > 100)
				    {
				    	
				    	errorprinter("Error: Number of characters is not in valid range (1-100)", messages);
				    	
				    }
				    else
				    {
				    	
				    /**
				     * set line length attribute
				     */
				    	
				    cmds[0] = num;  
				    
				    }
				    
				} 
				
				catch (NumberFormatException nfe)
				{
					
				      errorprinter("Error: Please enter a valid number", messages);
				      
				}
				
				break;
				
			/**
			 * justification 
			 */
				
			case 'l':
				
				cmds[1] = 0;
				
				break;
				
			case 'c':
				
				cmds[1] = 1;
				
				/**
				 * no paragraph indentation when NOT left justified
				 */
				
				cmds[5] = 0; 
				
				break;
				
			case 'r':
				
				cmds[1] = 2;
				
				/**
				 * no paragraph indentation when NOT left justified
				 */
				
				cmds[5] = 0; 
				
				break;
				
			case 'e':
				
				cmds[1] = 3;
				
				/**
				 * no paragraph indentation when NOT left justified
				 */
				
				cmds[5] = 0; 
				
				break;
				
			/**
			 * wrap
			 */
				
			case 'w':
				
				if(j.charAt(2) == '+')
				{
					
					cmds[2] = 1;
					
				}
				else if(j.charAt(2) == '-')
				{
					cmds[2] = 0;
				}
				else
				{
					
					errorprinter("Error: Wrap must be '+' or '-'.", messages);
					
				}
				
				break;
				
			/**
			 * spacing
			 */
		 
			case 's':
				
				cmds[3] = 1;
				
				break;
				
			case 'd':
				
				cmds[3] = 2;
				
				break;
				
			/**
			 * title
			 */
				 	
			case 't':
				
				cmds[4] = 1;
				
				break;
				
			/**
			 * paragraph indentation
			 */
				
			case 'p':
				
				try
				{
				  
					/**
					 * convert String to int
					 */
					
					int num = Integer.parseInt(j.substring(2));
					
					/**
					 * if it is bigger than line length
					 */
					
				    if(num < 1 || num >= cmds[0]) 
				    {
				    	
				    	errorprinter("Error: Number of spaces is not in valid range (Less than line length)", messages);
				    	
				    }
				    else
				    {
				    
				    /**
				     * set paragraph indentation
				     */
				    	
				    cmds[5] = num;  
				    
				    }
				    
				} 
				
				catch (NumberFormatException nfe)
				{
					
				      errorprinter("Error: Please enter a valid number", messages);
				      
				}
				
				break;
			
			/**
			 * blank lines
			 */
				 
			case 'b':
				try
				{
					
					/**
					 * covert String to int
					 */
					
					int num = Integer.parseInt(j.substring(2));
					
				    if(num <= 1 || num >= 10)
				    {
				    	
				    	errorprinter("Error: Number of blank lines is not in valid range (0-10)", messages);
				    	
				    }
				    else
				    {
				    	
				    	cmds[6] = num;
				    	
				    	cmds[8] = 1;
				    	
				    }
				    
				} 
				catch (NumberFormatException nfe)
				{
					
				      errorprinter("Error: Please enter a valid number", messages);
				      
				}
				
				break;
			
				/**
				 * Columns
				 */
				
			case 'a':
				
				if(j.charAt(2) == '1')
				{
					
					/**
					 * if 2 columns were specified and it was changed to 1, print everything in 2 column fashion, change line length back to 80
					 */
					
					if(cmds[7] == 2)  
					{
						
						idx = sendgroup2columns(cmds, lines, idx, finaltxt);
							
						back_to_empty(lines);
						
						cmds[0] = 80;
						
					}
					
					cmds[7] = 1;
					
				}
				else if(j.charAt(2) == '2')
				{
					
					/**
					 * if 1 column was specified and it was changed to 2, print everything so far in 1 column fashion, change line length to 35
					 */
					
					if(cmds[7] == 1)  
					{	
						
						idx = sendgroup(cmds, lines, idx, finaltxt);
							
						back_to_empty(lines);
	
						cmds[0] = 35;
					}
					
					cmds[7] = 2;
					
				}
				else
				{
					
					errorprinter("Error: Invalid number of columns.  Please specify 1 or 2 columns.", messages);
					
				}
				
				break;
				
		}
		
		return idx;
		
	}
	
	/**
	 * 
	 * @param args
	 */
	
	public static void main(String[] args)
	{

		/**
		 * Creating the Frame
		 */
		
        JFrame frame = new JFrame("TextFormatterz");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(1024, 768);
        
        /**
         * Creating the panel at bottom and adding components
         * holds button panel and messages box and label
         */
        
        JPanel spanel = new JPanel(); 
        
        /**
         * holds the buttons
         */
        
        JPanel bpanel = new JPanel(); 
        
        /**
         * holds the messages text box
         */
        
        JPanel mespan = new JPanel(); 
        
        /**
         * holds the formatted text output box
         */
        
        JPanel outpan = new JPanel(); 
        
        /**
         * label for the Messages text box
         */
        
        JLabel label = new JLabel("Messages:"); 
        
        /**
         * Output text box
         */
        
        JTextArea output = new JTextArea(); 
        
        /**
         * Make it monospaced characters
         */
        
        output.setFont(new Font("monospaced", Font.PLAIN, 12));  
        
        /**
         * turns off editability of formatted output text box
         */
        
        output.setEditable(false); 
        
        /**
         * accepts up to 10 characters
         */
        
        JTextArea messages = new JTextArea(); 
        
        messages.setEditable(false);
        
        /**
         * Open file button
         */
        
        JButton open = new JButton("Open"); 
        
        /**
         * Save file button
         */
        
        JButton save = new JButton("Save As"); 
        
        /**
         * Sets the layouts of each of the three panels to BorderLayout for formatting purposes
         */
        
        mespan.setLayout(new BorderLayout());
        
        outpan.setLayout(new BorderLayout());
        
        spanel.setLayout(new BorderLayout());

        /**
         * Creates and sets up Vertical scroll bar to formatted output text box
         */
        
        JScrollPane oVert = new JScrollPane(output);
        
        oVert.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        /**
         * Creates and sets up Vertical scroll bar to messages text box
         */
        
        JScrollPane mVert = new JScrollPane(messages);
        
        mVert.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /**
         * Adds Messages text box and messages label to the mespan Panel
         */
        
        mespan.add(BorderLayout.CENTER, mVert);
        
        mespan.add(BorderLayout.NORTH, label);
        
        /**
         * Adds Open and Save buttons to the bpanel
         */
        
        bpanel.setLayout(new GridLayout(2,1));
        
        bpanel.add(open);
        
        bpanel.add(save);
        
        /**
         * Adds the button and messages panels to one single spanel
         */
        
        spanel.add(BorderLayout.WEST, bpanel);
        
        spanel.add(BorderLayout.CENTER, mespan);

        /**
         * Adding component panels to the JFrame
         */
        
        frame.add(BorderLayout.SOUTH, spanel);
        
        frame.add(BorderLayout.CENTER, oVert);
        
        /**
         * Sets a size for the components to keep them at a decent viewing ratio
         */
        
        bpanel.setPreferredSize(new Dimension(200,100));
        
        spanel.setPreferredSize(new Dimension(500,100));
        
        frame.setVisible(true);
        
        JFileChooser chooser = new JFileChooser();

        open.addActionListener(new ActionListener()
        {
        	
	        public void actionPerformed(ActionEvent e)
	        {
	        	
	            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files Only", "txt");
	            
	            chooser.setFileFilter(filter);
	            
	            int returnVal = chooser.showOpenDialog(null);
	            
	            if(returnVal == JFileChooser.APPROVE_OPTION) 
	            {
	            	
	            	/**
	            	 * Clear out the error message box each time a new file is formatted.
	            	 */
	            	
	               messages.selectAll();
	               
				   messages.replaceSelection("");
				   
	               errorprinter("You chose to open this file: " + chooser.getSelectedFile().getName(), messages);
	               
	               File f = chooser.getSelectedFile();
	               
	               errorprinter(f.getAbsolutePath()+"\n", messages);  
	               
	               /**
	                * ArrayList containing all the lines that are entered into the output file
	                */
	               
	               ArrayList<String> finaltxt = new ArrayList<String>();  
	               
	               Scanner sc;
	               
	               String[] lines = new String[100];
	               
	               int idx = back_to_empty(lines);
	               
	               
	               /**
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
	               
	               
					try 
					{
						
						sc = new Scanner(f);
						
						int[] cmds = new int[9];
						
						defaultcmd(cmds);
						
						while (sc.hasNextLine()) 
						{
							String j =sc.nextLine();
							
							/**
							 * Handle commands
							 * First handle blank lines
							 */
							
							if(cmds[8]==1)  
							{
								
								idx = blanklinehandler(cmds, lines, idx);
								
							}
							
							/**
							 * Then handle titles to be printed
							 */
							
							if (cmds[4] == 1)  
							{
								idx = titleprinterln(cmds,j, lines, idx, messages);
								
								/**
								 * Print an extra new line if doublespaced
								 */
								
								if (cmds[3] == 2)  
								{
									
									idx ++;
									
								}
								
							}
							else if (j.startsWith("-"))
							{
								
									idx = commandhandler(cmds, j, sc, lines, idx, messages, finaltxt);
									
							}
							else  
							{
								
								/**
								 * If paragraph indentation is valid, make the indentations
								 * Handle printing text
								 * Then handle all other formatting
								 */
								
								if( (cmds[7] == 1 && cmds[5]<cmds[0]) || (cmds[7] == 2 && cmds[5]<35) )  
								{
									
									int count = cmds[5];
									
									while(count > 0)
									{
										
										j = " "+ j;
										
										count--;
										
									}
									
								}
								else
								{
									errorprinter("Error: Paragraph indentation is too large", messages);
								}
								
								/**
								 * Then develop lines with wrapping.
								 */
								
							    if(cmds[2] == 1)  
								{
							    	
							    	/**
							    	 * While j still has characters to read, add them to the lines
							    	 */
							    	
									while(!j.isEmpty()) 
									{
										
										j = j.trim();
										
										/**
										 * lines[idx] = lines[idx].trim();
										 */
										
										int chars_remaining = cmds[0]-lines[idx].length();
										
										/**
										 * If there's space to hold the whole line, add it
										 */
										
										if(j.length()<chars_remaining)  
										{
											
											idx = smartprinter(cmds, j, lines, idx);
											
											idx++;
											
											if(cmds[3]==2)
											{
												
												idx++;
												
											}
											
											j = "";
											
										}
										else  
										{
											
											/**
											 * If there isn't space for the whole line, add what fits
											 */
											
											int index = j.indexOf(' ');
											
											String firstword;
											
											/**
											 * if there's more than one word
											 */
											
										    if (index > -1)  
										    {
										    	
										    	/**
										    	 * get the first word
										    	 */
										    	
										        firstword = j.substring(0, index).trim();  
										        
										        /**
										         * store the rest as j
										         */
										        
										        j = j.substring(index).trim();  
										        
										    } 
										    else 
										    {
										    	
										    	/**
										    	 * get the whole thing
										    	 */
										    	
										        firstword = j;  
										        
										        /**
										         * store the rest as j
										         */
										        
										        j = "";  
										        
										    }
										    
										    if (firstword.length()<chars_remaining)
										    {
										    	
												idx = smartprinter(cmds, firstword, lines, idx);
												
												idx++;
												
												if(cmds[3]==2)
												{
													
													idx++;
													
												}
												
												firstword = "";
												
										    }
										    else if(firstword.length()>cmds[0]) 
										    {
										    	
										    	/**
										    	 * If a word is too big, throw an error message.
										    	 */
										    	
										    	errorprinter("Error: A word is longer than the allowed line length.  Skipping line.", messages);
										    	
										    	break;
										    	
										    }
										    else 
										    {
										    	/**
										    	 * If the word doesn't fit, wrap up this line and move on to the next line in output
										    	 */
										    	
										    	idx++;
										    	
												if(cmds[3]==2)
												{
													
													idx++;
													
												}
												
										    	idx = smartprinter(cmds, firstword, lines, idx);
										    	
										    }
										    
										}
										
									}
									
								}
							    else 
							    {
							    	/**
							    	 * Develop lines with no wrapping
							    	 */
							    	
									while(j.length()>0)
									{
										
										if(j.length()<=cmds[0])
										{
											
											idx = smartprinter(cmds, j, lines, idx);
											
											idx++;
											
											if(cmds[3]==2)
											{
												idx++;
											}
											
											j = "";
											
										}
										else
										{
											int char_idx = Math.min(cmds[0]-1,j.length()-1);
											
											boolean tooLong = false;
											
											while(j.charAt(char_idx)!=' ')
											{
												
												char_idx--;
												
												if(char_idx<1)
												{
													
													tooLong = true;
													
													errorprinter("Warning: Word longer than max length, breaking up into segments.\n.", messages);
													
													char_idx = Math.min(cmds[0],j.length()-1);
													
													break;
													
												}
												
												tooLong = false;
												
											}
											
											String first_part = j.substring(0, char_idx);
											
											if(tooLong == false)
											{
												
												j = j.substring(char_idx+1, j.length());
												
											}
											else
											{
												
												j = j.substring(char_idx, j.length());
												
											}
											
											idx = smartprinter(cmds, first_part, lines, idx);
											
											idx++;
											
											if(cmds[3]==2)
											{
												idx++;
											}
											
										}
										
									}
									
							    }
							    
							}
							
						}
						
						/**
						 * Print any lines remaining.
						 */
						
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
						{
							
							errorprinter("Error", messages);
							
						}
						
						/**
						 * Write the formatted text to the file
						 */
						
						try
						{
							
			                 PrintWriter writer = new PrintWriter("output.txt");
			                 
			                 int j = 0;
			                 
			                 while (finaltxt.size() > j) 
			                 {
			                	 
			                    writer.print(finaltxt.get(j++));
			                    
			                 }
			                 
			                 writer.flush();
			                 
			                 writer.close();
			                 
			             } 
						
						catch (FileNotFoundException e1) 
						{
							e1.printStackTrace();
							
			                        System.exit(0) ; 
			                        
			             }
						
						/**
						 * Show a preview of the formatted text
						 */
						
						output.selectAll();
						
						/**
						 * Clear the preview window of leftovers
						 */
						
					    output.replaceSelection("");  
					    
					    File file = new File("output.txt");
					    
					    FileReader fr = new FileReader(file);
					    
					    BufferedReader br = new BufferedReader(fr);
					    
					    String line;
					    
					    while((line = br.readLine()) != null)
					    {
					    	
					        output.append(line);
					        
					        output.append("\n");
					        
					    }

					}
					
					catch (FileNotFoundException e1) 
					{
						
						errorprinter("Error, no file found", messages);  
						
					} 
					catch (IOException e1) 
					{
						
						/**
						 * TODO Auto-generated catch block
						 */
						
						e1.printStackTrace();
						
					}
					
				}   
	            
	        }
	        
        }
        
        		);
        
        
        /**
         * Adding Action Listener to Save Button
         */
        
        save.addActionListener(new ActionListener()
        {
        	
	        public void actionPerformed(ActionEvent e)
	        {
	        	
	        	String title = output.getText();
	        	
	        	FileWriter fw;
	        	
	        	File fts;
	        	
	        	chooser.setDialogTitle("Choose where to save the file:");
	        	
	            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files Only", "txt");
	            
	            chooser.setFileFilter(filter);
	            
	        	int userSelection = chooser.showSaveDialog(frame);
	        	
	        	if(userSelection == chooser.APPROVE_OPTION)
	        	{
	        		
		        		fts = chooser.getSelectedFile();
		        		
		        		/**
		        		 * messages.add("Saved file to: " fts.getAbsolutePath());
		        		 */
		        		
					try 
					{
						
						fw = new FileWriter(fts.getAbsolutePath() + ".txt");
						
						fw.write(title);
						
						fw.close();
						
					} 
					catch (IOException e1) 
					{
						
						/**
						 * TODO Auto-generated catch block
						 */
						
						e1.printStackTrace();
						
					}
					
	        	}
	        	
	        }
	        
        }
        
        		);
        
	}

}


