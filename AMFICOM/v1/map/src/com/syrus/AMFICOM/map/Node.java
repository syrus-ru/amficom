/*
 * $Id: Node.java,v 1.1 2004/11/26 09:23:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/26 09:23:37 $
 * @author $Author: bob $
 * @module map_v1
 */
public abstract class Node extends StorableObject implements Characterized {

	protected List		characteristics;

	protected String	name;

	protected String	description;

	protected double	longitude;

	protected double	latitude;

	protected Node(Identifier id) {
		super(id);
		this.characteristics = new LinkedList();
	}

	protected Node(Identifier id,
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			String name,
			String desription,
			double longitude,
			double latitude) {
		super(id, created, modified, creatorId, modifierId);
		this.name = name;
		this.description = desription;
		this.longitude = longitude;
		this.latitude = latitude;
		this.characteristics = new LinkedList();
	}

	protected Node(StorableObject_Transferable transferable) {
		super(transferable);
		this.characteristics = new LinkedList();
	}

	public List getCharacteristics() {
		return this.characteristics;
	}

	public String getDescription() {
		return this.description;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public String getName() {
		return this.name;
	}

	public void setCharacteristics(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		super.currentVersion = super.getNextVersion();
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		super.currentVersion = super.getNextVersion();
	}

	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
}
