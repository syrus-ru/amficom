/*
 * $Id: MonitoredElement.java,v 1.14 2004/08/13 14:08:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
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
 * @version $Revision: 1.14 $, $Date: 2004/08/13 14:08:15 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MonitoredElement extends DomainMember {
	private Identifier measurementPortId;
	private int sort;
	private String localAddress;

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
		this.measurementPortId = new Identifier(met.meport_id);
		this.sort = met.sort.value();
		this.localAddress = new String(met.local_address);

		this.monitoredElementDatabase = ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			this.monitoredElementDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}
	
	private MonitoredElement(Identifier id,
						Identifier creatorId,
						Identifier domainId,
						Identifier measurementPortId,
						int sort,
						String localAddress){
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				domainId);
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;
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
												  String localAddress){
		return new MonitoredElement(id,
									creatorId,
									domainId,
									measurementPortId,
									sort,
									localAddress);
	}

	public Object getTransferable() {
		return new MonitoredElement_Transferable((Identifier_Transferable)super.id.getTransferable(),
																						 super.created.getTime(),
																						 super.modified.getTime(),
																						 (Identifier_Transferable)super.creatorId.getTransferable(),
																						 (Identifier_Transferable)super.modifierId.getTransferable(),
																						 (Identifier_Transferable)super.domainId.getTransferable(),
																						 (Identifier_Transferable)this.measurementPortId.getTransferable(),
																						 MonitoredElementSort.from_int(this.sort),
																						 new String(this.localAddress));
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
}
