/*-
 * $Id: ReflectometryAnalysisCriteria.java,v 1.1 2005/09/01 16:48:30 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.io.DataFormatException;

/**
 * ��������� ������� ��� ������������������� ����������.
 * ������� ��:
 * <ul>
 * <li> {@link #getDadaraCriteria()} ���������� ������� dadara
 * </ul>
 * <p>
 * �� ������ ������ modifier-������ �� ��������������.
 * <p>
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/09/01 16:48:30 $
 * @module
 */
public class ReflectometryAnalysisCriteria {
	private ParameterSet criteriaSet;
	private byte[] dadaraCriteria;

	ReflectometryAnalysisCriteria(final ParameterSet criteria)
	throws DataFormatException {
		this.criteriaSet = criteria;
		unpack();
	}

	private void unpack() throws DataFormatException {
		Parameter[] params = this.criteriaSet.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			ParameterType p = params[i].getType();
			if (p.equals(ParameterType.DADARA_CRITERIA)) {
				this.dadaraCriteria = params[i].getValue();
			}
		}
		if (this.dadaraCriteria == null) {
			throw new DataFormatException(); // no criteria found in set
		}
	}

	/**
	 * @return ������ ���� ��� �������������� ������� ���������� ������� dadara
	 */
	public byte[] getDadaraCriteria() {
		return this.dadaraCriteria.clone();
	}

	/**
	 * �� ����������
	 */
	public void setDadaraCriteria(byte[] dadaraCriteria) {
//		this.dadaraCriteria = dadaraCriteria;
		throw new UnsupportedOperationException(); // @todo: implement
	}
}
