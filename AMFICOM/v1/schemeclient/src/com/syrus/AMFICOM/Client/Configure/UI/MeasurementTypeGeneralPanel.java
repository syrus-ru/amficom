/*
 * $Id: MeasurementTypeGeneralPanel.java,v 1.2 2005/03/14 13:36:18 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/14 13:36:18 $
 * @module schemeclient_v1
 */

public class MeasurementTypeGeneralPanel extends GeneralPanel {
	protected MeasurementType type;
	private static Dimension btn_size = new Dimension(24, 24);
	private static Dimension list_size = new Dimension(150, 50);
	
	JPanel pnMainPanel = new JPanel();
	JTextField tfNameText = new JTextField( );
	JLabel lbNameLabel = new JLabel(LangModelConfig.getString("label_name"));

	JLabel lbInParamsLabel1 = new JLabel(LangModelConfig.getString("label_parameters_in"));
	JLabel lbInParamsLabel2 = new JLabel(LangModelConfig.getString("label_parameters"));
	JPanel pnInParamsPanel = new JPanel();
	JList lsInList = new ObjList(ParameterTypeController.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);
	JList lsOtherInList = new ObjList(ParameterTypeController.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);
	JLabel lbInLabel = new JLabel(LangModelConfig.getString("label_used"));
	JLabel lbOtherInLabel = new JLabel(LangModelConfig.getString("label_valid"));
	JButton btAddInBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/left.gif")));
	JButton btRemoveInBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/right.gif")));
	
	JLabel lbOutParamsLabel1 = new JLabel(LangModelConfig.getString("label_parameters_out"));
	JLabel lbOutParamsLabel2 = new JLabel(LangModelConfig.getString("label_parameters"));
	JPanel pnOutParamsPanel = new JPanel();
	JList lsOutList = new ObjList(ParameterTypeController.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);
	JList lsOtherOutList = new ObjList(ParameterTypeController.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);
	JLabel lbOutLabel = new JLabel(LangModelConfig.getString("label_used"));
	JLabel lbOtherOutLabel = new JLabel(LangModelConfig.getString("label_valid"));
	JButton btAddOutBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/left.gif")));
	JButton btRemoveOutBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/right.gif")));
	
	JLabel lbPortsLabel1 = new JLabel(LangModelConfig.getString("label_measurement_port_types1"));
	JLabel lbPortsLabel2 = new JLabel(LangModelConfig.getString("label_measurement_port_types2"));
	JPanel pnPortsPanel = new JPanel();
	JList lsAddedPortsList = new ObjList(MeasurementPortTypeController.getInstance(), StorableObjectWrapper.COLUMN_NAME);
	JList lsOtherPortsList = new ObjList(MeasurementPortTypeController.getInstance(), StorableObjectWrapper.COLUMN_NAME);
	JLabel lbAddedPortsLabel = new JLabel(LangModelConfig.getString("label_linked_ports"));
	JLabel lbOtherPortsLabel = new JLabel(LangModelConfig.getString("label_nonlinked_ports"));
	JButton btAddPortBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/left.gif")));
	JButton btRemovePortBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/right.gif")));

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
		
		GridBagLayout gbMainPanel = new GridBagLayout();
		GridBagConstraints gbcMainPanel = new GridBagConstraints();
		pnMainPanel.setLayout( gbMainPanel );
		
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 0;
		gbcMainPanel.gridwidth = 9;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( tfNameText, gbcMainPanel );
		pnMainPanel.add( tfNameText );

		lbNameLabel.setFocusable( false );
		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 0;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbcMainPanel.insets = new Insets( 2,2,2,2 );
		gbMainPanel.setConstraints( lbNameLabel, gbcMainPanel );
		pnMainPanel.add( lbNameLabel );

		lbOutParamsLabel1.setFocusable( false );
		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 7;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbcMainPanel.insets = new Insets( 2,2,0,2 );
		gbMainPanel.setConstraints( lbOutParamsLabel1, gbcMainPanel );
		pnMainPanel.add( lbOutParamsLabel1 );

		pnPortsPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbPortsPanel = new GridBagLayout();
		GridBagConstraints gbcPortsPanel = new GridBagConstraints();
		pnPortsPanel.setLayout( gbPortsPanel );

