/*
 * $Id: AbstractLinkTypeGeneralPanel.java,v 1.1 2005/03/10 08:09:08 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.general.ApplicationException;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:09:08 $
 * @module schemeclient_v1
 */

public class AbstractLinkTypeGeneralPanel extends GeneralPanel
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
		pnPanel0.setLayout( gbPanel0 );

		lbNameLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbNameLabel, gbcPanel0 );
		pnPanel0.add( lbNameLabel );

		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( tfNameText, gbcPanel0 );
		pnPanel0.add( tfNameText );

		lbManufacturerLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbManufacturerLabel, gbcPanel0 );
		pnPanel0.add( lbManufacturerLabel );

		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( tfManufacturerText, gbcPanel0 );
		pnPanel0.add( tfManufacturerText );

		lbManufacturerCodeLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbManufacturerCodeLabel, gbcPanel0 );
		pnPanel0.add( lbManufacturerCodeLabel );

		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( tfManufacturerCodeText, gbcPanel0 );
		pnPanel0.add( tfManufacturerCodeText );

		lbDescriptionLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbDescriptionLabel, gbcPanel0 );
		pnPanel0.add( lbDescriptionLabel );

		JScrollPane scpDescriptionArea = new JScrollPane( taDescriptionArea );
		scpDescriptionArea.setPreferredSize(Constants.TEXT_AREA_SIZE);
		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescriptionArea, gbcPanel0 );
		pnPanel0.add( scpDescriptionArea );

		this.setLayout(new BorderLayout());
		this.add(pnPanel0, BorderLayout.CENTER);
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

	public boolean modify() {
		if(!MiscUtil.validName(this.tfNameText.getText()))
			return false;

		linkType.setName(this.tfNameText.getText());
		linkType.setDescription(this.taDescriptionArea.getText());
		linkType.setManufacturer(this.tfManufacturerText.getText());
		linkType.setManufacturerCode(this.tfManufacturerCodeText.getText());
		return true;
	}
	
	public boolean save() {
		try {
			ConfigurationStorableObjectPool.putStorableObject(linkType);
			ConfigurationStorableObjectPool.flush(true);
		} catch (ApplicationException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean delete() {
		return true;
	}
}
