/**
 * $Id: CreateSiteCommandAtomic.java,v 1.10 2005/02/02 09:05:10 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Point;
import java.awt.geom.Point2D;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * ���������� ������� ������� �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @version $Revision: 1.10 $, $Date: 2005/02/02 09:05:10 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateSiteCommandAtomic extends MapActionCommand
{
	/**
	 * ����������� ����
	 */
	SiteNode site;
	
	/** ��� ������������ �������� */
	SiteNodeType proto;
	
	Map map;
	
	/**
	 * �������� �����, � ������� ��������� ����� �������������� ����
	 */
	Point point = null;
	
	/**
	 * �������������� �����, � ������� ��������� ����� �������������� ����.
	 * ����� ������������������ �� point
	 */
	DoublePoint coordinatePoint = null;

	public CreateSiteCommandAtomic(
			SiteNodeType proto,
			DoublePoint dpoint)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.proto = proto;
		this.coordinatePoint = dpoint;
	}

	public CreateSiteCommandAtomic(
			SiteNodeType proto,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.proto = proto;
		this.point = point;
	}

	public SiteNode getSite()
	{
		return site;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;
		
		if(coordinatePoint == null)
			coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		map = logicalNetLayer.getMapView().getMap();

		// ������� ����� ����
		try
		{
			site = SiteNode.createInstance(
					logicalNetLayer.getUserId(),
					coordinatePoint,
					proto);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		SiteNodeController snc = (SiteNodeController)getLogicalNetLayer().getMapViewController().getController(site);
		
		snc.updateScaleCoefficient(site);

		map.addNode(site);
		
		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					site, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);

	}
	
	public void undo()
	{
		map.removeNode(site);
	}
	
	public void redo()
	{
		map.addNode(site);
	}
}
