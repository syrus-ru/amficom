/*
 * $Id: ClientConfigurationObjectLoader.java,v 1.19 2005/02/14 15:17:39 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import com.syrus.AMFICOM.general.SessionContext;
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
 * @version $Revision: 1.19 $, $Date: 2005/02/14 15:17:39 $
 * @author $Author: max $
 * @module generalclient_v1
 */

public final class ClientConfigurationObjectLoader implements ConfigurationObjectLoader {

	private CMServer								server;
	
	private AccessIdentifier_Transferable getAccessIdentifierTransferable() {
		return  (AccessIdentifier_Transferable) SessionContext.getAccessIdentity().getTransferable();
	}
	
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

	public void delete(Identifier id) throws CommunicationException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public void delete(Collection ids) throws CommunicationException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.delete | AMFICOMRemoteException ";
			throw new CommunicationException(msg, e);
		}
	}

	public CableLinkType loadCableLinkType(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new CableLinkType(this.server.transmitCableLinkType((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCableLinkType | server.transmitCableLinkType(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public CableThread loadCableThread(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new CableThread(this.server.transmitCableThread((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCableThread | server.transmitCableThread(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new CableThreadType(this.server.transmitCableThreadType((Identifier_Transferable) id
					.getTransferable(), getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCharacteristicType | server.transmitCharacteristicType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadCableLinkTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableLinkType_Transferable[] transferables = this.server.transmitCableLinkTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableLinkType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadCableThreads(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThread_Transferable[] transferables = this.server.transmitCableThreads(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableThread(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadCableThreadTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThreadType_Transferable[] transferables = this.server.transmitCableThreadTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CableThreadType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadCableThreadTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThreadType_Transferable[] transferables = this.server.transmitCableThreadTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable()));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadAEquipment | new Equipment(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipment | server.transmitEquipment(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadEquipments(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Equipment_Transferable[] transferables = this.server.transmitEquipments(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadEquipmentsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));			
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable()));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipmentType | new EquipmentType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipmentType | server.transmitEquipmentType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadEquipmentTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EquipmentType_Transferable[] transferables = this.server.transmitEquipmentTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadEquipmentTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EquipmentType_Transferable[] transferables = this.server.transmitEquipmentTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable()));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadKIS | new KIS(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadKIS | server.transmitKIS(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadKISs(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			KIS_Transferable[] transferables = this.server.transmitKISs(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadKISsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable()));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadLink | new Link(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadLink | server.transmitLink(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadLinks(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Link_Transferable[] transferables = this.server.transmitLinks(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Link_Transferable[] transferables = this.server.transmitLinksButIds(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadKISType | server.transmitKISType(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadKISType | server.transmitKISType(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadLinkTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AbstractLinkType_Transferable[] transferables = this.server.transmitLinkTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AbstractLinkType_Transferable[] transferables = this.server.transmitLinkTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());

			Collection list = new ArrayList(transferables.length);
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

	public Collection loadCableLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			CableLinkType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitCableLinkTypesButIdsCondition(identifierTransferables, getAccessIdentifierTransferable(),
				this.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadCableThreadsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			CableThread_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitCableThreadButIdsCondition(identifierTransferables, getAccessIdentifierTransferable(),
				this.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
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
					.getTransferable(), getAccessIdentifierTransferable()));
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

	public Collection loadMeasurementPorts(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementPort_Transferable[] transferables = this.server.transmitMeasurementPorts(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadMeasurementPortsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(),
				this.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
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
					.getTransferable(), getAccessIdentifierTransferable()));
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

	public Collection loadMeasurementPortTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementPortType_Transferable[] transferables = this.server.transmitMeasurementPortTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadMeasurementPortTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementPortType_Transferable[] transferables = this.server.transmitMeasurementPortTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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
					.getTransferable(), getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadMonitoredElement | server.loadMonitoredElement("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadMonitoredElements(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MonitoredElement_Transferable[] transferables = this.server.transmitMonitoredElements(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MonitoredElement(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadMonitoredElementsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));
			
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable()));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadPort | new Port(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadPort | server.transmitPort(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadPorts(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Port_Transferable[] transferables = this.server.transmitPorts(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadPortsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));
			
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable()));
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadPortType | new PortType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadPortType | server.transmitPortType(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadPortTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			PortType_Transferable[] transferables = this.server.transmitPortTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadPortTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			PortType_Transferable[] transferables = this.server.transmitPortTypesButIds(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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
					.getTransferable(), getAccessIdentifierTransferable()));
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

	public Collection loadTransmissionPaths(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPath_Transferable[] transferables = this.server.transmitTransmissionPaths(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadTransmissionPathsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));
			
			Collection list = new ArrayList(transferables.length);
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
					.getTransferable(), getAccessIdentifierTransferable()));
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

	public Collection loadTransmissionPathTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPathType_Transferable[] transferables = this.server.transmitTransmissionPathTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadTransmissionPathTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPathType_Transferable[] transferables = this.server.transmitTransmissionPathTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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
				getAccessIdentifierTransferable());

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}
	
	private void updateStorableObjectHeader(Collection storableObjects, StorableObject_Transferable[] transferables) {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			Identifier_Transferable id = (Identifier_Transferable) storableObject.getId().getTransferable();
			for (int i = 0; i < transferables.length; i++) {
				if (transferables[i].id.equals(id)) {
					storableObject.updateFromHeaderTransferable(transferables[i]);
				}
			}
		}
	}

	public void saveCableThread(CableThread cableThread, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		CableThread_Transferable transferables = (CableThread_Transferable) cableThread.getTransferable();
		try {
			cableThread.updateFromHeaderTransferable(this.server.receiveCableThread(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThread ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		CableLinkType_Transferable transferables = (CableLinkType_Transferable) cableLinkType.getTransferable();
		try {
			cableLinkType.updateFromHeaderTransferable(this.server.receiveCableLinkType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableLinkType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CableLinkType_Transferable) ((CableLinkType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveCableLinkTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableLinkTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableThreads(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CableThread_Transferable[] transferables = new CableThread_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CableThread_Transferable) ((CableThread) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveCableThreads(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThreads ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		CableThreadType_Transferable transferables = (CableThreadType_Transferable) cableThreadType.getTransferable();
		try {
			cableThreadType.updateFromHeaderTransferable(this.server.receiveCableThreadType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThreadType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCableThreadTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CableThreadType_Transferable) ((CableThreadType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveCableThreadTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveCableThreadTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

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
			equipment.updateFromHeaderTransferable(this.server.receiveEquipment(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveEquipment ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEquipments(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Equipment_Transferable[] transferables = new Equipment_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Equipment_Transferable) ((Equipment) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveEquipments(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveEquipments ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		EquipmentType_Transferable transferables = (EquipmentType_Transferable) equipmentType.getTransferable();
		try {
			equipmentType.updateFromHeaderTransferable(this.server.receiveEquipmentType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveEquipmentType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEquipmentTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (EquipmentType_Transferable) ((EquipmentType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveEquipmentTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveEquipmentTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		KIS_Transferable transferables = (KIS_Transferable) kis.getTransferable();
		try {
			kis.updateFromHeaderTransferable(this.server.receiveKIS(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveKIS ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveKISs(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		KIS_Transferable[] transferables = new KIS_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (KIS_Transferable) ((KIS) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveKISs(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveKISs ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Link_Transferable transferables = (Link_Transferable) link.getTransferable();
		try {
			link.updateFromHeaderTransferable(this.server.receiveLink(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveLink ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Link_Transferable[] transferables = new Link_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Link_Transferable) ((Link) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveLinks(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveLinks ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		LinkType_Transferable transferables = (LinkType_Transferable) linkType.getTransferable();
		try {
			linkType.updateFromHeaderTransferable(this.server.receiveLinkType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveLinkType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (AbstractLinkType_Transferable) ((LinkType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveLinkTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveLinkTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		MeasurementPort_Transferable transferables = (MeasurementPort_Transferable) measurementPort.getTransferable();
		try {
			measurementPort.updateFromHeaderTransferable(this.server.receiveMeasurementPort(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMeasurementPort ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementPorts(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementPort_Transferable) ((MeasurementPort) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveMeasurementPorts(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMeasurementPorts ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		MeasurementPortType_Transferable transferables = (MeasurementPortType_Transferable) measurementPortType
				.getTransferable();
		try {
			measurementPortType.updateFromHeaderTransferable(this.server.receiveMeasurementPortType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMeasurementPortType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementPortTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementPortType_Transferable) ((MeasurementPortType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveMeasurementPortTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMeasurementPortTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		MonitoredElement_Transferable transferables = (MonitoredElement_Transferable) monitoredElement
				.getTransferable();
		try {
			monitoredElement.updateFromHeaderTransferable(this.server.receiveMonitoredElement(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMonitoredElement ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMonitoredElements(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (MonitoredElement_Transferable) ((MonitoredElement) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveMonitoredElements(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveMonitoredElements ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Port_Transferable transferables = (Port_Transferable) port.getTransferable();
		try {
			port.updateFromHeaderTransferable(this.server.receivePort(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveEquipment ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void savePorts(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Port_Transferable[] transferables = new Port_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Port_Transferable) ((Port) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receivePorts(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.savePorts ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PortType_Transferable transferables = (PortType_Transferable) portType.getTransferable();
		try {
			portType.updateFromHeaderTransferable(this.server.receivePortType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.savePortType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void savePortTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PortType_Transferable[] transferables = new PortType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (PortType_Transferable) ((PortType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receivePortTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.savePortTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		TransmissionPath_Transferable transferables = (TransmissionPath_Transferable) transmissionPath
				.getTransferable();
		try {
			transmissionPath.updateFromHeaderTransferable(this.server.receiveTransmissionPath(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPath ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPaths(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (TransmissionPath_Transferable) ((TransmissionPath) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveTransmissionPaths(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPaths ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		TransmissionPathType_Transferable transferables = (TransmissionPathType_Transferable) transmissionPathType
				.getTransferable();
		try {
			transmissionPathType.updateFromHeaderTransferable(this.server.receiveTransmissionPathType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPathType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTransmissionPathTypes(Collection list, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (TransmissionPathType_Transferable) ((TransmissionPathType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveTransmissionPathTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.saveTransmissionPathTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	// public void savePermissionAttributes(PermissionAttributes
	// permissionAttributes) throws VersionCollisionException,
	// DatabaseException, CommunicationException{
	// TODO auto generated stub
	// }

}
