/*
 * $Id: PortTypeGeneralPanel.java,v 1.4 2005/03/30 13:33:39 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.scheme.ui.Constants;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/03/30 13:33:39 $
 * @module schemeclient_v1
 */

public class PortTypeGeneralPanel implements StorableObjectEditor {
	protected PortType portType;
	protected String[] sorts = new String[] {
			Constants.TEXT_PORTTYPESORT_OPTICAL,
			Constants.TEXT_PORTTYPESORT_THERMAL,
			Constants.TEXT_PORTTYPESORT_ELECTICAL
	};  
	
	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(Constants.TEXT_NAME);
	JTextField tfNameText = new JTextField();
	JLabel lbSortLabel = new JLabel(Constants.TEXT_TYPE);
	JComboBox cmbSortCombo = new AComboBox(sorts);
	JLabel lbDescriptionLabel = new JLabel(Constants.TEXT_DESCRIPTION);
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JPanel pnGeneralPanel = new JPanel();
	
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

		lbDescriptionLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,5,0,2 );
		gbPanel0.setConstraints( lbDescriptionLabel, gbcPanel0 );
		pnPanel0.add( lbDescriptionLabel );

		JScrollPane scpDescriptionArea = new JScrollPane( taDescriptionArea );
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints( scpDescriptionArea, gbcPanel0 );
		pnPanel0.add( scpDescriptionArea );

		GridBagLayout gbGeneralPanel = new GridBagLayout();
		GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		pnGeneralPanel.setLayout( gbGeneralPanel );

		lbNameLabel.setFocusable( false );
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,2 );
		gbGeneralPanel.setConstraints( lbNameLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbNameLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,0 );
		gbGeneralPanel.setConstraints( tfNameText, gbcGeneralPanel );
		pnGeneralPanel.add( tfNameText );

		lbSortLabel.setFocusable( false );
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,2 );
		gbGeneralPanel.setConstraints( lbSortLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbSortLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,0 );
		gbGeneralPanel.setConstraints( cmbSortCombo, gbcGeneralPanel );
		pnGeneralPanel.add( cmbSortCombo );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,0,0,0 );
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder(Constants.TEXT_EMPTY) );
		pnGeneralPanel.setBackground(Color.WHITE);
		pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		cmbSortCombo.setFont(UIGeneralStorage.DIALOG_FONT);
	}
	
	public JComponent getGUI() {
		return pnPanel0; 
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

	public void commitChanges() {
		if (MiscUtil.validName(this.tfNameText.getText())) {
		portType.setName(this.tfNameText.getText());
		portType.setDescription(this.taDescriptionArea.getText());

		if (cmbSortCombo.getSelectedItem().equals(sorts[0]))
			portType.setSort(PortTypeSort.PORTTYPESORT_OPTICAL);
		else if (cmbSortCombo.getSelectedItem().equals(sorts[1]))
			portType.setSort(PortTypeSort.PORTTYPESORT_THERMAL);
		else if (cmbSortCombo.getSelectedItem().equals(sorts[2]))
			portType.setSort(PortTypeSort.PORTTYPESORT_ELECTRICAL);
		}
	}
}
