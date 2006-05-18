/*-
 * $Id: PipeBlockWrapper.java,v 1.3 2005/10/25 19:53:10 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/25 19:53:10 $
 * @module map
 */
public final class PipeBlockWrapper extends StorableObjectWrapper<PipeBlock> {
	
	public static final String COLUMN_NUMBER = "pipe_number";
	public static final String COLUMN_DIMENSION_X = "dimension_x";
	public static final String COLUMN_DIMENSION_Y = "dimension_y";
	public static final String COLUMN_LEFT_TO_RIGHT = "left_to_right";
	public static final String COLUMN_TOP_TO_BOTTOM = "top_to_bottom";
	public static final String COLUMN_HORIZONTAL_VERTICAL = "horizontal_vertical";

	private static PipeBlockWrapper instance;

	private PipeBlockWrapper() {
		throw new UnsupportedOperationException();
	}

	public static PipeBlockWrapper getInstance() {
		if (instance == null) {
			instance = new PipeBlockWrapper();
		}
		return instance;
	}

	@Override
	public void setValue(final PipeBlock pipeBlock, String key, Object value) throws PropertyChangeException {
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
	public Object getValue(final PipeBlock pipeBlock, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}
}
