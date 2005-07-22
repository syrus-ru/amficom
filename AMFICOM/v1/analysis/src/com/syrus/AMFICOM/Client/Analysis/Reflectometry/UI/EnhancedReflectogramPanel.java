/*-
 * $Id: EnhancedReflectogramPanel.java,v 1.4 2005/07/22 06:56:49 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;

/**
 * Отрисовывает рефлектограмму и кучу всяких сопутствующих вещей
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/07/22 06:56:49 $
 * @module
 */
public class EnhancedReflectogramPanel extends ReflectogramPanel {
	public boolean draw_alarms = false;
	public boolean draw_min_trace_level = false;
	public boolean draw_noise_level = false;
	protected double noise_level = 28; // ???!
	protected ReflectogramMismatch[] alarms;

	public EnhancedReflectogramPanel(TraceEventsLayeredPanel panel, double[] y, double deltaX) {
		super(panel, y, deltaX);
	}

	public void updateAlarms (ReflectogramMismatch[] alarms1)
	{
		this.alarms = alarms1;
		if (alarms1 != null)
		{
			draw_alarms = true;
		} else
			draw_alarms = false;
	}

	protected void paint_specific(Graphics g) {
		super.paint_specific(g);
		if (showAll) {
			if (draw_alarms)
				paint_alarms(g);
		}
		if (draw_min_trace_level && isDraw_events())
		{
			paint_noise_level(g);
			paint_min_trace_level(g);
		} else if (draw_noise_level && isDraw_events())
			paint_noise_level(g);

	}

	protected void paint_noise_level(Graphics g)
	{
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE).darker());
		int h = (int)((noise_level - top) * scaleY - 1);
		g.drawLine(0, h, jw, h);
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
		g.drawString(LangModelAnalyse.getString("Noise level"), jw - 87, h - 1);
	}

	protected void paint_alarms(Graphics g)
	{
		if (alarms == null)
			return;

		g.setColor(Color.red);
		for (int j = 0; j < alarms.length; j++)
		{
			if ((alarms[j].getCoord() <= end) && (alarms[j].getCoord() >= start))
				for (int i = Math.max(0, alarms[j].getCoord() - start); i < Math.min (end, alarms[j].getEndCoord()) - start; i++)
				{
					g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY - 1),
					(int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY - 1));
				}
		}
	}

	protected boolean hasMinTraceLevel() {
		return Heap.hasMinTraceLevel();
	}
	protected int getMinTraceLevelCoord() {
		return value2coord(Heap.getMinTraceLevel());
	}

	protected void paint_min_trace_level(Graphics g)
	{
		if (! hasMinTraceLevel())
			return;
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_MIN_TRACE_LEVEL));
		g.drawLine(0, getMinTraceLevelCoord(), jw, getMinTraceLevelCoord());
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}
}
