package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.CORBA.Map.MapNodeLinkElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Strategy.AddPhysicalNodeElementStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapNodeLinkElementDisplayModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.DEF;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

//A0A
public class MapNodeLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "mapnodelinkelement";
	public MapNodeLinkElement_Transferable transferable;

	public String ism_map_id = "";

	protected final int mouseTolerancy = 3;

	Color backgroundNodeLinkSizeTable = Color.white;

	Rectangle labelBox = new Rectangle();

	public MapNodeLinkElement()
	{
	}
	
	public MapNodeLinkElement(MapNodeLinkElement_Transferable Tmyransferable)
	{
		this.transferable = Tmyransferable;
		setLocalFromTransferable();
	}

	public MapNodeLinkElement (
			String myID, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			MapContext myMapContext)
	{
		mapContext = myMapContext;

		id = myID;
		name = myID;
		if(mapContext != null)
			mapContextID = mapContext.id;
		startNode = stNode;
		endNode = eNode;
		attributes = new Hashtable();

		selected = false;

		transferable = new MapNodeLinkElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapNodeLinkElement.typ, cloned_id);

//		MapContext mc = (MapContext )Pool.get(MapContext.typ, mapContextID);
		MapNodeLinkElement mnle = new MapNodeLinkElement(
				dataSource.GetUId(MapNodeLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(MapContext )mapContext.clone(dataSource) );
				
		mnle.backgroundNodeLinkSizeTable = new Color(backgroundNodeLinkSizeTable.getRGB());
		mnle.changed = changed;
		mnle.description = description;
//		mnle.endNode = (MapNodeElement )endNode.clone(dataSource);
		mnle.endNode_id = endNode_id;
		mnle.ism_map_id = ism_map_id;
		mnle.name = name;
		mnle.owner_id = dataSource.getSession().getUserId();
		mnle.PhysicalLinkID = PhysicalLinkID;
		mnle.selected = selected;
		mnle.show_alarmed = show_alarmed;
//		mnle.startNode = (MapNodeElement )startNode.clone(dataSource);
		mnle.startNode_id = startNode_id;
		mnle.type_id = type_id;

		Pool.put(MapNodeLinkElement.typ, mnle.getId(), mnle);
		Pool.put("mapclonedids", id, mnle.getId());

		mnle.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mnle.attributes.put(ea2.type_id, ea2);
		}

		return mnle;
	}

/////////////////////////////////////// AAAA
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.owner_id = transferable.owner_id;
		this.mapContextID = transferable.map_id;
		this.PhysicalLinkID = transferable.physicalLinkID;
		this.ism_map_id = transferable.ism_map_id;
		this.startNode_id = transferable.startNode_id;
		this.endNode_id = transferable.endNode_id;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.owner_id = mapContext.user_id ;
		transferable.map_id = mapContext.id;
		transferable.startNode_id = this.startNode.getId();
		transferable.endNode_id = this.endNode.getId();
		transferable.physicalLinkID = mapContext.getPhysicalLinkbyNodeLink( this.getId()).getId();//this.PhysicalLinkID;
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
		return id;
	}

	//Используется для для загрузки класса из базы данных
	public void updateLocalFromTransferable()
	{
		this.startNode = (MapNodeElement)Pool.get("mapequipmentelement", startNode_id);
		if(this.startNode == null)
			this.startNode = (MapNodeElement)Pool.get("mapnodeelement", startNode_id);

		this.endNode = (MapNodeElement)Pool.get("mapequipmentelement", endNode_id);
		if(this.endNode == null)
			this.endNode = (MapNodeElement)Pool.get("mapnodeelement", endNode_id);

		this.mapContext = (MapContext)Pool.get("mapcontext", this.mapContextID);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new MapNodeLinkElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapNodeLinkElementDisplayModel();
	}
	
