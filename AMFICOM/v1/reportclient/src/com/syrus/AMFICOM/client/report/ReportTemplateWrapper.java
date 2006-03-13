/*
 * $Id: ReportTemplateWrapper.java,v 1.2 2006/03/13 15:54:27 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * @version $Revision: 1.2 $, $Date: 2006/03/13 15:54:27 $
 * @author $Author: bass $
 * @module map
 */
public class ReportTemplateWrapper extends StorableObjectWrapper<ReportTemplate> {

	private static ReportTemplateWrapper instance;

	private List<String> keys;

	protected ReportTemplateWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_DESCRIPTION};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static ReportTemplateWrapper getInstance() {
		if (instance == null)
			instance = new ReportTemplateWrapper();
		return instance;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		return String.class;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final ReportTemplate reportTemplate, final String key) {
		if (reportTemplate != null) {
			if (key.equals(COLUMN_NAME)) {
				return reportTemplate.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return reportTemplate.getDescription();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public void setValue(final ReportTemplate reportTemplate, final String key, final Object value) {
		if (reportTemplate != null) {
			if (key.equals(COLUMN_NAME)) {
				reportTemplate.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				reportTemplate.setDescription((String) value);
			}
		}
	}

}
