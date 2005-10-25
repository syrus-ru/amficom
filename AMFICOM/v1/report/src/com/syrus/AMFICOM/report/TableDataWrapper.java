/*-
 * $Id: TableDataWrapper.java,v 1.2 2005/10/25 19:53:08 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:08 $
 * @module report
 */
public final class TableDataWrapper extends DataWrapper {
	public static final String COLUMN_VERTICAL_DIVISION_COUNT = "vertical_division";

	private static TableDataWrapper instance;

	private TableDataWrapper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(final DataStorableElement tableData, String key, Object value) throws PropertyChangeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getKeys() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValue(final DataStorableElement tableData, String key) {
		throw new UnsupportedOperationException();
	}

	@Override
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
