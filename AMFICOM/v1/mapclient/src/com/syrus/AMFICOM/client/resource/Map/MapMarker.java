//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Маркер связывания оптической дистанции Lo, полученной      * //
// *         экспериментальным путем, со строительной дистанцией Lf,      * //
// *         вводимой при конфигурировании системы, и топологической      * //
// *         дистанцией Lt, полученной в результате расчетов по           * //
// *         координатам и используемо для отображения в окне карты       * //
// *         Связывание дистанций производится по коэффициентам:          * //
// *             Ku = Lo / Lf                                             * //
// *             Kd = Lf / Lt                                             * //
// *         Связывание отображение маркера на карте и на рефлектограмме  * //
// *         строится на основе дистанции Lo, в связи с чем принимаемая   * //
// *         в сообщении из окна рефлектограммы дистанция преобразуется   * //
// *         из Lo в Lf и Lt и наоборот, в отсылаемом сообщении Lt        * //
// *         преобразуется в Lf и Lo. Исключение составляет случай        * //
// *         создания маркера с карты, в этом случае отправляется Lf,     * //
// *         так как топологическая схема не содержит информации о Ku,    * //
// *         и окно рефлектограммы инициализирует маркер такой            * //
// *         информацией, после чего опять используется Lo.               * //
// *                                                                      * //
// * Тип: Java                                                            * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 21 aug 2003                                                      * //
// * Расположение: ISM\prog\java\AMFICOM\com\syrus\AMFICOM\Client\        * //
// *        Resource\Map\MapMarker.java                                   * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 9.0.3.988                        * //
// *                                                                      * //
// * Компилятор: javac (Java 2 SDK, Standard Edition, ver 1.3.1_02)       * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////
package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapMarkerDisplayModel;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePathDecompositor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class MapMarker extends ObjectResource implements MapElement
{
	static final public String typ = "mapmarker";

	protected MapContext mapContext;

	//Размер пиктограммы маркера
	public final static Rectangle defaultBounds = new Rectangle(20, 20);
	protected int selectedNodeLineSize = 2;

	protected boolean selected;
	protected Image icon;

	protected Rectangle bounds = new Rectangle(defaultBounds);

	public String id;
	public String name = "Маркер";
	public double distance = 0.0;
	public Color color = Color.green;
	public String symbol_id;
	public String path_id;
	public String description;

	protected SxDoublePoint anchor = new SxDoublePoint( 0, 0);
	protected SxDoublePoint bufferAnchor = new SxDoublePoint( 0, 0);
	public String owner_id ;
	public String mapContextID;
	protected String imageID;

	public MapNodeLinkElement nodeLink;
	public MapNodeElement startNode;
	public MapNodeElement endNode;

	public int nodeLinkIndex = 0;

	public Hashtable attributes = new Hashtable();
	public MapTransmissionPathElement transmissionPath;
	public SchemePath schemePath = null;
	public TransmissionPath catalogPath = null;
	public String me_id = "";

	public SchemePathDecompositor spd = null;

	public MapMarker(
			String myID, 
			MapContext myMapContext,
			Rectangle myBounds, 
			String myImageID,
			double mylen, 
			MapTransmissionPathElement path)
	{

		id = myID;
		mapContext = myMapContext;
		bounds = myBounds;

		transmissionPath = path;
		if(transmissionPath.PATH_ID != null)
		{
			schemePath = (SchemePath )Pool.get(SchemePath.typ, transmissionPath.PATH_ID);
			if(schemePath != null)
			{
				if(schemePath.path_id != null)
				{
					catalogPath = (TransmissionPath )Pool.get(TransmissionPath.typ, schemePath.path_id);
					if(catalogPath != null)
						me_id = catalogPath.monitored_element_id;
				}
				spd = new SchemePathDecompositor();
				spd.setSchemePath(schemePath);
			}
		}
		setImageID("images/marker.gif");

		Vector nl = transmissionPath.sortNodeLinks();
		Vector n = transmissionPath.sortNodes();

		nodeLink = (MapNodeLinkElement )nl.get(0);
		nodeLinkIndex = 0;
		
		if ( n.indexOf(nodeLink.startNode) < n.indexOf(nodeLink.endNode))
		{
			startNode = nodeLink.startNode;
			endNode = nodeLink.endNode;
		}
		else
		{
			startNode = nodeLink.endNode;
			endNode = nodeLink.startNode;
		}

		anchor = new SxDoublePoint(0.0, 0.0);

		moveToFromStartLo(mylen);
	}

	public Object clone(DataSourceInterface dataSource)
	{
		return null;
	}
	
	//Послать сообщения что маркер создан
	public void sendMessage_Marker_Created()
	{
		if(transmissionPath.PATH_ID == null || transmissionPath.PATH_ID.equals(""))
			return;

		if(schemePath != null)
		{
			spd = new SchemePathDecompositor();
			spd.setSchemePath(schemePath);
		}

		MapNavigateEvent mne = new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_CREATED_EVENT,
				getId(),
				getFromStartLengthLf(),
				transmissionPath.PATH_ID,
				me_id);
//		mne.spd = spd;
		getLogicalNetLayer().mapMainFrame.aContext.getDispatcher().notify(mne);
	}

	//Послать сообщения что маркер удален
	public void sendMessage_Marker_Deleted()
	{
		getLogicalNetLayer().mapMainFrame.aContext.getDispatcher().notify( 
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_DELETED_EVENT,
				getId(),
				0.0D,
				transmissionPath.getId(),
				me_id) );
	}

	//Послать сообщения что маркер перемещается
	public void sendMessage_Marker_Moved()
	{
		getLogicalNetLayer().mapMainFrame.aContext.getDispatcher().notify( 
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_MOVED_EVENT,
				getId(),
				getFromStartLengthLo(),
				transmissionPath.getId(),
				me_id) );
	}

	//Получить сообщение что маркер выбран
	public void getMessage_Marker_Selected()
	{
		selected = true;
	}

	//Получить сообщение что выбор маркера снят
	public void getMessage_Marker_Deselected()
	{
		selected = false;
	}

	public Rectangle getBounds()
	{
		return bounds;
	}

	public void setBounds(Rectangle rec)
	{
		bounds = rec;
	}

	public int getSelectedNodeLineSize()
	{
		return selectedNodeLineSize;
	}

	public void setSelectedNodeLineSize(int size)
	{
		selectedNodeLineSize = size;
	}

	public void setImageID(String iconPath)
	{
		imageID = iconPath;
		ImageIcon myImageIcon = new ImageIcon(imageID);
		icon = myImageIcon.getImage().getScaledInstance(
				(int)getBounds().getWidth(),
				(int)getBounds().getHeight(),
				Image.SCALE_SMOOTH);
	}

	public String getImageID()
	{
		return imageID;
	}
	public SxDoublePoint getAnchor()
	{
		Vector nl = transmissionPath.sortNodeLinks();
		if ( anchor != bufferAnchor )
		{
			LogicalNetLayer lnl = getLogicalNetLayer();

			//Рисование о пределение координат маркера происходит путм проецирования координат
			//курсора	на линию на которой маркер находится
			double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
			double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

			double endNodeX = lnl.convertLongLatToScreen(endNode.getAnchor()).x;
			double endNodeY = lnl.convertLongLatToScreen(endNode.getAnchor()).y;

			double nodeLinkLength =  Math.sqrt( 
					(endNodeX - startNodeX) * (endNodeX - startNodeX) +
					(endNodeY - startNodeY) * (endNodeY - startNodeY) );

			double thisX = lnl.convertLongLatToScreen(anchor).x;
			double thisY = lnl.convertLongLatToScreen(anchor).y;

			double lengthFromStartNode = Math.sqrt( 
					(thisX - startNodeX) * (thisX - startNodeX) +
					(thisY - startNodeY) * (thisY - startNodeY) );

			double cos_b =  (endNodeY - startNodeY) / nodeLinkLength;
			double sin_b =  (endNodeX - startNodeX) / nodeLinkLength;

			if ( lengthFromStartNode > nodeLinkLength )
			{
//				int i1 = nl.indexOf(nodeLink);

				if(nodeLinkIndex < nl.size() - 1)
				{
					nodeLinkIndex++;
					MapNodeLinkElement myNodeLink = (MapNodeLinkElement) nl.get(nodeLinkIndex);
					startNode = endNode;
					nodeLink = myNodeLink;
					endNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, startNode);
					lengthFromStartNode -= nodeLinkLength;
				}
				lengthFromStartNode = nodeLinkLength;
			}
			else
			if ( lengthFromStartNode < 0 )
			{
//				int i1 = nl.indexOf(nodeLink);

				if(nodeLinkIndex > 0)
				{
					nodeLinkIndex--;
					MapNodeLinkElement myNodeLink = (MapNodeLinkElement) nl.get(nodeLinkIndex);
					endNode = startNode;
					nodeLink = myNodeLink;
					startNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, endNode);
					lengthFromStartNode += getNodeLinkScreenLength(myNodeLink);
				}
				lengthFromStartNode = 0;
			}

			anchor = lnl.convertScreenToLongLat(new Point(
				(int)Math.round(startNodeX + sin_b * lengthFromStartNode ),
				(int)Math.round(startNodeY + cos_b * lengthFromStartNode ) ) );
			bufferAnchor = anchor;
		}// if ( anchor != bufferAnchor )

		return anchor;
	}

	public void setAnchor(SxDoublePoint aAnchor)
	{
		anchor = aAnchor;
	}

	public boolean isSelected ()
	{
		return selected;
	}

	public void select()
	{
		selected = true;
	}

	public void deselect()
	{
		selected = false;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void setId(String myID)
	{
		id = myID;
	}
	//рисуем маркер
	public void paint (Graphics g)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();
	
		Point p = lnl.convertMapToScreen( getAnchor());

		Graphics2D pg = ( Graphics2D)g;
		pg.setStroke(new BasicStroke(getSelectedNodeLineSize()));

		pg.drawImage(
				icon,
				p.x - icon.getWidth(lnl) / 2,
				p.y - icon.getHeight(lnl) / 2,
				lnl);

		//Если выбрано то рисуем прямоугольник
		if (isSelected())
		{
			pg.setColor( Color.red);
			pg.drawRect( 
				p.x - icon.getWidth(lnl) / 2,
				p.y - icon.getHeight(lnl) / 2,
				icon.getWidth(lnl),
				icon.getHeight(lnl));
		}
	}

	public void paint (Graphics g, Point myPoint)
	{
	}

	public boolean isMouseOnThisObject (Point currentMousePoint)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();

		int width = icon.getWidth( lnl);
		int height = icon.getHeight( lnl);
		Point p = lnl.convertMapToScreen(anchor);
		Rectangle imageBounds = new Rectangle( 
				p.x - width / 2, 
				p.y - height / 2, 
				width, 
				height);
		if (imageBounds.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	public void move (double deltaX, double deltaY)
	{
		double x = anchor.getX();
		double y = anchor.getY();
		this.anchor = new SxDoublePoint(x + deltaX, y + deltaY);
	}

	//Обработка событии связпнный с Node
	public synchronized MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();

		int mode = lnl.getMode();
		int actionMode = logicalNetLayer.getActionMode();

		MapStrategy strategy = new VoidStrategy();
		Point myPoint = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != LogicalNetLayer.SELECT_ACTION_MODE) &&
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();
			switch (mode)
			{
				case LogicalNetLayer.MOUSE_DRAGGED:
					//Проверка того что маркер можно перемещать и его перемещение
					if ( lnl.mapMainFrame
							.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
					if(isMovable())
					{
						transmissionPath.select();

						//Посылаем сообщение что маркер перемещается
						this.sendMessage_Marker_Moved();

						MotionDescriptor md = getMotionDescriptor(myPoint);

						Vector nl = transmissionPath.sortNodeLinks();
						while ( md.lengthFromStartNode > md.nodeLinkLength )
						{
							if(nodeLinkIndex >= nl.size() - 1)
							{
								md.lengthFromStartNode = md.nodeLinkLength;
								break;
							}

							nodeLinkIndex++;
							MapNodeLinkElement myNodeLink = (MapNodeLinkElement) nl.get(nodeLinkIndex);
//							System.out.print(md.lengthFromStartNode  + " > " + md.nodeLinkLength);
//							System.out.print(" : move from " + nodeLink.getId() + " to " + myNodeLink.getId());
							startNode = endNode;
							nodeLink = myNodeLink;
							endNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, startNode);
							md = getMotionDescriptor(myPoint);
//							System.out.println(" (dist " + md.lengthFromStartNode  + ") ");

							double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
							double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

							bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
								new Point(
									(int)Math.round(startNodeX + md.sin_b * ( md.lengthFromStartNode )),
									(int)Math.round(startNodeY + md.cos_b * ( md.lengthFromStartNode )) ) );

							setAnchor(bufferAnchor);
						}

						while ( md.lengthFromStartNode < 0 )
						{
							if(nodeLinkIndex <= 0)
							{
								md.lengthFromStartNode = 0;
								break;
							}

							nodeLinkIndex--;
							MapNodeLinkElement myNodeLink = (MapNodeLinkElement) nl.get(nodeLinkIndex);
//							System.out.print(md.lengthFromStartNode  + " < 0 ");
//							System.out.print(" : move from " + nodeLink.getId() + " to " + myNodeLink.getId());
							endNode = startNode;
							nodeLink = myNodeLink;
							startNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, endNode);
							md = getMotionDescriptor(myPoint);
//							System.out.println(" (dist " + md.lengthFromStartNode  + ") ");

							double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
							double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

							bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
								new Point(
									(int)Math.round(startNodeX + md.sin_b * ( md.lengthFromStartNode )),
									(int)Math.round(startNodeY + md.cos_b * ( md.lengthFromStartNode )) ) );

							setAnchor(bufferAnchor);
						}

						double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
						double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

						bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
							new Point(
								(int)Math.round(startNodeX + md.sin_b * ( md.lengthFromStartNode )),
								(int)Math.round(startNodeY + md.cos_b * ( md.lengthFromStartNode )) ) );

						setAnchor(bufferAnchor);
					}// if(isMovable())
					break;
			}//switch

		}//SwingUtilities.isLeftMouseButton(me)
		return strategy;
	}

