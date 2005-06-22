/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.18 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * убрать кабельный путь с привязкой из карты
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
 */
public class UnPlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	CablePath cablePath = null;
	
	public UnPlaceSchemeCableLinkCommand(CablePath cablePath)
	{
		super();
		this.cablePath = cablePath;
	}

	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		try {
			SchemeCableLink scl = this.cablePath.getSchemeCableLink();
			scl.setCableChannelingItems(Collections.EMPTY_SET);
			List ccis = new LinkedList();
			for(Iterator it = this.cablePath.getLinks().iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				if(link instanceof UnboundLink)
					continue;
				ccis.add(this.cablePath.getBinding().getCCI(link));
//			scl.channelingItems.add(cablePath.getBinding().getCCI(link));
			}
			super.removeCablePathLinks(this.cablePath);
			super.removeCablePath(this.cablePath);
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
