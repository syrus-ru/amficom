/*
 * $Id: MeasurementPortTypeGeneralPanel.java,v 1.14 2005/06/17 11:36:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.tree.*;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.Constants;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/06/17 11:36:21 $
 * @module schemeclient_v1
 */

public class MeasurementPortTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected MeasurementPortType type;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(Constants.NAME));
	JTextField tfNameText = new JTextField();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(Constants.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JLabel lbTestTypeLabel = new JLabel(LangModelScheme.getString(Constants.MEASUREMENT_TYPES));
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

	private void jbInit() throws Exception {
		root = createRoot();
		CheckableTreeUI treeUI = new CheckableTreeUI(root); 
		trTestTypeTree = treeUI.getTree();
		trTestTypeTree.setRootVisible(false);
		measurementTypeNodes = getMeasurementTypeNodes();
	
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
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,0 );
		gbGeneralPanel.setConstraints( tfNameText, gbcGeneralPanel );
		pnGeneralPanel.add( tfNameText );
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
		
		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( Constants.EMPTY ));
//		pnPanel0.setBackground(Color.WHITE);
//		pnGeneralPanel.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		scpTestTypeTree.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(taDescriptionArea);
		addToUndoableListener(trTestTypeTree);
	}

	public JComponent getGUI() {
		return pnPanel0; 
	}
	
	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (MeasurementPortType)or;

		if (this.type != null) {
			this.tfNameText.setText(type.getName());
			this.taDescriptionArea.setText(type.getDescription());

			try {
				LinkedIdsCondition condition = new LinkedIdsCondition(type.getId(),
						ObjectEntities.MEASUREMENT_TYPE_CODE);
				Collection mPTypes = StorableObjectPool.getStorableObjectsByCondition(
						condition, true);
				
				for (Iterator it = measurementTypeNodes.iterator(); it.hasNext();) {
					CheckableNode node = (CheckableNode)it.next();
					node.setChecked(mPTypes.contains(node.getObject()));
				}
				trTestTypeTree.updateUI();
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			this.tfNameText.setText(Constants.EMPTY);
			this.taDescriptionArea.setText(Constants.EMPTY);
			for (Iterator it = measurementTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(false);
			}
			trTestTypeTree.updateUI();
		}
	}
	
	List getMeasurementTypeNodes() {
		return root.getChildren();
	}

	public void commitChanges() {
		if (MiscUtil.validName(tfNameText.getText())) {
			if (type == null) {
				try {
					type = SchemeObjectsFactory.createMeasurementPortType(tfNameText.getText());
					aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, type, SchemeEvent.CREATE_OBJECT));
				} 
				catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}

			this.type.setName(tfNameText.getText());
			this.type.setDescription(this.taDescriptionArea.getText());

			for (Iterator it = measurementTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				MeasurementType mtype = (MeasurementType) node.getObject();
				if (node.isChecked()) {
					Collection pTypes = mtype.getMeasurementPortTypeIds();
					if (!pTypes.contains(type.getId())) {
						Set newPTypes = new HashSet(pTypes);
						newPTypes.add(type.getId());
						mtype.setMeasurementPortTypeIds(newPTypes);
					}
				} else {
					Collection pTypes = mtype.getMeasurementPortTypeIds();
					// TODO add/remove MeasurementPortType to/from MeasurementType
					if (pTypes.contains(type.getId())) {
						Set newPTypes = new HashSet(pTypes);
						newPTypes.remove(type.getId());
						mtype.setMeasurementPortTypeIds(newPTypes);
					}
				}
			}
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, type, SchemeEvent.UPDATE_OBJECT));
		}
	}
	
	Item createRoot() {
		Item root1 = new IconedNode(Constants.ROOT, LangModelScheme.getString(Constants.ROOT));
		
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
