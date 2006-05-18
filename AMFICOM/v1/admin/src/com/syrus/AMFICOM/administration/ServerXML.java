/*-
* $Id: ServerXML.java,v 1.3 2006/02/28 15:19:58 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.administration.ServerWrapper.COLUMN_HOSTNAME;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.AbstractStorableObjectXML;
/**
 * @version $Revision: 1.3 $, $Date: 2006/02/28 15:19:58 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class ServerXML extends AbstractStorableObjectXML<Server> {

	private static List<String> keys;

	@Override
	public short getEntityCode() {
		return SERVER_CODE;
	}

	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_DOMAIN_ID, COLUMN_HOSTNAME };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}

	@Override
	public Server getStorableObject(final Map<String, Object> objectMap) {
		final Server server = new Server(this.getIdentifier(objectMap, COLUMN_ID),
				this.getIdentifier(objectMap, COLUMN_CREATOR_ID),
				this.getVersion(objectMap, COLUMN_VERSION),
				this.getIdentifier(objectMap, COLUMN_DOMAIN_ID),
				this.getString(objectMap, COLUMN_NAME),
				this.getString(objectMap, COLUMN_DESCRIPTION),
				this.getString(objectMap, COLUMN_HOSTNAME));
		return server;
	}

}

