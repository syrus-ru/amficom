/**
 * $Id: AbstractNode.java,v 1.13 2005/03/04 13:34:49 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */
package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ����������� �����, ����������� ������� ������� �������������� ����� 
 * ({@link Map}). ������� ������ ��������������� �������� ���������
 * ({@link #location}) � ������������ ({@link #imageId}).
 * 
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/03/04 13:34:49 $
 * @module map_v1
 * @see SiteNode
 * @see TopologicalNode
 */
public abstract class AbstractNode 
	extends StorableObject 
	implements Characterizable, MapElement
{

	static final long serialVersionUID = -2623880496462305233L;

	protected List		characteristics;

	protected String	name;

	protected String	description;

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getX() getX()}
	 */
	protected double	longitude;

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getY() getY()}
	 */
	protected double	latitude;

	/**
	 * ������������� �����������, ������� ������������ �� �������������� �����
	 * � ����� ��������� ����.
	 */
	protected Identifier imageId;

	/**
	 * �������������� ���������� ����.
	 */
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
			long version,
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
			version,
			name,
			desription,
			new DoublePoint(longitude, latitude));
	}

	protected AbstractNode(Identifier id,
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String name,
			String desription,
			DoublePoint location) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = desription;
		this.location.setLocation(location.getX(), location.getY());
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
		this.changed = true;
	}
	
	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}

	public void addCharacteristic(Characteristic ch)
	{
		this.characteristics.add(ch);
		this.changed = true;
	}

	public void removeCharacteristic(Characteristic ch)
	{
		this.characteristics.remove(ch);
		this.changed = true;
	}

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getY() getY()}
	 */
	public double getLatitude() {
		return this.location.getY();
	}

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getX() getX()}
	 */
	public double getLongitude() {
		return this.location.getX();
	}
	
	protected void setLongitude(double longitude) {
		this.location.setLocation(longitude, this.location.getY());
	}

	protected void setLatitude(double latitude) {
		this.location.setLocation(this.location.getX(), latitude);
	}


	public String getDescription() {
		return this.description;
	}
	
	protected void setDescription0(String description) {
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.setDescription0(description);
		this.changed = true;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setName0(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.setName0(name);
		this.changed = true;
	}

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		this.changed = true;
	}

	public DoublePoint getLocation(){
		return (DoublePoint)this.location.clone();
	}

	public void setLocation(DoublePoint location)
	{
		this.location.setLocation(location.getX(), location.getY());
		this.changed = true;
	}

	/**
	 * �������� ������ NodeLinks, ���������� �������� Node.
	 * @return ������ ����������
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
	 * ���������� �������� �����, ���������� ������ ����, �� �� ������ 
	 * ����������� � ���������. ���� �������� � � �������� � ����� ����� 
	 * ����� �, �� ����� ������ <code>�.getOtherNodeLink(�)</code> ������ �, � �����
	 * <code>�.getOtherNodeLink(�)</code> ������ �. ����� �������, ��� ��������������� 
	 * ���� ���������� ������������ ���������������,
	 * ��� �������� ���� �� ����� ���� ���������, �� ���� ������� �����
	 * �� ������ �������������� � ���������� null
	 * @param nodeLink �������� �����
	 * @return ������ �������� �����
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
	 * �������� ������ �����, ������������ ��� ���������������
	 * �� ������ ����.
	 * @return ������ �����
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
	 * �������� ������ ����� �� ��������������� ������ ���� ���������� ����� 
	 * ������� ��������.
	 * @return ������ �����
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

	/**
	 * {@inheritDoc}
	 */
	public Map getMap()
	{
		return this.map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState()
	{
		return this.alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved()
	{
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}
}
