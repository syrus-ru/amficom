/*
 * $Id: TopologicalNode.java,v 1.5 2004/12/16 10:35:05 bob Exp $
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2004/12/16 10:35:05 $
 * @author $Author: bob $
 * @module map_v1
 */
public class TopologicalNode extends AbstractNode {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258130254244885554L;
	private PhysicalLink			physicalLink;
	private boolean					active;

	private StorableObjectDatabase	topologicalNodeDatabase;

	public TopologicalNode(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			this.topologicalNodeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public TopologicalNode(TopologicalNode_Transferable tnt) throws CreateObjectException {
		super(tnt.header);
		super.name = tnt.name;
		super.description = tnt.description;
		super.longitude = tnt.longitude;
		super.latitude = tnt.latitude;
		this.active = tnt.active;

		try {
			this.physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(
				new Identifier(tnt.physicalLinkId), true);

			this.characteristics = new ArrayList(tnt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(tnt.characteristicIds.length);
			for (int i = 0; i < tnt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(tnt.characteristicIds[i]));

			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected TopologicalNode(final Identifier id,
			final Identifier creatorId,
			String name,
			String description,
			double longitude,
			double latitude,
			boolean active) {
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
		this.active = active;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
	}

	public void insert() throws CreateObjectException {
		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			if (this.topologicalNodeDatabase != null)
				this.topologicalNodeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public List getDependencies() {
		return this.characteristics;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		return new TopologicalNode_Transferable(super.getHeaderTransferable(), this.name, this.description,
												this.longitude, this.latitude,
												(Identifier_Transferable) this.physicalLink.getId().getTransferable(),
												this.active, charIds);
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
		super.currentVersion = super.getNextVersion();
	}

	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}
	
	public void setPhysicalLink(PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		// do not change version due to physical link is not dependence object
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  String description,
											  double longitude,
											  double latitude,
											  boolean active) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.description = description;
			this.longitude = longitude;
			this.latitude = latitude;
			this.active = active;					
	}
}
