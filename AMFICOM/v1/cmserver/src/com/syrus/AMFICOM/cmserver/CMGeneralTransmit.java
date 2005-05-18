/*
 * $Id: CMGeneralTransmit.java,v 1.23 2005/05/18 13:11:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.23 $, $Date: 2005/05/18 13:11:21 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMGeneralTransmit extends CMMeasurementReceive {

	private static final long serialVersionUID = 6832454869836527727L;



	/* Transmit multiple objects*/

	public ParameterType_Transferable[] transmitParameterTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		ParameterType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (ParameterType) it.next();
			transferables[i] = (ParameterType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		CharacteristicType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CharacteristicType) it.next();
			transferables[i] = (CharacteristicType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Characteristic_Transferable[] transmitCharacteristics(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		Characteristic object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Characteristic) it.next();
			transferables[i] = (Characteristic_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private Set getObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			Set ids = Identifier.fromTransferables(idsT);
			Set objects = StorableObjectPool.getStorableObjects(ids, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}


	/* Transmit multiple objects but ids by condition*/

	public ParameterType_Transferable[] transmitParameterTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		ParameterType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (ParameterType) it.next();
			transferables[i] = (ParameterType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		CharacteristicType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CharacteristicType) it.next();
			transferables[i] = (CharacteristicType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		Characteristic object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Characteristic) it.next();
			transferables[i] = (Characteristic_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private Set getObjectsButIdsCondition(Identifier_Transferable[] idsT, StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		try {

			StorableObjectCondition condition = null;
			try {
				condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			}
			catch (IllegalDataException ide) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
						CompletionStatus.COMPLETED_NO,
						"Cannot restore condition -- " + ide.getMessage());
			}

			try {
				Set ids = Identifier.fromTransferables(idsT);
				Set objects = StorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
				return objects;
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
			}
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/*	Refresh*/

	public Identifier_Transferable[] transmitRefreshedGeneralObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Map storableObjectsTMap = new HashMap();
		for (int i = 0; i < storableObjectsT.length; i++)
			storableObjectsTMap.put(new Identifier(storableObjectsT[i].id), storableObjectsT[i]);

		try {
			StorableObjectPool.refresh();

			Set storableObjects = StorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject so = (StorableObject) it.next();
				final StorableObject_Transferable soT = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same versions.
				// Not only with older ones!
				if (!so.hasNewerVersion(soT.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
		}
	}

}
