/**
 * $Id: CreateSiteCommandAtomic.java,v 1.21 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.util.Log;

/**
 * ���������� ������� ������� �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.21 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
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
		try
		{
			Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
			if ( !getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
				return;
			if(this.coordinatePoint == null)
				this.coordinatePoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.point);
			this.map = this.logicalNetLayer.getMapView().getMap();
			// ������� ����� ����
			try
			{
				this.site = SiteNode.createInstance(
						LoginManager.getUserId(),
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
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			this.logicalNetLayer.setCurrentMapElement(this.site);
			this.logicalNetLayer.notifySchemeEvent(this.site);
			setResult(Command.RESULT_OK);
		}
		catch(Exception e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
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
