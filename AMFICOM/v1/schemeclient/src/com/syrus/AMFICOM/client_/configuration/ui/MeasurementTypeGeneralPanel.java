/*
 * $Id: MeasurementTypeGeneralPanel.java,v 1.2 2005/03/17 14:45:35 stas Exp $
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

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 14:45:35 $
 * @module schemeclient_v1
 */

public class MeasurementTypeGeneralPanel implements StorableObjectEditor {
	protected MeasurementType type;
	
	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(Constants.TEXT_NAME);
	JTextField tfNameText = new JTextField();
	JLabel lbParametersLabel = new JLabel(Constants.TEXT_PARAMETERS);
	SONode parametersRoot;
	JTree trParametersTree;
	JLabel lbPortTypesLabel = new JLabel(Constants.TEXT_MEASUREMENT_PORT_TYPES);
	SONode portsRoot;
	JTree trPortTypesTree;
	JPanel pnGeneralPanel = new JPanel();
	List allInPTypes;
	List allOutPTypes;
	List allMPTypes;
	
	protected MeasurementTypeGeneralPanel() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected MeasurementTypeGeneralPanel(MeasurementType type) {
		this();
		setObject(type);
	}

	private void jbInit() throws Exception {
		Dispatcher dispatcher = new Dispatcher();
		ParametersModel parametersModel = new ParametersModel();
		parametersRoot = new SOMutableNode(parametersModel, Constants.ROOT);
		trParametersTree = new Tree(dispatcher, parametersRoot);
		trParametersTree.setRootVisible(false);
		
		PortsModel portsModel = new PortsModel();
		portsRoot = new SOMutableNode(portsModel, Constants.ROOT);
		trPortTypesTree = new Tree(dispatcher, portsRoot);
		trPortTypesTree.setRootVisible(false);
				
		allInPTypes = getParameterTypes("input");
		allOutPTypes = getParameterTypes("output");
		allMPTypes = getMeasurementPortTypes();

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

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
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

		pnGeneralPanel.setBackground(Color.WHITE);
		pnPanel0.setBackground(Color.WHITE);
		scpParametersTree.setPreferredSize(Constants.TEXT_AREA_SIZE);
		scpPortTypesTree.setPreferredSize(Constants.TEXT_AREA_SIZE);
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

			for (Iterator it = allInPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode)it.next();
				node.setChecked(inPTypes.contains(node.getUserObject()));
			}
			for (Iterator it = allOutPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode)it.next();
				node.setChecked(outPTypes.contains(node.getUserObject()));
			}
			trParametersTree.updateUI();
				
			Collection mPTypes = this.type.getMeasurementPortTypes();
			for (Iterator it = allMPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode)it.next();
				node.setChecked(mPTypes.contains(node.getUserObject()));
			}
			trPortTypesTree.updateUI();
		}
		else {
			tfNameText.setText(Constants.TEXT_EMPTY);
			for (Iterator it = allInPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode)it.next();
				node.setChecked(false);
			}
			for (Iterator it = allOutPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode)it.next();
				node.setChecked(false);
			}
			trParametersTree.updateUI();
			for (Iterator it = allMPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode)it.next();
				node.setChecked(false);
			}
			trPortTypesTree.updateUI();
		}
	}
	
	public void commitChanges() {
		if (MiscUtil.validName(tfNameText.getText())) {
			type.setDescription(tfNameText.getText());

			Collection inPTypes = new LinkedList(this.type.getInParameterTypes());
			Collection outPTypes = new LinkedList(this.type.getOutParameterTypes());

			for (Iterator it = allInPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode) it.next();
				if (node.isChecked() && !inPTypes.contains(node.getUserObject()))
					inPTypes.add(node.getUserObject());
				if (!node.isChecked() && inPTypes.contains(node.getUserObject()))
					inPTypes.remove(node.getUserObject());
			}
			type.setInParameterTypes(inPTypes);
			for (Iterator it = allOutPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode) it.next();
				if (node.isChecked() && !outPTypes.contains(node.getUserObject()))
					outPTypes.add(node.getUserObject());
				if (!node.isChecked() && outPTypes.contains(node.getUserObject()))
					outPTypes.remove(node.getUserObject());
			}
			type.setOutParameterTypes(outPTypes);

			Collection mPTypes = new LinkedList(this.type.getMeasurementPortTypes());
			for (Iterator it = allMPTypes.iterator(); it.hasNext();) {
				SOCheckableNode node = (SOCheckableNode) it.next();
				if (node.isChecked() && !mPTypes.contains(node.getUserObject()))
					mPTypes.add(node.getUserObject());
				if (!node.isChecked() && mPTypes.contains(node.getUserObject()))
					mPTypes.remove(node.getUserObject());
			}
			type.setMeasurementPortTypes(mPTypes);
		}
	}
	
	List getParameterTypes(String way) {
		for (Enumeration en = parametersRoot.children(); en.hasMoreElements();) {
			SONode node = (SONode)en.nextElement();
			if (node.getUserObject().equals(way)) {
				node.setExpanded(true);
				node.updateChildNodes();
				List parameters = new ArrayList(node.getChildCount());
				for (Enumeration en2 = node.children(); en2.hasMoreElements();)
					parameters.add(en2.nextElement());
				return parameters;
			}
		}
		return null;
	}
	
	List getMeasurementPortTypes() {
		List ports = new ArrayList(portsRoot.getChildCount());
		for (Enumeration en = portsRoot.children(); en.hasMoreElements();) {
			ports.add(en.nextElement());
		}
		return ports;
	}
	
	class ParametersModel implements SOTreeDataModel {
		public Color getNodeColor(SONode node) {
			return Color.BLACK;
		}
		
		public Icon getNodeIcon(SONode node) {
			return null;
		}
		
		public String getNodeName(SONode node) {
			if (node.getUserObject() instanceof String) {
				String s = (String)node.getUserObject();
				if (s.equals(Constants.ROOT))
					return Constants.ROOT;
				if (s.equals(Constants.INPUT))
					return Constants.TEXT_INPUT;
				if (s.equals(Constants.OUTPUT))
					return Constants.TEXT_OUTPUT;
			}
			if (node.getUserObject() instanceof ParameterType) {
				return ((ParameterType)node.getUserObject()).getName();				
			}
			throw new UnsupportedOperationException("Unsupported object"); //$NON-NLS-1$
		}

		public ObjectResourceController getNodeController(SONode node) {
			return ParameterTypeController.getInstance();
		}

		public void updateChildNodes(SONode node) {
			if(!node.isExpanded())
				return;
			List contents = node.getChildrenUserObjects();
			
			if (node.getUserObject() instanceof String) {
				String s = (String) node.getUserObject();
				if (s.equals("root")) {
					if(!contents.contains(Constants.INPUT))
						node.add(new SOMutableNode(this, Constants.INPUT));
					if(!contents.contains(Constants.OUTPUT))
						node.add(new SOMutableNode(this, Constants.OUTPUT));
				}
				else {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
					try {
						Collection pTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(condition, true);
						for (Iterator it = pTypes.iterator(); it.hasNext();) {
							ParameterType t = (ParameterType) it.next();
							if (!contents.contains(t))
								node.add(new SOCheckableNode(this, t, false));
						}
					} 
					catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	class PortsModel implements SOTreeDataModel {
		public Color getNodeColor(SONode node) {
			return Color.BLACK;
		}
		
		public Icon getNodeIcon(SONode node) {
			return null;
		}
		
		public String getNodeName(SONode node) {
			if (node.getUserObject() instanceof String) {
				return Constants.ROOT;
			}
			if (node.getUserObject() instanceof MeasurementPortType) {
				return ((MeasurementPortType)node.getUserObject()).getName();				
			}
			throw new UnsupportedOperationException("Unsupported object"); //$NON-NLS-1$
		}

		public ObjectResourceController getNodeController(SONode node) {
			return MeasurementPortTypeController.getInstance();
		}

		public void updateChildNodes(SONode node) {
			if(!node.isExpanded())
				return;
			List contents = node.getChildrenUserObjects();
			
			if (node.getUserObject() instanceof String) {
				String s = (String) node.getUserObject();
				if (s.equals("root")) {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
					try {
						Collection mpTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
						for (Iterator it = mpTypes.iterator(); it.hasNext();) {
							MeasurementPortType t = (MeasurementPortType) it.next();
							if (!contents.contains(t))
								node.add(new SOCheckableNode(this, t, false));
						}
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
