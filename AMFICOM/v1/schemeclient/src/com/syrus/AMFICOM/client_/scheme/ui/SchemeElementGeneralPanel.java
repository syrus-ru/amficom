/*-
 * $Id: SchemeElementGeneralPanel.java,v 1.33 2006/05/24 06:32:01 stas Exp $
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.NameableListCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.ProtoEquipmentWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.KISWrapper;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.33 $, $Date: 2006/05/24 06:32:01 $
 * @module schemeclient
 */

public class SchemeElementGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeElement schemeElement;
	private Identifier imageId;

	JPanel pnPanel0 = new JPanel();
	JPanel pnPanel3n = new JPanel();
	JPanel pnEquipmentPanel = new JPanel();
	JPanel pnPanel6 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton btCommitBut = new JButton();
	JLabel lbSymbolLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField tfSymbolText = new JTextField();
	JButton btSymbolBut = new JButton();
	JLabel lbCodenameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.CODENAME));
	AComboBox eqtCombo;
	JLabel lbTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox<ProtoEquipment> cmbTypeCombo = new WrapperedComboBox<ProtoEquipment>(
			ProtoEquipmentWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel lbManufacturerLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
	JTextField tfManufacturerText = new JTextField();
	JLabel lbManufacturerCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
	JTextField tfManufacturerCodeText = new JTextField();
	JCheckBox cbInstanceBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.INSTANCE));
	JLabel lbSupplierLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER));
	JTextField tfSupplierText = new JTextField();
	JLabel lbSupplierCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER_CODE));
	JTextField tfSupplierCodeText = new JTextField();
	JLabel lbHwLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.HARDWARE));
	JLabel lbHwsnLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
	JTextField tfHwsnText = new JTextField();
	JLabel lbHwvLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.VERSION));
	JTextField tfHwvText = new JTextField();
	JLabel lbSwLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SOFTWARE));
	JLabel lbSwsnLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
	JTextField tfSwsnText = new JTextField();
	JLabel lbSwvLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.VERSION));
	JTextField tfSwvText = new JTextField();
	JLabel lbLongLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LONGITUDE));
	JTextField tfLongText = new JTextField();
	JLabel lbLatLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LATITUDE));
	JTextField tfLatText = new JTextField();
	JCheckBox cbKisBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.KIS));
	JLabel lbKisLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.KIS));
	WrapperedComboBox<KIS> cmbKisCombo = new WrapperedComboBox<KIS>(KISWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel lbKisAddrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.ADDRESS));
	JTextField tfKisAddrText = new JTextField();
	JLabel lbKisPortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PORT));
	NumberFormatter nf = new NumberFormatter(NumberFormat.getIntegerInstance());
	JFormattedTextField tfKisPortText = new JFormattedTextField(this.nf);
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2, 10);

	protected SchemeElementGeneralPanel() {
		super();

		try {
			this.eqtCombo = new AComboBox(EquipmentType.valuesArray());
			this.eqtCombo.removeItem(EquipmentType.valueOf("bug136"));
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}

		this.nf.setValueClass(Integer.class);
		this.nf.setMinimum(new Integer(0));
		this.nf.setCommitsOnValidEdit(true);
		
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);
		
		this.lbDescrLabel.setFocusable(false);
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
		
		JScrollPane scpDescrArea = new JScrollPane(this.taDescrArea);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 12;
		gbcPanel0.gridwidth = 12;
		gbcPanel0.gridheight = 3;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(scpDescrArea, gbcPanel0);
		this.pnPanel0.add(scpDescrArea);
		
		final GridBagLayout gbPanel3n = new GridBagLayout();
		final GridBagConstraints gbcPanel3n = new GridBagConstraints();
		this.pnPanel3n.setLayout(gbPanel3n);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 0;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.lbNameLabel, gbcPanel3n);
		this.pnPanel3n.add(this.lbNameLabel);
		
		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 0;
		gbcPanel3n.gridwidth = 6;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.tfNameText, gbcPanel3n);
		this.pnPanel3n.add(this.tfNameText);
		
		gbcPanel3n.gridx = 8;
		gbcPanel3n.gridy = 0;
		gbcPanel3n.gridwidth = 1;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.btCommitBut, gbcPanel3n);
		this.pnPanel3n.add(this.btCommitBut);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 1;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.lbSymbolLabel, gbcPanel3n);
		this.pnPanel3n.add(this.lbSymbolLabel);
		
		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 1;
		gbcPanel3n.gridwidth = 6;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.tfSymbolText, gbcPanel3n);
		this.pnPanel3n.add(this.tfSymbolText);
		
		gbcPanel3n.gridx = 8;
		gbcPanel3n.gridy = 1;
		gbcPanel3n.gridwidth = 1;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.btSymbolBut, gbcPanel3n);
		this.pnPanel3n.add(this.btSymbolBut);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 2;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.lbCodenameLabel, gbcPanel3n);
		this.pnPanel3n.add(this.lbCodenameLabel);
		
		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 2;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.eqtCombo, gbcPanel3n);
		this.pnPanel3n.add(this.eqtCombo);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 3;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.lbTypeLabel, gbcPanel3n);
		this.pnPanel3n.add(this.lbTypeLabel);
		
		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 3;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.cmbTypeCombo, gbcPanel3n);
		this.pnPanel3n.add(this.cmbTypeCombo);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 4;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.lbManufacturerLabel, gbcPanel3n);
		this.pnPanel3n.add(this.lbManufacturerLabel);
		
		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 4;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.tfManufacturerText, gbcPanel3n);
		this.pnPanel3n.add(this.tfManufacturerText);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 5;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.lbManufacturerCodeLabel, gbcPanel3n);
		this.pnPanel3n.add(this.lbManufacturerCodeLabel);
		
		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 5;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.tfManufacturerCodeText, gbcPanel3n);
		this.pnPanel3n.add(this.tfManufacturerCodeText);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 6;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.cbInstanceBox, gbcPanel3n);
		this.pnPanel3n.add(this.cbInstanceBox);
		
		this.pnEquipmentPanel.setBorder(BorderFactory.createTitledBorder(""));
		final GridBagLayout gbEquipmentPanel = new GridBagLayout();
		final GridBagConstraints gbcEquipmentPanel = new GridBagConstraints();
		this.pnEquipmentPanel.setLayout(gbEquipmentPanel);
		
		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 0;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbSupplierLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbSupplierLabel);
		
		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 0;
		gbcEquipmentPanel.gridwidth = 6;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfSupplierText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfSupplierText);
		
		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 1;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbSupplierCodeLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbSupplierCodeLabel);
		
		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 1;
		gbcEquipmentPanel.gridwidth = 6;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfSupplierCodeText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfSupplierCodeText);
		
		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 3;
		gbcEquipmentPanel.gridwidth = 8;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbHwLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbHwLabel);
		
		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbHwsnLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbHwsnLabel);
		
		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfHwsnText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfHwsnText);
		
		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbLongLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbLongLabel);
		
		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfLongText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfLongText);
		
		gbcEquipmentPanel.gridx = 4;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbLatLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbLatLabel);
		
		gbcEquipmentPanel.gridx = 6;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfLatText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfLatText);
		
		gbcEquipmentPanel.gridx = 4;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbHwvLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbHwvLabel);
		
		gbcEquipmentPanel.gridx = 6;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfHwvText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfHwvText);
		
		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 5;
		gbcEquipmentPanel.gridwidth = 8;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbSwLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbSwLabel);
		
		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbSwsnLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbSwsnLabel);
		
		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfSwsnText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfSwsnText);
		
		gbcEquipmentPanel.gridx = 4;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.lbSwvLabel, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.lbSwvLabel);
		
		gbcEquipmentPanel.gridx = 6;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints(this.tfSwvText, gbcEquipmentPanel);
		this.pnEquipmentPanel.add(this.tfSwvText);
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 7;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 8;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.pnEquipmentPanel, gbcPanel3n);
		this.pnPanel3n.add(this.pnEquipmentPanel);
		
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 15;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.cbKisBox, gbcPanel3n);
		this.pnPanel3n.add(this.cbKisBox);
		
		this.pnPanel6.setBorder(BorderFactory.createTitledBorder(""));
		GridBagLayout gbPanel6 = new GridBagLayout();
		GridBagConstraints gbcPanel6 = new GridBagConstraints();
		this.pnPanel6.setLayout(gbPanel6);
		
		gbcPanel6.gridx = 0;
		gbcPanel6.gridy = 0;
		gbcPanel6.gridwidth = 2;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints(this.lbKisLabel, gbcPanel6);
		this.pnPanel6.add(this.lbKisLabel);
		
		gbcPanel6.gridx = 2;
		gbcPanel6.gridy = 0;
		gbcPanel6.gridwidth = 7;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 1;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints(this.cmbKisCombo, gbcPanel6);
		this.pnPanel6.add(this.cmbKisCombo);
		
		gbcPanel6.gridx = 0;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 2;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints(this.lbKisAddrLabel, gbcPanel6);
		this.pnPanel6.add(this.lbKisAddrLabel);
		
		gbcPanel6.gridx = 2;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 5;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 1;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints(this.tfKisAddrText, gbcPanel6);
		this.pnPanel6.add(this.tfKisAddrText);
		
		gbcPanel6.gridx = 7;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 1;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints(this.lbKisPortLabel, gbcPanel6);
		this.pnPanel6.add(this.lbKisPortLabel);
		
		gbcPanel6.gridx = 8;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 1;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0.2;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints(this.tfKisPortText, gbcPanel6);
		this.pnPanel6.add(this.tfKisPortText);
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 16;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 4;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints(this.pnPanel6, gbcPanel3n);
		this.pnPanel3n.add(this.pnPanel6);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 12;
		gbcPanel0.gridheight = 11;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnPanel3n, gbcPanel0);
		this.pnPanel0.add(this.pnPanel3n);
		
		this.tfManufacturerText.setEnabled(false);
		this.tfManufacturerCodeText.setEnabled(false);
		
		this.eqtCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				SchemeElementGeneralPanel.this.eqtCombo_stateChanged((EquipmentType)e.getItem());
			}
		});
		this.cmbTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				SchemeElementGeneralPanel.this.typeCombo_stateChanged((ProtoEquipment) e.getItem());
			}
		});
		this.cbInstanceBox.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				SchemeElementGeneralPanel.this.instanceBox_stateChanged();
			}
		});
		this.cbKisBox.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				SchemeElementGeneralPanel.this.kisBox_stateChanged();
			}
		});
		this.cmbKisCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				SchemeElementGeneralPanel.this.kisCombo_stateChanged((KIS) e.getItem());
			}
		});
		this.taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		super.addToUndoableListener(this.tfNameText);
		super.addToUndoableListener(this.btSymbolBut);
		super.addToUndoableListener(this.eqtCombo);
		super.addToUndoableListener(this.cmbTypeCombo);
		super.addToUndoableListener(this.tfManufacturerText);
		super.addToUndoableListener(this.tfManufacturerCodeText);
		super.addToUndoableListener(this.cbInstanceBox);
		super.addToUndoableListener(this.tfSupplierText);
		super.addToUndoableListener(this.tfSupplierCodeText);
		super.addToUndoableListener(this.tfHwsnText);
		super.addToUndoableListener(this.tfHwvText);
		super.addToUndoableListener(this.tfSwsnText);
		super.addToUndoableListener(this.tfSwvText);
		super.addToUndoableListener(this.tfLongText);
		super.addToUndoableListener(this.tfLatText);
		super.addToUndoableListener(this.cbKisBox);
		super.addToUndoableListener(this.cmbKisCombo);
		super.addToUndoableListener(this.tfKisAddrText);
		super.addToUndoableListener(this.tfKisPortText);
		super.addToUndoableListener(this.taDescrArea);
		
		this.eqtCombo.setRenderer(new NameableListCellRenderer());
		
		this.btCommitBut.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.btCommitBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.btCommitBut.setFocusPainted(false);
		this.btCommitBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.btCommitBut.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SchemeElementGeneralPanel.this.commitChanges();
			}
		});
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	protected SchemeElementGeneralPanel(final SchemeElement schemeElement) {
		this();
		this.setObject(schemeElement);
	}

	void kisCombo_stateChanged(final KIS kis) {
		this.tfKisAddrText.setText(kis.getHostName());
		this.tfKisPortText.setValue(Short.valueOf(kis.getTCPPort()));
	}

	void eqtCombo_stateChanged(final EquipmentType eqt) {
		this.cmbTypeCombo.removeAllItems();

		final LinkedIdsCondition condition = new LinkedIdsCondition(eqt, ObjectEntities.PROTOEQUIPMENT_CODE);
		try {
			final Set<ProtoEquipment> protoEquipments = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			this.cmbTypeCombo.addElements(protoEquipments);
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
	}

	void typeCombo_stateChanged(final ProtoEquipment protoEq) {
		if (protoEq != null) {
			this.tfManufacturerText.setText(protoEq.getManufacturer());
			this.tfManufacturerCodeText.setText(protoEq.getManufacturerCode());
		} else {
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public void instanceBox_stateChanged() {
		this.setEquipmentEnabled(this.cbInstanceBox.isSelected());
		if (!this.cbInstanceBox.isSelected()) {
			this.cbKisBox.setSelected(false);
			this.kisBox_stateChanged();
		}
	}
	
	public void kisBox_stateChanged() {
		this.setKISEnabled(this.cbKisBox.isSelected());
	}
	
	void setEquipmentEnabled(final boolean b) {
		this.pnEquipmentPanel.setVisible(b);
		this.cbKisBox.setVisible(b);
	}
	
	void setKISEnabled(final boolean b) {
		this.pnPanel6.setVisible(b);
	}

	public JComponent getGUI() {
		return this.pnPanel0; 
	}

	public Object getObject() {
		return this.schemeElement;
	}

	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isEditionAllowed();
	}

	public void setObject(final Object or) {
		this.btCommitBut.setEnabled(isEditable());
		
		this.schemeElement = (SchemeElement) or;
		ProtoEquipment protoEq = null;
		Equipment eq = null;
		KIS kis = null;
		Icon symbol = null;

		this.cmbTypeCombo.removeAllItems();
		this.cmbKisCombo.removeAllItems();

		if (this.schemeElement != null) {
			this.tfNameText.setText(this.schemeElement.getName());
			this.taDescrArea.setText(this.schemeElement.getDescription());
			this.tfSymbolText.setText(this.schemeElement.getLabel());
			BitmapImageResource s = this.schemeElement.getSymbol();
			if (s != null) {
				symbol = new ImageIcon(s.getImage());
			}
			
			if (this.schemeElement.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
				this.cbInstanceBox.setVisible(false);
			} else {
				this.cbInstanceBox.setVisible(true);
				try {
					protoEq = this.schemeElement.getProtoEquipment();
				} catch (IllegalStateException e) {
					Log.debugMessage("No EqT set for SE '" + this.schemeElement.getId() + "'", Level.FINE);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				
				try {
					eq = this.schemeElement.getEquipment();
				} catch (IllegalStateException e) {
					// ignore as it means no Equipment created
				} 
				
				try {
					kis = this.schemeElement.getKis();
				} catch (IllegalStateException e) {
					// ignore as it means no KIS created
				}
				
//				if (protoEq != null) {
//					this.eqtCombo.setSelectedItem(protoEq.getType());
//				} else {
//					eqtCombo_stateChanged((EquipmentType)this.eqtCombo.getSelectedItem());
//				}
								
				final EquivalentCondition condition1 = new EquivalentCondition(ObjectEntities.KIS_CODE);
				try {
					final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
					this.cmbKisCombo.addElements(kiss);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		} 
		else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescrArea.setText(SchemeResourceKeys.EMPTY);
			this.tfSymbolText.setText(SchemeResourceKeys.EMPTY);
		}
		
		if (protoEq != null) {
			this.eqtCombo.setEnabled(true);
			this.cmbTypeCombo.setEnabled(true);
			try {
				final EquipmentType equipmentType = protoEq.getType();
				this.eqtCombo.setSelectedItem(equipmentType);
				this.eqtCombo_stateChanged(equipmentType);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
			this.cmbTypeCombo.setSelectedItem(protoEq);
			this.typeCombo_stateChanged(protoEq);
		} else {
			this.eqtCombo.setEnabled(false);
			this.cmbTypeCombo.setEnabled(false);
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
		if (kis != null) {
			this.cbKisBox.setSelected(true);
			this.cmbKisCombo.setSelectedItem(kis);
			this.tfKisAddrText.setText(kis.getHostName());
			this.tfKisPortText.setValue(Short.valueOf(kis.getTCPPort()));
		} else {
			this.cbKisBox.setSelected(false);
			this.tfKisAddrText.setText(SchemeResourceKeys.EMPTY);
			this.tfKisPortText.setValue(new Short((short)0));
		}
		if (eq != null) {
			this.cbInstanceBox.setSelected(true);
			this.tfSupplierText.setText(eq.getSupplier());
			this.tfSupplierCodeText.setText(eq.getSupplierCode());
			this.tfHwsnText.setText(eq.getHwSerial());
			this.tfHwvText.setText(eq.getHwVersion());
			this.tfSwsnText.setText(eq.getSwSerial());
			this.tfSwvText.setText(eq.getSwVersion());
			this.tfLongText.setText(Float.toString(eq.getLongitude()));
			this.tfLatText.setText(Float.toString(eq.getLatitude()));
		} else {
			this.cbInstanceBox.setSelected(false);
			this.tfSupplierText.setText(SchemeResourceKeys.EMPTY);
			this.tfSupplierCodeText.setText(SchemeResourceKeys.EMPTY);
			this.tfHwsnText.setText(SchemeResourceKeys.EMPTY);
			this.tfHwvText.setText(SchemeResourceKeys.EMPTY);
			this.tfSwsnText.setText(SchemeResourceKeys.EMPTY);
			this.tfSwvText.setText(SchemeResourceKeys.EMPTY);
			this.tfLongText.setText(SchemeResourceKeys.EMPTY);
			this.tfLatText.setText(SchemeResourceKeys.EMPTY);
		}
		this.btSymbolBut.setIcon(symbol);
		this.instanceBox_stateChanged();
		this.kisBox_stateChanged();
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.schemeElement != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemeElement.setName(this.tfNameText.getText());
			this.schemeElement.setDescription(this.taDescrArea.getText());
			this.schemeElement.setLabel(this.tfSymbolText.getText());
			if (this.btSymbolBut.getIcon() == null) {
				this.schemeElement.setSymbol(null);
			} else {
				try {
					this.schemeElement.setSymbol((BitmapImageResource) StorableObjectPool.getStorableObject(this.imageId, true));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
			final ProtoEquipment protoEq = (ProtoEquipment)this.cmbTypeCombo.getSelectedItem();
			if (protoEq != null) {
				this.schemeElement.setProtoEquipment(protoEq);
//				eqt.setManufacturer(this.tfManufacturerText.getText());
//				eqt.setManufacturerCode(this.tfManufacturerCodeText.getText());
			}
			Equipment eq = this.schemeElement.getEquipment();
			if (this.cbInstanceBox.isSelected()) {
				if (eq == null) {
					try {
						eq = SchemeObjectsFactory.createEquipment(this.schemeElement);
					} catch (CreateObjectException e) {
						Log.errorMessage(e);
					}
				}
				if (eq != null) {
					eq.setName(this.schemeElement.getName());
					eq.setDescription(this.schemeElement.getDescription());
					eq.setSupplier(this.tfSupplierText.getText());
					eq.setSupplierCode(this.tfSupplierCodeText.getText());
					eq.setHwSerial(this.tfHwsnText.getText());
					eq.setHwVersion(this.tfHwvText.getText());
					eq.setSwSerial(this.tfSwsnText.getText());
					eq.setSwVersion(this.tfSwvText.getText());
					try {
						eq.setLongitude(Float.parseFloat(this.tfLongText.getText()));
					} catch (NumberFormatException e) {
						eq.setLongitude(0);
					}
					try {
						eq.setLatitude(Float.parseFloat(this.tfLatText.getText()));
					} catch (NumberFormatException e1) {
						eq.setLatitude(0);
					}
				}
			} else if (eq != null) {
				this.schemeElement.setEquipment(null);
				StorableObjectPool.delete(eq.getId());
			}

			if (this.cbKisBox.isSelected()) {
				final KIS kis = (KIS) this.cmbKisCombo.getSelectedItem();
				if (kis != null) {
					this.schemeElement.setKis(kis);
					// kis.setName(schemeElement.getName());
					// kis.setDescription(schemeElement.getDescription());
					kis.setHostName(this.tfKisAddrText.getText());
					kis.setTCPPort(((Short) this.tfKisPortText.getValue()).shortValue());
				}
			} else {
				this.schemeElement.setKis(null);
			}
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this,
					this.schemeElement.getId(),
					SchemeEvent.UPDATE_OBJECT));
		}
	}
}
