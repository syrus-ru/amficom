/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.5 2005/01/31 12:19:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * удаление непривязанно линии из карты, включая элементы, из
 * которых она состоит
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/31 12:19:18 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		return unbound;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		super.removeUnboundLink(unbound);
	}

}

