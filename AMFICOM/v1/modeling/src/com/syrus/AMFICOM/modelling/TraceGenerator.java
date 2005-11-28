/*-
 * $Id: TraceGenerator.java,v 1.10 2005/11/28 11:31:23 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.modelling;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.log10;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.io.BellcoreModelWriter;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;

/**
 * ���������� �������������� �� ��������� ������ �������� �� ������.
 * ���������� ����������� �������� ���������, ����������� ��������� �
 * ������������ �������������.
 * ��������� ��������� ����-��������� ��������� RC � BC.
 * ����� ������������ BellcoreStructure.
 * <h3>�����������:</h3>
 * <p>
 * �� ���������� ��������� ����������� �������, ��������� ����������� �������
 *   � ��������� ����������� �������.
 * </p><p>
 * �� ��������� ��������� ��������� �������������:
 * <ul>
 * <li> ������������ �� ��������� ������� ���� (������, ��� ���������
 *   ����������� ���������� �����������);
 * <li> ������������� ��������� (����� ������������ ��������) �������������
 *   �������;
 * <li> ������������� ����� ����� ������������� ������� (������� �� ������
 *   ������ �������������), �� ������������� RC- � BC- �����������.
 * </ul>
 * </p>
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.10 $, $Date: 2005/11/28 11:31:23 $
 * @module modeling
 */
public class TraceGenerator {
	public static final double MAX_NOISE = -10.0; // dB

	private Parameters pars;
	private ModelEvent[] events;
	private double[] traceData; // ����� �������� ������������� � �������

	/**
	 * ������� ������������� ���������
	 * 0: R, 1: R+RRR, 2: R+RRR+RRRRR, 3:...+RRRRRRR
	 * ������������� �������� 2 � 3 ��� ���������� ������������.
	 */
	private static final int REFLECTION_SIMULATION_ORDER = 3;

	/**
	 * ������������ ���������, ��������� ������� ���� ���
	 *  (������� ���� + ��� ��������).
	 * ������� ��� {@link #REFLECTION_SIMULATION_ORDER} ����� 2 ��� 3.
	 */
	private static final double SIMULATION_PRECISION_TO_NOISE = -20.0;

	/**
	 * ����������� ��������� ���������
	 */
	public static class Parameters {
		public double initialLevel; // ��������� ������� [dB]
		public double noiseLevel; // [dB] <= MAX_NOISE
		public double darkLevel; // ������� ������� [dB]  
		public double deltaX; // ��� ������������� [�], > 0
		public double distance; // ��������� ������������ [�], > 0; ����� ������ deltaX
		public int wavelength; // ����� ����� [��]
		public int pulseWidth; // ����� �������� [��]
		public double refactionIndex; // ���������� �����������

		/**
		 * @return ����� this-������ ����������
		 */
		public Parameters copy() {
			return new Parameters(initialLevel, noiseLevel, darkLevel,
					deltaX, distance, wavelength, pulseWidth, refactionIndex);
		}

		/**
		 * ������� ����� ����������
		 * @param initialLevel ��������� ������� (�� ���� ��� �������� Po), ��, ������������� (��������, -5.0)
		 * @param noiseLevel ������� ����, ��, �������������, ������������� �������� initialLevel-��. (��������, -20..-45)
		 * @param darkLevel ������� '-inf', ��, �������������, ������������� ������������ �������� �� �/�, ������� ������� ���� ����� ~10 �� (��������, -30..-55).
		 *   ��� ���������� � Bellcore �� ����� ���� ������ -65 ��
		 * @param deltaX ����������, �����, �������������
		 * @param distance ��������� ������������, �����
		 * @param wavelength ����� �����, ��
		 * @param pulseWidth ����� ��������, ��
		 * @param refactionIndex ���������� �����������
		 */
		public Parameters(
				double initialLevel,
				double noiseLevel,
				double darkLevel,
				double deltaX,
				double distance,
				int wavelength,
				int pulseWidth,
				double refactionIndex) {
			this.initialLevel = initialLevel;
			this.noiseLevel = noiseLevel;
			this.darkLevel = darkLevel;
			this.deltaX = deltaX;
			this.distance = distance;
			this.wavelength = wavelength;
			this.pulseWidth = pulseWidth;
			this.refactionIndex = refactionIndex;
		}
	}

