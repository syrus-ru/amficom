/*
 * $Id: AbstractNode.java,v 1.2 2004/12/03 17:54:58 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/12/03 17:54:58 $
 * @author $Author: bob $
 * @module map_v1
 */
public abstract class AbstractNode extends StorableObject implements Characterized {

	static final long serialVersionUID = -2623880496462305233L;

	protected List		characteristics;

	protected String	name;

	protected String	description;

	protected double	longitude;

	protected double	latitude;

	protected AbstractNode(Identifier id) {
		super(id);
		this.characteristics = new LinkedList();
	}

	protected AbstractNode(Identifier id,
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

	protected AbstractNode(StorableObject_Transferable transferable) {
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

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
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
