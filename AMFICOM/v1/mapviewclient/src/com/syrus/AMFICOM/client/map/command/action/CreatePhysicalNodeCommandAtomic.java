/*-
 * $$Id: CreatePhysicalNodeCommandAtomic.java,v 1.39 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.CreateObjectException;
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
 * @version $Revision: 1.39 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
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
		Log.debugMessage("create topological node on link " //$NON-NLS-1$
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
			Log.errorMessage(e);
		}
	}

	@Override
	public void redo() {
		Log.errorMessage(NOT_IMPLEMENTED);

//		Этот метод всё равно не работает
//		try {
//			StorableObjectPool.putStorableObject(this.node);
//			this.logicalNetLayer.getMapView().getMap().addNode(this.node);
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
		StorableObjectPool.delete(this.node.getId());
	}
}
