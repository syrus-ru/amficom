/*-
 * $Id: SchemePortGeneralPanel.java,v 1.2 2005/04/18 10:57:46 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.Constants;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 10:57:46 $
 * @module schemeclient_v1
 */

public class SchemePortGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemePort schemePort;
	
	static JColorChooser tcc;
	JPanel panel0 = new JPanel();
	JPanel generalPanel = new JPanel();
	JLabel nameLabel = new JLabel(LangModelScheme.getString(Constants.NAME));
	JTextField nameText = new JTextField();
	JLabel typeLabel = new JLabel(LangModelScheme.getString(Constants.TYPE));
	ObjComboBox typeCombo = new ObjComboBox(PortTypeController.getInstance(), StorableObjectWrapper.COLUMN_NAME);
	JCheckBox portBox = new JCheckBox();
	JLabel markLabel = new JLabel(LangModelScheme.getString(Constants.LABEL));
	JTextField markText = new JTextField();
	JLabel colorLabel = new JLabel(LangModelScheme.getString(Constants.COLOR));
	JComboBox colorCombo = new JComboBox();
	JButton colorBut = new JButton();
	JCheckBox mpBox = new JCheckBox();
	JLabel mpTypeLabel = new JLabel(LangModelScheme.getString(Constants.TYPE));
	ObjComboBox mpTypeCombo = new ObjComboBox(MeasurementPortTypeController.getInstance(), StorableObjectWrapper.COLUMN_NAME);
	JLabel descrLabel = new JLabel(LangModelScheme.getString(Constants.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2,10);
	
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
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(nameLabel, gbcgeneralPanel);
		generalPanel.add(nameLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(nameText, gbcgeneralPanel);
		generalPanel.add(nameText);

		typeLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(typeLabel, gbcgeneralPanel);
		generalPanel.add(typeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 1;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(typeCombo, gbcgeneralPanel);
		generalPanel.add(typeCombo);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 3;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(portBox, gbcgeneralPanel);
		generalPanel.add(portBox);

		markLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(markLabel, gbcgeneralPanel);
		generalPanel.add(markLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(markText, gbcgeneralPanel);
		generalPanel.add(markText);

		colorLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(colorLabel, gbcgeneralPanel);
		generalPanel.add(colorLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(colorCombo, gbcgeneralPanel);
		generalPanel.add(colorCombo);

		gbcgeneralPanel.gridx = 7;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(colorBut, gbcgeneralPanel);
		generalPanel.add(colorBut);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 3;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(mpBox, gbcgeneralPanel);
		generalPanel.add(mpBox);

		mpTypeLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(mpTypeLabel, gbcgeneralPanel);
		generalPanel.add(mpTypeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(mpTypeCombo, gbcgeneralPanel);
		generalPanel.add(mpTypeCombo);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 0;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 8;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(generalPanel, gbcpanel0);
		panel0.add(generalPanel);

		descrLabel.setFocusable(false);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 8;
		gbcpanel0.gridwidth = 3;
		gbcpanel0.gridheight = 1;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 0;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(descrLabel, gbcpanel0);
		panel0.add(descrLabel);

		JScrollPane scpdescrArea = new JScrollPane(descrArea);
		gbcpanel0.gridx = 1;
		gbcpanel0.gridy = 9;
		gbcpanel0.gridwidth = 7;
		gbcpanel0.gridheight = 2;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 1;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(scpdescrArea, gbcpanel0);
		panel0.add(scpdescrArea);
		
		colorCombo.setRenderer(ColorListCellRenderer.getInstance());
		for (int i = 0; i < Constants.DEFAULT_COLOR_SET.length; i++)
			colorCombo.addItem(Constants.DEFAULT_COLOR_SET[i]);
		
		colorBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorButton_actionPerformed(e);
			}
		});
		portBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setPortEnabled(portBox.isSelected());
				if (!portBox.isSelected())
					mpBox.setSelected(false);
			}
		});
		mpBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setMPTypeEnabled(mpBox.isSelected());
			}
		});
		generalPanel.setBorder( BorderFactory.createTitledBorder( Constants.EMPTY ));
		descrArea.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		
		addToUndoableListener(nameText);
		addToUndoableListener(typeCombo);
		addToUndoableListener(portBox);
		addToUndoableListener(markText);
		addToUndoableListener(colorCombo);
		addToUndoableListener(colorBut);
		addToUndoableListener(mpBox);
		addToUndoableListener(mpTypeCombo);
		addToUndoableListener(descrArea);
	}
	
	void setPortEnabled(boolean b) {
		markLabel.setEnabled(b);
		markText.setEnabled(b);
		colorLabel.setEnabled(b);
		colorCombo.setEnabled(b);
		colorBut.setEnabled(b);
	}
	
	void setMPTypeEnabled(boolean b) {
		mpTypeLabel.setEnabled(b);
		mpTypeCombo.setEnabled(b);
	}
	
	public JComponent getGUI() {
		return panel0;
	}

	public void setObject(Object or) {
		this.schemePort = (SchemePort)or;
		MeasurementPort mPort = null;
		Port port = null;
		typeCombo.removeAllItems();
		mpTypeCombo.removeAllItems();
		
		if (schemePort != null) {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PORTTYPE_ENTITY_CODE);
			try {
				typeCombo.addElements(ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
			try {
				mpTypeCombo.addElements(ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			
			this.nameText.setText(schemePort.getName());
			this.descrArea.setText(schemePort.getDescription());
			this.typeCombo.setSelectedItem(schemePort.getPortType());
			port = schemePort.getPort();
			mPort = schemePort.getMeasurementPort();
		}
		if (port != null) {
			portBox.setSelected(true);
			// TODO add mark and color fields to Port
//			markText.setText(port.getMark());
//			Color color = new Color(port.getColor());
//			if (!isConatainsColor(color))
//				colorCombo.addItem(color);
//			colorCombo.setSelectedItem(color);
		}
		if (mPort != null) {
			mpBox.setSelected(true);
			mpTypeCombo.setSelectedItem(mPort.getType());
		}
	}

	public Object getObject() {
		return schemePort;
	}

	public void commitChanges() {
		if (schemePort != null && MiscUtil.validName(this.nameText.getText())) {
			schemePort.setName(this.nameText.getText());
			schemePort.setDescription(this.descrArea.getText());
			schemePort.setPortType((PortType)this.typeCombo.getSelectedItem());
			
			Port port = schemePort.getPort();
			if (portBox.isSelected()) {
				if (port == null) {
					try {
						port = SchemeObjectsFactory.createPort(PortSort.PORT_SORT_PORT);
						schemePort.setPort(port);
					} catch (CreateObjectException e) {
						Log.errorException(e);
					}
				}
				if (port != null) {
					port.setDescription(schemePort.getDescription());
					// TODO add mark and color fields to Port
//					port.setMark(markText.getText());
//					port.setColor(((Color) colorCombo.getSelectedItem()).getRGB());
				}
			}
			else if (port != null) {
				ConfigurationStorableObjectPool.delete(port.getId());
				schemePort.setPort(null);
			}
			
			MeasurementPort mp = schemePort.getMeasurementPort();
			if (mpBox.isSelected()) {
				if (mp == null) {
					try {
						mp = SchemeObjectsFactory.createMeasurementPort();
						schemePort.setMeasurementPort(mp);
					} catch (CreateObjectException e) {
						Log.errorException(e);
					}
				}
				if (mp != null) {
					mp.setName(schemePort.getName());
					mp.setDescription(schemePort.getDescription());
					mp.setType((MeasurementPortType)mpTypeCombo.getSelectedItem());
					// TODO check kis presents
					mp.setKISId(schemePort.getParentSchemeDevice().getParentSchemeElement().getKis().getId());
					// XXX a nax nam tut vawe port?
					mp.setPortId(schemePort.getPort().getId());
				}
			}
			else if (mp != null) {
				ConfigurationStorableObjectPool.delete(mp.getId());
				schemePort.setMeasurementPort(null);
			} 
			aContext.getDispatcher().notify(new SchemeEvent(this, schemePort, SchemeEvent.UPDATE_OBJECT));
		}
	}
	
	void colorButton_actionPerformed(ActionEvent e) {
		if (tcc == null)
			tcc = new JColorChooser((Color) colorCombo.getSelectedItem());
		else
			tcc.setColor((Color) colorCombo.getSelectedItem());

		int res = JOptionPane.showOptionDialog(Environment.getActiveWindow(), tcc,
				LangModelConfig.getString("label_chooseColor"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,
				null);
		if (res == JOptionPane.OK_OPTION) {
			Color newColor = tcc.getColor();
			if (isConatainsColor(newColor)) {
				colorCombo.setSelectedItem(newColor);
				return;
			}
			colorCombo.addItem(newColor);
			colorCombo.setSelectedItem(newColor);
		}
	}
	
	boolean isConatainsColor(Color color) {
		for (int i = 0; i < colorCombo.getItemCount(); i++)
			if (((Color)colorCombo.getItemAt(i)).equals(color))
				return true;
		return false;
	}
}
