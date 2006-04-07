/*-
 * $Id: SchemeProtoElementGeneralPanel.java,v 1.29 2006/04/07 13:53:02 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import static com.syrus.AMFICOM.configuration.EquipmentTypeCodename.BUG_136;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.NameableListCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.ProtoEquipmentWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.29 $, $Date: 2006/04/07 13:53:02 $
 * @module schemeclient
 */

public class SchemeProtoElementGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeProtoElement schemeProtoElement;
	private Identifier imageId;

	JPanel panel0 = new JPanel();
	JPanel generalPanel = new JPanel();
	JLabel nameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField nameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel symbolLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField labelText = new JTextField();
	JButton symbolBut = new JButton();
	JLabel lbCodenameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.CODENAME));
	AComboBox eqtCombo;
	JLabel typeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox<ProtoEquipment> typeCombo = new WrapperedComboBox<ProtoEquipment>(ProtoEquipmentWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel manufacturerLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
	JTextField manufacturerText = new JTextField();
	JLabel manufacturerCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
	JTextField manufacturerCodeText = new JTextField();
	JLabel lbParentLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PARENT_GROUP));
	WrapperedComboBox<SchemeProtoGroup> parentCombo = new WrapperedComboBox<SchemeProtoGroup>(SchemeProtoGroupWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel descrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2, 10);

	protected SchemeProtoElementGeneralPanel() {
		super();

		try {
			this.eqtCombo = new AComboBox(EquipmentType.valuesArray());
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}

		final GridBagLayout gbpanel0 = new GridBagLayout();
		final GridBagConstraints gbcpanel0 = new GridBagConstraints();
		this.panel0.setLayout(gbpanel0);
		
		final GridBagLayout gbgeneralPanel = new GridBagLayout();
		final GridBagConstraints gbcgeneralPanel = new GridBagConstraints();
		this.generalPanel.setLayout(gbgeneralPanel);
		
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.insets = new Insets(0, 0, 0, 0);
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.nameLabel, gbcgeneralPanel);
		this.generalPanel.add(this.nameLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.nameText, gbcgeneralPanel);
		this.generalPanel.add(this.nameText);
		
		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.commitButton, gbcgeneralPanel);
		this.generalPanel.add(this.commitButton);
		
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.symbolLabel, gbcgeneralPanel);
		this.generalPanel.add(this.symbolLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.labelText, gbcgeneralPanel);
		this.generalPanel.add(this.labelText);
		
		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.symbolBut, gbcgeneralPanel);
		this.generalPanel.add(this.symbolBut);
		
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.lbCodenameLabel, gbcgeneralPanel);
		this.generalPanel.add(this.lbCodenameLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.eqtCombo, gbcgeneralPanel);
		this.generalPanel.add(this.eqtCombo);
		
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.typeLabel, gbcgeneralPanel);
		this.generalPanel.add(this.typeLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.typeCombo, gbcgeneralPanel);
		this.generalPanel.add(this.typeCombo);
		
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.manufacturerLabel, gbcgeneralPanel);
		this.generalPanel.add(this.manufacturerLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.manufacturerText, gbcgeneralPanel);
		this.generalPanel.add(this.manufacturerText);
		
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.manufacturerCodeLabel, gbcgeneralPanel);
		this.generalPanel.add(this.manufacturerCodeLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.manufacturerCodeText, gbcgeneralPanel);
		this.generalPanel.add(this.manufacturerCodeText);
		
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.lbParentLabel, gbcgeneralPanel);
		this.generalPanel.add(this.lbParentLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.parentCombo, gbcgeneralPanel);
		this.generalPanel.add(this.parentCombo);
		
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 0;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 7;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(this.generalPanel, gbcpanel0);
		this.panel0.add(this.generalPanel);
		
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 7;
		gbcpanel0.gridwidth = 3;
		gbcpanel0.gridheight = 1;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 0;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(this.descrLabel, gbcpanel0);
		this.panel0.add(this.descrLabel);
		
		JScrollPane scpdescrArea = new JScrollPane(this.descrArea);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 8;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 3;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 1;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(scpdescrArea, gbcpanel0);
		this.panel0.add(scpdescrArea);
		
		// typeCombo.setEnabled(false);
		// manufacturerText.setEnabled(false);
		// manufacturerCodeText.setEnabled(false);
		this.eqtCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					SchemeProtoElementGeneralPanel.this.eqtCombo_stateChanged((EquipmentType) e.getItem());
				}
			}
		});
		this.typeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				SchemeProtoElementGeneralPanel.this.typeCombo_stateChanged((ProtoEquipment) e.getItem());
			}
		});
		this.generalPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		this.descrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		super.addToUndoableListener(this.nameText);
		super.addToUndoableListener(this.labelText);
		super.addToUndoableListener(this.symbolBut);
		super.addToUndoableListener(this.eqtCombo);
		super.addToUndoableListener(this.typeCombo);
		super.addToUndoableListener(this.manufacturerText);
		super.addToUndoableListener(this.manufacturerCodeText);
		super.addToUndoableListener(this.parentCombo);
		super.addToUndoableListener(this.descrArea);
		
		this.eqtCombo.setRenderer(new NameableListCellRenderer());
		
		this.commitButton.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SchemeProtoElementGeneralPanel.this.commitChanges();
			}
		});
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	protected SchemeProtoElementGeneralPanel(final SchemeProtoElement schemeProtoElement) {
		this();
		this.setObject(schemeProtoElement);
	}

	void eqtCombo_stateChanged(final EquipmentType eqt) {
		this.typeCombo.removeAllItems();

		final LinkedIdsCondition condition = new LinkedIdsCondition(eqt, ObjectEntities.PROTOEQUIPMENT_CODE);
		try {
			final Set<ProtoEquipment> protoEquipments = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			this.typeCombo.addElements(protoEquipments);
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
	}

	void typeCombo_stateChanged(final ProtoEquipment protoEq) {
		if (protoEq != null) {
			this.manufacturerText.setText(protoEq.getManufacturer());
			this.manufacturerCodeText.setText(protoEq.getManufacturerCode());
		} else {
			this.manufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.manufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public JComponent getGUI() {
		return this.panel0;
	}

	public Object getObject() {
		return this.schemeProtoElement;
	}

	public void setObject(final Object or) {
		this.schemeProtoElement = (SchemeProtoElement) or;
		ProtoEquipment protoEq = null;
		Icon symbol = null;

		this.parentCombo.removeAllItems();
		final EquivalentCondition condition = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
		try {
			final Set<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			final List<SchemeProtoGroup> sortedGroups = new LinkedList<SchemeProtoGroup>(groups);
			Collections.sort(sortedGroups, new WrapperComparator<SchemeProtoGroup>(SchemeProtoGroupWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME));
			this.parentCombo.addElements(sortedGroups);
			// parentCombo.addItem(NON_GROUP_ITEM);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}

		this.typeCombo.removeAllItems();

		if (this.schemeProtoElement != null) {
			this.nameText.setText(this.schemeProtoElement.getName());
			this.descrArea.setText(this.schemeProtoElement.getDescription());
			this.labelText.setText(this.schemeProtoElement.getLabel());
			if (this.schemeProtoElement.getSymbol() != null) {
				symbol = new ImageIcon(this.schemeProtoElement.getSymbol().getImage());
			}
			
			try {
				protoEq = this.schemeProtoElement.getProtoEquipment();
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}

			try {
				SchemeProtoElement parentPE = this.schemeProtoElement.getParentSchemeProtoElement();
				if (parentPE != null && parentPE.getProtoEquipment() != null &&
						!parentPE.getProtoEquipment().getType().getCodename().equals(BUG_136.stringValue())) {
					this.parentCombo.addItem(null);
					this.parentCombo.setSelectedItem(null);
					this.parentCombo.setEnabled(false);
				} else {
					final SchemeProtoGroup parent = this.schemeProtoElement.getParentSchemeProtoGroup();
					if (parent != null) {
						this.parentCombo.setSelectedItem(parent);
					}
					this.parentCombo.setEnabled(true);
				}
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
			} catch (IllegalStateException e1) {
				// ignore as it means parent == null
				this.parentCombo.setEnabled(true);
			}
		} else {
			this.nameText.setText(SchemeResourceKeys.EMPTY);
			this.descrArea.setText(SchemeResourceKeys.EMPTY);
			this.labelText.setText(SchemeResourceKeys.EMPTY);

			this.parentCombo.setEnabled(true);
		}

		if (protoEq != null) {
			this.eqtCombo.setEnabled(true);
			this.typeCombo.setEnabled(true);
			try {
				final EquipmentType equipmentType = protoEq.getType();
				this.eqtCombo.setSelectedItem(equipmentType);
				this.eqtCombo_stateChanged(equipmentType);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
			this.typeCombo.setSelectedItem(protoEq);
			this.typeCombo_stateChanged(protoEq);
		} else {
			this.eqtCombo.setEnabled(false);
			this.typeCombo.setEnabled(false);
			this.manufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.manufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
		this.symbolBut.setIcon(symbol);
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.schemeProtoElement != null && MiscUtil.validName(this.nameText.getText())) {
			this.schemeProtoElement.setName(this.nameText.getText());
			this.schemeProtoElement.setDescription(this.descrArea.getText());
			this.schemeProtoElement.setLabel(this.labelText.getText());
			if (this.symbolBut.getIcon() == null) {
				this.schemeProtoElement.setSymbol(null);
			} else {
				try {
					this.schemeProtoElement.setSymbol((BitmapImageResource) StorableObjectPool.getStorableObject(this.imageId, true));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}

			final ProtoEquipment protoEq = (ProtoEquipment)this.typeCombo.getSelectedItem();
			if (protoEq != null) {
				this.schemeProtoElement.setProtoEquipment(protoEq);
			}
			final Object parent = this.parentCombo.getSelectedItem();
			if (parent != null) {
				try {
					this.schemeProtoElement.setParentSchemeProtoGroup((SchemeProtoGroup) parent, false);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this,
					this.schemeProtoElement.getId(),
					SchemeEvent.UPDATE_OBJECT));

			// try {
			// StorableObjectPool.delete(schemeProtoElement.getId());
			// StorableObjectPool.flush(schemeProtoElement.getId(), true);
			// } catch (ApplicationException e) {
			// // TODO Auto-generated catch block
			// Log.errorMessage(e);
			//			}
		}
	}
}
