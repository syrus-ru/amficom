/*-
 * $Id: TableDataWrapper.java,v 1.3 2005/11/16 18:37:17 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.report;

import java.util.List;

import com.syrus.util.PropertyChangeException;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.3 $, $Date: 2005/11/16 18:37:17 $
 * @module report
 */
public final class TableDataWrapper extends StorableElementWrapper<TableDataStorableElement> {
	public static final String COLUMN_VERTICAL_DIVISION_COUNT = "vertical_division";

	private static TableDataWrapper instance;

	private TableDataWrapper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(final TableDataStorableElement tableData, String key, Object value) throws PropertyChangeException {
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
	public Object getValue(final TableDataStorableElement tableData, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}

	public static TableDataWrapper getInstance() {
		if (instance == null) {
			instance = new TableDataWrapper();
		}
		return instance;
	}
}
