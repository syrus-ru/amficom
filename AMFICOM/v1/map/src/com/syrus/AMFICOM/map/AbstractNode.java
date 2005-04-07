/**
 * $Id: AbstractNode.java,v 1.18 2005/04/07 10:07:24 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */
package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * Абстрактный класс, описывающий узловой элемент топологической схемы 
 * ({@link Map}). Узловой объект характеризуется наличием координат
 * ({@link #location}) и изображением ({@link #imageId}).
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.18 $, $Date: 2005/04/07 10:07:24 $
 * @module map_v1
 * @see SiteNode
 * @see TopologicalNode
 */
public abstract class AbstractNode 
	extends StorableObject 
	implements MapElement
{

	static final long serialVersionUID = -2623880496462305233L;

	protected Set		characteristics;

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
		this.characteristics = new HashSet();
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
		this.characteristics = new HashSet();
	}

	protected AbstractNode(StorableObject_Transferable transferable) throws CreateObjectException {
		this.fromTransferable(transferable);
	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		StorableObject_Transferable sot = (StorableObject_Transferable) transferable;
		super.fromTransferable(sot);
		this.characteristics = new HashSet();
	}

	public Identifier getImageId() {
		return this.imageId;
	}
	
	public void setImageId(Identifier imageId) {
		this.imageId = imageId;
		this.changed = true;
	}
	
	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
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

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final Set characteristics) {
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
	 * Получить список NodeLinks, содержащих заданный Node.
	 * @return Список фрагментов
	 */
	public Set getNodeLinks()
	{
		Set returnNodeLinks = new HashSet();
		for(Iterator it = getMap().getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink )it.next();
			
			if ( (nodeLink.getEndNode().equals(this)) 
				|| (nodeLink.getStartNode().equals(this)))
			{
				returnNodeLinks.add(nodeLink);
			}
		}

		return returnNodeLinks;
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
	public Set getPhysicalLinks()
	{
		Set returnLinks = new HashSet();

		for(Iterator it = getMap().getPhysicalLinks().iterator(); it.hasNext();)
		{
			PhysicalLink physicalLink = (PhysicalLink )it.next();
			
			if ( (physicalLink.getEndNode().equals(this)) 
					|| (physicalLink.getStartNode().equals(this)) )
				returnLinks.add(physicalLink);
		}

		return returnLinks;
	}

	/**
	 * Получить вектор узлов на противоположных концах всех фрагментов линий 
	 * данного элемента.
	 * @return список узлов
	 */
	public Set getOppositeNodes()
	{
		Iterator e = getNodeLinks().iterator();
		Set returnNodes = new HashSet();

		while (e.hasNext())
		{
			NodeLink nodeLink = (NodeLink )e.next();

			if ( nodeLink.getEndNode().equals(this) )
				returnNodes.add(nodeLink.getStartNode());
			else
				returnNodes.add(nodeLink.getEndNode());
		}

		return returnNodes;
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
