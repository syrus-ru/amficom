/*
 * $Id: AbstractLinkTypeGeneralPanel.java,v 1.3 2005/03/14 13:36:18 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.configuration.AbstractLinkType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/03/14 13:36:18 $
 * @module schemeclient_v1
 */

public class AbstractLinkTypeGeneralPanel implements StorableObjectEditor
{
	protected AbstractLinkType linkType;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(Constants.TEXT_NAME);
	JTextField tfNameText = new JTextField();
	JLabel lbManufacturerLabel = new JLabel(Constants.TEXT_MANUFACTURER);
	JTextField tfManufacturerText = new JTextField();
	JLabel lbManufacturerCodeLabel = new JLabel(Constants.TEXT_MANUFACTURER_CODE);
	JTextField tfManufacturerCodeText = new JTextField();
	JLabel lbDescriptionLabel = new JLabel(Constants.TEXT_DESCRIPTION);
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

	protected AbstractLinkTypeGeneralPanel(AbstractLinkType linkType) {
		this();
		setObject(linkType);
	}

	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout(gbPanel0);

		pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(""));
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

		pnGeneralPanel.setBackground(Color.WHITE);
		pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(Constants.TEXT_AREA_SIZE);
	}
	
	public JComponent getGUI() {
		return pnPanel0; 
	}

	public Object getObject() {
		return linkType;
	}

	public void setObject(Object or) {
		this.linkType = (AbstractLinkType) or;

		if (linkType != null) {
			this.tfNameText.setText(linkType.getName());
			this.taDescriptionArea.setText(linkType.getDescription());
			this.tfManufacturerText.setText(linkType.getManufacturer());
			this.tfManufacturerCodeText.setText(linkType.getManufacturerCode());
		} 
		else {
			this.tfNameText.setText(Constants.TEXT_EMPTY);
			this.taDescriptionArea.setText(Constants.TEXT_EMPTY);
			this.tfManufacturerText.setText(Constants.TEXT_EMPTY);
			this.tfManufacturerCodeText.setText(Constants.TEXT_EMPTY);
		}
	}

	public void commitChanges() {
		if(MiscUtil.validName(this.tfNameText.getText())) {
			linkType.setName(this.tfNameText.getText());
			linkType.setDescription(this.taDescriptionArea.getText());
			linkType.setManufacturer(this.tfManufacturerText.getText());
			linkType.setManufacturerCode(this.tfManufacturerCodeText.getText());
		}
	}
}
