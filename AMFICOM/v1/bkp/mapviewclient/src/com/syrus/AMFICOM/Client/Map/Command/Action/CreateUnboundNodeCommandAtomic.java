/**
 * $Id: CreateUnboundNodeCommandAtomic.java,v 1.4 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.UnboundNodeController;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.geom.Point2D;

/**
 * Разместить сетевой элемент на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateUnboundNodeCommandAtomic extends MapActionCommand
{
	/**
	 * создаваемый узел
	 */
	MapUnboundNodeElement unbound;

	SchemeElement se;	

	Map map;
	
	/**
	 * географическая точка, в которой создается новый топологический узел.
	 * может инициализироваться по point
	 */
	DoublePoint coordinatePoint = null;

	public CreateUnboundNodeCommandAtomic(
			SchemeElement se,
			DoublePoint dpoint)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.se = se;
		this.coordinatePoint = dpoint;
	}

	public MapUnboundNodeElement getUnbound()
	{
		return unbound;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;
		
		DataSourceInterface dataSource = aContext.getDataSource();
	
		map = logicalNetLayer.getMapView().getMap();

		// создать новый узел
		unbound = new MapUnboundNodeElement(
			se,
			dataSource.GetUId(MapSiteNodeElement.typ),
			coordinatePoint,
			map,
			logicalNetLayer.getUnboundProto());
	
		UnboundNodeController unc = (UnboundNodeController )getLogicalNetLayer().getMapViewController().getController(unbound);

		unc.updateScaleCoefficient(unbound);
	
		Pool.put(MapSiteNodeElement.typ, unbound.getId(), unbound);
		map.addNode(unbound);
	}
	
	public void undo()
	{
		map.removeNode(unbound);
		Pool.remove(MapSiteNodeElement.typ, unbound.getId());
	}
	
	public void redo()
	{
		map.addNode(unbound);
		Pool.put(MapSiteNodeElement.typ, unbound.getId(), unbound);
	}
}
