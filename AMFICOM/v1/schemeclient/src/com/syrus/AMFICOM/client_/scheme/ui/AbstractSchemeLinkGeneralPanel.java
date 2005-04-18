/*-
 * $Id: AbstractSchemeLinkGeneralPanel.java,v 1.1 2005/04/18 10:45:17 stas Exp $
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
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.Constants;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public abstract class AbstractSchemeLinkGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected AbstractSchemeLink schemeLink;
	
	static JColorChooser tcc;
	JPanel panel0 = new JPanel();
	JPanel generalPanel = new JPanel();
	JLabel nameLabel = new JLabel(LangModelScheme.getString(Constants.NAME));
	JTextField nameText = new JTextField();
	JLabel typeLabel = new JLabel(LangModelScheme.getString(Constants.TYPE));
	ObjComboBox typeCombo = new ObjComboBox(LinkTypeController.getInstance(), StorableObjectWrapper.COLUMN_NAME);
	JLabel opticalLabel = new JLabel(LangModelScheme.getString(Constants.OPTICAL_LENGTH));
	JTextField opticalText = new JTextField();
	JLabel physicalLabel = new JLabel(LangModelScheme.getString(Constants.PHYSICAL_LENGTH));
	JTextField physicalText = new JTextField();
	JCheckBox linkBox = new JCheckBox(LangModelScheme.getString(Constants.INSTANCE));
	JLabel invNumberLabel = new JLabel(LangModelScheme.getString(Constants.INVNUMBER));
	JTextField invNumberText = new JTextField();
	JLabel supplierLabel = new JLabel(LangModelScheme.getString(Constants.SUPPLIER));
	JTextField supplierText = new JTextField();
	JLabel supplierCodeLabel = new JLabel(LangModelScheme.getString(Constants.SUPPLIER_CODE));
	JTextField supplierCodeText = new JTextField();
	JLabel markLabel = new JLabel(LangModelScheme.getString(Constants.LABEL));
	JTextField markText = new JTextField();
	JLabel colorLabel = new JLabel(LangModelScheme.getString(Constants.COLOR));
	JComboBox colorCombo = new JComboBox();
	JButton colorBut = new JButton();
	JLabel descrLabel = new JLabel(LangModelScheme.getString(Constants.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2,10);
	
	protected AbstractSchemeLinkGeneralPanel(AbstractSchemeLink schemeLink) {
		this();
		setObject(schemeLink);
	}
	
	protected AbstractSchemeLinkGeneralPanel() {
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

		opticalLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(opticalLabel, gbcgeneralPanel);
		generalPanel.add(opticalLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(opticalText, gbcgeneralPanel);
		generalPanel.add(opticalText);

		physicalLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 4;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,1);
		gbgeneralPanel.setConstraints(physicalLabel, gbcgeneralPanel);
		generalPanel.add(physicalLabel);

		gbcgeneralPanel.gridx = 5;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 3;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(physicalText, gbcgeneralPanel);
		generalPanel.add(physicalText);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 3;
		gbcgeneralPanel.gridwidth = 3;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(linkBox, gbcgeneralPanel);
		generalPanel.add(linkBox);

		invNumberLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(invNumberLabel, gbcgeneralPanel);
		generalPanel.add(invNumberLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 4;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(invNumberText, gbcgeneralPanel);
		generalPanel.add(invNumberText);

		supplierLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(supplierLabel, gbcgeneralPanel);
		generalPanel.add(supplierLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 5;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(supplierText, gbcgeneralPanel);
		generalPanel.add(supplierText);

		supplierCodeLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,0,0,1);
		gbgeneralPanel.setConstraints(supplierCodeLabel, gbcgeneralPanel);
		generalPanel.add(supplierCodeLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 6;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(supplierCodeText, gbcgeneralPanel);
		generalPanel.add(supplierCodeText);

		markLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 7;
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
		gbcgeneralPanel.gridy = 7;
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
		gbcgeneralPanel.gridy = 8;
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
		gbcgeneralPanel.gridy = 8;
		gbcgeneralPanel.gridwidth = 5;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,1);
		gbgeneralPanel.setConstraints(colorCombo, gbcgeneralPanel);
		generalPanel.add(colorCombo);

		gbcgeneralPanel.gridx = 7;
		gbcgeneralPanel.gridy = 8;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcgeneralPanel.insets = new Insets(0,1,0,0);
		gbgeneralPanel.setConstraints(colorBut, gbcgeneralPanel);
		generalPanel.add(colorBut);

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
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 0;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 9;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(generalPanel, gbcpanel0);
		panel0.add(generalPanel);

		descrLabel.setFocusable(false);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 9;
		gbcpanel0.gridwidth = 3;
		gbcpanel0.gridheight = 1;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 0;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(descrLabel, gbcpanel0);
		panel0.add(descrLabel);

		JScrollPane scpdescrArea = new JScrollPane(descrArea);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 10;
		gbcpanel0.gridwidth = 8;
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
		linkBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setLinkEnabled(linkBox.isSelected());
			}
		});
		
		generalPanel.setBorder( BorderFactory.createTitledBorder( LangModelScheme.getString(Constants.EMPTY )));
		descrArea.setPreferredSize(Constants.DIMENSION_TEXTAREA);
		
		addToUndoableListener(nameText);
		addToUndoableListener(typeCombo);
		addToUndoableListener(opticalText);
		addToUndoableListener(physicalText);
		addToUndoableListener(linkBox);
		addToUndoableListener(invNumberText);
		addToUndoableListener(supplierText);
		addToUndoableListener(supplierCodeText);
		addToUndoableListener(markText);
		addToUndoableListener(colorCombo);
		addToUndoableListener(colorBut);
		addToUndoableListener(descrArea);
		
	}
	
	public JComponent getGUI() {
		return panel0;
	}

	public void setObject(Object or) {
		Link link = null;
		setLinkEnabled(false);
		
		if (schemeLink != null) {
			this.nameText.setText(schemeLink.getName());
			this.descrArea.setText(schemeLink.getDescription());
			this.opticalText.setText(Double.toString(schemeLink.getOpticalLength()));
			this.physicalText.setText(Double.toString(schemeLink.getPhysicalLength()));
			this.typeCombo.setSelectedItem(schemeLink.getAbstractLinkType());
			link = schemeLink.getLink();
		}
		else {
			this.nameText.setText(LangModelScheme.getString(Constants.EMPTY));
			this.descrArea.setText(LangModelScheme.getString(Constants.EMPTY));
			this.opticalText.setText(LangModelScheme.getString(Constants.EMPTY));
			this.physicalText.setText(LangModelScheme.getString(Constants.EMPTY));
		}
		if (link != null) {
			linkBox.setSelected(true);
			invNumberText.setText(link.getInventoryNo());
			supplierText.setText(link.getSupplier());
			supplierCodeText.setText(link.getSupplierCode());
			markText.setText(link.getMark());
			Color color = new Color(link.getColor());
			if (!isConatainsColor(color))
				colorCombo.addItem(color);
			colorCombo.setSelectedItem(color);
		}
		else {
			linkBox.setSelected(false);
			invNumberText.setText(LangModelScheme.getString(Constants.EMPTY));
			supplierText.setText(LangModelScheme.getString(Constants.EMPTY));
			supplierCodeText.setText(LangModelScheme.getString(Constants.EMPTY));
			markText.setText(LangModelScheme.getString(Constants.EMPTY));
		}
	}

	public Object getObject() {
		return schemeLink;
	}

	public void commitChanges() {
		if (schemeLink != null) {
			schemeLink.setName(this.nameText.getText());
			schemeLink.setDescription(this.descrArea.getText());
			schemeLink.setAbstractLinkType((AbstractLinkType)this.typeCombo.getSelectedItem());
			try {
				schemeLink.setOpticalLength(Double.parseDouble(opticalText.getText()));
			} catch (NumberFormatException e) {
				schemeLink.setOpticalLength(0);
			}
			try {
				schemeLink.setPhysicalLength(Double.parseDouble(physicalText.getText()));
			} catch (NumberFormatException e1) {
				schemeLink.setPhysicalLength(0);
			}
			/**
			 * create Link instance and binding to SchemeLink became in descendants
			 */
			Link link = schemeLink.getLink();
			if (link != null) {
				link.setName(schemeLink.getName());
				link.setDescription(schemeLink.getDescription());
				link.setType(schemeLink.getAbstractLinkType());

				// TODO add link.setInventoryNo()
				// link.setInventoryNo(invNumberText.getText());
				link.setSupplier(supplierText.getText());
				link.setSupplierCode(supplierCodeText.getText());
				link.setMark(markText.getText());
				link.setColor(((Color) colorCombo.getSelectedItem()).getRGB());
			}
			aContext.getDispatcher().notify(new SchemeEvent(this, schemeLink, SchemeEvent.UPDATE_OBJECT));
		}
	}
	
	void setLinkEnabled(boolean b) {
		invNumberLabel.setEnabled(b);
		invNumberText.setEnabled(b);
		supplierLabel.setEnabled(b);
		supplierText.setEnabled(b);
		supplierCodeLabel.setEnabled(b);
		supplierCodeText.setEnabled(b);
		markLabel.setEnabled(b);
		markText.setEnabled(b);
		colorLabel.setEnabled(b);
		colorCombo.setEnabled(b);
		colorBut.setEnabled(b);
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
