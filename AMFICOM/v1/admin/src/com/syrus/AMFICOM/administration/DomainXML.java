/*-
* $Id: DomainXML.java,v 1.1 2005/08/22 12:09:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.administration.DomainWrapper.*;
/**
 * @version $Revision: 1.1 $, $Date: 2005/08/22 12:09:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module admin
 */
public class DomainXML extends AbstractStorableObjectXML<Domain> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.DOMAIN_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_NAME, 
					COLUMN_DESCRIPTION, 
					COLUMN_DOMAIN_ID  };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@Override
	public Domain getStorableObject(final Map<String, Object> objectMap) {
		Domain domain = 
			new Domain(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_DOMAIN_ID),
				this.getString(objectMap, COLUMN_NAME),
				this.getString(objectMap, COLUMN_DESCRIPTION));
		return domain;
	}
	
}

