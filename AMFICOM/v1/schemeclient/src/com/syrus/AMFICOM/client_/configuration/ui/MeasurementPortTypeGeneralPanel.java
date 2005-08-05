/*
 * $Id: MeasurementPortTypeGeneralPanel.java,v 1.19 2005/08/05 12:39:58 stas Exp $
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.tree.CheckableNode;
import com.syrus.AMFICOM.client.UI.tree.CheckableTreeUI;
import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.19 $, $Date: 2005/08/05 12:39:58 $
 * @module schemeclient_v1
 */

public class MeasurementPortTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected MeasurementPortType type;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JLabel lbTestTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MEASUREMENT_TYPES));
	JPanel pnGeneralPanel = new JPanel();
	JTree trTestTypeTree;
	List measurementTypeNodes;
	Item root;
		
	protected MeasurementPortTypeGeneralPanel() {
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

	protected MeasurementPortTypeGeneralPanel(MeasurementPortType apt) {
		this();
		setObject(apt);
	}

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		this.root = createRoot();
		CheckableTreeUI treeUI = new CheckableTreeUI(this.root); 
		this.trTestTypeTree = treeUI.getTree();
		this.trTestTypeTree.setRootVisible(false);
		this.measurementTypeNodes = getMeasurementTypeNodes();
	
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

		lbDescriptionLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
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
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,2,0,2 );
		gbPanel0.setConstraints( scpDescriptionArea, gbcPanel0 );
		pnPanel0.add( scpDescriptionArea );

		lbTestTypeLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 4;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,5,0,2 );
		gbPanel0.setConstraints( lbTestTypeLabel, gbcPanel0 );
		pnPanel0.add( lbTestTypeLabel );

		JScrollPane scpTestTypeTree = new JScrollPane( trTestTypeTree );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 5;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,2,0,2 );
		gbPanel0.setConstraints( scpTestTypeTree, gbcPanel0 );
		pnPanel0.add( scpTestTypeTree );

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
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,0,0,0 );
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );
		
		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
//		pnPanel0.setBackground(Color.WHITE);
//		pnGeneralPanel.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		scpTestTypeTree.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(taDescriptionArea);
		addToUndoableListener(trTestTypeTree);
		
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
		return this.pnPanel0; 
	}
	
	public Object getObject() {
		return this.type;
	}

	public void setObject(Object or) {
		this.type = (MeasurementPortType)or;

		if (this.type != null) {
			this.tfNameText.setText(this.type.getName());
			this.taDescriptionArea.setText(this.type.getDescription());

			try {
				LinkedIdsCondition condition = new LinkedIdsCondition(this.type.getId(), ObjectEntities.MEASUREMENT_TYPE_CODE);
				Collection mPTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				
				for (Iterator it = this.measurementTypeNodes.iterator(); it.hasNext();) {
					CheckableNode node = (CheckableNode)it.next();
					node.setChecked(mPTypes.contains(node.getObject()));
				}
				this.trTestTypeTree.updateUI();
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			for (Iterator it = this.measurementTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(false);
			}
			this.trTestTypeTree.updateUI();
		}
	}
	
	List getMeasurementTypeNodes() {
		return this.root.getChildren();
	}

	public void commitChanges() {
		if (MiscUtil.validName(this.tfNameText.getText())) {
			if (this.type == null) {
				try {
					this.type = SchemeObjectsFactory.createMeasurementPortType(this.tfNameText.getText());
					apply();
					this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.type, SchemeEvent.CREATE_OBJECT));
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, this.type, MeasurementPortTypePropertiesManager.getInstance(this.aContext), ObjectSelectedEvent.MEASUREMENTPORT_TYPE));
				} 
				catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			} else {
				apply();
			}
		}
	}
	
	private void apply() {
		this.type.setName(this.tfNameText.getText());
		this.type.setDescription(this.taDescriptionArea.getText());

		for (Iterator it = this.measurementTypeNodes.iterator(); it.hasNext();) {
			CheckableNode node = (CheckableNode)it.next();
			MeasurementType mtype = (MeasurementType) node.getObject();
			if (node.isChecked()) {
				Collection<Identifier> pTypes = mtype.getMeasurementPortTypeIds();
				if (!pTypes.contains(this.type.getId())) {
					Set<Identifier> newPTypes = new HashSet<Identifier>(pTypes);
					newPTypes.add(this.type.getId());
					mtype.setMeasurementPortTypeIds(newPTypes);
				}
			} else {
				Collection<Identifier> pTypes = mtype.getMeasurementPortTypeIds();
				// TODO add/remove MeasurementPortType to/from MeasurementType
				if (pTypes.contains(this.type.getId())) {
					Set<Identifier> newPTypes = new HashSet<Identifier>(pTypes);
					newPTypes.remove(this.type.getId());
					mtype.setMeasurementPortTypeIds(newPTypes);
				}
			}
		}
		try {
			StorableObjectPool.flush(this.type.getId(), LoginManager.getUserId(), true);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.type, SchemeEvent.UPDATE_OBJECT));
	}
	
	Item createRoot() {
		Item root1 = new IconedNode(SchemeResourceKeys.ROOT, LangModelScheme.getString(SchemeResourceKeys.ROOT));
		
		EquivalentCondition condition = new EquivalentCondition(
				ObjectEntities.MEASUREMENT_TYPE_CODE);
		try {
			Collection allMPTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (Iterator it = allMPTypes.iterator(); it.hasNext();) {
				MeasurementType t = (MeasurementType) it.next();
					root1.addChild(new CheckableNode(t, false));
			}
		} 
		catch (ApplicationException e) {
			Log.errorException(e);
		}
		return root1;
	}
}
