/*
 * $Id: ClientConfigurationObjectLoader.java,v 1.17 2005/02/08 11:55:39 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.TypicalCondition_Transferable;

/**
 * @version $Revision: 1.17 $, $Date: 2005/02/08 11:55:39 $
 * @author $Author: bob $
 * @module generalclient_v1
 */

public final class ClientConfigurationObjectLoader implements ConfigurationObjectLoader {

	private static AccessIdentifier_Transferable	accessIdentifierTransferable;

	private CMServer								server;
	
	private StorableObjectCondition_Transferable getConditionTransferable(StorableObjectCondition condition) {
		StorableObjectCondition_Transferable condition_Transferable = new StorableObjectCondition_Transferable();
		Object transferable = condition.getTransferable();
		if (condition instanceof LinkedIdsCondition) {
			condition_Transferable.linkedIdsCondition((LinkedIdsCondition_Transferable) transferable);
		} else if (condition instanceof CompoundCondition) {
			condition_Transferable.compoundCondition((CompoundCondition_Transferable) transferable);
		} else if (condition instanceof TypicalCondition) {
			condition_Transferable.typicalCondition((TypicalCondition_Transferable) transferable);
		} else if (condition instanceof EquivalentCondition) {
			condition_Transferable.equialentCondition((EquivalentCondition_Transferable) transferable);
		} 
		return condition_Transferable;
	}

	public ClientConfigurationObjectLoader(CMServer server) {
		this.server = server;
	}

	public static void setAccessIdentifierTransferable(AccessIdentifier_Transferable accessIdentifier_Transferable) {
		accessIdentifierTransferable = accessIdentifier_Transferable;
	}

