/*-
 * $$Id: CreatePhysicalNodeCommandAtomic.java,v 1.34 2005/10/30 15:20:31 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * создание топологического узла, внесение его в пул и на карту - атомарное
 * действие
 * 
 * @version $Revision: 1.34 $, $Date: 2005/10/30 15:20:31 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreatePhysicalNodeCommandAtomic extends MapActionCommand {
	/** создаваемый топологический узел */
	TopologicalNode node;

	/** географические координаты узла */
	DoublePoint point;

	PhysicalLink physicalLink;

	public CreatePhysicalNodeCommandAtomic(
			PhysicalLink physicalLink,
			DoublePoint point) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
		this.physicalLink = physicalLink;
	}

	public TopologicalNode getNode() {
		return this.node;
	}

	@Override
	public void execute() {
		assert Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "create topological node on link " //$NON-NLS-1$
				+ this.physicalLink.getName() 
				+ " (" + this.physicalLink.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		try {
			this.node = TopologicalNode.createInstance(
					LoginManager.getUserId(),
					I18N.getString(MapEditorResourceKeys.NONAME),
					MapEditorResourceKeys.EMPTY_STRING,
					this.point);

			TopologicalNodeController tnc = (TopologicalNodeController) getLogicalNetLayer()
					.getMapViewController().getController(this.node);

			// по умолчанию топологиеский узел не активен
			tnc.setActive(this.node, false);
			// установить коэффициент для масштабирования изображения
			// в соответствии с текущим масштабом отображения карты
			tnc.updateScaleCoefficient(this.node);

			this.logicalNetLayer.getMapView().getMap().addNode(this.node);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}

	@Override
	public void redo() {
		try {
			StorableObjectPool.putStorableObject(this.node);
			this.logicalNetLayer.getMapView().getMap().addNode(this.node);
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
		StorableObjectPool.delete(this.node.getId());
	}
}