/*
	public synchronized MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();

		int mode = lnl.getMode();
		int actionMode = logicalNetLayer.getActionMode();

		MapStrategy strategy = new VoidStrategy();
		Point myPoint = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != LogicalNetLayer.SELECT_ACTION_MODE) &&
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();
			switch (mode)
			{
				case LogicalNetLayer.MOUSE_DRAGGED:
					if ( lnl.mapMainFrame
							.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
					if(isMovable())
					{
						transmissionPath.select();

						this.sendMessage_Marker_Moved();

						double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
						double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

						double endNodeX = lnl.convertLongLatToScreen(endNode.getAnchor()).x;
						double endNodeY = lnl.convertLongLatToScreen(endNode.getAnchor()).y;

						double nodeLinkLength =  Math.sqrt( 
							(endNodeX - startNodeX) * (endNodeX - startNodeX) +
							(endNodeY - startNodeY) * (endNodeY - startNodeY) );

						double thisX = lnl.convertLongLatToScreen(getAnchor()).x;
						double thisY = lnl.convertLongLatToScreen(getAnchor()).y;

						double lengthFromStartNode = Math.sqrt( 
							(thisX - startNodeX) * (thisX - startNodeX) +
							(thisY - startNodeY) * (thisY - startNodeY) );

						double cos_b =  (endNodeY - startNodeY) / nodeLinkLength;

						double sin_b =  (endNodeX - startNodeX) / nodeLinkLength;

						double mousePointX = myPoint.x;
						double mousePointY = myPoint.y;

						double lengthThisToMousePoint = Math.sqrt( 
							(mousePointX - thisX) * (mousePointX - thisX) +
							(mousePointY - thisY) * (mousePointY - thisY) );

						double cos_a = (lengthThisToMousePoint == 0 ) ? 0.0 :
							(	(endNodeX - startNodeX) * (mousePointX - thisX) + 
								(endNodeY - startNodeY)*(mousePointY - thisY) ) /
							( nodeLinkLength * lengthThisToMousePoint );

						lengthFromStartNode = lengthFromStartNode + cos_a * lengthThisToMousePoint;

						if ( lengthFromStartNode > nodeLinkLength )
						{
							Vector nl = transmissionPath.sortNodeLinks();
							int i1 = nl.indexOf(nodeLink);

							if(i1 < nl.size() - 1)
							{
								MapNodeLinkElement myNodeLink = (MapNodeLinkElement) nl.get(i1 + 1);
								startNode = endNode;
								nodeLink = myNodeLink;
								endNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, startNode);
								lengthFromStartNode -= nodeLinkLength;
//								return new VoidStrategy();
							}
							else
								lengthFromStartNode = nodeLinkLength;
						}
						else
						if ( lengthFromStartNode < 0 )
						{
							Vector nl = transmissionPath.sortNodeLinks();

							int i1 = nl.indexOf(nodeLink);

							if(i1 > 0)
							{
								MapNodeLinkElement myNodeLink = (MapNodeLinkElement) nl.get(i1 - 1);
								endNode = startNode;
								nodeLink = myNodeLink;
								startNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, endNode);
								lengthFromStartNode += getNodeLinkScreenLength(myNodeLink);
//								return new VoidStrategy();
							}
							else
								lengthFromStartNode = 0;
						}

						startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
						startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

						bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
							new Point(
								(int)Math.round(startNodeX + sin_b * ( lengthFromStartNode )),
								(int)Math.round(startNodeY + cos_b * ( lengthFromStartNode )) ) );

						setAnchor(bufferAnchor);
					}
					break;
			}//switch

		}//SwingUtilities.isLeftMouseButton(me)
		return strategy;
	}
*/
	public MotionDescriptor getMotionDescriptor(Point myPoint)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();

		//Рисование о пределение координат маркера происходит путм проецирования координат
		//курсора	на линию на которой маркер находится

		double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
		double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

		double endNodeX = lnl.convertLongLatToScreen(endNode.getAnchor()).x;
		double endNodeY = lnl.convertLongLatToScreen(endNode.getAnchor()).y;

		double nodeLinkLength = Math.sqrt( 
			(endNodeX - startNodeX) * (endNodeX - startNodeX) +
			(endNodeY - startNodeY) * (endNodeY - startNodeY) );

		double thisX = lnl.convertLongLatToScreen(getAnchor()).x;
		double thisY = lnl.convertLongLatToScreen(getAnchor()).y;

		double lengthFromStartNode = Math.sqrt( 
			(thisX - startNodeX) * (thisX - startNodeX) +
			(thisY - startNodeY) * (thisY - startNodeY) );

		double cos_b =  (endNodeY - startNodeY) / nodeLinkLength;

		double sin_b =  (endNodeX - startNodeX) / nodeLinkLength;

		double mousePointX = myPoint.x;
		double mousePointY = myPoint.y;

		double lengthThisToMousePoint = Math.sqrt( 
			(mousePointX - thisX) * (mousePointX - thisX) +
			(mousePointY - thisY) * (mousePointY - thisY) );

		double cos_a = (lengthThisToMousePoint == 0 ) ? 0.0 :
			(	(endNodeX - startNodeX) * (mousePointX - thisX) + 
				(endNodeY - startNodeY)*(mousePointY - thisY) ) /
			( nodeLinkLength * lengthThisToMousePoint );

		lengthFromStartNode = lengthFromStartNode + cos_a * lengthThisToMousePoint;
		
		return new MotionDescriptor(
				cos_b,
				sin_b,
				lengthThisToMousePoint,
				cos_a,
				lengthFromStartNode,
				nodeLinkLength);
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return mapContext.getLogicalNetLayer();
	}

	public MapContext getMapContext()
	{
		return mapContext;
	}

	public void setMapContext( MapContext myMapContext)
	{
		mapContext = myMapContext;
	}

	public int getType()
	{
		return 0;
	}

	//Контекстное меню
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		return new JPopupMenu();
	}

	public static double getNodeLinkScreenLength( MapNodeLinkElement myNodeLink)
	{
		LogicalNetLayer lnl = myNodeLink.getLogicalNetLayer();
		double x1 = lnl.convertLongLatToScreen(myNodeLink.startNode.getAnchor()).x;
		double y1 = lnl.convertLongLatToScreen(myNodeLink.startNode.getAnchor()).y;

		double x1_ = lnl.convertLongLatToScreen(myNodeLink.endNode.getAnchor()).x;
		double y1_ = lnl.convertLongLatToScreen(myNodeLink.endNode.getAnchor()).y;

		return Math.sqrt( 
				(x1_ - x1) * (x1_ - x1) +
				(y1_ - y1) * (y1_ - y1) );
	}

	public void setLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return null;
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapMarkerDisplayModel();
	}
	
	public ObjectResourceModel getModel()
	{
		return new MapMarkerModel(this);
	}

	public double getFromStartLengthLt()
	{
		Vector nl = transmissionPath.sortNodeLinks();
		Vector pl = transmissionPath.sortPhysicalLinks();
		Vector n = transmissionPath.sortNodes();
		
		double path_length = 0;

		MapNodeLinkElement mnle;
		boolean point_reached = false;
		for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();
			if(nodeLink.PhysicalLinkID.equals(mple.getId()))
			{
				Vector nl2 = mple.sortNodeLinks();
				point_reached = true;
				boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
				int size = nl2.size();
				for(int i = 0; i < size; i++)
				{
					if(direct_order)
						mnle = (MapNodeLinkElement )nl2.get(i);
					else
						mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
					if ( mnle == nodeLink)
					{
						if ( n.indexOf(startNode) < n.indexOf(endNode))
							return path_length + getSizeInDoubleLt();
						else
							return path_length + nodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
					}
					else
					{
						path_length += mnle.getSizeInDoubleLt();
					}
				}// for(int i
			}// if(nodeLink.PhysicalLinkID
			else
			{
				path_length += mple.getSizeInDoubleLt();
			}
		}// for(Enumeration plen
		return 0;
	}

	public double getFromStartLengthLf()
	{
		if(schemePath == null)
			return 0.0D;

		Vector nl = transmissionPath.sortNodeLinks();
		Vector pl = transmissionPath.sortPhysicalLinks();
		Vector n = transmissionPath.sortNodes();
		
		double path_length = 0;
		MapNodeElement bufferNode = transmissionPath.startNode;

		boolean point_reached = false;
		MapNodeLinkElement mnle;

		PathElement pes[] = new PathElement[schemePath.links.size()];
		for(int i = 0; i < schemePath.links.size(); i++)
		{
			PathElement pe = (PathElement )schemePath.links.get(i);
			pes[pe.n] = pe;
		}
		Vector pvec = new Vector();
		for(int i = 0; i < pes.length; i++)
		{
			pvec.add(pes[i]);
		}

		Enumeration enum = pvec.elements();
		PathElement pe = null;

		for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

			pe = (PathElement )enum.nextElement();
			bufferNode.countPhysicalLength(schemePath, pe, enum);
			path_length += bufferNode.getPhysicalLength();

			if(bufferNode.equals(mple.startNode))
				bufferNode = mple.endNode;
			else
				bufferNode = mple.startNode;

			if(nodeLink.PhysicalLinkID.equals(mple.getId()))
			{
				Vector nl2 = mple.sortNodeLinks();
				point_reached = true;
				double temp_length = 0.0D; // Count topological length over cable until marker reached
				boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
				int size = nl2.size();
				for(int i = 0; i < size; i++)
				{
					if(direct_order)
						mnle = (MapNodeLinkElement )nl2.get(i);
					else
						mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
					if ( mnle == nodeLink)
					{
						if ( n.indexOf(startNode) < n.indexOf(endNode))
							temp_length += getSizeInDoubleLt();
						else
							temp_length += nodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
						return path_length + mple.getKd() * temp_length;// Convert to physical length
					}
					else
					{
						temp_length += mnle.getSizeInDoubleLt();
					}
				}// for(int i
			}// if(nodeLink.PhysicalLinkID
			else
			{
				path_length += mple.getSizeInDoubleLf();
			}
		}// for(Enumeration plen
		return 0.0D;
	}

	public double getFromEndLengthLt()
	{
		double length = transmissionPath.getSizeInDoubleLt();
		return length - getFromStartLengthLt();
	}	

	public double getFromEndLengthLf()
	{
		double length = transmissionPath.getSizeInDoubleLf();
		return length - getFromStartLengthLf();
	}

	public double getSizeInDoubleLt()
	{

		SxDoublePoint from = startNode.getAnchor();
		SxDoublePoint to = getAnchor();

		return LogicalNetLayer.distance(from, to);
	}

	public double getSizeInDoubleLf()
	{
		double Kd = getMapContext().getPhysicalLinkbyNodeLink(nodeLink.getId()).getKd();
		return getSizeInDoubleLt() * Kd;
	}

	//Передвинуть в точку на заданной расстоянии от нсчала
	public void moveToFromStartLt(double distance)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();
		if ( lnl.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
		{
			double pathl = transmissionPath.getSizeInDoubleLt();
			if ( distance > pathl)
				distance = pathl;

			Vector nl = transmissionPath.sortNodeLinks();
			Vector pl = transmissionPath.sortPhysicalLinks();
			Vector n = transmissionPath.sortNodes();
		
			double path_length = 0;

			boolean point_reached = false;
			MapNodeLinkElement mnle;
			
			for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
			{
				MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

				if ( path_length + mple.getSizeInDoubleLt() > distance)
				{
					Vector nl2 = mple.sortNodeLinks();
					point_reached = true;
					boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
					int size = nl2.size();
					for(int i = 0; i < size; i++)
					{
						if(direct_order)
							mnle = (MapNodeLinkElement )nl2.get(i);
						else
							mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
						if ( path_length + mnle.getSizeInDoubleLt() > distance)
						{
							nodeLink = mnle;
							nodeLinkIndex = nl.indexOf(mnle);
							if ( n.indexOf(mnle.startNode) < n.indexOf(mnle.endNode))
							{
								startNode = mnle.startNode;
								endNode = mnle.endNode;
							}
							else
							{
								startNode = mnle.endNode;
								endNode = mnle.startNode;
							}

							double nl_distance = distance - path_length;

							adjustPosition(nl_distance, false);
							return;
						}// if ( ... > distance
						else
						{
							path_length += mnle.getSizeInDoubleLt();
						}
					}// for(int i
				}// if ( ... > distance
				else
				{
					path_length += mple.getSizeInDoubleLt();
				}
			}// for(Enumeration plen
		}// if ( lnl.mapMainFrame
	}

	public double getFromStartLengthLo()
	{
		if(spd == null)
			return getFromStartLengthLf();
		else
			return spd.getOpticalDistance(getFromStartLengthLf());
	}

	public void moveToFromStartLo(double dist)
	{
		if(spd == null)
			moveToFromStartLf(dist);
		else
			moveToFromStartLf(spd.getPhysicalDistance(dist));
	}

	//Передвинуть в точку на заданном расстоянии от начала (физ)
	public void moveToFromStartLf(double dist)
	{
		double distance = 0.0D;
		distance = dist;

		if ( transmissionPath.getLogicalNetLayer().mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
		{
			double pathl = transmissionPath.getSizeInDoubleLf();
			if ( distance > pathl)
				distance = pathl;

			Vector pl = transmissionPath.sortPhysicalLinks();
		
			double path_length = 0;
			MapNodeElement bufferNode = transmissionPath.startNode;

			boolean point_reached = false;
			MapNodeLinkElement mnle;

			PathElement pes[] = new PathElement[schemePath.links.size()];
			for(int i = 0; i < schemePath.links.size(); i++)
			{
				PathElement pe = (PathElement )schemePath.links.get(i);
				pes[pe.n] = pe;
			}
			Vector pvec = new Vector();
			for(int i = 0; i < pes.length; i++)
			{
				pvec.add(pes[i]);
			}

			Enumeration enum = pvec.elements();
			PathElement pe = null;

			for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
			{
				MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

				pe = (PathElement )enum.nextElement();
				bufferNode.countPhysicalLength(schemePath, pe, enum);
				double nd = bufferNode.getPhysicalLength();
				if(path_length + nd > distance)
				{
					setRelativeToLink(0, mple);
					return;
				}
				else
				{
					path_length += nd;
				}

				if(bufferNode.equals(mple.startNode))
					bufferNode = mple.endNode;
				else
					bufferNode = mple.startNode;

				if ( path_length + mple.getSizeInDoubleLf() > distance)
				{
					setRelativeToLink(distance - path_length, mple);
					return;
				}
				else
				{
					path_length += mple.getSizeInDoubleLf();
				}
			}// for(Enumeration plen
		}// if ( isEnabled("mapActionMarkerMove")
	}

	public void setRelativeToLink(double pl_distance, MapPhysicalLinkElement mple)
	{
		MapNodeLinkElement mnle;
	
		Vector nl = transmissionPath.sortNodeLinks();
		Vector n = transmissionPath.sortNodes();
		Vector nl2 = mple.sortNodeLinks();
		boolean point_reached = true;
		double temp_length = 0.0D; // Count topological length over cable until marker reached
		boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nl2.get(nl2.size() - 1)));
		int size = nl2.size();
		for(int i = 0; i < size; i++)
		{
			if(direct_order)
				mnle = (MapNodeLinkElement )nl2.get(i);
			else
				mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
			if ( temp_length + mnle.getSizeInDoubleLf() > pl_distance)
			{
				nodeLink = mnle;
				nodeLinkIndex = nl.indexOf(mnle);
				if ( n.indexOf(mnle.startNode) < n.indexOf(mnle.endNode))
				{
					startNode = mnle.startNode;
					endNode = mnle.endNode;
				}
				else
				{
					startNode = mnle.endNode;
					endNode = mnle.startNode;
				}

				double nl_distance = pl_distance - temp_length;

				adjustPosition(nl_distance, true);
				return;
			}// if ( ... > pl_distance
			else
			{
				temp_length += mnle.getSizeInDoubleLf();
			}
		}// for(int i
	}

	public void adjustPosition(double nl_distance, boolean is_lf)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();
	
		double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
		double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

		double endNodeX = lnl.convertLongLatToScreen(endNode.getAnchor()).x;
		double endNodeY = lnl.convertLongLatToScreen(endNode.getAnchor()).y;

		double nodeLinkLength =  Math.sqrt( 
				(endNodeX - startNodeX) * (endNodeX - startNodeX) +
				(endNodeY - startNodeY) * (endNodeY - startNodeY) );
