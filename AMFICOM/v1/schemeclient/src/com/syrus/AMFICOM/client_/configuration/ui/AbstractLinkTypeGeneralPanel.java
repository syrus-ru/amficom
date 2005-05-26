/*
 * $Id: AbstractLinkTypeGeneralPanel.java,v 1.9 2005/05/26 07:40:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.Constants;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/05/26 07:40:50 $
 * @module schemeclient_v1
 */

public abstract class AbstractLinkTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected AbstractLinkType linkType;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(Constants.NAME));
	JTextField tfNameText = new JTextField();
	JLabel lbManufacturerLabel = new JLabel(LangModelScheme.getString(Constants.MANUFACTURER));
	JTextField tfManufacturerText = new JTextField();
	JLabel lbManufacturerCodeLabel = new JLabel(LangModelScheme.getString(Constants.MANUFACTURER_CODE));
	JTextField tfManufacturerCodeText = new JTextField();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(Constants.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JPanel pnGeneralPanel = new JPanel();
	
	protected AbstractLinkTypeGeneralPanel() {
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

	protected AbstractLinkTypeGeneralPanel(AbstractLinkType linkType) {
		this();
		setObject(linkType);
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

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( Constants.EMPTY ));
//		pnGeneralPanel.setBackground(Color.WHITE);
//		pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(tfManufacturerText);
		addToUndoableListener(tfManufacturerCodeText);
		addToUndoableListener(taDescriptionArea);
	}
	
	public JComponent getGUI() {
		return this.pnPanel0; 
	}

	public Object getObject() {
		return this.linkType;
	}

	public void setObject(Object or) {
		this.linkType = (AbstractLinkType) or;

		if (this.linkType != null) {
			this.tfNameText.setText(this.linkType.getName());
			this.taDescriptionArea.setText(this.linkType.getDescription());
			this.tfManufacturerText.setText(this.linkType.getManufacturer());
			this.tfManufacturerCodeText.setText(this.linkType.getManufacturerCode());
		} 
		else {
			this.tfNameText.setText(Constants.EMPTY);
			this.taDescriptionArea.setText(Constants.EMPTY);
			this.tfManufacturerText.setText(Constants.EMPTY);
			this.tfManufacturerCodeText.setText(Constants.EMPTY);
		}
	}

	public void commitChanges() {
		if(MiscUtil.validName(this.tfNameText.getText())) {
			this.linkType.setName(this.tfNameText.getText());
			this.linkType.setDescription(this.taDescriptionArea.getText());
			this.linkType.setManufacturer(this.tfManufacturerText.getText());
			this.linkType.setManufacturerCode(this.tfManufacturerCodeText.getText());
			
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.linkType, SchemeEvent.UPDATE_OBJECT));
		}
	}
}
