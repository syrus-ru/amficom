/*-
 * $Id: SchemePathGeneralPanel.java,v 1.1 2005/08/11 07:43:40 stas Exp $
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionWrapper;
import com.syrus.AMFICOM.scheme.SchemePath;

public class SchemePathGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemePath schemePath;
	
	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JLabel lbSolutionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_MONITORING_SOLUTION));
	WrapperedComboBox cmbSolutionCombo = new WrapperedComboBox(SchemeMonitoringSolutionWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
	JButton btCommitBut = new JButton();
	JLabel lbStartLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.START_ELEMENT));
	JTextField tfStartText = new JTextField();
	JLabel lbEndLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.END_ELEMENT));
	JTextField tfEndText = new JTextField();
	JLabel lbPesLabel = new JLabel("pes");
	JList lsPesList = new JList();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2,10);
	
	protected SchemePathGeneralPanel() {
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

	protected SchemePathGeneralPanel(SchemePath schemePath) {
		this();
		setObject(schemePath);
	}

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

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

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbSolutionLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbSolutionLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( cmbSolutionCombo, gbcGeneralPanel );
		pnGeneralPanel.add( cmbSolutionCombo );

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
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbStartLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbStartLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfStartText, gbcGeneralPanel );
		pnGeneralPanel.add( tfStartText );

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbEndLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbEndLabel );

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( tfEndText, gbcGeneralPanel );
		pnGeneralPanel.add( tfEndText );
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 7;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lbPesLabel, gbcGeneralPanel );
		pnGeneralPanel.add( lbPesLabel );

		gbcGeneralPanel.gridx = 1;
		gbcGeneralPanel.gridy = 5;
		gbcGeneralPanel.gridwidth = 9;
		gbcGeneralPanel.gridheight = 3;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 1;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints( lsPesList, gbcGeneralPanel );
		pnGeneralPanel.add( lsPesList );
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 10;
		gbcPanel0.gridheight = 10;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pnGeneralPanel, gbcPanel0 );
		pnPanel0.add( pnGeneralPanel );

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 10;
		gbcPanel0.gridwidth = 8;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( lbDescriptionLabel, gbcPanel0 );
		pnPanel0.add( lbDescriptionLabel );

		taDescriptionArea = new JTextArea(2,10);
		JScrollPane scpDescriptionArea = new JScrollPane( taDescriptionArea );
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 11;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 3;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( scpDescriptionArea, gbcPanel0 );
		pnPanel0.add( scpDescriptionArea );
		
		addToUndoableListener(tfNameText);
		addToUndoableListener(cmbSolutionCombo);
		
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
	
	public void setObject(Object or) {
		this.schemePath = (SchemePath)or;
		this.cmbSolutionCombo.removeAllItems();
		
		if (this.schemePath != null) {
			SchemeMonitoringSolution solution = this.schemePath.getParentSchemeMonitoringSolution();
			Scheme scheme = solution.getParentScheme(); 
			this.cmbSolutionCombo.addElements(scheme.getSchemeMonitoringSolutions());
			this.cmbSolutionCombo.setSelectedItem(solution);
			
			this.tfNameText.setText(this.schemePath.getName());
			this.taDescriptionArea.setText(this.schemePath.getDescription());
			
			try {
				SchemeElement startElement = this.schemePath.getStartSchemeElement();
				this.tfStartText.setText(startElement.getName());
			} catch (IllegalStateException e) {
				this.tfStartText.setText(SchemeResourceKeys.EMPTY); 
			}
			try {
				SchemeElement endElement = this.schemePath.getEndSchemeElement();
				this.tfEndText.setText(endElement.getName());
			} catch (IllegalStateException e) {
				this.tfEndText.setText(SchemeResourceKeys.EMPTY); 
			}
			Vector<String> peNames = new Vector<String>(); 
			for (PathElement pe : this.schemePath.getPathMembers()) {
				peNames.add(pe.getName());
			}
			this.lsPesList.setListData(peNames);
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			this.tfStartText.setText(SchemeResourceKeys.EMPTY);
			this.tfEndText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.schemePath;
	}

	public void commitChanges() {
		if (this.schemePath != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemePath.setName(this.tfNameText.getText());
			this.schemePath.setDescription(this.taDescriptionArea.getText());
			this.schemePath.setParentSchemeMonitoringSolution((SchemeMonitoringSolution)this.cmbSolutionCombo.getSelectedItem());

			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemePath, SchemeEvent.UPDATE_OBJECT));
		}
	}
}
