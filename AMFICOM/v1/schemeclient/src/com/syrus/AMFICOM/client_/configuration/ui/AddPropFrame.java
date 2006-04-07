/*
 * $Id: AddPropFrame.java,v 1.20 2006/02/15 12:18:10 stas Exp $
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
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.20 $, $Date: 2006/02/15 12:18:10 $
 * @module schemeclient
 */

public class AddPropFrame extends JDialog {
	private static final long serialVersionUID = 1962940947796494298L;
	protected int res = JOptionPane.CANCEL_OPTION;
	protected IdlCharacteristicTypeSort sort;
	
	private CharacteristicType selectedType;
	final WrapperedComboBox<CharacteristicType> characteristicTypeComboBox = new WrapperedComboBox<CharacteristicType>(CharacteristicTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_DESCRIPTION,
			StorableObjectWrapper.COLUMN_ID);
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

	public AddPropFrame(final Frame parent, final String title) {
		super(parent, title);

		this.buttonGroup.add(this.existingRadioButton);
		this.buttonGroup.add(this.newRadioButton);
		this.existingRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				radioButtonStateChanged();
			}
		});
		this.newRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				radioButtonStateChanged();
			}
		});
		
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension frameSize = new Dimension(350, 250);
		
		super.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		super.setSize(frameSize);
		super.setTitle(LangModelScheme.getString(SchemeResourceKeys.CHARACTERISTIC));
		
		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(this.descrArea);
		this.descrArea.setAutoscrolls(true);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		
		final GridBagLayout gridbag = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		this.panel.setLayout(gridbag);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.addToPanel(this.existingRadioButton, gridbag, c);
		this.addToPanel(this.characteristicTypeComboBox, gridbag, c);
		this.addToPanel(this.newRadioButton, gridbag, c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		this.addToPanel(this.name, gridbag, c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.addToPanel(this.nameField, gridbag, c);
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.RELATIVE;
		this.addToPanel(this.descr, gridbag, c);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.addToPanel(scrollPane, gridbag, c);
		c.weighty = 0.0;
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.addToPanel(this.buttonPanel, gridbag, c);
		
		this.buttonPanel.setLayout(new FlowLayout());
		this.buttonPanel.add(this.okButton);
		this.buttonPanel.add(this.cancelButton);
		
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.panel, BorderLayout.CENTER);

	}

	private void addToPanel(final Component comp, final GridBagLayout gridbag, final GridBagConstraints c) {
		gridbag.setConstraints(comp, c);
		this.panel.add(comp);
	}

	void radioButtonStateChanged() {
		final boolean b = this.existingRadioButton.isSelected();
		this.characteristicTypeComboBox.setEnabled(b);
		this.nameField.setEnabled(!b);
		this.descrArea.setEnabled(!b);
	}

	void okButton_actionPerformed(final ActionEvent e) {
		if (this.existingRadioButton.isSelected()) {
			if (this.characteristicTypeComboBox.getSelectedItem() != null) {
				this.selectedType = (CharacteristicType) this.characteristicTypeComboBox.getSelectedItem();
			} else {
				return;
			}
		} else {
			if (!this.nameField.getText().equals(SchemeResourceKeys.EMPTY)) {
				try {
					this.selectedType = SchemeObjectsFactory.createCharacteristicType(this.nameField.getText(), this.sort);
				} catch (CreateObjectException ex) {
					Log.errorMessage(ex);
					return;
				}
			} else {
				return;
			}
		}
		this.res = JOptionPane.OK_OPTION;
		super.dispose();
	}

	void cancelButton_actionPerformed(final ActionEvent e) {
		super.dispose();
	}

	public int showDialog(final IdlCharacteristicTypeSort sort0, final Collection<CharacteristicType> chars) {
		this.sort = sort0;

		try {
			final EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CHARACTERISTIC_TYPE_CODE);
			Collection<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (final CharacteristicType ctype : characteristicTypes) {
				if (ctype.getSort().ordinal() == this.sort.value() && !chars.contains(ctype)) {
					this.characteristicTypeComboBox.addItem(ctype);
				}
			}
		} catch (ApplicationException ex) {
			Log.errorMessage(ex);
		}

		if (this.characteristicTypeComboBox.getModel().getSize() == 0) {
			this.existingRadioButton.setEnabled(false);
			this.newRadioButton.doClick();
		} else {
			this.existingRadioButton.doClick();
		}

		super.setModal(true);
		super.setVisible(true);
		return this.res;
	}

	public CharacteristicType getCharacteristicType() {
		return this.selectedType;
	}
}