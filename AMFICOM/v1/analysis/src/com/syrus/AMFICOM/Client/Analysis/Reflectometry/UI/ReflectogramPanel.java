/*-
 * $Id: ReflectogramPanel.java,v 1.1 2005/07/08 10:08:37 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Graphics;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceRange;

/**
 * Отрисовывает рефлектограмму (исходную и модельную, с учетом расцветки)
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/07/08 10:08:37 $
 * @module
 */
public class ReflectogramPanel extends TraceEventsPanel {
	public boolean draw_modeled = false;
	private ModelTrace mt = null; // модельная кривая

	/**
	 * создает полноценную панель с рефлектограммой - с сеткой координат
	 */
	public ReflectogramPanel(ResizableLayeredPanel panel,
			double[] y, double deltaX) {
		super(panel, y, deltaX);
	}

	/**
	 * создает панель с рефлектограммой без сетки координат
	 * @param arg должен быть true
	 */
	public ReflectogramPanel(ResizableLayeredPanel panel,
			String id, boolean arg) {
		super(panel,
				Heap.getAnyTamByKey(id).getTraceData(),
				Heap.getAnyTamByKey(id).getDeltaX());
		if (arg == false)
			throw new UnsupportedOperationException();
		draw_scales = false;
		// draw_events = false; -- use this to see proginal trace color
		updEvents(id);
	}

	public void setGraphModelShowMode(boolean draw_graphs,
			boolean draw_modeled) {
		this.showGraph = draw_graphs;
		this.draw_modeled = draw_modeled;
	}

	protected void paint_specific(Graphics g) {
		super.paint_specific(g);
		if (showAll) {
			if (draw_modeled) {
				paint_modeled_trace(g);
			}
		}
	}

	public void updEvents(String id) {
		super.updEvents(id);
		ModelTraceAndEvents mtae = Heap.getAnyMTAE(id);
        this.mt = mtae != null ? mtae.getModelTrace() : null;
	}

    /**
     * Draw model curve using current graphics plotting settings.
     * @param g graphics to plot (not null)
     * @param mtr trace and range to plot
     * @param avoidLastPoint true to draw [sre.begin .. sre.end-1];
     *   false to draw [sre.begin .. sre.end].
     */
    protected void drawModelCurve(Graphics g, ModelTraceRange mtr,
            boolean avoidLastPoint)
    {
        int n1 = mtr.getBegin();
        int n2 = mtr.getEnd() - (avoidLastPoint ? 1 : 0);
        if ((n1 <= end) && (n2 >= start))
        {
            int iFrom = Math.max(start, n1);
            int iTo = Math.min(end, n2);
            if (iTo - iFrom >= 0)
            {
                double[] vArr = mtr.getYArray(iFrom, iTo - iFrom + 1);
                draw_y_curve(g, vArr, 0, iFrom - start, iTo - iFrom);
            }
        }
    }

    /**
     * Draw model curve using current graphics plotting settings.
     * @param g graphics to plot, or null if no plotting is required
     * @param r GraphRange to be updated, or null if update not requied
     * @param mtr trace and range to plot
     * @param avoidLastPoint true to draw [sre.begin .. sre.end-1];
     *   false to draw [sre.begin .. sre.end].
     */
    protected void drawModelCurve(Graphics g, GraphRange r, ModelTraceRange mtr,
            boolean avoidLastPoint)
    {
        if (g != null)
            drawModelCurve(g, mtr, avoidLastPoint);
        if (r != null) {
            int n1 = mtr.getBegin();
            int n2 = mtr.getEnd() - (avoidLastPoint ? 1 : 0);
            int iFrom = n1;
            int iTo = n2;
            if (iTo - iFrom >= 0)
            {
                double[] vArr = mtr.getYArray(iFrom, iTo - iFrom + 1);
                update_range_by_y_curve(r, vArr, 0, iFrom - start, iTo - iFrom);
            }
        }
    }

    protected void paint_modeled_trace(Graphics g)
	{
		if (mt == null)
			return;
		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_MODELED));
		drawModelCurve(g, mt, false);
	}
}
