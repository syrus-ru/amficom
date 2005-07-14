/**
 * $Id: LogicalNetLayer.java,v 1.98 2005/07/14 15:45:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.command.action.MoveNodeCommand;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.AbstractNodeController;
import com.syrus.AMFICOM.client.map.controllers.AlarmMarkerController;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.EventMarkerController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapElementController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.MeasurementPathController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.CommandList;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
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
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.util.Log;

/**
 * ��������� ������������ ���������� ��������� ����.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.98 $, $Date: 2005/07/14 15:45:55 $
 * @module mapviewclient_v2
 */
public class LogicalNetLayer
{
	protected CommandList commandList = new CommandList(20);
	
	/** ��������� ������������ ��� ������ ����������. */
	protected MapState mapState = new MapState();
	
	/** ���������� �����. */
	protected MapView mapView = null;
	
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
	protected PhysicalLinkType currentPhysicalLinkType = null;

	/** ��� �������������� �������� ��������. */
	protected SiteNodeType unboundNodeType = null;

	/** ��� �������������� ������. */
	protected PhysicalLinkType unboundLinkType = null;
	
	/** ������� ����� ������� ���� �� ����� (� �������� �����������). */	
	protected Point currentPoint = new Point(0, 0);

	/** �������� ����������. */
	protected ApplicationContext aContext = null;

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
	 * �������� ���� �� �������� ������ ����� � ����� �� �������� ������
	 */
	public static final double MOVE_CENTER_STEP_SIZE = 0.33;

	private final MapCoordinatesConverter converter;

	private final MapContext mapContext;
	
	private MapViewController mapViewController;
	
	public LogicalNetLayer(ApplicationContext aContext, MapCoordinatesConverter converter, MapContext mapContext) throws ApplicationException {
		this.aContext = aContext;
		this.converter = converter;
		this.mapContext = mapContext;

		Identifier userId = LoginManager.getUserId();

		LinkTypeController.createDefaults(userId);
		NodeTypeController.createDefaults(userId);

		AlarmMarkerController.init(userId);
		TopologicalNodeController.init(userId);
		EventMarkerController.init(userId);
		MarkController.init(userId);
		MarkerController.init(userId);
	}
	
	/**
	 * ��� ��������� �������� ����������� ����� ���������� ��������
	 * ������� ����������� ���� �������� �� �����.
	 * @throws MapConnectionException 
	 * @throws MapDataException 
	 */
	public void updateZoom() throws MapDataException, MapConnectionException
	{
		Log.debugMessage(getClass().getName() + "::" + "updateZoom()" + " | " + "method call", Level.FINER);
		
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
		if(MapPropertiesManager.isOptimizeLinks())
//			calculateVisualLinks();
			calculateVisualElements();
	}

