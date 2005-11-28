/*-
 * $Id: TraceGenerator.java,v 1.10 2005/11/28 11:31:23 saa Exp $
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
 * Моделирует рефлектограмму по заданному набору объектов на трассе.
 * Моделирует однократное обратное рассеяние, однократное отражение и
 * многократные переотражения.
 * Позволяет проводить пост-обработку фильтрами RC и BC.
 * Умеет генерировать BellcoreStructure.
 * <h3>Ограничения:</h3>
 * <p>
 * Не моделирует рассеяние отраженного сигнала, отражение рассеянного сигнала
 *   и рассеяние рассеянного сигнала.
 * </p><p>
 * Не учитывает следующую специфику рефлектометра:
 * <ul>
 * <li> Изменяющийся по дистанции уровень шума (видимо, это изменение
 *   обусловлено раздельным усреднением);
 * <li> Специфическое удлинение (сверх длительности импульса) отражательных
 *   событий;
 * <li> Специфическая форма спада отражательных событий (зависит от режима
 *   работы рефлектометра), не описывающаяся RC- и BC- фильтрацией.
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
	private double[] traceData; // здесь значения отрицательные и нулевые

	/**
	 * Порядок аппроксимации отражений
	 * 0: R, 1: R+RRR, 2: R+RRR+RRRRR, 3:...+RRRRRRR
	 * рекомендуются значение 2 и 3 как достаточно реалистичные.
	 */
	private static final int REFLECTION_SIMULATION_ORDER = 3;

	/**
	 * Пренебрегаем сигналами, амплитуда которых ниже чем
	 *  (уровень шума + эта величина).
	 * Полезен при {@link #REFLECTION_SIMULATION_ORDER} более 2 или 3.
	 */
	private static final double SIMULATION_PRECISION_TO_NOISE = -20.0;

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

		/**
		 * @return копию this-набора параметров
		 */
		public Parameters copy() {
			return new Parameters(initialLevel, noiseLevel, darkLevel,
					deltaX, distance, wavelength, pulseWidth, refactionIndex);
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

	/**
	 * Создает генератор модельной рефлектограммы и выполняет собственно генерацию
	 * @param pars Модельные параметры измерения
	 * @param events Модельный список объектов
	 */
	public TraceGenerator(Parameters pars, ModelEvent[] events) {
		long t0 = System.nanoTime();
		this.pars = pars.copy(); // делаем копию, т.к. pars modifiable
		this.events = events.clone(); // копируем только массив, т.к. events unmodifiable
		long t1 = System.nanoTime();
		double[] linTrace = generate();
		long t2 = System.nanoTime();
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
	 * Выполнить BoxCar-фильтрацию.
	 * Вызывать перед {@link #getBellcore()} и {@link #getTraceData()}
	 * @param width число точек усреднения, небольшое целое положительное
	 */
	public void performBoxCarFiltering(int width) {
		if (width < 1) {
			throw new IllegalArgumentException(
					"performBoxCarFiltering: got non-positive width " + width);
		}
		// переводим в лин. масштаб
		double[] tmp = new double[this.traceData.length];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = log2lin(this.traceData[i]);
		}
		// усредняем и переводим в лог. масштаб
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
	 * Выполнить RC-фильтрацию.
	 * Вызывать перед {@link #getBellcore()} и {@link #getTraceData()}
	 * @param tau ширина усреднения, положительное число
	 */
	public void performRCFiltering(double tau) {
		if (tau <= 0 ) {
			throw new IllegalArgumentException(
					"performRCFiltering: got non-positive tau " + tau);
		}
		if (this.traceData.length == 0) {
			return; // nothing to do
		}
		// переводим в лин. масштаб
		double[] tmp = new double[this.traceData.length];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = log2lin(this.traceData[i]);
		}
		// усредняем и переводим в лог. масштаб
		double acc = tmp[0];
		double wei = 1.0 - Math.exp(-1.0 / tau);
		System.err.println("wei = " + wei);
		for (int i = 0; i < tmp.length; i++) {
			acc = acc * (1.0 - wei) + tmp[i] * wei;
			this.traceData[i] = lin2log(acc);
		}
	}

	/**
	 * @return копия массива точек сгенерированной рефлектограммы
	 */
	public double[] getTraceData() {
		return this.traceData.clone();
	}

	/**
	 * Создает Bellcore для сгенерированной рефлектограммы
	 * @return BellcoreStructure-объект сгенерированной рефлектограммы
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
	 * распечатывает кривую сгенерированной рефлектограммы в ASCII-формате
	 * @param ps
	 */
	public void printTrace(PrintStream ps) {
		for (int i = 0; i < traceData.length; i++) {
			ps.println("" + i * pars.deltaX + " " + traceData[i]);
		}
	}

	/**
	 * вычисляет нормально распределенное N(1,0) случайное число
	 * XXX: performance: возможно ускорить в 1.5-2 раза за счет генерации парами
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
	 * @return амплитуда коннектора, дБ с масштабом 5
	 */
	private static double getConnectorHeight(ModelEvent ev, Parameters pars) {
		double refl = ev.getReflection(); // уровень отражения
		double sigma = MathRef.calcSigma(pars.wavelength, pars.pulseWidth);
		return MathRef.calcPeakByReflectance(sigma, refl);
	}

	/**
	 * Определяет амплитуду коннектора со 100% отражением.
	 * Используется для оценки глубины просмотра дерева отражений,
	 * чтобы определить, где отражением можно пренебречь.
	 * @param pars параметры измерения
	 * @return амплитуда коннектора, дБ с масштабом 5
	 */
	private static double getMaxConnectorHeight(Parameters pars) {
		double sigma = MathRef.calcSigma(pars.wavelength, pars.pulseWidth);
		return MathRef.calcPeakByReflectance(sigma, 0.0);
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
	 * @param rtau обратная постоянная "времени" (координаты) затухания в волокне
	 * @return количество света в эаданном единичном временном интервале
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
	 * Быстрое (аналитическое) вычисление двойного интеграла, представленного
	 * в {@link #calcExpPulseIntSlow}. Описание параметров и возвращаемого
	 * значения см. там же.
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
		// определяем диапазон координат, затрагиваемые импульсом
		int iBeg = Math.max((int)Math.floor(posBegin), 0);
		int iEnd = Math.min((int)Math.floor(posEnd + pWidth), data.length - 1);
		for (int i = iBeg; i <= iEnd; i++) {
			data[i] += calcExpPulseInt(pWidth, posBegin, posEnd, i, rtau)
					* levelBegin;
		}
	}

	/**
	 * @param y дБ с масштабом 5
	 * @return интенсивность или отношение интенсивностей
	 */
	private static double log2lin(double y) {
		return Math.pow(10.0, y * 0.2);
	}
	/**
	 * @param power интенсивность или отношение интенсивностей
	 * @return дБ с масштабом 5
	 */
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

		double level0 = log2lin(this.pars.initialLevel);
		// рассчитываем рассеяние (S - процессы)
		// обеспечиваем переход от метров к точкам, от дБ к лин. ед.
		{
			double pos = 0; // текущая дистанция (точки)
			double level = level0; // текущий абс. уровень мощности (лин. ед.)
			// идем по трассе
			for (int k = 0; k < this.events.length; k++) {
				ModelEvent ev = events[k];
				// точечное отражение (в начале участка)
				//  - игнорируем
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
							pos, pos + len, level, rtau, pulseW);
					pos = endPos;
					level = endLevel;
				}
			}
		}
		// добавляем отражения (R) и фантомные отражения (RRR, RRRRR, ...)
		// указываем уровень сигнала, отражение которого будет незаметно
		// на фоне шума.
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
			double pos0, // суммарное время
			double level0,
			int index, // событие, с которого начали
			int recurse, // остающаяся глубина рекурсии (нечетные начальные значения давать смысла нет, это пустая трата времени)
			double pulseW,
			double[] data, // data - аккмулятор света
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
			// Когда идем в обратном направлении, игнорируем и отражения
			// (четные отражения не видны со стороны рефлектометра)
			// и потери (потери по пути назад уже учтены по пути вперед
			// за счет масштаба 5 дБ)
			if (!forward) {
				continue;
			}
			if (ev.hasReflection()) {
				// если идем в прямом направлении, то отражение будет
				// видно приемником рефлектометра
//				System.err.println("added[" + recurse + "]:" + k + " at " + pos);
				double factor = log2lin(getConnectorHeight(ev, this.pars));
				addSquarePulse(data, pos, pos + pulseW, level * factor);
			}
			// Учитываем затухание и протяженность.
			// Мы не можем здесь корректно добавить их в р/г
			// из-за того, что в таком случае из всех сигналов, полученных
			// двумя отражениями и рассеянием (ООР), будет добавляться только
			// тот, у которого сначала идет два отражения, а потом рассеяние.
			// Сигнал же, который после рассеяния еще был переотражен (ОРО, РОО),
			// не может быть здесь учтен простым способом.
			// Чтобы избежать некорректного учета таких процессов,
			// мы вообще не пытаемся их здесь учитывать, а оставляем это
			// методу generate().
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
	 * Пример создания модельной рефлектограммы
	 * @return генератор модельной рефлектограммы
	 */
	protected static TraceGenerator myTestGenerator1() {
		// Создаем набор моделируемых событий
		ModelEvent me[] = new ModelEvent[] {
				// начало
				ModelEvent.createReflective(1.0, -5),
				// todo ошибка - потеря точности при малых затуханиях
				// раскомментировать следующую строку для воспроизведения ошибки "малые затухания"
				//ModelEvent.createLinear(100.3, 1e-18),
				ModelEvent.createLinear(200.4, 2e-4),
				ModelEvent.createLinear(100.3, 1e-5),
				// сварка
				ModelEvent.createSlice(0.2),
				ModelEvent.createLinear(3000.0, 2e-4),
				// два близких отражения
				ModelEvent.createReflective(0.0, -5),
				ModelEvent.createLinear(80, 2e-4),
				ModelEvent.createReflective(1.0, -5),
				ModelEvent.createLinear(12000.0, 2e-4),
				// конец волокна
				ModelEvent.createReflective(1.0, -20)
		};

		// Создаем набор параметров тестирования
		Parameters pars = new Parameters(-15.0, -30.0, -40.0,
				4.0, 18000.0, 1625, 500, 1.468);

		// Выполняем моделирование
		return new TraceGenerator(pars, me);
	}

	/**
	 * Пример создания модельной рефлектограммы по входному файлу с описаниями.
	 * Формат входного файла недокументирован. В качестве примера использования
	 * рекомендуется {@link #myTestGenerator1()}
	 * @return генератор модельной рефлектограммы
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
	 * пример использования генератора рефлектограммы
	 */
	public static void main(String[] args)
	throws InternalError, IOException {
		// Пример создания генератора рефлектограммы (вместе с рефлектограммой).
		// Именно в этом примере инициализируются все необходимые параметры.
		TraceGenerator tg = myTestGenerator1();
		//TraceGenerator tg = myTestGenerator2();

		// При желании, проводим фильтрацию
//		tg.performRCFiltering(14);
//		tg.performBoxCarFiltering(9);

		//tg.printTrace(new PrintStream(new File("dump.txt")));

		// Сохраняем в Bellcore
		BellcoreStructure bs = tg.getBellcore();

		// Записываем Bellcore в файл
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

		// Сохраняем в текстовом формате
		double[] y = tg.getTraceData();
		PrintStream out = new PrintStream("./model.dat");
		for (int i = 0; i < y.length; i++)
			out.println("" + i + " " + y[i]);
		out.close();

		// Конец примера
		System.out.println("Done!");
	}
}
