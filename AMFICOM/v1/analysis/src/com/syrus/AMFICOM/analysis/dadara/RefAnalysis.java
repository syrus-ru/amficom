package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.events.DeadZoneDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.io.BellcoreStructure;

public class RefAnalysis
{
	public TraceEvent[] events; // hope nobody will change it // @todo: hide TraceEvents[]
	public double[] noise; // hope nobody will change it
	public double[] filtered; // hope nobody will change it
	public TraceEvent overallStats; // hope nobody will change it

    private BellcoreStructure bs;
    private ModelTraceAndEventsImpl mtae;

    public RefAnalysis(BellcoreStructure bs, ModelTraceAndEventsImpl mtae)
    {
        this.bs = bs;
        this.mtae = mtae;
        decode();
    }
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

    private void decode ()
	{
        if (false){ // FIXME: just a debug code
            SimpleReflectogramEvent []se = mtae.getSimpleEvents();
            System.out.println("NEvents=" + se.length);
            System.out.println("EVENTS");
            for (int i = 0; i < se.length; i++) {
                ReliabilitySimpleReflectogramEvent re = 
                    (ReliabilitySimpleReflectogramEventImpl) se[i];
                String line = "T=" + re.getEventType()
                    + " B=" + re.getBegin()
                    + " E=" + re.getEnd();
                int tloss = 0;
                if (re.getEventType() == SimpleReflectogramEvent.LINEAR)
                    tloss = 2;
                else if (re.hasReliability() && re.getReliability() <
                            ReliabilitySimpleReflectogramEvent.RELIABLE)
                    tloss = 1;
                line += " N=0 L=" + tloss;
                line += " # begin=" + re.getBegin() * mtae.getDeltaX();
                System.out.println(line);
            }
        }

        double[] y = bs.getTraceData();
		// ComplexReflectogramEvent[] re = mtae.getComplexEvents();
        DetailedEvent[] de = mtae.getDetailedEvents();
		ModelTrace mt = mtae.getModelTrace();
		events = null; //new TraceEvent[re.length];

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

		int lastPoint = de.length > 0
			? de[de.length - 1].getBegin()
			: 0;

		int veryLastPoint = de.length > 0
			? de[de.length - 1].getEnd()
			: 0;

        // XXX: we need to find trace length again 'cause mtae have forgotten it
        int noiseStart = CoreAnalysisManager.calcTraceLength(y);

//		for (int i = 0; i < re.length; i++)
//		{
//            events[i] = decodeEvent(y, mtae, i, top, re, mt);
//		}

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
            //double po = re[0].getAsympY0();
            double po;
            if (de.length > 0 && de[0] instanceof DeadZoneDetailedEvent)
                po = ((DeadZoneDetailedEvent)de[0]).getPo();
            else
                po = 0;
    		data[0] = po; // y0
    		data[1] = maxY - y[lastPoint]; // y1
            //data[2] = (maxY - maxNoise * 0.98); // noise // @todo: use correct noise determination algo - here and everywhere
            data[2] = maxY - CoreAnalysisManager.getMedian(y, noiseStart, y.length, 0.98);
    		data[3] = po; // po ?
    		data[4] = de.length;
    		overallStats.setData(data);
        }

		filtered = new double[veryLastPoint];
		noise = new double[lastPoint];

		// long t0 = System.currentTimeMillis();
		for (int i = 0; i < de.length; i++)
		{
			// for(int j = re[i].getBegin(); j < y.length; j++) -- changed just
			// because was too slow -- saa
			int posFrom = de[i].getBegin();
			int posTo = de[i].getEnd();
			double[] yArrMT = mt.getYArrayZeroPad(posFrom, posTo - posFrom);
			for (int j = posFrom; j < posTo && j < veryLastPoint; j++)
			{
                filtered[j] = Math.max(0, yArrMT[j - posFrom]);
				if (j < lastPoint)
					noise[j] = Math.abs(y[j] - filtered[j]);
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
