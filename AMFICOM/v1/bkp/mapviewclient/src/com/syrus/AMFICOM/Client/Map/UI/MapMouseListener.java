/**
 * $Id: MapMouseListener.java,v 1.14 2004/12/01 10:55:32 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * Обработчик мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если режим нулевой (NO_OPERATION),
 * то обработка события передается текущему активному элементу карты
 * (посредством объекта MapStrategy)
 * 
 * 
 * 
 * @version $Revision: 1.14 $, $Date: 2004/12/01 10:55:32 $
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
							VoidMapElement.getInstance(
								logicalNetLayer.getMapView() ) );
					}
					break;
				case MapState.NODELINK_SIZE_EDIT:
					// fall throuth
				case MapState.NO_OPERATION:
					if(logicalNetLayer.getCurrentMapElement() != null)
						if(logicalNetLayer.getCurrentMapElement() instanceof MapNodeElement)
							if(mapState.getShowMode() == MapState.SHOW_NODE_LINK)
						{
							MapNodeElement node = (MapNodeElement )logicalNetLayer.getCurrentMapElement();
							MapNodeLinkElement nodelink = logicalNetLayer.getEditedNodeLink(me.getPoint());
							if(nodelink != null)
								if(nodelink.getStartNode() == node
									|| nodelink.getEndNode() == node)
							{
								if(this.sizeEditBox != null)
									if(this.sizeEditBox.isVisible())
										return;
								sizeEditBox = new MapNodeLinkSizeField(logicalNetLayer, nodelink, node);
								Rectangle rect = nodelink.getLabelBox();
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
					if(curElement instanceof MapSelection)
					{
						mapElement = curElement;
					}
					else
					if(!(curElement instanceof VoidMapElement) 
						&& curElement.isMouseOnThisObject(me.getPoint()))
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
							contextMenu.setMapElement(mapElement);
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

					Point2D.Double sp = logicalNetLayer.convertScreenToMap(logicalNetLayer.getStartPoint());
					Point2D.Double ep = logicalNetLayer.convertScreenToMap(me.getPoint());

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
								+ MapPropertiesManager.getDistanceFormat().format(distance),
							LangModelMap.getString("MeasureDistance"),
							JOptionPane.PLAIN_MESSAGE);

					logicalNetLayer.repaint(false);
					break;
				case MapState.ZOOM_TO_POINT :
					Point2D.Double pp = logicalNetLayer.convertScreenToMap(me.getPoint());

					logicalNetLayer.setCenter(pp);
					logicalNetLayer.zoomIn();
					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					logicalNetLayer.getContext().getApplicationModel().setSelected(
							MapApplicationModel.OPERATION_ZOOM_TO_POINT, 
							false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

					logicalNetLayer.getMapView().setScale(logicalNetLayer.getScale());
					logicalNetLayer.getMapView().setCenter(logicalNetLayer.getCenter());
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
					break;
				case MapState.MOVE_HAND:
					Point2D.Double center = logicalNetLayer.getCenter();

					Point2D.Double p1 = logicalNetLayer.convertScreenToMap(logicalNetLayer.getStartPoint());
					Point2D.Double p2 = logicalNetLayer.convertScreenToMap(me.getPoint());
					double dx = p1.x - p2.x;
					double dy = p1.y - p2.y;

					center.x += dx;
					center.y += dy;

					logicalNetLayer.setCenter(center);

					logicalNetLayer.getMapView().setCenter(
							logicalNetLayer.getCenter());
					break;
				case MapState.MOVE_FIXDIST:
					// fall through
				case MapState.NODELINK_SIZE_EDIT:
					// fall throuth
				case MapState.NO_OPERATION:

					if(!logicalNetLayer.isMenuShown())
					{
						// Контекстное меню показывать не надо и передеём управление 
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
