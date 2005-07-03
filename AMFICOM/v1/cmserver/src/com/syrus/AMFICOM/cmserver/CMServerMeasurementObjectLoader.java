/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.53 2005/06/21 12:44:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitButIdsByConditionProcedure;
import com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitProcedure;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.53 $, $Date: 2005/06/21 12:44:32 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public final class CMServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	
	private Map  lastRefesh;
	
	/**
	 * refresh timeout
	 */
	private long refreshTimeout;

	private CORBACMServerObjectLoader corbaCMServerObjectLoader;
	
	public CMServerMeasurementObjectLoader(long refreshTimeout, final CMServerServantManager cmServerServantManager) {
		this.refreshTimeout = refreshTimeout;
		this.lastRefesh = new HashMap();

		this.corbaCMServerObjectLoader = new CORBACMServerObjectLoader(cmServerServantManager);
	}



	/* Load multiple objects*/

	public final Set loadMeasurements(final Set ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjects(ObjectEntities.MEASUREMENT_CODE, ids, new TransmitProcedure() {
			public final IDLEntity[] transmitStorableObjects(final CommonServer commonServer,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
				return ((MServer) commonServer).transmitMeasurements(idsT, sessionKeyT);
			}
		});
	}

	public Set loadAnalyses(Set ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjects(ObjectEntities.ANALYSIS_CODE, ids, new TransmitProcedure() {
			public final IDLEntity[] transmitStorableObjects(final CommonServer commonServer,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
				return ((MServer) commonServer).transmitAnalyses(idsT, sessionKeyT);
			}
		});
	}

	public Set loadEvaluations(Set ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjects(ObjectEntities.EVALUATION_CODE, ids, new TransmitProcedure() {
			public final IDLEntity[] transmitStorableObjects(final CommonServer commonServer,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
				return ((MServer) commonServer).transmitEvaluations(idsT, sessionKeyT);
			}
		});
	}





	public Set loadMeasurementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENT_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public final IDLEntity[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKeyT,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) commonServer).transmitMeasurementsButIdsByCondition(idsT, conditionT, sessionKeyT);
					}
				});
	}

	public Set loadAnalysesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSIS_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public final IDLEntity[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKeyT,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) commonServer).transmitAnalysesButIdsByCondition(idsT, conditionT, sessionKeyT);
					}
				});
	}

	public Set loadEvaluationsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATION_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public final IDLEntity[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKeyT,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) commonServer).transmitEvaluationsButIdsByCondition(idsT, conditionT, sessionKeyT);
					}
				});
	}



	/*	Refresh*/

	public Set refresh(Set storableObjects) throws DatabaseException {
		/* refresh no often than one in refreshTimeout ms */
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		StorableObject firstStorableObject = (StorableObject) storableObjects.iterator().next();
		Short entityCode = new Short(firstStorableObject.getId().getMajor());
		
		Date lastRefreshDate = (Date) this.lastRefesh.get(entityCode);
		
		if (lastRefreshDate != null && System.currentTimeMillis() - lastRefreshDate.getTime() < this.refreshTimeout)
			return Collections.EMPTY_SET;

		/* put current date*/
		this.lastRefesh.put(entityCode, new Date());

		try {
			StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode.shortValue());
			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}

	}

	public void delete(Set identifiables) {
		if (identifiables == null || identifiables.isEmpty()) {
			return;
		}

		Set nonTestIdentifiers = null;
		Set testIdentifiers = null;
		for (final Iterator it = nonTestIdentifiers.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.TEST_CODE) {
				if (testIdentifiers == null) {
					testIdentifiers = new HashSet();
				}
				testIdentifiers.add(id);
			}
			else {
				if (nonTestIdentifiers == null) {
					nonTestIdentifiers = new HashSet();
				}
				nonTestIdentifiers.add(id);
			}
		}

		if (nonTestIdentifiers != null) {
			super.delete(nonTestIdentifiers);
		}

		if (testIdentifiers != null) {
			try {
				MServer mServerRef = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getMServerReference();
				mServerRef.deleteTests(Identifier.createTransferables(testIdentifiers), LoginManager.getSessionKeyTransferable());
			}
			catch (CommunicationException ce) {
				Log.errorException(ce);
			}
			catch (AMFICOMRemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
