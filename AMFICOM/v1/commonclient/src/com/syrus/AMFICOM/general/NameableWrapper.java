/*-
* $Id: NameableWrapper.java,v 1.1 2006/06/20 12:15:24 saa Exp $
*
* Copyright (c) 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Namable;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.1 $, $Date: 2006/06/20 12:15:24 $
 * @author $Author: saa $
 * @author many authors
 * @module commonclient
 */
public class NameableWrapper implements Wrapper<Namable> {
	public static final String COLUMN_NAME = "name";
	private static final NameableWrapper instance = new NameableWrapper();

	private final List<String> keys;

	public static NameableWrapper getInstance() {
		return instance;
	}

	private NameableWrapper() {
		this.keys = Collections.singletonList(COLUMN_NAME);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
//		@todo?
//		if (key == COLUMN_NAME) {
//			return LangModelGeneral.getString("NameableWrapper.Text.Name");
//		}
//		return null;
		return key;
	}

	public Class<?> getPropertyClass(final String key) {
		if (key.equals(COLUMN_NAME)) {
			return String.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		return null;
	}

	public void setPropertyValue(final String key,
			final Object objectKey,
			final Object objectValue) {
		// nothing
	}

	public Object getValue(final Namable nameable, final String key) {
		if (nameable != null && key == COLUMN_NAME) {
			return nameable.getName();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Namable nameable,
			final String key,
			final Object value) {
		// not editable!
		throw new UnsupportedOperationException();
	}
}

