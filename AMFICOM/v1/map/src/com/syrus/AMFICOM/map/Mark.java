/*
 * $Id: Mark.java,v 1.7 2004/12/20 12:36:01 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @version $Revision: 1.7 $, $Date: 2004/12/20 12:36:01 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class Mark extends AbstractNode implements Characterized {

	public static final String IMAGE_NAME = "mark";
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258126938496186164L;

	private PhysicalLink			physicalLink;

	private double					distance;

	private String					city;
	private String					street;
	private String					building;

	private StorableObjectDatabase	markDatabase;


	protected transient double sizeInDoubleLt;

	protected transient NodeLink nodeLink;

	protected transient AbstractNode startNode;


	public Mark(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.markDatabase = MapDatabaseContext.getMarkDatabase();
		try {
			this.markDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Mark(Mark_Transferable mt) throws CreateObjectException {
		super(mt.header);
		super.name = mt.name;
		super.description = mt.description;

		super.location.x = mt.longitude;
		super.location.y = mt.latitude;

		this.distance = mt.distance;

		this.city = mt.city;
		this.street = mt.street;
		this.building = mt.building;

		try {
			this.physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(
				new Identifier(mt.physicalLinkId), true);

			super.characteristics = new ArrayList(mt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(mt.characteristicIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(mt.characteristicIds[i]));
			super.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
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
		super.name = name;
		super.description = description;
		super.location.x = longitude;
		super.location.y = latitude;		
		this.physicalLink = physicalLink;
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;

		super.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.markDatabase = MapDatabaseContext.getMarkDatabase();

//		this.setIconName(IMAGE_NAME);
	}

	
	public void insert() throws CreateObjectException {
		this.markDatabase = MapDatabaseContext.getMarkDatabase();
		try {
			if (this.markDatabase != null)
				this.markDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static Mark createInstance(
			PhysicalLink link,
			double len)
		throws CreateObjectException 
	{
		if (link == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			Identifier ide =
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.MARK_ENTITY_CODE);
			return new Mark(
				ide,
				link.getMap().getCreatorId(),
				ide.toString(),
				"",
				0.0D,
				0.0D,
				link,
				len,
				"",
				"",
				"");
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Mark.createInstance | cannot generate identifier ", e);
		}
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
						this.location.x,
						this.location.y,
						(Identifier_Transferable)this.physicalLink.getId().getTransferable(),
						this.distance,
						this.city,
						this.street,
						this.building,
						charIds);
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
	
	public double getDistance() {
		return this.distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
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
	
	protected synchronized void setAttributes(
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,											  
			String name,
			String description,
			double longitude,
			double latitude,
			PhysicalLink physicalLink,
			double distance,
			String city,
			String street,
			String building) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId);
		super.name = name;
		super.description = description;
		super.location.x = longitude;
		super.location.y = latitude;
		this.physicalLink = physicalLink;
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;					
	}

	public void setNodeLink(NodeLink nodeLink)
	{
		this.nodeLink = nodeLink;
	}

	public NodeLink getNodeLink()
	{
		return nodeLink;
	}

	public void setStartNode(AbstractNode startNode)
	{
		this.startNode = startNode;
	}

	public AbstractNode getStartNode()
	{
		return startNode;
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(location.x, location.y);
	}

	public void setLocation(DoublePoint location)
	{
		super.setLocation(location);
		setDistance(this.getFromStartLengthLt());
	}

	public double getFromStartLengthLt()
	{
		getPhysicalLink().sortNodeLinks();

		double pathLength = 0;
		
		for(Iterator it = getPhysicalLink().getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nl = (NodeLink )it.next();
			if(nl.equals(nodeLink))
			{
				pathLength += this.getSizeInDoubleLt();
				break;
			}
			else
			{
				pathLength += nl.getLengthLt();
			}
		}
		return pathLength;
	}

	public double getFromEndLengthLt()
	{
		getPhysicalLink().sortNodeLinks();

		double pathLength = 0;
		
		ListIterator it = getPhysicalLink().getNodeLinks().listIterator(
				getPhysicalLink().getNodeLinks().size());
		while(it.hasPrevious())
		{
			NodeLink nl = (NodeLink )it.previous();
			if(nl == nodeLink)
			{
				pathLength += nl.getLengthLt() - this.getSizeInDoubleLt();
				break;
			}
			else
				pathLength += nl.getLengthLt();
		}
		return pathLength;
	}

	public void setSizeInDoubleLt(double sizeInDoubleLt)
	{
		this.sizeInDoubleLt = sizeInDoubleLt;
	}

	public double getSizeInDoubleLt()
	{
		return sizeInDoubleLt;
	}

	public List getNodeLinks()
	{
		throw new UnsupportedOperationException();
	}

	public NodeLink getOtherNodeLink(NodeLink nl)
	{
		throw new UnsupportedOperationException();
	}

	public List getPhysicalLinks()
	{
		throw new UnsupportedOperationException();
	}

	public List getOppositeNodes()
	{
		throw new UnsupportedOperationException();
	}

	public MapElementState getState()
	{
		return new NodeState(this);
	}

	public void revert(MapElementState state)
	{
		NodeState mnes = (NodeState)state;
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		setLocation(mnes.location);
	}
}
