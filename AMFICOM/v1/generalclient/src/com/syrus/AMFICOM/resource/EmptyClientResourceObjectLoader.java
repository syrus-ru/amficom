/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.6 2005/04/04 14:05:37 bob Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/04/04 14:05:37 $
 * @author $Author: bob $
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

	public void delete(Identifier id) throws IllegalDataException {
		// empty
	}

	public void delete(Set ids) throws IllegalDataException {
		// empty
	}

}
