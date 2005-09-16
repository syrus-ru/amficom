/**
 * $Id: CreatePhysicalLinkCommandAtomic.java,v 1.22 2005/09/16 14:53:32 krupenn Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * создание физической линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class CreatePhysicalLinkCommandAtomic extends MapActionCommand {
	/** создаваемая линия */
	PhysicalLink link;

	/** начальный узел */
	AbstractNode startNode;

	/** конечный узел */
	AbstractNode endNode;

	public CreatePhysicalLinkCommandAtomic(
			AbstractNode startNode,
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public PhysicalLink getLink() {
		return this.link;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "create PhysicalLink with start at node "  //$NON-NLS-1$
				+ this.startNode.getName() + " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		try {
			this.link = PhysicalLink.createInstance(
					LoginManager.getUserId(),
					this.startNode.getId(),
					this.endNode.getId(),
					this.logicalNetLayer.getCurrentPhysicalLinkType());
			this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
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
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
	}
}
