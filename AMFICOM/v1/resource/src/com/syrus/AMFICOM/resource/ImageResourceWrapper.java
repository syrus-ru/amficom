/*
 * $Id: ImageResourceWrapper.java,v 1.18 2005/10/25 19:53:14 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * @version $Revision: 1.18 $, $Date: 2005/10/25 19:53:14 $
 * @author $Author: bass $
 * @module resource
 */
public final class ImageResourceWrapper extends StorableObjectWrapper<AbstractImageResource> {

	public static final String COLUMN_FILENAME = "filename";
	
	public static final String COLUMN_SORT = "sort";

	public static final String COLUMN_DATA = "data";

	private static ImageResourceWrapper instance;

	private List<String> keys;

	private ImageResourceWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_SORT, COLUMN_CODENAME, COLUMN_DATA };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static ImageResourceWrapper getInstance() {
		if (instance == null)
			instance = new ImageResourceWrapper();
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME)) {
			return String.class;
		} else if (key.equals(COLUMN_SORT)) {
			return Integer.class;
		} else if (key.equals(COLUMN_DATA)) {
			return byte[].class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Object getValue(final AbstractImageResource abstractImageResource, final String key) {
		final Object value = super.getValue(abstractImageResource, key);
		if (value == null && abstractImageResource != null) {
			if (key.equals(COLUMN_SORT))
				return new Integer(abstractImageResource.getSort().value());
			if (key.equals(COLUMN_CODENAME)) {
				switch (abstractImageResource.getSort().value()) {
					case ImageResourceSort._BITMAP:
						return ((BitmapImageResource) abstractImageResource).getCodename();
					case ImageResourceSort._FILE:
						return ((FileImageResource) abstractImageResource).getCodename();
					case ImageResourceSort._SCHEME:
						return "";
				}
			}
			if (key.equals(COLUMN_DATA)) {
				switch (abstractImageResource.getSort().value()) {
					case ImageResourceSort._BITMAP:
						return ((BitmapImageResource) abstractImageResource).getImage();
					case ImageResourceSort._FILE:
						return null;
					case ImageResourceSort._SCHEME:
						return ((SchemeImageResource) abstractImageResource).getImage();
				}
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final AbstractImageResource abstractImageResource, final String key, final Object value) {
		if (abstractImageResource != null) {
			if (key.equals(COLUMN_CODENAME)) {
				switch (abstractImageResource.getSort().value()) {
					case ImageResourceSort._BITMAP:
					/*
					 * Fall-through.
					 */
					case ImageResourceSort._FILE:
						((AbstractBitmapImageResource) abstractImageResource).setCodename((String) value);
						break;
				}
			}
			if (key.equals(COLUMN_DATA)) {
				switch (abstractImageResource.getSort().value()) {
					case ImageResourceSort._BITMAP:
						((BitmapImageResource) abstractImageResource).setImage((byte[]) value);
						break;
					case ImageResourceSort._SCHEME:
						((SchemeImageResource) abstractImageResource).setImage((byte[]) value);
						break;
				}
			}
		}
	}

}
