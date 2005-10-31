/*
 * $Id: LinkTypeGeneralPanel.java,v 1.12 2005/10/31 12:30:25 bass Exp $
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/10/31 12:30:25 $
 * @module schemeclient
 */

public class LinkTypeGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected LinkType linkType;

	JPanel pnPanel0 = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton commitButton = new JButton();
	JLabel lbManufacturerLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
	JTextField tfManufacturerText = new JTextField();
	JLabel lbManufacturerCodeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
	JTextField tfManufacturerCodeText = new JTextField();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2, 10);
	JPanel pnGeneralPanel = new JPanel();

	protected LinkTypeGeneralPanel() {
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

	protected LinkTypeGeneralPanel(final LinkType linkType) {
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

		this.lbDescriptionLabel.setFocusable(false);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 6;
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

		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		// pnGeneralPanel.setBackground(Color.WHITE);
		// pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);

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
				LinkTypeGeneralPanel.this.commitChanges();
			}
		});
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.linkType;
	}

	public void setObject(final Object or) {
		this.linkType = (LinkType) or;

		// EquivalentCondition condition = new
		// EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
		// try {
		// Set lTypes = StorableObjectPool.getStorableObjectsByCondition(condition,
		// true);
		// for (Iterator it = lTypes.iterator(); it.hasNext();) {
		// LinkType type = (LinkType) it.next();
		// if (type.getName().startsWith("Q"))
		// StorableObjectPool.delete(type.getId());
		// }
		// StorableObjectPool.flush(ObjectEntities.LINK_TYPE_CODE, true);
		// } catch (ApplicationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		if (this.linkType != null) {
			this.tfNameText.setText(this.linkType.getName());
			this.taDescriptionArea.setText(this.linkType.getDescription());
			this.tfManufacturerText.setText(this.linkType.getManufacturer());
			this.tfManufacturerCodeText.setText(this.linkType.getManufacturerCode());
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	@Override
	public void commitChanges() {
		if (MiscUtil.validName(this.tfNameText.getText())) {
			if (this.linkType == null) {
				try {
					this.linkType = SchemeObjectsFactory.createLinkType(this.tfNameText.getText());
					this.apply();
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this,
							this.linkType,
							LinkTypePropertiesManager.getInstance(this.aContext),
							ObjectSelectedEvent.LINK_TYPE));
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
		this.linkType.setName(this.tfNameText.getText());
		this.linkType.setDescription(this.taDescriptionArea.getText());
		this.linkType.setManufacturer(this.tfManufacturerText.getText());
		this.linkType.setManufacturerCode(this.tfManufacturerCodeText.getText());

		try {
			StorableObjectPool.flush(this.linkType.getId(), LoginManager.getUserId(), true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.linkType.getId(), SchemeEvent.UPDATE_OBJECT));
	}
}
