package com.syrus.AMFICOM.analysis.dadara;

import java.io.*;

/**
 * <p>Represents r/g modelling functions with native implementation.</p>
 *
 * <p>Hides the implementation of f(x).
 * provides access to f(x) values, fitting procedure, RMS calculation,
 * Threshold function build procedure.
 * 
 * Does not cares for begin and end of <i>Amficom</i> events.
 * Does not remember the original r/g itself.</p>
 *
 * <p>Input X for the model function is integer.
 * The physical X-scale is not concerned.</p>
 *
 * <p>Output Y = f(X), its units are rgdB derived from Y=5.0*log_10(I).</p>
 *
 * <p>Should be constructed as one of three AMFICOM-specific simple functions.
 * The modelling function will probably change when fit() will be called.</p>
 *
 * @version $Revision: 1.7 $, $Date: 2005/02/08 11:46:27 $
 * @author $Author: saa $
 * @module analysis_v1
 */

public class ModelFunction {
	private int shapeID; // тип кривой (внутренний идентификатор native-кода)
	private double[] pars; // параметры кривой

	private native double nF(double x);
	private native double[] nFArray(double x0, double step, int length);
	private native void nChangeByACXL(double dA, double dC, double dX, double dL); // преобразование к своему ACXL-порогу
	private native void nChangeByThresh(Thresh[] thresh, int key); // изменение порогом Thresh
	private native void nFixThresh(Thresh[] thresh); // корректировка параметров Thresh в соответствии с м.ф. 
	//private native void nChangeByOther(int code, double[] changePars); // изменение другим специфическим способом
	private native double nRMS(double y[], int begin, int end); // end is included

	private static final int FITMODE_VARY_ALL = 1; // фитируем кривую, варьируем все параметры
	private static final int FITMODE_VARY_LIN = 2; // фитируем кривую, варьируя только линейные параметры
	private static final int FITMODE_SET_LINEAR = 3; // заменяем прямой
	private static final int FITMODE_SET_BREAKL = 4; // заменяем ломаной
	private static final int FITMODE_SET_SMOOTH = 5; // заменяем сглаживающей кривой -- пока не реализовано
	
	public static final int LINK_FIXLEFT = 0x1; // зафиксировать левую точку
	
	/**
	 * @deprecated use only for debugging
	 * @return Internal data: shape ID
	 */
	public int snoopShapeID()
	{
		return shapeID;
	}

	/**
	 * @deprecated Use only for debugging
	 * @return Internal data: parameters array
	 */
	public double[] snoopPars()
	{
		return pars;
	}

	/**
	 * Процедура фитировки.
	 * @param y кривая, к которой фитируемся. Индекс в массиве
	 * 	совпадает с X-координатой для модельной функции и варьируется
	 *  от begin до end. Полное число фитируемых точек = begin - end + 1.
	 * @param begin начальная точка (включительно)
	 * @param end конечная точка (включительно)
	 * @param fitMode режим фитировки. Задается константами FITMODE_*
	 *  Для режимов FITMODE_VARY_* используется текущий тип кривой,
	 *  и в качестве начального приближения - текущее состояние объекта.
	 *  Для режимов FITMODE_SET_* предыдущее состояние не используется,
	 *  т.е. фактически создается новая модельная функция.
	 * @param linkFlags флаги сшивки со смежными событиями.
	 *  На данный момент поддерживается только флаг LINK_FIXLEFT,
	 *  и только для фитировки ломаной.
	 * @param linkData0 Данные для сшивки по LINK_FIXLEFT.
	 *  (поскольку это всего одно число, оно пока реализовано
	 *  в виде одного числа double).
	 */
	private native void nFit1(double y[], int begin, int end, int fitMode,
			int linkFlags, double linkData0);

	/**
	 * Процедура фитировки. Описание остальных параметров см. в nFit1)
	 * @param errorR начальная отн. ошибка (+- дБ)
	 * @param errorA начальная абс. ошибка (дБ, ~ уровню шума по 3 сигма)
	 * @param maxPoints макс. число точек
	 */
	private native void nFit2(double y[], int begin, int end, int fitMode,
			int linkFlags, double linkData0,
			double errorR, double errorA, int maxPoints);

	/**
	 * Процедура фитировки. Описание остальных параметров см. в nFit1)
	 * @param error желаемая точность фитировки вдоль кривой
	 */
	private native void nFit3(double y[], int begin, int end, int fitMode,
			int linkFlags, double linkData0,
			double[] error);

	/**
	 * Получить значение заданного параметра (по его имени)
	 * @param name имя параметра (поддерживаются только ASCII-имена)
	 * @param default_value значение по умолчанию на случай, если
	 *  параметр не определен.
	 * @return значение параметра
	 */
	private native double nGetAttr(String name, double default_value);

	/**
	 * Самая примитивная инициализация - как линейной функции y = 0 * x + 0
	 */
	private native void nInitAsLinear();

