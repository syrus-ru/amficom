/*-
 * $$Id: MapEditor.java,v 1.23 2006/05/03 04:46:32 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.map.editor.MapPermissionManager;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.23 $, $Date: 2006/05/03 04:46:32 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditor extends DefaultStorableObjectEditor {
	Map map;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel domainLabel = new JLabel();
	private WrapperedComboBox domainComboBox = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JButton commitButton = new JButton();

	public MapEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}

	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.domainComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
//		this.jPanel.setName(I18N.getString(MapEditorResourceKeys.TITLE_PROPERTIES));

		this.nameLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_NAME));
		this.domainLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_DOMAIN));
		this.descLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_DESCRIPTION));

		this.commitButton.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameTextField, constraints);

		constraints.gridx =  2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.commitButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.domainLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.domainComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(new JScrollPane(this.descTextArea), constraints);

		this.domainComboBox.setEnabled(false);
		
		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.descTextArea);
	}

	public Object getObject() {
		return this.map;
	}

	@Override
	protected boolean isEditable() {
		return MapPermissionManager.isEditionAllowed();
	}

	public void setObject(Object object) {
		this.commitButton.setEnabled(isEditable());
		
		if(object instanceof Map)
			this.map = (Map)object;
		else
			if(object instanceof VoidElement)
				this.map = ((VoidElement )object).getMapView().getMap();
		
		this.domainComboBox.removeAllItems();

		if(this.map == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText(""); //$NON-NLS-1$
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText(""); //$NON-NLS-1$
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.map.getName());

			Domain domain = null;
			Collection domains = null;

//			StorableObjectCondition condition = 
//				new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
//			try {
//				domains = StorableObjectPool.getStorableObjectsByCondition(
//						condition,
//						true);
//			} catch(ApplicationException e) {
//				Log.errorMessage(e);
//			}

			try {
				domain = StorableObjectPool.getStorableObject(
						this.map.getDomainId(), 
						false);
			} catch(ApplicationException e) {
				Log.errorMessage(e);
			}
			domains = Collections.singleton(domain);

			this.domainComboBox.addElements(domains);
			this.domainComboBox.setSelectedItem(domain);

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.map.getDescription());
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	@Override
	public void commitChanges() {
		String name = this.nameTextField.getText();
		if(MiscUtil.validName(name))
		try {
			this.map.setName(name);
			this.map.setDescription(this.descTextArea.getText());
		} 
		catch (Exception ex) {
			Log.errorMessage(ex);
		} 
		super.commitChanges();
	}
}
