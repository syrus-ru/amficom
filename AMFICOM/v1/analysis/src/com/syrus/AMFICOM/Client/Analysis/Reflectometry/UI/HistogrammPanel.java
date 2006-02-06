package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.Formatter;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.Histogramm;
import com.syrus.AMFICOM.analysis.dadara.InvalidAnalysisParametersException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.Wavelet;

/**
 * Окно "Гистограмма рабочей области"
 * (с гауссовой фитировкой и линией порога чувствительности распознавания)
 * @author $Author: saa $
 * @version $Revision: 1.50 $, $Date: 2005/11/29 08:27:38 $
 * @module analysis
 */
public class HistogrammPanel extends ScaledGraphPanel
{
	// Note about SINX or ABSXSINX
	// ABSXSINX is harder for computations than TYPE_SINX, and
	// seem to produce results of the same as TYPE_SINX.
	// So I prefer to use SINX //saa

	//private static int waveletType = Wavelet.TYPE_ABSXSINX;
	private static int waveletType = Wavelet.TYPE_SINX;

	private static final float GAUSS_W = 1.6f; // width of gaussian in pixels
	private static final Stroke GAUSS_STROKE = new BasicStroke(GAUSS_W);
	private static final Stroke THRESHOLD_STROKE = new BasicStroke(1);

	private double level;

	// transfer coefficient dB/km to dB 
	private double alpha = 0.0;

	private int gaussI0 = 0; // при отображении массив gauss будет смещаться на эту величину
	private double[] derivative;
	private double[] gauss;
	private double[] threshold;

	private Color gaussColor;
	private Color thresholdColor;

	private boolean moveLevel = false;

	public HistogrammPanel(ResizableLayeredPanel panel, double[] y, double deltaX)
	{
		super(panel, y, deltaX);
		inversed_y = false;

		init();

		ModelTraceAndEvents mtae = Heap.getMTAEPrimary();
		if (mtae != null)
			updateHistogrammData(0, mtae.getModelTrace().getLength() - 1);
		else
			updateHistogrammData(0, y.length / 2);

		this.level = getHeapLevel();
	}

	public void init()
	{
		int event_size = ReflectogramMath.getReflectiveEventSize(y, 0.1);
		double normMx = Wavelet.getNormMx(waveletType, event_size);
		double normS = Wavelet.getNormStep(waveletType, event_size);
		derivative = Wavelet.makeTransform(
				waveletType, event_size, y, 0, y.length - 1, normMx);

		for (int i = 0; i < derivative.length; i++)
			derivative[i] = -derivative[i];

		//Normalizing of the derivative to the dimension db/km
		double tmp = 1000./deltaX;
		for (int i = 0; i < derivative.length; i++)
			derivative[i] = derivative[i]*tmp;

		this.alpha = normMx / normS / tmp;
	}

	@Override
	protected void updColorModel() {
		super.updColorModel();

		gaussColor = UIManager.getColor(AnalysisResourceKeys.COLOR_CONNECTOR);
		thresholdColor = UIManager.getColor(AnalysisResourceKeys.COLOR_END);
	}

	@Override
	public void paint(Graphics g) {
		paint_trace(g);
		paint_scales(g);
		if (showAll) {
			paint_gauss(g);
			paint_threshold(g);
		}
		paint_scale_digits(g);
		paint_level(g);
	}

	@Override
	protected void paint_trace(Graphics g) {
		g.setColor(color);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, y.length) - start - 1; i++)
		{
			if (y[i+start] > level)
				g.setColor(color);
			else
				g.setColor(Color.gray);

			g.fillRect((int)(i * scaleX + 1), (int)((maxY - y[i+start] - top) * scaleY - 1),
								 (int)(scaleX+1), (int)((maxY - bottom) * scaleY + 1));
		}
	}

	protected void paint_gauss(Graphics g)
	{
		g.setColor(gaussColor);
		((Graphics2D) g).setStroke(GAUSS_STROKE);

		for (int i= Math.max(0, gaussI0-start); i < Math.min (end + 1, gauss.length + gaussI0) - start - 1; i++)
			g.drawLine((int)(i*scaleX+1), (int)((maxY - gauss[i+start - gaussI0] - top) * scaleY - 1),
								 (int)((i+1)*scaleX+1), (int)((maxY - gauss[i+start+1 - gaussI0] - top) * scaleY - 1));

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_threshold(Graphics g)
	{
		g.setColor(thresholdColor);
		((Graphics2D) g).setStroke(THRESHOLD_STROKE);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, threshold.length) - start - 1; i++)
			g.drawLine((int)(i*scaleX+1), (int)((maxY - threshold[i+start] - top) * scaleY - 1),
								 (int)((i+1)*scaleX+1), (int)((maxY - threshold[i+start+1] - top) * scaleY - 1));

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_level(Graphics g)
	{
		g.setColor(Color.RED);
		int jw = getWidth();

		g.drawLine(10, value2coord(this.level),
				 jw - 10, value2coord(this.level));

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE_DIGITS));
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format("%2.0f %% \u2014 %.4f %s", // \u2014 - тире
				new Double(level * 100.0),
				new Double(level2thresh(this.level)),
				LangModelAnalyse.getString("dB"));
		String str = sb.toString();
		// учитываем, что на оси абсцисс есть подписи, с которыми не надо пересекаться
		g.drawString(str,
				jw - g.getFontMetrics().stringWidth(str) - 6,
				Math.min((int)((maxY - level - top) * scaleY - 4),
						getHeight() - getFontMetrics(getFont()).getHeight() - 3));
	}

