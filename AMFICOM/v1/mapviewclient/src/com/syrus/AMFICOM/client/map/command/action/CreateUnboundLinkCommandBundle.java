/**
 * $Id: CreateUnboundLinkCommandBundle.java,v 1.14 2005/08/26 15:39:54 krupenn Exp $
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
 * @version $Revision: 1.14 $, $Date: 2005/08/26 15:39:54 $
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
			getClass().getName() + "::execute() | "
				+ "create UnboundLink with start at node " 
				+ this.startNode.getName() + " (" + this.startNode.getId() 
				+ ") and end at node " + this.endNode.getName() 
				+ " (" + this.endNode.getId() + ")", 
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
