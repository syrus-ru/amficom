/*-
 * $$Id: EditorDialog.java,v 1.11 2005/10/11 08:56:11 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * @version $Revision: 1.11 $, $Date: 2005/10/11 08:56:11 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class EditorDialog {
	public static boolean showEditorDialog(
			String title,
			Object object,
			StorableObjectEditor editor) {
		
		if(editor != null)
			editor.setObject(object);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(editor.getGUI(), BorderLayout.CENTER);
		
		final String okButton = I18N.getString(MapEditorResourceKeys.BUTTON_OK);
		final String cancelButton = I18N.getString(MapEditorResourceKeys.BUTTON_CANCEL);
		final JOptionPane optionPane = new JOptionPane(
				mainPanel, 
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, 
				null, 
				new Object[] { okButton, cancelButton });

		final JDialog dialog = optionPane.createDialog(
				Environment.getActiveWindow(), title);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = Math.min(screenDim.width / 2, 590);
		int height = Math.min(screenDim.height / 2, 400);
		dialog.setLocation(screenDim.width / 2 - width / 2, screenDim.height / 2 - height / 2);
		dialog.setSize(new Dimension(width, height));

		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.dispose();

		final Object selectedValue = optionPane.getValue();

		if (selectedValue == okButton) {
			editor.commitChanges();
			return true;
		}
		return false;
	}
	
	private EditorDialog() {
		// empty
	}
}
