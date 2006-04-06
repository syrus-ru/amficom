/*-
 * $Id: ReflectometryAnalysisCriteria.java,v 1.1.2.5 2006/04/06 09:20:14 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.ActionParameter;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;

/**
 * ��������� ������� � ������� ��� ������������������� ����������.
 * ������� ��:
 * <ul>
 * <li> ���������� ������� (���� �����������):
 * <ul>
 * <li> {@link #getDadaraCriteria()} ���������� ������� dadara
 * </ul>
 * <li> ������� (����� �� ����, ���� ��������� � ��� �������
 * ������� {@link #hasEtalon()}):
 * <ul>
 * <li> {@link #getDadaraEtalon()} �������-������� (������������ � dadara)
 * <li> {@link #getReflectogrammaEtalon()} ��������� �������������� (BellcoreStructure)
 * </ul>
 * </ul>
 * <p>
 * �� ������ ������ modifier-������ �� ��������������.
 * <p>
 * @todo �������������, �.�. ������ �������� � ������
 * 
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1.2.5 $, $Date: 2006/04/06 09:20:14 $
 * @module
 */
public class ReflectometryAnalysisCriteria {
	private ActionTemplate<Analysis> analysisTemplate;
	private byte[] dadaraCriteria = null;
	private boolean hasEtalon = false;
	private byte[] dadaraEtalon = null;
	private byte[] reflectogrammaEtalon = null;

	/**
	 * @param analysisTemplate
	 *        ������ ��� �������.
	 * @throws DataFormatException
	 *         ������������ ������ ������� ������.
	 * @throws ApplicationException
	 *         ������ ��� ���������� �� ����.
	 */
	ReflectometryAnalysisCriteria(final ActionTemplate<Analysis> analysisTemplate) throws DataFormatException, ApplicationException {
		this.analysisTemplate = analysisTemplate;
		this.unpack();
	}

	private void unpack() throws DataFormatException, ApplicationException {
		final Set<ActionParameter> actionParameters = this.analysisTemplate.getActionParameters();
		for (final ActionParameter actionParameter : actionParameters) {
			final String parameterTypeCodename = actionParameter.getTypeCodename();
			if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.DADARA_CRITERIA.stringValue())) {
				this.dadaraCriteria = actionParameter.getValue();
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.DADARA_ETALON.stringValue())) {
				this.dadaraEtalon = actionParameter.getValue();
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.REFLECTOGRAMMA_ETALON.stringValue())) {
				this.reflectogrammaEtalon = actionParameter.getValue();
			} else {
				Log.errorMessage("Unknown codename: " + parameterTypeCodename);
			}
		}

		if (this.dadaraCriteria == null) {
			throw new DataFormatException("No criteria"); // no criteria found
		}
		this.hasEtalon = this.dadaraEtalon != null || this.reflectogrammaEtalon != null;
		if (this.hasEtalon) {
			if (this.dadaraEtalon == null) {
				throw new DataFormatException("No dadara etalon"); // no dadara etalon
			}
			if (this.reflectogrammaEtalon == null) {
				throw new DataFormatException("No reflectogramma etalon"); // no reflectogramma etalon
			}
		} else {
			this.dadaraEtalon = null;
			this.reflectogrammaEtalon = null;
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

	/**
	 * @return true, ���� � ���� ��������� ���� ������
	 */
	public boolean hasEtalon() {
		return this.hasEtalon;
	}

	/**
	 * @return ������ ���� ��� �������������� �������-������� dadara
	 * @throws IllegalStateException ������� ��� (hasEtalon() == false)
	 */
	public byte[] getDadaraEtalon() {
		if (!hasEtalon()) {
			throw new IllegalStateException();
		}
		return this.dadaraEtalon.clone();
	}

	/**
	 * �� ����������
	 */
	public void setDadaraEtalon(byte[] dadaraEtalon) {
//		this.dadaraEtalon = dadaraEtalon;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	/**
	 * @return ������ ���� ��� �������������� �������������� BellcoreStructure
	 * @throws IllegalStateException ������� ��� (hasEtalon() == false)
	 */
	public byte[] getReflectogrammaEtalon() {
		if (!hasEtalon()) {
			throw new IllegalStateException();
		}
		return this.reflectogrammaEtalon.clone();
	}

	/**
	 * �� ����������
	 */
	public void setReflectogrammaEtalon(byte[] reflectogrammaEtalon) {
//		this.reflectogrammaEtalon = reflectogrammaEtalon;
		throw new UnsupportedOperationException(); // @todo: implement
	}
}
