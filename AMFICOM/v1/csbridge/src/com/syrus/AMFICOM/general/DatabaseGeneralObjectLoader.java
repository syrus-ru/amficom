/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.8 2005/06/03 15:23:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.8 $, $Date: 2005/06/03 15:23:58 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseGeneralObjectLoader extends DatabaseObjectLoader implements GeneralObjectLoader {

	/* Load multiple objects*/

	public Set loadParameterTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	/* Save multiple objects*/

	public void saveParameterTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCharacteristics(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
