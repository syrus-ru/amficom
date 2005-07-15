/**
 * $Id: MapVoidElementStrategy.java,v 1.29 2005/07/15 17:06:08 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.util.Log;

/**
 * ��������� ���������� ����������, ����� ��� ��������� ���������.
 * @author $Author: krupenn $
 * @version $Revision: 1.29 $, $Date: 2005/07/15 17:06:08 $
 * @module mapviewclient_v1
 */
public final class MapVoidElementStrategy extends AbstractMapStrategy 
{
	/**
	 * �������������� �����.
	 */
	Map map;
	/**
	 * ��� �����.
	 */
	MapView mapView;

	/**
	 * Instance.
	 */
	private static MapVoidElementStrategy instance = new MapVoidElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapVoidElementStrategy()
	{//empty
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapVoidElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.mapView = ((VoidElement)me).getMapView();
		this.map = this.mapView.getMap();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.NULL_ACTION_MODE)
		{
			super.logicalNetLayer.deselectAll();
			this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
		}//MapState.NULL_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (actionMode == MapState.NULL_ACTION_MODE &&
			operationMode == MapState.NO_OPERATION)
		{
			mapState.setActionMode(MapState.SELECT_MARKER_ACTION_MODE);
		}//MapState.NULL_ACTION_MODE && MapState.NO_OPERATION
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (actionMode == MapState.SELECT_MARKER_ACTION_MODE &&
			operationMode == MapState.NO_OPERATION)
		{
			int startX = super.logicalNetLayer.getStartPoint().x;
			int startY = super.logicalNetLayer.getStartPoint().y;
			int endX = point.x;
			int endY = point.y;
			Rectangle selectionRect = new Rectangle(
					Math.min(startX, endX),
					Math.min(startY, endY),
					Math.abs(endX - startX),
					Math.abs(endY - startY));
			selectElementsInRect(selectionRect);

			mapState.setActionMode(MapState.NULL_ACTION_MODE);
		}//MapState.SELECT_MARKER_ACTION_MODE && MapState.NO_OPERATION
	}

	/**
	 * �������� ��� ��������, ���������� � �������.
	 * @param selectionRect ������� ������� � �������� �����������
	 */
	protected  void selectElementsInRect(Rectangle selectionRect)
		throws MapConnectionException, MapDataException
	{
		long t1 = System.nanoTime();
		//����� ������ ����������� ��� ������� ���������� � ������������� �������
		Iterator e = this.map.getNodes().iterator();
		
		//��������� � ������� ������ �� � ������� MapNodeElement
		while (e.hasNext())
		{
			AbstractNode node = (AbstractNode)e.next();
			Point p = super.logicalNetLayer.getConverter().convertMapToScreen(node.getLocation());

			if (selectionRect.contains(p))
			{
				this.map.setSelected(node, true);
			}
			else
			{
				this.map.setSelected(node, false);
			}
		}

		e = this.map.getAllNodeLinks().iterator();

		if(super.logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			//��������� � ������� ������ �� � ������� nodeLink
			while (e.hasNext() )
			{
				NodeLink nodeLink = (NodeLink)e.next();
				if (
					selectionRect.contains(
						super.logicalNetLayer.getConverter().convertMapToScreen(
							nodeLink.getStartNode().getLocation())) 
					&& selectionRect.contains(
						super.logicalNetLayer.getConverter().convertMapToScreen(
							nodeLink.getEndNode().getLocation())))
				{
					this.map.setSelected(nodeLink, true);
				}
				else
				{
					this.map.setSelected(nodeLink, false);
				}
			}
		}
		else
		if(super.logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			for(Iterator it = this.map.getAllPhysicalLinks().iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				boolean select = true;
				for(Iterator it2 = link.getNodeLinks().iterator(); it2.hasNext();)
				{
					NodeLink nodeLink = (NodeLink)it2.next();
					if (! (
						selectionRect.contains(
							super.logicalNetLayer.getConverter().convertMapToScreen(
								nodeLink.getStartNode().getLocation())) 
						&& selectionRect.contains(
							super.logicalNetLayer.getConverter().convertMapToScreen(
								nodeLink.getEndNode().getLocation()))))
					{ 
						select = false;
					}
				}
				this.map.setSelected(link, select);
			}
		}
		
		Set selection = super.logicalNetLayer.getSelectedElements();
		if(selection.size() == 1)
		{
			MapElement me = (MapElement)selection.iterator().next();
			super.logicalNetLayer.setCurrentMapElement(me);
		}
		else
		if(selection.size() > 1)
		{
			Selection sel = new Selection();
			sel.addAll(selection);
			super.logicalNetLayer.setCurrentMapElement(sel);
		}
		long t2 = System.nanoTime();
		this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
		Log.debugMessage("MapVoidElementStrategy.selectElementsInRect | " + (t2 - t1) + " ns ", Level.INFO);
	}
}

