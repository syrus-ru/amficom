/*-
 * $Id: SchemeProtoElementGeneralPanel.java,v 1.18 2005/08/19 15:41:35 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

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
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.EquipmentTypeCodenames;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: stas $
 * @version $Revision: 1.18 $, $Date: 2005/08/19 15:41:35 $
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
	JComboBox codenameCombo = new AComboBox();
	JLabel typeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox typeCombo = new WrapperedComboBox(EquipmentTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel manufacturerLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
	JTextField manufacturerText = new JTextField();
	JLabel manufacturerCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
	JTextField manufacturerCodeText = new JTextField();
	JLabel lbParentLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PARENT_GROUP));
	WrapperedComboBox parentCombo = new WrapperedComboBox(SchemeProtoGroupWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel descrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2,10);
	
	protected SchemeProtoElementGeneralPanel() {
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

	protected SchemeProtoElementGeneralPanel(SchemeProtoElement schemeProtoElement) {
		this();
		setObject(schemeProtoElement);
	}

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		GridBagLayout gbpanel0 = new GridBagLayout();
		GridBagConstraints gbcpanel0 = new GridBagConstraints();
		panel0.setLayout(gbpanel0);

		GridBagLayout gbgeneralPanel = new GridBagLayout();
		GridBagConstraints gbcgeneralPanel = new GridBagConstraints();
		generalPanel.setLayout(gbgeneralPanel);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.insets = new Insets( 0,0,0,0 );
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(nameLabel, gbcgeneralPanel);
		generalPanel.add(nameLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(nameText, gbcgeneralPanel);
		generalPanel.add(nameText);
		
		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(commitButton, gbcgeneralPanel);
		generalPanel.add(commitButton);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(symbolLabel, gbcgeneralPanel);
		generalPanel.add(symbolLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(labelText, gbcgeneralPanel);
		generalPanel.add(labelText);

		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(symbolBut, gbcgeneralPanel);
		generalPanel.add(symbolBut);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(lbCodenameLabel, gbcgeneralPanel);
		generalPanel.add(lbCodenameLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(codenameCombo, gbcgeneralPanel);
		generalPanel.add(codenameCombo);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(typeLabel, gbcgeneralPanel);
		generalPanel.add(typeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(typeCombo, gbcgeneralPanel);
		generalPanel.add(typeCombo);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerLabel, gbcgeneralPanel);
		generalPanel.add(manufacturerLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerText, gbcgeneralPanel);
		generalPanel.add(manufacturerText);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerCodeLabel, gbcgeneralPanel);
		generalPanel.add(manufacturerCodeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(manufacturerCodeText, gbcgeneralPanel);
		generalPanel.add(manufacturerCodeText);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(lbParentLabel, gbcgeneralPanel);
		generalPanel.add(lbParentLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(parentCombo, gbcgeneralPanel);
		generalPanel.add(parentCombo);
		
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 0;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 7;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(generalPanel, gbcpanel0);
		panel0.add(generalPanel);

		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 7;
		gbcpanel0.gridwidth = 3;
		gbcpanel0.gridheight = 1;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 0;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(descrLabel, gbcpanel0);
		panel0.add(descrLabel);

		JScrollPane scpdescrArea = new JScrollPane(descrArea);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 8;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 3;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 1;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(scpdescrArea, gbcpanel0);
		panel0.add(scpdescrArea);
		
//		typeCombo.setEnabled(false);
//		manufacturerText.setEnabled(false);
//		manufacturerCodeText.setEnabled(false);
		codenameCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				codeNameCombo_stateChanged((String)e.getItem());
			}
		});
		typeCombo.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				typeCombo_stateChanged((EquipmentType)e.getItem());
			}
		});
		generalPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
		descrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(this.nameText);
		addToUndoableListener(this.labelText);
		addToUndoableListener(this.symbolBut);
		addToUndoableListener(this.codenameCombo);
		addToUndoableListener(this.typeCombo);
		addToUndoableListener(this.manufacturerText);
		addToUndoableListener(this.manufacturerCodeText);
		addToUndoableListener(this.parentCombo);
		addToUndoableListener(this.descrArea);
		
		for (int i = 0; i < EquipmentTypeCodenames.DEFAULT_CODENAMES.length; i++) {
			this.codenameCombo.addItem(EquipmentTypeCodenames.DEFAULT_CODENAMES[i].stringValue());			
		}
		this.codenameCombo.setRenderer(EquipmentTypeCodenames.getListCellRenderer());
		
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
	
	void codeNameCombo_stateChanged(String eqtCodename) {
		this.typeCombo.removeAllItems();

		TypicalCondition condition = new TypicalCondition(eqtCodename, OperationSort.OPERATION_EQUALS, ObjectEntities.EQUIPMENT_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
//		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
		try {
			this.typeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
		} catch (ApplicationException e1) {
			Log.errorException(e1);
		}
	}
	
	void typeCombo_stateChanged(EquipmentType eqt) {
		if (eqt != null) {
			this.manufacturerText.setText(eqt.getManufacturer());
			this.manufacturerCodeText.setText(eqt.getManufacturerCode());
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

	public void setObject(Object or) {
		this.schemeProtoElement = (SchemeProtoElement) or;
		EquipmentType eqt = null;
		Icon symbol = null;
		
		this.parentCombo.removeAllItems();
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
		try {
			Set<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			List<SchemeProtoGroup> sortedGroups = new LinkedList<SchemeProtoGroup>(groups);
			Collections.sort(sortedGroups, new WrapperComparator<SchemeProtoGroup>(SchemeProtoGroupWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
			this.parentCombo.addElements(sortedGroups);
//			parentCombo.addItem(NON_GROUP_ITEM);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		
		this.typeCombo.removeAllItems();

		if (this.schemeProtoElement != null) {
			this.nameText.setText(this.schemeProtoElement.getName());
			this.descrArea.setText(this.schemeProtoElement.getDescription());
			this.labelText.setText(this.schemeProtoElement.getLabel());
			if (this.schemeProtoElement.getSymbol() != null)
				symbol = new ImageIcon(this.schemeProtoElement.getSymbol().getImage());
			eqt = this.schemeProtoElement.getEquipmentType();
			
			try {
				if (this.schemeProtoElement.getParentSchemeProtoElement() != null) {
					this.parentCombo.addItem(null);
					this.parentCombo.setSelectedItem(null);
					this.parentCombo.setEnabled(false);
				} else {
					SchemeProtoGroup parent = this.schemeProtoElement.getParentSchemeProtoGroup();
					if (parent != null)
						this.parentCombo.setSelectedItem(parent);
					this.parentCombo.setEnabled(true);
				}
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
		
		if (eqt != null) {
			this.codenameCombo.setEnabled(true);
			this.typeCombo.setEnabled(true);
			this.codenameCombo.setSelectedItem(eqt.getCodename());
			codeNameCombo_stateChanged(eqt.getCodename());
			this.typeCombo.setSelectedItem(eqt);
			typeCombo_stateChanged(eqt);
		} else {
			this.codenameCombo.setEnabled(false);
			this.typeCombo.setEnabled(false);
			this.manufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.manufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
		this.symbolBut.setIcon(symbol);
	}

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
					this.schemeProtoElement.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(this.imageId, true));
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}

			EquipmentType eqt = (EquipmentType)this.typeCombo.getSelectedItem();
			if (eqt != null) {
				this.schemeProtoElement.setEquipmentType(eqt);
			}
			Object parent = this.parentCombo.getSelectedItem();
			if (parent != null) {
				this.schemeProtoElement.setParentSchemeProtoGroup((SchemeProtoGroup)parent);	
			}			
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemeProtoElement.getId(), SchemeEvent.UPDATE_OBJECT));
			
//			try {
//				StorableObjectPool.delete(schemeProtoElement.getId());
//				StorableObjectPool.flush(schemeProtoElement.getId(), true);
//			} catch (ApplicationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
