/*-
 * $Id: SchemePortGeneralPanel.java,v 1.16 2005/08/05 12:40:00 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeWrapper;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.16 $, $Date: 2005/08/05 12:40:00 $
 * @module schemeclient_v1
 */

public class SchemePortGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemePort schemePort;
	protected SchemeElement parent;
	
	static JColorChooser tcc;
	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JPanel pnPortPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton btCommitBut = new JButton();
	JLabel lbTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox cmbTypeCombo = new WrapperedComboBox(PortTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel lbPortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.INSTANCE));
	JLabel lbMarkLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField tfMarkText = new JTextField();
	JLabel lbColorLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.COLOR));
	JComboBox cmbColorCombo = new ColorChooserComboBox();
	JCheckBox cbMpBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.MEASUREMENT_PORT));
	JLabel lbMpTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox cmbMpTypeCombo = new WrapperedComboBox(MeasurementPortTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2,10);
	
	protected SchemePortGeneralPanel(SchemePort schemePort) {
		this();
		setObject(schemePort);
	}
	
	protected SchemePortGeneralPanel() {
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

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 8;
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbDescrLabel, gbcPanel0 );
		pnPanel0.add( lbDescrLabel );

		JScrollPane scpDescrArea = new JScrollPane( taDescrArea );
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 9;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescrArea, gbcPanel0 );
		pnPanel0.add( scpDescrArea );

		GridBagLayout gbGeneralPanel = new GridBagLayout();
		GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		pnGeneralPanel.setLayout( gbGeneralPanel );

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbNameLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbNameLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 7;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfNameText, gbcGeneralPanel );
		pnGeneralPanel.add( tfNameText );

		gbcGeneralPanel.gridx = 9;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( btCommitBut, gbcGeneralPanel );
		pnGeneralPanel.add( btCommitBut );

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbTypeLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbTypeLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( cmbTypeCombo, gbcGeneralPanel );
		pnGeneralPanel.add( cmbTypeCombo );

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 9;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbPortLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbPortLabel );

		pnPortPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbPortPanel = new GridBagLayout();
		GridBagConstraints gbcPortPanel = new GridBagConstraints();
		pnPortPanel.setLayout( gbPortPanel );

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 0;
		gbcPortPanel.gridwidth = 2;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints( lbMarkLabel, gbcPortPanel );
		pnPortPanel.add( lbMarkLabel );

		gbcPortPanel.gridx = 2;
		gbcPortPanel.gridy = 0;
		gbcPortPanel.gridwidth = 8;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 1;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints( tfMarkText, gbcPortPanel );
		pnPortPanel.add( tfMarkText );

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 1;
		gbcPortPanel.gridwidth = 2;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints( lbColorLabel, gbcPortPanel );
		pnPortPanel.add( lbColorLabel );

		gbcPortPanel.gridx = 2;
		gbcPortPanel.gridy = 1;
		gbcPortPanel.gridwidth = 8;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 1;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints( cmbColorCombo, gbcPortPanel );
		pnPortPanel.add( cmbColorCombo );

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 2;
		gbcPortPanel.gridwidth = 9;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints( cbMpBox, gbcPortPanel );
		pnPortPanel.add( cbMpBox );

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 3;
		gbcPortPanel.gridwidth = 2;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints( lbMpTypeLabel, gbcPortPanel );
		pnPortPanel.add( lbMpTypeLabel );

		gbcPortPanel.gridx = 2;
		gbcPortPanel.gridy = 3;
		gbcPortPanel.gridwidth = 8;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 1;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints( cmbMpTypeCombo, gbcPortPanel );
		pnPortPanel.add( cmbMpTypeCombo );
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 10;
		gbcGeneralPanel.gridheight = 5;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( pnPortPanel, gbcGeneralPanel );
		pnGeneralPanel.add( pnPortPanel );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 11;
		gbcPanel0.gridheight = 8;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );
		
		cbMpBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setMPTypeEnabled(cbMpBox.isSelected());
			}
		});
		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
		taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(cmbTypeCombo);
		addToUndoableListener(tfMarkText);
		addToUndoableListener(cmbColorCombo);
		addToUndoableListener(cbMpBox);
		addToUndoableListener(cmbMpTypeCombo);
		addToUndoableListener(taDescrArea);
		
		this.btCommitBut.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.btCommitBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.btCommitBut.setFocusPainted(false);
		this.btCommitBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.btCommitBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});
	}
	
	void setPortEnabled(boolean b) {
		this.lbPortLabel.setVisible(b);
		this.pnPortPanel.setVisible(b);
		
		if (b && this.parent != null && this.parent.getKis() != null)
			setMPTypeEnabled(true);
		else
			setMPTypeEnabled(false);			
	}
	
	void setMPTypeEnabled(boolean b) {
		this.cbMpBox.setVisible(b);
		this.lbMpTypeLabel.setVisible(b);
		this.cmbMpTypeCombo.setVisible(b);
	}
	
	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public void setObject(Object or) {
		this.schemePort = (SchemePort)or;
		MeasurementPort mPort = null;
		Port port = null;
		this.cmbTypeCombo.removeAllItems();
		this.cmbMpTypeCombo.removeAllItems();
		
		if (this.schemePort != null) {
			try {
				this.parent = this.schemePort.getParentSchemeDevice().getParentSchemeElement();
				port = this.schemePort.getPort();
				mPort = this.schemePort.getMeasurementPort();
			} catch (IllegalStateException e1) {
				Log.debugMessage(this.getClass().getName() + ": SchemeDevice has no parent SchemeElement yet", Level.FINEST); //$NON-NLS-1$
				this.parent = null;
			}
			TypicalCondition condition1 = new TypicalCondition(PortTypeKind._PORT_KIND_SIMPLE, 0, OperationSort.OPERATION_EQUALS,ObjectEntities.PORT_TYPE_CODE, PortTypeWrapper.COLUMN_KIND);
			try {
				this.cmbTypeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition1, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			EquivalentCondition condition2 = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
			try {
				this.cmbMpTypeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition2, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			
			this.tfNameText.setText(this.schemePort.getName());
			this.taDescrArea.setText(this.schemePort.getDescription());
			this.cmbTypeCombo.setSelectedItem(this.schemePort.getPortType());
		}
		if (port != null) {
			setPortEnabled(true);
			// TODO add mark and color fields to Port
//			markText.setText(port.getMark());
//			Color color = new Color(port.getColor());
//			if (!isConatainsColor(color))
//				colorCombo.addItem(color);
//			colorCombo.setSelectedItem(color);
		} else {
			setPortEnabled(false);
			this.tfMarkText.setText(SchemeResourceKeys.EMPTY);
		}
		if (mPort != null) {
			this.cbMpBox.setSelected(true);
			this.cmbMpTypeCombo.setSelectedItem(mPort.getType());
		} else {
			this.cbMpBox.setSelected(false);
			setMPTypeEnabled(false);
		}
	}

	public Object getObject() {
		return this.schemePort;
	}

	public void commitChanges() {
		if (this.schemePort != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemePort.setName(this.tfNameText.getText());
			this.schemePort.setDescription(this.taDescrArea.getText());
			this.schemePort.setPortType((PortType)this.cmbTypeCombo.getSelectedItem());
			
			Port port = this.schemePort.getPort();
			if (port != null) {
				port.setDescription(this.schemePort.getDescription());
				// TODO add mark and color fields to Port
//				port.setMark(markText.getText());
//				port.setColor(((Color) colorCombo.getSelectedItem()).getRGB());
			}

			MeasurementPort mp = this.schemePort.getMeasurementPort();
			if (this.cbMpBox.isSelected()) {
				if (mp == null) {
					if (this.parent != null && this.parent.getKis() != null) {
						try {
							MeasurementPortType mpType = (MeasurementPortType) this.cmbMpTypeCombo.getSelectedItem();
							mp = SchemeObjectsFactory.createMeasurementPort(mpType, this.schemePort);
							this.schemePort.setMeasurementPort(mp);
						} catch (CreateObjectException e) {
							Log.errorException(e);
						}
					} else {
						Log.debugMessage("Parent KIS not created. Cannot create MeasurementPort", Level.FINEST); //$NON-NLS-1$
					}
				} else if (mp != null) {
					mp.setName(this.schemePort.getName());
					mp.setDescription(this.schemePort.getDescription());
					mp.setType((MeasurementPortType)this.cmbMpTypeCombo.getSelectedItem());
				}
			} else if (mp != null) {
				StorableObjectPool.delete(mp.getId());
				this.schemePort.setMeasurementPort(null);
			} 
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemePort, SchemeEvent.UPDATE_OBJECT));
		}
	}
}
