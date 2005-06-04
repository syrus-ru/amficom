/*-
 * $Id: CharacteristicAddDialog.java,v 1.5 2005/06/04 16:56:24 bass Exp $
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
import java.util.Iterator;

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
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeWrapper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.CharacteristicType_TransferablePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/04 16:56:24 $
 * @module commonclient_v1
 */

public class CharacteristicAddDialog {	

	protected int result = JOptionPane.CANCEL_OPTION;

	protected CharacteristicTypeSort sort;
	private CharacteristicType selectedType;
	
	WrapperedComboBox characteristicTypeComboBox;
	JRadioButton existingRadioButton;	
	private JRadioButton newRadioButton;
	
	private ButtonGroup buttonGroup;
	
	private JPanel panel;
	JTextField nameField;
	JTextArea descriptionArea;
	private JLabel name;
	private JLabel description;
	private final Frame	parent;
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

		ActionListener actionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean b = CharacteristicAddDialog.this.existingRadioButton.isSelected();
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
		
		this.characteristicTypeComboBox = new WrapperedComboBox(CharacteristicTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_DESCRIPTION,
			StorableObjectWrapper.COLUMN_ID);

	}
	
	private JPanel getPanel() {
		if (this.panel == null) {
			this.createUIItems();

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.getViewport().add(this.descriptionArea);
			this.descriptionArea.setAutoscrolls(true);
			scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());

			this.panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();

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

	public int showDialog(CharacteristicTypeSort sort, Collection characaterisctics) {
		this.sort = sort;

		try {
			TypicalCondition condition = new TypicalCondition(sort.value(), sort.value(), OperationSort.OPERATION_EQUALS,				
					ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, CharacteristicTypeWrapper.COLUMN_SORT);
			Collection characteristicTypes = StorableObjectPool
					.getStorableObjectsByCondition(condition, true);
			for (Iterator it = characteristicTypes.iterator(); it.hasNext();) {
				CharacteristicType characteristicType = (CharacteristicType) it.next();
				if (!characaterisctics.contains(characteristicType)) {
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
		
		
		final String okButton = LangModelGeneral.getString(ResourceKeys.I18N_ADD);
		final String cancelButton = LangModelGeneral.getString(ResourceKeys.I18N_CANCEL);
		final JOptionPane optionPane = new JOptionPane(this.getPanel(), JOptionPane.PLAIN_MESSAGE,
														JOptionPane.OK_CANCEL_OPTION, null, new Object[] { okButton,
																cancelButton}, null);

		final JDialog dialog = optionPane.createDialog(this.parent, this.title);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// TODO fix absolute size to relative , maybe using pack ?
		Dimension frameSize = new Dimension(350, 250);
		dialog.setLocation((screenSize.width - frameSize.width) / 2,
			(screenSize.height - frameSize.height) / 2);
		dialog.setSize(frameSize);
		dialog.setTitle(LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTIC));
		
		dialog.setModal(true);
		dialog.show();
		dialog.dispose();

		final Object selectedValue = optionPane.getValue();

		if (selectedValue == okButton) {

			if (this.existingRadioButton.isSelected()) {
				if (this.characteristicTypeComboBox.getSelectedItem() != null) {
					this.selectedType = (CharacteristicType) this.characteristicTypeComboBox
							.getSelectedItem();
				}
			} else {
				String text = this.nameField.getText();
				if (text != null && text.trim().length() > 0) {
					try {
						Identifier userId = LoginManager.getUserId();
						this.selectedType = CharacteristicType.createInstance(userId,
							this.nameField.getText(), this.nameField
									.getText(), DataType.DATA_TYPE_STRING, this.sort);
					} catch (CreateObjectException ex) {
						ex.printStackTrace();
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
