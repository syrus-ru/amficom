/**
 * $Id: MapPhysicalLinkElement.java,v 1.27 2004/11/02 17:00:53 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapPhysicalLinkElement_Transferable;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.27 $, $Date: 2004/11/02 17:00:53 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "maplinkelement";

	protected MapPhysicalLinkElement_Transferable transferable;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PROTO_ID = "proto_id";	
	public static final String COLUMN_START_NODE_ID = "start_node_id";	
	public static final String COLUMN_END_NODE_ID = "end_node_id";	
	public static final String COLUMN_NODE_LINKS = "node_links";	
	public static final String COLUMN_CITY = "city";	
	public static final String COLUMN_STREET = "street";	
	public static final String COLUMN_BUILDING = "building";	

	protected List nodeLinkIds = new ArrayList();

	protected String mapProtoId = "";

	//������ NodeLink �� ������� ������� path
	protected List nodeLinks = new LinkedList();
	protected MapLinkProtoElement proto;

	protected String city = "";
	protected String street = "";
	protected String building = "";

	protected List sortedNodes = new LinkedList();
	protected boolean nodeLinksSorted = false;

	protected MapPhysicalLinkBinding binding;

	/**
	 * ������� ��������� ���� ������ ����
	 */	
	protected boolean topToBottom = true;

	/**
	 * ������� ��������� ����� �������
	 */	
	protected boolean leftToRight = true;

	public static String[][] exportColumns = null;

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapLinkPane";

	public MapPhysicalLinkElement()
	{
		selected = false;
		
		transferable = new MapPhysicalLinkElement_Transferable();
	}

	public MapPhysicalLinkElement( MapPhysicalLinkElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapPhysicalLinkElement (
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			Map map,
			MapLinkProtoElement proto)
	{
		this.map = map;

		this.id = id;
		this.name = id;
		if(map != null)
			mapId = map.getId();
		startNode = stNode;
		endNode = eNode;
		selected = false;
		mapProtoId = proto.getId();
		this.proto = proto;
		
		binding = new MapPhysicalLinkBinding(proto.getBindingDimension());

		transferable = new MapPhysicalLinkElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedId != null)
			return Pool.get(MapPhysicalLinkElement.typ, clonedId);

		MapPhysicalLinkElement mple = new MapPhysicalLinkElement(
				dataSource.GetUId(MapPhysicalLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map )map.clone(dataSource),
				getProto());
				
		mple.changed = changed;
		mple.description = description;
		mple.name = name;
		mple.selected = selected;
		mple.mapProtoId = mapProtoId;

		Pool.put(MapPhysicalLinkElement.typ, mple.getId(), mple);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mple.getId());

		mple.nodeLinkIds = new ArrayList(nodeLinks.size());
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			mple.nodeLinkIds.add(Pool.get(MapPropertiesManager.MAP_CLONED_IDS, mnle.getId()));
		}

		mple.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mple.attributes.put(ea2.type_id, ea2);
		}

		return mple;
	}

	public void updateAttributes()
	{
		attributes.clear();
	    for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, Pool.get(ElementAttribute.typ, transferable.attributes[i].id));
	}

	public void setLocalFromTransferable()
	{
		int i;

		this.id = transferable.id;
		this.name = transferable.name;
		this.mapProtoId = transferable.mapProtoId;
		this.description = transferable.description;
		this.mapId = transferable.mapId;
		this.startNodeId = transferable.startNodeId;
		this.endNodeId = transferable.endNodeId;
		for(i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		this.nodeLinkIds = new ArrayList();
		for (i = 0; i < transferable.nodeLinkIds.length; i++)
		{
			this.nodeLinkIds.add( transferable.nodeLinkIds[i]);
		}
		
		binding = new MapPhysicalLinkBinding(new Dimension(
				transferable.dimensionX,
				transferable.dimensionY));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.mapId = map.id;
		transferable.startNodeId = this.startNode.getId();
		transferable.endNodeId = this.endNode.getId();

		transferable.dimensionX = binding.getDimension().width;
		transferable.dimensionY = binding.getDimension().height;

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
		transferable.mapProtoId = mapProtoId;


		this.nodeLinkIds = new ArrayList();
		for (i = 0; i < nodeLinks.size(); i++)
		{
			this.nodeLinkIds.add( ((MapNodeLinkElement )nodeLinks.get(i)).getId());
		}
		transferable.nodeLinkIds = (String[] )nodeLinkIds.toArray(new String[nodeLinkIds.size()]);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getTyp()
	{
		return typ;
	}

	//������������ ��� ��� �������� ������ �� ���� ������
	public void updateLocalFromTransferable()
	{
		this.startNode = (MapNodeElement )Pool.get(MapSiteNodeElement.typ, startNodeId);
		if(this.startNode == null)
			this.startNode = (MapNodeElement )Pool.get(MapPhysicalNodeElement.typ, startNodeId);

		this.endNode = (MapNodeElement )Pool.get(MapSiteNodeElement.typ, endNodeId);
		if(this.endNode == null)
			this.endNode = (MapNodeElement )Pool.get(MapPhysicalNodeElement.typ, endNodeId);
		this.map = (Map )Pool.get(Map.typ, this.mapId);

		this.nodeLinks = new LinkedList();
		for (int i = 0; i < nodeLinkIds.size(); i++)
		{
			MapNodeLinkElement mnle = getMap().getNodeLink((String )nodeLinkIds.get(i));
			this.nodeLinks.add( mnle);
		}
		
		proto = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, mapProtoId);

		binding = new MapPhysicalLinkBinding(proto.getBindingDimension());
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public String getToolTipText()
	{
		String s1 = name;
		String s2 = "";
		String s3 = "";
		try
		{
			MapNodeElement smne = getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " ["
				+ LangModel.getString("node" + smne.getTyp()) 
				+ "]";
			MapNodeElement emne = getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ LangModel.getString("node" + emne.getTyp()) 
				+ "]";
		}
		catch(Exception e)
		{
			Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getToolTipText()", 
				e);
		}
		return s1 + s2 + s3;
	}

	public void setStartNode(MapNodeElement startNode)
	{
		super.setStartNode(startNode);
		nodeLinksSorted = false;
	}
	
	public void setEndNode(MapNodeElement endNode)
	{
		super.setEndNode(endNode);
		nodeLinksSorted = false;
	}

	protected boolean selectionVisible = false;

	public boolean isSelectionVisible()
	{
		return isSelected() || selectionVisible;
	}
	
	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		boolean vis = false;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();
			if(nodelink.isVisible(visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
	{
		if(!isVisible(visibleBounds))
			return;

		updateLengthLt();

		boolean showName = false;
		if(MapPropertiesManager.isShowLinkNames())
		{
			showName = true;
		}

		this.selectionVisible = selectionVisible;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();
			nodelink.paint(g, visibleBounds, stroke, color);
			
			if(showName)
			{
				MapCoordinatesConverter converter = getMap().getConverter();
				Point from = converter.convertMapToScreen(nodelink.getStartNode().getAnchor());
				Point to = converter.convertMapToScreen(nodelink.getEndNode().getAnchor());

				g.setColor(MapPropertiesManager.getBorderColor());
				g.setFont(MapPropertiesManager.getFont());
	
				int fontHeight = g.getFontMetrics().getHeight();
				String text = getName();
				int textWidth = g.getFontMetrics().stringWidth(text);
				int centerX = (from.x + to.x) / 2;
				int centerY = (from.y + to.y) / 2;

				g.drawRect(
						centerX,
						centerY - fontHeight + 2,
						textWidth,
						fontHeight);
	
				g.setColor(MapPropertiesManager.getTextBackground());
				g.fillRect(
						centerX,
						centerY - fontHeight + 2,
						textWidth,
						fontHeight);
	
				g.setColor(MapPropertiesManager.getTextColor());
				g.drawString(
						text,
						centerX,
						centerY);

				showName = false;
			}
		}
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(),
				stroke.getEndCap(),
				stroke.getLineJoin(),
				stroke.getMiterLimit(),
				stroke.getDashArray(),
				stroke.getDashPhase());
		Color color = getColor();

		paint(g, visibleBounds, str, color, false);
	}

	/**
	 * ���������� �������������� ������ �����
	 */
	public double getLengthLt()
	{
		double returnValue = 0;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			returnValue += nodeLink.getLengthLt();
		}
		return returnValue;
	}

	public void updateLengthLt()
	{
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			nodeLink.updateLengthLt();
		}
	}

	/**
	 * ���������� false, ��������� �������� ���������� ��� ������� �� ����
	 * ���������, � � ��������� �� ���������� �����, ��� ������ true
	 * ��� ���������. ������� � ������ ������ � ������� ������ ��������
	 * � �� ���� ��� ������� ������ �� ��� �����
	 */
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(me.isMouseOnThisObject(currentMousePoint))
				return true;
		}
		return false;
	}

	public List getNodeLinks()
	{	
		return nodeLinks;
	}

	public void clearNodeLinks()
	{	
		nodeLinks.clear();
		nodeLinksSorted = false;
	}

	public MapNodeLinkElement getStartNodeLink()
	{
		MapNodeLinkElement startNodeLink = null;

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			startNodeLink = (MapNodeLinkElement )it.next();
			if(startNodeLink.getStartNode() == getStartNode()
				|| startNodeLink.getEndNode() == getStartNode())
			{
				break;
			}
		}
		return startNodeLink;
	}

	/**
	 * ��������! �������� ����� ����� �� �����������
	 */
	public void removeNodeLink(MapNodeLinkElement nodeLink)
	{
		nodeLinks.remove(nodeLink);
		nodeLinksSorted = false;
	}

	/**
	 * ��������! �������� ����� ����� �� �����������
	 */
	public void addNodeLink(MapNodeLinkElement addNodeLink)
	{
		nodeLinks.add(addNodeLink);
		nodeLinksSorted = false;
	}

	/**
	 * �������� NodeLinks ���������� ������ node � ������ transmissionPath
	 */
	public java.util.List getNodeLinksAt(MapNodeElement node)
	{
		LinkedList returnNodeLink = new LinkedList();
		Iterator e = getNodeLinks().iterator();

		while (e.hasNext())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
			if ( (nodeLink.endNode == node) || (nodeLink.startNode == node))
				returnNodeLink.add(nodeLink);
		}
		return returnNodeLink;
	}

	/**
	 * �������� ����� (���) �����
	 */
	public Point2D.Double getAnchor()
	{
		int count = 0;
		Point2D.Double point = new Point2D.Double(0.0, 0.0);

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			Point2D.Double an = mnle.getAnchor();
			point.x += an.x;
			point.y += an.y;
			count ++;
		}
		point.x /= count;
		point.y /= count;
		
		return point;
	}
	
	public void sortNodes()
	{
		sortNodeLinks();
	}
	
	public java.util.List getSortedNodes()
	{
		if(!nodeLinksSorted)
			return null;
		return sortedNodes;
	}

	public void sortNodeLinks()
	{
		if(!nodeLinksSorted)
		{
			MapNodeElement smne = this.getStartNode();
			MapNodeLinkElement nl = null;
			LinkedList vec = new LinkedList();
			List nodevec = new LinkedList();
			int count = getNodeLinks().size();
			for (int i = 0; i < count; i++) 
			{
				nodevec.add(smne);

				for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();

					if(! nodeLink.equals(nl))
					{
						if(nodeLink.getStartNode().equals(smne))
						{
							vec.add(nodeLink);
							it.remove();
							smne = nodeLink.getEndNode();
							nl = nodeLink;
							break;
						}
						else
						if(nodeLink.getEndNode().equals(smne))
						{
							vec.add(nodeLink);
							it.remove();
							smne = nodeLink.getStartNode();
							nl = nodeLink;
							break;
						}
					}
				}
			}
			nodevec.add(this.endNode);
			this.nodeLinks = vec;
			nodeLinksSorted = true;
			this.sortedNodes = nodevec;
		}
	}

	public MapNodeLinkElement nextNodeLink(MapNodeLinkElement nl)
	{
		int index = getNodeLinks().indexOf(nl);
		if(index == getNodeLinks().size() - 1)
			return nl;
		else
			return (MapNodeLinkElement )getNodeLinks().get(index + 1);
	}

	public MapNodeLinkElement previousNodeLink(MapNodeLinkElement nl)
	{
		int index = getNodeLinks().indexOf(nl);
		if(index == 0)
			return nl;
		else
			return (MapNodeLinkElement )getNodeLinks().get(index - 1);
	}

	/**
	 * �������� ������� ���������
	 */
	public MapElementState getState()
	{
		return new MapPhysicalLinkElementState(this);
	}

	/**
	 * ������������ ���������
	 */
	public void revert(MapElementState state)
	{
		super.revert(state);
		
		MapPhysicalLinkElementState mples = (MapPhysicalLinkElementState )state;

		this.nodeLinks = new LinkedList();
		for(Iterator it = mples.nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			mnle.setPhysicalLinkId(getId());
			this.nodeLinks.add(mnle);
		}
		this.setMapProtoId(mples.mapProtoId);
		
		nodeLinksSorted = false;

		updateLengthLt();
	}

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[10][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PROTO_ID;
			exportColumns[4][0] = COLUMN_START_NODE_ID;
			exportColumns[5][0] = COLUMN_END_NODE_ID;
			exportColumns[6][0] = COLUMN_NODE_LINKS;
			exportColumns[7][0] = COLUMN_CITY;
			exportColumns[8][0] = COLUMN_STREET;
			exportColumns[9][0] = COLUMN_BUILDING;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getMapProtoId();
		exportColumns[4][1] = getStartNode().getId();
		exportColumns[5][1] = getEndNode().getId();
		exportColumns[6][1] = "";
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			exportColumns[6][1] += mnle.getId() + " ";
		}
		exportColumns[7][1] = getCity();
		exportColumns[8][1] = getStreet();
		exportColumns[9][1] = getBuilding();
		
		return exportColumns;
	}
	
	public void setColumn(String field, String value)
	{
		if(field.equals(COLUMN_ID))
			setId(value);
		else
		if(field.equals(COLUMN_NAME))
			setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			setDescription(value);
		else
		if(field.equals(COLUMN_PROTO_ID))
			setMapProtoId(value);
		else
		if(field.equals(COLUMN_START_NODE_ID))
			startNodeId = value;
		else
		if(field.equals(COLUMN_END_NODE_ID))
			endNodeId = value;
		else
		if(field.equals(COLUMN_NODE_LINKS))
		{
			nodeLinkIds.clear();
			for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
				nodeLinkIds.add(it.next());
		}
		else
		if(field.equals(COLUMN_CITY))
			setCity(value);
		else
		if(field.equals(COLUMN_STREET))
			setStreet(value);
		else
		if(field.equals(COLUMN_BUILDING))
			setBuilding(value);
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(mapId);

		startNodeId = getStartNode().getId();
		endNodeId = getEndNode().getId();

		out.writeObject(startNodeId);
		out.writeObject(endNodeId);

		out.writeObject(attributes);

		out.writeObject(mapProtoId);

		this.nodeLinkIds = new ArrayList();
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mne = (MapNodeLinkElement )it.next();
			nodeLinkIds.add(mne.getId());
		}
		out.writeObject(nodeLinkIds);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		mapId = (String )in.readObject();
		startNodeId = (String )in.readObject();
		endNodeId = (String )in.readObject();
		attributes = (HashMap )in.readObject();

		mapProtoId = (String )in.readObject();
		nodeLinkIds = (ArrayList )in.readObject();

		transferable = new MapPhysicalLinkElement_Transferable();

