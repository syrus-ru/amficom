/**
 * $Id: MapMouseListener.java,v 1.23 2005/02/18 12:19:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Map.Popup.MapPopupMenu;
import com.syrus.AMFICOM.Client.Map.Popup.MapPopupMenuManager;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategyManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;

/**
 * Обработчик мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если режим нулевой (NO_OPERATION),
 * то обработка события передается текущему активному элементу карты
 * (посредством объекта MapStrategy)
 * @version $Revision: 1.23 $, $Date: 2005/02/18 12:19:47 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapMouseListener implements MouseListener
{
	LogicalNetLayer logicalNetLayer;

	protected MapNodeLinkSizeField sizeEditBox = null;

	public MapMouseListener(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void mouseClicked(MouseEvent me)
	{
		if(me.getClickCount() == 2
			&& SwingUtilities.isLeftMouseButton(me))
		{
			// show properties on double click
			if ( this.logicalNetLayer.getMapView() != null)
			{
				showContextMenu();
			}
		}
	}

	/**
	 * 
	 */
	private void showContextMenu() {
		MapElement mapElement = this.logicalNetLayer.getCurrentMapElement();
		if(mapElement != null)
		{
				MapPopupMenu contextMenu = MapPopupMenuManager.getPopupMenu(mapElement);
				if(contextMenu != null)
				{
					contextMenu.setLogicalNetLayer(this.logicalNetLayer);
					contextMenu.showProperties(mapElement);
				}
		}
	}

	public void mousePressed(MouseEvent me)
	{
		MapState mapState = this.logicalNetLayer.getMapState();
		
		mapState.setMouseMode(MapState.MOUSE_PRESSED);//Установить режим

//		System.out.println("Pressed at (" + me.getPoint().x + ", " + me.getPoint().y + ")");

		if ( this.logicalNetLayer.getMapView() != null)
		{
			Point point = me.getPoint();
			this.logicalNetLayer.setStartPoint(point);//Установить начальную точку
			this.logicalNetLayer.setEndPoint(point);//Установить конечную точку
			switch ( mapState.getOperationMode())
			{
				case MapState.MOVE_HAND://Флаг для меремещения карты лапкой
					// fall throuth
				case MapState.MEASURE_DISTANCE:
					// fall throuth
				case MapState.ZOOM_TO_POINT:
					// fall throuth
				case MapState.ZOOM_TO_RECT:
					// fall throuth
				case MapState.MOVE_TO_CENTER:
					//Берём фокус
					this.logicalNetLayer.getMapViewer().getVisualComponent().grabFocus();
					break;
				case MapState.MOVE_FIXDIST:
					try {
						moveFixedDistance(point);
					} catch(MapConnectionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch(MapDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case MapState.NODELINK_SIZE_EDIT:
					// fall throuth
				case MapState.NO_OPERATION:
					boolean proceed = checkNodeSizeEdit(mapState, point);
					
					if(!proceed)
						break;

					try {
						defaultAction(me);
					} catch(MapConnectionException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch(MapDataException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					break;
				default:
					try
					{
						System.out.println("unknown map operation: " + mapState.getOperationMode());
						throw new Exception("dummy");
					}
					catch(Exception e)
					{
						Environment.log(Environment.LOG_LEVEL_FINER, "current execution point with call stack:", null, null, e);
					}
					break;
			}//switch (mapState.getOperationMode()
		}
		try {
			this.logicalNetLayer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	/**
	 * @param me
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void defaultAction(MouseEvent me) throws MapConnectionException, MapDataException {
		Point point = me.getPoint();
		//Берём фокус
		this.logicalNetLayer.getMapViewer().getVisualComponent().grabFocus();
		MapElement mapElement = this.logicalNetLayer.getMapElementAtPoint(point);
		MapElement curElement = this.logicalNetLayer.getCurrentMapElement();
		MapElementController mec = this.logicalNetLayer.getMapViewController().getController(curElement);
		if(curElement instanceof Selection)
		{
			mapElement = curElement;
		}
		else
		if(!(curElement instanceof VoidElement) 
			&& mec.isMouseOnElement(curElement, point))
		{
			mapElement = curElement;
		}
		else
		{
			this.logicalNetLayer.setCurrentMapElement(mapElement);
		}
		if (SwingUtilities.isLeftMouseButton(me))
		{
			MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
			if(strategy != null)
			{
				strategy.setLogicalNetLayer(this.logicalNetLayer);
				strategy.doContextChanges(me);
			}

			mapElement = this.logicalNetLayer.getCurrentMapElement();

			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		}
		else
		if (SwingUtilities.isRightMouseButton(me))
		{
			MapPopupMenu contextMenu;
			
			contextMenu = MapPopupMenuManager.getPopupMenu(mapElement);
			if(contextMenu != null)
			{
				contextMenu.setLogicalNetLayer(this.logicalNetLayer);
				contextMenu.setElement(mapElement);
				contextMenu.setPoint(point);
				contextMenu.show(
					this.logicalNetLayer.getMapViewer().getVisualComponent(), 
					point.x, 
					point.y);
				this.logicalNetLayer.setMenuShown(true);
			}
		}
	}

	/**
	 * @param mapState
	 * @param point
	 */
	private boolean checkNodeSizeEdit(MapState mapState, Point point) {
		boolean proceed = true;
		if(this.logicalNetLayer.getCurrentMapElement() != null)
			if(this.logicalNetLayer.getCurrentMapElement() instanceof AbstractNode)
				if(mapState.getShowMode() == MapState.SHOW_NODE_LINK)
			{
				AbstractNode node = (AbstractNode)this.logicalNetLayer.getCurrentMapElement();
				NodeLink nodelink = this.logicalNetLayer.getEditedNodeLink(point);
				if(nodelink != null)
					if(nodelink.getStartNode().equals(node)
						|| nodelink.getEndNode().equals(node))
				{
					if(this.sizeEditBox != null)
						if(this.sizeEditBox.isVisible())
							proceed = false;
					this.sizeEditBox = new MapNodeLinkSizeField(this.logicalNetLayer, nodelink, node);
					NodeLinkController nlc = (NodeLinkController)this.logicalNetLayer.getMapViewController().getController(nodelink);
					Rectangle rect = nlc.getLabelBox(nodelink);
					this.sizeEditBox.setBounds(rect.x, rect.y, rect.width + 3, rect.height + 3);
					this.sizeEditBox.setText(MapPropertiesManager.getDistanceFormat().format(nodelink.getLengthLt()));
					this.sizeEditBox.setSelectionStart(0);
					this.sizeEditBox.setSelectionEnd(this.sizeEditBox.getText().length());
					this.sizeEditBox.selectAll();
					this.logicalNetLayer.getMapViewer().getVisualComponent().add(this.sizeEditBox);
					this.sizeEditBox.setVisible(true);
					this.sizeEditBox.setEditable(true);
					this.sizeEditBox.grabFocus();
					
					mapState.setMouseMode(MapState.MOUSE_NONE);
					mapState.setOperationMode(MapState.NODELINK_SIZE_EDIT);

					proceed = false;
				}
			}
		return proceed;
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void moveFixedDistance(Point point) throws MapConnectionException, MapDataException {
		this.logicalNetLayer.deselectAll();
		this.logicalNetLayer.getFixedNode().setSelected(true);
		MapElement mel = this.logicalNetLayer.getMapElementAtPoint(point);
		if(this.logicalNetLayer.getFixedNodeList().contains(mel))
		{
			mel.setSelected(true);
			this.logicalNetLayer.setCurrentMapElement(mel);
		}
		else
		{
			this.logicalNetLayer.setCurrentMapElement(
				com.syrus.AMFICOM.mapview.VoidElement.getInstance(
					this.logicalNetLayer.getMapView() ) );
		}
	}

	public void mouseEntered(MouseEvent me)
	{//empty
	}

	public void mouseExited(MouseEvent me)
	{//empty
	}

	public void mouseReleased(MouseEvent me)
	{
		MapState mapState = this.logicalNetLayer.getMapState();

		if(this.sizeEditBox != null)
			if(this.sizeEditBox.isVisible())
				return;

		mapState.setMouseMode(MapState.MOUSE_RELEASED);
		if ( this.logicalNetLayer.getMapView() != null)
		{
			try {
				//Обрабатывает события на панели инстрементов
				Point point = me.getPoint();
				switch (mapState.getOperationMode())
				{
					case MapState.MEASURE_DISTANCE :
						finishMeasureDistance(point);
						break;
					case MapState.ZOOM_TO_POINT :
						finishZoomToPoint(point);
						break;
					case MapState.ZOOM_TO_RECT:
						finishZoomToRect();
						break;
					case MapState.MOVE_TO_CENTER:
						finishMoveToCenter(point);
						break;
					case MapState.MOVE_HAND:
						finishMoveHand(point);
						break;
					case MapState.MOVE_FIXDIST:
						// fall through
					case MapState.NODELINK_SIZE_EDIT:
						// fall throuth
					case MapState.NO_OPERATION:
						finishDefaultAction(me);
						break;
					default:
						try
						{
							System.out.println("unknown map operation: " + mapState.getOperationMode());
							throw new Exception("dummy");
						}
						catch(Exception e)
						{
							Environment.log(Environment.LOG_LEVEL_FINER, "current execution point with call stack:", null, null, e);
						}
						break;
				}//switch (mapState.getOperationMode()
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(mapState.getOperationMode() != MapState.MOVE_HAND
				&& mapState.getOperationMode() != MapState.MOVE_FIXDIST)
				mapState.setOperationMode(MapState.NO_OPERATION);

			//Убираем флаг
			this.logicalNetLayer.setMenuShown(false);
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);//Для мыши
	}

	/**
	 * @param me
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishDefaultAction(MouseEvent me) throws MapConnectionException, MapDataException {
		if(!this.logicalNetLayer.isMenuShown())
		{
			// Контекстное меню показывать не надо и передаём управление 
			// стратегии текущего объекта
			MapElement mapElement = this.logicalNetLayer.getCurrentMapElement();

			MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
			if(strategy != null)
			{
				strategy.setLogicalNetLayer(this.logicalNetLayer);
				strategy.doContextChanges(me);
			}

			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.SELECTION_CHANGED));
		}
		//					mapState.setActionMode(MapState.NULL_ACTION_MODE);
		this.logicalNetLayer.repaint(false);
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishMoveHand(Point point) throws MapConnectionException, MapDataException {
		DoublePoint center = this.logicalNetLayer.getCenter();
		DoublePoint p1 = this.logicalNetLayer.convertScreenToMap(this.logicalNetLayer.getStartPoint());
		DoublePoint p2 = this.logicalNetLayer.convertScreenToMap(point);
		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY();
		center.setLocation(center.getX() + dx, center.getY() + dy);
		this.logicalNetLayer.setCenter(center);
		this.logicalNetLayer.getMapView().setCenter(
				this.logicalNetLayer.getCenter());
		this.logicalNetLayer.repaint(true);
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishMoveToCenter(Point point) throws MapConnectionException, MapDataException {
		this.logicalNetLayer.setCenter(
			this.logicalNetLayer.convertScreenToMap(point));
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_MOVE_TO_CENTER, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
		this.logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.logicalNetLayer.getMapView().setCenter(
				this.logicalNetLayer.getCenter());
		this.logicalNetLayer.repaint(true);
	}

	/**
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishZoomToRect() throws MapConnectionException, MapDataException {
		this.logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_BOX, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
		if (!this.logicalNetLayer.getStartPoint().equals(this.logicalNetLayer.getEndPoint()))
		{
			this.logicalNetLayer.zoomToBox(
					this.logicalNetLayer.convertScreenToMap(this.logicalNetLayer.getStartPoint()),
					this.logicalNetLayer.convertScreenToMap(this.logicalNetLayer.getEndPoint()));

			this.logicalNetLayer.getMapView().setScale(
				this.logicalNetLayer.getScale());
			this.logicalNetLayer.getMapView().setCenter(
				this.logicalNetLayer.getCenter());
			this.logicalNetLayer.repaint(true);
		}
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishZoomToPoint(Point point) throws MapConnectionException, MapDataException {
		DoublePoint pp = this.logicalNetLayer.convertScreenToMap(point);
		this.logicalNetLayer.setCenter(pp);
		this.logicalNetLayer.zoomIn();
		this.logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_TO_POINT, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
		this.logicalNetLayer.getMapView().setScale(this.logicalNetLayer.getScale());
		this.logicalNetLayer.getMapView().setCenter(this.logicalNetLayer.getCenter());
		this.logicalNetLayer.repaint(true);
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishMeasureDistance(Point point) throws MapConnectionException, MapDataException {
		DoublePoint sp = this.logicalNetLayer.convertScreenToMap(this.logicalNetLayer.getStartPoint());
		DoublePoint ep = this.logicalNetLayer.convertScreenToMap(point);
		double distance = this.logicalNetLayer.distance(sp, ep);
		this.logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_MEASURE_DISTANCE, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelMap.getString("Distance") 
					+ " = " 
					+ MapPropertiesManager.getDistanceFormat().format(distance)
					+ MapPropertiesManager.getMetric(),
				LangModelMap.getString("MeasureDistance"),
				JOptionPane.PLAIN_MESSAGE);
		this.logicalNetLayer.repaint(false);
	}
}
