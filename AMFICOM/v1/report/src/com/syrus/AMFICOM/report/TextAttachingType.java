/*
 * $Id: TextAttachingType.java,v 1.5 2005/10/08 13:16:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
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
	 * Привязка по вертикали к верхнему краю поля шаблона
	 */
	TO_FIELDS_TOP("report.TextAttachingType.toFieldsTop"),
	/**
	 * Привязка по вертикали к верхнему краю элемента шаблона
	 */
	TO_TOP("report.TextAttachingType.toTop"),
	/**
	 * Привязка по вертикали к нижнему краю элемента шаблона
	 */
	TO_BOTTOM("report.TextAttachingType.toBottom"),
	/**
	 * Привязка по горизонтали к левому краю поля шаблона
	 */
	TO_FIELDS_LEFT("report.TextAttachingType.toFieldsLeft"),
	/**
	 * Привязка по горизонтали к левому краю элемента шаблона
	 */
	TO_LEFT("report.TextAttachingType.toLeft"),
	/**
	 * Привязка по горизонтали к правому краю элемента шаблона
	 */
	TO_RIGHT("report.TextAttachingType.toRight"),
	/**
	 * Привязка по горизонтали по центру элемента шаблона
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
