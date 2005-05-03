/*
 * $Id: CMConfigurationReceive.java,v 1.19 2005/05/03 14:27:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.HashSet;
import java.util.Set;

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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.SessionKey_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.19 $, $Date: 2005/05/03 14:27:01 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMConfigurationReceive extends CMAdministrationReceive {

	private static final long serialVersionUID = 5462858483804681509L;


	public StorableObject_Transferable[] receiveEquipmentTypes(EquipmentType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			EquipmentType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (EquipmentType) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new EquipmentType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receivePortTypes(PortType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			PortType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (PortType) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new PortType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurementPortTypes(MeasurementPortType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			MeasurementPortType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (MeasurementPortType) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new MeasurementPortType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTransmissionPathTypes(TransmissionPathType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			TransmissionPathType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (TransmissionPathType) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new TransmissionPathType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveLinkTypes(LinkType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			LinkType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (LinkType) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new LinkType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCableLinkTypes(CableLinkType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			CableLinkType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (CableLinkType) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new CableLinkType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		CableLinkTypeDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCableThreadTypes(CableThreadType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			CableThreadType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (CableThreadType) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new CableThreadType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	public StorableObject_Transferable[] receiveEquipments(Equipment_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Equipment object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Equipment) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Equipment(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		EquipmentDatabase database = ConfigurationDatabaseContext.getEquipmentDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receivePorts(Port_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Port object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Port) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Port(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurementPorts(MeasurementPort_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			MeasurementPort object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (MeasurementPort) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new MeasurementPort(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTransmissionPaths(TransmissionPath_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			TransmissionPath object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (TransmissionPath) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new TransmissionPath(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveKISs(KIS_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			KIS object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (KIS) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new KIS(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMonitoredElements(MonitoredElement_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			MonitoredElement object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (MonitoredElement) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new MonitoredElement(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveLinks(Link_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Link object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Link) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Link(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCableThreads(CableThread_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			CableThread object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (CableThread) ConfigurationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new CableThread(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		CableThreadDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
