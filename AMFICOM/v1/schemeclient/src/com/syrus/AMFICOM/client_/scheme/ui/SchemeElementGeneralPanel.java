/*-
 * $Id: SchemeElementGeneralPanel.java,v 1.8 2005/06/23 12:58:23 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2005/06/23 12:58:23 $
 * @module schemeclient_v1
 */

public class SchemeElementGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeElement schemeElement;
	private Identifier imageId;
	
	JPanel panel0 = new JPanel();
	JPanel generalPanel = new JPanel();
	JLabel nameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField nameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel symbolLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField labelText = new JTextField();
	JButton symbolBut = new JButton();
	JCheckBox typeBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.EQUIPMENT_TYPE));
	JLabel typeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox typeCombo = new WrapperedComboBox(EquipmentTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel manufacturerLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
	JTextField manufacturerText = new JTextField();
	JLabel manufacturerCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
	JTextField manufacturerCodeText = new JTextField();
	JCheckBox equipmentBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.INSTANCE));
	JLabel supplierLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER));
	JTextField supplierText = new JTextField();
	JLabel supplierCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER_CODE));
	JTextField supplierCodeText = new JTextField();
	JLabel hwLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.HARDWARE));
	JLabel hwsnLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
	JTextField hwsnText = new JTextField();
	JLabel hwvLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.VERSION));
	JTextField hwvText = new JTextField();
	JLabel swLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SOFTWARE));
	JLabel swsnLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
	JTextField swsnText = new JTextField();
	JLabel swvLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.VERSION));
	JTextField swvText = new JTextField();
	JLabel longLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LONGITUDE));
	JTextField longText = new JTextField();
	JLabel latLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LATITUDE));
	JTextField latText = new JTextField();
	JCheckBox kisBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.KIS));
	JLabel kisLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.KIS));
	WrapperedComboBox kisCombo = new WrapperedComboBox(KISWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel kisAddrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.ADDRESS));
	JTextField kisAddrText = new JTextField();
	JLabel kisPortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PORT));
	JTextField kisPortText = new JTextField();
	JLabel descrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2,10);
	
	protected SchemeElementGeneralPanel() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	protected SchemeElementGeneralPanel(SchemeElement schemeElement) {
		this();
		setObject(schemeElement);
	}

	private void jbInit() throws Exception {
		GridBagLayout gbpanel0 = new GridBagLayout();
		GridBagConstraints gbcpanel0 = new GridBagConstraints();
		panel0.setLayout(gbpanel0);

		GridBagLayout gbgeneralPanel = new GridBagLayout();
		GridBagConstraints gbcgeneralPanel = new GridBagConstraints();
		generalPanel.setLayout(gbgeneralPanel);

		nameLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(nameLabel, gbcgeneralPanel);
		generalPanel.add(nameLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(nameText, gbcgeneralPanel);
		generalPanel.add(nameText);
		
		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(commitButton, gbcgeneralPanel);
		generalPanel.add(commitButton);

		symbolLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(symbolLabel, gbcgeneralPanel);
		generalPanel.add(symbolLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(labelText, gbcgeneralPanel);
		generalPanel.add(labelText);

		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(symbolBut, gbcgeneralPanel);
		generalPanel.add(symbolBut);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(typeBox, gbcgeneralPanel);
		generalPanel.add(typeBox);

		typeLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(typeLabel, gbcgeneralPanel);
		generalPanel.add(typeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(typeCombo, gbcgeneralPanel);
		generalPanel.add(typeCombo);

		manufacturerLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerLabel, gbcgeneralPanel);
		generalPanel.add(manufacturerLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerText, gbcgeneralPanel);
		generalPanel.add(manufacturerText);

		manufacturerCodeLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerCodeLabel, gbcgeneralPanel);
		generalPanel.add(manufacturerCodeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerCodeText, gbcgeneralPanel);
		generalPanel.add(manufacturerCodeText);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(equipmentBox, gbcgeneralPanel);
		generalPanel.add(equipmentBox);

		supplierLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 7;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(supplierLabel, gbcgeneralPanel);
		generalPanel.add(supplierLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 7;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(supplierText, gbcgeneralPanel);
		generalPanel.add(supplierText);

		supplierCodeLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 8;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(supplierCodeLabel, gbcgeneralPanel);
		generalPanel.add(supplierCodeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 8;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(supplierCodeText, gbcgeneralPanel);
		generalPanel.add(supplierCodeText);

		hwLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 10;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(hwLabel, gbcgeneralPanel);
		generalPanel.add(hwLabel);

		hwsnLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 11;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(hwsnLabel, gbcgeneralPanel);
		generalPanel.add(hwsnLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 11;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(hwsnText, gbcgeneralPanel);
		generalPanel.add(hwsnText);

		hwvLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 4;
		gbcgeneralPanel.gridy = 11;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(hwvLabel, gbcgeneralPanel);
		generalPanel.add(hwvLabel);

		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 11;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(hwvText, gbcgeneralPanel);
		generalPanel.add(hwvText);

		swLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 12;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(swLabel, gbcgeneralPanel);
		generalPanel.add(swLabel);

		swsnLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 13;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(swsnLabel, gbcgeneralPanel);
		generalPanel.add(swsnLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 13;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(swsnText, gbcgeneralPanel);
		generalPanel.add(swsnText);

		swvLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 4;
		gbcgeneralPanel.gridy = 13;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(swvLabel, gbcgeneralPanel);
		generalPanel.add(swvLabel);

		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 13;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(swvText, gbcgeneralPanel);
		generalPanel.add(swvText);

		longLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 9;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(longLabel, gbcgeneralPanel);
		generalPanel.add(longLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 9;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(longText, gbcgeneralPanel);
		generalPanel.add(longText);

		latLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 3;
		gbcgeneralPanel.gridy = 9;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(latLabel, gbcgeneralPanel);
		generalPanel.add(latLabel);

		gbcgeneralPanel.gridx = 5;
		gbcgeneralPanel.gridy = 9;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(latText, gbcgeneralPanel);
		generalPanel.add(latText);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 14;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(kisBox, gbcgeneralPanel);
		generalPanel.add(kisBox);

		kisLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 15;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(kisLabel, gbcgeneralPanel);
		generalPanel.add(kisLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 15;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(kisCombo, gbcgeneralPanel);
		generalPanel.add(kisCombo);

		kisAddrLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 16;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(kisAddrLabel, gbcgeneralPanel);
		generalPanel.add(kisAddrLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 16;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(kisAddrText, gbcgeneralPanel);
		generalPanel.add(kisAddrText);

		kisPortLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 4;
		gbcgeneralPanel.gridy = 16;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(kisPortLabel, gbcgeneralPanel);
		generalPanel.add(kisPortLabel);

		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 16;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(kisPortText, gbcgeneralPanel);
		generalPanel.add(kisPortText);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 0;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 7;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(generalPanel, gbcpanel0);
		panel0.add(generalPanel);

		descrLabel.setFocusable(false);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 7;
		gbcpanel0.gridwidth = 3;
		gbcpanel0.gridheight = 1;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 0;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(descrLabel, gbcpanel0);
		panel0.add(descrLabel);

		JScrollPane scpdescrArea = new JScrollPane(descrArea);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 8;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 3;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 1;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(scpdescrArea, gbcpanel0);
		panel0.add(scpdescrArea);
		
		manufacturerText.setEnabled(false);
		manufacturerCodeText.setEnabled(false);
		typeBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				typeCombo.setEnabled(typeBox.isSelected());
			}
		});
		typeCombo.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				EquipmentType eqt = (EquipmentType)typeCombo.getSelectedItem();
				if (eqt != null) {
					manufacturerText.setText(eqt.getManufacturer());
					manufacturerCodeText.setText(eqt.getManufacturerCode());
				} else {
					manufacturerText.setText(SchemeResourceKeys.EMPTY);
					manufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
				}
			}
		});
		equipmentBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setEquipmentEnabled(equipmentBox.isSelected());
				if (!equipmentBox.isSelected())
					kisBox.setSelected(false);
			}
		});
		kisBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setKISEnabled(kisBox.isSelected());
			}
		});
		generalPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
		descrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(nameText);
		addToUndoableListener(labelText);
		addToUndoableListener(symbolBut);
		addToUndoableListener(typeBox);
		addToUndoableListener(typeCombo);
		addToUndoableListener(manufacturerText);
		addToUndoableListener(manufacturerCodeText);
		addToUndoableListener(equipmentBox);
		addToUndoableListener(supplierText);
		addToUndoableListener(supplierCodeText);
		addToUndoableListener(hwsnText);
		addToUndoableListener(hwvText);
		addToUndoableListener(swsnText);
		addToUndoableListener(swvText);
		addToUndoableListener(longText);
		addToUndoableListener(latText);
		addToUndoableListener(kisBox);
		addToUndoableListener(kisCombo);
		addToUndoableListener(kisAddrText);
		addToUndoableListener(kisPortText);
		addToUndoableListener(descrArea);
		
		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_ADD_CHARACTERISTIC));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});
	}
	
	void setEquipmentEnabled(boolean b) {
		supplierLabel.setEnabled(b);
		supplierText.setEnabled(b);
		supplierCodeLabel.setEnabled(b);
		supplierCodeText.setEnabled(b);
		hwLabel.setEnabled(b);
		hwsnLabel.setEnabled(b);
		hwsnText.setEnabled(b);
		hwvLabel.setEnabled(b);
		hwvText.setEnabled(b);
		swLabel.setEnabled(b);
		swsnLabel.setEnabled(b);
		swsnText.setEnabled(b);
		swvLabel.setEnabled(b);
		swvText.setEnabled(b);
		longLabel.setEnabled(b);
		longText.setEnabled(b);
		latLabel.setEnabled(b);
		latText.setEnabled(b);
		kisBox.setEnabled(b);
		kisLabel.setEnabled(b);
	}
	
	void setKISEnabled(boolean b) {
		kisLabel.setEnabled(b);
		kisCombo.setEnabled(b);
		kisAddrLabel.setEnabled(b);
		kisAddrText.setEnabled(b);
		kisPortLabel.setEnabled(b);
		kisPortText.setEnabled(b);
	}

	public JComponent getGUI() {
		return panel0; 
	}

	public Object getObject() {
		return schemeElement;
	}

	public void setObject(Object or) {
		this.schemeElement = (SchemeElement) or;
		EquipmentType eqt = null;
		Equipment eq = null;
		KIS kis = null;
		Icon symbol = null;
		
		typeCombo.removeAllItems();
		kisCombo.removeAllItems();

		if (this.schemeElement != null) {
			this.nameText.setText(schemeElement.getName());
			this.descrArea.setText(schemeElement.getDescription());
			this.labelText.setText(schemeElement.getLabel());
			if (schemeElement.getSymbol() != null)
				symbol = new ImageIcon(schemeElement.getSymbol().getImage());
			eqt = this.schemeElement.getEquipmentType();
			eq = this.schemeElement.getEquipment(); 
			kis = this.schemeElement.getKis();
			
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
			try {
				typeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			condition = new EquivalentCondition(ObjectEntities.KIS_CODE);
			try {
				kisCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		} 
		else {
			this.nameText.setText(SchemeResourceKeys.EMPTY);
			this.descrArea.setText(SchemeResourceKeys.EMPTY);
			this.labelText.setText(SchemeResourceKeys.EMPTY);
		}
		
		if (eqt != null) {
			this.typeBox.setSelected(true);
			this.typeCombo.setSelectedItem(eqt);
			this.manufacturerText.setText(eqt.getManufacturer());
			this.manufacturerCodeText.setText(eqt.getManufacturerCode());
		} else {
			this.typeBox.setSelected(false);
			this.manufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.manufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
		if (kis != null) {
			this.kisBox.setSelected(true);
			this.kisCombo.setSelectedItem(kis);
			this.kisAddrText.setText(kis.getHostName());
			this.kisPortText.setText(Short.toString(kis.getTCPPort()));
		} else {
			this.kisBox.setSelected(false);
			this.kisAddrText.setText(SchemeResourceKeys.EMPTY);
			this.kisPortText.setText(SchemeResourceKeys.EMPTY);
		}
		if (eq != null) {
			this.equipmentBox.setSelected(true);
			this.supplierText.setText(eq.getSupplier());
			this.supplierCodeText.setText(eq.getSupplierCode());
			this.hwsnText.setText(eq.getHwSerial());
			this.hwvText.setText(eq.getHwVersion());
			this.swsnText.setText(eq.getSwSerial());
			this.swvText.setText(eq.getSwVersion());
			this.longText.setText(Float.toString(eq.getLongitude()));
			this.latText.setText(Float.toString(eq.getLatitude()));
		} else {
			this.equipmentBox.setSelected(false);
			this.supplierText.setText(SchemeResourceKeys.EMPTY);
			this.supplierCodeText.setText(SchemeResourceKeys.EMPTY);
			this.hwsnText.setText(SchemeResourceKeys.EMPTY);
			this.hwvText.setText(SchemeResourceKeys.EMPTY);
			this.swsnText.setText(SchemeResourceKeys.EMPTY);
			this.swvText.setText(SchemeResourceKeys.EMPTY);
			this.longText.setText(SchemeResourceKeys.EMPTY);
			this.latText.setText(SchemeResourceKeys.EMPTY);
		}
		this.symbolBut.setIcon(symbol);
	}

	public void commitChanges() {
		if (schemeElement != null && MiscUtil.validName(this.nameText.getText())) {
			schemeElement.setName(this.nameText.getText());
			schemeElement.setDescription(this.descrArea.getText());
			schemeElement.setLabel(this.labelText.getText());
			if (this.symbolBut.getIcon() == null) {
				schemeElement.setSymbol(null);
			} else {
				try {
					schemeElement.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(imageId, true));
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
			if (typeBox.isSelected()) {
				EquipmentType eqt = (EquipmentType)typeCombo.getSelectedItem();
				if (eqt != null) {
					schemeElement.setEquipmentType(eqt);
					eqt.setManufacturer(this.manufacturerText.getText());
					eqt.setManufacturerCode(this.manufacturerCodeText.getText());
				}
			} else {
				schemeElement.setEquipmentType(null);
			}
			Equipment eq = schemeElement.getEquipment();
			if (equipmentBox.isSelected()) {
				if (eq == null) {
					try {
						eq = SchemeObjectsFactory.createEquipment();
						schemeElement.setEquipment(eq);
					} catch (CreateObjectException e) {
						Log.errorException(e);
					}
				}
				if (eq != null) {
					eq.setName(schemeElement.getName());
					eq.setDescription(schemeElement.getDescription());
					eq.setSupplier(this.supplierText.getText());
					eq.setSupplierCode(this.supplierCodeText.getText());
					eq.setHwSerial(this.hwsnText.getText());
					eq.setHwVersion(this.hwvText.getText());
					eq.setSwSerial(this.swsnText.getText());
					eq.setSwVersion(this.swvText.getText());
					try {
						eq.setLongitude(Float.parseFloat(this.longText.getText()));
					} catch (NumberFormatException e) {
						eq.setLongitude(0);
					}
					try {
						eq.setLatitude(Float.parseFloat(this.latText.getText()));
					} catch (NumberFormatException e1) {
						eq.setLatitude(0);
					}
				}
			} else if (eq != null) {
				StorableObjectPool.delete(eq.getId());
				schemeElement.setEquipment(null);
			}

			if (kisBox.isSelected()) {
				KIS kis = (KIS)kisCombo.getSelectedItem();
				if (kis != null) {
					schemeElement.setKis(kis);
					kis.setName(schemeElement.getName());
					kis.setDescription(schemeElement.getDescription());
					kis.setHostName(this.kisAddrText.getText());
					kis.setTCPPort(Short.parseShort(this.kisPortText.getText()));
				}
			} else {
				schemeElement.setKis(null);
			}
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, schemeElement, SchemeEvent.UPDATE_OBJECT));
		}
	}
}
