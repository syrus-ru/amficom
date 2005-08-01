/*
 * $Id: LinkTypeGeneralPanel.java,v 1.4 2005/08/01 07:52:27 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/08/01 07:52:27 $
 * @module schemeclient_v1
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
	JTextArea taDescriptionArea = new JTextArea(2,10);
	JPanel pnGeneralPanel = new JPanel();
	
	protected LinkTypeGeneralPanel() {
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

	protected LinkTypeGeneralPanel(LinkType linkType) {
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
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 6;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(pnGeneralPanel, gbcPanel0);
		pnPanel0.add(pnGeneralPanel);

		lbDescriptionLabel.setFocusable(false);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 6;
		gbcPanel0.gridwidth = 2;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets(0, 5, 0, 2);
		gbPanel0.setConstraints(lbDescriptionLabel, gbcPanel0);
		pnPanel0.add(lbDescriptionLabel);

		JScrollPane scpDescriptionArea = new JScrollPane(taDescriptionArea);
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
		pnPanel0.add(scpDescriptionArea);

		pnGeneralPanel.setBorder( BorderFactory.createTitledBorder( SchemeResourceKeys.EMPTY ));
//		pnGeneralPanel.setBackground(Color.WHITE);
//		pnPanel0.setBackground(Color.WHITE);
		scpDescriptionArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);
		
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

	public void setObject(Object or) {
		this.linkType = (LinkType) or;

//		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
//		try {
//			Set lTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
//			for (Iterator it = lTypes.iterator(); it.hasNext();) {
//				LinkType type = (LinkType) it.next();
//				if (type.getName().startsWith("Q"))
//					StorableObjectPool.delete(type.getId());
//			}
//			StorableObjectPool.flush(ObjectEntities.LINK_TYPE_CODE, true);
//		} catch (ApplicationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if (this.linkType != null) {
			this.tfNameText.setText(this.linkType.getName());
			this.taDescriptionArea.setText(this.linkType.getDescription());
			this.tfManufacturerText.setText(this.linkType.getManufacturer());
			this.tfManufacturerCodeText.setText(this.linkType.getManufacturerCode());
		} 
		else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerText.setText(SchemeResourceKeys.EMPTY);
			this.tfManufacturerCodeText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public void commitChanges() {
		if(MiscUtil.validName(this.tfNameText.getText())) {
			if (linkType == null) {
				try {
					linkType = SchemeObjectsFactory.createLinkType(tfNameText.getText());
					apply();
					aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, linkType, SchemeEvent.CREATE_OBJECT));
					aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, linkType, LinkTypePropertiesManager.getInstance(aContext), ObjectSelectedEvent.LINK_TYPE));
				} 
				catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			} else {
				apply();
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
			Log.errorException(e);
		}
		this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.linkType, SchemeEvent.UPDATE_OBJECT));
	}
}
