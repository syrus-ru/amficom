package com.syrus.AMFICOM.analysis.dadara;

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.io.SignatureMismatchException;
import com.syrus.util.Log;

/**
 * <p>Represents r/g modelling functions with native implementation.</p>
 *
 * <p>Hides the implementation of f(x).
 * provides access to f(x) values, fitting procedure, RMS calculation,
 * Threshold function build procedure.
 * 
 * Does not cares for begin and end of <i>Amficom</i> events, otherwise than
 * to hint BreakL fitter about recommended stop points.
 *
 * Does not remember the original r/g itself.</p>
 *
 * <p>Input X for the model function is integer.
 * The physical X-scale is not concerned.</p>
 *
 * <p>Output Y = f(X), its units are rgdB derived from Y=5.0*log_10(I) rule.</p>
 *
 * <p>The modelling function type can change when fit() will be called.</p>
 *
 * @version $Revision: 1.36 $, $Date: 2005/11/10 13:16:37 $
 * @author $Author: saa $
 * @author saa
 * @module dadara
 */

public class ModelFunction {
	private static final short SIGNATURE_MF_SHORT = 11360;

	private int shapeID; // ��� ������ (���������� ������������� native-����)
	private double[] pars; // ��������� ������

	static {
		CoreAnalysisManager.loadDLL();
	}

	private native double nF(double x);
	private native double[] nFArray(double x0, double step, int length);
	private native void nChangeByACXL(double dA, double dC, double dX, double dL); // �������������� � ������ ACXL-������
	private native void nChangeByThresh(ThreshDX[] threshDX, ThreshDY[] threshDY, int key); // ��������� ������� Thresh

	// �����������, ����� threshDX-����� �������� �� ��������� ������� x
	// ����������� � �������� �.�., ������� ���������� nChangeByThresh,
	// ��:
	// 1. ������������� �������� x,
	// 2. �� �������� ��� ������ mf,
	// 3. ���������� ��������� ������:
	// -1: ����� �� ������;
	// >=0: ������ ���������� ������ � ������� threshDX
	private native int nFindResponsibleThreshDXDYID(ThreshDX[] threshDX, ThreshDY[] threshDY, int key, int x, int xThType);
	private native int[] nFindResponsibleThreshDXArray(ThreshDX[] threshDX, ThreshDY[] threshDY, int key, int xMin, int xMax);
	private native double nRMS(double y[], int begin, int end); // end is included

	// �������� native-�������������� � �����.
	// ����� ����� �������� ��:
	// (1) int shapeID
	// (2) boolean usingNativeEncoding
	// (3) int len - ����� ������� ����
	// (4) byte[len] - ������ ������, ������������ ��/� native-���
	// ��� ��� ������������� ���� ������� ���������, �������� �� ��� ���.
	private static native boolean nIsNativeStreamingPossible(int shapeID); // ��������� ������������ native-��������������
	private native byte[] nParsToByteArray(); // ��������� pars � byteArray (���� �������� �����������, ������ null)
	private native boolean nParsFromByteArray(byte[] bar); // ��������������� pars �� byteArray. � ������ ��������/������� ������ ��� ������, ���������� true.

	private static final int FITMODE_VARY_ALL = 1; // �������� ������, ��������� ��� ���������
	private static final int FITMODE_VARY_LIN = 2; // �������� ������, �������� ������ �������� ���������
	private static final int FITMODE_SET_LINEAR = 3; // �������� ������
	private static final int FITMODE_SET_BREAKL = 4; // �������� �������
	//private static final int FITMODE_SET_SMOOTH = 5; // �������� ������������ ������ -- ���� �� �����������
	
	public static final int LINK_FIXLEFT = 0x1; // ������������� ����� �����
	
	/**
	 * @deprecated use only for debugging
	 * @return Internal data: shape ID
	 */
	@Deprecated
	public int snoopShapeID()
	{
		return this.shapeID;
	}

	/**
	 * @deprecated Use only for debugging
	 * @return Internal data: parameters array
	 */
	@Deprecated
	public double[] snoopPars()
	{
		return this.pars;
	}

