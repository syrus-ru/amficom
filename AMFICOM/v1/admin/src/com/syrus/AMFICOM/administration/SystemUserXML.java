/*-
* $Id: SystemUserXML.java,v 1.6 2006/02/28 15:19:58 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_SORT;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.LINK_COLUMN_ROLE_IDS;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.Identifier;
/**
 * @version $Revision: 1.6 $, $Date: 2006/02/28 15:19:58 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class SystemUserXML extends AbstractStorableObjectXML<SystemUser> {

	private static List<String> keys;

	@Override
	public short getEntityCode() {
		return SYSTEMUSER_CODE;
	}

	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] { COLUMN_DESCRIPTION,
					COLUMN_LOGIN,
					COLUMN_NAME,
					COLUMN_SORT,
					LINK_COLUMN_ROLE_IDS };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SystemUser getStorableObject(final Map<String, Object> objectMap) {
		final SystemUser systemUser = new SystemUser(this.getIdentifier(objectMap, COLUMN_ID),
				this.getIdentifier(objectMap, COLUMN_CREATOR_ID),
				this.getVersion(objectMap, COLUMN_VERSION),
				this.getString(objectMap, COLUMN_LOGIN),
				this.getInteger(objectMap, COLUMN_SORT).intValue(),
				this.getString(objectMap, COLUMN_NAME),
				this.getString(objectMap, COLUMN_DESCRIPTION));
		systemUser.setRoleIds0((Set<Identifier>) objectMap.get(LINK_COLUMN_ROLE_IDS));
		return systemUser;
	}
}

