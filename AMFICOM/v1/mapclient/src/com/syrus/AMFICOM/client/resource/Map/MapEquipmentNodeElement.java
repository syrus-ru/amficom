package com.syrus.AMFICOM.Client.Resource.Map;

import java.io.*;
import javax.swing.*;
import com.ofx.geometry.SxDoublePoint;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

/// 777
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Map.Popup.*;
import com.syrus.AMFICOM.Client.Map.Strategy.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Map.UI.Display.*;
import com.syrus.AMFICOM.Client.General.Model.*;

//A0A
public class MapEquipmentNodeElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 01L;
	public MapElement_Transferable transferable;

	static final public String typ = "mapequipmentelement";

	public String element_id = "";
	public String element_type_id = "";

	public MapEquipmentNodeElement()
	{
		transferable = new MapElement_Transferable();
	}

	public MapEquipmentNodeElement(MapElement_Transferable Tmyransferable)
	{
		this.transferable = Tmyransferable;
		setLocalFromTransferable();
	}

	public MapEquipmentNodeElement(
		String myID,
		SxDoublePoint myAnchor,
		MapContext myMapContext,
		double coef,
		MapProtoElement pe)
	{
		this(myID, myAnchor, myMapContext, coef, pe.getImageID(), pe.getId(), pe.getEquipmentTypeId());
		name = pe.getName();
	}

	public MapEquipmentNodeElement(
		String myID,
		SxDoublePoint myAnchor,
		MapContext myMapContext,
		double coef,
		String myImageID,
		String typeID,
		String eq_type_id)
	{
		mapContext = myMapContext;

		id = myID;
		name = myID;
		type_id = typeID;
		description = "";
		setAnchor(myAnchor);
		owner_id = "";
		if(mapContext != null)
			mapContextID = mapContext.getId();
		attributes = new Hashtable();
		element_id = "";
		element_type_id = eq_type_id;
		setScaleCoefficient(coef);
		setImageID( myImageID);
		selected = false;

		transferable = new MapElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapEquipmentNodeElement.typ, cloned_id);

//		mapContextID = (String )Pool.get("mapclonedids", mapContext.id);
//		MapContext mc = (MapContext )Pool.get(MapContext.typ, mapContextID);
		MapEquipmentNodeElement mene = new MapEquipmentNodeElement(
				dataSource.GetUId(MapEquipmentNodeElement.typ),
				new SxDoublePoint(anchor.x, anchor.y),
				(MapContext )mapContext.clone(dataSource), 
				scaleCoefficient,
				imageID,
				type_id,
				element_type_id);
				
		mene.bounds = new Rectangle(bounds);
		mene.alarmState = alarmState;
		mene.changed = changed;
		mene.description = description;
		mene.element_id = (String )Pool.get("schemeclonedids", element_id);
		mene.element_type_id = element_type_id;
		mene.name = name;
		mene.optimizerAttribute = optimizerAttribute;
		mene.owner_id = dataSource.getSession().getUserId();
		mene.scaleCoefficient = scaleCoefficient;
		mene.selected = selected;
		mene.selectedNodeLineSize = selectedNodeLineSize;
		mene.showAlarmState = showAlarmState;
		mene.type_id = type_id;

		Pool.put(MapEquipmentNodeElement.typ, mene.getId(), mene);
		Pool.put("mapclonedids", id, mene.getId());

		mene.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mene.attributes.put(ea2.type_id, ea2);
		}

		return mene;
	}

	public void updateAttributes()
	{
		attributes.clear();
	    for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, Pool.get("attribute", transferable.attributes[i].id));
	}

	//Устанавливаем переменные класса из базы данных
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.type_id = transferable.type_id;
		this.description = transferable.description;
		this.anchor.x = Double.parseDouble(transferable.longitude);
		this.anchor.y = Double.parseDouble(transferable.latitude);
		this.owner_id = transferable.owner_id;
		this.mapContextID = transferable.map_id;
		this.setImageID( transferable.symbol_id);
		this.element_id = transferable.element_id;
		this.element_type_id = transferable.element_type_id;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	//Передаём переменные в transferable которая используется для передачи их в базу данных
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.type_id = this.type_id;
		transferable.description = this.description;
		transferable.longitude = String.valueOf(this.anchor.x);
		transferable.latitude = String.valueOf(this.anchor.y);
		transferable.owner_id = mapContext.user_id ;
		transferable.map_id = mapContext.getId();
		transferable.symbol_id = this.imageID;
		transferable.element_id = this.element_id;
		transferable.element_type_id = this.element_type_id;

		transferable.map_kis_id = "";
		transferable.has_kis = false;
		transferable.kis_element_type_id = "";
		transferable.ism_map_id = "";

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Enumeration e = attributes.elements(); e.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute)e.nextElement();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	//Используется для для загрузки класса из базы данных
	public void updateLocalFromTransferable()
	{
		this.mapContext = (MapContext)Pool.get("mapcontext", this.mapContextID);
	}

	public ObjectResourceModel getModel()
	{
		return new MapEquipmentNodeElementModel(this);
	}
	
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapEquipmentNodeElementDisplayModel();
	}

	public Enumeration getChildTypes()
    {
		Vector vec = new Vector();
        vec.add(MapPhysicalLinkElement.typ);
		return vec.elements();
    }
		
	public Class getChildClass(String key)
    {
		if(key.equals(MapPhysicalLinkElement.typ))
			return MapPhysicalLinkElement.class;
        return ObjectResource.class;
    }
		
	public Enumeration getChildren(String key)
	{
		Vector vec = new Vector();
//		System.out.println("get CP children, count = " + connectionPoints.ports.size());
		if(key.equals(MapPhysicalLinkElement.typ))
		{
			return mapContext.getPhysicalLinksContainingNode(this).elements();
		}
		return vec.elements();
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new MapEquipmentPane();
	}

