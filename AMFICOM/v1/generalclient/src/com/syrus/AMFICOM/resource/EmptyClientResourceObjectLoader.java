/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.5 2005/02/25 07:07:47 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/25 07:07:47 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class EmptyClientResourceObjectLoader implements ResourceObjectLoader {

	public StorableObject loadImageResource(Identifier id) throws ApplicationException {
		return null;
	}

	public Collection loadImageResources(Collection ids) throws ApplicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadImageResourcesButIds(	StorableObjectCondition condition,
												Collection ids) throws ApplicationException {
		return Collections.EMPTY_LIST;
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public void saveImageResource(	AbstractImageResource abstractImageResource,
									boolean force) throws ApplicationException {
	}

	public void saveImageResources(	Collection list,
									boolean force) throws ApplicationException {
	}

	public void delete(Identifier id) throws IllegalDataException {
	}

	public void delete(Collection ids) throws IllegalDataException {
	}

}
