/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.3 2004/10/19 10:07:43 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;

/**
 * удаление непривязанно линии из карты, включая элементы, из
 * которых она состоит
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:07:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveUnboundLinkCommandBundle extends MapActionCommandBundle
{
	MapUnboundLinkElement unbound;
	
	public RemoveUnboundLinkCommandBundle(MapUnboundLinkElement unbound)
	{
		this.unbound = unbound;
	}
	
	public MapUnboundLinkElement getUnbound()
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

