package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Popup.VoidMapElementPopupMenu;
import com.syrus.AMFICOM.Client.Map.Strategy.ChangeEndPointStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.SelectMarkerStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

//Пустой элемент

public class VoidMapElement extends StubResource implements MapElement 
{
	protected LogicalNetLayer converter;
	public Hashtable attributes = new Hashtable();

	public VoidMapElement( LogicalNetLayer logocal)
	{
		converter = logocal;
	}

	public VoidMapElement()
	{
	}

	public Object clone(DataSourceInterface dataSource)
	{
		return null;
	}
	
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		VoidMapElementPopupMenu menu = new VoidMapElementPopupMenu(
				myFrame, 
				this.getLogicalNetLayer().getMapContext());
		return menu;
	}

	public void paint(Graphics g)
	{
	}

	public void paint(Graphics g, Point myPoint)
	{
	}

	public boolean isSelected()
	{
		return false;
	}

	public void select()
	{
	}

	public void deselect()
	{
	}

	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		return false;
	}

	public String getId()
	{
		return null;
	}

	public void setId(String myID)
	{
	}

	public SxDoublePoint getAnchor()
	{
		return new SxDoublePoint(
			getLogicalNetLayer().getMapViewer().getCenter()[0],
			getLogicalNetLayer().getMapViewer().getCenter()[1] );
	}

	public void move(double deltaX, double deltaY)
	{
	}

//Обработка событий
	public MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		converter = logicalNetLayer;
		int mode = logicalNetLayer.getMode();
		int actionMode = logicalNetLayer.getActionMode();
		int mapState = logicalNetLayer.getMapState();

		MapStrategy strategy = new VoidStrategy();
		Point myPoint = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
//A0A
			if (actionMode == LogicalNetLayer.NULL_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_DRAGGED)
			{
				logicalNetLayer.setActionMode( LogicalNetLayer.SELECT_MARKER_ACTION_MODE );
				return new ChangeEndPointStrategy(aContext, me, logicalNetLayer);
			}
//A0A
			if (actionMode == LogicalNetLayer.NULL_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_PRESSED)
			{
				logicalNetLayer.getMapContext().deselectAll();
				return new VoidStrategy();
			}
//A0A
			if (actionMode == LogicalNetLayer.SELECT_MARKER_ACTION_MODE &&
				logicalNetLayer.getMapState() == LogicalNetLayer.NO_ACTION &&
				mode == LogicalNetLayer.MOUSE_RELEASED)
			{
				logicalNetLayer.setActionMode(LogicalNetLayer.NULL_ACTION_MODE);

				int startX = logicalNetLayer.getStartPoint().x;
				int startY = logicalNetLayer.getStartPoint().y;
				int endX = me.getPoint().x;
				int endY = me.getPoint().y;
				Rectangle selectionRect = new Rectangle(Math.min(startX,endX),
					Math.min(startY,endY),
					Math.abs(endX-startX),
					Math.abs(endY-startY));
				return new SelectMarkerStrategy(aContext, logicalNetLayer, selectionRect);
			}
		}
		return strategy;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return converter;
	}

	public MapContext getMapContext()
	{
		return converter.getMapContext();
	}

	public void setMapContext( MapContext myMapContext)
	{
		converter.setMapContext( myMapContext);
	}

	public ObjectResourceModel getModel()
	{
		return new MapContextModel(getMapContext());
	}

	public String getName()
	{  
		return null;
	}

	public boolean isMovable()
	{
		return false;
	}

	public String getToolTipText()
	{
		MapContext mc = converter.getMapContext();
		String s1 = mc.getName();

		return s1;
	}
}
