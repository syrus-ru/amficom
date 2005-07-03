package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.Histogramm;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.Wavelet;

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

    private double level = 0.2;

    // transfer coefficient dB/km to dB 
	private double alpha = 0.0;

    private int gaussI0 = 0; // при отображении массив gauss будет смещаться на эту величину
	private double[] derivative;
	private double[] gauss;
	private double[] threshold;

	private Color gaussColor;
	private Color thresholdColor;

	private boolean moveLevel = false;
    private boolean movedHere = false;

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

	protected void updColorModel()
	{
		super.updColorModel();

		gaussColor = UIManager.getColor(AnalysisResourceKeys.COLOR_CONNECTOR);
		thresholdColor = UIManager.getColor(AnalysisResourceKeys.COLOR_END);
	}

	public void paint(Graphics g)
	{
		paint_trace(g);
		paint_scales(g);
		paint_gauss(g);
		paint_threshold(g);
		paint_scale_digits(g);
		paint_level(g);
	}

	protected void paint_trace(Graphics g)
	{
		g.setColor(traceColor);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, y.length) - start - 1; i++)
		{
			if (y[i+start] > level)
				g.setColor(traceColor);
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

		g.drawLine(10, (int)((maxY - level - top) * scaleY - 1),
							 jw - 10, (int)((maxY - level - top) * scaleY - 1));

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE_DIGITS));
		g.drawString(String.valueOf(MathRef.round_2(level)),
								 jw - 30,
								 (int)((maxY - level - top) * scaleY - 4));
	}
	

	// use this method to paint scale digits for krivulka in dB  
	protected void paint_scale_digits(Graphics g)
	{
		super.paint_scale_digits(g);
				
		//int jh = getHeight();
		int jw = getWidth();

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE_DIGITS));

		double m = calcNodeDistance (cell_w / scaleX * Kx); // единиц на одно деление
		double delta =	m * scaleX / Kx; // число экранных точек на одно деление
		int x = (int)(((int)(start * Kx / m) ) * delta - start * scaleX); // сдвиг относительно начала

		for (int i = 0; i < jw / delta + 1; i++)
			g.drawString(String.valueOf(MathRef.round_2 ((i + (int)(start * Kx / m) ) * m   * alpha * 1000d)), (int)(i * delta + x - 12), 10);
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (SwingUtilities.isRightMouseButton(e) ||
				Math.abs(currpos.y-(int)((maxY - level - top)*scaleY)) < MOUSE_COUPLING)
		{
			moveLevel = true;
			level = coord2value(currpos.y);
            levelUpdated();
            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			parent.repaint();
			return;
		}

		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (moveLevel)
		{
			upd_currpos(e);

			level = coord2value(currpos.y);
            levelUpdated();
			parent.repaint();
			return;
		}
		super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (moveLevel)
		{
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
            if (threshold[i] < level1) {
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
        return ap.getMinSplice();
    }
    private void setHeapThreshold(double vThresh) {
        AnalysisParameters ap = Heap.getMinuitAnalysisParams();
        ap.setMinSplice(vThresh); // FIXME: проверять, чтобы оно не оказывалось за пределами minThresh/minConn (это специфично для AnalysisParameters)
        movedHere = true;
        Heap.notifyAnalysisParametersUpdated(); // FIXME: implement other senders and subscribers
        movedHere = false;
    }

    protected void levelUpdated() {
        double vThresh = level2thresh(this.level);
        // округляем
        // XXX: скорее всего, округление должно проводиться в AnalysisParameters, а не здесь
        vThresh = MathRef.round_4(vThresh);
        if (vThresh > 0) {
            setHeapThreshold(vThresh);
        }
    }

    public void updAnalysisParameters() {
        if (movedHere)
            return;
        double level1 = thresh2level(getHeapThreshold());
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
