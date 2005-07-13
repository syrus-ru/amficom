/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.56 2005/07/13 19:35:43 arseniy Exp $
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
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifiable;
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
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.56 $, $Date: 2005/07/13 19:35:43 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public final class CMServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	
	private Map<Short, Date>  lastRefesh;
	
	/**
	 * refresh timeout
	 */
	private long refreshTimeout;

	private CORBACMServerObjectLoader corbaCMServerObjectLoader;
	
	public CMServerMeasurementObjectLoader(long refreshTimeout, final CMServerServantManager cmServerServantManager) {
		this.refreshTimeout = refreshTimeout;
		this.lastRefesh = new HashMap<Short, Date>();

		this.corbaCMServerObjectLoader = new CORBACMServerObjectLoader(cmServerServantManager);
	}



	/* Load multiple objects*/

	@Override
	public final Set loadMeasurements(final Set<Identifier> ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjects(ObjectEntities.MEASUREMENT_CODE, ids, new TransmitProcedure() {
			public final IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
				return ((MServer) commonServer).transmitMeasurements(idsT, sessionKeyT);
			}
		});
	}

	@Override
	public Set loadAnalyses(final Set<Identifier> ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjects(ObjectEntities.ANALYSIS_CODE, ids, new TransmitProcedure() {
			public final IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
				return ((MServer) commonServer).transmitAnalyses(idsT, sessionKeyT);
			}
		});
	}

	@Override
	public Set loadEvaluations(final Set<Identifier> ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjects(ObjectEntities.EVALUATION_CODE, ids, new TransmitProcedure() {
			public final IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
				return ((MServer) commonServer).transmitEvaluations(idsT, sessionKeyT);
			}
		});
	}





	@Override
	public Set loadMeasurementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENT_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public final IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKeyT,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) commonServer).transmitMeasurementsButIdsByCondition(idsT, conditionT, sessionKeyT);
					}
				});
	}

	@Override
	public Set loadAnalysesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSIS_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public final IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKeyT,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) commonServer).transmitAnalysesButIdsByCondition(idsT, conditionT, sessionKeyT);
					}
				});
	}

	@Override
	public Set loadEvaluationsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return this.corbaCMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATION_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public final IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKeyT,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) commonServer).transmitEvaluationsButIdsByCondition(idsT, conditionT, sessionKeyT);
					}
				});
	}



	/*	Refresh*/

	@Override
	public Set refresh(final Set<? extends StorableObject> storableObjects) throws DatabaseException {
		/* refresh no often than one in refreshTimeout ms */
		if (storableObjects.isEmpty())
			return Collections.emptySet();

		final StorableObject firstStorableObject = storableObjects.iterator().next();
		final Short entityCode = new Short(firstStorableObject.getId().getMajor());

		final Date lastRefreshDate = this.lastRefesh.get(entityCode);

		if (lastRefreshDate != null && System.currentTimeMillis() - lastRefreshDate.getTime() < this.refreshTimeout)
			return Collections.emptySet();

		/* put current date*/
		this.lastRefesh.put(entityCode, new Date());

		try {
			final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode.shortValue());
			if (database != null)
				return database.refresh(storableObjects);

			return Collections.emptySet();
		}
		catch (DatabaseException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}

	}

	@Override
	public void delete(final Set<? extends Identifiable> identifiables) {
		if (identifiables == null || identifiables.isEmpty()) {
			return;
		}

		Set<Identifier> nonTestIdentifiers = null;
		Set<Identifier> testIdentifiers = null;
		for (final Identifiable identifiable : identifiables) {
			final Identifier id = identifiable.getId();
			if (id.getMajor() == ObjectEntities.TEST_CODE) {
				if (testIdentifiers == null) {
					testIdentifiers = new HashSet<Identifier>();
				}
				testIdentifiers.add(id);
			}
			else {
				if (nonTestIdentifiers == null) {
					nonTestIdentifiers = new HashSet<Identifier>();
				}
				nonTestIdentifiers.add(id);
			}
		}

		if (nonTestIdentifiers != null) {
			super.delete(nonTestIdentifiers);
		}

		if (testIdentifiers != null) {
			try {
				final MServer mServerRef = MServerHelper.narrow(CMServerSessionEnvironment.getInstance().getCMServerServantManager().getServerReference());
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
