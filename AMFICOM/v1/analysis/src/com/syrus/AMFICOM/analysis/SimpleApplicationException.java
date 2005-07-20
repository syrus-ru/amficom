/*-
 * $Id: SimpleApplicationException.java,v 1.1 2005/07/20 11:09:36 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/07/20 11:09:36 $
 * @module
 */
public class SimpleApplicationException extends Exception {
	public static final String KEY_NULL_REFLECTOGRAMMA = "errorNoReflectogramma";
	public SimpleApplicationException(String key) {
		super(key);
	}

	@Override
	public String getLocalizedMessage() {
		return LangModelAnalyse.getString(getMessage());
	}
}
