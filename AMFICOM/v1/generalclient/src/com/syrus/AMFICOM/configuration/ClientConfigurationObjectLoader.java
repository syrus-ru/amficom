/*
 * $Id: ClientConfigurationObjectLoader.java,v 1.27 2005/04/13 14:02:14 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkTypeSort;
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
import com.syrus.AMFICOM.general.AbstractClientObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27 $, $Date: 2005/04/13 14:02:14 $
 * @author $Author: bob $
 * @module generalclient_v1
 */

public final class ClientConfigurationObjectLoader  extends AbstractClientObjectLoader implements ConfigurationObjectLoader {

	private CMServer								server;
	
	private AccessIdentifier_Transferable getAccessIdentifierTransferable() {
		return  (AccessIdentifier_Transferable) SessionContext.getAccessIdentity().getTransferable();
	}
	
	public ClientConfigurationObjectLoader(CMServer server) {
		this.server = server;
	}

	public void delete(Identifier id) {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			Log.errorException(e);
		}
	}

	public void delete(Set ids) {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			Log.errorException(e);
		}
	}
	
	private StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		StorableObject so = null;
		try {
			so = ConfigurationStorableObjectPool.getStorableObject(id, false);
		} catch (ApplicationException e) {
			// nothing do
		}
		if (so != null)
			super.fromTransferable(so, transferable);
		return so;
	}

	public CableLinkType loadCableLinkType(Identifier id) throws ApplicationException {
		try {
			CableLinkType_Transferable cltt = this.server.transmitCableLinkType((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());			
			CableLinkType cableLinkType = (CableLinkType) this.fromTransferable(id, cltt);
			if (cableLinkType == null)
				cableLinkType = new CableLinkType(cltt);
			return cableLinkType;	
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCableLinkType | server.transmitCableLinkType(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public CableThread loadCableThread(Identifier id) throws ApplicationException {
		try {
			CableThread_Transferable ctt = this.server.transmitCableThread((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());			
			CableThread cableThread = (CableThread) this.fromTransferable(id, ctt);
			if (cableThread == null)
				cableThread = new CableThread(ctt);
			return cableThread;			
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCableThread | server.transmitCableThread(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public CableThreadType loadCableThreadType(Identifier id) throws ApplicationException {
		try {
			CableThreadType_Transferable cttt = this.server.transmitCableThreadType((Identifier_Transferable) id
				.getTransferable(), getAccessIdentifierTransferable());
			CableThreadType cableThreadType = (CableThreadType) this.fromTransferable(id, cttt);
			if (cableThreadType == null)
				cableThreadType = new CableThreadType(cttt);
			return cableThreadType;	
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationLoader.loadCharacteristicType | server.transmitCharacteristicType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableLinkType_Transferable[] transferables = this.server.transmitCableLinkTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {			
				CableLinkType cableLinkType = (CableLinkType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (cableLinkType == null)
					cableLinkType = new CableLinkType(transferables[j]);
				set.add(cableLinkType);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThread_Transferable[] transferables = this.server.transmitCableThreads(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				CableThread cableThread = (CableThread) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (cableThread == null)
					cableThread = new CableThread(transferables[j]);
				set.add(cableThread);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CableThreadType_Transferable[] transferables = this.server.transmitCableThreadTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				CableThreadType cableThreadType = (CableThreadType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (cableThreadType == null)
					cableThreadType = new CableThreadType(transferables[j]);
				set.add(cableThreadType);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new CableThreadType(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Equipment loadEquipment(Identifier id) throws ApplicationException {
		try {
			Equipment_Transferable et = this.server.transmitEquipment((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			Equipment equipment = (Equipment) this.fromTransferable(id, et);
			if (equipment == null)
				equipment = new Equipment(et);
			return equipment;
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadAEquipment | new Equipment(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipment | server.transmitEquipment(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Equipment_Transferable[] transferables = this.server.transmitEquipments(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Equipment equipment = (Equipment) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (equipment == null)
					equipment = new Equipment(transferables[j]);
				set.add(equipment);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));			
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Equipment(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public EquipmentType loadEquipmentType(Identifier id) throws ApplicationException {
		try {
			EquipmentType_Transferable ett = this.server.transmitEquipmentType((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			EquipmentType equipmentType = (EquipmentType) this.fromTransferable(id, ett);
			if (equipmentType == null)
				equipmentType = new EquipmentType(ett);
			return equipmentType;
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipmentType | new EquipmentType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadEquipmentType | server.transmitEquipmentType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EquipmentType_Transferable[] transferables = this.server.transmitEquipmentTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				EquipmentType equipmentType = (EquipmentType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (equipmentType == null)
					equipmentType = new EquipmentType(transferables[j]);
				set.add(equipmentType);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new EquipmentType(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public KIS loadKIS(Identifier id) throws ApplicationException {
		try {
			KIS_Transferable kt = this.server.transmitKIS((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			KIS kis = (KIS) this.fromTransferable(id, kt);
			if (kis == null)
				kis = new KIS(kt);
			return kis;			
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadKIS | new KIS(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadKIS | server.transmitKIS(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			KIS_Transferable[] transferables = this.server.transmitKISs(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				KIS kis = (KIS) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (kis == null)
					kis = new KIS(transferables[j]);
				set.add(kis);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new KIS(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Link loadLink(Identifier id) throws ApplicationException {
		try {
			Link_Transferable lt = this.server.transmitLink((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			Link link = (Link) this.fromTransferable(id, lt);
			if (link == null)
				link = new Link(lt);
			return link;
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadLink | new Link(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadLink | server.transmitLink(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Link_Transferable[] transferables = this.server.transmitLinks(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Link link = (Link) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (link == null)
					link = new Link(transferables[j]);
				set.add(link);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Link(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public LinkType loadLinkType(Identifier id) throws ApplicationException {
		try {
			LinkType_Transferable ltt = this.server.transmitLinkType((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			LinkType linkType = (LinkType) this.fromTransferable(id, ltt);
			if (linkType == null)
				linkType = new LinkType(ltt);
			return linkType;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadKISType | server.transmitKISType(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadKISType | server.transmitKISType(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AbstractLinkType_Transferable[] transferables = this.server.transmitLinkTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {

				LinkType linkType;
				CableLinkType cableLinkType;
				switch (transferables[i].discriminator().value()) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						cableLinkType = (CableLinkType) this.fromTransferable(new Identifier(transferables[i].cableLinkType().header.id), transferables[i].cableLinkType());
						if (cableLinkType == null)
							cableLinkType = new CableLinkType(transferables[i].cableLinkType());
						set.add(cableLinkType);
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						linkType = (LinkType) this.fromTransferable(new Identifier(transferables[i].linkType().header.id), transferables[i].linkType());
						if (linkType == null)
							linkType = new LinkType(transferables[i].linkType());
						set.add(linkType);
						break;
					default:
						throw new CommunicationException("ClientConfigurationObjectLoader.loadLinkTypesButIds"
								+ " | Wrong AbstractLinkTypeSort");
				}
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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

			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {

				LinkType linkType;
				CableLinkType cableLinkType;
				switch (transferables[i].discriminator().value()) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						cableLinkType = new CableLinkType(transferables[i].cableLinkType());
						set.add(cableLinkType);
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						linkType = new LinkType(transferables[i].linkType());
						set.add(linkType);
						break;
					default:
						throw new CommunicationException("ClientConfigurationObjectLoader.loadLinkTypesButIds"
								+ " | Wrong AbstractLinkTypeSort");
				}
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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
				StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new CableLinkType(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			CableThread_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitCableThreadsButIdsCondition(identifierTransferables, getAccessIdentifierTransferable(),
				StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new CableThread(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws ApplicationException {
		try {
			MeasurementPort_Transferable mpt = this.server.transmitMeasurementPort((Identifier_Transferable) id
				.getTransferable(), getAccessIdentifierTransferable());
			MeasurementPort measurementPort = (MeasurementPort) this.fromTransferable(id, mpt);
			if (measurementPort == null)
				measurementPort = new MeasurementPort(mpt);
			return measurementPort;
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

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementPort_Transferable[] transferables = this.server.transmitMeasurementPorts(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				MeasurementPort measurementPort = (MeasurementPort) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (measurementPort == null)
					measurementPort = new MeasurementPort(transferables[j]);
				set.add(measurementPort);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
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
				StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new MeasurementPort(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws ApplicationException {
		try {
			MeasurementPortType_Transferable mptt = this.server.transmitMeasurementPortType((Identifier_Transferable) id
				.getTransferable(), getAccessIdentifierTransferable());
			MeasurementPortType measurementPortType = (MeasurementPortType) this.fromTransferable(id, mptt);
			if (measurementPortType == null)
				measurementPortType = new MeasurementPortType(mptt);
			return measurementPortType;
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

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementPortType_Transferable[] transferables = this.server.transmitMeasurementPortTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				MeasurementPortType measurementPortType = (MeasurementPortType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (measurementPortType == null)
					measurementPortType = new MeasurementPortType(transferables[j]);
				set.add(measurementPortType);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementPortType_Transferable[] transferables = this.server.transmitMeasurementPortTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new MeasurementPortType(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws ApplicationException {
		try {
			MonitoredElement_Transferable met = this.server.transmitMonitoredElement((Identifier_Transferable) id
				.getTransferable(), getAccessIdentifierTransferable());
			MonitoredElement measurementPortType = (MonitoredElement) this.fromTransferable(id, met);
			if (measurementPortType == null)
				measurementPortType = new MonitoredElement(met);
			return measurementPortType;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadMonitoredElement | server.loadMonitoredElement("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		}
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MonitoredElement_Transferable[] transferables = this.server.transmitMonitoredElements(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				MonitoredElement monitoredElement = (MonitoredElement) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (monitoredElement == null)
					monitoredElement = new MonitoredElement(transferables[j]);
				set.add(monitoredElement);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MonitoredElement_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			
			transferables = this.server.transmitMonitoredElementsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new MonitoredElement(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Port loadPort(Identifier id) throws ApplicationException {
		try {
			Port_Transferable pt = this.server.transmitPort((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			Port port = (Port) this.fromTransferable(id, pt);
			if (port == null)
				port = new Port(pt);
			return port;			
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadPort | new Port(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadPort | server.transmitPort(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Port_Transferable[] transferables = this.server.transmitPorts(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Port port = (Port) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (port == null)
					port = new Port(transferables[j]);
				set.add(port);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Port_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitPortsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Port(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public PortType loadPortType(Identifier id) throws ApplicationException {
		try {
			PortType_Transferable ptt = this.server.transmitPortType((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			PortType portType = (PortType) this.fromTransferable(id, ptt);
			if (portType == null)
				portType = new PortType(ptt);
			return portType;			
		} catch (CreateObjectException e) {
			String msg = "ClientConfigurationObjectLoader.loadPortType | new PortType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientConfigurationObjectLoader.loadPortType | server.transmitPortType(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			PortType_Transferable[] transferables = this.server.transmitPortTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				PortType portType = (PortType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (portType == null)
					portType = new PortType(transferables[j]);
				set.add(portType);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			PortType_Transferable[] transferables = this.server.transmitPortTypesButIds(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new PortType(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws ApplicationException {
		try {
			TransmissionPath_Transferable tpt = this.server.transmitTransmissionPath((Identifier_Transferable) id
				.getTransferable(), getAccessIdentifierTransferable());
			TransmissionPath transmissionPath = (TransmissionPath) this.fromTransferable(id, tpt);
			if (transmissionPath == null)
				transmissionPath = new TransmissionPath(tpt);
			return transmissionPath;			
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

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPath_Transferable[] transferables = this.server.transmitTransmissionPaths(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				TransmissionPath transmissionPath = (TransmissionPath) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (transmissionPath == null)
					transmissionPath = new TransmissionPath(transferables[j]);
				set.add(transmissionPath);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			TransmissionPath_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitTransmissionPathsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new TransmissionPath(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws ApplicationException {
		try {
			TransmissionPathType_Transferable tptt = this.server.transmitTransmissionPathType((Identifier_Transferable) id
				.getTransferable(), getAccessIdentifierTransferable());
			TransmissionPathType transmissionPathType = (TransmissionPathType) this.fromTransferable(id, tptt);
			if (transmissionPathType == null)
				transmissionPathType = new TransmissionPathType(tptt);
			return transmissionPathType;
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

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPathType_Transferable[] transferables = this.server.transmitTransmissionPathTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				TransmissionPathType transmissionPathType = (TransmissionPathType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (transmissionPathType == null)
					transmissionPathType = new TransmissionPathType(transferables[j]);
				set.add(transmissionPathType);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TransmissionPathType_Transferable[] transferables = this.server.transmitTransmissionPathTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new TransmissionPathType(transferables[j]));
			}
			return set;
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
	
	private void updateStorableObjectHeader(Set storableObjects, StorableObject_Transferable[] transferables) {
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

	public void saveCableLinkTypes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveCableThreads(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveCableThreadTypes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveEquipments(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveEquipmentTypes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveKISs(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveLinks(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveLinkTypes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveMeasurementPorts(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveMeasurementPortTypes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void saveMonitoredElements(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void savePorts(Set list, boolean force) throws VersionCollisionException, DatabaseException,
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

	public void savePortTypes(Set set, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PortType_Transferable[] transferables = new PortType_Transferable[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			transferables[i] = (PortType_Transferable) ((PortType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(set, this.server.receivePortTypes(transferables, force, getAccessIdentifierTransferable()));
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

	public void saveTransmissionPaths(Set set, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			transferables[i] = (TransmissionPath_Transferable) ((TransmissionPath) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(set, this.server.receiveTransmissionPaths(transferables, force, getAccessIdentifierTransferable()));
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

	public void saveTransmissionPathTypes(Set set, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			transferables[i] = (TransmissionPathType_Transferable) ((TransmissionPathType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(set, this.server.receiveTransmissionPathTypes(transferables, force, getAccessIdentifierTransferable()));
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
