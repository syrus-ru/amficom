/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.10 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * Команда генерации тоннеля по непривязанной линии.
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class GenerateUnboundLinkCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * кабельный путь
	 */
	CablePath path;
	
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
		this.path = unbound.getCablePath();
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		this.path.removeLink(this.unbound);
		this.link = super.createPhysicalLink(
				this.unbound.getStartNode(), 
				this.unbound.getEndNode());
		super.removePhysicalLink(this.unbound);
		
		// перенести фрагменты линии в сгенерированный тоннель
		for(Iterator it2 = this.unbound.getNodeLinks().iterator(); it2.hasNext();)
		{
			NodeLink mnle = (NodeLink)it2.next();
			mnle.setPhysicalLink(this.link);
			this.link.addNodeLink(mnle);
		}
		this.path.addLink(this.link, CableController.generateCCI(this.link));
		this.link.getBinding().add(this.path);

		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

