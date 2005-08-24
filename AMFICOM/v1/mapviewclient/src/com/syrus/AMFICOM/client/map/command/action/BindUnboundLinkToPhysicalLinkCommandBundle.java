/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.26 2005/08/24 08:19:58 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 *  команда привязывания непривязанной линии к тоннелю. концевые узлы
 *  неправязанной линии и тоннеля должны совпадать
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.26 $, $Date: 2005/08/24 08:19:58 $
 * @module mapviewclient 
 */
public class BindUnboundLinkToPhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * привязываемая линия
	 */
	UnboundLink unbound;
	
	/**
	 * тоннель
	 */
	PhysicalLink link;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindUnboundLinkToPhysicalLinkCommandBundle(
		UnboundLink unbound, 
		PhysicalLink link)
	{
		this.unbound = unbound;
		this.link = link;
	}
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try
		{
			List endNodesList = new ArrayList(2);
			endNodesList.add(this.unbound.getStartNode());
			endNodesList.add(this.unbound.getEndNode());
			if(! (endNodesList.contains(this.link.getStartNode())
					&& endNodesList.contains(this.link.getEndNode()))) {
				setResult(RESULT_NO);
				return;
			}
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			// удаляется непривязанная линия
			super.removeUnboundLink(this.unbound);
			// одновляется информация о привязке кабульного пути
			CablePath cablePath = this.unbound.getCablePath();

			CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(this.unbound);
			CableChannelingItem newCableChannelingItem = 
				CableController.generateCCI(
						cablePath, 
						this.link,
						cableChannelingItem.getStartSiteNode(),
						cableChannelingItem.getEndSiteNode());
			newCableChannelingItem.insertSelfBefore(cableChannelingItem);
			cableChannelingItem.setParentPathOwner(null, false);
			cablePath.removeLink(cableChannelingItem);
			cablePath.addLink(this.link, newCableChannelingItem);

			this.link.getBinding().add(cablePath);
			setResult(Command.RESULT_OK);
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