	/**
	 * Инициализация функции как прямой по двум точкам.
	 * @param x1 x-координата 1-й точки
	 * @param y1 y-коодрината 1-й точки
	 * @param x2 x-координата 2-й точки
	 * @param y2 y-координата 2-й точки
	 */
	private native void nSetAsLinear(
			int x1, double y1, int x2, double y2);

	/**
	 * Конструктор.
	 * Предназначен для создания объекта в производящих методах,
	 * операторе copy() и в native-коде.
	 *
	 */
	private ModelFunction() {
		shapeID = 0;
		pars = null;
	}

	/**
	 * Создает модельную функцию линейного типа (ax+b).
	 * Может быть использован как производящий метод.
	 * 
	 * @return модельная функция линейного типа ax+b, a=0, b=0 
	 */
	public static ModelFunction createLinear()
	{
		ModelFunction ret = new ModelFunction();
		ret.nInitAsLinear();
		return ret;
	}

	public void setAsLinear(
			int x1, double y1, int x2, double y2)
	{
		nSetAsLinear(x1, y1, x2, y2);
	}

	/*public int steal_shapeID() // debug etc
	 {
	 return shapeID;
	 }*/

	/**
	 * @return The width of the event.
	 * Gives zero for linears; transition region width for welds;
	 * front-to-edge distance for connectors.
	 * 
	 * @deprecated Undefined for currently used curves!
	 */
	public double getWidth0() {
		return nGetAttr("width", 0);
	}

	/**
	 * @return the height of the event front.
	 * @deprecated Undefined for currently used curves!
	 */
	public double getFrontHeight0() {
		return nGetAttr("fHeight", 0);
	}

	/**
	 * @return the y-step of the WELD(SPLICE) event; usually it is negative.
	 */
	public double getWeldStep0() {
		System.out.println("weldStep = " + nGetAttr("weldStep", 0)); // FIXIT
		return nGetAttr("weldStep", 0);
	}
	
	public double getEstimatedNoiseSuppressionLength()
	{
	    return nGetAttr("noiseSuppressionLength", 1.0);
	}
	
	public boolean allowsLeftLinking()
	{
		return nGetAttr("canLeftLink", 0.0) != 0.0;
	}

	/**
	 * Shifts the curve to left as f_new(x) = f_old(x+dx).
	 * @param dx X shift distance
	 */
	public void shiftX(double dx) {
		//nShiftX(dx);
		nChangeByACXL(0, -dx, 0, 0);
	}

	/**
	 * Shifts the curve down as f_new(x) = f_old(x) - dy.
	 * @param dy Y shift distance
	 */
	public void shiftY(double dy) {
		//nShiftY(dy);
		nChangeByACXL(-dy, 0, 0, 0);
	}

	/**
	 * makes a deep copy (equal to clone) of this
	 * @return a copy
	 */
	public ModelFunction copy() {
		ModelFunction that = new ModelFunction();
		that.shapeID = this.shapeID;
		if (this.pars != null) {
			int len = this.pars.length;
			that.pars = new double[len];
			int i;
			for (i = 0; i < this.pars.length; i++)
				that.pars[i] = this.pars[i];
		}
		return that;
	}

	/**
	 * Calculates f(x) at one point
	 *
	 * @param x is arg of f(x)
	 * @return value of f(x)
	 */
	public double fun(double x) {
		return nF(x);
	}

	/**
	 * Fills the array with the values of f(x).
	 *
	 * @param x0      initial x (i.e. begin)
	 * @param step    x step (typically 1.0)
	 * @param length  number of points to evaluate (i.e. end + begin - 1)
	 * @return        the array, i-th element being equal f(x0 + step * i)
	 */
	public double[] funFillArray(double x0, double step, int length) {
		return nFArray(x0, step, length);

		// эквивалентная (но медленная) реализация:
		//double ret[] = new double[length];
		//for (int i = 0; i < length; i++)
		//  ret[i] = nF(x0 + step * i);
		//return ret;
	}

	/**
	 * Вычисляет среднеквадратичное отклонение между моделью и р/г
	 *
	 * @param y      рефлектограмма
	 * @param begin  начальный индекс
	 * @param end    конечный индекс включительно
	 * @return       среднеквадратичное отклонение
	 */
	public double calcRMS(double y[], int begin, int end) {
		return nRMS(y, begin, end);
	}

	/**
	 * Вычисляет максимальное отклонение между моделью и р/г.
	 * довольно медленно, но важно ли это, если метод не используется? 
	 * 
	 * @param y	    рефлектограмма
	 * @param begin   начальный индекс 
	 * @param end     конечный индекс включительно
	 * @return		максимальное по модулю отклонение
	 */
	public double calcMaxDeviation_unused(double y[], int begin, int end) {
		double ret = 0;
		for (int i = begin; i < end; i++) {
			double dev = Math.abs(nF(i) - y[i]);
			if (dev > ret)
				ret = dev;
		}
		return ret;
	}

