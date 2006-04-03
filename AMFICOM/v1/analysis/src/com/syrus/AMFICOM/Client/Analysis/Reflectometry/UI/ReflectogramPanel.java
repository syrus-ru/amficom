/*-
 * $Id: ReflectogramPanel.java,v 1.10 2006/04/03 13:51:49 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2006/04/03 13:51:49 $
 * @module
 */
public class ReflectogramPanel extends TraceEventsPanel {
	private ModelTrace mt = null; // модельная кривая

	/**
	 * создает полноценную панель с рефлектограммой - с сеткой координат
	 */
	public ReflectogramPanel(TraceEventsLayeredPanel panel,
			double[] y, double deltaX) {
		super(panel, y, deltaX, false);
	}

	/**
	 * создает панель с рефлектограммой без сетки координат
	 * @param arg должен быть true
	 */
	public ReflectogramPanel(TraceEventsLayeredPanel panel,
			String id, boolean arg) {
		super(panel,
				Heap.getAnyTraceByKey(id).getTraceData(),
				Heap.getAnyTraceByKey(id).getDeltaX(),
				true);
		if (arg == false)
			throw new UnsupportedOperationException();
		this.draw_scales = false;
		// draw_events = false; -- use this to see proginal trace color
		updEvents(id);
	}

	@Override
	protected void paint_specific(Graphics g) {
		super.paint_specific(g);
		if (this.showAll) {
			if (isDraw_modeled()) {
				paint_modeled_trace(g);
			}
		}
	}

	@Override
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
		if ((n1 <= this.end) && (n2 >= this.start))
		{
			int iFrom = Math.max(this.start, n1);
			int iTo = Math.min(this.end, n2);
			if (iTo - iFrom >= 0)
			{
				double[] vArr = mtr.getYArray(iFrom, iTo - iFrom + 1);
				drawYCurve(g, vArr, 0, iFrom, iTo - iFrom);
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
				updateRangeByYCurve(r, vArr, 0, iFrom, iTo - iFrom);
			}
		}
	}

	protected void paint_modeled_trace(Graphics g)
	{
		if (this.mt == null)
			return;
		if (isDraw_events()) {
			draw_eventized_curve(g, this.mt.getYArray(), false);
		} else {
			g.setColor(correctColor(color, false));
			drawModelCurve(g, this.mt, false);
		}
	}

	@Override
	protected boolean hasWeakTraceColors() {
		// Когда включено отображение модельной кривой, исходная рефлектограмма
		// будет отображаться блеклыми цветами
		return isDraw_modeled();
	}

	@Override
	protected boolean hasWeakBothColors() {
		// Когда включено отображение модельной кривой, исходная рефлектограмма
		// будет отображаться блеклыми цветами
		return isPale_secondary() && this.isSecondary;
	}

	@Override
	protected boolean isShowGraph() {
		return ((TraceEventsLayeredPanel)this.parent).graphsShowDesired();
	}
	@Override
	protected boolean isDraw_events() {
		return ((TraceEventsLayeredPanel)this.parent).eventsShowDesired();
	}
	protected boolean isDraw_modeled() {
		return ((TraceEventsLayeredPanel)this.parent).modelShowDesired();
	}
	protected boolean isPale_secondary() {
		return ((TraceEventsLayeredPanel)this.parent).paleSecondaryDesired();
	}
}
