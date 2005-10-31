/*
 * $Id: CableLinkTypeGeneralPanel.java,v 1.21 2005/10/31 12:30:25 bass Exp $
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

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
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.CableThreadTypeWrapper;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.LinkTypeWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.21 $, $Date: 2005/10/31 12:30:25 $
 * @module schemeclient
 */

public class CableLinkTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected CableLinkType linkType;
	protected List<CableThreadType> sortedTheradTypes;

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
	JTextArea taDescriptionArea = new JTextArea(2, 10);
	JPanel pnGeneralPanel = new JPanel();

	JLabel lbTNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.THREAD));
	WrapperedComboBox<CableThreadType> cmbTNameCombo = new WrapperedComboBox<CableThreadType>(CableThreadTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel lbTMarkLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MARK));
	JTextField tfTMarkText = new JTextField();
	JLabel lbTTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox<LinkType> cmbTTypeCombo = new WrapperedComboBox<LinkType>(LinkTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel lbTColorLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.COLOR));
	JComboBox cmbTColorCombo = new ColorChooserComboBox();
	JLabel lbTDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taTDescrArea = new JTextArea(2, 10);
	JPanel pnThreadPanel = new JPanel();

	Map<CableThreadType, ThreadTypeFields> cashedFields;
	ThreadTypeFields currentCash;

	private class ThreadTypeFields {
		private ThreadTypeFields(final String mark, final String description, final LinkType type, final int color) {
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
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	protected CableLinkTypeGeneralPanel(final CableLinkType linkType) {
		this();
		this.setObject(linkType);
	}

	private void jbInit() throws Exception {
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);

		final GridBagLayout gbGeneralPanel = new GridBagLayout();
		final GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		this.pnGeneralPanel.setLayout(gbGeneralPanel);

		this.lbNameLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbNameLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbNameLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 3;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfNameText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfNameText);
		
		gbcGeneralPanel.gridx = 5;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.commitButton, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.commitButton);

		this.lbManufacturerLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbManufacturerLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbManufacturerLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfManufacturerText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfManufacturerText);

		this.lbManufacturerCodeLabel.setFocusable(false);
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 2);
		gbGeneralPanel.setConstraints(this.lbManufacturerCodeLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbManufacturerCodeLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfManufacturerCodeText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfManufacturerCodeText);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 6;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( this.lbThreadNumLabel, gbcGeneralPanel );
		this.pnGeneralPanel.add(this.lbThreadNumLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 6;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.tfThreadNumText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfThreadNumText);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 6;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);

		this.lbDescriptionLabel.setFocusable( false );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 6;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints(this.lbDescriptionLabel, gbcPanel0);
		this.pnPanel0.add(this.lbDescriptionLabel);

		final JScrollPane scpDescriptionArea = new JScrollPane(this.taDescriptionArea);
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 7;
		gbcPanel0.gridwidth = 5;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints(scpDescriptionArea, gbcPanel0);
		this.pnPanel0.add(scpDescriptionArea);

		this.pnThreadPanel.setBorder(BorderFactory.createTitledBorder(""));
		final GridBagLayout gbThreadPanel = new GridBagLayout();
		final GridBagConstraints gbcThreadPanel = new GridBagConstraints();
		this.pnThreadPanel.setLayout(gbThreadPanel);

		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 0;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.insets = new Insets(0, 0, 0, 2);
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.lbTNameLabel, gbcThreadPanel);
		this.pnThreadPanel.add(this.lbTNameLabel);
		
		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 0;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.cmbTNameCombo, gbcThreadPanel);
		this.pnThreadPanel.add(this.cmbTNameCombo);

		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 1;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.lbTTypeLabel, gbcThreadPanel);
		this.pnThreadPanel.add(this.lbTTypeLabel);

		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 1;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.cmbTTypeCombo, gbcThreadPanel);
		this.pnThreadPanel.add(this.cmbTTypeCombo);

		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 2;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.lbTColorLabel, gbcThreadPanel);
		this.pnThreadPanel.add(this.lbTColorLabel);

		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 2;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.cmbTColorCombo, gbcThreadPanel);
		this.pnThreadPanel.add(this.cmbTColorCombo);
