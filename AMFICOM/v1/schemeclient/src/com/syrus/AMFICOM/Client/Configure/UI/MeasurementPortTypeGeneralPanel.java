package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;

public class MeasurementPortTypeGeneralPanel extends GeneralPanel {
	private static Dimension list_size = new Dimension(100, 50);
	private static Dimension btn_size = new Dimension(24, 24);
	
	protected MeasurementPortType type;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelConfig.getString("label_name"));
	JTextField tfNameText = new JTextField( );
	JLabel lbDesrcLabel = new JLabel(LangModelConfig.getString("label_description"));
	JTextArea taDesrcArea = new JTextArea(2,10);
	JLabel lbTestTypeLabel = new JLabel(LangModelConfig.getString("label_test_types"));

	JPanel pnPanel1 = new JPanel();
	JLabel lbTypeUsedLabel = new JLabel(LangModelConfig.getString("label_used"));
	JLabel lbTypeOtherLabel = new JLabel(LangModelConfig.getString("label_valid"));
	JList lsTypeUsedList = new ObjList(MeasurementTypeController.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);
	JList lsTypeOtherList = new ObjList(MeasurementTypeController.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);
	JButton btAddUsedBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/left.gif")));
	JButton btRemoveUsedBut = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/right.gif")));
	
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
		this.setLayout(new GridBagLayout());
				
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
		gbcPanel0.insets = new Insets( 2,2,2,2 );
		gbPanel0.setConstraints( lbNameLabel, gbcPanel0 );
		pnPanel0.add( lbNameLabel );

		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( tfNameText, gbcPanel0 );
		pnPanel0.add( tfNameText );

		lbDesrcLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 2,2,2,2 );
		gbPanel0.setConstraints( lbDesrcLabel, gbcPanel0 );
		pnPanel0.add( lbDesrcLabel );

		JScrollPane scpDesrcArea = new JScrollPane( taDesrcArea );
		scpDesrcArea.setPreferredSize(list_size);
		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 4;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDesrcArea, gbcPanel0 );
		pnPanel0.add( scpDesrcArea );

		lbTestTypeLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 5;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 2,2,2,2 );
		gbPanel0.setConstraints( lbTestTypeLabel, gbcPanel0 );
		pnPanel0.add( lbTestTypeLabel );

		GridBagLayout gbPanel1 = new GridBagLayout();
		pnPanel1.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagConstraints gbcPanel1 = new GridBagConstraints();
		pnPanel1.setLayout( gbPanel1 );

		lbTypeUsedLabel.setFocusable( false );
		gbcPanel1.gridx = 1;
		gbcPanel1.gridy = 0;
		gbcPanel1.gridwidth = 2;
		gbcPanel1.gridheight = 1;
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.weightx = 1;
		gbcPanel1.weighty = 0;
		gbcPanel1.anchor = GridBagConstraints.NORTH;
		gbcPanel1.insets = new Insets( 2,2,2,2 );
		gbPanel1.setConstraints( lbTypeUsedLabel, gbcPanel1 );
		pnPanel1.add( lbTypeUsedLabel );

		lbTypeOtherLabel.setFocusable( false );
		gbcPanel1.gridx = 6;
		gbcPanel1.gridy = 0;
		gbcPanel1.gridwidth = 2;
		gbcPanel1.gridheight = 1;
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.weightx = 1;
		gbcPanel1.weighty = 0;
		gbcPanel1.anchor = GridBagConstraints.NORTH;
		gbcPanel1.insets = new Insets( 2,2,2,2 );
		gbPanel1.setConstraints( lbTypeOtherLabel, gbcPanel1 );
		pnPanel1.add( lbTypeOtherLabel );

		JScrollPane scpTypeUsedList = new JScrollPane( lsTypeUsedList );
		scpTypeUsedList.setPreferredSize(list_size);
		gbcPanel1.gridx = 0;
		gbcPanel1.gridy = 1;
		gbcPanel1.gridwidth = 4;
		gbcPanel1.gridheight = 4;
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.weightx = 1;
		gbcPanel1.weighty = 1;
		gbcPanel1.anchor = GridBagConstraints.NORTH;
		gbcPanel1.insets = new Insets( 0,0,0,0 );
		gbPanel1.setConstraints( scpTypeUsedList, gbcPanel1 );
		pnPanel1.add( scpTypeUsedList );

		JScrollPane scpTypeOtherList = new JScrollPane( lsTypeOtherList );
		scpTypeOtherList.setPreferredSize(list_size);
		gbcPanel1.gridx = 5;
		gbcPanel1.gridy = 1;
		gbcPanel1.gridwidth = 4;
		gbcPanel1.gridheight = 4;
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.weightx = 1;
		gbcPanel1.weighty = 1;
		gbcPanel1.anchor = GridBagConstraints.NORTH;
		gbPanel1.setConstraints( scpTypeOtherList, gbcPanel1 );
		pnPanel1.add( scpTypeOtherList );

		btAddUsedBut.setPreferredSize(btn_size);
		btAddUsedBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btAddUsedBut_actionPerformed(e);
			}
		});
		gbcPanel1.gridx = 4;
		gbcPanel1.gridy = 2;
		gbcPanel1.gridwidth = 1;
		gbcPanel1.gridheight = 1;
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.weightx = 0;
		gbcPanel1.weighty = 0;
		gbcPanel1.anchor = GridBagConstraints.NORTH;
		gbPanel1.setConstraints( btAddUsedBut, gbcPanel1 );
		pnPanel1.add( btAddUsedBut );

		btRemoveUsedBut.setPreferredSize(btn_size);
		btRemoveUsedBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				btRemoveUsedBut_actionPerformed(e);
			}
		});
		gbcPanel1.gridx = 4;
		gbcPanel1.gridy = 3;
		gbcPanel1.gridwidth = 1;
		gbcPanel1.gridheight = 1;
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.weightx = 0;
		gbcPanel1.weighty = 0;
		gbcPanel1.anchor = GridBagConstraints.NORTH;
		gbPanel1.setConstraints( btRemoveUsedBut, gbcPanel1 );
		pnPanel1.add( btRemoveUsedBut );
		gbcPanel0.gridx = 2;
		gbcPanel0.gridy = 5;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 5;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnPanel1, gbcPanel0 );
		pnPanel0.add( pnPanel1 );
		
		this.setLayout(new BorderLayout());
		this.add(pnPanel0, BorderLayout.CENTER);
	}

	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (MeasurementPortType)or;

		this.tfNameText.setText(type.getName());
		this.taDesrcArea.setText(type.getDescription());

		Collection mPTypes = null;
		Collection otherMPTypes = new LinkedList();
		
		try {
			LinkedIdsCondition condition = new LinkedIdsCondition(type.getId(), ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
			mPTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
			EquivalentCondition condition2 = new EquivalentCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE); 
			Collection allMPTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition2, true);
			
			for (Iterator it = allMPTypes.iterator(); it.hasNext();) {
				MeasurementType t = (MeasurementType)it.next();
				if (!mPTypes.contains(t))
					otherMPTypes.add(t); 
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		((ObjListModel)this.lsTypeUsedList.getModel()).removeAllElements();
		((ObjListModel)this.lsTypeUsedList.getModel()).addElements(mPTypes);
		((ObjListModel)this.lsTypeOtherList.getModel()).removeAllElements();
		((ObjListModel)this.lsTypeOtherList.getModel()).addElements(otherMPTypes);
	}

	public boolean modify() {
		if (MiscUtil.validName(tfNameText.getText()))
			this.type.setName(tfNameText.getText());
		else
			return false;

		this.type.setDescription(this.taDesrcArea.getText());
		
		ObjListModel usedModel = (ObjListModel)this.lsTypeUsedList.getModel();
		for (int i = 0; i < usedModel.getSize(); i++) {
			MeasurementType t = (MeasurementType)usedModel.getElementAt(i);
			Collection mpTypes = t.getMeasurementPortTypes();
			if (!mpTypes.contains(type)) {
				List newMPTypes = new ArrayList(mpTypes.size() + 1);
				newMPTypes.addAll(mpTypes);
				newMPTypes.add(type);
				t.setMeasurementPortTypes(newMPTypes);
			}
		}
		ObjListModel otherModel = (ObjListModel)this.lsTypeOtherList.getModel();
		for (int i = 0; i < otherModel.getSize(); i++) {
			MeasurementType t = (MeasurementType)otherModel.getElementAt(i);
			Collection mpTypes = t.getMeasurementPortTypes();
			if (mpTypes.contains(type)) {
				List newMPTypes = new LinkedList(mpTypes);
				newMPTypes.remove(type);
				t.setMeasurementPortTypes(newMPTypes);
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
	
	void btAddUsedBut_actionPerformed(ActionEvent e) {
		Object[] mTypes = lsTypeOtherList.getSelectedValues();
		for (int i = 0; i < mTypes.length; i++) {
			((ObjListModel) lsTypeUsedList.getModel()).addElement(mTypes[i]);
			((ObjListModel) lsTypeOtherList.getModel()).removeElement(mTypes[i]);
		}
	}
	
	void btRemoveUsedBut_actionPerformed(ActionEvent e) {
		Object[] mTypes = lsTypeUsedList.getSelectedValues();
		for (int i = 0; i < mTypes.length; i++) {
			((ObjListModel) lsTypeUsedList.getModel()).removeElement(mTypes[i]);
			((ObjListModel) lsTypeOtherList.getModel()).addElement(mTypes[i]);
		}
	}
}
