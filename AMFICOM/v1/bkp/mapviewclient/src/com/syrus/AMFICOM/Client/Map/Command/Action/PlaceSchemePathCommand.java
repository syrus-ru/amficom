/**
 * $Id: PlaceSchemePathCommand.java,v 1.11 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElement;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;
import com.syrus.AMFICOM.scheme.corba.SchemePath;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class PlaceSchemePathCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	SiteNode startNode = null;
	SiteNode endNode = null;

	MeasurementPath measurementPath = null;
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

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		Scheme scheme = this.path.scheme();
		
		this.startNode = this.mapView.getStartNode(this.path);
		this.endNode = this.mapView.getEndNode(this.path);
		
		this.measurementPath = this.mapView.findMeasurementPath(this.path);
		if(this.measurementPath == null)
			this.measurementPath = super.createMeasurementPath(this.path);
		else
		// если путь уже есть, все его составляющие наносятся заново
			super.removeMeasurementPathCables(this.measurementPath);

		for(int i = 0; i < this.path.links().length; i++)
		{
			PathElement pe = this.path.links()[i];
			switch(pe.type().value())
			{
				case Type._SCHEME_ELEMENT:
					SchemeElement schemeElement = (SchemeElement )pe.abstractSchemeElement();
				SiteNode site = this.mapView.findElement(schemeElement);
				if(site != null)
				{
//					mPath.addCablePath(site);
				}
					break;
				case Type._SCHEME_LINK:
					SchemeLink schemeLink = (SchemeLink )pe.abstractSchemeElement();
					SchemeElement startSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.sourceSchemePort().schemeDevice());
					SchemeElement endSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.targetSchemePort().schemeDevice());
					SiteNode startSite = this.mapView.findElement(startSchemeElement);
					SiteNode endSite = this.mapView.findElement(endSchemeElement);
					if(startSite.equals(endSite))
					{
	//					mPath.addCablePath(ssite);
					}
					break;
				case Type._SCHEME_CABLE_LINK:
					SchemeCableLink schemeCableLink = (SchemeCableLink )pe.abstractSchemeElement();
					CablePath cablePath = this.mapView.findCablePath(schemeCableLink);
					if(cablePath != null)
					{
	//					mPath.addCablePath(cablePath);
					}
					break;
				default:
					throw new UnsupportedOperationException();
			}
		}

		// операция закончена - оповестить слушателей
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				this.measurementPath, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		this.logicalNetLayer.setCurrentMapElement(this.measurementPath);
		this.logicalNetLayer.notifySchemeEvent(this.measurementPath);
	}
}