/*
		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 3;
		gbcThreadPanel.gridwidth = 2;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.lbTMarkLabel, gbcThreadPanel);
		this.pnThreadPanel.add(this.lbTMarkLabel);

		gbcThreadPanel.gridx = 2;
		gbcThreadPanel.gridy = 3;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.tfTMarkText, gbcThreadPanel);
		this.pnThreadPanel.add(this.tfTMarkText);
		*/
		gbcThreadPanel.gridx = 0;
		gbcThreadPanel.gridy = 4;
		gbcThreadPanel.gridwidth = 4;
		gbcThreadPanel.gridheight = 1;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 0;
		gbcThreadPanel.weighty = 0;
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(this.lbTDescrLabel, gbcThreadPanel);
		this.pnThreadPanel.add(this.lbTDescrLabel);

		JScrollPane scpTDescrArea = new JScrollPane(this.taTDescrArea);
		gbcThreadPanel.gridx = 1;
		gbcThreadPanel.gridy = 5;
		gbcThreadPanel.gridwidth = 5;
		gbcThreadPanel.gridheight = 2;
		gbcThreadPanel.fill = GridBagConstraints.BOTH;
		gbcThreadPanel.weightx = 1;
		gbcThreadPanel.weighty = 0.5;
		gbcThreadPanel.insets = new Insets(0, 0, 0, 0);
		gbcThreadPanel.anchor = GridBagConstraints.NORTH;
		gbThreadPanel.setConstraints(scpTDescrArea, gbcThreadPanel);
		this.pnThreadPanel.add(scpTDescrArea);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 9;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 5;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0.5;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnThreadPanel, gbcPanel0);
		this.pnPanel0.add(this.pnThreadPanel);

		this.pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
