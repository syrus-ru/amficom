/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.7 2005/01/31 12:19:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.syrus.AMFICOM.mapview.CablePathBinding;

/**
 * убрать кабельный путь с привязкой из карты
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/31 12:19:18 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		SchemeCableLink scl = cablePath.getSchemeCableLink();
		scl.cableChannelingItems(null);//.clear();
		
		List ccis = new LinkedList();

		for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			if(link instanceof UnboundLink)
				continue;
			ccis.add(cablePath.getBinding().getCCI(link));
//			scl.channelingItems.add(cablePath.getBinding().getCCI(link));
		}
		scl.cableChannelingItems((CableChannelingItem [])ccis.toArray(new CableChannelingItem [0]));

		super.removeCablePathLinks(cablePath);

		super.removeCablePath(cablePath);

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
