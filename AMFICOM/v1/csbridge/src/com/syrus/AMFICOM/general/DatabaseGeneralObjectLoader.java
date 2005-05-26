/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.7 2005/05/26 19:13:24 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/26 19:13:24 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class DatabaseGeneralObjectLoader extends DatabaseObjectLoader implements GeneralObjectLoader {

	/* Load multiple objects*/

	public Set loadParameterTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	/* Save multiple objects*/

	public void saveParameterTypes(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveCharacteristics(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}
}
