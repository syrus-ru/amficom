/**
 * $Id: LogicalNetLayer.java,v 1.52 2005/03/16 12:54:57 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.CommandList;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveNodeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.EventMarker;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.PathDecomposer;

/**
 * ��������� ������������ ���������� ��������� ����.
 * 
 * 
 * 
 * @author $Author: bass $
 * @version $Revision: 1.52 $, $Date: 2005/03/16 12:54:57 $
 * @module mapviewclient_v2
 */
public abstract class LogicalNetLayer implements MapCoordinatesConverter
{
	protected CommandList commandList = new CommandList(20);
	
	/** ����, ����������� ��������� �� ����. */
	protected AnimateThread animateThread = null;

	/** ��������� ������������ ��� ������ ����������. */
	protected MapState mapState = new MapState();
	
	/** ���������� �����. */
	protected MapView mapView = null;
	
	/**
	 * ���� ��������� �������, ����������� ��� ��������� ������������.
	 * ��� �������� ��������� ���������� ���� ������������ � false � ���, 
	 * ����� ��������� ��������� ����� ������� (������� ������ ����).
	 * ����� ������� ���� ������������ ������� � true. ��� ���������
	 * ������� ������� ��������� ���� ����, � ���� �� ����� false, �� ���������
	 * ����� ������� �� �����������.
	 */
	protected boolean performProcessing = true;
	
	/** ���� �������� ���������. */
	protected boolean doNotify = true;
	
	/** ������� �������. */
	protected MapElement currentMapElement = null;

	/** 
	 * ������������� ����. 
	 * ������������ � ������ {@link MapState#MOVE_FIXDIST}.
	 */
	protected AbstractNode fixedNode = null;

	/** 
	 * ������ ������������� �����. 
	 * ������������ � ������ {@link MapState#MOVE_FIXDIST}.
	 */
	protected List fixedNodeList = new LinkedList();

	/** ������� ��� ����������� ���������� �����. */
	protected PhysicalLinkType currentPen = null;

	/** ��� �������������� �������� ��������. */
	protected SiteNodeType unboundProto = null;

	/** ��� �������������� ������. */
	protected PhysicalLinkType unboundLinkProto = null;
	
	/** ������� ����� ������� ���� �� ����� (� �������� �����������). */	
	protected Point currentPoint = new Point(0, 0);

	/** �������� ����������. */
	protected ApplicationContext aContext = null;

	/** ������������� ������������. ������������ ��� �������� ����� ��������. */
	protected Identifier userId = null;

	/** ������, ������������� �� ���������� ������������ �����. */
	protected NetMapViewer viewer = null;

	/**
	 * ��������� ����� ��� �������� ������������ �� ����� � ������� ����.
	 * ��� ������� ������ ������ ���� � � �����������, ���� startX, startY ��������
	 * ���������� ��������� ����� (��� ��������� �������), � ���� endX, endY ����������
	 * �������� ��������� ����.
	 */
	protected Point startPoint = new Point(0, 0);

	/**
	 * �������� ����� ��� �������� ������������ �� ����� � ������� ����
	 * ��� ������� ������ ������ ���� � � �����������, ���� startX, startY ��������
	 * ���������� ��������� ����� (��� ��������� �������), � ���� endX, endY ����������
	 * �������� ��������� ����.
	 */
	protected Point endPoint = new Point(0, 0);

	/** ���� ����, ��� ����������� ���� ����������. */	
	
	protected boolean menuShown = false;

	/**
	 * ������ ������������ ���������.
	 */
	protected List elementsToDisplay = new LinkedList();

	/**
	 * ������� ����������� �������� ����� �� ���������. ������������ ���
	 * ��������������� ��������������� ��������� ��������. �����������
	 * ���������� ������� ��������������� ������� ������������ ��� ���������
	 * defaultScale / currentScale.
	 */
	protected double defaultScale = 0.00001;
	
	/** ������� ������� ����������� �������� �����. */
	protected double currentScale = 0.00001;

	/**
	 * �������� �������� ���������� �� �������������� �����������.
	 * @param point �������������� ����������
	 * @return �������� ����������
	 */
	public abstract Point convertMapToScreen(DoublePoint point)
		throws MapConnectionException, MapDataException;
	
	/**
	 * �������� �������������� ���������� �� ��������.
	 * @param point �������� ����������
	 * @return �������������� ����������
	 */
	public abstract DoublePoint convertScreenToMap(Point point)
		throws MapConnectionException, MapDataException;

	/**
	 * �������� ��������� ����� ����� ������� � �������� �����������.
	 * @param from �������������� ����������
	 * @param to �������������� ����������
	 * @return ����������
	 */
	public abstract double distance(DoublePoint from, DoublePoint to)
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� ����������� ����� ���� �����.
	 * @param center �������������� ���������� ������
	 */
	public abstract void setCenter(DoublePoint center)
		throws MapConnectionException, MapDataException;

	/**
	 * �������� ����������� ����� ���� �����.
	 * @return �������������� ���������� ������
	 */
	public abstract DoublePoint getCenter()
		throws MapConnectionException, MapDataException;

	/**
	 * �������� ������� ������� � �������������� �����������.
	 * @return ������� �������
	 */
	public abstract Rectangle2D.Double getVisibleBounds()
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� ����� �������������� �������� �� ���������.
	 * @param searchText ����� ������
	 * @return ������ ��������� �������� ({@link SpatialObject})
	 */
	public abstract List findSpatialObjects(String searchText)
		throws MapConnectionException, MapDataException;
	
