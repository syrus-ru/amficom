package com.syrus.AMFICOM.analysis.dadara;

import java.io.*;

/**
 * <p>Represents r/g modelling functions with native implementation.</p>
 *
 * <p>Hides the implementation of f(x).
 * provides access to f(x) values, fitting procedure, RMS calculation.
 * Does not cares for X range limitation,
 * does not contain any info concerning original r/g.</p>
 *
 * <p>Input X is integer. The physical X-scale is not concerned.</p>
 * <p> <i>Amficom:</i> X ranges within [begin..end]
 *
 * <p>Output Y = f(X), its units are rgdB derived from Y=5.0*log_10(I).</p>
 *
 * <p>Should be constructed as one of three AMFICOM-specific simple functions.
 * The modelling function will probably change when fit() will be called.</p>
 *
 * @author saa
 * @version (incomplete) 1.0b
 */

public class ModelFunction {
	private int shapeID;

	private double[] pars;

	private native double nF(double x);

	private native double[] nFArray(double x0, double step, int length);

	private native void nChangeByACXL(double dA, double dC, double dX, double dL); // преобразование к ACXL-порогу

	private native double nRMS(double y[], int begin, int end); // end is included
	
	private static final int FITMODE_VARY_ALL = 1; // фитируем кривую как есть, варьируем все параметры
	private static final int FITMODE_VARY_LIN = 2; // фитируем кривую, варьируя только линейные параметры
	private static final int FITMODE_SET_LINEAR = 3; // заменяем прямой
	private static final int FITMODE_SET_BREAKL = 4; // заменяем ломаной
	private static final int FITMODE_SET_SMOOTH = 5; // заменяем сглаживающей кривой -- пока нет
	
	public static final int LINK_FIXLEFT = 0x1; // зафиксировать левую точку

	// при fitMode = FITMODE_SET_* допускается фитировать "пустой" mf
	// длина = end - begin + 1
	private native void nFit(double y[], int begin, int end, int fitMode,
			int errorMode, double error1, double error2, int maxpoints,
			int linkFlags, double linkData0);

	private native double nGetAttr(String name, double default_value);

	private native void nInitAsLinear();

	private native void nSetAsLinear(
			int x1, double y1, int x2, double y2);

	private ModelFunction() {
		shapeID = 0;
		pars = null;
	}

	/**
	 * Создает модельную функцию линейного типа (ax+b).
	 * 
	 * сейчас это - единственный не-native способ создать объект,
	 * и он используется для вспомогательных целей.
	 * 
	 * @return модельная функция линейного типа ax+b, a=0, b=0 
	 */
	public static ModelFunction CreateLinear()
	{
		ModelFunction ret = new ModelFunction();
		ret.nInitAsLinear();
		return ret;
	}
	
	public void SetAsLinear(
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
	 * Is zero for linears, transition region width for welds,
	 * front-to-edge distance for connectors.
	 */
	public double getWidth() {
		return nGetAttr("width", 0);
	}

	/**
	 * @return the height of the event front.
	 */
	public double getFrontHeight() {
		return nGetAttr("fHeight", 0);
	}

	/**
	 * @return the y-step of the WELD(SPLICE) event; usually it is negative.
	 */
	public double getWeldStep() {
		System.out.println("weldStep = " + nGetAttr("weldStep", 0)); // FIXIT
		return nGetAttr("weldStep", 0);
	}
	
	public double getEstimatedNoiseSuppressionLength()
	{
	    //System.out.println("noiseSuppressionLength: " + nGetAttr("noiseSuppressionLength", 0));
	    return nGetAttr("noiseSuppressionLength", 1.0);
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
	 * <i>AMFICOM specifics:</i>
	 * We need to know the position where an event occures.
	 * @return event front position
	 * (it is double to provide resolution higher than integer)
	 */
	/*public double getFrontPos()
	 {
	 return nGetAttr("fPos", 0.0); // XXX: note, 0.0 is not a good default value
	 }*/

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
	 * Fills the array with the values of f(x)
	 *
	 * @param x0      initial x
	 * @param step    x step
	 * @param length  length of array (i.e. number of steps)
	 * @return        the array of values of f(x)
	 */
	public double[] funFillArray(double x0, double step, int length) {
		//double ret[] = new double[length];
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
	 * @param end    конечный инденкс включительно
	 * @return       среднеквадратичное отклонение
	 */
	public double calcRMS(double y[], int begin, int end) {
		return nRMS(y, begin, end);
	}

	/**
	 * Вычисляет максимальное отклонение между моделью и р/г 
	 * 
	 * @param y	    рефлектограмма
	 * @param begin   начальный индекс 
	 * @param end     конечный индекс включительно
	 * @return		максимальное по модулю отклонение
	 */

	public double calcMaxDeviation_unused(double y[], int begin, int end) { // довольно медленно, но наверное это не важно
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
	 * @param errorMode [test] precision mode (use '0' as default and for fixed-dimesion fitting)
	 * @param error1 [test] undocumented; does not matter if errorMode == 0
	 * @param error2 [test] undocumented; does not matter if errorMode == 0
	 * @param linkFlags флажки для связывания с соседними событиями (пока только LINK_FIXLEFT)
	 * @param linkData0 данные для флажка LINK_FIXLEFT
	 */
	public void fit(double y[], int begin, int end, int errorMode,
			double error1, double error2, int maxpoints,
			int linkFlags, double linkData0)
	{
	    //System.out.println("fit-2: linkFlags " + linkFlags);
		nFit(y, begin, end, FITMODE_VARY_ALL, errorMode, error1, error2, maxpoints,
		    linkFlags, linkData0);
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
		nFit(y, begin, end, FITMODE_VARY_ALL, 0, 0.0, 0.0, 0, linkFlags, linkData0);
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
		nFit(y, begin, end, FITMODE_VARY_LIN, 0, 0.0, 0.0, 0, linkFlags, linkData0);
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

	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeInt(shapeID);
		dos.writeInt(pars.length);
		for (int i = 0; i < pars.length; i++)
			dos.writeDouble(pars[i]);
	}

	public void readFromDIS(DataInputStream dis) throws IOException {
		shapeID = dis.readInt();
		int npars = dis.readInt();
		pars = new double[npars];
		for (int i = 0; i < npars; i++)
			pars[i] = dis.readDouble();
	}
}