/*-
 * $Id: CharacteristicAddDialog.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

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

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeWrapper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
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
	JPanel panel = new JPanel();
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

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.panel.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(this.existingRadioButton, gridbag, c);
		addToPanel(this.characteristicTypeComboBox, gridbag, c);
		addToPanel(this.newRadioButton, gridbag, c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		addToPanel(this.name, gridbag, c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(this.nameField, gridbag, c);
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.RELATIVE;
		addToPanel(this.descr, gridbag, c);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(scrollPane, gridbag, c);
		c.weighty = 0.0;
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		addToPanel(this.buttonPanel, gridbag, c);

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
					}
					else
						return;
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
					}
					else
						return;
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

	public int showDialog(CharacteristicTypeSort sort1, Collection chars) {
		this.sort = sort1;

		try {
			EquivalentCondition condition = new EquivalentCondition(
					ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE);
			Collection characteristicTypes = StorableObjectPool
					.getStorableObjectsByCondition(condition, true);
			for (Iterator it = characteristicTypes.iterator(); it.hasNext();) {
				CharacteristicType ctype = (CharacteristicType) it.next();
				if (ctype.getSort().equals(sort1) && !chars.contains(ctype))
					this.characteristicTypeComboBox.addItem(ctype);
			}
		} catch (ApplicationException ex) {
			ex.printStackTrace();
		}
		
		if (this.characteristicTypeComboBox.getModel().getSize() == 0) {
			this.existingRadioButton.setEnabled(false);
			this.newRadioButton.doClick();
		}
		else
			this.existingRadioButton.doClick();
		
		setModal(true);
		setVisible(true);
		return this.res;
	}

	public CharacteristicType getCharacteristicType() {
		return this.selectedType;
	}
}
