/*
 * $Id: ReportTemplateRendererMouseListener.java,v 1.1 2005/09/01 14:21:40 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTemplateRenderer.MODE;
import com.syrus.AMFICOM.report.ImageStorableElement;

public class ReportTemplateRendererMouseListener implements MouseListener {

	private final ReportTemplateRenderer renderer;

	public ReportTemplateRendererMouseListener(ReportTemplateRenderer renderer){
		this.renderer = renderer;
	}
		
	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		if (this.renderer.commands_processing_mode.equals(MODE.CREATE_IMAGE)){
			ImageStorableElement element = new ImageStorableElement();
			
			
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
	
	private String getFileName() {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter bmpFilter = new ChoosableFileFilter(
				"bmp",
				"Export Save File");
		fileChooser.addChoosableFileFilter(bmpFilter);

		ChoosableFileFilter gifFilter = new ChoosableFileFilter(
				"gif",
				"Export Save File");
		fileChooser.addChoosableFileFilter(gifFilter);

		ChoosableFileFilter jpgFilter = new ChoosableFileFilter(
				"jpg",
				"Export Save File");
		fileChooser.addChoosableFileFilter(jpgFilter);

		ChoosableFileFilter pngFilter = new ChoosableFileFilter(
				"png",
				"Export Save File");
		fileChooser.addChoosableFileFilter(pngFilter);

		fileChooser.setDialogTitle("Выберите файл для чтения");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml") || fileName.endsWith(".esf")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}
}
