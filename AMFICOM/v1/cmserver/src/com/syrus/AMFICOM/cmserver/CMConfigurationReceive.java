/*
 * $Id: CMConfigurationReceive.java,v 1.13 2005/02/10 12:09:02 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThread;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.CableLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.configuration.corba.CableThread_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.13 $, $Date: 2005/02/10 12:09:02 $
 * @author $Author: bob $
 * @module cmserver_v1
 */
public abstract class CMConfigurationReceive extends CMAdministrationReceive {
	
	private static final long serialVersionUID = 5462858483804681509L;

	public StorableObject_Transferable receiveCableThreadType(CableThreadType_Transferable cableThreadType_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableThreadType | Received " + " cableThreadType", Log.DEBUGLEVEL07);
		try {
			cableThreadType_Transferable.header.modifier_id = accessIdentifier.user_id;
			CableThreadType cableThreadType = new CableThreadType(cableThreadType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(cableThreadType);
			CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.getCableThreadTypeDatabase();
			database.update(cableThreadType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return cableThreadType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());        
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	
	public StorableObject_Transferable[] receiveCableLinkTypes(	CableLinkType_Transferable[] cableLinkType_Transferables,
										boolean force,
										AccessIdentifier_Transferable accessIdentifier_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableThreadTypes | Received "
				+ cableLinkType_Transferables.length + " cableThreadTypes", Log.DEBUGLEVEL07);
		List cableThreadTypeList = new ArrayList(cableLinkType_Transferables.length);
		try {
			for (int i = 0; i < cableLinkType_Transferables.length; i++) {
				cableLinkType_Transferables[i].header.modifier_id = accessIdentifier_Transferable.user_id;
				CableLinkType cableLinkType = new CableLinkType(cableLinkType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(cableLinkType);
				cableThreadTypeList.add(cableLinkType);
			}
			CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext
					.getCableLinkTypeDatabase();
			database.update(cableThreadTypeList, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(cableThreadTypeList);
		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	
	public StorableObject_Transferable[] receiveCableThreads(CableThread_Transferable[] cableThreads_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {

		Log.debugMessage("CMConfigurationReceive.receiveCableThreads | Received " + cableThreads_Transferable.length
                + " transmissionPathTypes", Log.DEBUGLEVEL07);
		List cableThreadList = new ArrayList(cableThreads_Transferable.length);
		try {
			for (int i = 0; i < cableThreads_Transferable.length; i++) {
				cableThreads_Transferable[i].header.modifier_id = accessIdentifier.user_id;
				CableThread cableThread = new CableThread(cableThreads_Transferable[i]);
				ConfigurationStorableObjectPool.putStorableObject(cableThread);
				cableThreadList.add(cableThread);
			}
			CableThreadDatabase cableThreadDatabase = (CableThreadDatabase) ConfigurationDatabaseContext.getCableThreadDatabase();
			cableThreadDatabase.update(cableThreadList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(cableThreadList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
		}
	
	public StorableObject_Transferable receiveCableLinkType(	CableLinkType_Transferable cableLinkType_Transferable,
										boolean force,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableLinkType | Received " + " cableLinkType", Log.DEBUGLEVEL07);
		try {
			cableLinkType_Transferable.header.modifier_id = accessIdentifier.user_id;
			CableLinkType cableLinkType = new CableLinkType(cableLinkType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(cableLinkType);
			CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.getCableLinkTypeDatabase();
			database.update(cableLinkType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return cableLinkType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());        
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	public StorableObject_Transferable receiveCableThread(	CableThread_Transferable cableThread_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableThread | Received " + " cableThread", Log.DEBUGLEVEL07);
		try {
			cableThread_Transferable.header.modifier_id = accessIdentifier.user_id;
			CableThread cableThread = new CableThread(cableThread_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(cableThread);
			CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.getCableThreadDatabase();
			database.update(cableThread, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return cableThread.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());        
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	public StorableObject_Transferable[] receiveCableThreadTypes(CableThreadType_Transferable[] cableThreadType_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableThreadTypes | Received " + cableThreadType_Transferables.length
                + " cableThreadTypes", Log.DEBUGLEVEL07);
		List cableThreadTypeList = new ArrayList(cableThreadType_Transferables.length);
		try {
			for (int i = 0; i < cableThreadType_Transferables.length; i++) {
				cableThreadType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				CableThreadType cableThreadType = new CableThreadType(cableThreadType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(cableThreadType);
				cableThreadTypeList.add(cableThreadType);
			}
			CableThreadTypeDatabase database = (CableThreadTypeDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
			database.update(cableThreadTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(cableThreadTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());       
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveEquipment(Equipment_Transferable equipment_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveEquipment | Received " + " equipment", Log.DEBUGLEVEL07);
		try {
			equipment_Transferable.header.modifier_id = accessIdentifier.user_id;
			Equipment equipment = new Equipment(equipment_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(equipment);
			EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext.getEquipmentDatabase();
			equipmentDatabase.update(equipment, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return equipment.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveEquipments(Equipment_Transferable[] equipment_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveEquipments | Received " + equipment_Transferables.length + " equipments", Log.DEBUGLEVEL07);
		List equipmentList = new ArrayList(equipment_Transferables.length);
		try {
			for (int i = 0; i < equipment_Transferables.length; i++) {
				equipment_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Equipment equipment = new Equipment(equipment_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(equipment);
				equipmentList.add(equipment);
			}
			EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext.getEquipmentDatabase();
			equipmentDatabase.update(equipmentList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(equipmentList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveEquipmentType(EquipmentType_Transferable equipmentType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveEquipmentType | Received " + " equipmentType", Log.DEBUGLEVEL07);
		try {
			equipmentType_Transferable.header.modifier_id = accessIdentifier.user_id;
			EquipmentType equipmentType = new EquipmentType(equipmentType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(equipmentType);
			EquipmentTypeDatabase equipmentTypeDatabase = (EquipmentTypeDatabase) ConfigurationDatabaseContext.getEquipmentTypeDatabase();
			equipmentTypeDatabase.update(equipmentType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return equipmentType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveEquipmentTypes(EquipmentType_Transferable[] equipmentType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveEquipmentTypes | Received " + equipmentType_Transferables.length
						+ " equipmentTypes", Log.DEBUGLEVEL07);
		List equipmentTypeList = new ArrayList(equipmentType_Transferables.length);
		try {
			for (int i = 0; i < equipmentType_Transferables.length; i++) {
				equipmentType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				EquipmentType equipmentType = new EquipmentType(equipmentType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(equipmentType);
				equipmentTypeList.add(equipmentType);
			}
			EquipmentTypeDatabase equipmentTypeDatabase = (EquipmentTypeDatabase) ConfigurationDatabaseContext.getEquipmentTypeDatabase();
			equipmentTypeDatabase.update(equipmentTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(equipmentTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveKIS(KIS_Transferable kis_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveKIS | Received " + " kis", Log.DEBUGLEVEL07);
		try {
			kis_Transferable.header.modifier_id = accessIdentifier.user_id;
			KIS kis = new KIS(kis_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(kis);
			KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext.getKISDatabase();
			kisDatabase.update(kis, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return kis.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveKISs(KIS_Transferable[] kis_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveKISs | Received " + kis_Transferables.length
						+ " kiss", Log.DEBUGLEVEL07);
		List kisList = new ArrayList(kis_Transferables.length);
		try {
			for (int i = 0; i < kis_Transferables.length; i++) {
				kis_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				KIS kis = new KIS(kis_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(kis);
				kisList.add(kis);
			}
			KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext.getKISDatabase();
			kisDatabase.update(kisList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(kisList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
    
	public StorableObject_Transferable receiveLink(Link_Transferable link_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveLink | Received " + " link", Log.DEBUGLEVEL07);
		try {
			link_Transferable.header.modifier_id = accessIdentifier.user_id;
			Link link = new Link(link_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(link);
			LinkDatabase linkDatabase = (LinkDatabase) ConfigurationDatabaseContext.getLinkDatabase();
			linkDatabase.update(link, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return link.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveLinks(Link_Transferable[] link_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveLinks | Received " + link_Transferables.length + " links", Log.DEBUGLEVEL07);
		List linkList = new ArrayList(link_Transferables.length);
		try {
			for (int i = 0; i < link_Transferables.length; i++) {
				link_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Link list = new Link(link_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(list);
				linkList.add(list);
			}
			LinkDatabase linkDatabase = (LinkDatabase) ConfigurationDatabaseContext.getLinkDatabase();
			linkDatabase.update(linkList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(linkList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveLinkType(LinkType_Transferable linkType_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveLinkType | Received " + " linkType", Log.DEBUGLEVEL07);
		try {
			linkType_Transferable.header.modifier_id = accessIdentifier.user_id;
			LinkType linkType = new LinkType(linkType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(linkType);
			LinkTypeDatabase linkTypeDatabase = (LinkTypeDatabase) ConfigurationDatabaseContext.getLinkTypeDatabase();
			linkTypeDatabase.update(linkType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return linkType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveLinkTypes(AbstractLinkType_Transferable[] linkType_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveLinkTypes | Received " + linkType_Transferables.length + " links", Log.DEBUGLEVEL07);
		List linkTypeList = new ArrayList(linkType_Transferables.length);
		try {
			for (int i = 0; i < linkType_Transferables.length; i++) {
				AbstractLinkType abstractLinkType;
				int sort = linkType_Transferables[i].discriminator().value();
				switch(sort) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						CableLinkType_Transferable cableLinkTypeTransferable = linkType_Transferables[i].cableLinkType();
						cableLinkTypeTransferable.header.modifier_id = accessIdentifier.user_id;
						abstractLinkType = new CableLinkType(cableLinkTypeTransferable);
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						LinkType_Transferable linkTypeTransferable = linkType_Transferables[i].linkType();
						linkTypeTransferable.header.modifier_id = accessIdentifier.user_id;
						abstractLinkType = new LinkType(linkType_Transferables[i].linkType());
						break;
					default:
						throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,  CompletionStatus.COMPLETED_NO,
											 "Unsupported AbstractLinkTypeSort = " + sort);
				}
				ConfigurationStorableObjectPool.putStorableObject(abstractLinkType);
				linkTypeList.add(abstractLinkType);
			}

			LinkTypeDatabase linkTypeDatabase = (LinkTypeDatabase) ConfigurationDatabaseContext.getLinkTypeDatabase();
			linkTypeDatabase.update(linkTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return this.getListHeaders(linkTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveMeasurementPort(MeasurementPort_Transferable measurementPort_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMeasurementPort | Received " + " measurementPort", Log.DEBUGLEVEL07);
		try {
			measurementPort_Transferable.header.modifier_id = accessIdentifier.user_id;
			MeasurementPort measurementPort = new MeasurementPort(measurementPort_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(measurementPort);
			MeasurementPortDatabase measurementPortDatabase = (MeasurementPortDatabase) ConfigurationDatabaseContext.getMeasurementPortDatabase();
			measurementPortDatabase.update(measurementPort, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return measurementPort.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurementPorts(MeasurementPort_Transferable[] measurementPort_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMeasurementPorts | Received " + measurementPort_Transferables.length + " measurementPorts", Log.DEBUGLEVEL07);
		List measurementPortList = new ArrayList(measurementPort_Transferables.length);
		try {
			for (int i = 0; i < measurementPort_Transferables.length; i++) {
				measurementPort_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				MeasurementPort measurementPort = new MeasurementPort(measurementPort_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(measurementPort);
				measurementPortList.add(measurementPort);
			}
			MeasurementPortDatabase measurementPortDatabase = (MeasurementPortDatabase) ConfigurationDatabaseContext.getMeasurementPortDatabase();
			measurementPortDatabase.update(measurementPortList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(measurementPortList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveMeasurementPortType(MeasurementPortType_Transferable measurementPortType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMeasurementPortType | Received " + " measurementPortType", Log.DEBUGLEVEL07);
		try {
			measurementPortType_Transferable.header.modifier_id = accessIdentifier.user_id;
			MeasurementPortType measurementPortType = new MeasurementPortType(measurementPortType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(measurementPortType);
			MeasurementPortTypeDatabase measurementPortTypeDatabase = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
			measurementPortTypeDatabase.update(measurementPortType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return measurementPortType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurementPortTypes(MeasurementPortType_Transferable[] measurementPortType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMeasurementPortTypes | Received " + measurementPortType_Transferables.length
						+ " measurementPortTypes", Log.DEBUGLEVEL07);
		List measurementPortTypeList = new ArrayList(measurementPortType_Transferables.length);
		try {
			for (int i = 0; i < measurementPortType_Transferables.length; i++) {
				measurementPortType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				MeasurementPortType measurementPortType = new MeasurementPortType(measurementPortType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(measurementPortType);
				measurementPortTypeList.add(measurementPortType);
			}
			MeasurementPortTypeDatabase measurementPortTypeDatabase = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
			measurementPortTypeDatabase.update(measurementPortTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(measurementPortTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveMonitoredElement(MonitoredElement_Transferable monitoredElement_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMonitoredElement | Received " + " monitoredElement", Log.DEBUGLEVEL07);
		try {
			monitoredElement_Transferable.header.modifier_id = accessIdentifier.user_id;
			MonitoredElement monitoredElement = new MonitoredElement(monitoredElement_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(monitoredElement);
			MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext.getMonitoredElementDatabase();
			monitoredElementDatabase.update(monitoredElement, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return monitoredElement.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMonitoredElements(MonitoredElement_Transferable[] monitoredElement_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMonitoredElements | Received " + monitoredElement_Transferables.length
							+ " monitoredElements", Log.DEBUGLEVEL07);
		List monitoredElementList = new ArrayList(monitoredElement_Transferables.length);
		try {
			for (int i = 0; i < monitoredElement_Transferables.length; i++) {
				monitoredElement_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				MonitoredElement monitoredElement = new MonitoredElement(monitoredElement_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(monitoredElement);
				monitoredElementList.add(monitoredElement);
			}
			MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext.getMonitoredElementDatabase();
			monitoredElementDatabase.update(monitoredElementList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(monitoredElementList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receivePort(Port_Transferable port_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePort | Received " + " port", Log.DEBUGLEVEL07);
		try {
			port_Transferable.header.modifier_id = accessIdentifier.user_id;
			Port port = new Port(port_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(port);
			PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext.getPortDatabase();
			portDatabase.update(port, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return port.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receivePorts(Port_Transferable[] port_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePorts | Received " + port_Transferables.length + " ports", Log.DEBUGLEVEL07);
		List portList = new ArrayList(port_Transferables.length);
		try {
			for (int i = 0; i < port_Transferables.length; i++) {
				port_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Port port = new Port(port_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(port);
				portList.add(port);
			}
			PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext.getPortDatabase();
			portDatabase.update(portList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(portList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e .getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receivePortType(PortType_Transferable portType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePortType | Received " + " domain", Log.DEBUGLEVEL07);
		try {
			portType_Transferable.header.modifier_id = accessIdentifier.user_id;
			PortType portType = new PortType(portType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(portType);
			PortTypeDatabase portTypeDatabase = (PortTypeDatabase) ConfigurationDatabaseContext.getPortTypeDatabase();
			portTypeDatabase.update(portType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return portType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receivePortTypes(PortType_Transferable[] portType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePortTypes | Received " + portType_Transferables.length
						+ " portTypes", Log.DEBUGLEVEL07);
		List portTypeList = new ArrayList(portType_Transferables.length);
		try {
			for (int i = 0; i < portType_Transferables.length; i++) {
			portType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
			PortType portType = new PortType(portType_Transferables[i]);
			ConfigurationStorableObjectPool.putStorableObject(portType);
			portTypeList.add(portType);
			}
			PortTypeDatabase portTypeDatabase = (PortTypeDatabase) ConfigurationDatabaseContext.getPortTypeDatabase();
			portTypeDatabase.update(portTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(portTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveTransmissionPath(TransmissionPath_Transferable transmissionPath_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveTransmissionPath | Received " + " transmissionPath", Log.DEBUGLEVEL07);
		try {
			transmissionPath_Transferable.header.modifier_id = accessIdentifier.user_id;
			TransmissionPath transmissionPath = new TransmissionPath(transmissionPath_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(transmissionPath);
			TransmissionPathDatabase transmissionPathDatabase = (TransmissionPathDatabase) ConfigurationDatabaseContext.getTransmissionPathDatabase();
			transmissionPathDatabase.update(transmissionPath, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return transmissionPath.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e .getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveTransmissionPathType(TransmissionPathType_Transferable transmissionPathType_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveTransmissionPathType | Received " + " transmissionPathType", Log.DEBUGLEVEL07);
		try {
			transmissionPathType_Transferable.header.modifier_id = accessIdentifier.user_id;
			TransmissionPathType transmissionPathType = new TransmissionPathType(transmissionPathType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(transmissionPathType);
			TransmissionPathTypeDatabase transmissionPathTypeDatabase = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.getTransmissionPathDatabase();
			transmissionPathTypeDatabase.update(transmissionPathType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return transmissionPathType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTransmissionPaths(TransmissionPath_Transferable[] transmissionPath_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveTransmissionPaths | Received " + transmissionPath_Transferables.length
                + " transmissionPaths", Log.DEBUGLEVEL07);
    List transmissionPathList = new ArrayList(transmissionPath_Transferables.length);
		try {
			for (int i = 0; i < transmissionPath_Transferables.length; i++) {
				transmissionPath_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				TransmissionPath transmissionPath = new TransmissionPath(transmissionPath_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(transmissionPath);
				transmissionPathList.add(transmissionPath);
			}
			TransmissionPathDatabase transmissionPathDatabase = (TransmissionPathDatabase) ConfigurationDatabaseContext.getTransmissionPathDatabase();
			transmissionPathDatabase.update(transmissionPathList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(transmissionPathList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTransmissionPathTypes(TransmissionPathType_Transferable[] transmissionPathType_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveTransmissionPathTypes | Received " + transmissionPathType_Transferables.length
                + " transmissionPathTypes", Log.DEBUGLEVEL07);
		List transmissionPathTypeList = new ArrayList(transmissionPathType_Transferables.length);
		try {
			for (int i = 0; i < transmissionPathType_Transferables.length; i++) {
				transmissionPathType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				TransmissionPathType transmissionPathType = new TransmissionPathType(transmissionPathType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(transmissionPathType);
				transmissionPathTypeList.add(transmissionPathType);
			}
			TransmissionPathTypeDatabase transmissionPathTypeDatabase = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.getTransmissionPathDatabase();
			transmissionPathTypeDatabase.update(transmissionPathTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
			return super.getListHeaders(transmissionPathTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

}
