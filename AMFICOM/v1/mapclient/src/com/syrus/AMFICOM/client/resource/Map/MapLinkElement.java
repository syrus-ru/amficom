/**
 * $Id: MapLinkElement.java,v 1.15 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.util.HashMap;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.15 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapLinkElement extends StubResource implements MapElement 
{
	/**
	 * @deprecated
	 */
	protected String id = "";
	
	/**
	 * @deprecated
	 */
	protected String name = "";
	
	/**
	 * @deprecated
	 */
	protected String description = "";
	
	/**
	 * @deprecated
	 */
	protected String mapId = "";

	/**
	 * @deprecated
	 */
	protected String startNodeId;

	/**
	 * @deprecated
	 */
	protected String endNodeId;

	protected Map map;
	
	/** начальный узел */
	/**
	 * @deprecated
	 */
	protected MapNodeElement startNode;
	/** конечный узел */
	/**
	 * @deprecated
	 */
	protected MapNodeElement endNode;

	/** атрибуты отображения */
	/**
	 * @deprecated
	 */
	public java.util.Map attributes = new HashMap();

	/** флаг выделения */
	protected boolean selected = false;

	/** флаг того, что объект удален */
	protected boolean removed = false;

	/** флаг наличия сигнала тревоги */
	protected boolean alarmState = false;
	
	/**
	 * Установить наличие сигнала тревоги
	 */
	public void setAlarmState(boolean i)
	{
		alarmState = i;
	}

	/**
	 * получить наличие сигнала тревоги
	 */
	public boolean getAlarmState()
	{
		return alarmState;
	}

	/**
	 * получить флаг удаления элемента
	 */
	public boolean isRemoved()
	{
		return removed;
	}
	
	/**
	 * установить флаг удаления элемента
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	public MapNodeElement getStartNode()
	{
		return this.startNode;
	}
	
	public void setStartNode(MapNodeElement startNode)
	{
		this.startNode = startNode;
	}
	
	public MapNodeElement getEndNode()
	{
		return this.endNode;
	}
	
	public void setEndNode(MapNodeElement endNode)
	{
		this.endNode = endNode;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @deprecated
	 */
	public String getDomainId()
	{
		return this.getMap().getDomainId();
	}
	
	public String getName()
	{
		return "\"" + getStartNode().getName() + " - " + getEndNode().getName() + "\"";//name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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

	public Map getMap()
	{
		return map;
	}

	public void setMap( Map map)
	{
		this.map = map;
		if(map != null)
			this.mapId = map.getId();
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(
			(getStartNode().getLocation().getX() + getEndNode().getLocation().getX()) / 2,
			(getStartNode().getLocation().getY() + getEndNode().getLocation().getY()) / 2);
	}

	/**
	 * Получить другой конец по заданному NodeElement
	 */
	public MapNodeElement getOtherNode(MapNodeElement node)
	{
		Environment.log(
			Environment.LOG_LEVEL_FINER, 
			"method call", 
			getClass().getName(), 
			"getOtherNode(" + node + ")");
		

		if ( this.getEndNode().equals(node) )
			return getStartNode();
		if ( this.getStartNode().equals(node) )
			return getEndNode();
		return null;
	}

	/**
	 * восстановить состояние
	 */
	public void revert(MapElementState state)
	{
		MapLinkElementState mles = (MapLinkElementState )state;
		this.setName(mles.name);
		this.setDescription(mles.description);
		attributes = new HashMap(mles.attributes);
		this.setStartNode(mles.startNode);
		this.setEndNode(mles.endNode);
	}
}
