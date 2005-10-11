/*-
 * $Id: ReflectometryAnalysisResult.java,v 1.2 2005/10/11 13:22:40 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * ���������� ������� � ������.
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/10/11 13:22:40 $
 * @module reflectometry
 */
public interface ReflectometryAnalysisResult {
	/**
	 * ���������� byte[]-������������� ��� AnalysisResult �� dadara.
	 * @return byte[]-������������� ��� AnalysisResult �� dadara;
	 *   null, ���� ������� �� ����.
	 */
	byte[] getDadaraAnalysisResultBytes();
	/**
	 * ���������� byte[]-������������� ��� ReflectogramMismatchImpl[] �� dadara.
	 * @return byte[]-������������� ��� ReflectogramMismatchImpl[] �� dadara;
	 *   null, ���� ��������� �� ����.
	 */
	byte[] getDadaraReflectogramMismatchBytes();
	/**
	 * ���������� byte[]-����� ��� EvaluationPerEventResult �� dadara.
	 * @return byte[]-����� ��� EvaluationPerEventResult �� dadara;
	 *   null, ���� ��������� �� ����.
	 */
	byte[] getDadaraEvaluationPerEventResultBytes();
	/**
	 * ���������� ����� ��������� ���������� ��������� � ��������.
	 * @return ����� ��������� ���������� ��������� � ��������;
	 *   null, ���� ��������� �� ����.
	 */
	ReflectometryEvaluationOverallResult
			getReflectometryEvaluationOverallResult();
}
