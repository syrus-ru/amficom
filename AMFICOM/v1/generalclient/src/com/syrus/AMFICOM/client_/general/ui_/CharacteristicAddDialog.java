/*-
 * $Id: CharacteristicAddDialog.java,v 1.1 2005/04/18 08:55:31 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.resource.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 08:55:31 $
 * @module generalclient_v1
 */

public class CharacteristicAddDialog extends JDialog {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	private ApplicationContext aContext;

	public static final int OK = 1;
	public static final int CANCEL = 0;
	protected int res = CANCEL;

	protected CharacteristicTypeSort sort;
	private CharacteristicType selectedType;
	ObjComboBox characteristicTypeComboBox;
	JRadioButton existingRadioButton = new JRadioButton(LangModelGeneral
			.getString(Constants.EXISTING_CHARACTERISTICTYPE));
	JRadioButton newRadioButton = new JRadioButton(LangModelGeneral
			.getString(Constants.NEW_CHARACTERISTICTYPE));
	ButtonGroup buttonGroup = new ButtonGroup();
	JPanel panel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JTextField nameField = new JTextField();
	JTextArea descrArea = new JTextArea();
	JLabel name = new JLabel();
	JLabel descr = new JLabel();
	JButton okButton = new JButton();
	JButton cancelButton = new JButton();

	public CharacteristicAddDialog(Frame parent, String title, ApplicationContext aContext) {
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

		characteristicTypeComboBox = new ObjComboBox(CharacteristicTypeController
				.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(350, 250);

		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		setSize(frameSize);
		setTitle(LangModelGeneral.getString(Constants.CHARACTERISTIC));

		name.setText(LangModelGeneral.getString(Constants.NAME));
		descr.setText(LangModelGeneral.getString(Constants.DESCRIPTION));
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

		okButton.setText(LangModelGeneral.getString(Constants.ADD));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});
		cancelButton.setText(LangModelGeneral.getString(Constants.CANCEL));
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
									DataType.DATA_TYPE_STRING, sort);
				} catch (CreateObjectException ex) {
					ex.printStackTrace();
					return;
				}
			}
			else
				return;
		}
		res = OK;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e) {
		dispose();
	}

	public int showDialog(CharacteristicTypeSort sort1, Collection chars) {
		this.sort = sort1;

		try {
			EquivalentCondition condition = new EquivalentCondition(
					ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE);
			Collection characteristicTypes = GeneralStorableObjectPool
					.getStorableObjectsByCondition(condition, true);
			for (Iterator it = characteristicTypes.iterator(); it.hasNext();) {
				CharacteristicType ctype = (CharacteristicType) it.next();
				if (ctype.getSort().equals(sort1) && !chars.contains(ctype))
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
