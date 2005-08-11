/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.20 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.util.Log;

/**
 * В данном классе реализуется алгоритм добавления топологического узла на 
 * фрагмент линии. При этом фрагмент линии удаляется, и вместо него создаются 
 * два других фрагмента, разделенные новывм топологичсеским узлом. Команда
 * состоит из последовательности атомарных действий
 * 
 * @version $Revision: 1.20 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
 * @author $Author: arseniy $
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
			Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
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

			MapElementState pls = physicalLink.getState();
			// разбить фрагмент на две части - т.е. создать два новых фрагмента
			NodeLink link1 = super.createNodeLink(physicalLink, startNode, node);
			NodeLink link2 = super.createNodeLink(physicalLink, node, endNode);
			// удаляется старый фрагмент с карты
			super.removeNodeLink(this.nodeLink);
			// удаляется старый фрагмент из линии
			physicalLink.removeNodeLink(this.nodeLink);
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
