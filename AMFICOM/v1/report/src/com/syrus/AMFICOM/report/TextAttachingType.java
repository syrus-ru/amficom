/*
 * $Id: TextAttachingType.java,v 1.5 2005/10/08 13:16:31 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/10/08 13:16:31 $
 * @module report
 */
public enum TextAttachingType {

	/**
	 * �������� �� ��������� � �������� ���� ���� �������
	 */
	TO_FIELDS_TOP("report.TextAttachingType.toFieldsTop"),
	/**
	 * �������� �� ��������� � �������� ���� �������� �������
	 */
	TO_TOP("report.TextAttachingType.toTop"),
	/**
	 * �������� �� ��������� � ������� ���� �������� �������
	 */
	TO_BOTTOM("report.TextAttachingType.toBottom"),
	/**
	 * �������� �� ����������� � ������ ���� ���� �������
	 */
	TO_FIELDS_LEFT("report.TextAttachingType.toFieldsLeft"),
	/**
	 * �������� �� ����������� � ������ ���� �������� �������
	 */
	TO_LEFT("report.TextAttachingType.toLeft"),
	/**
	 * �������� �� ����������� � ������� ���� �������� �������
	 */
	TO_RIGHT("report.TextAttachingType.toRight"),
	/**
	 * �������� �� ����������� �� ������ �������� �������
	 */
	TO_WIDTH_CENTER("report.TextAttachingType.toWidthCenter");
	
	private final String type;
	
	TextAttachingType(String type) {
		this.type = type;
	}
	
	public String stringValue() {
		return this.type;
	}
	
	public int intValue() {
		return this.ordinal();
	}
	
	public static TextAttachingType fromInt(int i) {
		TextAttachingType[] types = TextAttachingType.values();
		return types[i];
	}
}