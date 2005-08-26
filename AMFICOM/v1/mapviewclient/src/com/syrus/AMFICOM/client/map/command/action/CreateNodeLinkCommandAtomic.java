/**
 * $Id: CreateNodeLinkCommandAtomic.java,v 1.18 2005/08/26 15:39:54 krupenn Exp $
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

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * создание фрагмента линии связи, внесение ее в пул и на карту - 
 * атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand {
	/**
	 * создаваемый фрагмент линии
	 */
	NodeLink nodeLink;

	AbstractNode startNode;
	AbstractNode endNode;
	PhysicalLink physicalLink;

	public CreateNodeLinkCommandAtomic(
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public NodeLink getNodeLink() {
		return this.nodeLink;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "
				+ "create NodeLink for link " 
				+ this.physicalLink.getName() 
				+ " (" + this.physicalLink.getId() 
				+ ") with start at node " + this.startNode.getName() 
				+ " (" + this.startNode.getId() 
				+ ") and end at node " + this.endNode.getName() 
				+ " (" + this.endNode.getId() + ")", 
			Level.FINEST);

		try {
			this.nodeLink = NodeLink.createInstance(
					LoginManager.getUserId(),
					this.physicalLink,
					this.startNode,
					this.endNode);

			this.logicalNetLayer.getMapView().getMap().addNodeLink(
					this.nodeLink);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
	}
}

