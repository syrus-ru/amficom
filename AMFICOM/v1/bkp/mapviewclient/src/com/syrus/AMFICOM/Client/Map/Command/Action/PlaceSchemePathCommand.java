/**
 * $Id: PlaceSchemePathCommand.java,v 1.8 2005/01/30 15:38:17 krupenn Exp $
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;

import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import java.awt.Point;

import java.util.Iterator;

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.8 $, $Date: 2005/01/30 15:38:17 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemePathCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	SiteNode startNode = null;
	SiteNode endNode = null;

	MeasurementPath mPath = null;
	UnboundLink unbound = null;
	NodeLink nodeLink;

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
		Scheme scheme = path.scheme();
		
		SiteNode[] mne = logicalNetLayer.getMapViewController().getSideNodes(path);
		
		startNode = mne[0];
		endNode = mne[1];
		
		mPath = logicalNetLayer.getMapViewController().findMeasurementPath(path);
		if(mPath == null)
			mPath = super.createMeasurementPath(path, startNode, endNode);
		else
		// если путь уже есть, все его составляющие наносятся заново
			super.removeMeasurementPathCables(mPath);

		for(int i = 0; i < path.links().length; i++)
		{
			PathElement pe = (PathElement )path.links()[i];
			switch(pe.type().value())
			{
				case Type._SCHEME_ELEMENT:
					SchemeElement se = (SchemeElement )pe.abstractSchemeElement();
				SiteNode site = logicalNetLayer.getMapViewController().findElement(se);
				if(site != null)
				{
//					mPath.addCablePath(site);
				}
					break;
				case Type._SCHEME_LINK:
					SchemeLink link = (SchemeLink )pe.abstractSchemeElement();
					SchemeElement sse = SchemeUtils.getSchemeElementByDevice(scheme, link.sourceSchemePort().schemeDevice());
					SchemeElement ese = SchemeUtils.getSchemeElementByDevice(scheme, link.targetSchemePort().schemeDevice());
					SiteNode ssite = logicalNetLayer.getMapViewController().findElement(sse);
					SiteNode esite = logicalNetLayer.getMapViewController().findElement(ese);
					if(ssite == esite)
					{
	//					mPath.addCablePath(ssite);
					}
					break;
				case Type._SCHEME_CABLE_LINK:
					SchemeCableLink clink = (SchemeCableLink )pe.abstractSchemeElement();
					CablePath cp = logicalNetLayer.getMapViewController().findCablePath(clink);
					if(cp != null)
					{
	//					mPath.addCablePath(cp);
					}
					break;
				default:
					throw new UnsupportedOperationException();
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
