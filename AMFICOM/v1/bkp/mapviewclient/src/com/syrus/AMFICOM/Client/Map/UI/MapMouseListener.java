/**
 * $Id: MapMouseListener.java,v 1.21 2005/02/01 13:29:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.Popup.MapPopupMenu;
import com.syrus.AMFICOM.Client.Map.Popup.MapPopupMenuManager;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategyManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * Обработчик мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если режим нулевой (NO_OPERATION),
 * то обработка события передается текущему активному элементу карты
 * (посредством объекта MapStrategy)
 * 
 * 
 * 
 * @version $Revision: 1.21 $, $Date: 2005/02/01 13:29:57 $
 * @module
 * @author $Author: krupenn $
 * @see
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
			if ( logicalNetLayer.getMapView() != null)
			{
				MapElement mapElement = logicalNetLayer.getCurrentMapElement();
				if(mapElement != null)
				{
						MapPopupMenu contextMenu = MapPopupMenuManager.getPopupMenu(mapElement);
						if(contextMenu != null)
						{
							contextMenu.setLogicalNetLayer(logicalNetLayer);
							contextMenu.showProperties(mapElement);
						}
				}
			}
		}
	}

	public void mousePressed(MouseEvent me)
	{
		MapState mapState = logicalNetLayer.getMapState();
		
		mapState.setMouseMode(MapState.MOUSE_PRESSED);//Установить режим

//		System.out.println("Pressed at (" + me.getPoint().x + ", " + me.getPoint().y + ")");

		if ( logicalNetLayer.getMapView() != null)
		{
			logicalNetLayer.setStartPoint(me.getPoint());//Установить начальную точку
			logicalNetLayer.setEndPoint(me.getPoint());//Установить конечную точку
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
					logicalNetLayer.getMapViewer().getVisualComponent().grabFocus();
					break;
				case MapState.MOVE_FIXDIST:
					logicalNetLayer.deselectAll();
					logicalNetLayer.getFixedNode().setSelected(true);
					MapElement mel = logicalNetLayer.getMapElementAtPoint(me.getPoint());
					if(logicalNetLayer.getFixedNodeList().contains(mel))
					{
						mel.setSelected(true);
						logicalNetLayer.setCurrentMapElement(mel);
					}
					else
					{
						logicalNetLayer.setCurrentMapElement(
							com.syrus.AMFICOM.mapview.VoidElement.getInstance(
								logicalNetLayer.getMapView() ) );
					}
					break;
				case MapState.NODELINK_SIZE_EDIT:
					// fall throuth
				case MapState.NO_OPERATION:
					if(logicalNetLayer.getCurrentMapElement() != null)
						if(logicalNetLayer.getCurrentMapElement() instanceof AbstractNode)
							if(mapState.getShowMode() == MapState.SHOW_NODE_LINK)
						{
							AbstractNode node = (AbstractNode)logicalNetLayer.getCurrentMapElement();
							NodeLink nodelink = logicalNetLayer.getEditedNodeLink(me.getPoint());
							if(nodelink != null)
								if(nodelink.getStartNode().equals(node)
									|| nodelink.getEndNode().equals(node))
							{
								if(this.sizeEditBox != null)
									if(this.sizeEditBox.isVisible())
										return;
								sizeEditBox = new MapNodeLinkSizeField(logicalNetLayer, nodelink, node);
								NodeLinkController nlc = (NodeLinkController)logicalNetLayer.getMapViewController().getController(nodelink);
								Rectangle rect = nlc.getLabelBox(nodelink);
								sizeEditBox.setBounds(rect.x, rect.y, rect.width + 3, rect.height + 3);
								sizeEditBox.setText(MapPropertiesManager.getDistanceFormat().format(nodelink.getLengthLt()));
								sizeEditBox.setSelectionStart(0);
								sizeEditBox.setSelectionEnd(sizeEditBox.getText().length());
								sizeEditBox.selectAll();
								logicalNetLayer.getMapViewer().getVisualComponent().add(sizeEditBox);
								sizeEditBox.setVisible(true);
								sizeEditBox.setEditable(true);
								sizeEditBox.grabFocus();
								
								mapState.setMouseMode(MapState.MOUSE_NONE);
								mapState.setOperationMode(MapState.NODELINK_SIZE_EDIT);

								return;
							}
						}

					//Берём фокус
					logicalNetLayer.getMapViewer().getVisualComponent().grabFocus();

					MapElement mapElement = logicalNetLayer.getMapElementAtPoint(me.getPoint());
					MapElement curElement = logicalNetLayer.getCurrentMapElement();
					MapElementController mec = logicalNetLayer.getMapViewController().getController(curElement);
					if(curElement instanceof Selection)
					{
						mapElement = curElement;
					}
					else
					if(!(curElement instanceof VoidElement) 
						&& mec.isMouseOnElement(curElement, me.getPoint()))
					{
						mapElement = curElement;
					}
					else
					{
						logicalNetLayer.setCurrentMapElement(mapElement);
					}

					if (SwingUtilities.isLeftMouseButton(me))
					{
						MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
						if(strategy != null)
						{
							strategy.setLogicalNetLayer(logicalNetLayer);
							strategy.doContextChanges(me);
						}
	
						mapElement = logicalNetLayer.getCurrentMapElement();

						logicalNetLayer.sendMapEvent(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					}
					else
					if (SwingUtilities.isRightMouseButton(me))
					{
						MapPopupMenu contextMenu;
						
						contextMenu = MapPopupMenuManager.getPopupMenu(mapElement);
						if(contextMenu != null)
						{
							contextMenu.setLogicalNetLayer(logicalNetLayer);
							contextMenu.setElement(mapElement);
							contextMenu.setPoint(me.getPoint());
							JPopupMenu popup = (JPopupMenu )contextMenu;
							popup.show(
								logicalNetLayer.getMapViewer().getVisualComponent(), 
								me.getPoint().x, 
								me.getPoint().y);
							logicalNetLayer.setMenuShown(true);
						}
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
		logicalNetLayer.repaint(false);
		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	public void mouseEntered(MouseEvent me)
	{
	}

	public void mouseExited(MouseEvent me)
	{
	}

	public void mouseReleased(MouseEvent me)
	{
		MapState mapState = logicalNetLayer.getMapState();

		if(this.sizeEditBox != null)
			if(this.sizeEditBox.isVisible())
				return;

//		System.out.println("Released at (" + me.getPoint().x + ", " + me.getPoint().y + ")");

		mapState.setMouseMode(MapState.MOUSE_RELEASED);
		if ( logicalNetLayer.getMapView() != null)
		{
			//Обрабатывает события на панели инстрементов
			switch (mapState.getOperationMode())
			{
				case MapState.MEASURE_DISTANCE :

					DoublePoint sp = logicalNetLayer.convertScreenToMap(logicalNetLayer.getStartPoint());
					DoublePoint ep = logicalNetLayer.convertScreenToMap(me.getPoint());

					double distance = logicalNetLayer.distance(sp, ep);

					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					logicalNetLayer.getContext().getApplicationModel().setSelected(
							MapApplicationModel.OPERATION_MEASURE_DISTANCE, 
							false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelMap.getString("Distance") 
								+ " = " 
								+ MapPropertiesManager.getDistanceFormat().format(distance)
								+ MapPropertiesManager.getMetric(),
							LangModelMap.getString("MeasureDistance"),
							JOptionPane.PLAIN_MESSAGE);

					logicalNetLayer.repaint(false);
					break;
				case MapState.ZOOM_TO_POINT :
					DoublePoint pp = logicalNetLayer.convertScreenToMap(me.getPoint());

					logicalNetLayer.setCenter(pp);
					logicalNetLayer.zoomIn();
					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					logicalNetLayer.getContext().getApplicationModel().setSelected(
							MapApplicationModel.OPERATION_ZOOM_TO_POINT, 
							false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

					logicalNetLayer.getMapView().setScale(logicalNetLayer.getScale());
					logicalNetLayer.getMapView().setCenter(logicalNetLayer.getCenter());
					logicalNetLayer.repaint(true);
					break;
				case MapState.ZOOM_TO_RECT:
					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					
					logicalNetLayer.getContext().getApplicationModel().setSelected(
							MapApplicationModel.OPERATION_ZOOM_BOX, 
							false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

					if (!logicalNetLayer.getStartPoint().equals(logicalNetLayer.getEndPoint()))
					{
						logicalNetLayer.zoomToBox(
								logicalNetLayer.convertScreenToMap(logicalNetLayer.getStartPoint()),
								logicalNetLayer.convertScreenToMap(logicalNetLayer.getEndPoint()));

						logicalNetLayer.getMapView().setScale(
							logicalNetLayer.getScale());
						logicalNetLayer.getMapView().setCenter(
							logicalNetLayer.getCenter());
						logicalNetLayer.repaint(true);
					}
					break;
				case MapState.MOVE_TO_CENTER:
					logicalNetLayer.setCenter(
						logicalNetLayer.convertScreenToMap(me.getPoint()));
					
					logicalNetLayer.getContext().getApplicationModel().setSelected(
							MapApplicationModel.OPERATION_MOVE_TO_CENTER, 
							false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					logicalNetLayer.getMapView().setCenter(
							logicalNetLayer.getCenter());
					logicalNetLayer.repaint(true);
					break;
				case MapState.MOVE_HAND:
					DoublePoint center = logicalNetLayer.getCenter();

					DoublePoint p1 = logicalNetLayer.convertScreenToMap(logicalNetLayer.getStartPoint());
					DoublePoint p2 = logicalNetLayer.convertScreenToMap(me.getPoint());
					double dx = p1.getX() - p2.getX();
					double dy = p1.getY() - p2.getY();

					center.setLocation(center.getX() + dx, center.getY() + dy);

					logicalNetLayer.setCenter(center);

					logicalNetLayer.getMapView().setCenter(
							logicalNetLayer.getCenter());
					logicalNetLayer.repaint(true);
					break;
				case MapState.MOVE_FIXDIST:
					// fall through
				case MapState.NODELINK_SIZE_EDIT:
					// fall throuth
				case MapState.NO_OPERATION:

					if(!logicalNetLayer.isMenuShown())
					{
						// Контекстное меню показывать не надо и передаём управление 
						// стратегии текущего объекта
						MapElement mapElement = logicalNetLayer.getCurrentMapElement();
		
						MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
						if(strategy != null)
						{
							strategy.setLogicalNetLayer(logicalNetLayer);
							strategy.doContextChanges(me);
						}
		
						logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.SELECTION_CHANGED));
					}
//					mapState.setActionMode(MapState.NULL_ACTION_MODE);
					logicalNetLayer.repaint(false);
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
			if(mapState.getOperationMode() != MapState.MOVE_HAND
				&& mapState.getOperationMode() != MapState.MOVE_FIXDIST)
				mapState.setOperationMode(MapState.NO_OPERATION);

			//Убираем флаг
			logicalNetLayer.setMenuShown(false);
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);//Для мыши
	}
}
