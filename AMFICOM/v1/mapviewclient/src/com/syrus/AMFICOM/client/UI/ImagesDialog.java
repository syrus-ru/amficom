/*-
 * $$Id: ImagesDialog.java,v 1.9 2005/10/11 08:56:10 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/11 08:56:10 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ImagesDialog {
	public static Object showImageDialog() {
		return showImageDialog(null);
	}

	public static AbstractImageResource showImageDialog(AbstractImageResource ir) {

		AbstractImageResource returnObject = null;
		
		final ImagesPanel imagesPanel = new ImagesPanel();
		if(ir instanceof BitmapImageResource
				|| ir instanceof FileImageResource)
			imagesPanel.setImageResource(ir);

		final JButton cancelButton = new JButton(I18N.getString(MapEditorResourceKeys.BUTTON_CANCEL));
		final JButton chooseButton = new JButton(I18N.getString(MapEditorResourceKeys.BUTTON_CHOOSE));
		final JButton addButton = new JButton(I18N.getString(MapEditorResourceKeys.BUTTON_ADD));

		final JOptionPane optionPane = new JOptionPane(
				imagesPanel,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, 
				null, 
				new Object[] { chooseButton, /*addButton,*/ cancelButton }); 

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
				Environment.getActiveWindow(), I18N.getString(MapEditorResourceKeys.TITLE_IMAGES_DIALOG_IMAGE_SELECTION));
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
