package com.syrus.util.sms.ui;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.security.cert.*;
import javax.net.ssl.*;
import javax.swing.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 */
final class JSMSSendFrame extends javax.swing.JFrame {
	private static final int DEFAULT_SMTP_PORT = 25;

	private static final int DEFAULT_HTTP_PORT = 80;

	private static final int DEFAULT_HTTPS_PORT = 443;

	public JSMSSendFrame() {
		initComponents();
	}
	
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		jConnectionSettingsPanel1 = new javax.swing.JPanel();
		jProtocolLabel1 = new javax.swing.JLabel();
		jProtocolComboBox1 = new javax.swing.JComboBox();
		jHostLabel2 = new javax.swing.JLabel();
		jHostTextField1 = new javax.swing.JTextField();
		jPortLabel3 = new javax.swing.JLabel();
		jPortTextField2 = new javax.swing.JTextField();
		jFileLabel4 = new javax.swing.JLabel();
		jFileTextField3 = new javax.swing.JTextField();
		jRequestMethodLabel5 = new javax.swing.JLabel();
		jRequestMethodComboBox2 = new javax.swing.JComboBox();
		jAuthenticationSettingsPanel2 = new javax.swing.JPanel();
		jLoginLabel6 = new javax.swing.JLabel();
		jLoginTextField4 = new javax.swing.JTextField();
		jPasswordLabel7 = new javax.swing.JLabel();
		jPasswordField1 = new javax.swing.JPasswordField();
		jMessagePanel3 = new javax.swing.JPanel();
		jOriginatorLabel8 = new javax.swing.JLabel();
		jOriginatorTextField6 = new javax.swing.JTextField();
		jIndividualMessagesCheckBox1 = new javax.swing.JCheckBox();
		jPhonesScrollPane1 = new javax.swing.JScrollPane();
		jPhonesTextArea1 = new javax.swing.JTextArea();
		jMessageScrollPane2 = new javax.swing.JScrollPane();
		jMessageTextArea2 = new javax.swing.JTextArea();
		jStartDateLabel9 = new javax.swing.JLabel();
		jStartDateTextField7 = new javax.swing.JTextField();
		jWantSMSIdsCheckBox2 = new javax.swing.JCheckBox();
		jEncodingLabel10 = new javax.swing.JLabel();
		jEncodingComboBox3 = new javax.swing.JComboBox();
		jSeparator1 = new javax.swing.JSeparator();
		jPanel4 = new javax.swing.JPanel();
		jSendButton1 = new javax.swing.JButton();
		jCloseButton2 = new javax.swing.JButton();
		jHelpButton3 = new javax.swing.JButton();
		
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Send an SMS Message");
		jConnectionSettingsPanel1.setLayout(new java.awt.GridBagLayout());
		
