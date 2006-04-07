/*-
 * $Id: ProtoEquipmentGeneralPanel.java,v 1.11 2006/04/07 13:53:02 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import com.syrus.AMFICOM.client.UI.NameableListCellRenderer;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2006/04/07 13:53:02 $
 * @module schemeclient
 */

public class ProtoEquipmentGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected ProtoEquipment protoEq;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbCodenameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.CODENAME));
	AComboBox tfEqtCombo;
	JLabel lbManufacturerLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
	JTextField tfManufacturerText = new JTextField();
	JLabel lbManufacturerCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
	JTextField tfManufacturerCodeText = new JTextField();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2, 10);
	JPanel pnGeneralPanel = new JPanel();

	protected ProtoEquipmentGeneralPanel() {
		super();

		final Set<EquipmentType> eqts = new HashSet<EquipmentType>();
		try {
			eqts.addAll(EquipmentType.values());
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
		Bug136Remover.removeEquipmentTypeBug136(eqts);
		this.tfEqtCombo = new AComboBox(eqts.toArray(new EquipmentType[eqts.size()]));
		
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);
		
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
		gbcGeneralPanel.gridwidth = 3;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfNameText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfNameText);
		
		gbcGeneralPanel.gridx = 5;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.commitButton, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.commitButton);
		
		this.lbCodenameLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbCodenameLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbCodenameLabel);
		
		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfEqtCombo, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfEqtCombo);
		
		this.lbManufacturerLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbManufacturerLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbManufacturerLabel);
		
		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfManufacturerText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfManufacturerText);
		
		this.lbManufacturerCodeLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbManufacturerCodeLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbManufacturerCodeLabel);
		
		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfManufacturerCodeText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfManufacturerCodeText);
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 6;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);
		
		this.lbDescriptionLabel.setFocusable(false);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 6;
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
		gbcPanel0.gridy = 7;
		gbcPanel0.gridwidth = 5;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints(scpDescriptionArea, gbcPanel0);
		this.pnPanel0.add(scpDescriptionArea);
		
		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		this.tfEqtCombo.setRenderer(new NameableListCellRenderer());
		this.tfEqtCombo.setFontSize(this.tfEqtCombo.getFont().getSize() - 2);
		
		this.addToUndoableListener(this.tfNameText);
		this.addToUndoableListener(this.tfEqtCombo);
		this.addToUndoableListener(this.tfManufacturerText);
		this.addToUndoableListener(this.tfManufacturerCodeText);
		this.addToUndoableListener(this.taDescriptionArea);
		
		this.commitButton.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				ProtoEquipmentGeneralPanel.this.commitChanges();
			}
		});
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	protected ProtoEquipmentGeneralPanel(final ProtoEquipment protoEquipment) {
		this();
		this.setObject(protoEquipment);
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.protoEq;
	}

	public void setObject(final Object or) {
		this.protoEq = (ProtoEquipment) or;

		if (this.protoEq != null) {
			this.tfNameText.setText(this.protoEq.getName());
			this.taDescriptionArea.setText(this.protoEq.getDescription());
			this.tfManufacturerText.setText(this.protoEq.getManufacturer());
			this.tfManufacturerCodeText.setText(this.protoEq.getManufacturerCode());
			try {
				this.tfEqtCombo.setSelectedItem(this.protoEq.getType());
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (MiscUtil.validName(this.tfNameText.getText())) {
			if (this.protoEq == null) {
				try {
					final String name = this.tfNameText.getText();
					EquipmentType eq = (EquipmentType)this.tfEqtCombo.getSelectedItem();
					this.protoEq = SchemeObjectsFactory.createProtoEquipment(name, eq);
					this.apply();
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this,
							this.protoEq,
							ProtoEquipmentPropertiesManager.getInstance(this.aContext),
							ObjectSelectedEvent.PROTO_EQUIPMENT));
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
		this.protoEq.setName(this.tfNameText.getText());
		this.protoEq.setDescription(this.taDescriptionArea.getText());
		this.protoEq.setManufacturer(this.tfManufacturerText.getText());
		this.protoEq.setManufacturerCode(this.tfManufacturerCodeText.getText());
		this.protoEq.setType((EquipmentType)this.tfEqtCombo.getSelectedItem());

		try {
			StorableObjectPool.flush(this.protoEq.getId(), LoginManager.getUserId(), true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.protoEq.getId(), SchemeEvent.UPDATE_OBJECT));
	}
}
