package com.ingloriouscoders.sheepdebugger.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;

import javax.swing.*;
import javax.swing.border.Border;

import java.util.*;
import java.io.*;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class SheepDebugger extends JFrame implements ItemListener, ActionListener, MessageListener {
	
	public Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	private JPanel panelLeft, panelRight, panelUp;
	
	public JTextField msg_1, subj_1, rec_1;
	private JCheckBox internal_1, timestamp_1;
	private JLabel descr_1v0, descr_1v1, descr_1v2, descr_1v3;
	private JButton send_1;
	
	public JTextField rec_2, msg_2, subj_2;
	public JLabel descr_2v0, descr_2v1, descr_2v2, descr_2v3;
	
	private JLabel descr_usrname, descr_usrname_pre, descr_passwrd;
	public JTextField usrname, passwrd;
	public JButton login;
	
	public boolean internal, timestamp;
	public int messageCount = 0;
	
	private String confUsername = "", confPassword = "", confRecipient = "";
	
	XMPPConnection connection;
	
	public static void main(String[] args) {
		XMPPConnection.DEBUG_ENABLED = true;

		SheepDebugger dbg = new SheepDebugger("SheepDebugger");
		dbg.setVisible(true);
	}

	public SheepDebugger(String title) {
		super(title);
		this.setSize(800,400);
		this.setResizable(false);
		this.initGUI();
		
		for(int i=0;i<panelLeft.getComponentCount();i++) {
			panelLeft.getComponent(i).setEnabled(false);
		}
		for(int i=0;i<panelRight.getComponentCount();i++) {
			panelRight.getComponent(i).setEnabled(false);
		}
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void initGUI() {
		panelUp = new JPanel(new FlowLayout());
		panelUp.setBorder(BorderFactory.createTitledBorder(loweredbevel, "Login/Logout"));
		descr_usrname = new JLabel("Benutzername: ");
		panelUp.add(descr_usrname);
		usrname = new JTextField(15);
		panelUp.add(usrname);
		descr_usrname_pre = new JLabel("@googlemail.com   |");
		panelUp.add(descr_usrname_pre);
		descr_passwrd = new JLabel("  Passwort: ");
		panelUp.add(descr_passwrd);
		passwrd = new JTextField(15);
		panelUp.add(passwrd);
		login = new JButton("Login");
		login.addActionListener(this);
		panelUp.add(login);
		
		
		panelLeft = new JPanel(new GridLayout(10,0));
		panelLeft.setPreferredSize(new Dimension(this.getWidth()/2,this.getHeight()));
		panelLeft.setBorder(BorderFactory.createTitledBorder(loweredbevel, "Senden"));
		
		descr_1v0 = new JLabel("Empfänger:");
		panelLeft.add(descr_1v0);
		rec_1 = new JTextField();
		panelLeft.add(rec_1);
		
		descr_1v1 = new JLabel("Nachricht:");
		panelLeft.add(descr_1v1);
		msg_1 = new JTextField();
		panelLeft.add(msg_1);
		
		descr_1v2 = new JLabel("Subject:");
		panelLeft.add(descr_1v2);
		subj_1 = new JTextField();
		panelLeft.add(subj_1);
		descr_1v3 = new JLabel(" ");
		panelLeft.add(descr_1v3);
		
		internal_1 = new JCheckBox("Interne Nachricht");
		internal_1.addItemListener(this);
		panelLeft.add(internal_1);
		
		timestamp_1 = new JCheckBox("Timestamp an das Subject anhängen");
		timestamp_1.addItemListener(this);
		panelLeft.add(timestamp_1);
		
		send_1 = new JButton("Senden");
		send_1.setAlignmentX(CENTER_ALIGNMENT);
		send_1.addActionListener(this);
		panelLeft.add(send_1);
		
		
		
		panelRight = new JPanel(new GridLayout(10,0));
		panelRight.setPreferredSize(new Dimension(this.getWidth()/2,this.getHeight()));
		panelRight.setBorder(BorderFactory.createTitledBorder(loweredbevel, "Empfangen"));
		
		descr_2v0 = new JLabel("Absender:");
		panelRight.add(descr_2v0);
		rec_2 = new JTextField();
		rec_2.setEditable(false);
		panelRight.add(rec_2);
		descr_2v1 = new JLabel("Nachricht:");
		panelRight.add(descr_2v1);
		msg_2 = new JTextField();
		msg_2.setEditable(false);
		panelRight.add(msg_2);
		descr_2v2 = new JLabel("Subject:");
		panelRight.add(descr_2v2);
		subj_2 = new JTextField();
		subj_2.setEditable(false);
		panelRight.add(subj_2);
		descr_2v3 = new JLabel("Keine Nachricht empfangen", SwingConstants.CENTER);
		panelRight.add(descr_2v3);
		
		try {
			String[] config = readConfig();
			
			this.usrname.setText(config[2]);
			this.passwrd.setText(config[3]);
			this.rec_1.setText(config[4]);
		} catch (IOException e) {
			System.out.println("Keine Config-Datei vorhanden. Erstelle neue Datei.");
			String[] emptyArg = {"~Config file for SheepDebugger~","~Manual modifications may cause a crash~"};
			try {
				writeConfig(emptyArg);
			} catch (IOException e1) {
				System.out.println("Konnte Config-Datei nicht erstellen.");
				e1.printStackTrace();
			}
		}
		
		
		this.getContentPane().add(panelUp, BorderLayout.NORTH);
		this.getContentPane().add(panelLeft, BorderLayout.WEST);
		this.getContentPane().add(panelRight, BorderLayout.EAST);
	}
	
	  public void writeConfig(String[] arguments) throws IOException  {
		    Writer out = new OutputStreamWriter(new FileOutputStream("config.txt"));
		    try {
		      for (int i=0;i<arguments.length;i++) {	
		    	  out.write(arguments[i] + "\n");
		      }
		    }
		    finally {
		      out.close();
		    }
	  }
	  
	  public void writeSheepConfig() {
		    String[] sheepArguments = {"~Config file for SheepDebugger~", "~Manual modifications may cause a crash~", this.confUsername, this.confPassword, this.confRecipient};
		    try {
				writeConfig(sheepArguments);
			} catch (IOException e) {
				System.out.println("Konnte Eintrag in der Config-Datei nicht erstellen.");
				e.printStackTrace();
			}
	  }
		
	  public String[] readConfig() throws IOException {
		String[] config = new String[10];
		int line = 0;
		
	    Scanner scanner = new Scanner(new FileInputStream("config.txt"));
	    try {
	      while (scanner.hasNextLine()){
	    	config[line] = scanner.nextLine();
	    	
	        line++;
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    return config;
	  }
	
	public void addTimestampRunnable() {
		Thread thread = new Thread()
		{
		    @Override
		    public void run() {
		        try {
		            while(timestamp) {
		            	Calendar c = Calendar.getInstance();
		            	//String timestampStr = "&time=" + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + ";" + Integer.toString(c.get(Calendar.MONTH) + 1) + ";" + Integer.toString(c.get(Calendar.YEAR)) + ";" + Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ";" + Integer.toString(c.get(Calendar.MINUTE)) + ";" + Integer.toString(c.get(Calendar.SECOND));
		            	String timestampStr = "&time=" + String.valueOf(c.getTimeInMillis());
		            	descr_1v3.setText(timestampStr);
		            	sleep(100);
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    }
		};

		thread.start();
	}

	@Override
	public void itemStateChanged(ItemEvent ievt) {
		if(ievt.getSource() == internal_1) {
			this.internal = internal_1.isSelected();
			
			if (this.internal) {
				msg_1.setEnabled(false);
				msg_1.setText("");
			} else {
				msg_1.setEnabled(true);
			}
		}
		
		if(ievt.getSource() == timestamp_1) {
			this.timestamp = timestamp_1.isSelected();
			
			if (this.timestamp) {
				timestamp = true;
				addTimestampRunnable();
			} else {
				timestamp = false;
				descr_1v3.setText("");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent aevt) {
		if (aevt.getSource() == login) {
			if (connection != null) {
				if (!connection.isConnected()) {
					try {
						this.login(usrname.getText(), passwrd.getText());
					} catch (XMPPException e) {
						e.printStackTrace();
					}
					login.setText("Logout");
					
					for(int i=0;i<panelLeft.getComponentCount();i++) {
						panelLeft.getComponent(i).setEnabled(true);
					}
					
					if (this.internal) {
						msg_1.setEnabled(false);
						msg_1.setText("");
					}
					
					for(int i=0;i<panelRight.getComponentCount();i++) {
						panelRight.getComponent(i).setEnabled(true);
					}
				}
				
				if (connection.isConnected()) {
					this.disconnect();
					login.setText("Login");
					
					for(int i=0;i<panelLeft.getComponentCount();i++) {
						panelLeft.getComponent(i).setEnabled(false);
					}
					
					if (this.internal) {
						msg_1.setEnabled(false);
						msg_1.setText("");
					}
					
					for(int i=0;i<panelRight.getComponentCount();i++) {
						panelRight.getComponent(i).setEnabled(true);
					}
				}
			} else {
				try {
					this.login(usrname.getText(), passwrd.getText());
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				login.setText("Logout");
				
				for(int i=0;i<panelLeft.getComponentCount();i++) {
					panelLeft.getComponent(i).setEnabled(true);
				}
				
				for(int i=0;i<panelRight.getComponentCount();i++) {
					panelRight.getComponent(i).setEnabled(true);
				}
				
				if (this.internal) {
					msg_1.setEnabled(false);
					msg_1.setText("");
				}
			}
			
			this.confUsername = usrname.getText();
			this.confPassword = passwrd.getText();
			writeSheepConfig();
		}
		
		if (aevt.getSource() == send_1) {
			try {
				this.sendMessage(msg_1.getText(), rec_1.getText());
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			
			this.confRecipient = rec_1.getText();
			writeSheepConfig();
		}
	}
	
    public void login(String userName, String password) throws XMPPException {
	    ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "SHEEP_DESKTOP1");
	    connection = new XMPPConnection(config);
	 
	    connection.connect();
	    connection.login(userName + "@googlemail.com", password);
    }
 
    
    public void sendMessage(String message, String to) throws XMPPException {
	    Chat chat = connection.getChatManager().createChat(to, this);
	    
	    Message msg = new Message();
	    msg.setBody(message);
	    
	    String subjString = subj_1.getText();
	    if (this.timestamp)
	    	subjString = subjString + descr_1v3.getText();
	    
	    msg.setSubject(subjString);
	    
	    chat.sendMessage(msg);
    }
 
    public void disconnect() {
	    connection.disconnect();
    }
 
    public void processMessage(Chat chat, Message message) {
    	msg_2.setText(message.getBody());
    	subj_2.setText(message.getSubject());
    	rec_2.setText(message.getFrom());
    	
    	messageCount++;
    	
    	if (messageCount == 1)
    		descr_2v3.setText(messageCount + " Nachricht empfangen");
    	else
    		descr_2v3.setText(messageCount + " Nachrichten empfangen");
    	
    	panelRight.validate();
    }
}