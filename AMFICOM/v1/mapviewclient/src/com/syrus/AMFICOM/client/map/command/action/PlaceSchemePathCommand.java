/**
 * $Id: PlaceSchemePathCommand.java,v 1.15 2005/03/25 18:16:47 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.Client.General.Command.Command;
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
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElementKind;

/**
 * ���������� ������� ���� mpe �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/03/25 18:16:47 $
 * @module mapviewclient_v1
 */
public class PlaceSchemePathCommand extends MapActionCommandBundle
{
	/**
	 * ��������� �������� �����
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
	 * �����, � ������� ��������� ����� �������������� ����
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
		try {
			Scheme scheme = this.path.scheme();
			this.startNode = this.mapView.getStartNode(this.path);
			this.endNode = this.mapView.getEndNode(this.path);
			this.measurementPath = this.mapView.findMeasurementPath(this.path);
			if(this.measurementPath == null)
				this.measurementPath = super.createMeasurementPath(this.path);
			else
			// ���� ���� ��� ����, ��� ��� ������������ ��������� ������
				super.removeMeasurementPathCables(this.measurementPath);
			for(int i = 0; i < this.path.links().length; i++)
			{
				PathElement pe = this.path.links()[i];
				switch(pe.getPathElementKind().value())
				{
					case PathElementKind._SCHEME_ELEMENT:
						SchemeElement schemeElement = (SchemeElement )pe.getAbstractSchemeElement();
					SiteNode site = this.mapView.findElement(schemeElement);
					if(site != null)
					{
//					mPath.addCablePath(site);
					}
						break;
					case PathElementKind._SCHEME_LINK:
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
					case PathElementKind._SCHEME_CABLE_LINK:
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
			// �������� ��������� - ���������� ����������
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
