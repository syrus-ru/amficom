/*
 * $Id: AddPropFrame.java,v 1.2 2005/03/30 13:33:39 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.scheme.ui.Constants;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeController;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/30 13:33:39 $
 * @module schemeclient_v1
 */

public class AddPropFrame extends JDialog {
	private ApplicationContext aContext;

	protected int res = Constants.CANCEL;
	protected CharacteristicTypeSort sort;
	
	private CharacteristicType selectedType;
	ObjComboBox characteristicTypeComboBox = new ObjComboBox(CharacteristicTypeController
			.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);
	JRadioButton existingRadioButton = new JRadioButton(Constants.TEXT_EXISTING_TYPE);
	JRadioButton newRadioButton = new JRadioButton(Constants.TEXT_NEW_TYPE);
	ButtonGroup buttonGroup = new ButtonGroup();
	JPanel panel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JTextField nameField = new JTextField();
	JTextArea descrArea = new JTextArea();
	JLabel name = new JLabel(Constants.TEXT_NAME);
	JLabel descr = new JLabel(Constants.TEXT_DESCRIPTION);
	JButton okButton = new JButton(Constants.TEXT_OK);
	JButton cancelButton = new JButton(Constants.TEXT_CANCEL);

	public AddPropFrame(Frame parent, String title, ApplicationContext aContext) {
		super(parent, title);
		this.aContext = aContext;

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		buttonGroup.add(existingRadioButton);
		buttonGroup.add(newRadioButton);
		existingRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radioButtonStateChanged();
			}
		});
		newRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radioButtonStateChanged();
			}
		});

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(350, 250);

		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		setSize(frameSize);
		setTitle(Constants.TEXT_CHARACTERISTIC);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(descrArea);
		descrArea.setAutoscrolls(true);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(existingRadioButton, gridbag, c);
		addToPanel(characteristicTypeComboBox, gridbag, c);
		addToPanel(newRadioButton, gridbag, c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		addToPanel(name, gridbag, c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(nameField, gridbag, c);
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.RELATIVE;
		addToPanel(descr, gridbag, c);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(scrollPane, gridbag, c);
		c.weighty = 0.0;
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(buttonPanel, gridbag, c);

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void addToPanel(Component comp, GridBagLayout gridbag,
			GridBagConstraints c) {
		gridbag.setConstraints(comp, c);
		panel.add(comp);
	}

	void radioButtonStateChanged() {
		boolean b = existingRadioButton.isSelected();
		characteristicTypeComboBox.setEnabled(b);
		nameField.setEnabled(!b);
		descrArea.setEnabled(!b);
	}

	void okButton_actionPerformed(ActionEvent e) {
		if (existingRadioButton.isSelected()) {
			if (characteristicTypeComboBox.getSelectedItem() != null) {
				selectedType = (CharacteristicType) characteristicTypeComboBox
						.getSelectedItem();
			}
			else
				return;
		} 
		else {
			if (!nameField.getText().equals("")) {
				try {
					Identifier userId = new Identifier(((RISDSessionInfo) aContext
							.getSessionInterface()).getAccessIdentifier().user_id);
					selectedType = CharacteristicType
							.createInstance(userId, nameField.getText(), nameField.getText(),
									DataType._DATA_TYPE_STRING, sort);
				} catch (CreateObjectException ex) {
					ex.printStackTrace();
					return;
				}
			}
			else
				return;
		}
		res = Constants.OK;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e) {
		dispose();
	}

	public int showDialog(CharacteristicTypeSort sort0, Collection chars) {
		this.sort = sort0;

		try {
			EquivalentCondition condition = new EquivalentCondition(
					ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE);
			Collection characteristicTypes = GeneralStorableObjectPool
					.getStorableObjectsByCondition(condition, true);
			for (Iterator it = characteristicTypes.iterator(); it.hasNext();) {
				CharacteristicType ctype = (CharacteristicType) it.next();
				if (ctype.getSort().equals(this.sort) && !chars.contains(ctype))
					characteristicTypeComboBox.addItem(ctype);
			}
		} catch (ApplicationException ex) {
			ex.printStackTrace();
		}
		
		if (characteristicTypeComboBox.getModel().getSize() == 0) {
			existingRadioButton.setEnabled(false);
			newRadioButton.doClick();
		}
		else
			existingRadioButton.doClick();
		
		setModal(true);
		setVisible(true);
		return res;
	}

	public CharacteristicType getCharacteristicType() {
		return selectedType;
	}
}