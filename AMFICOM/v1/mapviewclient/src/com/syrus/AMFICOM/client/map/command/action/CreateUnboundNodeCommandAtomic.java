/*-
 * $$Id: CreateUnboundNodeCommandAtomic.java,v 1.30 2005/10/30 14:48:56 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * Разместить сетевой элемент на карте. используется при переносе (drag/drop), в
 * точке point (в экранных координатах)
 * 
 * @version $Revision: 1.30 $, $Date: 2005/10/30 14:48:56 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateUnboundNodeCommandAtomic extends MapActionCommand {
	/**
	 * создаваемый узел
	 */
	UnboundNode unbound;

	SchemeElement schemeElement;

	MapView mapView;
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
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "create unbound node for scheme element " //$NON-NLS-1$
				+ this.schemeElement.getName() 
				+ " (" + this.schemeElement.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		if(!getLogicalNetLayer().getContext().getApplicationModel().isEnabled(
				MapApplicationModel.ACTION_EDIT_BINDING))
			return;

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();

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

			this.mapView.addUnboundNode(this.unbound);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugMessage(e, Level.SEVERE);
		} catch(ApplicationException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugMessage(e, Level.SEVERE);
		}
	}

	@Override
	public void undo() {
		this.mapView.removeUnboundNode(this.unbound);
	}

	@Override
	public void redo() {
		this.mapView.addUnboundNode(this.unbound);
	}
}
