/**
 * $Id: MapMouseListener.java,v 1.5 2004/09/18 14:12:04 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Command.Command;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.Popup.MapPopupMenu;
import com.syrus.AMFICOM.Client.Map.Popup.MapPopupMenuManager;
import com.syrus.AMFICOM.Client.Map.Popup.SelectionPopupMenu;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategyManager;
import com.syrus.AMFICOM.Client.Map.UI.MapNodeLinkSizeField;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;

import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import java.util.List;

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
 * @version $Revision: 1.5 $, $Date: 2004/09/18 14:12:04 $
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
		//Берём фокус
		logicalNetLayer.getMapViewer().getVisualComponent().grabFocus();
		MapState mapState = logicalNetLayer.getMapState();
		
		mapState.setMouseMode(MapState.MOUSE_PRESSED);//Установить режим

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
					break;

				case MapState.NODELINK_SIZE_EDIT:
					// fall throuth
				case MapState.NO_OPERATION:
					if(logicalNetLayer.getCurrentMapElement() != null)
						if(logicalNetLayer.getCurrentMapElement() instanceof MapNodeElement)
							if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
						{
							MapNodeElement node = (MapNodeElement )logicalNetLayer.getCurrentMapElement();
							MapNodeLinkElement nodelink = logicalNetLayer.getEditedNodeLink(me.getPoint());
							if(nodelink != null)
								if(nodelink.getStartNode() == node
									|| nodelink.getEndNode() == node)
							{
								sizeEditBox = new MapNodeLinkSizeField(logicalNetLayer, nodelink, node);
								Rectangle rect = nodelink.getLabelBox();
								sizeEditBox.setBounds(rect.x, rect.y, rect.width + 3, rect.height + 3);
								sizeEditBox.setText(nodelink.getSizeAsString());
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
					
					logicalNetLayer.updateCurrentMapElement(me.getPoint());//Устанавливаем текущий объект
					MapElement mapElement = logicalNetLayer.getCurrentMapElement();
	
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
						
						List selection = logicalNetLayer.getSelectedElements();
						//Выводим контекстное меню
						if(selection.size() > 1)
							mapElement = new MapSelection(logicalNetLayer);

						contextMenu = MapPopupMenuManager.getPopupMenu(mapElement);
						if(contextMenu != null)
						{
							contextMenu.setLogicalNetLayer(logicalNetLayer);
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
					break;
			}//switch (mapState.getOperationMode()
		}
		logicalNetLayer.repaint();
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

					logicalNetLayer.getContext().getApplicationModel().setSelected("mapActionMeasureDistance", false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelMap.getString("Distance") + " = " + String.valueOf(MiscUtil.fourdigits(distance)),
							LangModelMap.getString("MeasureDistance"),
							JOptionPane.PLAIN_MESSAGE);

//					Command com = logicalNetLayer.getContext().getApplicationModel().getCommand("mapActionMeasureDistance");
//					com.setParameter("applicationModel", logicalNetLayer.getContext().getApplicationModel());
//					com.setParameter("logicalNetLayer", logicalNetLayer);
//					com.execute();
					
					break;
				case MapState.ZOOM_TO_POINT :
					Point2D.Double pp = logicalNetLayer.convertScreenToMap(me.getPoint());

					logicalNetLayer.setCenter(pp);
					logicalNetLayer.zoomIn();
					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					logicalNetLayer.getContext().getApplicationModel().setSelected("mapActionZoomToPoint", false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

//					Command com = logicalNetLayer.getContext().getApplicationModel().getCommand("mapActionZoomToPoint");
//					com.setParameter("applicationModel", logicalNetLayer.getContext().getApplicationModel());
//					com.setParameter("logicalNetLayer", logicalNetLayer);
//					com.execute();
					
					logicalNetLayer.getMapView().setScale(logicalNetLayer.getScale());
					logicalNetLayer.getMapView().setCenter(logicalNetLayer.getCenter());

					break;
				case MapState.ZOOM_TO_RECT:
					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					
					logicalNetLayer.getContext().getApplicationModel().setSelected("mapActionZoomBox", false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

//					Command com2 = logicalNetLayer.getContext().getApplicationModel().getCommand("mapActionZoomBox");
//					com2.setParameter("applicationModel", logicalNetLayer.getContext().getApplicationModel());
//					com2.setParameter("logicalNetLayer", logicalNetLayer);
//					com2.execute();
					
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
					
					logicalNetLayer.getContext().getApplicationModel().setSelected("mapActionMoveToCenter", false);
					logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

//					Command com3 = logicalNetLayer.getContext().getApplicationModel().getCommand("mapActionMoveToCenter");
//					com3.setParameter("applicationModel", logicalNetLayer.getContext().getApplicationModel());
//					com3.setParameter("logicalNetLayer", logicalNetLayer);
//					com3.execute();
					
					logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					logicalNetLayer.getMapView().setCenter(
							logicalNetLayer.getCenter());
					break;
				case MapState.MOVE_HAND:
					Point2D.Double center = logicalNetLayer.getCenter();

/*
					Point screencenter = logicalNetLayer.convertMapToScreen(center);

					int dx = me.getPoint().x - logicalNetLayer.getStartPoint().x;
					int dy = me.getPoint().y - logicalNetLayer.getStartPoint().y;

					System.out.print("center " + MiscUtil.fourdigits(screencenter.x) + ", " + MiscUtil.fourdigits(screencenter.y));
					System.out.println("	shift " + MiscUtil.fourdigits(dx) + ", " + MiscUtil.fourdigits(dy));

					screencenter.x += dx;
					screencenter.y += dy;

					System.out.println("newter " + MiscUtil.fourdigits(screencenter.x) + ", " + MiscUtil.fourdigits(screencenter.y));
					System.out.println("");
					
					center = logicalNetLayer.convertScreenToMap(screencenter);
*/				
					Point2D.Double p1 = logicalNetLayer.convertScreenToMap(logicalNetLayer.getStartPoint());
					Point2D.Double p2 = logicalNetLayer.convertScreenToMap(me.getPoint());
					double dx = p1.x - p2.x;
					double dy = p1.y - p2.y;

//					System.out.print("center " + MiscUtil.fourdigits(center.x) + ", " + MiscUtil.fourdigits(center.y));
//					System.out.println("	shift " + MiscUtil.fourdigits(dx) + ", " + MiscUtil.fourdigits(dy));
					
					center.x += dx;
					center.y += dy;

//					System.out.println("newter " + MiscUtil.fourdigits(center.x) + ", " + MiscUtil.fourdigits(center.y));
//					System.out.println("");
					
					logicalNetLayer.setCenter(center);

					logicalNetLayer.getMapView().setCenter(
							logicalNetLayer.getCenter());
					break;
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
					break;
				default:
					break;
			}//switch (mapState.getOperationMode()
			if(mapState.getOperationMode() != MapState.MOVE_HAND)
				mapState.setOperationMode(MapState.NO_OPERATION);

			//Убираем флаг
			logicalNetLayer.setMenuShown(false);
		}
		logicalNetLayer.repaint();
		mapState.setMouseMode(MapState.MOUSE_NONE);//Для мыши
	}
}
