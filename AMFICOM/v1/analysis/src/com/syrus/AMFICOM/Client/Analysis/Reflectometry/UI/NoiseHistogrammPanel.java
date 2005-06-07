package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.Graphics;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

// TEST VERSION!

public class NoiseHistogrammPanel extends ScaledGraphPanel {
	private int nBins = 400;

	private double downLimit;

	private double upLimit;

	private double[] histData;

	public NoiseHistogrammPanel(ResizableLayeredPanel panel) {
		super(panel, new double[] {}, 0.0); // XXX
		inversed_y = false;
		grid_shift_x = downLimit;
		select_by_mouse = true;

		updateHistogrammData();
		setDefaultScales();
	}

	protected void updColorModel() {
		super.updColorModel();
	}

	public void paint(Graphics g) {
		((Graphics2D) g).setStroke((Stroke)UIManager.get(AnalysisResourceKeys.STROKE_NOISE_HISTOGRAMM));
		traceColor = UIManager.getColor(AnalysisResourceKeys.COLOR_PRIMARY_TRACE);
		paint_trace(g);
		((Graphics2D) g).setStroke((Stroke)UIManager.get(AnalysisResourceKeys.STROKE_DEFAULT));
		paint_scales(g);
		paint_scale_digits(g);
	}

	/*
	protected void paint_trace(Graphics g) {
		Color c = UIManager.getColor(AnalysisResourceKeys.COLOR_PRIMARY_TRACE);
		g.setColor(c);

		for (int i = Math.max(0, -start); i < Math.min(end + 1, y.length) - start - 1; i++) {
			g.fillRect((int) (i * scaleX + 1), (int) ((maxY - y[i + start] - top)
					* scaleY - 1), (int) (scaleX + 1),
					(int) ((maxY - bottom) * scaleY + 1));
		}
//		traceColor = UIManager.getColor(AnalysisResourceKeys.COLOR_PRIMARY_TRACE);
//		super.paint_trace(g);
	}*/

	public void updateHistogrammData() {
		// get reflectogram
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs == null)
			return; // XXX: no data to process
		double[] y1 = bs.getTraceData();

		// get end of trace
		ModelTraceAndEvents mtae = Heap.getMTAEPrimary();
		int noiseStart;
		if (mtae == null || mtae.getNEvents() == 0)
			noiseStart = 0;
		else
			noiseStart = mtae.getSimpleEvent(mtae.getNEvents() - 1).getEnd();
		if (noiseStart >= y1.length)
			return; // XXX: no data to process

		// --- we will process y[noiseStart] .. y[y.length-1] ---

		// find scales
		downLimit = y1[ReflectogramMath.getArrayMinIndex(y1, noiseStart, y1.length - 1)];
		upLimit = y1[ReflectogramMath.getArrayMaxIndex(y1, noiseStart, y1.length - 1)];
		double yTop = y1[ReflectogramMath.getArrayMaxIndex(y1, 0, y1.length - 1)];
		double delta = yTop - Math.floor(yTop + 0.999);
		yTop -= delta;
		downLimit += delta;

		grid_shift_x = -yTop; // we treat abs max of trace data as 0.0 dB 
		Kx = (upLimit - downLimit) / nBins;
		Ky = 1;

		// calc histogram
		Histogramm histo = new Histogramm(downLimit, upLimit, nBins);
		histData = histo.init(y1, noiseStart, y1.length - 1);

		// convert histogram to cumulative
		for (int i = 1; i < histData.length; i++)
			histData[i] += histData[i - 1];

		// normalize
		double vMax = histData[histData.length - 1];
		for (int i = 0; i < histData.length; i++)
			histData[i] /= vMax;

		super.init(histData, 1.0); // XXX: 1.0 -- ?
		minY = 0;
	}
}
