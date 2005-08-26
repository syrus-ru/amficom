/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.24 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * создание топологического узла, внесение его в пул и на карту - атомарное
 * действие
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.24 $, $Date: 2005/08/26 15:39:54 $
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
		Log.debugMessage(
			getClass().getName() + "::execute() | "
				+ "create topological node on link "
				+ this.physicalLink.getName() 
				+ " (" + this.physicalLink.getId() + ")", 
			Level.FINEST);

		try {
			this.node = TopologicalNode.createInstance(
					LoginManager.getUserId(),
					this.physicalLink,
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
			e.printStackTrace();
		}
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
	}
}