	/**
	 * ������������ �������������� ������.
	 * @param so �������������� ������
	 */
	public abstract void centerSpatialObject(SpatialObject so)
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� ������� ���������� � ������ � ��������� ����������� �����.
	 */
	public abstract void release()
		throws MapConnectionException, MapDataException;

	/**
	 * ������������ ���������� ���������� � ������.
	 * @param fullRepaint ����������� �� ������ �����������.
	 * <code>true</code> - ���������������� �������������� ������� � ��������
	 * �������������� �����. <code>false</code> - ���������������� ������ 
	 * �������� �������������� �����.
	 */
	public abstract void repaint(boolean fullRepaint)
		throws MapConnectionException, MapDataException;
	
	/**
	 * ���������� ������ ���� �� ���������� ����������� �����.
	 * @param cursor ������
	 */
	public abstract void setCursor(Cursor cursor);

	/**
	 * �������� ������������� ������.
	 * @return ������
	 */
	public abstract Cursor getCursor();

	/**
	 * �������� ������� ������� ���� �����.
	 * @return �������
	 */
	public abstract double getScale()
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� �������� ������� ���� �����.
	 * @param scale �������
	 */
	public abstract void setScale(double scale)
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� ������� ���� ����� � �������� �������������.
	 * @param scale�oef ����������� ���������������
	 */
	public abstract void scaleTo(double scale�oef)
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� ��� ����� �� ����������� �������������.
	 */
	public abstract void zoomIn()
		throws MapConnectionException, MapDataException;

	/**
	 * �������� ��� ����� �� ����������� �������������.
	 */
	public abstract void zoomOut()
		throws MapConnectionException, MapDataException;
	
	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����.
	 * @param from �������������� ����������
	 * @param to �������������� ����������
	 */
	public abstract void zoomToBox(DoublePoint from, DoublePoint to)
		throws MapConnectionException, MapDataException;

	/**
	 * � ������ ����������� ����� "������" ({@link MapState#MOVE_HAND})
	 * ����������� ����.
	 * @param me ������� �������
	 */	
	public abstract void handDragged(MouseEvent me)
		throws MapConnectionException, MapDataException;
	
	/**
	 * ��� ��������� �������� ����������� ����� ���������� ��������
	 * ������� ����������� ���� �������� �� �����.
	 */
	public void updateZoom()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateZoom()");
		