		jConnectionSettingsPanel1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Connection Settings: "));
		jProtocolLabel1.setDisplayedMnemonic('P');
		jProtocolLabel1.setLabelFor(jProtocolComboBox1);
		jProtocolLabel1.setText("Protocol:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jConnectionSettingsPanel1.add(jProtocolLabel1, gridBagConstraints);
		
		jProtocolComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HTTP", "HTTPS" }));
		jProtocolComboBox1.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				jProtocolComboBox1ItemStateChanged(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		jConnectionSettingsPanel1.add(jProtocolComboBox1, gridBagConstraints);
		
		jHostLabel2.setDisplayedMnemonic('t');
		jHostLabel2.setLabelFor(jHostTextField1);
		jHostLabel2.setText("Host:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jConnectionSettingsPanel1.add(jHostLabel2, gridBagConstraints);
		
		jHostTextField1.setText("smsmail.ru");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		jConnectionSettingsPanel1.add(jHostTextField1, gridBagConstraints);
		
		jPortLabel3.setDisplayedMnemonic('r');
		jPortLabel3.setLabelFor(jPortTextField2);
		jPortLabel3.setText("Port:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jConnectionSettingsPanel1.add(jPortLabel3, gridBagConstraints);
		
		jPortTextField2.setText("80");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		jConnectionSettingsPanel1.add(jPortTextField2, gridBagConstraints);
		
		jFileLabel4.setDisplayedMnemonic('F');
		jFileLabel4.setLabelFor(jFileTextField3);
		jFileLabel4.setText("File:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jConnectionSettingsPanel1.add(jFileLabel4, gridBagConstraints);
		
		jFileTextField3.setText("/corp/multi.php");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		jConnectionSettingsPanel1.add(jFileTextField3, gridBagConstraints);
		
		jRequestMethodLabel5.setDisplayedMnemonic('M');
		jRequestMethodLabel5.setLabelFor(jRequestMethodComboBox2);
		jRequestMethodLabel5.setText("Request Method:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jConnectionSettingsPanel1.add(jRequestMethodLabel5, gridBagConstraints);
		
		jRequestMethodComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "POST", "GET" }));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		jConnectionSettingsPanel1.add(jRequestMethodComboBox2, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
		getContentPane().add(jConnectionSettingsPanel1, gridBagConstraints);
		
		jAuthenticationSettingsPanel2.setLayout(new java.awt.GridBagLayout());
		
		jAuthenticationSettingsPanel2.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Authentication Settings: "));
		jLoginLabel6.setDisplayedMnemonic('L');
		jLoginLabel6.setLabelFor(jLoginTextField4);
		jLoginLabel6.setText("Login:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jAuthenticationSettingsPanel2.add(jLoginLabel6, gridBagConstraints);
		
		jLoginTextField4.setText("syrus");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		jAuthenticationSettingsPanel2.add(jLoginTextField4, gridBagConstraints);
		
		jPasswordLabel7.setDisplayedMnemonic('w');
		jPasswordLabel7.setLabelFor(jPasswordField1);
		jPasswordLabel7.setText("Password:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jAuthenticationSettingsPanel2.add(jPasswordLabel7, gridBagConstraints);
		
		jPasswordField1.setText("B112233");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		jAuthenticationSettingsPanel2.add(jPasswordField1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 10);
		getContentPane().add(jAuthenticationSettingsPanel2, gridBagConstraints);
		
		jMessagePanel3.setLayout(new java.awt.GridBagLayout());
		
		jMessagePanel3.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Message Settings: "));
		jOriginatorLabel8.setDisplayedMnemonic('O');
		jOriginatorLabel8.setLabelFor(jOriginatorTextField6);
		jOriginatorLabel8.setText("Originator:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jMessagePanel3.add(jOriginatorLabel8, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		jMessagePanel3.add(jOriginatorTextField6, gridBagConstraints);
		
		jIndividualMessagesCheckBox1.setMnemonic('I');
		jIndividualMessagesCheckBox1.setText("Individual Messages");
		jIndividualMessagesCheckBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jIndividualMessagesCheckBox1ActionPerformed(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jMessagePanel3.add(jIndividualMessagesCheckBox1, gridBagConstraints);
		
		jPhonesScrollPane1.setViewportBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Phones: "), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
		jPhonesTextArea1.setBackground(UIManager.getColor("text"));
		jPhonesTextArea1.setTabSize(4);
		jPhonesScrollPane1.setViewportView(jPhonesTextArea1);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jMessagePanel3.add(jPhonesScrollPane1, gridBagConstraints);
		
		jMessageScrollPane2.setViewportBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Message: "), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
		jMessageTextArea2.setBackground(UIManager.getColor("text"));
		jMessageTextArea2.setTabSize(4);
		jMessageScrollPane2.setViewportView(jMessageTextArea2);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jMessagePanel3.add(jMessageScrollPane2, gridBagConstraints);
		
		jStartDateLabel9.setDisplayedMnemonic('D');
		jStartDateLabel9.setLabelFor(jStartDateTextField7);
		jStartDateLabel9.setText("Start Date (YYYY-MM-DD hh:mm:ss):");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jMessagePanel3.add(jStartDateLabel9, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		jMessagePanel3.add(jStartDateTextField7, gridBagConstraints);
		
		jWantSMSIdsCheckBox2.setMnemonic('A');
		jWantSMSIdsCheckBox2.setSelected(true);
		jWantSMSIdsCheckBox2.setText("Want SMS Ids");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jMessagePanel3.add(jWantSMSIdsCheckBox2, gridBagConstraints);
		
		jEncodingLabel10.setDisplayedMnemonic('N');
		jEncodingLabel10.setLabelFor(jEncodingComboBox3);
		jEncodingLabel10.setText("Send Messages As:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jMessagePanel3.add(jEncodingLabel10, gridBagConstraints);
		
		jEncodingComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Transliterated", "KOI8-R", "windows-1251" }));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		jMessagePanel3.add(jEncodingComboBox3, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
		getContentPane().add(jMessagePanel3, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		getContentPane().add(jSeparator1, gridBagConstraints);
		
		jPanel4.setLayout(new java.awt.GridBagLayout());
		
		jSendButton1.setMnemonic('S');
		jSendButton1.setText("Send");
		jSendButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jSendButton1ActionPerformed(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		jPanel4.add(jSendButton1, gridBagConstraints);
		
		jCloseButton2.setMnemonic('C');
		jCloseButton2.setText("Close");
		jCloseButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jCloseButton2ActionPerformed(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		jPanel4.add(jCloseButton2, gridBagConstraints);
		
		jHelpButton3.setMnemonic('H');
		jHelpButton3.setText("Help");
		jHelpButton3.setEnabled(false);
		jHelpButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jHelpButton3ActionPerformed(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		jPanel4.add(jHelpButton3, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
		getContentPane().add(jPanel4, gridBagConstraints);
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-400)/2, (screenSize.height-600)/2, 400, 600);
	}//GEN-END:initComponents

	private void jHelpButton3ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jHelpButton3ActionPerformed

	}//GEN-LAST:event_jHelpButton3ActionPerformed

	private void jCloseButton2ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jCloseButton2ActionPerformed
		System.exit(0);
	}//GEN-LAST:event_jCloseButton2ActionPerformed

	private void jSendButton1ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jSendButton1ActionPerformed
		boolean individualMessages = jIndividualMessagesCheckBox1.isSelected();
		String charsetName = (String) (jEncodingComboBox3.getSelectedItem());
		String requestMethod = (String) (jRequestMethodComboBox2.getSelectedItem());
		String protocol = (String) (jProtocolComboBox1.getSelectedItem());
		boolean rus = (! charsetName.equals("Transliterated"));
		String phones = jPhonesTextArea1.getText();
		String message;

		if (rus) {
			if (individualMessages) {
				message = "";
				try {
					phones = new String(phones.getBytes(), charsetName);
				} catch (UnsupportedEncodingException uee) {
				}
			} else {
				message = jMessageTextArea2.getText();
				try {
					message = new String(message.getBytes(), charsetName);
				} catch (UnsupportedEncodingException uee) {
				}
			}
		} else {
			if (individualMessages)
				message = "";
			else
				message = jMessageTextArea2.getText();
		}

		try {
			HttpURLConnection httpURLConnection = null;
			if (requestMethod.equals("POST")) {
				httpURLConnection = (HttpURLConnection) ((new URL(protocol, jHostTextField1.getText(), Integer.parseInt(jPortTextField2.getText()), jFileTextField3.getText())).openConnection());
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setRequestMethod(requestMethod);
				httpURLConnection.connect();

				if (protocol.equals("HTTPS")) {
					HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
					System.out.println(httpsURLConnection.getCipherSuite());
					Certificate certificates[] = httpsURLConnection.getServerCertificates();
					for (int i = 0; i < certificates.length; i ++)
						System.out.println(certificates[i]);
				}

				PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
				out.print("login=" + jLoginTextField4.getText());
				out.print("&password=" + new String(jPasswordField1.getPassword()));
				out.print("&originator=" + jOriginatorTextField6.getText());
				out.print("&individual_messages=" + (individualMessages ? '1' : '0'));
				out.print("&phones=" + phones);
				out.print("&message=" + message);
				out.print("&want_sms_ids=" + (jWantSMSIdsCheckBox2.isSelected() ? '1' : '0'));
				out.print("&start_date=" + jStartDateTextField7.getText());
				out.print("&rus=" + (rus ? '1' : '0'));
				out.println();
				out.close();
			} else if (requestMethod.equals("GET")) {
				ByteArrayOutputStream _out = new ByteArrayOutputStream();
				PrintWriter out = new PrintWriter(_out);
				out.print('?');
				out.print("login=" + jLoginTextField4.getText());
				out.print("&password=" + new String(jPasswordField1.getPassword()));
				out.print("&originator=" + jOriginatorTextField6.getText());
				out.print("&individual_messages=" + (individualMessages ? '1' : '0'));
				out.print("&phones=" + phones);
				out.print("&message=" + message);
				out.print("&want_sms_ids=" + (jWantSMSIdsCheckBox2.isSelected() ? '1' : '0'));
				out.print("&start_date=" + jStartDateTextField7.getText());
				out.print("&rus=" + (rus ? '1' : '0'));
				out.close();

				httpURLConnection = (HttpURLConnection) ((new URL(protocol, jHostTextField1.getText(), Integer.parseInt(jPortTextField2.getText()), jFileTextField3.getText() + _out.toString())).openConnection());
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(false);
				httpURLConnection.setRequestMethod(requestMethod);
				httpURLConnection.connect();

				if (protocol.equals("HTTPS")) {
					HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
					System.out.println(httpsURLConnection.getCipherSuite());
					Certificate certificates[] = httpsURLConnection.getServerCertificates();
					for (int i = 0; i < certificates.length; i ++)
						System.out.println(certificates[i]);
				}
			}
			System.out.println("" + httpURLConnection.getResponseCode() + ' ' + httpURLConnection.getResponseMessage());
			InputStream in = httpURLConnection.getInputStream();
			Reader _in = new InputStreamReader(in);
			while (true) {
				int c = _in.read();
				if (c == - 1)
					break;
				System.out.print((char) c);
			}
			_in.close();
			in.close();
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (IOException ioe) {
			/*
			 * HTTPS hostname wrong.
			 */
			ioe.printStackTrace();
		}
	}//GEN-LAST:event_jSendButton1ActionPerformed

	private void jIndividualMessagesCheckBox1ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jIndividualMessagesCheckBox1ActionPerformed
		jMessageTextArea2.setEnabled(! jIndividualMessagesCheckBox1.isSelected());
	}//GEN-LAST:event_jIndividualMessagesCheckBox1ActionPerformed

	private void jProtocolComboBox1ItemStateChanged(java.awt.event.ItemEvent e) {//GEN-FIRST:event_jProtocolComboBox1ItemStateChanged
		if (e.getID() == ItemEvent.ITEM_STATE_CHANGED) {
			int stateChange = e.getStateChange();
			if (stateChange == ItemEvent.SELECTED) {
				Object item = e.getItem();
				if (item.equals("HTTP"))
					jPortTextField2.setText("80");
				else if (item.equals("HTTPS"))
					jPortTextField2.setText("443");
			} else if (stateChange == ItemEvent.DESELECTED) {
			}
		}
	}//GEN-LAST:event_jProtocolComboBox1ItemStateChanged
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new JSMSSendFrame().show();
	}
	
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jAuthenticationSettingsPanel2;
	private javax.swing.JButton jCloseButton2;
	private javax.swing.JPanel jConnectionSettingsPanel1;
	private javax.swing.JComboBox jEncodingComboBox3;
	private javax.swing.JLabel jEncodingLabel10;
	private javax.swing.JLabel jFileLabel4;
	private javax.swing.JTextField jFileTextField3;
	private javax.swing.JButton jHelpButton3;
	private javax.swing.JLabel jHostLabel2;
	private javax.swing.JTextField jHostTextField1;
	private javax.swing.JCheckBox jIndividualMessagesCheckBox1;
	private javax.swing.JLabel jLoginLabel6;
	private javax.swing.JTextField jLoginTextField4;
	private javax.swing.JPanel jMessagePanel3;
	private javax.swing.JScrollPane jMessageScrollPane2;
	private javax.swing.JTextArea jMessageTextArea2;
	private javax.swing.JLabel jOriginatorLabel8;
	private javax.swing.JTextField jOriginatorTextField6;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPasswordField jPasswordField1;
	private javax.swing.JLabel jPasswordLabel7;
	private javax.swing.JScrollPane jPhonesScrollPane1;
	private javax.swing.JTextArea jPhonesTextArea1;
	private javax.swing.JLabel jPortLabel3;
	private javax.swing.JTextField jPortTextField2;
	private javax.swing.JComboBox jProtocolComboBox1;
	private javax.swing.JLabel jProtocolLabel1;
	private javax.swing.JComboBox jRequestMethodComboBox2;
	private javax.swing.JLabel jRequestMethodLabel5;
	private javax.swing.JButton jSendButton1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JLabel jStartDateLabel9;
	private javax.swing.JTextField jStartDateTextField7;
	private javax.swing.JCheckBox jWantSMSIdsCheckBox2;
	// End of variables declaration//GEN-END:variables
}
