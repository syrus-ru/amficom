package com.syrus.AMFICOM.Client.Analysis.Report;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScaledGraphPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalysisPanel;

import com.syrus.AMFICOM.Client.General.Report.RenderingObject;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.AMTReportPanel;


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

public class EvaluationGraphPanel extends JPanel
{
	ScaledGraphPanel sgPanel = null;
	RenderingObject ro = null;

	public EvaluationGraphPanel(RenderingObject ro)
		throws CreateReportException
	{
		this.ro = ro;

		AMTReportPanel rPanel = (AMTReportPanel) ro.getReportToRender().getReserve();
		this.sgPanel = (ScaledGraphPanel)rPanel.panel;

		if (sgPanel == null)
			throw new CreateReportException(
				ro.getReportToRender().getName(),
				CreateReportException.cantImplement);

		this.setPreferredSize(new Dimension(ro.width,ro.height));
	}

	public void paint (Graphics g)
	{
		super.paint(g);

		Dimension curSgpSize = sgPanel.getSize();
		boolean analShowMarkers = false;

		if (sgPanel instanceof AnalysisPanel)
		{
			analShowMarkers = ((AnalysisPanel) sgPanel).show_markers;
			((AnalysisPanel) sgPanel).show_markers = false;
		}

		g.setColor(Color.white);
		g.fillRect(0,0,ro.width,ro.height);

		Dimension curPrefSize = getPreferredSize();
		curPrefSize.width -= 10;
		curPrefSize.height -= 10;

		sgPanel.setGraphSize(curPrefSize);

		curPrefSize.width += 10;
		curPrefSize.height += 10;

		sgPanel.paint(g);

		sgPanel.setGraphSize(curSgpSize);

		if (sgPanel instanceof AnalysisPanel)
			((AnalysisPanel)sgPanel).show_markers = analShowMarkers;
//		sgPanel.setBackground(curBackColor);
	}
}