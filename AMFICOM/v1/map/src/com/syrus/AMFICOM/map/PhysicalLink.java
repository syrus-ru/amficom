/*
 * $Id: PhysicalLink.java,v 1.2 2004/11/28 14:34:48 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2004/11/28 14:34:48 $
 * @author $Author: bob $
 * @module map_v1
 */
public class PhysicalLink extends StorableObject implements Characterized, TypedObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 4121409622671570743L;
	
	private String					name;
	private String					description;

	private PhysicalLinkType		physicalLinkType;

	private Node					startNode;
	private Node					endNode;
	private String					city;
	private String					street;
	private String					building;

	private long					dimensionX;
	private long					dimensionY;

	private boolean					leftToRight;
	private boolean					topToBottom;

	private List					nodeLinks;

	private List					characteristics;

	private StorableObjectDatabase	physicalLinkDatabase;

	public PhysicalLink(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.physicalLinkDatabase = MapDatabaseContext.physicalLinkDatabase;
		try {
			this.physicalLinkDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLink(PhysicalLink_Transferable plt) throws CreateObjectException {
		super(plt.header);
		this.name = plt.name;
		this.description = plt.description;

		this.city = plt.city;
		this.street = plt.street;
		this.building = plt.building;
		this.dimensionX = plt.dimensionX;
		this.dimensionY = plt.dimensionY;
		this.leftToRight = plt.leftToRight;
		this.topToBottom = plt.topToBottom;

		try {
			this.physicalLinkType = (PhysicalLinkType) MapStorableObjectPool.getStorableObject(
				new Identifier(plt.physicalLinkTypeId), true);

			this.startNode = (Node) MapStorableObjectPool.getStorableObject(new Identifier(plt.startNodeId), true);
			this.endNode = (Node) MapStorableObjectPool.getStorableObject(new Identifier(plt.endNodeId), true);

			this.nodeLinks = new ArrayList(plt.nodeLinkIds.length);
			ArrayList nodeLinksIds = new ArrayList(plt.nodeLinkIds.length);
			for (int i = 0; i < plt.nodeLinkIds.length; i++)
				nodeLinksIds.add(new Identifier(plt.nodeLinkIds[i]));
			this.nodeLinks.addAll(MapStorableObjectPool.getStorableObjects(nodeLinksIds, true));

			this.characteristics = new ArrayList(plt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(plt.characteristicIds.length);
			for (int i = 0; i < plt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(plt.characteristicIds[i]));
			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected PhysicalLink(final Identifier id,
						   final Identifier creatorId,
						   final String name, 
						   final String description,
						   final PhysicalLinkType physicalLinkType,
						   final Node startNode, 
						   final Node endNode, 
						   final String city, 
						   final String street, 
						   final String building,
						   final long dimensionX,
						   final long dimensionY,
						   final boolean leftToRight,
						   final boolean topToBottom) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.description = description;
		this.physicalLinkType = physicalLinkType;
		this.startNode = startNode;
		this.endNode = endNode;
		this.city = city;
		this.street = street;
		this.building = building;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;

		this.characteristics = new LinkedList();
		this.nodeLinks = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.physicalLinkDatabase = MapDatabaseContext.physicalLinkDatabase;
	}

	public static PhysicalLink getInstance(PhysicalLink_Transferable plt) throws CreateObjectException {
		PhysicalLink physicalLink = new PhysicalLink(plt);

		physicalLink.physicalLinkDatabase = MapDatabaseContext.physicalLinkDatabase;
		try {
			if (physicalLink.physicalLinkDatabase != null)
				physicalLink.physicalLinkDatabase.insert(physicalLink);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return physicalLink;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.physicalLinkType);
		dependencies.add(this.startNode);
		dependencies.add(this.endNode);
		dependencies.addAll(this.characteristics);
		dependencies.addAll(this.nodeLinks);
		return dependencies;
	}
	
	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] nodeLinkIds = new Identifier_Transferable[this.nodeLinks.size()];
		for (Iterator iterator = this.nodeLinks.iterator(); iterator.hasNext();)
			nodeLinkIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		return new PhysicalLink_Transferable(super.getHeaderTransferable(),
						this.name,
						this.description,
						(Identifier_Transferable)this.physicalLinkType.getId().getTransferable(),
						(Identifier_Transferable)this.startNode.getId().getTransferable(),
						(Identifier_Transferable)this.endNode.getId().getTransferable(),
						this.city,
						this.street,
						this.building,
						this.dimensionX,
						this.dimensionY,
						this.leftToRight,
						this.topToBottom,
						nodeLinkIds,
						charIds);
	}
	
	public StorableObjectType getType() {
		return this.physicalLinkType;
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
	
	public long getDimensionX() {
		return this.dimensionX;
	}
	
	public void setDimensionX(long dimensionX) {
		this.dimensionX = dimensionX;
		super.currentVersion = super.getNextVersion();
	}
	
	public long getDimensionY() {
		return this.dimensionY;
	}
	
	public void setDimensionY(long dimensionY) {
		this.dimensionY = dimensionY;
		super.currentVersion = super.getNextVersion();
	}
	
	public Node getEndNode() {
		return this.endNode;
	}
	
	public void setEndNode(Node endNode) {
		this.endNode = endNode;
		super.currentVersion = super.getNextVersion();
	}
	
	public boolean isLeftToRight() {
		return this.leftToRight;
	}
	
	public void setLeftToRight(boolean leftToRight) {
		this.leftToRight = leftToRight;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	public List getNodeLinks() {
		return this.nodeLinks;
	}
	
	public void setNodeLinks(final List nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null)
			this.nodeLinks.addAll(nodeLinks);
		super.currentVersion = super.getNextVersion();
	}
	
	public Node getStartNode() {
		return this.startNode;
	}
	
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public void setStreet(String street) {
		this.street = street;
		super.currentVersion = super.getNextVersion();
	}
	
	public boolean isTopToBottom() {
		return this.topToBottom;
	}
	
	public void setTopToBottom(boolean topToBottom) {
		this.topToBottom = topToBottom;
		super.currentVersion = super.getNextVersion();
	}
}
