/*-
* $Id: DeliveryAttributesXML.java,v 1.1 2005/11/10 13:20:34 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.LINKED_COLUMN_ROLE_IDS;
import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.LINKED_COLUMN_SYSTEM_USER_IDS;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
/**
 * @version $Revision: 1.1 $, $Date: 2005/11/10 13:20:34 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module event
 */
public class DeliveryAttributesXML extends AbstractStorableObjectXML<DeliveryAttributes> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.DELIVERYATTRIBUTES_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_SEVERITY, 
					LINKED_COLUMN_SYSTEM_USER_IDS, 
					LINKED_COLUMN_ROLE_IDS};
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DeliveryAttributes getStorableObject(final Map<String, Object> objectMap) {
		DeliveryAttributes deliveryAttributes = 
			new DeliveryAttributes(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				new Date(),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				Severity.valueOf(this.getInteger(objectMap, COLUMN_SEVERITY).intValue()));
		deliveryAttributes.setRoleIds0((Set<Identifier>) objectMap.get(LINKED_COLUMN_ROLE_IDS));
		deliveryAttributes.setSystemUserIds0((Set<Identifier>) objectMap.get(LINKED_COLUMN_SYSTEM_USER_IDS));
		return deliveryAttributes;
	}
}

