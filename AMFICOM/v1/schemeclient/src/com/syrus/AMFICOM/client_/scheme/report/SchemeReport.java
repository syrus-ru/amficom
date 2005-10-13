/*
 * $Id: SchemeReport.java,v 1.2 2005/10/13 10:24:35 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client_.scheme.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.scheme.Scheme;

public class SchemeReport {
	private static final int SCHEME_FIELD = 10;

	public static ImageRenderingComponent createReport (
			Scheme scheme,
			StorableElement element,
			ApplicationContext aContext) {
		int elementWidth = element.getWidth();
		int elementHeight = element.getHeight();		

		int imageWidth = elementWidth - SCHEME_FIELD * 2;
		int imageHeight = elementHeight - SCHEME_FIELD * 2;		

		UgoTabbedPane ugoTabbedPane = new UgoTabbedPane(aContext);
		UgoPanel ugoPanel = ugoTabbedPane.getCurrentPanel();

		SchemeGraph schemeGraph = ugoPanel.getGraph();
		SchemeActions.openSchemeImageResource(
				schemeGraph,
				scheme.getSchemeCell(),
				false);

		Rectangle schemeCellBounds = schemeGraph.getCellBounds(schemeGraph.getRoots());
		double newScaleX = (double)(imageWidth) / (double)(schemeCellBounds.width);
		double newScaleY = (double)(imageHeight) / (double)(schemeCellBounds.height);		
		double newScale = newScaleX < newScaleY ? newScaleX : newScaleY;
		schemeGraph.setScale(newScale);
		
		Dimension bigImageSize = new Dimension(
			imageWidth + 2 * schemeCellBounds.x,
			imageHeight + 2 * schemeCellBounds.y);
			
		BufferedImage bigImage = new BufferedImage(
				bigImageSize.width,
				bigImageSize.height,
				BufferedImage.TYPE_USHORT_565_RGB);
			
		Graphics bigImageGraphics = bigImage.getGraphics();
		bigImageGraphics.setColor(Color.white);
		bigImageGraphics.fillRect(
			0,
			0,
			bigImageSize.width,
			bigImageSize.height);
		
		ugoTabbedPane.setSize(new Dimension(imageWidth,imageHeight));
		ugoTabbedPane.setPreferredSize(ugoTabbedPane.getSize());
		ugoTabbedPane.setToolBarVisible(false);

		//TODO А как без JFrame?		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame();
		frame.setLocation(screenSize.width,screenSize.height);
		frame.setSize(bigImageSize);
		frame.setPreferredSize(bigImageSize);
		frame.setLayout(new BorderLayout());
		frame.add(ugoTabbedPane,BorderLayout.CENTER);
		frame.setVisible(true);
		
		ugoTabbedPane.paint(bigImageGraphics);
		
		BufferedImage image = new BufferedImage(
			elementWidth,
			elementHeight,
			BufferedImage.TYPE_USHORT_565_RGB);

		Graphics imageGraphics = image.getGraphics();
		imageGraphics.setColor(Color.white);
		imageGraphics.fillRect(
			0,
			0,
			elementWidth,
			elementHeight);
			
		imageGraphics.setColor(Color.black);
		imageGraphics.drawRect(
			0,
			0,
			elementWidth - 1,
			elementHeight - 1);

		imageGraphics.drawImage(
			bigImage,
			(int)(-schemeCellBounds.x * newScale) + SCHEME_FIELD,
			(int)(-schemeCellBounds.y * newScale + /*(imageHeight - schemeCellBounds.height) / 2 +*/ SCHEME_FIELD),
			ugoTabbedPane);
		frame.dispose();


//----------------------------------------------------------------		
//		UgoTabbedPane ugoTabbedPane = new UgoTabbedPane(aContext);
//		UgoPanel ugoPanel = ugoTabbedPane.getCurrentPanel();
//
//		SchemeGraph schemeGraph = ugoPanel.getGraph();
//		SchemeActions.openSchemeImageResource(
//				schemeGraph,
//				scheme.getSchemeCell(),
//				false);
//
//		Object[] cells = schemeGraph.getRoots();
//		Rectangle bounds = schemeGraph.getCellBounds(cells);
//		
//		double newScale = (double)elementWidth / (double)(bounds.width);		
//		schemeGraph.setScale(newScale);
//		
//		GraphActions.move(schemeGraph,cells,new Point(-bounds.x,-bounds.y),false);
//		schemeGraph.setActualSize(new Dimension(
//				(int)(bounds.width * newScale),
//				(int)(bounds.height * newScale)));
//	
//		BufferedImage image = new BufferedImage(
//				elementWidth,
//				elementHeight,
//				BufferedImage.TYPE_USHORT_565_RGB);
//			
//		Graphics imageGraphics = image.getGraphics();
//		imageGraphics.setColor(Color.white);
//		imageGraphics.fillRect(0,0,elementWidth,elementHeight);
//		
//		imageGraphics.setClip(
//			(int)(bounds.x * newScale),
//			(int)(bounds.y * newScale),
//			(int)(bounds.width * newScale),
//			(int)(bounds.height * newScale));
//		schemeGraph.getUI().paint(imageGraphics,schemeGraph);
//-----------------------------------------------------------

		ImageRenderingComponent renderingComponent =
			new ImageRenderingComponent(element,image);
		renderingComponent.setSize(
			elementWidth,
			elementHeight);
		
		return renderingComponent;
	}
}
