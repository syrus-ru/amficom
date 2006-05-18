/*
 * $Id: TemporalPatternWrapper.java,v 1.14 2006/03/13 15:54:25 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.14 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @module measurement
 */
public final class TemporalPatternWrapper extends StorableObjectWrapper<CronTemporalPattern> {

	public static final String COLUMN_VALUE = "value";

	private static TemporalPatternWrapper instance;

	private List<String> keys;

	private TemporalPatternWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_VALUE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TemporalPatternWrapper getInstance() {
		if (instance == null)
			instance = new TemporalPatternWrapper();
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final CronTemporalPattern cronTemporalPattern, final String key) {
		final Object value = super.getValue(cronTemporalPattern, key);
		if (value == null && cronTemporalPattern != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				return cronTemporalPattern.getDescription();
			}
			if (key.equals(COLUMN_VALUE)) {
				return Arrays.asList(cronTemporalPattern.getCronStrings());
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final CronTemporalPattern cronTemporalPattern, final String key, final Object value) {
		if (cronTemporalPattern != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				cronTemporalPattern.setDescription((String) value);
			} else if (key.equals(COLUMN_VALUE)) {
				List cronStrings = (List) value;
				for (Iterator it = cronStrings.iterator(); it.hasNext();) {
					cronTemporalPattern.addTemplate((String) it.next());
				}
			}

		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(COLUMN_VALUE)) {
			return List.class;
		}
		return null;
	}

}
