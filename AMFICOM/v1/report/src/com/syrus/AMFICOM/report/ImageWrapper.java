/*-
 * $Id: ImageWrapper.java,v 1.1 2005/09/30 12:34:07 max Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/09/30 12:34:07 $
 * @module report
 */
public class ImageWrapper extends StorableObjectWrapper {

	public static final String COLUMN_BITMAP_IMAGE_RESOURCE_ID = "bitmap_image_res_id";
	
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