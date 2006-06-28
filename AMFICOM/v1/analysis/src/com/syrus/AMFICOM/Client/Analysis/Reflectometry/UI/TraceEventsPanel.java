package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

public class TraceEventsPanel extends ScaledGraphPanel
{
	protected SimpleReflectogramEvent[] sevents;

	protected boolean isSecondary;
	protected Color connectColor;
	protected Color deadzoneColor;
	protected Color weldColor;
	protected Color linezoneColor;
	protected Color nonidColor;
	protected Color noiseColor;
	protected Color endColor;

	// XXX: mark as deprecated?
	public TraceEventsPanel(ResizableLayeredPanel panel,
			double[] y,
			double deltaX) {
		this(panel, y, deltaX, false);
	}

	public TraceEventsPanel(ResizableLayeredPanel panel,
			double[] y,
			double deltaX,
			boolean isSecondary) {
		super (panel, y, deltaX);

		this.isSecondary = isSecondary;

		Kx = deltaX / 1000d;
		Ky = 1;
	}

	public void updEvents(String id)
	{
		sevents = Heap.getAnyMTAE(id) != null
			? Heap.getAnyMTAE(id).getSimpleEvents()
			: null; // XXX: наверное, лучше инкапсулировать sevents, и обращаться к Heap каждый раз, а не отслеживать изменения
	}

	@Override
	protected void updColorModel()
	{
		super.updColorModel();

		connectColor = UIManager.getColor(AnalysisResourceKeys.COLOR_CONNECTOR);
		deadzoneColor = UIManager.getColor(AnalysisResourceKeys.COLOR_DEADZONE);
		weldColor =  UIManager.getColor(AnalysisResourceKeys.COLOR_WELD);
		linezoneColor = UIManager.getColor(AnalysisResourceKeys.COLOR_LINEZONE);
		nonidColor = UIManager.getColor(AnalysisResourceKeys.COLOR_NON_ID);
		endColor = UIManager.getColor(AnalysisResourceKeys.COLOR_END);
		noiseColor = UIManager.getColor(AnalysisResourceKeys.COLOR_NOISE);
	}

	@Override
	public void paint(Graphics g)
	{
		paint_scales(g);
		paint_specific(g);
		paint_scale_digits(g);
	}

	protected void paint_specific(Graphics g) {
		if (showAll) {
			if (isDraw_events()) {
				paint_events(g);
			} else {
				paint_trace(g);
			}
		}
	}

	protected void paint_events(Graphics g)
	{
		if (isShowGraph()) {
			draw_eventized_curve(g, y, true);
		}
	}

	protected Color getColorByEventType(int type) {
		Color color = null;
		switch (type) {
			case SimpleReflectogramEvent.LINEAR:   color = linezoneColor; break;
			case SimpleReflectogramEvent.DEADZONE: color = deadzoneColor; break;
			case SimpleReflectogramEvent.GAIN:     color = weldColor; break;
			case SimpleReflectogramEvent.LOSS:     color = weldColor; break;
			case SimpleReflectogramEvent.CONNECTOR: color = connectColor; break;
			case SimpleReflectogramEvent.ENDOFTRACE: color = endColor; break;
			case SimpleReflectogramEvent.NOTIDENTIFIED: color = nonidColor; break;
			default: color = noiseColor;
		}
		return color;
	}

	protected void draw_eventized_curve(Graphics g, double[] y1, boolean isTraceColor)
	{
		if (sevents == null)
		{
			paint_trace(g);
			return;
		}
		draw_y_curve_with_events(g, sevents, y1, 0, 0, y1.length - 1, isTraceColor);
	}

	private void clipped_draw(Graphics g, double[] y1, int i0, int x0, int N,
			int rB, int rE, Color baseColor, boolean isTraceColor) {
		if (rB < x0) {
			rB = x0;
		}
		if (rB < start) {
			rB = start;
		}
		if (rE > x0 + N) {
			rE = x0 + N;
		}
		if (rE > end) {
			rE = end;
		}
		if (rB > rE) {
			return;
		}
		g.setColor(correctColor(baseColor, isTraceColor));
		drawYCurve(g, y1, rB + i0 - x0, rB, rE - rB);
	}

	// performs clipping
	// y1[] array length is N+1
	protected void draw_y_curve_with_events(Graphics g,
			SimpleReflectogramEvent[] se,
			double[] y1, int i0, int x0, int N, boolean isTraceColor) {
		for (int j = 0; j < se.length; j++) {
			int eB = se[j].getBegin();
			int eE = se[j].getEnd();
			Color color = getColorByEventType(se[j].getEventType());
			clipped_draw(g, y1, i0, x0, N, eB, eE, color, isTraceColor);
		}
		int lastPoint = se.length > 0 ? se[se.length - 1].getEnd() : 0;
		clipped_draw(g, y1, i0, x0, N, lastPoint, N, noiseColor, isTraceColor);
	}

	protected boolean isDraw_events() {
		return true;
	}
}
