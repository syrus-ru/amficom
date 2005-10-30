/*-
 * $Id: CharacteristicAddDialog.java,v 1.19 2005/10/30 15:20:24 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeWrapper;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.19 $, $Date: 2005/10/30 15:20:24 $
 * @module commonclient
 */

public class CharacteristicAddDialog {	

	protected int result = JOptionPane.CANCEL_OPTION;

	protected CharacteristicTypeSort sort;
	private CharacteristicType selectedType;

	WrapperedComboBox<CharacteristicType> characteristicTypeComboBox;
	JRadioButton existingRadioButton;
	private JRadioButton newRadioButton;

	private ButtonGroup buttonGroup;

	private JPanel panel;
	JTextField nameField;
	JTextArea descriptionArea;
	private JLabel name;
	private JLabel description;
	private final Frame parent;
	private final String title;

	public CharacteristicAddDialog(final Frame parent, final String title) {
		this.parent = parent;
		this.title = title;
	}

	private void createUIItems() {
		this.existingRadioButton = new JRadioButton(LangModelGeneral.getString(ResourceKeys.I18N_EXISTING_CHARACTERISTICTYPE));
		this.newRadioButton = new JRadioButton(LangModelGeneral.getString(ResourceKeys.I18N_NEW_CHARACTERISTICTYPE));
		this.buttonGroup = new ButtonGroup();

		this.buttonGroup.add(this.existingRadioButton);
		this.buttonGroup.add(this.newRadioButton);

		final ActionListener actionListener = new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				final boolean b = CharacteristicAddDialog.this.existingRadioButton.isSelected();
				CharacteristicAddDialog.this.characteristicTypeComboBox.setEnabled(b);
				CharacteristicAddDialog.this.nameField.setEnabled(!b);
				CharacteristicAddDialog.this.descriptionArea.setEnabled(!b);

			}
		};
		this.existingRadioButton.addActionListener(actionListener);
		this.newRadioButton.addActionListener(actionListener);

		this.nameField = new JTextField();
		this.descriptionArea = new JTextArea();
		this.name = new JLabel(LangModelGeneral.getString(ResourceKeys.I18N_NAME));
		this.description = new JLabel(LangModelGeneral.getString(ResourceKeys.I18N_DESCRIPTION));

		this.characteristicTypeComboBox = new WrapperedComboBox<CharacteristicType>(CharacteristicTypeWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_NAME,
				StorableObjectWrapper.COLUMN_ID);
	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.createUIItems();

			final JScrollPane scrollPane = new JScrollPane();
			scrollPane.getViewport().add(this.descriptionArea);
			this.descriptionArea.setAutoscrolls(true);
			scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());

			this.panel = new JPanel(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints();

			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			this.panel.add(this.existingRadioButton, gbc);
			this.panel.add(this.characteristicTypeComboBox, gbc);
			this.panel.add(this.newRadioButton, gbc);
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			this.panel.add(this.name, gbc);
			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			this.panel.add(this.nameField, gbc);
			gbc.weightx = 0.0;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			this.panel.add(this.description, gbc);
			gbc.weighty = 1.0;
			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			this.panel.add(scrollPane, gbc);
			gbc.weighty = 0.0;
			gbc.weightx = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;			
		}
		return this.panel;
	}

	public int showDialog(final CharacteristicTypeSort ctSort, final Collection<Characteristic> characterisctics) {
		this.sort = ctSort;

		final String okButton = LangModelGeneral.getString(ResourceKeys.I18N_ADD);
		final String cancelButton = LangModelGeneral.getString(ResourceKeys.I18N_CANCEL);
		final JOptionPane optionPane = new JOptionPane(this.getPanel(),
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION,
				null,
				new Object[] { okButton, cancelButton },
				null);

		try {
			final TypicalCondition condition = new TypicalCondition(ctSort.value(),
					ctSort.value(),
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.CHARACTERISTIC_TYPE_CODE,
					CharacteristicTypeWrapper.COLUMN_SORT);
			final Collection<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(condition,
					true,
					true);

			final Collection<CharacteristicType> existingTypes = new HashSet<CharacteristicType>();
			for (Characteristic characteristic : characterisctics) {
				existingTypes.add(characteristic.getType());
			}

			for (final CharacteristicType characteristicType : characteristicTypes) {
				if (!existingTypes.contains(characteristicType)) {
					this.characteristicTypeComboBox.addItem(characteristicType);
				}
			}
		} catch (ApplicationException ex) {
			ex.printStackTrace();
		}

		if (this.characteristicTypeComboBox.getModel().getSize() == 0) {
			this.existingRadioButton.setEnabled(false);
			this.newRadioButton.doClick();
		} else {
			this.existingRadioButton.doClick();
		}

		final JDialog dialog = optionPane.createDialog(this.parent, this.title);

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// TODO fix absolute size to relative , maybe using pack ?
		final Dimension frameSize = new Dimension(350, 250);
		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		dialog.setSize(frameSize);
		dialog.setTitle(LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTIC));

		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.dispose();

		final Object selectedValue = optionPane.getValue();

		if (selectedValue == okButton) {

			if (this.existingRadioButton.isSelected()) {
				if (this.characteristicTypeComboBox.getSelectedItem() != null) {
					this.selectedType = (CharacteristicType) this.characteristicTypeComboBox.getSelectedItem();
				}
			} else {
				final String text = this.nameField.getText();
				if (text != null && text.trim().length() > 0) {
					try {
						final Identifier userId = LoginManager.getUserId();
						// TODO maybe create separated fields for codename, name and description ?
						this.selectedType = CharacteristicType.createInstance(userId,
								this.nameField.getText(),
								this.nameField.getText(),
								this.nameField.getText(),
								DataType.STRING,
								this.sort);
						StorableObjectPool.flush(this.selectedType, LoginManager.getUserId(), false); 
					} catch (ApplicationException e) {
						assert Log.errorMessage(e);
					}
				}
			}
			this.result = JOptionPane.OK_OPTION;
		} else {
			this.result = JOptionPane.CANCEL_OPTION;
		}

		return this.result;
	}

	public CharacteristicType getCharacteristicType() {
		return this.selectedType;
	}
}
