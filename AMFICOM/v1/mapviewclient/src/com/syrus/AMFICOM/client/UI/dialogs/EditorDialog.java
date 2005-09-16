/**
 * $Id: EditorDialog.java,v 1.8 2005/09/16 14:53:32 krupenn Exp $
 * Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
import com.syrus.AMFICOM.client.resource.LangModelGeneral;

/**
 * @version $Revision: 1.8 $
 * @author $Author: krupenn $
 * @module commonclient_v1
 */
public class EditorDialog
{
	public static boolean showEditorDialog(
			String title,
			Object object,
			StorableObjectEditor editor) {
		
		if(editor != null)
			editor.setObject(object);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(editor.getGUI(), BorderLayout.CENTER);
		
		final String okButton = LangModelGeneral.getString("Button.OK"); //$NON-NLS-1$
		final String cancelButton = LangModelGeneral.getString("Button.Cancel"); //$NON-NLS-1$
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
