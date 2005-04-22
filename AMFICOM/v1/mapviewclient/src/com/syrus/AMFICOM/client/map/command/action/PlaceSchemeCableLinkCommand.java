/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.20 2005/04/22 15:08:45 krupenn Exp $
 *
 * Syrus Systems
 * Ќаучно-технический центр
 * ѕроект: јћ‘» ќћ
 *
 * ѕлатформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

/**
 * –азместить кабель на карте.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.20 $, $Date: 2005/04/22 15:08:45 $
 * @module mapviewclient_v1
 */
public class PlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	/**
	 * начальный узел кабельного пути
	 */
	SiteNode startNode = null;
	
	/**
	 * конечный узел кабельного пути
	 */
	SiteNode endNode = null;

	/**
	 * создаваемый кабельный путь
	 */
	CablePath cablePath = null;
	
	/**
	 * размещаемый кабель
	 */
	SchemeCableLink scl = null;
	
	Map map;

	MapView mapView;
	
	public PlaceSchemeCableLinkCommand(SchemeCableLink scl)
	{
		super();
		this.scl = scl;
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
		
		try {
			this.startNode = this.mapView.getStartNode(this.scl);
			this.endNode = this.mapView.getEndNode(this.scl);
			this.cablePath = this.mapView.findCablePath(this.scl);
			// если кабельный путь уже есть - ничего не делать
			if(this.cablePath != null)
			{
//			super.checkCablePathLinks(cablePath);
				return;
			}
			this.cablePath = super.createCablePath(this.scl, this.startNode, this.endNode);
			// идем по всем узлам кабельного пути от начального
			SiteNode bufferStartSite = this.startNode;
			// цикл по элементам прив€зки кабел€.
			for(Iterator iter = this.scl.getCableChannelingItems().iterator(); iter.hasNext();) {
				CableChannelingItem cci = (CableChannelingItem )iter.next();
				SiteNode smsne = cci.getStartSiteNode();
				SiteNode emsne = cci.getEndSiteNode();

				// если элемент прив€зки не соответствует топологической схеме
				// (один из узлов прив€зки не нанесен на карту) то элемент
				// прив€зки опускаетс€
				if(smsne == null
					|| emsne == null)
				{
					continue;
				}

				// a link between bufferStartSite and current cci exists
				boolean exists = false;
				
				// переходим к следующему узлу кабельного пути
				if(bufferStartSite.equals(smsne))
				{
					bufferStartSite = emsne;
					exists = true;
				}
				else
				if(bufferStartSite.equals(emsne))
				{
					bufferStartSite = smsne;
					exists = true;
				}
				
				// если ни одно из двух предыдущих условий не выполнено, то есть
				// существует разрыв последовательности прив€зки (линии 
				// bufferStartSite - cci.startSiteId не существует), то
				// создать на месте разрыва непроложенную линию из одного фрагмента
				if(!exists)
				{
					UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, smsne);
					this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, this.logicalNetLayer.getUserId()));
					unbound.setCablePath(this.cablePath);

					bufferStartSite = emsne;
				}
				// в противном случае прив€зать кабель к существующей линии
				{
					
					PhysicalLink link = cci.getPhysicalLink();
					
					// если лини€ не существует, опустить данный элемент прив€зки
					if(link == null)
					{
						UnboundLink unbound = super.createUnboundLinkWithNodeLink(smsne, emsne);
						this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, this.logicalNetLayer.getUserId()));
						unbound.setCablePath(this.cablePath);
					}
					else
					{
						link.getBinding().add(this.cablePath);
						if(cci.getRowX() != -1
							&& cci.getPlaceY() != -1)
							link.getBinding().bind(this.cablePath, cci.getRowX(), cci.getPlaceY());
			
						this.cablePath.addLink(link, CableController.generateCCI(this.cablePath, link, this.logicalNetLayer.getUserId()));
					}
				}
			}
			// если элементы прив€зки не доход€т до конца, создать неприв€занную
			// линию от текущего до конечного узла
			if(this.endNode != bufferStartSite)
			{
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, this.endNode);
				this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, this.logicalNetLayer.getUserId()));
				unbound.setCablePath(this.cablePath);
			}
			// операци€ закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					this.cablePath, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			this.logicalNetLayer.setCurrentMapElement(this.cablePath);
			this.logicalNetLayer.notifySchemeEvent(this.cablePath);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}

}
