/*-
 * $Id: SchemeGeneralPanel.java,v 1.4 2005/06/23 12:58:23 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/06/23 12:58:23 $
 * @module schemeclient_v1
 */

public class SchemeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected Scheme scheme;
	private Identifier imageId;
	private static final Item[] schemeKinds = new IconedNode[] { 
		new IconedNode(Kind.NETWORK, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_NETWORK)),
		new IconedNode(Kind.BUILDING, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_BUILDING)),
		new IconedNode(Kind.CABLE_SUBNETWORK, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_CABLE))
	};
	
	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbSymbolLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField tfLabelText= new JTextField();
	JButton btSymbolBut = new JButton();
	JLabel lbKindLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_KIND));
	JComboBox cmbKindCombo = new JComboBox();
	JLabel lbDimensionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_DIMENSION));
	JLabel lbWidthLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_SHORT_WIDTH));
	JFormattedTextField tfWidthText = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
	JLabel lbHeightLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_SHORT_HEIGHT));
	JFormattedTextField tfHeightText = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
	JLabel lbSizeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_SIZE));
	JComboBox cmbSizeCombo = new AComboBox();
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2,10);
	private static final Integer ZERO = new Integer(0);
	
	protected SchemeGeneralPanel() {
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

	protected SchemeGeneralPanel(Scheme scheme) {
		this();
		setObject(scheme);
	}

	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

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
		gbcGeneralPanel.gridwidth = 5;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfNameText, gbcGeneralPanel );
		pnGeneralPanel.add( tfNameText );
		
		gbcGeneralPanel.gridx = 7;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( commitButton, gbcGeneralPanel );
		pnGeneralPanel.add( commitButton );

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbSymbolLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbSymbolLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 4;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfLabelText, gbcGeneralPanel );
		pnGeneralPanel.add( tfLabelText );

		gbcGeneralPanel.gridx = 6;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( btSymbolBut, gbcGeneralPanel );
		pnGeneralPanel.add( btSymbolBut );

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbKindLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbKindLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 6;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( cmbKindCombo, gbcGeneralPanel );
		pnGeneralPanel.add( cmbKindCombo );

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbDimensionLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbDimensionLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbWidthLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbWidthLabel );

		gbcGeneralPanel.gridx = 3;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfWidthText, gbcGeneralPanel );
		pnGeneralPanel.add( tfWidthText );

		gbcGeneralPanel.gridx = 4;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbHeightLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbHeightLabel );

		gbcGeneralPanel.gridx = 5;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfHeightText, gbcGeneralPanel );
		pnGeneralPanel.add( tfHeightText );

		gbcGeneralPanel.gridx = 6;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbSizeLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbSizeLabel );

		gbcGeneralPanel.gridx = 7;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( cmbSizeCombo, gbcGeneralPanel );
		pnGeneralPanel.add( cmbSizeCombo );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 8;
		gbcPanel0.gridheight = 6;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 6;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbDescrLabel, gbcPanel0 );
		pnPanel0.add( lbDescrLabel );

		JScrollPane scpDescrArea = new JScrollPane( taDescrArea );
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 7;
		gbcPanel0.gridwidth = 7;
		gbcPanel0.gridheight = 5;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescrArea, gbcPanel0 );
		pnPanel0.add( scpDescrArea );
		
		for (int i = 0; i < SchemeResourceKeys.SIZES.length; i++) {
			cmbSizeCombo.addItem(LangModelScheme.getString(SchemeResourceKeys.SIZES[i]));			
		}
		for (int i = 0; i < schemeKinds.length; i++) {
			cmbSizeCombo.addItem(schemeKinds[i]);			
		}
		pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		cmbSizeCombo.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cmbSizeCombo_itemStateChanged(e);
			}
		});
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(tfLabelText);
		addToUndoableListener(btSymbolBut);
		addToUndoableListener(cmbKindCombo);
		addToUndoableListener(cmbSizeCombo);
		addToUndoableListener(tfHeightText);
		addToUndoableListener(tfWidthText);
		
		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_ADD_CHARACTERISTIC));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});
	}
	
	void cmbSizeCombo_itemStateChanged(ItemEvent e) {
		Object selected = e.getItem();
		for (int i = 0; i < SchemeResourceKeys.SIZES.length - 1; i++) {
			if (SchemeResourceKeys.SIZES[i].equals(selected)) {
				tfWidthText.setValue(SchemeResourceKeys.WIDTHS[i]);
				tfWidthText.setValue(SchemeResourceKeys.HEIGHTS[i]);
				break;
			}
		}
	}
	
	void initSizeFields() {
		int w = scheme.getWidth() / 4; // in mm
		int h = scheme.getHeight() / 4; // in mm
		
		boolean sizeSet = false;
		for (int i = 0; i < SchemeResourceKeys.WIDTHS.length; i++) {
			if (SchemeResourceKeys.WIDTHS[i].intValue() == w && SchemeResourceKeys.HEIGHTS[i].intValue() == h) {
				cmbSizeCombo.setSelectedItem(SchemeResourceKeys.SIZES[i]);
				sizeSet = true;
				break;
			}
		}
		if (!sizeSet) {
			this.cmbSizeCombo.setSelectedIndex(cmbSizeCombo.getItemCount() - 1);
			this.tfWidthText.setValue(new Integer(scheme.getWidth() / 4));
			this.tfHeightText.setValue(new Integer(scheme.getHeight() / 4));
		}
	}

	public void setObject(Object or) {
		this.scheme = (Scheme)or;
		Icon symbol = null;
		
		if (this.scheme != null) {
			this.tfNameText.setText(scheme.getName());
			this.taDescrArea.setText(scheme.getDescription());
			this.tfLabelText.setText(scheme.getLabel());
			if (scheme.getSymbol() != null)
				symbol = new ImageIcon(scheme.getSymbol().getImage());
			initSizeFields();
			for (int i = 0; i < cmbKindCombo.getItemCount(); i++) {
				Item item = (Item)cmbKindCombo.getItemAt(i);
				if (item.getObject().equals(scheme.getKind())) {
					cmbKindCombo.setSelectedItem(item);
					break;
				}
			}
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescrArea.setText(SchemeResourceKeys.EMPTY);
			this.tfLabelText.setText(SchemeResourceKeys.EMPTY);
			this.cmbSizeCombo.setSelectedIndex(cmbSizeCombo.getItemCount() - 1);
			this.tfWidthText.setValue(ZERO);
			this.tfHeightText.setValue(ZERO);
		}
		this.btSymbolBut.setIcon(symbol);
	}

	public JComponent getGUI() {
		return pnPanel0;
	}

	public Object getObject() {
		return scheme;
	}

	public void commitChanges() {
		if (scheme != null && MiscUtil.validName(this.tfNameText.getText())) {
			initSizeFields();
			scheme.setName(this.tfNameText.getText());
			scheme.setDescription(this.taDescrArea.getText());
			scheme.setLabel(this.tfLabelText.getText());
			if (this.btSymbolBut.getIcon() == null) {
				scheme.setSymbol(null);
			} else {
				try {
					scheme.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(imageId, true));
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
			Item item = (Item)cmbKindCombo.getSelectedItem();
			scheme.setKind((Kind)item.getObject());
			scheme.setWidth(((Integer)this.tfWidthText.getValue()).intValue());
			scheme.setHeight(((Integer)this.tfHeightText.getValue()).intValue());
		}	
	}
}
