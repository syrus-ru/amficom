/*
 * $Id: CORBACMServerObjectLoader.java,v 1.1 2005/06/07 13:56:59 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/07 13:56:59 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
final class CORBACMServerObjectLoader extends CORBAObjectLoader {

	protected CORBACMServerObjectLoader(final CMServerServantManager cmServerServantManager) {
		super(cmServerServantManager);
	}

	protected final Set loadStorableObjects(final short entityCode, final Set ids, final TransmitProcedure transmitProcedure)
			throws ApplicationException {
		final Set objects = DatabaseObjectLoader.loadStorableObjects(ids);

		final Set loadIds = createLoadIds(ids, objects);
		if (loadIds.isEmpty())
			return objects;

		final Set loadedObjects = super.loadStorableObjects(entityCode, loadIds, transmitProcedure);

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			try {
				final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	protected final Set loadStorableObjectsButIdsByCondition(final short entityCode,
			final Set ids,
			final StorableObjectCondition condition,
			final TransmitButIdsByConditionProcedure transmitButIdsConditionProcedure) throws ApplicationException {
		final Set objects = DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);

		final Set loadButIds = createLoadButIds(ids, objects);
		final Set loadedObjects = super.loadStorableObjectsButIdsByCondition(entityCode,
				loadButIds,
				condition,
				transmitButIdsConditionProcedure);

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			try {
				final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

}
