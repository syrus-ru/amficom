/*
 * $Id: ClientConfigurationObjectLoader.java,v 1.7 2004/10/08 14:53:45 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

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
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.corba.LinkedIdsCondition_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.7 $, $Date: 2004/10/08 14:53:45 $
 * @author $Author: max $
 * @module cmserver_v1
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Characteristic_Transferable[] transferables = this.server
                    .transmitCharacteristics(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            CharacteristicType_Transferable[] transferables = this.server
                    .transmitCharacteristicTypes(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Domain_Transferable[] transferables = this.server
                    .transmitDomains(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Equipment_Transferable[] transferables = this.server
                    .transmitEquipments(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            EquipmentType_Transferable[] transferables = this.server
                    .transmitEquipmentTypes(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            KIS_Transferable[] transferables = this.server
                    .transmitKISs(identifier_Transferables,
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

	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MCM_Transferable[] transferables = this.server
                    .transmitMCMs(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementPort_Transferable[] transferables = this.server
                    .transmitMeasurementPorts(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementPortType_Transferable[] transferables = this.server
                    .transmitMeasurementPortTypes(identifier_Transferables,
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
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MonitoredElement_Transferable[] transferables = this.server
					.transmitMonitoredElements(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Port_Transferable[] transferables = this.server
                    .transmitPorts(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            PortType_Transferable[] transferables = this.server
                    .transmitPortTypes(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Server_Transferable[] transferables = this.server
                    .transmitServers(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            TransmissionPath_Transferable[] transferables = this.server
                    .transmitTransmissionPaths(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            User_Transferable[] transferables = this.server
                    .transmitUsers(identifier_Transferables,
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
//    TODO auto generated stub
     }

     public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

//     public void savePermissionAttributes(PermissionAttributes permissionAttributes, Boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
//    }

     public void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMCM(MCM mCM, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveKIS(KIS kIS, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveEquipmentTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void savePortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMeasurementPortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

//     public void savePermissionAttributes(PermissionAttributes permissionAttributes) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
//     }

     public void saveUsers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveDomains(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveServers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMCMs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveEquipments(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void savePorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveTransmissionPaths(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveKISs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMeasurementPorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMonitoredElements(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

    public List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            CharacteristicType_Transferable[] transferables = this.server
                    .transmitCharacteristicTypesButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            EquipmentType_Transferable[] transferables = this.server
                    .transmitEquipmentTypesButIds(identifier_Transferables,
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

    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadPortTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.List)
     */
    public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            PortType_Transferable[] transferables = this.server
                    .transmitPortTypesButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementPortType_Transferable[] transferables = this.server
                    .transmitMeasurementPortTypesButIds(identifier_Transferables,
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

    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCharacteristicsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.List)
     */
    public List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Characteristic_Transferable[] transferables = this.server
                    .transmitCharacteristicsButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            User_Transferable[] transferables = this.server
                    .transmitUsersButIds(identifier_Transferables,
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

    public List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            Domain_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitDomainsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitDomainsButIds(identifier_Transferables,
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

    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadServersButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.List)
     */
    public List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            Server_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitServersButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitServersButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            MCM_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitMCMsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitMCMsButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            Equipment_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitEquipmentsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitEquipmentsButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            Port_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitPortsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitPortsButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            TransmissionPath_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitTransmissionPathsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitTransmissionPathsButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            KIS_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitKISsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());
            
            } else {
                transferables = this.server
                        .transmitKISsButIds(identifier_Transferables,
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
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            MeasurementPort_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitMeasurementPortsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitMeasurementPortsButIds(identifier_Transferables,
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

    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadMonitoredElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.List)
     */
    public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            MonitoredElement_Transferable[] transferables;
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            if (condition instanceof DomainCondition) {
                transferables = this.server
                        .transmitMonitoredElementsButIdsCondition(identifier_Transferables,
                                                                   accessIdentifierTransferable,
                                                                   (DomainCondition_Transferable)condition.getTransferable());            
            } else {
                transferables = this.server
                        .transmitMonitoredElementsButIds(identifier_Transferables,
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
