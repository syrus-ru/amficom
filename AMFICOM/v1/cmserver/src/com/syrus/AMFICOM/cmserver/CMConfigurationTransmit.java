/*
 * $Id: CMConfigurationTransmit.java,v 1.16 2005/04/01 17:38:41 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThread;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
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
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2005/04/01 17:38:41 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class CMConfigurationTransmit extends CMAdministrationTransmit {

	private static final long serialVersionUID = 3564457938839553536L;

	public CableLinkType_Transferable transmitCableLinkType(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitCableLinkType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			CableLinkType cableLinkType = (CableLinkType) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (CableLinkType_Transferable) cableLinkType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CableThreadType_Transferable transmitCableThreadType(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreadType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			CableThreadType cableThreadType = (CableThreadType) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (CableThreadType_Transferable) cableThreadType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public LinkType_Transferable transmitLinkType(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitLinkType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			LinkType linkType = (LinkType) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (LinkType_Transferable) linkType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public PortType_Transferable transmitPortType(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.PortType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			PortType portType = (PortType) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (PortType_Transferable) portType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public EquipmentType_Transferable transmitEquipmentType(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.EquipmentType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			EquipmentType equipmentType = (EquipmentType) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (EquipmentType_Transferable) equipmentType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPortType_Transferable transmitMeasurementPortType(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.MeasurementPortType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			MeasurementPortType measurementPortType = (MeasurementPortType) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (MeasurementPortType_Transferable) measurementPortType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TransmissionPathType_Transferable transmitTransmissionPathType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			TransmissionPathType transmissionPathType = (TransmissionPathType) ConfigurationStorableObjectPool.getStorableObject(id,
					true);
			return (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}







	public CableThread_Transferable transmitCableThread(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThread | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			CableThread cableThread = (CableThread) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (CableThread_Transferable) cableThread.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Link_Transferable transmitLink(Identifier_Transferable id_Transferable, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitLink | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Link link = (Link) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (Link_Transferable) link.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Port_Transferable transmitPort(Identifier_Transferable id_Transferable, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.Port | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Port port = (Port) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (Port_Transferable) port.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Equipment_Transferable transmitEquipment(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.Equipment | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Equipment equipment = (Equipment) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (Equipment_Transferable) equipment.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public KIS_Transferable transmitKIS(Identifier_Transferable id_Transferable, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.KIS | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			KIS kis = (KIS) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (KIS_Transferable) kis.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPort_Transferable transmitMeasurementPort(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMConfigurationTransmit.MeasurementPort | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (MeasurementPort_Transferable) measurementPort.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MonitoredElement_Transferable transmitMonitoredElement(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitMonitoredElement | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			MonitoredElement monitoredElement = (MonitoredElement) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (MonitoredElement_Transferable) monitoredElement.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TransmissionPath_Transferable transmitTransmissionPath(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPath | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			TransmissionPath transmissionPath = (TransmissionPath) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (TransmissionPath_Transferable) transmissionPath.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}






	public CableLinkType_Transferable[] transmitCableLinkTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitCableLinkTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
		int i = 0;
		CableLinkType cableLinkType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			cableLinkType = (CableLinkType) it.next();
			transferables[i] = (CableLinkType_Transferable) cableLinkType.getTransferable();
		}
		return transferables;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreadTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
		int i = 0;
		CableThreadType cableThreadType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			cableThreadType = (CableThreadType) it.next();
			transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
		}
		return transferables;
	}

	public AbstractLinkType_Transferable[] transmitLinkTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitLinkTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[objects.size()];
		int i = 0;
		AbstractLinkType abstractLinkType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			abstractLinkType = (AbstractLinkType) it.next();
			transferables[i] = (AbstractLinkType_Transferable) abstractLinkType.getTransferable();
		}
		return transferables;
	}

	public PortType_Transferable[] transmitPortTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitPortTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
		int i = 0;
		PortType portType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			portType = (PortType) it.next();
			transferables[i] = (PortType_Transferable) portType.getTransferable();
		}
		return transferables;
	}

	public EquipmentType_Transferable[] transmitEquipmentTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipmentTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
		int i = 0;
		EquipmentType equipmentType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			equipmentType = (EquipmentType) it.next();
			transferables[i] = (EquipmentType_Transferable) equipmentType.getTransferable();
		}
		return transferables;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
		int i = 0;
		MeasurementPortType measurementPortType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementPortType = (MeasurementPortType) it.next();
			transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
		}
		return transferables;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
		int i = 0;
		TransmissionPathType transmissionPathType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			transmissionPathType = (TransmissionPathType) it.next();
			transferables[i] = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
		}
		return transferables;
	}







	public CableThread_Transferable[] transmitCableThreads(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreads | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
		int i = 0;
		CableThread cableThread;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			cableThread = (CableThread) it.next();
			transferables[i] = (CableThread_Transferable) cableThread.getTransferable();
		}
		return transferables;
	}

	public Link_Transferable[] transmitLinks(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitLinks | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Link_Transferable[] transferables = new Link_Transferable[objects.size()];
		int i = 0;
		Link link;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			link = (Link) it.next();
			transferables[i] = (Link_Transferable) link.getTransferable();
		}
		return transferables;
	}

	public Port_Transferable[] transmitPorts(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitPorts | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		Port port;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			port = (Port) it.next();
			transferables[i] = (Port_Transferable) port.getTransferable();
		}
		return transferables;
	}

	public Equipment_Transferable[] transmitEquipments(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipments | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
		int i = 0;
		Equipment equipment;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			equipment = (Equipment) it.next();
			transferables[i] = (Equipment_Transferable) equipment.getTransferable();
		}
		return transferables;
	}

	public KIS_Transferable[] transmitKISs(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitKISs | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		KIS kis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			kis = (KIS) it.next();
			transferables[i] = (KIS_Transferable) kis.getTransferable();
		}
		return transferables;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPorts(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPorts | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		MeasurementPort measurementPort;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementPort = (MeasurementPort) it.next();
			transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
		}
		return transferables;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMonitoredElements | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		MonitoredElement monitoredElement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			monitoredElement = (MonitoredElement) it.next();
			transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
		}
		return transferables;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPaths(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPaths | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		TransmissionPath transmissionPath;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			transmissionPath = (TransmissionPath) it.next();
			transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
		}
		return transferables;
	}






	public CableThreadType_Transferable[] transmitCableThreadTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreadTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
		int i = 0;
		CableThreadType cableThreadType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			cableThreadType = (CableThreadType) it.next();
			transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
		}
		return transferables;
	}

	public AbstractLinkType_Transferable[] transmitLinkTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitLinkTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[objects.size()];
		int i = 0;
		AbstractLinkType abstractLinkType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			abstractLinkType = (AbstractLinkType) it.next();
			transferables[i] = (AbstractLinkType_Transferable) abstractLinkType.getTransferable();
		}
		return transferables;
	}

	public PortType_Transferable[] transmitPortTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitPortTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
		int i = 0;
		PortType portType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			portType = (PortType) it.next();
			transferables[i] = (PortType_Transferable) portType.getTransferable();
		}
		return transferables;
	}

	public EquipmentType_Transferable[] transmitEquipmentTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipmentTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
		int i = 0;
		EquipmentType equipmentType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			equipmentType = (EquipmentType) it.next();
			transferables[i] = (EquipmentType_Transferable) equipmentType.getTransferable();
		}
		return transferables;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
		int i = 0;
		MeasurementPortType measurementPortType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementPortType = (MeasurementPortType) it.next();
			transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
		}
		return transferables;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
		int i = 0;
		TransmissionPathType transmissionPathType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			transmissionPathType = (TransmissionPathType) it.next();
			transferables[i] = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
		}
		return transferables;
	}







	public Link_Transferable[] transmitLinksButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitLinksButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Link_Transferable[] transferables = new Link_Transferable[objects.size()];
		int i = 0;
		Link link;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			link = (Link) it.next();
			transferables[i] = (Link_Transferable) link.getTransferable();
		}
		return transferables;
	}

	public Port_Transferable[] transmitPortsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitPortsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		Port port;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			port = (Port) it.next();
			transferables[i] = (Port_Transferable) port.getTransferable();
		}
		return transferables;
	}

	public Equipment_Transferable[] transmitEquipmentsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipmentsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
		int i = 0;
		Equipment equipment;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			equipment = (Equipment) it.next();
			transferables[i] = (Equipment_Transferable) equipment.getTransferable();
		}
		return transferables;
	}

	public KIS_Transferable[] transmitKISsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitKISsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		KIS kis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			kis = (KIS) it.next();
			transferables[i] = (KIS_Transferable) kis.getTransferable();
		}
		return transferables;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		MeasurementPort measurementPort;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementPort = (MeasurementPort) it.next();
			transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
		}
		return transferables;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElementsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMonitoredElementsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		MonitoredElement monitoredElement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			monitoredElement = (MonitoredElement) it.next();
			transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
		}
		return transferables;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		TransmissionPath transmissionPath;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			transmissionPath = (TransmissionPath) it.next();
			transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
		}
		return transferables;
	}






	public CableLinkType_Transferable[] transmitCableLinkTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitCableLinkTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
		int i = 0;
		CableLinkType cableLinkType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			cableLinkType = (CableLinkType) it.next();
			transferables[i] = (CableLinkType_Transferable) cableLinkType.getTransferable();
		}
		return transferables;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreadTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
		int i = 0;
		CableThreadType cableThreadType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			cableThreadType = (CableThreadType) it.next();
			transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
		}
		return transferables;
	}






	public CableThread_Transferable[] transmitCableThreadsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreadsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
		int i = 0;
		CableThread cableThread;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			cableThread = (CableThread) it.next();
			transferables[i] = (CableThread_Transferable) cableThread.getTransferable();
		}
		return transferables;
	}

	public Port_Transferable[] transmitPortsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitPortsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		Port port;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			port = (Port) it.next();
			transferables[i] = (Port_Transferable) port.getTransferable();
		}
		return transferables;
	}

	public Equipment_Transferable[] transmitEquipmentsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipmentsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
		int i = 0;
		Equipment equipment;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			equipment = (Equipment) it.next();
			transferables[i] = (Equipment_Transferable) equipment.getTransferable();
		}
		return transferables;
	}

	public KIS_Transferable[] transmitKISsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitKISsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		KIS kis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			kis = (KIS) it.next();
			transferables[i] = (KIS_Transferable) kis.getTransferable();
		}
		return transferables;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		MeasurementPort measurementPort;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementPort = (MeasurementPort) it.next();
			transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
		}
		return transferables;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElementsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitMonitoredElementsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		MonitoredElement monitoredElement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			monitoredElement = (MonitoredElement) it.next();
			transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
		}
		return transferables;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		TransmissionPath transmissionPath;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			transmissionPath = (TransmissionPath) it.next();
			transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
		}
		return transferables;
	}

	private Set getObjectsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		Set ids = Identifier.fromTransferables(identifier_Transferables);

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		Set objects = null;
		try {
			objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		return objects;
	}






	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedConfigurationObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMConfigurationTransmit.transmitRefreshedConfigurationObjects | Refreshing for user '"
				+ accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Map storableObjectsTMap = new HashMap();
			for (int i = 0; i < storableObjects_Transferables.length; i++)
				storableObjectsTMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);

			ConfigurationStorableObjectPool.refresh();
			Set storableObjects = ConfigurationStorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject so = (StorableObject) it.next();
				StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same
				// versions -- not only with older ones!
				if (!so.hasNewerVersion(sot.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable th) {
			Log.errorException(th);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, th.getMessage());
		}
	}

}
