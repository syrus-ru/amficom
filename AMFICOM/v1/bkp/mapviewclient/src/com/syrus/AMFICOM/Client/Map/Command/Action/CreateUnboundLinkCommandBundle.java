/**
 * $Id: CreateUnboundLinkCommandBundle.java,v 1.7 2005/02/18 12:19:44 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * создание непривязанной линии, состоящей из одного фрагмента, 
 * внесение ее в пул и на карту
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/18 12:19:44 $
 * @module mapviewclient_v1
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		try {
			this.unbound = super.createUnboundLinkWithNodeLink(this.startNode, this.endNode);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

