/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.10 2005/06/22 19:29:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/22 19:29:31 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseGeneralObjectLoader extends DatabaseObjectLoader implements GeneralObjectLoader {

	/* Load multiple objects*/

	public Set loadParameterTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCharacteristicTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCharacteristics(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadParameterTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCharacteristicTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCharacteristicsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	/* Save multiple objects*/

	public void saveParameterTypes(Set<ParameterType> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCharacteristicTypes(Set<CharacteristicType> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCharacteristics(Set<Characteristic> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
