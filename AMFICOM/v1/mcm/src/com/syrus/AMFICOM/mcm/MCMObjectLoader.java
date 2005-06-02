/*
 * $Id: MCMObjectLoader.java,v 1.2 2005/06/02 14:44:03 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:44:03 $
 * @author $Author: bass $
 * @module mcm_v1
 */
abstract class MCMObjectLoader extends CORBAObjectLoader {
	//TODO Make all methods static in DatabaseObjectLoader and thus remove this field
	DatabaseObjectLoader databaseObjectLoader;

	public MCMObjectLoader(final MCMServantManager mcmServantManager, final DatabaseObjectLoader databaseObjectLoader) {
		super(mcmServantManager);
		this.databaseObjectLoader = databaseObjectLoader;
	}

	private final Set createLoadIds(final Set ids, final Set butIdentifiables) {
		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = butIdentifiables.iterator(); it.hasNext();) {
			id = ((Identifiable) it.next()).getId();
			loadIds.remove(id);
		}
		return loadIds;
	}

	private final Set createLoadButIds(final Set butIds, final Set alsoButIdentifiables) {
		Identifier id;
		Set loadButIds = new HashSet(butIds);
		for (Iterator it = alsoButIdentifiables.iterator(); it.hasNext();) {
			id = ((Identifiable) it.next()).getId();
			loadButIds.add(id);
		}
		return loadButIds;
	}

	protected Set loadStorableObjects(final Set ids, final short entityCode, final TransmitProcedure transmitProcedure)
			throws ApplicationException {
		final Set objects = this.databaseObjectLoader.loadStorableObjects(ids);
		final Set loadIds = this.createLoadIds(ids, objects);
		if (loadIds.isEmpty())
			return objects;

		final Set loadedObjects = super.loadStorableObjects(loadIds, entityCode, transmitProcedure);
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

	protected Set loadStorableObjectsButIdsCondition(final Set ids,
			final StorableObjectCondition condition,
			final short entityCode,
			final TransmitButIdsConditionProcedure transmitButIdsConditionProcedure) throws ApplicationException {
		final Set objects = this.databaseObjectLoader.loadStorableObjectsButIds(condition, ids);
		final Set loadButIds = this.createLoadButIds(ids, objects);

		final Set loadedObjects = super.loadStorableObjectsButIdsCondition(loadButIds,
				condition,
				entityCode,
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
	 * {@link CORBAObjectLoader#saveStorableObjects(Set, short, ReceiveProcedure)},
	 * since it has an extra argument, <code>final boolean force</code>,
	 * unnecessary in super implementation.
	 */
	protected void saveStorableObjects(final Set storableObjects,
			final short entityCode,
			final boolean force,
			final ReceiveProcedure receiveProcedure)
			throws ApplicationException {
		this.databaseObjectLoader.saveStorableObjects(storableObjects, force);

		super.saveStorableObjects(storableObjects, entityCode, receiveProcedure);
	}

	public void delete(Set identifiables) {
		this.databaseObjectLoader.delete(identifiables);
	}
}
