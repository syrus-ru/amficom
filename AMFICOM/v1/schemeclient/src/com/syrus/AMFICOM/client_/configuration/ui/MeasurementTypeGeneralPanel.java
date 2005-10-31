/*
 * $Id: MeasurementTypeGeneralPanel.java,v 1.28 2005/10/31 12:30:25 bass Exp $
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.tree.CheckableNode;
import com.syrus.AMFICOM.client.UI.tree.CheckableTreeUI;
import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.28 $, $Date: 2005/10/31 12:30:25 $
 * @module schemeclient
 */

public class MeasurementTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected MeasurementType type;
	
	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbParametersLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PARAMETERS));
	Item parametersRoot;
	JTree trParametersTree;
	JLabel lbPortTypesLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MEASUREMENT_PORT_TYPES));
	Item portsRoot;
//	JTree trPortTypesTree;
	JPanel pnGeneralPanel = new JPanel();
	List allInPTypeNodes;
	List allOutPTypeNodes;
//	List allMPTypeNodes;
	
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

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		this.parametersRoot = createParametersRoot();
		CheckableTreeUI parametersTreeUI = new CheckableTreeUI(this.parametersRoot);
		this.trParametersTree = parametersTreeUI.getTree();
		this.trParametersTree.setRootVisible(false);
		
//		this.portsRoot = createPortsRoot();
//		CheckableTreeUI portsTreeUI = new CheckableTreeUI(this.portsRoot);
//		this.trPortTypesTree = portsTreeUI.getTree();
//		this.trPortTypesTree.setRootVisible(false);
				
		this.allInPTypeNodes = getParameterTypeNodes(SchemeResourceKeys.INPUT);
		this.allOutPTypeNodes = getParameterTypeNodes(SchemeResourceKeys.OUTPUT);
//		this.allMPTypeNodes = getMeasurementPortTypeNodes();

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

//		JScrollPane scpPortTypesTree = new JScrollPane( trPortTypesTree );
//		gbcPanel0.gridx = 1;
//		gbcPanel0.gridy = 6;
//		gbcPanel0.gridwidth = 2;
//		gbcPanel0.gridheight = 2;
//		gbcPanel0.fill = GridBagConstraints.BOTH;
//		gbcPanel0.weightx = 1;
//		gbcPanel0.weighty = 1;
//		gbcPanel0.anchor = GridBagConstraints.NORTH;
//		gbcPanel0.insets = new Insets( 0,2,0,2 );
//		gbPanel0.setConstraints( scpPortTypesTree, gbcPanel0 );
//		pnPanel0.add( scpPortTypesTree );

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
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets( 0,0,0,0 );
		gbGeneralPanel.setConstraints( tfNameText, gbcGeneralPanel );
		pnGeneralPanel.add( tfNameText );
		
		gbcGeneralPanel.gridx = 2;
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
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,0,0,0 );
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
//		pnGeneralPanel.setBackground(Color.WHITE);
//		pnPanel0.setBackground(Color.WHITE);
		scpParametersTree.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
