/*
 * $Id: Mark.java,v 1.10 2005/01/13 15:14:00 krupenn Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
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
 * @version $Revision: 1.10 $, $Date: 2005/01/13 15:14:00 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class Mark extends AbstractNode implements Characterized {

	public static final String IMAGE_NAME = "mark";
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258126938496186164L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_STREET = "street";
	public static final String COLUMN_BUILDING = "building";

	/** 
	 * ������ ���������� ��� ��������. ���������������� ������ � ������
	 * ������������� ��������
	 */
	private static Object[][] exportColumns = null;

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

		super.location = new DoublePoint(mt.longitude, mt.latitude);

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
		super.location = new DoublePoint(longitude, latitude);		
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
			final Identifier creatorId,
			final PhysicalLink link,
			final double len)
		throws CreateObjectException 
	{
		return Mark.createInstance(
			creatorId,
			"",
			"",
			0.0D,
			0.0D,
			link,
			len,
			"",
			"",
			"");
	}

	public static Mark createInstance(	final Identifier creatorId,
										final String name,
										final String description,
										final double longitude,
										final double latitude,
										final PhysicalLink physicalLink,
										final double distance,
										final String city,
										final String street,
										final String building) throws CreateObjectException {
		if (creatorId == null || name == null || description == null || physicalLink == null 
				|| city == null || street == null || building == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Mark(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MARK_ENTITY_CODE), 
					creatorId, 
					name,
					description,
					longitude, 
					latitude,
					physicalLink,
					distance,
					city,
					street,
					building);
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
						this.location.getX(),
						this.location.getY(),
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
		super.location.setLocation(longitude, latitude);
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
		return this.nodeLink;
	}

	public void setStartNode(AbstractNode startNode)
	{
		this.startNode = startNode;
	}

	public AbstractNode getStartNode()
	{
		return this.startNode;
	}

	public DoublePoint getLocation()
	{
		return (DoublePoint)this.location.clone();
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
		
		for(Iterator it = getPhysicalLink().getNodeLinks().iterator(); it.hasNext();){
			NodeLink nl = (NodeLink )it.next();
			if(nl.equals(this.nodeLink)){
				pathLength += this.getSizeInDoubleLt();
				break;
			} else {
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
			if(nl == this.nodeLink)
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
		return this.sizeInDoubleLt;
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
		NodeState mnes = (NodeState )state;
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		setLocation(mnes.location);
	}

	public Object[][] exportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new Object[10][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
			exportColumns[4][0] = COLUMN_DISTANCE;
			exportColumns[5][0] = COLUMN_X;
			exportColumns[6][0] = COLUMN_Y;
			exportColumns[7][0] = COLUMN_CITY;
			exportColumns[8][0] = COLUMN_STREET;
			exportColumns[9][0] = COLUMN_BUILDING;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getPhysicalLink().getId();
		exportColumns[4][1] = String.valueOf(getDistance());
		exportColumns[5][1] = String.valueOf(getLocation().getX());
		exportColumns[6][1] = String.valueOf(getLocation().getY());
		exportColumns[7][1] = getCity();
		exportColumns[8][1] = getStreet();
		exportColumns[9][1] = getBuilding();
		
		return exportColumns;
	}

	public static Mark createInstance(
			Identifier creatorId,
			Object[][] exportColumns)
		throws CreateObjectException 
	{
		Identifier id = null;
		String name = null;
		String description = null;
		Identifier physicalLinkId = null;
		double distance = -1.0D;
		String city = null;
		String street = null;
		String building = null;
		double x = -1.0D;
		double y = -1.0D;

		Object field;
		Object value;

		if (creatorId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		for(int i = 0; i < exportColumns.length; i++)
		{
			field = exportColumns[i][0];
			value = exportColumns[i][1];

			if(field.equals(COLUMN_ID))
				id = (Identifier )value;
			else
			if(field.equals(COLUMN_NAME))
				name = (String )value;
			else
			if(field.equals(COLUMN_DESCRIPTION))
				description = (String )value;
			else
			if(field.equals(COLUMN_PHYSICAL_LINK_ID))
				physicalLinkId = (Identifier )value;
			else
			if(field.equals(COLUMN_DISTANCE))
				distance = Double.parseDouble((String )value);
			else
			if(field.equals(COLUMN_X))
				x = Double.parseDouble((String )value);
			else
			if(field.equals(COLUMN_Y))
				y = Double.parseDouble((String )value);
			else
			if(field.equals(COLUMN_CITY))
				city = (String )value;
			else
			if(field.equals(COLUMN_STREET))
				street = (String )value;
			else
			if(field.equals(COLUMN_BUILDING))
				building = (String )value;
		}

		if (id == null || name == null || description == null || physicalLinkId == null 
				|| city == null || street == null || building == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLink physicalLink = (PhysicalLink ) 
				MapStorableObjectPool.getStorableObject(
					physicalLinkId, false);
			return new Mark(
					id, 
					creatorId, 
					name,
					description,
					x, 
					y,
					physicalLink,
					distance,
					city,
					street,
					building);
		} catch (ApplicationException e) {
			throw new CreateObjectException("Mark.createInstance |  ", e);
		}
	}

}
