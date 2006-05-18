/*-
 * $Id: ReportTemplateWrapper.java,v 1.2 2005/10/25 19:53:08 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:08 $
 * @module report
 */
public final class ReportTemplateWrapper
		extends StorableObjectWrapper<ReportTemplate> {

	public static final String COLUMN_SHEET_SIZE = "sheet_size";
	public static final String COLUMN_ORIENTATION = "orientation";
	public static final String COLUMN_MARGIN_SIZE = "margin_size";
	public static final String COLUMN_DESTINATION_MODULE = "destination_module";
	public static final int SIZE_DESTINATION_MODULE_COLUMN = 64;

	private static ReportTemplateWrapper instance;

	private ReportTemplateWrapper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(final ReportTemplate reportTemplate, String key, Object value) throws PropertyChangeException {
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
	public Object getValue(final ReportTemplate reportTemplate, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}

	public static ReportTemplateWrapper getInstance() {
		if (instance == null) {
			instance = new ReportTemplateWrapper();
		}
		return instance;
	}
}
