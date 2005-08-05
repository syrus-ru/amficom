/*
 * $Id: AddPropFrame.java,v 1.13 2005/08/05 12:39:58 stas Exp $
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeWrapper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.13 $, $Date: 2005/08/05 12:39:58 $
 * @module schemeclient_v1
 */

public class AddPropFrame extends JDialog {
	private static final long serialVersionUID = 1962940947796494298L;
	protected int res = JOptionPane.CANCEL_OPTION;
	protected CharacteristicTypeSort sort;
	
	private CharacteristicType selectedType;
	WrapperedComboBox characteristicTypeComboBox = new WrapperedComboBox(CharacteristicTypeWrapper
			.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION, StorableObjectWrapper.COLUMN_ID);
	JRadioButton existingRadioButton = new JRadioButton(LangModelScheme.getString(SchemeResourceKeys.EXISTING_TYPE));
	JRadioButton newRadioButton = new JRadioButton(LangModelScheme.getString(SchemeResourceKeys.NEW_TYPE));
	ButtonGroup buttonGroup = new ButtonGroup();
	JPanel panel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JTextField nameField = new JTextField();
	JTextArea descrArea = new JTextArea();
	JLabel name = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JLabel descr = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JButton okButton = new JButton(LangModelScheme.getString(SchemeResourceKeys.OK));
	JButton cancelButton = new JButton(LangModelScheme.getString(SchemeResourceKeys.CANCEL));

	public AddPropFrame(Frame parent, String title) {
		super(parent, title);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		this.buttonGroup.add(this.existingRadioButton);
		this.buttonGroup.add(this.newRadioButton);
		this.existingRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radioButtonStateChanged();
			}
		});
		this.newRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radioButtonStateChanged();
			}
		});

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(350, 250);

		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		setSize(frameSize);
		setTitle(LangModelScheme.getString(SchemeResourceKeys.CHARACTERISTIC));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(this.descrArea);
		this.descrArea.setAutoscrolls(true);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.panel.setLayout(gridbag);

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
		this.getContentPane().add(this.panel, BorderLayout.CENTER);
	}

	private void addToPanel(Component comp, GridBagLayout gridbag,
			GridBagConstraints c) {
		gridbag.setConstraints(comp, c);
		this.panel.add(comp);
	}

	void radioButtonStateChanged() {
		boolean b = this.existingRadioButton.isSelected();
		this.characteristicTypeComboBox.setEnabled(b);
		this.nameField.setEnabled(!b);
		this.descrArea.setEnabled(!b);
	}

	void okButton_actionPerformed(ActionEvent e) {
		if (this.existingRadioButton.isSelected()) {
			if (this.characteristicTypeComboBox.getSelectedItem() != null) {
				this.selectedType = (CharacteristicType) this.characteristicTypeComboBox
						.getSelectedItem();
			} else
				return;
		} 
		else {
			if (!this.nameField.getText().equals(SchemeResourceKeys.EMPTY)) {
				try {
					this.selectedType = SchemeObjectsFactory.createCharacteristicType(this.nameField.getText(), this.sort);
				} catch (CreateObjectException ex) {
					Log.errorException(ex);
					return;
				}
			} else
				return;
		}
		this.res = JOptionPane.OK_OPTION;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e) {
		dispose();
	}

	public int showDialog(CharacteristicTypeSort sort0, Collection chars) {
		this.sort = sort0;

		try {
			EquivalentCondition condition = new EquivalentCondition(
					ObjectEntities.CHARACTERISTIC_TYPE_CODE);
			Collection characteristicTypes = StorableObjectPool
					.getStorableObjectsByCondition(condition, true);
			for (Iterator it = characteristicTypes.iterator(); it.hasNext();) {
				CharacteristicType ctype = (CharacteristicType) it.next();
				if (ctype.getSort().equals(this.sort) && !chars.contains(ctype))
					this.characteristicTypeComboBox.addItem(ctype);
			}
		} catch (ApplicationException ex) {
			ex.printStackTrace();
		}
		
		if (this.characteristicTypeComboBox.getModel().getSize() == 0) {
			this.existingRadioButton.setEnabled(false);
			this.newRadioButton.doClick();
		} else
			this.existingRadioButton.doClick();
		
		setModal(true);
		setVisible(true);
		return this.res;
	}

	public CharacteristicType getCharacteristicType() {
		return this.selectedType;
	}
}