/*
 * $Id: TemporalPatternWrapper.java,v 1.3 2005/02/03 08:36:47 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/03 08:36:47 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class TemporalPatternWrapper implements StorableObjectWrapper {

	public static final String				COLUMN_VALUE		= "value";

	private static TemporalPatternWrapper	instance;

	private List							keys;

	private TemporalPatternWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_VALUE};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static TemporalPatternWrapper getInstance() {
		if (instance == null)
			instance = new TemporalPatternWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof TemporalPattern) {
			TemporalPattern temporalPattern = (TemporalPattern) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return temporalPattern.getDescription();
			if (key.equals(COLUMN_VALUE))
				return Arrays.asList(temporalPattern.getCronStrings());

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof TemporalPattern) {
			TemporalPattern temporalPattern = (TemporalPattern) object;
			if (key.equals(COLUMN_DESCRIPTION))
				temporalPattern.setDescription((String) value);
			else if (key.equals(COLUMN_VALUE)) {
				List cronStrings = (List) value;
				for (Iterator it = cronStrings.iterator(); it.hasNext();) {
					temporalPattern.addTemplate((String) it.next());

				}
			}

		}
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_VALUE))
			return List.class;
		return String.class;
	}

}