		if(getMapView() == null)
			return;
		Map map = getMapView().getMap();
		if(map != null)
		{
			Iterator en =  map.getNodes().iterator();
			while (en.hasNext())
			{
				AbstractNode curNode = (AbstractNode)en.next();
				((AbstractNodeController)getMapViewController().getController(curNode)).updateScaleCoefficient(curNode);
			}
		}
	}

	/**
	 * ���������� �������� ����������.
	 * @param aContext �������� ����������
	 */
	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;

		this.userId = new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().user_id);
	}

	/**
	 * �������� �������� ����������.
	 * @return �������� ����������
	 */
	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	/**
	 * ���������� ������������.
	 * @param mapViewer ������������
	 */
	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapViewer(" + mapViewer + ")");
		
		this.viewer = mapViewer;
	}

	/**
	 * �������� ������������.
	 * @return ������������
	 */
	public NetMapViewer getMapViewer()
	{
		return this.viewer;
	}

	/**
	 * ���������������� ������������ �������� ����� �����.
	 * @param mapView ���
	 */
	public void setMapView(MapView mapView)
		throws MapConnectionException, MapDataException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapView(" + mapView + ")");

		if(this.animateThread != null)
			this.animateThread.stopRunning();

		if(	getContext() != null
			&& getContext().getDispatcher() != null)
		{
				if(mapView != null
					&& mapView.getMap() != null)
				{
					this.aContext.getDispatcher().notify(
						new MapEvent(mapView, MapEvent.MAP_VIEW_SELECTED));
					this.aContext.getDispatcher().notify(
						new MapEvent(mapView.getMap(), MapEvent.MAP_SELECTED));
				}
				else
				{
					this.aContext.getDispatcher().notify(
							new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
					this.aContext.getDispatcher().notify(
						new MapEvent(this, MapEvent.MAP_DESELECTED));
				}
		}

		this.mapView = mapView;

		if(mapView == null)
		{
			System.out.println("mapView null!");
		}
		else
		{
			setScale(this.mapView.getScale());
			setCenter(this.mapView.getCenter());

			if(this.aContext != null)
				if(this.aContext.getApplicationModel() != null)
					if (this.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_INDICATION))
				{
					this.animateThread = new AnimateThread(this);
					this.animateThread.start();
				}
		}

		getMapViewController().setMapView(this.mapView);

		//����������� ������� ������� Void
		this.currentMapElement = VoidElement.getInstance(this.mapView);

		this.commandList.flush();

		repaint(true);
	}

	/**
	 * �������� ���.
	 * @return ���
	 */
	public MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * ���������� �������������� �����.
	 * @param map �������������� �����
	 */
	public void setMap( Map map)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMap(" + map + ")");
		
		getMapView().setMap(map);
	}

	/**
	 * �������� ������� ��������� �� ���������.
	 * @return ������� ��������� �� ���������
	 */
	public double getDefaultScale()
	{
		return this.defaultScale;
	}
	
	/**
	 * ���������� ������� ��������� �� ���������.
	 * @param defaultScale ������� ��������� �� ���������
	 */
	public void setDefaultScale(double defaultScale)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setDefaultScale(" + defaultScale + ")");
		
		this.defaultScale = defaultScale;
		updateZoom();
	}
	
	/**
	 * �������� ������� ������� �������� ���������.
	 * @return ������� ������� �������� ���������
	 */
	public double getCurrentScale()
	{
		return this.currentScale;
	}
	
	/**
	 * ���������� ������� ������� �������� ���������.
	 * @param currentScale ������� ������� �������� ���������
	 */
	public void setCurrentScale(double currentScale)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentScale(" + currentScale + ")");
		this.currentScale = currentScale;
		updateZoom();
	}
	
	/**
	 * �������� ������� ��������� ���� �����.
	 * @return ��������� ���� �����
	 */
	public MapState getMapState()
	{
		return this.mapState;
	}

	/**
	 * ���������� ������� ��������� ���� �����.
	 * @param state ����� ���������
	 */
	public void setMapState(MapState state)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapState(" + state + ")");
		
		this.mapState = state;
	}

	/**
	 * �������� ������� �������� �����.
	 * @return ������� �������� �����
	 */
	public Point getCurrentPoint()
	{
		return this.currentPoint;
	}
	
	/**
	 * ���������� ������� �������� �����.
	 * @param point ������� �������� �����
	 */
	public void setCurrentPoint(Point point)
	{
		this.currentPoint = point;
	}
	
	/**
	 * �������� ������� �� ����������� ��������� ������� ����� � ���� ���������.
	 * @param point �������� ���������� ����
	 */	
	public void showLatLong(Point point)
		throws MapConnectionException, MapDataException
	{
		if(this.aContext == null)
			return;
		Dispatcher disp = this.aContext.getDispatcher();
		if(disp == null)
			return;
		DoublePoint p = this.convertScreenToMap(point);
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_CENTER_CHANGED));
	}

	/**
	 * �������� ����������� ��������� �������� �����.
	 * @return ��������� �������� �����
	 */
	public Point getStartPoint()
	{
		return new Point(this.startPoint);
	}

	/**
	 * ���������� ��������� �������� �����.
	 * @param point ��������� �������� �����
	 */
	public void setStartPoint(Point point)
	{
		this.startPoint = new Point(point);
	}

	/**
	 * �������� ����������� �������� �������� �����.
	 * @return �������� �������� �����
	 */
	public Point getEndPoint()
	{
		return new Point(this.endPoint);
	}

	/**
	 * ���������� �������� �������� �����.
	 * @param point �������� �������� �����
	 */
	public void setEndPoint(Point point)
	{
		this.endPoint = new Point(point);
	}
	
	/**
	 * �������� ������ ������, ����������� �� ����.
	 * @return ������ ������
	 */
	public CommandList getCommandList()
	{
		return this.commandList;
	}

	/**
	 * �������� ���� ����������� ������������ ����.
	 * @return ����
	 */
	public boolean isMenuShown()
	{
		return this.menuShown;
	}

	/**
	 * ���������� ���� ����������� ������������ ����.
	 * @param isMenuShown ����
	 */	
	public void setMenuShown(boolean isMenuShown)
	{
		this.menuShown = isMenuShown;
	}

	/**
	 * ���������� ��� �������� ����������� ���� �������������� �����.
	 * @param g ����������� ��������
	 */
	public void paint(Graphics g)
		throws MapConnectionException, MapDataException
	{
		Graphics2D p = (Graphics2D )g;
		
		// remember settings from graphics
		Color color = p.getColor();
		Stroke stroke = p.getStroke();
		Font font = p.getFont();
		Color background = p.getBackground();

		drawLines(p);
		drawNodes(p);
		drawSelection(p);
		drawTempLines(p);
		
		// revert graphics to previous settings
		p.setColor(color);
		p.setStroke(stroke);
		p.setFont(font);
		p.setBackground(background);
	}

	/**
	 * ���������� �������� �������.
	 * @param g ����������� ��������
	 */
	public void drawLines(Graphics g)
		throws MapConnectionException, MapDataException
	{
		Iterator e;
	
		Rectangle2D.Double visibleBounds = this.getVisibleBounds();
		
		this.elementsToDisplay.clear();

		//���� ����� ������ nodeLink �� ��������, �� ������� ����� ������ physicalLink
		if (! this.aContext.getApplicationModel().isEnabled("mapModeNodeLink"))
		{
			Command com = getContext().getApplicationModel().getCommand("mapModeLink");
			com.setParameter("applicationModel", this.aContext.getApplicationModel());
			com.setParameter("logicalNetLayer", this);
			com.execute();
		}

		if (getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH)
		{
			this.elementsToDisplay.addAll(getMapView().getMap().getAllPhysicalLinks());
			for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();)
			{
				MeasurementPath mpath = 
					(MeasurementPath)it.next();

				getMapViewController().getController(mpath).paint(mpath, g, visibleBounds);
				
				// to avoid multiple cicling through scheme elements
				// use once sorted cable links
				for(Iterator it2 = mpath.getSortedCablePaths().iterator(); it2.hasNext();)
				{
					CablePath cpath = 
						(CablePath)it2.next();
					this.elementsToDisplay.removeAll(cpath.getLinks());
				}
			}
			for(Iterator it = this.elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink mple = 
					(PhysicalLink)it.next();
				getMapViewController().getController(mple).paint(mple, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			this.elementsToDisplay.addAll(getMapView().getMap().getAllPhysicalLinks());
			for(Iterator it = this.mapView.getCablePaths().iterator(); it.hasNext();)
			{
				CablePath cpath = 
					(CablePath)it.next();
				getMapViewController().getController(cpath).paint(cpath, g, visibleBounds);
				this.elementsToDisplay.removeAll(cpath.getLinks());
			}
			for(Iterator it = this.elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink mple = 
					(PhysicalLink)it.next();
				getMapViewController().getController(mple).paint(mple, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			e = getMapView().getMap().getAllPhysicalLinks().iterator();
			while (e.hasNext())
			{
				PhysicalLink mple = 
					(PhysicalLink)e.next();
				getMapViewController().getController(mple).paint(mple, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			e = getMapView().getMap().getAllNodeLinks().iterator();
			while (e.hasNext())
			{
				NodeLink curNodeLink = (NodeLink)e.next();
				getMapViewController().getController(curNodeLink).paint(curNodeLink, g, visibleBounds);
			}
		}
	}

	/**
	 * ���������� ����.
	 * @param g ����������� ��������
	 */
	public void drawNodes(Graphics g)
		throws MapConnectionException, MapDataException
	{
		Rectangle2D.Double visibleBounds = this.getVisibleBounds();

		boolean showNodes = MapPropertiesManager.isShowPhysicalNodes();
		Iterator e = getMapView().getMap().getNodes().iterator();
		while (e.hasNext())
		{
			AbstractNode curNode = (AbstractNode )e.next();
			if(curNode instanceof TopologicalNode)
			{
				if(showNodes)
				{
					getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
				}
			}
			else
				getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
		}
/*
		e = getMapView().getMarkers().iterator();
		while (e.hasNext())
		{
			MapMarker marker = (MapMarker)e.next();
			marker.paint(pg, visibleBounds);
		}
*/
	}

	/**
	 * ���������� ��������� �����.
	 * 		- ������������� ������ ��������,
	 * 		- ����� ���������� �����
	 *      - ������������� ���������������
	 * @param g ����������� ��������
	 */
	public void drawTempLines(Graphics g)
	{
		Graphics2D p = ( Graphics2D )g;
		int startX = getStartPoint().x;
		int startY = getStartPoint().y;
		int endX = getEndPoint().x;
		int endY = getEndPoint().y;
		
		p.setStroke(new BasicStroke(3));

		switch (this.mapState.getActionMode())
		{
			case MapState.SELECT_MARKER_ACTION_MODE:
				p.setColor( Color.BLUE);
				p.drawRect(
						Math.min(startX, endX),
						Math.min(startY, endY),
						Math.abs(endX - startX),
						Math.abs(endY - startY));
				break;
			case MapState.DRAW_LINES_ACTION_MODE:
				p.setColor( Color.RED);
				p.drawLine( startX, startY, endX, endY);
				break;
			default:
				break;
		}

		//�� �������� �������������
		if (this.mapState.getOperationMode() == MapState.ZOOM_TO_RECT )
		{
			p.setColor(Color.YELLOW);
			p.drawRect(
					Math.min(startX, endX),
					Math.min(startY, endY),
					Math.abs(endX - startX),
					Math.abs(endY - startY));
		}
		else
		if (this.mapState.getOperationMode() == MapState.MEASURE_DISTANCE )
		{
			p.setColor(Color.GREEN);
			p.drawLine(startX, startY, endX, endY);
		}
	}

	/**
	 * ���������� ���������� ��������.
	 * @param g ����������� ��������
	 */
	public void drawSelection(Graphics g)
		throws MapConnectionException, MapDataException
	{
		Rectangle2D.Double visibleBounds = this.getVisibleBounds();

		Iterator e = getMapView().getMap().getSelectedElements().iterator();
		while (e.hasNext())
		{
			MapElement el = (MapElement)e.next();
			getMapViewController().getController(el).paint(el, g, visibleBounds);
		}
	}

	/**
	 * ��������� �������.
	 * @param ae �������
	 */
	public void operationPerformed(OperationEvent ae)
	{
		if(!this.performProcessing)
			return;

		try
		{
			if(ae.getActionCommand().equals(MapEvent.NEED_FULL_REPAINT))
			{
				repaint(true);
			}
			else
			if(ae.getActionCommand().equals(MapEvent.NEED_REPAINT))
			{
				repaint(false);
			}
			else
			if(ae.getActionCommand().equals(MapEvent.DESELECT_ALL))
			{
				this.mapView.deselectAll();
			}
			else
			if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED))
			{
//			getMapView().setChanged(true);
			}
			else
			if(ae.getActionCommand().equals(MapEvent.MAP_CHANGED))
			{
				Set selectedElements = getMapView().getMap().getSelectedElements();
				if(selectedElements.size() > 1)
				{
					Selection sel;
					if(! (getCurrentMapElement() instanceof Selection))
					{
						sel = new Selection(this.getMapView().getMap());
						setCurrentMapElement(sel);
					}
					else
						sel = (Selection)getCurrentMapElement();

					sel.clear();
					sel.addAll(selectedElements);
					this.sendMapEvent(new MapEvent(sel, MapEvent.MAP_ELEMENT_SELECTED));
				}
				else
				if(selectedElements.size() == 1)
				{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
						MapElement me = (MapElement)selectedElements.iterator().next();
						setCurrentMapElement(me);
						this.sendMapEvent(new MapEvent(me, MapEvent.MAP_ELEMENT_SELECTED));
//				}
				}
				else
				//selectedElements.size() == 0
				{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
						setCurrentMapElement(com.syrus.AMFICOM.mapview.VoidElement.getInstance(getMapView()));
						this.sendMapEvent(new MapEvent(getCurrentMapElement(), MapEvent.MAP_ELEMENT_SELECTED));
//				}
				}
				updateZoom();
				repaint(false);
			}
			else
			if(ae.getActionCommand().equals(MapEvent.MAP_ELEMENT_CHANGED))
			{
				Object me = ae.getSource();
				if(me instanceof SchemeElement)
				{
					getMapViewController().scanElement((SchemeElement )me);
					getMapViewController().scanCables(((SchemeElement )me).scheme());
				}
				else
				if(me instanceof SchemeCableLink)
				{
					getMapViewController().scanCable((SchemeCableLink )me);
					getMapViewController().scanPaths(((SchemeCableLink )me).scheme());
				}
				else
				if(me instanceof CablePath)
				{
					getMapViewController().scanCable(((CablePath)me).getSchemeCableLink());
					getMapViewController().scanPaths(((CablePath)me).getSchemeCableLink().scheme());
				}
				else
				if(me instanceof SiteNode)
				{
					SiteNode site = (SiteNode)me;
					SiteNodeController snc = (SiteNodeController)getMapViewController().getController(site);
					snc.updateScaleCoefficient(site);
				}

				repaint(false);
			}
			else
			if(ae.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
			{
				MapNavigateEvent mne = (MapNavigateEvent )ae;

				//����� ������������ ������� �� �������� � ���������� ��������
				if(mne.isDataMarkerCreated())
				{
					MeasurementPath path;
					try
					{
						path = getMapViewController().getMeasurementPathByMonitoredElementId(mne.getMeId());
					}
					catch (ApplicationException e)
					{
						e.printStackTrace();
						return;
					}

					if(path != null)
					{
						Marker marker = new Marker(
							mne.getMarkerId(),
							getUserId(),
			                getMapView(),
							mne.getDistance(),
							path,
							mne.getMeId(),
							LangModelMap.getString("Marker"));
						this.mapView.addMarker(marker);

						MarkerController mc = (MarkerController)getMapViewController().getController(marker);
						mc.moveToFromStartLo(marker, mne.getDistance());
					}
				}
				else
				if(mne.isDataEventMarkerCreated())
				{
					MeasurementPath path;
					try
					{
						path = getMapViewController().getMeasurementPathByMonitoredElementId(mne.getMeId());
					}
					catch (ApplicationException e)
					{
						e.printStackTrace();
						return;
					}

					if(path != null)
					{
						EventMarker marker = new EventMarker(
							mne.getMarkerId(),
							getUserId(),
			                getMapView(),
							mne.getDistance(),
							path,
							mne.getMeId(),
							LangModelMap.getString("Event"));
						this.mapView.addMarker(marker);

						MarkerController mc = (MarkerController)getMapViewController().getController(marker);

						mc.moveToFromStartLo(marker, mne.getDistance());
					}
				}
				else
				if(mne.isDataAlarmMarkerCreated())
				{
					MeasurementPath path;
					try
					{
						path = getMapViewController().getMeasurementPathByMonitoredElementId(mne.getMeId());
					}
					catch (ApplicationException e)
					{
						e.printStackTrace();
						return;
					}

					AlarmMarker marker = null;
					if(path != null)
					{
						for(Iterator it = this.mapView.getMarkers().iterator(); it.hasNext();)
						{
							try
							{
								marker = (AlarmMarker)it.next();
								if(marker.getMeasurementPath().equals(path))
									break;
								marker = null;
							}
							catch(Exception ex)
							{
								ex.printStackTrace();
							}
						}
						if(marker == null)
						{
							marker = new AlarmMarker(
								mne.getMarkerId(),
								getUserId(),
								getMapView(),
								mne.getDistance(),
								path,
								mne.getMeId(),
								LangModelMap.getString("Alarm"));
							this.mapView.addMarker(marker);
						}
						else
						{
							marker.setId(mne.getMarkerId());
						}

						MarkerController mc = (MarkerController)getMapViewController().getController(marker);

						mc.moveToFromStartLo(marker, mne.getDistance());
					}
/*
					boolean found = false;

					MapPhysicalLinkElement link = 
					getMapView().findCablePath(mne.getSchemePathElementId());
					if(link != null)
					{
						link.setAlarmState(true);
						link.select();
					}
					else
					{
						MapSiteNodeElement node = findMapElementByCableLink(mne.linkID);
						if(node != null)
						{
							node.setAlarmState(true);
							node.select();
						}
					}
*/
				}
				else
				if(mne.isDataMarkerMoved())
				{
					Marker marker = this.mapView.getMarker(mne.getMarkerId());
					if(marker != null)
					{
						if(marker.getPathDecompositor() == null)
							marker.setPathDecompositor((PathDecomposer )mne.getSchemePathDecompositor());

						MarkerController mc = (MarkerController)getMapViewController().getController(marker);

						mc.moveToFromStartLo(marker, mne.getDistance());
					}
				}
				else
				if(mne.isDataMarkerSelected())
				{
					Marker marker = this.mapView.getMarker(mne.getMarkerId());
					if(marker != null)
						marker.setSelected(true);
				}
				else
				if(mne.isDataMarkerDeselected())
				{
					Marker marker = this.mapView.getMarker(mne.getMarkerId());
					if(marker != null)
						marker.setSelected(false);
				}
				else
				if(mne.isDataMarkerDeleted())
				{
					Marker marker = this.mapView.getMarker(mne.getMarkerId());
					if(marker != null)
						this.mapView.removeMarker(marker);
					if(marker instanceof AlarmMarker)
					{
/*
						AlarmMarker amarker = (AlarmMarker)marker;
						MapPhysicalLinkElement link = findMapLinkByCableLink(marker.link_id);
						if(link != null)
						{
							link.setAlarmState(false);
							link.deselect();
						}
						else
						{
							MapSiteNodeElement node = findMapElementByCableLink(marker.link_id);
							if(node != null)
							{
								node.setAlarmState(false);
								node.deselect();
							}
						}
*/
					}
				}
				else
				if(mne.isMapElementSelected())
				{
					if(this.performProcessing)
					{
						MapElement me = (MapElement)mne.getSource();
						if(me != null)
							me.setSelected(true);
					}
				}
				else
				if(mne.isMapElementDeselected())
				{
					if(this.performProcessing)
					{
						MapElement me = (MapElement)mne.getSource();
						if(me != null)
							me.setSelected(false);
					}
				}

				repaint(false);
			}
			else
			if(ae.getActionCommand().equals(TreeDataSelectionEvent.type))
			{
				TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)ae;

				List data = tdse.getList();
				int n = tdse.getSelectionNumber();

				if (n != -1)
				{
					try 
					{
						MapElement me = (MapElement)data.get(n);
						me.setSelected(true);
						repaint(false);
					} 
					catch (Exception ex) 
					{
						ex.printStackTrace();
					} 
				}
			}
			else
			if(ae.getActionCommand().equals(TreeListSelectionEvent.typ))
			{
				if(ae.getSource() instanceof MapElement)
				{
					MapElement me = (MapElement)ae.getSource();
					me.setSelected(true);
					repaint(false);
				} 
			}
			else
			if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
			{
				if(this.performProcessing)
				{
				SchemeNavigateEvent sne = (SchemeNavigateEvent )ae;
					if(sne.SCHEME_ELEMENT_SELECTED)
					{
						SchemeElement[] ses = (SchemeElement[] )sne.getSource();

						for(int i = 0; i < ses.length; i++)
						{
							SiteNode site = this.mapView.findElement(ses[i]);
							if(site != null)
								site.setSelected(true);
						}
					}
					else
					if(sne.SCHEME_PATH_SELECTED)
					{
						SchemePath[] sps = (SchemePath[] )sne.getSource();
						
						for(int i = 0; i < sps.length; i++)
						{
							MeasurementPath mmp = this.mapView.findMeasurementPath(sps[i]);
							if(mmp != null)
								mmp.setSelected(true);
						}
					}
					else
					if(sne.SCHEME_CABLE_LINK_SELECTED)
					{
						SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
						for(int i = 0; i < scs.length; i++)
						{
							CablePath mcp = this.mapView.findCablePath(scs[i]);
							if(mcp != null)
								mcp.setSelected(true);
						}
					}
					else
					if(sne.SCHEME_ELEMENT_DESELECTED)
					{
						SchemeElement[] ses = (SchemeElement[] )sne.getSource();

						for(int i = 0; i < ses.length; i++)
						{
							SiteNode site = this.mapView.findElement(ses[i]);
							if(site != null)
								site.setSelected(false);
						}
					}

					if(sne.SCHEME_PATH_DESELECTED)
					{
						SchemePath[] sps = (SchemePath[] )sne.getSource();

						for(int i = 0; i < sps.length; i++)
						{
							MeasurementPath mmp = this.mapView.findMeasurementPath(sps[i]);
							if(mmp != null)
								mmp.setSelected(false);
						}
					}

					if(sne.SCHEME_CABLE_LINK_DESELECTED)
					{
						SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
						for(int i = 0; i < scs.length; i++)
						{
							CablePath mcp = this.mapView.findCablePath(scs[i]);
							if(mcp != null)
								mcp.setSelected(false);
						}
					}

					repaint(false);
				}
			}
		}
		catch(MapConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(MapDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��������� ������� �����. ����� �� ��������� ������� ���� ������, 
	 * ������������ ���� {@link #performProcessing}
	 * @param me ������� �����
	 */
	public void sendMapEvent(MapEvent me)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			this.performProcessing = false;
			getContext().getDispatcher().notify(me);
			this.performProcessing = true;
		}
	}

	/**
	 * ��������� �������� � ������� �������� �����.
	 * @param mapElement ��������� ������� �����
	 */
	public void notifyMapEvent(MapElement mapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "notifyMapEvent(" + mapElement + ")");

		if(this.doNotify)
		{
			Dispatcher disp = getContext().getDispatcher();
			if(disp != null)
			{
				this.performProcessing = false;
				disp.notify(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				this.performProcessing = true;
			}
		}
	}

	/**
	 * ��������� �������� � ������� �������� �����.
	 * @param mapElement ��������� ������� �����
	 */
	public void notifySchemeEvent(MapElement mapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "notifySchemeEvent(" + mapElement + ")");
/*		
		SchemeNavigateEvent sne;
		Dispatcher dispatcher = logicalNetLayer.mapMainFrame.this.aContext.getDispatcher();
		try 
		{
			MapSiteNodeElement mapel = (MapSiteNodeElement )mapElement;

			if(mapel.element_id != null && !mapel.element_id.equals(""))
			{
				SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mapel.element_id);
//				System.out.println("notify SCHEME_ELEMENT_SELECTED_EVENT " + se.getId());
				this.logicalNetLayer.performProcessing = false;
				sne = new SchemeNavigateEvent(
						new SchemeElement[] { se },
						SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.performProcessing = true;
				return;
			}
		} 
		catch (Exception ex){ }

		try
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )mapElement;

			if(link.LINK_ID != null && !link.LINK_ID.equals(""))
			{
				SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
//				System.out.println("notify SCHEME_CABLE_LINK_SELECTED_EVENT " + scl.getId());
				this.logicalNetLayer.performProcessing = false;
				sne = new SchemeNavigateEvent(
						new SchemeCableLink[] { scl },
						SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.performProcessing = true;
				return;
			}
		} 
		catch (Exception ex){ }

		try 
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )mapElement;

			if(path.PATH_ID != null && !path.PATH_ID.equals(""))
			{
				SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
//				System.out.println("notify SCHEME_PATH_SELECTED_EVENT " + sp.getId());
				this.logicalNetLayer.performProcessing = false;
				sne = new SchemeNavigateEvent(
						new SchemePath[] { sp },
						SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.performProcessing = true;
				return;
			}
		} 
		catch (Exception ex){ } 
*/
	}

	/**
	 * �������� ������� ������������� �������.
	 * ������������ � ������ {@link MapState#MOVE_FIXDIST}.
	 * @return ������� ������������� �������
	 */
	public AbstractNode getFixedNode()
	{
		return this.fixedNode;
	}

	/**
	 * �������� ������ �����, �������� (����� ���������) � ������������� �����.
	 * ������������ � ������ {@link MapState#MOVE_FIXDIST}.
	 * @return ������ ���������. 
	 * 
	 */
	public List getFixedNodeList()
	{
		return this.fixedNodeList;
	}

	/**
	 * �������� ������� ��������� �������.
	 * @return ������� ��������� �������
	 */
	public MapElement getCurrentMapElement()
	{
		return this.currentMapElement;
	}

	/**
	 * ���������� ������� ��������� �������.
	 * @param curMapElement ������� ��������� �������
	 */
	public void setCurrentMapElement(MapElement curMapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentMapElement(" + curMapElement + ")");
		
		this.currentMapElement = curMapElement;

		if(getMapState().getOperationMode() == MapState.NO_OPERATION)
		{
			boolean canFixDist = (curMapElement instanceof TopologicalNode)
				|| (curMapElement instanceof SiteNode);
			if(getContext().getApplicationModel().isEnabled(
				MapApplicationModel.OPERATION_MOVE_FIXED) != canFixDist)
			{
				if(canFixDist)
				{
					this.fixedNode = (AbstractNode )curMapElement;
					this.fixedNodeList.clear();
					for(Iterator it = this.fixedNode.getNodeLinks().iterator(); it.hasNext();)
					{
						NodeLink mnle = (NodeLink)it.next();
						this.fixedNodeList.add(mnle.getOtherNode(this.fixedNode));
					}
				}
				getContext().getApplicationModel().setEnabled(
					MapApplicationModel.OPERATION_MOVE_FIXED,
					canFixDist);
				getContext().getApplicationModel().fireModelChanged();
			}
		}

		if(curMapElement instanceof VoidElement)
			return;
		curMapElement.setSelected(true);
		notifyMapEvent(this.currentMapElement);
		notifySchemeEvent(this.currentMapElement);
	}

	/**
	 * �������� ������� ������� �� �������� ���������� �� �����.
	 * @param point �������� ����������
	 * @return ������� � �����
	 */
	public MapElement getMapElementAtPoint(Point point)
		throws MapConnectionException, MapDataException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapElementAtPoint(" + point + ")");
		
		int showMode = getMapState().getShowMode();
		MapElement curME = VoidElement.getInstance(this.getMapView());

		Rectangle2D.Double visibleBounds = this.getVisibleBounds();
		
		//����� ����������� �� ���� ��������� � ���� �� �����-������ �� ��� ������
		//�� ������������� ��� ������� ���������
		Iterator e = this.mapView.getAllElements().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			MapElementController controller = getMapViewController().getController(mapElement);
			if(controller.isElementVisible(mapElement, visibleBounds))
			if ( controller.isMouseOnElement(mapElement, point))
			{
				curME = mapElement;
			
				if ( mapElement instanceof NodeLink)
				{
					//����� ������� �� ����� linkState ��� ������
					if ( showMode == MapState.SHOW_NODE_LINK)
					{// curME �������� NodeLink
					}
					else
					if ( showMode == MapState.SHOW_PHYSICAL_LINK)
					{
						curME = ((NodeLink)mapElement).getPhysicalLink();
					}
					else
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = this.mapView.getCablePaths((NodeLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (CablePath)it.next();
						}
						else
						{
							curME = ((NodeLink)mapElement).getPhysicalLink();
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = this.mapView.getMeasurementPaths((NodeLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MeasurementPath)it.next();
						}
						else
						{
							curME = ((NodeLink)mapElement).getPhysicalLink();
						}
					}
				}
				else
				if ( mapElement instanceof PhysicalLink)
				{
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = this.mapView.getCablePaths((PhysicalLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (CablePath)it.next();
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = this.mapView.getMeasurementPaths((PhysicalLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MeasurementPath)it.next();
						}
					}
				}
				break;
			}
		}
		return curME;
	}

	/**
	 * �������� ����� ���� ���������.
	 */
	public void deselectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");

		Iterator e = this.mapView.getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			mapElement.setSelected(false);
		}
		getMapView().getMap().clearSelection();
	}

	/**
	 * ������� ��� ��������.
	 */
	public void selectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "selectAll()");
		
		Iterator e = this.mapView.getAllElements().iterator();

		while(e.hasNext())
		{
			MapElement curElement = (MapElement)e.next();
			curElement.setSelected(true);
		}
	}

	/**
	 * �������� ������ ��������� ���������.
	 * @return ������ ���������
	 */	
	public Set getSelectedElements()
	{
		return getMapView().getMap().getSelectedElements();
	}

	/**
	 * �������� ����, ��� ���� �� ���� ������� ������.
	 * @return <code>true</code> ���� ���� ��������� �������
	 */
	public boolean isSelectionEmpty()
	{
//		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "isSelectionEmpty()");
//		
//		Iterator e = getMapView().getAllElements().iterator();
//
//		while (e.hasNext())
//		{
//			MapElement curElement = (MapElement)e.next();
//			if (curElement.isSelected())
//			{
//				return false;
//			}
//		}
//		return true;
		return getMapView().getMap().getSelectedElements().isEmpty();
	}

	/**
	 * �������� �������� �����, ��� �������� ����� �������������
	 * �������������� �����, �� ���������� �� �����.
	 * @param point �������� ����������
	 * @return �������� �����, ��� <code>null</code>, ���� ��������� � ������
	 * ����� ���
	 */
	public NodeLink getEditedNodeLink(Point point)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getEditedNodeLink(" + point + ")");
		
		NodeLinkController nlc = null;
		
		Iterator e = getMapView().getMap().getNodeLinks().iterator();
		while (e.hasNext())
		{
			NodeLink mapElement = (NodeLink)e.next();
			if(nlc == null)
				nlc = (NodeLinkController)getMapViewController().getController(mapElement);
			if (nlc.isMouseOnThisObjectsLabel(mapElement, point))
			{
				return mapElement;
			}
		}
		return null;
	}
	
	/**
	 * ���������� ����� ����� ��������� ����� - ���������� �� ���� node ��
	 * ������� ����.
	 * @param nodelink �������� �����
	 * @param node ����, �� �������� ������������� ����������
	 * @param dist ����������
	 */
	public void setNodeLinkSizeFrom(NodeLink nodelink, AbstractNode node, double dist)
		throws MapConnectionException, MapDataException
	{
		DoublePoint anchor1 = node.getLocation();
		
		MoveNodeCommand cmd = new MoveNodeCommand(node);
		cmd.setLogicalNetLayer(this);
		
		((NodeLinkController)getMapViewController().getController(nodelink))
			.setSizeFrom(nodelink, node, dist);

		DoublePoint anchor2 = node.getLocation();
		cmd.setParameter(
				MoveSelectionCommandBundle.DELTA_X, 
				String.valueOf(anchor2.getX() - anchor1.getX()));
		cmd.setParameter(
				MoveSelectionCommandBundle.DELTA_Y, 
				String.valueOf(anchor2.getY() - anchor1.getY()));
		getCommandList().add(cmd);
		getCommandList().execute();

		getMapState().setOperationMode(MapState.NO_OPERATION);
	}

	/**
	 * ��������� �������� ��������� ���������.
	 */
	public void delete()
		throws MapConnectionException, MapDataException
	{
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(this);
		this.commandList.add(command);
		this.commandList.execute();
		repaint(false);
	}

	/**
	 * �������� ��������� ����������� ������� ������������.
	 */
	public void undo()
		throws MapConnectionException, MapDataException
	{
		this.commandList.undo();
		repaint(false);
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
	/**
	 * ��������� ���������� ������� �������������.
	 */
	public void redo()
		throws MapConnectionException, MapDataException
	{
		this.commandList.redo();
		repaint(false);
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
	/**
	 * �������� ��������� ��� ��� �������� ����� �����.
	 * @return ��� �����
	 */
	public PhysicalLinkType getPen()
	{
		if(this.currentPen == null)
		{
			this.currentPen = LinkTypeController.getDefaultPen(getUserId());
		}
		return this.currentPen;
	}
	
	/**
	 * �������� ��� �������������� ����.
	 * @return ��� ����
	 */
	public SiteNodeType getUnboundProto()
	{
		if(this.unboundProto == null)
		{
			this.unboundProto = NodeTypeController.getDefaultUnboundProto(getUserId());
		}
		return this.unboundProto;
	}
	
	/**
	 * �������� ��� �������������� ������.
	 * @return ��� �������������� ������
	 */
	public PhysicalLinkType getUnboundPen()
	{
		if(this.unboundLinkProto == null)
		{

			this.unboundLinkProto = LinkTypeController.getDefaultUnboundPen(getUserId());
		}
		return this.unboundLinkProto;
	}
	
	/**
	 * ������� ��� ����� ��� �������� ����� �����.
	 * @param pen ��� �����
	 */
	public void setPen(PhysicalLinkType pen)
	{
		this.currentPen = pen;
	}

	/**
	 * �������� ���������� ����.
	 * @return ���������� ����
	 */
	public MapViewController getMapViewController()
	{
		return com.syrus.AMFICOM.Client.Map.Controllers.MapViewController.getInstance(this);
	}

	/**
	 * �������� ������������� ������������, �� ����� ����� ���������
	 * ����� �������.
	 * @return ������������� ������������
	 */
	public Identifier getUserId()
	{
		return this.userId;
	}
}
