/**
 * ReflectogramEvent.java
 * 
 * @version $Revision: 1.10 $, $Date: 2004/12/15 12:02:20 $
 * @author $Author: saa $
 * @module general_v1
 */

/*
 * Два варианта создания объектов:
 * 1. Native-код AnalysisManager'а
 * 2. static метод fromByteArray() 
 */

// TODO: упорядочить методы согласно хотя бы доступу

package com.syrus.AMFICOM.analysis.dadara;

import java.util.Map;
import java.util.Iterator;
import com.syrus.io.BellcoreStructure;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReflectogramEvent
{
    //private static final long serialVersionUID = 8468909716459300200L;
    private static final long signature = 8468920041210182200L;
    
    // event types
	public static final int RESERVED_VALUE = -1; // заведомо не использующееся значение
	public static final int LINEAR = 1;
	public static final int WELD = 2;
	public static final int CONNECTOR = 3;
	public static final int SINGULARITY = 4;

	// store flags for int vars
	private static final int STORE_MASK_INT		= 0xff;
	private static final int STORE_linkFlags	= 0x01;

	// store flags for long vars
	private static final int STORE_MASK_LONG	= 0xff00;

	// store flags for double vars
	private static final int STORE_MASK_DOUBLE	= 0xff0000;
	private static final int STORE_yB			= 0x010000;
	private static final int STORE_yE			= 0x020000;
	private static final int STORE_aLet			= 0x040000;
	private static final int STORE_asympY0		= 0x080000;
	private static final int STORE_asympY1		= 0x100000;
	private static final int STORE_mloss		= 0x200000;

	private static final int STORE_FLAGS
		= STORE_yB
		| STORE_yE
		| STORE_aLet
		| STORE_asympY0
		| STORE_asympY1
		| STORE_mloss
		| STORE_linkFlags;

	private static int LINK_FIXLEFT = ModelFunction.LINK_FIXLEFT;

	private int begin; // начальная точка
	private int end; // последняя точка включительно: длина = end - begin + 1
	private int eventType; // тип события (как определено)
	private int thresholdType; // тип для маски (как задано)
	private ModelFunction mf;
	private int linkFlags;
	private double delta_x = 1.; // XXX

	private FittingParameters fittingParameters;

	private Threshold threshold;

	// XXX
	// "краевые значения"
	// вычисляются во время doFit()
	// для лин. событий - на основе лин. аппрокс.
	// для нел. событий - на основе значений р/г в конечных точках
	private double yB; // Y в точке begin
	private double yE; // Y в точке end

	// XXX
	// аппроксимации с учетом смежных событий на основе calcMutualParameters
	// для лин. события - по его собств. аппрокс.,
	// для нел. с лин. соседом - по соседу,
	// для нел. с нел. соседом (или без соседа) - по значению р/г в точке
	private double asympY0; // слева
	private double asympY1; // справа

	//////////////////////
	// loss, reflectance -- используются в сравнении,
	// так же сохраняются на сервер.
	// TODO: оформить в виде производного класса
	// (поэтому мало толку от getter/setter-методов)
	// mloss хранит усиление - причем для нелин. событий должна бы
	// быть величина соотв. ступеньки, а не просто
	// (FIXME - как сейчас) полное смещение р/г на длине события
	private double mloss; // для нел. событий - усиление, для лин. - (-1) * ослабление
	private double aLet; // величина всплеска вверх (для определения отражения)
	
	// cache part

	private ReflectogramEvent[] cacheThresholdData; // 4 elements
	private long cacheThresholdSerialNumber;
	private double cacheRefAmplitude[]; // end + 1 - begin elements
	
	// left-right linking
	
	private ReflectogramEvent eventAtLeft;
	private ReflectogramEvent eventAtRight;
	
	// methods
	
	private ModelFunction getThresholdBaseMF()
	{
		ModelFunction ret;
		if (thresholdType == ReflectogramEvent.LINEAR && false) // true приведет к тому, что пороги окажутся вне мф
		{
			ret = ModelFunction.CreateLinear();
			ret.SetAsLinear(begin, yB, end, yE); // NB: needs yB, yE
		}
		else
			ret = mf.copy();
		return ret;
	}
	
	public int getBegin()
	{
		return begin;
	}

	public int getEnd()
	{
		return end;
	}

	public void setBegin(int x)
	{
		// XXX: need to refit mf?
		begin = x;
		updated();
	}

	public void setEnd(int x)
	{
		// XXX: need to refit mf?
		updated();
		end = x;
	}

	public int getEventType()
	{
		return eventType;
	}

	public void setEventType(int newType)
	{
		eventType = newType; // XXX, it is used but not possible!
		thresholdType = newType;
		updated();
		// todo: recalc yB, yEp?
	}

	public int getThresholdType()
	{
		return thresholdType;
	}

	public void shiftY(double dy)
	{ // seems to be used in comparison
		mf.shiftY(dy);
		updated();
	}

	//  seems to be used only in event-based comparer (to get nearest event)
	// XXX: i guess the use of getMiddle in such a way is not a best idea //saa
	public int getMiddle()
	{
		return (begin + end) / 2;
	}

	public Threshold getThreshold()
	{
		return threshold;
	}

	public void setThreshold(Threshold threshold)
	{
		this.threshold = threshold;
		cacheThresholdInvalidate();
	}

	/*
	 * public int steal_shapeID() { return mf.steal_shapeID(); }
	 */

	public void setDeltaX(double delta_x)
	{
		this.delta_x = delta_x;
		updated();
	}

	public double getDeltaX()
	{
		return this.delta_x;
	}

	// можно вызывать только после doFit()
	private double getAsympYB()
	{
		return yB;
	}
	private double getAsympYE()
	{
		return yE;
	}
	
	// только после doFit() + calcMutialParameters()
	public double getAsympY0()
	{
		return asympY0;
	}

	public double getAsympY1()
	{
		return asympY1;
	}

	public double getWidth0()
	{
		return (end - begin) * delta_x;
	}

	/*
	 * public double getWidth() { if (getType() == LINEAR) return end - begin;
	 * else return mf.getWidth(); }
	 */

	// XXX: не самый хороший способ определения амплитуды коннектора
	// XXX: дополнительная плохость - необходимость предварительной фитировки
	// XXX: относительно медленная реализация
	private double getYMax1()
	{
		double yMax = refAmplitude(begin);
		for (int i = begin + 1; i <= end; i++)
		{
			if (yMax < refAmplitude(i))
				yMax = refAmplitude(i);
		}
		return yMax;
	}

	// Расчет параметров
	// нужно предварительное doFit()
	public double getMLoss()
	{
		return mloss;
	}
	public double getALet()
	{
		return aLet;
	}

	public static void createLeftRightLinks(ReflectogramEvent[] re)
	{
		int len = re.length;
		re[0].eventAtLeft = null;
		for (int i = 1; i < len; i++)
			re[i].eventAtLeft = re[i - 1];
		re[len - 1].eventAtRight = null;
		for (int i = 1; i < len - 1; i++)
			re[i].eventAtRight = re[i + 1];
	}

	/**
	 * Заполняет поля mloss и reflectance, анализируя весь массив событий.
	 * (эдакий пост-анализ)
	 * 
	 * TODO: вынести это из этого класса в производный, либо как-то еще
	 * (например, в IA)
	 * 
	 * @param re
	 *            массив событий
	 * @param y
	 *            рефлектограмма, по которым он получен
	 */
	public static void calcMutualParameters(ReflectogramEvent[] re, double[] y)
	{
		for (int i = 0; i < re.length; i++)
		{
			// определяем "асимптотические" значения слева и справа
			// если наш участок нелинейный,
			// а соответствующий смежный участок линейный,
			// то берем из смежного, иначе - со своего.
			// TODO: переделать?

			double asympB = re[i].getAsympYB();
			double asympE = re[i].getAsympYE();
			if (re[i].getEventType() != ReflectogramEvent.LINEAR)
			if (i > 0 && re[i - 1].getEventType() == ReflectogramEvent.LINEAR)
				asympB = re[i - 1].getAsympYE();
			if (i < re.length - 1
					&& re[i + 1].getEventType() == ReflectogramEvent.LINEAR)
				asympE = re[i + 1].getAsympYB();

			re[i].mloss = asympE - asympB;

			double yMax = y[re[i].getBegin()];
			for (int j = re[i].getBegin(); j < re[i].getEnd(); j++)
			{
				if (yMax < y[j])
					yMax = y[j];
			}

			// XXX: вместо asympB лучше брать усредненное значение в конце
			// предыдущего события
			re[i].aLet = yMax - asympB;
			re[i].asympY0 = asympB;
			re[i].asympY1 = asympE;
		}
		
		// теперь, когда в класс введены ссылки createLeftRightLinks,
		// многие вещи можно вынести из calcMutualMarametes - TODO

		createLeftRightLinks(re);
	}

	// рассчитываем массив значений на всей длине события
	private void refAmplitudeFillCache()
	{
		cacheRefAmplitude = mf.funFillArray(begin, 1, end - begin + 1);
	}

	public double refAmplitude(int x)
	{
		// значения из [begin .. end] ищем в кэше,
		// при необходимости заполняем кэш
		if (x >= begin && x <= end)
		{
			if (!cacheRefAmplitudeIsValid())
				refAmplitudeFillCache();

			return cacheRefAmplitude[x - begin];
		} else
		{
			//System.out.println("O: "+this+" [ "+x+" !in
			// ["+begin+";"+end+"]");
			//if (x != 0)
			//	throw new Error();
			return mf.fun(x);
		}
	}

	/*
	 * public void doFit(double[] y, int fitMode) { doFit(y, fitMode, 0, 0.0,
	 * 0.0); }
	 */

	public static class FittingParameters
	{
		public double[] y;
		public int errorMode;
		public double error1;
		public double error2;
		public FittingParameters(double[] y, int errorMode, double error1, double error2)
		{
			this.y = y;
			this.errorMode = errorMode;
			this.error1 = error1;
			this.error2 = error2;
		}
	}

	// errorMode:
	//	0: default mode (best fit)
	//  2: rgdB noise level is supplied

	public void setFittingParameters(FittingParameters fPars)
	{
		this.fittingParameters = fPars;
	}
	public void doFit()
	{
		int maxpoints = eventType == CONNECTOR
							? 25 // FIXIT
							: 5; // FIXIT
		FittingParameters fP = fittingParameters;
		mf.fit(fP.y, begin, end,
			fP.errorMode, fP.error1, fP.error2, maxpoints,
			activeLinkFlags(), activeLinkData0());
		
		updated();

		if (eventType == LINEAR)
		{
			//System.out.println("lin fit");
			// лин. аппроксимация для лин. событий
			// (this.mf не обязательно линейна, это м б ломаная)
			ModelFunction lin = ModelFunction.CreateLinear();
			lin.fit(fP.y, begin, end, 0, 0.0);
			yB = lin.fun(begin);
			yE = lin.fun(end);
		} else
		{
			// грубая оценка - по самой р/г
			yB = fP.y[begin];
			yE = fP.y[end];
		}
	}

	public double getRMSDeviation(double[] y)
	{
		return mf.calcRMS(y, begin, end);
	}
	
	// вычисляет отклонение ЭТОЙ функции от ТОЙ
	private double[] getMaxDeviationPM(ModelFunction that)
	{
		double[] maxDev = new double[2];
		for (int i = begin; i <= end; i++)
		{
			double val = refAmplitude(i) - that.fun(i);
			if (val > maxDev[0])
				maxDev[0] = val;
			if (val < maxDev[1])
				maxDev[1] = val;
		}
		return maxDev;
	}

	public double getMaxDeviation(double[] y)
	{
		double maxDev = 0;
		for (int i = begin; i <= end; i++)
		{
			double val = Math.abs(refAmplitude(i) - y[i]);
			if (val > maxDev)
				maxDev = val;
		}
		return maxDev;
	}
	
	private ReflectogramEvent copyWithoutMFAndTh()
	{
		ReflectogramEvent ret = new ReflectogramEvent();
		ret.threshold = null;
		ret.begin = begin;
		ret.end = end;
		ret.mf = null;
		ret.delta_x = delta_x;
		ret.mloss = mloss;
		ret.aLet = aLet;
		ret.asympY0 = asympY0;
		ret.asympY1 = asympY1;
		ret.eventType = eventType;
		ret.thresholdType = thresholdType;
		ret.yB = yB;
		ret.yE = yE;
		return ret;
	}

	/**
	 * NOTE: the copy is partially swallow, it do not copy the threshold, only
	 * its reference XXX: I don't know if it matters. //saa It is also partially
	 * deep since it does copy mf, and this does matter.
	 * 
	 * @return the partially swallow copy of this
	 */
	public ReflectogramEvent copy()
	{
		ReflectogramEvent ret = copyWithoutMFAndTh();
		ret.threshold = threshold; // XXX: три варианта - null, reference, deep
		// copy; неизвестно какой правильнее
		ret.mf = mf.copy();
		return ret;
	}

	// byte array presentation functions are untested
	public void writeToDOS(DataOutputStream dos) throws IOException
	{
	    dos.writeLong(signature);
		dos.writeInt(begin);
		dos.writeInt(end);
		dos.writeInt(eventType);
		dos.writeInt(thresholdType);
		dos.writeDouble(delta_x);
		mf.writeToDOS(dos);
		dos.writeInt(STORE_FLAGS);
		int i;
		for (i = 1; i != 0; i = i * 2)
		{
			if ((STORE_FLAGS & i) == 0)
			    continue;

			switch (i)
			{
			case STORE_yB:
			    dos.writeDouble(yB);
				break;
			case STORE_yE:
			    dos.writeDouble(yE);
				break;
			case STORE_aLet:
			    dos.writeDouble(aLet);
				break;
			case STORE_mloss:
			    dos.writeDouble(mloss);
				break;
			case STORE_asympY0:
			    dos.writeDouble(asympY0);
				break;
			case STORE_asympY1:
			    dos.writeDouble(asympY1);
				break;
			case STORE_linkFlags:
			    dos.writeInt(linkFlags);
				break;
			}
		}
		// we do not write thresholds
	}

	/**
	 * Считывает все параметры события с потока.
	 * Метод сделан private на всякий случай на будущее,
	 * когда, возможно, будут сохраняться не все параметры
	 * и придется вычислять из с использованием смежных событий.
	 * @param dis Входной поток
	 * @throws IOException, SignatureMismatchException
	 */
	private void readFromDIS(DataInputStream dis) throws IOException, SignatureMismatchException
	{
	    long tsig = dis.readLong();
	    if (tsig != signature)
	    {
	        throw new SignatureMismatchException();
	    }
		begin = dis.readInt();
		end = dis.readInt();
		eventType = dis.readInt();
		thresholdType = dis.readInt();
		delta_x = dis.readDouble();
		mf = ModelFunction.createFromDIS(dis);
		int flags = dis.readInt();
		int i;
		for (i = 1; i != 0; i = i * 2)
		{
			if ((flags & i) != 0)
			{
			    int ti = 0;
			    long tl = 0; // unused
			    double td = 0;
			    if ((i & STORE_MASK_INT) != 0)
			        ti = dis.readInt();
			    if ((i & STORE_MASK_LONG) != 0)
			        tl = dis.readLong();
			    if ((i & STORE_MASK_DOUBLE) != 0)
			        td = dis.readDouble();
				switch (i)
				{
				case STORE_yB:
					yB = td;
					break;
				case STORE_yE:
					yE = td;
					break;
				case STORE_aLet:
					aLet = td;
					break;
				case STORE_mloss:
					mloss = td;
					break;
				case STORE_asympY0:
					asympY0 = td;
					break;
				case STORE_asympY1:
					asympY1 = td;
					break;
				case STORE_linkFlags:
					linkFlags = ti;
				}
			}
		}
		// do not read thresholds

		updated();
	}

	// xtodo: change name: toByteArray -> arrayToBytes
	// xtodo: use stream operations instead of byte arrays - for simplicity
	public static byte[] toByteArray(ReflectogramEvent[] revents)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeInt(revents.length);
			for (int i = 0; i < revents.length; i++)
				revents[i].writeToDOS(dos);
			return baos.toByteArray();
		} catch (IOException e)
		{
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return null; // XXX
		}
	}

	public static ReflectogramEvent[] fromByteArray(byte[] bar)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			int len = dis.readInt();
			ReflectogramEvent[] ret = new ReflectogramEvent[len];
			for (int i = 0; i < len; i++)
			{
				ret[i] = new ReflectogramEvent();
				ret[i].readFromDIS(dis);
			}
			return ret;
		}
		catch (IOException e)
		{
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return null; // XXX
		}
		catch (SignatureMismatchException e)
		{
			System.out.println("SignatureMismatchException caught: " + e);
			e.printStackTrace();
			return null; // XXX
		}
	}

	private ReflectogramEvent makeThresholdEvent(Threshold th,
			int thresholdNumeral)
	{
		ReflectogramEvent ret = this.getThresholdBaseRE();
		double[] thresholdValues = th.getThresholds(thresholdNumeral);
		switch (thresholdType)
		{
		case LINEAR:
			// fall through
		case WELD:
			thresholdValues[1] = 0;
			thresholdValues[2] = 0;
			// fall through
		case SINGULARITY:
			// fall through
		case CONNECTOR:
			break;
		}

		ret.mf.changeByACXLThreshold(thresholdValues);
		return ret;
	}

	public ReflectogramEvent getThresholdReflectogramEvent(int thresholdNumeral)
	{
		//кэширование результата makeThresholdEvent в thresholdsCache
		if (thresholdNumeral < 4)
		{
			if (!cacheThresholdIsValid())
			{
				cacheThresholdData = new ReflectogramEvent[4];
				cacheThresholdSerialNumber = threshold.getSerialNumber();
			}

			if (cacheThresholdData[thresholdNumeral] == null)
			{
				cacheThresholdData[thresholdNumeral] = makeThresholdEvent(
						threshold, thresholdNumeral);
			}

			return cacheThresholdData[thresholdNumeral];
		}
		return makeThresholdEvent(threshold, thresholdNumeral);
	}

	public void changeThresholdType(int newType, double[] y)
	{
		thresholdType = newType;
		System.out.println("changeThresholdType: y = " + y);
		if (y != null)
			setDefaultThreshold(y);
		cacheThresholdInvalidate();
	}

	// выставляет порог, рекомендуемый для рефлектограммы
	public void setDefaultThreshold_old2(double[] y)
	{
		// составляющая, соответствующая оценке шума
		// основана на разнице модельной функции события и р/г
		double noiseDev = Math.max(getMaxDeviation(y) * 0.5,
				getRMSDeviation(y) * 3.0); // XXX

		// составляющая, соответствующая разнице базовой мф порога
		// и р/г
		ModelFunction base = getThresholdBaseMF();
		double[] dif = ReflectogramMath.getMaxDifPM(y, base, begin, end);
		threshold.initFromDY(
				dif[0] - noiseDev * 2,
				dif[0] - noiseDev,
				dif[1] + noiseDev,
				dif[1] + noiseDev * 2);
	}

	private int activeLinkFlags()
	{
		return
			eventAtLeft != null
			&& ((linkFlags & LINK_FIXLEFT) != 0)
			&& eventAtLeft.end == begin
						? LINK_FIXLEFT
						: 0;
	}

	// XXX:
	// Метод возвращает корректное значение только после того
	// как левое событие профитируется.
	// Посколько метод используется в фитировке, мы сильно
	// полагаемся на то, что фитировка будет проводиться слева направо.
	private double activeLinkData0()
	{
		return ((activeLinkFlags() & LINK_FIXLEFT) != 0)
		? eventAtLeft.mf.fun(begin) // значение левого события на стыке событий 
		: 0;
	}

	// выставляет порог, рекомендуемый для модельной функции,
	// фитируемой к эталонной в линейном режиме.
	// Если указаны primaryTrace и bellcoreTraces,
	// и среди bellcoreTraces есть хотя бы две совместимые по параметрам
	// с primaryTrace, то добавляет также оценку
	// отклонения по разбросу р/г
	// если указан primaryTrace, он должен совпадать с кривой y
	// <br>
	// NB: эта функция должна вызываться ПОСЛЕ calcMutualParameters
	// т.к. она использует флаг leftLink и поэтому может зависеть от
	// смежных событий. TODO(?): если ссылок на смежные события нет,
	// то не падать, а просто игнорировать leftLink.
	protected void setDefaultThreshold(double[] y, BellcoreStructure primaryTrace, Map bellcoreTraces)
	{
		//System.out.println("setDefaultThreshold: pTrace " + primaryTrace + " bellcoreTraces " + bellcoreTraces);
		//if (bellcoreTraces != null)
		//    System.out.println("bellcoreTraces.size() " + (bellcoreTraces.size()));

		// составляющая, соответствующая оценке шума
		// основана на разнице модельной функции события и р/г
		double noiseDev = Math.max(getMaxDeviation(y) * 1.0, // XXX
				getRMSDeviation(y) * 5.0); // XXX

		// оцениваем фактор уменьшения уровня флуктуаций при сглаживании
		double noiseSL = mf.getEstimatedNoiseSuppressionLength();
		double typicalAverageLength = 10; // NetTest Specific (8..30) // XXX
		
		// оценка флуктуаций сглаженной кривой
		double noiseModel =
			noiseSL > typicalAverageLength
					? noiseDev / Math.sqrt(noiseSL / typicalAverageLength)
					: noiseDev;
					
		// определяем макс. и мин. модели отклонения в выбранном наборе
		double maxDevP = 0;
		double maxDevM = 0;
		//int noiseSetCount = 0;
		BellcoreStructure pTrace = primaryTrace;
		if (pTrace != null && bellcoreTraces != null)
		{
			int count = 0;
			for (Iterator it = bellcoreTraces.keySet().iterator(); it.hasNext();)
			{
				// берем очередную р/г из списка
				Object key = it.next();
				BellcoreStructure bs = (BellcoreStructure )bellcoreTraces.get(key);
				
				// пропускаем рефлектограммы, несовпадающие по условиям измерения 
				final boolean strictMode = true;
				if (bs.getRange() != pTrace.getRange()
						|| bs.getResolution() != pTrace.getResolution()
						|| strictMode
						&& (bs.getWavelength() != pTrace.getWavelength() || bs.getPulsewidth() != pTrace.getPulsewidth()))
				{
					//System.out.println("ReflectogramEvent: setDefaultThresholds: reflectogram ignored: " + key.toString()); // слишком обильный вывод - выводит список на каждое событие
					continue;
				}
				//System.out.println("ReflectogramEvent: setDefaultThresholds: reflectogram accepted: " + key.toString());

				// пропускаем рефлектограммы, несовпадающие по длине
				double[] yt = bs.getTraceData();
				if (yt == null || yt.length != y.length)
					continue;

				// смотрим maxDev нашей модельной функции
				ModelFunction temp = mf.copy();
				temp.fitLinearOnly(yt, begin, end, activeLinkFlags(), activeLinkData0());
				double[] maxDev = getMaxDeviationPM(temp);
				//System.out.println("ReflectogramEvent: setDefaultThresholds: maxDev = (" + maxDev[0] + ", " + maxDev[1] + ")");

				count++;

				// нас интересует знак отклонение ТОЙ функции от ЭТОЙ,
				// а getMaxDeviationPM вычисляет отклонение ЭТОЙ от ТОЙ.
				// Здесь мы учтем это, поменяв maxDevP и maxDevM знаками и местами
				if (-maxDev[1] > maxDevP)
					maxDevP = -maxDev[1];
				if (-maxDev[0] < maxDevM)
					maxDevM = -maxDev[0];
			}
			//System.out.println("setDefaultThreshold: count " + count);
		}

		// устанавливаем пороги

		double maxDiff = maxDevP - maxDevM; // разброс
		double width0 = noiseModel * 1.0 + maxDiff * 0.1; // XXX

		//System.out.println("ReflectogramEvent: setDefaultThresholds: noiseModel " + noiseModel + " maxDevP " + maxDevP + " maxDevM " + maxDevM);

		threshold.initFromDY(
			maxDevM - width0 * 2,
			maxDevM - width0,
			maxDevP + width0,
			maxDevP + width0 * 2);
	}

	public void setDefaultThreshold(double[] y)
	{
		setDefaultThreshold(y, null, null);
	}

	public void setDefaultThreshold(BellcoreStructure primaryTrace, Map allTraces)
	{
		double y[] = primaryTrace.getTraceData();
		setDefaultThreshold(y, primaryTrace, allTraces);
	}

	// returns true if event has changes
	// needs fitting parameters already initialized (FIXME?)
	public boolean setLeftLink(boolean leftLink)
	{
		int newLinkFlags = linkFlags & ~LINK_FIXLEFT;
		if (leftLink)
			newLinkFlags |= LINK_FIXLEFT;
		if (newLinkFlags == linkFlags)
			return false;
		linkFlags = newLinkFlags;
		doFit();
		updated();
		return true;
	}

	public boolean getLeftLink()
	{
		return (linkFlags & LINK_FIXLEFT) != 0;
	}

	// cache methods

	private void cacheThresholdInvalidate()
	{
		cacheThresholdData = null;
	}

	private boolean cacheThresholdIsValid()
	{
		return cacheThresholdData != null
				&& cacheThresholdSerialNumber == threshold.getSerialNumber();
	}

	private void cacheRefAmplitudeInvalidate()
	{
		cacheRefAmplitude = null;
	}

	private boolean cacheRefAmplitudeIsValid()
	{
		return cacheRefAmplitude != null;
	}

	private void updated()
	{
		cacheRefAmplitudeInvalidate();
		cacheThresholdInvalidate();
	}

	// end of cache methods

	// XXX: зачем копировать?
	// может, лучше совсем отказаться от использовании RE в верхнем и нижнем порогах?
	private ReflectogramEvent getThresholdBaseRE()
	{
		ReflectogramEvent ret = this.copyWithoutMFAndTh();
		ret.threshold = null; // undefined -- XXX
		ret.mf = getThresholdBaseMF();
		return ret;
	}
   
	private ReflectogramEvent()
	{
		threshold = new Threshold(); // для амфикома нужно значение по умолчанию
	} // FIXIT

}
