/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.14 2005/08/17 14:14:17 arseniy Exp $
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
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление непривязанно линии из карты, включая элементы, из
 * которых она состоит
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/08/17 14:14:17 $
 * @module mapviewclient
 */
public class RemoveUnboundLinkCommandBundle extends MapActionCommandBundle
{
	UnboundLink unbound;
	
	public RemoveUnboundLinkCommandBundle(UnboundLink unbound)
	{
		this.unbound = unbound;
	}
	
	public UnboundLink getUnbound()
	{
		return this.unbound;
	}
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try {
			super.removeUnboundLink(this.unbound);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}

}

