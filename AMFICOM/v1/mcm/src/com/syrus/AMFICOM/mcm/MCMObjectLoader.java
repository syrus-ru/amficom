/*
 * $Id: MCMObjectLoader.java,v 1.14 2005/07/14 20:16:35 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/07/14 20:16:35 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
abstract class MCMObjectLoader extends CORBAObjectLoader {

	public MCMObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}

	



	@Override
	protected final Set loadStorableObjects(final short entityCode,
			final Set<Identifier> ids,
			final TransmitProcedure transmitProcedure) throws ApplicationException {
		final Set<StorableObject> objects = DatabaseObjectLoader.loadStorableObjects(ids);
		final Set<Identifier> loadIds = Identifier.createSubtractionIdentifiers(ids, objects);
		if (loadIds.isEmpty())
			return objects;

		final Set<StorableObject> loadedObjects = super.loadStorableObjects(entityCode, loadIds, transmitProcedure);
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

	@Override
	protected final Set loadStorableObjectsButIdsByCondition(final short entityCode,
			final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final TransmitButIdsByConditionProcedure transmitButIdsConditionProcedure) throws ApplicationException {
		final Set<StorableObject> objects = DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
		final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, objects);

		final Set<StorableObject> loadedObjects = super.loadStorableObjectsButIdsByCondition(entityCode,
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

	/**
	 * This method does <em>not</em> override
	 * {@link CORBAObjectLoader#saveStorableObjects(short, Set, ReceiveProcedure)}
	 * since it has an extra argument, <code>final boolean force</code>,
	 * unnecessary in super implementation.
	 */
	protected final void saveStorableObjects(final short entityCode,
			final Set<? extends StorableObject> storableObjects,
			final boolean force,
			final ReceiveProcedure receiveProcedure) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);

		super.saveStorableObjects(entityCode, storableObjects, receiveProcedure);
	}

	/**
	 * Currently not need to implement this method
	 * @todo Using this method load objects, changed on server relatively to MCM
	 */
	@Override
	public Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException {
		assert false : storableObjects;
		return null;
	}

	/**
	 * Currently not need to implement this method
	 */
	@Override
	public void delete(final Set<? extends Identifiable> identifiables) {
		assert false : identifiables;
	}
}
