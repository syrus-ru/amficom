/**
 * $Id: LogicalNetLayer.java,v 1.121 2005/09/07 12:32:00 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.command.action.MoveNodeCommand;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.AbstractNodeController;
import com.syrus.AMFICOM.client.map.controllers.AlarmMarkerController;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.EventMarkerController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapElementController;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ��������� ������������ ���������� ��������� ����.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.121 $, $Date: 2005/09/07 12:32:00 $
 * @module mapviewclient_v2
 */
public final class LogicalNetLayer {
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
	protected List<AbstractNode> fixedNodeList = new LinkedList<AbstractNode>();

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
	protected List<PhysicalLink> elementsToDisplay = new LinkedList<PhysicalLink>();

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

	public LogicalNetLayer(final ApplicationContext aContext, final MapCoordinatesConverter converter, final MapContext mapContext)
			throws ApplicationException {
		this.aContext = aContext;
		this.converter = converter;
		this.mapContext = mapContext;

		final Identifier userId = LoginManager.getUserId();

		MapLibraryController.createDefaults(userId);

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
	public void updateZoom() throws MapDataException, MapConnectionException {
		Log.debugMessage(this.getClass().getName() + "::" + "updateZoom()" + " | " + "method call", Level.FINEST);

		if (this.getMapView() == null) {
			return;
		}
		final Map map = this.getMapView().getMap();
		if (map != null) {
			for (final AbstractNode curNode : map.getNodes()) {
				((AbstractNodeController) this.getMapViewController().getController(curNode)).updateScaleCoefficient(curNode);
			}
		}
		if (MapPropertiesManager.isOptimizeLinks()) {
			// calculateVisualLinks();
			calculateVisualElements();
		}
	}

	/**
	 * �������� �������� ����������.
	 * 
	 * @return �������� ����������
	 */
	public ApplicationContext getContext() {
		return this.aContext;
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	/**
	 * ���������������� ������������ �������� ����� �����.
	 * 
	 * @param mapView
	 *        ���
	 */
	public void setMapView(final MapView mapView) throws MapConnectionException, MapDataException {
		Log.debugMessage(this.getClass().getName() + "::" + "setMapView(" + mapView + ")" + " | " + "method call", Level.FINEST);

		if (this.getContext() != null && this.getContext().getDispatcher() != null) {
			if (mapView != null && mapView.getMap() != null) {
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_VIEW_SELECTED, mapView));
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_SELECTED, mapView.getMap()));
			} else {
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
				this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(this, MapEvent.MAP_DESELECTED));
			}
		}

		this.mapView = mapView;
		this.linksForNodes.clear();

		if (mapView == null) {
			Log.debugMessage("mapView null!", Level.SEVERE);
		}

		this.getMapViewController().setMapView(this.mapView);

		// ����������� ������� ������� Void
		this.currentMapElement = VoidElement.getInstance(this.mapView);

		this.commandList.flush();
	}

	/**
	 * �������� ���.
	 * 
	 * @return ���
	 */
	public MapView getMapView() {
		return this.mapView;
	}

	/**
	 * �������� ������� ��������� �� ���������.
	 * 
	 * @return ������� ��������� �� ���������
	 */
	public double getDefaultScale() {
		return this.defaultScale;
	}

	/**
	 * ���������� ������� ��������� �� ���������.
	 * 
	 * @param defaultScale
	 *        ������� ��������� �� ���������
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	public void setDefaultScale(final double defaultScale) throws MapDataException, MapConnectionException {
		Log.debugMessage(this.getClass().getName() + "::" + "setDefaultScale(" + defaultScale + ")" + " | " + "method call", Level.FINEST);

		this.defaultScale = defaultScale;
		this.updateZoom();
	}

	/**
	 * �������� ������� ������� �������� ���������.
	 * 
	 * @return ������� ������� �������� ���������
	 */
	public double getCurrentScale() {
		return this.currentScale;
	}

	/**
	 * �������� ������� ��������� ���� �����.
	 * 
	 * @return ��������� ���� �����
	 */
	public MapState getMapState() {
		return this.mapState;
	}

	/**
	 * ���������� ������� ��������� ���� �����.
	 * 
	 * @param state
	 *        ����� ���������
	 */
	public void setMapState(final MapState state) {
		Log.debugMessage(this.getClass().getName() + "::" + "setMapState(" + state + ")" + " | " + "method call", Level.FINEST);

		this.mapState = state;
	}

	/**
	 * �������� ������� �������� �����.
	 * 
	 * @return ������� �������� �����
	 */
	public Point getCurrentPoint() {
		return this.currentPoint;
	}

	/**
	 * ���������� ������� �������� �����.
	 * 
	 * @param point
	 *        ������� �������� �����
	 */
	public void setCurrentPoint(final Point point) {
		this.currentPoint = point;
	}

	/**
	 * �������� ����������� ��������� �������� �����.
	 * 
	 * @return ��������� �������� �����
	 */
	public Point getStartPoint() {
		return new Point(this.startPoint);
	}

	/**
	 * ���������� ��������� �������� �����.
	 * 
	 * @param point
	 *        ��������� �������� �����
	 */
	public void setStartPoint(final Point point) {
		this.startPoint = new Point(point);
	}

	/**
	 * �������� ����������� �������� �������� �����.
	 * 
	 * @return �������� �������� �����
	 */
	public Point getEndPoint() {
		return new Point(this.endPoint);
	}

	/**
	 * ���������� �������� �������� �����.
	 * 
	 * @param point
	 *        �������� �������� �����
	 */
	public void setEndPoint(final Point point) {
		this.endPoint = new Point(point);
	}

	/**
	 * �������� ������ ������, ����������� �� ����.
	 * 
	 * @return ������ ������
	 */
	public CommandList getCommandList() {
		return this.commandList;
	}

	/**
	 * ���������� ��� �������� ����������� ���� �������������� �����.
	 * 
	 * @param g
	 *        ����������� ��������
	 */
	public void paint(final Graphics g, final Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException {
		if(this.mapView == null) {
			return;
		}
		final Graphics2D p = (Graphics2D) g;

		// remember settings from graphics
		final Color color = p.getColor();
		final Stroke stroke = p.getStroke();
		final Font font = p.getFont();
		final Color background = p.getBackground();

		Log.debugMessage("\n\n------------------ LogicalNetLayer.paint() called ----------------------", Level.FINE);
		final long f = System.currentTimeMillis();
		if (MapPropertiesManager.isOptimizeLinks()) {
			drawVisualElements(p, visibleBounds);
		} else {
			drawLines(p, visibleBounds);
			drawNodes(p, visibleBounds);
		}
		drawSelection(p, visibleBounds);
		drawTempLines(p, visibleBounds);
		final long d = System.currentTimeMillis();
		Log.debugMessage("\n--------------------- LogicalNetLayer.paint() finished in "
				+ String.valueOf(d - f)
				+ " ms -----------------\n", Level.FINE);

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
	public void drawLines(final Graphics g, final Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException {
		final long f = System.currentTimeMillis();

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();

		this.elementsToDisplay.clear();

		// ���� ����� ������ nodeLink �� ��������, �� ������� ����� ������
		// physicalLink
		if (!this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK)) {
			final Command modeCommand = this.getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		if (this.getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH) {
			this.elementsToDisplay.addAll(this.getMapView().getMap().getAllPhysicalLinks());
			for (final MeasurementPath measurementPath : this.mapView.getMeasurementPaths()) {
				// if(this.getMapViewController().getController(measurementPath).isElementVisible(measurementPath,
				// visibleBounds))
				this.getMapViewController().getController(measurementPath).paint(measurementPath, g, visibleBounds);

				// to avoid multiple cicling through scheme elements
				// use once sorted cable links
				for (final CablePath cablePath : measurementPath.getSortedCablePaths()) {
					this.elementsToDisplay.removeAll(cablePath.getLinks());
				}
			}
			for (final PhysicalLink physicalLink : this.elementsToDisplay) {
				// if(this.getMapViewController().getController(physicalLink).isElementVisible(physicalLink,
				// visibleBounds))
				this.getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		} else if (this.getMapState().getShowMode() == MapState.SHOW_CABLE_PATH) {
			this.elementsToDisplay.addAll(this.getMapView().getMap().getAllPhysicalLinks());
			for (final CablePath cablePath : this.mapView.getCablePaths()) {
				// if(this.getMapViewController().getController(cablePath).isElementVisible(cablePath,
				// visibleBounds))
				this.getMapViewController().getController(cablePath).paint(cablePath, g, visibleBounds);
				this.elementsToDisplay.removeAll(cablePath.getLinks());
			}
			for (final PhysicalLink physicalLink : this.elementsToDisplay) {
				// if(this.getMapViewController().getController(physicalLink).isElementVisible(physicalLink,
				// visibleBounds))
				this.getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		} else if (this.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK) {
			for (final PhysicalLink physicalLink : this.getMapView().getMap().getAllPhysicalLinks()) {
				// if(this.getMapViewController().getController(physicalLink).isElementVisible(physicalLink,
				// visibleBounds))
				this.getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		} else if (this.getMapState().getShowMode() == MapState.SHOW_NODE_LINK) {
			for (final NodeLink nodeLink : this.getMapView().getMap().getAllNodeLinks()) {
				// if(this.getMapViewController().getController(nodeLink).isElementVisible(nodeLink,
				// visibleBounds))
				this.getMapViewController().getController(nodeLink).paint(nodeLink, g, visibleBounds);
			}
		}
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawLines | " + String.valueOf(d - f) + " ms\n" + "		"
				+ String.valueOf(MapViewController.getTime1()) + " ms (isElementVisible)\n" + "		"
				+ String.valueOf(MapViewController.getTime2()) + " ms (getStroke)\n" + "		"
				+ String.valueOf(MapViewController.getTime3()) + " ms (getColor)\n" + "		"
				+ String.valueOf(MapViewController.getTime4()) + " ms (paint)\n" + "		"
				+ String.valueOf(MapViewController.getTime5()) + " ms (painting labels)", Level.FINE);
	}

	/**
	 * ���������� ����.
	 * 
	 * @param g
	 *        ����������� ��������
	 */
	public void drawNodes(final Graphics g, final Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException {
		final long f = System.currentTimeMillis();
		final boolean showNodes = MapPropertiesManager.isShowPhysicalNodes();
		for (final AbstractNode curNode : this.getMapView().getMap().getNodes()) {
			if (curNode instanceof TopologicalNode) {
				if (showNodes) {
					this.getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
				}
			} else {
				this.getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
			}
		}
		for (final AbstractNode curNode : this.getMapView().getMap().getExternalNodes()) {
			this.getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
		}
		/*
		 * e = getMapView().getMarkers().iterator(); while (e.hasNext()) { MapMarker
		 * marker = (MapMarker)e.next(); marker.paint(pg, visibleBounds); }
		 */
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawNodes | " + String.valueOf(d - f) + " ms", Level.FINE);
	}

	/**
	 * ���������� ��������� �����. - ������������� ������ ��������, - �����
	 * ���������� ����� - ������������� ���������������
	 * 
	 * @param g
	 *        ����������� ��������
	 */
	public void drawTempLines(final Graphics g, final Rectangle2D.Double visibleBounds) {
		final long f = System.currentTimeMillis();
		final Graphics2D p = (Graphics2D) g;
		final int startX = getStartPoint().x;
		final int startY = getStartPoint().y;
		final int endX = getEndPoint().x;
		final int endY = getEndPoint().y;

		p.setStroke(new BasicStroke(3));

		switch (this.mapState.getActionMode()) {
			case MapState.SELECT_MARKER_ACTION_MODE:
				p.setColor(Color.BLUE);
				p.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
				break;
			case MapState.DRAW_LINES_ACTION_MODE:
				p.setColor(Color.RED);
				p.drawLine(startX, startY, endX, endY);
				break;
			default:
				break;
		}

		// �� �������� �������������
		if (this.mapState.getOperationMode() == MapState.ZOOM_TO_RECT) {
			p.setColor(Color.YELLOW);
			p.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
		} else if (this.mapState.getOperationMode() == MapState.MEASURE_DISTANCE) {
			p.setColor(Color.GREEN);
			p.drawLine(startX, startY, endX, endY);
		}
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawTempLines | " + String.valueOf(d - f) + " ms", Level.FINE);
	}

	/**
	 * ���������� ���������� ��������.
	 * 
	 * @param g
	 *        ����������� ��������
	 */
	public void drawSelection(final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		final long f = System.currentTimeMillis();
		for (final MapElement el : this.getMapView().getMap().getSelectedElements()) {
			this.getMapViewController().getController(el).paint(el, g, visibleBounds);
		}
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawSelection | " + String.valueOf(d - f) + " ms", Level.FINE);
	}

	/**
	 * ��������� ������� �����.
	 */
	public void sendScaleEvent(final Double scale) {
		if (this.getContext() != null) {
			if (this.getContext().getDispatcher() != null) {
				this.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_SCALE_CHANGED, scale));
			}
		}
	}

	/**
	 * ��������� ������� �����.
	 */
	public void sendMapEvent(final String eventString) {
		if (this.getContext() != null) {
			if (this.getContext().getDispatcher() != null) {
				this.getContext().getDispatcher().firePropertyChange(new MapEvent(this, eventString));
			}
		}
	}

	public void sendSelectionChangeEvent() {
		if (this.getContext() != null) {
			if (this.getContext().getDispatcher() != null) {
				this.getContext().getDispatcher().firePropertyChange(new MapEvent(this,
						MapEvent.SELECTION_CHANGED,
						this.mapView.getMap().getSelectedElements()));
			}
		}
	}

	/**
	 * �������� ������� ������������� �������. ������������ � ������
	 * {@link MapState#MOVE_FIXDIST}.
	 * 
	 * @return ������� ������������� �������
	 */
	public AbstractNode getFixedNode() {
		return this.fixedNode;
	}

	/**
	 * �������� ������ �����, �������� (����� ���������) � ������������� �����.
	 * ������������ � ������ {@link MapState#MOVE_FIXDIST}.
	 * 
	 * @return ������ ���������.
	 */
	public List getFixedNodeList() {
		return this.fixedNodeList;
	}

	/**
	 * �������� ������� ��������� �������.
	 * 
	 * @return ������� ��������� �������
	 */
	public MapElement getCurrentMapElement() {
		return this.currentMapElement;
	}

	/**
	 * ���������� ������� ��������� �������.
	 * 
	 * @param curMapElement
	 *        ������� ��������� �������
	 */
	public void setCurrentMapElement(final MapElement curMapElement) {
		Log.debugMessage(this.getClass().getName() + "::" + "setCurrentMapElement(" + curMapElement + ")" + " | " + "method call",
				Level.FINEST);

		this.currentMapElement = curMapElement;

		if (this.getMapState().getOperationMode() == MapState.NO_OPERATION) {
			final boolean canFixDist = (curMapElement instanceof TopologicalNode) || (curMapElement instanceof SiteNode);
			if (this.getContext().getApplicationModel().isEnabled(MapApplicationModel.OPERATION_MOVE_FIXED) != canFixDist) {
				if (canFixDist) {
					this.fixedNode = (AbstractNode) curMapElement;
					this.fixedNodeList.clear();
					for (final NodeLink mnle : this.mapView.getMap().getNodeLinks(this.fixedNode)) {
						this.fixedNodeList.add(mnle.getOtherNode(this.fixedNode));
					}
				}
				this.getContext().getApplicationModel().setEnabled(MapApplicationModel.OPERATION_MOVE_FIXED, canFixDist);
				this.getContext().getApplicationModel().fireModelChanged();
			}
		}

		if (curMapElement instanceof VoidElement) {
			return;
		}
		if (!(curMapElement instanceof Selection)) {
			this.mapView.getMap().setSelected(curMapElement, true);
		}
	}

	/**
	 * �������� ������� ������� �� �������� ���������� �� �����.
	 * 
	 * @param point
	 *        �������� ����������
	 * @return ������� � �����
	 */
	public MapElement getMapElementAtPoint(final Point point, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		Log.debugMessage(this.getClass().getName() + "::" + "getMapElementAtPoint(" + point + ")" + " | " + "method call", Level.FINEST);

		final int showMode = this.getMapState().getShowMode();
		MapElement curME = VoidElement.getInstance(this.getMapView());

		// ����� ����������� �� ���� ��������� � ���� �� �����-������ �� ��� ������
		// �� ������������� ��� ������� ���������
		for (final MapElement mapElement : this.mapView.getAllElements()) {
			final MapElementController controller = this.getMapViewController().getController(mapElement);
			if (controller.isElementVisible(mapElement, visibleBounds))
				if (controller.isMouseOnElement(mapElement, point)) {
					curME = mapElement;

					if (mapElement instanceof NodeLink) {
						// ����� ������� �� ����� linkState ��� ������
						if (showMode == MapState.SHOW_NODE_LINK) {// curME �������� NodeLink
						} else if (showMode == MapState.SHOW_PHYSICAL_LINK) {
							curME = ((NodeLink) mapElement).getPhysicalLink();
						} else if (showMode == MapState.SHOW_CABLE_PATH) {
							final Iterator<CablePath> it = this.mapView.getCablePaths((NodeLink) mapElement).iterator();
							if (it.hasNext()) {
								curME = it.next();
							} else {
								curME = ((NodeLink) mapElement).getPhysicalLink();
							}
						} else if (showMode == MapState.SHOW_MEASUREMENT_PATH) {
							final Iterator<MeasurementPath> it = this.mapView.getMeasurementPaths((NodeLink) mapElement).iterator();
							if (it.hasNext()) {
								curME = it.next();
							} else {
								curME = ((NodeLink) mapElement).getPhysicalLink();
							}
						}
					} else if (mapElement instanceof PhysicalLink) {
						if (showMode == MapState.SHOW_CABLE_PATH) {
							final Iterator<CablePath> it = this.mapView.getCablePaths((PhysicalLink) mapElement).iterator();
							if (it.hasNext()) {
								curME = it.next();
							}
						} else if (showMode == MapState.SHOW_MEASUREMENT_PATH) {
							final Iterator<MeasurementPath> it = this.mapView.getMeasurementPaths((PhysicalLink) mapElement).iterator();
							if (it.hasNext()) {
								curME = it.next();
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
	public void deselectAll() {
		Log.debugMessage(this.getClass().getName() + "::" + "deselectAll()" + " | " + "method call", Level.FINEST);

		final Collection<MapElement> selectedElements = new LinkedList<MapElement>();
		selectedElements.addAll(this.mapView.getMap().getSelectedElements());
		for (final MapElement mapElement : selectedElements) {
			mapElement.setSelected(false);
		}
		this.mapView.getMap().clearSelection();
	}

	/**
	 * ������� ��� ��������.
	 */
	public void selectAll() {
		Log.debugMessage(this.getClass().getName() + "::" + "selectAll()" + " | " + "method call", Level.FINEST);

		Map map = this.mapView.getMap();
		for (final MapElement curElement : this.mapView.getAllElements()) {
			map.setSelected(curElement, true);
		}
	}

	/**
	 * �������� ������ ��������� ���������.
	 * 
	 * @return ������ ���������
	 */
	public Set<MapElement> getSelectedElements() {
		return this.mapView.getMap().getSelectedElements();
	}

	/**
	 * �������� ����, ��� ���� �� ���� ������� ������.
	 * 
	 * @return <code>true</code> ���� ���� ��������� �������
	 */
	public boolean isSelectionEmpty() {
		return this.getMapView().getMap().getSelectedElements().isEmpty();
	}

	/**
	 * �������� �������� �����, ��� �������� ����� ������������� ��������������
	 * �����, �� ���������� �� �����.
	 * 
	 * @param point
	 *        �������� ����������
	 * @return �������� �����, ��� <code>null</code>, ���� ��������� � ������
	 *         ����� ���
	 */
	public NodeLink getEditedNodeLink(final Point point) {
		Log.debugMessage(this.getClass().getName() + "::" + "getEditedNodeLink(" + point + ")" + " | " + "method call", Level.FINEST);

		NodeLinkController nlc = null;
		for (final NodeLink mapElement : this.getMapView().getMap().getNodeLinks()) {
			if (nlc == null) {
				nlc = (NodeLinkController) this.getMapViewController().getController(mapElement);
			}
			if (nlc.isMouseOnThisObjectsLabel(mapElement, point)) {
				return mapElement;
			}
		}
		return null;
	}

	/**
	 * ���������� ����� ����� ��������� ����� - ���������� �� ���� node �� �������
	 * ����.
	 * 
	 * @param nodelink
	 *        �������� �����
	 * @param node
	 *        ����, �� �������� ������������� ����������
	 * @param dist
	 *        ����������
	 */
	public void setNodeLinkSizeFrom(final NodeLink nodelink, final AbstractNode node, final double dist)
			throws MapConnectionException,
				MapDataException {
		final DoublePoint anchor1 = node.getLocation();

		final MoveNodeCommand cmd = new MoveNodeCommand(node);
		cmd.setLogicalNetLayer(this);

		((NodeLinkController) this.getMapViewController().getController(nodelink)).setSizeFrom(nodelink, node, dist);

		final DoublePoint anchor2 = node.getLocation();
		cmd.setParameter(MoveSelectionCommandBundle.DELTA_X, String.valueOf(anchor2.getX() - anchor1.getX()));
		cmd.setParameter(MoveSelectionCommandBundle.DELTA_Y, String.valueOf(anchor2.getY() - anchor1.getY()));
		this.getCommandList().add(cmd);
		this.getCommandList().execute();

		this.getMapState().setOperationMode(MapState.NO_OPERATION);
		sendMapEvent(MapEvent.MAP_CHANGED);
	}

	/**
	 * �������� ��������� ����������� ������� ������������.
	 */
	public void undo() {
		this.commandList.undo();
		this.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	/**
	 * ��������� ���������� ������� �������������.
	 */
	public void redo() {
		this.commandList.redo();
		this.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	/**
	 * �������� ��������� ��� ��� �������� ����� �����.
	 * 
	 * @return ��� �����
	 * @throws ApplicationException
	 */
	public PhysicalLinkType getCurrentPhysicalLinkType() throws ApplicationException {
		if (this.currentPhysicalLinkType == null) {
			this.currentPhysicalLinkType = LinkTypeController.getDefaultPhysicalLinkType();
		}
		return this.currentPhysicalLinkType;
	}

	/**
	 * �������� ��� �������������� ����.
	 * 
	 * @return ��� ����
	 * @throws ApplicationException
	 */
	public SiteNodeType getUnboundNodeType() throws ApplicationException {
		if (this.unboundNodeType == null) {
			this.unboundNodeType = NodeTypeController.getUnboundNodeType();
		}
		return this.unboundNodeType;
	}

	/**
	 * �������� ��� �������������� ������.
	 * 
	 * @return ��� �������������� ������
	 * @throws ApplicationException
	 */
	public PhysicalLinkType getUnboundLinkType() throws ApplicationException {
		if (this.unboundLinkType == null) {
			this.unboundLinkType = LinkTypeController.getUnboundPhysicalLinkType();
		}
		return this.unboundLinkType;
	}

	/**
	 * ������� ��� ����� ��� �������� ����� �����.
	 * 
	 * @param type
	 *        ��� �����
	 */
	public void setCurrentPhysicalLinkType(final PhysicalLinkType type) {
		this.currentPhysicalLinkType = type;
	}

	/**
	 * �������� ���������� ����.
	 * 
	 * @return ���������� ����
	 */
	public MapViewController getMapViewController() {
		return this.mapViewController;
	}

	public void setMapViewController(final MapViewController mapViewController) {
		this.mapViewController = mapViewController;
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
	 * 
	 * @author $Author: krupenn $
	 * @version $Revision: 1.121 $, $Date: 2005/09/07 12:32:00 $
	 * @module mapviewclient_modifying
	 */
	private class VisualMapElement {
		public AbstractNode startNode = null;
		public AbstractNode endNode = null;
		public Color color = null;
		public Stroke stroke = null;
		public MapElement mapElement = null;

		public VisualMapElement(final AbstractNode startNode, final AbstractNode endNode) {
			this.startNode = startNode;
			this.endNode = endNode;
		}

		public VisualMapElement(final AbstractNode startNode, final AbstractNode endNode, final Color color, final Stroke stroke) {
			this.startNode = startNode;
			this.endNode = endNode;
			this.color = color;
			this.stroke = stroke;
		}

		public VisualMapElement(final MapElement mapElement, final Color color, final Stroke stroke) {
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
	 * ����� � �������� ������ �������, ����� ��������� ��������� � ��������� �
	 * ���� ����� ������������ �������
	 */
	private static final int MINIMUM_SCREEN_LENGTH = 20;

	private java.util.Map<AbstractNode, Set<NodeLink>> linksForNodes = new HashMap<AbstractNode, Set<NodeLink>>();

	/**
	 * ��������� ������ ��������� ������� ������ ������������ ��� �������
	 * ���������.
	 */
	public void calculateVisualElements() throws MapDataException, MapConnectionException {

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();
		MapViewController.nullTime6();
		MapViewController.nullTime7();
		MapViewController.nullTime8();

		final long startTime = System.currentTimeMillis();
		this.visualElements.clear();
		final long t1 = System.currentTimeMillis();

		final Map map = this.getMapView().getMap();

		final Set<NodeLink> allNodeLinks = new HashSet<NodeLink>(this.mapView.getMap().getAllNodeLinks());
		final Set<AbstractNode> allNodes = new HashSet<AbstractNode>(this.mapView.getMap().getNodes());
		final long t2 = System.currentTimeMillis();

		if (this.getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH) {
			for (final MeasurementPath measurementPath : this.mapView.getMeasurementPaths()) {
				final Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				final Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				this.fillOptimizationSets(measurementPath, allNodes, allNodeLinks, nodes, nodeLinks);

				final MeasurementPathController controller = (MeasurementPathController) this.getMapViewController().getController(measurementPath);
				this.calculateVisualElements(nodes,
						nodeLinks,
						controller.getColor(measurementPath),
						controller.getStroke(measurementPath),
						false);
			}
			this.calculateVisualElements(map.getNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		} else if (this.getMapState().getShowMode() == MapState.SHOW_CABLE_PATH) {
			for (final CablePath cablePath : this.mapView.getCablePaths()) {
				final Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				final Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				this.fillOptimizationSets(cablePath, allNodes, allNodeLinks, nodes, nodeLinks);

				final CableController controller = (CableController) this.getMapViewController().getController(cablePath);
				this.calculateVisualElements(nodes, nodeLinks, controller.getColor(cablePath), controller.getStroke(cablePath), false);
			}
			this.calculateVisualElements(map.getNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		} else if (this.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK) {
			this.calculateVisualElements(map.getNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		} else if (this.getMapState().getShowMode() == MapState.SHOW_NODE_LINK) {
			this.calculateVisualElements(map.getNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		
		this.visualElements.addAll(this.getMapView().getMarkers());
//		for(AbstractNode node : allNodes) {
//			this.visualElements.add(node);
//		}
		final long endTime = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.calculateVisualLinks | "
				+ "optimized map for " + (endTime - startTime) + "ms. Got " + this.visualElements.size() + " visual links.\n"
				+ "		" + (t1 - startTime) + " ms (visualElements.clear())\n"
				+ "		" + (t2 - t1) + " ms (getNodeLinks)\n"
				+ "		" + (endTime - t2) + " ms (recursing) divided to:\n"
				+ "			" + MapViewController.getTime1() + " ms (fillOptimizationSets)\n"
				+ "			" + MapViewController.getTime2() + " ms (searchLinksForNodes)\n"
				+ "			" + MapViewController.getTime3() + " ms (fill Calculated maps)\n"
				+ "			" + Long.toString(MapViewController.getTime4()) + " ms (create VisualElements)\n"
				+ "			" + MapViewController.getTime6() + " ns (getCharacteristics)\n"
				+ "			" + MapViewController.getTime5() + " ms (calculate distance)\n", Level.FINE);
	}

	private void fillOptimizationSets(final CablePath cablePath,
			final Set<AbstractNode> allNodes, 
			final Set<NodeLink> allNodeLinks,
			final Set<AbstractNode> nodes,
			final Set<NodeLink> nodeLinks) {

		final long t1 = System.currentTimeMillis();
		for (final PhysicalLink physicalLink : cablePath.getLinks()) {
			for (final NodeLink nodeLink : physicalLink.getNodeLinks()) {
				nodes.add(nodeLink.getStartNode());
				nodes.add(nodeLink.getEndNode());
				nodeLinks.add(nodeLink);
				allNodeLinks.remove(nodeLink);
				allNodes.remove(nodeLink.getStartNode());
				allNodes.remove(nodeLink.getEndNode());
			}
		}
		final long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	private void fillOptimizationSets(final MeasurementPath measurementPath,
			final Set<AbstractNode> allNodes, 
			final Set<NodeLink> allNodeLinks,
			final Set<AbstractNode> nodes,
			final Set<NodeLink> nodeLinks) {

		final long t1 = System.currentTimeMillis();
		for (final CablePath cablePath : measurementPath.getSortedCablePaths()) {
			for (final PhysicalLink physicalLink : cablePath.getLinks()) {
				for (final NodeLink nodeLink : physicalLink.getNodeLinks()) {
					nodes.add(nodeLink.getStartNode());
					nodes.add(nodeLink.getEndNode());
					nodeLinks.add(nodeLink);
					allNodeLinks.remove(nodeLink);
					allNodes.remove(nodeLink.getStartNode());
					allNodes.remove(nodeLink.getEndNode());
				}
			}
		}
		final long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	public void calculateVisualElements(final Set<AbstractNode> nodes,
			final Set<NodeLink> nodeLinks,
			final Color color,
			final Stroke stroke,
			final boolean pullAttributesFromController) throws MapDataException, MapConnectionException {

		this.searchLinksForNodes(nodeLinks);

		final long t1 = System.currentTimeMillis();
		// �������, � ������� ���������� ���������� � ���, ������������� ��
		// ��� ���� ��� ���
		final java.util.Map<AbstractNode, Boolean> nodesCalculated = new HashMap<AbstractNode, Boolean>(nodes.size());
		for (final AbstractNode node : nodes) {
			nodesCalculated.put(node, Boolean.FALSE);
		}

		final java.util.Map<NodeLink, Boolean> nodeLinksCalculated = new HashMap<NodeLink, Boolean>(nodeLinks.size());
		for (final NodeLink nodeLink : nodeLinks) {
			nodeLinksCalculated.put(nodeLink, Boolean.FALSE);
		}
		final long t2 = System.currentTimeMillis();
		MapViewController.addTime3(t2 - t1);

		// ��������� ��������.
		// ���� ��������� ����������� ���� ��� ��������� - �������� � ���� ��
		// ���������.
		// ���� ����� - ��� ������� ����, �� �������� ����� �������� ���� ���.
		final Collection<AbstractNode> frontedge = new LinkedList<AbstractNode>();
		for (final Iterator<AbstractNode> nodesIt = nodes.iterator(); nodesIt.hasNext();) {
			AbstractNode startRecursionNode = nodesIt.next();

			while (startRecursionNode != null) {
				if (!nodesCalculated.get(startRecursionNode).booleanValue()) {
					this.pullVisualLinksFromNode(startRecursionNode,
							startRecursionNode,
							null,
							nodesCalculated,
							nodeLinksCalculated,
							frontedge,
							color,
							stroke,
							pullAttributesFromController);
				}
				final Iterator<AbstractNode> it = frontedge.iterator();
				if (it.hasNext()) {
					startRecursionNode = it.next();
					it.remove();
				} else {
					startRecursionNode = null;
				}
			}
		}
	}

	/**
	 * ����������� ��������� ������ �������� ����� �����, �������� �����������
	 * "�����������" ����.
	 * 
	 * @param nodeToPullFrom
	 *        ����, �� �������� ������� ������� VisualMapElement.
	 * @param lastNode
	 *        ���������� ���� - �� ���� �� ������ � ������� ����
	 * @param incomingLink
	 *        ����, �� �������� �� ������ � ������ ����. ��� ������� ������
	 *        �������� = null.
	 * @param nodesCalculated
	 *        ������� ���������� ���������� � ���, ��������� �� ��� ��� ���� ����.
	 * @param nodeLinksCalculated
	 * @param frontedge
	 * @param stroke
	 * @param color
	 */
	private void pullVisualLinksFromNode(final AbstractNode nodeToPullFrom,
			final AbstractNode lastNode,
			final NodeLink incomingLink,
			final java.util.Map<AbstractNode, Boolean> nodesCalculated,
			final java.util.Map<NodeLink, Boolean> nodeLinksCalculated,
			final Collection<AbstractNode> frontedge,
			final Color color,
			final Stroke stroke,
			final boolean pullAttributesFromController) throws MapDataException, MapConnectionException {
		// ��������������� ����
		AbstractNode nodeProcessed = null;

		if (incomingLink == null) {
			nodeProcessed = lastNode;
		}
		else {
			nodeProcessed = incomingLink.getOtherNode(lastNode);
		}

		if(nodeProcessed == null){
			int a = 0;
			nodeProcessed = incomingLink.getOtherNode(lastNode);
		}

		assert nodeProcessed != null : "LogicalNetLayer | pullVisualLinksFromNode | The nodeProcessed can't be null";

		//�������� ������ ���� ��������/��������� ����� ��� ������� ����
		final Set<NodeLink> allLinksForNodeProcessed = this.linksForNodes.get(nodeProcessed);

		boolean hasUncalculatedLinks = false;
		if (allLinksForNodeProcessed != null) {
			for (final NodeLink outgoingLink : allLinksForNodeProcessed) {
				// ��������� �� ������ ����, �� �������� �� ������ � ������ ����
				if (!(outgoingLink == incomingLink) && !nodeLinksCalculated.get(outgoingLink).booleanValue()) {
					hasUncalculatedLinks = true;
					break;
				}
			}
		}

		if (allLinksForNodeProcessed == null || allLinksForNodeProcessed.size() == 0) {
			// ��� ��������� ���� (��� ������). �� ������������ ������.
			this.visualElements.add(nodeProcessed);
			// �������� � �������, ��� ���� ���������.
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
			return;
		}

		if (nodesCalculated.get(nodeProcessed).booleanValue()
				|| (allLinksForNodeProcessed.size() == 1 && allLinksForNodeProcessed.contains(incomingLink))
				|| !hasUncalculatedLinks) {
			// ������ ������� �������� - ������ ���� ��� �������������� ���
			//�������� �������� � ������� ��� ��� ��������� �����, ��������� 
			//�� ����, ��� �����������.
			//������ ������� ����������� � �������.

			//�������� � �������, ��� ���� ��������� - ��� ������ ���������
			//���� � �������.			
			nodesCalculated.put(lastNode, Boolean.TRUE);
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);

			VisualMapElement visualMapElement;
			final long t1 = System.currentTimeMillis();
			if (lastNode == nodeToPullFrom) {
				// ���� ��������� "������������" ������������ ������� ������� �� ������
				// ����� - ��� � ����������
				if (pullAttributesFromController) {
					final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(incomingLink);
					visualMapElement = new VisualMapElement(incomingLink,
							controller.getColor(incomingLink),
							controller.getStroke(incomingLink));
				} else {
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				}
			} else {
				visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
			}
			final long t2 = System.currentTimeMillis();
			MapViewController.addTime4(t2 - t1);

			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			return;
		}

		final long t5 = System.currentTimeMillis();
		// �������� ���������� ����, �� �������� �� ����� ���������� �������
		final Point startVENodeScr = this.converter.convertMapToScreen(nodeToPullFrom.getLocation());
		// �������� ���������� �������� ����
		final Point currEndVENodeScr = this.converter.convertMapToScreen(nodeProcessed.getLocation());
		final double distance = Math.sqrt((currEndVENodeScr.x - startVENodeScr.x)
				* (currEndVENodeScr.x - startVENodeScr.x)
				+ (currEndVENodeScr.y - startVENodeScr.y)
				* (currEndVENodeScr.y - startVENodeScr.y));
		final long t6 = System.currentTimeMillis();
		MapViewController.addTime5(t6 - t5);

		if (distance >= MINIMUM_SCREEN_LENGTH) {
			// ������ ������� �������� - �������� ������� ������� �����������
			// ����� - �������� ���������������, �� �������������
			// ���� ������� �������� �� ����� �����.

			VisualMapElement visualMapElement;
			AbstractNode lastVMENode;
			if (lastNode == nodeToPullFrom) {
				final long t1 = System.currentTimeMillis();
				// ���� "������������" ������������ ������� ������� �� ������
				// ����� - ��� � ����������
				if (pullAttributesFromController) {
					final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(incomingLink);
					visualMapElement = new VisualMapElement(incomingLink,
							controller.getColor(incomingLink),
							controller.getStroke(incomingLink));
				} else {
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				}
				final long t2 = System.currentTimeMillis();
				MapViewController.addTime4(t2 - t1);
				lastVMENode = nodeProcessed;
				nodesCalculated.put(lastNode, Boolean.TRUE);
				nodeLinksCalculated.put(incomingLink, Boolean.TRUE);
			}
			else {
				//� �������� ������ - ������ ������������ ������� ��
				//����������� ����

				//���� � ������ ����� �������� ����������� ���� ���� ������� - �� ������ ��� � ������
				NodeLink linkBetween = null;
				for (final NodeLink outLink : this.linksForNodes.get(nodeToPullFrom)) {
					if (outLink.getStartNode() == lastNode || outLink.getEndNode() == lastNode) {
						linkBetween = outLink;
						break;
					}
				}

				final long t1 = System.currentTimeMillis();
				if (linkBetween != null) {
					if (pullAttributesFromController) {
						final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(linkBetween);
						visualMapElement = new VisualMapElement(linkBetween,
								controller.getColor(linkBetween),
								controller.getStroke(linkBetween));
					} else {
						visualMapElement = new VisualMapElement(linkBetween, color, stroke);
					}
				} else {
					visualMapElement = new VisualMapElement(nodeToPullFrom, lastNode, color, stroke);
				}
				final long t2 = System.currentTimeMillis();
				MapViewController.addTime4(t2 - t1);
				lastVMENode = lastNode;
			}

			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(lastVMENode);

			frontedge.add(lastVMENode);
			return;
		}

		// ���������� ��� - ���� �� ����� ���� � SiteNode
		SiteNodeType nodeType = null;
		if (nodeProcessed instanceof SiteNode) {
			final SiteNode sNode = (SiteNode) nodeProcessed;
			nodeType = sNode.getType();
		}

		if ((incomingLink != null)
				&& ((allLinksForNodeProcessed.size() > 2) || ((nodeType != null) && (nodeType.getSort().value() == SiteNodeTypeSort._BUILDING
						|| nodeType.getSort().value() == SiteNodeTypeSort._UNBOUND || nodeType.getSort().value() == SiteNodeTypeSort._ATS)))) {
			// ������ ������� �������� - ���������� �� ���� � ��������� ���
			// ���� ����, �� ����������� �����������, - �������� ���������������,
			// �� ��������� ���� ������� �������� �� ����� �����.


				VisualMapElement visualMapElement;
				final long t1 = System.currentTimeMillis();
			if (lastNode == nodeToPullFrom) {
				// ���� ��������� "������������" ������������ ������� ������� �� ������
				// ����� - ��� � ����������
				if (pullAttributesFromController) {
					final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(incomingLink);
					visualMapElement = new VisualMapElement(incomingLink,
							controller.getColor(incomingLink),
							controller.getStroke(incomingLink));
				} else {
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				}
			} else {
				visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
			}
			final long t2 = System.currentTimeMillis();
			MapViewController.addTime4(t2 - t1);

			nodesCalculated.put(lastNode, Boolean.TRUE);
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);

			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			frontedge.add(nodeProcessed);
			return;
		}

		nodesCalculated.put(lastNode, Boolean.TRUE);
		if (incomingLink != null) {
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);
		}

		//����� ������� ����������� ������. ������ ��������� ���������� ����
		//(������ ������� ��������). ������� ����� ������������ ������ �� �����
		//����� ��������� ��������.
		for (final NodeLink outgoingLink : allLinksForNodeProcessed) {
			// ��������� �� ������ ����, �� �������� �� ������ � ������ ����
			if (!(outgoingLink == incomingLink) && !nodeLinksCalculated.get(outgoingLink).booleanValue()) {
				this.pullVisualLinksFromNode(nodeToPullFrom,
						nodeProcessed,
						outgoingLink,
						nodesCalculated,
						nodeLinksCalculated,
						frontedge,
						color,
						stroke,
						pullAttributesFromController);
			}
		}
	}

	/**
	 * ������������ �������� �����������, ������������� � �������� ��������
	 * 
	 * @param g
	 * @param visibleBounds
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void drawVisualElements(final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		final long t1 = System.currentTimeMillis();

		// ���� ����� ������ nodeLink �� ��������, �� ������� ����� ������
		// physicalLink
		if (!this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK)) {
			final Command modeCommand = this.getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		for (final Object veElement : this.visualElements) {
			if (veElement instanceof SiteNode) {
				continue;
			}

			if (veElement instanceof VisualMapElement) {
				final VisualMapElement vme = (VisualMapElement) veElement;
				if (vme.mapElement != null) {
					if (vme.mapElement instanceof NodeLink) {
						final NodeLink nodeLink = (NodeLink) vme.mapElement;
						final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(nodeLink);
						if (this.getMapState().getShowMode() == MapState.SHOW_NODE_LINK) {
							controller.paint(nodeLink, g, visibleBounds);
						}
						else {
							final boolean selectionVisible = false;
							controller.paint(nodeLink, g, visibleBounds, vme.stroke, vme.color, selectionVisible);
						}
					}
					continue;
				}

				final DoublePoint startLocation = vme.startNode.getLocation();
				final DoublePoint endLocation = vme.endNode.getLocation();
				if (visibleBounds.intersectsLine(startLocation.getX(), startLocation.getY(), endLocation.getX(), endLocation.getY())) {
					final Point from = this.converter.convertMapToScreen(startLocation);
					final Point to = this.converter.convertMapToScreen(endLocation);
					g.setColor(vme.color);
					((Graphics2D) g).setStroke(vme.stroke);
					g.drawLine(from.x, from.y, to.x, to.y);
				}
			}
		}
		for (final Object veElement : this.visualElements) {
			if (veElement instanceof AbstractNode) {
				this.getMapViewController().getController((AbstractNode) veElement).paint((AbstractNode) veElement, g, visibleBounds);
			}
		}
		final long t2 = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawVisualElements | " + String.valueOf(t2 - t1) + " ms\n", Level.FINE);
	}

	private void searchLinksForNodes(final Set<NodeLink> nodeLinks) {
		final long t1 = System.currentTimeMillis();

		final Collection<Set<NodeLink>> emptySets = this.linksForNodes.values();
		for (final Set set : emptySets) {
			set.clear();
		}

		for (final NodeLink link : nodeLinks) {
			final AbstractNode startNode = link.getStartNode();
			Set<NodeLink> startNodeLinksSet = this.linksForNodes.get(startNode);
			if (startNodeLinksSet == null) {
				startNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(startNode, startNodeLinksSet);
			}
			startNodeLinksSet.add(link);

			final AbstractNode endNode = link.getEndNode();
			Set<NodeLink> endNodeLinksSet = this.linksForNodes.get(endNode);
			if (endNodeLinksSet == null) {
				endNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(endNode, endNodeLinksSet);
			}
			endNodeLinksSet.add(link);
		}

		final long t2 = System.currentTimeMillis();
		MapViewController.addTime2(t2 - t1);
	}
}
