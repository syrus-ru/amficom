/*
 * $Id: TransmissionPath.java,v 1.4 2004/08/09 08:38:48 bob Exp $
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
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
/**
 * @version $Revision: 1.4 $, $Date: 2004/08/09 08:38:48 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class TransmissionPath extends MonitoredDomainMember implements Characterized {
	
	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;
	
	private List characteristicIds;
	private String name;
	private String description;	

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

		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			this.transmissionPathDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}
	
	public Object getTransferable() {
		int i = 0;
		
		Identifier_Transferable[] meIds = new Identifier_Transferable[super.monitoredElementIds.size()];
		for (Iterator iterator = super.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;		
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristicIds.size()];
		for (Iterator iterator = this.characteristicIds.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();		
		

		return new TransmissionPath_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creatorId.getTransferable(),
																			(Identifier_Transferable)super.modifierId.getTransferable(),
																			(Identifier_Transferable)super.domainId.getTransferable(),																			
																			meIds,
																			new String(this.name),
																			new String(this.description),
																			charIds);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public List getCharacteristicIds() {
		return this.characteristicIds;
	}
	
	/**
	 * @param characteristicIds The characteristicIds to set.
	 */
	public void setCharacteristicIds(List characteristicIds) {
		this.characteristicIds = characteristicIds;
	}
	
	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												Identifier domainId,												
												String name,
												String description) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							domainId);		
		this.name = name;
		this.description = description;
	}
	
}
