/**
 * $Id: MapMarkElementStrategy.java,v 1.14 2005/02/01 16:16:13 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveMarkCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkController;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.UI.MotionDescriptor;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * ��������� ���������� ����� �� ���������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/01 16:16:13 $
 * @module mapviewclient_v1
 */
public final class MapMarkElementStrategy extends MapStrategy 
{
	/**
	 * �����.
	 */
	Mark mark;
	/**
	 * ������� ����������� ����� ����� �����. ������� ��������� ��� ������
	 * ����������� � ����������� ��� ���������� �����������.
	 */
	MoveMarkCommand command;

	/**
	 * instance.
	 */
	private static MapMarkElementStrategy instance = new MapMarkElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapMarkElementStrategy()
	{
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapMarkElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.mark = (Mark)me;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doContextChanges(MouseEvent me)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges()");
		
		MapState mapState = logicalNetLayer.getMapState();
		
		MapCoordinatesConverter converter = logicalNetLayer;

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof Selection)
					{
						Selection sel = (Selection)mel;
						sel.add(mark);
					}
					else
					{
						Selection sel = new Selection(logicalNetLayer.getMapView().getMap());
						sel.addAll(logicalNetLayer.getSelectedElements());
						logicalNetLayer.setCurrentMapElement(sel);
					}
				}
				if ((actionMode != MapState.SELECT_ACTION_MODE) &&
					(actionMode != MapState.MOVE_ACTION_MODE) )
				{
					logicalNetLayer.deselectAll();
				}
				mark.setSelected(true);
			}//MapState.MOUSE_PRESSED
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				if(command == null)
					command = new MoveMarkCommand(mark);
				
				if (aContext.getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_EDIT_MAP))
				{
					NodeLink nodeLink = mark.getNodeLink();
					AbstractNode sn = mark.getStartNode();
					AbstractNode en = nodeLink.getOtherNode(sn);

					//��������� � ���������� ��������� ������� ���������� ���� ������������� ���������
					//������� �� ����� �� ������� ������ ���������

					Point anchorPoint = converter.convertMapToScreen(mark.getLocation());
					
					Point start = converter.convertMapToScreen(sn.getLocation());
					Point end = converter.convertMapToScreen(en.getLocation());

					double lengthFromStartNode;
					
					MotionDescriptor md = new MotionDescriptor(start, end, anchorPoint, point);

					lengthFromStartNode = md.lengthFromStartNode;

					while(lengthFromStartNode > md.nodeLinkLength)
					{
						nodeLink = mark.getPhysicalLink().nextNodeLink(nodeLink);
						if(nodeLink == null)
							lengthFromStartNode = md.nodeLinkLength;
						else
						{
							sn = en;
							en = nodeLink.getOtherNode(sn);
	
							mark.setNodeLink(nodeLink);
							mark.setStartNode(sn);

							start = converter.convertMapToScreen(sn.getLocation());
							end = converter.convertMapToScreen(en.getLocation());

							md = new MotionDescriptor(start, end, anchorPoint, point);

							lengthFromStartNode = md.lengthFromStartNode;
							
							if(lengthFromStartNode < 0)
							{
								lengthFromStartNode = 0;
								break;
							}
						}
					}
					while(lengthFromStartNode < 0)
					{
						nodeLink = mark.getPhysicalLink().previousNodeLink(mark.getNodeLink());
						if(nodeLink == null)
							lengthFromStartNode = 0;
						else
						{
							en = sn;
							sn = nodeLink.getOtherNode(en);
	
							mark.setNodeLink(nodeLink);
							mark.setStartNode(sn);
	
							start = converter.convertMapToScreen(sn.getLocation());
							end = converter.convertMapToScreen(en.getLocation());

							md = new MotionDescriptor(start, end, anchorPoint, point);

							lengthFromStartNode = md.lengthFromStartNode;
							
							if(lengthFromStartNode > md.nodeLinkLength)
							{
								lengthFromStartNode = md.nodeLinkLength;
								break;
							}
						}
					}

					MarkController mc = (MarkController)logicalNetLayer.getMapViewController().getController(mark);

					mc.adjustPosition(mark, lengthFromStartNode);
				}
			}//MapState.MOUSE_DRAGGED
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
			}//MapState.MOUSE_RELEASED

		}//SwingUtilities.isLeftMouseButton(me)

	}
}

