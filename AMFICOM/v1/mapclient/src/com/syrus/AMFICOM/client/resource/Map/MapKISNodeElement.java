package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.CORBA.Map.MapElement_Transferable;
import com.syrus.AMFICOM.Client.Configure.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Configure.Map.Popup.KISElementPopupMenu;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.AddLinkStrategy;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.ChangeEndPointStrategy;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.MoveSelectionStrategy;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.Configure.Map.UI.MapKISPane;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import java.io.IOException;

import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class MapKISNodeElement extends MapEquipmentNodeElement
{
	private static final long serialVersionUID = 01L;
	public String ism_map_id = "";
	public String map_kis_id = "";
	public boolean has_kis = true;

	static final public String typ = "mapkiselement";

	public MapKISNodeElement()
	{
		super();
	}

	public MapKISNodeElement(MapElement_Transferable Tmyransferable)
	{
		this.transferable = Tmyransferable;
		setLocalFromTransferable();
	}

	public MapKISNodeElement(
		String myID,
		String myElementID,
		SxDoublePoint myAnchor,
		MapContext myMapContext,
		double coef,
		MapProtoElement pe)
	{
		this(myID, myElementID, myAnchor, myMapContext, coef, pe.getImageID(), pe.getId(), pe.getEquipmentTypeId());
		name = pe.getName();
	}

	public MapKISNodeElement (
			String myID,
			String myElementID,
			SxDoublePoint myAnchor,
			MapContext myMapContext,
			double coef,
			String myImageID,
			String typeID,
			String KIStype_id)
	{
		super(myElementID, myAnchor, myMapContext, coef, myImageID, typeID, KIStype_id);
		map_kis_id = myID;

		if(mapContext instanceof ISMMapContext)
		{
			ism_map_id = ((ISMMapContext)mapContext).ISM_id;
		}
	}

//Устанавливаем переменные класса из базы данных
	public void setLocalFromTransferable()
	{
		super.setLocalFromTransferable();
		this.ism_map_id = transferable.ism_map_id;
		this.map_kis_id = transferable.map_kis_id;
		this.has_kis = transferable.has_kis;
	}

//Передаём переменные в transferable которая используется для передачи их в базу данных
	public void setTransferableFromLocal()
	{
		super.setTransferableFromLocal();
		transferable.ism_map_id = mapContext.getId();
		transferable.map_kis_id = map_kis_id;
		transferable.has_kis = has_kis;
	}

	public String getTyp()
	{
		return typ;
	}

	public String getId()
	{
		return map_kis_id;
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new MapKISPane();
	}

/////////////////////////////////////// AAAA

	public void paint (Graphics g)
	{
		super.paint(g);
		Point p = getLogicalNetLayer().convertMapToScreen( new SxDoublePoint(
				getAnchor().x, 
				getAnchor().y) );

		Graphics2D pg = ( Graphics2D)g;
		pg.setStroke(new BasicStroke(getSelectedNodeLineSize()));

		pg.drawImage(
				icon,
                p.x-icon.getWidth(getLogicalNetLayer())/2,
                p.y-icon.getHeight(getLogicalNetLayer())/2,
                getLogicalNetLayer());

		pg.setColor( new Color(255, 0, 255));
		pg.drawOval(p.x-icon.getWidth( getLogicalNetLayer()) / 2 + 1,
					p.y-icon.getHeight( getLogicalNetLayer()) / 2 + 1,
					5,
					5);
		pg.setColor( new Color(0, 255, 0));
		pg.drawOval(p.x-icon.getWidth( getLogicalNetLayer()) / 2 + 2,
					p.y-icon.getHeight( getLogicalNetLayer()) / 2 + 2,
					3,
					3);
	}


//Кнтекстное меню
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		KISElementPopupMenu menu = new KISElementPopupMenu(myFrame, this);
		return menu;
	}

	public void finalUpdate()
	{
	}

