/*-
 * $Id: AnalysisParameters.java,v 1.14 2005/08/29 09:39:31 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Ќабор параметров анализа с контролем допустимости (согласованности) набора.
 * ≈сли нужно изменить сразу несколько аргументов - используйте методы
 * {@link #getStorageClone()} и {@link #setAllFrom(AnalysisParametersStorage)}
 * @author $Author: saa $
 * @version $Revision: 1.14 $, $Date: 2005/08/29 09:39:31 $
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
	 * @return список рекомендуемых значений noiseFactor
	 */
	public static double[] getRecommendedNoiseFactors() {
		return AnalysisParametersStorage.getRecommendedNoiseFactors();
	}

	public AnalysisParameters(double minThreshold,
			double minSplice,
			double minConnector,
			double minEnd,
			double noiseFactor)
	throws InvalidAnalysisParametersException {
		this.storage = new AnalysisParametersStorage(
			minThreshold,
			minSplice,
			minConnector,
			minEnd,
			noiseFactor);
		checkStorage(this.storage);
	}

	public AnalysisParameters(DataInputStream dis)
	throws IOException, InvalidAnalysisParametersException {
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
	 * @return {@link AnalysisParametersStorage} дл€ данного объекта
	 */
	public AnalysisParametersStorage getStorageClone() {
		return (AnalysisParametersStorage) this.storage.clone();
	}

	/**
	 * ”станавливает параметры по заданному {@link AnalysisParametersStorage}
	 * @param aps устанавливаемый набор параметров
	 * @throws InvalidAnalysisParametersException если заданный набор
	 * параметров недопустим 
	 */
	public void setAllFrom(AnalysisParametersStorage aps)
	throws InvalidAnalysisParametersException {
		// провер€ем допустимость данного набора.
		// ≈сли недопустим - бросаем исключение
		checkStorage(aps);
		// Ќабор допустим. «агружаем его
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
				// если считан недопустимый набор парамеров,
				// считаем это ошибкой входного потока
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

	public double getMinConnector() {
		return storage.getMinConnector();
	}

	public double getMinEnd() {
		return storage.getMinEnd();
	}

	public double getMinSplice() {
		return storage.getMinSplice();
	}

	public double getMinThreshold() {
		return storage.getMinThreshold();
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
		// создаем копию набора параметров
		AnalysisParametersStorage test = getTestStorage();
		// устанавливаем новый параметр
		test.setL2rsaBig(big);
		// провер€ем его допустимость. ≈сли недопустим - бросаем исключение
		checkStorage(test);
		// Ќабор допустим. «агружаем его
		this.storage.setAllFrom(test);
	}

	public void setMinConnector(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setMinConnector(v);
		setAllFrom(test);
	}

	public void setMinEnd(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setMinEnd(v);
		setAllFrom(test);
	}

	public void setMinSplice(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setMinSplice(v);
		setAllFrom(test);
	}

	public void setMinThreshold(double v)
	throws InvalidAnalysisParametersException {
		AnalysisParametersStorage test = getTestStorage();
		test.setMinThreshold(v);
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
		AnalysisParametersStorage test = getTestStorage();
		test.setSensitivity(v);
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
}
