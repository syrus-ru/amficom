/**
 * $Id: PlaceSchemePathCommand.java,v 1.31 2005/07/15 17:06:07 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
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
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.DataPackage.Kind;
import com.syrus.util.Log;

/**
 * ���������� ������� ���� mpe �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.31 $, $Date: 2005/07/15 17:06:07 $
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
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public PlaceSchemePathCommand(SchemePath path)
	{
		super();
		this.schemePath = path;
	}

	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

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
			// ���� ���� ��� ����, ��� ��� ������������ ��������� ������
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
						SchemeElement startSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.getSourceAbstractSchemePort().getParentSchemeDevice());
						SchemeElement endSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.getTargetAbstractSchemePort().getParentSchemeDevice());
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
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			this.logicalNetLayer.setCurrentMapElement(this.measurementPath);
			this.logicalNetLayer.notifySchemeEvent(this.measurementPath);
			this.logicalNetLayer.sendSelectionChangeEvent();
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
