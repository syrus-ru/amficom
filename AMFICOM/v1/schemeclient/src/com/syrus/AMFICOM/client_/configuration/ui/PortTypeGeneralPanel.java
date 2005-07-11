/*
 * $Id: PortTypeGeneralPanel.java,v 1.11 2005/07/11 12:31:37 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.11 $, $Date: 2005/07/11 12:31:37 $
 * @module schemeclient_v1
 */

public class PortTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected PortType portType;
	protected String[] sorts = new String[] {
			LangModelScheme.getString(SchemeResourceKeys.PORTTYPESORT_OPTICAL),
			LangModelScheme.getString(SchemeResourceKeys.PORTTYPESORT_THERMAL),
			LangModelScheme.getString(SchemeResourceKeys.PORTTYPESORT_ELECTICAL),
	};  
	
	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbSortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.CODENAME));
	JComboBox cmbSortCombo = new AComboBox(sorts);
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JPanel pnGeneralPanel = new JPanel();
	
	PortTypeKind kind;
	
	protected PortTypeGeneralPanel() {
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
	
	public void setPortKind(PortTypeKind kind) {
		this.kind = kind;
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
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,0 );
		gbGeneralPanel.setConstraints( tfNameText, gbcGeneralPanel );
		pnGeneralPanel.add( tfNameText );
		
		gbcGeneralPanel.gridx = 3;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,0 );
		gbGeneralPanel.setConstraints( commitButton, gbcGeneralPanel );
		pnGeneralPanel.add( commitButton );

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

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
//		pnGeneralPanel.setBackground(Color.WHITE);
//		pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(cmbSortCombo);
		addToUndoableListener(taDescriptionArea);
		
		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
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
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public void commitChanges() {
		if (MiscUtil.validName(tfNameText.getText())) {
			if (portType == null) {
				try {
					portType = SchemeObjectsFactory.createPortType(tfNameText.getText(), this.kind);
					apply();
					aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, portType, SchemeEvent.CREATE_OBJECT));
					aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, portType, PortTypePropertiesManager.getInstance(aContext, this.kind), ObjectSelectedEvent.PORT_TYPE));
				} catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			} else {
				apply();
			}
		}
	}
	
	private void apply() {
		portType.setName(this.tfNameText.getText());
		portType.setDescription(this.taDescriptionArea.getText());

		if (cmbSortCombo.getSelectedItem().equals(sorts[0]))
			portType.setSort(PortTypeSort.PORTTYPESORT_OPTICAL);
		else if (cmbSortCombo.getSelectedItem().equals(sorts[1]))
			portType.setSort(PortTypeSort.PORTTYPESORT_THERMAL);
		else if (cmbSortCombo.getSelectedItem().equals(sorts[2]))
			portType.setSort(PortTypeSort.PORTTYPESORT_ELECTRICAL);
		
		try {
			StorableObjectPool.flush(portType.getId(), true);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, portType, SchemeEvent.UPDATE_OBJECT));
	}
}
