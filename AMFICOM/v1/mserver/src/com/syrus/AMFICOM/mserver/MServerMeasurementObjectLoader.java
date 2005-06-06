/*
 * $Id: MServerMeasurementObjectLoader.java,v 1.32 2005/06/06 14:41:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.32 $, $Date: 2005/06/06 14:41:10 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public final class MServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {

	/**
	 * @todo make access validation on MCM
	 */
	public Set loadMeasurements(final Set ids) throws ApplicationException {
		return MServerObjectLoader.loadStorableObjects(ObjectEntities.MEASUREMENT_ENTITY_CODE,
				ids,
				new MServerObjectLoader.TransmitProcedure() {
					public final IDLEntity[] transmitStorableObjects(final MCM mcmRef,
							final Identifier_Transferable loadIdsT[],
							final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitMeasurements(loadIdsT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	public Set loadAnalyses(final Set ids) throws ApplicationException {
		return MServerObjectLoader.loadStorableObjects(ObjectEntities.ANALYSIS_ENTITY_CODE,
				ids,
				new MServerObjectLoader.TransmitProcedure() {
					public final IDLEntity[] transmitStorableObjects(final MCM mcmRef,
							final Identifier_Transferable loadIdsT[],
							final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitAnalyses(loadIdsT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	public Set loadEvaluations(final Set ids) throws ApplicationException {
		return MServerObjectLoader.loadStorableObjects(ObjectEntities.ANALYSIS_ENTITY_CODE,
				ids,
				new MServerObjectLoader.TransmitProcedure() {
					public final IDLEntity[] transmitStorableObjects(final MCM mcmRef,
							final Identifier_Transferable loadIdsT[],
							final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitEvaluations(loadIdsT);
					}
				});
	}



	/**
	 * @todo make access validation on MCM
	 */
	public Set loadMeasurementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return MServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENT_ENTITY_CODE,
				ids,
				condition,
				new MServerObjectLoader.TransmitButIdsByConditionProcedure() {
					public final IDLEntity[] transmitStorableObjectsButIdsByCondition(final MCM mcmRef,
							final Identifier_Transferable loadButIdsT[],
							final StorableObjectCondition_Transferable conditionT,
							final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitMeasurementsButIdsByCondition(loadButIdsT, conditionT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	public Set loadAnalysesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return MServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSIS_ENTITY_CODE,
				ids,
				condition,
				new MServerObjectLoader.TransmitButIdsByConditionProcedure() {
					public final IDLEntity[] transmitStorableObjectsButIdsByCondition(final MCM mcmRef,
							final Identifier_Transferable loadButIdsT[],
							final StorableObjectCondition_Transferable conditionT,
							final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitAnalysesButIdsByCondition(loadButIdsT, conditionT);
					}
				});
	}

	/**
	 * @todo make access validation on MCM
	 */
	public Set loadEvaluationsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return MServerObjectLoader.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATION_ENTITY_CODE,
				ids,
				condition,
				new MServerObjectLoader.TransmitButIdsByConditionProcedure() {
					public final IDLEntity[] transmitStorableObjectsButIdsByCondition(final MCM mcmRef,
							final Identifier_Transferable loadButIdsT[],
							final StorableObjectCondition_Transferable conditionT,
							final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
						return mcmRef.transmitEvaluationsButIdsByCondition(loadButIdsT, conditionT);
					}
				});
	}



	public void delete(Set identifiables) {

		if (identifiables == null || identifiables.isEmpty()) {
			return;
		}

		Set nonTestIdentifiers = null;
		Set testIdentifiers = null;
		for (Iterator it = nonTestIdentifiers.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.TEST_ENTITY_CODE) {
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
			Set nonProcessingIdentifiers = null;
			Map mcmIdTestIdsMap = new HashMap();
			for (Iterator iterator = testIdentifiers.iterator(); iterator.hasNext();) {
				Identifier testId = (Identifier) iterator.next();
				try {
					final Test test = (Test) StorableObjectPool.getStorableObject(testId, true);
					final TestStatus status = test.getStatus();
					switch (status.value()) {
						case TestStatus._TEST_STATUS_NEW:
						case TestStatus._TEST_STATUS_SCHEDULED:
							if (nonProcessingIdentifiers == null) {
								nonProcessingIdentifiers = new HashSet();
							}
							nonProcessingIdentifiers.add(testId);
							super.delete(Collections.singleton(testId));
							break;
						default:
							test.setStatus(TestStatus.TEST_STATUS_ABORTED);
							break;
					}

					final Identifier mcmId = test.getMCMId();
					java.util.Set mcmIdTestIds = (java.util.Set) mcmIdTestIdsMap.get(mcmId);
					if (mcmIdTestIds == null) {
						mcmIdTestIds = new HashSet();
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

			MServerSessionEnvironment sessionEnvironment = MServerSessionEnvironment.getInstance();
			for (final Iterator it = mcmIdTestIdsMap.keySet().iterator(); it.hasNext();) {
				final Identifier mcmId = (Identifier) it.next();
				final Identifier_Transferable[] idsT = Identifier.createTransferables((Collection) mcmIdTestIdsMap.get(mcmId));
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
