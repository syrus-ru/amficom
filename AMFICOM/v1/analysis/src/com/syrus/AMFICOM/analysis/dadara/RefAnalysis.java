package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.io.BellcoreStructure;

public class RefAnalysis
{
	public TraceEvent[] events; // hope nobody will change it
	public double[] noise; // hope nobody will change it
	public double[] filtered; // hope nobody will change it
	public TraceEvent overallStats; // hope nobody will change it

    private BellcoreStructure bs;
    private ModelTraceAndEventsImpl mtae;

	public RefAnalysis(BellcoreStructure bs)
	{
        this.bs = bs;

        AnalysisParameters ap = Heap.getMinuitAnalysisParams();
        if (ap == null) {
            new ClientAnalysisManager();
            ap = Heap.getMinuitAnalysisParams();
        }

        mtae = CoreAnalysisManager.makeAnalysis(bs, ap);

        decode();
	}

    private static TraceEvent decodeEvent(double[] y,
            ModelTraceAndEvents mtae,
            int i,
            double top,
            ComplexReflectogramEvent[] re,
            ModelTrace mt)
    {
        int type;
        switch (re[i].getEventType())
        {
        case SimpleReflectogramEvent.LINEAR:
            type = TraceEvent.LINEAR;
            break;
        case SimpleReflectogramEvent.DEADZONE:
            type = TraceEvent.INITIATE;
            break;
        case SimpleReflectogramEvent.ENDOFTRACE:
            type = TraceEvent.TERMINATE;
            break;
        case SimpleReflectogramEvent.CONNECTOR:
            type = TraceEvent.CONNECTOR;
            break;
        case SimpleReflectogramEvent.LOSS:
            type = TraceEvent.LOSS;
            break;
        case SimpleReflectogramEvent.GAIN:
            type = TraceEvent.GAIN;
            break;
        default:
            type = TraceEvent.NON_IDENTIFIED;
        }

        TraceEvent event = new TraceEvent(type, re[i].getBegin(), re[i].getEnd());

        // определяем "асимптотические" значения слева и справа

        double asympB = re[i].getAsympY0();
        double asympE = re[i].getAsympY1();

        if (type == TraceEvent.LINEAR) {
            double[] data = new double[5];
            data[0] = top - asympB;
            data[1] = top - asympE;
            data[2] = -(asympE - asympB)
                    / (re[i].getEnd() - re[i].getBegin());
            ModelTrace yMT = new ArrayModelTrace(y);
            data[3] = ReflectogramComparer.getRMSDeviation(mtae.getModelTrace(), yMT, re[i]);
            data[4] = ReflectogramComparer.getMaxDeviation(mtae.getModelTrace(), yMT, re[i]);
            event.setLoss(re[i].getMLoss());
            event.setData(data);
        } else if (type == TraceEvent.CONNECTOR) {
            double[] data = new double[4];
            data[0] = top - asympB;
            data[1] = top - asympE;
            data[2] = data[0]
                    - re[i].getALet();
            data[3] = 0; // FIXIT: больше не используется, убрать
            event.setLoss(re[i].getMLoss());
            event.setData(data);
        } else if (type == TraceEvent.LOSS || type == TraceEvent.GAIN) {
            double[] data = new double[3];
            data[0] = top - asympB;
            data[1] = top - asympE;
            data[2] = re[i].getMLoss();
            event.setLoss(re[i].getMLoss());
            event.setData(data);
        } else if (type == TraceEvent.TERMINATE) {
            double[] data = new double[3];
            data[0] = top - asympB;
            data[1] = data[0]
                    - re[i].getALet();
            data[2] = 0; // FIXME: больше не используется, убрать
            event.setLoss(0);
            event.setData(data);
        } else if (type == TraceEvent.INITIATE) {
            // extrapolate first linear event to x = 0
            double po = asympB;

            int adz = 0;
            int edz = 0;
            final int N = re[i].getEnd() - re[i].getBegin();
            double[] yarr = mt.getYArrayZeroPad(re[i].getBegin(), N);
            // find max
            double vmax = po;
            for (int k = 0; k < N; k++) {
                if (vmax < yarr[k])
                    vmax = yarr[k];
            }
            // find width
            for (int k = 0; k < N; k++) {
                if (yarr[k] > vmax - 1.5)
                    edz++;
                if (yarr[k] > po + .5)
                    adz++;
            }

            double[] data = new double[4];
            data[0] = top - vmax; // changed by saa
            data[1] = top - po; // Po
            data[2] = edz;
            data[3] = adz;
            event.setLoss(0); 
            event.setData(data);
        } else if (type == TraceEvent.NON_IDENTIFIED) {
            double[] data = new double[3];
            data[0] = top - asympB;
            data[1] = top - asympE;
            data[2] = data[1] - data[0]; //eventMaxDeviation - ?
            event.setLoss(re[i].getMLoss()); 
            event.setData(data);
        }
        return event;
    }

	private void decode ()
	{
        double[] y = bs.getTraceData();
		ComplexReflectogramEvent[] re = mtae.getComplexEvents();
		ModelTrace mt = mtae.getModelTrace();
		events = new TraceEvent[re.length];

		double maxY = 0; // XXX: saa: is 0 good for min/max? shall we use y[0] instead?
		double minY = 0;
		for (int i = 0; i < y.length; i++)
		{
			if (maxY < y[i])
				maxY = y[i];
			if (minY > y[i])
				minY = y[i];
		}
		double top = maxY - minY;

		int lastPoint = re.length > 0
			? re[re.length - 1].getBegin()
			: 0;

		int veryLastPoint = re.length > 0
			? re[re.length - 1].getEnd()
			: 0;

		for (int i = 0; i < re.length; i++)
		{
            events[i] = decodeEvent(y, mtae, i, top, re, mt);
		}

		double maxNoise = 0.;
		boolean b = false;
		for(int i = veryLastPoint; i < y.length; i++)
		{
			if (y[i] == minY)
				b = true;
			if(b && maxNoise < y[i])
				maxNoise = y[i];
		}
		overallStats = new TraceEvent(TraceEvent.OVERALL_STATS, 0, lastPoint);
        {
    		double[] data = new double[5];
            double po = re[0].getAsympY0();
    		data[0] = maxY - po;
    		data[1] = maxY - y[lastPoint];
    		data[2] = (maxY - maxNoise * 0.98);
    		data[3] = (maxY - po);
    		data[4] = re.length;
    		overallStats.setData(data);
        }

		filtered = new double[y.length];
		noise = new double[y.length];
		maxNoise = 0;

		// long t0 = System.currentTimeMillis();
		for (int i = 0; i < re.length; i++)
		{
			// for(int j = re[i].getBegin(); j < y.length; j++) -- changed just
			// because was too slow -- saa
			int posFrom = re[i].getBegin();
			int posTo = re[i].getEnd();
			double[] yArrMT = mt.getYArrayZeroPad(posFrom, posTo - posFrom);
			for (int j = posFrom; j < posTo; j++)
			{
				if (j < lastPoint) // XXX: saa: I think there should be '<='
				{
					filtered[j] = Math.max(0, yArrMT[j - posFrom]);
					noise[j] = Math.abs(y[j] - filtered[j]);
					if (noise[j] > maxNoise)
						maxNoise = noise[j];
				} else
				{
					filtered[j] = y[j];
					noise[j] = maxNoise;
				}
			}
		}
	}

    public ModelTraceAndEventsImpl getMTAE() {
        return mtae;
    }
    public BellcoreStructure getBS() {
        return bs;
    }
}
