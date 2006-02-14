/*-
 * $$Id: WrapperedComboChooserDialog.java,v 1.10 2006/02/14 10:20:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class WrapperedComboChooserDialog {
	public static Object showChooserDialog(Collection contents) {
		return showNameChooserDialog(
				contents, 
				null, 
				NamedObjectWrapper.getInstance(),
				NamedObjectWrapper.KEY_NAME,
				NamedObjectWrapper.KEY_NAME);
	}

	public static Object showNameChooserDialog(
			Collection contents,
			Object object) {
		return showNameChooserDialog(
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
		return showNameChooserDialog(contents, null, wrapper, key, compareKey);
	}

		public static Object showNameChooserDialog(
			Collection contents,
			Object object,
			Wrapper wrapper,
			String key,
			String compareKey) {

		Object returnObject = null;
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel jLabel = new JLabel(I18N.getString("Element")); //$NON-NLS-1$
		WrapperedComboBox comboBox = new WrapperedComboBox(
				wrapper, 
				key, 
				compareKey);
		
		comboBox.addElements(contents);
		((WrapperedListModel)comboBox.getModel()).sort();
		
		if(object != null)
			comboBox.setSelectedItem(object);
		
		mainPanel.add(jLabel, BorderLayout.WEST);
		mainPanel.add(comboBox, BorderLayout.CENTER);

		final String okButton = I18N.getString(MapEditorResourceKeys.BUTTON_CHOOSE);
		final String cancelButton = I18N.getString(MapEditorResourceKeys.BUTTON_CANCEL);

		int result = JOptionPane.showOptionDialog(
				Environment.getActiveWindow(), 
				mainPanel,
				I18N.getString(MapEditorResourceKeys.TITLE_SELECT_ELEMENT),
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

