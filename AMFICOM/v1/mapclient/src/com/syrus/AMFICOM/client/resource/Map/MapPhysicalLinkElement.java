package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.CORBA.Map.MapPhysicalLinkElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Popup.PhysicalLinkPopupMenu;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapPhysicalLinkElementDisplayModel;
import com.syrus.AMFICOM.Client.Map.UI.MapLinkPane;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.DEF;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

//A0A
public class MapPhysicalLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "maplinkelement";
	public Color backgroundNodeLinkSizeTable = Color.white;

	//Вектор NodeLink из которых состоит path
	public Vector nodeLink_ids = new Vector();
	public MapPhysicalLinkElement_Transferable transferable;

	public MapNodeElement startNode;
	public MapNodeElement endNode;
	protected boolean selected = false;
	public String LINK_ID = "";
	public String link_type_id = "";
	boolean is_proto = false;
	public String ism_map_id = "";

	private Vector sortedNodeLinks = new Vector();
	private Vector sortedNodes = new Vector();
	private boolean nodeLinksSorted = false;

	public MapPhysicalLinkElement()
	{
	}
	
	public MapPhysicalLinkElement( MapPhysicalLinkElement_Transferable Tmyransferable)
	{
		this.transferable = Tmyransferable;
		setLocalFromTransferable();
	}

	public MapPhysicalLinkElement (
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
		if(mapContext != null)
			if(mapContext instanceof ISMMapContext)
			  ism_map_id = ((ISMMapContext)mapContext).ISM_id;
		selected = false;

		transferable = new MapPhysicalLinkElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapPhysicalLinkElement.typ, cloned_id);

//		MapContext mc = (MapContext )Pool.get(MapContext.typ, mapContextID);
		MapPhysicalLinkElement mple = new MapPhysicalLinkElement(
				dataSource.GetUId(MapPhysicalLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(MapContext )mapContext.clone(dataSource) );
				
		mple.backgroundNodeLinkSizeTable = new Color(backgroundNodeLinkSizeTable.getRGB());
		mple.changed = changed;
		mple.description = description;
//		mple.endNode = (MapNodeElement )endNode.clone(dataSource);
		mple.endNode_id = endNode_id;
		mple.is_proto = is_proto;
		mple.ism_map_id = ism_map_id;
		mple.LINK_ID = (String )Pool.get("schemeclonedids", LINK_ID);
		mple.name = name;
		mple.owner_id = dataSource.getSession().getUserId();
		mple.PhysicalLinkID = PhysicalLinkID;
		mple.selected = selected;
		mple.show_alarmed = show_alarmed;
//		mple.startNode = (MapNodeElement )startNode.clone(dataSource);
		mple.startNode_id = startNode_id;
		mple.type_id = type_id;

		Pool.put(MapPhysicalLinkElement.typ, mple.getId(), mple);
		Pool.put("mapclonedids", id, mple.getId());

		mple.nodeLink_ids = new Vector(nodeLink_ids.size());
		for (int i = 0; i < nodeLink_ids.size(); i++)
			mple.nodeLink_ids.add(Pool.get("mapclonedids", (String )nodeLink_ids.get(i)));

		mple.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mple.attributes.put(ea2.type_id, ea2);
		}

		return mple;
	}

	public void updateAttributes()
	{
		attributes.clear();
	    for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, Pool.get("attribute", transferable.attributes[i].id));
	}

