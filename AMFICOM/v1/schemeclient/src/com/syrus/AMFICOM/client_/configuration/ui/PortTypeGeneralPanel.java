/*
 * $Id: PortTypeGeneralPanel.java,v 1.18 2005/10/30 14:49:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
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
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/10/30 14:49:18 $
 * @module schemeclient
 */

public class PortTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected PortType portType;
	protected String[] sorts = new String[] { LangModelScheme.getString(SchemeResourceKeys.PORTTYPESORT_OPTICAL),
			LangModelScheme.getString(SchemeResourceKeys.PORTTYPESORT_THERMAL),
			LangModelScheme.getString(SchemeResourceKeys.PORTTYPESORT_ELECTICAL), };

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbSortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.CODENAME));
	JComboBox cmbSortCombo = new AComboBox(this.sorts);
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2, 10);
	JPanel pnGeneralPanel = new JPanel();

	PortTypeKind kind;

	protected PortTypeGeneralPanel() {
		super();
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void setPortKind(final PortTypeKind kind) {
		this.kind = kind;
	}

	protected PortTypeGeneralPanel(final PortType portType) {
		this();
		this.setObject(portType);
	}

	private void jbInit() throws Exception {
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);

		this.lbDescriptionLabel.setFocusable(false);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 5, 0, 2);
		gbPanel0.setConstraints(this.lbDescriptionLabel, gbcPanel0);
		this.pnPanel0.add(this.lbDescriptionLabel);

		final JScrollPane scpDescriptionArea = new JScrollPane(this.taDescriptionArea);
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints(scpDescriptionArea, gbcPanel0);
		this.pnPanel0.add(scpDescriptionArea);

		final GridBagLayout gbGeneralPanel = new GridBagLayout();
		final GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		this.pnGeneralPanel.setLayout(gbGeneralPanel);

		this.lbNameLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbNameLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbNameLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfNameText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfNameText);

		gbcGeneralPanel.gridx = 3;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.commitButton, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.commitButton);

		this.lbSortLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbSortLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbSortLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.cmbSortCombo, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.cmbSortCombo);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);

		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		// pnGeneralPanel.setBackground(Color.WHITE);
		// pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);

		super.addToUndoableListener(this.tfNameText);
		super.addToUndoableListener(this.cmbSortCombo);
		super.addToUndoableListener(this.taDescriptionArea);

		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				PortTypeGeneralPanel.this.commitChanges();
			}
		});
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.portType;
	}

	public void setObject(final Object or) {
		this.portType = (PortType) or;

		if (this.portType != null) {
			this.tfNameText.setText(this.portType.getName());
			this.taDescriptionArea.setText(this.portType.getDescription());
			switch (this.portType.getSort().value()) {
				case PortTypeSort._PORTTYPESORT_OPTICAL:
					this.cmbSortCombo.setSelectedItem(this.sorts[0]);
					break;
				case PortTypeSort._PORTTYPESORT_THERMAL:
					this.cmbSortCombo.setSelectedItem(this.sorts[1]);
					break;
				case PortTypeSort._PORTTYPESORT_ELECTRICAL:
					this.cmbSortCombo.setSelectedItem(this.sorts[2]);
			}
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
		}
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (MiscUtil.validName(this.tfNameText.getText())) {
			if (this.portType == null) {
				try {
					this.portType = SchemeObjectsFactory.createPortType(this.tfNameText.getText(), this.kind);
					this.apply();
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this,
							this.portType,
							PortTypePropertiesManager.getInstance(this.aContext, this.kind),
							ObjectSelectedEvent.PORT_TYPE));
				} catch (CreateObjectException e) {
					Log.errorMessage(e);
					return;
				}
			} else {
				this.apply();
			}
		}
	}

	private void apply() {
		this.portType.setName(this.tfNameText.getText());
		this.portType.setDescription(this.taDescriptionArea.getText());

		if (this.cmbSortCombo.getSelectedItem().equals(this.sorts[0])) {
			this.portType.setSort(PortTypeSort.PORTTYPESORT_OPTICAL);
		} else if (this.cmbSortCombo.getSelectedItem().equals(this.sorts[1])) {
			this.portType.setSort(PortTypeSort.PORTTYPESORT_THERMAL);
		} else if (this.cmbSortCombo.getSelectedItem().equals(this.sorts[2])) {
			this.portType.setSort(PortTypeSort.PORTTYPESORT_ELECTRICAL);
		}

		try {
			StorableObjectPool.flush(this.portType.getId(), LoginManager.getUserId(), true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.portType.getId(), SchemeEvent.UPDATE_OBJECT));
	}
}
