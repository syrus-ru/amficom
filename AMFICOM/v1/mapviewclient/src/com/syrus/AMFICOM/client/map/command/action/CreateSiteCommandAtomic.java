/**
 * $Id: CreateSiteCommandAtomic.java,v 1.11 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * ���������� ������� ������� �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
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
		return this.site;
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
		
		if(this.coordinatePoint == null)
			this.coordinatePoint = this.logicalNetLayer.convertScreenToMap(this.point);
		
		this.map = this.logicalNetLayer.getMapView().getMap();

		// ������� ����� ����
		try
		{
			this.site = SiteNode.createInstance(
					this.logicalNetLayer.getUserId(),
					this.coordinatePoint,
					this.proto);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		SiteNodeController snc = (SiteNodeController)getLogicalNetLayer().getMapViewController().getController(this.site);
		
		snc.updateScaleCoefficient(this.site);

		this.map.addNode(this.site);
		
		// �������� ��������� - ���������� ����������
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					this.site, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		this.logicalNetLayer.setCurrentMapElement(this.site);
		this.logicalNetLayer.notifySchemeEvent(this.site);

	}
	
	public void undo()
	{
		this.map.removeNode(this.site);
	}
	
	public void redo()
	{
		this.map.addNode(this.site);
	}
}