		lbAddedPortsLabel.setFocusable( false );
		gbcPortsPanel.gridx = 1;
		gbcPortsPanel.gridy = 0;
		gbcPortsPanel.gridwidth = 2;
		gbcPortsPanel.gridheight = 1;
		gbcPortsPanel.fill = GridBagConstraints.BOTH;
		gbcPortsPanel.weightx = 1;
		gbcPortsPanel.weighty = 0;
		gbcPortsPanel.anchor = GridBagConstraints.NORTH;
		gbcPortsPanel.insets = new Insets( 2,2,2,0 );
		gbPortsPanel.setConstraints( lbAddedPortsLabel, gbcPortsPanel );
		pnPortsPanel.add( lbAddedPortsLabel );

		JScrollPane scpAddedPortsList = new JScrollPane( lsAddedPortsList );
		scpAddedPortsList.setPreferredSize(list_size);
		gbcPortsPanel.gridx = 0;
		gbcPortsPanel.gridy = 1;
		gbcPortsPanel.gridwidth = 4;
		gbcPortsPanel.gridheight = 4;
		gbcPortsPanel.fill = GridBagConstraints.BOTH;
		gbcPortsPanel.weightx = 1;
		gbcPortsPanel.weighty = 1;
		gbcPortsPanel.anchor = GridBagConstraints.NORTH;
		gbcPortsPanel.insets = new Insets( 0,0,0,0 );
		gbPortsPanel.setConstraints( scpAddedPortsList, gbcPortsPanel );
		pnPortsPanel.add( scpAddedPortsList );

		lbOtherPortsLabel.setFocusable( false );
		gbcPortsPanel.gridx = 6;
		gbcPortsPanel.gridy = 0;
		gbcPortsPanel.gridwidth = 2;
		gbcPortsPanel.gridheight = 1;
		gbcPortsPanel.fill = GridBagConstraints.BOTH;
		gbcPortsPanel.weightx = 1;
		gbcPortsPanel.weighty = 0;
		gbcPortsPanel.anchor = GridBagConstraints.NORTH;
		gbcPortsPanel.insets = new Insets( 2,2,2,0 );
		gbPortsPanel.setConstraints( lbOtherPortsLabel, gbcPortsPanel );
		pnPortsPanel.add( lbOtherPortsLabel );

		JScrollPane scpOtherPortsList = new JScrollPane( lsOtherPortsList );
		scpOtherPortsList.setPreferredSize(list_size);
		gbcPortsPanel.gridx = 5;
		gbcPortsPanel.gridy = 1;
		gbcPortsPanel.gridwidth = 4;
		gbcPortsPanel.gridheight = 4;
		gbcPortsPanel.fill = GridBagConstraints.BOTH;
		gbcPortsPanel.weightx = 1;
		gbcPortsPanel.weighty = 1;
		gbcPortsPanel.insets = new Insets( 0,0,0,0 );
		gbcPortsPanel.anchor = GridBagConstraints.NORTH;
		gbPortsPanel.setConstraints( scpOtherPortsList, gbcPortsPanel );
		pnPortsPanel.add( scpOtherPortsList );