/////////////////////////////////////// AAAA

	//Кнтекстное меню
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		EquipmentNodeElementPopupMenu menu = new EquipmentNodeElementPopupMenu(myFrame, this);
		return menu;
	}

	public Object getTransferable()
	{
		return transferable;
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

			if (actionMode == LogicalNetLayer.MOVE_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_DRAGGED )
			{
				//Если разрешено то перемещаем объект
				if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMoveEquipment"))
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

			if (actionMode == LogicalNetLayer.DRAW_LINES_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_RELEASED)
			{
				logicalNetLayer.setActionMode(LogicalNetLayer.NULL_ACTION_MODE) ;
				MapElement curElementAtPoint = logicalNetLayer.getMapContext().getCurrentMapElement(myPoint);

				//Проверка того что элемент под курсором является объестом типа MapNodeElement,
				//VoidMapElement и не является текущем
				if (( 	(curElementAtPoint instanceof MapNodeElement) ||
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
		if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMoveEquipment"))
		{
			return true;
		}
		return false;
	}

	double physical_length = 0.0;

	public double getPhysicalLength()
	{
		return physical_length;
	}
	
	//Возвращяет длинну линий внутри данного узла,
	//пересчитанную на коэффициент топологической привязки
	public PathElement countPhysicalLength(SchemePath sp, PathElement pe, Enumeration pathelements)
	{
		physical_length = 0.0;
		if(this.element_id == null || this.element_id.equals(""))
			return pe;
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, this.element_id);

		Vector vec = new Vector();
		Enumeration e = se.getAllSchemesLinks();
		for(;e.hasMoreElements();)
			vec.add(e.nextElement());

		for(;;)
		{
			if(pe.is_cable)
			{
				SchemeCableLink schemeCableLink =
						(SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				if(schemeCableLink == null)
				{
					System.out.println("Something wrong... - schemeCableLink == null");
					return pe;
				}
				if(!vec.contains(schemeCableLink))
					return pe;
				physical_length += schemeCableLink.getPhysicalLength();
			}
			else
			{
				SchemeLink schemeLink =
						(SchemeLink )Pool.get(SchemeLink.typ, pe.link_id);
				if(schemeLink == null)
				{
					System.out.println("Something wrong... - schemeLink == null");
					return pe;
				}
				if(!vec.contains(schemeLink))
					return pe;
				physical_length += schemeLink.getPhysicalLength();
			}
			if(pathelements.hasMoreElements())
				pe = (PathElement )pathelements.nextElement();
			else
				return null;
		}
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
		attributes = (Hashtable )in.readObject();

		transferable = new MapElement_Transferable();
//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}
}
