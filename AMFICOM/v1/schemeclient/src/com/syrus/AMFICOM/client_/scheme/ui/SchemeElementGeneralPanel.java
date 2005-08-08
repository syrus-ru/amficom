/*-
 * $Id: SchemeElementGeneralPanel.java,v 1.13 2005/08/08 11:58:07 arseniy Exp $
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
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeWrapper;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.EquipmentTypeCodenames;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2005/08/08 11:58:07 $
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
	JTextField tfLabelText = new JTextField();
	JButton btSymbolBut = new JButton();
	JLabel lbCodenameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.CODENAME));
	JComboBox codenameCombo = new AComboBox();
	JLabel lbTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox cmbTypeCombo = new WrapperedComboBox(EquipmentTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
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
	WrapperedComboBox cmbKisCombo = new WrapperedComboBox(KISWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel lbKisAddrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.ADDRESS));
	JTextField tfKisAddrText = new JTextField();
	JLabel lbKisPortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PORT));
	NumberFormatter nf = new NumberFormatter(NumberFormat.getIntegerInstance());
	JFormattedTextField tfKisPortText = new JFormattedTextField(this.nf);
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2,10);
	
	protected SchemeElementGeneralPanel() {
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

	protected SchemeElementGeneralPanel(SchemeElement schemeElement) {
		this();
		setObject(schemeElement);
	}

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		nf.setValueClass(Integer.class);
		nf.setMinimum(new Integer(0));
		nf.setCommitsOnValidEdit(true);
		
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

		lbDescrLabel.setFocusable( false );
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
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 12;
		gbcPanel0.gridwidth = 12;
		gbcPanel0.gridheight = 3;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescrArea, gbcPanel0 );
		pnPanel0.add( scpDescrArea );

		GridBagLayout gbPanel3n = new GridBagLayout();
		GridBagConstraints gbcPanel3n = new GridBagConstraints();
		pnPanel3n.setLayout( gbPanel3n );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 0;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( lbNameLabel, gbcPanel3n );
		pnPanel3n.add( lbNameLabel );

		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 0;
		gbcPanel3n.gridwidth = 6;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( tfNameText, gbcPanel3n );
		pnPanel3n.add( tfNameText );

		gbcPanel3n.gridx = 8;
		gbcPanel3n.gridy = 0;
		gbcPanel3n.gridwidth = 1;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( btCommitBut, gbcPanel3n );
		pnPanel3n.add( btCommitBut );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 1;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( lbSymbolLabel, gbcPanel3n );
		pnPanel3n.add( lbSymbolLabel );

		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 1;
		gbcPanel3n.gridwidth = 6;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( tfSymbolText, gbcPanel3n );
		pnPanel3n.add( tfSymbolText );

		gbcPanel3n.gridx = 8;
		gbcPanel3n.gridy = 1;
		gbcPanel3n.gridwidth = 1;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( btSymbolBut, gbcPanel3n );
		pnPanel3n.add( btSymbolBut );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 2;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( lbCodenameLabel, gbcPanel3n );
		pnPanel3n.add( lbCodenameLabel );

		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 2;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( codenameCombo, gbcPanel3n );
		pnPanel3n.add( codenameCombo );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 3;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( lbTypeLabel, gbcPanel3n );
		pnPanel3n.add( lbTypeLabel );

		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 3;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( cmbTypeCombo, gbcPanel3n );
		pnPanel3n.add( cmbTypeCombo );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 4;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( lbManufacturerLabel, gbcPanel3n );
		pnPanel3n.add( lbManufacturerLabel );

		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 4;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( tfManufacturerText, gbcPanel3n );
		pnPanel3n.add( tfManufacturerText );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 5;
		gbcPanel3n.gridwidth = 2;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 0;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( lbManufacturerCodeLabel, gbcPanel3n );
		pnPanel3n.add( lbManufacturerCodeLabel );

		gbcPanel3n.gridx = 2;
		gbcPanel3n.gridy = 5;
		gbcPanel3n.gridwidth = 7;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( tfManufacturerCodeText, gbcPanel3n );
		pnPanel3n.add( tfManufacturerCodeText );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 6;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( cbInstanceBox, gbcPanel3n );
		pnPanel3n.add( cbInstanceBox );

		pnEquipmentPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbEquipmentPanel = new GridBagLayout();
		GridBagConstraints gbcEquipmentPanel = new GridBagConstraints();
		pnEquipmentPanel.setLayout( gbEquipmentPanel );

		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 0;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbSupplierLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbSupplierLabel );

		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 0;
		gbcEquipmentPanel.gridwidth = 6;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfSupplierText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfSupplierText );

		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 1;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbSupplierCodeLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbSupplierCodeLabel );

		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 1;
		gbcEquipmentPanel.gridwidth = 6;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfSupplierCodeText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfSupplierCodeText );

		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 3;
		gbcEquipmentPanel.gridwidth = 8;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbHwLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbHwLabel );

		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbHwsnLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbHwsnLabel );

		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfHwsnText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfHwsnText );

		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbLongLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbLongLabel );

		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfLongText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfLongText );

		gbcEquipmentPanel.gridx = 4;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbLatLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbLatLabel );

		gbcEquipmentPanel.gridx = 6;
		gbcEquipmentPanel.gridy = 2;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfLatText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfLatText );

		gbcEquipmentPanel.gridx = 4;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbHwvLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbHwvLabel );

		gbcEquipmentPanel.gridx = 6;
		gbcEquipmentPanel.gridy = 4;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfHwvText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfHwvText );

		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 5;
		gbcEquipmentPanel.gridwidth = 8;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbSwLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbSwLabel );

		gbcEquipmentPanel.gridx = 0;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbSwsnLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbSwsnLabel );

		gbcEquipmentPanel.gridx = 2;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfSwsnText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfSwsnText );

		gbcEquipmentPanel.gridx = 4;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 0;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( lbSwvLabel, gbcEquipmentPanel );
		pnEquipmentPanel.add( lbSwvLabel );

		gbcEquipmentPanel.gridx = 6;
		gbcEquipmentPanel.gridy = 6;
		gbcEquipmentPanel.gridwidth = 2;
		gbcEquipmentPanel.gridheight = 1;
		gbcEquipmentPanel.fill = GridBagConstraints.BOTH;
		gbcEquipmentPanel.weightx = 1;
		gbcEquipmentPanel.weighty = 0;
		gbcEquipmentPanel.anchor = GridBagConstraints.NORTH;
		gbEquipmentPanel.setConstraints( tfSwvText, gbcEquipmentPanel );
		pnEquipmentPanel.add( tfSwvText );
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 7;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 8;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( pnEquipmentPanel, gbcPanel3n );
		pnPanel3n.add( pnEquipmentPanel );

		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 15;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 1;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( cbKisBox, gbcPanel3n );
		pnPanel3n.add( cbKisBox );

		pnPanel6.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbPanel6 = new GridBagLayout();
		GridBagConstraints gbcPanel6 = new GridBagConstraints();
		pnPanel6.setLayout( gbPanel6 );

		gbcPanel6.gridx = 0;
		gbcPanel6.gridy = 0;
		gbcPanel6.gridwidth = 2;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints( lbKisLabel, gbcPanel6 );
		pnPanel6.add( lbKisLabel );

		gbcPanel6.gridx = 2;
		gbcPanel6.gridy = 0;
		gbcPanel6.gridwidth = 7;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 1;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints( cmbKisCombo, gbcPanel6 );
		pnPanel6.add( cmbKisCombo );

		gbcPanel6.gridx = 0;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 2;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints( lbKisAddrLabel, gbcPanel6 );
		pnPanel6.add( lbKisAddrLabel );

		gbcPanel6.gridx = 2;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 5;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 1;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints( tfKisAddrText, gbcPanel6 );
		pnPanel6.add( tfKisAddrText );

		gbcPanel6.gridx = 7;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 1;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints( lbKisPortLabel, gbcPanel6 );
		pnPanel6.add( lbKisPortLabel );

		gbcPanel6.gridx = 8;
		gbcPanel6.gridy = 1;
		gbcPanel6.gridwidth = 1;
		gbcPanel6.gridheight = 1;
		gbcPanel6.fill = GridBagConstraints.BOTH;
		gbcPanel6.weightx = 0.2;
		gbcPanel6.weighty = 0;
		gbcPanel6.anchor = GridBagConstraints.NORTH;
		gbPanel6.setConstraints( tfKisPortText, gbcPanel6 );
		pnPanel6.add( tfKisPortText );
		gbcPanel3n.gridx = 0;
		gbcPanel3n.gridy = 16;
		gbcPanel3n.gridwidth = 9;
		gbcPanel3n.gridheight = 4;
		gbcPanel3n.fill = GridBagConstraints.BOTH;
		gbcPanel3n.weightx = 1;
		gbcPanel3n.weighty = 0;
		gbcPanel3n.anchor = GridBagConstraints.NORTH;
		gbPanel3n.setConstraints( pnPanel6, gbcPanel3n );
		pnPanel3n.add( pnPanel6 );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 12;
		gbcPanel0.gridheight = 11;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnPanel3n, gbcPanel0 );
		pnPanel0.add( pnPanel3n );
		
		tfManufacturerText.setEnabled(false);
		tfManufacturerCodeText.setEnabled(false);

		codenameCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				codeNameCombo_stateChanged((String)e.getItem());
			}
		});
		cmbTypeCombo.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				typeCombo_stateChanged((EquipmentType)e.getItem());
			}
		});
		cbInstanceBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				instanceBox_stateChanged();
			}
		});
		cbKisBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				kisBox_stateChanged();
			}
		});
		cmbKisCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				kisCombo_stateChanged((KIS)e.getItem());
			}
		});
		taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(tfLabelText);
		addToUndoableListener(btSymbolBut);
		addToUndoableListener(codenameCombo);
		addToUndoableListener(cmbTypeCombo);
		addToUndoableListener(tfManufacturerText);
		addToUndoableListener(tfManufacturerCodeText);
		addToUndoableListener(cbInstanceBox);
		addToUndoableListener(tfSupplierText);
		addToUndoableListener(tfSupplierCodeText);
		addToUndoableListener(tfHwsnText);
		addToUndoableListener(tfHwvText);
		addToUndoableListener(tfSwsnText);
		addToUndoableListener(tfSwvText);
		addToUndoableListener(tfLongText);
		addToUndoableListener(tfLatText);
		addToUndoableListener(cbKisBox);
		addToUndoableListener(cmbKisCombo);
		addToUndoableListener(tfKisAddrText);
		addToUndoableListener(tfKisPortText);
		addToUndoableListener(taDescrArea);
		
		for (int i = 0; i < EquipmentTypeCodenames.DEFAULT_CODENAMES.length; i++) {
			this.codenameCombo.addItem(EquipmentTypeCodenames.DEFAULT_CODENAMES[i]);			
		}
		this.codenameCombo.setRenderer(EquipmentTypeCodenames.getListCellRenderer());
		
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
	
	void kisCombo_stateChanged(KIS kis) {
		this.tfKisAddrText.setText(kis.getHostName());
		this.tfKisPortText.setValue(Short.valueOf(kis.getTCPPort()));
	}

	void codeNameCombo_stateChanged(String eqtCodename) {
		this.cmbTypeCombo.removeAllItems();
		TypicalCondition condition = new TypicalCondition(eqtCodename, OperationSort.OPERATION_EQUALS, ObjectEntities.EQUIPMENT_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
		try {
			this.cmbTypeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
		} catch (ApplicationException e1) {
			Log.errorException(e1);
		}
	}

	void typeCombo_stateChanged(EquipmentType eqt) {
		if (eqt != null) {
			this.tfManufacturerText.setText(eqt.getManufacturer());
			this.tfManufacturerCodeText.setText(eqt.getManufacturerCode());
		} else {
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
	}
	
	public void instanceBox_stateChanged() {
		setEquipmentEnabled(this.cbInstanceBox.isSelected());
		if (!this.cbInstanceBox.isSelected()) {
			this.cbKisBox.setSelected(false);
			kisBox_stateChanged();
		}
	}
	
	public void kisBox_stateChanged() {
		setKISEnabled(this.cbKisBox.isSelected());
	}
	
	void setEquipmentEnabled(boolean b) {
		this.pnEquipmentPanel.setVisible(b);
		this.cbKisBox.setVisible(b);
	}
	
	void setKISEnabled(boolean b) {
		this.pnPanel6.setVisible(b);
	}

	public JComponent getGUI() {
		return this.pnPanel0; 
	}

	public Object getObject() {
		return this.schemeElement;
	}

	public void setObject(Object or) {
		this.schemeElement = (SchemeElement) or;
		EquipmentType eqt = null;
		Equipment eq = null;
		KIS kis = null;
		Icon symbol = null;
		
		this.cmbTypeCombo.removeAllItems();
		this.cmbKisCombo.removeAllItems();

		if (this.schemeElement != null) {
			this.tfNameText.setText(this.schemeElement.getName());
			this.taDescrArea.setText(this.schemeElement.getDescription());
			this.tfLabelText.setText(this.schemeElement.getLabel());
			if (this.schemeElement.getSymbol() != null)
				symbol = new ImageIcon(this.schemeElement.getSymbol().getImage());
			
			if (this.schemeElement.getScheme() != null) {
				this.cbInstanceBox.setVisible(false);
			} else {
				this.cbInstanceBox.setVisible(true);
				try {
					eqt = this.schemeElement.getEquipmentType();
				} catch (IllegalStateException e) {
					Log.debugMessage("No EqT set for SE '" + this.schemeElement.getId() + "'", Level.FINE);
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
				
				EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
				try {
					this.cmbTypeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
				condition = new EquivalentCondition(ObjectEntities.KIS_CODE);
				try {
					this.cmbKisCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
		} 
		else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescrArea.setText(SchemeResourceKeys.EMPTY);
			this.tfLabelText.setText(SchemeResourceKeys.EMPTY);
		}
		
		if (eqt != null) {
			this.codenameCombo.setEnabled(true);
			this.cmbTypeCombo.setEnabled(true);
			this.codenameCombo.setSelectedItem(eqt.getCodename());
			codeNameCombo_stateChanged(eqt.getCodename());
			this.cmbTypeCombo.setSelectedItem(eqt);
			typeCombo_stateChanged(eqt);
		} else {
			this.codenameCombo.setEnabled(false);
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
		instanceBox_stateChanged();
		kisBox_stateChanged();
	}

	public void commitChanges() {
		if (this.schemeElement != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemeElement.setName(this.tfNameText.getText());
			this.schemeElement.setDescription(this.taDescrArea.getText());
			this.schemeElement.setLabel(this.tfLabelText.getText());
			if (this.btSymbolBut.getIcon() == null) {
				this.schemeElement.setSymbol(null);
			} else {
				try {
					this.schemeElement.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(this.imageId, true));
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
			EquipmentType eqt = (EquipmentType)this.cmbTypeCombo.getSelectedItem();
			if (eqt != null) {
				this.schemeElement.setEquipmentType(eqt);
				eqt.setManufacturer(this.tfManufacturerText.getText());
				eqt.setManufacturerCode(this.tfManufacturerCodeText.getText());
			}
			Equipment eq = this.schemeElement.getEquipment();
			if (this.cbInstanceBox.isSelected()) {
				if (eq == null) {
					try {
						eq = SchemeObjectsFactory.createEquipment(this.schemeElement);
					} catch (CreateObjectException e) {
						Log.errorException(e);
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
				StorableObjectPool.delete(eq.getId());
				this.schemeElement.setEquipment(null);
			}

			if (this.cbKisBox.isSelected()) {
				KIS kis = (KIS)this.cmbKisCombo.getSelectedItem();
				if (kis != null) {
					this.schemeElement.setKis(kis);
//					kis.setName(schemeElement.getName());
//					kis.setDescription(schemeElement.getDescription());
					kis.setHostName(this.tfKisAddrText.getText());
					kis.setTCPPort(((Short)this.tfKisPortText.getValue()).shortValue());
				}
			} else {
				this.schemeElement.setKis(null);
			}
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemeElement, SchemeEvent.UPDATE_OBJECT));
		}
	}
}
