/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.24 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 * Команда генерации тоннеля по непривязанной линии.
 * @author $Author: arseniy $
 * @version $Revision: 1.24 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
 */
public class GenerateUnboundLinkCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * кабельный путь
	 */
	CablePath cablePath;
	
	/**
	 * непривязанная линия
	 */
	UnboundLink unbound;
	
	/**
	 * созданный тоннель
	 */
	PhysicalLink link;

	/**
	 * Карта, на которой производится операция
	 */
	MapView mapView;

	Map map;

	public GenerateUnboundLinkCablingCommandBundle(UnboundLink unbound)
	{
		this.unbound = unbound;
		this.cablePath = unbound.getCablePath();
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();

		if(this.unbound.getStartNode() instanceof UnboundNode
				|| this.unbound.getEndNode() instanceof UnboundNode) {
			setResult(Command.RESULT_NO);
			return;
		}
		
		try {
			this.link = super.createPhysicalLink(
					this.unbound.getStartNode(), 
					this.unbound.getEndNode());
			// перенести фрагменты линии в сгенерированный тоннель
			for(Iterator it2 = new LinkedList(this.unbound.getNodeLinks()).iterator(); it2.hasNext();)
			{
				NodeLink mnle = (NodeLink)it2.next();
				mnle.setPhysicalLink(this.link);
			}

			CableChannelingItem cableChannelingItem = this.cablePath.getFirstCCI(this.unbound);
			CableChannelingItem newCableChannelingItem = CableController.generateCCI(this.cablePath, this.link);
			newCableChannelingItem.insertSelfBefore(cableChannelingItem);
			cableChannelingItem.setParentPathOwner(null, false);
			this.cablePath.removeLink(cableChannelingItem);
			this.cablePath.addLink(this.link, newCableChannelingItem);

			super.removePhysicalLink(this.unbound);
			this.link.getBinding().add(this.cablePath);
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

}

