package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.CORBA.Map.MapPathElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Popup.TransmissionPathPopupMenu;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapTransmissionPathElementDisplayModel;
import com.syrus.AMFICOM.Client.Map.UI.MapPathGeneralPanel;
import com.syrus.AMFICOM.Client.Map.UI.MapPathPane;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.DEF;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;

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
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

//A0A
public class MapTransmissionPathElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "mappathelement";
	public MapPathElement_Transferable transferable;

	public Color backgroundNodeLinkSizeTable = Color.white;

	public String PATH_ID = "";

	public Vector physicalLink_ids = new Vector();

	boolean is_proto = false;
	public String ism_map_id = "";

	private Vector sortedNodes = new Vector();
	private Vector sortedNodeLinks = new Vector();
	private boolean nodeLinksSorted = false;

	private Vector sortedLinks = new Vector();
	private boolean linksSorted = false;

	public MapTransmissionPathElement()
	{
	}

	public MapTransmissionPathElement( MapPathElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapTransmissionPathElement (
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
		if(mapContext instanceof ISMMapContext)
			ism_map_id = ((ISMMapContext)mapContext).ISM_id;
		startNode = stNode;
		endNode = eNode;
		attributes = new Hashtable();

		transferable = new MapPathElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapTransmissionPathElement.typ, cloned_id);

//		MapContext mc = (MapContext )Pool.get(MapContext.typ, mapContextID);
		MapTransmissionPathElement mtpe = new MapTransmissionPathElement(
				dataSource.GetUId(MapTransmissionPathElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(MapContext )mapContext.clone(dataSource) );
				
		mtpe.backgroundNodeLinkSizeTable = new Color(backgroundNodeLinkSizeTable.getRGB());
		mtpe.changed = changed;
		mtpe.description = description;
//		mtpe.endNode = (MapNodeElement )endNode.clone(dataSource);
		mtpe.endNode_id = endNode_id;
		mtpe.is_proto = is_proto;
		mtpe.ism_map_id = ism_map_id;
		mtpe.name = name;
		mtpe.owner_id = dataSource.getSession().getUserId();
		mtpe.PATH_ID = (String )Pool.get("schemeclonedids", PATH_ID);
		mtpe.PhysicalLinkID = PhysicalLinkID;
		mtpe.selected = selected;
		mtpe.show_alarmed = show_alarmed;
//		mtpe.startNode = (MapNodeElement )startNode.clone(dataSource);
		mtpe.startNode_id = startNode_id;
		mtpe.type_id = type_id;

		Pool.put(MapTransmissionPathElement.typ, mtpe.getId(), mtpe);
		Pool.put("mapclonedids", id, mtpe.getId());

		mtpe.physicalLink_ids = new Vector(physicalLink_ids.size());
		for (int i = 0; i < physicalLink_ids.size(); i++)
			mtpe.physicalLink_ids.add(Pool.get("mapclonedids", (String )physicalLink_ids.get(i)));

		mtpe.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mtpe.attributes.put(ea2.type_id, ea2);
		}

		return mtpe;
	}

	public void updateAttributes()
	{
		attributes.clear();
	    for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, Pool.get("attribute", transferable.attributes[i].id));
	}

	//этот класс используетс€ дл€ востановлени€ данных из базы
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.type_id = transferable.type_id;
		this.description = transferable.description;
		this.owner_id = transferable.owner_id;
		this.mapContextID = transferable.map_id;
		this.startNode_id = transferable.startNode_id;
		this.endNode_id = transferable.endNode_id;
		this.ism_map_id = transferable.ism_map_id;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		this.PATH_ID = transferable.path_id;

		this.physicalLink_ids = new Vector();
		for (int i = 0; i < transferable.physicalLink_ids.length; i++)
			this.physicalLink_ids.add( transferable.physicalLink_ids[i]);
	}

	//этот класс используетс€ дл€ отпрвки данных в базу
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.type_id = this.type_id;
		transferable.description = this.description;
		transferable.owner_id = mapContext.user_id ;
		transferable.map_id = mapContext.id;
		transferable.ism_map_id = ism_map_id;
		transferable.startNode_id = this.startNode.getId();
		transferable.endNode_id = this.endNode.getId();

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Enumeration e = attributes.elements(); e.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )e.nextElement();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
		transferable.path_id = this.PATH_ID;

		transferable.physicalLink_ids = new String[ this.physicalLink_ids.size()];

		for (i = 0; i < transferable.physicalLink_ids.length; i++)
			transferable.physicalLink_ids[i] = (String) this.physicalLink_ids.get(i);
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

	//»спользуетс€ дл€ загрузкт данных из базы
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

	public ObjectResourceModel getModel()
	{
		return new MapTransmissionPathElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapTransmissionPathElementDisplayModel();
	}
	
	public static PropertiesPanel getPropertyPane()
	{
//		return new MapPathGeneralPanel();
		return new MapPathPane();
	}
	
