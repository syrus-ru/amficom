/*
 * $Id: SiteNode.java,v 1.1 2004/11/26 09:23:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/26 09:23:37 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNode extends Node implements TypedObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257567325699190835L;
	private Identifier				imageId;
	private SiteNodeType			type;

	private String					city;
	private String					street;
	private String					building;

	private StorableObjectDatabase	siteNodeDatabase;

	public SiteNode(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.siteNodeDatabase = MapDatabaseContext.siteNodeDatabase;
		try {
			this.siteNodeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public SiteNode(SiteNode_Transferable snt) throws CreateObjectException {
		super(snt.header);
		super.name = snt.name;
		super.description = snt.description;
		super.longitude = snt.longitude;
		super.latitude = snt.latitude;
		this.imageId = new Identifier(snt.imageId);		
		this.city = snt.city;
		this.street = snt.street;
		this.building = snt.building;

		try {
			this.type = (SiteNodeType) MapStorableObjectPool.getStorableObject(new Identifier(snt.siteNodeTypeId), true);
			
			this.characteristics = new ArrayList(snt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(snt.characteristicIds.length);
			for (int i = 0; i < snt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(snt.characteristicIds[i]));

			this.characteristics = ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected SiteNode(final Identifier id,
			final Identifier creatorId,
			final Identifier imageId,
			String name,
			String description,
			SiteNodeType type,
			double longitude,
			double latitude,
			String city,
			String steet,
			String building) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.imageId = imageId;
		this.type = type;
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
		this.street = steet;
		this.building = building;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.siteNodeDatabase = MapDatabaseContext.siteNodeDatabase;
	}

	public static SiteNode getInstance(SiteNode_Transferable sntt) throws CreateObjectException {
		SiteNode siteNodeType = new SiteNode(sntt);

		siteNodeType.siteNodeDatabase = MapDatabaseContext.siteNodeDatabase;
		try {
			if (siteNodeType.siteNodeDatabase != null)
				siteNodeType.siteNodeDatabase.insert(siteNodeType);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return siteNodeType;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.add(this.imageId);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new SiteNode_Transferable(super.getHeaderTransferable(), 
							this.name,
							this.description,
							this.longitude,
							this.latitude, 
							(Identifier_Transferable) this.imageId.getTransferable(),
							(Identifier_Transferable) this.type.getId().getTransferable(),
							this.city,
							this.street, 
							this.building, 
							charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getBuilding() {
		return this.building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
		super.currentVersion = super.getNextVersion();
	}
	
	public Identifier getImageId() {
		return this.imageId;
	}
	
	public void setImageId(Identifier imageId) {
		this.imageId = imageId;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public void setStreet(String street) {
		this.street = street;
		super.currentVersion = super.getNextVersion();
	}
}
