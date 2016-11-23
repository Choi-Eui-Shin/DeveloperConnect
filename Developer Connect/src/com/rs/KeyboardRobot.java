/**
 *
 * Property of IBM Korea, Copyrightⓒ. IBM Korea 2016 All Rights Reserved.
 *
 * @author: Choi Eui Shin
 *
 */
package com.rs;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Robot;

import javax.swing.JTextField;
import javax.swing.UIManager;

import org.json.JSONObject;

import com.ibm.iotf.client.app.ApplicationClient;
import com.ibm.iotf.client.app.Command;
import com.ibm.iotf.client.app.Event;
import com.ibm.iotf.client.app.EventCallback;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;
import java.awt.event.ActionEvent;

public class KeyboardRobot extends JFrame  implements EventCallback
{
	private JTextField txtOrg;
	private JTextField txtKey;
	private JTextField txtAuth;
	private JButton cmdConnect;
	private ApplicationClient myAppClient = null;
	private Robot robot;
	
	public KeyboardRobot()
	{
		setTitle("Keyboard Robot");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Org ID");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(20, 20, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		txtOrg = new JTextField();
		GridBagConstraints gbc_txtOrg = new GridBagConstraints();
		gbc_txtOrg.insets = new Insets(20, 0, 5, 20);
		gbc_txtOrg.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOrg.gridx = 1;
		gbc_txtOrg.gridy = 0;
		getContentPane().add(txtOrg, gbc_txtOrg);
		txtOrg.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("API Key");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 20, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		txtKey = new JTextField();
		GridBagConstraints gbc_txtKey = new GridBagConstraints();
		gbc_txtKey.insets = new Insets(0, 0, 5, 20);
		gbc_txtKey.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtKey.gridx = 1;
		gbc_txtKey.gridy = 1;
		getContentPane().add(txtKey, gbc_txtKey);
		txtKey.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("API Token");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 20, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		txtAuth = new JTextField();
		GridBagConstraints gbc_txtAuth = new GridBagConstraints();
		gbc_txtAuth.insets = new Insets(0, 0, 5, 20);
		gbc_txtAuth.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAuth.gridx = 1;
		gbc_txtAuth.gridy = 2;
		getContentPane().add(txtAuth, gbc_txtAuth);
		txtAuth.setColumns(10);
		
		cmdConnect = new JButton("CONNECT");
		cmdConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SwingWorker work = new SwingWorker() {
					@Override
					public Object construct() {
						cmdConnect.setEnabled(false);
						_connect();
						return null;
					}
				};
				
				work.start();
			}
		});
		GridBagConstraints gbc_cmdConnect = new GridBagConstraints();
		gbc_cmdConnect.gridwidth = 2;
		gbc_cmdConnect.insets = new Insets(5, 20, 0, 20);
		gbc_cmdConnect.gridx = 0;
		gbc_cmdConnect.gridy = 3;
		getContentPane().add(cmdConnect, gbc_cmdConnect);
	}

	/**
	 * 
	 */
	private void _connect()
	{
		try
		{
			Properties props = new Properties();
			
			props.put("id", "KeyboardRobot");
			props.put("Organization-ID", txtOrg.getText());
			props.put("Authentication-Method", "apikey");
			props.put("API-Key", txtKey.getText());
			props.put("Authentication-Token", txtAuth.getText());
			props.put("Device-Type", "RealDevice");
			props.put("Device-ID", "KeyboardRobot");
			props.put("Clean-Session", "true");
			
			myAppClient = new ApplicationClient(props);
			myAppClient.connect();
			myAppClient.setEventCallback(this);
			//System.out.println("@.@ Subscribing to ESP8266 device events..");
			myAppClient.subscribeToDeviceEvents("RealDevice", "ESP8266");
			
			robot = new Robot();
			
			System.out.println("@.@ Ready...");
		}catch(Exception ex) {
			ex.printStackTrace();
			cmdConnect.setEnabled(true);
		}		
	}
	
	@Override
	public void processEvent(Event evt)
	{
		try
		{
		    //System.out.println("@.@ Event => " + evt.getDeviceId() + " || " + evt.getEvent() + " || " + evt.getPayload().replace("\n", "").replaceAll("\r\n", ""));
			JSONObject payload = new JSONObject(evt.getPayload());
			JSONObject d = (JSONObject)payload.get("d");
	
			if ( d != null && robot != null )
			{
				int button = d.getInt("irButton");

				switch(button)
				{
				case 1:
					robot.keyPress(KeyEvent.VK_PAGE_UP);
					try{Thread.sleep(100);}catch(Exception ig){}
					robot.keyRelease(KeyEvent.VK_PAGE_UP);
					break;
					
				case 2:
					robot.keyPress(KeyEvent.VK_PAGE_DOWN);
					try{Thread.sleep(100);}catch(Exception ig){}
					robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
					break;
					
				case 3:
					robot.mousePress(InputEvent.BUTTON1_MASK);
					try{Thread.sleep(100);}catch(Exception ig){}
				    robot.mouseRelease(InputEvent.BUTTON1_MASK);
					break;
					
				case 4:
					robot.mousePress(InputEvent.BUTTON3_MASK);
					try{Thread.sleep(100);}catch(Exception ig){}
				    robot.mouseRelease(InputEvent.BUTTON3_MASK);
					break;					
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace(System.out);
		}		
	}

	@Override
	public void processCommand(Command cmd) {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception ex) {}
		
		KeyboardRobot robo = new KeyboardRobot();
		robo.setSize(350, 180);
		robo.setVisible(true);

	}
}
