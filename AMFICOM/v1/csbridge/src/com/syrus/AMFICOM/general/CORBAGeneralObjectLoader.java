/*-
 * $Id: CORBAGeneralObjectLoader.java,v 1.21 2005/06/14 11:46:28 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.21 $, $Date: 2005/06/14 11:46:28 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class CORBAGeneralObjectLoader extends CORBAObjectLoader implements GeneralObjectLoader {

	public CORBAGeneralObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadParameterTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitParameterTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristicTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristics(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitParameterTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristicTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristicsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveParameterTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveParameterTypes((ParameterType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCharacteristicTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCharacteristicTypes((CharacteristicType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCharacteristics(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCharacteristics((Characteristic_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
