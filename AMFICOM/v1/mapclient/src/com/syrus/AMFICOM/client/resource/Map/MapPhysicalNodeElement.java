package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.CORBA.Map.MapPhysicalNodeElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Popup.PhysicalNodeElementPopupMenu;
import com.syrus.AMFICOM.Client.Map.Strategy.AddLinkStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.ChangeEndPointStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MoveFixedDistanceStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MoveSelectionStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapPhysicalNodeElementDisplayModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

//A0A
public class MapPhysicalNodeElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "mapnodeelement";
	protected String physicalLinkID = "";
	protected boolean active = true;//Флаг показывающий закрыт ли узел
	//true значит что из узла выходит две линии, false одна
	public String ism_map_id = "";

	public MapPhysicalNodeElement_Transferable transferable;


	public MapPhysicalNodeElement()
	{
		transferable = new MapPhysicalNodeElement_Transferable();
	}

	public MapPhysicalNodeElement(MapPhysicalNodeElement_Transferable Tmyransferable)
	{
		this.transferable = Tmyransferable;
		setLocalFromTransferable();
	}

	public MapPhysicalNodeElement (
			String myID, 
			String myphysicalLinkID, 
			SxDoublePoint myAnchor,
            MapContext myMapContext,
            Rectangle myBounds)
	{
		mapContext = myMapContext;
		id = myID;
		name = myID;
		setAnchor(myAnchor);
		mapContextID = mapContext.id;
		if(mapContext instanceof ISMMapContext)
		{
			ism_map_id = ((ISMMapContext)mapContext).ISM_id;
		}
		setImageID("node");
		physicalLinkID = myphysicalLinkID;
		attributes = new Hashtable();

		setBounds( myBounds);
		selected = false;

		transferable = new MapPhysicalNodeElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapPhysicalNodeElement.typ, cloned_id);

//		mapContextID = (String )Pool.get("mapclonedids", mapContext.id);
//		MapContext mc = (MapContext )Pool.get(MapContext.typ, mapContextID);
		MapPhysicalNodeElement mpne = new MapPhysicalNodeElement(
				dataSource.GetUId(MapPhysicalNodeElement.typ),
				(String )Pool.get("mapclonedids", physicalLinkID),
				new SxDoublePoint(anchor.x, anchor.y),
				(MapContext )mapContext.clone(dataSource), 
				bounds);
				
		mpne.active = active;
		mpne.alarmState = alarmState;
		mpne.changed = changed;
		mpne.description = description;
		mpne.ism_map_id = ism_map_id;
		mpne.name = name;
		mpne.optimizerAttribute = optimizerAttribute;
		mpne.owner_id = dataSource.getSession().getUserId();
		mpne.scaleCoefficient = scaleCoefficient;
		mpne.selected = selected;
		mpne.selectedNodeLineSize = selectedNodeLineSize;
		mpne.showAlarmState = showAlarmState;
		mpne.type_id = type_id;

		Pool.put(MapPhysicalNodeElement.typ, mpne.getId(), mpne);
		Pool.put("mapclonedids", id, mpne.getId());

		mpne.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mpne.attributes.put(ea2.type_id, ea2);
		}

		return mpne;
	}

/////////////////////////////////////// AAAA
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.anchor.x = Double.parseDouble(transferable.longitude);
		this.anchor.y = Double.parseDouble(transferable.latitude);
		this.owner_id = transferable.owner_id;
		this.mapContextID = transferable.map_id;
		this.setImageID( transferable.symbol_id);
		this.physicalLinkID = transferable.physicalLinkID;
		this.active = transferable.active;
		this.ism_map_id = transferable.ism_map_id;
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.longitude = String.valueOf(this.anchor.x);
		transferable.latitude = String.valueOf(this.anchor.y);
		transferable.owner_id = mapContext.user_id ;
		transferable.map_id = mapContext.id;
		transferable.symbol_id = this.imageID;
		transferable.physicalLinkID = "";
		transferable.active = this.active;
		transferable.ism_map_id = this.ism_map_id;
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
		return this.id;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	//Используется для для загрузки класса из базы данных
	public void updateLocalFromTransferable()
	{
		this.mapContext = (MapContext)Pool.get("mapcontext", this.mapContextID);
	}

	public ObjectResourceModel getModel()
	{
		return new MapPhysicalNodeElementModel(this);
	}
	
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapPhysicalNodeElementDisplayModel();
	}