	/**
	 * ������� ��������� ��������� �������������� � ��������� ���������� ���������
	 * @param pars ��������� ��������� ���������
	 * @param events ��������� ������ ��������
	 */
	public TraceGenerator(Parameters pars, ModelEvent[] events) {
		long t0 = System.nanoTime();
		this.pars = pars.copy(); // ������ �����, �.�. pars modifiable
		this.events = events.clone(); // �������� ������ ������, �.�. events unmodifiable
		long t1 = System.nanoTime();
		double[] linTrace = generate();
		long t2 = System.nanoTime();
		// ����������� ��� � ��������� � ��
		this.traceData = new double[linTrace.length];
		double absNoise = log2lin(this.pars.noiseLevel);
		double absMinLevel = log2lin(this.pars.darkLevel);
		for (int i = 0; i <linTrace.length; i++) {
			// ��������� � ������ ������ ����
			double value = linTrace[i] + normalRandom() * absNoise;
			if (value <= absMinLevel) {
				this.traceData[i] = this.pars.darkLevel;
			} else {
				this.traceData[i] = lin2log(value);
				if (this.traceData[i] > 0) {
					this.traceData[i] = 0;
				}
			}
		}
		long t3 = System.nanoTime();
		long tpct = (t3 - t0) / 100;
		if (tpct == 0)
			tpct = 1;

		System.err.println("TraceGenerator():"
				+ " copy "  + (t1-t0)/1e6 + " ms (" + (t1-t0)/tpct + "%)"
				+ " gen "   + (t2-t1)/1e6 + " ms (" + (t2-t1)/tpct + "%)"
				+ " noise " + (t3-t2)/1e6 + " ms (" + (t3-t2)/tpct + "%)");
	}

