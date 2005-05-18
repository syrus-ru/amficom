/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.8 2005/05/18 14:01:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.8 $, $Date: 2005/05/18 14:01:19 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class EmptyClientResourceObjectLoader implements ResourceObjectLoader {

	public StorableObject loadImageResource(Identifier id) throws ApplicationException {
		return null;
	}

	public Set loadImageResources(Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadImageResourcesButIds(StorableObjectCondition condition,
										Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public void saveImageResource(	AbstractImageResource abstractImageResource,
									boolean force) throws ApplicationException {
		// empty
	}

	public void saveImageResources(	Set set,
									boolean force) throws ApplicationException {
		// empty
	}

	public void delete(Identifier id) {
		// empty
	}

	public void delete(Set ids) {
		// empty
	}

}
