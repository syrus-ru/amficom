/*-
* $Id: KISXML.java,v 1.2 2005/09/08 16:38:29 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_EQUIPMENT_ID;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_HOSTNAME;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_MCM_ID;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_TCP_PORT;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;


/**
 * @version $Revision: 1.2 $, $Date: 2005/09/08 16:38:29 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module measurement
 */
public class KISXML extends AbstractStorableObjectXML<KIS> {

private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.KIS_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_DESCRIPTION,
					COLUMN_NAME,
					COLUMN_DOMAIN_ID,
					COLUMN_EQUIPMENT_ID,
					COLUMN_MCM_ID,
					COLUMN_HOSTNAME,
					COLUMN_TCP_PORT};
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@Override
	public KIS getStorableObject(final Map<String, Object> objectMap) {
		KIS kis = 
			new KIS(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_DOMAIN_ID),
				this.getString(objectMap, COLUMN_NAME),
				this.getString(objectMap, COLUMN_DESCRIPTION),
				this.getString(objectMap, COLUMN_HOSTNAME),
				this.getShort(objectMap, COLUMN_TCP_PORT).shortValue(),
				this.getIdentifier(objectMap, COLUMN_EQUIPMENT_ID),
				this.getIdentifier(objectMap, COLUMN_MCM_ID));
		return kis;
	}
}

