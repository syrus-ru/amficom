/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.10 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление непривязанно линии из карты, включая элементы, из
 * которых она состоит
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
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
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		try {
			super.removeUnboundLink(this.unbound);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}

}