//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}


	public void setMapProtoId(String mapProtoId)
	{
		this.mapProtoId = mapProtoId;
		proto = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, mapProtoId);
	}


	public String getMapProtoId()
	{
		return mapProtoId;
	}


	public MapPhysicalLinkBinding getBinding()
	{
		return binding;
	}


	public void setCity(String city)
	{
		this.city = city;
	}


	public String getCity()
	{
		return city;
	}


	public void setStreet(String street)
	{
		this.street = street;
	}


	public String getStreet()
	{
		return street;
	}


	public void setBuilding(String building)
	{
		this.building = building;
	}


	public String getBuilding()
	{
		return building;
	}


	public void setProto(MapLinkProtoElement proto)
	{
		this.proto = proto;
		if(proto != null)
			this.mapProtoId = proto.getId();
	}


	public MapLinkProtoElement getProto()
	{
		return proto;
	}

	/**
	 * �������� ������� �����
	 */
	public int getLineSize ()
	{
		return proto.getLineSize();
	}

	/**
	 * �������� ��� �����
	 */
	public String getStyle ()
	{
		return proto.getStyle();
	}

	/**
	 * �������� ����� �����
	 */
	public Stroke getStroke ()
	{
		return proto.getStroke();
	}

	/**
	 * �������� ����
	 */
	public Color getColor()
	{
		return proto.getColor();
	}

	/**
	 * �������� ���� ��� ������� ������� �������
	 */
	public Color getAlarmedColor()
	{
		return proto.getAlarmedColor();
	}

	/**
	 * �������� ������� ����� ��� ������ ������� �������
	 */
	public int getAlarmedLineSize ()
	{
		return proto.getAlarmedLineSize();
	}
	
}