		btAddPortBut.setPreferredSize(btn_size);
		btAddPortBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btAddPortBut_actionPerformed(e);
			}
		});
		gbcPortsPanel.gridx = 4;
		gbcPortsPanel.gridy = 2;
		gbcPortsPanel.gridwidth = 1;
		gbcPortsPanel.gridheight = 1;
		gbcPortsPanel.fill = GridBagConstraints.BOTH;
		gbcPortsPanel.weightx = 0;
		gbcPortsPanel.weighty = 0;
		gbcPortsPanel.anchor = GridBagConstraints.NORTH;
		gbPortsPanel.setConstraints( btAddPortBut, gbcPortsPanel );
		pnPortsPanel.add( btAddPortBut );

		btRemovePortBut.setPreferredSize(btn_size);
		btRemovePortBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btRemovePortBut_actionPerformed(e);
			}
		});
		gbcPortsPanel.gridx = 4;
		gbcPortsPanel.gridy = 3;
		gbcPortsPanel.gridwidth = 1;
		gbcPortsPanel.gridheight = 1;
		gbcPortsPanel.fill = GridBagConstraints.BOTH;
		gbcPortsPanel.weightx = 0;
		gbcPortsPanel.weighty = 0;
		gbcPortsPanel.anchor = GridBagConstraints.NORTH;
		gbPortsPanel.setConstraints( btRemovePortBut, gbcPortsPanel );
		pnPortsPanel.add( btRemovePortBut );
		 
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 13;
		gbcMainPanel.gridwidth = 9;
		gbcMainPanel.gridheight = 6;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 1;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( pnPortsPanel, gbcMainPanel );
		pnMainPanel.add( pnPortsPanel );

		pnInParamsPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbInParamsPanel = new GridBagLayout();
		GridBagConstraints gbcInParamsPanel = new GridBagConstraints();
		pnInParamsPanel.setLayout( gbInParamsPanel );

		JScrollPane scpInList = new JScrollPane( lsInList );
		scpInList.setPreferredSize(list_size);
		gbcInParamsPanel.gridx = 0;
		gbcInParamsPanel.gridy = 1;
		gbcInParamsPanel.gridwidth = 4;
		gbcInParamsPanel.gridheight = 4;
		gbcInParamsPanel.fill = GridBagConstraints.BOTH;
		gbcInParamsPanel.weightx = 1;
		gbcInParamsPanel.weighty = 1;
		gbcInParamsPanel.anchor = GridBagConstraints.NORTH;
		gbInParamsPanel.setConstraints( scpInList, gbcInParamsPanel );
		pnInParamsPanel.add( scpInList );

		JScrollPane scpOtherInList = new JScrollPane( lsOtherInList );
		scpOtherInList.setPreferredSize(list_size);
		gbcInParamsPanel.gridx = 5;
		gbcInParamsPanel.gridy = 1;
		gbcInParamsPanel.gridwidth = 4;
		gbcInParamsPanel.gridheight = 4;
		gbcInParamsPanel.fill = GridBagConstraints.BOTH;
		gbcInParamsPanel.weightx = 1;
		gbcInParamsPanel.weighty = 1;
		gbcInParamsPanel.anchor = GridBagConstraints.NORTH;
		gbInParamsPanel.setConstraints( scpOtherInList, gbcInParamsPanel );
		pnInParamsPanel.add( scpOtherInList );

		btAddInBut.setPreferredSize(btn_size);
		btAddInBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btAddInBut_actionPerformed(e);
			}
		});
		gbcInParamsPanel.gridx = 4;
		gbcInParamsPanel.gridy = 2;
		gbcInParamsPanel.gridwidth = 1;
		gbcInParamsPanel.gridheight = 1;
		gbcInParamsPanel.fill = GridBagConstraints.BOTH;
		gbcInParamsPanel.weightx = 0;
		gbcInParamsPanel.weighty = 0;
		gbcInParamsPanel.anchor = GridBagConstraints.NORTH;
		gbInParamsPanel.setConstraints( btAddInBut, gbcInParamsPanel );
		pnInParamsPanel.add( btAddInBut );

		btRemoveInBut.setPreferredSize(btn_size);
		btRemoveInBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btRemoveInBut_actionPerformed(e);
			}
		});
		gbcInParamsPanel.gridx = 4;
		gbcInParamsPanel.gridy = 3;
		gbcInParamsPanel.gridwidth = 1;
		gbcInParamsPanel.gridheight = 1;
		gbcInParamsPanel.fill = GridBagConstraints.BOTH;
		gbcInParamsPanel.weightx = 0;
		gbcInParamsPanel.weighty = 0;
		gbcInParamsPanel.anchor = GridBagConstraints.NORTH;
		gbInParamsPanel.setConstraints( btRemoveInBut, gbcInParamsPanel );
		pnInParamsPanel.add( btRemoveInBut );

		lbInLabel.setFocusable( false );
		gbcInParamsPanel.gridx = 1;
		gbcInParamsPanel.gridy = 0;
		gbcInParamsPanel.gridwidth = 2;
		gbcInParamsPanel.gridheight = 1;
		gbcInParamsPanel.fill = GridBagConstraints.BOTH;
		gbcInParamsPanel.weightx = 1;
		gbcInParamsPanel.weighty = 0;
		gbcInParamsPanel.anchor = GridBagConstraints.NORTH;
		gbInParamsPanel.setConstraints( lbInLabel, gbcInParamsPanel );
		pnInParamsPanel.add( lbInLabel );

		lbOtherInLabel.setFocusable( false );
		gbcInParamsPanel.gridx = 6;
		gbcInParamsPanel.gridy = 0;
		gbcInParamsPanel.gridwidth = 2;
		gbcInParamsPanel.gridheight = 1;
		gbcInParamsPanel.fill = GridBagConstraints.BOTH;
		gbcInParamsPanel.weightx = 1;
		gbcInParamsPanel.weighty = 0;
		gbcInParamsPanel.anchor = GridBagConstraints.NORTH;
		gbInParamsPanel.setConstraints( lbOtherInLabel, gbcInParamsPanel );
		pnInParamsPanel.add( lbOtherInLabel );
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 1;
		gbcMainPanel.gridwidth = 9;
		gbcMainPanel.gridheight = 6;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 1;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( pnInParamsPanel, gbcMainPanel );
		pnMainPanel.add( pnInParamsPanel );

		pnOutParamsPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbOutParamsPanel = new GridBagLayout();
		GridBagConstraints gbcOutParamsPanel = new GridBagConstraints();
		pnOutParamsPanel.setLayout( gbOutParamsPanel );

		lbOutLabel.setFocusable( false );
		gbcOutParamsPanel.gridx = 1;
		gbcOutParamsPanel.gridy = 0;
		gbcOutParamsPanel.gridwidth = 2;
		gbcOutParamsPanel.gridheight = 1;
		gbcOutParamsPanel.fill = GridBagConstraints.BOTH;
		gbcOutParamsPanel.weightx = 1;
		gbcOutParamsPanel.weighty = 0;
		gbcOutParamsPanel.anchor = GridBagConstraints.NORTH;
		gbOutParamsPanel.setConstraints( lbOutLabel, gbcOutParamsPanel );
		pnOutParamsPanel.add( lbOutLabel );

		lbOtherOutLabel.setFocusable( false );
		gbcOutParamsPanel.gridx = 6;
		gbcOutParamsPanel.gridy = 0;
		gbcOutParamsPanel.gridwidth = 2;
		gbcOutParamsPanel.gridheight = 1;
		gbcOutParamsPanel.fill = GridBagConstraints.BOTH;
		gbcOutParamsPanel.weightx = 1;
		gbcOutParamsPanel.weighty = 0;
		gbcOutParamsPanel.anchor = GridBagConstraints.NORTH;
		gbOutParamsPanel.setConstraints( lbOtherOutLabel, gbcOutParamsPanel );
		pnOutParamsPanel.add( lbOtherOutLabel );

		JScrollPane scpOutList = new JScrollPane( lsOutList );
		scpOutList.setPreferredSize(list_size);
		gbcOutParamsPanel.gridx = 0;
		gbcOutParamsPanel.gridy = 1;
		gbcOutParamsPanel.gridwidth = 4;
		gbcOutParamsPanel.gridheight = 4;
		gbcOutParamsPanel.fill = GridBagConstraints.BOTH;
		gbcOutParamsPanel.weightx = 1;
		gbcOutParamsPanel.weighty = 1;
		gbcOutParamsPanel.anchor = GridBagConstraints.NORTH;
		gbOutParamsPanel.setConstraints( scpOutList, gbcOutParamsPanel );
		pnOutParamsPanel.add( scpOutList );

		JScrollPane scpOtherOutList = new JScrollPane( lsOtherOutList );
		scpOtherOutList.setPreferredSize(list_size);
		gbcOutParamsPanel.gridx = 5;
		gbcOutParamsPanel.gridy = 1;
		gbcOutParamsPanel.gridwidth = 4;
		gbcOutParamsPanel.gridheight = 4;
		gbcOutParamsPanel.fill = GridBagConstraints.BOTH;
		gbcOutParamsPanel.weightx = 1;
		gbcOutParamsPanel.weighty = 1;
		gbcOutParamsPanel.anchor = GridBagConstraints.NORTH;
		gbOutParamsPanel.setConstraints( scpOtherOutList, gbcOutParamsPanel );
		pnOutParamsPanel.add( scpOtherOutList );

		btAddOutBut.setPreferredSize(btn_size);
		btAddOutBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btAddOutBut_actionPerformed(e);
			}
		});
		gbcOutParamsPanel.gridx = 4;
		gbcOutParamsPanel.gridy = 2;
		gbcOutParamsPanel.gridwidth = 1;
		gbcOutParamsPanel.gridheight = 1;
		gbcOutParamsPanel.fill = GridBagConstraints.BOTH;
		gbcOutParamsPanel.weightx = 0;
		gbcOutParamsPanel.weighty = 0;
		gbcOutParamsPanel.anchor = GridBagConstraints.NORTH;
		gbOutParamsPanel.setConstraints( btAddOutBut, gbcOutParamsPanel );
		pnOutParamsPanel.add( btAddOutBut );

		btRemoveOutBut.setPreferredSize(btn_size);
		btRemoveOutBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btRemoveOutBut_actionPerformed(e);
			}
		});
		gbcOutParamsPanel.gridx = 4;
		gbcOutParamsPanel.gridy = 3;
		gbcOutParamsPanel.gridwidth = 1;
		gbcOutParamsPanel.gridheight = 1;
		gbcOutParamsPanel.fill = GridBagConstraints.BOTH;
		gbcOutParamsPanel.weightx = 0;
		gbcOutParamsPanel.weighty = 0;
		gbcOutParamsPanel.anchor = GridBagConstraints.NORTH;
		gbOutParamsPanel.setConstraints( btRemoveOutBut, gbcOutParamsPanel );
		pnOutParamsPanel.add( btRemoveOutBut );
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 7;
		gbcMainPanel.gridwidth = 9;
		gbcMainPanel.gridheight = 6;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 1;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( pnOutParamsPanel, gbcMainPanel );
		pnMainPanel.add( pnOutParamsPanel );

		lbInParamsLabel1.setFocusable( false );
		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 1;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbcMainPanel.insets = new Insets( 2,2,0,2 );
		gbMainPanel.setConstraints( lbInParamsLabel1, gbcMainPanel );
		pnMainPanel.add( lbInParamsLabel1 );

		lbInParamsLabel2.setFocusable( false );
		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 2;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbcMainPanel.insets = new Insets( 0,2,2,2 );
		gbMainPanel.setConstraints( lbInParamsLabel2, gbcMainPanel );
		pnMainPanel.add( lbInParamsLabel2 );

		lbOutParamsLabel2.setFocusable( false );
		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 8;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbcMainPanel.insets = new Insets( 0,2,2,2 );
		gbMainPanel.setConstraints( lbOutParamsLabel2, gbcMainPanel );
		pnMainPanel.add( lbOutParamsLabel2 );

		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 13;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbcMainPanel.insets = new Insets( 2,2,0,2 );
		gbMainPanel.setConstraints( lbPortsLabel1, gbcMainPanel );
		pnMainPanel.add( lbPortsLabel1 );

		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 14;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbcMainPanel.insets = new Insets( 0,2,2,2 );
		gbMainPanel.setConstraints( lbPortsLabel2, gbcMainPanel );
		pnMainPanel.add( lbPortsLabel2 );
		
		this.setLayout(new BorderLayout());
		this.add(pnMainPanel, BorderLayout.CENTER);
	}
	
	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (MeasurementType)or;
		
		Collection inPTypes = this.type.getInParameterTypes();
		Collection outPTypes = this.type.getOutParameterTypes();
		Collection otherPTypes1 = new LinkedList();
		Collection otherPTypes2 = new LinkedList();
		
		try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
			Collection pTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();) {
				ParameterType pType = (ParameterType)it.next();
				if (!inPTypes.contains(pType))
					otherPTypes1.add(pType);
				if (!outPTypes.contains(pType))
					otherPTypes2.add(pType);
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collection mPTypes = this.type.getMeasurementPortTypes();
		Collection otherMPTypes = new LinkedList();
		try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
			Collection mpTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (Iterator it = mpTypes.iterator(); it.hasNext();) {
				MeasurementPortType mpType = (MeasurementPortType)it.next();
				if (mPTypes.contains(mpType))
					continue;
				otherMPTypes.add(mpType);
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tfNameText.setText(this.type.getDescription());
		((ObjListModel)this.lsInList.getModel()).removeAllElements();
		((ObjListModel)this.lsInList.getModel()).addElements(inPTypes);
		((ObjListModel)this.lsOutList.getModel()).removeAllElements();
		((ObjListModel)this.lsOutList.getModel()).addElements(outPTypes);
		((ObjListModel)this.lsOtherInList.getModel()).removeAllElements();
		((ObjListModel)this.lsOtherInList.getModel()).addElements(otherPTypes1);
		((ObjListModel)this.lsOtherOutList.getModel()).removeAllElements();
		((ObjListModel)this.lsOtherOutList.getModel()).addElements(otherPTypes2);
		((ObjListModel)this.lsAddedPortsList.getModel()).removeAllElements();
		((ObjListModel)this.lsAddedPortsList.getModel()).addElements(mPTypes);
		((ObjListModel)this.lsOtherPortsList.getModel()).removeAllElements();
		((ObjListModel)this.lsOtherPortsList.getModel()).addElements(otherMPTypes);
	}
	
	public boolean modify() {
		if (MiscUtil.validName(tfNameText.getText()))
			type.setDescription(tfNameText.getText());
		else
			return false;
		
		ObjListModel inModel = (ObjListModel)lsInList.getModel();
		ObjListModel outModel = (ObjListModel)lsOutList.getModel();
		ObjListModel portsModel = (ObjListModel)lsAddedPortsList.getModel();
		
		Collection inPTypes = new ArrayList(inModel.getSize());
		Collection outPTypes = new ArrayList(outModel.getSize());
		Collection mPTypes = new ArrayList(portsModel.getSize());
				
		for (int i = 0; i < inModel.getSize(); i++)
			inPTypes.add(inModel.getElementAt(i));
		for (int i = 0; i < outModel.getSize(); i++)
			outPTypes.add(outModel.getElementAt(i));
		for (int i = 0; i < portsModel.getSize(); i++)
			mPTypes.add(portsModel.getElementAt(i));
		
		type.setInParameterTypes(inPTypes);
		type.setOutParameterTypes(outPTypes);
		type.setMeasurementPortTypes(mPTypes);
		return true;
	}
	
	void btAddInBut_actionPerformed(ActionEvent e) {
		Object[] pTypes = lsOtherInList.getSelectedValues();
		for (int i = 0; i < pTypes.length; i++) {
			((ObjListModel) lsInList.getModel()).addElement(pTypes[i]);
			((ObjListModel) lsOtherInList.getModel()).removeElement(pTypes[i]);
		}
	}

	void btRemoveInBut_actionPerformed(ActionEvent e) {
		Object[] pTypes = lsInList.getSelectedValues();
		for (int i = 0; i < pTypes.length; i++) {
			((ObjListModel) lsInList.getModel()).removeElement(pTypes[i]);
			((ObjListModel) lsOtherInList.getModel()).addElement(pTypes[i]);
		}
	}

	void btAddOutBut_actionPerformed(ActionEvent e) {
		Object[] pTypes = lsOtherOutList.getSelectedValues();
		for (int i = 0; i < pTypes.length; i++) {
			((ObjListModel) lsOutList.getModel()).addElement(pTypes[i]);
			((ObjListModel) lsOtherOutList.getModel()).removeElement(pTypes[i]);
		}
	}

	void btRemoveOutBut_actionPerformed(ActionEvent e) {
		Object[] pTypes = lsOutList.getSelectedValues();
		for (int i = 0; i < pTypes.length; i++) {
			((ObjListModel) lsOutList.getModel()).removeElement(pTypes[i]);
			((ObjListModel) lsOtherOutList.getModel()).addElement(pTypes[i]);
		}
	}

	void btAddPortBut_actionPerformed(ActionEvent e) {
		Object[] mpTypes = lsOtherPortsList.getSelectedValues();
		for (int i = 0; i < mpTypes.length; i++) {
			((ObjListModel) lsAddedPortsList.getModel()).addElement(mpTypes[i]);
			((ObjListModel) lsOtherPortsList.getModel()).removeElement(mpTypes[i]);
		}
	}

	void btRemovePortBut_actionPerformed(ActionEvent e) {
		Object[] mpTypes = lsAddedPortsList.getSelectedValues();
		for (int i = 0; i < mpTypes.length; i++) {
			((ObjListModel) lsAddedPortsList.getModel()).removeElement(mpTypes[i]);
			((ObjListModel) lsOtherPortsList.getModel()).addElement(mpTypes[i]);
		}
	}
}
