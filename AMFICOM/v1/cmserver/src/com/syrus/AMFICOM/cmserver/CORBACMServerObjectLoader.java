/*
 * $Id: CORBACMServerObjectLoader.java,v 1.5 2005/07/13 19:10:01 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/07/13 19:10:01 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
final class CORBACMServerObjectLoader extends CORBAObjectLoader {

	protected CORBACMServerObjectLoader(final CMServerServantManager cmServerServantManager) {
		super(cmServerServantManager);
	}

	@Override
	protected final Set loadStorableObjects(final short entityCode, final Set<Identifier> ids, final TransmitProcedure transmitProcedure)
			throws ApplicationException {
		final Set objects = DatabaseObjectLoader.loadStorableObjects(ids);

		final Set<Identifier> loadIds = Identifier.createSubtractionIdentifiers(ids, objects);
		if (loadIds.isEmpty())
			return objects;

		Set loadedObjects = null;
		try {
			loadedObjects = super.loadStorableObjects(entityCode, loadIds, transmitProcedure);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
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

	@Override
	protected final Set loadStorableObjectsButIdsByCondition(final short entityCode,
			final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final TransmitButIdsByConditionProcedure transmitButIdsConditionProcedure) throws ApplicationException {
		final Set objects = DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);

		final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, objects);

		Set loadedObjects = null;
		try {
			loadedObjects = super.loadStorableObjectsButIdsByCondition(entityCode,
					loadButIds,
					condition,
					transmitButIdsConditionProcedure);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
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
