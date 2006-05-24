/*
 * $Id: MeasurementPortTypeGeneralPanel.java,v 1.33 2006/05/24 06:42:26 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.tree.CheckableNode;
import com.syrus.AMFICOM.client.UI.tree.CheckableTreeUI;
import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.33 $, $Date: 2006/05/24 06:42:26 $
 * @module schemeclient
 */

public class MeasurementPortTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected MeasurementPortType type;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2, 10);
	JLabel lbTestTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MEASUREMENT_TYPES));
	JPanel pnGeneralPanel = new JPanel();
	JTree trTestTypeTree;
	List measurementTypeNodes;
	Item root;

	protected MeasurementPortTypeGeneralPanel() {
		super();

		this.root = this.createRoot();
		final CheckableTreeUI treeUI = new CheckableTreeUI(this.root);
		this.trTestTypeTree = treeUI.getTree();
		this.trTestTypeTree.setRootVisible(false);
		this.measurementTypeNodes = this.getMeasurementTypeNodes();
		
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);
		
		this.lbDescriptionLabel.setFocusable(false);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 5, 0, 2);
		gbPanel0.setConstraints(this.lbDescriptionLabel, gbcPanel0);
		this.pnPanel0.add(this.lbDescriptionLabel);
		
		final JScrollPane scpDescriptionArea = new JScrollPane(this.taDescriptionArea);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints(scpDescriptionArea, gbcPanel0);
		this.pnPanel0.add(scpDescriptionArea);
		
		this.lbTestTypeLabel.setFocusable(false);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 4;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 5, 0, 2);
		gbPanel0.setConstraints(this.lbTestTypeLabel, gbcPanel0);
		this.pnPanel0.add(this.lbTestTypeLabel);
		
		final JScrollPane scpTestTypeTree = new JScrollPane(this.trTestTypeTree);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 5;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 2, 0, 2);
		gbPanel0.setConstraints(scpTestTypeTree, gbcPanel0);
		this.pnPanel0.add(scpTestTypeTree);
		
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
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.tfNameText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfNameText);
		
		gbcGeneralPanel.gridx = 3;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbcGeneralPanel.insets = new Insets(0, 0, 0, 0);
		gbGeneralPanel.setConstraints(this.commitButton, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.commitButton);
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 0, 0, 0);
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);
		
		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		// pnPanel0.setBackground(Color.WHITE);
		// pnGeneralPanel.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		scpTestTypeTree.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
		this.addToUndoableListener(this.tfNameText);
		this.addToUndoableListener(this.taDescriptionArea);
		this.addToUndoableListener(this.trTestTypeTree);
		
		this.commitButton.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				MeasurementPortTypeGeneralPanel.this.commitChanges();
			}
		});
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	protected MeasurementPortTypeGeneralPanel(final MeasurementPortType apt) {
		this();
		this.setObject(apt);
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.type;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isTypeEditionAllowed();
	}

	public void setObject(final Object or) {
		this.commitButton.setEnabled(isEditable());
		
		this.type = (MeasurementPortType) or;

		if (this.type != null) {
			this.tfNameText.setText(this.type.getName());
			this.taDescriptionArea.setText(this.type.getDescription());

			final Collection mTypes = this.type.getMeasurementTypes();
			for (final Iterator it = this.measurementTypeNodes.iterator(); it.hasNext();) {
				final CheckableNode node = (CheckableNode) it.next();
				node.setChecked(mTypes.contains(node.getObject()));
			}
			this.trTestTypeTree.updateUI();
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			for (final Iterator it = this.measurementTypeNodes.iterator(); it.hasNext();) {
				final CheckableNode node = (CheckableNode) it.next();
				node.setChecked(false);
			}
			this.trTestTypeTree.updateUI();
		}
	}

	List getMeasurementTypeNodes() {
		return this.root.getChildren();
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (MiscUtil.validName(this.tfNameText.getText())) {
			if (this.type == null) {
				try {
					this.type = SchemeObjectsFactory.createMeasurementPortType(this.tfNameText.getText());
					this.apply();
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this,
							this.type,
							MeasurementPortTypePropertiesManager.getInstance(this.aContext),
							ObjectSelectedEvent.MEASUREMENTPORT_TYPE));
				} catch (CreateObjectException e) {
					Log.errorMessage(e);
					return;
				}
			} else {
				this.apply();
			}
		}
	}

	private void apply() {
		this.type.setName(this.tfNameText.getText());
		this.type.setDescription(this.taDescriptionArea.getText());

		final EnumSet<MeasurementType> mTypeSet = EnumSet.noneOf(MeasurementType.class);

		for (final Iterator it = this.measurementTypeNodes.iterator(); it.hasNext();) {
			final CheckableNode node = (CheckableNode) it.next();
			if (node.isChecked()) {
				final MeasurementType mtype = (MeasurementType) node.getObject();
				mTypeSet.add(mtype);
			}
		}
		this.type.setMeasurementTypes(mTypeSet);

		try {
			StorableObjectPool.flush(this.type.getId(), LoginManager.getUserId(), true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.type.getId(), SchemeEvent.UPDATE_OBJECT));
	}

	Item createRoot() {
		final Item root1 = new IconedNode(SchemeResourceKeys.ROOT, LangModelScheme.getString(SchemeResourceKeys.ROOT));
		for (final MeasurementType measurementType : MeasurementType.values()) {
			if (!measurementType.equals(MeasurementType.UNKNOWN)) {
				root1.addChild(new CheckableNode(measurementType, measurementType.getDescription(), false));
			}
		}
		return root1;
	}
}
