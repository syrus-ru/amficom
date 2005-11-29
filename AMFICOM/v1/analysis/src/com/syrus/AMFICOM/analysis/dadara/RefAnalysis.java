package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.events.ConnectorDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DeadZoneDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.EndOfTraceDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.NotIdentifiedDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;

public class RefAnalysis {
	public double[] noise; // hope nobody will change it
	public TraceEvent overallStats; // hope nobody will change it
	private double[] filtered; // hope nobody will change it

	private PFTrace pfTrace;
	private AnalysisResult ar;

	/**
	 * use this ctor to replace mtae of the existing RefAnalysis
	 * 
	 * @param that
	 * @param mtae
	 */
	public RefAnalysis(final RefAnalysis that, final ModelTraceAndEventsImpl mtae) {
		this.pfTrace = that.pfTrace;
		this.ar = new AnalysisResult(that.ar.getDataLength(), that.ar.getTraceLength(), mtae);
		this.decode();
	}

	/**
	 * Конструктор по trace.
	 * При необходимости, проводит анализ средствами trace. 
	 * @param trace
	 */
	public RefAnalysis(final Trace trace) {
		this.pfTrace = trace.getPFTrace();
		this.ar = trace.getAR();
		this.decode();
	}

	public RefAnalysis(final PFTrace pfTrace, final AnalysisResult ar) {
		this.pfTrace = pfTrace;
		this.ar = ar;
		this.decode();
	}

	public RefAnalysis(final PFTrace pfTrace) {
		this.pfTrace = pfTrace;
		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		if (ap == null) {
			new ClientAnalysisManager();
			ap = Heap.getMinuitAnalysisParams();
		}
		this.ar = CoreAnalysisManager.performAnalysis(pfTrace, ap);
		this.decode();
	}

