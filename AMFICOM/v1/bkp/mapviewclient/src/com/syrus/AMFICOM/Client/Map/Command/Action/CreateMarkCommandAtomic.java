/**
 * $Id: CreateMarkCommandAtomic.java,v 1.4 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.MarkController;
import com.syrus.AMFICOM.Client.Resource.Map.NodeLinkController;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;

/**
 * ������� �������� ����� �� �����
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateMarkCommandAtomic extends MapActionCommand
{
	/**
	 * ��������� ������� �����
	 */
	MapMarkElement mark;

	/**
	 * ��������� �������� �����
	 */
	MapPhysicalLinkElement link;
	
	Map map;
	
	/**
	 * ��������� �� ������ �����, �� ������� ��������� �����
	 */
	double distance;
	
	/**
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public CreateMarkCommandAtomic(
			MapPhysicalLinkElement link,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.link = link;
		this.point = point;
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
		
		map = logicalNetLayer.getMapView().getMap();

		link.sortNodeLinks();
		distance = 0.0;
		MapNodeElement node = link.getStartNode();

		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();

			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(mnle);

			if(nlc.isMouseOnElement(mnle, point))
			{
				DoublePoint dpoint = logicalNetLayer.convertScreenToMap(point);
				distance += logicalNetLayer.distance(node.getLocation(), dpoint);
				break;
			}
			else
			{
				nlc.updateLengthLt(mnle);
				distance += mnle.getLengthLt();
			}

			if(mnle.getStartNode().equals(node))
				node = mnle.getEndNode();
			else
				node = mnle.getStartNode();
		}

		mark = new MapMarkElement(
				aContext.getDataSource().GetUId(MapMarkElement.typ),
				map, 
				link, 
				distance);

		Pool.put(MapMarkElement.typ, mark.getId(), mark);
		map.addNode(mark);

		MarkController mc = (MarkController )getLogicalNetLayer().getMapViewController().getController(mark);

		mc.updateScaleCoefficient(mark);
		
		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					mark,
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(mark);
	}
	
	public void undo()
	{
		map.removeNode(mark);
		Pool.remove(MapMarkElement.typ, mark.getId());
	}
	
	public void redo()
	{
		map.addNode(mark);
		Pool.put(MapMarkElement.typ, mark.getId(), mark);
	}
}
