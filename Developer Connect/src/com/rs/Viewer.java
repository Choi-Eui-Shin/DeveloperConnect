/**
 *
 * Property of IBM Korea, Copyrightⓒ. IBM Korea 2016 All Rights Reserved.
 *
 * @author: Choi Eui Shin
 *
 */
package com.rs;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONObject;

import com.base.table.DisplayCellRenderer;
import com.base.table.MyDefaultTable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.iotf.client.device.DeviceClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.rs.DriverService.DriverBehaviorDetail;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridLayout;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Viewer extends JPanel
{
	class DataEntry
	{
		public String dataFilename;
		public String fromDate;
		public String toDate;
		public String jobId;
		
		public String toString()
		{
			return dataFilename;
		}
	}
	
	private DefaultComboBoxModel<DataEntry> dataModel = new DefaultComboBoxModel<DataEntry>();
	
	private MapView mapView;
	private JTextField txtTenant;
	private JButton cmdResult;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JTextField txtUser;
	private JTextField txtPasswd;
	private JTabbedPane tabbedPane;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JComboBox cbData;
	private JButton cmdSend;
	private JProgressBar progressBar;
	private JLabel lblNewLabel_7;
	private JLabel lblNewLabel_8;
	private JLabel lblNewLabel_9;
	private JLabel lblNewLabel_10;
	private JTextField txtOrg;
	private JTextField txtDevType;
	private JTextField txtDevId;
	private JTextField txtAuthtok;
	private JLabel lblNewLabel_11;
	private JLabel lblNewLabel_12;
	private JLabel lblNewLabel_13;
	private JTextField txtUser2;
	private JTextField txtPasswd2;
	private JTextField txtTenant2;
	private JTextField txtStatus;
	private Progress progress;
	private JComboBox cbResult;
	private JPanel pnMap;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private JTable table;
	private BehaviorTableModel model;
	private ScoreTableModel scoreModel;
	private JPanel panel_4;
	private JScrollPane scrollPane_1;
	private JTable scoreTable;
	private JPanel panel_5;
	
	public Viewer() {
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		add(tabbedPane, BorderLayout.CENTER);
		
		panel_3 = new JPanel();
		tabbedPane.addTab("Probe Data", null, panel_3, null);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		lblNewLabel_7 = new JLabel("Organization ID");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_7.insets = new Insets(20, 10, 5, 5);
		gbc_lblNewLabel_7.gridx = 0;
		gbc_lblNewLabel_7.gridy = 0;
		panel_3.add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		txtOrg = new JTextField();
		GridBagConstraints gbc_txtOrg = new GridBagConstraints();
		gbc_txtOrg.gridwidth = 2;
		gbc_txtOrg.insets = new Insets(20, 0, 5, 5);
		gbc_txtOrg.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOrg.gridx = 1;
		gbc_txtOrg.gridy = 0;
		panel_3.add(txtOrg, gbc_txtOrg);
		txtOrg.setColumns(10);
		
		lblNewLabel_8 = new JLabel("Device Type");
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_8.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel_8.gridx = 0;
		gbc_lblNewLabel_8.gridy = 1;
		panel_3.add(lblNewLabel_8, gbc_lblNewLabel_8);
		
		txtDevType = new JTextField();
		txtDevType.setText("CAR");
		GridBagConstraints gbc_txtDevType = new GridBagConstraints();
		gbc_txtDevType.gridwidth = 2;
		gbc_txtDevType.insets = new Insets(0, 0, 5, 5);
		gbc_txtDevType.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDevType.gridx = 1;
		gbc_txtDevType.gridy = 1;
		panel_3.add(txtDevType, gbc_txtDevType);
		txtDevType.setColumns(10);
		
		lblNewLabel_9 = new JLabel("Device ID");
		GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
		gbc_lblNewLabel_9.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_9.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel_9.gridx = 0;
		gbc_lblNewLabel_9.gridy = 2;
		panel_3.add(lblNewLabel_9, gbc_lblNewLabel_9);
		
		txtDevId = new JTextField();
		txtDevId.setText("MyCar");
		GridBagConstraints gbc_txtDevId = new GridBagConstraints();
		gbc_txtDevId.gridwidth = 2;
		gbc_txtDevId.insets = new Insets(0, 0, 5, 5);
		gbc_txtDevId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDevId.gridx = 1;
		gbc_txtDevId.gridy = 2;
		panel_3.add(txtDevId, gbc_txtDevId);
		txtDevId.setColumns(10);
		
		lblNewLabel_10 = new JLabel("Authentication Token");
		GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
		gbc_lblNewLabel_10.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_10.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel_10.gridx = 0;
		gbc_lblNewLabel_10.gridy = 3;
		panel_3.add(lblNewLabel_10, gbc_lblNewLabel_10);
		
		txtAuthtok = new JTextField();
		txtAuthtok.setText("1234567890");
		GridBagConstraints gbc_txtAuthtok = new GridBagConstraints();
		gbc_txtAuthtok.gridwidth = 2;
		gbc_txtAuthtok.insets = new Insets(0, 0, 5, 5);
		gbc_txtAuthtok.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAuthtok.gridx = 1;
		gbc_txtAuthtok.gridy = 3;
		panel_3.add(txtAuthtok, gbc_txtAuthtok);
		txtAuthtok.setColumns(10);
		
		lblNewLabel_11 = new JLabel("User ID");
		GridBagConstraints gbc_lblNewLabel_11 = new GridBagConstraints();
		gbc_lblNewLabel_11.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_11.insets = new Insets(20, 0, 5, 5);
		gbc_lblNewLabel_11.gridx = 0;
		gbc_lblNewLabel_11.gridy = 4;
		panel_3.add(lblNewLabel_11, gbc_lblNewLabel_11);
		
		txtUser2 = new JTextField();
		GridBagConstraints gbc_txtUser2 = new GridBagConstraints();
		gbc_txtUser2.gridwidth = 2;
		gbc_txtUser2.insets = new Insets(20, 0, 5, 5);
		gbc_txtUser2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUser2.gridx = 1;
		gbc_txtUser2.gridy = 4;
		panel_3.add(txtUser2, gbc_txtUser2);
		txtUser2.setColumns(10);
		
		lblNewLabel_12 = new JLabel("Password");
		GridBagConstraints gbc_lblNewLabel_12 = new GridBagConstraints();
		gbc_lblNewLabel_12.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_12.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_12.gridx = 0;
		gbc_lblNewLabel_12.gridy = 5;
		panel_3.add(lblNewLabel_12, gbc_lblNewLabel_12);
		
		txtPasswd2 = new JTextField();
		GridBagConstraints gbc_txtPasswd2 = new GridBagConstraints();
		gbc_txtPasswd2.gridwidth = 2;
		gbc_txtPasswd2.insets = new Insets(0, 0, 5, 5);
		gbc_txtPasswd2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPasswd2.gridx = 1;
		gbc_txtPasswd2.gridy = 5;
		panel_3.add(txtPasswd2, gbc_txtPasswd2);
		txtPasswd2.setColumns(10);
		
		lblNewLabel_13 = new JLabel("Tenant ID");
		GridBagConstraints gbc_lblNewLabel_13 = new GridBagConstraints();
		gbc_lblNewLabel_13.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_13.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_13.gridx = 0;
		gbc_lblNewLabel_13.gridy = 6;
		panel_3.add(lblNewLabel_13, gbc_lblNewLabel_13);
		
		txtTenant2 = new JTextField();
		GridBagConstraints gbc_txtTenant2 = new GridBagConstraints();
		gbc_txtTenant2.gridwidth = 2;
		gbc_txtTenant2.insets = new Insets(0, 0, 5, 5);
		gbc_txtTenant2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTenant2.gridx = 1;
		gbc_txtTenant2.gridy = 6;
		panel_3.add(txtTenant2, gbc_txtTenant2);
		txtTenant2.setColumns(10);
		
		lblNewLabel_5 = new JLabel("Data File");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(20, 10, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 7;
		panel_3.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		cbData = new JComboBox();
		cbData.setModel(dataModel);
		GridBagConstraints gbc_cbData = new GridBagConstraints();
		gbc_cbData.gridwidth = 2;
		gbc_cbData.insets = new Insets(20, 0, 5, 5);
		gbc_cbData.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbData.gridx = 1;
		gbc_cbData.gridy = 7;
		panel_3.add(cbData, gbc_cbData);
		
		cmdSend = new JButton("Send");
		cmdSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final DataEntry de = (DataEntry)cbData.getSelectedItem();
				if ( de != null )
				{
					SwingWorker work = new SwingWorker() {
						
						@Override
						public Object construct() {
							cmdSend.setEnabled(false);
							_sendProbeData(de);
							cmdSend.setEnabled(true);
							return null;
						}
					};
					
					work.start();
				}
			}
		});
		GridBagConstraints gbc_cmdSend = new GridBagConstraints();
		gbc_cmdSend.fill = GridBagConstraints.BOTH;
		gbc_cmdSend.gridheight = 2;
		gbc_cmdSend.insets = new Insets(20, 0, 5, 20);
		gbc_cmdSend.gridx = 3;
		gbc_cmdSend.gridy = 7;
		panel_3.add(cmdSend, gbc_cmdSend);
		
		lblNewLabel_6 = new JLabel("진행");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_6.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 8;
		panel_3.add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(146, 20));
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 8;
		panel_3.add(progressBar, gbc_progressBar);
		
		txtStatus = new JTextField();
		txtStatus.setColumns(10);
		GridBagConstraints gbc_txtStatus = new GridBagConstraints();
		gbc_txtStatus.insets = new Insets(0, 0, 20, 5);
		gbc_txtStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStatus.gridx = 1;
		gbc_txtStatus.gridy = 9;
		panel_3.add(txtStatus, gbc_txtStatus);
		
		progress = new Progress();
		progress.setPreferredSize(new Dimension(146, 20));
		GridBagConstraints gbc_progress = new GridBagConstraints();
		gbc_progress.fill = GridBagConstraints.BOTH;
		gbc_progress.insets = new Insets(0, 0, 20, 5);
		gbc_progress.gridx = 2;
		gbc_progress.gridy = 9;
		panel_3.add(progress, gbc_progress);
		
		panel_2 = new JPanel();
		tabbedPane.addTab("Analysis", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_2.add(panel, BorderLayout.NORTH);
		panel.setBorder(new TitledBorder(null, "\uBD84\uC11D", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblNewLabel_4 = new JLabel("User ID");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(10, 10, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 0;
		panel.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		txtUser = new JTextField();
		GridBagConstraints gbc_txtUser = new GridBagConstraints();
		gbc_txtUser.insets = new Insets(10, 0, 5, 5);
		gbc_txtUser.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUser.gridx = 1;
		gbc_txtUser.gridy = 0;
		panel.add(txtUser, gbc_txtUser);
		txtUser.setColumns(10);
		
		lblNewLabel_3 = new JLabel("Password");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		txtPasswd = new JTextField();
		GridBagConstraints gbc_txtPasswd = new GridBagConstraints();
		gbc_txtPasswd.insets = new Insets(0, 0, 5, 5);
		gbc_txtPasswd.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPasswd.gridx = 1;
		gbc_txtPasswd.gridy = 1;
		panel.add(txtPasswd, gbc_txtPasswd);
		txtPasswd.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Tenant ID");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		txtTenant = new JTextField();
		GridBagConstraints gbc_txtTenant = new GridBagConstraints();
		gbc_txtTenant.insets = new Insets(0, 0, 5, 5);
		gbc_txtTenant.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTenant.gridx = 1;
		gbc_txtTenant.gridy = 2;
		panel.add(txtTenant, gbc_txtTenant);
		txtTenant.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Job ID");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 10, 10, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 3;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		cbResult = new JComboBox();
		cbResult.setModel(dataModel);

		cmdResult = new JButton("Result");
		cmdResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				final DataEntry de = (DataEntry)cbResult.getSelectedItem();
				if ( de != null ) {
					SwingWorker work = new SwingWorker() {
						
						@Override
						public Object construct() {
							cmdResult.setEnabled(false);
							_showResult(de);
							cmdResult.setEnabled(true);
							return null;
						}
					};
					
					work.start();					
				}
			}
		});
		
		GridBagConstraints gbc_cbResult = new GridBagConstraints();
		gbc_cbResult.insets = new Insets(0, 0, 10, 5);
		gbc_cbResult.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbResult.gridx = 1;
		gbc_cbResult.gridy = 3;
		panel.add(cbResult, gbc_cbResult);
		GridBagConstraints gbc_cmdResult = new GridBagConstraints();
		gbc_cmdResult.insets = new Insets(0, 0, 10, 10);
		gbc_cmdResult.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmdResult.gridx = 2;
		gbc_cmdResult.gridy = 3;
		panel.add(cmdResult, gbc_cmdResult);
		
		pnMap = new JPanel();
		panel_2.add(pnMap, BorderLayout.CENTER);
		pnMap.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\uACB0\uACFC", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnMap.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		tabbedPane.addTab("Behavior", null, panel_1, null);
		panel_1.setLayout(new GridLayout(2, 1, 0, 0));
		
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Score", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		scrollPane_1 = new JScrollPane();
		panel_4.add(scrollPane_1, BorderLayout.CENTER);
		
		scoreTable = getScoreTable(); //new JTable();
		scrollPane_1.setViewportView(scoreTable);
		
		panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Behavior", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel_5.add(scrollPane, BorderLayout.CENTER);
		
		table = getTable(); //new JTable();
		scrollPane.setViewportView(table);
		
		init();
	}
	
	/**
	 * 
	 */
	private void init()
	{
		DataEntry de = new DataEntry();
		de.dataFilename = "GPS160311-000000.json";
		de.fromDate = "2016-03-11";
		de.toDate = "2016-03-11";
		de.jobId = "";
		
		dataModel.addElement(de);
		
		de = new DataEntry();
		de.dataFilename = "GPS161120-193939.json";
		de.fromDate = "2016-11-20";
		de.toDate = "2016-11-20";
		de.jobId = "";
		
		dataModel.addElement(de);
		
		de = new DataEntry();
		de.dataFilename = "GPS161122-070733.json";
		de.fromDate = "2016-11-22";
		de.toDate = "2016-11-22";
		de.jobId = "";
		
		dataModel.addElement(de);
		
		de = new DataEntry();
		de.dataFilename = "GPS161123-191838.json";
		de.fromDate = "2016-11-23";
		de.toDate = "2016-11-23";
		de.jobId = "";
		
		dataModel.addElement(de);
	}
	
	/**
	 * @return
	 */
	private JTable getScoreTable()
	{
		if (scoreTable == null )
		{
            int[] colWidth = {300, 100, 100};
            int[] align = {JLabel.LEFT, JLabel.RIGHT, JLabel.RIGHT};
            String [] colHeader = {"Behavior Name", "Score", "Total Time"};

            TableCellRenderer [] cren = {
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
            };
            
            scoreModel = new ScoreTableModel();
            scoreModel.setColumnIdentifiers(colHeader);

            scoreTable = new MyDefaultTable();
            scoreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scoreTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            scoreTable.setModel(scoreModel);
            scoreTable.setToolTipText(null);

            TableColumn column = null;

            for (int i = 0; i < colHeader.length; i++) {
                if ( cren[i] instanceof DisplayCellRenderer ) {
                    ((DisplayCellRenderer)cren[i]).setBackground(Color.white);
                    ((DisplayCellRenderer)cren[i]).setHorizontalAlignment(align[i]);
                }

                column = new TableColumn(i, colWidth[i], cren[i],  null);
                scoreTable.addColumn(column);
            }			
		}
		
		return scoreTable;
	}
	
	/**
	 * @return
	 */
	private JTable getTable()
	{
		if ( table == null )
		{
            int[] colWidth = {100, 100, 100, 100, 100, 100, 100};
            int[] align = {JLabel.LEFT, JLabel.RIGHT, JLabel.RIGHT, JLabel.RIGHT, JLabel.RIGHT, JLabel.CENTER, JLabel.CENTER};
            String [] colHeader = {"Behavior Name", "시작 위도", "시작 경도", "끝 위도", "종료 경도", "시작 시간", "종료 시간"};

            //behaviorName: Harsh braking, startLatitude: 30.3981933, startLongitude: -97.7498869, endLatitude: 30.3981933, endLongitude: -97.7498869, startTime: 1457703865000, endTime: 1457703866000
            
            TableCellRenderer [] cren = {
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
                new DisplayCellRenderer(),
            };
            
            model = new BehaviorTableModel();
            model.setColumnIdentifiers(colHeader);

            table = new MyDefaultTable();
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            table.setModel(model);
            table.setToolTipText(null);

            TableColumn column = null;

            for (int i = 0; i < colHeader.length; i++) {
                if ( cren[i] instanceof DisplayCellRenderer ) {
                    ((DisplayCellRenderer)cren[i]).setBackground(Color.white);
                    ((DisplayCellRenderer)cren[i]).setHorizontalAlignment(align[i]);
                }

                column = new TableColumn(i, colWidth[i], cren[i],  null);
                table.addColumn(column);
            }
		}
		
		return table;
	}
	
	/**
	 * @param de
	 */
	private void _sendProbeData(DataEntry de)
	{
		Properties deviceProps = new Properties();
		deviceProps.setProperty("Organization-ID", txtOrg.getText());
		deviceProps.setProperty("Device-Type", txtDevType.getText());
		deviceProps.setProperty("Device-ID", txtDevId.getText());
		deviceProps.setProperty("Authentication-Method", "token");
		deviceProps.setProperty("Authentication-Token", txtAuthtok.getText());
		deviceProps.setProperty("Clean-Session", "true");
		
		String inputFilename = "res/" + de.dataFilename;

		DeviceClient deviceClient = null;
		BufferedReader br = null;
		try {
			deviceClient = new DeviceClient(deviceProps);
			deviceClient.connect();
			
			br = new BufferedReader(new FileReader(inputFilename));
			JsonParser parsor = new JsonParser();
			JsonArray jsonArray = parsor.parse(br).getAsJsonArray();
			
			progressBar.setValue(0);
			progressBar.setMaximum(jsonArray.size());
			
			for (int i = 0; i < jsonArray.size(); i++)
			{
				JsonObject jsonObject = (JsonObject) jsonArray.get(i); 
				boolean status = deviceClient.publishEvent("load", jsonObject);
				
				progressBar.setValue(i+1);
				//System.out.println(jsonObject);
				Thread.sleep(100);
				if (!status) {
					throw new Exception("Failed to publish the event: " + jsonObject);
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try{br.close();}catch(Exception ig){}
			}
			
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try
					{
						Thread.sleep(1000);
					}catch(Exception ex) {}
					progressBar.setValue(0);
				}
			};
			
			(new Thread(r)).start();
		}	
		
		
		/*
		 * 분석 요청
		 */
		String jobId = null;
		String tenId = txtTenant2.getText();
		String userId = txtUser2.getText();
		String password = txtPasswd2.getText();
		
		try {
			HttpResponse<JsonNode> response = null;
			JsonNode rs = null;
			JSONObject req = null;
			
			JSONObject payload = new JSONObject();
			payload.put("from", de.fromDate);
			payload.put("to", de.toDate);
			
			response = Unirest.post("https://automotive.internetofthings.ibmcloud.com/driverinsights/jobcontrol/job?tenant_id=" + tenId)
					  .basicAuth(userId, password)
					  .header("accept", "application/json")
					  .header("content-type", "application/json")
					  .body(payload)
					  .asJson();
			
			rs = response.getBody();
			req = rs.getObject();
			de.jobId = req.getString("job_id");
			jobId = de.jobId;

			/*
			 * 작업이 끝나기를 기다린다.
			 */
			progress.start();
			boolean ok = false;
			do
			{
				response = Unirest.get("https://automotive.internetofthings.ibmcloud.com/driverinsights/jobcontrol/job?tenant_id=" + tenId + "&job_id=" + jobId)
						.basicAuth(userId, password)
						.header("accept", "application/json")
						.header("content-type", "application/json")
						.asJson();
				
				rs = response.getBody();
				req = rs.getObject();
				String st = req.getString("job_status");
				txtStatus.setText(st);
				ok = "SUCCEEDED".equals(st) || "KILLED".equals(st);
				if ( !ok )
					Thread.sleep(1000);
			}while(ok == false);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally{
			progress.stop();
		}		
	}
	
	/**
	 * 
	 */
	private void _showResult(DataEntry de)
	{
		if ( de.jobId != null )
		{
			if ( mapView != null ) {
				pnMap.remove(mapView);
				pnMap.validate();
				mapView = null;
			}
			
			mapView = new MapView();
			pnMap.add(mapView, BorderLayout.CENTER);
			pnMap.validate();
			List<DriverBehaviorDetail> list = mapView.prepare(de.dataFilename, de.jobId);
			
			model.clear();
			scoreModel.clear();
			
			for(DriverBehaviorDetail d : list) {
				model.addBehavior(d);
				scoreModel.addBehavior(d);
			}
			
			model.fireTableDataChanged();
			scoreModel.fireTableDataChanged();
			
			mapView.init();
		}
		else {
			showMessage("분석 결과가 없습니다.", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * @param msg
	 * @param type
	 */
	private void showMessage(String msg, int type)
	{
		JOptionPane.showMessageDialog(this, msg, "Viewer", type);
	}
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
		// os.name
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception ex) {}

		
		JFrame main = new JFrame("Driver Behavior");
		//main.setAlwaysOnTop(true);
		main.setContentPane(new Viewer());
		main.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		main.pack();
		main.setSize(1000, 700);
		main.show();
	}		

}
