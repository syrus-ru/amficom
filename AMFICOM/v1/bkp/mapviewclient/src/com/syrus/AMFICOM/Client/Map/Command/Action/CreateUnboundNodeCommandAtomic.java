/**
 * $Id: CreateUnboundNodeCommandAtomic.java,v 1.13 2005/03/16 12:54:57 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.UnboundNodeController;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * Разместить сетевой элемент на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/03/16 12:54:57 $
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
				this.logicalNetLayer.getUnboundProto());
			
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
