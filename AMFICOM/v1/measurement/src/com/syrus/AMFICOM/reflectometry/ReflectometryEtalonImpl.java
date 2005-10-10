/*-
 * $Id: ReflectometryEtalonImpl.java,v 1.1 2005/10/10 07:38:51 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;

/**
 * Эталон для рефлектометрических измерений. Состоит из:
 * <ul>
 * <li>{@link #getDadaraEtalon} объекта-эталона (используется в dadara)
 * <li>{@link #getReflectogramma} эталонной рефлектограммы (BellcoreStructure)
 * </ul>
 * <p>
 * На данный момент modifier-методы не поддерживаются.
 * </p>
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/10 07:38:51 $
 * @module
 */
public class ReflectometryEtalonImpl implements ReflectometryEtalon {
	private ParameterSet etalonSet;
	private byte[] dadaraEtalon;
	private byte[] reflectogramma;

	ReflectometryEtalonImpl(final ParameterSet etalonSet) {
		this.etalonSet = etalonSet;
		this.unpack();
	}

	private void unpack() {
		Parameter[] params = this.etalonSet.getParameters();
		for (int i = 0; i < params.length; i++) {
			ParameterType type = params[i].getType();
			if (type.equals(ParameterType.DADARA_ETALON)) {
				this.dadaraEtalon = params[i].getValue();
			} else if (type.equals(ParameterType.REFLECTOGRAMMA_ETALON)) {
				this.reflectogramma = params[i].getValue();
			}
		}
	}

	/**
	 * @return массив байт для восстановления объекта-эталона dadara
	 */
	public byte[] getDadaraEtalon() {
		return this.dadaraEtalon.clone();
	}

	/**
	 * не реализован
	 */
	public void setDadaraEtalon(byte[] dadaraEtalon) {
//		this.dadaraEtalon = dadaraEtalon;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	/**
	 * @return массив байт для восстановления рефлектограммы BellcoreStructure
	 */
	public byte[] getReflectogramma() {
		return this.reflectogramma.clone();
	}

	/**
	 * не реализован
	 */
	public void setReflectogramma(byte[] reflectogrammaEtalon) {
		this.reflectogramma = reflectogrammaEtalon;
		throw new UnsupportedOperationException(); // @todo: implement
	}
}