/////////////////////////////////////// AAAA
	public void setLocalFromTransferable()
	{
		int i;

		this.id = transferable.id;
		this.name = transferable.name;
		this.type_id = transferable.type_id;
		this.description = transferable.description;
		this.owner_id = transferable.owner_id;
		this.mapContextID = transferable.map_id;
		this.startNode_id = transferable.startNode_id;
		this.endNode_id = transferable.endNode_id;
		this.ism_map_id = transferable.ism_map_id;
		for(i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		this.LINK_ID = transferable.link_id;
		this.link_type_id = transferable.link_type_id;

		this.nodeLink_ids = new Vector();
		for (i = 0; i < transferable.nodeLink_ids.length; i++)
			this.nodeLink_ids.add( transferable.nodeLink_ids[i]);
	}

	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.type_id = this.type_id;
		transferable.description = this.description;
		transferable.owner_id = mapContext.user_id ;
		transferable.map_id = mapContext.id;
		transferable.startNode_id = this.startNode.getId();
		transferable.endNode_id = this.endNode.getId();
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
		transferable.link_id = this.LINK_ID;
		transferable.link_type_id = link_type_id;

/*
 count = nodeLinks.size();
  nodeLink_ids = new Vector();
  for(i = 0; i < count; i++)
  {
   os = (ObjectResource )nodeLinks.get(i);
   nodeLink_ids.add(os.getId());
  }
*/
		transferable.nodeLink_ids = new String[ this.nodeLink_ids.size()];
		nodeLink_ids.copyInto(transferable.nodeLink_ids);
	}

	public ObjectResourceModel getModel()
	{
		return new MapPhysicalLinkElementModel(this);
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
			this.startNode = (MapNodeElement)Pool.get("mapkiselement", startNode_id);
		if(this.startNode == null)
			this.startNode = (MapNodeElement)Pool.get("mapnodeelement", startNode_id);

		this.endNode = (MapNodeElement)Pool.get("mapequipmentelement", endNode_id);
		if(this.endNode == null)
			this.endNode = (MapNodeElement)Pool.get("mapkiselement", endNode_id);
		if(this.endNode == null)
			this.endNode = (MapNodeElement)Pool.get("mapnodeelement", endNode_id);
		this.mapContext = (MapContext)Pool.get("mapcontext", this.mapContextID);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapPhysicalLinkElementDisplayModel();
	}
	
	public static PropertiesPanel getPropertyPane()
	{
		return new MapLinkPane();
	}

	public String getToolTipText()
	{
		String s1 = name;
		String s2 = "";
		String s3 = "";
		try
		{
//			MapNodeElement smne = (MapNodeElement )getMapContext().getNode(startNode_id);
			MapNodeLinkElement snle = (MapNodeLinkElement )getMapContext().getNodeLinksInPhysicalLink(getId()).get(0);
			MapNodeElement smne = snle.startNode;
			s2 =  ":\n" + "   от " + smne.getName() + " [" + LangModel.String("node" + smne.getTyp()) + "]";
//			MapNodeElement emne = (MapNodeElement )getMapContext().getNode(endNode_id);
			MapNodeLinkElement enle = (MapNodeLinkElement )getMapContext().getNodeLinksInPhysicalLink(getId()).get(
					getMapContext().getNodeLinksInPhysicalLink(getId()).size() - 1);
			MapNodeElement emne = enle.endNode;
			s3 = "\n" + "   до " + emne.getName() + " [" + LangModel.String("node" + emne.getTyp()) + "]";
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
		return s1 + s2 + s3;
	}

/////////////////////////////////////// AAAA

	public void paint (Graphics g, Color color)
	{
		//A0A
		Graphics2D p = (Graphics2D)g;
		Stroke rem_str = p.getStroke();

		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		p.setStroke( str);
		p.setColor( this.getColor());

		Enumeration e = mapContext.getNodeLinksInPhysicalLink( this.getId() ).elements();
		while (e.hasMoreElements())
		{
			MapNodeLinkElement myNodeLink = (MapNodeLinkElement)e.nextElement();

			Point from = getLogicalNetLayer().convertMapToScreen(myNodeLink.startNode.getAnchor());
			Point to = getLogicalNetLayer().convertMapToScreen(myNodeLink.endNode.getAnchor());

			if (this.getAlarmState())
			{
				if ( this.getShowAlarmed() )
					p.setColor(this.getAlarmedColor());
				else
					p.setColor(this.getColor());
			}
			else
				p.setColor( this.getColor());

//			p.setStroke(new BasicStroke(3) );

			p.drawLine( from.x, from.y, to.x, to.y);
		}

		p.setStroke(rem_str);
	}

	//Рисуем NodeLink взависимости от того выбрана она или нет,
	//  а так же если она выбрана выводим её рамер
	public void paint (Graphics g)
	{
		Graphics2D p = (Graphics2D)g;
		Stroke rem_str = p.getStroke();
		
		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
//		p.setStroke( str);
//		p.setColor( color);

		int a;
		if(!selected)
			a = 0;
		//A0A
		Vector nodeLinksVector = getMapContext().getNodeLinksInPhysicalLink( this.getId());
		Enumeration e = nodeLinksVector.elements();
		while ( e.hasMoreElements())
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement)e.nextElement();

			Point from = getLogicalNetLayer().convertMapToScreen( nodelink.startNode.getAnchor());
			Point to = getLogicalNetLayer().convertMapToScreen( nodelink.endNode.getAnchor());

			p.setStroke(str );
			p.setColor( this.getColor());

			if (this.getAlarmState())
			{
				if ( this.getShowAlarmed() )
					p.setColor(this.getAlarmedColor());
				else
					p.setColor(this.getColor());
			}
			else
				p.setColor(this.getColor());

			p.drawLine( from.x, from.y, to.x, to.y);
			if ( isSelected())
			{
				p.setStroke(DEF.selected_stroke);
/*
				new BasicStroke( 
						1,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_BEVEL,
                        (float)0.0,
                        new float[] {5, 5},
                        (float)0.0));
*/
				double l = 4;
				double l1 = 6;
				double cos_a = (from.y - to.y)/
					Math.sqrt( (from.x - to.x)*(from.x - to.x) + (from.y - to.y)*(from.y - to.y) );

				double sin_a = (from.x - to.x)/
					Math.sqrt( (from.x - to.x)*(from.x - to.x) + (from.y - to.y)*(from.y - to.y) );

				p.setColor(Color.black);
				p.drawLine(from.x + (int)(l * cos_a), from.y  - (int)(l * sin_a), to.x + (int)(l * cos_a), to.y - (int)(l * sin_a));
				p.drawLine(from.x - (int)(l * cos_a), from.y  + (int)(l * sin_a), to.x - (int)(l * cos_a), to.y + (int)(l * sin_a));

				p.setColor(Color.red);
				p.drawLine(from.x + (int)(l1 * cos_a), from.y  - (int)(l1 * sin_a), to.x + (int)(l1 * cos_a), to.y - (int)(l1 * sin_a));
				p.drawLine(from.x - (int)(l1 * cos_a), from.y  + (int)(l1 * sin_a), to.x - (int)(l1 * cos_a), to.y + (int)(l1 * sin_a));
			}
		}
		p.setStroke(rem_str);
	}

	public void paint (Graphics g, Point myPoint)
	{
	}

	//Возвращяет длинну линии
	public double getSizeInDoubleLt()
	{
		double returnValue = 0;
		Vector vec = getMapContext().getNodeLinksInPhysicalLink( getId());
		Enumeration e = vec.elements();
		while ( e.hasMoreElements())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement)e.nextElement();
			returnValue += nodeLink.getSizeInDoubleLt();
		}
		return returnValue;
	}

