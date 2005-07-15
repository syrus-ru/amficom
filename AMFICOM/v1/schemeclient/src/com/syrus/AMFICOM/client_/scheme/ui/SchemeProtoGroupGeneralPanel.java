/*-
 * $Id: SchemeProtoGroupGeneralPanel.java,v 1.2 2005/07/15 13:07:57 stas Exp $
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
import java.util.Collections;
import java.util.HashSet;
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

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/07/15 13:07:57 $
 * @module schemeclient_v1
 */

public class SchemeProtoGroupGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeProtoGroup schemeProtoGroup;
	private Identifier imageId;
	private String NON_GROUP_ITEM = "nonGroupItem";
	
	JPanel panel0 = new JPanel();
	JPanel generalPanel = new JPanel();
	JLabel nameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField nameText = new JTextField();
	JButton commitButton = new JButton();
	JButton symbolBut = new JButton();
	JLabel lbParentLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PARENT_GROUP));
	WrapperedComboBox parentCombo = new WrapperedComboBox(SchemeProtoGroupWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel descrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2,10);
	
	protected SchemeProtoGroupGeneralPanel() {
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

	protected SchemeProtoGroupGeneralPanel(SchemeProtoGroup schemeProtoGroup) {
		this();
		setObject(schemeProtoGroup);
	}

	private void jbInit() throws Exception {
		GridBagLayout gbpanel0 = new GridBagLayout();
		GridBagConstraints gbcpanel0 = new GridBagConstraints();
		panel0.setLayout(gbpanel0);

		GridBagLayout gbgeneralPanel = new GridBagLayout();
		GridBagConstraints gbcgeneralPanel = new GridBagConstraints();
		generalPanel.setLayout(gbgeneralPanel);

		nameLabel.setFocusable(false);
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
		gbgeneralPanel.setConstraints(symbolBut, gbcgeneralPanel);
		generalPanel.add(symbolBut);
		
		gbcgeneralPanel.gridx = 7;
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
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(lbParentLabel, gbcgeneralPanel);
		generalPanel.add(lbParentLabel);
		
		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 6;
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

		descrLabel.setFocusable(false);
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
		
		generalPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
		descrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(this.nameText);
		addToUndoableListener(this.symbolBut);
		addToUndoableListener(this.parentCombo);
		addToUndoableListener(this.descrArea);
			
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
		return panel0; 
	}

	public Object getObject() {
		return schemeProtoGroup;
	}

	public void setObject(Object or) {
		this.schemeProtoGroup = (SchemeProtoGroup) or;
		Icon symbol = null;
		
		parentCombo.removeAllItems();
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
		
		try {
			Set<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			groups.remove(this.schemeProtoGroup);
			List<SchemeProtoGroup> sortedGroups = new LinkedList<SchemeProtoGroup>(groups);
			Collections.sort(sortedGroups, new WrapperComparator(SchemeProtoGroupWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
			parentCombo.addElements(sortedGroups);
			parentCombo.addItem(NON_GROUP_ITEM);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		
		if (this.schemeProtoGroup != null) {
			this.nameText.setText(schemeProtoGroup.getName());
			this.descrArea.setText(schemeProtoGroup.getDescription());
			if (schemeProtoGroup.getSymbol() != null)
				symbol = new ImageIcon(schemeProtoGroup.getSymbol().getImage());
			parentCombo.setSelectedItem(schemeProtoGroup.getParent());
		} else {
			this.nameText.setText(SchemeResourceKeys.EMPTY);
			this.descrArea.setText(SchemeResourceKeys.EMPTY);
		}
		this.symbolBut.setIcon(symbol);
	}

	public void commitChanges() {
		if (MiscUtil.validName(this.nameText.getText())) {
			if (this.schemeProtoGroup == null) {
				try {
					this.schemeProtoGroup = SchemeObjectsFactory.createSchemeProtoGroup();
					apply();
					aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemeProtoGroup, SchemeEvent.CREATE_OBJECT));
					aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, this.schemeProtoGroup, SchemeProtoGroupPropertiesManager.getInstance(aContext), ObjectSelectedEvent.SCHEME_PROTOGROUP));
				} 
				catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			} else {
				apply();
				aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemeProtoGroup, SchemeEvent.UPDATE_OBJECT));
				aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, this.schemeProtoGroup, SchemeProtoGroupPropertiesManager.getInstance(aContext), ObjectSelectedEvent.SCHEME_PROTOGROUP));
			}
		}
		/*try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
			Set objects = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			Set ids = new HashSet();
			for (Iterator iter = objects.iterator(); iter.hasNext();) {
				StorableObject element = (StorableObject) iter.next();
				ids.add(element.getId());
			}
			StorableObjectPool.delete(ids);
			StorableObjectPool.flush(ObjectEntities.SCHEMEPROTOGROUP_CODE, true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void apply() {
		this.schemeProtoGroup.setName(this.nameText.getText());
		this.schemeProtoGroup.setDescription(this.descrArea.getText());
		if (this.symbolBut.getIcon() == null) {
			this.schemeProtoGroup.setSymbol(null);
		} else {
			try {
				this.schemeProtoGroup.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(imageId, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
		
		Object parent = this.parentCombo.getSelectedItem();
		if (parent == null || parent.equals(NON_GROUP_ITEM)) {
			this.schemeProtoGroup.setParentSchemeProtoGroup(null);
		} else {
			this.schemeProtoGroup.setParentSchemeProtoGroup((SchemeProtoGroup)parent);	
		}
		try {
			StorableObjectPool.flush(this.schemeProtoGroup.getId(), false);
			
//			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
//			Set objects = StorableObjectPool.getStorableObjectsByCondition(condition, true);
//			Set ids = new HashSet();
			
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
	}
}
