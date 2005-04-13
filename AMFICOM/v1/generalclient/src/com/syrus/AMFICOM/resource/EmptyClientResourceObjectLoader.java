/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.7 2005/04/13 20:20:12 arseniy Exp $
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/13 20:20:12 $
 * @author $Author: arseniy $
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
