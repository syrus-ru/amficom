/*
 * $Id: TransmissionPath.java,v 1.13 2004/08/30 14:39:41 bob Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
/**
 * @version $Revision: 1.13 $, $Date: 2004/08/30 14:39:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class TransmissionPath extends MonitoredDomainMember implements Characterized {
	
	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;
	
	private List characteristics;
	private String name;
	private String description;	
	private Identifier startPortId;
	private Identifier finishPortId;

	private StorableObjectDatabase transmissionPathDatabase;

	public TransmissionPath(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			this.transmissionPathDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}
	
	protected TransmissionPath(Identifier id,
								Identifier creatorId,
								Identifier domainId,
								String name,
								String description,
								Identifier startPortId,
								Identifier finishPortId){
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				domainId);
		this.name = name;
		this.description = description;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;
		
		this.characteristics = new ArrayList();
		super.monitoredElementIds = new ArrayList();
		
		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param startPortId
	 * @param finishPortId
	 * @return
	 */
	public static TransmissionPath createInstance(Identifier id,
													Identifier creatorId,
													Identifier domainId,
													String name,
													String description,
													Identifier startPortId,
													Identifier finishPortId) {
				return new TransmissionPath(id,
					 creatorId,					 
					 domainId,
					 name,
					 description,
					 startPortId,
					 finishPortId);
		}


	public TransmissionPath(TransmissionPath_Transferable tpt) throws CreateObjectException {
		super(new Identifier(tpt.id),
					new Date(tpt.created),
					new Date(tpt.modified),
					new Identifier(tpt.creator_id),
					new Identifier(tpt.modifier_id),
					new Identifier(tpt.domain_id));
		super.monitoredElementIds = new ArrayList(tpt.monitored_element_ids.length);
		for (int i = 0; i < tpt.monitored_element_ids.length; i++)
			super.monitoredElementIds.add(new Identifier(tpt.monitored_element_ids[i]));

		this.name = new String(tpt.name);
		this.description = new String(tpt.description);
		this.startPortId = new Identifier(tpt.start_port_id);
		this.finishPortId = new Identifier(tpt.finish_port_id);

		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			this.transmissionPathDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}

		try {
			this.characteristics = new ArrayList(tpt.characteristic_ids.length);
			for (int i = 0; i < tpt.characteristic_ids.length; i++)
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(tpt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}
	
	public Object getTransferable() {
		int i = 0;
		
		Identifier_Transferable[] meIds = new Identifier_Transferable[super.monitoredElementIds.size()];
		for (Iterator iterator = super.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;		
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
		

		return new TransmissionPath_Transferable((Identifier_Transferable)super.id.getTransferable(),
																						 super.created.getTime(),
																						 super.modified.getTime(),
																						 (Identifier_Transferable)super.creatorId.getTransferable(),
																						 (Identifier_Transferable)super.modifierId.getTransferable(),
																						 (Identifier_Transferable)super.domainId.getTransferable(),
																						 meIds,
																						 new String(this.name),
																						 new String(this.description),
																						 (Identifier_Transferable)this.startPortId.getTransferable(),
																						 (Identifier_Transferable)this.finishPortId.getTransferable(),
																						 charIds);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public Identifier getFinishPortId() {
		return this.finishPortId;
	}

	public Identifier getStartPortId() {
		return this.startPortId;
	}

	public List getCharacteristics() {
		return this.characteristics;
	}
	
	/**
	 * @param characteristicIds The characteristicIds to set.
	 */
	public void setCharacteristics(List characteristics) {
		this.characteristics = characteristics;
	}
	
	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						String name,
																						String description,
																						Identifier startPortId,
																						Identifier finishPortId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);		
		this.name = name;
		this.description = description;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;
	}
}
