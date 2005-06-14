/**
 * $Id: WrapperedComboChooserDialog.java,v 1.2 2005/06/14 11:29:53 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.util.Wrapper;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $
 * @module commonclient_v1
 */
public class WrapperedComboChooserDialog {
	public static Object showChooserDialog(Collection contents) {
		return showChooserDialog(
				contents, 
				null, 
				NamedObjectWrapper.getInstance(),
				NamedObjectWrapper.KEY_NAME,
				NamedObjectWrapper.KEY_NAME);
	}

	public static Object showChooserDialog(
			Collection contents,
			Object object) {
		return showChooserDialog(
				contents, 
				object, 
				NamedObjectWrapper.getInstance(),
				NamedObjectWrapper.KEY_NAME,
				NamedObjectWrapper.KEY_NAME);
	}

	public static Object showChooserDialog(
			Collection contents,
			Wrapper wrapper,
			String key,
			String compareKey) {
		return showChooserDialog(contents, null, wrapper, key, compareKey);
	}

		public static Object showChooserDialog(
			Collection contents,
			Object object,
			Wrapper wrapper,
			String key,
			String compareKey) {

		Object returnObject = null;
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel jLabel = new JLabel(LangModelGeneral.getString("Element"));
		WrapperedComboBox comboBox = new WrapperedComboBox(
				wrapper, 
				new ArrayList(contents),
				key, 
				compareKey);
		if(object != null)
			comboBox.setSelectedItem(object);
		
		mainPanel.add(jLabel, BorderLayout.WEST);
		mainPanel.add(comboBox, BorderLayout.CENTER);

		final String okButton = LangModelGeneral.getString("Choose");
		final String cancelButton = LangModelGeneral.getString("Cancel");

		int result = JOptionPane.showOptionDialog(
				Environment.getActiveWindow(), 
				mainPanel,
				LangModelGeneral.getString("SelectElement"),
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE, 
				null,
				new Object[] { okButton, cancelButton }, 
				okButton);
		
		if (result == JOptionPane.OK_OPTION) {
			returnObject = comboBox.getSelectedItem();
		}
		
		return returnObject;
	}

	private WrapperedComboChooserDialog() {
		// empty
	}
}