	/**
	 * �������� �������� ����������.
	 * @return �������� ����������
	 */
	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	/**
	 * ���������������� ������������ �������� ����� �����.
	 * @param mapView ���
	 */
	public void setMapView(MapView mapView)
		throws MapConnectionException, MapDataException
	{
		Log.debugMessage(getClass().getName() + "::" + "setMapView(" + mapView + ")" + " | " + "method call", Level.FINER);

		if(	getContext() != null
			&& getContext().getDispatcher() != null)
		{
				if(mapView != null
					&& mapView.getMap() != null)
				{
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(mapView, MapEvent.MAP_VIEW_SELECTED));
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(mapView.getMap(), MapEvent.MAP_SELECTED));
				}
				else
				{
					this.aContext.getDispatcher().firePropertyChange(
							new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_DESELECTED));
				}
		}

		this.mapView = mapView;
		this.linksForNodes.clear();

		if(mapView == null)
		{
			System.out.println("mapView null!");
		}

		getMapViewController().setMapView(this.mapView);

		//����������� ������� ������� Void
		this.currentMapElement = VoidElement.getInstance(this.mapView);

		this.commandList.flush();
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
	 * @throws MapConnectionException 
	 * @throws MapDataException 
	 */
	public void setDefaultScale(double defaultScale) throws MapDataException, MapConnectionException
	{
		Log.debugMessage(getClass().getName() + "::" + "setDefaultScale(" + defaultScale + ")" + " | " + "method call", Level.FINER);
		
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
		Log.debugMessage(getClass().getName() + "::" + "setMapState(" + state + ")" + " | " + "method call", Level.FINER);
		
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
	 * ���������� ��� �������� ����������� ���� �������������� �����.
	 * @param g ����������� ��������
	 */
	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		Graphics2D p = (Graphics2D )g;
		
		// remember settings from graphics
		Color color = p.getColor();
		Stroke stroke = p.getStroke();
		Font font = p.getFont();
		Color background = p.getBackground();

		long f;
		long d;

		Log.debugMessage("\n\n------------------ LogicalNetLayer.paint() called ----------------------", 
				Level.INFO);
		try {
			throw new Exception("stacktrace");
		}
		catch (Exception e) {
//			Log.debugException(e, Log.FINE);
//			e.printStackTrace();
		}
//		System.out.println("--------------------------------------");
		f = System.currentTimeMillis();
		if(MapPropertiesManager.isOptimizeLinks()) {
//			drawVisualLinks(p, visibleBounds);
			drawVisualElements(p, visibleBounds);
		}
		else {
			drawLines(p, visibleBounds);
			drawNodes(p, visibleBounds);
		}
		drawSelection(p, visibleBounds);
		drawTempLines(p, visibleBounds);
		d = System.currentTimeMillis();
		Log.debugMessage("\n--------------------- LogicalNetLayer.paint() finished in " + String.valueOf(d - f) + " ms -----------------\n", 
				Level.INFO);
//		System.out.println("--------------------------------------");

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
	public void drawLines(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		long f = System.currentTimeMillis();

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();

		this.elementsToDisplay.clear();
		
		//���� ����� ������ nodeLink �� ��������, �� ������� ����� ������ physicalLink
		if (! this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK))
		{
			Command modeCommand = getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		if (getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH)
		{
			this.elementsToDisplay.addAll(getMapView().getMap().getAllPhysicalLinks());
			for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();)
			{
				MeasurementPath measurementPath = 
					(MeasurementPath)it.next();

//				if(getMapViewController().getController(measurementPath).isElementVisible(measurementPath, visibleBounds))
						getMapViewController().getController(measurementPath).paint(measurementPath, g, visibleBounds);
				
				// to avoid multiple cicling through scheme elements
				// use once sorted cable links
				for(Iterator it2 = measurementPath.getSortedCablePaths().iterator(); it2.hasNext();)
				{
					CablePath cablePath = 
						(CablePath)it2.next();
					this.elementsToDisplay.removeAll(cablePath.getLinks());
				}
			}
			for(Iterator it = this.elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink physicalLink = 
					(PhysicalLink)it.next();
//				if(getMapViewController().getController(physicalLink).isElementVisible(physicalLink, visibleBounds))
					getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			this.elementsToDisplay.addAll(getMapView().getMap().getAllPhysicalLinks());
			for(Iterator it = this.mapView.getCablePaths().iterator(); it.hasNext();)
			{
				CablePath cablePath = 
					(CablePath)it.next();
//				if(getMapViewController().getController(cablePath).isElementVisible(cablePath, visibleBounds))
					getMapViewController().getController(cablePath).paint(cablePath, g, visibleBounds);
				this.elementsToDisplay.removeAll(cablePath.getLinks());
			}
			for(Iterator it = this.elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink physicalLink = 
					(PhysicalLink)it.next();
//				if(getMapViewController().getController(physicalLink).isElementVisible(physicalLink, visibleBounds))
					getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			Iterator iterator = getMapView().getMap().getAllPhysicalLinks().iterator();
			while (iterator.hasNext())
			{
				PhysicalLink physicalLink = 
					(PhysicalLink)iterator.next();
//				if(getMapViewController().getController(physicalLink).isElementVisible(physicalLink, visibleBounds))
					getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			Iterator iterator = getMapView().getMap().getAllNodeLinks().iterator();
			while (iterator.hasNext())
			{
				NodeLink nodeLink = (NodeLink)iterator.next();
//				if(getMapViewController().getController(nodeLink).isElementVisible(nodeLink, visibleBounds))
					getMapViewController().getController(nodeLink).paint(nodeLink, g, visibleBounds);
			}
		}
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawLines | " + String.valueOf(d - f) + " ms\n" 
				+ "		" + String.valueOf(MapViewController.getTime1()) + " ms (isElementVisible)\n"
				+ "		" + String.valueOf(MapViewController.getTime2()) + " ms (getStroke)\n"
				+ "		" + String.valueOf(MapViewController.getTime3()) + " ms (getColor)\n"
				+ "		" + String.valueOf(MapViewController.getTime4()) + " ms (paint)\n"
				+ "		" + String.valueOf(MapViewController.getTime5()) + " ms (painting labels)",
				Level.INFO);
	}

	/**
	 * ���������� ����.
	 * @param g ����������� ��������
	 */
	public void drawNodes(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		long f = System.currentTimeMillis();
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
		e = getMapView().getMap().getExternalNodes().iterator();
		while (e.hasNext())
		{
			AbstractNode curNode = (AbstractNode )e.next();
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
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawNodes | " + String.valueOf(d - f) + " ms", 
				Level.INFO);
	}

	/**
	 * ���������� ��������� �����.
	 * 		- ������������� ������ ��������,
	 * 		- ����� ���������� �����
	 *      - ������������� ���������������
	 * @param g ����������� ��������
	 */
	public void drawTempLines(Graphics g, Rectangle2D.Double visibleBounds)
	{
		long f = System.currentTimeMillis();
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
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawTempLines | " + String.valueOf(d - f) + " ms", 
				Level.INFO);
	}

	/**
	 * ���������� ���������� ��������.
	 * @param g ����������� ��������
	 */
	public void drawSelection(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		long f = System.currentTimeMillis();
		Iterator e = getMapView().getMap().getSelectedElements().iterator();
		while (e.hasNext())
		{
			MapElement el = (MapElement)e.next();
			getMapViewController().getController(el).paint(el, g, visibleBounds);
		}
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawSelection | " + String.valueOf(d - f) + " ms", 
				Level.INFO);
	}

	/**
	 * ��������� ������� �����.
	 */
	public void sendScaleEvent(Double scale)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_SCALE_CHANGED, scale));
		}
	}

	/**
	 * ��������� ������� �����.
	 */
	public void sendMapEvent(String eventString)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			getContext().getDispatcher().firePropertyChange(new MapEvent(this, eventString));
		}
	}

	/**
	 * ��������� �������� � ������� �������� �����.
	 * @param selectedElement ��������� ������� �����
	 */
	public void sendMapSelectedEvent(MapElement selectedElement)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			getContext().getDispatcher().firePropertyChange(new MapNavigateEvent(this, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT, selectedElement));
		}
	}

	/**
	 * ��������� �������� � ���������� �������� �����.
	 * @param deselectedElement ������������ ������� �����
	 */
	public void sendMapDeselectedEvent(MapElement deselectedElement)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			getContext().getDispatcher().firePropertyChange(new MapNavigateEvent(this, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT, deselectedElement));
		}
	}

	/**
	 * ��������� �������� � ������� �������� �����.
	 * @param mapElement ��������� ������� �����
	 */
	public void notifySchemeEvent(MapElement mapElement)
	{
		Log.debugMessage(getClass().getName() + "::" + "notifySchemeEvent(" + mapElement + ")" + " | " + "method call", Level.FINER);
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
		Log.debugMessage(getClass().getName() + "::" + "setCurrentMapElement(" + curMapElement + ")" + " | " + "method call", Level.FINER);
		
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
					for(Iterator it = this.mapView.getMap().getNodeLinks(this.fixedNode).iterator(); it.hasNext();)
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
		if(! (curMapElement instanceof Selection))
			this.mapView.getMap().setSelected(curMapElement, true);
		sendMapSelectedEvent(this.currentMapElement);
		notifySchemeEvent(this.currentMapElement);
	}

	/**
	 * �������� ������� ������� �� �������� ���������� �� �����.
	 * @param point �������� ����������
	 * @return ������� � �����
	 */
	public MapElement getMapElementAtPoint(Point point, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		Log.debugMessage(getClass().getName() + "::" + "getMapElementAtPoint(" + point + ")" + " | " + "method call", Level.FINER);
		
		int showMode = getMapState().getShowMode();
		MapElement curME = VoidElement.getInstance(this.getMapView());

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
		Log.debugMessage(getClass().getName() + "::" + "deselectAll()" + " | " + "method call", Level.FINER);

		Collection selectedElements = new LinkedList();
		selectedElements.addAll(this.mapView.getMap().getSelectedElements());
		Iterator it = selectedElements.iterator();
		while ( it.hasNext())
		{
			MapElement mapElement = (MapElement )it.next();
			mapElement.setSelected(false);
			
			sendMapDeselectedEvent(mapElement);			
		}
		this.mapView.getMap().clearSelection();
	}

	/**
	 * ������� ��� ��������.
	 */
	public void selectAll()
	{
		Log.debugMessage(getClass().getName() + "::" + "selectAll()" + " | " + "method call", Level.FINER);
		
		Iterator e = this.mapView.getAllElements().iterator();
		Map map = this.mapView.getMap();

		while(e.hasNext())
		{
			MapElement curElement = (MapElement)e.next();
			map.setSelected(curElement, true);
		}
	}

	/**
	 * �������� ������ ��������� ���������.
	 * @return ������ ���������
	 */	
	public Set getSelectedElements()
	{
		return this.mapView.getMap().getSelectedElements();
	}

	/**
	 * �������� ����, ��� ���� �� ���� ������� ������.
	 * @return <code>true</code> ���� ���� ��������� �������
	 */
	public boolean isSelectionEmpty()
	{
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
		Log.debugMessage(getClass().getName() + "::" + "getEditedNodeLink(" + point + ")" + " | " + "method call", Level.FINER);
		
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
	 * �������� ��������� ����������� ������� ������������.
	 */
	public void undo()
	{
		this.commandList.undo();
		sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	/**
	 * ��������� ���������� ������� �������������.
	 */
	public void redo()
	{
		this.commandList.redo();
		sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	/**
	 * �������� ��������� ��� ��� �������� ����� �����.
	 * @return ��� �����
	 * @throws ApplicationException 
	 */
	public PhysicalLinkType getCurrentPhysicalLinkType() throws ApplicationException
	{
		if(this.currentPhysicalLinkType == null) {
			this.currentPhysicalLinkType = LinkTypeController.getDefaultPhysicalLinkType();
		}
		return this.currentPhysicalLinkType;
	}
	
	/**
	 * �������� ��� �������������� ����.
	 * @return ��� ����
	 * @throws ApplicationException 
	 */
	public SiteNodeType getUnboundNodeType() throws ApplicationException
	{
		if(this.unboundNodeType == null) {
			this.unboundNodeType = NodeTypeController.getUnboundNodeType();
		}
		return this.unboundNodeType;
	}
	
	/**
	 * �������� ��� �������������� ������.
	 * @return ��� �������������� ������
	 * @throws ApplicationException 
	 */
	public PhysicalLinkType getUnboundLinkType() throws ApplicationException
	{
		if(this.unboundLinkType == null) {
			this.unboundLinkType = LinkTypeController.getUnboundPhysicalLinkType();
		}
		return this.unboundLinkType;
	}
	
	/**
	 * ������� ��� ����� ��� �������� ����� �����.
	 * @param type ��� �����
	 */
	public void setCurrentPhysicalLinkType(PhysicalLinkType type)
	{
		this.currentPhysicalLinkType = type;
	}

	/**
	 * �������� ���������� ����.
	 * @return ���������� ����
	 */
	public MapViewController getMapViewController()
	{
		return this.mapViewController;
	}

	public void setMapViewController(MapViewController mapViewController )
	{
		this.mapViewController = mapViewController ;
	}

	/**
	 * @return Returns the converter.
	 */
	public MapCoordinatesConverter getConverter() {
		return this.converter;
	}
	/**
	 * @return Returns the mapContext.
	 */
	public MapContext getMapContext() {
		return this.mapContext;
	}

	/**
	 * ������, ���������� ��� ����������� ��������� NodeLink'�� 
	 * @author $Author: krupenn $
	 * @version $Revision: 1.98 $, $Date: 2005/07/14 15:45:55 $
	 * @module mapviewclient_v1_modifying
	 */
	private class VisualMapElement
	{
		public AbstractNode startNode = null;
		public AbstractNode endNode = null;
		public Color color = null;
		public Stroke stroke = null;
		public MapElement mapElement = null;

		public VisualMapElement(AbstractNode startNode, AbstractNode endNode)
		{
			this.startNode = startNode;
			this.endNode = endNode;			
		}

		public VisualMapElement(
				AbstractNode startNode, 
				AbstractNode endNode,
				Color color,
				Stroke stroke)
		{
			this.startNode = startNode;
			this.endNode = endNode;
			this.color = color;
			this.stroke = stroke;			
		}

		public VisualMapElement(
				MapElement mapElement,
				Color color,
				Stroke stroke)
		{
			this.mapElement = mapElement;
			this.color = color;
			this.stroke = stroke;			
		}
	}
	
	/**
	 * ������ VisualMapElement'��, ������� ����� ������������ ��� ������� ��������
	 */
	private Set<Object> visualElements = new HashSet<Object>();

	/**
	 * ����� � �������� ������ �������, ����� ��������� ��������� � ���������
	 * � ���� ����� ������������ �������
	 */
	private static final int MINIMUM_SCREEN_LENGTH = 20;
	
	private java.util.Map<AbstractNode,Set<NodeLink>> linksForNodes =
		new HashMap<AbstractNode,Set<NodeLink>>();

	/**
	 * ��������� ������ ��������� ������� ������ ������������ ��� ������� ���������.
	 */
	public void calculateVisualLinks() throws MapDataException,
			MapConnectionException {
		long startTime = System.currentTimeMillis();

		this.visualElements.clear();
		long t1 = System.currentTimeMillis();
		
		this.searchLinksForNodes();
		long t15 = System.currentTimeMillis();


		Map map = this.getMapView().getMap();

		//�������, � ������� ���������� ���������� � ���, ������������� ��
		//��� ���� ��� ���
		java.util.Map<AbstractNode, Boolean> nodesCalculated = 
			new HashMap<AbstractNode, Boolean>(map.getNodes().size());
		for (Iterator nodesIt = map.getNodes().iterator(); nodesIt.hasNext();) {
			AbstractNode node = (AbstractNode) nodesIt.next();
			nodesCalculated.put(node, Boolean.FALSE);
		}

		java.util.Map<NodeLink, Boolean> nodeLinksCalculated = 
			new HashMap<NodeLink, Boolean>(map.getNodeLinks().size());
		for (Iterator nodesIt = map.getNodeLinks().iterator(); nodesIt.hasNext();) {
			NodeLink nodeLink = (NodeLink) nodesIt.next();
			nodeLinksCalculated.put(nodeLink, Boolean.FALSE);
		}

		long t2 = System.currentTimeMillis();

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();

		//��������� ��������. 
		//���� ��������� ����������� ���� ��� ��������� - �������� � ���� �� ���������.
		//���� ����� - ��� ������� ����, �� �������� ����� �������� ���� ���.
		Collection<AbstractNode> frontedge = new LinkedList<AbstractNode>();
		for (Iterator nodesIt = map.getNodes().iterator(); nodesIt.hasNext();) {
			AbstractNode startRecursionNode = (AbstractNode) nodesIt.next();

			while(startRecursionNode != null) {
				if (! nodesCalculated.get(startRecursionNode).booleanValue()) {
					long t3 = System.currentTimeMillis();
					Set<NodeLink> allLinksForStartRecursionNode =
						this.linksForNodes.get(startRecursionNode);
					long t4 = System.currentTimeMillis();
					MapViewController.addTime3(t4 - t3);
					
					if (allLinksForStartRecursionNode != null){
						//���� � ���� �� ���� ��������� �� ������ NodeLinka, �� ��� ���� Set == null
						for (NodeLink outgoingLink : allLinksForStartRecursionNode) {
							if (! nodeLinksCalculated.get(outgoingLink).booleanValue()) {
								nodeLinksCalculated.put(outgoingLink, Boolean.TRUE);
								pullVisualLinksFromNode(
										startRecursionNode, 
										startRecursionNode,
										outgoingLink, 
										nodesCalculated,
										nodeLinksCalculated,
										frontedge,
										map);
							}
						}
					}
				}
				Iterator<AbstractNode> it = frontedge.iterator();
				if(it.hasNext()) {
					startRecursionNode = it.next();
					it.remove();
				}
				else
					startRecursionNode = null;
			}
		}
		long endTime = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.calculateVisualLinks | "
				+ "optimized map for " + (endTime - startTime) + "ms. Got " + this.visualElements.size() + " visual links.\n"
				+ "		" + (t1 - startTime) + " ms (visualElements.clear())\n"
				+ "		" + (t15 - t1) + " ms (searching links for nodes)\n"				
				+ "		" + (t2 - t15) + " ms (fill nodesCalculated)\n"
				+ "		" + (endTime - t2) + " ms (recursing)\n"
				+ "		" + MapViewController.getTime1() + " ms (check nodesCalculated.get(nodeProcessed))\n"
				+ "		" + MapViewController.getTime2() + " ms (create new VisualMapElements)\n"
				+ "		" + MapViewController.getTime3() + " ms (get links for node Processed)\n"
				+ "		" + MapViewController.getTime4() + " ms (calculate distance)\n"
				+ "		" + MapViewController.getTime5() + " (total calls to pullVisualLinksFromNode)\n",
				Level.INFO);
	}

	/**
	 * ����������� ��������� ������ �������� ����� �����, �������� �����������
	 * "�����������" ����.
	 * @param nodeToPullFrom ����, �� �������� ������� ������� VisualMapElement.
	 * @param lastNode ���������� ���� - �� ���� �� ������ � ������� ����
	 * @param incomingLink ����, �� �������� �� ������ � ������ ����. ��� �������
	 * ������ �������� = null.
	 * @param nodesCalculated ������� ���������� ���������� � ���, ��������� �� ��� ��� 
	 * ���� ����.
	 * @param nodeLinksCalculated 
	 * @param frontedge 
	 * @param map ������ "�����"
	 */
	private void pullVisualLinksFromNode(
			AbstractNode nodeToPullFrom,
			AbstractNode lastNode, 
			NodeLink incomingLink,
			java.util.Map<AbstractNode, Boolean> nodesCalculated, 
			java.util.Map<NodeLink, Boolean> nodeLinksCalculated, 
			Collection<AbstractNode> frontedge, 
			Map map) 
			throws MapDataException, MapConnectionException {
		MapViewController.addTime5(1);
		//��������������� ����
		AbstractNode nodeProcessed = null;

		if (incomingLink == null)
			nodeProcessed = lastNode;
		else if (incomingLink.getStartNode().equals(lastNode))
			nodeProcessed = incomingLink.getEndNode();
		else if (incomingLink.getEndNode().equals(lastNode))
			nodeProcessed = incomingLink.getStartNode();

		if (nodeProcessed == null)
			throw new AssertionError(
					"LogicalNetLayer | pullVisualLinksFromNode | The nodeProcessed can't be null");

		long t1 = System.currentTimeMillis();
		if (nodesCalculated.get(nodeProcessed).booleanValue()) {
			long t2 = System.currentTimeMillis();
			MapViewController.addTime1(t2 - t1);
			//������� �������� - ������ ���� ��� ��������������.
			//������ ������� ����������� � �������.
			if (lastNode == nodeToPullFrom)
				//���� ��������� "������������" ������������ ������� ������� �� ������
				//����� - ��� � ����������
				this.visualElements.add(incomingLink);
			else {
				long d = System.currentTimeMillis();
				this.visualElements.add(
						new VisualMapElement(nodeToPullFrom, nodeProcessed));
				long f = System.currentTimeMillis();
				MapViewController.addTime2(f - d);
			}
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			return;
		}
		long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);

		long t3 = System.currentTimeMillis();
		//�������� ������ ���� ��������/��������� ����� ��� ������� ����
		Set<NodeLink> allLinksForNodeProcessed = this.linksForNodes.get(nodeProcessed);
		long t4 = System.currentTimeMillis();
		MapViewController.addTime3(t4 - t3);

		long t5 = System.currentTimeMillis();
		//�������� ���������� ����, �� �������� �� ����� ���������� ������� 
		Point startVENodeScr = this.converter.convertMapToScreen(
				nodeToPullFrom.getLocation());
		//�������� ���������� �������� ����		
		Point currEndVENodeScr = this.converter
				.convertMapToScreen(nodeProcessed.getLocation());

		double distance = Math.sqrt(
				(currEndVENodeScr.x - startVENodeScr.x) * (currEndVENodeScr.x - startVENodeScr.x)
			+	(currEndVENodeScr.y - startVENodeScr.y) * (currEndVENodeScr.y - startVENodeScr.y));

		long t6 = System.currentTimeMillis();
		MapViewController.addTime4(t6 - t5);
		if (	(distance >= MINIMUM_SCREEN_LENGTH)
			||	(allLinksForNodeProcessed.size() > 2)) {
			//�������� ���������� ������� ������� ��� ���������� �� ���� � ��������� -
			//������ ������� ����������� � ������ ����� �����(�) ������� �����������,
			//������� �� ���������������� ��������.
			if (lastNode == nodeToPullFrom)
				//���� ��������� "������������" ������������ ������� ������� �� ������
				//����� - ��� � ����������
				this.visualElements.add(incomingLink);
			else {
				long d = System.currentTimeMillis();
				this.visualElements.add(
						new VisualMapElement(nodeToPullFrom, nodeProcessed));
				long f = System.currentTimeMillis();
				MapViewController.addTime2(f - d);
			}
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);

			frontedge.add(nodeProcessed);
//			for (NodeLink outgoingLink : allLinksForNodeProcessed) {
//				pullVisualLinksFromNode(
//						nodeProcessed, 
//						nodeProcessed,
//						outgoingLink, 
//						nodesCalculated, 
//						map);
//			}
		} else {
			//����� ������� ����������� ������. � ������ ��������� - �� ����� �������
			//������ ��� ������ ����� ����� ������ ��������� ������� ����������� (��������
			//�������), ������������ � ��� ���� �� �������� �� ������ �� ���� �������
			//��������
			for (NodeLink outgoingLink : allLinksForNodeProcessed) {
				//��������� �� ������ ����, �� �������� �� ������ � ������ ����
				if (!outgoingLink.equals(incomingLink))		
					if (! nodeLinksCalculated.get(outgoingLink).booleanValue()) {
						nodeLinksCalculated.put(outgoingLink, Boolean.TRUE);
						pullVisualLinksFromNode(
								nodeToPullFrom, 
								nodeProcessed,
								outgoingLink, 
								nodesCalculated, 
								nodeLinksCalculated,
								frontedge,
								map);
				}
			}
			//�������� � �������, ��� ���� ���������. 
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
		}
	}

	/**
	 * ������������ �������� �����������, ������������� � �������� ��������
	 * @param g
	 * @param visibleBounds
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void drawVisualLinks(Graphics g, Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		long t1 = System.currentTimeMillis();

		//���� ����� ������ nodeLink �� ��������, �� ������� ����� ������ physicalLink
		if (! this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK))
		{
			Command modeCommand = getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		for (Iterator veIterator = this.visualElements.iterator(); veIterator
				.hasNext();) {
			AbstractNode startNode = null;
			AbstractNode endNode = null;

			Object veElement = veIterator.next();
			if (veElement instanceof VisualMapElement) {
				VisualMapElement vme = (VisualMapElement) veElement;
				startNode = vme.startNode;
				endNode = vme.endNode;
			} else if (veElement instanceof NodeLink) {
				NodeLink nl = (NodeLink) veElement;
				startNode = nl.getStartNode();
				endNode = nl.getEndNode();
			}
			else
				continue;

			if ((startNode == null) && (endNode == null))
				throw new AssertionError(
						"LogicalnetLayer | drawVisualLinks | startNode and endNode can't be null!");

			if (visibleBounds.intersectsLine(
					startNode.getLocation().getX(),
					startNode.getLocation().getY(), 
					endNode.getLocation().getX(), 
					endNode.getLocation().getY())) {
				Point from = this.converter.convertMapToScreen(
						startNode.getLocation());
				Point to = this.converter.convertMapToScreen(
						endNode.getLocation());

				if (veElement instanceof VisualMapElement) {
					g.setColor(MapPropertiesManager.getColor());
					((Graphics2D )g).setStroke(MapPropertiesManager.getStroke());
					g.drawLine(from.x, from.y, to.x, to.y);
				}
				else if (veElement instanceof NodeLink) {
					//TODO ����� ������ ��������� �������������� NodeLink
//					g.drawLine(from.x, from.y, to.x, to.y);
					NodeLink nodeLink = (NodeLink)veElement;
					if (getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH) {
						for(Iterator iter = this.mapView.getMeasurementPaths(nodeLink).iterator(); iter.hasNext();) {
							MeasurementPath measurementPath = (MeasurementPath )iter.next();
							getMapViewController().getController(measurementPath).paint(measurementPath, g, visibleBounds);
						}
					}
					else
					if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH) {
						for(Iterator iter = this.mapView.getCablePaths(nodeLink).iterator(); iter.hasNext();) {
							CablePath cablePath = (CablePath )iter.next();
							getMapViewController().getController(cablePath).paint(cablePath, g, visibleBounds);
						}
					}
					else
					if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK) {
						PhysicalLink physicalLink = nodeLink.getPhysicalLink();
						getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
					}
					else
					if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK) {
						getMapViewController().getController(nodeLink).paint(nodeLink, g, visibleBounds);
					}
				}
			}
		}
		for (Iterator veIterator = this.visualElements.iterator(); veIterator.hasNext();) {
			Object veElement = veIterator.next();
			if (veElement instanceof SiteNode) {
				getMapViewController().getController((SiteNode)veElement).paint((SiteNode)veElement, g, visibleBounds);
			}
		}
		long t2 = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawVisualLinks | " + String.valueOf(t2 - t1) + " ms\n", 
				Level.INFO);
	}
	
	private void searchLinksForNodes()
	{
		long t1 = System.currentTimeMillis();

		//Cleaning the map
		//TODO ����� ����� ����� ������ ������ ��������� � ������ ��������������		
//		this.linksForNodes.clear();

		/////////////////////////
//		java.util.Map<AbstractNode,Set<NodeLink>> newlfnMap =
//			new HashMap<AbstractNode,Set<NodeLink>>();
		
		Collection <Set<NodeLink>> emptySets = this.linksForNodes.values();
		for (Iterator emptySetsIt = emptySets.iterator(); emptySetsIt.hasNext();)
		{
			Set set = (Set)emptySetsIt.next();
			set.clear();
		}
		/////////////////////////		
		
		for (Iterator nodesLinksIt = this.mapView.getMap().getNodeLinks().iterator();
				nodesLinksIt.hasNext();)
		{
			NodeLink link = (NodeLink)nodesLinksIt.next();
			
			AbstractNode startNode = link.getStartNode();
			Set<NodeLink> startNodeLinksSet = this.linksForNodes.get(startNode);
			if (startNodeLinksSet == null)
			{
				startNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(startNode,startNodeLinksSet);
			}
			startNodeLinksSet.add(link);

			AbstractNode endNode = link.getEndNode();
			Set<NodeLink> endNodeLinksSet = this.linksForNodes.get(endNode);
			if (endNodeLinksSet == null)
			{
				endNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(endNode,endNodeLinksSet);
			}
			endNodeLinksSet.add(link);
		}
		
		long t2 = System.currentTimeMillis();		
		Log.debugMessage("LogicalNetLayer.searchLinksForNodes | total time "
				+ (t2 - t1) + " ms.", Level.INFO);		
	}

	/**
	 * ��������� ������ ��������� ������� ������ ������������ ��� ������� ���������.
	 */
	public void calculateVisualElements() 
			throws MapDataException, MapConnectionException {

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();
		MapViewController.nullTime6();

		long startTime = System.currentTimeMillis();
		this.visualElements.clear();
		long t1 = System.currentTimeMillis();
		
		Map map = this.getMapView().getMap();

		Set<NodeLink> allNodeLinks = new HashSet<NodeLink>(this.mapView.getMap().getNodeLinks());
		long t2 = System.currentTimeMillis();

		if (getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH)
		{
			for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();)
			{
				MeasurementPath measurementPath = (MeasurementPath)it.next();

				Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				fillOptimizationSets(measurementPath, allNodeLinks, nodes, nodeLinks);
				
				MeasurementPathController controller = (MeasurementPathController )
						getMapViewController().getController(measurementPath);
				calculateVisualElements(
						nodes, 
						nodeLinks,
						controller.getColor(measurementPath),
						controller.getStroke(measurementPath),
						false);
			}
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			for(Iterator it = this.mapView.getCablePaths().iterator(); it.hasNext();)
			{
				CablePath cablePath = (CablePath)it.next();

				Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				fillOptimizationSets(cablePath, allNodeLinks, nodes, nodeLinks);
				
				CableController controller = (CableController )
				getMapViewController().getController(cablePath);
				calculateVisualElements(
						nodes, 
						nodeLinks,
						controller.getColor(cablePath),
						controller.getStroke(cablePath),
						false);
			}
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		long endTime = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.calculateVisualLinks | "
				+ "optimized map for " + (endTime - startTime) + "ms. Got " + this.visualElements.size() + " visual links.\n"
				+ "		" + (t1 - startTime) + " ms (visualElements.clear())\n"
				+ "		" + (t2 - t1) + " ms (getNodeLinks)\n"				
				+ "		" + (endTime - t2) + " ms (recursing) divided to:\n"
				+ "			" + MapViewController.getTime1() + " ms (fillOptimizationSets)\n"
				+ "			" + MapViewController.getTime2() + " ms (searchLinksForNodes)\n"
				+ "			" + MapViewController.getTime3() + " ms (fill Calculated maps)\n"
				+ "			" + MapViewController.getTime4() + " ms (create VisualElements)\n"
				+ "			" + MapViewController.getTime5() + " ms (calculate distance)\n",
				Level.INFO);
	}

	private void fillOptimizationSets(
			CablePath cablePath, 
			Set<NodeLink> allNodeLinks, 
			Set<AbstractNode> nodes, 
			Set<NodeLink> nodeLinks) {

		long t1 = System.currentTimeMillis();
		for(Iterator cpIterator = cablePath.getLinks().iterator(); cpIterator.hasNext();) {
			PhysicalLink physicalLink = (PhysicalLink )cpIterator.next();
			for(Iterator plIterator = physicalLink.getNodeLinks().iterator(); plIterator.hasNext();) {
				NodeLink nodeLink = (NodeLink )plIterator.next();
				nodes.add(nodeLink.getStartNode());
				nodes.add(nodeLink.getEndNode());
				nodeLinks.add(nodeLink);
				allNodeLinks.remove(nodeLink);
			}
		}
		long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	private void fillOptimizationSets(
			MeasurementPath measurementPath, 
			Set<NodeLink> allNodeLinks, 
			Set<AbstractNode> nodes, 
			Set<NodeLink> nodeLinks) {

		long t1 = System.currentTimeMillis();
		for(Iterator mpIterator = measurementPath.getSortedCablePaths().iterator(); mpIterator.hasNext();) {
			CablePath cablePath = (CablePath )mpIterator.next();
			for(Iterator cpIterator = cablePath.getLinks().iterator(); cpIterator.hasNext();) {
				PhysicalLink physicalLink = (PhysicalLink )cpIterator.next();
				for(Iterator plIterator = physicalLink.getNodeLinks().iterator(); plIterator.hasNext();) {
					NodeLink nodeLink = (NodeLink )plIterator.next();
					nodes.add(nodeLink.getStartNode());
					nodes.add(nodeLink.getEndNode());
					nodeLinks.add(nodeLink);
					allNodeLinks.remove(nodeLink);
				}
			}
		}
		long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	public void calculateVisualElements(
			Set<AbstractNode> nodes,
			Set<NodeLink> nodeLinks,
			Color color,
			Stroke stroke,
			boolean pullAttributesFromController)
			throws MapDataException, MapConnectionException {

		this.searchLinksForNodes(nodeLinks);

		Map map = this.getMapView().getMap();

		long t1 = System.currentTimeMillis();
		//�������, � ������� ���������� ���������� � ���, ������������� ��
		//��� ���� ��� ���
		java.util.Map<AbstractNode, Boolean> nodesCalculated = 
			new HashMap<AbstractNode, Boolean>(nodes.size());
		for (Iterator nodesIt = nodes.iterator(); nodesIt.hasNext();) {
			AbstractNode node = (AbstractNode) nodesIt.next();
			nodesCalculated.put(node, Boolean.FALSE);
		}

		java.util.Map<NodeLink, Boolean> nodeLinksCalculated = 
			new HashMap<NodeLink, Boolean>(nodeLinks.size());
		for (Iterator nodesIt = nodeLinks.iterator(); nodesIt.hasNext();) {
			NodeLink nodeLink = (NodeLink) nodesIt.next();
			nodeLinksCalculated.put(nodeLink, Boolean.FALSE);
		}
		long t2 = System.currentTimeMillis();
		MapViewController.addTime3(t2 - t1);

		//��������� ��������. 
		//���� ��������� ����������� ���� ��� ��������� - �������� � ���� �� ���������.
		//���� ����� - ��� ������� ����, �� �������� ����� �������� ���� ���.
		Collection<AbstractNode> frontedge = new LinkedList<AbstractNode>();
		for (Iterator nodesIt = nodes.iterator(); nodesIt.hasNext();) {
			AbstractNode startRecursionNode = (AbstractNode) nodesIt.next();

			while(startRecursionNode != null) {
				if (! nodesCalculated.get(startRecursionNode).booleanValue()) {
					Set<NodeLink> allLinksForStartRecursionNode =
						this.linksForNodes.get(startRecursionNode);
					
					if (allLinksForStartRecursionNode != null){
						//���� � ���� �� ���� ��������� �� ������ NodeLinka, �� ��� ���� Set == null
						for (NodeLink outgoingLink : allLinksForStartRecursionNode) {
							if (! nodeLinksCalculated.get(outgoingLink).booleanValue()) {
								nodeLinksCalculated.put(outgoingLink, Boolean.TRUE);
								pullVisualLinksFromNode1(
										startRecursionNode, 
										startRecursionNode,
										outgoingLink, 
										nodesCalculated,
										nodeLinksCalculated,
										frontedge,
										map,
										color,
										stroke,
										pullAttributesFromController);
							}
						}
					}
				}
				Iterator<AbstractNode> it = frontedge.iterator();
				if(it.hasNext()) {
					startRecursionNode = it.next();
					it.remove();
				}
				else
					startRecursionNode = null;
			}
		}
	}

	/**
	 * ����������� ��������� ������ �������� ����� �����, �������� �����������
	 * "�����������" ����.
	 * @param nodeToPullFrom ����, �� �������� ������� ������� VisualMapElement.
	 * @param lastNode ���������� ���� - �� ���� �� ������ � ������� ����
	 * @param incomingLink ����, �� �������� �� ������ � ������ ����. ��� �������
	 * ������ �������� = null.
	 * @param nodesCalculated ������� ���������� ���������� � ���, ��������� �� ��� ��� 
	 * ���� ����.
	 * @param nodeLinksCalculated 
	 * @param frontedge 
	 * @param map ������ "�����"
	 * @param stroke 
	 * @param color 
	 */
	private void pullVisualLinksFromNode1(
			AbstractNode nodeToPullFrom,
			AbstractNode lastNode, 
			NodeLink incomingLink,
			java.util.Map<AbstractNode, Boolean> nodesCalculated, 
			java.util.Map<NodeLink, Boolean> nodeLinksCalculated, 
			Collection<AbstractNode> frontedge, 
			Map map, 
			Color color, 
			Stroke stroke,
			boolean pullAttributesFromController) 
			throws MapDataException, MapConnectionException {
		//��������������� ����
		AbstractNode nodeProcessed = null;

		if (incomingLink == null)
			nodeProcessed = lastNode;
		else if (incomingLink.getStartNode().equals(lastNode))
			nodeProcessed = incomingLink.getEndNode();
		else if (incomingLink.getEndNode().equals(lastNode))
			nodeProcessed = incomingLink.getStartNode();

		if (nodeProcessed == null)
			throw new AssertionError(
					"LogicalNetLayer | pullVisualLinksFromNode | The nodeProcessed can't be null");

		if (nodesCalculated.get(nodeProcessed).booleanValue()) {
			long t1 = System.currentTimeMillis();
			//������� �������� - ������ ���� ��� ��������������.
			//������ ������� ����������� � �������.
			VisualMapElement visualMapElement;
			if (lastNode == nodeToPullFrom) {
				//���� ��������� "������������" ������������ ������� ������� �� ������
				//����� - ��� � ����������
				if(pullAttributesFromController) {
					NodeLinkController controller = (NodeLinkController )getMapViewController().getController(incomingLink);
					visualMapElement = new VisualMapElement(incomingLink, controller.getColor(incomingLink), controller.getStroke(incomingLink));
				}
				else
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
			}
			else {
				visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
			}
			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			long t2 = System.currentTimeMillis();
			MapViewController.addTime4(t2 - t1);
			return;
		}
		//�������� ������ ���� ��������/��������� ����� ��� ������� ����
		Set<NodeLink> allLinksForNodeProcessed = this.linksForNodes.get(nodeProcessed);

		long t5 = System.currentTimeMillis();
		//�������� ���������� ����, �� �������� �� ����� ���������� ������� 
		Point startVENodeScr = this.converter.convertMapToScreen(
				nodeToPullFrom.getLocation());
		//�������� ���������� �������� ����		
		Point currEndVENodeScr = this.converter.convertMapToScreen(
				nodeProcessed.getLocation());
		double distance = Math.sqrt(
				(currEndVENodeScr.x - startVENodeScr.x) * (currEndVENodeScr.x - startVENodeScr.x)
			+	(currEndVENodeScr.y - startVENodeScr.y) * (currEndVENodeScr.y - startVENodeScr.y));
		long t6 = System.currentTimeMillis();
		MapViewController.addTime5(t6 - t5);

		if (	(distance >= MINIMUM_SCREEN_LENGTH)
			||	(allLinksForNodeProcessed.size() > 2)) {
			long t1 = System.currentTimeMillis();
			//�������� ���������� ������� ������� ��� ���������� �� ���� � ��������� -
			//������ ������� ����������� � ������ ����� �����(�) ������� �����������,
			//������� �� ���������������� ��������.
			VisualMapElement visualMapElement;
			if (lastNode == nodeToPullFrom) {
				//���� ��������� "������������" ������������ ������� ������� �� ������
				//����� - ��� � ����������
				if(pullAttributesFromController) {
					NodeLinkController controller = (NodeLinkController )getMapViewController().getController(incomingLink);
					visualMapElement = new VisualMapElement(incomingLink, controller.getColor(incomingLink), controller.getStroke(incomingLink));
				}
				else
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
			}
			else {
				visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
			}
			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			long t2 = System.currentTimeMillis();
			MapViewController.addTime4(t2 - t1);
			frontedge.add(nodeProcessed);
		} else {
			//����� ������� ����������� ������. � ������ ��������� - �� ����� �������
			//������ ��� ������ ����� ����� ������ ��������� ������� ����������� (��������
			//�������), ������������ � ��� ���� �� �������� �� ������ �� ���� �������
			//��������
			for (NodeLink outgoingLink : allLinksForNodeProcessed) {
				//��������� �� ������ ����, �� �������� �� ������ � ������ ����
				if (!outgoingLink.equals(incomingLink))		
					if (! nodeLinksCalculated.get(outgoingLink).booleanValue()) {
						nodeLinksCalculated.put(outgoingLink, Boolean.TRUE);
						pullVisualLinksFromNode1(
								nodeToPullFrom, 
								nodeProcessed,
								outgoingLink, 
								nodesCalculated, 
								nodeLinksCalculated,
								frontedge,
								map,
								color,
								stroke,
								pullAttributesFromController);
				}
			}
			//�������� � �������, ��� ���� ���������. 
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
		}
	}

	/**
	 * ������������ �������� �����������, ������������� � �������� ��������
	 * @param g
	 * @param visibleBounds
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void drawVisualElements(Graphics g, Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		long t1 = System.currentTimeMillis();

		//���� ����� ������ nodeLink �� ��������, �� ������� ����� ������ physicalLink
		if (! this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK))
		{
			Command modeCommand = getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		for (Iterator veIterator = this.visualElements.iterator(); veIterator
				.hasNext();) {
			Object veElement = veIterator.next();
			if (veElement instanceof SiteNode) 
				continue;

			if (veElement instanceof VisualMapElement) {
				VisualMapElement vme = (VisualMapElement) veElement;
				if(vme.mapElement != null) {
					if(vme.mapElement instanceof NodeLink) {
						NodeLink nodeLink = (NodeLink)vme.mapElement;
						NodeLinkController controller = (NodeLinkController )getMapViewController().getController(nodeLink);
						if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
							controller.paint(nodeLink, g, visibleBounds);
						else
							controller.paint(nodeLink, g, visibleBounds, vme.stroke, vme.color);
					}
					continue;
				}

				DoublePoint startLocation = vme.startNode.getLocation();
				DoublePoint endLocation = vme.endNode.getLocation();
				if (visibleBounds.intersectsLine(
						startLocation.getX(),
						startLocation.getY(), 
						endLocation.getX(), 
						endLocation.getY())) {
					Point from = this.converter.convertMapToScreen(startLocation);
					Point to = this.converter.convertMapToScreen(endLocation);
					g.setColor(vme.color);
					((Graphics2D )g).setStroke(vme.stroke);
					g.drawLine(from.x, from.y, to.x, to.y);
				}
			}
		}
		for (Iterator veIterator = this.visualElements.iterator(); veIterator.hasNext();) {
			Object veElement = veIterator.next();
			if (veElement instanceof SiteNode) {
				getMapViewController().getController((SiteNode)veElement).paint((SiteNode)veElement, g, visibleBounds);
			}
		}
		long t2 = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawVisualLinks | " + String.valueOf(t2 - t1) + " ms\n", 
				Level.INFO);
	}
	
	private void searchLinksForNodes(
			Set<NodeLink> nodeLinks)
	{
		long t1 = System.currentTimeMillis();

		Collection <Set<NodeLink>> emptySets = this.linksForNodes.values();
		for (Iterator emptySetsIt = emptySets.iterator(); emptySetsIt.hasNext();)
		{
			Set set = (Set)emptySetsIt.next();
			set.clear();
		}
		
		for (Iterator nodesLinksIt = nodeLinks.iterator(); nodesLinksIt.hasNext();)
		{
			NodeLink link = (NodeLink)nodesLinksIt.next();
			
			AbstractNode startNode = link.getStartNode();
			Set<NodeLink> startNodeLinksSet = this.linksForNodes.get(startNode);
			if (startNodeLinksSet == null)
			{
				startNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(startNode, startNodeLinksSet);
			}
			startNodeLinksSet.add(link);

			AbstractNode endNode = link.getEndNode();
			Set<NodeLink> endNodeLinksSet = this.linksForNodes.get(endNode);
			if (endNodeLinksSet == null)
			{
				endNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(endNode, endNodeLinksSet);
			}
			endNodeLinksSet.add(link);
		}
		
		long t2 = System.currentTimeMillis();
		MapViewController.addTime2(t2 - t1);
	}
}
