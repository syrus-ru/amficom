package com.syrus.AMFICOM.Client.Map;

import com.ofx.geometry.SxDoublePoint;
import com.ofx.mapViewer.SxMapLayer;

import com.syrus.AMFICOM.Client.Map.Popup.MyPopupMenu;
import com.syrus.AMFICOM.Client.Map.Strategy.DeleteSelectionStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarker;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathProtoElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import java.awt.*;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.util.Iterator;

public class LogicalNetLayer extends SxMapLayer
		implements
		MouseMotionListener,
		MouseListener,
		DropTargetListener
{
	//Флаги состояния действий над картой
	public final static int NULL_MODE = 0;//Пустой режим
	public final static int NULL_ACTION_MODE = 100;
	public final static int ALT_LINK_ACTION_MODE = 101;//Клаваша alt нажата (режим добавления PhysicalNodeElement)
	public final static int MOVE_ACTION_MODE = 102;//Режим перемещения. Клавища ctrl нажата
	public final static int DRAW_ACTION_MODE = 103;
	public final static int SELECT_ACTION_MODE = 104;//Режив выбора. Клавиша Shift нажата
	public final static int SELECT_MARKER_ACTION_MODE = 105;//Режим выбора области
	public final static int DRAW_LINES_ACTION_MODE = 106;//Режим рисования линий (NodeLink и т.д.)
	public final static int FIXDIST_ACTION_MODE = 107;//Режим рисования линий 
													  // с фиксированной дистанцией до соседнего узла

	//Флаги состояния мыши. Устанавливаются и убираются в этом классе
	public final static int MOUSE_NONE = 0;
	public final static int MOUSE_PRESSED = 301;
	public final static int MOUSE_RELEASED = 302;
	public final static int MOUSE_MOVED = 303;
	public final static int MOUSE_DRAGGED = 304;

	//Флаги состояния меню для работы с картой
	public final static int NO_ACTION = 0;
	public final static int ZOOM_TO_POINT = 1;
	public final static int ZOOM_TO_RECT = 2;
	public final static int MOVE_TO_CENTER = 3;
	public final static int MOVE_HAND_BUTTON = 4;

	protected int mapState = NO_ACTION;//Флаг состояния меню для работы с картой
	protected int mode = MOUSE_NONE;//Флаг состояния мыши
	protected int actionMode = NULL_ACTION_MODE;//Флаг состояния режимов

	//Эти переменная просто нужна для определения сдвига карты
	protected Point moveHandButtonStartPoint;
	protected double dx, dy;

	protected MapContext mapContext;//Инициализация содержимого карты

	public MapMainFrame parent;
	public NetMapViewer viewer;//Обозрематель карты
	public JFrame mainFrame;
	public MapMainFrame mapMainFrame;

	boolean isMenuShown = false;//Флаг появления контекстного меню
	boolean moveMapByHandActionState = false;//Флаг состояния того , что карту двигают

	//При нажатии правой кнопки мыши и её перемещении, поля startX, startY получают
	//координаты начальной точки (где произошло нажатие), а поля endX, endY координаты
	// текущего положения мыши.
	protected int startX;//координаты при
	protected int startY;//нажатии мыши

	protected int endX;//координаты при
	protected int endY;//отпуске мыши

	public boolean perform_processing = true;

	public Point currentPoint = new Point(0, 0);

	protected MapNodeLinkSizeField sizeEditBox = null;

	public LogicalNetLayer(MapMainFrame myParent, NetMapViewer myViewer, MapMainFrame myMapMainFrame)
	{
		super();
		try
		{
			parent = myParent;
			viewer = myViewer;
			mainFrame = (JFrame )(myMapMainFrame.getParent());
			mapMainFrame = myMapMainFrame;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void paint(Graphics g)
	{
		Graphics2D p = (Graphics2D )g;

		drawLines(p);
		drawNodes(p);
		drawTempLines(p);

		if (mapState == ZOOM_TO_RECT )//То рисовать прямоугольник
		{
			Stroke st = p.getStroke();
			p.setStroke(new BasicStroke(3));
			
			int startX = getStartPoint().x;
			int startY = getStartPoint().y;
			int endX = getEndPoint().x;
			int endY = getEndPoint().y;

			p.setColor(Color.yellow);
			p.drawRect(
					Math.min(startX, endX),
					Math.min(startY, endY),
					Math.abs(endX - startX),
					Math.abs(endY - startY));

			p.setStroke(st);
		}
	}

	//Рисует линии
	public void drawLines(Graphics g)
	{
		Graphics2D p = (Graphics2D )g;
		Iterator e;

	//Если режим показа nodeLink не разрешён, то включам режим показа physicalLink
		if ( mapMainFrame.aContext.getApplicationModel().isEnabled("mapModeNodeLink") == false
				&& getMapContext().linkState == MapContext.SHOW_NODE_LINK)
		{
			Command com = mapMainFrame.aContext.getApplicationModel().getCommand("mapModeLink");
			com.setParameter("applicationModel", mapMainFrame.aContext.getApplicationModel());
			com.setParameter("logicalNetLayer", this);
			com.execute();
		}


		if ( mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionShowPath") &&
				getMapContext().linkState == MapContext.SHOW_TRANSMISSION_PATH)
		{
			//Последовательность отображения
			//1 transmissionPath
			//2 physicalLink которвые не вошли в transmissionPath
			//Выбираем какие physicalLink надо показать
			LinkedList physicalLinkToShow = new LinkedList();
			e = mapContext.getPhysicalLinks().iterator();
			while (e.hasNext())
			{
				MapPhysicalLinkElement myMapPhysicalLinkElement = 
					(MapPhysicalLinkElement )e.next();
				physicalLinkToShow.add(myMapPhysicalLinkElement.getId());
			}

			//Удаляем из них какие не надо
			e = mapContext.getTransmissionPath().iterator();
			while (e.hasNext())
			{
				MapTransmissionPathElement myMapTransmissionPathElement = 
					(MapTransmissionPathElement )e.next();
				myMapTransmissionPathElement.paint( g);
				physicalLinkToShow.removeAll(myMapTransmissionPathElement.physicalLink_ids);
			}

			e = physicalLinkToShow.iterator();
			while (e.hasNext())
			{
				mapContext.getPhysicalLink((String )e.next()).paint(g);
			}
			return;
		}

		if (mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionShowLink") &&
				getMapContext().linkState == MapContext.SHOW_PHYSICALLINK)
		{
			e = mapContext.getPhysicalLinks().iterator();
			while (e.hasNext())
			{
				MapPhysicalLinkElement myMapPhysicalLinkElement = 
					(MapPhysicalLinkElement )e.next();
				myMapPhysicalLinkElement.paint( g);
			}
			return;
		}

		e = mapContext.getNodeLinks().iterator();
		while (e.hasNext())
		{
			MapNodeLinkElement curNodeLink = (MapNodeLinkElement )e.next();
			if ( !getMapContext().getPhysicalLinkbyNodeLink(curNodeLink.getId()).isSelected())
			{
				curNodeLink.paint(p);
			}
		}

	}

	//Рисует временние линии
	//A0A что такое временние линии?
	public void drawTempLines(Graphics g)
	{
		Graphics2D p = ( Graphics2D)g;
		int startX = getStartPoint().x;
		int startY = getStartPoint().y;
		int endX = getEndPoint().x;
		int endY = getEndPoint().y;

		switch (getActionMode())
		{
			case SELECT_MARKER_ACTION_MODE:
				p.setColor( Color.blue);
				p.drawRect(
						Math.min(startX,endX),
						Math.min(startY,endY),
						Math.abs(endX-startX),
						Math.abs(endY-startY));
				break;
			case DRAW_LINES_ACTION_MODE :
				p.setColor( Color.red);
				p.drawLine( startX, startY, endX, endY);
				break;
		}
	}

	//Рисует элементы наследники от класса Node
	public void drawNodes(Graphics g)
	{
		Graphics2D pg = (Graphics2D )g;

		Iterator e = mapContext.getNodes().iterator();
		while (e.hasNext())
		{
			MapNodeElement curNode = (MapNodeElement )e.next();
			if ( curNode instanceof MapEquipmentNodeElement)
			{
				if ( mapMainFrame
						.aContext.getApplicationModel().isEnabled("mapActionShowEquipment"))
				{
					curNode.paint(pg);
				}
			}
			if ( curNode instanceof MapPhysicalNodeElement)
			{
				curNode.paint(pg);
			}
			if ( curNode instanceof MapMarkElement)
			{
				if ( mapMainFrame
						.aContext.getApplicationModel().isEnabled("mapActionMarkShow"))
				{
					MapMarkElement mme = (MapMarkElement )curNode;
					mme.moveToFromStart(mme.distance);
					curNode.paint(pg);
				}
			}
		}

		e = mapContext.markers.iterator();
		while (e.hasNext())
		{
			MapMarker marker = (MapMarker )e.next();
			marker.paint(pg);
		}
	}

	public void setLocation(int x, int y)
	{
		viewer.getSxMapViewer().getMapCanvas().setLocation(x, y);
		System.out.println("Set location " + x + ", " + y);
		try 
		{
			throw new Exception("");
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}

	public Point getCurrentPoint()
	{
		return currentPoint;
	}

	public void mouseDragged(MouseEvent me)
	{
		currentPoint = me.getPoint();
		
		setMode(MOUSE_DRAGGED);
		if ( mapContext != null)
		{
			setEndPoint(me.getPoint());

			setLatLong(me.getPoint());

			//Если перемещают карту лапкой
			if(mapState == MOVE_HAND_BUTTON &&
				moveMapByHandActionState == true )
			{
				double startX = this.convertScreenToLongLat(me.getPoint()).x;
				double startY = this.convertScreenToLongLat(me.getPoint()).y;

				viewer.getSxMapViewer().getMapCanvas().setLocation( 
						me.getPoint().x - moveHandButtonStartPoint.x,
						me.getPoint().y - moveHandButtonStartPoint.y);
			}
			else
			{
				MapElement myMapElement = mapContext.getCurrentMapElement();
				MapStrategy myStrategy = myMapElement.getMapStrategy( parent.getContext(), this, me, me.getPoint());
				myStrategy.doContextChanges();

				Dispatcher disp = parent.getContext().getDispatcher();
				if(disp != null)
				{
					perform_processing = false;
					disp.notify(new MapNavigateEvent(myMapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					perform_processing = true;
				}

				postDirtyEvent();
				postPaintEvent();
			}
		}
		setMode(NULL_MODE);
	}

	public void mouseMoved(MouseEvent me)
	{
		currentPoint = me.getPoint();
		
		setMode( MOUSE_MOVED);
		setLatLong( me.getPoint());//Выводим значения широты и долготы
		setMode( NULL_MODE);
	}

	public void mouseClicked(MouseEvent me)
	{
	}

	public void mousePressed(MouseEvent me)
	{
		//Берём фокус
		this.parent.myMapScrollPane.grabFocus();
		setMode(MOUSE_PRESSED);//Установить режим

		if ( mapContext != null)
		{
			setStartPoint(me.getPoint());//Установить начальную точку
			setEndPoint(me.getPoint());//Установить начальную точку
			switch ( mapState)
			{
				case MOVE_HAND_BUTTON ://Флаг для меремещения карты лапкой
				{
					moveHandButtonStartPoint = me.getPoint();
					moveMapByHandActionState = true;
				}
				break;

			//A0A
			default:
			{
				if(mapContext.getCurrentMapElement() != null)
					if(mapContext.getCurrentMapElement() instanceof MapNodeElement)
					{
						MapNodeElement node = (MapNodeElement )mapContext.getCurrentMapElement();
						MapNodeLinkElement nodelink = mapContext.getEditedNodeLink(me.getPoint());
						if(nodelink != null)
						{
							sizeEditBox = new MapNodeLinkSizeField(this, nodelink, node);
							Rectangle rect = nodelink.getLabelBox();
							sizeEditBox.setBounds(rect.x, rect.y, rect.width + 3, rect.height + 3);
							sizeEditBox.setText(nodelink.getSize());
							sizeEditBox.setSelectionStart(0);
							sizeEditBox.setSelectionEnd(sizeEditBox.getText().length());
							sizeEditBox.selectAll();
							this.parent.myMapViewer.add(sizeEditBox);
							sizeEditBox.setVisible(true);
							setMode(NULL_MODE);
							return;
						}
					}
				
				mapContext.setCurrentMapElement( me.getPoint());//Устанавливаем текущий объект
				MapElement myMapElement = mapContext.getCurrentMapElement();

				if (SwingUtilities.isLeftMouseButton(me))
				{
					MapStrategy myStrategy = myMapElement.getMapStrategy(parent.getContext(), this, me, me.getPoint());
					myStrategy.doContextChanges();
//					System.out.println("selected " + myMapElement.getId());

					myMapElement = mapContext.getCurrentMapElement();
					Dispatcher disp = parent.getContext().getDispatcher();
					if(disp != null)
					{
						perform_processing = false;
						disp.notify(new MapNavigateEvent(myMapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
						perform_processing = true;
					}
				}

				if (SwingUtilities.isRightMouseButton(me))
				{
					//Выводим контекстное меню
					//  mapContext.deselectAll();
					myMapElement.select();
					JPopupMenu contextMenu = myMapElement.getContextMenu(mainFrame);
					if(contextMenu != null)
					{
						MyPopupMenu myPopup = (MyPopupMenu )contextMenu;
						myPopup.setContext(mapMainFrame.aContext);
						contextMenu.show(viewer, me.getPoint().x, me.getPoint().y);
						isMenuShown = true;
					}
				}

			}
			break;
			}
		}
		postDirtyEvent();
		postPaintEvent();
		setMode(NULL_MODE);
	}

	public void mouseEntered(MouseEvent me)
	{
	}

	public void mouseExited(MouseEvent me)
	{
	}

	public void mouseReleased(MouseEvent me)
	{
		if(this.sizeEditBox != null)
			if(this.sizeEditBox.isVisible())
				return;
	
		double startX = 0;
		double startY = 0;
		double endX = 0;
		double endY = 0;

		setMode(MOUSE_RELEASED);
		if ( mapContext != null)
		{
			//Обрабатывает события на панели инстрементов
			switch (mapState)
			{
				case ZOOM_TO_POINT :
					SxDoublePoint pp = new SxDoublePoint(0, 0);

					pp.setX( this.convertScreenToLongLat(me.getPoint()).x);
					pp.setY( this.convertScreenToLongLat(me.getPoint()).y);

					viewer.zoomToPoint(pp.x, pp.y);
					mapCanvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					Command com = mapMainFrame.aContext.getApplicationModel().getCommand("mapActionZoomToPoint");
					com.setParameter("applicationModel", mapMainFrame.aContext.getApplicationModel());
					com.setParameter("logicalNetLayer", this);
					com.execute();
					
					getMapContext().zoom(viewer.getScale());
					getMapContext().setLongLat(viewer.getCenter()[0], viewer.getCenter()[1]);

					break;
				case ZOOM_TO_RECT:
					startX = this.convertScreenToLongLat(getStartPoint()).x;
					startY = this.convertScreenToLongLat(getStartPoint()).y;
					endX = this.convertScreenToLongLat(getEndPoint()).x;
					endY = this.convertScreenToLongLat(getEndPoint()).y;

					mapCanvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					
					Command com2 = mapMainFrame.aContext.getApplicationModel().getCommand("mapActionZoomBox");
					com2.setParameter("applicationModel", mapMainFrame.aContext.getApplicationModel());
					com2.setParameter("logicalNetLayer", this);
					com2.execute();
					
					if (!getStartPoint().equals(getEndPoint()))
					{
						double startScale = this.getMapViewer().getScale();
						viewer.zoomToBox(startX, startY, endX, endY);

						getMapContext().zoom(viewer.getScale());
						getMapContext().setLongLat(viewer.getCenter()[0], viewer.getCenter()[1]);
					}
					break;
				case MOVE_TO_CENTER:
					startX = this.convertScreenToLongLat(me.getPoint()).x;
					startY = this.convertScreenToLongLat(me.getPoint()).y;

					viewer.getSxMapViewer().setCenter(startX, startY);
					
					Command com3 = mapMainFrame.aContext.getApplicationModel().getCommand("mapActionMoveToCenter");
					com3.setParameter("applicationModel", mapMainFrame.aContext.getApplicationModel());
					com3.setParameter("logicalNetLayer", this);
					com3.execute();
					
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					getMapContext().setLongLat(viewer.getCenter()[0], viewer.getCenter()[1]);
					break;
				case MOVE_HAND_BUTTON:
					startX = this.convertScreenToLongLat(me.getPoint()).x;
					startY = this.convertScreenToLongLat(me.getPoint()).y;

					dx = this.convertScreenToLongLat(moveHandButtonStartPoint).x - startX;
					dy = this.convertScreenToLongLat(moveHandButtonStartPoint).y - startY;

					//перемещаем область отображения
					viewer.getSxMapViewer().getMapCanvas().setBounds(0, 0,
							viewer.getSxMapViewer().getMapCanvas().getWidth(),
							viewer.getSxMapViewer().getMapCanvas().getHeight());

					viewer.getSxMapViewer().setCenter(
						viewer.getSxMapViewer().getMapCanvas().getCenter().x + dx,
						viewer.getSxMapViewer().getMapCanvas().getCenter().y + dy );

					getMapContext().setLongLat(viewer.getCenter()[0], viewer.getCenter()[1]);
					break;
				default:
					mapState = NO_ACTION;
					break;
			}

			if ( isMenuShown == false)
			{
				//Контекстное меню показывать не надо и передеём управление стратегии текущегообъекта
				MapElement myMapElement = mapContext.getCurrentMapElement();

				MapStrategy myStrategy = myMapElement.getMapStrategy(parent.getContext(), this, me, me.getPoint());
				myStrategy.doContextChanges();

				Dispatcher disp = parent.getContext().getDispatcher();
				if(disp != null)
				{
					perform_processing = false;
					disp.notify(new OperationEvent(this, 0, "mapselectionchangeevent"));
					perform_processing = true;
				}
			}

			if(mapState != MOVE_HAND_BUTTON)
				mapState = NO_ACTION;

			//Убираем флаг
			if ( isMenuShown == true)
			{
				isMenuShown = false;
			}
		}
		postDirtyEvent();
		postPaintEvent();
		setMode(NULL_MODE);//Для мыши
	}

	public void dragEnter(DropTargetDragEvent dtde)
	{
	}

	public void dragExit(DropTargetEvent dte)
	{
	}

	public void dragOver(DropTargetDragEvent dtde)
	{
	}

	//Здесь мы получаем объект который пользователь переносит с панели
	public void drop(DropTargetDropEvent dtde)
	{
		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		boolean do_create = false;
		boolean do_cable = false;
		boolean do_path = false;

		MapProtoElement mpe = null;
		MapPhysicalLinkProtoElement mplpe = null;
		MapTransmissionPathProtoElement mtppe = null;

		SchemeElement se = null;
		SchemeCableLink scl = null;
		SchemePath sp = null;

		ObjectResource or = null;

		Point myPoint = dtde.getLocation();
		SxDoublePoint mySXPoint = convertScreenToMap(myPoint);

		if ( mapContext != null)
		{
			DataFlavor[] df = dtde.getCurrentDataFlavors();
			Transferable transferable = dtde.getTransferable();
			try
			{
				if (df[0].getHumanPresentableName().equals("ElementLabel"))
				{
					mpe = (MapProtoElement )transferable.getTransferData(df[0]);
					do_create = true;
				}

				if (df[0].getHumanPresentableName().equals("SchemeElementLabel"))
				{
					
					or = (ObjectResource )transferable.getTransferData(df[0]);
					if(or instanceof SchemeElement)
					{
						se = (SchemeElement )or;
						mpe = se.mpe;
						do_create = true;
					}
					else
					if(or instanceof SchemeCableLink)
					{
						scl = (SchemeCableLink )or;
						mplpe = scl.mplpe;
						do_cable = true;
					}
					else
					if(or instanceof SchemePath)
					{
						sp = (SchemePath )or;
						mtppe = sp.mtppe;
						do_path = true;
					}
				}

				if(do_create)
				{
					if(se != null)
					{
						MapEquipmentNodeElement mene = findElement(se.getId());
						if(mene != null)
						{
							do_create = false;
							mene.setAnchor(mySXPoint);
						}
					}
				}
				if(do_create)
				{
					placeElement(se, mpe, mySXPoint);
				}
				else
				if(do_cable)
				{
					placeElement(scl, mplpe);
				}
				else
				if(do_path)
				{
					placeElement(sp, mtppe);
				}
				else
				{
					dtde.rejectDrop();
				}

				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				dtde.getDropTargetContext().dropComplete(true);
				postDirtyEvent();
				postPaintEvent();

				Dispatcher disp = parent.getContext().getDispatcher();
				if(disp != null)
				{
					perform_processing = false;
					disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
					perform_processing = true;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void placeElement(SchemePath sp, MapTransmissionPathProtoElement mtppe)
	{
		if(sp == null)
			return;

		// fill in plink_ids. assume plink_ids are sorted!
		Vector plink_ids = new Vector();

		PathElement pes[] = new PathElement[sp.links.size()];
		for(int i = 0; i < sp.links.size(); i++)
		{
			PathElement pe = (PathElement )sp.links.get(i);
			pes[pe.n] = pe;
		}
		for(int i = 0; i < pes.length; i++)
		{
			PathElement pe = pes[i];
			if(pe.is_cable)
			{
				MapPhysicalLinkElement mple = findPhysicalLink(pe.link_id);
				if(mple == null)
				{
					System.out.println("Place cables first!!!");
					return;
				}
				plink_ids.add(mple.getId());
			}
		}

		MapTransmissionPathElement mtpe = findPath(sp.getId());
		if(mtpe != null)
		{
			mtpe.physicalLink_ids = plink_ids;

			mtpe.select();
			return;
		}

		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		MapTransmissionPathElement thePath;
		if ( mapMainFrame.aContext.getApplicationModel()
				.isEnabled("mapActionCreatePath"))
		{
			Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMapContext().scheme_id);

//			MapEquipmentNodeElement smne = findElement(scheme.getTopLevelElement(sp.start_device_id).getId());
			MapEquipmentNodeElement smne = findElement(scheme.getTopologicalElement(sp.start_device_id).getId());
//			MapEquipmentNodeElement emne = findElement(scheme.getTopLevelElement(sp.end_device_id).getId());
			MapEquipmentNodeElement emne = findElement(scheme.getTopologicalElement(sp.end_device_id).getId());

			if(smne == null || emne == null)
				return;

			thePath = new MapTransmissionPathElement(
					dataSource.GetUId( MapPhysicalLinkElement.typ ),
					smne, 
					emne,
					getMapContext());
			Pool.put(MapTransmissionPathElement.typ, thePath.getId(), thePath);

			thePath.type_id = mtppe.getId();
			thePath.attributes = ResourceUtil.copyAttributes(dataSource, mtppe.attributes);
			thePath.physicalLink_ids = plink_ids;
			thePath.PATH_ID = sp.getId();
			thePath.name = sp.getName();

			getMapContext().getTransmissionPath().add(thePath);
		}
	}
	
	public void placeElement(SchemeCableLink scl, MapPhysicalLinkProtoElement mplpe)
	{
		MapPhysicalLinkElement mple = findPhysicalLink(scl.getId());
		if(mple != null)
		{
			MapEquipmentNodeElement smne;
			MapEquipmentNodeElement emne;
			try
			{
				Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMapContext().scheme_id);

//				SchemeElement se = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				SchemeElement se = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				smne = findElement(se.getId());
//				SchemeElement se2 = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				SchemeElement se2 = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				emne = findElement(se2.getId());
			}
			catch(Exception ex)
			{
				System.out.println("Place nodes first!!!");
				return;
			}
			if(smne != null && emne != null)
			{
				mple.startNode = smne; 
				mple.endNode = emne;
			}

			mple.select();
			return;
		}

		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		MapPhysicalLinkElement theLink;
		if ( mapMainFrame.aContext.getApplicationModel()
				.isEnabled("mapActionCreateLink"))
		{
			if(scl == null)
				return;

			Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMapContext().scheme_id);

			MapEquipmentNodeElement smne;
			MapEquipmentNodeElement emne;
			try
			{
//				SchemeElement se = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				SchemeElement se = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				smne = findElement(se.getId());
//				SchemeElement se2 = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				SchemeElement se2 = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				emne = findElement(se2.getId());
			}
			catch(Exception ex)
			{
				System.out.println("Place nodes first!!!");
				return;
			}
			

			if(smne == null || emne == null)
				return;

			MapNodeLinkElement myNodeLink = new MapNodeLinkElement( 
				dataSource.GetUId( MapNodeLinkElement.typ),
				smne, 
				emne, 
				getMapContext());
			Pool.put(MapNodeLinkElement.typ, myNodeLink.getId(), myNodeLink);
			getMapContext().getNodeLinks().add(myNodeLink);

			theLink = new MapPhysicalLinkElement(
					dataSource.GetUId( MapPhysicalLinkElement.typ ),
					smne, 
					emne,
					getMapContext());

			theLink.type_id = mplpe.getId();
			theLink.attributes = ResourceUtil.copyAttributes(dataSource, mplpe.attributes);
			theLink.nodeLink_ids.add( myNodeLink.getId());
			theLink.link_type_id = scl.cable_link_type_id;
			theLink.LINK_ID = scl.getId();
			theLink.name = scl.getName();

			Pool.put( MapPhysicalLinkElement.typ, theLink.getId(), theLink );
			getMapContext().getPhysicalLinks().add(theLink);
		}
	}
	
	public void placeElement(SchemeElement se, MapProtoElement mpe, SxDoublePoint mySXPoint)
	{
		MapEquipmentNodeElement mene = findElement(se.getId());
		if(mene != null)
		{
			mene.select();
			return;
		}

		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		MapNodeElement theNode = null;

		if ( mapMainFrame.aContext.getApplicationModel()
				.isEnabled("mapActionCreateEquipment"))
		{
			theNode = new MapEquipmentNodeElement(
					dataSource.GetUId( MapEquipmentNodeElement.typ),
					mySXPoint,
					getMapContext(),
					getMapContext().defaultScale/getMapContext().currentScale,
					mpe);
			theNode.attributes = ResourceUtil.copyAttributes(dataSource, mpe.attributes);
			theNode.name = se.getName();

			Pool.put( ((MapEquipmentNodeElement)(theNode)).getTyp(), theNode.getId(), theNode );
			mapContext.getNodes().add(theNode);

			if(se != null)
			{
				MapEquipmentNodeElement en = (MapEquipmentNodeElement )theNode;
				en.element_type_id = se.proto_element_id;
				en.element_id = se.getId();
			}
		}

		if(theNode != null)
			theNode.setScaleCoefficient(getMapContext().defaultScale / this.getMapContext().getLogicalNetLayer().getScale());
	}

	public MapEquipmentNodeElement findElement(String id)
	{
		for(Iterator it = getMapContext().getNodes().iterator(); it.hasNext();)
		{
			MapNodeElement node = (MapNodeElement )it.next();
			if(node instanceof MapEquipmentNodeElement)
			{
				MapEquipmentNodeElement en = (MapEquipmentNodeElement )node;
				if(en.element_id.equals(id))
					return en;
			}
		}
		return null;
	}

	public MapPhysicalLinkElement findPhysicalLink(String id)
	{
		for(Iterator it = getMapContext().getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link.LINK_ID.equals(id))
				return link;
		}
		return null;
	}

	public MapTransmissionPathElement findPath(String id)
	{
		for(Iterator it = getMapContext().getTransmissionPath().iterator(); it.hasNext();)
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )it.next();
			if(path.PATH_ID.equals(id))
				return path;
		}
		return null;
	}

	public void dragGestureRecognized( DragGestureEvent event)
	{
	}

	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	}

	public void release()
	{
		super.release();
	}

//Операция удаления
	public void delete()
	{
		MapStrategy myStrategy = new VoidStrategy();
		myStrategy = new DeleteSelectionStrategy(parent.getContext(), this);

		myStrategy.doContextChanges();
		postDirtyEvent();
		postPaintEvent();
	}

//Устанавить курсор
	public void setCursor(Cursor myCursor)
	{
		mapCanvas.setCursor(myCursor);
	}

//Устанавливаем Latitude и Longitude
	public void setLatLong ( Point point)
	{
		try
		{
			java.text.DecimalFormat df2 = new java.text.DecimalFormat("###,##0.0000");

			parent.mapToolBar.LatitudeTextField.setText(df2.format(this.convertScreenToMap(point).y));
			parent.mapToolBar.LongitudeField.setText(df2.format(this.convertScreenToMap(point).x));
		}
		catch(Exception e)
		{
			//      e.printStackTrace();
		}
	}

//Получить переменную mode
	public int getMode()
	{
		return mode;
	}

//Установить переменную mode
	public void setMode(int myMode)
	{
		mode = myMode;
	}

//Получить переменную actionMode
	public int getActionMode()
	{
		return actionMode;
	}

//Установить переменную actionMode
	public void setActionMode(int myMode)
	{
		actionMode = myMode;
	}

//Получить переменную actionMode
	public int getMapState()
	{
		return mapState;
	}

//Установить переменную actionMode
	public void setMapState(int myState)
	{
		mapState = myState;
	}

//Получить startX startY ввиде типа Point
	public Point getStartPoint()
	{
//    this.getMapViewer().
		return new Point(startX, startY);
	}

//Установить startX startY
	public void setStartPoint(Point myPoint)
	{
		startX = myPoint.x;
		startY = myPoint.y;
	}

//Получить endX endX ввиде типа Point
	public Point getEndPoint()
	{
		return new Point(endX, endY);
	}

//Установить endX endY
	public void setEndPoint(Point myPoint)
	{
		endX = myPoint.x;
		endY = myPoint.y;
	}

	public void setMapContext( MapContext myMapContext)
	{
		if(this.mapContext != null)
		{
			this.mapContext.setLogicalNetLayer(null);
			this.mapContext.removeMarkers();
		}
		this.mapContext = myMapContext;
		if(this.mapContext != null)
			mapContext.setLogicalNetLayer(this);
	}

	public MapContext getMapContext()
	{
		return this.mapContext;
	}

	static public double distance(SxDoublePoint from, SxDoublePoint to)
	{
		double a1 = from.x * 3.14 / 180;
		double a2 = from.y * 3.14 / 180;
		double b1 = to.x * 3.14 / 180;
		double b2 = to.y * 3.14 / 180;

		double r = 6400000;

		double d;

		d = r * Math.sqrt(
			( Math.cos(a1) * Math.cos(a2) - Math.cos(b1) * Math.cos(b2)) *
			( Math.cos(a1) * Math.cos(a2) - Math.cos(b1) * Math.cos(b2)) +

			( Math.sin(a1) * Math.cos(a2) - Math.sin(b1) * Math.cos(b2)) *
			( Math.sin(a1) * Math.cos(a2) - Math.sin(b1) * Math.cos(b2)) +

			( Math.sin(a2) - Math.sin(b2)) * ( Math.sin(a2) - Math.sin(b2)) );

		return d;
/*
		SxDoublePoint from1 = getLogicalNetLayer().convertMapToLatLong(from);
		SxDoublePoint to1 = getLogicalNetLayer().convertMapToLatLong(to);

		return 100000.0 * getLogicalNetLayer().mapMainFrame.myMapViewer.getSxMapViewer().distance(from1.x, from1.y, to1.x, to1.y);
*/
/*
		double a1 = (to.x - from.x) * (to.x - from.x);
		double a2 = (to.y - from.y) * (to.y - from.y);

		double d = Math.sqrt(a1 + a2);

		return d;
*/
	}



/*
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
// Moved from MapContext
////////////////////////////////////////////////////////////////////////

	protected MapElement curMapElement;//Текущий элемент

	static final public int SHOW_NODE_LINK = 1;//Показывать nodeLink
	static final public int SHOW_PHYSICALLINK = 2;//Показывать physicalLink
	static final public int SHOW_TRANSMISSION_PATH = 3;//Показывать transmissionPath

	public int linkState = SHOW_PHYSICALLINK;//Перменная показывающая что сейчас отображать

//Установить текущий элемент по коордитате на карте
	public void setCurrentMapElement (Point myPoint)
	{
		MapContext mc = getMapContext();
		//Здесь пробегаемся по всем элементам и если на каком-нибудь из них курсор
		//то устанавливаем его текущим элементом
		for (int i = 0;i < mc.markers.size(); i++)
		{
			MapMarker myMarer = (MapMarker )mc.markers.get(i);
			if(myMarer.isMouseOnThisObject(myPoint))
			{
				curMapElement = myMarer;
				return;
			}
		}
//A0A
		Enumeration e = mc.getAllElements().elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			if (mapElement.isMouseOnThisObject(myPoint))
			{
				if ( mapElement instanceof MapNodeElement)
				{
					notifySchemeEvent(mapElement);
					notifyCatalogueEvent(mapElement);
				}
				if ( mapElement instanceof MapNodeLinkElement)
				{
					//Здесь смотрим по флагу linkState что делать
					if ( linkState == SHOW_NODE_LINK)
					{
						curMapElement = mapElement;
						notifySchemeEvent(curMapElement);
						notifyCatalogueEvent(curMapElement);
						return;
					}

					if ( linkState == SHOW_PHYSICALLINK)
					{
						curMapElement = mc.getPhysicalLinkbyNodeLink( mapElement.getId());
						notifySchemeEvent(curMapElement);
						notifyCatalogueEvent(curMapElement);
						return;
					}

					if ( linkState == SHOW_TRANSMISSION_PATH)
					{
						Dispatcher dispatcher = mapMainFrame.aContext.getDispatcher();

						Enumeration e1 = mc.getTransmissionPathByNodeLink( mapElement.getId()).elements();
						while( e1.hasMoreElements())
						{
							MapTransmissionPathElement path = (MapTransmissionPathElement)e1.nextElement();
							path.select();

							perform_processing = false;
							MapNavigateEvent mne = new MapNavigateEvent(mc, MapNavigateEvent.MAP_PATH_SELECTED_EVENT);
							mne.mappathID = path.getId();
							dispatcher.notify(mne);
							perform_processing = true;

							notifySchemeEvent(path);
							notifyCatalogueEvent(path);

							curMapElement = path;
							return;
						}

					}
				}
				curMapElement = mapElement;
				return;
			}
		}

		curMapElement = new VoidMapElement(this);
	}

	public void notifySchemeEvent(MapElement mapElement)
	{
		SchemeNavigateEvent sne;
		Dispatcher dispatcher = mapMainFrame.aContext.getDispatcher();
		try 
		{
			MapEquipmentNodeElement mapel = (MapEquipmentNodeElement )mapElement;

			if(mapel.element_id != null && !mapel.element_id.equals(""))
			{
				SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mapel.element_id);
//				System.out.println("notify SCHEME_ELEMENT_SELECTED_EVENT " + se.getId());
				perform_processing = false;
				sne = new SchemeNavigateEvent(
						new SchemeElement[] { se },
						SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT);
				dispatcher.notify(sne);
				perform_processing = true;
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
				perform_processing = false;
				sne = new SchemeNavigateEvent(
						new SchemeCableLink[] { scl },
						SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT);
				dispatcher.notify(sne);
				perform_processing = true;
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
				perform_processing = false;
				sne = new SchemeNavigateEvent(
						new SchemePath[] { sp },
						SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT);
				dispatcher.notify(sne);
				perform_processing = true;
				return;
			}
		} 
		catch (Exception ex){ } 
	}

	public void notifyCatalogueEvent(MapElement mapElement)
	{
		CatalogNavigateEvent cne;
		Dispatcher dispatcher = mapMainFrame.aContext.getDispatcher();
		try 
		{
			MapEquipmentNodeElement mapel = (MapEquipmentNodeElement )mapElement;

			if(mapel.element_id != null && !mapel.element_id.equals(""))
			{
				SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mapel.element_id);
				
				if(se.equipment_id != null && !se.equipment_id.equals(""))
				{
					Equipment eq = (Equipment )Pool.get(Equipment.typ, se.equipment_id);
//					System.out.println("notify CATALOG_EQUIPMENT_SELECTED_EVENT " + eq.getId());
					perform_processing = false;
					cne = new CatalogNavigateEvent(
						new Equipment[] { eq },
						CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT);
					dispatcher.notify(cne);
					perform_processing = true;
					return;
				}
			}
		} 
		catch (Exception ex){ }

		try
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )mapElement;

			if(link.LINK_ID != null && !link.LINK_ID.equals(""))
			{
				SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
				
				if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
				{
					CableLink cl = (CableLink )Pool.get(CableLink.typ, scl.cable_link_id);
//					System.out.println("notify CATALOG_EQUIPMENT_SELECTED_EVENT " + cl.getId());
					perform_processing = false;
					cne = new CatalogNavigateEvent(
						new CableLink[] { cl },
						CatalogNavigateEvent.CATALOG_CABLE_LINK_SELECTED_EVENT);
					dispatcher.notify(cne);
					perform_processing = true;
					return;
				}
			}
		} 
		catch (Exception ex){ }

		try 
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )mapElement;

			if(path.PATH_ID != null && !path.PATH_ID.equals(""))
			{
				SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
				if(sp.path_id != null && !sp.path_id.equals(""))
				{
					TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
//					System.out.println("notify CATALOG_PATH_SELECTED_EVENT " + tp.getId());
					perform_processing = false;
					cne = new CatalogNavigateEvent(
						new TransmissionPath[] { tp }, 
						CatalogNavigateEvent.CATALOG_PATH_SELECTED_EVENT);
					dispatcher.notify(cne);
					perform_processing = true;
				}
			}
		} 
		catch (Exception ex){ } 
	}


//Получить текущий элемент
	public MapElement getCurrentMapElement()
	{
		return curMapElement;
	}

//Получить текущий элемент по коордитате на карте
	public MapElement getCurrentMapElement(Point myPoint)
	{
		MapContext mc = getMapContext();
		MapElement curME = new VoidMapElement(this);
		Enumeration e = mc.getAllElements().elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			if ( mapElement.isMouseOnThisObject(myPoint))
			{
				if ( mapElement instanceof MapNodeLinkElement)
				{
					//Здесь смотрим по флагу linkState что делать
					if ( linkState == SHOW_NODE_LINK)
					{
						curME = mapElement;
						break;
					}

					if ( linkState == SHOW_PHYSICALLINK)
					{
						curME = mc.getPhysicalLinkbyNodeLink( mapElement.getId());
						break;
					}

					if ( linkState == SHOW_TRANSMISSION_PATH)
					{
						Enumeration e1 = mc.getTransmissionPathByNodeLink( mapElement.getId()).elements();
						if( e1.hasMoreElements())
						{
							MapTransmissionPathElement path = (MapTransmissionPathElement)e1.nextElement();

							curME = path;
							break;
						}

					}
				}
				curME = mapElement;
				break;
			}
		}
		return curME;
	}
*/

}
