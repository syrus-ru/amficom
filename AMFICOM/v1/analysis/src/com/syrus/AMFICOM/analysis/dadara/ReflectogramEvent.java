package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class ReflectogramEvent {
	public static final int NUMBER_OF_PARAMETERS = 38;
	public static final int EVENT_SIZE = 35 * 8 + 3 * 4;
	//public static final int OLD_EVENT_SIZE = 316;

	public static final int LINEAR = 1;
	public static final int WELD = 2;
	public static final int CONNECTOR = 3;
	public static final int SINGULARITY = 4;

	private int type;
	public int begin;
	public int end;
// Parameters for linear part
	public double  a_linear;
	public double  a_linearError;
	public double  b_linear;
	public double  b_linearError;
	public double  chi2Linear;
// Parameters for the welds
	public double  a_weld;
	public double  a_weldError;
	public double  b_weld;
	public double  b_weldError;
	public double  boost_weld;
	public double  boost_weldError;
	public double  center_weld;
	public double  center_weldError;
	public double  width_weld;
	public double  width_weldError;
	public double  chi2Weld;
// Parameters for the Connectors
	public double  a1_connector;
	public double  a1_connectorError;
	public double  a2_connector;
	public double  a2_connectorError;
	public double  aLet_connector;
	public double  aLet_connectorError;
	public double  width_connector;
	public double  width_connectorError;
	public double  center_connector;
	public double  center_connectorError;
	public double  sigma1_connector;
	public double  sigma1_connectorError;
	public double  sigma2_connector;
	public double  sigma2_connectorError;
	public double  sigmaFit_connector;
	public double  sigmaFit_connectorError;
	public double  k_connector;
	public double  k_connectorError;
	public double  chi2Connector;

// Delta_x
	private double delta_x = 1.;

	public Threshold threshold;

	public ReflectogramEvent() {
		threshold = new Threshold(this);
	}

	public ReflectogramEvent(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		DataInputStream dis = new DataInputStream(bais);
		try	{
			this.type = dis.readInt();
			this.begin = dis.readInt();
			this.end = dis.readInt();

			this.a_linear = dis.readDouble();
			this.a_linearError = dis.readDouble();
			this.b_linear = dis.readDouble();
			this.b_linearError = dis.readDouble();
			this.chi2Linear = dis.readDouble();
// Parameters for the welds
			this.a_weld = dis.readDouble();
			this.a_weldError = dis.readDouble();
			this.b_weld = dis.readDouble();
			this.b_weldError = dis.readDouble();
			this.boost_weld = dis.readDouble();
			this.boost_weldError = dis.readDouble();
			this.center_weld = dis.readDouble();
			this.center_weldError = dis.readDouble();
			this.width_weld = dis.readDouble();
			this.width_weldError = dis.readDouble();
			this.chi2Weld = dis.readDouble();
// Parameters for the Connectors
			this.a1_connector = dis.readDouble();
			this.a1_connectorError = dis.readDouble();
			this.a2_connector = dis.readDouble();
			this.a2_connectorError = dis.readDouble();
			this.aLet_connector = dis.readDouble();
			this.aLet_connectorError = dis.readDouble();
			this.width_connector = dis.readDouble();
			this.width_connectorError = dis.readDouble();
			this.center_connector = dis.readDouble();
			this.center_connectorError = dis.readDouble();
			this.sigma1_connector = dis.readDouble();
			this.sigma1_connectorError = dis.readDouble();
			this.sigma2_connector = dis.readDouble();
			this.sigma2_connectorError = dis.readDouble();
			this.sigmaFit_connector = dis.readDouble();
			this.sigmaFit_connectorError = dis.readDouble();
			this.k_connector = dis.readDouble();
			this.k_connectorError = dis.readDouble();
			this.chi2Connector = dis.readDouble();

			dis.close();
		}
		catch (IOException e)	{
			System.out.println("Exception while converting byte array to ReflectogramEvent: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public Threshold getThreshold()	{
		return threshold;
	}

	public void setThreshold(Threshold threshold)	{
		this.threshold = threshold;
	}

	public double connectorF(int x) {
		double ret = 0.;
		double arg;
		double arg1;
		double arg2;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;
		double tmp;

		if(arg<-width_connector/2.)	{
			ret = a1_connector;
		}
		else
			if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
				ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
							a1_connector;
			}
			else
				if(arg>width_connector/2.) {
					tmp = a1_connector+aLet_connector*
								(1.-Math.exp(-width_connector/sigma1_connector));

					ret = tmp -
								(tmp-a2_connector)*(1. - expa(arg2, sigma2_connector,
								sigmaFit_connector, k_connector));
				}
				else
					ret = 0.;

				return ret;
	}

	public double linearF(int x)	{
		double ret;
		double arg = x-begin;
		ret = a_linear + b_linear*arg;
		return ret;
	}

	public double weldF(int x) {
		double ret;
		double arg = x-this.center_weld;
		double halfWidth = this.width_weld/2.;

		if(arg<-halfWidth) ret = -1.;
		else
			if(arg> halfWidth) ret = 1.;
		else
			ret = Math.sin(3.14159*arg/width_weld);

		ret = ret*this.boost_weld/2. + a_weld + b_weld*arg;
		return ret;
	}


	public double[] refAmpl(int x) {
		double []ret = new double[2];
		double retA = 0.;
		double retErr = 0.;
	//	double distance = 0.;
		if(this.type == LINEAR)	{
			retA = this.linearF(x);
	//		distance = endLinear - beginLinear;
			retErr = Math.sqrt(a_linearError*a_linearError);// +
//                         b_linearError*distance*b_linearError*distance/2.);
		}
		else
			if(this.type == CONNECTOR) {
				retA = this.connectorF(x);
				retErr = Math.sqrt(a1_connectorError*a1_connectorError +
													 a2_connectorError*a2_connectorError +
													 aLet_connectorError*aLet_connectorError);
			}
			else
				if(this.type == WELD)	{
					retA = this.weldF(x);
					retErr = Math.sqrt(a_weldError*a_weldError +
														 boost_weldError*boost_weldError);
				}
				else {
					retA = 0.;
					retErr = 10.;
				}
				ret[0] = retA;
				ret[1] = retErr;
				return ret;
	}



	public double refAmplitude(int x) {
		double retA = 0.;

		if(this.type == LINEAR)	{
			retA = this.linearF(x);
		}
		else
			if(this.type == CONNECTOR) {
				retA = this.connectorF(x);
			}
			else
				if(this.type == WELD)	{
					retA = this.weldF(x);
				}
				else {
					retA = 0.;
				}
				return retA;
	}


	public void setParams(double []signal, int begin) {
		this.type = (int)signal[begin];
		this.begin = (int)signal[begin+1];
		this.end =   (int)signal[begin+2];
		// Linear
		a_linear = signal[begin+3];
		a_linearError = signal[begin+4];
		b_linear = signal[begin+5];
		b_linearError = signal[begin+6];
		chi2Linear = signal[begin+7];
		// Weld
		a_weld = signal[begin+8];
		a_weldError = signal[begin+9];
		b_weld = signal[begin+10];
		b_weldError = signal[begin+11];
		boost_weld = signal[begin+12];
		boost_weldError = signal[begin+13];
		center_weld = signal[begin+14];
		center_weldError = signal[begin+15];
		width_weld = signal[begin+16];
		width_weldError = signal[begin+17];
		chi2Weld = signal[begin+18];
		//Connector
		a1_connector = signal[begin+19];
		a1_connectorError = signal[begin+20];
		a2_connector = signal[begin+21];
		a2_connectorError = signal[begin+22];
		aLet_connector = signal[begin+23];
		aLet_connectorError = signal[begin+24];
		width_connector = signal[begin+25];
		width_connectorError = signal[begin+26];
		center_connector = signal[begin+27];
		center_connectorError = signal[begin+28];
		sigma1_connector = signal[begin+29];
		sigma1_connectorError = signal[begin+30];
		sigma2_connector = signal[begin+31];
		sigma2_connectorError = signal[begin+32];
		sigmaFit_connector = signal[begin+33];
		sigmaFit_connectorError = signal[begin+34];
		k_connector = signal[begin+35];
		k_connectorError = signal[begin+36];
		chi2Connector = signal[begin+37];
	}

	public void toDoubleArray(double[] d, int start)
	{
		d[start] = this.type;
		d[start+1] = this.begin;
		d[start+2] = this.end;
		// Linear
		d[start+3] = a_linear;
		d[start+4] = a_linearError;
		d[start+5] = b_linear;
		d[start+6] = b_linearError;
		d[start+7] = chi2Linear;
		// Weld
		d[start+8] = a_weld;
		d[start+9] = a_weldError;
		d[start+10] = b_weld;
		d[start+11] = b_weldError;
		d[start+12] = boost_weld;
		d[start+13] = boost_weldError;
		d[start+14] = center_weld;
		d[start+15] = center_weldError;
		d[start+16] = width_weld;
		d[start+17] = width_weldError;
		d[start+18] = chi2Weld;
		//Connector
		d[start+19] = a1_connector;
		d[start+20] = a1_connectorError;
		d[start+21] = a2_connector;
		d[start+22] = a2_connectorError;
		d[start+23] = aLet_connector;
		d[start+24] = aLet_connectorError;
		d[start+25] = width_connector;
		d[start+26] = width_connectorError;
		d[start+27] = center_connector;
		d[start+28] = center_connectorError;
		d[start+29] = sigma1_connector;
		d[start+30] = sigma1_connectorError;
		d[start+31] = sigma2_connector;
		d[start+32] = sigma2_connectorError;
		d[start+33] = sigmaFit_connector;
		d[start+34] = sigmaFit_connectorError;
		d[start+35] = k_connector;
		d[start+36] = k_connectorError;
		d[start+37] = chi2Connector;
	}


	public double expa(double x, double s1, double s2, double part)	{
		double ret = 0.;

		double arg1 = x/s1;
		double arg2 = x/s2;

		ret = Math.exp(-arg1)*part + Math.exp(-arg2)*(1.-part);

		return ret;
	}

// functions for default threshold
//  public double connectorThresholdF(int x, double dL, double dA, double dX, double dC,
//                                    ReflectogramEvent previousEvent, ReflectogramEvent nextEvent)
//  {
//    double ret = 0.;
//    double arg;
//    double arg1;
//    double arg2;
//
//    this.begin = (int)(this.begin-dX+dC);
//    this.end = (int)(this.end+dX+dC);
//
//    if(this.begin<0)
//      this.begin = 0;
//
//    this.begin = this.begin = this.begin;
//    this.end = this.end = this.end;
//
//    if(previousEvent != null)
//    {
//      previousEvent.end = previousEvent.end = previousEvent.end = this.begin;
//    }
//    if(nextEvent != null)
//    {
//      nextEvent.begin = nextEvent.begin = nextEvent.begin = this.end;
//    }
//
//
//
//    double width_connector = this.width_connector+dX*2.;
//    double a1_connector = this.a1_connector+dA;
//    double a2_connector = this.a2_connector+dA;
//    double aLet_connector = this.aLet_connector+dL;
//    double center_connector = this.center_connector+dC;
//
//    arg = x-center_connector;
//    arg1 = arg+width_connector/2.;
//    arg2 = arg-width_connector/2.;
//
//    if(arg<-width_connector/2.)	{
//      ret = a1_connector;
//    }
//    else
//      if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
//        ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
//              a1_connector;
//      }
//      else
//        if(arg>width_connector/2.) {
//          ret = (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
//              sigma1_connector))) -
//                (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
//                sigma1_connector))-a2_connector)*
//                (1. - expa(arg2, sigma2_connector,
//                sigmaFit_connector, k_connector) );
//        }
//        else
//          ret = 0.;
//
//        return ret;
//  }


	public double connectorThresholdF(int x, double dL, double dA, double dX, double dC)
	{
		double ret = 0.;
		double arg;
		double arg1;
		double arg2;

		double width_connector = this.width_connector+dX*2.;
		double a1_connector = this.a1_connector+dA;
		double a2_connector = this.a2_connector+dA;
		double aLet_connector = this.aLet_connector+dL;
		double center_connector = this.center_connector+dC;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;

		if(arg<-width_connector/2.)	{
			ret = a1_connector;
		}
		else
			if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
				ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
							a1_connector;
			}
			else
				if(arg>width_connector/2.) {
					ret = (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
							sigma1_connector))) -
								(a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
								sigma1_connector))-a2_connector)*
								(1. - expa(arg2, sigma2_connector,
								sigmaFit_connector, k_connector) );
				}
				else
					ret = 0.;

				return ret;
	}


	public double connectorThresholdF(int x, int thresholdNumeral)
	{
		double dL;
		double dA;
		double dX;
		double dC;
		if(threshold == null) //if threshold is not set;
			return 0.;
		else
		{
			dL = threshold.dL[thresholdNumeral];
			dA = threshold.dA[thresholdNumeral];
			dX = threshold.dX[thresholdNumeral];
			dC = threshold.dC[thresholdNumeral];
		}


		double ret = 0.;
		double arg;
		double arg1;
		double arg2;

		double width_connector = this.width_connector+dX*2.;
		double a1_connector = this.a1_connector+dA;
		double a2_connector = this.a2_connector+dA;
		double aLet_connector = this.aLet_connector+dL;
		double center_connector = this.center_connector+dC;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;

		if(arg<-width_connector/2.)	{
			ret = a1_connector;
		}
		else
			if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
				ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
							a1_connector;
			}
			else
				if(arg>width_connector/2.) {
					ret = (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
							sigma1_connector))) -
								(a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
								sigma1_connector))-a2_connector)*
								(1. - expa(arg2, sigma2_connector,
								sigmaFit_connector, k_connector) );
				}
				else
					ret = 0.;

				return ret;
	}


	public double linearThresholdF(int x, double dA)	{
		double ret;
		double arg = x-begin;
		double a_linear = this.a_linear+dA;
		ret = a_linear + b_linear*arg;
		return ret;
	}


	public double linearThresholdF(int x, int thresholdNumeral)
	{
		double dA;
		if(threshold == null) // Case, when threshold is not set;
			return 0.;
		else
		{
			dA = threshold.dA[thresholdNumeral];
		}

		double ret;
		double arg = x-begin;

		double a_linear = this.a_linear+dA;

		ret = a_linear + b_linear*arg;
		return ret;
	}

	public double weldThresholdF(int x, double dA, double dX) {

		double a_weld = this.a_weld + dA;

		double ret;
		double arg = x-this.center_weld;
		double halfWidth = this.width_weld/2.;

		if(arg<-halfWidth) ret = -1.;
		else
			if(arg> halfWidth) ret = 1.;
		else
			ret = Math.sin(3.14159*arg/width_weld);

		ret = ret*this.boost_weld/2. + a_weld + b_weld*arg;
		return ret;
	}


	public double weldThresholdF(int x, int thresholdNumeral)
	{
		double dA;
		if(threshold == null)
			return 0.;
		else
		{
			dA = threshold.dA[thresholdNumeral];
		}

		double a_weld = this.a_weld + dA;

		double ret;
		double arg = x-this.center_weld;
		double halfWidth = this.width_weld/2.;

		if(arg<-halfWidth) ret = -1.;
		else
			if(arg> halfWidth) ret = 1.;
		else
			ret = Math.sin(3.14159*arg/width_weld);

		ret = ret*this.boost_weld/2. + a_weld + b_weld*arg;
		return ret;
	}

	public int getType() {
		return type;
	}

	public void setType(int type)	{
		this.type = type;
	}

	public ReflectogramEvent copy()	{
		ReflectogramEvent re = new ReflectogramEvent();

		re.type = type;
		re.begin = begin;
		re.end = end;

		re.a_linear = a_linear;
		re.a_linearError = a_linearError;
		re.b_linear = b_linear;
		re.b_linearError = b_linearError;
		re.chi2Linear = chi2Linear;
// Parameters for the welds
		re.a_weld = a_weld;
		re.a_weldError = a_weldError;
		re.b_weld = b_weld;
		re.b_weldError = b_weldError;
		re.boost_weld = boost_weld;
		re.boost_weldError = boost_weldError;
		re.center_weld = center_weld;
		re.center_weldError = center_weldError;
		re.width_weld = width_weld;
		re.width_weldError = width_weldError;
		re.chi2Weld = chi2Weld;
// Parameters for the Connectors
		re.a1_connector = a1_connector;
		re.a1_connectorError = a1_connectorError;
		re.a2_connector = a2_connector;
		re.a2_connectorError = a2_connectorError;
		re.aLet_connector = aLet_connector;
		re.aLet_connectorError = aLet_connectorError;
		re.width_connector = width_connector;
		re.width_connectorError = width_connectorError;
		re.center_connector = center_connector;
		re.center_connectorError = center_connectorError;
		re.sigma1_connector = sigma1_connector;
		re.sigma1_connectorError = sigma1_connectorError;
		re.sigma2_connector = sigma2_connector;
		re.sigma2_connectorError = sigma2_connectorError;
		re.sigmaFit_connector = sigmaFit_connector;
		re.sigmaFit_connectorError = sigmaFit_connectorError;
		re.k_connector = k_connector;
		re.k_connectorError = k_connectorError;
		re.chi2Connector = chi2Connector;

		re.threshold = threshold;
		return re;
	}

	public byte[] getByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(EVENT_SIZE);
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(this.type);
			dos.writeInt(this.begin);
			dos.writeInt(this.end);

			dos.writeDouble(this.a_linear);
			dos.writeDouble(this.a_linearError);
			dos.writeDouble(this.b_linear);
			dos.writeDouble(this.b_linearError);
			dos.writeDouble(this.chi2Linear);

			dos.writeDouble(this.a_weld);
			dos.writeDouble(this.a_weldError);
			dos.writeDouble(this.b_weld);
			dos.writeDouble(this.b_weldError);
			dos.writeDouble(this.boost_weld);
			dos.writeDouble(this.boost_weldError);
			dos.writeDouble(this.center_weld);
			dos.writeDouble(this.center_weldError);
			dos.writeDouble(this.width_weld);
			dos.writeDouble(this.width_weldError);
			dos.writeDouble(this.chi2Weld);

			dos.writeDouble(this.a1_connector);
			dos.writeDouble(this.a1_connectorError);
			dos.writeDouble(this.a2_connector);
			dos.writeDouble(this.a2_connectorError);
			dos.writeDouble(this.aLet_connector);
			dos.writeDouble(this.aLet_connectorError);
			dos.writeDouble(this.width_connector);
			dos.writeDouble(this.width_connectorError);
			dos.writeDouble(this.center_connector);
			dos.writeDouble(this.center_connectorError);
			dos.writeDouble(this.sigma1_connector);
			dos.writeDouble(this.sigma1_connectorError);
			dos.writeDouble(this.sigma2_connector);
			dos.writeDouble(this.sigma2_connectorError);
			dos.writeDouble(this.sigmaFit_connector);
			dos.writeDouble(this.sigmaFit_connectorError);
			dos.writeDouble(this.k_connector);
			dos.writeDouble(this.k_connectorError);
			dos.writeDouble(this.chi2Connector);

			dos.close();
		}
		catch (IOException ioe) {
			System.out.println("Exception while getting byte array from reflectogram event: " + ioe.getMessage());
			ioe.printStackTrace();
			return null;
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(ReflectogramEvent[] revents) {
		byte[] bar = new byte[EVENT_SIZE * revents.length];
		byte[] bar1;
		for (int i = 0; i < revents.length; i++) {
			bar1 = revents[i].getByteArray();
			for (int j = 0; j < EVENT_SIZE; j++)
				bar[i * EVENT_SIZE + j] = bar1[j];
		}
		return bar;
	}

	public static ReflectogramEvent[] fromByteArray(byte[] bar) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		byte[] buf = new byte[EVENT_SIZE];
		LinkedList ll = new LinkedList();
		try {
			while (dis.read(buf) == EVENT_SIZE)
				ll.add(new ReflectogramEvent(buf));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return (ReflectogramEvent[])ll.toArray(new ReflectogramEvent[ll.size()]);
	}


	public ReflectogramEvent getThresholdReflectogramEvent(int thresholdNumeral)
	{
		ReflectogramEvent re = this.copy();
		if(threshold != null && thresholdNumeral>=0 && thresholdNumeral<4)
		{
			if(re.type == CONNECTOR)
			{
				re.width_connector = this.width_connector + threshold.dX[thresholdNumeral]*2.;
				re.a1_connector = this.a1_connector + threshold.dA[thresholdNumeral];
				re.a2_connector = this.a2_connector + threshold.dA[thresholdNumeral];
				re.aLet_connector = this.aLet_connector + threshold.dL[thresholdNumeral];
				re.center_connector = this.center_connector + threshold.dC[thresholdNumeral];

				re.begin = (int)(re.begin - threshold.dX[thresholdNumeral] +
														threshold.dC[thresholdNumeral]);

				re.end = (int)(re.end + threshold.dX[thresholdNumeral] +
														threshold.dC[thresholdNumeral]);
			}
			else if(re.type == LINEAR)
			{
				re.a_linear = this.a_linear + threshold.dA[thresholdNumeral];
			}
			else
			{
				re.a_weld = this.a_weld + threshold.dA[thresholdNumeral];
			}
		}

		return re;
	}

	public void setDeltaX(double delta_x)
	{
		this.delta_x = delta_x;
	}

	public double getDeltaX()
	{
		return this.delta_x;
	}

}
/*

package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class ReflectogramEvent {
	public static final int NUMBER_OF_PARAMETERS = 44;
	public static final int EVENT_SIZE = 316;

	public static final int LINEAR = 10;
	public static final int WELD = 11;
	public static final int CONNECTOR = 12;

// Parameters for linear part
	public double  a_linear;
	public double  a_linearError;
	public double  b_linear;
	public double  b_linearError;
	public int     beginLinear;
	public int     endLinear;
	public double  chi2Linear;
	public int     linearEvent=0;
// Parameters for the welds
	public double  a_weld;
	public double  a_weldError;
	public double  b_weld;
	public double  b_weldError;
	public double  boost_weld;
	public double  boost_weldError;
	public double  center_weld;
	public double  center_weldError;
	public double  width_weld;
	public double  width_weldError;
	public int     beginWeld;
	public int     endWeld;
	public double  chi2Weld;
	public int     weldEvent=0;
// Parameters for the Connectors
	public double  a1_connector;
	public double  a1_connectorError;
	public double  a2_connector;
	public double  a2_connectorError;
	public double  aLet_connector;
	public double  aLet_connectorError;
	public double  width_connector;
	public double  width_connectorError;
	public double  center_connector;
	public double  center_connectorError;
	public double  sigma1_connector;
	public double  sigma1_connectorError;
	public double  sigma2_connector;
	public double  sigma2_connectorError;
	public double  sigmaFit_connector;
	public double  sigmaFit_connectorError;
	public double  k_connector;
	public double  k_connectorError;
	public int     beginConnector;
	public int     endConnector;
	public double  chi2Connector;
	public int     connectorEvent=0;

// Delta_x
	private double delta_x = 1.;

	public Threshold threshold;

	public ReflectogramEvent() {
		threshold = new Threshold(this);
	}

	public ReflectogramEvent(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		DataInputStream dis = new DataInputStream(bais);
		try	{
			this.a_linear = dis.readDouble();
			this.a_linearError = dis.readDouble();
			this.b_linear = dis.readDouble();
			this.b_linearError = dis.readDouble();
			this.beginLinear = dis.readInt();
			this.endLinear = dis.readInt();
			this.chi2Linear = dis.readDouble();
			this.linearEvent = dis.readInt();
// Parameters for the welds
			this.a_weld = dis.readDouble();
			this.a_weldError = dis.readDouble();
			this.b_weld = dis.readDouble();
			this.b_weldError = dis.readDouble();
			this.boost_weld = dis.readDouble();
			this.boost_weldError = dis.readDouble();
			this.center_weld = dis.readDouble();
			this.center_weldError = dis.readDouble();
			this.width_weld = dis.readDouble();
			this.width_weldError = dis.readDouble();
			this.beginWeld = dis.readInt();
			this.endWeld = dis.readInt();
			this.chi2Weld = dis.readDouble();
			this.weldEvent = dis.readInt();
// Parameters for the Connectors
			this.a1_connector = dis.readDouble();
			this.a1_connectorError = dis.readDouble();
			this.a2_connector = dis.readDouble();
			this.a2_connectorError = dis.readDouble();
			this.aLet_connector = dis.readDouble();
			this.aLet_connectorError = dis.readDouble();
			this.width_connector = dis.readDouble();
			this.width_connectorError = dis.readDouble();
			this.center_connector = dis.readDouble();
			this.center_connectorError = dis.readDouble();
			this.sigma1_connector = dis.readDouble();
			this.sigma1_connectorError = dis.readDouble();
			this.sigma2_connector = dis.readDouble();
			this.sigma2_connectorError = dis.readDouble();
			this.sigmaFit_connector = dis.readDouble();
			this.sigmaFit_connectorError = dis.readDouble();
			this.k_connector = dis.readDouble();
			this.k_connectorError = dis.readDouble();
			this.beginConnector = dis.readInt();
			this.endConnector = dis.readInt();
			this.chi2Connector = dis.readDouble();
			this.connectorEvent = dis.readInt();

			dis.close();
		}
		catch (IOException e)	{
			System.out.println("Exception while converting byte array to ReflectogramEvent: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public Threshold getThreshold()	{
		return threshold;
	}

	public void setThreshold(Threshold threshold)	{
		this.threshold = threshold;
	}

	public double connectorF(int x) {
		double ret = 0.;
		double arg;
		double arg1;
		double arg2;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;
		double tmp;

		if(arg<-width_connector/2.)	{
			ret = a1_connector;
		}
		else
			if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
				ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
							a1_connector;
			}
			else
				if(arg>width_connector/2.) {
					tmp = a1_connector+aLet_connector*
								(1.-Math.exp(-width_connector/sigma1_connector));

					ret = tmp -
								(tmp-a2_connector)*(1. - expa(arg2, sigma2_connector,
								sigmaFit_connector, k_connector));
				}
				else
					ret = 0.;

				return ret;
	}

	public double linearF(int x)	{
		double ret;
		double arg = x-beginLinear;
		ret = a_linear + b_linear*arg;
		return ret;
	}

	public double weldF(int x) {
		double ret;
		double arg = x-this.center_weld;
		double halfWidth = this.width_weld/2.;

		if(arg<-halfWidth) ret = -1.;
		else
			if(arg> halfWidth) ret = 1.;
		else
			ret = Math.sin(3.14159*arg/width_weld);

		ret = ret*this.boost_weld/2. + a_weld + b_weld*arg;
		return ret;
	}


	public double[] refAmpl(int x) {
		double []ret = new double[2];
		double retA = 0.;
		double retErr = 0.;
	//	double distance = 0.;
		if(this.linearEvent == 1)	{
			retA = this.linearF(x);
	//		distance = endLinear - beginLinear;
			retErr = Math.sqrt(a_linearError*a_linearError);// +
//                         b_linearError*distance*b_linearError*distance/2.);
		}
		else
			if(this.connectorEvent == 1) {
				retA = this.connectorF(x);
				retErr = Math.sqrt(a1_connectorError*a1_connectorError +
													 a2_connectorError*a2_connectorError +
													 aLet_connectorError*aLet_connectorError);
			}
			else
				if(this.weldEvent == 1)	{
					retA = this.weldF(x);
					retErr = Math.sqrt(a_weldError*a_weldError +
														 boost_weldError*boost_weldError);
				}
				else {
					retA = 0.;
					retErr = 10.;
				}
				ret[0] = retA;
				ret[1] = retErr;
				return ret;
	}



	public double refAmplitude(int x) {
		double retA = 0.;

		if(this.linearEvent == 1)	{
			retA = this.linearF(x);
		}
		else
			if(this.connectorEvent == 1) {
				retA = this.connectorF(x);
			}
			else
				if(this.weldEvent == 1)	{
					retA = this.weldF(x);
				}
				else {
					retA = 0.;
				}
				return retA;
	}


	public void setParams(double []signal, int begin) {
		// Linear
		a_linear = signal[begin];
		a_linearError = signal[begin+1];
		b_linear = signal[begin+2];
		b_linearError = signal[begin+3];
		beginLinear = (int)signal[begin+4];
		endLinear =   (int)signal[begin+5];
		chi2Linear = signal[begin+6];
		linearEvent = (int)signal[begin+7];
		// Weld
		a_weld = signal[begin+8];
		a_weldError = signal[begin+9];
		b_weld = signal[begin+10];
		b_weldError = signal[begin+11];
		boost_weld = signal[begin+12];
		boost_weldError = signal[begin+13];
		center_weld = signal[begin+14];
		center_weldError = signal[begin+15];
		width_weld = signal[begin+16];
		width_weldError = signal[begin+17];
		beginWeld = (int)signal[begin+18];
		endWeld = (int)signal[begin+19];
		chi2Weld = signal[begin+20];
		weldEvent = (int)signal[begin+21];
		//Connector
		a1_connector = signal[begin+22];
		a1_connectorError = signal[begin+23];
		a2_connector = signal[begin+24];
		a2_connectorError = signal[begin+25];
		aLet_connector = signal[begin+26];
		aLet_connectorError = signal[begin+27];
		width_connector = signal[begin+28];
		width_connectorError = signal[begin+29];
		center_connector = signal[begin+30];
		center_connectorError = signal[begin+31];
		sigma1_connector = signal[begin+32];
		sigma1_connectorError = signal[begin+33];
		sigma2_connector = signal[begin+34];
		sigma2_connectorError = signal[begin+35];
		sigmaFit_connector = signal[begin+36];
		sigmaFit_connectorError = signal[begin+37];
		k_connector = signal[begin+38];
		k_connectorError = signal[begin+39];
		beginConnector = (int)signal[begin+40];
		endConnector = (int)signal[begin+41];
		chi2Connector = signal[begin+42];
		connectorEvent = (int)signal[begin+43];
	}

	public double expa(double x, double s1, double s2, double part)	{
		double ret = 0.;

		double arg1 = x/s1;
		double arg2 = x/s2;

		ret = Math.exp(-arg1)*part + Math.exp(-arg2)*(1.-part);

		return ret;
	}

// functions for default threshold
//  public double connectorThresholdF(int x, double dL, double dA, double dX, double dC,
//                                    ReflectogramEvent previousEvent, ReflectogramEvent nextEvent)
//  {
//    double ret = 0.;
//    double arg;
//    double arg1;
//    double arg2;
//
//    this.beginConnector = (int)(this.beginConnector-dX+dC);
//    this.endConnector = (int)(this.endConnector+dX+dC);
//
//    if(this.beginConnector<0)
//      this.beginConnector = 0;
//
//    this.beginLinear = this.beginWeld = this.beginConnector;
//    this.endLinear = this.endWeld = this.endConnector;
//
//    if(previousEvent != null)
//    {
//      previousEvent.endConnector = previousEvent.endLinear = previousEvent.endWeld = this.beginConnector;
//    }
//    if(nextEvent != null)
//    {
//      nextEvent.beginConnector = nextEvent.beginLinear = nextEvent.beginWeld = this.endConnector;
//    }
//
//
//
//    double width_connector = this.width_connector+dX*2.;
//    double a1_connector = this.a1_connector+dA;
//    double a2_connector = this.a2_connector+dA;
//    double aLet_connector = this.aLet_connector+dL;
//    double center_connector = this.center_connector+dC;
//
//    arg = x-center_connector;
//    arg1 = arg+width_connector/2.;
//    arg2 = arg-width_connector/2.;
//
//    if(arg<-width_connector/2.)	{
//      ret = a1_connector;
//    }
//    else
//      if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
//        ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
//              a1_connector;
//      }
//      else
//        if(arg>width_connector/2.) {
//          ret = (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
//              sigma1_connector))) -
//                (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
//                sigma1_connector))-a2_connector)*
//                (1. - expa(arg2, sigma2_connector,
//                sigmaFit_connector, k_connector) );
//        }
//        else
//          ret = 0.;
//
//        return ret;
//  }


	public double connectorThresholdF(int x, double dL, double dA, double dX, double dC)
	{
		double ret = 0.;
		double arg;
		double arg1;
		double arg2;

		double width_connector = this.width_connector+dX*2.;
		double a1_connector = this.a1_connector+dA;
		double a2_connector = this.a2_connector+dA;
		double aLet_connector = this.aLet_connector+dL;
		double center_connector = this.center_connector+dC;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;

		if(arg<-width_connector/2.)	{
			ret = a1_connector;
		}
		else
			if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
				ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
							a1_connector;
			}
			else
				if(arg>width_connector/2.) {
					ret = (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
							sigma1_connector))) -
								(a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
								sigma1_connector))-a2_connector)*
								(1. - expa(arg2, sigma2_connector,
								sigmaFit_connector, k_connector) );
				}
				else
					ret = 0.;

				return ret;
	}


	public double connectorThresholdF(int x, int thresholdNumeral)
	{
		double dL;
		double dA;
		double dX;
		double dC;
		if(threshold == null) //if threshold is not set;
			return 0.;
		else
		{
			dL = threshold.dL[thresholdNumeral];
			dA = threshold.dA[thresholdNumeral];
			dX = threshold.dX[thresholdNumeral];
			dC = threshold.dC[thresholdNumeral];
		}


		double ret = 0.;
		double arg;
		double arg1;
		double arg2;

		double width_connector = this.width_connector+dX*2.;
		double a1_connector = this.a1_connector+dA;
		double a2_connector = this.a2_connector+dA;
		double aLet_connector = this.aLet_connector+dL;
		double center_connector = this.center_connector+dC;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;

		if(arg<-width_connector/2.)	{
			ret = a1_connector;
		}
		else
			if(arg>=-width_connector/2. && arg<=width_connector/2.)	{
				ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
							a1_connector;
			}
			else
				if(arg>width_connector/2.) {
					ret = (a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
							sigma1_connector))) -
								(a1_connector+aLet_connector*(1.-Math.exp(-width_connector/
								sigma1_connector))-a2_connector)*
								(1. - expa(arg2, sigma2_connector,
								sigmaFit_connector, k_connector) );
				}
				else
					ret = 0.;

				return ret;
	}


	public double linearThresholdF(int x, double dA)	{
		double ret;
		double arg = x-beginLinear;
		double a_linear = this.a_linear+dA;
		ret = a_linear + b_linear*arg;
		return ret;
	}


	public double linearThresholdF(int x, int thresholdNumeral)
	{
		double dA;
		if(threshold == null) // Case, when threshold is not set;
			return 0.;
		else
		{
			dA = threshold.dA[thresholdNumeral];
		}

		double ret;
		double arg = x-beginLinear;

		double a_linear = this.a_linear+dA;

		ret = a_linear + b_linear*arg;
		return ret;
	}

	public double weldThresholdF(int x, double dA, double dX) {

		double a_weld = this.a_weld + dA;

		double ret;
		double arg = x-this.center_weld;
		double halfWidth = this.width_weld/2.;

		if(arg<-halfWidth) ret = -1.;
		else
			if(arg> halfWidth) ret = 1.;
		else
			ret = Math.sin(3.14159*arg/width_weld);

		ret = ret*this.boost_weld/2. + a_weld + b_weld*arg;
		return ret;
	}


	public double weldThresholdF(int x, int thresholdNumeral)
	{
		double dA;
		if(threshold == null)
			return 0.;
		else
		{
			dA = threshold.dA[thresholdNumeral];
		}

		double a_weld = this.a_weld + dA;

		double ret;
		double arg = x-this.center_weld;
		double halfWidth = this.width_weld/2.;

		if(arg<-halfWidth) ret = -1.;
		else
			if(arg> halfWidth) ret = 1.;
		else
			ret = Math.sin(3.14159*arg/width_weld);

		ret = ret*this.boost_weld/2. + a_weld + b_weld*arg;
		return ret;
	}

	public int getType() {
		if (linearEvent == 1)
			return LINEAR;
		if (weldEvent == 1)
			return WELD;
		if (connectorEvent == 1)
			return CONNECTOR;
		return 0;
	}

	public void setType(int type)	{
		if (type == LINEAR)	{
			linearEvent = 1;
			weldEvent = 0;
			connectorEvent = 0;
		}
		else if (type == WELD) {
			linearEvent = 0;
			weldEvent = 1;
			connectorEvent = 0;
		}
		else if (type == CONNECTOR)	{
			linearEvent = 0;
			weldEvent = 0;
			connectorEvent = 1;
		}
	}

	public ReflectogramEvent copy()	{
		ReflectogramEvent re = new ReflectogramEvent();
		re.a_linear = a_linear;
		re.a_linearError = a_linearError;
		re.b_linear = b_linear;
		re.b_linearError = b_linearError;
		re.beginLinear = beginLinear;
		re.endLinear = endLinear;
		re.chi2Linear = chi2Linear;
		re.linearEvent = linearEvent;
// Parameters for the welds
		re.a_weld = a_weld;
		re.a_weldError = a_weldError;
		re.b_weld = b_weld;
		re.b_weldError = b_weldError;
		re.boost_weld = boost_weld;
		re.boost_weldError = boost_weldError;
		re.center_weld = center_weld;
		re.center_weldError = center_weldError;
		re.width_weld = width_weld;
		re.width_weldError = width_weldError;
		re.beginWeld = beginWeld;
		re.endWeld = endWeld;
		re.chi2Weld = chi2Weld;
		re.weldEvent = weldEvent;
// Parameters for the Connectors
		re.a1_connector = a1_connector;
		re.a1_connectorError = a1_connectorError;
		re.a2_connector = a2_connector;
		re.a2_connectorError = a2_connectorError;
		re.aLet_connector = aLet_connector;
		re.aLet_connectorError = aLet_connectorError;
		re.width_connector = width_connector;
		re.width_connectorError = width_connectorError;
		re.center_connector = center_connector;
		re.center_connectorError = center_connectorError;
		re.sigma1_connector = sigma1_connector;
		re.sigma1_connectorError = sigma1_connectorError;
		re.sigma2_connector = sigma2_connector;
		re.sigma2_connectorError = sigma2_connectorError;
		re.sigmaFit_connector = sigmaFit_connector;
		re.sigmaFit_connectorError = sigmaFit_connectorError;
		re.k_connector = k_connector;
		re.k_connectorError = k_connectorError;
		re.beginConnector = beginConnector;
		re.endConnector = endConnector;
		re.chi2Connector = chi2Connector;
		re.connectorEvent = connectorEvent;

		re.threshold = threshold;
		return re;
	}

	public byte[] getByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(EVENT_SIZE);
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeDouble(this.a_linear);
			dos.writeDouble(this.a_linearError);
			dos.writeDouble(this.b_linear);
			dos.writeDouble(this.b_linearError);
			dos.writeInt(this.beginLinear);
			dos.writeInt(this.endLinear);
			dos.writeDouble(this.chi2Linear);
			dos.writeInt(this.linearEvent);

			dos.writeDouble(this.a_weld);
			dos.writeDouble(this.a_weldError);
			dos.writeDouble(this.b_weld);
			dos.writeDouble(this.b_weldError);
			dos.writeDouble(this.boost_weld);
			dos.writeDouble(this.boost_weldError);
			dos.writeDouble(this.center_weld);
			dos.writeDouble(this.center_weldError);
			dos.writeDouble(this.width_weld);
			dos.writeDouble(this.width_weldError);
			dos.writeInt(this.beginWeld);
			dos.writeInt(this.endWeld);
			dos.writeDouble(this.chi2Weld);
			dos.writeInt(this.weldEvent);

			dos.writeDouble(this.a1_connector);
			dos.writeDouble(this.a1_connectorError);
			dos.writeDouble(this.a2_connector);
			dos.writeDouble(this.a2_connectorError);
			dos.writeDouble(this.aLet_connector);
			dos.writeDouble(this.aLet_connectorError);
			dos.writeDouble(this.width_connector);
			dos.writeDouble(this.width_connectorError);
			dos.writeDouble(this.center_connector);
			dos.writeDouble(this.center_connectorError);
			dos.writeDouble(this.sigma1_connector);
			dos.writeDouble(this.sigma1_connectorError);
			dos.writeDouble(this.sigma2_connector);
			dos.writeDouble(this.sigma2_connectorError);
			dos.writeDouble(this.sigmaFit_connector);
			dos.writeDouble(this.sigmaFit_connectorError);
			dos.writeDouble(this.k_connector);
			dos.writeDouble(this.k_connectorError);
			dos.writeInt(this.beginConnector);
			dos.writeInt(this.endConnector);
			dos.writeDouble(this.chi2Connector);
			dos.writeInt(this.connectorEvent);

			dos.close();
		}
		catch (IOException ioe) {
			System.out.println("Exception while getting byte array from reflectogram event: " + ioe.getMessage());
			ioe.printStackTrace();
			return null;
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(ReflectogramEvent[] revents) {
		byte[] bar = new byte[EVENT_SIZE * revents.length];
		byte[] bar1;
		for (int i = 0; i < revents.length; i++) {
			bar1 = revents[i].getByteArray();
			for (int j = 0; j < EVENT_SIZE; j++)
				bar[i * EVENT_SIZE + j] = bar1[j];
		}
		return bar;
	}

	public static ReflectogramEvent[] fromByteArray(byte[] bar) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		byte[] buf = new byte[EVENT_SIZE];
		LinkedList ll = new LinkedList();
		try {
			while (dis.read(buf) == EVENT_SIZE)
				ll.add(new ReflectogramEvent(buf));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return (ReflectogramEvent[])ll.toArray(new ReflectogramEvent[ll.size()]);
	}


	public ReflectogramEvent getThresholdReflectogramEvent(int thresholdNumeral)
	{
		ReflectogramEvent re = this.copy();
		if(threshold != null && thresholdNumeral>=0 && thresholdNumeral<4)
		{
			if(re.connectorEvent == 1)
			{
				re.width_connector = this.width_connector + threshold.dX[thresholdNumeral]*2.;
				re.a1_connector = this.a1_connector + threshold.dA[thresholdNumeral];
				re.a2_connector = this.a2_connector + threshold.dA[thresholdNumeral];
				re.aLet_connector = this.aLet_connector + threshold.dL[thresholdNumeral];
				re.center_connector = this.center_connector + threshold.dC[thresholdNumeral];

				re.beginLinear = re.beginWeld =
				re.beginConnector = (int)(re.beginConnector - threshold.dX[thresholdNumeral] +
														threshold.dC[thresholdNumeral]);

				re.endLinear = re.endWeld =
				re.endConnector = (int)(re.endConnector + threshold.dX[thresholdNumeral] +
														threshold.dC[thresholdNumeral]);
			}
			else if(re.linearEvent == 1)
			{
				re.a_linear = this.a_linear + threshold.dA[thresholdNumeral];
			}
			else
			{
				re.a_weld = this.a_weld + threshold.dA[thresholdNumeral];
			}
		}

		return re;
	}

	public void setDeltaX(double delta_x)
	{
		this.delta_x = delta_x;
	}

	public double getDeltaX()
	{
		return this.delta_x;
	}

}


*/