/////////////////////////////////////// AAAA

	public void paint(Graphics g)
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

		Enumeration e = getMapContext().getNodeLinksInTransmissionPath( getId()).elements();

		while ( e.hasMoreElements())
		{
			MapNodeLinkElement nodeLinlElement = (MapNodeLinkElement) e.nextElement();
      //  nodeLinlElement.paint( g, getTransmissionPathStroke().basicStroke, getTransmissionPathColor().color);

			Point from = getLogicalNetLayer().convertMapToScreen(nodeLinlElement.startNode.getAnchor());
			Point to = getLogicalNetLayer().convertMapToScreen(nodeLinlElement.endNode.getAnchor());

//			Graphics2D p = (Graphics2D)g;
			p.setStroke( str);

			p.setColor( this.getColor());

			if (this.getAlarmState())
			{
				if ( this.getShowAlarmed() )
					p.setColor(this.getAlarmedColor());
				else
					p.setColor(this.getColor());
			}
			else
				p.setColor( this.getColor());

			p.drawLine(from.x,from.y,to.x,to.y);

			if (isSelected())
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

	  //JOptionPane.showMessageDialog(this.getLogicalNetLayer().mapMainFrame, "!!!!!!!!!!!!!!!!!!!!!!! ");
		}
		p.setStroke(rem_str);
	}

	public void paint (Graphics g, Point myPoint)
	{
	}

	public void move (double deltaX, double deltaY)
	{
	}

	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		return false;
	}

	//«десь обрабатываютс€ событи€ над объектом
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

	// онтекстное меню
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		return new TransmissionPathPopupMenu(myFrame, this);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void finalUpdate()
	{
	}

	//¬озвращает топологическую длинну в метрах
	public double getSizeInDoubleLt()
	{
		double length = 0;
		Enumeration e = getMapContext().getNodeLinksInTransmissionPath(getId()).elements();
		while( e.hasMoreElements())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement) e.nextElement();
			length = length + nodeLink.getSizeInDoubleLt();
		}
		return length;
	}

	//¬озвращает строительную длинну в метрах
	public double getSizeInDoubleLf()
	{
		SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, this.PATH_ID);
		if(sp == null)
			return 0.0;
		return sp.getPhysicalLength();
	}

	public double getSizeInDoubleLf1()
	{
		double length = 0;
		Enumeration e = getMapContext().getNodeLinksInTransmissionPath(getId()).elements();
		while( e.hasMoreElements())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement) e.nextElement();
			length = length + nodeLink.getSizeInDoubleLf();
		}
		return length;
	}
/*
	public double getDistance(Point pt)
	{
		double length = 0;
		Enumeration e = sortNodeLinks().elements();
		while( e.hasMoreElements())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement) e.nextElement();
//			if(nodeLink.isMouseOnThisObject(pt));
			length = length + nodeLink.getSizeInDoubleLt();
		}
		return length;
	}
*/
	public double getDistanceFromStartLf(Point pt)
	{
		double distance = 0.0;
		MapNodeElement node = startNode;
		for(Enumeration enum = sortNodeLinks().elements();
				enum.hasMoreElements();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )enum.nextElement();
			if(mnle.isMouseOnThisObject(pt))
			{
				distance += mnle.getLengthLf(node, pt);
				break;
			}
			else
				distance += mnle.getLengthLf();

			if(mnle.startNode.equals(node))
				node = mnle.endNode;
			else
				node = mnle.startNode;
		}
		return distance;
	}
