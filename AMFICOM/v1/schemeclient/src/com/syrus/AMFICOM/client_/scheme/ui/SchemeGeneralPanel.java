/*-
 * $Id: SchemeGeneralPanel.java,v 1.15 2006/01/25 12:59:01 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.util.MissingResourceException;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.15 $, $Date: 2006/01/25 12:59:01 $
 * @module schemeclient
 */

public class SchemeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected Scheme scheme;
	private Identifier imageId;
	private static final Item[] schemeKinds = new IconedNode[] { 
		new IconedNode(IdlKind.NETWORK, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_NETWORK)),
		new IconedNode(IdlKind.BUILDING, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_BUILDING)),
		new IconedNode(IdlKind.FLOOR, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_FLOOR)),
		new IconedNode(IdlKind.CABLE_SUBNETWORK, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_CABLE))
	};
	
	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton btCommitBut = new JButton();
	JLabel lbSymbolLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField tfSymbolText= new JTextField();
	JButton btSymbolBut = new JButton();
	JLabel lbKindLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_KIND));
	JComboBox cmbKindCombo = new AComboBox();
	JLabel lbDimensionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_DIMENSION));
	JLabel lbWidthLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_SHORT_WIDTH));
	JLabel lbMMLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MILLIMETER));
	JFormattedTextField tfWidthText = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
	JLabel lbHeightLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_SHORT_HEIGHT));
	JFormattedTextField tfHeightText = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
	JLabel lbSizeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_SIZE));
	JComboBox cmbSizeCombo = new AComboBox();
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2,10);
	private static final Long ZERO = new Long(0);
	
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

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

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
		gbcPanel0.gridwidth = 11;
		gbcPanel0.gridheight = 5;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescrArea, gbcPanel0 );
		pnPanel0.add( scpDescrArea );

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( "" ) );
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
		gbGeneralPanel.setConstraints( lbSymbolLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbSymbolLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 7;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfSymbolText, gbcGeneralPanel );
		pnGeneralPanel.add( tfSymbolText );

		gbcGeneralPanel.gridx = 9;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 1;
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
		gbcGeneralPanel.gridwidth = 8;
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
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( cmbSizeCombo, gbcGeneralPanel );
		pnGeneralPanel.add( cmbSizeCombo );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbWidthLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbWidthLabel );

		gbcGeneralPanel.gridx = 3;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 3;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0.5;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfWidthText, gbcGeneralPanel );
		pnGeneralPanel.add( tfWidthText );

		gbcGeneralPanel.gridx = 6;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbHeightLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbHeightLabel );

		gbcGeneralPanel.gridx = 7;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0.5;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfHeightText, gbcGeneralPanel );
		pnGeneralPanel.add( tfHeightText );
		
		gbcGeneralPanel.gridx = 9;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbMMLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbMMLabel );
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 12;
		gbcPanel0.gridheight = 6;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );
		
		for (int i = 0; i < SchemeResourceKeys.SIZES.length; i++) {
			cmbSizeCombo.addItem(SchemeResourceKeys.SIZES[i]);			
		}
		cmbSizeCombo.setRenderer(new SchemeExternedObjectRenderer());
		for (int i = 0; i < schemeKinds.length; i++) {
			cmbKindCombo.addItem(schemeKinds[i]);			
		}
		pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		cmbSizeCombo.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cmbSizeCombo_itemStateChanged(e.getItem());
			}
		});
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(tfSymbolText);
		addToUndoableListener(btSymbolBut);
		addToUndoableListener(cmbKindCombo);
		addToUndoableListener(cmbSizeCombo);
		addToUndoableListener(tfHeightText);
		addToUndoableListener(tfWidthText);
		
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
	
	private static class SchemeExternedObjectRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = -1316715209536756611L;

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			String key = (String)value;
			try {
				key = LangModelScheme.getString(key);
				setText(key);
			} catch (MissingResourceException e) {
				Log.debugMessage(this.getClass().getSimpleName() + "| resource not found for key " + key, Level.FINER); //$NON-NLS-1$
			}
			return this;
		}
	}
	
	void cmbSizeCombo_itemStateChanged(Object selected) {
		for (int i = 0; i < SchemeResourceKeys.SIZES.length - 1; i++) {
			if (SchemeResourceKeys.SIZES[i].equals(selected)) {
				this.tfWidthText.setValue(SchemeResourceKeys.WIDTHS[i]);
				this.tfHeightText.setValue(SchemeResourceKeys.HEIGHTS[i]);
				break;
			}
		}
	}
	
	void initSizeFields() {
		int w = this.scheme.getWidth() / 4; // in mm
		int h = this.scheme.getHeight() / 4; // in mm
		
		for (int i = 0; i < SchemeResourceKeys.WIDTHS.length; i++) {
			if (SchemeResourceKeys.WIDTHS[i].intValue() == w && SchemeResourceKeys.HEIGHTS[i].intValue() == h) {
				this.cmbSizeCombo.setSelectedItem(SchemeResourceKeys.SIZES[i]);
				cmbSizeCombo_itemStateChanged(SchemeResourceKeys.SIZES[i]);
				return;
			}
		}
		this.cmbSizeCombo.setSelectedIndex(this.cmbSizeCombo.getItemCount() - 1);
		this.tfWidthText.setValue(new Long(this.scheme.getWidth() / 4));
		this.tfHeightText.setValue(new Long(this.scheme.getHeight() / 4));
	}

	public void setObject(Object or) {
		this.scheme = (Scheme)or;
		Icon symbol = null;
		
		if (this.scheme != null) {
			this.tfNameText.setText(this.scheme.getName());
			this.taDescrArea.setText(this.scheme.getDescription());
			this.tfSymbolText.setText(this.scheme.getLabel());
			if (this.scheme.getSymbol() != null)
				symbol = new ImageIcon(this.scheme.getSymbol().getImage());
			initSizeFields();
			for (int i = 0; i < this.cmbKindCombo.getItemCount(); i++) {
				Item item = (Item)this.cmbKindCombo.getItemAt(i);
				if (item.getObject().equals(this.scheme.getKind())) {
					this.cmbKindCombo.setSelectedItem(item);
					break;
				}
			}
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescrArea.setText(SchemeResourceKeys.EMPTY);
			this.tfSymbolText.setText(SchemeResourceKeys.EMPTY);
			this.cmbSizeCombo.setSelectedIndex(this.cmbSizeCombo.getItemCount() - 1);
			this.tfWidthText.setValue(ZERO);
			this.tfHeightText.setValue(ZERO);
		}
		this.btSymbolBut.setIcon(symbol);
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.scheme;
	}

	public void commitChanges() {
		super.commitChanges();
		if (this.scheme != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.scheme.setName(this.tfNameText.getText());
			this.scheme.setDescription(this.taDescrArea.getText());
			this.scheme.setLabel(this.tfSymbolText.getText());
			if (this.btSymbolBut.getIcon() == null) {
				this.scheme.setSymbol(null);
			} else {
				try {
					this.scheme.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(this.imageId, true));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
			Item item = (Item)this.cmbKindCombo.getSelectedItem();
			this.scheme.setKind((IdlKind)item.getObject());
			this.scheme.setWidth(((Long)this.tfWidthText.getValue()).intValue() * 4);
			this.scheme.setHeight(((Long)this.tfHeightText.getValue()).intValue() * 4);
			
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.scheme.getId(), SchemeEvent.UPDATE_OBJECT));
		}
	}
}
