/*
 * $Id: MeasurementTypeGeneralPanel.java,v 1.6 2005/04/18 10:53:40 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.tree_.*;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.Constants;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/04/18 10:53:40 $
 * @module schemeclient_v1
 */

public class MeasurementTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected MeasurementType type;
	
	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(Constants.NAME));
	JTextField tfNameText = new JTextField();
	JLabel lbParametersLabel = new JLabel(LangModelScheme.getString(Constants.PARAMETERS));
	Item parametersRoot;
	JTree trParametersTree;
	JLabel lbPortTypesLabel = new JLabel(LangModelScheme.getString(Constants.MEASUREMENT_PORT_TYPES));
	Item portsRoot;
	JTree trPortTypesTree;
	JPanel pnGeneralPanel = new JPanel();
	List allInPTypeNodes;
	List allOutPTypeNodes;
	List allMPTypeNodes;
	
	protected MeasurementTypeGeneralPanel() {
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

	protected MeasurementTypeGeneralPanel(MeasurementType type) {
		this();
		setObject(type);
	}

	private void jbInit() throws Exception {
		parametersRoot = createParametersRoot();
		CheckableTreeUI parametersTreeUI = new CheckableTreeUI(parametersRoot);
		trParametersTree = parametersTreeUI.getTree();
		trParametersTree.setRootVisible(false);
		
		portsRoot = createPortsRoot();
		CheckableTreeUI portsTreeUI = new CheckableTreeUI(portsRoot);
		trPortTypesTree = portsTreeUI.getTree();
		trPortTypesTree.setRootVisible(false);
				
		allInPTypeNodes = getParameterTypeNodes("input");
		allOutPTypeNodes = getParameterTypeNodes("output");
		allMPTypeNodes = getMeasurementPortTypeNodes();

		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

		lbParametersLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,5,0,2 );
		gbPanel0.setConstraints( lbParametersLabel, gbcPanel0 );
		pnPanel0.add( lbParametersLabel );

		JScrollPane scpParametersTree = new JScrollPane( trParametersTree );
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,2,0,2 );
		gbPanel0.setConstraints( scpParametersTree, gbcPanel0 );
		pnPanel0.add( scpParametersTree );

		lbPortTypesLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 5;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,5,0,2 );
		gbPanel0.setConstraints( lbPortTypesLabel, gbcPanel0 );
		pnPanel0.add( lbPortTypesLabel );

		JScrollPane scpPortTypesTree = new JScrollPane( trPortTypesTree );
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 6;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,2,0,2 );
		gbPanel0.setConstraints( scpPortTypesTree, gbcPanel0 );
		pnPanel0.add( scpPortTypesTree );

		GridBagLayout gbGeneralPanel = new GridBagLayout();
		GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		pnGeneralPanel.setLayout( gbGeneralPanel );

		lbNameLabel.setFocusable( false );
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,2 );
		gbGeneralPanel.setConstraints( lbNameLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbNameLabel );

		gbcGeneralPanel.gridx = 1;
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
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,0,0,0 );
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( LangModelScheme.getString(Constants.EMPTY )));
//		pnGeneralPanel.setBackground(Color.WHITE);
//		pnPanel0.setBackground(Color.WHITE);
		scpParametersTree.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		scpPortTypesTree.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(trPortTypesTree);
		addToUndoableListener(trParametersTree);
	}
	
	public JComponent getGUI() {
		return pnPanel0; 
	}
	
	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (MeasurementType)or;
		
		if (this.type != null) {
			tfNameText.setText(this.type.getDescription());
		
			Collection inPTypes = this.type.getInParameterTypes();
			Collection outPTypes = this.type.getOutParameterTypes();

			for (Iterator it = allInPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(inPTypes.contains(node.getObject()));
			}
			for (Iterator it = allOutPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(outPTypes.contains(node.getObject()));
			}
			trParametersTree.updateUI();
				
			Collection mPTypes = this.type.getMeasurementPortTypes();
			for (Iterator it = allMPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(mPTypes.contains(node.getObject()));
			}
			trPortTypesTree.updateUI();
		}
		else {
			tfNameText.setText(Constants.EMPTY);
			for (Iterator it = allInPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(false);
			}
			for (Iterator it = allOutPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(false);
			}
			trParametersTree.updateUI();
			for (Iterator it = allMPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(false);
			}
			trPortTypesTree.updateUI();
		}
	}
	
	public void commitChanges() {
		if (MiscUtil.validName(tfNameText.getText())) {
			if (type == null) {
				try {
					type = SchemeObjectsFactory.createMeasurementType(tfNameText.getText());
					aContext.getDispatcher().notify(new SchemeEvent(this, type, SchemeEvent.CREATE_OBJECT));
				} catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}
			
			if (!type.getDescription().equals(tfNameText.getText()))
				type.setDescription(tfNameText.getText());

			Set inPTypes = new HashSet();
			Set outPTypes = new HashSet();
			for (Iterator it = allInPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode) it.next();
				if (node.isChecked())
					inPTypes.add(node.getObject());
			}
			if (!type.getInParameterTypes().equals(inPTypes))
				type.setInParameterTypes(inPTypes);
			for (Iterator it = allOutPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode) it.next();
				if (node.isChecked())
					outPTypes.add(node.getObject());
			}
			if (!type.getOutParameterTypes().equals(outPTypes))
				type.setOutParameterTypes(outPTypes);

			Set mPTypes = new HashSet();
			for (Iterator it = allMPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode) it.next();
				if (node.isChecked())
					mPTypes.add(node.getObject());
			}
			if (!type.getMeasurementPortTypes().equals(mPTypes))
				type.setMeasurementPortTypes(mPTypes);
			aContext.getDispatcher().notify(new SchemeEvent(this, type, SchemeEvent.UPDATE_OBJECT));
		}
	}
	
	List getParameterTypeNodes(String way) {
		for (Iterator it = parametersRoot.getChildren().iterator(); it.hasNext();) {
			Item node = (Item)it.next();
			if (node.getObject().equals(way)) {
				return node.getChildren();
			}
		}
		return null;
	}
	
	List getMeasurementPortTypeNodes() {
		return portsRoot.getChildren();
	}
	
	Item createParametersRoot() {
		Item root = new IconedNode(Constants.ROOT, LangModelScheme.getString(Constants.ROOT));
		Item input = new IconedNode(Constants.INPUT, LangModelScheme.getString(Constants.INPUT));
		Item output = new IconedNode(Constants.OUTPUT, LangModelScheme.getString(Constants.OUTPUT));
		root.addChild(input);
		root.addChild(output);
		
		EquivalentCondition condition = new EquivalentCondition(
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
		try {
			Collection pTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();) {
				ParameterType t = (ParameterType) it.next();
				input.addChild(new CheckableNode(t, false));
				output.addChild(new CheckableNode(t, false));
			}
		} 
		catch (ApplicationException e) {
			Log.errorException(e);
		}
		return root;
	}
	
	Item createPortsRoot() {
		Item root = new IconedNode(Constants.ROOT, Constants.ROOT);
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		try {
			Collection mpTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (Iterator it = mpTypes.iterator(); it.hasNext();) {
				MeasurementPortType t = (MeasurementPortType) it.next();
					root.addChild(new CheckableNode(t, false));
			}
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		return root;
	}	
}
