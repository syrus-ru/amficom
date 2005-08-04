/*-
 * $Id: TraceGenerator.java,v 1.2 2005/08/04 12:33:55 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.modelling;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.log10;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.io.BellcoreModelWriter;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;


/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/08/04 12:33:55 $
 * @module
 */
public class TraceGenerator {
	public static final double MAX_NOISE = -10.0; // dB

	private Parameters pars;
	private ModelEvent[] events;
	private double[] traceData; // здесь значения отрицательные и нулевые

	/**
	 * Аггрегирует параметры измерения
	 */
	public static class Parameters {
		public double initialLevel; // начальный уровень [dB]
		public double noiseLevel; // [dB] <= MAX_NOISE
		public double darkLevel; // уровень темноты [dB]  
		public double deltaX; // шаг дискретизации [м], > 0
		public double distance; // дистанция тестирования [м], > 0; много больше deltaX
		public int wavelength; // длина волны [нм]
		public int pulseWidth; // длина импульса [нс]
		public double refactionIndex; // показатель преломления
		public Parameters copy() {
			return new Parameters(initialLevel, noiseLevel, darkLevel,
					deltaX, distance, wavelength, pulseWidth, refactionIndex);
		}
		public Parameters() {
			// no initialization; no check for correctness
		}
		/**
		 * Создает набор параметров
		 * @param initialLevel начальный уровень (по сути это примерно Po), дБ, отрицательное (Например, -5.0)
		 * @param noiseLevel уровень шума, дБ, отрицательное, соответствует разности initialLevel-ДД. (Например, -20..-45)
		 * @param darkLevel уровень '-inf', дБ, отрицательное, соответствует минимальному значению на р/г, типично уровень шума минус ~10 дБ (Например, -30..-55).
		 *   При сохранении в Bellcore не может быть меньше -65 дБ
		 * @param deltaX разрешение, метры, положительное
		 * @param distance дистанция тестирования, метры
		 * @param wavelength длина волны, нм
		 * @param pulseWidth длина импульса, нс
		 * @param refactionIndex показатель преломления
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

	public TraceGenerator(Parameters pars, ModelEvent[] events) {
		this.pars = pars.copy(); // делаем копию, т.к. pars modifiable
		this.events = events.clone(); // копируем только массив, т.к. events unmodifiable
		double[] linTrace = generate();
		// накладываем шум и переводим к дБ
		this.traceData = new double[linTrace.length];
		double absNoise = log2lin(this.pars.noiseLevel);
		double absMinLevel = log2lin(this.pars.darkLevel);
		for (int i = 0; i <linTrace.length; i++) {
			// вычисляем с учетом уровня шума
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
	}

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

	public void printTrace(PrintStream ps) {
		for (int i = 0; i < traceData.length; i++) {
			ps.println("" + i * pars.deltaX + " " + traceData[i]);
		}
	}

	/**
	 * вычисляет нормально распределенное N(1,0) случайное число
	 */
	private double normalRandom() {
		double r1 = Math.random();
		double r2 = Math.random();
		return Math.sqrt(-2 * log(r1)) * Math.sin(2 * Math.PI * r2);
	}

	/**
	 * Определяет амплитуду коннектора на рефлектограмме
	 *   по его отражению и параметрам измерения.
	 * @param ev парметры коннектора
	 * @param pars параметры измерения
	 * @return амплитуда коннектора
	 */
	private static double getConnectorHeight(ModelEvent ev, Parameters pars) {
		double refl = ev.getReflection(); // уровень отражения
		double sigma = MathRef.calcSigma(pars.wavelength, pars.pulseWidth);
		return MathRef.calcPeakByReflectance(sigma, refl);
	}

	/**
	 * Добавляет к массиву интенсивности прямоугольный импульс.
	 * Учитывает частичное перекрытие импульса с целочисленной координатной
	 * сеткой. Точки, оказавшиеся за пределами массива, игнорируются.
	 * @param data массив интенсивности
	 * @param posBegin начальная позиция
	 * @param posEnd конечная позиция
	 * @param value уровень мощности
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
	 * Рассчитывает двойной интеграл, представляющий количество рассеянного
	 * света, полученное фотоприемником за временной интервал отсчета
	 * с длиной 1.0 и с начальной координатой (начальным моментом времени) x0,
	 * при условии, что свет излучен лазером в виде прямоугольного импульса
	 * заданной длительности и рассеян на участке с заданными дистанциями
	 * начала и конца.
	 * <p> Величина c/2n предполагается равной 1, что делает эквивалентными
	 * термины "время" и дистанция". </p>
	 * <p> Расчет медленный (численным интегрированием), и этот метод
	 * сохранен лишь для справочных целей.
	 * Рекомендуется использовать {@link #calcExpPulseInt}, реализующий
	 * этот же интеграл, взятый аналитически.</p>
	 * @param w длительность импульса лазера (расположен слева)
	 *   (энергия считается равной 1.0)
	 * @param p0 координата начала рассматриваемого участка волокна
	 * @param p1 координата конца рассматриваемого участка волокна
	 * @param xPos координата начала отсчета времени
	 * @param tau постоянная "времени" (координаты) затухания в волокне
	 * @return количество света в эаданном единичном временном интервале
	 */
	public static double calcExpPulseIntSlow(
			double w,
			double p0,
			double p1,
			double xPos,
			double tau) {
		double step = 0.01;
		double sum = 0.0;
		for (double x = xPos + step / 2; x < xPos + 1; x += step) {
			for (double t = - w + step / 2; t < 0; t += step) {
				double pos = x + t;
				if (pos < p0 || pos >= p1)
					continue;
				sum += exp(-(pos - p0) / tau);
			}
		}
		sum *= step * step / w;
		return sum;
	}
	/**
	 * Быстрое (аналитическое) вычисление двойного интеграла, представленного
	 * в {@link #calcExpPulseIntSlow}. Описание параметров и возвращаемого
	 * значения см. там же.
	 */
	public static double calcExpPulseInt(
			double w,
			double p0,
			double p1,
			double xPos,
			double tau) {
		double x0 = xPos - w; 
		double rtau = 1.0 / tau;
		double mult = -tau / w * exp(-(x0 - p0) * rtau);
		double sum = 0.0;
		sum = 0.0;
		double a, b;
		a = Math.max(p0 - x0, 0);
		b = Math.min(p1 - x0, 1);
		if (b > a) {
			sum += (b + tau) * exp(-b * rtau)
					- (a + tau) * exp(-a * rtau);
		}
		a = Math.max(p0 - x0, 1);
		b = Math.min(p1 - x0, w + 1);
		if (b > a) {
			sum += exp(-b * rtau) - exp(-a * rtau);
		}
		a = Math.max(p0 - x0, w);
		b = Math.min(p1 - x0, w + 1);
		if (b > a) {
			sum += (w - b - tau) * exp(-b * rtau)
					- (w - a - tau) * exp(-a * rtau);
		}
		sum *= mult;
		return sum;
	}

