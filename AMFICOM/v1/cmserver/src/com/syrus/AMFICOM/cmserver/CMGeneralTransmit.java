/*
 * $Id: CMGeneralTransmit.java,v 1.4 2005/02/02 13:57:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/02 13:57:37 $
 * @author $Author: bob $
 * @module cmserver_v1
 */

public abstract class CMGeneralTransmit extends CMMeasurementReceive {

	private static final long	serialVersionUID	= 6832454869836527727L;

	public ParameterType_Transferable transmitParameterType(Identifier_Transferable identifier_Transferable,
															AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMGeneralTransmit.transmitParameterType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			ParameterType parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(id, true);
			return (ParameterType_Transferable) parameterType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public User_Transferable transmitUser(	Identifier_Transferable identifier_Transferable,
											AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMGeneralTransmit.transmitUser | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			User user = (User) GeneralStorableObjectPool.getStorableObject(id, true);
			return (User_Transferable) user.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public User_Transferable[] transmitUsers(	Identifier_Transferable[] ids_Transferable,
												AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitUsers | require "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE), true);

			User_Transferable[] transferables = new User_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				User user = (User) it.next();
				transferables[i] = (User_Transferable) user.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public User_Transferable[] transmitUsersButIdsCondition(Identifier_Transferable[] ids_Transferable,
															AccessIdentifier_Transferable accessIdentifier,
															StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitUsersButIdsCondition | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE), true);
			} else
				list = GeneralStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE), true);

			User_Transferable[] transferables = new User_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				User user = (User) it.next();
				transferables[i] = (User_Transferable) user.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable transmitMCM(Identifier_Transferable identifier_Transferable,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMGeneralTransmit.transmitMCM | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MCM mcm = (MCM) GeneralStorableObjectPool.getStorableObject(id, true);
			return (MCM_Transferable) mcm.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMs(	Identifier_Transferable[] ids_Transferable,
											AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitMCMs | require "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.MCM_ENTITY_CODE), true);

			MCM_Transferable[] transferables = new MCM_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MCM mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMsButIdsCondition(	Identifier_Transferable[] ids_Transferable,
															AccessIdentifier_Transferable accessIdentifier,
															StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitMCMsButIdsCondition | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.MCM_ENTITY_CODE), true);
			} else
				list = GeneralStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.MCM_ENTITY_CODE), true);

			MCM_Transferable[] transferables = new MCM_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MCM mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Characteristic_Transferable transmitCharacteristic(	Identifier_Transferable id_Transferable,
																AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristic | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Characteristic characteristic = (Characteristic) GeneralStorableObjectPool.getStorableObject(id, true);
			return (Characteristic_Transferable) characteristic.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CharacteristicType_Transferable transmitCharacteristicType(	Identifier_Transferable id_Transferable,
																		AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMGeneralTransmit.CharacteristicType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			CharacteristicType characteristicType = (CharacteristicType) GeneralStorableObjectPool.getStorableObject(
				id, true);
			return (CharacteristicType_Transferable) characteristicType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Characteristic_Transferable[] transmitCharacteristics(	Identifier_Transferable[] ids_Transferable,
																	AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitCharacteristics | require "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);

			Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Characteristic characteristic = (Characteristic) it.next();
				transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIds(	Identifier_Transferable[] ids_Transferable,
																		AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitCharacteristicsButIds | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);
			} else
				list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);

			Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Characteristic characteristic = (Characteristic) it.next();
				transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIdsCondition(Identifier_Transferable[] ids_Transferable,
																				AccessIdentifier_Transferable accessIdentifier,
																				StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitCharacteristicsButIdsCondition | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s) ",
				Log.DEBUGLEVEL07);
			List list;
			StorableObjectCondition condition = this.restoreCondition(condition_Transferable);
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(idsList, condition, true);
			} else
				list = GeneralStorableObjectPool.getStorableObjectsByCondition(condition, true);

			Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Characteristic characteristic = (Characteristic) it.next();
				transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(	Identifier_Transferable[] ids_Transferable,
																			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitCharacteristicTypes | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = GeneralStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = GeneralStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE), true);

			CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				CharacteristicType characteristicType = (CharacteristicType) it.next();
				transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypesButIds(	Identifier_Transferable[] ids_Transferable,
																				AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMGeneralTransmit.transmitCharacteristicTypes | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
				Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);
			} else
				list = GeneralStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE), true);

			CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				CharacteristicType characteristicType = (CharacteristicType) it.next();
				transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public ParameterType_Transferable[] transmitParameterTypes(	Identifier_Transferable[] identifier_Transferables,
																AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage(
				"CMGeneralTransmit.transmitParameterTypes | requiere "
						+ (identifier_Transferables.length == 0 ? "all" : Integer
								.toString(identifier_Transferables.length)) + " item(s)", Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

			ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				ParameterType parameterType = (ParameterType) it.next();
				transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public ParameterType_Transferable[] transmitParameterTypesButIds(	Identifier_Transferable[] identifier_Transferables,
																		AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage(
				"CMGeneralTransmit.transmitParameterTypesButIds | requiere "
						+ (identifier_Transferables.length == 0 ? "all" : Integer
								.toString(identifier_Transferables.length)) + " item(s)", Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

			ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				ParameterType parameterType = (ParameterType) it.next();
				transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public ParameterType_Transferable[] transmitParameterTypesButIdsCondition(	Identifier_Transferable[] identifier_Transferables,
																				AccessIdentifier_Transferable accessIdentifier,
																				StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMGeneralTransmit.transmitParameterTypesButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			StorableObjectCondition condition = this.restoreCondition(condition_Transferable);
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, condition, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);

			ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				ParameterType parameterType = (ParameterType) it.next();
				transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedGeneralObjects(	StorableObject_Transferable[] storableObjects_Transferables,
																		AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Map storableObjectMap = new HashMap();
			for (int i = 0; i < storableObjects_Transferables.length; i++) {
				storableObjectMap.put(new Identifier(storableObjects_Transferables[i].id),
					storableObjects_Transferables[i]);
			}
			GeneralStorableObjectPool.refresh();
			List storableObjects = GeneralStorableObjectPool.getStorableObjects(new ArrayList(storableObjectMap
					.keySet()), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject so = (StorableObject) it.next();
				StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectMap.get(so.getId());
				// Checking for confomity
				Identifier sotCreatorId = new Identifier(sot.creator_id);
				Identifier sotModifierId = new Identifier(sot.modifier_id);
				if ((Math.abs(sot.created - so.getCreated().getTime()) < 1000)
						&& (Math.abs(sot.modified - so.getModified().getTime()) < 1000)
						&& sotCreatorId.equals(so.getCreatorId()) && sotModifierId.equals(so.getModifierId())) {
					it.remove();
				}
			}
			int i = 0;
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[storableObjects.size()];
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
				StorableObject so = (StorableObject) it.next();
				identifierTransferables[i] = (Identifier_Transferable) so.getId().getTransferable();
			}
			return identifierTransferables;
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
}
