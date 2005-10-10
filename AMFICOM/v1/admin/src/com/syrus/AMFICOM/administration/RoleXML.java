/*-
* $Id: RoleXML.java,v 1.1 2005/10/10 15:41:59 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
/**
 * @version $Revision: 1.1 $, $Date: 2005/10/10 15:41:59 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class RoleXML extends AbstractStorableObjectXML<Role> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.ROLE_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_CODENAME,
					COLUMN_DESCRIPTION};
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@Override
	public Role getStorableObject(final Map<String, Object> objectMap) {
		Role role = 
			new Role(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getString(objectMap, COLUMN_CODENAME),
				this.getString(objectMap, COLUMN_DESCRIPTION));
		return role;
	}
}

