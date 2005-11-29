package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.Histogramm;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

/**
 * Окно "Гистограмма области шумов"
 * @author $Author: saa $
 * @version $Revision: 1.12 $, $Date: 2005/11/29 08:27:38 $
 * @module analysis
 */
public class NoiseHistogrammPanel extends ScaledGraphPanel {
	private int nBins = 400;

	private double downLimit;

	private double upLimit;

	private double[] histData;

	public NoiseHistogrammPanel(ResizableLayeredPanel panel) {
		super(panel, new double[] {}, 0.0); // XXX
		this.inversed_y = false;
		this.grid_shift_x = this.downLimit;
		this.select_by_mouse = true;

		updateHistogrammData();
		setDefaultScales();
	}

	@Override
	protected void updColorModel() {
		super.updColorModel();
	}

	@Override
	public void paint(Graphics g) {
		paint_scales(g);
		if (this.showAll) {
			((Graphics2D) g).setStroke((Stroke)UIManager.get(AnalysisResourceKeys.STROKE_NOISE_HISTOGRAMM));
			this.color = UIManager.getColor(AnalysisResourceKeys.COLOR_PRIMARY_TRACE);
			paint_trace(g);
			((Graphics2D) g).setStroke((Stroke)UIManager.get(AnalysisResourceKeys.STROKE_DEFAULT));
		}
		paint_scale_digits(g);
	}

	public void updateHistogrammData() {
		// get reflectogram
		PFTrace pf = Heap.getPFTracePrimary();
		if (pf == null)
			return; // XXX: no data to process
		double[] y1 = pf.getFilteredTraceClone();

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
		this.downLimit = y1[ReflectogramMath.getArrayMinIndex(y1, noiseStart, y1.length - 1)];
		this.upLimit = y1[ReflectogramMath.getArrayMaxIndex(y1, noiseStart, y1.length - 1)];
		double yTop = y1[ReflectogramMath.getArrayMaxIndex(y1, 0, y1.length - 1)];
		double delta = yTop - Math.floor(yTop + 0.999);
		yTop -= delta;
		this.downLimit += delta;

		this.grid_shift_x = -yTop; // we treat abs max of trace data as 0.0 dB 
		this.Kx = (this.upLimit - this.downLimit) / this.nBins;
		this.Ky = 1;

		// calc histogram
		Histogramm histo = new Histogramm(this.downLimit, this.upLimit, this.nBins);
		this.histData = histo.init(y1, noiseStart, y1.length - 1);

		// convert histogram to cumulative
		for (int i = 1; i < this.histData.length; i++) {
			this.histData[i] += this.histData[i - 1];
		}

		// normalize
		double vMax = this.histData[this.histData.length - 1];
		for (int i = 0; i < this.histData.length; i++) {
			this.histData[i] /= vMax;
		}

		super.init(this.histData, 1.0); // XXX: 1.0 -- ?
		this.minY = 0;
	}
}
