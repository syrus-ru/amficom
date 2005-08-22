/*-
* $Id: LayoutItemXML.java,v 1.1 2005/08/22 12:10:45 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

import static com.syrus.AMFICOM.resource.LayoutItemWrapper.*;
/**
 * @version $Revision: 1.1 $, $Date: 2005/08/22 12:10:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module resource
 */
public class LayoutItemXML extends AbstractStorableObjectXML<LayoutItem> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.LAYOUT_ITEM_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {
					COLUMN_PARENT_ID, 
					COLUMN_LAYOUT_NAME, 
					COLUMN_NAME };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@Override
	public LayoutItem getStorableObject(final Map<String, Object> objectMap) {
		LayoutItem layoutItem = 
			new LayoutItem(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_PARENT_ID),
				this.getString(objectMap, COLUMN_LAYOUT_NAME),
				this.getString(objectMap, COLUMN_NAME));
		return layoutItem;
	}
	
}