	/**
	 * ��������� ���������.
	 * @param y ������, � ������� ����������. ������ � �������
	 * 	��������� � X-����������� ��� ��������� ������� � �����������
	 *  �� begin �� end. ������ ����� ���������� ����� = begin - end + 1.
	 * @param begin ��������� ����� (������������)
	 * @param end �������� ����� (������������)
	 * @param fitMode ����� ���������. �������� ����������� FITMODE_*
	 *  ��� ������� FITMODE_VARY_* ������������ ������� ��� ������,
	 *  � � �������� ���������� ����������� - ������� ��������� �������.
	 *  ��� ������� FITMODE_SET_* ���������� ��������� �� ������������,
	 *  �.�. ���������� ��������� ����� ��������� �������.
	 * @param linkFlags ����� ������ �� �������� ���������.
	 *  �� ������ ������ �������������� ������ ���� LINK_FIXLEFT,
	 *  � ������ ��� ��������� �������.
	 * @param linkData0 ������ ��� ������ �� LINK_FIXLEFT.
	 *  (��������� ��� ����� ���� �����, ��� ���� �����������
	 *  � ���� ������ ����� double).
	 */
	private native void nFit1(double y[], int begin, int end, int fitMode,
			int linkFlags, double linkData0);

	/**
	 * ��������� ���������. �������� ��������� ���������� ��. � nFit1)
	 * @param errorR ��������� ���. ������ (+- ��)
	 * @param errorA ��������� ���. ������ (��, ~ ������ ���� �� 3 �����)
	 * @param maxPoints ����. ����� �����
	 */
	private native void nFit2(double y[], int begin, int end, int fitMode,
			int linkFlags, double linkData0,
			double errorR, double errorA, int maxPoints);

	/**
	 * ��������� ���������. �������� ��������� ���������� ��. � nFit1)
	 * @param error �������� �������� ��������� ����� ������
	 * @param xStops �����, � ������� ������������� ������� ������ (��� �������)
	 */
	private native void nFit4(double y[], int begin, int end, int fitMode,
			int linkFlags, double linkData0,
			double[] error, int[] xStops);

	/**
	 * �������� �������� ��������� ��������� (�� ��� �����)
	 * @param name ��� ��������� (�������������� ������ ASCII-�����)
	 * @param default_value �������� �� ��������� �� ������, ����
	 *  �������� �� ���������.
	 * @return �������� ���������
	 */
	private native double nGetAttr(String name, double default_value);

	/**
	 * ����� ����������� ������������� - ��� �������� ������� y = 0 * x + 0
	 */
	private native void nInitAsLinear();

	/**
	 * ������������� ������� ��� ������ �� ���� ������.
	 * @param x1 x-���������� 1-� �����
	 * @param y1 y-���������� 1-� �����
	 * @param x2 x-���������� 2-� �����
	 * @param y2 y-���������� 2-� �����
	 */
	private native void nSetAsLinear(
			int x1, double y1, int x2, double y2);

	/**
	 * �����������.
	 * ������������ ��� �������� ������� � ������������ �������,
	 * ��������� copy() � � native-����.
	 *
	 */
	private ModelFunction() {
		this.shapeID = 0;
		this.pars = null;
	}

	/**
	 * ������� ��������� ������� ��������� ���� (ax+b).
	 * ����� ���� ����������� ��� ������������ �����.
	 * 
	 * @return ��������� ������� ��������� ���� ax+b, a=0, b=0 
	 */
	public static ModelFunction createLinear()
	{
		ModelFunction ret = new ModelFunction();
		ret.nInitAsLinear();
		return ret;
	}

	/**
	 * ������� ��������� ������� ��������� ���� (ax+b), ������� �� RMS.
	 * �������������� �������� X �� ���� �� y.length-1
	 * @return ��������� ������� ��������� ���� ax+b, a=0, b=0 
	 */
	public static ModelFunction createLinearFrom0(double[]y)
	{
		ModelFunction ret = new ModelFunction();
		ret.nFit1(y, 0, y.length - 1, FITMODE_SET_LINEAR, 0, 0);
		return ret;
	}

	public void setAsLinear(
			int x1, double y1, int x2, double y2)
	{
		nSetAsLinear(x1, y1, x2, y2);
	}

	/**
	 * @return The width of the event.
	 * Gives zero for linears; transition region width for welds;
	 * front-to-edge distance for connectors.
	 * 
	 * @deprecated Undefined for currently used curves!
	 */
	@Deprecated
	public double getWidth0() {
		return nGetAttr("width", 0);
	}

