/*
 * $Id: ImageResourceWrapper.java,v 1.1 2005/02/08 10:22:31 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/08 10:22:31 $
 * @author $Author: bob $
 * @module resource_v1
 */
public class ImageResourceWrapper implements StorableObjectWrapper {

	public static final String				COLUMN_SORT	= "sort";

	public static final String				COLUMN_DATA	= "data";

	protected static ImageResourceWrapper	instance;

	protected List							keys;

	private ImageResourceWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_SORT, COLUMN_CODENAME, COLUMN_DATA};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static ImageResourceWrapper getInstance() {
		if (instance == null)
			instance = new ImageResourceWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}

	public Class getPropertyClass(String key) {
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Object getValue(Object object, String key) {
		if (object instanceof AbstractImageResource) {
			AbstractImageResource abstractImageResource = (AbstractImageResource) object;
			if (key.equals(COLUMN_SORT))
				return new Integer(abstractImageResource.getSort().value());
			if (key.equals(COLUMN_CODENAME)) {
				switch (abstractImageResource.getSort().value()) {
					case ImageResourceSort._BITMAP:
						return ((BitmapImageResource) abstractImageResource).getCodename();
					case ImageResourceSort._FILE:
						return ((FileImageResource) abstractImageResource).getCodename();
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
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof AbstractImageResource) {
			AbstractImageResource abstractImageResource = (AbstractImageResource) object;
			if (key.equals(COLUMN_CODENAME)) {
				switch (abstractImageResource.getSort().value()) {
					case ImageResourceSort._BITMAP:
						((BitmapImageResource) abstractImageResource).setCodename((String) value);
					case ImageResourceSort._FILE:
						((FileImageResource) abstractImageResource).setCodename((String) value);
					default:
						break;
				}
			}
			if (key.equals(COLUMN_DATA)) {
				switch (abstractImageResource.getSort().value()) {
					case ImageResourceSort._BITMAP:
						((BitmapImageResource) abstractImageResource).setImage((byte[]) value);
					case ImageResourceSort._FILE:
						break;
					case ImageResourceSort._SCHEME:
						((SchemeImageResource) abstractImageResource).setImage((byte[]) value);
				}
			}
		}
	}

}
