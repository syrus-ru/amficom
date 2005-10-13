/*-
 * $Id: EtalonComparison.java,v 1.3 2005/10/13 17:09:45 saa Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/10/13 17:09:45 $
 * @module dadara
 */
public interface EtalonComparison {
	/**
	 * ���������� ������ ���������� ��������������, not null
	 * XXX: ������� �� ���������� �� ������ �� �� ������ ��������������
	 * @return ������ ���������� ��������������, not null
	 */
	List<ReflectogramMismatchImpl> getAlarms();
	/**
	 * ���������� ����� ��������� �������� �/�, ���������� �� ����������� ���������, not null
	 * � ������������ � ���������� {@link ReflectometryEvaluationOverallResult}
	 * @return ����� ��������� �������� �/�, ���������� �� ����������� ���������, not null
	 */
	ReflectometryEvaluationOverallResult getOverallResult();
	/**
	 * ���������� ��������� �������� �/� �� ������� �������, ���������� �� ����������� ���������, not null 
	 * � ������������ � ���������� {@link EvaluationPerEventResult}
	 * @return ��������� �������� �/� �� ������� �������, ���������� �� ����������� ���������, not null
	 */
	EvaluationPerEventResult getPerEventResult();
}
