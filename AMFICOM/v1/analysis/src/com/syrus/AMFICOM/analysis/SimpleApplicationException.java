/*-
 * $Id: SimpleApplicationException.java,v 1.2 2005/07/22 08:35:06 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

/**
 * ���������� ��� ��������� � ����� ����������� ������� ��������� ������.
 * ������������� ��� ������������ ������ � GUI �������������� ���������������
 * ������.
 * � �������� ��������� ������������ ���� ��� �����������.
 * ����� getLocalizedMessage() ���������� �������������� ������ �� ����� �����.
 * @author $Author: saa $
 * @author saa (��� ����� �� ������������ ������ � ����������� ���������� ����)
 * @version $Revision: 1.2 $, $Date: 2005/07/22 08:35:06 $
 * @module
 */
public class SimpleApplicationException extends Exception {
	// XXX: ������ KEY_NULL_REFLECTOGRAMMA ��������� �� ������ ������ GUIUtil.MSG_ERROR_NO_ONE_RESULT_HAS_TRACE
	public static final String KEY_NULL_REFLECTOGRAMMA = "errorNoReflectogramma";
	public SimpleApplicationException(String key) {
		super(key);
	}

	// XXX: ���� �� ������������
	@Override
	public String getLocalizedMessage() {
		return LangModelAnalyse.getString(getMessage());
	}
}