	/**
	 * ��������� BoxCar-����������.
	 * �������� ����� {@link #getBellcore()} � {@link #getTraceData()}
	 * @param width ����� ����� ����������, ��������� ����� �������������
	 */
	public void performBoxCarFiltering(int width) {
		if (width < 1) {
			throw new IllegalArgumentException(
					"performBoxCarFiltering: got non-positive width " + width);
		}
		// ��������� � ���. �������
		double[] tmp = new double[this.traceData.length];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = log2lin(this.traceData[i]);
		}
		// ��������� � ��������� � ���. �������
		for (int i = 0; i < tmp.length; i++) {
			double sum = 0;
			int j0 = Math.max(i - width + 1, 0);
			int j1 = i;
			for (int j = j0; j <= j1; j++) {
				sum += tmp[j];
			}
			this.traceData[i] = lin2log(sum / (j1 - j0 + 1));
		}
	}

	/**
	 * ��������� RC-����������.
	 * �������� ����� {@link #getBellcore()} � {@link #getTraceData()}
	 * @param tau ������ ����������, ������������� �����
	 */
	public void performRCFiltering(double tau) {
		if (tau <= 0 ) {
			throw new IllegalArgumentException(
					"performRCFiltering: got non-positive tau " + tau);
		}
		if (this.traceData.length == 0) {
			return; // nothing to do
		}
		// ��������� � ���. �������
		double[] tmp = new double[this.traceData.length];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = log2lin(this.traceData[i]);
		}
		// ��������� � ��������� � ���. �������
		double acc = tmp[0];
		double wei = 1.0 - Math.exp(-1.0 / tau);
		System.err.println("wei = " + wei);
		for (int i = 0; i < tmp.length; i++) {
			acc = acc * (1.0 - wei) + tmp[i] * wei;
			this.traceData[i] = lin2log(acc);
		}
	}

	/**
	 * @return ����� ������� ����� ��������������� ��������������
	 */
	public double[] getTraceData() {
		return this.traceData.clone();
	}

	/**
	 * ������� Bellcore ��� ��������������� ��������������
	 * @return BellcoreStructure-������ ��������������� ��������������
	 */
	public BellcoreStructure getBellcore() {
		BellcoreStructure bs = new BellcoreStructure();
		BellcoreModelWriter bmw = new BellcoreModelWriter(bs);
		bmw.setAverages(1);
		double[] temp = new double[this.traceData.length];
		for (int i = 0; i < this.traceData.length; i++) {
			temp[i] = this.traceData[i] - this.pars.darkLevel;
		}
		bmw.setData(temp);
		bmw.setPulseWidth(this.pars.pulseWidth);
		bmw.setWavelength(this.pars.wavelength);
		bmw.setOpticalModule("");
		double kmRes = this.pars.deltaX / 1000.0;
		bmw.setRangeParameters(this.pars.refactionIndex,
				kmRes,
				kmRes * temp.length);
		bmw.finalizeChanges();
		return bs;
	}

	/**
	 * ������������� ������ ��������������� �������������� � ASCII-�������
	 * @param ps
	 */
	public void printTrace(PrintStream ps) {
		for (int i = 0; i < traceData.length; i++) {
			ps.println("" + i * pars.deltaX + " " + traceData[i]);
		}
	}

	/**
	 * ��������� ��������� �������������� N(1,0) ��������� �����
	 * XXX: performance: �������� �������� � 1.5-2 ���� �� ���� ��������� ������
	 */
	private double normalRandom() {
		double r1 = Math.random();
		double r2 = Math.random();
		return Math.sqrt(-2 * log(r1)) * Math.sin(2 * Math.PI * r2);
	}

	/**
	 * ���������� ��������� ���������� �� ��������������
	 *   �� ��� ��������� � ���������� ���������.
	 * @param ev �������� ����������
	 * @param pars ��������� ���������
	 * @return ��������� ����������, �� � ��������� 5
	 */
	private static double getConnectorHeight(ModelEvent ev, Parameters pars) {
		double refl = ev.getReflection(); // ������� ���������
		double sigma = MathRef.calcSigma(pars.wavelength, pars.pulseWidth);
		return MathRef.calcPeakByReflectance(sigma, refl);
	}

	/**
	 * ���������� ��������� ���������� �� 100% ����������.
	 * ������������ ��� ������ ������� ��������� ������ ���������,
	 * ����� ����������, ��� ���������� ����� ����������.
	 * @param pars ��������� ���������
	 * @return ��������� ����������, �� � ��������� 5
	 */
	private static double getMaxConnectorHeight(Parameters pars) {
		double sigma = MathRef.calcSigma(pars.wavelength, pars.pulseWidth);
		return MathRef.calcPeakByReflectance(sigma, 0.0);
	}

	/**
	 * ��������� � ������� ������������� ������������� �������.
	 * ��������� ��������� ���������� �������� � ������������� ������������
	 * ������. �����, ����������� �� ��������� �������, ������������.
	 * @param data ������ �������������
	 * @param posBegin ��������� �������
	 * @param posEnd �������� �������
	 * @param value ������� ��������
	 */
	private static void addSquarePulse(double[] data,
			double posBegin, double posEnd,
			double value) {
		int iBeg = Math.max((int)Math.floor(posBegin), 0);
		int iEnd = Math.min((int)Math.ceil(posEnd - 1), data.length - 1);
		// assert iBeg > posBegin - 1;
		// assert iEnd < posEnd;
		//System.out.println("addSquarePulse: iBeg " + iBeg + ", iEnd " + iEnd);
		if (iEnd < iBeg) {
			return;
		}
		data[iBeg] += Math.min(iBeg + 1 - posBegin, 1) * value;
		for (int i = iBeg + 1; i < iEnd; i++) {
			data[i] += value;
		}
		if (iEnd > posEnd - 1) {
			data[iEnd] += Math.min(posEnd - iEnd, 1) * value;
		}
	}
	/**
	 * ������������ ������� ��������, �������������� ���������� �����������
	 * �����, ���������� �������������� �� ��������� �������� �������
	 * � ������ 1.0 � � ��������� ����������� (��������� �������� �������) x0,
	 * ��� �������, ��� ���� ������� ������� � ���� �������������� ��������
	 * �������� ������������ � ������� �� ������� � ��������� �����������
	 * ������ � �����.
	 * <p> �������� c/2n �������������� ������ 1, ��� ������ ��������������
	 * ������� "�����" � ���������". </p>
	 * <p> ������ ��������� (��������� ���������������), � ���� �����
	 * �������� ���� ��� ���������� �����.
	 * ������������� ������������ {@link #calcExpPulseInt}, �����������
	 * ���� �� ��������, ������ ������������.</p>
	 * @param w ������������ �������� ������ (���������� �����)
	 *   (������� ��������� ������ 1.0)
	 * @param p0 ���������� ������ ���������������� ������� �������
	 * @param p1 ���������� ����� ���������������� ������� �������
	 * @param xPos ���������� ������ ������� �������
	 * @param rtau �������� ���������� "�������" (����������) ��������� � �������
	 * @return ���������� ����� � �������� ��������� ��������� ���������
	 */
	protected static double calcExpPulseIntSlow(
			double w,
			double p0,
			double p1,
			double xPos,
			double rtau) {
		double step = 0.01;
		double sum = 0.0;
		for (double x = xPos + step / 2; x < xPos + 1; x += step) {
			for (double t = - w + step / 2; t < 0; t += step) {
				double pos = x + t;
				if (pos < p0 || pos >= p1)
					continue;
				sum += exp(-(pos - p0) * rtau);
			}
		}
		sum *= step * step / w;
		return sum;
	}

	private static double expm1mx(double x) {
		if (Math.abs(x) < 1e-8)
			return x * x / 2 * (1.0 + x / 3);
		return Math.expm1(x) - x;
	}

	private static double aEmaeMbEmbePreEmaeMEmbe(double a, double b, double eps) {
		// should return a*exp(-a*eps) - b*exp(-a*eps) + (exp(-a*eps)-exp(-b*eps) / eps)
		double k = b - a;
		double ekm1 = Math.expm1(k * eps);
		double v1 = a * ekm1;
		double v2 = expm1mx(k * eps) / eps;
		return exp(-b * eps) * (a * ekm1 + expm1mx(k * eps) / eps);
	}
	/**
	 * ������� (�������������) ���������� �������� ���������, ���������������
	 * � {@link #calcExpPulseIntSlow}. �������� ���������� � �������������
	 * �������� ��. ��� ��.
	 */
	protected static double calcExpPulseInt(
			double w,
			double p0,
			double p1,
			double xPos,
			double rtau) {
		double x0 = xPos - w;
		double mult0 = - exp(-(x0 - p0) * rtau) / w;
		double sum1 = 0.0;
		double a, b;
		a = Math.max(p0 - x0, 0);
		b = Math.min(p1 - x0, 1);
		if (b > a) {
			sum1 -= aEmaeMbEmbePreEmaeMEmbe(a, b, rtau);
		}
		a = Math.max(p0 - x0, 1);
		b = Math.min(p1 - x0, w + 1);
		if (b > a) {
			//sum1 -= exp(-a * rtau) - exp(-b * rtau);
			sum1 -= exp(-b * rtau) * Math.expm1((b - a) * rtau);
		}
		a = Math.max(p0 - x0, w);
		b = Math.min(p1 - x0, w + 1);
		if (b > a) {
			double eA = exp(-a * rtau);
			double eB = exp(-b * rtau);
			sum1 += -w * (eA - eB);
			sum1 += aEmaeMbEmbePreEmaeMEmbe(a, b, rtau);
		}
		sum1 *= mult0 / rtau;
		return sum1;
	}

	private static void addExponentCurve(double[] data,
			double posBegin, double posEnd,
			double levelBegin, double rtau, double pWidth) {
		// ���������� �������� ���������, ������������� ���������
		int iBeg = Math.max((int)Math.floor(posBegin), 0);
		int iEnd = Math.min((int)Math.floor(posEnd + pWidth), data.length - 1);
		for (int i = iBeg; i <= iEnd; i++) {
			data[i] += calcExpPulseInt(pWidth, posBegin, posEnd, i, rtau)
					* levelBegin;
		}
	}

	/**
	 * @param y �� � ��������� 5
	 * @return ������������� ��� ��������� ��������������
	 */
	private static double log2lin(double y) {
		return Math.pow(10.0, y * 0.2);
	}
	/**
	 * @param power ������������� ��� ��������� ��������������
	 * @return �� � ��������� 5
	 */
	private static double lin2log(double power) {
		return 5.0 * log10(power);
	}

	private double[] generate() {
		// ���������� ����� �����
		final int N = (int)(this.pars.distance / this.pars.deltaX);

		// ������� � �������� ������ ��� �������� �������������
		double[] data = new double[N];
		for (int i = 0; i < N; i++) {
			data[i] = 0.0;
		}

		// ��������� ����� �������� � �����
		double pulseW = 1.5e8 / this.pars.refactionIndex * 1e-9 // [�/��]
				* this.pars.pulseWidth // [��]
				/ this.pars.deltaX; // [�]
		//System.out.println("TraceGenerator: generate(): pulseW " + pulseW);

		double level0 = log2lin(this.pars.initialLevel);
		// ������������ ��������� (S - ��������)
		// ������������ ������� �� ������ � ������, �� �� � ���. ��.
		{
			double pos = 0; // ������� ��������� (�����)
			double level = level0; // ������� ���. ������� �������� (���. ��.)
			// ���� �� ������
			for (int k = 0; k < this.events.length; k++) {
				ModelEvent ev = events[k];
				// �������� ��������� (� ������ �������)
				//  - ����������
				// �������� ������ (� ������ �������)
				if (ev.hasLoss()) {
					level *= log2lin(-ev.getLoss());
				}
				// ������������� (�������� ���������)
				if (ev.hasLength()) {
					assert ev.hasAttenuation();
					double len = ev.getLength() / this.pars.deltaX;
					// ������������ ���������: attnDB - �� (������� 5) �� �����
					double attnDB = ev.getAttenuation() * this.pars.deltaX;
					// rtau - �������� ��������� ��������� � exp(1) ���
					double rtau = attnDB * log(10.0) / 5.0;
					double endPos = pos + len;
					double endLevel = level * exp(-len * rtau);
					// FIXME ������� ��������� ������� ������� �� ����
					addExponentCurve(data,
							pos, pos + len, level, rtau, pulseW);
					pos = endPos;
					level = endLevel;
				}
			}
		}
		// ��������� ��������� (R) � ��������� ��������� (RRR, RRRRR, ...)
		// ��������� ������� �������, ��������� �������� ����� ���������
		// �� ���� ����.
		addReflectionsAndPhantoms(true,
				0,
				level0,
				-1,
				REFLECTION_SIMULATION_ORDER * 2,
				pulseW,
				data,
				log2lin(this.pars.noiseLevel + SIMULATION_PRECISION_TO_NOISE
						- getMaxConnectorHeight(this.pars)));
		return data;
	}

	private void addReflectionsAndPhantoms(boolean forward,
			double pos0, // ��������� �����
			double level0,
			int index, // �������, � �������� ������
			int recurse, // ���������� ������� �������� (�������� ��������� �������� ������ ������ ���, ��� ������ ����� �������)
			double pulseW,
			double[] data, // data - ���������� �����
			double precision) {
		double pos = pos0;
		double level = level0;
		int sign = forward ? 1 : -1;
		int kStart = index + sign;
		int kEnd = forward ? this.events.length - 1 : 0;
//		System.err.println("recurse " + recurse + " index " + index + " sign " + sign + " kStart " + kStart + " kEnd " + kEnd + " pos " + pos + " level " + level0 + " precision " + precision);
		for (int k = kStart; k * sign <= kEnd * sign; k += sign) {
			ModelEvent ev = events[k];
			if (recurse > 0 && ev.hasReflection()) {
				final double reflLevel = level * log2lin(ev.getReflection());
				if (reflLevel > precision) {
					addReflectionsAndPhantoms(!forward,
						pos,
						reflLevel,
						k,
						recurse - 1,
						pulseW,
						data,
						precision);
				}
			}
			// ����� ���� � �������� �����������, ���������� � ���������
			// (������ ��������� �� ����� �� ������� �������������)
			// � ������ (������ �� ���� ����� ��� ������ �� ���� ������
			// �� ���� �������� 5 ��)
			if (!forward) {
				continue;
			}
			if (ev.hasReflection()) {
				// ���� ���� � ������ �����������, �� ��������� �����
				// ����� ���������� �������������
//				System.err.println("added[" + recurse + "]:" + k + " at " + pos);
				double factor = log2lin(getConnectorHeight(ev, this.pars));
				addSquarePulse(data, pos, pos + pulseW, level * factor);
			}
			// ��������� ��������� � �������������.
			// �� �� ����� ����� ��������� �������� �� � �/�
			// ��-�� ����, ��� � ����� ������ �� ���� ��������, ����������
			// ����� ����������� � ���������� (���), ����� ����������� ������
			// ���, � �������� ������� ���� ��� ���������, � ����� ���������.
			// ������ ��, ������� ����� ��������� ��� ��� ����������� (���, ���),
			// �� ����� ���� ����� ����� ������� ��������.
			// ����� �������� ������������� ����� ����� ���������,
			// �� ������ �� �������� �� ����� ���������, � ��������� ���
			// ������ generate().
			if (ev.hasLoss()) {
				level *= log2lin(-ev.getLoss());
			}
			if (ev.hasLength()) {
				pos += ev.getLength() / this.pars.deltaX;
				level *= log2lin(-ev.getLength() * ev.getAttenuation());
			}
		}
	}

	/**
	 * ������ �������� ��������� ��������������
	 * @return ��������� ��������� ��������������
	 */
	protected static TraceGenerator myTestGenerator1() {
		// ������� ����� ������������ �������
		ModelEvent me[] = new ModelEvent[] {
				// ������
				ModelEvent.createReflective(1.0, -5),
				// todo ������ - ������ �������� ��� ����� ����������
				// ����������������� ��������� ������ ��� ��������������� ������ "����� ���������"
				//ModelEvent.createLinear(100.3, 1e-18),
				ModelEvent.createLinear(200.4, 2e-4),
				ModelEvent.createLinear(100.3, 1e-5),
				// ������
				ModelEvent.createSlice(0.2),
				ModelEvent.createLinear(3000.0, 2e-4),
				// ��� ������� ���������
				ModelEvent.createReflective(0.0, -5),
				ModelEvent.createLinear(80, 2e-4),
				ModelEvent.createReflective(1.0, -5),
				ModelEvent.createLinear(12000.0, 2e-4),
				// ����� �������
				ModelEvent.createReflective(1.0, -20)
		};

		// ������� ����� ���������� ������������
		Parameters pars = new Parameters(-15.0, -30.0, -40.0,
				4.0, 18000.0, 1625, 500, 1.468);

		// ��������� �������������
		return new TraceGenerator(pars, me);
	}

	/**
	 * ������ �������� ��������� �������������� �� �������� ����� � ����������.
	 * ������ �������� ����� ����������������. � �������� ������� �������������
	 * ������������� {@link #myTestGenerator1()}
	 * @return ��������� ��������� ��������������
	 */
	@Deprecated
	protected static TraceGenerator myTestGenerator2() {
		List<ModelEvent> events = new ArrayList<ModelEvent>();
		try {
			File f = new File("input.dat");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			double prevLinLen = 0.0;
			while ((s = br.readLine()) != null) {
				prevLinLen = ModelEvent.addEventsByString(events, s, prevLinLen);
			}
			br.close();
		} catch (IOException e) {
			throw new InternalError("IOException in debug code: " + e);
		}
		ModelEvent[] me = events.toArray(new ModelEvent[events.size()]);
		Parameters pars = new Parameters(-5.0, -30.0, -40.0,
				8.0, 100000.0, 1625, 1000, 1.467);

		return new TraceGenerator(pars, me);
	}

	/**
	 * ������ ������������� ���������� ��������������
	 */
	public static void main(String[] args)
	throws InternalError, IOException {
		// ������ �������� ���������� �������������� (������ � ���������������).
		// ������ � ���� ������� ���������������� ��� ����������� ���������.
		TraceGenerator tg = myTestGenerator1();
		//TraceGenerator tg = myTestGenerator2();

		// ��� �������, �������� ����������
//		tg.performRCFiltering(14);
//		tg.performBoxCarFiltering(9);

		//tg.printTrace(new PrintStream(new File("dump.txt")));

		// ��������� � Bellcore
		BellcoreStructure bs = tg.getBellcore();

		// ���������� Bellcore � ����
		BellcoreWriter bw = new BellcoreWriter();
		byte[] bar = bw.write(bs);
		File outFile = new File("./model.sor");
		outFile.delete();
		if (!outFile.createNewFile()) {
			throw new InternalError("Could not create new file");
		}
		OutputStream os = new FileOutputStream(outFile);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		bos.write(bar);
		bos.close();

		// ��������� � ��������� �������
		double[] y = tg.getTraceData();
		PrintStream out = new PrintStream("./model.dat");
		for (int i = 0; i < y.length; i++)
			out.println("" + i + " " + y[i]);
		out.close();

		// ����� �������
		System.out.println("Done!");
	}
}
