/*
 * $Id: NewIdentifierPool.java,v 1.7 2004/11/22 12:41:26 bass Exp $
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
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2004/11/22 12:41:26 $
 * @author $Author: bass $
 * @module general_v1
 */
 
public class NewIdentifierPool {
	public static final int DEFAULT_ENTITY_POOL_SIZE = 10;
	public static final int MAX_ENTITY_POOL_SIZE = 50;

	private static Map entityIdentifierPools;	//Map <Short entityCode, List <Identifier> >
	private static IdentifierGeneratorServer igServer;
	
	private NewIdentifierPool() {
		// nothing
	}
	
	public static void init(IdentifierGeneratorServer igServer1) {
		igServer = igServer1;

		entityIdentifierPools = new Hashtable(100);

		createEntityIdentifierPool(ObjectEntities.SET_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.MS_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.ANALYSIS_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.EVALUATION_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.TEST_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.RESULT_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.RESULTPARAMETER_ENTITY_CODE);
		createEntityIdentifierPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE);
	}
	
	public static Identifier getGeneratedIdentifier(short entityCode, int preferredRangeSize) throws IllegalObjectEntityException, AMFICOMRemoteException {
		Short eCode = new Short(entityCode);
		List entityIdPool = (List)entityIdentifierPools.get(eCode);
		if (entityIdPool != null) {
			if (entityIdPool.isEmpty())
				updateIdentifierPool(eCode, preferredRangeSize);

			Identifier identifier = (Identifier)entityIdPool.remove(0);
			return identifier;
		}
		throw new IllegalObjectEntityException("Identifier pool for entity '" + ObjectEntities.codeToString(entityCode) + "' not found", IllegalObjectEntityException.NULL_ENTITY_CODE);
	}
		
	public static void updateIdentifierPool(short entityCode, int size) throws IllegalObjectEntityException, AMFICOMRemoteException {
		updateIdentifierPool(new Short(entityCode), size);
	}

	private static void updateIdentifierPool(Short eCode, int size) throws IllegalObjectEntityException, AMFICOMRemoteException {
		List entityIdPool = (List)entityIdentifierPools.get(eCode);
		if (entityIdPool != null) {
			try {
				if (size <= 1) {
					Identifier identifier = new Identifier(igServer.getGeneratedIdentifier(eCode.shortValue()));
					entityIdPool.add(identifier);
				}
				else {
					Identifier_Transferable[] idst = igServer.getGeneratedIdentifierRange(eCode.shortValue(), (size < MAX_ENTITY_POOL_SIZE)?size:MAX_ENTITY_POOL_SIZE);
					for (int i = 0; i < idst.length; i++)
						entityIdPool.add(new Identifier(idst[i]));
				}
			}
			catch (org.omg.CORBA.SystemException se) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_NO_CONNECT, CompletionStatus.COMPLETED_NO, "System exception -- " + se.getMessage());
			}
		}
		else
			throw new IllegalObjectEntityException("Identifier pool for entity '" + ObjectEntities.codeToString(eCode.shortValue()) + "' not found", IllegalObjectEntityException.NULL_ENTITY_CODE);
	}
	
	public static int getIdentifierPoolSize(short entityCode) throws IllegalObjectEntityException {
		Short eCode = new Short(entityCode);
		List entityIdPool = (List)entityIdentifierPools.get(eCode);
		if (entityIdPool != null) {
			return entityIdPool.size();
		}
		throw new IllegalObjectEntityException("Identifier pool for entity '" + ObjectEntities.codeToString(entityCode) + "' not found", IllegalObjectEntityException.NULL_ENTITY_CODE);
	}

	public static void setIdentifierGeneratorServer(IdentifierGeneratorServer igServer1) {
		igServer = igServer1;
	}
	
	private static void createEntityIdentifierPool(short entityCode) {
		Short eCode = new Short(entityCode);
		if (! entityIdentifierPools.containsKey(eCode)) {
			entityIdentifierPools.put(eCode, new ArrayList(DEFAULT_ENTITY_POOL_SIZE));
		}
		else
			Log.errorMessage("IdentifierPool | Pool of identifiers for entity: " + ObjectEntities.codeToString(entityCode) + " already created");
	}
}
