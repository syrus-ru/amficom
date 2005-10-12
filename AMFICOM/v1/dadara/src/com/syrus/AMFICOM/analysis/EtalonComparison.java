/*-
 * $Id: EtalonComparison.java,v 1.2 2005/10/12 12:06:02 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.List;

import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;

/**
 * ���������� ���������� ���������.
 * �� ������������ �������������� - ��� ������� �� ������� �����������.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/10/12 12:06:02 $
 * @module dadara
 */
public interface EtalonComparison {
	/**
	 * ���������� ������ ���������� ��������������
	 * XXX: ������� �� ���������� �� ������ �� �� ������ ��������������
	 * @return ������ ���������� ��������������
	 */
	List<ReflectogramMismatchImpl> getAlarms();
	/**
	 * ���������� ����� ��������� �������� �/�, ���������� �� ����������� ���������
	 * � ������������ � ���������� {@link ReflectometryEvaluationOverallResult}
	 * @return ����� ��������� �������� �/�, ���������� �� ����������� ���������
	 */
	ReflectometryEvaluationOverallResult getOverallResult();
	/**
	 * ���������� ��������� �������� �/� �� ������� �������, ���������� �� ����������� ��������� 
	 * � ������������ � ���������� {@link EvaluationPerEventResult}
	 * @return ��������� �������� �/� �� ������� �������, ���������� �� ����������� ���������
	 */
	EvaluationPerEventResult getPerEventResult();
}