	public void delete(Identifier id) throws CommunicationException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public void delete(List ids) throws CommunicationException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.delete | AMFICOMRemoteException ";
			throw new CommunicationException(msg, e);
		}
	}

	public CableLinkType loadCableLinkType(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new CableLinkType(this.server.transmitCableLinkType((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCableLinkType | server.transmitCableLinkType(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public CableThread loadCableThread(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new CableThread(this.server.transmitCableThread((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCableThread | server.transmitCableThread(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new CableThreadType(this.server.transmitCableThreadType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCharacteristicType | server.transmitCharacteristicType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadCableLinkTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableLinkType_Transferable[] transferables = this.server.transmitCableLinkTypes(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableLinkType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCableThreads(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThread_Transferable[] transferables = this.server.transmitCableThreads(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableThread(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCableThreadTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThreadType_Transferable[] transferables = this.server.transmitCableThreadTypes(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableThreadType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCableThreadTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThreadType_Transferable[] transferables = this.server.transmitCableThreadTypesButIds(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableThreadType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Equipment loadEquipment(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Equipment(this.server.transmitEquipment((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadAEquipment | new Equipment(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipment | server.transmitEquipment(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
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
			Equipment_Transferable[] transferables = this.server.transmitEquipments(identifierTransferables,
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

	public List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Equipment_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitEquipmentsButIdsCondition(identifierTransferables,
				accessIdentifierTransferable, this.getConditionTransferable(condition));			List list = new ArrayList(transferables.length);
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

	public EquipmentType loadEquipmentType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new EquipmentType(this.server.transmitEquipmentType((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipmentType | new EquipmentType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipmentType | server.transmitEquipmentType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
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
			EquipmentType_Transferable[] transferables = this.server.transmitEquipmentTypes(identifierTransferables,
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

	public List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EquipmentType_Transferable[] transferables = this.server.transmitEquipmentTypesButIds(
				identifierTransferables, accessIdentifierTransferable);
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

	public KIS loadKIS(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new KIS(this.server.transmitKIS((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadKIS | new KIS(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadKIS | server.transmitKIS(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
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
			KIS_Transferable[] transferables = this.server.transmitKISs(identifierTransferables,
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

	public List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			KIS_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitKISsButIdsCondition(identifierTransferables,
				accessIdentifierTransferable, this.getConditionTransferable(condition));
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

	public Link loadLink(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new Link(this.server.transmitLink((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadLink | new Link(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadLink | server.transmitLink(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadLinks(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Link_Transferable[] transferables = this.server.transmitLinks(identifierTransferables,
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

	public List loadLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Link_Transferable[] transferables = this.server.transmitLinksButIds(identifierTransferables,
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

	public LinkType loadLinkType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new LinkType(this.server.transmitLinkType((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadKISType | server.transmitKISType(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadKISType | server.transmitKISType(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadLinkTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AbstractLinkType_Transferable[] transferables = this.server.transmitLinkTypes(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {

				LinkType linkType;
				CableLinkType cableLinkType;
				switch (transferables[i].discriminator().value()) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						cableLinkType = new CableLinkType(transferables[i].cableLinkType());
						list.add(cableLinkType);
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						linkType = new LinkType(transferables[i].linkType());
						list.add(linkType);
						break;
					default:
						throw new CommunicationException("ClientConfigurationObjectLoader.loadLinkTypesButIds"
								+ " | Wrong AbstractLinkTypeSort");
				}
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AbstractLinkType_Transferable[] transferables = this.server.transmitLinkTypesButIds(
				identifierTransferables, accessIdentifierTransferable);

			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {

				LinkType linkType;
				CableLinkType cableLinkType;
				switch (transferables[i].discriminator().value()) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						cableLinkType = new CableLinkType(transferables[i].cableLinkType());
						list.add(cableLinkType);
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						linkType = new LinkType(transferables[i].linkType());
						list.add(linkType);
						break;
					default:
						throw new CommunicationException("ClientConfigurationObjectLoader.loadLinkTypesButIds"
								+ " | Wrong AbstractLinkTypeSort");
				}
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCableLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			CableLinkType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitCableLinkTypesButIdsCondition(identifierTransferables, accessIdentifierTransferable,
				this.getConditionTransferable(condition));
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableLinkType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCableThreadsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			CableThread_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitCableThreadButIdsCondition(identifierTransferables, accessIdentifierTransferable,
				this.getConditionTransferable(condition));
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableThread(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new MeasurementPort(this.server.transmitMeasurementPort((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadMeasurementPort | new MeasurementPort(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadMeasurementPort | server.transmitMeasurementPort("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
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
			MeasurementPort_Transferable[] transferables = this.server.transmitMeasurementPorts(
				identifierTransferables, accessIdentifierTransferable);
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

	public List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MeasurementPort_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitMeasurementPortsButIdsCondition(identifierTransferables,
				accessIdentifierTransferable,
				this.getConditionTransferable(condition));
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

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		try {
			return new MeasurementPortType(this.server.transmitMeasurementPortType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadMeasurementPortType | new MeasurementPortType("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadMeasurementPortType | server.transmitMeasurementPortType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
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
			MeasurementPortType_Transferable[] transferables = this.server.transmitMeasurementPortTypes(
				identifierTransferables, accessIdentifierTransferable);
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

	public List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementPortType_Transferable[] transferables = this.server.transmitMeasurementPortTypesButIds(
				identifierTransferables, accessIdentifierTransferable);
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

	public MonitoredElement loadMonitoredElement(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new MonitoredElement(this.server.transmitMonitoredElement((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadMonitoredElement | server.loadMonitoredElement("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
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
			MonitoredElement_Transferable[] transferables = this.server.transmitMonitoredElements(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MonitoredElement(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MonitoredElement_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			
			transferables = this.server.transmitMonitoredElementsButIdsCondition(identifierTransferables,
				accessIdentifierTransferable, this.getConditionTransferable(condition));
			
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MonitoredElement(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Port loadPort(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Port(this.server.transmitPort((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadPort | new Port(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadPort | server.transmitPort(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
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
			Port_Transferable[] transferables = this.server.transmitPorts(identifierTransferables,
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

	public List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Port_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitPortsButIdsCondition(identifierTransferables,
				accessIdentifierTransferable, this.getConditionTransferable(condition));
			
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

	public PortType loadPortType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new PortType(this.server.transmitPortType((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadPortType | new PortType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadPortType | server.transmitPortType(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
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
			PortType_Transferable[] transferables = this.server.transmitPortTypes(identifierTransferables,
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

	public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			PortType_Transferable[] transferables = this.server.transmitPortTypesButIds(identifierTransferables,
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

	public TransmissionPath loadTransmissionPath(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new TransmissionPath(this.server.transmitTransmissionPath((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadTransmissionPath | new TransmissionPath(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadTransmissionPath | server.transmitTransmissionPath("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
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
			TransmissionPath_Transferable[] transferables = this.server.transmitTransmissionPaths(
				identifierTransferables, accessIdentifierTransferable);
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

	public List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			TransmissionPath_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitTransmissionPathsButIdsCondition(identifierTransferables,
				accessIdentifierTransferable, this.getConditionTransferable(condition));
			
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadTransmissionPathType(com.syrus.AMFICOM.general.Identifier)
	 */
	public TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException,
			CommunicationException {
		try {
			return new TransmissionPathType(this.server.transmitTransmissionPathType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadTransmissionPathType | new TransmissionPathType("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadTransmissionPathType | server.transmitTransmissionPathType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadTransmissionPathTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPathType_Transferable[] transferables = this.server.transmitTransmissionPathTypes(
				identifierTransferables, accessIdentifierTransferable);
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

	public List loadTransmissionPathTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPathType_Transferable[] transferables = this.server.transmitTransmissionPathTypesButIds(
				identifierTransferables, accessIdentifierTransferable);
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

	// public PermissionAttributes loadPermissionAttributes(Identifier id)
	// throws DatabaseException {
	// return new PermissionAttributes(id);
	// }

	public Set refresh(Set storableObjects) throws CommunicationException {

		try {
			Set refreshedIds = new HashSet();
			Identifier_Transferable[] identifier_Transferables;
			StorableObject_Transferable[] storableObject_Transferables = new StorableObject_Transferable[storableObjects
					.size()];
			int i = 0;
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObject_Transferables[i] = storableObject.getHeaderTransferable();
			}
			identifier_Transferables = this.server.transmitRefreshedConfigurationObjects(storableObject_Transferables,
				accessIdentifierTransferable);

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public void saveCableThread(CableThread cableThread, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		CableThread_Transferable transferables = (CableThread_Transferable) cableThread.getTransferable();
		try {
			this.server.receiveCableThread(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThread ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		CableLinkType_Transferable transferables = (CableLinkType_Transferable) cableLinkType.getTransferable();
		try {
			this.server.receiveCableLinkType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableLinkType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CableLinkType_Transferable) ((CableLinkType) it.next()).getTransferable();
		}
		try {
			this.server.receiveCableLinkTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableLinkTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableThreads(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CableThread_Transferable[] transferables = new CableThread_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CableThread_Transferable) ((CableThread) it.next()).getTransferable();
		}
		try {
			this.server.receiveCableThreads(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThreads ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		CableThreadType_Transferable transferables = (CableThreadType_Transferable) cableThreadType.getTransferable();
		try {
			this.server.receiveCableThreadType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThreadType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableThreadTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CableThreadType_Transferable) ((CableThreadType) it.next()).getTransferable();
		}
		try {
			this.server.receiveCableThreadTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThreadTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	// public void savePermissionAttributes(PermissionAttributes
	// permissionAttributes, Boolean force) throws VersionCollisionException,
	// DatabaseException, CommunicationException{
	// TODO auto generated stub
	// }

	public void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
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

	public void saveEquipments(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Equipment_Transferable[] transferables = new Equipment_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Equipment_Transferable) ((Equipment) it.next()).getTransferable();
		}
		try {
			this.server.receiveEquipments(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveEquipments ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
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

	public void saveEquipmentTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (EquipmentType_Transferable) ((EquipmentType) it.next()).getTransferable();
		}
		try {
			this.server.receiveEquipmentTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveEquipmentTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
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

	public void saveKISs(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		KIS_Transferable[] transferables = new KIS_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (KIS_Transferable) ((KIS) it.next()).getTransferable();
		}
		try {
			this.server.receiveKISs(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveKISs ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Link_Transferable[] transferables = new Link_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Link_Transferable) ((Link) it.next()).getTransferable();
		}
		try {
			this.server.receiveLinks(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveLinks ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (AbstractLinkType_Transferable) ((LinkType) it.next()).getTransferable();
		}
		try {
			this.server.receiveLinkTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveLinkTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
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

	public void saveMeasurementPorts(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementPort_Transferable) ((MeasurementPort) it.next()).getTransferable();
		}
		try {
			this.server.receiveMeasurementPorts(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMeasurementPorts ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		MeasurementPortType_Transferable transferables = (MeasurementPortType_Transferable) measurementPortType
				.getTransferable();
		try {
			this.server.receiveMeasurementPortType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMeasurementPortType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementPortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementPortType_Transferable) ((MeasurementPortType) it.next()).getTransferable();
		}
		try {
			this.server.receiveMeasurementPortTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMeasurementPortTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		MonitoredElement_Transferable transferables = (MonitoredElement_Transferable) monitoredElement
				.getTransferable();
		try {
			this.server.receiveMonitoredElement(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMonitoredElement ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMonitoredElements(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (MonitoredElement_Transferable) ((MonitoredElement) it.next()).getTransferable();
		}
		try {
			this.server.receiveMonitoredElements(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMonitoredElements ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
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

	public void savePorts(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Port_Transferable[] transferables = new Port_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Port_Transferable) ((Port) it.next()).getTransferable();
		}
		try {
			this.server.receivePorts(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.savePorts ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
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

	public void savePortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PortType_Transferable[] transferables = new PortType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (PortType_Transferable) ((PortType) it.next()).getTransferable();
		}
		try {
			this.server.receivePortTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.savePortTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		TransmissionPath_Transferable transferables = (TransmissionPath_Transferable) transmissionPath
				.getTransferable();
		try {
			this.server.receiveTransmissionPath(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPath ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPaths(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (TransmissionPath_Transferable) ((TransmissionPath) it.next()).getTransferable();
		}
		try {
			this.server.receiveTransmissionPaths(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPaths ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		TransmissionPathType_Transferable transferables = (TransmissionPathType_Transferable) transmissionPathType
				.getTransferable();
		try {
			this.server.receiveTransmissionPathType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPathType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPathTypes(List list, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (TransmissionPathType_Transferable) ((TransmissionPathType) it.next()).getTransferable();
		}
		try {
			this.server.receiveTransmissionPathTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPathTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	// public void savePermissionAttributes(PermissionAttributes
	// permissionAttributes) throws VersionCollisionException,
	// DatabaseException, CommunicationException{
	// TODO auto generated stub
	// }

}