//		pnGeneralPanel.setBackground(Color.WHITE);
//		pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		this.cmbTNameCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				final CableThreadType ctt = (CableThreadType)e.getItem();
				CableLinkTypeGeneralPanel.this.currentCash = CableLinkTypeGeneralPanel.this.cashedFields.get(ctt);
				final boolean b = (CableLinkTypeGeneralPanel.this.currentCash != null); 
				CableLinkTypeGeneralPanel.this.tfTMarkText.setEnabled(b);
				CableLinkTypeGeneralPanel.this.cmbTColorCombo.setEnabled(b);
				CableLinkTypeGeneralPanel.this.cmbTTypeCombo.setEnabled(b);
				CableLinkTypeGeneralPanel.this.taTDescrArea.setEnabled(b);

				CableLinkTypeGeneralPanel.this.tfTMarkText.setText(b ? CableLinkTypeGeneralPanel.this.currentCash.mark : "");
				CableLinkTypeGeneralPanel.this.taTDescrArea.setText(b ? CableLinkTypeGeneralPanel.this.currentCash.description : "");
				if (b) {
					CableLinkTypeGeneralPanel.this.cmbTColorCombo.addItem(new Color(CableLinkTypeGeneralPanel.this.currentCash.color));
					CableLinkTypeGeneralPanel.this.cmbTTypeCombo.setSelectedItem(CableLinkTypeGeneralPanel.this.currentCash.type);
				}
			}
		});
		
		this.tfTMarkText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				if (CableLinkTypeGeneralPanel.this.currentCash != null) {
					CableLinkTypeGeneralPanel.this.currentCash.mark = CableLinkTypeGeneralPanel.this.tfTMarkText.getText();
				}
			}
		});

		this.cmbTColorCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				if (CableLinkTypeGeneralPanel.this.currentCash != null && e.getItem() instanceof Color) {
					CableLinkTypeGeneralPanel.this.currentCash.color = ((Color) e.getItem()).getRGB();
				}
			}
		});

		this.cmbTTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				if (CableLinkTypeGeneralPanel.this.currentCash != null) {
					CableLinkTypeGeneralPanel.this.currentCash.type = (LinkType) e.getItem();
				}
			}
		});

		this.taTDescrArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				if (CableLinkTypeGeneralPanel.this.currentCash != null) {
					CableLinkTypeGeneralPanel.this.currentCash.description = CableLinkTypeGeneralPanel.this.taTDescrArea.getText();
				}
			}
		});

		super.addToUndoableListener(this.tfNameText);
		super.addToUndoableListener(this.tfManufacturerText);
		super.addToUndoableListener(this.tfManufacturerCodeText);
		super.addToUndoableListener(this.taDescriptionArea);

		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				CableLinkTypeGeneralPanel.this.commitChanges();
			}
		});
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.linkType;
	}

	public void setObject(Object or) {
		this.linkType = (CableLinkType) or;

		this.cmbTTypeCombo.removeAllItems();
		try {
			final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
			final Set<LinkType> linkTypes = StorableObjectPool.getStorableObjectsByCondition(ec, true);
			this.cmbTTypeCombo.addElements(new LinkedList<LinkType>(linkTypes));
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}

		if (this.linkType != null) {
			this.tfNameText.setText(this.linkType.getName());
			this.taDescriptionArea.setText(this.linkType.getDescription());
			this.tfManufacturerText.setText(this.linkType.getManufacturer());
			this.tfManufacturerCodeText.setText(this.linkType.getManufacturerCode());
			this.sortedTheradTypes = ClientUtils.getSortedThreadTypes(this.linkType);
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
			this.sortedTheradTypes = new LinkedList<CableThreadType>();
		}

		this.cashedFields = new HashMap<CableThreadType, ThreadTypeFields>();
		this.currentCash = null;
		for (Iterator it = this.sortedTheradTypes.iterator(); it.hasNext();) {
			CableThreadType ctt = (CableThreadType) it.next();
			this.cashedFields.put(ctt, new ThreadTypeFields(ctt.getCodename(), ctt.getDescription(), ctt.getLinkType(), ctt.getColor()));
		}

		this.cmbTNameCombo.removeAllItems();
		if (!this.sortedTheradTypes.isEmpty()) {
			this.cmbTNameCombo.addElements(this.sortedTheradTypes);
			this.cmbTNameCombo.setEnabled(true);
			this.tfThreadNumText.setValue(new Integer(this.sortedTheradTypes.size()));
		} else {
			this.cmbTNameCombo.setEnabled(false);
			this.tfThreadNumText.setValue(new Integer(0));
		}
		this.tfTMarkText.setEnabled(false);
		this.cmbTColorCombo.setEnabled(false);
		this.cmbTTypeCombo.setEnabled(false);
		this.taTDescrArea.setEnabled(false);
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		
		if(MiscUtil.validName(this.tfNameText.getText())) {
			if (this.linkType == null) {
				try {
					this.linkType = SchemeObjectsFactory.createCableLinkType(this.tfNameText.getText());
					apply();
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, this.linkType, CableLinkTypePropertiesManager.getInstance(this.aContext), ObjectSelectedEvent.CABLELINK_TYPE));
				} 
				catch (CreateObjectException e) {
					Log.errorMessage(e);
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
		
		for (Iterator it = this.cashedFields.keySet().iterator(); it.hasNext();) {
			CableThreadType ctt = (CableThreadType)it.next();
			ThreadTypeFields fields = this.cashedFields.get(ctt);
//			ctt.setCodename(fields.mark);
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
					tlType = this.sortedTheradTypes.iterator().next().getLinkType(); 
				} else {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
					Set linkTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					if (linkTypes.isEmpty()) {
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
								LangModelScheme.getString("Message.error.cablelinktype_not_found"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					tlType = (LinkType)linkTypes.iterator().next();
				}
				
				// FIXME check name creation: need codename - number; name - "fiberX"
				String s1 = this.linkType.getId().getIdentifierString() + ":";
				for (int i = oldSize; i < newSize; i++) {
					this.sortedTheradTypes.add(SchemeObjectsFactory.createCableThreadType(
							s1 + Integer.toString(i + 1),
							LangModelScheme.getString(SchemeResourceKeys.THREAD) + (i + 1), 
							tlType, this.linkType));
				}
			} else if (oldSize > newSize) { // remove
				int i = oldSize;
				Set<Identifier> removed = new HashSet<Identifier>();
				for (ListIterator it = this.sortedTheradTypes.listIterator(oldSize); it.hasPrevious() && i > newSize; i--) {
					CableThreadType ctt = (CableThreadType)it.previous();
					removed.add(ctt.getId());
//					it.remove();
				}
				Log.debugMessage("Will be removed " + removed.size() + " CableThreadTypes", Level.FINEST);
				StorableObjectPool.delete(removed);
				StorableObjectPool.flush(removed, LoginManager.getUserId(), false);
			}
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
		
		try {
			Identifier userId = LoginManager.getUserId();
			for (Identifiable identifiable : this.sortedTheradTypes) {
				StorableObjectPool.flush(identifiable, userId, false);
			}
			StorableObjectPool.flush(this.linkType.getId(), userId, false);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.linkType.getId(), SchemeEvent.UPDATE_OBJECT));
	}
}