//		scpPortTypesTree.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
//		addToUndoableListener(trPortTypesTree);
		addToUndoableListener(trParametersTree);
		
		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ñ) {
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
		this.type = (MeasurementType)or;
		
		if (this.type != null) {
			this.tfNameText.setText(this.type.getDescription());
		
			Set<ParameterType> inPTypes = this.type.getInParameterTypes();
			Set<ParameterType> outPTypes = this.type.getOutParameterTypes();

			for (Iterator it = this.allInPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(inPTypes.contains(node.getObject()));
			}
			for (Iterator it = this.allOutPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(outPTypes.contains(node.getObject()));
			}
			this.trParametersTree.updateUI();
				
//			Set mPTypeIds = this.type.getMeasurementPortTypeIds();
//			try {
//				Collection mPTypes = StorableObjectPool.getStorableObjects(mPTypeIds, true);
//				for (Iterator it = this.allMPTypeNodes.iterator(); it.hasNext();) {
//					CheckableNode node = (CheckableNode)it.next();
//					node.setChecked(mPTypes.contains(node.getObject()));
//				}
//			} catch (ApplicationException e1) {
//				Log.errorException(e1);
//			}
//			this.trPortTypesTree.updateUI();
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			for (Iterator it = this.allInPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(false);
			}
			for (Iterator it = this.allOutPTypeNodes.iterator(); it.hasNext();) {
				CheckableNode node = (CheckableNode)it.next();
				node.setChecked(false);
			}
			this.trParametersTree.updateUI();
//			for (Iterator it = this.allMPTypeNodes.iterator(); it.hasNext();) {
//				CheckableNode node = (CheckableNode)it.next();
//				node.setChecked(false);
//			}
//			this.trPortTypesTree.updateUI();
		}
	}
	
	@Override
	public void commitChanges() {
		super.commitChanges();
	/*	if (MiscUtil.validName(this.tfNameText.getText())) {
			if (this.type == null) {
				try {
					this.type = SchemeObjectsFactory.createMeasurementType(this.tfNameText.getText());
//					apply();
					this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.type.getId(), SchemeEvent.CREATE_OBJECT));
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, this.type, MeasurementTypePropertiesManager.getInstance(this.aContext), ObjectSelectedEvent.MEASUREMENT_TYPE));
				} catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			} else {
//				apply();
			}
		}*/
	}
	/*
	private void apply() {
		if (!this.type.getDescription().equals(this.tfNameText.getText())) {
			this.type.setDescription(this.tfNameText.getText());
		}

		Set<String> inPTypeIds = new HashSet<String>();
		Set<String> outPTypeIds = new HashSet<String>();
		for (Iterator it = this.allInPTypeNodes.iterator(); it.hasNext();) {
			CheckableNode node = (CheckableNode) it.next();
			if (node.isChecked()) {
				inPTypeIds.add(((String)node.getObject()));
			}
		}
		if (!this.type.getInParameterTypes().equals(inPTypeIds)) {
			this.type.setInParameterTypeIds(inPTypeIds);
		}
		for (Iterator it = this.allOutPTypeNodes.iterator(); it.hasNext();) {
			CheckableNode node = (CheckableNode) it.next();
			if (node.isChecked()) {
				outPTypeIds.add(((ParameterType)node.getObject()).getId());
			}
		}
		if (!this.type.getOutParameterTypeIds().equals(outPTypeIds)) {
			this.type.setOutParameterTypeIds(outPTypeIds);
		}

		Set<Identifier> mPTypeIds = new HashSet<Identifier>();
		for (Iterator it = this.allMPTypeNodes.iterator(); it.hasNext();) {
			CheckableNode node = (CheckableNode) it.next();
			if (node.isChecked())
				mPTypeIds.add(((MeasurementPortType)node.getObject()).getId());
		}
		if (!this.type.getMeasurementPortTypeIds().equals(mPTypeIds)) {
			this.type.setMeasurementPortTypeIds(mPTypeIds);
		}
		
		try {
			StorableObjectPool.flush(this.type.getId(), LoginManager.getUserId(), true);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.type.getId(), SchemeEvent.UPDATE_OBJECT));
	}
	*/
	List getParameterTypeNodes(String way) {
		for (Iterator it = this.parametersRoot.getChildren().iterator(); it.hasNext();) {
			Item node = (Item)it.next();
			if (node.getObject().equals(way)) {
				return node.getChildren();
			}
		}
		return null;
	}
	
	List getMeasurementPortTypeNodes() {
		return this.portsRoot.getChildren();
	}
	
	Item createParametersRoot() {
		Item root = new IconedNode(SchemeResourceKeys.ROOT, LangModelScheme.getString(SchemeResourceKeys.ROOT));
		Item input = new IconedNode(SchemeResourceKeys.INPUT, LangModelScheme.getString(SchemeResourceKeys.INPUT));
		Item output = new IconedNode(SchemeResourceKeys.OUTPUT, LangModelScheme.getString(SchemeResourceKeys.OUTPUT));
		root.addChild(input);
		root.addChild(output);
		
		for (ParameterType parameterType : ParameterType.values()) {
			if (!parameterType.equals(ParameterType.UNKNOWN)) {
				input.addChild(new CheckableNode(parameterType, parameterType.getDescription(), false));
				output.addChild(new CheckableNode(parameterType, parameterType.getDescription(), false));
			}
		}
		return root;
	}
	
	Item createPortsRoot() {
		Item root = new IconedNode(SchemeResourceKeys.ROOT, SchemeResourceKeys.ROOT);
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
		try {
			Collection mpTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (Iterator it = mpTypes.iterator(); it.hasNext();) {
				MeasurementPortType t = (MeasurementPortType) it.next();
					root.addChild(new CheckableNode(t, false));
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return root;
	}	
}
