/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.7 2004/11/25 13:00:49 krupenn Exp $
 *
 * Syrus Systems
 * Ќаучно-технический центр
 * ѕроект: јћ‘» ќћ
 *
 * ѕлатформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.util.Iterator;
import java.util.List;

/**
 * –азместить кабель на карте.
 * 
 * @version $Revision: 1.7 $, $Date: 2004/11/25 13:00:49 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	/**
	 * начальный узел кабельного пути
	 */
	MapSiteNodeElement startNode = null;
	
	/**
	 * конечный узел кабельного пути
	 */
	MapSiteNodeElement endNode = null;

	/**
	 * создаваемый кабельный путь
	 */
	MapCablePathElement cablePath = null;
	
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

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		MapSiteNodeElement[] mne = mapView.getSideNodes(scl);
		
		startNode = mne[0];
		endNode = mne[1];
		
		cablePath = mapView.findCablePath(scl);
		// если кабельный путь уже есть - ничего не делать
		if(cablePath != null)
		{
//			super.checkCablePathLinks(cablePath);
			return;
		}

		cablePath = super.createCablePath(scl, startNode, endNode);

		List ccis = (List )scl.channelingItems;

		// идем по всем узлам кабельного пути от начального
		MapSiteNodeElement bufferStartSite = startNode;

		// цикл по элементам прив€зки кабел€.
		for(Iterator it = ccis.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			MapSiteNodeElement smsne = map.getMapSiteNodeElement(cci.startSiteId);
			MapSiteNodeElement emsne = map.getMapSiteNodeElement(cci.endSiteId);

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
				MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, smsne);
				cablePath.addLink(unbound);
				unbound.setCablePath(cablePath);

				bufferStartSite = emsne;
			}
			// в противном случае прив€зать кабель к существующей линии
			{
				
				MapPhysicalLinkElement link = map.getPhysicalLink(cci.physicalLinkId);
				
				// если лини€ не существует, опустить данный элемент прив€зки
				if(link == null)
				{
					MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(smsne, emsne);
					cablePath.addLink(unbound);
					unbound.setCablePath(cablePath);
				}
				else
				{
					link.getBinding().add(cablePath);
					if(cci.row_x != -1
						&& cci.place_y != -1)
						link.getBinding().bind(cablePath, cci.row_x, cci.place_y);
		
					cablePath.addLink(link);
				}
			}
		}

		// если элементы прив€зки не доход€т до конца, создать неприв€занную
		// линию от текущего до конечного узла
		if(endNode != bufferStartSite)
		{
			MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, endNode);
			cablePath.addLink(unbound);
			unbound.setCablePath(cablePath);
		}

		// операци€ закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				cablePath, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(cablePath);
		logicalNetLayer.notifySchemeEvent(cablePath);
	}

}
