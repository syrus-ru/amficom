/**
 * $Id: CreateUnboundNodeCommandAtomic.java,v 1.24 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.UnboundNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * Разместить сетевой элемент на карте. используется при переносе (drag/drop), в
 * точке point (в экранных координатах)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.24 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclietn_v1
 */
public class CreateUnboundNodeCommandAtomic extends MapActionCommand {
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

	public CreateUnboundNodeCommandAtomic(SchemeElement se, DoublePoint dpoint) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.schemeElement = se;
		this.coordinatePoint = dpoint;
	}

	public UnboundNode getUnbound() {
		return this.unbound;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "
				+ "create unbound node for scheme element "
				+ this.schemeElement.getName() 
				+ " (" + this.schemeElement.getId() + ")", 
			Level.FINEST);

		if(!getLogicalNetLayer().getContext().getApplicationModel().isEnabled(
				MapApplicationModel.ACTION_EDIT_BINDING))
			return;

		this.map = this.logicalNetLayer.getMapView().getMap();

		try {
			// создать новый узел
			this.unbound = UnboundNode.createInstance(
					LoginManager.getUserId(),
					this.schemeElement,
					this.coordinatePoint,
					this.logicalNetLayer.getUnboundNodeType());

			UnboundNodeController unc = (UnboundNodeController) getLogicalNetLayer()
					.getMapViewController().getController(this.unbound);

			unc.updateScaleCoefficient(this.unbound);

			this.map.addNode(this.unbound);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(ApplicationException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	@Override
	public void undo() {
		this.map.removeNode(this.unbound);
	}

	@Override
	public void redo() {
		this.map.addNode(this.unbound);
	}
}
