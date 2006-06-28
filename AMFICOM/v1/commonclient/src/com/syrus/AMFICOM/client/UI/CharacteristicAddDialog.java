/*-
 * $Id: CharacteristicAddDialog.java,v 1.27 2006/05/29 11:12:29 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import static com.syrus.AMFICOM.general.CharacteristicTypeWrapper.COLUMN_SORT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.CharacteristicTypeWrapper;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.27 $, $Date: 2006/05/29 11:12:29 $
 * @module commonclient
 */

public class CharacteristicAddDialog {	

	protected int result = JOptionPane.CANCEL_OPTION;

	protected IdlCharacteristicTypeSort sort;
	private CharacteristicType selectedType;

	WrapperedComboBox<CharacteristicType> characteristicTypeComboBox;

	private JPanel panel;
	private final Frame parent;
	private final String title;

	public CharacteristicAddDialog(final Frame parent, final String title) {
		this.parent = parent;
		this.title = title;
	}

	private void createUIItems() {
		this.characteristicTypeComboBox = new WrapperedComboBox<CharacteristicType>(CharacteristicTypeWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_NAME,
				StorableObjectWrapper.COLUMN_ID);
	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.createUIItems();

			this.panel = new JPanel(new BorderLayout());
			this.panel.add(this.characteristicTypeComboBox, BorderLayout.CENTER);
		}
		return this.panel;
	}

	public int showDialog(final IdlCharacteristicTypeSort ctSort, final Collection<Characteristic> characterisctics) {
		this.sort = ctSort;

		final String okButton = I18N.getString(ResourceKeys.I18N_ADD);
		final String cancelButton = I18N.getString(ResourceKeys.I18N_CANCEL);
		final JOptionPane optionPane = new JOptionPane(this.getPanel(),
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION,
				null,
				new Object[] { okButton, cancelButton },
				null);

		try {
			final TypicalCondition condition = new TypicalCondition(
					CharacteristicTypeSort.valueOf(ctSort),
					OPERATION_EQUALS,
					CHARACTERISTIC_TYPE_CODE,
					COLUMN_SORT);
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

		final JDialog dialog = optionPane.createDialog(this.parent, this.title);

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.pack();
		final Dimension frameSize = dialog.getSize();
		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		dialog.setTitle(I18N.getString(ResourceKeys.I18N_CHARACTERISTIC));

		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.dispose();

		final Object selectedValue = optionPane.getValue();

		if (selectedValue == okButton) {
			if (this.characteristicTypeComboBox.getSelectedItem() != null) {
				this.selectedType = (CharacteristicType) this.characteristicTypeComboBox.getSelectedItem();
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