/*
		double thisX = lnl.convertLongLatToScreen(anchor).x;
		double thisY = lnl.convertLongLatToScreen(anchor).y;

		double lengthFromStartNode = Math.sqrt( 
			(thisX - startNodeX) * (thisX - startNodeX) +
			(thisY - startNodeY) * (thisY - startNodeY) );
*/
		double cos_b = (endNodeY - startNodeY) / nodeLinkLength;

		double sin_b = (endNodeX - startNodeX) / nodeLinkLength;

		double nl_fromstart = (is_lf) ? nodeLink.getSizeInDoubleLf() : nodeLink.getSizeInDoubleLt();

		setAnchor(lnl.convertScreenToLongLat(new Point(
			(int)Math.round(startNodeX + sin_b * ( nl_distance / nl_fromstart * nodeLinkLength )),
			(int)Math.round(startNodeY + cos_b * ( nl_distance / nl_fromstart * nodeLinkLength )) ) ) );
	}

	//Проверка того что объект можно перемещать
	public boolean isMovable()
	{
		if ( transmissionPath.getLogicalNetLayer().mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
		{
			return true;
		}
		return false;
	}

	public String getToolTipText()
	{
		String s1 = getName() + " (путь " + transmissionPath.getName() + ")";

		return s1;
	}

	class MotionDescriptor
	{
		double cos_b;
		double sin_b;
		double lengthThisToMousePoint;
		double cos_a;
		double lengthFromStartNode;
		double nodeLinkLength;

		public MotionDescriptor(double cb, double sb, double lt, double ca, double lf, double nl)
		{
			cos_b = cb;
			sin_b = sb;
			lengthThisToMousePoint = lt;
			cos_a = ca;
			lengthFromStartNode = lf;
			nodeLinkLength = nl;
		}
	}
}