/*
	public double getDistanceFromStart(Point pt)
	{
		double distance = 0.0;
		MapNodeElement node = transmissionPath.startNode;
		for(Enumeration enum = transmissionPath.sortNodeLinks().elements();
				enum.hasMoreElements();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )enum.nextElement();
			if(mnle.isMouseOnThisObject(point))
			{
				distance += mnle.getSizeInDouble(node, point);
				break;
			}
			else
				distance += mnle.getSizeInDouble();

			if(mnle.startNode.equals(node))
				node = mnle.endNode;
			else
				node = mnle.startNode;
		}
		return distance;
	}
*/
	public Vector sortNodes()
	{
		sortNodeLinks();
		return sortedNodes;
	}
	
	public Vector sortNodeLinks()
	{
		if(!nodeLinksSorted)
		{
			Vector pl = sortPhysicalLinks();

			MapNodeElement smne = this.startNode;
			MapNodeLinkElement mnle;
			Vector vec = new Vector();
			Vector nodevec = new Vector();

			for(int i = 0; i < pl.size(); i++)
			{
				if(i == 31)
					i = 31;
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )pl.get(i);
				Vector nl = link.sortNodeLinks();
				Vector n = link.sortNodes();
				
				boolean direct_order = (n.indexOf(smne) == 0);

				int size = nl.size();
				for(int j = 0; j < nl.size(); j++)
				{
					nodevec.add(smne);

					if(direct_order)
						mnle = (MapNodeLinkElement )nl.get(j);
					else
						mnle = (MapNodeLinkElement )nl.get(size - j - 1);
					if(mnle.startNode.equals(smne))
					{
						vec.add(mnle);
						smne = mnle.endNode;
					}
					else
					if(mnle.endNode.equals(smne))
					{
						vec.add(mnle);
						smne = mnle.startNode;
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

	// assume plink_ids are sorted!
	public Vector sortPhysicalLinks()
	{
		if(!linksSorted)
		{
			SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, this.PATH_ID);
			if(sp != null)
			{
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
						MapPhysicalLinkElement mple = getLogicalNetLayer().findPhysicalLink(pe.link_id);
						if(mple != null)
							plink_ids.add(mple.getId());
					}
				}

				physicalLink_ids = plink_ids;
			}

			Vector vec = new Vector();
			for(int i = 0; i < physicalLink_ids.size(); i++)
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )
						getMapContext().getPhysicalLink(
							(String )physicalLink_ids.get(i));
				vec.add(link);
				
			}
			this.sortedLinks = vec;
			linksSorted = true;
		}
		return sortedLinks;
	}

	public Vector sortNodeLinks1()
	{
		if(!nodeLinksSorted)
		{
			MapNodeElement smne = this.startNode;
			Vector vec = new Vector();
			Vector nodevec = new Vector();
			Vector nodelinks = getMapContext().getNodeLinksInTransmissionPath(getId());

			System.out.print("Sorting path node links (total " + nodelinks.size() + ")");
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
				System.out.print(".");
			}
			System.out.println("ready!");
			nodevec.add(this.endNode);
			this.sortedNodeLinks = vec;
			nodeLinksSorted = true;
			this.sortedNodes = nodevec;
		}
		return sortedNodeLinks;
	}

	public Vector sortPhysicalLinks1()
	{
		if(!linksSorted)
		{
			MapNodeElement smne = this.startNode;
			Vector vec = new Vector();
			Vector links = getMapContext().getPhysicalLinksInTransmissiionPath(getId());
			System.out.print("Sorting path links (total " + links.size() + ")");
			while(!smne.equals(this.endNode))
			{
				Enumeration e = links.elements();
				while( e.hasMoreElements())
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.nextElement();
					if(link.startNode.equals(smne))
					{
						vec.add(link);
						links.remove(link);
						smne = link.endNode;
						break;
					}
					else
					if(link.endNode.equals(smne))
					{
						vec.add(link);
						links.remove(link);
						smne = link.startNode;
						break;
					}
				}
				System.out.print(".");
			}
			System.out.println("ready!");
			this.sortedLinks = vec;
			linksSorted = true;
		}
		return sortedLinks;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(type_id);
		out.writeObject(description);
		out.writeObject(owner_id);
		out.writeObject(mapContextID);
		out.writeObject(ism_map_id);
		out.writeObject(startNode_id);
		out.writeObject(endNode_id);

		out.writeObject(attributes);

		out.writeObject(PATH_ID);

		out.writeObject(physicalLink_ids);
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
		ism_map_id = (String )in.readObject();
		startNode_id = (String )in.readObject();
		endNode_id = (String )in.readObject();
		attributes = (Hashtable )in.readObject();

		PATH_ID = (String )in.readObject();
		physicalLink_ids = (Vector )in.readObject();

		transferable = new MapPathElement_Transferable();

    backgroundNodeLinkSizeTable = Color.white;

//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}

//---------------------------------------------------------------------------------------------------------------
  // возвращает строку, описывающую данный путь
	public  String getTransmissionPathInString()
	{ 
		String s = getName() + " : ";
        Vector v = physicalLink_ids;

        for(int i = 0; i < v.size(); i++)// по всему массиву линков
        {  
			String link_id = (String )v.elementAt(i);
			s += Pool.getName(MapPhysicalLinkElement.typ, link_id);
			if(i != v.size() - 1 ) 
				s += " -> ";      
		}
		return s;
	}
//---------------------------------------------------------------------------------------------------------------
}