/*-
 * $Id: SchemeCablePortGeneralPanel.java,v 1.21 2005/10/31 12:30:28 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.logging.Level;

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

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.21 $, $Date: 2005/10/31 12:30:28 $
 * @module schemeclient
 */

public class SchemeCablePortGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeCablePort schemePort;
	protected SchemeElement parent;

	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JPanel pnPortPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton btCommitBut = new JButton();
	JLabel lbTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox<PortType> cmbTypeCombo = new WrapperedComboBox<PortType>(PortTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel lbPortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.INSTANCE));
	JLabel lbMarkLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField tfMarkText = new JTextField();
	JLabel lbColorLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.COLOR));
	JComboBox cmbColorCombo = new ColorChooserComboBox();
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2, 10);

	protected SchemeCablePortGeneralPanel(final SchemeCablePort schemePort) {
		this();
		this.setObject(schemePort);
	}

	protected SchemeCablePortGeneralPanel() {
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

	private void jbInit() throws Exception {
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 8;
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.lbDescrLabel, gbcPanel0);
		this.pnPanel0.add(this.lbDescrLabel);

		final JScrollPane scpDescrArea = new JScrollPane(this.taDescrArea);
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 9;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(scpDescrArea, gbcPanel0);
		this.pnPanel0.add(scpDescrArea);

		final GridBagLayout gbGeneralPanel = new GridBagLayout();
		final GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		this.pnGeneralPanel.setLayout(gbGeneralPanel);

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbNameLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbNameLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 7;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.tfNameText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfNameText);

		gbcGeneralPanel.gridx = 9;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.btCommitBut, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.btCommitBut);

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbTypeLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbTypeLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.cmbTypeCombo, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.cmbTypeCombo);

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 9;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbPortLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbPortLabel);

		this.pnPortPanel.setBorder(BorderFactory.createTitledBorder(""));
		final GridBagLayout gbPortPanel = new GridBagLayout();
		final GridBagConstraints gbcPortPanel = new GridBagConstraints();
		this.pnPortPanel.setLayout(gbPortPanel);

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 0;
		gbcPortPanel.gridwidth = 2;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.lbMarkLabel, gbcPortPanel);
		this.pnPortPanel.add(this.lbMarkLabel);

		gbcPortPanel.gridx = 2;
		gbcPortPanel.gridy = 0;
		gbcPortPanel.gridwidth = 8;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 1;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.tfMarkText, gbcPortPanel);
		this.pnPortPanel.add(this.tfMarkText);

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 1;
		gbcPortPanel.gridwidth = 2;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.lbColorLabel, gbcPortPanel);
		this.pnPortPanel.add(this.lbColorLabel);

		gbcPortPanel.gridx = 2;
		gbcPortPanel.gridy = 1;
		gbcPortPanel.gridwidth = 8;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 1;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.cmbColorCombo, gbcPortPanel);
		this.pnPortPanel.add(this.cmbColorCombo);

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 10;
		gbcGeneralPanel.gridheight = 5;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.pnPortPanel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.pnPortPanel);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 11;
		gbcPanel0.gridheight = 8;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);

		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		this.taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);

		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		this.taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);

		super.addToUndoableListener(this.tfNameText);
		super.addToUndoableListener(this.cmbTypeCombo);
		super.addToUndoableListener(this.lbPortLabel);
		super.addToUndoableListener(this.tfMarkText);
		super.addToUndoableListener(this.cmbColorCombo);
		super.addToUndoableListener(this.taDescrArea);

		this.btCommitBut.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.btCommitBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.btCommitBut.setFocusPainted(false);
		this.btCommitBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.btCommitBut.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SchemeCablePortGeneralPanel.this.commitChanges();
			}
		});
	}

	void setPortEnabled(final boolean b) {
		this.lbPortLabel.setVisible(b);
		this.pnPortPanel.setVisible(b);
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public void setObject(final Object or) {
		this.schemePort = (SchemeCablePort) or;
		Port port = null;
		this.cmbTypeCombo.removeAllItems();

		if (this.schemePort != null) {
			try {
				this.parent = this.schemePort.getParentSchemeDevice().getParentSchemeElement();
				port = this.schemePort.getPort();
			} catch (IllegalStateException e1) {
				Log.debugMessage("SchemeDevice has no parent SchemeElement yet", Level.FINEST); //$NON-NLS-1$
				this.parent = null;
			}
			final TypicalCondition condition1 = new TypicalCondition(PortTypeKind._PORT_KIND_CABLE,
					0,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PORT_TYPE_CODE,
					PortTypeWrapper.COLUMN_KIND);
			try {
				final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
				this.cmbTypeCombo.addElements(portTypes);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}

			this.tfNameText.setText(this.schemePort.getName());
			this.taDescrArea.setText(this.schemePort.getDescription());
			this.cmbTypeCombo.setSelectedItem(this.schemePort.getPortType());
		}
		if (port != null) {
			this.setPortEnabled(true);
			// TODO add mark and color fields to Port
			// markText.setText(port.getMark());
			// Color color = new Color(port.getColor());
			// if (!isConatainsColor(color))
			// colorCombo.addItem(color);
			// colorCombo.setSelectedItem(color);
		} else {
			this.setPortEnabled(false);
			this.tfMarkText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public Object getObject() {
		return this.schemePort;
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.schemePort != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemePort.setName(this.tfNameText.getText());
			this.schemePort.setDescription(this.taDescrArea.getText());
			this.schemePort.setPortType((PortType) this.cmbTypeCombo.getSelectedItem());

			final Port port = this.schemePort.getPort();
			if (port != null) {
				port.setDescription(this.schemePort.getDescription());
				// TODO add mark and color fields to Port
				// port.setMark(markText.getText());
				// port.setColor(((Color) colorCombo.getSelectedItem()).getRGB());
			}
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemePort.getId(), SchemeEvent.UPDATE_OBJECT));
		}
	}
}