	/**
	 * Performs fitting
	 *
	 * @param y      real r/g data array
	 * @param begin  starting fitting array index
	 * @param end    ending fitting array index (length is end - begin + 1)
	 * @param linkFlags флажки для связывания с соседними событиями (пока только LINK_FIXLEFT)
	 * @param linkData0 данные для флажка LINK_FIXLEFT
	 */
	public void fit(double y[], int begin, int end, int linkFlags, double linkData0)
	{
	    //System.out.println("fit-3: linkFlags " + linkFlags);
		nFit1(y, begin, end, FITMODE_VARY_ALL, linkFlags, linkData0);
	}

	/**
	 * Performs fitting
	 *
	 * @param y      real r/g data array
	 * @param begin  starting fitting array index
	 * @param end    ending fitting array index (length is end - begin + 1)
	 * @param linkFlags флажки для связывания с соседними событиями (пока только LINK_FIXLEFT)
	 * @param linkData0 данные для флажка LINK_FIXLEFT
	 * @param errorR [test] undocumented
	 * @param errorA [test] undocumented
	 * @param maxPoints [test] undocumented
	 */
	public void fit(double y[], int begin, int end,
			int linkFlags, double linkData0,
			double errorR, double errorA, int maxPoints)
	{
	    //System.out.println("fit-2: linkFlags " + linkFlags);
		nFit2(y, begin, end, FITMODE_VARY_ALL,
			linkFlags, linkData0,
			errorR, errorA, maxPoints);
	}

	/**
	 * Performs fitting
	 *
	 * @param y      real r/g data array
	 * @param begin  starting fitting array index
	 * @param end    ending fitting array index (length is end - begin + 1)
	 * @param linkFlags флажки для связывания с соседними событиями (пока только LINK_FIXLEFT)
	 * @param linkData0 данные для флажка LINK_FIXLEFT
	 * @param noise рекомендуемый уровень ошибки
	 */
	public void fit(double y[], int begin, int end, int linkFlags, double linkData0,
			double[] noise)
	{
	    //System.out.println("fit-3: linkFlags " + linkFlags);
		nFit3(y, begin, end, FITMODE_VARY_ALL, linkFlags, linkData0,
			noise);
	}

	/**
	 * Performs linear fitting
	 *
	 * @param y      real r/g data array
	 * @param begin  starting fitting array index
	 * @param end    ending fitting array index (length is end - begin + 1)
	 * @param linkFlags флажки для связывания с соседними событиями (пока только LINK_FIXLEFT)
	 * @param linkData0 данные для флажка LINK_FIXLEFT
	 */
	public void fitLinearOnly(double y[], int begin, int end, int linkFlags, double linkData0)
	{
	    //System.out.println("fit-1(Lin): linkFlags " + linkFlags);
		nFit1(y, begin, end, FITMODE_VARY_LIN, linkFlags, linkData0);
	}

	/**
	 * Смещает профиль кривой, преобразуя его в АМФИКОМовский ACXL-порог.
	 * Может
	 * 
	 * @param ACXL  массив из четырех double порогов:
	 * ACXL[0]: A - сдвиг вверх,
	 * ACXL[1]: C - сдвиг вправо,
	 * ACXL[2]: X - изменение полуширины относительно центра,
	 * ACXL[3]: L - изменение амплитуры (размаха), относительно базового уроня.
	 */
	public void changeByACXLThreshold(double[] ACXL) {
		nChangeByACXL(ACXL[0], ACXL[1], ACXL[2], ACXL[3]);
	}

//	public void changeByOther0Threshold(double[] changePars)
//	{
//		//nChangeByOther(0, changePars);
//	}
	public void changeByThresh(Thresh[] thresh, int key)
	{
		nChangeByThresh(thresh, key);
	}

	public void fixThresh(Thresh[] thresh)
	{
//		System.err.print("fixThresh: A:");
//		for (int i = 0; i < thresh.length; i++)
//			System.err.print(" (" + thresh[i].xMin + "," + thresh[i].xMax + ")");
//		System.err.println();

		nFixThresh(thresh);

//		System.err.print("fixThresh: B:");
//		for (int i = 0; i < thresh.length; i++)
//			System.err.print(" (" + thresh[i].xMin + "," + thresh[i].xMax + ")");
//		System.err.println();
	}

	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeInt(shapeID);
		dos.writeInt(pars.length);
		for (int i = 0; i < pars.length; i++)
			dos.writeDouble(pars[i]);
	}

	public static ModelFunction createFromDIS(DataInputStream dis) throws IOException {
	    ModelFunction mf = new ModelFunction();
	    mf.readFromDIS(dis);
		return mf;
	}

	private void readFromDIS(DataInputStream dis) throws IOException {
		shapeID = dis.readInt();
		int npars = dis.readInt();
		pars = new double[npars];
		for (int i = 0; i < npars; i++)
			pars[i] = dis.readDouble();
	}
}

