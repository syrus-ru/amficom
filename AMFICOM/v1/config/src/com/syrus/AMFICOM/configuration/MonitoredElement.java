/*
 * $Id: MonitoredElement.java,v 1.19 2004/09/01 15:08:01 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;

/**
 * @version $Revision: 1.19 $, $Date: 2004/09/01 15:08:01 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MonitoredElement extends DomainMember {
	private Identifier measurementPortId;
	private int sort;
	private String localAddress;

	private List monitoredDomainMemberIds;

	private StorableObjectDatabase monitoredElementDatabase;

	public MonitoredElement(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			this.monitoredElementDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MonitoredElement(MonitoredElement_Transferable met) throws CreateObjectException {
		super(new Identifier(met.id),
					new Date(met.created),
					new Date(met.modified),
					new Identifier(met.creator_id),
					new Identifier(met.modifier_id),
					new Identifier(met.domain_id));
		this.measurementPortId = new Identifier(met.measurement_port_id);
		this.sort = met.sort.value();
		this.localAddress = new String(met.local_address);

		this.monitoredDomainMemberIds = new ArrayList(met.monitored_domain_member_ids.length);
		for (int i= 0; i < met.monitored_domain_member_ids.length; i++)
			this.monitoredDomainMemberIds.add(new Identifier(met.monitored_domain_member_ids[i]));		
	}
	
	protected MonitoredElement(Identifier id,
													 Identifier creatorId,
													 Identifier domainId,
													 Identifier measurementPortId,
													 int sort,
													 String localAddress,
													 List monitoredDomainMemberIds) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;
		this.monitoredDomainMemberIds = monitoredDomainMemberIds;
		
		this.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
	}
	
	/**
	 * create new instance for client
	 * @param id
	 * @param creatorId
	 * @param domainId
	 * @param measurementPortId
	 * @param sort
	 * @param localAddress
	 * @return
	 */
	public static MonitoredElement createInstance(Identifier id,
																								Identifier creatorId,
																								Identifier domainId,
																								Identifier measurementPortId,
																								int sort,
																								String localAddress,
																								List monitoredDomainMemberIds) {
		return new MonitoredElement(id,
																creatorId,
																domainId,
																measurementPortId,
																sort,
																localAddress,
																monitoredDomainMemberIds);
	}

	public static MonitoredElement getInstance(MonitoredElement_Transferable met) throws CreateObjectException {
		MonitoredElement me = new MonitoredElement(met);
		
		me.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			if (me.monitoredElementDatabase != null)
				me.monitoredElementDatabase.insert(me);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return me;
	}
	
	public Object getTransferable() {
		Identifier_Transferable[] mdmIds = new Identifier_Transferable[this.monitoredDomainMemberIds.size()];
		int i = 0;
		for (Iterator it = this.monitoredDomainMemberIds.iterator(); it.hasNext();)
			mdmIds[i++] = (Identifier_Transferable)((Identifier)it.next()).getTransferable();
		return new MonitoredElement_Transferable((Identifier_Transferable)super.id.getTransferable(),
																						 super.created.getTime(),
																						 super.modified.getTime(),
																						 (Identifier_Transferable)super.creatorId.getTransferable(),
																						 (Identifier_Transferable)super.modifierId.getTransferable(),
																						 (Identifier_Transferable)super.domainId.getTransferable(),
																						 (Identifier_Transferable)this.measurementPortId.getTransferable(),
																						 MonitoredElementSort.from_int(this.sort),
																						 new String(this.localAddress),
																						 mdmIds);
	}

	public Identifier getMeasurementPortId() {
		return this.measurementPortId;
	}

	public MonitoredElementSort getSort() {
		return MonitoredElementSort.from_int(this.sort);
	}

	public String getLocalAddress() {
		return this.localAddress;
	}

	public List getMonitoredDomainMemberIds() {
		return this.monitoredDomainMemberIds;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domainId,
																						Identifier measurementPortId,
																						int sort,
																						String localAddress) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												domainId);
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;
	}

	protected synchronized void setMonitoredDomainMemberIds(List monitoredDomainMemberIds) {
		this.monitoredDomainMemberIds = monitoredDomainMemberIds;
	}
}
