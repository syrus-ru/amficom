/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.1 2004/10/14 15:39:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление узла из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/10/14 15:39:05 $
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
		super.removeUnboundLink(unbound);
	}

}

