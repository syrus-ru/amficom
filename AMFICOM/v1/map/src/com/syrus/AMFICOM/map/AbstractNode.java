/**
 * $Id: AbstractNode.java,v 1.10 2005/02/02 14:48:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */
package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Абстрактный класс, описывающий узловой элемент топологической схемы 
 * ({@link Map}). Узловой объект характеризуется наличием координат
 * ({@link #location}) и изображением ({@link #imageId}).
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/02/02 14:48:45 $
 * @module map_v1
 * @see SiteNode
 * @see TopologicalNode
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
	 * @deprecated use {@link #location location}.{@link DoublePoint#getX() getX()}
	 */
	protected double	longitude;

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getY() getY()}
	 */
	protected double	latitude;

	/**
	 * Идентификатор изображения, которое отображается на топологической схеме
	 * в точке координат узла.
	 */
	protected Identifier imageId;

	/**
	 * Географические координаты узла.
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

	protected void setLatitude(double longitude) {
		this.location.setLocation(longitude, this.location.getY());
	}


	public String getDescription() {
		return this.description;
	}
	
	protected void setDescription0(String description) {
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.setDescription0(description);
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setName0(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.setName0(name);
		super.currentVersion = super.getNextVersion();
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

	public DoublePoint getLocation(){
		return (DoublePoint)this.location.clone();
	}

	public void setLocation(DoublePoint location)
	{
		this.location.setLocation(location.getX(), location.getY());
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Получить список NodeLinks, содержащих заданный Node.
	 * @return Список фрагментов
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
	 * Возвращает фрагмент линии, включающий данный узел, по не равный 
	 * переданному в параметре. Если фрагмент А и фрагмент Б имеют общую 
	 * точку Т, то вызов метода <code>Т.getOtherNodeLink(А)</code> вернет Б, а вызов
	 * <code>Т.getOtherNodeLink(Б)</code> вернет А. Таким образом, для топологического 
	 * узла возвращает единственный противоположный,
	 * для сетевого узла их может быть несколько, по этой причине метод
	 * не должен использоваться и возвращает null
	 * @param nodeLink фрагмент линии
	 * @return другой фрагмент линии
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
	 * Получить список линий, начинающихся или заканчивающихся
	 * на данном узле.
	 * @return список линий
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
	 * Получить вектор узлов на противоположных концах всех фрагментов линий 
	 * данного элемента.
	 * @return список узлов
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
