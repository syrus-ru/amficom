/*-
 * $Id: SchemeProtoElementGeneralPanel.java,v 1.9 2005/06/23 12:58:23 stas Exp $
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
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/06/23 12:58:23 $
 * @module schemeclient_v1
 */

public class SchemeProtoElementGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeProtoElement schemeProtoElement;
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
	JLabel descrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2,10);
	
	protected SchemeProtoElementGeneralPanel() {
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

	protected SchemeProtoElementGeneralPanel(SchemeProtoElement schemeProtoElement) {
		this();
		setObject(schemeProtoElement);
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
		generalPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
		descrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(nameText);
		addToUndoableListener(labelText);
		addToUndoableListener(symbolBut);
		addToUndoableListener(typeBox);
		addToUndoableListener(typeCombo);
		addToUndoableListener(manufacturerText);
		addToUndoableListener(manufacturerCodeText);
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
	
	public JComponent getGUI() {
		return panel0; 
	}

	public Object getObject() {
		return schemeProtoElement;
	}

	public void setObject(Object or) {
		this.schemeProtoElement = (SchemeProtoElement) or;
		EquipmentType eqt = null;
		Icon symbol = null;
		
		typeCombo.removeAllItems();

		if (this.schemeProtoElement != null) {
			this.nameText.setText(schemeProtoElement.getName());
			this.descrArea.setText(schemeProtoElement.getDescription());
			this.labelText.setText(schemeProtoElement.getLabel());
			if (schemeProtoElement.getSymbol() != null)
				symbol = new ImageIcon(schemeProtoElement.getSymbol().getImage());
			eqt = this.schemeProtoElement.getEquipmentType();
			
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
			try {
				typeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		} else {
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
		this.symbolBut.setIcon(symbol);
	}

	public void commitChanges() {
		if (schemeProtoElement != null && MiscUtil.validName(this.nameText.getText())) {
			schemeProtoElement.setName(this.nameText.getText());
			schemeProtoElement.setDescription(this.descrArea.getText());
			schemeProtoElement.setLabel(this.labelText.getText());
			if (this.symbolBut.getIcon() == null) {
				schemeProtoElement.setSymbol(null);
			} else {
				try {
					schemeProtoElement.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(imageId, true));
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
			if (typeBox.isSelected()) {
				EquipmentType eqt = (EquipmentType)typeCombo.getSelectedItem();
				if (eqt != null) {
					schemeProtoElement.setEquipmentType(eqt);
					eqt.setManufacturer(this.manufacturerText.getText());
					eqt.setManufacturerCode(this.manufacturerCodeText.getText());
				}
			} else {
				schemeProtoElement.setEquipmentType(null);
			}
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, schemeProtoElement, SchemeEvent.UPDATE_OBJECT));
		}
	}
}
