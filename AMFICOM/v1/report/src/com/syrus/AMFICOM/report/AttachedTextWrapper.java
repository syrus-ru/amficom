/*-
 * $Id: AttachedTextWrapper.java,v 1.2 2005/10/25 19:53:08 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.List;

import com.syrus.util.PropertyChangeException;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:08 $
 * @module report
 */

public final class AttachedTextWrapper extends StorableElementWrapper<AttachedTextStorableElement> {

	public static final String COLUMN_TEXT = "text";
	public static final String COLUMN_FONT_NAME = "font_name";
	public static final String COLUMN_FONT_SYLE = "font_syle";
	public static final String COLUMN_FONT_SIZE = "font_size";
	public static final String COLUMN_HORIZONTAL_ATTACH_TYPE = "horizontal_attach_type";
	public static final String COLUMN_VERTICAL_ATTACH_TYPE = "vertical_attach_type";
	public static final String COLUMN_DISTANCE_X = "distance_x";
	public static final String COLUMN_DISTANCE_Y = "distance_y";
	public static final String COLUMN_HORIZONTAL_ATTACHER_ID = "horizontal_attacher_id";
	public static final String COLUMN_VERTICAL_ATTACHER_ID = "vertical_attacher_id";
	
	public static final int SIZE_FONT_NAME_COLUMN = 64;

	private static AttachedTextWrapper instance;

	private AttachedTextWrapper() {
		throw new UnsupportedOperationException();
	}

	public static AttachedTextWrapper getInstance() {
		if (instance == null) {
			instance = new AttachedTextWrapper();
		}
		return instance;
	}

	@Override
	public void setValue(final AttachedTextStorableElement attachedText, String key, Object value) throws PropertyChangeException {
		throw new UnsupportedOperationException();		
	}

	public List<String> getKeys() {
		throw new UnsupportedOperationException();
	}

	public String getName(String key) {
		throw new UnsupportedOperationException();
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException();
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValue(final AttachedTextStorableElement attachedText, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}
}
