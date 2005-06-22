/**
 * $Id: LogicalNetLayer.java,v 1.78 2005/06/22 07:27:32 krupenn Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.command.action.DeleteSelectionCommand;
import com.syrus.AMFICOM.client.map.command.action.MoveNodeCommand;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.AbstractNodeController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapElementController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.CommandList;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
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

/**
 * ��������� ������������ ���������� ��������� ����.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.78 $, $Date: 2005/06/22 07:27:32 $
 * @module mapviewclient_v2
 */
public class LogicalNetLayer
{
	protected CommandList commandList = new CommandList(20);
	
	/** ����, ����������� ��������� �� ����. */
//	protected AnimateThread animateThread = null;

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
//	protected boolean performProcessing = true;
	
	/** ���� �������� ���������. */
//	protected boolean doNotify = true;
	
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

	/** ������������� ������������. ������������ ��� �������� ����� ��������. */
//	protected Identifier userId = null;

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

	public LogicalNetLayer(MapCoordinatesConverter converter, MapContext mapContext) {
		this.converter = converter;
		this.mapContext = mapContext;
	}
	
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

		if(aContext != null) {
			Identifier userId = LoginManager.getUserId();

			LinkTypeController.createDefaults(userId);
			NodeTypeController.createDefaults(userId);
		}
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
	 * ���������������� ������������ �������� ����� �����.
	 * @param mapView ���
	 */
	public void setMapView(MapView mapView)
		throws MapConnectionException, MapDataException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapView(" + mapView + ")");

//		if(this.animateThread != null)
//			this.animateThread.stopRunning();

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

		if(mapView == null)
		{
			System.out.println("mapView null!");
		}
		else
		{
			if(this.aContext != null)
				if(this.aContext.getApplicationModel() != null)
					if (this.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_INDICATION))
				{
//					this.animateThread = new AnimateThread(this);
//					this.animateThread.start();
				}
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
//	public void setCurrentScale(double currentScale)
//	{
//		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentScale(" + currentScale + ")");
//		this.currentScale = currentScale;
//		updateZoom();
//	}
	
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

//		long f;
//		long d;

//		System.out.println("------------------ paint called ----------------------");
//		try {
//			throw new Exception("stacktrace");
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("--------------------------------------");
//		f = System.currentTimeMillis();
		drawLines(p, visibleBounds);
//		d = System.currentTimeMillis();
//		detailedDateFormat.format(new Date(System.currentTimeMillis()))
//		System.out.println("draw lines in " + String.valueOf(d - f) + " ms");
//		f = System.currentTimeMillis();
		drawNodes(p, visibleBounds);
//		d = System.currentTimeMillis();
//		System.out.println("draw nodes in " + String.valueOf(d - f) + " ms");
//		f = System.currentTimeMillis();
		drawSelection(p, visibleBounds);
//		d = System.currentTimeMillis();
//		System.out.println("draw selection in " + String.valueOf(d - f) + " ms");
//		f = System.currentTimeMillis();
		drawTempLines(p, visibleBounds);
//		d = System.currentTimeMillis();
//		System.out.println("draw temp lines in " + String.valueOf(d - f) + " ms");
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
		Iterator e;
	
		this.elementsToDisplay.clear();

		//���� ����� ������ nodeLink �� ��������, �� ������� ����� ������ physicalLink
		if (! this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK))
		{
			Command com = getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
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
	public void drawNodes(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
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
	public void drawSelection(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		Iterator e = getMapView().getMap().getSelectedElements().iterator();
		while (e.hasNext())
		{
			MapElement el = (MapElement)e.next();
			getMapViewController().getController(el).paint(el, g, visibleBounds);
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapElementAtPoint(" + point + ")");
		
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");

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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "selectAll()");
		
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
	}

	/**
	 * �������� ��������� ����������� ������� ������������.
	 */
	public void undo()
		throws MapConnectionException, MapDataException
	{
		this.commandList.undo();
		sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	/**
	 * ��������� ���������� ������� �������������.
	 */
	public void redo()
		throws MapConnectionException, MapDataException
	{
		this.commandList.redo();
		sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	/**
	 * �������� ��������� ��� ��� �������� ����� �����.
	 * @return ��� �����
	 */
	public PhysicalLinkType getCurrentPhysicalLinkType()
	{
		if(this.currentPhysicalLinkType == null) {
			this.currentPhysicalLinkType = LinkTypeController.getDefaultPhysicalLinkType();
		}
		return this.currentPhysicalLinkType;
	}
	
	/**
	 * �������� ��� �������������� ����.
	 * @return ��� ����
	 */
	public SiteNodeType getUnboundNodeType()
	{
		if(this.unboundNodeType == null) {
			this.unboundNodeType = NodeTypeController.getUnboundNodeType();
		}
		return this.unboundNodeType;
	}
	
	/**
	 * �������� ��� �������������� ������.
	 * @return ��� �������������� ������
	 */
	public PhysicalLinkType getUnboundLinkType()
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

}