	/**
	 * @return the height of the event front.
	 * @deprecated Undefined for currently used curves!
	 */
	@Deprecated
	public double getFrontHeight0() {
		return nGetAttr("fHeight", 0);
	}

	/**
	 * @return the y-step of the WELD(SPLICE) event; usually it is negative.
	 */
	public double getWeldStep0() {
		Log.debugMessage("weldStep = " + nGetAttr("weldStep", 0), INFO); // FIXIT
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

		// ������������� (�� ���������) ����������:
		//double ret[] = new double[length];
		//for (int i = 0; i < length; i++)
		//  ret[i] = nF(x0 + step * i);
		//return ret;
	}

	/**
	 * ��������� ������������������ ���������� ����� ������� � �/�
	 *
	 * @param y      ��������������
	 * @param begin  ��������� ������
	 * @param end    �������� ������ ������������
	 * @return       ������������������ ����������
	 */
	public double calcRMS(double y[], int begin, int end) {
		return nRMS(y, begin, end);
	}

	/**
	 * Performs fitting
	 *
	 * @param y      real r/g data array
	 * @param begin  starting fitting array index
	 * @param end    ending fitting array index (length is end - begin + 1)
	 * @param linkFlags ������ ��� ���������� � ��������� ��������� (���� ������ LINK_FIXLEFT)
	 * @param linkData0 ������ ��� ������ LINK_FIXLEFT
	 */
	public void fit(double y[], int begin, int end, int linkFlags, double linkData0)
	{
		Log.debugMessage("fit-3: linkFlags " + linkFlags, FINEST);
		nFit1(y, begin, end, FITMODE_VARY_ALL, linkFlags, linkData0);
	}

	/**
	 * Performs fitting
	 *
	 * @param y      real r/g data array
	 * @param begin  starting fitting array index
	 * @param end    ending fitting array index (length is end - begin + 1)
	 * @param linkFlags ������ ��� ���������� � ��������� ��������� (���� ������ LINK_FIXLEFT)
	 * @param linkData0 ������ ��� ������ LINK_FIXLEFT
	 * @param errorR [test] undocumented
	 * @param errorA [test] undocumented
	 * @param maxPoints [test] undocumented
	 */
	public void fit(double y[], int begin, int end,
			int linkFlags, double linkData0,
			double errorR, double errorA, int maxPoints)
	{
		Log.debugMessage("fit-2: linkFlags " + linkFlags, FINEST);
		nFit2(y, begin, end, FITMODE_VARY_ALL,
			linkFlags, linkData0,
			errorR, errorA, maxPoints);
	}

	/**
	 * �������� ������� �������� ������ ������ �������.
	 * ����� ������������ "���������" � �������� �������, �����
	 * ����� ���������� ���� ����������� �������.
	 * @param y ���������� ������
	 * @param begin ��������� ����� (������������) ���������� ������
	 * @param end �������� ����� (������������) ���������� ������
	 * @param noise �������� ��������� (������� ����) XXX: ����� ��� �� 3 ����� 
	 * @param re ����� �������, ��������������, ��� ����� ��������� ����
	 *  ������� (������������ ����� ����� �������), may be null
	 * @return ������������ ������� � ���� {@link ModelFunction}
	 */
	public static ModelFunction createFitedAsBreakL(double[] y,
			int begin, int end,
			double[] noise,
			SimpleReflectogramEvent[] re)
	{
		int[] stops = re != null ? new int[re.length] : null;
		if (re != null) {
			for (int i = 0; i < re.length; i++)
				stops[i] = re[i].getBegin();
		}
		ModelFunction mf = createLinear();
		mf.nFit4(y, begin, end, FITMODE_SET_BREAKL, 0, 0,
			noise, stops);
		return mf;
	}

	/**
	 * Performs linear fitting
	 *
	 * @param y      real r/g data array
	 * @param begin  starting fitting array index
	 * @param end    ending fitting array index (length is end - begin + 1)
	 * @param linkFlags ������ ��� ���������� � ��������� ��������� (���� ������ LINK_FIXLEFT)
	 * @param linkData0 ������ ��� ������ LINK_FIXLEFT
	 */
	public void fitLinearOnly(double y[], int begin, int end, int linkFlags, double linkData0)
	{
		Log.debugMessage("fit-1(Lin): linkFlags " + linkFlags, FINEST);
		nFit1(y, begin, end, FITMODE_VARY_LIN, linkFlags, linkData0);
	}

