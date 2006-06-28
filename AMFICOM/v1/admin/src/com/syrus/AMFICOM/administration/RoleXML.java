/*-
* $Id: RoleXML.java,v 1.3 2006/04/10 16:56:18 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.RoleWrapper.LINK_COLUMN_SYSTEM_USER_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.Identifier;
/**
 * @version $Revision: 1.3 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class RoleXML extends AbstractStorableObjectXML<Role> {

	private static List<String> keys;

	@Override
	public short getEntityCode() {
		return ROLE_CODE;
	}

	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}

	@Override
	public Role getStorableObject(final Map<String, Object> objectMap) {
		final Role role = new Role(this.getIdentifier(objectMap, COLUMN_ID),
				this.getIdentifier(objectMap, COLUMN_CREATOR_ID),
				this.getVersion(objectMap, COLUMN_VERSION),
				this.getString(objectMap, COLUMN_CODENAME),
				this.getString(objectMap, COLUMN_DESCRIPTION),
				Collections.<Identifier> emptySet());
		role.setSystemUserIds0((Set<Identifier>) objectMap.get(LINK_COLUMN_SYSTEM_USER_ID));
		return role;
	}
}