//	// use this method to paint scale digits for krivulka in dB  
//	@Override
//	protected void paint_scale_digits(Graphics g) {
//		super.paint_scale_digits(g);
//				
//		//int jh = getHeight();
//		int jw = getWidth();
//
//		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE_DIGITS));
//
//		double m = calcNodeDistance (cell_w / scaleX * Kx); // единиц на одно деление
//		double delta =	m * scaleX / Kx; // число экранных точек на одно деление
//		int x = (int)(((int)(start * Kx / m) ) * delta - start * scaleX); // сдвиг относительно начала
//
//		for (int i = 0; i < jw / delta + 1; i++)
//			g.drawString(String.valueOf(MathRef.round_2 ((i + (int)(start * Kx / m) ) * m   * alpha * 1000d)), (int)(i * delta + x - 12), 10);
//	}

	@Override
	protected void this_mousePressed(MouseEvent e) {
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (SwingUtilities.isRightMouseButton(e) ||
				Math.abs(currpos.y-(int)((maxY - level - top)*scaleY)) < MOUSE_COUPLING) {
			moveLevel = true;
			updateLevel(coord2value(currpos.y));
			setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			parent.repaint();
			return;
		}

		super.this_mousePressed(e);
	}

	@Override
	protected void this_mouseDragged(MouseEvent e) {
		if (moveLevel) {
			upd_currpos(e);

			updateLevel(coord2value(currpos.y));
			parent.repaint();
			return;
		}
		super.this_mouseDragged(e);
	}

	@Override
	protected void this_mouseReleased(MouseEvent e) {
		if (moveLevel) {
			moveLevel = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			parent.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}

	private double level2thresh(double level1) {
		double thresh = 0.0;
		for (int i = 0; i < threshold.length; i++) {
			if (threshold[i] <= level1) {
				thresh = i * Kx * alpha;
				break;
			}
		}
		return thresh;
	}

	private double thresh2level(double thresh) {
		double fIndex = Math.round(thresh / alpha / Kx);
		if (fIndex < 0)
			return threshold[0];
		else if (fIndex > threshold.length - 1)
			return threshold[threshold.length - 1];
		else
		return threshold[(int)fIndex];
	}
	private double getHeapThreshold() {
		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		return ap.getSentitivity();
	}

	private double getHeapLevel() {
		return thresh2level(getHeapThreshold());
	}

	/**
	 * пытается установить новое значение уровня
	 */
	private void updateLevel(double v) {
		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		try {
			// try to change sensitivity
			ap.setSensitivity(level2thresh(v), true);
			// success, apply changes
			// NB: мы *должны* будем получить и обработать уведомление
			Heap.notifyAnalysisParametersUpdated(); // FIXME: implement other senders and subscribers
		} catch (InvalidAnalysisParametersException e) {
			// just ignore
		}
	}

	public void updAnalysisParameters() {
		double level1 = getHeapLevel();
		if (level1 == this.level)
			return;
		this.level = level1;
		parent.repaint();
	}

	public void updateHistogrammData(int start1, int end1)
	{
		int nBins = 4000;
		// '1' for total histogram
		double downLimit1 = -4;
		double upLimit1 = 8;
		// '2' for gauss histogram
		double downLimit2 = -0.1;
		double upLimit2 = 0.5;

		Histogramm histo1 = new Histogramm(downLimit1, upLimit1, nBins);
		double[] y1 = histo1.init(derivative, start1, end1);
		int i0 = (int)Math.round((downLimit2 - downLimit1) / (upLimit1 - downLimit1) * nBins);
		int i1 = (int)Math.round((upLimit2 - downLimit1) / (upLimit1 - downLimit1) * nBins);
		gaussI0 = i0;
		double[] y2 = new double[i1 - i0];
		System.arraycopy(y1, i0, y2, 0, i1 - i0);
		int maxIndex2 = ReflectogramMath.getArrayMaxIndex(y2, 0, y2.length - 1);
		double yMax = y2[maxIndex2];

		grid_shift_x = downLimit1;
		Kx = (upLimit1 - downLimit1) / nBins;
		Ky = 1;

		init(y1, deltaX);
//        long t0 = System.currentTimeMillis();
		double[] fitResultingParams = new double[3];
		gauss = CoreAnalysisManager.calcGaussian(y2, maxIndex2, fitResultingParams); // XXX: takes about 98% of updateHistogrammData execution time
//        long t1 = System.currentTimeMillis();
		int maxIndexFitted = (int)Math.round(fitResultingParams[0]);
		threshold = CoreAnalysisManager.calcThresholdCurve(y1,
				maxIndexFitted + i0);
//        long t2 = System.currentTimeMillis();
//        System.out.println("dt hist: gauss " + (t1-t0) + ", thr " + (t2-t1));
		for (int i = 0; i < y1.length; i++)
			y1[i] /= yMax;
		for (int i = 0; i < y2.length; i++)
			gauss[i] /= yMax;
		maxY = 1;
		minY = 0;
	}
}
