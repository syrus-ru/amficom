/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.14 2005/06/16 10:57:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * В данном классе реализуется алгоритм добавления топологического узла на 
 * фрагмент линии. При этом фрагмент линии удаляется, и вместо него создаются 
 * два других фрагмента, разделенные новывм топологичсеским узлом. Команда
 * состоит из последовательности атомарных действий
 * 
 * @version $Revision: 1.14 $, $Date: 2005/06/16 10:57:19 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class CreatePhysicalNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	NodeLink nodeLink;
	
	Map map;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public CreatePhysicalNodeCommandBundle(
			NodeLink nodeLink,
			Point point)
	{
		super();
		this.nodeLink = nodeLink;
		this.point = point;
	}

	public void execute()
	{
		try
		{
			Environment.log(
					Environment.LOG_LEVEL_FINER, 
					"method call", 
					getClass().getName(), 
					"execute()");
			DoublePoint coordinatePoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.point);
			this.map = this.logicalNetLayer.getMapView().getMap();
			// получить линию связи, которой принадлежит фрагмент
			PhysicalLink physicalLink = this.nodeLink.getPhysicalLink();
			// создать новый активный топологический узел
			TopologicalNode node = super.createPhysicalNode(physicalLink, coordinatePoint);
			super.changePhysicalNodeActivity(node, true);
			// взять начальный и конечный узлы фрагмента
			AbstractNode startNode = this.nodeLink.getStartNode();
			AbstractNode endNode = this.nodeLink.getEndNode();
			// разбить фрагмент на две части - т.е. создать два новых фрагмента
			NodeLink link1 = super.createNodeLink(physicalLink, startNode, node);
			link1.setPhysicalLink(physicalLink);
			NodeLink link2 = super.createNodeLink(physicalLink, node, endNode);
			link2.setPhysicalLink(physicalLink);
			// удаляется старый фрагмент с карты
			super.removeNodeLink(this.nodeLink);
			MapElementState pls = physicalLink.getState();
			// удаляется старый фрагмент из линии
			physicalLink.removeNodeLink(this.nodeLink);
			// добавляются два новых фрагмента
			physicalLink.addNodeLink(link1);
			physicalLink.addNodeLink(link2);
			super.registerStateChange(physicalLink, pls, physicalLink.getState());
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			this.logicalNetLayer.setCurrentMapElement(node);
			this.logicalNetLayer.notifySchemeEvent(node);
			setResult(Command.RESULT_OK);
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}

	}
}
