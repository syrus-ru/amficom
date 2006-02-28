/*-
* $Id: LayoutItemXML.java,v 1.2 2006/02/28 15:19:58 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.resource.LayoutItemWrapper.COLUMN_LAYOUT_NAME;
import static com.syrus.AMFICOM.resource.LayoutItemWrapper.COLUMN_PARENT_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
/**
 * @version $Revision: 1.2 $, $Date: 2006/02/28 15:19:58 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module resource
 */
public class LayoutItemXML extends AbstractStorableObjectXML<LayoutItem> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return LAYOUT_ITEM_CODE;
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
			new LayoutItem(this.getIdentifier(objectMap, COLUMN_ID),
				this.getIdentifier(objectMap, COLUMN_CREATOR_ID),
				this.getVersion(objectMap, COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_PARENT_ID),
				this.getString(objectMap, COLUMN_LAYOUT_NAME),
				this.getString(objectMap, COLUMN_NAME));
		return layoutItem;
	}
	
}