//Возвращяет длинну линии пересчитанную на коэффициент топологической привязки
	public double getSizeInDoubleLf()
	{
		double Kd = getKd();
		return getSizeInDoubleLt() * Kd;
	}

	public double getPhysicalLength()
	{
		if(LINK_ID == null)
			return 0.0D;
		SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, LINK_ID);
		if(scl == null)
			return 0.0D;
		return scl.getPhysicalLength();
	}

	public double getKd()
	{
		double ph_len = getPhysicalLength();
		if(ph_len == 0.0D)
			return 1.0;
		double top_len = getSizeInDoubleLt();
		if(top_len == 0.0D)
			return 1.0;

		double Kd = ph_len / top_len;
		return Kd;
	}
		
	public boolean isClicked(Point currentMousePoint)
	{
		return false;
	}

	public void move (double deltaX, double deltaY)
	{
	}

	//A0A
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
	//A0A
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

		if(SwingUtilities.isLeftMouseButton(me))
		{
			//A0A
			if ((actionMode != LogicalNetLayer.SELECT_ACTION_MODE) &&
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE))
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();
		}

		MapStrategy strategy = new VoidStrategy();
		return strategy;
	}

	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		return new PhysicalLinkPopupMenu( myFrame, this, getLogicalNetLayer());
	}

	//Добавляет NodeLink к данному path
	public void addMapNodeLink(MapNodeLinkElement addNodeLink)
	{
		nodeLink_ids.add( addNodeLink.getId() );

		//Проверяем концевые точки и дабавляем NodeLink
		if ( addNodeLink.startNode == this.startNode )
		{
			if ( addNodeLink.endNode == this.endNode )
			{
				//A0A
				if ( !(this.startNode instanceof MapPhysicalNodeElement) )
					this.endNode = this.startNode;
				//A0A
				else
					this.startNode = this.endNode;
				return;
			}
			return;
		}

		if ( addNodeLink.endNode == this.startNode )
		{
			if ( addNodeLink.startNode == this.endNode )
			{
				//A0A
				if ( !(this.startNode instanceof MapPhysicalNodeElement) )
					this.endNode = this.startNode;
				//A0A
				else
					this.startNode = this.endNode;
				return;
			}
			this.startNode = addNodeLink.startNode;
			return;
		}

		//A0A
		if ( addNodeLink.startNode == this.endNode )
		{
			this.endNode = addNodeLink.endNode;
			return;
		}

		//A0A
		if ( addNodeLink.endNode == this.endNode )
		{
			if ( mapContext.getNodeLinksContainingNode( addNodeLink.startNode ).size() == 1 )
			{
				this.endNode = addNodeLink.startNode;
			}
			return;
		}

//		JOptionPane.showMessageDialog( getLogicalNetLayer().mainFrame, "Cannot add MapNodeLinkElement to MapPhysicalLinkElement");
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void finalUpdate()
	{
	}

	public SxDoublePoint getAnchor()
	{
		Vector vec = new Vector();
		SxDoublePoint pts[];

		for(Enumeration enum = getMapContext().getNodeLinksInPhysicalLink(getId()).elements();
			enum.hasMoreElements();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )enum.nextElement();
			vec.add(mnle.getAnchor());
		}

		pts = new SxDoublePoint[vec.size()];
		vec.copyInto(pts);
		SxDoublePoint point = new SxDoublePoint(0.0, 0.0);
		for(int i = 0; i < pts.length; i++)
		{
			point.x += pts[i].x;
			point.y += pts[i].y;
		}
		point.x /= pts.length;
		point.y /= pts.length;
		
		return point;
	}
	
	public Vector sortNodes()
	{
		sortNodeLinks();
		return sortedNodes;
	}
	
	public Vector sortNodeLinks()
	{
		if(!nodeLinksSorted)
		{
			MapNodeElement smne = this.startNode;
			Vector vec = new Vector();
			Vector nodevec = new Vector();
			Vector nodelinks = getMapContext().getNodeLinksInPhysicalLink(getId());
			while(!smne.equals(this.endNode))
			{
				nodevec.add(smne);

				Enumeration e = nodelinks.elements();
				while( e.hasMoreElements())
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement) e.nextElement();
					if(nodeLink.startNode.equals(smne))
					{
						vec.add(nodeLink);
						nodelinks.remove(nodeLink);
						smne = nodeLink.endNode;
						break;
					}
					else
					if(nodeLink.endNode.equals(smne))
					{
						vec.add(nodeLink);
						nodelinks.remove(nodeLink);
						smne = nodeLink.startNode;
						break;
					}
				}
			}
			nodevec.add(this.endNode);
			this.sortedNodeLinks = vec;
			nodeLinksSorted = true;
			this.sortedNodes = nodevec;
		}
		return sortedNodeLinks;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(type_id);
		out.writeObject(description);
		out.writeObject(owner_id);
		out.writeObject(mapContextID);
		out.writeObject(PhysicalLinkID);
		out.writeObject(ism_map_id);
		out.writeObject(startNode_id);
		out.writeObject(endNode_id);

		out.writeObject(attributes);

		out.writeObject(LINK_ID);
		out.writeObject(link_type_id);

		out.writeObject(nodeLink_ids);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		type_id = (String )in.readObject();
		description = (String )in.readObject();
		owner_id = (String )in.readObject();
		mapContextID = (String )in.readObject();
		PhysicalLinkID = (String )in.readObject();
		ism_map_id = (String )in.readObject();
		startNode_id = (String )in.readObject();
		endNode_id = (String )in.readObject();
		attributes = (Hashtable )in.readObject();

		LINK_ID = (String )in.readObject();
		link_type_id = (String )in.readObject();
		nodeLink_ids = (Vector )in.readObject();

		transferable = new MapPhysicalLinkElement_Transferable();

		backgroundNodeLinkSizeTable = Color.white;

//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}

}