	/**
	 * ������� ������� ������, ���������� ��� � ������������� ACXL-�����.
	 * �����
	 * 
	 * @param ACXL  ������ �� ������� double �������:
	 * ACXL[0]: A - ����� �����,
	 * ACXL[1]: C - ����� ������,
	 * ACXL[2]: X - ��������� ���������� ������������ ������,
	 * ACXL[3]: L - ��������� ��������� (�������), ������������ �������� �����.
	 */
	public void changeByACXLThreshold(double[] ACXL) {
		nChangeByACXL(ACXL[0], ACXL[1], ACXL[2], ACXL[3]);
	}

	public void changeByThresh(ThreshDX[] threshDX, ThreshDY[] threshDY, int key)
	{
		nChangeByThresh(threshDX, threshDY, key);
	}

	// no javadoc yet; see comment of nFindResponsibleThreshDXDYID
	public int findResponsibleThreshDXID(ThreshDX[] threshDX, ThreshDY[] threshDY, int key, int x)
	{
		return nFindResponsibleThreshDXDYID(threshDX, threshDY, key, x, 1);
	}
	public int findResponsibleThreshDYID(ThreshDX[] threshDX, ThreshDY[] threshDY, int key, int x)
	{
		return nFindResponsibleThreshDXDYID(threshDX, threshDY, key, x, 2);
	}

	// xMax *must* be >= xMin
	public int[] findResponsibleThreshDXArray(ThreshDX[] threshDX, ThreshDY[] threshDY, int key, int xMin, int xMax)
	{
		return nFindResponsibleThreshDXArray(threshDX, threshDY, key, xMin, xMax);
	}

	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeShort(SIGNATURE_MF_SHORT);
		dos.writeInt(this.shapeID);
		boolean useNativeStreaming = nIsNativeStreamingPossible(this.shapeID);
		dos.writeBoolean(useNativeStreaming);
		if (useNativeStreaming) {
			// native-��������������
			byte[] bar = nParsToByteArray();
			dos.writeInt(bar.length);
			dos.write(bar);
		} else {
			// Java-��������������
			dos.writeInt(this.pars.length);
			for (int i = 0; i < this.pars.length; i++)
				dos.writeDouble(this.pars[i]);
		}
	}

	public byte[] toByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			writeToDOS(dos);
			dos.close();
			return baos.toByteArray();
		} catch (IOException e)
		{
			throw new InternalError("Unexpected exception" + e.toString());
		}
	}

	public static ModelFunction createFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		ModelFunction mf = new ModelFunction();
		mf.readFromDIS(dis);
		return mf;
	}

	private void readFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readShort() != SIGNATURE_MF_SHORT) {
			throw new SignatureMismatchException();
		}
		this.shapeID = dis.readInt();
		boolean useNativeStreaming = dis.readBoolean();
		if (useNativeStreaming) {
			if (! nIsNativeStreamingPossible(this.shapeID))
				throw new InternalError("native streaming not supported for " + this.shapeID); // XXX: maybe a SignatureMismatchException would be better
			// native-��������������
			int len = dis.readInt();
			byte[] bar = new byte[len];
			if (dis.read(bar) != bar.length)
				throw new SignatureMismatchException("Unexpected EOF");
			nParsFromByteArray(bar);
		} else {
			// Java-��������������
			int npars = dis.readInt();
			this.pars = new double[npars];
			for (int i = 0; i < npars; i++)
				this.pars[i] = dis.readDouble();
		}
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + shapeID;
		for (int i = 0; i < pars.length; i++) {
			long bits = Double.doubleToLongBits(pars[i]);
			result = 37 * result + (int)(bits ^ (bits >>>32));
		}
		return result;
	}

	// XXX: currently does return string presentation of
	// shapeID that is not available otherwise. This is bad, see EJ #9
	@Override
	public String toString() {
		return "MF(" + shapeID + ":" + pars.length + ":" + hashCode() + ")";
	}
}

