/*
 * $Id: NewIdentifierPool.java,v 1.2 2004/07/21 13:33:11 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.util.Log;

/**
 * @version $ $
 * @author $Author: arseniy $
 * @module general_v1
 */
 
public class NewIdentifierPool {
	public static final int DEFAULT_ENTITY_POOL_SIZE = 10;
	public static final int MAX_ENTITY_POOL_SIZE = 10;

	private static Map entityIdentifierPools;
	private static IdentifierGeneratorServer igServer;
	
	private NewIdentifierPool() {
		// nothing
	}
	
	public static void init(IdentifierGeneratorServer igServer1) {
		igServer = igServer1;

		entityIdentifierPools = new Hashtable(100);

		createEntityIdentifierPool(ObjectEntities.SET_ENTITY);
		createEntityIdentifierPool(ObjectEntities.SETPARAMETER_ENTITY);
		createEntityIdentifierPool(ObjectEntities.MS_ENTITY);
		createEntityIdentifierPool(ObjectEntities.MEASUREMENT_ENTITY);
		createEntityIdentifierPool(ObjectEntities.ANALYSIS_ENTITY);
		createEntityIdentifierPool(ObjectEntities.EVALUATION_ENTITY);
		createEntityIdentifierPool(ObjectEntities.TEST_ENTITY);
		createEntityIdentifierPool(ObjectEntities.RESULT_ENTITY);
		createEntityIdentifierPool(ObjectEntities.RESULTPARAMETER_ENTITY);
		createEntityIdentifierPool(ObjectEntities.TEMPORALPATTERN_ENTITY);
	}
	
	public static Identifier getGeneratedIdentifier(String entity, int preferredRangeSize) throws IllegalObjectEntityException, AMFICOMRemoteException {
		List entityIdPool = (List)entityIdentifierPools.get(entity);
		if (entityIdPool != null) {
			if (entityIdPool.isEmpty())
				updateIdentifierPool(entity, preferredRangeSize);

			Identifier identifier = (Identifier)entityIdPool.remove(0);
			return identifier;
		}
		else
			throw new IllegalObjectEntityException("Identifier pool for entity '" + entity + "' not found", IllegalObjectEntityException.NULL_ENTITY_CODE);
	}
		
	public static void updateIdentifierPool(String entity, int size) throws IllegalObjectEntityException, AMFICOMRemoteException {
		List entityIdPool = (List)entityIdentifierPools.get(entity);
		if (entityIdPool != null) {
			if (size <= 1) {
				Identifier identifier = new Identifier(igServer.getGeneratedIdentifier(entity));
				entityIdPool.add(identifier);
			}
			else {
				Identifier_Transferable[] idst = igServer.getGeneratedIdentifierRange(entity, (size < MAX_ENTITY_POOL_SIZE)?size:MAX_ENTITY_POOL_SIZE);
				for (int i = 0; i < idst.length; i++)
					entityIdPool.add(new Identifier(idst[i]));
			}
		}
		else
			throw new IllegalObjectEntityException("Identifier pool for entity '" + entity + "' not found", IllegalObjectEntityException.NULL_ENTITY_CODE);
	}
	
	public static int getIdentifierPoolSize(String entity) throws IllegalObjectEntityException {
		List entityIdPool = (List)entityIdentifierPools.get(entity);
		if (entityIdPool != null) {
			return entityIdPool.size();
		}
		else
			throw new IllegalObjectEntityException("Identifier pool for entity '" + entity + "' not found", IllegalObjectEntityException.NULL_ENTITY_CODE);
	}
	
	private static void createEntityIdentifierPool(String entity) {
		if (! entityIdentifierPools.containsKey(entity)) {
			entityIdentifierPools.put(entity, new ArrayList(DEFAULT_ENTITY_POOL_SIZE));
		}
		else
			Log.errorMessage("IdentifierPool | Pool of identifiers for entity: " + entity + " already created");
	}
}
