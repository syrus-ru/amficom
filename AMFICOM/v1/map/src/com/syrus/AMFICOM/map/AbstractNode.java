/*
 * $Id: AbstractNode.java,v 1.3 2004/12/20 12:36:00 krupenn Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/20 12:36:00 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public abstract class AbstractNode 
	extends StorableObject 
	implements Characterized, MapElement
{

	static final long serialVersionUID = -2623880496462305233L;

	protected List		characteristics;

	protected String	name;

	protected String	description;

	/**
	 * @deprecated use location.x
	 */
	protected double	longitude;

	/**
	 * @deprecated use location.y
	 */
	protected double	latitude;

	protected Identifier imageId;

	protected DoublePoint location = new DoublePoint(0, 0);


	protected transient boolean selected = false;

	protected transient boolean alarmState = false;

	protected transient boolean removed = false;

	protected transient Map map = null;

	protected AbstractNode(Identifier id) {
		super(id);
		this.characteristics = new LinkedList();
	}

	/**
	 * @deprecated use constructor with DoublePoint location
	 * instead of pair longitude, latitude
	 */
	protected AbstractNode(Identifier id,
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			String name,
			String desription,
			double longitude,
			double latitude) {
		this(
			id,
			created,
			modified,
			creatorId,
			modifierId,
			name,
			desription,
			new DoublePoint(longitude, latitude));
	}

	protected AbstractNode(Identifier id,
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			String name,
			String desription,
			DoublePoint location) {
		super(id, created, modified, creatorId, modifierId);
		this.name = name;
		this.description = desription;
		this.location = new DoublePoint(location.x, location.y);
		this.characteristics = new LinkedList();
	}

	protected AbstractNode(StorableObject_Transferable transferable) {
		super(transferable);
		this.characteristics = new LinkedList();
	}

	public Identifier getImageId() {
		return this.imageId;
	}
	
	public void setImageId(Identifier imageId) {
		this.imageId = imageId;
		super.currentVersion = super.getNextVersion();
	}
	
	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}

	public void addCharacteristic(Characteristic ch)
	{
		this.characteristics.add(ch);
		super.currentVersion = super.getNextVersion();
	}

	public void removeCharacteristic(Characteristic ch)
	{
		this.characteristics.remove(ch);
		super.currentVersion = super.getNextVersion();
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * @deprecated use getLocation().y
	 */
	public double getLatitude() {
		return location.getY();
	}

	/**
	 * @deprecated use getLocation().x
	 */
	public double getLongitude() {
		return location.getX();
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

	/**
	 * @deprecated use setLocation(DoublePoint )
	 */
	public void setLatitude(double latitude) {
		this.location.y = latitude;
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * @deprecated uset setLocation(DoublePoint )
	 */
	public void setLongitude(double longitude) {
		this.location.x = longitude;
		super.currentVersion = super.getNextVersion();
	}

	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(location.x, location.y);
	}

	public void setLocation(DoublePoint location)
	{
		this.location.x = location.x;
		this.location.y = location.y;
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * �������� ������ NodeLinks, ���������� �������� Node
	 */
	public List getNodeLinks()
	{
		List returnList = new LinkedList();
		for(Iterator it = getMap().getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink )it.next();
			
			if ( (nodeLink.getEndNode().equals(this)) 
				|| (nodeLink.getStartNode().equals(this)))
			{
				returnList.add(nodeLink);
			}
		}

		return returnList;
	}

	/**
	 * ���������� �������� �����, �� ������ ����������� � ���������.
	 * ��� ��������������� ���� ���������� ������������ ���������������,
	 * ��� �������� ���� �� ����� ���� ���������, �� ���� ������� �����
	 * �� ������ �������������� � ���������� null
	 */
	public NodeLink getOtherNodeLink(NodeLink nodeLink)
	{
		if(!this.getClass().equals(TopologicalNode.class))
		{
			return null;
		}

		NodeLink startNodeLink = null;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
			{
				NodeLink nl = (NodeLink )it.next();
				if(nodeLink != nl)
				{
					startNodeLink = nl;
					break;
				}
			}
			
		return startNodeLink;
	}

	/**
	 * �������� ������ PhysicalLink, ������������ ��� ���������������
	 * �� ������ ����
	 */
	public List getPhysicalLinks()
	{
		List returnList = new LinkedList();

		for(Iterator it = getMap().getPhysicalLinks().iterator(); it.hasNext();)
		{
			PhysicalLink physicalLink = (PhysicalLink )it.next();
			
			if ( (physicalLink.getEndNode().equals(this)) 
					|| (physicalLink.getStartNode().equals(this)) )
				returnList.add(physicalLink);
		}

		return returnList;
	}

	/**
	 * �������� ������ Node ��������������� � ���� �������� NodeLink, �������
	 * ��������
	 */
	public List getOppositeNodes()
	{
		Iterator e = getNodeLinks().iterator();
		LinkedList returnList = new LinkedList();

		while (e.hasNext())
		{
			NodeLink nodeLink = (NodeLink )e.next();

			if ( nodeLink.getEndNode().equals(this) )
				returnList.add(nodeLink.getStartNode());
			else
				returnList.add(nodeLink.getEndNode());
		}

		return returnList;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	public boolean getAlarmState()
	{
		return alarmState;
	}

	public boolean isRemoved()
	{
		return removed;
	}

	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}
}
