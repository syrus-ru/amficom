/*-
* $Id: PermissionAttributesXML.java,v 1.4 2005/09/09 15:03:06 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_PERMISSION_MASK;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_USER_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
/**
 * @version $Revision: 1.4 $, $Date: 2005/09/09 15:03:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module admin
 */
public class PermissionAttributesXML extends AbstractStorableObjectXML<PermissionAttributes> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.PERMATTR_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_DOMAIN_ID, 
					COLUMN_USER_ID, 
					COLUMN_PERMISSION_MASK };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@Override
	public PermissionAttributes getStorableObject(final Map<String, Object> objectMap) {
		PermissionAttributes permissionAttributes = 
			new PermissionAttributes(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_DOMAIN_ID),
				this.getIdentifier(objectMap, COLUMN_USER_ID),
				this.getBigInteger(objectMap, COLUMN_PERMISSION_MASK));
		return permissionAttributes;
	}
}