/////////////////////////////////////// AAAA
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		return null;//new PhysicalNodeElementPopupMenu(myFrame);
	}

	public Image get_image()
	{
		ImageIcon myImageIcon;
		if(this.getImageID().equals("node"))
			myImageIcon = new ImageIcon("images/node.gif");
		else
			myImageIcon = new ImageIcon("images/void.gif");
		return myImageIcon.getImage();
	}

	public void setActive()
	{
		setImageID("node");
		active = true;
	}

	public void setNonactive()
	{
		setImageID("void");
		active = false;
	}

	public boolean isActive()
	{
		return active;
	}

	public String getPhysicalLinkID()
	{
		return physicalLinkID;
	}

	public void paint (Graphics g)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();
		
		//Поверка того что их надо показывать
		if ( getMapContext().isPhysicalNodeElementVisible())
		{
			Point p = lnl.convertMapToScreen( this.getAnchor());

			Graphics2D pg = (Graphics2D)g;
			pg.setStroke(new BasicStroke(getSelectedNodeLineSize()));

			pg.drawImage(
					icon,
					p.x - icon.getWidth(lnl) / 2,
					p.y - icon.getHeight(lnl) / 2,
					lnl);

			if (isSelected())
			{
				pg.setColor( Color.red);
				pg.drawRect( 
						p.x - icon.getWidth( lnl) / 2,
						p.y - icon.getHeight( lnl) / 2,
						icon.getWidth( lnl),
						icon.getHeight( lnl));
			}
		}
	}

	public boolean isMouseOnThisObject (Point currentMousePoint)
	{
		if (getMapContext().isPhysicalNodeElementVisible())
			return super.isMouseOnThisObject(currentMousePoint);
		return false;
	}

	//Обработка событий связанных с NodeLink
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
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE))
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();

			//A0A
			if (actionMode == LogicalNetLayer.MOVE_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_DRAGGED)
			{
				if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMoveNode"))
				{
					return new MoveSelectionStrategy( aContext, me, logicalNetLayer);
				}
			}
			//A0A
			if (actionMode == LogicalNetLayer.FIXDIST_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_DRAGGED)
			{
				if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMoveNode"))
				{
					logicalNetLayer.getMapContext().deselectAll();
					select();
					return new MoveFixedDistanceStrategy( aContext, me, logicalNetLayer);
				}
			}
			//A0A
			if (actionMode == LogicalNetLayer.NULL_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_DRAGGED && !isActive())
			{
				logicalNetLayer.setActionMode(LogicalNetLayer.DRAW_LINES_ACTION_MODE) ;
				return  new ChangeEndPointStrategy(aContext, me, logicalNetLayer);
			}
			//A0A
			if (actionMode == LogicalNetLayer.DRAW_LINES_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_RELEASED)
			{
				//Рисуем линию
				logicalNetLayer.setActionMode(LogicalNetLayer.NULL_ACTION_MODE);
				MapElement curElementAtPoint = logicalNetLayer.getMapContext().getCurrentMapElement(myPoint);

				if (( (curElementAtPoint instanceof MapNodeElement) ||
					(curElementAtPoint instanceof VoidMapElement)) &&
					(curElementAtPoint != this ) &&
					!isActive() )
				{
					MapNodeElement myNode ;

					//Если точка сущуствуеи на карте то myNode != null
					if ( (curElementAtPoint instanceof VoidMapElement))
					{
						myNode = null;
					}
					else
					{
						myNode = (MapNodeElement)curElementAtPoint;
					}
					//A0A
					if (curElementAtPoint instanceof MapPhysicalNodeElement)
					{
						MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)curElementAtPoint ;

						if (! physicalNodeElement1.isActive() )
						{
							if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel()
									.isEnabled("mapActionCreateLink"))
							{
								setActive();
								return new AddLinkStrategy( aContext, logicalNetLayer, myNode, myPoint);
							}
						}
					}
					else
					{
						if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel()
								.isEnabled("mapActionCreateLink"))
						{
							setActive();
							return new AddLinkStrategy( aContext, logicalNetLayer, myNode, myPoint);
						}
					}
				}
			}
		}
		return strategy;
	}

	public void finalUpdate()
	{
	}

	public final static Rectangle defaultBounds = new Rectangle(10, 10);
	public final static Rectangle minBounds = new Rectangle(2, 2);
	public final static Rectangle maxBounds = new Rectangle(15, 15);

	public void setScaleCoefficient(double ss)
	{
		scaleCoefficient = ss;
		int w = (int )((double )defaultBounds.width * scaleCoefficient);
		int h = (int )((double )defaultBounds.height * scaleCoefficient);
		
		if (w >= maxBounds.width || h >= maxBounds.height )
			setBounds(maxBounds);
		else
		if (w <= minBounds.width || h <= minBounds.height )
			setBounds(minBounds);
		else
			setBounds(new Rectangle(w, h));
	}
	
	//Проверка того что элемент меремещаем
	public boolean isMovable()
	{
		if ( getLogicalNetLayer().mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMoveNode"))
			return true;
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
		out.writeObject(getImageID());
		out.writeObject(physicalLinkID);
		out.writeBoolean(active);
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
		physicalLinkID = (String )in.readObject();
		active = in.readBoolean();
		ism_map_id = (String )in.readObject();

		attributes = (Hashtable )in.readObject();

		transferable = new MapPhysicalNodeElement_Transferable();
//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}
}
