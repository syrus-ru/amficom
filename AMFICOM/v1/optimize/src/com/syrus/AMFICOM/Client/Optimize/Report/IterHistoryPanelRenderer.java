package com.syrus.AMFICOM.Client.Optimize.Report;

import com.syrus.AMFICOM.Client.General.Report.RenderingObject;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
//import com.syrus.AMFICOM.Client.General.Report.AMTReportPanel;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

/**
 * <p>Title: </p>
 * <p>Description: Панель для отображения графиков, рефлектограмм итд из
 * модулей анализа/прогнозирования/...</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class IterHistoryPanelRenderer extends JPanel
{
	JPanel insidePanel = null;
	RenderingObject ro = null;

	public IterHistoryPanelRenderer(RenderingObject ro)
		throws CreateReportException
	{
		this.ro = ro;

		insidePanel = (JPanel)ro.getReportToRender().getReserve();

		if (insidePanel == null)
			throw new CreateReportException(
				ro.getReportToRender().getName(),
				CreateReportException.cantImplement);

		this.setPreferredSize(new Dimension(ro.width,ro.height));
	}

	public void paint (Graphics g)
	{
		super.paint(g);

		g.setColor(Color.white);
		g.fillRect(0,0,ro.width,ro.height);

    Dimension curIPSize = insidePanel.getPreferredSize();
    
    insidePanel.setPreferredSize(new Dimension(ro.width,ro.height));
    insidePanel.setSize(ro.width,ro.height);
    
		insidePanel.paint(g);
    
    insidePanel.setPreferredSize(curIPSize);
    insidePanel.setSize(curIPSize);    
	}
}