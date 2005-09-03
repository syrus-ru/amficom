/*
 * $Id: ReportTemplateRendererMouseListener.java,v 1.2 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.MODE;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.resource.IntDimension;

public class ReportTemplateRendererMouseListener implements MouseListener {

	private final ReportTemplateRenderer renderer;
	private final ApplicationContext applicationContext;	

	public ReportTemplateRendererMouseListener(
		ReportTemplateRenderer renderer,
		ApplicationContext aContext){
		this.renderer = renderer;
		this.applicationContext = aContext;
	}
		
	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		this.applicationContext.getDispatcher().firePropertyChange(
				new ComponentSelectionChangeEvent(
					this,
					null));
		this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));		
	}

	public void mouseReleased(MouseEvent e) {
		if (RendererMode.getMode().equals(MODE.CREATE_IMAGE)){
			createImageRenderingComponent(e.getX(), e.getY());
		}
		else if (RendererMode.getMode().equals(MODE.CREATE_LABEL)){
			createTextRenderingComponent(e.getX(), e.getY());
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
	
	private void createImageRenderingComponent(int x, int y){
		String imageFileName = this.getImageFileName();
		if (imageFileName == null)
			return;

		BufferedImage elementImage = null;
		boolean imageReadCorrectly = false;
		try {
			elementImage = ImageIO.read(new File(imageFileName));
			if (elementImage != null)
				imageReadCorrectly = true;
		} catch (IOException e1) {
		}
		
		if (!imageReadCorrectly) {
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				"Ошибка при чтении файла изображения.",
				"Ошибка",
				JOptionPane.ERROR_MESSAGE);
				
			this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.IMAGE_CREATION_CANCELED));
				
			return;
		}
					
		ImageStorableElement element = new ImageStorableElement();
		element.setLocation(x,y);
		element.setSize(new IntDimension(ImageStorableElement.DEFAULT_IMAGE_SIZE));
		element.setImage(elementImage);
		 
		//TODO Где создавать компонент?
		ImageRenderingComponent component = new ImageRenderingComponent(element,element.getImage());
//			this.renderer.componentsContainer.addRenderingComponent(textComponent);
		this.renderer.add(component);
		component.setLocation(element.getX(),element.getY());
		component.setSize(element.getWidth(),element.getHeight());
		component.addMouseListener(DRIComponentMouseListener.getInstance());
		component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
		
		this.applicationContext.getDispatcher().firePropertyChange(
			new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
		this.applicationContext.getDispatcher().firePropertyChange(
			new ReportFlagEvent(this,ReportFlagEvent.IMAGE_CREATION_CANCELED));			
	}

	private void createTextRenderingComponent(int x, int y){
		AttachedTextStorableElement element = new AttachedTextStorableElement();
		element.setLocation(x,y);
		element.setSize(new IntDimension(AttachedTextStorableElement.MINIMUM_COMPONENT_SIZE));
		 
		//TODO Где создавать компонент?
		AttachedTextComponent component = new AttachedTextComponent(element);
//			this.renderer.componentsContainer.addRenderingComponent(textComponent);
		this.renderer.add(component);
		component.setLocation(element.getX(),element.getY());
		component.setSize(element.getWidth(),element.getHeight());
		component.setBorder(AttachedTextComponent.DEFAULT_BORDER);
		
		component.addMouseListener(ATComponentMouseListener.getInstance());
		component.addMouseMotionListener(ATComponentMouseMotionListener.getInstance());
		component.addKeyListener(ATComponentKeyListener.getInstance());
		this.applicationContext.getDispatcher().addPropertyChangeListener(
				ReportEvent.TYPE,
				new ATComponentPropertyChangeListener(
						component,
						this.applicationContext));
		
		this.applicationContext.getDispatcher().firePropertyChange(
			new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
		this.applicationContext.getDispatcher().firePropertyChange(
			new ReportFlagEvent(this,ReportFlagEvent.LABEL_CREATION_CANCELED));			
	}
	
	private String getImageFileName() {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		String[] filters = {"bmp","gif","jpg","png"};
		ChoosableFileFilter bmpFilter = new ChoosableFileFilter(
				filters,
				"Image file formats");
		fileChooser.addChoosableFileFilter(bmpFilter);

		fileChooser.setDialogTitle("Выберите файл для чтения");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!	(	fileName.endsWith(".bmp")
					 || fileName.endsWith(".gif")
					 || fileName.endsWith(".jpg")
					 || fileName.endsWith(".png")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}
}
