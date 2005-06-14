/**
 * $Id: PlaceSchemePathCommand.java,v 1.25 2005/06/14 10:53:43 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @author $Author: bass $
 * @version $Revision: 1.25 $, $Date: 2005/06/14 10:53:43 $
 * @module mapviewclient_v1
 */
public class PlaceSchemePathCommand extends MapActionCommandBundle
{
	SiteNode startNode = null;
	SiteNode endNode = null;

	MeasurementPath measurementPath = null;

	SchemePath schemePath = null;
	
	Map map;
	MapView mapView;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public PlaceSchemePathCommand(SchemePath path)
	{
		super();
		this.schemePath = path;
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
			Scheme scheme = this.schemePath.getParentScheme();
			this.startNode = this.mapView.getStartNode(this.schemePath);
			this.endNode = this.mapView.getEndNode(this.schemePath);
			this.measurementPath = this.mapView.findMeasurementPath(this.schemePath);
			if(this.measurementPath == null)
				this.measurementPath = super.createMeasurementPath(this.schemePath, this.startNode, this.endNode);
			else
			// если путь уже есть, все его составляющие наносятся заново
				super.removeMeasurementPathCables(this.measurementPath);
			for(Iterator iter = this.schemePath.getPathElements().iterator(); iter.hasNext();) {
				PathElement pe = (PathElement )iter.next();
				switch(pe.getKind().value())
				{
					case Kind._SCHEME_ELEMENT:
						SchemeElement schemeElement = (SchemeElement )pe.getAbstractSchemeElement();
					SiteNode site = this.mapView.findElement(schemeElement);
					if(site != null)
					{
//					mPath.addCablePath(site);
					}
						break;
					case Kind._SCHEME_LINK:
						SchemeLink schemeLink = (SchemeLink )pe.getAbstractSchemeElement();
						SchemeElement startSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.getSourceSchemePort().getParentSchemeDevice());
						SchemeElement endSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.getTargetSchemePort().getParentSchemeDevice());
						SiteNode startSite = this.mapView.findElement(startSchemeElement);
						SiteNode endSite = this.mapView.findElement(endSchemeElement);
						if(startSite.equals(endSite))
						{
//					mPath.addCablePath(ssite);
						}
						break;
					case Kind._SCHEME_CABLE_LINK:
						SchemeCableLink schemeCableLink = (SchemeCableLink )pe.getAbstractSchemeElement();
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
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
