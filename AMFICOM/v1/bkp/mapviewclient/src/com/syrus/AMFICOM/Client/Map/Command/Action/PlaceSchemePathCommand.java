/**
 * $Id: PlaceSchemePathCommand.java,v 1.5 2004/11/01 15:40:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import java.awt.Point;

import java.util.Iterator;

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.5 $, $Date: 2004/11/01 15:40:10 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemePathCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	MapSiteNodeElement startNode = null;
	MapSiteNodeElement endNode = null;

	MapMeasurementPathElement mPath = null;
	MapUnboundLinkElement unbound = null;
	MapNodeLinkElement nodeLink;

	SchemePath path = null;
	
	Map map;
	MapView mapView;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public PlaceSchemePathCommand(SchemePath path)
	{
		super();
		this.path = path;
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
		Scheme scheme = (Scheme )Pool.get(Scheme.typ, path.getSchemeId());
		
		MapSiteNodeElement[] mne = mapView.getSideNodes(path);
		
		startNode = mne[0];
		endNode = mne[1];
		
		mPath = mapView.findMeasurementPath(path);
		if(mPath == null)
			mPath = super.createMeasurementPath(path, startNode, endNode);
		else
		// если путь уже есть, все его составляющие наносятся заново
			super.removeMeasurementPathCables(mPath);

		for(Iterator it = path.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement )it.next();
			if(pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement )pe.getSchemeElement();
				MapSiteNodeElement site = mapView.findElement(se);
				if(site != null)
				{
//					mPath.addCablePath(site);
				}
			}
			else
			if(pe.getType() == PathElement.LINK)
			{
				SchemeLink link = (SchemeLink )pe.getSchemeLink();
				SchemeElement sse = scheme.getSchemeElementByPort(link.sourcePortId);
				SchemeElement ese = scheme.getSchemeElementByPort(link.targetPortId);
				MapSiteNodeElement ssite = mapView.findElement(sse);
				MapSiteNodeElement esite = mapView.findElement(ese);
				if(ssite == esite)
				{
//					mPath.addCablePath(ssite);
				}
			}
			else
			if(pe.getType() == PathElement.CABLE_LINK)
			{
				SchemeCableLink clink = (SchemeCableLink )pe.getSchemeCableLink();
				MapCablePathElement cp = mapView.findCablePath(clink);
				if(cp != null)
				{
//					mPath.addCablePath(cp);
				}
			}
		}

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				mPath, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(mPath);
		logicalNetLayer.notifySchemeEvent(mPath);
	}
}
