/*
 * $Id: CMServerImpl.java,v 1.70 2004/11/24 08:47:56 max Exp $
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

import com.syrus.AMFICOM.configuration.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
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
import com.syrus.AMFICOM.configuration.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.StringFieldSort;
import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.configuration.corba.User_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultCondition;
import com.syrus.AMFICOM.measurement.ResultSortCondition;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.StringFieldCondition;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TemporalCondition;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;

import com.syrus.AMFICOM.measurement.corba.MeasurementSetupCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSortCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;

import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.70 $, $Date: 2004/11/24 08:47:56 $
 * @author $Author: max $
 * @module cmserver_v1
 */

public class CMServerImpl extends CMConfigurationMeasurementReceive {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 4048793468187259186L;

	private DomainCondition domainCondition;

    private MServer mServer;
    //////////////////////////////////Name Resolver/////////////////////////////////////////////////

    
	public String lookupDomainName(Identifier_Transferable idTransferable)
		throws AMFICOMRemoteException {
		try {
			Identifier id = new Identifier(idTransferable);
			return ( (Domain)ConfigurationStorableObjectPool.getStorableObject(id, true) ).getName();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public String lookupUserLogin(Identifier_Transferable identifier_Transferable)
            throws AMFICOMRemoteException {
        try {
            Identifier id = new Identifier(identifier_Transferable);
            return ( (User)ConfigurationStorableObjectPool.getStorableObject(id, true) ).getLogin();
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public String lookupUserName(Identifier_Transferable identifier_Transferable)
            throws AMFICOMRemoteException {
        try {
            Identifier id = new Identifier(identifier_Transferable);
            return ( (User)ConfigurationStorableObjectPool.getStorableObject(id, true) ).getName();
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Identifier_Transferable reverseLookupDomainName(String domainName)
            throws AMFICOMRemoteException {
        try {
        List list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new StringFieldCondition(domainName, ObjectEntities.DOMAIN_ENTITY_CODE), true);
        if (list.isEmpty())
        	throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, "list is empty");
        Identifier id = ( (Domain)list.get(0) ).getId();
        return (Identifier_Transferable)id.getTransferable();
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Identifier_Transferable reverseLookupUserLogin(String userLogin)
            throws AMFICOMRemoteException {
        try {
        	StringFieldCondition stringFieldCondition = StringFieldCondition.getInstance();
			stringFieldCondition.setEntityCode(ObjectEntities.USER_ENTITY_CODE);
			stringFieldCondition.setString(userLogin);
			stringFieldCondition.setSort(StringFieldSort.STRINGSORT_USERLOGIN);
            List list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(stringFieldCondition, true);
            Identifier  id = ( (User)list.get(0) ).getId();
            return (Identifier_Transferable)id.getTransferable();
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public Identifier_Transferable reverseLookupUserName(final String userName)
			throws AMFICOMRemoteException {
		try {
			StringFieldCondition stringFieldCondition = StringFieldCondition.getInstance();
			stringFieldCondition.setEntityCode(ObjectEntities.USER_ENTITY_CODE);
			stringFieldCondition.setString(userName);
			stringFieldCondition.setSort(StringFieldSort.STRINGSORT_USERNAME);
			List list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(stringFieldCondition, true);
			Identifier  id = ( (User)list.get(0) ).getId();
			return (Identifier_Transferable)id.getTransferable();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
				.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	///////////// Configuration Transmit /////////////
    
	public CableThreadType_Transferable transmitCableThreadType(
			Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.transmitCableThreadType | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            CableThreadType cableThreadType = (CableThreadType) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (CableThreadType_Transferable) cableThreadType.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public Characteristic_Transferable transmitCharacteristic(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.transmitCharacteristic | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Characteristic characteristic = (Characteristic) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (Characteristic_Transferable) characteristic.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public CharacteristicType_Transferable transmitCharacteristicType(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.CharacteristicType | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            CharacteristicType characteristicType = (CharacteristicType) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (CharacteristicType_Transferable) characteristicType.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Equipment_Transferable transmitEquipment(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.Equipment | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Equipment equipment = (Equipment) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (Equipment_Transferable) equipment.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public EquipmentType_Transferable transmitEquipmentType(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.EquipmentType | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            EquipmentType equipmentType = (EquipmentType) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (EquipmentType_Transferable) equipmentType.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public KIS_Transferable transmitKIS(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.KIS | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            KIS kis = (KIS) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (KIS_Transferable) kis.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public MCM_Transferable transmitMCM(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.MCM | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            MCM mcm = (MCM) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (MCM_Transferable) mcm.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementPort_Transferable transmitMeasurementPort(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.MeasurementPort | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (MeasurementPort_Transferable) measurementPort.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementPortType_Transferable transmitMeasurementPortType(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.MeasurementPortType | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            MeasurementPortType measurementPortType = (MeasurementPortType) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (MeasurementPortType_Transferable) measurementPortType.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Port_Transferable transmitPort(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.Port | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Port port = (Port) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (Port_Transferable) port.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public PortType_Transferable transmitPortType(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.PortType | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            PortType portType = (PortType) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (PortType_Transferable) portType.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Server_Transferable transmitServer(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.Server | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Server server = (Server) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (Server_Transferable) server.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public User_Transferable transmitUser(
            Identifier_Transferable id_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.User | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            User user = (User) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (User_Transferable) user.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }



    public Domain_Transferable transmitDomain(	Identifier_Transferable identifier_Transferable,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitDomain | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (Domain_Transferable) domain.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MonitoredElement_Transferable transmitMonitoredElement(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMonitoredElement | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MonitoredElement monitoredElement = (MonitoredElement) ConfigurationStorableObjectPool
					.getStorableObject(id, true);
			return (MonitoredElement_Transferable) monitoredElement.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public TransmissionPath_Transferable transmitTransmissionPath(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitTransmissionPath | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			TransmissionPath transmissionPath = (TransmissionPath) ConfigurationStorableObjectPool
					.getStorableObject(id, true);
			return (TransmissionPath_Transferable) transmissionPath.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public TransmissionPathType_Transferable transmitTransmissionPathType(  Identifier_Transferable identifier_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
        throws AMFICOMRemoteException {
        Identifier id = new Identifier(identifier_Transferable);
        Log.debugMessage("CMServerImpl.transmitTransmissionPathType | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            TransmissionPathType transmissionPathType = (TransmissionPathType) ConfigurationStorableObjectPool
                    .getStorableObject(id, true);
        return (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
        } catch (ObjectNotFoundException onfe) {
        Log.errorException(onfe);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
        Log.errorException(roe);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
        .getMessage());
        } catch (CommunicationException ce) {
        Log.errorException(ce);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
        .getMessage());
        } catch (DatabaseException de) {
        Log.errorException(de);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
        .getMessage());
        } catch (Throwable t) {
        Log.errorException(t);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public CableThreadType_Transferable[] transmitCableThreadTypes(
            Identifier_Transferable[] ids_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitCableThreadTypes | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.CABLETHREADTYPE_ENTITY_CODE), true);

            CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                CableThreadType cableThreadType = (CableThreadType) it.next();
                transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public CableThreadType_Transferable[] transmitCableThreadTypesButIds(
			Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitCableThreadTypesButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.CABLETHREADTYPE_ENTITY_CODE), true);

            CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                CableThreadType cableThreadType = (CableThreadType) it.next();
                transferables[i] = (CableThreadType_Transferable) cableThreadType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public Characteristic_Transferable[] transmitCharacteristics(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitCharacteristics | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);

            Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Characteristic characteristic = (Characteristic) it.next();
                transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Characteristic_Transferable[] transmitCharacteristicsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitCharacteristicsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);

            Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Characteristic characteristic = (Characteristic) it.next();
                transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
	public Characteristic_Transferable[] transmitCharacteristicsButIdsCondition(
			Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
			throws AMFICOMRemoteException {
		try {
            Log.debugMessage("CMServerImpl.transmitCharacteristicsButIdsCondition | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) ", Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new LinkedIdsCondition(linkedIdsCondition_Transferable), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Characteristic characteristic = (Characteristic) it.next();
                transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public CharacteristicType_Transferable[] transmitCharacteristicTypes(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitCharacteristicTypes | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE), true);

            CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                CharacteristicType characteristicType = (CharacteristicType) it.next();
                transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public CharacteristicType_Transferable[] transmitCharacteristicTypesButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitCharacteristicTypes | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.CHARACTERISTIC_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE), true);

            CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                CharacteristicType characteristicType = (CharacteristicType) it.next();
                transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Domain_Transferable[] transmitDomainsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitDomainsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.DOMAIN_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.DOMAIN_ENTITY_CODE), true);

            Domain_Transferable[] transferables = new Domain_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Domain domain2 = (Domain) it.next();
                transferables[i] = (Domain_Transferable) domain2.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Domain_Transferable[] transmitDomainsButIdsCondition(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier, DomainCondition_Transferable domainCondition) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitDomainsButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new DomainCondition(domainCondition), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition) , true);

            Domain_Transferable[] transferables = new Domain_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Domain domain2 = (Domain) it.next();
                transferables[i] = (Domain_Transferable) domain2.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Equipment_Transferable[] transmitEquipments(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitEquipments | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EQUIPMENT_ENTITY_CODE), true);

            Equipment_Transferable[] transferables = new Equipment_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Equipment equipment = (Equipment) it.next();
                transferables[i] = (Equipment_Transferable) equipment.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Equipment_Transferable[] transmitEquipmentsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitEquipmentsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EQUIPMENT_ENTITY_CODE), true);

            Equipment_Transferable[] transferables = new Equipment_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Equipment equipment = (Equipment) it.next();
                transferables[i] = (Equipment_Transferable) equipment.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Equipment_Transferable[] transmitEquipmentsButIdsCondition(
            Identifier_Transferable[] ids_Transferable,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitEquipmentsButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new DomainCondition(domainCondition), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition), true);

            Equipment_Transferable[] transferables = new Equipment_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Equipment equipment = (Equipment) it.next();
                transferables[i] = (Equipment_Transferable) equipment.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public EquipmentType_Transferable[] transmitEquipmentTypes(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitEquipmentTypes | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE), true);

            EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                EquipmentType equipmentType = (EquipmentType) it.next();
                transferables[i] = (EquipmentType_Transferable) equipmentType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public EquipmentType_Transferable[] transmitEquipmentTypesButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitEquipmentTypesButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSIS_ENTITY_CODE), true);

            EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                EquipmentType equipmentType = (EquipmentType) it.next();
                transferables[i] = (EquipmentType_Transferable) equipmentType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public KIS_Transferable[] transmitKISs(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitKISs | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);

            KIS_Transferable[] transferables = new KIS_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                KIS kis = (KIS) it.next();
                transferables[i] = (KIS_Transferable) kis.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public KIS_Transferable[] transmitKISsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitKISsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);

            KIS_Transferable[] transferables = new KIS_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                KIS kis = (KIS) it.next();
                transferables[i] = (KIS_Transferable) kis.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public KIS_Transferable[] transmitKISsButIdsCondition(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier, DomainCondition_Transferable domainCondition) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitKISsButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new DomainCondition(domainCondition),  true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition), true);

            KIS_Transferable[] transferables = new KIS_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                KIS kis = (KIS) it.next();
                transferables[i] = (KIS_Transferable) kis.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public Link_Transferable transmitLink(
			Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.transmitLink | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Link link = (Link) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (Link_Transferable) link.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Link_Transferable[] transmitLinks(
			Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitLinks | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);

            Link_Transferable[] transferables = new Link_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Link link = (Link) it.next();
                transferables[i] = (Link_Transferable) link.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Link_Transferable[] transmitLinksButIds(
			Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitLinksButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);

            Link_Transferable[] transferables = new Link_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Link link = (Link) it.next();
                transferables[i] = (Link_Transferable) link.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public LinkType_Transferable transmitLinkType(
			Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("CMServerImpl.transmitLinkType | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            LinkType linkType = (LinkType) ConfigurationStorableObjectPool.getStorableObject(id, true);
            return (LinkType_Transferable) linkType.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public AbstractLinkType_Transferable[] transmitLinkTypes(
			Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitLinks | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);

            AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                AbstractLinkType linkType = (AbstractLinkType) it.next();
                transferables[i] = (AbstractLinkType_Transferable) linkType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public AbstractLinkType_Transferable[] transmitLinkTypesButIds(
			Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitLinksButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.KIS_ENTITY_CODE), true);

            AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
            	AbstractLinkType linkType = (AbstractLinkType) it.next();
                transferables[i] = (AbstractLinkType_Transferable) linkType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public MCM_Transferable[] transmitMCMs(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMCMs | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MCM_ENTITY_CODE), true);

            MCM_Transferable[] transferables = new MCM_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MCM mcm = (MCM) it.next();
                transferables[i] = (MCM_Transferable) mcm.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MCM_Transferable[] transmitMCMsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMCMsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.MCM_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MCM_ENTITY_CODE), true);

            MCM_Transferable[] transferables = new MCM_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MCM mcm = (MCM) it.next();
                transferables[i] = (MCM_Transferable) mcm.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MCM_Transferable[] transmitMCMsButIdsCondition(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier, DomainCondition_Transferable domainCondition) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitMCMsButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new DomainCondition(domainCondition), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition),  true);

            MCM_Transferable[] transferables = new MCM_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MCM mcm = (MCM) it.next();
                transferables[i] = (MCM_Transferable) mcm.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementPort_Transferable[] transmitMeasurementPorts(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementPorts | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE), true);

            MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementPort measurementPort = (MeasurementPort) it.next();
                transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    public MeasurementPort_Transferable[] transmitMeasurementPortsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementPortsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE), true);

            MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementPort measurementPort = (MeasurementPort) it.next();
                transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsCondition(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier, DomainCondition_Transferable domainCondition) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitMeasurementPortsButIds | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new DomainCondition(domainCondition),  true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition), true);

            MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementPort measurementPort = (MeasurementPort) it.next();
                transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementPortTypes | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE), true);

            MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementPortType measurementPortType = (MeasurementPortType) it.next();
                transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementPortType_Transferable[] transmitMeasurementPortTypesButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementPortTypesButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE), true);

            MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementPortType measurementPortType = (MeasurementPortType) it.next();
                transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MonitoredElement_Transferable[] transmitMonitoredElementsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMonitoredElementsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ME_ENTITY_CODE), true);

            MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MonitoredElement monitoredElement = (MonitoredElement) it.next();
                transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MonitoredElement_Transferable[] transmitMonitoredElementsButIdsCondition(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier, DomainCondition_Transferable domainCondition) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitMonitoredElementsButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition), true);

            MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MonitoredElement monitoredElement = (MonitoredElement) it.next();
                transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Port_Transferable[] transmitPorts(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitPorts | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PORT_ENTITY_CODE), true);

            Port_Transferable[] transferables = new Port_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Port port = (Port) it.next();
                transferables[i] = (Port_Transferable) port.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Port_Transferable[] transmitPortsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitPortsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.PORT_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PORT_ENTITY_CODE), true);

            Port_Transferable[] transferables = new Port_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Port port = (Port) it.next();
                transferables[i] = (Port_Transferable) port.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Port_Transferable[] transmitPortsButIdsCondition(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier, DomainCondition_Transferable domainCondition) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitPortsButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds( idsList,new DomainCondition(domainCondition), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition), true);

            Port_Transferable[] transferables = new Port_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Port port = (Port) it.next();
                transferables[i] = (Port_Transferable) port.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public PortType_Transferable[] transmitPortTypes(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitPortTypes | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PORTTYPE_ENTITY_CODE), true);

            PortType_Transferable[] transferables = new PortType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                PortType portType = (PortType) it.next();
                transferables[i] = (PortType_Transferable) portType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public PortType_Transferable[] transmitPortTypesButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitPortTypesButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.PORTTYPE_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PORTTYPE_ENTITY_CODE), true);

            PortType_Transferable[] transferables = new PortType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                PortType portType = (PortType) it.next();
                transferables[i] = (PortType_Transferable) portType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Server_Transferable[] transmitServers(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitServers | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.SERVER_ENTITY_CODE), true);

            Server_Transferable[] transferables = new Server_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Server server = (Server) it.next();
                transferables[i] = (Server_Transferable) server.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Server_Transferable[] transmitServersButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitServersButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList , getDomainCondition(domain, ObjectEntities.SERVER_ENTITY_CODE), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.SERVER_ENTITY_CODE), true);

            Server_Transferable[] transferables = new Server_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Server server = (Server) it.next();
                transferables[i] = (Server_Transferable) server.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Server_Transferable[] transmitServersButIdsCondition(
            Identifier_Transferable[] ids_Transferable,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitServersButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s)", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList , new DomainCondition(domainCondition), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition), true);

            Server_Transferable[] transferables = new Server_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Server server = (Server) it.next();
                transferables[i] = (Server_Transferable) server.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public TransmissionPath_Transferable[] transmitTransmissionPathsButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitTransmissionPathsButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TRANSPATH_ENTITY_CODE), true);

            TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                TransmissionPath transmissionPath = (TransmissionPath) it.next();
                transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public TransmissionPathType_Transferable[] transmitTransmissionPathTypesButIds(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitTransmissionPathTypesButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TRANSPATHTYPE_ENTITY_CODE), true);

            TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                TransmissionPathType transmissionPathType = (TransmissionPathType) it.next();
                transferables[i] = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsCondition(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier, DomainCondition_Transferable domainCondition_Transferable) throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitTransmissionPathsButIdsCondition | requiere "
                + (ids_Transferable.length == 0 ? "all" : Integer
                        .toString(ids_Transferable.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new DomainCondition(domainCondition_Transferable), true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                TransmissionPath transmissionPath = (TransmissionPath) it.next();
                transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public User_Transferable[] transmitUsers(Identifier_Transferable[] ids_Transferable, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitUsers | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.USER_ENTITY_CODE), true);

            User_Transferable[] transferables = new User_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                User user = (User) it.next();
                transferables[i] = (User_Transferable) user.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
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
												   StringFieldCondition_Transferable stringFieldCondition_Transferable) throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitUsersButIds | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            StringFieldCondition stringFieldCondition = new StringFieldCondition(stringFieldCondition_Transferable);
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = ConfigurationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, stringFieldCondition, true);
            } else{
            	
                list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(stringFieldCondition, true);
            }

            User_Transferable[] transferables = new User_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                User user = (User) it.next();
                transferables[i] = (User_Transferable) user.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	///////////// Measurement Transmit /////////////
	public AnalysisType_Transferable transmitAnalysisType(	Identifier_Transferable identifier_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitAnalysisType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			AnalysisType analysisType = (AnalysisType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (AnalysisType_Transferable) analysisType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public Evaluation_Transferable transmitEvaluation(
            Identifier_Transferable identifier_Transferable,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(identifier_Transferable);
        Log.debugMessage("CMServerImpl.transmitEvaluation | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Evaluation evaluation = (Evaluation) MeasurementStorableObjectPool
                    .getStorableObject(id, true);
            return (Evaluation_Transferable) evaluation.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Evaluation_Transferable[] transmitEvaluations(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitEvaluations | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EVALUATION_ENTITY_CODE), true);

            Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Evaluation evaluation = (Evaluation) it.next();
                transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Evaluation_Transferable[] transmitEvaluationsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitEvaluationsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.EVALUATION_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EVALUATION_ENTITY_CODE), true);

            Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Evaluation evaluation = (Evaluation) it.next();
                transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Evaluation_Transferable[] transmitEvaluationsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitEvaluationsButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Evaluation evaluation = (Evaluation) it.next();
                transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public EvaluationType_Transferable transmitEvaluationType(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitEvaluationType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			EvaluationType evaluationType = (EvaluationType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (EvaluationType_Transferable) evaluationType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}



	public MeasurementType_Transferable transmitMeasurementType(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMeasurementType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (MeasurementType_Transferable) measurementType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public ParameterType_Transferable transmitParameterType(Identifier_Transferable identifier_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitParameterType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			ParameterType parameterType = (ParameterType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (ParameterType_Transferable) parameterType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Analysis_Transferable transmitAnalysis(	Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
        Identifier id = new Identifier(identifier_Transferable);
        Log.debugMessage("CMServerImpl.transmitAnalysis | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Analysis analysis = (Analysis) MeasurementStorableObjectPool
                    .getStorableObject(id, true);
            return (Analysis_Transferable) analysis.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Modeling_Transferable transmitModeling(	Identifier_Transferable identifier_Transferable,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitModeling | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Modeling modeling = (Modeling) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Modeling_Transferable) modeling.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Measurement_Transferable transmitMeasurement(	Identifier_Transferable identifier_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMeasurement | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Measurement measurement = (Measurement) MeasurementStorableObjectPool.getStorableObject(id,
														true);
			return (Measurement_Transferable) measurement.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MeasurementSetup_Transferable transmitMeasurementSetup(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMeasurementSetup | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Result_Transferable transmitResult(	Identifier_Transferable identifier_Transferable,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitResult | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Result result = (Result) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Result_Transferable) result.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Set_Transferable transmitSet(	Identifier_Transferable identifier_Transferable,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitSet | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Set set = (Set) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Set_Transferable) set.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public TemporalPattern_Transferable transmitTemporalPattern(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitTest | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			TemporalPattern temporalPattern = (TemporalPattern) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (TemporalPattern_Transferable) temporalPattern.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Test_Transferable transmitTest(	Identifier_Transferable identifier_Transferable,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitTest | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Test test = (Test) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Test_Transferable) test.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	///////////// Configuration Transmit /////////////

	public Domain_Transferable[] transmitDomains(	Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList();

		Log.debugMessage("CMServerImpl.transmitDomains | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer
						.toString(identifier_Transferables.length)) + " item(s)",
					Log.DEBUGLEVEL07);
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			List domainList = null;
			if (identifier_Transferables.length > 0) {
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				domainList = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			}

			if (domainList == null)
				domainList = ConfigurationStorableObjectPool
				.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.DOMAIN_ENTITY_CODE), true);

			int i = 0;
			Domain_Transferable[] transferables = new Domain_Transferable[domainList.size()];
			for (Iterator it = domainList.iterator(); it.hasNext(); i++) {
				Domain domain2 = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain2.getTransferable();

			}

			return transferables;
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(	Identifier_Transferable[] identifier_Transferables,
										AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			Log.debugMessage("CMServerImpl.transmitMonitoredElements | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length)) + " item(s)",
						Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = ConfigurationStorableObjectPool
						.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ME_ENTITY_CODE), true);


			MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MonitoredElement monitoredElement = (MonitoredElement) it.next();
				transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
			}

			return transferables;

		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public TransmissionPath_Transferable[] transmitTransmissionPaths(	Identifier_Transferable[] identifier_Transferables,
										AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			Log.debugMessage("CMServerImpl.transmitTransmissionPaths | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length)) + " item(s)",
						Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = list = ConfigurationStorableObjectPool
					.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TRANSPATH_ENTITY_CODE), true);

			TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				TransmissionPath transmissionPath = (TransmissionPath) it.next();
				transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
			}

			return transferables;

		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public TransmissionPathType_Transferable[] transmitTransmissionPathTypes(   Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            
            Log.debugMessage("CMServerImpl.transmitTransmissionPathTypes | requiere "
            + (identifier_Transferables.length == 0 ? "all" : Integer
            .toString(identifier_Transferables.length)) + " item(s)",
            Log.DEBUGLEVEL07);
            
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                	idsList.add(new Identifier(identifier_Transferables[i]));
                
                list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            } else
            	list = list = ConfigurationStorableObjectPool
				    .getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TRANSPATHTYPE_ENTITY_CODE), true);
            
            TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[list.size()];
            
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                TransmissionPathType transmissionPathType = (TransmissionPathType) it.next();
                transferables[i] = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
            }
            
            return transferables;
            
        } catch (ObjectNotFoundException onfe) {
        Log.errorException(onfe);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
            onfe.getMessage());
        } catch (RetrieveObjectException roe) {
        Log.errorException(roe);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
        .getMessage());
        } catch (CommunicationException ce) {
        Log.errorException(ce);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
        .getMessage());
        } catch (DatabaseException de) {
        Log.errorException(de);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
        .getMessage());
        } catch (IllegalDataException ide) {
        Log.errorException(ide);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
        .getMessage());
        } catch (IllegalObjectEntityException ioee) {
        Log.errorException(ioee);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
        .getMessage());
        } catch (ApplicationException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
        Log.errorException(t);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public AnalysisType_Transferable[] transmitAnalysisTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitAnalysisTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSISTYPE_ENTITY_CODE), true);


			AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				AnalysisType analysisType = (AnalysisType) it.next();
				transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

	}

    public AnalysisType_Transferable[] transmitAnalysisTypesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitAnalysisTypesButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.ANALYSISTYPE_ENTITY_CODE), true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSISTYPE_ENTITY_CODE), true);


            AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];

            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                AnalysisType analysisType = (AnalysisType) it.next();
                transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public AnalysisType_Transferable[] transmitAnalysisTypesButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitAnalysisTypesButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new LinkedIdsCondition(linkedIdsCondition_Transferable), true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);


            AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];

            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                AnalysisType analysisType = (AnalysisType) it.next();
                transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public EvaluationType_Transferable[] transmitEvaluationTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitEvaluationTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE), true);

			EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				EvaluationType evaluationType = (EvaluationType) it.next();
				transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

	}

    public EvaluationType_Transferable[] transmitEvaluationTypesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitEvaluationTypesButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE), true);

            EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                EvaluationType evaluationType = (EvaluationType) it.next();
                transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public EvaluationType_Transferable[] transmitEvaluationTypesButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitEvaluationTypesButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
            try {
                List list;
                if (identifier_Transferables.length > 0) {
                    List idsList = new ArrayList(identifier_Transferables.length);
                    for (int i = 0; i < identifier_Transferables.length; i++)
                        idsList.add(new Identifier(identifier_Transferables[i]));

                    list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

                } else
                    list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

                EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    EvaluationType evaluationType = (EvaluationType) it.next();
                    transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
                }

                return transferables;

            } catch (RetrieveObjectException roe) {
                Log.errorException(roe);
                throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                        .getMessage());
            } catch (IllegalDataException ide) {
                Log.errorException(ide);
                throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                        .getMessage());
            } catch (IllegalObjectEntityException ioee) {
                Log.errorException(ioee);
                throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                        .getMessage());
            } catch (ApplicationException e) {
                Log.errorException(e);
                throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
            } catch (Throwable t) {
                Log.errorException(t);
                throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
            }
    }

	public MeasurementType_Transferable[] transmitMeasurementTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitMeasurementTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

			MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementType measurementType = (MeasurementType) it.next();
				transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public MeasurementType_Transferable[] transmitMeasurementTypesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementTypesButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

            MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementType measurementType = (MeasurementType) it.next();
                transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsLinkedIdsCondition(
			Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
			throws AMFICOMRemoteException {
		try {
            Log.debugMessage("CMServerImpl.transmitMeasurementTypesButIdsLinkedIdsCondition | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) ", Log.DEBUGLEVEL07);
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementType measurementType = (MeasurementType) it.next();
                transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsStringFieldCondition(
			Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StringFieldCondition_Transferable stringFieldCondition_Transferable)
			throws AMFICOMRemoteException {
		try {
            Log.debugMessage("CMServerImpl.transmitMeasurementTypesButIdsStringFieldCondition | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) ", Log.DEBUGLEVEL07);
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new StringFieldCondition(stringFieldCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new StringFieldCondition(stringFieldCondition_Transferable), true);

            MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementType measurementType = (MeasurementType) it.next();
                transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
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
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitParameterTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

			ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				ParameterType parameterType = (ParameterType) it.next();
				transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public ParameterType_Transferable[] transmitParameterTypesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitParameterTypesButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

            ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                ParameterType parameterType = (ParameterType) it.next();
                transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public ParameterType_Transferable[] transmitParameterTypesButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            StringFieldCondition_Transferable stringFieldCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitParameterTypesButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new StringFieldCondition(stringFieldCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new StringFieldCondition(stringFieldCondition_Transferable), true);

            ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                ParameterType parameterType = (ParameterType) it.next();
                transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Analysis_Transferable[] transmitAnalyses(Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {

        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitAnalyses | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSIS_ENTITY_CODE), true);


            Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];

            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Analysis analysis = (Analysis) it.next();
                transferables[i] = (Analysis_Transferable) analysis.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public Analysis_Transferable[] transmitAnalysesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitAnalysesButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.ANALYSIS_ENTITY_CODE), true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSIS_ENTITY_CODE), true);

            Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Analysis analysis = (Analysis) it.next();
                transferables[i] = (Analysis_Transferable) analysis.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Analysis_Transferable[] transmitAnalysesButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitAnalysesButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Analysis analysis = (Analysis) it.next();
                transferables[i] = (Analysis_Transferable) analysis.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public Modeling_Transferable[] transmitModelings(	Identifier_Transferable[] identifier_Transferables,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitModelings | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MODELING_ENTITY_CODE), true);

			Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Modeling modeling = (Modeling) it.next();
				transferables[i] = (Modeling_Transferable) modeling.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

	}

    public Modeling_Transferable[] transmitModelingsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {

        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitModelingsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.MODELING_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MODELING_ENTITY_CODE), true);

            Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Modeling modeling = (Modeling) it.next();
                transferables[i] = (Modeling_Transferable) modeling.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Modeling_Transferable[] transmitModelingsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitModelingsButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domainCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Modeling modeling = (Modeling) it.next();
                transferables[i] = (Modeling_Transferable) modeling.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public Measurement_Transferable[] transmitMeasurements(	Identifier_Transferable[] ids_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurements | requiere "
                    + (ids_Transferable.length == 0 ? "all" : Integer
                            .toString(ids_Transferable.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (ids_Transferable.length > 0) {
                List idsList = new ArrayList(ids_Transferable.length);
                for (int i = 0; i < ids_Transferable.length; i++)
                    idsList.add(new Identifier(ids_Transferable[i]));
                list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public Measurement_Transferable[] transmitMeasurementsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Measurement_Transferable[] transmitMeasurementsButIdsLinkedCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitMeasurementsButIdsLinkedCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Measurement_Transferable[] transmitMeasurementsButIdsDomainCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitMeasurementsButIdsDomainCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementSetup_Transferable[] transmitMeasurementSetups(	Identifier_Transferable[] identifier_Transferables,
										AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitMeasurementSetups | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MS_ENTITY_CODE), true);

			MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
				transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementSetupsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.MS_ENTITY_CODE), true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MS_ENTITY_CODE), true);

            MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
                transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsMeasurementSetupCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            MeasurementSetupCondition_Transferable measurementSetupCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitMeasurementSetupsButIdsMeasurementSetupCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new MeasurementSetupCondition(measurementSetupCondition_Transferable) ,  true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new MeasurementSetupCondition(measurementSetupCondition_Transferable), true);

            MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
                transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsLinkedCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitMeasurementSetupsButIdsMeasurementLinkedCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new LinkedIdsCondition(linkedIdsCondition_Transferable) ,  true);
            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
                transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Result_Transferable[] transmitResults(	Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitResults | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.RESULT_ENTITY_CODE), true);

			Result_Transferable[] transferables = new Result_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Result result = (Result) it.next();
				transferables[i] = (Result_Transferable) result.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public Result_Transferable[] transmitResultsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitResultsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.RESULT_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.RESULT_ENTITY_CODE), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Result_Transferable[] transmitResultsButIdsDomainCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitResultsButIdsDomainCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList,new DomainCondition(domainCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition( new DomainCondition(domainCondition_Transferable), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Result_Transferable[] transmitResultsButIdsLinkedCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitResultsButIdsLinkedCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList,new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition( new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
	public Result_Transferable[] transmitResultsButIdsResultCondition(
			Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			ResultCondition_Transferable resultCondition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.transmitResultsButIdsLinkedResultCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList,new ResultCondition(resultCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition( new ResultCondition(resultCondition_Transferable), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Result_Transferable[] transmitResultsButIdsResultSortCondition(
			Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			ResultSortCondition_Transferable resultSortCondition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.transmitResultsButIdsLinkedResultSortCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList,new ResultSortCondition(resultSortCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition( new ResultSortCondition(resultSortCondition_Transferable), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public Set_Transferable[] transmitSets(	Identifier_Transferable[] identifier_Transferables,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitSets | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.SET_ENTITY_CODE), true);

			Set_Transferable[] transferables = new Set_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Set set = (Set) it.next();
				transferables[i] = (Set_Transferable) set.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public Set_Transferable[] transmitSetsButIds(Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitSetsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.SET_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.SET_ENTITY_CODE), true);

            Set_Transferable[] transferables = new Set_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Set set = (Set) it.next();
                transferables[i] = (Set_Transferable) set.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Set_Transferable[] transmitSetsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitSetsButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Set_Transferable[] transferables = new Set_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Set set = (Set) it.next();
                transferables[i] = (Set_Transferable) set.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public TemporalPattern_Transferable[] transmitTemporalPatterns(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitTemporalPatterns | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

			TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				TemporalPattern temporalPattern = (TemporalPattern) it.next();
				transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public TemporalPattern_Transferable[] transmitTemporalPatternsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitTemporalPatternsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

            TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                TemporalPattern temporalPattern = (TemporalPattern) it.next();
                transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public Test_Transferable[] transmitTests(	Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitTests | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEST_ENTITY_CODE), true);

			Test_Transferable[] transferables = new Test_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Test test = (Test) it.next();
				transferables[i] = (Test_Transferable) test.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public Test_Transferable[] transmitTestsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitTestsButIds | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, getDomainCondition(domain, ObjectEntities.TEST_ENTITY_CODE), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEST_ENTITY_CODE), true);

            Test_Transferable[] transferables = new Test_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Test test = (Test) it.next();
                transferables[i] = (Test_Transferable) test.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public Test_Transferable[] transmitTestsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            TemporalCondition_Transferable temporalCondition_Transferable)
            throws AMFICOMRemoteException {
        Log.debugMessage("CMServerImpl.transmitTestsButIdsCondition | requiere "
                + (identifier_Transferables.length == 0 ? "all" : Integer
                        .toString(identifier_Transferables.length))
                + " item(s) ", Log.DEBUGLEVEL07);
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new TemporalCondition(temporalCondition_Transferable), true);

            } else
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new TemporalCondition(temporalCondition_Transferable), true);

            Test_Transferable[] transferables = new Test_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Test test = (Test) it.next();
                transferables[i] = (Test_Transferable) test.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    //  Refresh object from a pool    
    public Identifier_Transferable[] transmitRefreshedConfigurationObjects(StorableObject_Transferable[] storableObjects_Transferables, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Map storableObjectMap = new HashMap();
            for (int i = 0; i < storableObjects_Transferables.length; i++) {
    			storableObjectMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);           
    		}
            ConfigurationStorableObjectPool.refresh();
            List storableObjects  = ConfigurationStorableObjectPool.getStorableObjects(new ArrayList(storableObjectMap.keySet()), true);
            for (Iterator it = storableObjects.iterator(); it.hasNext();) {
    			StorableObject so = (StorableObject) it.next();            
    			StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectMap.get(so.getId());
                //  Checking for conformity
                Identifier sotCreatorId = new Identifier(sot.creator_id);
                Identifier sotModifierId = new Identifier(sot.modifier_id);
                if ((Math.abs(sot.created - so.getCreated().getTime()) < 1000)
                        && (Math.abs(sot.modified - so.getModified().getTime()) < 1000) 
                        && !sotCreatorId.equals(so.getCreatorId())
                        && !sotModifierId.equals(so.getModifierId())) {
                    it.remove();
                }           
    		}
            int i=0;
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[storableObjects.size()];
            for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
    			StorableObject so = (StorableObject) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) so.getId().getTransferable(); 
    		}        
            Log.debugMessage("CMServer.transmitRefreshedConfigurationObjects | return " + identifier_Transferables.length
							 + " item(s)", Log.DEBUGLEVEL05);
            return identifier_Transferables;
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
            		.getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
            		.getMessage());
        } catch (Throwable th){
        	Log.errorException(th);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, th
            		.getMessage());
        }
    }
    
    public Identifier_Transferable[] transmitRefreshedMeasurementObjects(StorableObject_Transferable[] storableObjects_Transferables, AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
        try {
            Map storableObjectMap = new HashMap();
            for (int i = 0; i < storableObjects_Transferables.length; i++) {
                storableObjectMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);           
            }
            MeasurementStorableObjectPool.refresh();
            List storableObjects  = MeasurementStorableObjectPool.getStorableObjects(new ArrayList(storableObjectMap.keySet()), true);
            for (Iterator it = storableObjects.iterator(); it.hasNext();) {
                StorableObject so = (StorableObject) it.next();            
                StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectMap.get(so.getId());
                //  Checking for confomity
                Identifier sotCreatorId = new Identifier(sot.creator_id);
                Identifier sotModifierId = new Identifier(sot.modifier_id);
                if ((Math.abs(sot.created - so.getCreated().getTime()) < 1000)
                        && (Math.abs(sot.modified - so.getModified().getTime()) < 1000) 
                        && !sotCreatorId.equals(so.getCreatorId())
                        && !sotModifierId.equals(so.getModifierId())) {
                    it.remove();
                }              
            }
            int i=0;
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[storableObjects.size()];
            for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
                StorableObject so = (StorableObject) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) so.getId().getTransferable(); 
            }        
            return identifier_Transferables;
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable e){
        	Log.errorException(e);
        	throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e
							                    .getMessage());
        }
    }
    
    // Delete methods

	public void delete(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.delete | trying to delete... ", Log.DEBUGLEVEL03);
        Identifier id = new Identifier(id_Transferable);
        short entityCode = id.getMajor();
        try {
        if(ObjectGroupEntities.isInMeasurementGroup(entityCode))
            MeasurementStorableObjectPool.delete(id);
        if(ObjectGroupEntities.isInConfigurationGroup(entityCode))
            ConfigurationStorableObjectPool.delete(id);
        Log.errorMessage("CMServerImpl.delete | Wrong entity code: " + entityCode);
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        }
        
	}
    
	public void deleteList(Identifier_Transferable[] id_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.deleteList | Trying to delete... ", Log.DEBUGLEVEL03);
		List idList = new ArrayList(id_Transferables.length);
        List measurementList = new ArrayList(id_Transferables.length);
        List configurationList = new ArrayList(id_Transferables.length);
        for (int i = 0; i < id_Transferables.length; i++) {
            idList.add(new Identifier(id_Transferables[i]));			
		}
        for (Iterator iter = idList.iterator(); iter.hasNext();) {
			Identifier id = (Identifier) iter.next();
            short entityCode = id.getMajor();
            if(ObjectGroupEntities.isInMeasurementGroup(entityCode))
                measurementList.add(id);
            if(ObjectGroupEntities.isInConfigurationGroup(entityCode))
                configurationList.add(id);
            Log.errorMessage("CMServerImpl.deleteList | Wrong entity code: " + entityCode);
		}
        try {
        MeasurementStorableObjectPool.delete(measurementList);
        ConfigurationStorableObjectPool.delete(configurationList);
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        }
	}

	///////////////////////////////////////// Identifier Generator
	// ////////////////////////////////////////////////
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMServerImpl.getGeneratedIdentifier | generate new Identifer for "
					+ entityCode, Log.DEBUGLEVEL07);
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Illegal object entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Cannot create major/minor entries of identifier for entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "' -- " + ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size)
			throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i].getTransferable();
			return identifiersT;
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Illegal object entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Cannot create major/minor entries of identifier for entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "' -- " + ige.getMessage());
		}
	}


	private com.syrus.AMFICOM.measurement.DomainCondition getDomainCondition(Domain domain, short entityCode){
		if (this.domainCondition == null){
			this.domainCondition = new com.syrus.AMFICOM.measurement.DomainCondition(domain, new Short(entityCode));
		} else{
			this.domainCondition.setDomain(domain);
			this.domainCondition.setEntityCode(new Short(entityCode));
		}

		return this.domainCondition;
	}	
}
