/*-
* $Id: MCMXML.java,v 1.2 2005/09/14 19:01:23 arseniy Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.administration.MCMWrapper.COLUMN_HOSTNAME;
import static com.syrus.AMFICOM.administration.MCMWrapper.COLUMN_SERVER_ID;
import static com.syrus.AMFICOM.administration.MCMWrapper.COLUMN_USER_ID;
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
 * @version $Revision: 1.2 $, $Date: 2005/09/14 19:01:23 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class MCMXML extends AbstractStorableObjectXML<MCM> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.MCM_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_NAME, 
					COLUMN_DESCRIPTION, 
					COLUMN_USER_ID, 
					COLUMN_SERVER_ID, 
					COLUMN_DOMAIN_ID,
					COLUMN_HOSTNAME  };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@Override
	public MCM getStorableObject(final Map<String, Object> objectMap) {
		MCM mcm = 
			new MCM(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_DOMAIN_ID),
				this.getString(objectMap, COLUMN_NAME),
				this.getString(objectMap, COLUMN_DESCRIPTION),
				this.getString(objectMap, COLUMN_HOSTNAME),
				this.getIdentifier(objectMap, COLUMN_USER_ID),
				this.getIdentifier(objectMap, COLUMN_SERVER_ID));
		return mcm;
	}
	
}

