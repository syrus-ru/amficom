/*
 * $Id: EvaluationTypeWrapper.java,v 1.15 2005/08/08 11:31:45 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.15 $, $Date: 2005/08/08 11:31:45 $
 * @author $Author: arseniy $
 * @module measurement
 */
public class EvaluationTypeWrapper extends StorableObjectWrapper<EvaluationType> {

	public static final String LINK_COLUMN_EVALUATION_TYPE_ID = "evaluation_type_id";

	private static EvaluationTypeWrapper instance;

	private List<String> keys;

	private EvaluationTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EvaluationTypeWrapper getInstance() {
		if (instance == null)
			instance = new EvaluationTypeWrapper();
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
	public Object getValue(final EvaluationType evaluationType, final String key) {
		final Object value = super.getValue(evaluationType, key);
		if (value == null && evaluationType != null) {
			if (key.equals(COLUMN_CODENAME))
				return evaluationType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return evaluationType.getDescription();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final EvaluationType evaluationType, final String key, final Object value) {
		if (evaluationType != null) {
			if (key.equals(COLUMN_CODENAME))
				evaluationType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				evaluationType.setDescription((String) value);
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
	public Class getPropertyClass(final String key) {
		return String.class;
	}

}