	private void decode() {
		if (false) { // FIXME: just a debug code
			final SimpleReflectogramEvent[] se = getMTAE().getSimpleEvents();
			System.out.println("NEvents=" + se.length);
			System.out.println("EVENTS");
			for (int i = 0; i < se.length; i++) {
				final ReliabilitySimpleReflectogramEvent re = (ReliabilitySimpleReflectogramEventImpl) se[i];
				String line = "T=" + re.getEventType() + " B=" + re.getBegin() + " E=" + re.getEnd();
				int tloss = 0;
				if (re.getEventType() == SimpleReflectogramEvent.LINEAR) {
					tloss = 2;
				}
				else if (re.hasReliability() && re.getReliability() < ReliabilitySimpleReflectogramEvent.RELIABLE) {
					tloss = 1;
				}
				line += " N=0 L=" + tloss;
				line += " # begin=" + re.getBegin() * getMTAE().getDeltaX();
				System.out.println(line);
			}
		}

		if (false) { // FIXME: just another debug
			final double sigma = MathRef.calcSigma(this.pfTrace.getWavelength(), this.pfTrace.getPulsewidth());
			final double dx = getMTAE().getDeltaX();
			final DetailedEvent de[] = getMTAE().getDetailedEvents();
			System.out.println("detailed events (for modelling):");
			for (DetailedEvent ev : de) {
				final double len = (ev.getEnd() - ev.getBegin()) * getMTAE().getDeltaX();
				switch (ev.getEventType()) {
					case SimpleReflectogramEvent.LINEAR: // fall thru
						System.out.println("L " + len + " " + ((LinearDetailedEvent) ev).getAttenuation() / dx);
						break;
					case SimpleReflectogramEvent.NOTIDENTIFIED:
						System.out.println("U " + len + " " + ((NotIdentifiedDetailedEvent) ev).getLoss() / len);
						break;
					case SimpleReflectogramEvent.GAIN: // fall throu
					case SimpleReflectogramEvent.LOSS:
						System.out.println("S " + len + " " + ((SpliceDetailedEvent) ev).getLoss());
						break;
					case SimpleReflectogramEvent.DEADZONE:
						System.out.println("C " + len + " 0" + " " + -10); // XXX
						break;
					case SimpleReflectogramEvent.ENDOFTRACE:
						System.out.println("C " + 0 + " 0" + " " + MathRef.calcReflectance(sigma, ((EndOfTraceDetailedEvent) ev).getAmpl()));
						break;
					case SimpleReflectogramEvent.CONNECTOR:
						System.out.println("C " + len + " " + ((ConnectorDetailedEvent) ev).getLoss() + " "
								+ MathRef.calcReflectance(sigma, ((ConnectorDetailedEvent) ev).getAmpl()));
						break;
				}
			}
		}

		final double[] y = this.pfTrace.getFilteredTraceClone();
		// ComplexReflectogramEvent[] re = mtae.getComplexEvents();
		final DetailedEvent[] de = getMTAE().getDetailedEvents();
		final ModelTrace mt = getMTAE().getModelTrace();

		double maxY = y.length > 0 ? y[0] : 0;
		double minY = maxY;
		for (int i = 0; i < y.length; i++) {
			if (maxY < y[i]) {
				maxY = y[i];
			}
			if (minY > y[i]) {
				minY = y[i];
			}
		}

		final int lastPoint = de.length > 0 ? de[de.length - 1].getBegin() : 0;

		final int veryLastPoint = de.length > 0 ? de[de.length - 1].getEnd() : 0;

		final int noiseStart = this.ar.getTraceLength();

		double maxNoise = 0.;
		boolean b = false;
		for (int i = veryLastPoint; i < y.length; i++) {
			if (y[i] == minY) {
				b = true;
			}
			if (b && maxNoise < y[i]){
				maxNoise = y[i];
			}
		}
		//overallStats = new TraceEvent(TraceEvent.OVERALL_STATS, 0, lastPoint);
		this.overallStats = new TraceEvent(lastPoint);
		{
			final double[] data = new double[5];

			// Po (отрицательна) - относительно maxY
			double po;
			if (de.length > 0 && de[0] instanceof DeadZoneDetailedEvent) {
				po = ((DeadZoneDetailedEvent) de[0]).getPo();
			}
			else {
				po = 0; // мертвой зоны нет - берем ноль
			}
			// ур. шума по уровню 98% (отрицателен) - относительно maxY
			double noise98;
			double noiseRMS;
			if (y.length > noiseStart) {
				noise98 = CoreAnalysisManager.getMedian(y, noiseStart, y.length, 0.98) - maxY;
				// ур. шума по RMS (отрицателен) - относительно maxY
				noiseRMS = ReflectogramMath.getRMSValue(y, noiseStart, y.length, minY) - maxY;
			} else {
				noise98 = minY - maxY;
				noiseRMS = minY - maxY;
			}

			data[0] = -po; // y0 (ось вниз)
			data[1] = maxY - y[lastPoint]; // y1 (ось вниз)
			data[2] = de.length; // число событий
			data[3] = -noise98; // ур. ш. по 98%
			data[4] = -noiseRMS; // ур. ш. по RMS

			this.overallStats.setData(data);
		}

		this.filtered = new double[veryLastPoint];
		this.noise = new double[lastPoint];

		// long t0 = System.currentTimeMillis();
		for (int i = 0; i < de.length; i++) {
			int posFrom = de[i].getBegin(); // incl.
			int posTo = de[i].getEnd(); // excl.
			double[] yArrMT = mt.getYArrayZeroPad(posFrom, posTo - posFrom);
			for (int j = posFrom; j < posTo && j < veryLastPoint; j++) {
				this.filtered[j] = yArrMT[j - posFrom];
				if (j < lastPoint) {
					this.noise[j] = Math.abs(y[j] - this.filtered[j]);
				}
			}
		}
		// XXX: workaround:
		// из-за того, что м.ф. ограничена уровнем шума,
		// а м.з. начинается ниже, чем на уровне шума,
		// в разности y и yMT в начале м.з. получается очень
		// большой всплеск, который на графике "затмевает"
		// всю остальную кривую (из-за авт. масштабирования).
		// Поэтому, в качестве WORKAROUND,
		// уровень шума в первой точке не вычисляем.
		if (this.noise.length > 0) {
			this.noise[0] = 0.0;
		}
	}

	public ModelTraceAndEventsImpl getMTAE() {
		return this.ar.getMTAE();
	}

	public AnalysisResult getAR() {
		return this.ar;
	}
}
