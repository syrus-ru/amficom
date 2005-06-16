/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.25 2005/06/16 10:57:19 krupenn Exp $
 *
 * Syrus Systems
 * Ќаучно-технический центр
 * ѕроект: јћ‘» ќћ
 *
 * ѕлатформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * –азместить кабель на карте.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.25 $, $Date: 2005/06/16 10:57:19 $
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
	SchemeCableLink schemeCableLink = null;
	
	Map map;

	MapView mapView;
	
	public PlaceSchemeCableLinkCommand(SchemeCableLink schemeCableLink)
	{
		super();
		this.schemeCableLink = schemeCableLink;
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
			this.startNode = this.mapView.getStartNode(this.schemeCableLink);
			this.endNode = this.mapView.getEndNode(this.schemeCableLink);
			this.cablePath = this.mapView.findCablePath(this.schemeCableLink);
			// если кабельный путь уже есть - ничего не делать
			if(this.cablePath != null)
			{
//			super.checkCablePathLinks(cablePath);
				return;
			}
			this.cablePath = super.createCablePath(this.schemeCableLink, this.startNode, this.endNode);
			// идем по всем узлам кабельного пути от начального
			SiteNode bufferStartSite = this.startNode;
			// цикл по элементам прив€зки кабел€.
			Identifier userId = LoginManager.getUserId();
			for(Iterator iter = this.schemeCableLink.getCableChannelingItems().iterator(); iter.hasNext();) {
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
					this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, userId));
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
						this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, userId));
						unbound.setCablePath(this.cablePath);
					}
					else
					{
						link.getBinding().add(this.cablePath);
						if(cci.getRowX() != -1
							&& cci.getPlaceY() != -1)
							link.getBinding().bind(this.cablePath, cci.getRowX(), cci.getPlaceY());
			
						this.cablePath.addLink(link, CableController.generateCCI(this.cablePath, link, userId));
					}
				}
			}
			// если элементы прив€зки не доход€т до конца, создать неприв€занную
			// линию от текущего до конечного узла
			if(this.endNode != bufferStartSite)
			{
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, this.endNode);
				this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, userId));
				unbound.setCablePath(this.cablePath);
			}
			// операци€ закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			this.logicalNetLayer.setCurrentMapElement(this.cablePath);
			this.logicalNetLayer.notifySchemeEvent(this.cablePath);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}

}
