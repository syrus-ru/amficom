/*
 * $Id: Collector.java,v 1.9 2004/12/09 16:51:08 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2004/12/09 16:51:08 $
 * @author $Author: bob $
 * @module map_v1
 */
public class Collector extends StorableObject implements Characterized {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 4049922679379212598L;
	
	private String					name;
	private String					description;

	private List					physicalLinks;
	private List					characteristics;

	private StorableObjectDatabase	collectorDatabase;

	public Collector(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
		try {
			this.collectorDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Collector(Collector_Transferable mt) throws CreateObjectException {
		super(mt.header);
		this.name = mt.name;
		this.description = mt.description;

		try {
			this.physicalLinks = new ArrayList(mt.physicalLinkIds.length);
			ArrayList physicalLinkIds = new ArrayList(mt.physicalLinkIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				physicalLinkIds.add(new Identifier(mt.physicalLinkIds[i]));
			this.physicalLinks.addAll(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));

			this.characteristics = new ArrayList(mt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(mt.characteristicIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(mt.characteristicIds[i]));
			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Collector(final Identifier id,
					final Identifier creatorId,
					final String name,
					final String description) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.description = description;		

		this.physicalLinks = new LinkedList();
		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
	}

	public static Collector getInstance(Collector_Transferable plt) throws CreateObjectException {
		Collector collector = new Collector(plt);

		collector.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
		try {
			if (collector.collectorDatabase != null)
				collector.collectorDatabase.insert(collector);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return collector;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.addAll(this.physicalLinks);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] physicalLinkIds = new Identifier_Transferable[this.physicalLinks.size()];
		for (Iterator iterator = this.physicalLinks.iterator(); iterator.hasNext();)
			physicalLinkIds[i++] = (Identifier_Transferable) ((PhysicalLink) iterator.next()).getId().getTransferable();
		
		i= 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		return new Collector_Transferable(super.getHeaderTransferable(),
								this.name,
								this.description,
								physicalLinkIds,
								charIds);
	}

	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}	

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	public List getPhysicalLinks() {
		return  Collections.unmodifiableList(this.physicalLinks);
	}
	
	protected void setPhysicalLinks0(List physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
	}
	
	public void setPhysicalLinks(List physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		super.currentVersion = super.getNextVersion();		
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  String description) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.description = description;					
	}
}
