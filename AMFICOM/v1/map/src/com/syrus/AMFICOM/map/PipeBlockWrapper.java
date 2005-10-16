/*-
 * $Id: PipeBlockWrapper.java,v 1.2 2005/10/16 16:40:03 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.2 $, $Date: 2005/10/16 16:40:03 $
 * @module map
 */

public class PipeBlockWrapper extends StorableObjectWrapper {
	
	public static final String COLUMN_NUMBER = "pipe_number";
	public static final String COLUMN_DIMENSION_X = "dimension_x";
	public static final String COLUMN_DIMENSION_Y = "dimension_y";
	public static final String COLUMN_LEFT_TO_RIGHT = "left_to_right";
	public static final String COLUMN_TOP_TO_BOTTOM = "top_to_bottom";
	public static final String COLUMN_HORIZONTAL_VERTICAL = "horizontal_vertical";
	

	@Override
	public void setValue(StorableObject storableObject, String key, Object value) throws PropertyChangeException {
		throw new UnsupportedOperationException();
	}

	public List getKeys() {
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

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Object object, String key, Object value) throws PropertyChangeException {
		throw new UnsupportedOperationException();
	}

}
