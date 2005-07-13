/*
 * $Id: MServerMeasurementObjectLoader.java,v 1.42 2005/07/13 19:08:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.42 $, $Date: 2005/07/13 19:08:04 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

final class MServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {

	/**
	 * @todo make access validation on MCM
	 */
	@Override
	public Set loadMeasurements(final Set<Identifier> ids) throws ApplicationException {
		return CORBAMServerObjectLoader.loadStorableObjects(ObjectEntities.MEASUREMENT_CODE,
				ids,
				new CORBAMServerObjectLoader.TransmitProcedure() {
					public final IdlStorableObject[] transmitStorableObjects(final MCM mcmRef,
							final IdlIdentifier loadIdsT[],
							final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitMeasurements(loadIdsT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	@Override
	public Set loadAnalyses(final Set<Identifier> ids) throws ApplicationException {
		return CORBAMServerObjectLoader.loadStorableObjects(ObjectEntities.ANALYSIS_CODE,
				ids,
				new CORBAMServerObjectLoader.TransmitProcedure() {
					public final IdlStorableObject[] transmitStorableObjects(final MCM mcmRef,
							final IdlIdentifier loadIdsT[],
							final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitAnalyses(loadIdsT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	@Override
	public Set loadEvaluations(final Set<Identifier> ids) throws ApplicationException {
		return CORBAMServerObjectLoader.loadStorableObjects(ObjectEntities.ANALYSIS_CODE,
				ids,
				new CORBAMServerObjectLoader.TransmitProcedure() {
					public final IdlStorableObject[] transmitStorableObjects(final MCM mcmRef,
							final IdlIdentifier loadIdsT[],
							final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitEvaluations(loadIdsT);
					}
				});
	}



	/**
	 * @todo make access validation on MCM
	 */
	@Override
	public Set loadMeasurementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return CORBAMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENT_CODE,
				ids,
				condition,
				new CORBAMServerObjectLoader.TransmitButIdsByConditionProcedure() {
					public final IdlStorableObject[] transmitStorableObjectsButIdsByCondition(final MCM mcmRef,
							final IdlIdentifier loadButIdsT[],
							final IdlStorableObjectCondition conditionT,
							final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitMeasurementsButIdsByCondition(loadButIdsT, conditionT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	@Override
	public Set loadAnalysesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return CORBAMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSIS_CODE,
				ids,
				condition,
				new CORBAMServerObjectLoader.TransmitButIdsByConditionProcedure() {
					public final IdlStorableObject[] transmitStorableObjectsButIdsByCondition(final MCM mcmRef,
							final IdlIdentifier loadButIdsT[],
							final IdlStorableObjectCondition conditionT,
							final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitAnalysesButIdsByCondition(loadButIdsT, conditionT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	@Override
	public Set loadEvaluationsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return CORBAMServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATION_CODE,
				ids,
				condition,
				new CORBAMServerObjectLoader.TransmitButIdsByConditionProcedure() {
					public final IdlStorableObject[] transmitStorableObjectsButIdsByCondition(final MCM mcmRef,
							final IdlIdentifier loadButIdsT[],
							final IdlStorableObjectCondition conditionT,
							final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitEvaluationsButIdsByCondition(loadButIdsT, conditionT);
					}
				});
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
			Set<Identifier> nonProcessingIdentifiers = null;
			final Map<Identifier, Set<Identifier>> mcmIdTestIdsMap = new HashMap<Identifier, Set<Identifier>>();
			for (final Identifier testId : testIdentifiers) {
				try {
					final Test test = (Test) StorableObjectPool.getStorableObject(testId, true);
					final TestStatus status = test.getStatus();
					switch (status.value()) {
						case TestStatus._TEST_STATUS_NEW:
						case TestStatus._TEST_STATUS_SCHEDULED:
							if (nonProcessingIdentifiers == null) {
								nonProcessingIdentifiers = new HashSet<Identifier>();
							}
							nonProcessingIdentifiers.add(testId);
							super.delete(Collections.singleton(testId));
							break;
						default:
							test.setStatus(TestStatus.TEST_STATUS_ABORTED);
							break;
					}

					final Identifier mcmId = test.getMCMId();
					Set<Identifier> mcmIdTestIds = mcmIdTestIdsMap.get(mcmId);
					if (mcmIdTestIds == null) {
						mcmIdTestIds = new HashSet<Identifier>();
						mcmIdTestIdsMap.put(mcmId, mcmIdTestIds);
					}
					mcmIdTestIds.add(testId);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}

			if (nonProcessingIdentifiers != null) {
				super.delete(nonProcessingIdentifiers);
			}

			final MServerSessionEnvironment sessionEnvironment = MServerSessionEnvironment.getInstance();
			for (final Identifier mcmId : mcmIdTestIdsMap.keySet()) {
				final IdlIdentifier[] idsT = Identifier.createTransferables(mcmIdTestIdsMap.get(mcmId));
				final MCM mcmRef;
				try {
					mcmRef = sessionEnvironment.getMServerServantManager().getVerifiedMCMReference(mcmId);
					mcmRef.abortTests(idsT);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage(are.message);
				}
			}

		}

	}

}
