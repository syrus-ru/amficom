/*
 * $Id: CMConfigurationReceive.java,v 1.3 2004/11/23 17:42:26 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.KISType;
import com.syrus.AMFICOM.configuration.KISTypeDatabase;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.MCMDatabase;
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
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.ServerDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.UserDatabase;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.KISType_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.configuration.corba.User_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2004/11/23 17:42:26 $
 * @author $Author: max $
 * @module module
 */
public abstract class CMConfigurationReceive implements CMServerOperations {
    //////////////////////////////Configuration Recieve///////////////////////////////////

	public void receiveCableThreadType(
			CableThreadType_Transferable cableThreadType_Transferable,
			boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveCableThreadType | Received " + " cableThreadType", Log.DEBUGLEVEL07);
        try {

            CableThreadType cableThreadType = new CableThreadType(cableThreadType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(cableThreadType);
            CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext
                    .getCableThreadTypeDatabase();
            database.update(cableThreadType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                            CompletionStatus.COMPLETED_NO, e.getMessage());        
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public void receiveCableThreadTypes(
			CableThreadType_Transferable[] cableThreadType_Transferables,
			boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveCableThreadTypes | Received " + cableThreadType_Transferables.length
                + " cableThreadTypes", Log.DEBUGLEVEL07);
        List cableThreadTypeList = new ArrayList(cableThreadType_Transferables.length);
        try {

            for (int i = 0; i < cableThreadType_Transferables.length; i++) {
                CableThreadType cableThreadType = new CableThreadType(cableThreadType_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(cableThreadType);
                cableThreadTypeList.add(cableThreadType);
            }

            CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext
                    .getCharacteristicDatabase();
            database.update(cableThreadTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());       
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public void receiveCharacteristic(Characteristic_Transferable characteristic_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveCharacteristic | Received " + " characteristic", Log.DEBUGLEVEL07);
        try {

            Characteristic characteristic = new Characteristic(characteristic_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(characteristic);
            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) ConfigurationDatabaseContext
                    .getCharacteristicDatabase();
            characteristicDatabase.update(characteristic, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveCharacteristics(Characteristic_Transferable[] characteristic_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveCharacteristics | Received " + characteristic_Transferables.length
                + " characteristics", Log.DEBUGLEVEL07);
        List characteristicList = new ArrayList(characteristic_Transferables.length);
        try {

            for (int i = 0; i < characteristic_Transferables.length; i++) {
                Characteristic characteristic = new Characteristic(characteristic_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(characteristic);
                characteristicList.add(characteristic);
            }

            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) ConfigurationDatabaseContext
                    .getCharacteristicDatabase();
            characteristicDatabase.update(characteristicList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveCharacteristicType(CharacteristicType_Transferable characteristicType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveCharacteristicType | Received " + " characteristicTypes", Log.DEBUGLEVEL07);
        try {

            CharacteristicType characteristicType = new CharacteristicType(characteristicType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(characteristicType);
            CharacteristicTypeDatabase characteristicTypeDatabase = (CharacteristicTypeDatabase) ConfigurationDatabaseContext
                    .getCharacteristicDatabase();
            characteristicTypeDatabase.update(characteristicType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }



    public void receiveCharacteristicTypes(CharacteristicType_Transferable[] characteristicType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receivecharacteristicTypes | Received " + characteristicType_Transferables.length
                + " characteristicTypes", Log.DEBUGLEVEL07);
        List characteristicTypeList = new ArrayList(characteristicType_Transferables.length);
        try {

            for (int i = 0; i < characteristicType_Transferables.length; i++) {
                CharacteristicType characteristicType = new CharacteristicType(characteristicType_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(characteristicType);
                characteristicTypeList.add(characteristicType);
            }

            CharacteristicTypeDatabase characteristicTypeDatabase = (CharacteristicTypeDatabase) ConfigurationDatabaseContext
                    .getCharacteristicTypeDatabase();
            characteristicTypeDatabase.update(characteristicTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveDomain(Domain_Transferable domain_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveDomain | Received " + " domain", Log.DEBUGLEVEL07);
        try {

            Domain domain = new Domain(domain_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(domain);
            DomainDatabase domainDatabase = (DomainDatabase) ConfigurationDatabaseContext
                    .getDomainDatabase();
            domainDatabase.update(domain, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveDomains(Domain_Transferable[] domain_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveDomains | Received " + domain_Transferables.length
                + " domains", Log.DEBUGLEVEL07);
        List domainList = new ArrayList(domain_Transferables.length);
        try {

            for (int i = 0; i < domain_Transferables.length; i++) {
                Domain domain = new Domain(domain_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(domain);
                domainList.add(domain);
            }

            DomainDatabase domainDatabase = (DomainDatabase) ConfigurationDatabaseContext
                    .getDomainDatabase();
            domainDatabase.update(domainList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveEquipment(Equipment_Transferable equipment_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveEquipment | Received " + " equipment", Log.DEBUGLEVEL07);
        try {

            Equipment equipment = new Equipment(equipment_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(equipment);
            EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext
                    .getEquipmentDatabase();
            equipmentDatabase.update(equipment, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveEquipments(Equipment_Transferable[] equipment_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveEquipments | Received " + equipment_Transferables.length
                + " equipments", Log.DEBUGLEVEL07);
        List equipmentList = new ArrayList(equipment_Transferables.length);
        try {

            for (int i = 0; i < equipment_Transferables.length; i++) {
                Equipment equipment = new Equipment(equipment_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(equipment);
                equipmentList.add(equipment);
            }

            EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext
                    .getEquipmentDatabase();
            equipmentDatabase.update(equipmentList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveEquipmentType(EquipmentType_Transferable equipmentType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveEquipmentType | Received " + " equipmentType", Log.DEBUGLEVEL07);
        try {

            EquipmentType equipmentType = new EquipmentType(equipmentType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(equipmentType);
            EquipmentTypeDatabase equipmentTypeDatabase = (EquipmentTypeDatabase) ConfigurationDatabaseContext
                    .getEquipmentTypeDatabase();
            equipmentTypeDatabase.update(equipmentType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveEquipmentTypes(EquipmentType_Transferable[] equipmentType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveEquipmentTypes | Received " + equipmentType_Transferables.length
                + " equipmentTypes", Log.DEBUGLEVEL07);
        List equipmentTypeList = new ArrayList(equipmentType_Transferables.length);
        try {

            for (int i = 0; i < equipmentType_Transferables.length; i++) {
                EquipmentType equipmentType = new EquipmentType(equipmentType_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(equipmentType);
                equipmentTypeList.add(equipmentType);
            }

            EquipmentTypeDatabase equipmentTypeDatabase = (EquipmentTypeDatabase) ConfigurationDatabaseContext
                    .getEquipmentTypeDatabase();
            equipmentTypeDatabase.update(equipmentTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveKIS(KIS_Transferable kis_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveKIS | Received " + " kis", Log.DEBUGLEVEL07);
        try {

            KIS kis = new KIS(kis_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(kis);
            KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext
                    .getKISDatabase();
            kisDatabase.update(kis, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveKISs(KIS_Transferable[] kis_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveKISs | Received " + kis_Transferables.length
                + " kiss", Log.DEBUGLEVEL07);
        List kisList = new ArrayList(kis_Transferables.length);
        try {

            for (int i = 0; i < kis_Transferables.length; i++) {
                KIS kis = new KIS(kis_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(kis);
                kisList.add(kis);
            }

            KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext
                    .getKISDatabase();
            kisDatabase.update(kisList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }
    
    public void receiveKISType(KISType_Transferable kisType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveKISType | Received " + " kis", Log.DEBUGLEVEL07);
        try {

            KISType kisType = new KISType(kisType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(kisType);
            KISTypeDatabase kisTypeDatabase = (KISTypeDatabase) ConfigurationDatabaseContext
                    .getKISTypeDatabase();
            kisTypeDatabase.update(kisType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public void receiveKISTypes(KISType_Transferable[] kisType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveKISTypes | Received " + kisType_Transferables.length
                + " kiss", Log.DEBUGLEVEL07);
        List kisTypeList = new ArrayList(kisType_Transferables.length);
        try {

            for (int i = 0; i < kisType_Transferables.length; i++) {
                KISType kisType = new KISType(kisType_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(kisType);
                kisTypeList.add(kisType);
            }

            KISTypeDatabase kisTypeDatabase = (KISTypeDatabase) ConfigurationDatabaseContext
                    .getKISTypeDatabase();
            kisTypeDatabase.update(kisTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

	public void receiveLink(Link_Transferable link_Transferable, boolean force,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveLink | Received " + " link", Log.DEBUGLEVEL07);
        try {

            Link link = new Link(link_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(link);
            LinkDatabase linkDatabase = (LinkDatabase) ConfigurationDatabaseContext
                    .getLinkDatabase();
            linkDatabase.update(link, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public void receiveLinks(Link_Transferable[] link_Transferables,
			boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveLinks | Received " + link_Transferables.length
                + " links", Log.DEBUGLEVEL07);
        List linkList = new ArrayList(link_Transferables.length);
        try {

            for (int i = 0; i < link_Transferables.length; i++) {
                Link list = new Link(link_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(list);
                linkList.add(list);
            }

            LinkDatabase linkDatabase = (LinkDatabase) ConfigurationDatabaseContext
                    .getLinkDatabase();
            linkDatabase.update(linkList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public void receiveLinkType(LinkType_Transferable linkType_Transferable,
			boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveLinkType | Received " + " linkType", Log.DEBUGLEVEL07);
        try {

            LinkType linkType = new LinkType(linkType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(linkType);
            LinkTypeDatabase linkTypeDatabase = (LinkTypeDatabase) ConfigurationDatabaseContext
                    .getLinkTypeDatabase();
            linkTypeDatabase.update(linkType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public void receiveLinkTypes(
			AbstractLinkType_Transferable[] linkType_Transferables, boolean force,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveLinkTypes | Received " + linkType_Transferables.length
                + " links", Log.DEBUGLEVEL07);
        List linkTypeList = new ArrayList(linkType_Transferables.length);
        try {

            for (int i = 0; i < linkType_Transferables.length; i++) {
            	AbstractLinkType abstractLinkType;
                int sort = linkType_Transferables[i].discriminator().value();
            	switch(sort){
            		case AbstractLinkTypeSort._CABLE_LINK_TYPE:
            			abstractLinkType = new LinkType(linkType_Transferables[i].cableLinkType());
            			break;
            		case AbstractLinkTypeSort._LINK_TYPE:
            			abstractLinkType = new LinkType(linkType_Transferables[i].linkType());
            			break;
            		case AbstractLinkTypeSort._CABLE_THREAD_TYPE:
            			abstractLinkType = new CableThreadType(linkType_Transferables[i].cableThreadType());
            			break;
            		default:
            			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,  CompletionStatus.COMPLETED_NO,
														 "Unsupported AbstractLinkTypeSort = " + sort);
            	}
                ConfigurationStorableObjectPool.putStorableObject(abstractLinkType);
                linkTypeList.add(abstractLinkType);
            }

            LinkTypeDatabase linkTypeDatabase = (LinkTypeDatabase) ConfigurationDatabaseContext
                    .getLinkTypeDatabase();
            linkTypeDatabase.update(linkTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public void receiveMCM(MCM_Transferable mcm_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMCM | Received " + " mcm", Log.DEBUGLEVEL07);
        try {

            MCM mcm = new MCM(mcm_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(mcm);
            MCMDatabase mcmDatabase = (MCMDatabase) ConfigurationDatabaseContext
                    .getMCMDatabase();
            mcmDatabase.update(mcm, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveMCMs(MCM_Transferable[] mcm_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMCMs | Received " + mcm_Transferables.length
                + " mcms", Log.DEBUGLEVEL07);
        List mcmList = new ArrayList(mcm_Transferables.length);
        try {

            for (int i = 0; i < mcm_Transferables.length; i++) {
                MCM mcm = new MCM(mcm_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(mcm);
                mcmList.add(mcm);
            }

            MCMDatabase mcmDatabase = (MCMDatabase) ConfigurationDatabaseContext
                    .getMCMDatabase();
            mcmDatabase.update(mcmList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveMeasurementPort(MeasurementPort_Transferable measurementPort_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMeasurementPort | Received " + " measurementPort", Log.DEBUGLEVEL07);
        try {

            MeasurementPort measurementPort = new MeasurementPort(measurementPort_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(measurementPort);
            MeasurementPortDatabase measurementPortDatabase = (MeasurementPortDatabase) ConfigurationDatabaseContext
                    .getMeasurementPortDatabase();
            measurementPortDatabase.update(measurementPort, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveMeasurementPorts(MeasurementPort_Transferable[] measurementPort_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMeasurementPorts | Received " + measurementPort_Transferables.length
                + " measurementPorts", Log.DEBUGLEVEL07);
        List measurementPortList = new ArrayList(measurementPort_Transferables.length);
        try {

            for (int i = 0; i < measurementPort_Transferables.length; i++) {
                MeasurementPort measurementPort = new MeasurementPort(measurementPort_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(measurementPort);
                measurementPortList.add(measurementPort);
            }

            MeasurementPortDatabase measurementPortDatabase = (MeasurementPortDatabase) ConfigurationDatabaseContext
                    .getMeasurementPortDatabase();
            measurementPortDatabase.update(measurementPortList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveMeasurementPortType(MeasurementPortType_Transferable measurementPortType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMeasurementPortType | Received " + " measurementPortType", Log.DEBUGLEVEL07);
        try {

            MeasurementPortType measurementPortType = new MeasurementPortType(measurementPortType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(measurementPortType);
            MeasurementPortTypeDatabase measurementPortTypeDatabase = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext
                    .getMeasurementPortTypeDatabase();
            measurementPortTypeDatabase.update(measurementPortType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveMeasurementPortTypes(MeasurementPortType_Transferable[] measurementPortType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMeasurementPortTypes | Received " + measurementPortType_Transferables.length
                + " measurementPortTypes", Log.DEBUGLEVEL07);
        List measurementPortTypeList = new ArrayList(measurementPortType_Transferables.length);
        try {

            for (int i = 0; i < measurementPortType_Transferables.length; i++) {
                MeasurementPortType measurementPortType = new MeasurementPortType(measurementPortType_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(measurementPortType);
                measurementPortTypeList.add(measurementPortType);
            }

            MeasurementPortTypeDatabase measurementPortTypeDatabase = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext
                    .getMeasurementPortTypeDatabase();
            measurementPortTypeDatabase.update(measurementPortTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveMonitoredElement(MonitoredElement_Transferable monitoredElement_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMonitoredElement | Received " + " monitoredElement", Log.DEBUGLEVEL07);
        try {

            MonitoredElement monitoredElement = new MonitoredElement(monitoredElement_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(monitoredElement);
            MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
                    .getMonitoredElementDatabase();
            monitoredElementDatabase.update(monitoredElement, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveMonitoredElements(MonitoredElement_Transferable[] monitoredElement_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveMonitoredElements | Received " + monitoredElement_Transferables.length
                + " monitoredElements", Log.DEBUGLEVEL07);
        List monitoredElementList = new ArrayList(monitoredElement_Transferables.length);
        try {

            for (int i = 0; i < monitoredElement_Transferables.length; i++) {
                MonitoredElement monitoredElement = new MonitoredElement(monitoredElement_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(monitoredElement);
                monitoredElementList.add(monitoredElement);
            }

            MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
                    .getMonitoredElementDatabase();
            monitoredElementDatabase.update(monitoredElementList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receivePort(Port_Transferable port_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receivePort | Received " + " port", Log.DEBUGLEVEL07);
        try {

            Port port = new Port(port_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(port);
            PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext
                    .getPortDatabase();
            portDatabase.update(port, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receivePorts(Port_Transferable[] port_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receivePorts | Received " + port_Transferables.length
                + " ports", Log.DEBUGLEVEL07);
        List portList = new ArrayList(port_Transferables.length);
        try {

            for (int i = 0; i < port_Transferables.length; i++) {
                Port port = new Port(port_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(port);
                portList.add(port);
            }

            PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext
                    .getPortDatabase();
            portDatabase.update(portList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receivePortType(PortType_Transferable portType_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receivePortType | Received " + " domain", Log.DEBUGLEVEL07);
        try {

            PortType portType = new PortType(portType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(portType);
            PortTypeDatabase portTypeDatabase = (PortTypeDatabase) ConfigurationDatabaseContext
                    .getPortTypeDatabase();
            portTypeDatabase.update(portType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receivePortTypes(PortType_Transferable[] portType_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receivePortTypes | Received " + portType_Transferables.length
                + " portTypes", Log.DEBUGLEVEL07);
        List portTypeList = new ArrayList(portType_Transferables.length);
        try {

            for (int i = 0; i < portType_Transferables.length; i++) {
                PortType portType = new PortType(portType_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(portType);
                portTypeList.add(portType);
            }

            PortTypeDatabase portTypeDatabase = (PortTypeDatabase) ConfigurationDatabaseContext
                    .getPortTypeDatabase();
            portTypeDatabase.update(portTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveServer(Server_Transferable server_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveServer | Received " + " server", Log.DEBUGLEVEL07);
        try {

            Server server = new Server(server_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(server);
            ServerDatabase serverDatabase = (ServerDatabase) ConfigurationDatabaseContext
                    .getServerDatabase();
            serverDatabase.update(server, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveServers(Server_Transferable[] server_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveServers | Received " + server_Transferables.length
                + " servers", Log.DEBUGLEVEL07);
        List serverList = new ArrayList(server_Transferables.length);
        try {

            for (int i = 0; i < server_Transferables.length; i++) {
                Server server = new Server(server_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(server);
                serverList.add(server);
            }

            ServerDatabase serverDatabase = (ServerDatabase) ConfigurationDatabaseContext
                    .getServerDatabase();
            serverDatabase.update(serverList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveTransmissionPath(TransmissionPath_Transferable transmissionPath_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveTransmissionPath | Received " + " transmissionPath", Log.DEBUGLEVEL07);
        try {

            TransmissionPath transmissionPath = new TransmissionPath(transmissionPath_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(transmissionPath);
            TransmissionPathDatabase transmissionPathDatabase = (TransmissionPathDatabase) ConfigurationDatabaseContext
                    .getTransmissionPathDatabase();
            transmissionPathDatabase.update(transmissionPath, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public void receiveTransmissionPathType(
			TransmissionPathType_Transferable transmissionPathType_Transferable,
			boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveTransmissionPathType | Received " + " transmissionPathType", Log.DEBUGLEVEL07);
        try {

            TransmissionPathType transmissionPathType = new TransmissionPathType(transmissionPathType_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(transmissionPathType);
            TransmissionPathTypeDatabase transmissionPathTypeDatabase = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext
                    .getTransmissionPathDatabase();
            transmissionPathTypeDatabase.update(transmissionPathType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public void receiveTransmissionPaths(TransmissionPath_Transferable[] transmissionPath_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveTransmissionPaths | Received " + transmissionPath_Transferables.length
                + " transmissionPaths", Log.DEBUGLEVEL07);
        List transmissionPathList = new ArrayList(transmissionPath_Transferables.length);
        try {

            for (int i = 0; i < transmissionPath_Transferables.length; i++) {
                TransmissionPath transmissionPath = new TransmissionPath(transmissionPath_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(transmissionPath);
                transmissionPathList.add(transmissionPath);
            }

            TransmissionPathDatabase transmissionPathDatabase = (TransmissionPathDatabase) ConfigurationDatabaseContext
                    .getTransmissionPathDatabase();
            transmissionPathDatabase.update(transmissionPathList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public void receiveTransmissionPathTypes(
			TransmissionPathType_Transferable[] transmissionPathType_Transferables,
			boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.receiveTransmissionPathTypes | Received " + transmissionPathType_Transferables.length
                + " transmissionPathTypes", Log.DEBUGLEVEL07);
        List transmissionPathTypeList = new ArrayList(transmissionPathType_Transferables.length);
        try {

            for (int i = 0; i < transmissionPathType_Transferables.length; i++) {
                TransmissionPathType transmissionPathType = new TransmissionPathType(transmissionPathType_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(transmissionPathType);
                transmissionPathTypeList.add(transmissionPathType);
            }

            TransmissionPathTypeDatabase transmissionPathTypeDatabase = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext
                    .getTransmissionPathDatabase();
            transmissionPathTypeDatabase.update(transmissionPathTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public void receiveUser(User_Transferable user_Transferable, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveUser | Received " + " user", Log.DEBUGLEVEL07);
        try {

            User user = new User(user_Transferable);
            ConfigurationStorableObjectPool.putStorableObject(user);
            UserDatabase userDatabase = (UserDatabase) ConfigurationDatabaseContext
                    .getUserDatabase();
            userDatabase.update(user, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.cmserver.corba.CMServerOperations#receiveUsers(com.syrus.AMFICOM.configuration.corba.User_Transferable[], boolean, com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable)
     */
    public void receiveUsers(User_Transferable[] user_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.receiveUsers | Received " + user_Transferables.length
                + " users", Log.DEBUGLEVEL07);
        List userList = new ArrayList(user_Transferables.length);
        try {

            for (int i = 0; i < user_Transferables.length; i++) {
                User user = new User(user_Transferables[i]);
                ConfigurationStorableObjectPool.putStorableObject(user);
                userList.add(user);
            }

            UserDatabase userDatabase = (UserDatabase) ConfigurationDatabaseContext
                    .getUserDatabase();
            userDatabase.update(userList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (IllegalDataException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                                CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }


}
