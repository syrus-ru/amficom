/**
 * $Id: CreateUnboundLinkCommandBundle.java,v 1.15 2005/09/16 14:53:32 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * создание непривязанной линии, состоящей из одного фрагмента, внесение ее в
 * пул и на карту
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class CreateUnboundLinkCommandBundle extends MapActionCommandBundle {
	UnboundLink unbound;

	AbstractNode startNode;
	AbstractNode endNode;

	public CreateUnboundLinkCommandBundle(
			AbstractNode startNode,
			AbstractNode endNode) {
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public UnboundLink getUnbound() {
		return this.unbound;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "create UnboundLink with start at node "  //$NON-NLS-1$
				+ this.startNode.getName() + " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		try {
			this.unbound = super.createUnboundLinkWithNodeLink(
					this.startNode,
					this.endNode);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

}
