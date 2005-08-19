/*-
 * $Id: SchemeLinkGeneralPanel.java,v 1.16 2005/08/19 16:27:28 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkTypeWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.16 $, $Date: 2005/08/19 16:27:28 $
 * @module schemeclient
 */

public class SchemeLinkGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeLink schemeLink;
	
	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JPanel pnLinkPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton btCommitBut = new JButton();
	JLabel lbTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox cmbTypeCombo = new WrapperedComboBox(LinkTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel lbLengthLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LENGTH));
	JLabel lbOpticalLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.OPTICAL_LENGTH));
	NumberFormatter nf = new NumberFormatter(NumberFormat.getNumberInstance());
	JFormattedTextField tfOpticalText = new JFormattedTextField(this.nf);
	JLabel lbM2Label = new JLabel(LangModelScheme.getString(SchemeResourceKeys.METRE));
	JLabel lbPhysicalLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PHYSICAL_LENGTH));
	JFormattedTextField tfPhysicalText = new JFormattedTextField(this.nf);
	JLabel lbM1Label = new JLabel(LangModelScheme.getString(SchemeResourceKeys.METRE));
	JCheckBox cbLinkBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.INSTANCE));
	JLabel lbInvNumberLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.INVNUMBER));
	JTextField tfInvNumberText = new JTextField();
	JLabel lbSupplierLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER));
	JTextField tfSupplierText = new JTextField();
	JLabel lbSupplierCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER_CODE));
	JTextField tfSupplierCodeText = new JTextField();
	JLabel lbMarkLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField tfMarkText = new JTextField();
	JLabel lbColorLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.COLOR));
	JComboBox cmbColorCombo = new ColorChooserComboBox();
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2,10);
	
	protected SchemeLinkGeneralPanel(SchemeLink schemeLink) {
		this();
		setObject(schemeLink);
	}
	
	protected SchemeLinkGeneralPanel() {
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
		nf.setValueClass(Double.class);
		nf.setMinimum(new Double(0));
		nf.setCommitsOnValidEdit(true);
		
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 11;
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
		gbcPanel0.gridy = 12;
		gbcPanel0.gridwidth = 10;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescrArea, gbcPanel0 );
		pnPanel0.add( scpDescrArea );
		
		GridBagLayout gbPanel2 = new GridBagLayout();
		GridBagConstraints gbcPanel2 = new GridBagConstraints();
		pnGeneralPanel.setLayout( gbPanel2 );
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 0;
		gbcPanel2.gridwidth = 2;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( lbNameLabel, gbcPanel2 );
		pnGeneralPanel.add( lbNameLabel );
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 0;
		gbcPanel2.gridwidth = 8;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( tfNameText, gbcPanel2 );
		pnGeneralPanel.add( tfNameText );
		
		gbcPanel2.gridx = 10;
		gbcPanel2.gridy = 0;
		gbcPanel2.gridwidth = 1;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( btCommitBut, gbcPanel2 );
		pnGeneralPanel.add( btCommitBut );
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 1;
		gbcPanel2.gridwidth = 2;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( lbTypeLabel, gbcPanel2 );
		pnGeneralPanel.add( lbTypeLabel );
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 1;
		gbcPanel2.gridwidth = 9;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( cmbTypeCombo, gbcPanel2 );
		pnGeneralPanel.add( cmbTypeCombo );
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 2;
		gbcPanel2.gridwidth = 2;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( lbLengthLabel, gbcPanel2 );
		pnGeneralPanel.add( lbLengthLabel );
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 2;
		gbcPanel2.gridwidth = 3;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( lbOpticalLabel, gbcPanel2 );
		pnGeneralPanel.add( lbOpticalLabel );
		
		gbcPanel2.gridx = 5;
		gbcPanel2.gridy = 2;
		gbcPanel2.gridwidth = 5;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( tfOpticalText, gbcPanel2 );
		pnGeneralPanel.add( tfOpticalText );
		
		gbcPanel2.gridx = 10;
		gbcPanel2.gridy = 2;
		gbcPanel2.gridwidth = 1;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( lbM1Label, gbcPanel2 );
		pnGeneralPanel.add( lbM1Label );
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 3;
		gbcPanel2.gridwidth = 3;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( lbPhysicalLabel, gbcPanel2 );
		pnGeneralPanel.add( lbPhysicalLabel );
		
		gbcPanel2.gridx = 5;
		gbcPanel2.gridy = 3;
		gbcPanel2.gridwidth = 5;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( tfPhysicalText, gbcPanel2 );
		pnGeneralPanel.add( tfPhysicalText );
		
		gbcPanel2.gridx = 10;
		gbcPanel2.gridy = 3;
		gbcPanel2.gridwidth = 1;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( lbM2Label, gbcPanel2 );
		pnGeneralPanel.add( lbM2Label );
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 4;
		gbcPanel2.gridwidth = 10;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( cbLinkBox, gbcPanel2 );
		pnGeneralPanel.add( cbLinkBox );
		
		pnLinkPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbLinkPanel = new GridBagLayout();
		GridBagConstraints gbcLinkPanel = new GridBagConstraints();
		pnLinkPanel.setLayout( gbLinkPanel );
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 0;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( lbInvNumberLabel, gbcLinkPanel );
		pnLinkPanel.add( lbInvNumberLabel );
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 0;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( tfInvNumberText, gbcLinkPanel );
		pnLinkPanel.add( tfInvNumberText );
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 1;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( lbSupplierLabel, gbcLinkPanel );
		pnLinkPanel.add( lbSupplierLabel );
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 1;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( tfSupplierText, gbcLinkPanel );
		pnLinkPanel.add( tfSupplierText );
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 2;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( lbSupplierCodeLabel, gbcLinkPanel );
		pnLinkPanel.add( lbSupplierCodeLabel );
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 2;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( tfSupplierCodeText, gbcLinkPanel );
		pnLinkPanel.add( tfSupplierCodeText );
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 3;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( lbMarkLabel, gbcLinkPanel );
		pnLinkPanel.add( lbMarkLabel );
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 3;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( tfMarkText, gbcLinkPanel );
		pnLinkPanel.add( tfMarkText );
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 4;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( lbColorLabel, gbcLinkPanel );
		pnLinkPanel.add( lbColorLabel );
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 4;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints( cmbColorCombo, gbcLinkPanel );
		pnLinkPanel.add( cmbColorCombo );
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 5;
		gbcPanel2.gridwidth = 12;
		gbcPanel2.gridheight = 6;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints( pnLinkPanel, gbcPanel2 );
		pnGeneralPanel.add( pnLinkPanel );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 12;
		gbcPanel0.gridheight = 11;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );
		
		
		cbLinkBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setLinkEnabled(cbLinkBox.isSelected());
			}
		});
		
		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
		taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(cmbTypeCombo);
		addToUndoableListener(tfOpticalText);
		addToUndoableListener(tfPhysicalText);
		addToUndoableListener(cbLinkBox);
		addToUndoableListener(tfInvNumberText);
		addToUndoableListener(tfSupplierText);
		addToUndoableListener(tfSupplierCodeText);
		addToUndoableListener(tfMarkText);
		addToUndoableListener(cmbColorCombo);
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
	
	public JComponent getGUI() {
		return this.pnPanel0;
	}
	
	public void setObject(Object or) {
		this.schemeLink = (SchemeLink)or;
		
		this.cmbTypeCombo.removeAllItems();
		if (this.schemeLink != null) {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
			try {
				this.cmbTypeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			boolean b = false;
			try {
				b = this.schemeLink.getParentScheme() != null;
			} catch (IllegalStateException e) {
				// igore, same as schemeLink.getParentScheme() == null
			}
			this.cbLinkBox.setVisible(b); 
		}
		
		Link link = null;
		setLinkEnabled(false);
		
		if (this.schemeLink != null) {
			this.tfNameText.setText(this.schemeLink.getName());
			this.taDescrArea.setText(this.schemeLink.getDescription());
			this.tfOpticalText.setValue(Double.valueOf(this.schemeLink.getOpticalLength()));
			this.tfPhysicalText.setValue(Double.valueOf(this.schemeLink.getPhysicalLength()));
			this.cmbTypeCombo.setSelectedItem(this.schemeLink.getAbstractLinkType());
			link = this.schemeLink.getAbstractLink();
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescrArea.setText(SchemeResourceKeys.EMPTY);
			this.tfOpticalText.setText(SchemeResourceKeys.EMPTY);
			this.tfPhysicalText.setText(SchemeResourceKeys.EMPTY);
		}
		if (link != null) {
			this.cbLinkBox.setSelected(true);
			setLinkEnabled(true);
			this.tfInvNumberText.setText(link.getInventoryNo());
			this.tfSupplierText.setText(link.getSupplier());
			this.tfSupplierCodeText.setText(link.getSupplierCode());
			this.tfMarkText.setText(link.getMark());
			Color color = new Color(link.getColor());
			this.cmbColorCombo.addItem(color);
		} else {
			this.cbLinkBox.setSelected(false);
			setLinkEnabled(false);
			this.tfInvNumberText.setText(SchemeResourceKeys.EMPTY);
			this.tfSupplierText.setText(SchemeResourceKeys.EMPTY);
			this.tfSupplierCodeText.setText(SchemeResourceKeys.EMPTY);
			this.tfMarkText.setText(SchemeResourceKeys.EMPTY);
		}
	}
	
	public Object getObject() {
		return this.schemeLink;
	}
	
	public void commitChanges() {
		super.commitChanges();
		if (this.schemeLink != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemeLink.setName(this.tfNameText.getText());
			this.schemeLink.setDescription(this.taDescrArea.getText());
			this.schemeLink.setAbstractLinkType((AbstractLinkType)this.cmbTypeCombo.getSelectedItem());
			this.schemeLink.setOpticalLength((Double)(this.tfOpticalText.getValue()));
			this.schemeLink.setPhysicalLength((Double)(this.tfPhysicalText.getValue()));

			Link link = this.schemeLink.getAbstractLink();
			if (this.cbLinkBox.isSelected()) {
				if (link == null) {
					try {
						link = SchemeObjectsFactory.createLink(this.schemeLink);
					} catch (CreateObjectException e) {
						Log.errorException(e);
					}
				}
				if (link != null) {
					link.setName(this.schemeLink.getName());
					link.setDescription(this.schemeLink.getDescription());
					link.setType(this.schemeLink.getAbstractLinkType());
					
					// TODO add link.setInventoryNo()
					// link.setInventoryNo(invNumberText.getText());
					link.setSupplier(this.tfSupplierText.getText());
					link.setSupplierCode(this.tfSupplierCodeText.getText());
					link.setMark(this.tfMarkText.getText());
					link.setColor(((Color) this.cmbColorCombo.getSelectedItem()).getRGB());
				}	
			} else if (link != null) {
				this.schemeLink.setAbstractLink(null);
				StorableObjectPool.delete(link.getId());
			}
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemeLink.getId(), SchemeEvent.UPDATE_OBJECT));
		}
	}
	
	void setLinkEnabled(boolean b) {
		this.pnLinkPanel.setVisible(b);
	}
}