/////////////////////////////////////// AAAA

	//Кнтекстное меню
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		return null;//new NodeLinkPopupMenu(myFrame, this);
	}

	//Возвращяет длинну линии
	public String getSize()
	{
/*
		SxDoublePoint from = getLogicalNetLayer().convertMapToLatLong(
			new SxDoublePoint(startNode.getAnchor().x, startNode.getAnchor().y));
		SxDoublePoint to = getLogicalNetLayer().convertMapToLatLong(
			new SxDoublePoint(endNode.getAnchor().x, endNode.getAnchor().y));

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
			( Math.sin(a2) - Math.sin(b2)) *
			( Math.sin(a2) - Math.sin(b2)) );
*/
		double d = getLength();
//		java.text.DecimalFormat df2 = new java.text.DecimalFormat("#####0.0");

		double d100 = d * 10;
		int i = (int )d100;
		double retd = (double )i;

		return String.valueOf(retd / 10.0);

//		return df2.format(d);
	}

	//Возвращяет длинну линии
	public double getSizeInDoubleLt()
	{

		SxDoublePoint from = new SxDoublePoint(startNode.getAnchor().x, startNode.getAnchor().y);
		SxDoublePoint to = new SxDoublePoint(endNode.getAnchor().x, endNode.getAnchor().y);

		return LogicalNetLayer.distance(from, to);
	}

	//Возвращяет длинну линии пересчитанную на коэффициент топологической привязки
	public double getSizeInDoubleLf()
	{
		double Kd = getMapContext().getPhysicalLinkbyNodeLink(getId()).getKd();
		return getSizeInDoubleLt() * Kd;
	}

	public void paint (Graphics g, Point myPonit)
	{
	}

	public void paint (Graphics g)
	{
		paint(g, (BasicStroke )this.getStroke(), this.getColor());
	}

	//Рисуем NodeLink взависимости от того выбрана она или нет,
	//  а так же если она выбрана выводим её рамер
	public void paint (Graphics g, BasicStroke basistroke, Color color)
	{
		int lineSize = getMapContext().getPhysicalLinkbyNodeLink(getId()).getLineSize();
		basistroke = (BasicStroke )getMapContext().getPhysicalLinkbyNodeLink(getId()).getStroke();
		color = getMapContext().getPhysicalLinkbyNodeLink(getId()).getColor();
	
		Point from = getLogicalNetLayer().convertMapToScreen(
			new SxDoublePoint(startNode.getAnchor().x, startNode.getAnchor().y));
		Point to = getLogicalNetLayer().convertMapToScreen(
			new SxDoublePoint(endNode.getAnchor().x, endNode.getAnchor().y));

		Graphics2D p = (Graphics2D )g;
		Stroke rem_str = p.getStroke();

		Stroke str = new BasicStroke(
				lineSize, 
				basistroke.getEndCap(), 
				basistroke.getLineJoin(), 
				basistroke.getMiterLimit(), 
				basistroke.getDashArray(), 
				basistroke.getDashPhase());
		p.setStroke( str);
//		p.setStroke( basistroke);

		//Если alarm есть то специальный thread будет менять showAlarmState и NodeLink
		//будет мигать
		if ( (this.getAlarmState()) && this.getShowAlarmed() )
		{
			p.setColor(this.getAlarmedColor());
		}
		else
		{
			p.setColor(color);
		}
		p.drawLine(from.x, from.y, to.x, to.y);

		//A0A
		if (isSelected())
		{
			p.setStroke(DEF.selected_stroke);

			double length = Math.sqrt( (from.x - to.x)*(from.x - to.x) + (from.y - to.y)*(from.y - to.y) );

			double l = 4;
			double l1 = 6;
			double cos_a = (from.y - to.y) / length;

			double sin_a = (from.x - to.x) / length;

			p.setColor(Color.black);
			p.drawLine(from.x + (int)(l * cos_a), from.y  - (int)(l * sin_a), to.x + (int)(l * cos_a), to.y - (int)(l * sin_a));
			p.drawLine(from.x - (int)(l * cos_a), from.y  + (int)(l * sin_a), to.x - (int)(l * cos_a), to.y + (int)(l * sin_a));

			p.setColor(Color.red);
			p.drawLine(from.x + (int)(l1 * cos_a), from.y  - (int)(l1 * sin_a), to.x + (int)(l1 * cos_a), to.y - (int)(l1 * sin_a));
			p.drawLine(from.x - (int)(l1 * cos_a), from.y  + (int)(l1 * sin_a), to.x - (int)(l1 * cos_a), to.y + (int)(l1 * sin_a));

		    p.setStroke(new BasicStroke());
		}

		//Рисовать табличку с длинной NodeLink
		if ( this.getShowSizeContext() )
		{
			int font_height = g.getFontMetrics().getHeight();
			String text = getSize() + " " + getMetric();
			int text_width = g.getFontMetrics().stringWidth(text);
			int center_x = (from.x + to.x) / 2;
			int center_y = (from.y + to.y) / 2;

			g.setColor(Color.black);
			g.setFont(this.getFont());

			labelBox = new Rectangle(
					center_x  , 
					center_y - font_height + 2,
					text_width , 
					font_height );

			g.drawRect(
					center_x  , 
					center_y - font_height + 2,
					text_width , 
					font_height );

			g.setColor(getBackgroundNodeLinkSizeTable());
			g.fillRect(
					center_x , 
					center_y - font_height + 2,
					text_width , 
					font_height );

			g.setColor(Color.black);
			g.drawString(
					text, 
					center_x , 
					center_y );
		}
		p.setStroke(rem_str);
	}

	//A0A
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		int[] xx = new int[6];
		int[] yy = new int[6];

		Point from = getLogicalNetLayer().convertMapToScreen(
			new SxDoublePoint(startNode.getAnchor().x, startNode.getAnchor().y));
		Point to = getLogicalNetLayer().convertMapToScreen(
			new SxDoublePoint(endNode.getAnchor().x, endNode.getAnchor().y));

		int minX = (int )from.getX();
		int maxX = (int )to.getX();

		int minY = (int )from.getY();
		int maxY = (int )to.getY();

		//A0A
		if (Math.abs(maxX - minX) < Math.abs(maxY - minY))
		{
			xx[0] = minX - mouseTolerancy; yy[0] = minY;
			xx[1] = maxX - mouseTolerancy; yy[1] = maxY;
			xx[2] = maxX; yy[2] = maxY + mouseTolerancy;
			xx[3] = maxX + mouseTolerancy; yy[3] = maxY;
			xx[4] = minX + mouseTolerancy; yy[4] = minY;
			xx[5] = minX; yy[5] = minY - mouseTolerancy;
		}
		else
		{
			xx[0] = minX; yy[0] = minY + mouseTolerancy;
			xx[1] = maxX; yy[1] = maxY + mouseTolerancy;
			xx[2] = maxX + mouseTolerancy; yy[2] = maxY;
			xx[3] = maxX; yy[3] = maxY - mouseTolerancy;
			xx[4] = minX; yy[4] = minY - mouseTolerancy;
			xx[5] = minX - mouseTolerancy; yy[5] = minY;
		}

		Polygon curPoly = new Polygon(xx, yy, 6);
		if(curPoly.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	//A0A
	public boolean isMouseOnThisObjectsLabel(Point currentMousePoint)
	{
		if ( getMapContext().linkState == MapContext.SHOW_NODE_LINK && this.getShowSizeContext() )
			if(labelBox.contains(currentMousePoint))
			{
				return true;
			}
		return false;
	}

	public Rectangle getLabelBox()
	{
		return labelBox;
	}

	public void move (double deltaX, double deltaY)
	{
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
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();

			//A0A
			if (actionMode == LogicalNetLayer.ALT_LINK_ACTION_MODE &&
				mode == LogicalNetLayer.MOUSE_PRESSED)
			{
				return new AddPhysicalNodeElementStrategy(aContext, logicalNetLayer, this, sourcePoint);
			}
		}
		return strategy;
	}

	//Получить длинну NodeLink
	public double getLength()
	{
		return getSizeInDoubleLt();
/*
		return Math.sqrt(
			(endNode.getAnchor().getX() - startNode.getAnchor().getX())*
			(endNode.getAnchor().getX() - startNode.getAnchor().getX()) +
			(endNode.getAnchor().getY() - startNode.getAnchor().getY())*
			(endNode.getAnchor().getY() - startNode.getAnchor().getY()) );
*/
	}

	public double getLengthLf()
	{
		double Kd = getMapContext().getPhysicalLinkbyNodeLink(getId()).getKd();
		return getLength() * Kd;
	}

	public double getLength(MapNodeElement mne, Point point)
	{
		SxDoublePoint from = getLogicalNetLayer().convertScreenToMap(point);
		SxDoublePoint to = new SxDoublePoint(mne.getAnchor().x, mne.getAnchor().y);

		return LogicalNetLayer.distance(from, to);
	}

	public double getLengthLf(MapNodeElement mne, Point point)
	{
		double Kd = getMapContext().getPhysicalLinkbyNodeLink(getId()).getKd();
		return getLength(mne, point) * Kd;
	}

	public void setSizeFrom(MapNodeElement node, double dist)
	{
		MapNodeElement stNode = (startNode.equals(node)) ? endNode : startNode;
		MapNodeElement enNode = (startNode.equals(node)) ? startNode : endNode;

		double prev_dist = Double.parseDouble(getSize());

		double absc = (dist / prev_dist) * (enNode.getAnchor().x - stNode.getAnchor().x) + stNode.getAnchor().x;
		double ordi = (dist / prev_dist) * (enNode.getAnchor().y - stNode.getAnchor().y) + stNode.getAnchor().y;

		enNode.setAnchor(new SxDoublePoint(absc, ordi));
	}

	//Используется призагрузке данных из базы
	public void finalUpdate()
	{
	}

	public void setBackgroundNodeLinkSizeTable (Color myColor)
	{
		backgroundNodeLinkSizeTable = myColor;
	}

	public Color getBackgroundNodeLinkSizeTable ()
	{
		return backgroundNodeLinkSizeTable ;
	}

	public SxDoublePoint getAnchor()
	{
		return new SxDoublePoint(
				(startNode.getAnchor().getX() + endNode.getAnchor().getX()) / 2,
				(startNode.getAnchor().getY() + endNode.getAnchor().getY()) / 2);
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(owner_id);
		out.writeObject(mapContextID);
		out.writeObject(PhysicalLinkID);
		out.writeObject(ism_map_id);

		out.writeObject(startNode_id);
		out.writeObject(endNode_id);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		owner_id = (String )in.readObject();
		mapContextID = (String )in.readObject();
		PhysicalLinkID = (String )in.readObject();
		ism_map_id = (String )in.readObject();
		startNode_id = (String )in.readObject();
		endNode_id = (String )in.readObject();
		attributes = (Hashtable )in.readObject();

		transferable = new MapNodeLinkElement_Transferable();

    backgroundNodeLinkSizeTable = Color.white;

//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}
}