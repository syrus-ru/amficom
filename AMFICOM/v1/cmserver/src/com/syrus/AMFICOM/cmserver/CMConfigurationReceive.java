/*
 * $Id: CMConfigurationReceive.java,v 1.16 2005/04/01 17:38:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.HashSet;
import java.util.Set;

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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.16 $, $Date: 2005/04/01 17:38:27 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMConfigurationReceive extends CMAdministrationReceive {

	private static final long serialVersionUID = 5462858483804681509L;

	public StorableObject_Transferable receiveCableThreadType(CableThreadType_Transferable cableThreadType_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableThreadType | Received " + " cableThreadType", Log.DEBUGLEVEL07);
		try {
			CableThreadType cableThreadType = new CableThreadType(cableThreadType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(cableThreadType);
			CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
			database.update(cableThreadType, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return cableThreadType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveCableLinkTypes(CableLinkType_Transferable[] cableLinkType_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableThreadTypes | Received "
				+ cableLinkType_Transferables.length + " cableThreadTypes", Log.DEBUGLEVEL07);
		Set cableThreadTypeList = new HashSet(cableLinkType_Transferables.length);
		try {
			for (int i = 0; i < cableLinkType_Transferables.length; i++) {
				CableLinkType cableLinkType = new CableLinkType(cableLinkType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(cableLinkType);
				cableThreadTypeList.add(cableLinkType);
			}
			CableLinkTypeDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
			database.update(cableThreadTypeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(cableThreadTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveCableThreads(CableThread_Transferable[] cableThreads_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {

		Log.debugMessage("CMConfigurationReceive.receiveCableThreads | Received "
				+ cableThreads_Transferable.length + " transmissionPathTypes", Log.DEBUGLEVEL07);
		Set cableThreadList = new HashSet(cableThreads_Transferable.length);
		try {
			for (int i = 0; i < cableThreads_Transferable.length; i++) {
				CableThread cableThread = new CableThread(cableThreads_Transferable[i]);
				ConfigurationStorableObjectPool.putStorableObject(cableThread);
				cableThreadList.add(cableThread);
			}
			CableThreadDatabase cableThreadDatabase = ConfigurationDatabaseContext.getCableThreadDatabase();
			cableThreadDatabase.update(cableThreadList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(cableThreadList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receiveCableLinkType(CableLinkType_Transferable cableLinkType_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableLinkType | Received " + " cableLinkType", Log.DEBUGLEVEL07);
		try {
			CableLinkType cableLinkType = new CableLinkType(cableLinkType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(cableLinkType);
			CableLinkTypeDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
			database.update(cableLinkType, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return cableLinkType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receiveCableThread(CableThread_Transferable cableThread_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveCableThread | Received " + " cableThread", Log.DEBUGLEVEL07);
		try {
			CableThread cableThread = new CableThread(cableThread_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(cableThread);
			CableThreadDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
			database.update(cableThread, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return cableThread.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
		Log.debugMessage("CMConfigurationReceive.receiveCableThreadTypes | Received "
				+ cableThreadType_Transferables.length + " cableThreadTypes", Log.DEBUGLEVEL07);
		Set cableThreadTypeList = new HashSet(cableThreadType_Transferables.length);
		try {
			for (int i = 0; i < cableThreadType_Transferables.length; i++) {
				CableThreadType cableThreadType = new CableThreadType(cableThreadType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(cableThreadType);
				cableThreadTypeList.add(cableThreadType);
			}
			CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
			database.update(cableThreadTypeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(cableThreadTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
			Equipment equipment = new Equipment(equipment_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(equipment);
			EquipmentDatabase equipmentDatabase = ConfigurationDatabaseContext.getEquipmentDatabase();
			equipmentDatabase.update(equipment, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return equipment.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
		Log.debugMessage("CMConfigurationReceive.receiveEquipments | Received " + equipment_Transferables.length + " equipments",
				Log.DEBUGLEVEL07);
		Set equipmentList = new HashSet(equipment_Transferables.length);
		try {
			for (int i = 0; i < equipment_Transferables.length; i++) {
				Equipment equipment = new Equipment(equipment_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(equipment);
				equipmentList.add(equipment);
			}
			EquipmentDatabase equipmentDatabase = ConfigurationDatabaseContext.getEquipmentDatabase();
			equipmentDatabase.update(equipmentList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(equipmentList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receiveEquipmentType(EquipmentType_Transferable equipmentType_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveEquipmentType | Received " + " equipmentType", Log.DEBUGLEVEL07);
		try {
			EquipmentType equipmentType = new EquipmentType(equipmentType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(equipmentType);
			EquipmentTypeDatabase equipmentTypeDatabase = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
			equipmentTypeDatabase.update(equipmentType, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return equipmentType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveEquipmentTypes(EquipmentType_Transferable[] equipmentType_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveEquipmentTypes | Received "
				+ equipmentType_Transferables.length
				+ " equipmentTypes", Log.DEBUGLEVEL07);
		Set equipmentTypeList = new HashSet(equipmentType_Transferables.length);
		try {
			for (int i = 0; i < equipmentType_Transferables.length; i++) {
				EquipmentType equipmentType = new EquipmentType(equipmentType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(equipmentType);
				equipmentTypeList.add(equipmentType);
			}
			EquipmentTypeDatabase equipmentTypeDatabase = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
			equipmentTypeDatabase.update(equipmentTypeList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(equipmentTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receiveKIS(KIS_Transferable kis_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveKIS | Received " + " kis", Log.DEBUGLEVEL07);
		try {
			KIS kis = new KIS(kis_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(kis);
			KISDatabase kisDatabase = ConfigurationDatabaseContext.getKISDatabase();
			kisDatabase.update(kis, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return kis.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveKISs(KIS_Transferable[] kis_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveKISs | Received " + kis_Transferables.length + " kiss", Log.DEBUGLEVEL07);
		Set kisList = new HashSet(kis_Transferables.length);
		try {
			for (int i = 0; i < kis_Transferables.length; i++) {
				KIS kis = new KIS(kis_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(kis);
				kisList.add(kis);
			}
			KISDatabase kisDatabase = ConfigurationDatabaseContext.getKISDatabase();
			kisDatabase.update(kisList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(kisList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
			Link link = new Link(link_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(link);
			LinkDatabase linkDatabase = ConfigurationDatabaseContext.getLinkDatabase();
			linkDatabase.update(link, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return link.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
		Set linkList = new HashSet(link_Transferables.length);
		try {
			for (int i = 0; i < link_Transferables.length; i++) {
				Link list = new Link(link_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(list);
				linkList.add(list);
			}
			LinkDatabase linkDatabase = ConfigurationDatabaseContext.getLinkDatabase();
			linkDatabase.update(linkList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(linkList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
			LinkType linkType = new LinkType(linkType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(linkType);
			LinkTypeDatabase linkTypeDatabase = ConfigurationDatabaseContext.getLinkTypeDatabase();
			linkTypeDatabase.update(linkType, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return linkType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveLinkTypes(AbstractLinkType_Transferable[] linkType_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveLinkTypes | Received " + linkType_Transferables.length + " links",
				Log.DEBUGLEVEL07);
		Set linkTypeList = new HashSet(linkType_Transferables.length);
		try {
			for (int i = 0; i < linkType_Transferables.length; i++) {
				AbstractLinkType abstractLinkType;
				int sort = linkType_Transferables[i].discriminator().value();
				switch (sort) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						CableLinkType_Transferable cableLinkTypeTransferable = linkType_Transferables[i].cableLinkType();
						abstractLinkType = new CableLinkType(cableLinkTypeTransferable);
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						LinkType_Transferable linkTypeTransferable = linkType_Transferables[i].linkType();
						abstractLinkType = new LinkType(linkTypeTransferable);
						break;
					default:
						throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Unsupported AbstractLinkTypeSort = " + sort);
				}
				ConfigurationStorableObjectPool.putStorableObject(abstractLinkType);
				linkTypeList.add(abstractLinkType);
			}

			LinkTypeDatabase linkTypeDatabase = ConfigurationDatabaseContext.getLinkTypeDatabase();
			linkTypeDatabase.update(linkTypeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(linkTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
			MeasurementPort measurementPort = new MeasurementPort(measurementPort_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(measurementPort);
			MeasurementPortDatabase measurementPortDatabase = ConfigurationDatabaseContext.getMeasurementPortDatabase();
			measurementPortDatabase.update(measurementPort, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return measurementPort.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveMeasurementPorts(MeasurementPort_Transferable[] measurementPort_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMeasurementPorts | Received "
				+ measurementPort_Transferables.length + " measurementPorts", Log.DEBUGLEVEL07);
		Set measurementPortList = new HashSet(measurementPort_Transferables.length);
		try {
			for (int i = 0; i < measurementPort_Transferables.length; i++) {
				MeasurementPort measurementPort = new MeasurementPort(measurementPort_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(measurementPort);
				measurementPortList.add(measurementPort);
			}
			MeasurementPortDatabase measurementPortDatabase = ConfigurationDatabaseContext.getMeasurementPortDatabase();
			measurementPortDatabase.update(measurementPortList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(measurementPortList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receiveMeasurementPortType(MeasurementPortType_Transferable measurementPortType_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMeasurementPortType | Received " + " measurementPortType", Log.DEBUGLEVEL07);
		try {
			MeasurementPortType measurementPortType = new MeasurementPortType(measurementPortType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(measurementPortType);
			MeasurementPortTypeDatabase measurementPortTypeDatabase = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
			measurementPortTypeDatabase.update(measurementPortType, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return measurementPortType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveMeasurementPortTypes(MeasurementPortType_Transferable[] measurementPortType_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMeasurementPortTypes | Received "
				+ measurementPortType_Transferables.length + " measurementPortTypes", Log.DEBUGLEVEL07);
		Set measurementPortTypeList = new HashSet(measurementPortType_Transferables.length);
		try {
			for (int i = 0; i < measurementPortType_Transferables.length; i++) {
				MeasurementPortType measurementPortType = new MeasurementPortType(measurementPortType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(measurementPortType);
				measurementPortTypeList.add(measurementPortType);
			}
			MeasurementPortTypeDatabase measurementPortTypeDatabase = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
			measurementPortTypeDatabase.update(measurementPortTypeList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(measurementPortTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
			MonitoredElement monitoredElement = new MonitoredElement(monitoredElement_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(monitoredElement);
			MonitoredElementDatabase monitoredElementDatabase = ConfigurationDatabaseContext.getMonitoredElementDatabase();
			monitoredElementDatabase.update(monitoredElement, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return monitoredElement.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receiveMonitoredElements(MonitoredElement_Transferable[] monitoredElement_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveMonitoredElements | Received "
				+ monitoredElement_Transferables.length + " monitoredElements", Log.DEBUGLEVEL07);
		Set monitoredElementList = new HashSet(monitoredElement_Transferables.length);
		try {
			for (int i = 0; i < monitoredElement_Transferables.length; i++) {
				MonitoredElement monitoredElement = new MonitoredElement(monitoredElement_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(monitoredElement);
				monitoredElementList.add(monitoredElement);
			}
			MonitoredElementDatabase monitoredElementDatabase = ConfigurationDatabaseContext.getMonitoredElementDatabase();
			monitoredElementDatabase.update(monitoredElementList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(monitoredElementList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receivePort(Port_Transferable port_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePort | Received " + " port", Log.DEBUGLEVEL07);
		try {
			Port port = new Port(port_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(port);
			PortDatabase portDatabase = ConfigurationDatabaseContext.getPortDatabase();
			portDatabase.update(port, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return port.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receivePorts(Port_Transferable[] port_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePorts | Received " + port_Transferables.length + " ports", Log.DEBUGLEVEL07);
		Set portList = new HashSet(port_Transferables.length);
		try {
			for (int i = 0; i < port_Transferables.length; i++) {
				Port port = new Port(port_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(port);
				portList.add(port);
			}
			PortDatabase portDatabase = ConfigurationDatabaseContext.getPortDatabase();
			portDatabase.update(portList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(portList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receivePortType(PortType_Transferable portType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePortType | Received " + " domain", Log.DEBUGLEVEL07);
		try {
			PortType portType = new PortType(portType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(portType);
			PortTypeDatabase portTypeDatabase = ConfigurationDatabaseContext.getPortTypeDatabase();
			portTypeDatabase.update(portType, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return portType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable[] receivePortTypes(PortType_Transferable[] portType_Transferables,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receivePortTypes | Received " + portType_Transferables.length + " portTypes",
				Log.DEBUGLEVEL07);
		Set portTypeList = new HashSet(portType_Transferables.length);
		try {
			for (int i = 0; i < portType_Transferables.length; i++) {
				PortType portType = new PortType(portType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(portType);
				portTypeList.add(portType);
			}
			PortTypeDatabase portTypeDatabase = ConfigurationDatabaseContext.getPortTypeDatabase();
			portTypeDatabase.update(portTypeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(portTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
			TransmissionPath transmissionPath = new TransmissionPath(transmissionPath_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(transmissionPath);
			TransmissionPathDatabase transmissionPathDatabase = ConfigurationDatabaseContext.getTransmissionPathDatabase();
			transmissionPathDatabase.update(transmissionPath, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return transmissionPath.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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

	public StorableObject_Transferable receiveTransmissionPathType(TransmissionPathType_Transferable transmissionPathType_Transferable,
			boolean force,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("CMConfigurationReceive.receiveTransmissionPathType | Received " + " transmissionPathType", Log.DEBUGLEVEL07);
		try {
			TransmissionPathType transmissionPathType = new TransmissionPathType(transmissionPathType_Transferable);
			ConfigurationStorableObjectPool.putStorableObject(transmissionPathType);
			TransmissionPathTypeDatabase transmissionPathTypeDatabase = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
			transmissionPathTypeDatabase.update(transmissionPathType, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return transmissionPathType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
		Log.debugMessage("CMConfigurationReceive.receiveTransmissionPaths | Received "
				+ transmissionPath_Transferables.length + " transmissionPaths", Log.DEBUGLEVEL07);
		Set transmissionPathList = new HashSet(transmissionPath_Transferables.length);
		try {
			for (int i = 0; i < transmissionPath_Transferables.length; i++) {
				TransmissionPath transmissionPath = new TransmissionPath(transmissionPath_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(transmissionPath);
				transmissionPathList.add(transmissionPath);
			}
			TransmissionPathDatabase transmissionPathDatabase = ConfigurationDatabaseContext.getTransmissionPathDatabase();
			transmissionPathDatabase.update(transmissionPathList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(transmissionPathList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
		Log.debugMessage("CMConfigurationReceive.receiveTransmissionPathTypes | Received "
				+ transmissionPathType_Transferables.length + " transmissionPathTypes", Log.DEBUGLEVEL07);
		Set transmissionPathTypeList = new HashSet(transmissionPathType_Transferables.length);
		try {
			for (int i = 0; i < transmissionPathType_Transferables.length; i++) {
				TransmissionPathType transmissionPathType = new TransmissionPathType(transmissionPathType_Transferables[i]);
				ConfigurationStorableObjectPool.putStorableObject(transmissionPathType);
				transmissionPathTypeList.add(transmissionPathType);
			}
			TransmissionPathTypeDatabase transmissionPathTypeDatabase = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
			transmissionPathTypeDatabase.update(transmissionPathTypeList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(transmissionPathTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
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
