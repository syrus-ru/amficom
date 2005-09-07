/*-
 * $Id: SimpleApplicationException.java,v 1.3 2005/09/07 02:56:49 arseniy Exp $
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
 * @author $Author: arseniy $
 * @author saa (��� ����� �� ������������ ������ � ����������� ���������� ����)
 * @version $Revision: 1.3 $, $Date: 2005/09/07 02:56:49 $
 * @module
 */
public class SimpleApplicationException extends Exception {
	private static final long serialVersionUID = 4385984337203935705L;

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
