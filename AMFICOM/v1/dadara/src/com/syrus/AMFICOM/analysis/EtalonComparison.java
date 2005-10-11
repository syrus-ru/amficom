/*-
 * $Id: EtalonComparison.java,v 1.1 2005/10/11 16:42:01 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/10/11 16:42:01 $
 * @module dadara
 */
public interface EtalonComparison {
	/**
	 * ���������� ������ ���������� ��������������
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
