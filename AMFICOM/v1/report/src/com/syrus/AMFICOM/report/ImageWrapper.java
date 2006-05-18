/*-
 * $Id: ImageWrapper.java,v 1.2 2005/10/25 19:53:08 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.List;

import com.syrus.util.PropertyChangeException;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:08 $
 * @module report
 */
public final class ImageWrapper extends StorableElementWrapper<ImageStorableElement> {

	public static final String COLUMN_BITMAP_IMAGE_RESOURCE_ID = "bitmap_image_res_id";

	private static ImageWrapper instance;

	private ImageWrapper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(final ImageStorableElement image, String key, Object value) throws PropertyChangeException {
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
	public Object getValue(final ImageStorableElement image, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}

	public static ImageWrapper getInstance() {
		if (instance == null) {
			instance = new ImageWrapper();
		}
		return instance;
	}
}
