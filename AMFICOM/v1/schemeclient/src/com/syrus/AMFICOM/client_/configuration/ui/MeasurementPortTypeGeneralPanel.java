/*
 * $Id: MeasurementPortTypeGeneralPanel.java,v 1.1 2005/03/10 08:09:08 stas Exp $
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
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:09:08 $
 * @module schemeclient_v1
 */

public class MeasurementPortTypeGeneralPanel extends GeneralPanel {
	protected MeasurementPortType type;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(Constants.TEXT_NAME);
	JTextField tfNameText = new JTextField();
	JLabel lbDescriptionLabel = new JLabel(Constants.TEXT_DESCRIPTION);
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JLabel lbTestTypeLabel = new JLabel(Constants.TEXT_TEST_TYPES);
	JTree trTestTypeTree;
	TreeDataModel model;
	
	protected MeasurementPortTypeGeneralPanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected MeasurementPortTypeGeneralPanel(MeasurementPortType apt)
	{
		this();
		setObject(apt);
	}

	private void jbInit() throws Exception
	{
		model = new MeasurementTypeModel();
		trTestTypeTree = new StorableObjectTree(new Dispatcher(), model);
		trTestTypeTree.setRootVisible(false);
	
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

		lbNameLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbNameLabel, gbcPanel0 );
		pnPanel0.add( lbNameLabel );

		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( tfNameText, gbcPanel0 );
		pnPanel0.add( tfNameText );

		lbDescriptionLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbDescriptionLabel, gbcPanel0 );
		pnPanel0.add( lbDescriptionLabel );

		JScrollPane scpDescriptionArea = new JScrollPane( taDescriptionArea );
		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0.5;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescriptionArea, gbcPanel0 );
		pnPanel0.add( scpDescriptionArea );

		lbTestTypeLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbTestTypeLabel, gbcPanel0 );
		pnPanel0.add( lbTestTypeLabel );

		JScrollPane scpTestTypeTree = new JScrollPane( trTestTypeTree );
		scpTestTypeTree.setPreferredSize(Constants.TEXT_AREA_SIZE);
		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1.5;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpTestTypeTree, gbcPanel0 );
		pnPanel0.add( scpTestTypeTree );
		
		this.setLayout(new BorderLayout());
		this.add(pnPanel0, BorderLayout.CENTER);
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
						ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
				Collection mPTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(
						condition, true);
				
				List types = new LinkedList();
				getMeasurementTypes(types, model.getRoot());
				for (Iterator it = types.iterator(); it.hasNext();) {
					CheckableTreeNode node = (CheckableTreeNode)it.next();
					node.setChecked(mPTypes.contains(node.getObject()));
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	void getMeasurementTypes(List types, StorableObjectTreeNode startNode) {
		for (Enumeration en = startNode.children(); en.hasMoreElements();) {
			StorableObjectTreeNode node = (StorableObjectTreeNode)en.nextElement();
			if (node instanceof CheckableTreeNode) {
				types.add(node);
				getMeasurementTypes(types, node);
			}
		}
	}

	public boolean modify() {
		if (!MiscUtil.validName(tfNameText.getText()))
			return false;

		this.type.setName(tfNameText.getText());
		this.type.setDescription(this.taDescriptionArea.getText());
		
		List types = new LinkedList();
		getMeasurementTypes(types, model.getRoot());
		for (Iterator it = types.iterator(); it.hasNext();) {
			CheckableTreeNode node = (CheckableTreeNode) it.next();
			MeasurementType mtype = (MeasurementType)node.getObject(); 
			if (node.isChecked()) {
				Collection pTypes = mtype.getMeasurementPortTypes();
				if (!pTypes.contains(type)) {
					List newPTypes = new LinkedList(pTypes);
					newPTypes.add(type);
					mtype.setMeasurementPortTypes(newPTypes);
				}
			}
			else {
				Collection pTypes = mtype.getMeasurementPortTypes();
				if (pTypes.contains(type)) {
					List newPTypes = new LinkedList(pTypes);
					newPTypes.remove(type);
					mtype.setMeasurementPortTypes(newPTypes);
				}
			}
		}
		try {
			MeasurementStorableObjectPool.flush(false);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	class MeasurementTypeModel implements TreeDataModel {
		StorableObjectTreeNode root = new StorableObjectTreeNode("root", "root", new ImageIcon(Toolkit
					.getDefaultToolkit().getImage("images/folder.gif")));
		public MeasurementTypeModel() {
		}

		public StorableObjectTreeNode getRoot() {
			return root;
		}

		public Color getNodeTextColor(StorableObjectTreeNode node) {
			return null;
		}

		public Class getNodeChildClass(StorableObjectTreeNode node) {
			if (node.getObject() instanceof String) {
				String s = (String) node.getObject();
				if (s.equals("root"))
					return MeasurementType.class;
			}
			return null;
		}

		public ObjectResourceController getNodeChildController(
				StorableObjectTreeNode node) {
			if (node.getObject() instanceof String) {
				String s = (String) node.getObject();
				if (s.equals("root"))
					return MeasurementTypeController.getInstance();
			}
			return null;
		}

		public List getChildNodes(StorableObjectTreeNode node) {
			List vec = new ArrayList();
			if (node.getObject() instanceof String) {
				String s = (String) node.getObject();
				if (s.equals("root")) {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					try {
						Collection allMPTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
						for (Iterator it = allMPTypes.iterator(); it.hasNext();) {
							MeasurementType t = (MeasurementType) it.next();
							vec.add(new CheckableTreeNode(t, t.getDescription(), true));
						}
					} 
					catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return vec;
		}
	}
}
