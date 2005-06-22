/*
 * $Id: ImagesDialog.java,v 1.2 2005/06/22 13:20:07 krupenn Exp $
 *
 * Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.resource.BitmapImageResource;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $
 * @module commonclient_v1
 */
public class ImagesDialog {
	public static Object showImageDialog() {
		return showImageDialog(null);
	}

	public static BitmapImageResource showImageDialog(BitmapImageResource ir) {

		BitmapImageResource returnObject = null;
		
		final ImagesPanel imagesPanel = new ImagesPanel();
		if(ir != null)
			imagesPanel.setImageResource(ir);

		final JButton cancelButton = new JButton(LangModelGeneral.getString("Button.Cancel"));
		final JButton chooseButton = new JButton(LangModelGeneral.getString("Choose"));
		final JButton addButton = new JButton(LangModelGeneral.getString("add"));

		final JOptionPane optionPane = new JOptionPane(
				imagesPanel,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, 
				null, 
				new Object[] { chooseButton, addButton, cancelButton }); 

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagesPanel.showAddImageDialog();
			}
		});
		chooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPane.setValue(chooseButton);
				imagesPanel.deinit();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPane.setValue(cancelButton);
				imagesPanel.deinit();
			}
		});

		final JDialog dialog = optionPane.createDialog(
				Environment.getActiveWindow(), "Выбор изображения");
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = Math.min(screenDim.width / 2, 590);
		int height = Math.min(screenDim.height / 2, 400);
		dialog.setLocation(screenDim.width / 2 - width / 2, screenDim.height / 2 - height / 2);
		dialog.setSize(new Dimension(width, height));

		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.dispose();

		final Object selectedValue = optionPane.getValue();

		if (selectedValue == chooseButton) {
			returnObject = imagesPanel.getImageResource();
		}
		
		return returnObject;
	}

	private ImagesDialog() {
		// empty
	}
}
