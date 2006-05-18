/*-
 * $Id: SchemeCableLinkGeneralPanel.java,v 1.29.2.1 2006/05/18 17:50:01 bass Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.configuration.CableLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableLinkTypeWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.29.2.1 $, $Date: 2006/05/18 17:50:01 $
 * @module schemeclient
 */

public class SchemeCableLinkGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeCableLink schemeCableLink;

	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JPanel pnLinkPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton btCommitBut = new JButton();
	JLabel lbTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox<CableLinkType> cmbTypeCombo = new WrapperedComboBox<CableLinkType>(CableLinkTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel lbLengthLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LENGTH));
	JLabel lbOpticalLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.OPTICAL_LENGTH));
	NumberFormatter nf = new NumberFormatter(NumberFormat.getNumberInstance());
	JFormattedTextField tfOpticalText = new JFormattedTextField(this.nf);
	JLabel lbPhysicalLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PHYSICAL_LENGTH));
	JFormattedTextField tfPhysicalText = new JFormattedTextField(this.nf);
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

	protected SchemeCableLinkGeneralPanel(final SchemeCableLink schemeLink) {
		this();
		this.setObject(schemeLink);
	}

	protected SchemeCableLinkGeneralPanel() {
		super();

		this.nf.setValueClass(Double.class);
		this.nf.setMinimum(new Double(0));
		this.nf.setCommitsOnValidEdit(true);
		
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 11;
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.lbDescrLabel, gbcPanel0);
		this.pnPanel0.add(this.lbDescrLabel);
		
		final JScrollPane scpDescrArea = new JScrollPane(this.taDescrArea);
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 12;
		gbcPanel0.gridwidth = 10;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(scpDescrArea, gbcPanel0);
		this.pnPanel0.add(scpDescrArea);
		
		final GridBagLayout gbPanel2 = new GridBagLayout();
		final GridBagConstraints gbcPanel2 = new GridBagConstraints();
		this.pnGeneralPanel.setLayout(gbPanel2);
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 0;
		gbcPanel2.gridwidth = 2;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.lbNameLabel, gbcPanel2);
		this.pnGeneralPanel.add(this.lbNameLabel);
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 0;
		gbcPanel2.gridwidth = 8;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.tfNameText, gbcPanel2);
		this.pnGeneralPanel.add(this.tfNameText);
		
		gbcPanel2.gridx = 10;
		gbcPanel2.gridy = 0;
		gbcPanel2.gridwidth = 1;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.btCommitBut, gbcPanel2);
		this.pnGeneralPanel.add(this.btCommitBut);
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 1;
		gbcPanel2.gridwidth = 2;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.lbTypeLabel, gbcPanel2);
		this.pnGeneralPanel.add(this.lbTypeLabel);
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 1;
		gbcPanel2.gridwidth = 9;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.cmbTypeCombo, gbcPanel2);
		this.pnGeneralPanel.add(this.cmbTypeCombo);
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 2;
		gbcPanel2.gridwidth = 2;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.lbLengthLabel, gbcPanel2);
		this.pnGeneralPanel.add(this.lbLengthLabel);
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 2;
		gbcPanel2.gridwidth = 3;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.lbOpticalLabel, gbcPanel2);
		this.pnGeneralPanel.add(this.lbOpticalLabel);
		
		gbcPanel2.gridx = 5;
		gbcPanel2.gridy = 2;
		gbcPanel2.gridwidth = 6;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.tfOpticalText, gbcPanel2);
		this.pnGeneralPanel.add(this.tfOpticalText);
		
		gbcPanel2.gridx = 2;
		gbcPanel2.gridy = 3;
		gbcPanel2.gridwidth = 3;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.lbPhysicalLabel, gbcPanel2);
		this.pnGeneralPanel.add(this.lbPhysicalLabel);
		
		gbcPanel2.gridx = 5;
		gbcPanel2.gridy = 3;
		gbcPanel2.gridwidth = 6;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 1;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.tfPhysicalText, gbcPanel2);
		this.pnGeneralPanel.add(this.tfPhysicalText);
		
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 4;
		gbcPanel2.gridwidth = 10;
		gbcPanel2.gridheight = 1;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.cbLinkBox, gbcPanel2);
		this.pnGeneralPanel.add(this.cbLinkBox);
		
		this.pnLinkPanel.setBorder(BorderFactory.createTitledBorder(""));
		final GridBagLayout gbLinkPanel = new GridBagLayout();
		final GridBagConstraints gbcLinkPanel = new GridBagConstraints();
		this.pnLinkPanel.setLayout(gbLinkPanel);
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 0;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.lbInvNumberLabel, gbcLinkPanel);
		this.pnLinkPanel.add(this.lbInvNumberLabel);
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 0;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.tfInvNumberText, gbcLinkPanel);
		this.pnLinkPanel.add(this.tfInvNumberText);
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 1;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.lbSupplierLabel, gbcLinkPanel);
		this.pnLinkPanel.add(this.lbSupplierLabel);
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 1;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.tfSupplierText, gbcLinkPanel);
		this.pnLinkPanel.add(this.tfSupplierText);
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 2;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.lbSupplierCodeLabel, gbcLinkPanel);
		this.pnLinkPanel.add(this.lbSupplierCodeLabel);
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 2;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.tfSupplierCodeText, gbcLinkPanel);
		this.pnLinkPanel.add(this.tfSupplierCodeText);
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 3;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.lbMarkLabel, gbcLinkPanel);
		this.pnLinkPanel.add(this.lbMarkLabel);
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 3;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.tfMarkText, gbcLinkPanel);
		this.pnLinkPanel.add(this.tfMarkText);
		
		gbcLinkPanel.gridx = 0;
		gbcLinkPanel.gridy = 4;
		gbcLinkPanel.gridwidth = 2;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 0;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.lbColorLabel, gbcLinkPanel);
		this.pnLinkPanel.add(this.lbColorLabel);
		
		gbcLinkPanel.gridx = 2;
		gbcLinkPanel.gridy = 4;
		gbcLinkPanel.gridwidth = 9;
		gbcLinkPanel.gridheight = 1;
		gbcLinkPanel.fill = GridBagConstraints.BOTH;
		gbcLinkPanel.weightx = 1;
		gbcLinkPanel.weighty = 0;
		gbcLinkPanel.anchor = GridBagConstraints.NORTH;
		gbLinkPanel.setConstraints(this.cmbColorCombo, gbcLinkPanel);
		this.pnLinkPanel.add(this.cmbColorCombo);
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 5;
		gbcPanel2.gridwidth = 12;
		gbcPanel2.gridheight = 6;
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.weightx = 0;
		gbcPanel2.weighty = 0;
		gbcPanel2.anchor = GridBagConstraints.NORTH;
		gbPanel2.setConstraints(this.pnLinkPanel, gbcPanel2);
		this.pnGeneralPanel.add(this.pnLinkPanel);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 12;
		gbcPanel0.gridheight = 11;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);
		
		this.cbLinkBox.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				SchemeCableLinkGeneralPanel.this.setLinkEnabled(SchemeCableLinkGeneralPanel.this.cbLinkBox.isSelected());
			}
		});
		
		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		this.taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		super.addToUndoableListener(this.tfNameText);
		super.addToUndoableListener(this.cmbTypeCombo);
		super.addToUndoableListener(this.tfOpticalText);
		super.addToUndoableListener(this.tfPhysicalText);
		super.addToUndoableListener(this.cbLinkBox);
		super.addToUndoableListener(this.tfInvNumberText);
		super.addToUndoableListener(this.tfSupplierText);
		super.addToUndoableListener(this.tfSupplierCodeText);
		super.addToUndoableListener(this.tfMarkText);
		super.addToUndoableListener(this.cmbColorCombo);
		super.addToUndoableListener(this.taDescrArea);
		
		this.btCommitBut.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.btCommitBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.btCommitBut.setFocusPainted(false);
		this.btCommitBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.btCommitBut.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SchemeCableLinkGeneralPanel.this.commitChanges();
			}
		});
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isEditionAllowed();
	}

	public void setObject(final Object or) {
		this.btCommitBut.setEnabled(isEditable());
		
		this.schemeCableLink = (SchemeCableLink) or;

		this.cmbTypeCombo.removeAllItems();
		if (this.schemeCableLink != null) {
			final EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
			try {
				final Set<CableLinkType> cableLinkTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				this.cmbTypeCombo.addElements(cableLinkTypes);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			this.cbLinkBox.setVisible(this.schemeCableLink.getParentScheme() != null);
		}

		CableLink link = null;
		this.setLinkEnabled(false);

		if (this.schemeCableLink != null) {
			this.tfNameText.setText(this.schemeCableLink.getName());
			this.taDescrArea.setText(this.schemeCableLink.getDescription());
			this.tfOpticalText.setValue(Double.valueOf(this.schemeCableLink.getOpticalLength()));
			this.tfPhysicalText.setValue(Double.valueOf(this.schemeCableLink.getPhysicalLength()));
			this.cmbTypeCombo.setSelectedItem(this.schemeCableLink.getAbstractLinkType());
			link = this.schemeCableLink.getAbstractLink();
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescrArea.setText(SchemeResourceKeys.EMPTY);
			this.tfOpticalText.setText(SchemeResourceKeys.EMPTY);
			this.tfPhysicalText.setText(SchemeResourceKeys.EMPTY);
		}
		if (link != null) {
			this.cbLinkBox.setSelected(true);
			this.setLinkEnabled(true);
			this.tfInvNumberText.setText(link.getInventoryNo());
			this.tfSupplierText.setText(link.getSupplier());
			this.tfSupplierCodeText.setText(link.getSupplierCode());
			this.tfMarkText.setText(link.getMark());
			final Color color = new Color(link.getColor());
			this.cmbColorCombo.addItem(color);
		} else {
			this.cbLinkBox.setSelected(false);
			this.setLinkEnabled(false);
			this.tfInvNumberText.setText(SchemeResourceKeys.EMPTY);
			this.tfSupplierText.setText(SchemeResourceKeys.EMPTY);
			this.tfSupplierCodeText.setText(SchemeResourceKeys.EMPTY);
			this.tfMarkText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public Object getObject() {
		return this.schemeCableLink;
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.schemeCableLink != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemeCableLink.setName(this.tfNameText.getText());
			this.schemeCableLink.setDescription(this.taDescrArea.getText());
			try {
				final CableLinkType newType = (CableLinkType) this.cmbTypeCombo.getSelectedItem();
				if (this.schemeCableLink.getAbstractLinkType() == null
						|| (newType != null && !newType.equals(this.schemeCableLink.getAbstractLinkType()))
						|| (newType != null && newType.getCableThreadTypes(false).size() != this.schemeCableLink.getSchemeCableThreads().size())) {
					
					LinkedIdsCondition condition = new LinkedIdsCondition(this.schemeCableLink.getId(), ObjectEntities.PATHELEMENT_CODE);
					Set<PathElement> pes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					Map<PathElement, Integer> peThreads = Collections.emptyMap();
					if (!pes.isEmpty()) {
						List<SchemeCableThread> sortedCableThreads = ClientUtils.getSortedCableThreads(this.schemeCableLink);
						peThreads = new HashMap<PathElement, Integer>(sortedCableThreads.size());
						for (PathElement pe : pes) {
							SchemeCableThread sct = pe.getSchemeCableThread();
							peThreads.put(pe, Integer.valueOf(sortedCableThreads.indexOf(sct)));
						}
					}
										
					this.schemeCableLink.setAbstractLinkTypeExt(newType, LoginManager.getUserId());
					
					if (!peThreads.isEmpty()) {
						List<SchemeCableThread> sortedCableThreads2 = ClientUtils.getSortedCableThreads(this.schemeCableLink);
						for (PathElement pe : peThreads.keySet()) {
							int i = peThreads.get(pe).intValue();
							if (i < sortedCableThreads2.size()) {
								SchemeCableThread sct2 = sortedCableThreads2.get(i);
								pe.setSchemeCableThread(sct2);
							} else {
								pe.setParentPathOwner(null, true);
							}
						}
					}
					
					SchemeCablePort sourcePort = this.schemeCableLink.getSourceAbstractSchemePort();
					if (sourcePort != null) {
						SchemeActions.performAutoCommutation(sourcePort, this.schemeCableLink, true);
					}
					SchemeCablePort targetPort = this.schemeCableLink.getTargetAbstractSchemePort();
					if (targetPort != null) {
						SchemeActions.performAutoCommutation(targetPort, this.schemeCableLink, false);
					}
				}
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
			}
			this.schemeCableLink.setOpticalLength(((Double) this.tfOpticalText.getValue()).doubleValue());
			this.schemeCableLink.setPhysicalLength(((Double) this.tfPhysicalText.getValue()).doubleValue());

			CableLink link = this.schemeCableLink.getAbstractLink();
			if (this.cbLinkBox.isSelected()) {
				if (link == null) {
					try {
						link = SchemeObjectsFactory.createCableLink(this.schemeCableLink);
					} catch (CreateObjectException e) {
						Log.errorMessage(e);
					}
				}
				if (link != null) {
					link.setName(this.schemeCableLink.getName());
					link.setDescription(this.schemeCableLink.getDescription());
					link.setType(this.schemeCableLink.getAbstractLinkType());

					// TODO add link.setInventoryNo()
					// link.setInventoryNo(invNumberText.getText());
					link.setSupplier(this.tfSupplierText.getText());
					link.setSupplierCode(this.tfSupplierCodeText.getText());
					link.setMark(this.tfMarkText.getText());
					link.setColor(((Color) this.cmbColorCombo.getSelectedItem()).getRGB());
				}
			} else if (link != null) {
				this.schemeCableLink.setAbstractLink(null);
				StorableObjectPool.delete(link.getId());
			}
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this,
					this.schemeCableLink.getId(),
					SchemeEvent.UPDATE_OBJECT));
		}
	}

	void setLinkEnabled(final boolean b) {
		this.pnLinkPanel.setVisible(b);
	}
}
