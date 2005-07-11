/*
 * $Id: CableLinkTypeGeneralPanel.java,v 1.5 2005/07/11 12:31:37 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.CableThreadTypeWrapper;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.LinkTypeWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.EquipmentTypeCodenames;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/07/11 12:31:37 $
 * @module schemeclient_v1
 */

public class CableLinkTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected CableLinkType linkType;
	protected List sortedTheradTypes;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbManufacturerLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
	JTextField tfManufacturerText = new JTextField();
	JLabel lbManufacturerCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
	JTextField tfManufacturerCodeText = new JTextField();
	JLabel lbThreadNumLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.THREAD_NUMBER));
	JSpinner tfThreadNumText = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JPanel pnGeneralPanel = new JPanel();
	
	JLabel lbTNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.THREAD));
	WrapperedComboBox cmbTNameCombo = new WrapperedComboBox(CableThreadTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_CODENAME, StorableObjectWrapper.COLUMN_ID);
	JLabel lbTMarkLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MARK));
	JTextField tfTMarkText = new JTextField();
	JLabel lbTTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox cmbTTypeCombo = new WrapperedComboBox(LinkTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JLabel lbTColorLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.COLOR));
	JComboBox cmbTColorCombo = new ColorChooserComboBox();
	JLabel lbTDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taTDescrArea = new JTextArea(2, 10);
	JPanel pnThreadPanel = new JPanel();	
	
	Map<CableThreadType, ThreadTypeFields> cashedFields;
	ThreadTypeFields currentCash;
	
	private class ThreadTypeFields {
		private ThreadTypeFields(String mark, String description, LinkType type, int color) {
			this.mark = mark;
			this.type = type;
			this.color = color;
			this.description = description;
		}
		String mark;
		String description;
		LinkType type;
		int color;
	}
	
	protected CableLinkTypeGeneralPanel() {
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

	protected CableLinkTypeGeneralPanel(CableLinkType linkType) {
		this();
		setObject(linkType);
	}

	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout(gbPanel0);

		GridBagLayout gbGeneralPanel = new GridBagLayout();
		GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		pnGeneralPanel.setLayout(gbGeneralPanel);

		lbNameLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(lbNameLabel, gbcGeneralPanel);
		pnGeneralPanel.add(lbNameLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 3;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(tfNameText, gbcGeneralPanel);
		pnGeneralPanel.add(tfNameText);
		
		gbcGeneralPanel.gridx = 5;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(commitButton, gbcGeneralPanel);
		pnGeneralPanel.add(commitButton);

		lbManufacturerLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(lbManufacturerLabel, gbcGeneralPanel);
		pnGeneralPanel.add(lbManufacturerLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(tfManufacturerText, gbcGeneralPanel);
		pnGeneralPanel.add(tfManufacturerText);

		lbManufacturerCodeLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(lbManufacturerCodeLabel, gbcGeneralPanel);
		pnGeneralPanel.add(lbManufacturerCodeLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(tfManufacturerCodeText, gbcGeneralPanel);
		pnGeneralPanel.add(tfManufacturerCodeText);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 6;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbThreadNumLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbThreadNumLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 6;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfThreadNumText, gbcGeneralPanel );
		pnGeneralPanel.add( tfThreadNumText );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 6;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );

		lbDescriptionLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 6;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,2,0,2 );
		gbPanel0.setConstraints( lbDescriptionLabel, gbcPanel0 );
		pnPanel0.add( lbDescriptionLabel );

		JScrollPane scpDescriptionArea = new JScrollPane( taDescriptionArea );
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 7;
		gbcPanel0.gridwidth = 5;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,2,0,2 );
		gbPanel0.setConstraints( scpDescriptionArea, gbcPanel0 );
		pnPanel0.add( scpDescriptionArea );

		pnThreadPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
		GridBagLayout gbThreadPanel = new GridBagLayout();
		GridBagConstraints gbcThreadPanel = new GridBagConstraints();
		pnThreadPanel.setLayout( gbThreadPanel );

		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 0;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.insets = new Insets( 0,0,0,2 );
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( lbTNameLabel, gbcThreadPanel );
		pnThreadPanel.add( lbTNameLabel );
		
		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 0;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( cmbTNameCombo, gbcThreadPanel );
		pnThreadPanel.add( cmbTNameCombo );

		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 1;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( lbTTypeLabel, gbcThreadPanel );
		pnThreadPanel.add( lbTTypeLabel );

		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 1;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( cmbTTypeCombo, gbcThreadPanel );
		pnThreadPanel.add( cmbTTypeCombo );

		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 2;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( lbTColorLabel, gbcThreadPanel );
		pnThreadPanel.add( lbTColorLabel );

		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 2;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( cmbTColorCombo, gbcThreadPanel );
		pnThreadPanel.add( cmbTColorCombo );

		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 3;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( lbTMarkLabel, gbcThreadPanel );
		pnThreadPanel.add( lbTMarkLabel );

		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 3;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( tfTMarkText, gbcThreadPanel );
		pnThreadPanel.add( tfTMarkText );
		
		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 4;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( lbTDescrLabel, gbcThreadPanel );
		pnThreadPanel.add( lbTDescrLabel );

		JScrollPane scpTDescrArea = new JScrollPane( taTDescrArea );
		gbcThreadPanel.gridx = 1;
		gbcThreadPanel.gridy = 5;
		gbcThreadPanel.gridwidth = 5;
		gbcThreadPanel.gridheight = 2;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0.5;
		gbcThreadPanel.insets = new Insets( 0,0,0,0 );
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints( scpTDescrArea, gbcThreadPanel );
		pnThreadPanel.add( scpTDescrArea );

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 9;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 5;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0.5;
		gbcPanel0.insets = new Insets( 0,0,0,0 );
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnThreadPanel, gbcPanel0 );
		pnPanel0.add( pnThreadPanel );

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
//		pnGeneralPanel.setBackground(Color.WHITE);
//		pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		cmbTTypeCombo.addElements(new LinkedList(StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE), true)));
		
		cmbTNameCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				CableThreadType ctt = (CableThreadType)e.getItem();
				currentCash = cashedFields.get(ctt);
				boolean b = currentCash != null; 
				tfTMarkText.setEnabled(b);
				cmbTColorCombo.setEnabled(b);
				cmbTTypeCombo.setEnabled(b);
				taTDescrArea.setEnabled(b);

				tfTMarkText.setText(b ? currentCash.mark : "");
				taTDescrArea.setText(b ? currentCash.description : "");
				if (b) { 
					cmbTColorCombo.addItem(new Color(currentCash.color));
					cmbTTypeCombo.setSelectedItem(currentCash.type);
				}
			}
		});
		
		tfTMarkText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (currentCash != null)
					currentCash.mark = tfTMarkText.getText(); 
			}
		});
		
		cmbTColorCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (currentCash != null && e.getItem() instanceof Color)
					currentCash.color = ((Color)e.getItem()).getRGB();
			}
		});
		
		cmbTTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (currentCash != null)
					currentCash.type = (LinkType)e.getItem();
			}
		});
		
		taTDescrArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (currentCash != null)
					currentCash.description = taTDescrArea.getText(); 
			}
		});
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(tfManufacturerText);
		addToUndoableListener(tfManufacturerCodeText);
		addToUndoableListener(taDescriptionArea);
		
		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});
	}
	
	public JComponent getGUI() {
		return this.pnPanel0; 
	}

	public Object getObject() {
		return this.linkType;
	}
	
	private List<CableThreadType> getSortedThreadTypes() {
		List<CableThreadType> threads = new LinkedList<CableThreadType>(this.linkType.getCableThreadTypes(false));
		Collections.sort(threads, new WrapperComparator(CableThreadTypeWrapper.getInstance(), StorableObjectWrapper.COLUMN_CODENAME));
		return threads;
	}

	public void setObject(Object or) {
		this.linkType = (CableLinkType) or;
		sortedTheradTypes = Collections.EMPTY_LIST;
		
		if (this.linkType != null) {
			this.tfNameText.setText(this.linkType.getName());
			this.taDescriptionArea.setText(this.linkType.getDescription());
			this.tfManufacturerText.setText(this.linkType.getManufacturer());
			this.tfManufacturerCodeText.setText(this.linkType.getManufacturerCode());
			sortedTheradTypes = getSortedThreadTypes();
		} 
		else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
		
		cashedFields = new HashMap<CableThreadType, ThreadTypeFields>();
		currentCash = null;
		for (Iterator it = sortedTheradTypes.iterator(); it.hasNext();) {
			CableThreadType ctt = (CableThreadType) it.next();
			cashedFields.put(ctt, new ThreadTypeFields(ctt.getName(), ctt.getDescription(), ctt.getLinkType(), ctt.getColor()));
		}
		
		cmbTNameCombo.removeAllItems();
		if (!sortedTheradTypes.isEmpty()) {
			this.cmbTNameCombo.addElements(sortedTheradTypes);
			this.cmbTNameCombo.setEnabled(true);
			this.tfThreadNumText.setValue(new Integer(sortedTheradTypes.size()));
		} else {
			this.cmbTNameCombo.setEnabled(false);
			this.tfThreadNumText.setValue(new Integer(0));
		}
		this.tfTMarkText.setEnabled(false);
		this.cmbTColorCombo.setEnabled(false);
		this.cmbTTypeCombo.setEnabled(false);
		this.taTDescrArea.setEnabled(false);
	}

	public void commitChanges() {
		if(MiscUtil.validName(this.tfNameText.getText())) {
			if (linkType == null) {
				try {
					linkType = SchemeObjectsFactory.createCableLinkType(tfNameText.getText());
					apply();
					aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, linkType, SchemeEvent.CREATE_OBJECT));
					aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, linkType, CableLinkTypePropertiesManager.getInstance(aContext), ObjectSelectedEvent.CABLELINK_TYPE));
				} 
				catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}	else {
				apply();
			}
		}
	}
	
	private void apply() {
		this.linkType.setName(this.tfNameText.getText());
		this.linkType.setDescription(this.taDescriptionArea.getText());
		this.linkType.setManufacturer(this.tfManufacturerText.getText());
		this.linkType.setManufacturerCode(this.tfManufacturerCodeText.getText());
		
		for (Iterator it = cashedFields.keySet().iterator(); it.hasNext();) {
			CableThreadType ctt = (CableThreadType)it.next();
			ThreadTypeFields fields = (ThreadTypeFields)cashedFields.get(ctt);
			ctt.setName(fields.mark);
			ctt.setDescription(fields.description);
			ctt.setColor(fields.color);
			ctt.setLinkType(fields.type);
		}
		
		int newSize = ((Integer)this.tfThreadNumText.getValue()).intValue();
		int oldSize = this.sortedTheradTypes.size(); 
					
		try {
			if (oldSize < newSize) { // create
				LinkType tlType;
				if (!this.sortedTheradTypes.isEmpty()) {
					tlType = ((CableThreadType)this.sortedTheradTypes.iterator().next()).getLinkType(); 
				} else {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
					Set linkTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					if (linkTypes.isEmpty()) {
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), "No LinkType found", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					tlType = (LinkType)linkTypes.iterator().next();
				}
				
				for (int i = oldSize; i < newSize; i++) {
					SchemeObjectsFactory.createCableThreadType(
							EquipmentTypeCodenames.getName(EquipmentTypeCodenames.THREAD) + (i + 1), 
							Integer.toString(i + 1), 
							tlType, linkType);
				}
			} else if (oldSize > newSize) { // remove
				int i = oldSize;
				Set<Identifier> removed = new HashSet<Identifier>();
				for (ListIterator it = this.sortedTheradTypes.listIterator(oldSize); it.hasPrevious() && i > newSize; i--) {
					CableThreadType ctt = (CableThreadType)it.previous();
					removed.add(ctt.getId());
				}
				StorableObjectPool.delete(removed);
			}
		} catch (ApplicationException e1) {
			Log.errorException(e1);
		}
		
		try {
			StorableObjectPool.flush(this.linkType.getId(), false);
			StorableObjectPool.flush(ObjectEntities.CABLETHREAD_TYPE_CODE, false);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.linkType, SchemeEvent.UPDATE_OBJECT));
	}
}
