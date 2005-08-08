package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.events.ConnectorDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DeadZoneDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.EndOfTraceDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.NotIdentifiedDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.io.BellcoreStructure;

public class RefAnalysis
{
	public double[] noise; // hope nobody will change it
	public double[] filtered; // hope nobody will change it
	public TraceEvent overallStats; // hope nobody will change it

	private BellcoreStructure bs;
	private AnalysisResult ar;

	/**
	 * use this ctor to replace mtae of the existing RefAnalysis
	 * @param that
	 * @param mtae
	 */
	public RefAnalysis(RefAnalysis that, ModelTraceAndEventsImpl mtae) {
		this.bs = that.bs;
		this.ar = new AnalysisResult(that.ar.getDataLength(),
				that.ar.getTraceLength(),
				mtae);
		decode();
	}

	public RefAnalysis(BellcoreStructure bs, AnalysisResult ar) {
		this.bs = bs;
		this.ar = ar;
		decode();
	}

	public RefAnalysis(BellcoreStructure bs) {
		this.bs = bs;
		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		if (ap == null) {
			new ClientAnalysisManager();
			ap = Heap.getMinuitAnalysisParams();
		}
		ar = CoreAnalysisManager.performAnalysis(bs, ap);
		decode();
	}

	private void decode ()
	{
		if (false){ // FIXME: just a debug code
			SimpleReflectogramEvent []se = getMTAE().getSimpleEvents();
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
				line += " # begin=" + re.getBegin() * getMTAE().getDeltaX();
				System.out.println(line);
			}
		}

		if (false) { // FIXME: just another debug
			double sigma = MathRef.calcSigma(bs.getWavelength(),
					bs.getPulsewidth());
			double dx = getMTAE().getDeltaX();
			DetailedEvent de[] = getMTAE().getDetailedEvents();
			System.out.println("detailed events (for modelling):");
			for (DetailedEvent ev: de) {
				double len = (ev.getEnd() - ev.getBegin()) * getMTAE().getDeltaX();
				switch(ev.getEventType()) {
				case SimpleReflectogramEvent.LINEAR: // fall thru
					System.out.println("L " + len
							+ " " + ((LinearDetailedEvent)ev).getAttenuation() / dx);
					break;
				case SimpleReflectogramEvent.NOTIDENTIFIED:
					System.out.println("U " + len
							+ " " + ((NotIdentifiedDetailedEvent)ev).getLoss() / len);
					break;
				case SimpleReflectogramEvent.GAIN: // fall throu
				case SimpleReflectogramEvent.LOSS:
					System.out.println("S " + len
							+ " " + ((SpliceDetailedEvent)ev).getLoss());
					break;
				case SimpleReflectogramEvent.DEADZONE:
					System.out.println("C " + len
							+ " 0"
							+ " " + -10); // XXX
					break;
				case SimpleReflectogramEvent.ENDOFTRACE:
					System.out.println("C " + 0
							+ " 0"
							+ " " + MathRef.calcReflectance(sigma,
									((EndOfTraceDetailedEvent)ev).getAmpl()));
					break;
				case SimpleReflectogramEvent.CONNECTOR:
					System.out.println("C " + len
							+ " " + ((ConnectorDetailedEvent)ev).getLoss()
							+ " " + MathRef.calcReflectance(sigma,
									((ConnectorDetailedEvent)ev).getAmpl()));
					break;
				}
			}
		}

		double[] y = bs.getTraceData();
		// ComplexReflectogramEvent[] re = mtae.getComplexEvents();
		DetailedEvent[] de = getMTAE().getDetailedEvents();
		ModelTrace mt = getMTAE().getModelTrace();

		double maxY = y.length > 0 ? y[0] : 0;
		double minY = maxY;
		for (int i = 0; i < y.length; i++)
		{
			if (maxY < y[i])
				maxY = y[i];
			if (minY > y[i])
				minY = y[i];
		}

		int lastPoint = de.length > 0
			? de[de.length - 1].getBegin()
			: 0;

		int veryLastPoint = de.length > 0
			? de[de.length - 1].getEnd()
			: 0;

		int noiseStart = ar.getTraceLength();

		double maxNoise = 0.;
		boolean b = false;
		for(int i = veryLastPoint; i < y.length; i++)
		{
			if (y[i] == minY)
				b = true;
			if(b && maxNoise < y[i])
				maxNoise = y[i];
		}
		//overallStats = new TraceEvent(TraceEvent.OVERALL_STATS, 0, lastPoint);
		overallStats = new TraceEvent(lastPoint);
		{
			double[] data = new double[5];

			// Po (отрицательна) - относительно maxY
			double po;
			if (de.length > 0 && de[0] instanceof DeadZoneDetailedEvent)
				po = ((DeadZoneDetailedEvent)de[0]).getPo();
			else
				po = 0; // мертвой зоны нет - берем ноль
			// ур. шума по уровню 98% (отрицателен) - относительно maxY
			double noise98;
			double noiseRMS;
			if (y.length > noiseStart) {
				noise98 = CoreAnalysisManager.getMedian(
					y, noiseStart, y.length, 0.98) - maxY;
				// ур. шума по RMS (отрицателен) - относительно maxY
				noiseRMS = ReflectogramMath.getRMSValue(
					y, noiseStart, y.length, minY) - maxY;
			} else {
				noise98 = minY - maxY;
				noiseRMS = minY - maxY;
			}

			data[0] = -po; // y0 (ось вниз)
			data[1] = maxY - y[lastPoint]; // y1 (ось вниз)
			data[2] = de.length; // число событий
			data[3] = -noise98; // ур. ш. по 98%
			data[4] = -noiseRMS; // ур. ш. по RMS

			overallStats.setData(data);
		}

		filtered = new double[veryLastPoint];
		noise = new double[lastPoint];

		// long t0 = System.currentTimeMillis();
		for (int i = 0; i < de.length; i++)
		{
			int posFrom = de[i].getBegin();
			int posTo = de[i].getEnd();
			double[] yArrMT = mt.getYArrayZeroPad(posFrom, posTo - posFrom);
			for (int j = posFrom; j < posTo && j < veryLastPoint; j++)
			{
				filtered[j] = yArrMT[j - posFrom];
				if (j < lastPoint)
					noise[j] = Math.abs(y[j] - filtered[j]);
			}
		}
	}

	public ModelTraceAndEventsImpl getMTAE() {
		return ar.getMTAE();
	}
	public BellcoreStructure getBS() {
		return bs;
	}
	public AnalysisResult getAR() {
		return ar;
	}
}
