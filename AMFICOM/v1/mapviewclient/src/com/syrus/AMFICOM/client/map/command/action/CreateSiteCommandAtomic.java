/**
 * $Id: CreateSiteCommandAtomic.java,v 1.5 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.SiteNodeController;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Разместить сетевой элемент на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateSiteCommandAtomic extends MapActionCommand
{
	/**
	 * создаваемый узел
	 */
	MapSiteNodeElement site;
	
	/** тип создаваемого элемента */
	MapNodeProtoElement proto;
	
	Map map;
	
	/**
	 * экранная точка, в которой создается новый топологический узел
	 */
	Point point = null;
	
	/**
	 * географическая точка, в которой создается новый топологический узел.
	 * может инициализироваться по point
	 */
	DoublePoint coordinatePoint = null;

	public CreateSiteCommandAtomic(
			MapNodeProtoElement proto,
			DoublePoint dpoint)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.proto = proto;
		this.coordinatePoint = dpoint;
	}

	public CreateSiteCommandAtomic(
			MapNodeProtoElement proto,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.proto = proto;
		this.point = point;
	}

	public MapSiteNodeElement getSite()
	{
		return site;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;
		
		DataSourceInterface dataSource = aContext.getDataSource();
	
		if(coordinatePoint == null)
			coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		map = logicalNetLayer.getMapView().getMap();

		// создать новый узел
		site = new MapSiteNodeElement(
				dataSource.GetUId( MapSiteNodeElement.typ),
				coordinatePoint,
				map,
				proto);

		SiteNodeController snc = (SiteNodeController )getLogicalNetLayer().getMapViewController().getController(site);
		
		snc.updateScaleCoefficient(site);

		Pool.put(MapSiteNodeElement.typ, site.getId(), site);
		map.addNode(site);
		
		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					site, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);

	}
	
	public void undo()
	{
		map.removeNode(site);
		Pool.remove(MapSiteNodeElement.typ, site.getId());
	}
	
	public void redo()
	{
		map.addNode(site);
		Pool.put(MapSiteNodeElement.typ, site.getId(), site);
	}
}
