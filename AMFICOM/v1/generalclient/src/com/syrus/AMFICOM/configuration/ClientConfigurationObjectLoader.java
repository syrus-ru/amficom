/*
 * $Id: ClientConfigurationObjectLoader.java,v 1.2 2004/11/15 13:32:36 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.LinkedIdsCondition;
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
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.KISType_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;
import com.syrus.AMFICOM.configuration.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.configuration.corba.User_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.StringFieldCondition;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2004/11/15 13:32:36 $
 * @author $Author: bob $
 * @module generalclient_v1
 */

public final class ClientConfigurationObjectLoader implements ConfigurationObjectLoader {

	private CMServer				server;

	private static AccessIdentifier_Transferable	accessIdentifierTransferable;

	public ClientConfigurationObjectLoader(CMServer server) {
		this.server = server;
	}
	
	public static void setAccessIdentifierTransferable(AccessIdentifier_Transferable accessIdentifier_Transferable) {
		accessIdentifierTransferable = accessIdentifier_Transferable;
	}	
    
    public CharacteristicType loadCharacteristicType(Identifier id) throws RetrieveObjectException,
			CommunicationException {
        try {
            return new CharacteristicType(this.server.transmitCharacteristicType((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadCharacteristicType | new CharacteristicType(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadCharacteristicType | server.transmitCharacteristicType("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public EquipmentType loadEquipmentType(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new EquipmentType(this.server.transmitEquipmentType((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadEquipmentType | new EquipmentType(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadEquipmentType | server.transmitEquipmentType("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public PortType loadPortType(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new PortType(this.server.transmitPortType((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadPortType | new PortType(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadPortType | server.transmitPortType("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new MeasurementPortType(this.server.transmitMeasurementPortType((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadMeasurementPortType | new MeasurementPortType(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadMeasurementPortType | server.transmitMeasurementPortType("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}
    
    public KISType loadKISType(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new KISType(this.server.transmitKISType((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadKISType | new KISType(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadKISType | server.transmitKISType("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
    }
    
    /* (non-Javadoc)
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadTransmissionPathType(com.syrus.AMFICOM.general.Identifier)
	 */
	public TransmissionPathType loadTransmissionPathType(Identifier id)
			throws DatabaseException, CommunicationException {
		try {
            return new TransmissionPathType(this.server.transmitTransmissionPathType((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadTransmissionPathType | new TransmissionPathType(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadTransmissionPathType | server.transmitTransmissionPathType("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public Characteristic loadCharacteristic(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new Characteristic(this.server.transmitCharacteristic((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadCharacteristic | new Characteristic(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadCharacteristic | server.transmitCharacteristic("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}   
    
    public LinkType loadLinkType(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new LinkType(this.server.transmitLinkType((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadKISType | server.transmitKISType("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
    }

	//	public PermissionAttributes loadPermissionAttributes(Identifier id)
	// throws DatabaseException {
	//		return new PermissionAttributes(id);
	//	}

	public User loadUser(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new User(this.server.transmitUser((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadUser | new User(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadUser | server.transmitUser("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public Domain loadDomain(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new Domain(this.server.transmitDomain((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadDomain | new Domain(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadDomain | server.transmitDomain("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public Server loadServer(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new Server(this.server.transmitServer((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadServer | new Server(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadServer | server.transmiServer("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public MCM loadMCM(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new MCM(this.server.transmitMCM((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadMCM | new MCM(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadMCM | server.transmitMCM("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public Equipment loadEquipment(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new Equipment(this.server.transmitEquipment((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadAEquipment | new Equipment(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadEquipment | server.transmitEquipment("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }		
	}

	public Port loadPort(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new Port(this.server.transmitPort((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadPort | new Port(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadPort | server.transmitPort("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws RetrieveObjectException,
			CommunicationException {
        try {
            return new TransmissionPath(this.server.transmitTransmissionPath((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadTransmissionPath | new TransmissionPath(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadTransmissionPath | server.transmitTransmissionPath("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public KIS loadKIS(Identifier id) throws RetrieveObjectException, CommunicationException {
        try {
            return new KIS(this.server.transmitKIS((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadKIS | new KIS(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadKIS | server.transmitKIS("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public Link loadLink(Identifier id) throws DatabaseException,
			CommunicationException {
		try {
            return new Link(this.server.transmitLink((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadLink | new Link(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadLink | server.transmitLink("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws RetrieveObjectException,
			CommunicationException {
        try {
            return new MeasurementPort(this.server.transmitMeasurementPort((Identifier_Transferable) id
                    .getTransferable(), accessIdentifierTransferable));
        } catch (CreateObjectException e) {
            String msg = "ClientMeasurementObjectLoader.loadMeasurementPort | new MeasurementPort(" + id.toString()
                    + ")";
            throw new RetrieveObjectException(msg, e);
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.loadMeasurementPort | server.transmitMeasurementPort("
                    + id.toString() + ")";
            throw new CommunicationException(msg, e);
        }
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		try {
			return new MonitoredElement(this.server.transmitMonitoredElement((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadMonitoredElement | new MonitoredElement("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadMonitoredElement | server.loadMonitoredElement("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadCharacteristics(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Characteristic_Transferable[] transferables = this.server
                    .transmitCharacteristics(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Characteristic(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            CharacteristicType_Transferable[] transferables = this.server
                    .transmitCharacteristicTypes(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new CharacteristicType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadDomains(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Domain_Transferable[] transferables = this.server
                    .transmitDomains(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Domain(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadEquipments(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Equipment_Transferable[] transferables = this.server
                    .transmitEquipments(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Equipment(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            EquipmentType_Transferable[] transferables = this.server
                    .transmitEquipmentTypes(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new EquipmentType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadKISs(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            KIS_Transferable[] transferables = this.server
                    .transmitKISs(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new KIS(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadLinks(List ids) throws DatabaseException,
			CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Link_Transferable[] transferables = this.server
                    .transmitLinks(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Link(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadLinksButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Link_Transferable[] transferables = this.server
                    .transmitLinksButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Link(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}
    
	public List loadLinkTypes(List ids) throws DatabaseException,
			CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            LinkType_Transferable[] transferables = this.server
                    .transmitLinkTypes(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new LinkType(transferables[j]));
            }
            return list;
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadLinkTypesButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            LinkType_Transferable[] transferables = this.server
                    .transmitLinkTypesButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new LinkType(transferables[j]));
            }
            return list;
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadKISTypes(List ids) throws DatabaseException,
			CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            KISType_Transferable[] transferables = this.server
                    .transmitKISTypes(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new KISType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}
        
	public List loadKISTypesButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            KISType_Transferable[] transferables = this.server
                    .transmitKISTypesButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new KISType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadTransmissionPathTypes(List ids) throws DatabaseException,
			CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            TransmissionPathType_Transferable[] transferables = this.server
                    .transmitTransmissionPathTypes(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new TransmissionPathType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadTransmissionPathTypesButIds(
			StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            TransmissionPathType_Transferable[] transferables = this.server
                    .transmitTransmissionPathTypesButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new TransmissionPathType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}
    
	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MCM_Transferable[] transferables = this.server
                    .transmitMCMs(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new MCM(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementPort_Transferable[] transferables = this.server
                    .transmitMeasurementPorts(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new MeasurementPort(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementPortType_Transferable[] transferables = this.server
                    .transmitMeasurementPortTypes(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new MeasurementPortType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MonitoredElement_Transferable[] transferables = this.server
					.transmitMonitoredElements(identifierTransferables,
									accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MonitoredElement(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadPorts(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Port_Transferable[] transferables = this.server
                    .transmitPorts(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Port(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadPortTypes(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            PortType_Transferable[] transferables = this.server
                    .transmitPortTypes(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new PortType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadServers(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Server_Transferable[] transferables = this.server
                    .transmitServers(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Server(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            TransmissionPath_Transferable[] transferables = this.server
                    .transmitTransmissionPaths(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new TransmissionPath(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}

	public List loadUsers(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            User_Transferable[] transferables = this.server
                    .transmitUsers(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new User(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
	}
    
    public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
        CharacteristicType_Transferable transferables = (CharacteristicType_Transferable)characteristicType.getTransferable();         
        try {
            this.server.receiveCharacteristicType(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientConfigurationObjectLoader.saveCharacteristicType ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
        }
     }

     public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         EquipmentType_Transferable transferables = (EquipmentType_Transferable) equipmentType.getTransferable();         
         try {
             this.server.receiveEquipmentType(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveEquipmentType ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         PortType_Transferable transferables = (PortType_Transferable) portType.getTransferable();         
         try {
             this.server.receivePortType(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.savePortType ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MeasurementPortType_Transferable transferables = (MeasurementPortType_Transferable) measurementPortType.getTransferable();         
         try {
             this.server.receiveMeasurementPortType(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveMeasurementPortType ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }
     
     public void saveKISType(KISType kisType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
        KISType_Transferable transferables = (KISType_Transferable) kisType.getTransferable();         
        try {
            this.server.receiveKISType(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientConfigurationObjectLoader.saveKISType ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
        }
    }

     public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Characteristic_Transferable transferables = (Characteristic_Transferable) characteristic.getTransferable();         
         try {
             this.server.receiveCharacteristic(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveCharacteristic ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }
     
     
     
     

//     public void savePermissionAttributes(PermissionAttributes permissionAttributes, Boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
//    }

     public void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         User_Transferable transferables = (User_Transferable) user.getTransferable();         
         try {
             this.server.receiveUser(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveUser ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Domain_Transferable transferables = (Domain_Transferable) domain.getTransferable();         
         try {
             this.server.receiveDomain(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveDomain ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Server_Transferable transferables = (Server_Transferable) server.getTransferable();         
         try {
             this.server.receiveServer(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveDomain ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MCM_Transferable transferables = (MCM_Transferable) mcm.getTransferable();         
         try {
             this.server.receiveMCM(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveMCM ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Equipment_Transferable transferables = (Equipment_Transferable) equipment.getTransferable();         
         try {
             this.server.receiveEquipment(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveEquipment ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Port_Transferable transferables = (Port_Transferable) port.getTransferable();         
         try {
             this.server.receivePort(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveEquipment ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         TransmissionPath_Transferable transferables = (TransmissionPath_Transferable) transmissionPath.getTransferable();         
         try {
             this.server.receiveTransmissionPath(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveTransmissionPath ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }
     
     public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
        TransmissionPathType_Transferable transferables = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();         
        try {
            this.server.receiveTransmissionPathType(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientConfigurationObjectLoader.saveTransmissionPathType ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
        }
    }

     public void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         KIS_Transferable transferables = (KIS_Transferable) kis.getTransferable();         
         try {
             this.server.receiveKIS(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveKIS ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

	public void saveLink(Link link, boolean force)
			throws VersionCollisionException, DatabaseException,
			CommunicationException {
        Link_Transferable transferables = (Link_Transferable) link.getTransferable();         
        try {
            this.server.receiveLink(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientConfigurationObjectLoader.saveLink ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
        }
	}

	public void saveLinkType(LinkType linkType, boolean force)
			throws VersionCollisionException, DatabaseException,
			CommunicationException {
		LinkType_Transferable transferables = (LinkType_Transferable) linkType.getTransferable();         
        try {
            this.server.receiveLinkType(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
            String msg = "ClientConfigurationObjectLoader.saveLinkType ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
        }
	}

     public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MeasurementPort_Transferable transferables = (MeasurementPort_Transferable) measurementPort.getTransferable();         
         try {
             this.server.receiveMeasurementPort(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveMeasurementPort ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MonitoredElement_Transferable transferables = (MonitoredElement_Transferable) monitoredElement.getTransferable();         
         try {
             this.server.receiveMonitoredElement(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientConfigurationObjectLoader.saveMonitoredElement ";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }

     public void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (CharacteristicType_Transferable)( (CharacteristicType)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveCharacteristicTypes(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveCharacteristicTypes ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveEquipmentTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (EquipmentType_Transferable)( (EquipmentType)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveEquipmentTypes(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveEquipmentTypes ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void savePortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         PortType_Transferable[] transferables = new PortType_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (PortType_Transferable)( (PortType)it.next() ).getTransferable();                        
         }
         try {
             this.server.receivePortTypes(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.savePortTypes ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveMeasurementPortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (MeasurementPortType_Transferable)( (MeasurementPortType)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveMeasurementPortTypes(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveMeasurementPortTypes ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (Characteristic_Transferable)( (Characteristic)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveCharacteristics(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveCharacteristics ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }
     
     public void saveKISTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
        KISType_Transferable[] transferables = new KISType_Transferable[list.size()];
        int i=0;
        for (Iterator it = list.iterator(); it.hasNext();i++) {
            transferables[i] = (KISType_Transferable)( (KISType)it.next() ).getTransferable();                        
        }
        try {
            this.server.receiveKISTypes(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
           String msg = "ClientMeasurementObjectLoader.saveKISType ";
           
           if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
           
           throw new CommunicationException(msg, e);       
        }
     }

	public void saveLinks(List list, boolean force)
			throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Link_Transferable[] transferables = new Link_Transferable[list.size()];
        int i=0;
        for (Iterator it = list.iterator(); it.hasNext();i++) {
            transferables[i] = (Link_Transferable)( (Link)it.next() ).getTransferable();                        
        }
        try {
            this.server.receiveLinks(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
           String msg = "ClientMeasurementObjectLoader.saveLinks ";
           
           if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
           
           throw new CommunicationException(msg, e);       
        }
	}

	public void saveLinkTypes(List list, boolean force)
			throws VersionCollisionException, DatabaseException,
			CommunicationException {
		LinkType_Transferable[] transferables = new LinkType_Transferable[list.size()];
        int i=0;
        for (Iterator it = list.iterator(); it.hasNext();i++) {
            transferables[i] = (LinkType_Transferable)( (LinkType)it.next() ).getTransferable();                        
        }
        try {
            this.server.receiveLinkTypes(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
           String msg = "ClientMeasurementObjectLoader.saveLinkTypes ";
           
           if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
           
           throw new CommunicationException(msg, e);       
        }
	}
     

//     public void savePermissionAttributes(PermissionAttributes permissionAttributes) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
//     }

     public void saveUsers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         User_Transferable[] transferables = new User_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (User_Transferable)( (User)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveUsers(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveUsers ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveDomains(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Domain_Transferable[] transferables = new Domain_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (Domain_Transferable)( (Domain)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveDomains(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveDomains ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveServers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Server_Transferable[] transferables = new Server_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (Server_Transferable)( (Server)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveServers(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveServers ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveMCMs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MCM_Transferable[] transferables = new MCM_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (MCM_Transferable)( (MCM)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveMCMs(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveMCMs ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveEquipments(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Equipment_Transferable[] transferables = new Equipment_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (Equipment_Transferable)( (Equipment)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveEquipments(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveEquipments ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void savePorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Port_Transferable[] transferables = new Port_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (Port_Transferable)( (Port)it.next() ).getTransferable();                        
         }
         try {
             this.server.receivePorts(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.savePorts ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveTransmissionPaths(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (TransmissionPath_Transferable)( (TransmissionPath)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveTransmissionPaths(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveTransmissionPaths ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }
     
     public void saveTransmissionPathTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
        TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[list.size()];
        int i=0;
        for (Iterator it = list.iterator(); it.hasNext();i++) {
            transferables[i] = (TransmissionPathType_Transferable)( (TransmissionPathType)it.next() ).getTransferable();                        
        }
        try {
            this.server.receiveTransmissionPathTypes(transferables, force, accessIdentifierTransferable);         
        } catch (AMFICOMRemoteException e) {
           String msg = "ClientMeasurementObjectLoader.saveTransmissionPathTypes ";
           
           if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
               throw new VersionCollisionException(msg, e);
           
           throw new CommunicationException(msg, e);       
        }
    }
     
     

     public void saveKISs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         KIS_Transferable[] transferables = new KIS_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (KIS_Transferable)( (KIS)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveKISs(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveKISs ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveMeasurementPorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (MeasurementPort_Transferable)( (MeasurementPort)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveMeasurementPorts(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveMeasurementPorts ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

     public void saveMonitoredElements(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[list.size()];
         int i=0;
         for (Iterator it = list.iterator(); it.hasNext();i++) {
             transferables[i] = (MonitoredElement_Transferable)( (MonitoredElement)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveMonitoredElements(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
            String msg = "ClientMeasurementObjectLoader.saveMonitoredElements ";
            
            if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
            
            throw new CommunicationException(msg, e);       
         }
     }

    public List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            CharacteristicType_Transferable[] transferables = this.server
                    .transmitCharacteristicTypesButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new CharacteristicType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            EquipmentType_Transferable[] transferables = this.server
                    .transmitEquipmentTypesButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new EquipmentType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            PortType_Transferable[] transferables = this.server
                    .transmitPortTypesButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new PortType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementPortType_Transferable[] transferables = this.server
                    .transmitMeasurementPortTypesButIds(identifierTransferables,
                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new MeasurementPortType(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Characteristic_Transferable[] transferables;
            if (condition instanceof LinkedIdsCondition){
            	LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
            	transferables = this.server.transmitCharacteristicsButIdsCondition(identifierTransferables,
												                                   accessIdentifierTransferable,
																				   (LinkedIdsCondition_Transferable)linkedIdsCondition.getTransferable());
            } else
            	transferables = this.server
                .transmitCharacteristicsButIds(identifierTransferables,
			                                    accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Characteristic(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            List list = null;
            if (condition instanceof StringFieldCondition){
            	StringFieldCondition stringFieldCondition = (StringFieldCondition)condition;
            	User_Transferable[] transferables = this.server
                .transmitUsersButIdsCondition(identifierTransferables,
						  accessIdentifierTransferable,
						  (StringFieldCondition_Transferable)stringFieldCondition.getTransferable());
                list = new ArrayList(transferables.length);
                for (int j = 0; j < transferables.length; j++) {
                    list.add(new User(transferables[j]));
                }
            } 
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            Domain_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitDomainsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitDomainsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadMeasurementsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition or ");
                }
                
            }
                        
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Domain(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            Server_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitServersButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitServersButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadMeasurementPortsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition");
                }                
            }
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Server(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }


    public List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            MCM_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitMCMsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitMCMsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadMCMsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition or ");
                }
                
            }
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new MCM(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            Equipment_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitEquipmentsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitEquipmentsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadEquipmentsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition or ");
                }
                
            }
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Equipment(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            Port_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitPortsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitPortsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadMeasurementPortsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition");
                }                
            }
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new Port(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            TransmissionPath_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitTransmissionPathsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitTransmissionPathsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadMeasurementPortsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition");
                }                
            }
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new TransmissionPath(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }
    
    public List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            KIS_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitKISsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitKISsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadKISButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition or ");
                }
                
            }
            
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new KIS(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            MeasurementPort_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitMeasurementPortsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitMeasurementPortsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadMeasurementPortsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition");
                }
                
            }
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new MeasurementPort(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }

    public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
            MonitoredElement_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitMonitoredElementsButIdsCondition(identifierTransferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitMonitoredElementsButIds(identifierTransferables,
                                                    accessIdentifierTransferable);
                if (condition != null && !(condition instanceof DomainCondition) ) {
                    Log.errorMessage("ClientMeasurementObjectLoader.loadMeasurementPortsButIds | " +
                            "Class '" + condition.getClass().getName() + "' is not instanse of DomainCondition");
                }
                
            }
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new MonitoredElement(transferables[j]));
            }
            return list;
        } catch (CreateObjectException e) {
            throw new RetrieveObjectException(e);
        } catch (AMFICOMRemoteException e) {
            throw new CommunicationException(e);
        }
    }


}
