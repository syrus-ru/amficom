/*
 * $Id: UGOReport.java,v 1.2 2005/10/13 10:24:35 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client_.scheme.report;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.report.DataStorableElement;

public class UGOReport {
	public static ImageRenderingComponent createReport(
			DataStorableElement storableElement,
			VisualManager visualManager,
			Object dataObject) {
		StorableObjectEditor editor = visualManager.getAdditionalPropertiesPanel();
		editor.setObject(dataObject);
		JComponent component = editor.getGUI();
		
		BufferedImage image = new BufferedImage(
				storableElement.getWidth(),
				storableElement.getHeight(),
				BufferedImage.TYPE_USHORT_565_RGB);
		Graphics2D imageGraphics = (Graphics2D)image.getGraphics();
		
		double scaleX = (double)component.getWidth() / (double)storableElement.getWidth();
		double scaleY = (double)component.getHeight() / (double)storableElement.getHeight();		
		double scale = scaleX > scaleY ? scaleX : scaleY;
	
		imageGraphics.scale(scale,scale);
		
		component.paint(imageGraphics);
		
		ImageRenderingComponent renderingComponent =
			new ImageRenderingComponent(storableElement,image);
		renderingComponent.setSize(storableElement.getWidth(),storableElement.getHeight());
		renderingComponent.setPreferredSize(renderingComponent.getSize());		
		
		return renderingComponent;
	}
}