	private static void addExponentCurve(double[] data,
			double posBegin, double posEnd,
			double levelBegin, double tau, double pWidth) {
		// определяем диапазон координат, затрагиваемые импульсом
		int iBeg = Math.max((int)Math.floor(posBegin), 0);
		int iEnd = Math.min((int)Math.floor(posEnd + pWidth), data.length - 1);
		for (int i = iBeg; i <= iEnd; i++) {
			data[i] += calcExpPulseInt(pWidth, posBegin, posEnd, i, tau)
					* levelBegin;
		}
	}

	private static double log2lin(double y) {
		return Math.pow(10.0, y * 0.2);
	}
	private static double lin2log(double power) {
		return 5.0 * log10(power);
	}

	private double[] generate() {
		// определяем число точек
		final int N = (int)(this.pars.distance / this.pars.deltaX);

		// создаем и обнуляем массив для хранения интенсивности
		double[] data = new double[N];
		for (int i = 0; i < N; i++) {
			data[i] = 0.0;
		}

		// переводим длину импульса в точки
		double pulseW = 1.5e8 / this.pars.refactionIndex * 1e-9 // [м/нс]
				* this.pars.pulseWidth // [нс]
				/ this.pars.deltaX; // [м]
		//System.out.println("TraceGenerator: generate(): pulseW " + pulseW);

		// расставляем модельные события
		// обеспечиваем переход от метров к точкам, от дБ к лин. ед.
		{
			double pos = 0; // текущая дистанция (точки)
			double level = log2lin(this.pars.initialLevel); // текущий абс. уровень мощности (лин. ед.)
			// идем по трассе
			for (int k = 0; k < this.events.length; k++) {
				ModelEvent ev = events[k];
				// точечное отражение (в начале участка)
				if (ev.hasReflection()) {
					double factor = log2lin(getConnectorHeight(ev, this.pars));
					addSquarePulse(data, pos, pos + pulseW, level * factor);
				}
				// точечные потери (в начале участка)
				if (ev.hasLoss()) {
					level *= log2lin(-ev.getLoss());
				}
				// протяженность (обратное рассеяние)
				if (ev.hasLength()) {
					assert ev.hasAttenuation();
					double len = ev.getLength() / this.pars.deltaX;
					// рассчитываем затухание: attnDB - дБ (масштаб 5) на точку
					double attnDB = ev.getAttenuation() * this.pars.deltaX;
					// rtau - обратная дистанция затухания в exp(1) раз
					double rtau = attnDB * log(10.0) / 5.0;
					double endPos = pos + len;
					double endLevel = level * exp(-len * rtau);
					// FIXME нулевое затухание вызовет деление на ноль
					addExponentCurve(data,
							pos, pos + len, level, 1.0 / rtau, pulseW);
					pos = endPos;
					level = endLevel;
				}
			}
		}
		return data;
	}
	public static void main(String[] args)
	throws InternalError, IOException {
		// create input data for modelling
		ModelEvent me[] = new ModelEvent[] {
				// начало
				ModelEvent.createReflective(1.0, -20),
				ModelEvent.createLinear(1000.3, 2e-4),
				ModelEvent.createLinear(100.4, 2e-4),
				// сварка
				ModelEvent.createSlice(0.2),
				ModelEvent.createLinear(3000.0, 2e-4),
				// два близких отражения
				ModelEvent.createReflective(0.0, -80),
				ModelEvent.createLinear(80, 2e-4),
				ModelEvent.createReflective(1.0, -60),
				ModelEvent.createLinear(2000.0, 2e-4),
				// конец волокна
				ModelEvent.createReflective(1.0, -20)
		};
		Parameters pars = new Parameters(-5.0, -20.0, -30.0,
				4.0, 6500.0, 1625, 1000, 1.468);

		// perform modelling
		TraceGenerator tg = new TraceGenerator(pars, me);

		// export to bellcore
		BellcoreStructure bs = tg.getBellcore();

		// save to file
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
	}
}
