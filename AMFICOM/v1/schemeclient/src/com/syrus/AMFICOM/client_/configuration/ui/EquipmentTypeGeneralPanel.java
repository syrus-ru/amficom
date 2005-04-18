/*-
 * $Id: EquipmentTypeGeneralPanel.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.EquipmentTypeCodenames;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.Constants;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class EquipmentTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected EquipmentType eqt;

	private String[] eqtCodenames = new String[] {
			EquipmentTypeCodenames.OTHER,
			EquipmentTypeCodenames.REFLECTOMETER,
			EquipmentTypeCodenames.SWITCH,
			EquipmentTypeCodenames.FILTER,
			EquipmentTypeCodenames.CABLE_PANEL,
			EquipmentTypeCodenames.CROSS,
			EquipmentTypeCodenames.MUFF,
			EquipmentTypeCodenames.MULTIPLEXOR,
			EquipmentTypeCodenames.RECEIVER,
			EquipmentTypeCodenames.TRANSMITTER
		};
	
	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(Constants.NAME));
	JTextField tfNameText = new JTextField();
	JLabel lbCodenameLabel = new JLabel(LangModelScheme.getString(Constants.CODENAME));
	JComboBox tfCodenameCombo = new AComboBox();
	JLabel lbManufacturerLabel = new JLabel(LangModelScheme.getString(Constants.MANUFACTURER));
	JTextField tfManufacturerText = new JTextField();
	JLabel lbManufacturerCodeLabel = new JLabel(LangModelScheme.getString(Constants.MANUFACTURER_CODE));
	JTextField tfManufacturerCodeText = new JTextField();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(Constants.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JPanel pnGeneralPanel = new JPanel();
	
	protected EquipmentTypeGeneralPanel() {
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

	protected EquipmentTypeGeneralPanel(EquipmentType equipmentType) {
		this();
		setObject(equipmentType);
	}

	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout(gbPanel0);

		GridBagLayout gbGeneralPanel = new GridBagLayout();
		GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		pnGeneralPanel.setLayout(gbGeneralPanel);

		lbNameLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(lbNameLabel, gbcGeneralPanel);
		pnGeneralPanel.add(lbNameLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(tfNameText, gbcGeneralPanel);
		pnGeneralPanel.add(tfNameText);

		lbCodenameLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(lbCodenameLabel, gbcGeneralPanel);
		pnGeneralPanel.add(lbCodenameLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(tfCodenameCombo, gbcGeneralPanel);
		pnGeneralPanel.add(tfCodenameCombo);
		
		lbManufacturerLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(lbManufacturerLabel, gbcGeneralPanel);
		pnGeneralPanel.add(lbManufacturerLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(tfManufacturerText, gbcGeneralPanel);
		pnGeneralPanel.add(tfManufacturerText);

		lbManufacturerCodeLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(lbManufacturerCodeLabel, gbcGeneralPanel);
		pnGeneralPanel.add(lbManufacturerCodeLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(tfManufacturerCodeText, gbcGeneralPanel);
		pnGeneralPanel.add(tfManufacturerCodeText);
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 6;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(pnGeneralPanel, gbcPanel0);
		pnPanel0.add(pnGeneralPanel);

		lbDescriptionLabel.setFocusable(false);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 6;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 5, 0, 2);
		gbPanel0.setConstraints(lbDescriptionLabel, gbcPanel0);
		pnPanel0.add(lbDescriptionLabel);

		JScrollPane scpDescriptionArea = new JScrollPane(taDescriptionArea);
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 7;
		gbcPanel0.gridwidth = 5;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints(scpDescriptionArea, gbcPanel0);
		pnPanel0.add(scpDescriptionArea);

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( LangModelScheme.getString(Constants.EMPTY )));
		scpDescriptionArea.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		for (int i = 0; i < eqtCodenames.length; i++) {
			tfCodenameCombo.addItem(EquipmentTypeCodenames.getName(eqtCodenames[i]));			
		} 
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(tfCodenameCombo);
		addToUndoableListener(tfManufacturerText);
		addToUndoableListener(tfManufacturerCodeText);
		addToUndoableListener(taDescriptionArea);
	}
	
	public JComponent getGUI() {
		return pnPanel0; 
	}

	public Object getObject() {
		return eqt;
	}

	public void setObject(Object or) {
		this.eqt = (EquipmentType) or;

		if (eqt != null) {
			this.tfNameText.setText(eqt.getName());
			this.taDescriptionArea.setText(eqt.getDescription());
			this.tfManufacturerText.setText(eqt.getManufacturer());
			this.tfManufacturerCodeText.setText(eqt.getManufacturerCode());
			this.tfCodenameCombo.setSelectedItem(EquipmentTypeCodenames.getName(eqt.getCodename()));
		} 
		else {
			this.tfNameText.setText(LangModelScheme.getString(Constants.EMPTY));
			this.taDescriptionArea.setText(LangModelScheme.getString(Constants.EMPTY));
			this.tfManufacturerText.setText(LangModelScheme.getString(Constants.EMPTY));
			this.tfManufacturerCodeText.setText(LangModelScheme.getString(Constants.EMPTY));
		}
	}

	public void commitChanges() {
		if (MiscUtil.validName(tfNameText.getText())) {
			if (eqt == null) {
				try {
					eqt = SchemeObjectsFactory.createEquipmentType();
					aContext.getDispatcher().notify(new SchemeEvent(this, eqt, SchemeEvent.CREATE_OBJECT));
				} catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}

			eqt.setName(this.tfNameText.getText());
			eqt.setDescription(this.taDescriptionArea.getText());
			eqt.setManufacturer(this.tfManufacturerText.getText());
			eqt.setManufacturerCode(this.tfManufacturerCodeText.getText());
			eqt.setCodename(eqtCodenames[tfCodenameCombo.getSelectedIndex()]);
			
			aContext.getDispatcher().notify(new SchemeEvent(this, eqt, SchemeEvent.UPDATE_OBJECT));
		}
	}
}
