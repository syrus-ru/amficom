/*
 * $Id: CMConfigurationTransmit.java,v 1.20 2005/04/23 13:36:32 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.AccessIdentity_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.20 $, $Date: 2005/04/23 13:36:32 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class CMConfigurationTransmit extends CMAdministrationTransmit {

	private static final long serialVersionUID = 3564457938839553536L;

	public CableLinkType_Transferable transmitCableLinkType(Identifier_Transferable id_Transferable,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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

	public Link_Transferable transmitLink(Identifier_Transferable id_Transferable, AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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

	public Port_Transferable transmitPort(Identifier_Transferable id_Transferable, AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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

	public KIS_Transferable transmitKIS(Identifier_Transferable id_Transferable, AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitCableLinkTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
			int i = 0;
			CableLinkType cableLinkType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				cableLinkType = (CableLinkType) it.next();
				transferables[i] = (CableLinkType_Transferable) cableLinkType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CableThreadType_Transferable[] transmitCableThreadTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreadTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
			int i = 0;
			CableThreadType cableThreadType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				cableThreadType = (CableThreadType) it.next();
				transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public AbstractLinkType_Transferable[] transmitLinkTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitLinkTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[objects.size()];
			int i = 0;
			AbstractLinkType abstractLinkType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				abstractLinkType = (AbstractLinkType) it.next();
				transferables[i] = (AbstractLinkType_Transferable) abstractLinkType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public PortType_Transferable[] transmitPortTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitPortTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
			int i = 0;
			PortType portType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				portType = (PortType) it.next();
				transferables[i] = (PortType_Transferable) portType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public EquipmentType_Transferable[] transmitEquipmentTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipmentTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
			int i = 0;
			EquipmentType equipmentType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				equipmentType = (EquipmentType) it.next();
				transferables[i] = (EquipmentType_Transferable) equipmentType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
			int i = 0;
			MeasurementPortType measurementPortType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				measurementPortType = (MeasurementPortType) it.next();
				transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
			int i = 0;
			TransmissionPathType transmissionPathType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				transmissionPathType = (TransmissionPathType) it.next();
				transferables[i] = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}







	public CableThread_Transferable[] transmitCableThreads(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreads | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
			int i = 0;
			CableThread cableThread;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				cableThread = (CableThread) it.next();
				transferables[i] = (CableThread_Transferable) cableThread.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Link_Transferable[] transmitLinks(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitLinks | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		
		try {
			Set objects = this.getObjects(identifier_Transferables);

			Link_Transferable[] transferables = new Link_Transferable[objects.size()];
			int i = 0;
			Link link;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				link = (Link) it.next();
				transferables[i] = (Link_Transferable) link.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Port_Transferable[] transmitPorts(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitPorts | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			Port_Transferable[] transferables = new Port_Transferable[objects.size()];
			int i = 0;
			Port port;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				port = (Port) it.next();
				transferables[i] = (Port_Transferable) port.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Equipment_Transferable[] transmitEquipments(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipments | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
			int i = 0;
			Equipment equipment;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				equipment = (Equipment) it.next();
				transferables[i] = (Equipment_Transferable) equipment.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public KIS_Transferable[] transmitKISs(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitKISs | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
			int i = 0;
			KIS kis;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				kis = (KIS) it.next();
				transferables[i] = (KIS_Transferable) kis.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPort_Transferable[] transmitMeasurementPorts(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPorts | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
			int i = 0;
			MeasurementPort measurementPort;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				measurementPort = (MeasurementPort) it.next();
				transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitMonitoredElements | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
			int i = 0;
			MonitoredElement monitoredElement;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				monitoredElement = (MonitoredElement) it.next();
				transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TransmissionPath_Transferable[] transmitTransmissionPaths(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPaths | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
			int i = 0;
			TransmissionPath transmissionPath;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				transmissionPath = (TransmissionPath) it.next();
				transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	private Set getObjects(Identifier_Transferable[] identifier_Transferables) throws AMFICOMRemoteException {
		try {
			Set ids = Identifier.fromTransferables(identifier_Transferables);
			Set objects = ConfigurationStorableObjectPool.getStorableObjects(ids, true);
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






	public CableThreadType_Transferable[] transmitCableThreadTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitCableThreadTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.CABLETHREADTYPE_ENTITY_CODE);

			CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
			int i = 0;
			CableThreadType cableThreadType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				cableThreadType = (CableThreadType) it.next();
				transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public AbstractLinkType_Transferable[] transmitLinkTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitLinkTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.LINKTYPE_ENTITY_CODE);

			AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[objects.size()];
			int i = 0;
			AbstractLinkType abstractLinkType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				abstractLinkType = (AbstractLinkType) it.next();
				transferables[i] = (AbstractLinkType_Transferable) abstractLinkType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public PortType_Transferable[] transmitPortTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitPortTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.PORTTYPE_ENTITY_CODE);

			PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
			int i = 0;
			PortType portType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				portType = (PortType) it.next();
				transferables[i] = (PortType_Transferable) portType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public EquipmentType_Transferable[] transmitEquipmentTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipmentTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);

			EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
			int i = 0;
			EquipmentType equipmentType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				equipmentType = (EquipmentType) it.next();
				transferables[i] = (EquipmentType_Transferable) equipmentType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables,
				ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);

			MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
			int i = 0;
			MeasurementPortType measurementPortType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				measurementPortType = (MeasurementPortType) it.next();
				transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
		
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);

			TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
			int i = 0;
			TransmissionPathType transmissionPathType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				transmissionPathType = (TransmissionPathType) it.next();
				transferables[i] = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}







	public Link_Transferable[] transmitLinksButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitLinksButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.LINK_ENTITY_CODE);

			Link_Transferable[] transferables = new Link_Transferable[objects.size()];
			int i = 0;
			Link link;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				link = (Link) it.next();
				transferables[i] = (Link_Transferable) link.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Port_Transferable[] transmitPortsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitPortsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.PORT_ENTITY_CODE);

			Port_Transferable[] transferables = new Port_Transferable[objects.size()];
			int i = 0;
			Port port;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				port = (Port) it.next();
				transferables[i] = (Port_Transferable) port.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Equipment_Transferable[] transmitEquipmentsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitEquipmentsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.EQUIPMENT_ENTITY_CODE);

			Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
			int i = 0;
			Equipment equipment;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				equipment = (Equipment) it.next();
				transferables[i] = (Equipment_Transferable) equipment.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public KIS_Transferable[] transmitKISsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitKISsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.KIS_ENTITY_CODE);

			KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
			int i = 0;
			KIS kis;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				kis = (KIS) it.next();
				transferables[i] = (KIS_Transferable) kis.getTransferable();
			}
			return transferables;

		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);

			MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
			int i = 0;
			MeasurementPort measurementPort;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				measurementPort = (MeasurementPort) it.next();
				transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MonitoredElement_Transferable[] transmitMonitoredElementsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitMonitoredElementsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.ME_ENTITY_CODE);

			MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
			int i = 0;
			MonitoredElement monitoredElement;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				monitoredElement = (MonitoredElement) it.next();
				transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.TRANSPATH_ENTITY_CODE);

			TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
			int i = 0;
			TransmissionPath transmissionPath;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				transmissionPath = (TransmissionPath) it.next();
				transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	private Set getObjectsButIds(Identifier_Transferable[] identifier_Transferables, short entityCode) throws AMFICOMRemoteException {
		try {
			Set ids = Identifier.fromTransferables(identifier_Transferables);
			Set objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(entityCode),
					true);
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







	public EquipmentType_Transferable[] transmitEquipmentTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitEquipmentTypesButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
			int i = 0;
			EquipmentType equipmentType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				equipmentType = (EquipmentType) it.next();
				transferables[i] = (EquipmentType_Transferable) equipmentType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public PortType_Transferable[] transmitPortTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitPortTypesButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
			int i = 0;
			PortType portType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				portType = (PortType) it.next();
				transferables[i] = (PortType_Transferable) portType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortTypesButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
			int i = 0;
			MeasurementPortType measurementPortType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				measurementPortType = (MeasurementPortType) it.next();
				transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathTypesButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
			int i = 0;
			TransmissionPathType transmissionPathType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				transmissionPathType = (TransmissionPathType) it.next();
				transferables[i] = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public AbstractLinkType_Transferable[] transmitLinkTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitLinkTypesButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[objects.size()];
			int i = 0;
			AbstractLinkType abstractLinkType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				abstractLinkType = (AbstractLinkType) it.next();
				transferables[i] = (AbstractLinkType_Transferable) abstractLinkType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CableLinkType_Transferable[] transmitCableLinkTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitCableLinkTypesButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
			int i = 0;
			CableLinkType cableLinkType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				cableLinkType = (CableLinkType) it.next();
				transferables[i] = (CableLinkType_Transferable) cableLinkType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CableThreadType_Transferable[] transmitCableThreadTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitCableThreadTypesButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
			int i = 0;
			CableThreadType cableThreadType;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				cableThreadType = (CableThreadType) it.next();
				transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}






	public Link_Transferable[] transmitLinksButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitLinksButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			Link_Transferable[] transferables = new Link_Transferable[objects.size()];
			int i = 0;
			Link link;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				link = (Link) it.next();
				transferables[i] = (Link_Transferable) link.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CableThread_Transferable[] transmitCableThreadsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitCableThreadsButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
			int i = 0;
			CableThread cableThread;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				cableThread = (CableThread) it.next();
				transferables[i] = (CableThread_Transferable) cableThread.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Port_Transferable[] transmitPortsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitPortsButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			Port_Transferable[] transferables = new Port_Transferable[objects.size()];
			int i = 0;
			Port port;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				port = (Port) it.next();
				transferables[i] = (Port_Transferable) port.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Equipment_Transferable[] transmitEquipmentsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitEquipmentsButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
			int i = 0;
			Equipment equipment;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				equipment = (Equipment) it.next();
				transferables[i] = (Equipment_Transferable) equipment.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public KIS_Transferable[] transmitKISsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitKISsButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
			int i = 0;
			KIS kis;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				kis = (KIS) it.next();
				transferables[i] = (KIS_Transferable) kis.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitMeasurementPortsButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
			int i = 0;
			MeasurementPort measurementPort;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				measurementPort = (MeasurementPort) it.next();
				transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MonitoredElement_Transferable[] transmitMonitoredElementsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitMonitoredElementsButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
			int i = 0;
			MonitoredElement monitoredElement;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				monitoredElement = (MonitoredElement) it.next();
				transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		try {
			AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
			Log.debugMessage("CMConfigurationTransmit.transmitTransmissionPathsButIdsCondition | All, but "
					+ identifier_Transferables.length + " item(s) for '" + accessIdentity.getUserId() + "'",
				Log.DEBUGLEVEL07);

			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
			int i = 0;
			TransmissionPath transmissionPath;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				transmissionPath = (TransmissionPath) it.next();
				transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	private Set getObjectsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		try {
			Set ids = Identifier.fromTransferables(identifier_Transferables);
			Set objects = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
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






	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedConfigurationObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
