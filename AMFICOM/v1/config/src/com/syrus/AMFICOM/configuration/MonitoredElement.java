/*
 * $Id: MonitoredElement.java,v 1.53 2005/06/02 14:27:03 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.53 $, $Date: 2005/06/02 14:27:03 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class MonitoredElement extends DomainMember {
	private static final long serialVersionUID = 5689746173688711494L;

	private Identifier measurementPortId;
	private int sort;
	private String name;
	private String localAddress;

	private Set monitoredDomainMemberIds;

	MonitoredElement(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredDomainMemberIds = new HashSet();

		MonitoredElementDatabase database = (MonitoredElementDatabase) DatabaseContext.getDatabase(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MonitoredElement(MonitoredElement_Transferable met) throws CreateObjectException {
		this.fromTransferable(met);
	}
	
	MonitoredElement(Identifier id,
								 Identifier creatorId,
								 long version,
								 Identifier domainId,
								 String name,
								 Identifier measurementPortId,
								 int sort,
								 String localAddress,
								 Set monitoredDomainMemberIds) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version,
			domainId);
		this.name = name;
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;

		this.monitoredDomainMemberIds = new HashSet();
		this.setMonitoredDomainMemberIds0(monitoredDomainMemberIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param measurementPortId
	 * @param sort
	 * @param localAddress
	 * @throws CreateObjectException
	 */
	public static MonitoredElement createInstance(Identifier creatorId,
												  Identifier domainId,
												  String name,
												  Identifier measurementPortId,
												  MonitoredElementSort sort,
												  String localAddress,
												  Set monitoredDomainMemberIds) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || measurementPortId == null ||
				localAddress == null || monitoredDomainMemberIds == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			MonitoredElement monitoredElement =  new MonitoredElement(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE),
								creatorId,
								0L,
								domainId,
								name,
								measurementPortId,
								sort.value(),
								localAddress,
								monitoredDomainMemberIds);

			assert monitoredElement.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			monitoredElement.changed = true;
			try {
				StorableObjectPool.putStorableObject(monitoredElement);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

			return monitoredElement;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		MonitoredElement_Transferable met = (MonitoredElement_Transferable) transferable;
		super.fromTransferable(met.header, new Identifier(met.domain_id));
		this.measurementPortId = new Identifier(met.measurement_port_id);
		this.sort = met.sort.value();
		this.localAddress = met.local_address;

		this.name = met.name;

		this.monitoredDomainMemberIds = Identifier.fromTransferables(met.monitored_domain_member_ids);
	}

	public IDLEntity getTransferable() {
		Identifier_Transferable[] mdmIds = new Identifier_Transferable[this.monitoredDomainMemberIds.size()];
		int i = 0;
		for (Iterator it = this.monitoredDomainMemberIds.iterator(); it.hasNext();)
			mdmIds[i++] = (Identifier_Transferable) ((Identifier) it.next()).getTransferable();
		return new MonitoredElement_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				this.name,
				(Identifier_Transferable) this.measurementPortId.getTransferable(),
				MonitoredElementSort.from_int(this.sort),
				this.localAddress,
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
	
	public void setLocalAddress(String localAddress){
		super.changed = true;
		this.localAddress = localAddress;
	}

	public Set getMonitoredDomainMemberIds() {
		return Collections.unmodifiableSet(this.monitoredDomainMemberIds);
	}

	protected synchronized void setMonitoredDomainMemberIds0(Set monitoredDomainMemberIds) {
		this.monitoredDomainMemberIds.clear();
		if (monitoredDomainMemberIds != null)
			this.monitoredDomainMemberIds.addAll(monitoredDomainMemberIds);
	}

	protected synchronized void setMonitoredDomainMemberIds(Set monitoredDomainMemberIds) {
		this.setMonitoredDomainMemberIds0(monitoredDomainMemberIds);
		super.changed = true;
	}

	protected synchronized void setAttributes(Date created,
											Date modified,
											Identifier creatorId,
											Identifier modifierId,
											long version,
											Identifier domainId,
											String name,
											Identifier measurementPortId,
											int sort,
											String localAddress) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							domainId);
		this.name = name;
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.addAll(this.monitoredDomainMemberIds);
		dependencies.add(this.measurementPortId);
		return dependencies;
	}
	/**
	 * @param measurementPortId The measurementPortId to set.
	 */
	public void setMeasurementPortId(Identifier measurementPortId) {
		this.measurementPortId = measurementPortId;
		super.changed = true;
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(MonitoredElementSort sort) {
		this.sort = sort.value();
		super.changed = true;
	}
}