//здесь обрабативаются события связанные с действие над классом
	public MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		int mode = logicalNetLayer.getMode();
		int actionMode = logicalNetLayer.getActionMode();

		MapStrategy strategy = new VoidStrategy();
		Point myPoint = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
//A0A
			if ((actionMode != LogicalNetLayer.SELECT_ACTION_MODE) &&
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();
/*			
			if(this.KIS_id != null && !this.KIS_id.equals(""))
			{
				Dispatcher dispatcher = logicalNetLayer.mapMainFrame.aContext.getDispatcher();
				dispatcher.notify(new MapNavigateEvent(this, MapNavigateEvent.MAP_KIS_SELECTED_EVENT, this.KIS_id));
			}
*/
			if (actionMode == LogicalNetLayer.MOVE_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_DRAGGED)
			{
				//Если разрешено то перемещаем объект
				if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMoveKIS"))
				{
					return new MoveSelectionStrategy( aContext, me, logicalNetLayer);
				}
			}

			if (actionMode == LogicalNetLayer.NULL_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_DRAGGED)
			{
				//Это используется для рисования линии (NodeLink)
				logicalNetLayer.setActionMode(LogicalNetLayer.DRAW_LINES_ACTION_MODE) ;
				return  new ChangeEndPointStrategy(aContext, me, logicalNetLayer);
			}

	//A0A
			if (actionMode == LogicalNetLayer.DRAW_LINES_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_RELEASED)
			{
				logicalNetLayer.setActionMode(LogicalNetLayer.NULL_ACTION_MODE) ;
				MapElement curElementAtPoint = logicalNetLayer.getMapContext().getCurrentMapElement(myPoint);

	//Проверка того что элемент под курсором является объестом типа MapNodeElement,
	//VoidMapElement и не является текущем
				if (( (curElementAtPoint instanceof MapNodeElement) ||
					(curElementAtPoint instanceof VoidMapElement) ) &&
						(curElementAtPoint != this ) )
				{
					MapNodeElement myNode ;

					if ( (curElementAtPoint instanceof VoidMapElement))
					{
						myNode = null;
					}
					else
					{
						myNode = (MapNodeElement)curElementAtPoint;
					}

					if (curElementAtPoint instanceof MapPhysicalNodeElement)
					{
						MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)curElementAtPoint ;

							//Проверка того, что можно создать NodeLink
						if (! physicalNodeElement1.isActive() )
						{
							if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionCreateLink"))
							{
								return new AddLinkStrategy( aContext, logicalNetLayer, myNode, myPoint);
							}
						}
					}
					else
					{
								//Проверка того, что можно создать NodeLink
						if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionCreateLink"))
						{
							return new AddLinkStrategy( aContext, logicalNetLayer, myNode, myPoint);
						}
					}
				}
			}
		}//SwingUtilities.isLeftMouseButton(me)
    return strategy;
	}

//Проверка того что объект можно перемещать
	public boolean isMovable()
	{
		if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMoveKIS"))
		{
			return true;
		}
		return false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(type_id);
		out.writeObject(description);
		out.writeDouble(anchor.x);
		out.writeDouble(anchor.y);
		out.writeObject(owner_id);
		out.writeObject(mapContextID);
		out.writeObject(this.getImageID());
		out.writeObject(element_id);
		out.writeObject(element_type_id);
		out.writeObject(ism_map_id);

		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		type_id = (String )in.readObject();
		description = (String )in.readObject();
		anchor = new Point2D.Double( );
		anchor.x = in.readDouble();
		anchor.y = in.readDouble();
		owner_id = (String )in.readObject();
		mapContextID = (String )in.readObject();
		this.setImageID((String )in.readObject());
		element_id = (String )in.readObject();
		element_type_id = (String )in.readObject();
		ism_map_id = (String )in.readObject();
		attributes = (Hashtable )in.readObject();

		transferable = new MapElement_Transferable();
//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}
}

