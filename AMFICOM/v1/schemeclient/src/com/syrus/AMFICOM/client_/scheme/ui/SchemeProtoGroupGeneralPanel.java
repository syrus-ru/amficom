/*-
 * $Id: SchemeProtoGroupGeneralPanel.java,v 1.14 2005/10/30 15:20:54 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/10/30 15:20:54 $
 * @module schemeclient
 */

public class SchemeProtoGroupGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeProtoGroup schemeProtoGroup;
	private Identifier imageId;

	JPanel panel0 = new JPanel();
	JPanel generalPanel = new JPanel();
	JLabel nameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField nameText = new JTextField();
	JButton commitButton = new JButton();
	JButton symbolBut = new JButton();
	JLabel lbParentLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PARENT_GROUP));
	WrapperedComboBox<SchemeProtoGroup> parentCombo = new WrapperedComboBox<SchemeProtoGroup>(SchemeProtoGroupWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel descrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea descrArea = new JTextArea(2, 10);

	protected SchemeProtoGroupGeneralPanel() {
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

	protected SchemeProtoGroupGeneralPanel(final SchemeProtoGroup schemeProtoGroup) {
		this();
		this.setObject(schemeProtoGroup);
	}

	private void jbInit() throws Exception {
		final GridBagLayout gbpanel0 = new GridBagLayout();
		final GridBagConstraints gbcpanel0 = new GridBagConstraints();
		this.panel0.setLayout(gbpanel0);

		final GridBagLayout gbgeneralPanel = new GridBagLayout();
		final GridBagConstraints gbcgeneralPanel = new GridBagConstraints();
		this.generalPanel.setLayout(gbgeneralPanel);

		this.nameLabel.setFocusable(false);
		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.insets = new Insets(0, 0, 0, 0);
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.nameLabel, gbcgeneralPanel);
		this.generalPanel.add(this.nameLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 4;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.nameText, gbcgeneralPanel);
		this.generalPanel.add(this.nameText);

		gbcgeneralPanel.gridx = 6;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.symbolBut, gbcgeneralPanel);
		this.generalPanel.add(this.symbolBut);

		gbcgeneralPanel.gridx = 7;
		gbcgeneralPanel.gridy = 0;
		gbcgeneralPanel.gridwidth = 1;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.commitButton, gbcgeneralPanel);
		this.generalPanel.add(this.commitButton);

		gbcgeneralPanel.gridx = 0;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 2;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 0;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.lbParentLabel, gbcgeneralPanel);
		this.generalPanel.add(this.lbParentLabel);

		gbcgeneralPanel.gridx = 2;
		gbcgeneralPanel.gridy = 2;
		gbcgeneralPanel.gridwidth = 6;
		gbcgeneralPanel.gridheight = 1;
		gbcgeneralPanel.fill = GridBagConstraints.BOTH;
		gbcgeneralPanel.weightx = 1;
		gbcgeneralPanel.weighty = 0;
		gbcgeneralPanel.anchor = GridBagConstraints.NORTH;
		gbgeneralPanel.setConstraints(this.parentCombo, gbcgeneralPanel);
		this.generalPanel.add(this.parentCombo);

		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 0;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 7;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(this.generalPanel, gbcpanel0);
		this.panel0.add(this.generalPanel);

		this.descrLabel.setFocusable(false);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 7;
		gbcpanel0.gridwidth = 3;
		gbcpanel0.gridheight = 1;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 0;
		gbcpanel0.weighty = 0;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(this.descrLabel, gbcpanel0);
		this.panel0.add(this.descrLabel);

		final JScrollPane scpdescrArea = new JScrollPane(this.descrArea);
		gbcpanel0.gridx = 0;
		gbcpanel0.gridy = 8;
		gbcpanel0.gridwidth = 8;
		gbcpanel0.gridheight = 3;
		gbcpanel0.fill = GridBagConstraints.BOTH;
		gbcpanel0.weightx = 1;
		gbcpanel0.weighty = 1;
		gbcpanel0.anchor = GridBagConstraints.NORTH;
		gbpanel0.setConstraints(scpdescrArea, gbcpanel0);
		this.panel0.add(scpdescrArea);

		this.generalPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		this.descrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);

		super.addToUndoableListener(this.nameText);
		super.addToUndoableListener(this.symbolBut);
		super.addToUndoableListener(this.parentCombo);
		super.addToUndoableListener(this.descrArea);

		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SchemeProtoGroupGeneralPanel.this.commitChanges();
			}
		});
	}

	public JComponent getGUI() {
		return this.panel0;
	}

	public Object getObject() {
		return this.schemeProtoGroup;
	}

	public void setObject(final Object or) {
		this.schemeProtoGroup = (SchemeProtoGroup) or;
		Icon symbol = null;

		this.parentCombo.removeAllItems();
		final EquivalentCondition condition = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
		try {
			final Set<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			groups.remove(this.schemeProtoGroup);
			groups.remove(SchemeObjectsFactory.stubProtoGroup);
			final List<SchemeProtoGroup> sortedGroups = new LinkedList<SchemeProtoGroup>(groups);
			Collections.sort(sortedGroups, new WrapperComparator<SchemeProtoGroup>(SchemeProtoGroupWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME));
			this.parentCombo.addElements(sortedGroups);
			this.parentCombo.addItem(null);
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
		}

		if (this.schemeProtoGroup != null) {
			this.nameText.setText(this.schemeProtoGroup.getName());
			this.descrArea.setText(this.schemeProtoGroup.getDescription());
			if (this.schemeProtoGroup.getSymbol() != null) {
				symbol = new ImageIcon(this.schemeProtoGroup.getSymbol().getImage());
			}
			this.parentCombo.setSelectedItem(this.schemeProtoGroup.getParentSchemeProtoGroup());
		} else {
			this.nameText.setText(SchemeResourceKeys.EMPTY);
			this.descrArea.setText(SchemeResourceKeys.EMPTY);
		}
		this.symbolBut.setIcon(symbol);
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (MiscUtil.validName(this.nameText.getText())) {
			if (this.schemeProtoGroup == null) {
				try {
					this.schemeProtoGroup = SchemeObjectsFactory.createSchemeProtoGroup();
					this.apply();
					this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this,
							this.schemeProtoGroup,
							SchemeProtoGroupPropertiesManager.getInstance(this.aContext),
							ObjectSelectedEvent.SCHEME_PROTOGROUP));
				} catch (CreateObjectException e) {
					assert Log.errorMessage(e);
					return;
				}
			} else {
				this.apply();
				this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this,
						this.schemeProtoGroup.getId(),
						SchemeEvent.UPDATE_OBJECT));
				this.aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this,
						this.schemeProtoGroup,
						SchemeProtoGroupPropertiesManager.getInstance(this.aContext),
						ObjectSelectedEvent.SCHEME_PROTOGROUP));
			}
		}
	}

	private void apply() {
		this.schemeProtoGroup.setName(this.nameText.getText());
		this.schemeProtoGroup.setDescription(this.descrArea.getText());
		if (this.symbolBut.getIcon() == null) {
			this.schemeProtoGroup.setSymbol(null);
		} else {
			try {
				this.schemeProtoGroup.setSymbol((BitmapImageResource)StorableObjectPool.getStorableObject(this.imageId, true));
			} catch (ApplicationException e) {
				assert Log.errorMessage(e);
			}
		}

		try {
			this.schemeProtoGroup.setParentSchemeProtoGroup((SchemeProtoGroup)this.parentCombo.getSelectedItem(), false);
			StorableObjectPool.flush(this.schemeProtoGroup.getId(), LoginManager.getUserId(), false);
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
		}
	}
}
