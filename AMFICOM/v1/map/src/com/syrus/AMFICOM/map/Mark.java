/*
 * $Id: Mark.java,v 1.2 2004/11/28 14:34:48 bob Exp $
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
import com.syrus.AMFICOM.map.corba.Mark_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2004/11/28 14:34:48 $
 * @author $Author: bob $
 * @module map_v1
 */
public class Mark extends StorableObject implements Characterized {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258126938496186164L;
	private String					name;
	private String					description;

	private double					longitude;
	private double					latitude;
	private PhysicalLink			physicalLink;

	private double					distance;

	private String					city;
	private String					street;
	private String					building;

	private List					characteristics;

	private StorableObjectDatabase	markDatabase;

	public Mark(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.markDatabase = MapDatabaseContext.markDatabase;
		try {
			this.markDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Mark(Mark_Transferable mt) throws CreateObjectException {
		super(mt.header);
		this.name = mt.name;
		this.description = mt.description;

		this.longitude = mt.longitude;
		this.latitude = mt.latitude;

		this.distance = mt.distance;

		this.city = mt.city;
		this.street = mt.street;
		this.building = mt.building;

		try {
			this.physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(
				new Identifier(mt.physicalLinkId), true);

			this.characteristics = new ArrayList(mt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(mt.characteristicIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(mt.characteristicIds[i]));
			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Mark(final Identifier id, 
				   final Identifier creatorId, 
				   final String name, 
				   final String description,
				   final double longitude, 
				   final double latitude, 
				   final PhysicalLink physicalLink,
				   final double distance,
				   final String city, 
				   final String street, 
				   final String building) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;		
		this.physicalLink = physicalLink;
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.markDatabase = MapDatabaseContext.markDatabase;
	}

	public static Mark getInstance(Mark_Transferable plt) throws CreateObjectException {
		Mark physicalLink = new Mark(plt);

		physicalLink.markDatabase = MapDatabaseContext.markDatabase;
		try {
			if (physicalLink.markDatabase != null)
				physicalLink.markDatabase.insert(physicalLink);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return physicalLink;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.physicalLink);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		return new Mark_Transferable(super.getHeaderTransferable(),
						this.name,
						this.description,
						this.longitude,
						this.latitude,
						(Identifier_Transferable)this.physicalLink.getId().getTransferable(),
						this.distance,
						this.city,
						this.street,
						this.building,
						charIds);
	}
	
	public List getCharacteristics() {
		return this.characteristics;
	}
	
	public void setCharacteristics(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
		super.currentVersion = super.getNextVersion();
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
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getDistance() {
		return this.distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}
	
	public void setPhysicalLink(PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
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
