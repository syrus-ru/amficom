/*-
* $Id: SystemUserXML.java,v 1.2 2005/08/24 11:34:02 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_SORT;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
/**
 * @version $Revision: 1.2 $, $Date: 2005/08/24 11:34:02 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module admin
 */
public class SystemUserXML extends AbstractStorableObjectXML<SystemUser> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.SYSTEMUSER_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_DESCRIPTION, 
					COLUMN_LOGIN, 
					COLUMN_NAME, 
					COLUMN_SORT };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@SuppressWarnings("boxing")
	@Override
	public SystemUser getStorableObject(final Map<String, Object> objectMap) {
		SystemUser systemUser = 
			new SystemUser(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getString(objectMap, COLUMN_LOGIN),
				this.getInteger(objectMap, COLUMN_SORT),
				this.getString(objectMap, COLUMN_NAME),
				this.getString(objectMap, COLUMN_DESCRIPTION));
		return systemUser;
	}
}

