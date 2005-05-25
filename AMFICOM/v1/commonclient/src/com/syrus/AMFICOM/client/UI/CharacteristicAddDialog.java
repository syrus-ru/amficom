/*-
 * $Id: CharacteristicAddDialog.java,v 1.3 2005/05/25 10:28:17 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.BorderLayout;
import java.awt.Container;
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
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;

/**
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/05/25 10:28:17 $
 * @module commonclient_v1
 */

public class CharacteristicAddDialog extends JDialog {
	

	public static final int OK = 1;
	public static final int CANCEL = 0;
	protected int res = CANCEL;

	protected CharacteristicTypeSort sort;
	CharacteristicType selectedType;
	WrapperedComboBox characteristicTypeComboBox;
	JRadioButton existingRadioButton = new JRadioButton(LangModelGeneral
			.getString(ResourceKeys.I18N_EXISTING_CHARACTERISTICTYPE));
	JRadioButton newRadioButton = new JRadioButton(LangModelGeneral
			.getString(ResourceKeys.I18N_NEW_CHARACTERISTICTYPE));
	ButtonGroup buttonGroup = new ButtonGroup();
	JPanel panel;
	JPanel buttonPanel = new JPanel();
	JTextField nameField = new JTextField();
	JTextArea descrArea = new JTextArea();
	JLabel name = new JLabel();
	JLabel descr = new JLabel();
	JButton okButton = new JButton();
	JButton cancelButton = new JButton();

	public CharacteristicAddDialog(Frame parent, String title) {
		super(parent, title);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.buttonGroup.add(this.existingRadioButton);
		this.buttonGroup.add(this.newRadioButton);
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean b = CharacteristicAddDialog.this.existingRadioButton.isSelected();
				CharacteristicAddDialog.this.characteristicTypeComboBox.setEnabled(b);
				CharacteristicAddDialog.this.nameField.setEnabled(!b);
				CharacteristicAddDialog.this.descrArea.setEnabled(!b);
			
			}
		};
		this.existingRadioButton.addActionListener(actionListener);
		this.newRadioButton.addActionListener(actionListener);

		this.characteristicTypeComboBox = new WrapperedComboBox(CharacteristicTypeWrapper
				.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION, StorableObjectWrapper.COLUMN_ID);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(350, 250);

		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		setSize(frameSize);
		setTitle(LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTIC));

		this.name.setText(LangModelGeneral.getString(ResourceKeys.I18N_NAME));
		this.descr.setText(LangModelGeneral.getString(ResourceKeys.I18N_DESCRIPTION));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(this.descrArea);
		this.descrArea.setAutoscrolls(true);
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
		this.panel.add(this.descr, gbc);
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.panel.add(scrollPane, gbc);
		gbc.weighty = 0.0;
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.panel.add(this.buttonPanel, gbc);

		this.buttonPanel.setLayout(new FlowLayout());
		this.buttonPanel.add(this.okButton);
		this.buttonPanel.add(this.cancelButton);

		this.okButton.setText(LangModelGeneral.getString(ResourceKeys.I18N_ADD));
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CharacteristicAddDialog.this.existingRadioButton.isSelected()) {
					if (CharacteristicAddDialog.this.characteristicTypeComboBox.getSelectedItem() != null) {
						CharacteristicAddDialog.this.selectedType = (CharacteristicType) CharacteristicAddDialog.this.characteristicTypeComboBox
								.getSelectedItem();
					} else {
						return;
					}
				} 
				else {
					String text = CharacteristicAddDialog.this.nameField.getText();
					if (text != null && text.trim().length() > 0) {
						try {
							Identifier userId = LoginManager.getUserId();
							CharacteristicAddDialog.this.selectedType = CharacteristicType
									.createInstance(userId, CharacteristicAddDialog.this.nameField.getText(), CharacteristicAddDialog.this.nameField.getText(),
											DataType.DATA_TYPE_STRING, CharacteristicAddDialog.this.sort);
						} catch (CreateObjectException ex) {
							ex.printStackTrace();
							return;
						}
					} else {
						return;
					}
				}
				CharacteristicAddDialog.this.res = OK;
				CharacteristicAddDialog.this.dispose();
			}
		});
		this.cancelButton.setText(LangModelGeneral.getString(ResourceKeys.I18N_CANCEL));
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CharacteristicAddDialog.this.dispose();
			}
		});

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(this.panel, BorderLayout.CENTER);
	}

	public int showDialog(CharacteristicTypeSort sort1, Collection characaterisctics) {
		this.sort = sort1;

		try {
			TypicalCondition condition = new TypicalCondition(sort1.value(), sort1.value(), OperationSort.OPERATION_EQUALS,				
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
		
		setModal(true);
		setVisible(true);
		return this.res;
	}

	public CharacteristicType getCharacteristicType() {
		return this.selectedType;
	}
}
