/*
 * $Id: ClientConfigurationObjectLoader.java,v 1.5 2004/09/29 06:12:51 bob Exp $
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
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2004/09/29 06:12:51 $
 * @author $Author: bob $
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
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public EquipmentType loadEquipmentType(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public PortType loadPortType(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws RetrieveObjectException,
	/**
	 * FIXME method is not complete !
	 */
	CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Characteristic loadCharacteristic(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	//	public PermissionAttributes loadPermissionAttributes(Identifier id)
	// throws DatabaseException {
	//		return new PermissionAttributes(id);
	//	}

	public User loadUser(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public Domain loadDomain(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public Server loadServer(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public MCM loadMCM(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Equipment loadEquipment(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public Port loadPort(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		/**
		 * FIXME method is not complete !
		 */

		throw new UnsupportedOperationException();
	}

	public KIS loadKIS(Identifier id) throws RetrieveObjectException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
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
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadDomains(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadEquipments(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadKISs(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
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
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadPortTypes(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadServers(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public List loadUsers(List ids) throws DatabaseException, CommunicationException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
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


}
