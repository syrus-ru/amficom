/*-
 * $Id: CORBAGeneralObjectLoader.java,v 1.25 2005/06/22 19:29:31 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristic;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.25 $, $Date: 2005/06/22 19:29:31 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class CORBAGeneralObjectLoader extends CORBAObjectLoader implements GeneralObjectLoader {

	public CORBAGeneralObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadParameterTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PARAMETER_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitParameterTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CHARACTERISTIC_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristicTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristics(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CHARACTERISTIC_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristics(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadParameterTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PARAMETER_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitParameterTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CHARACTERISTIC_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristicTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CHARACTERISTIC_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristicsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveParameterTypes(final Set<ParameterType> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PARAMETER_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveParameterTypes((IdlParameterType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCharacteristicTypes(final Set<CharacteristicType> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CHARACTERISTIC_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCharacteristicTypes((IdlCharacteristicType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCharacteristics(final Set<Characteristic> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CHARACTERISTIC_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCharacteristics((IdlCharacteristic[]) transferables, force, sessionKey);
			}
		});
	}
}
