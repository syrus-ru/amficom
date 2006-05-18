/*-
* $Id: PermissionAttributesXML.java,v 1.9 2006/02/28 15:19:58 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_DENY_MASK;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_MODULE;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_PARENT_ID;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_PERMISSION_MASK;
import static com.syrus.AMFICOM.general.ObjectEntities.PERMATTR_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.AbstractStorableObjectXML;

/**
 * @version $Revision: 1.9 $, $Date: 2006/02/28 15:19:58 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class PermissionAttributesXML extends AbstractStorableObjectXML<PermissionAttributes> {

	private static List<String> keys;

	@Override
	public short getEntityCode() {
		return PERMATTR_CODE;
	}

	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] { COLUMN_DOMAIN_ID,
					COLUMN_PARENT_ID,
					COLUMN_MODULE,
					COLUMN_PERMISSION_MASK,
					COLUMN_DENY_MASK };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}

	@Override
	public PermissionAttributes getStorableObject(final Map<String, Object> objectMap) {
		final PermissionAttributes permissionAttributes = new PermissionAttributes(this.getIdentifier(objectMap, COLUMN_ID),
				this.getIdentifier(objectMap, COLUMN_CREATOR_ID),
				this.getVersion(objectMap, COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_DOMAIN_ID),
				this.getIdentifier(objectMap, COLUMN_PARENT_ID),
				Module.valueOf(this.getInteger(objectMap, COLUMN_MODULE)),
				this.getBigInteger(objectMap, COLUMN_PERMISSION_MASK),
				this.getBigInteger(objectMap, COLUMN_DENY_MASK));
		return permissionAttributes;
	}
}

