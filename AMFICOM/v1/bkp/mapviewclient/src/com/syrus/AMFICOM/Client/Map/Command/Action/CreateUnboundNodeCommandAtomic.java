/**
 * $Id: CreateUnboundNodeCommandAtomic.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.UnboundNodeController;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

import java.awt.geom.Point2D;

/**
 * Разместить сетевой элемент на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
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

		try
		{
			// создать новый узел
			unbound = MapUnboundNodeElement.createInstance(
				se,
				coordinatePoint,
				map,
				logicalNetLayer.getUnboundProto());
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			return;
		}
	
		UnboundNodeController unc = (UnboundNodeController )getLogicalNetLayer().getMapViewController().getController(unbound);

		unc.updateScaleCoefficient(unbound);
	
		map.addNode(unbound);
	}
	
	public void undo()
	{
		map.removeNode(unbound);
	}
	
	public void redo()
	{
		map.addNode(unbound);
	}
}
