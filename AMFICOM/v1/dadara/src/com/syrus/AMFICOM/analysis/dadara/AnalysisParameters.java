/*-
 * $Id: AnalysisParameters.java,v 1.20 2005/10/03 13:12:19 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.io.SignatureMismatchException;

/**
 * ����� ���������� ������� � ��������� ������������ (���������������) ������.
 * ���� ����� �������� ����� ��������� ���������� - ����������� ������
 * {@link #getStorageClone()} � {@link #setAllFrom(AnalysisParametersStorage)}
 * ��������������� ��������� ����� �� ������� ����� �������� double,
 * � ����� ����������� ��� ������ modifier-������� ��� �����-���� �����������.
 * @author $Author: saa $
 * @version $Revision: 1.20 $, $Date: 2005/10/03 13:12:19 $
 * @todo add extended parameters save to DOS / restore from DIS
 * @module
 */
public class AnalysisParameters
implements DataStreamable, Cloneable
{
	private static DSReader reader;

	private AnalysisParametersStorage storage;
	private AnalysisParametersStorage testStorage = null;

	/**
	 * @return ������ ������������� �������� noiseFactor
	 */
	public static double[] getRecommendedNoiseFactors() {
		return AnalysisParametersStorage.getRecommendedNoiseFactors();
	}

	public AnalysisParameters(double eventTh,
			double spliceTh,
			double connectorTh,
			double endTh,
			double noiseFactor)
	throws InvalidAnalysisParametersException {
		this.storage = new AnalysisParametersStorage(
			eventTh,
			spliceTh,
			connectorTh,
			endTh,
			noiseFactor);
		checkStorage(this.storage);
	}

	public AnalysisParameters(DataInputStream dis)
	throws IOException, InvalidAnalysisParametersException,
			SignatureMismatchException {
		this.storage = new AnalysisParametersStorage(dis);
		checkStorage(this.storage);
	}

	/**
	 * creates via string of parameters using the default values
	 * @param val text representation of parameters
	 * @param defaults default values
	 * @throws InvalidAnalysisParametersException
	 */
	public AnalysisParameters(String val, AnalysisParameters defaults)
	throws InvalidAnalysisParametersException {
		this.storage = new AnalysisParametersStorage(val, defaults.storage);
		checkStorage(this.storage);
	}

	/**
	 * creates via string of parameters
	 * @param val text representation of parameters
	 * @throws InvalidAnalysisParametersException 
	 * @throws IllegalArgumentException if input string is malformed
	 */
	public AnalysisParameters(String val)
	throws InvalidAnalysisParametersException {
		this.storage = new AnalysisParametersStorage(val);
		checkStorage(this.storage);
	}

	/**
	 * @return {@link AnalysisParametersStorage} ��� ������� �������
	 */
	public AnalysisParametersStorage getStorageClone() {
		return (AnalysisParametersStorage) this.storage.clone();
	}

	/**
	 * ������������� ��������� �� ��������� {@link AnalysisParametersStorage}
	 * @param aps ��������������� ����� ����������
	 * @throws InvalidAnalysisParametersException ���� �������� �����
	 * ���������� ���������� 
	 */
	public void setAllFrom(AnalysisParametersStorage aps)
	throws InvalidAnalysisParametersException {
		// ��������� ������������ ������� ������.
		// ���� ���������� - ������� ����������
		checkStorage(aps);
		// ����� ��������. ��������� ���
		this.storage.setAllFrom(aps);
	}

	@Override
	public String toString() {
		return storage.toString();
	}

	@Override
	public Object clone() {
		try {
			AnalysisParameters ret = (AnalysisParameters)super.clone();
			ret.storage = getStorageClone();
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new InternalError("Unexpected exception: " + e.getMessage());
		}
	}

	private static class DSReader implements DataStreamable.Reader {
		public DataStreamable readFromDIS(DataInputStream dis)
		throws IOException, SignatureMismatchException {
			try {
				return new AnalysisParameters (dis);
			} catch (InvalidAnalysisParametersException e) {
				// ���� ������ ������������ ����� ���������,
				// ������� ��� ������� �������� ������
				throw new SignatureMismatchException("IllegalAnalysisParameters");
			}
		}
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		storage.writeToDOS(dos);
	}
	
	public static DataStreamable.Reader getReader() {
		if (reader == null) {
			reader = new DSReader();
		}
		return reader;
	}

	public double getL2rsaBig() {
		return storage.getL2rsaBig();
	}

	public double getConnectorTh() {
		return storage.getConnectorTh();
	}

	public double getEndTh() {
		return storage.getEndTh();
	}

	public double getSpliceTh() {
		return storage.getSpliceTh();
	}

	public double getEventTh() {
		return storage.getEventTh();
	}

	public double getNoiseFactor() {
		return storage.getNoiseFactor();
	}

	public double getNrs2rsaBig() {
		return storage.getNrs2rsaBig();
	}

	public double getNrs2rsaSmall() {
		return storage.getNrs2rsaSmall();
	}

	public int getNrsMin() {
		return storage.getNrsMin();
	}

	public double getRsaCrit() {
		return storage.getRsaCrit();
	}

	public double getScaleFactor() {
		return storage.getScaleFactor();
	}

	public double getSentitivity() {
		return storage.getSentitivity();
	}

	public double getTau2nrs() {
		return storage.getTau2nrs();
	}

	public void setL2rsaBig(double big)
	throws InvalidAnalysisParametersException {
		// ������� ����� ������ ����������
		AnalysisParametersStorage test = getTestStorage();
		// ������������� ����� ��������
		test.setL2rsaBig(big);
		// ��������� ��� ������������. ���� ���������� - ������� ����������
		checkStorage(test);
		// ����� ��������. ��������� ���
		this.storage.setAllFrom(test);
	}

	public void setConnectorTh(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setConnectorTh(round(v, 1e4));
		setAllFrom(test);
	}

	public void setEndTh(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setEndTh(round(v, 1e4));
		setAllFrom(test);
	}

	public void setSpliceTh(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setSpliceTh(v);
		setAllFrom(test);
	}

	public void setThreshold(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setEventTh(v);
		setAllFrom(test);
	}

	public void setNoiseFactor(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setNoiseFactor(v);
		setAllFrom(test);
	}

	public void setNrs2rsaBig(double nrs2rsaBig)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setNrs2rsaBig(nrs2rsaBig);
		setAllFrom(test);
	}

	public void setNrs2rsaSmall(double nrs2rsaSmall)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setNrs2rsaSmall(nrs2rsaSmall);
		setAllFrom(test);
	}

	public void setNrsMin(int nrsMin)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setNrsMin(nrsMin);
		setAllFrom(test);
	}

	public void setRsaCrit(double rsaCrit)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setRsaCrit(rsaCrit);
		setAllFrom(test);
	}

	public void setScaleFactor(double scaleFactor)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setScaleFactor(scaleFactor);
		setAllFrom(test);
	}

	public void setSensitivity(double v)
	throws InvalidAnalysisParametersException {
		setSensitivity(v, false);
	}

	/**
	 * @param v ����� �������� ���������
	 * @param useLimit true, ���� ���������� ���������� ����� ��������
	 * ���������� ����������
	 * @throws InvalidAnalysisParametersException ���� ����� ��������
	 * ��������� ������� �� ���������� ���������, � ����������� ����������
	 * ���������� ���� �� ���� ���������, ���� �� �������
	 */
	public void setSensitivity(double v, boolean useLimit)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setSensitivity(round(v, 1e4), useLimit);
		setAllFrom(test);
	}

	public void setTau2nrs(double tau2nrs)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setTau2nrs(tau2nrs);
		setAllFrom(test);
	}

	private static void checkStorage(AnalysisParametersStorage aps)
	throws InvalidAnalysisParametersException {
		if (! aps.isCorrect()) {
			throw new InvalidAnalysisParametersException();
		}
	}

	private AnalysisParametersStorage getTestStorage() {
		if (testStorage == null) {
			testStorage = (AnalysisParametersStorage) this.storage.clone();
			return testStorage;
		}
		testStorage.setAllFrom(this.storage);
		return testStorage;
	}

	private static double round(double v, double d) {
		return Math.round(v * d) / d;
	}
}
