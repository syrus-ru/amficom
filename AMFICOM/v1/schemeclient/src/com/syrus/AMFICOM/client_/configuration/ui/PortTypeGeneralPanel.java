/*
 * $Id: PortTypeGeneralPanel.java,v 1.1 2005/03/10 08:09:08 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:09:08 $
 * @module schemeclient_v1
 */

public class PortTypeGeneralPanel extends GeneralPanel {
	protected PortType portType;
	protected String[] sorts = new String[] {
			Constants.PORTTYPESORT_OPTICAL,
			Constants.PORTTYPESORT_THERMAL,
			Constants.PORTTYPESORT_ELECTICAL
	};  
	
	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(Constants.TEXT_NAME);
	JTextField tfNameText = new JTextField();
	JLabel lbSortLabel = new JLabel(Constants.TEXT_TYPE);
	JComboBox cmbSortCombo = new AComboBox(sorts);
	JLabel lbDescriptionLabel = new JLabel(Constants.TEXT_DESCRIPTION);
	JTextArea taDescriptionArea = new JTextArea(2,10);
	
	protected PortTypeGeneralPanel() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected PortTypeGeneralPanel(PortType portType) {
		this();
		setObject(portType);
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

		lbSortLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbSortLabel, gbcPanel0 );
		pnPanel0.add( lbSortLabel );

		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( cmbSortCombo, gbcPanel0 );
		pnPanel0.add( cmbSortCombo );

		lbDescriptionLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
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
		gbcPanel0.gridy = 2;
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
		return portType;
	}

	public void setObject(Object or) {
		this.portType = (PortType) or;

		if (portType != null) {
			this.tfNameText.setText(portType.getName());
			this.taDescriptionArea.setText(portType.getDescription());
			switch (portType.getSort().value()) {
			case PortTypeSort._PORTTYPESORT_OPTICAL:
				cmbSortCombo.setSelectedItem(sorts[0]);
				break;
			case PortTypeSort._PORTTYPESORT_THERMAL:
				cmbSortCombo.setSelectedItem(sorts[1]);
				break;
			case PortTypeSort._PORTTYPESORT_ELECTRICAL:
				cmbSortCombo.setSelectedItem(sorts[2]);
			}
		} 
		else {
			this.tfNameText.setText(Constants.TEXT_EMPTY);
			this.taDescriptionArea.setText(Constants.TEXT_EMPTY);
		}
	}

	public boolean modify() {
		if (!MiscUtil.validName(this.tfNameText.getText()))
			return false;			
		
		portType.setName(this.tfNameText.getText());
		portType.setDescription(this.taDescriptionArea.getText());

		if (cmbSortCombo.getSelectedItem().equals(sorts[0]))
			portType.setSort(PortTypeSort.PORTTYPESORT_OPTICAL);
		else if (cmbSortCombo.getSelectedItem().equals(sorts[1]))
			portType.setSort(PortTypeSort.PORTTYPESORT_THERMAL);
		else if (cmbSortCombo.getSelectedItem().equals(sorts[2]))
			portType.setSort(PortTypeSort.PORTTYPESORT_ELECTRICAL);
		return true;
	}

	public boolean delete() {
		return true;
	}
}
