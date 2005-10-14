/*
 * $Id: ScaledGraphPanelReport.java,v 1.1 2005/10/14 12:00:48 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Analysis.Report;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleGraphPanel;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.report.DataStorableElement;

public class ScaledGraphPanelReport {
	public static ImageRenderingComponent createReport(
			DataStorableElement element,
			SimpleGraphPanel sgPanel) {
		int elementWidth = element.getWidth();
		int elementHeight = element.getHeight();
		
		BufferedImage image = new BufferedImage(
				elementWidth,
				elementHeight,
				BufferedImage.TYPE_USHORT_565_RGB);
		Graphics imageGraphics = image.getGraphics();
		imageGraphics.setColor(Color.white);
		imageGraphics.fillRect(0,0,elementWidth,elementHeight);

		sgPanel.paintAll(imageGraphics);

		imageGraphics.setColor(Color.BLACK);
		imageGraphics.drawRect(0,0,elementWidth - 1,elementHeight - 1);

		ImageRenderingComponent result =
			new ImageRenderingComponent(element,image);
		result.setSize(elementWidth,elementHeight);
		return result;
	}
}
