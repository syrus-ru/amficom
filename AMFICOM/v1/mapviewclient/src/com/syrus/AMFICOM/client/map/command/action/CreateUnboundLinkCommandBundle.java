/**
 * $Id: CreateUnboundLinkCommandBundle.java,v 1.12 2005/08/11 12:43:29 arseniy Exp $
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
 * создание непривязанной линии, состоящей из одного фрагмента, 
 * внесение ее в пул и на карту
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
 */
public class CreateUnboundLinkCommandBundle extends MapActionCommandBundle
{
	UnboundLink unbound;
	
	AbstractNode startNode;
	AbstractNode endNode;

	public CreateUnboundLinkCommandBundle(
			AbstractNode startNode,
			AbstractNode endNode)
	{
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public UnboundLink getUnbound()
	{
		return this.unbound;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try {
			this.unbound = super.createUnboundLinkWithNodeLink(this.startNode, this.endNode);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

