/**
 * $Id: MapViewController.java,v 1.1 2004/12/24 15:42:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.Client.Resource.Map.*;

import java.util.HashMap;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.CollectorController;
import com.syrus.AMFICOM.Client.Map.Controllers.TopologicalNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkController;
import com.syrus.AMFICOM.Client.Map.Controllers.UnboundNodeController;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;

/**
 * Класс используется для хранения и информации по канализационной
 * прокладке кабелей и положению узлов и других топологических объектов
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/24 15:42:12 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapViewController
{
	private static java.util.Map ctlMap = new HashMap();
	
	private static MapViewController instance = null;
	
	private LogicalNetLayer lnl;
	
	private MapViewController(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}
	
	public static MapViewController getInstance(LogicalNetLayer lnl)
	{
		if(instance == null)
			instance = new MapViewController(lnl);
		return instance;
	}

	static
	{
		ctlMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			TopologicalNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			SiteNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			NodeLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			PhysicalLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Mark.class,
			MarkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Collector.class,
			CollectorController.getInstance());

		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.CablePath.class,
			CableController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath.class,
			MeasurementPathController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.UnboundNode.class,
			UnboundNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.UnboundLink.class,
			UnboundLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.Marker.class,
			MarkerController.getInstance());
	}

	public MapElementController getController(MapElement me)
	{
		MapElementController controller = (MapElementController)ctlMap.get(me.getClass());
		if(controller != null)
			controller.setLogicalNetLayer(lnl);
		return controller;
	}


	/**
	 * отрисовка элемента
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		getController(me).paint(me, g, visibleBounds);
	}

	/**
	 * возвращает флаг, указывающий, что точка currentMousePoint находится
	 * в определенных границах элемента. Для узла границы определяются
	 * размерами иконки, для линии дельта-окрестностью линии. Дельта задается
	 * полем mouseTolerancy
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		return getController(me).isMouseOnElement(me, currentMousePoint);
	}

	/**
	 * определить, попадает ли элемент в область visibleBounds.
	 * Используется при отрисовке (отображаются только элементы, попавшие
	 * в видимую область)
	 */
	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		return getController(me).isElementVisible(me, visibleBounds);
	}

	/**
	 * текст всплывающей подсказки
	 */
	String getToolTipText(MapElement me)
	{
		return getController(me).getToolTipText(me);
	}

}
