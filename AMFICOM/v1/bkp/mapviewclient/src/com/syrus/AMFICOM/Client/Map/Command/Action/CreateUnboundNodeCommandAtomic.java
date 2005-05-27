/**
 * $Id: CreateUnboundNodeCommandAtomic.java,v 1.15 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.UnboundNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * Разместить сетевой элемент на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclietn_v1
 */
public class CreateUnboundNodeCommandAtomic extends MapActionCommand
{
	/**
	 * создаваемый узел
	 */
	UnboundNode unbound;

	SchemeElement schemeElement;	

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
		this.schemeElement = se;
		this.coordinatePoint = dpoint;
	}

	public UnboundNode getUnbound()
	{
		return this.unbound;
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
		
		this.map = this.logicalNetLayer.getMapView().getMap();

		try
		{
			// создать новый узел
			this.unbound = UnboundNode.createInstance(
				this.logicalNetLayer.getUserId(),
				this.schemeElement,
				this.coordinatePoint,
				this.logicalNetLayer.getUnboundNodeType());
			
			UnboundNodeController unc = (UnboundNodeController)getLogicalNetLayer().getMapViewController().getController(this.unbound);

			unc.updateScaleCoefficient(this.unbound);
		
			this.map.addNode(this.unbound);
			setResult(Command.RESULT_OK);
		}
		catch (CreateObjectException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	public void undo()
	{
		this.map.removeNode(this.unbound);
	}
	
	public void redo()
	{
		this.map.addNode(this.unbound);
	}
}
