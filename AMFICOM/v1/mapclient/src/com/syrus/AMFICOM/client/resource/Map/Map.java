/**
 * $Id: Map.java,v 1.16 2004/10/27 15:45:58 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.Map_Transferable;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * ����� ������������ ��� �������� � ���������� �� ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.16 $, $Date: 2004/10/27 15:45:58 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class Map extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "map";

	protected Map_Transferable transferable;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_CREATED_BY = "created_by";
	public static final String COLUMN_MODIFIED = "modified";
	public static final String COLUMN_MODIFIED_BY = "modified_by";
	
	protected String id = "";
	protected String name = "��� ��������";
	protected String description = "";
	protected String userId = "";
	protected String domainId = "";

	protected long created = 0;
	protected String createdBy = "";
	protected long modified = 0;
	protected String modifiedBy = "";

	/** ��������������� ������ ��� �������� � ������� */
	protected List nodeIds = new LinkedList();
	/** ��������������� ������ ��� �������� � ������� */
	protected List siteIds = new LinkedList();
	/** ��������������� ������ ��� �������� � ������� */
	protected List nodelinkIds = new LinkedList();
	/** ��������������� ������ ��� �������� � ������� */
	protected List linkIds = new LinkedList();
	/** ��������������� ������ ��� �������� � ������� */
	protected List markIds = new LinkedList();
	/** ��������������� ������ ��� �������� � ������� */
	protected List collectorIds = new LinkedList();

	/** ������ ��������� ����������� ������ Node */
	protected List nodes = new LinkedList();
	/** ������ ��������� ���� NodeLinks */
	protected List nodeLinks = new LinkedList();
	/** ������ ��������� ���� physicalLinks */
	protected List physicalLinks = new LinkedList();
	/** ������ ��������� ���� PipePaths */
	protected List collectors = new LinkedList();

	/** ������ ��������� ��������� */
	protected LinkedList removedElements = new LinkedList();
	
	/**
	 * ������, �������������� �������������� ��������������/�������� ���������
	 */
	protected MapCoordinatesConverter converter;

	/** ������ ���������� �������� */
	protected Set selectedElements = new HashSet();

	/** 
	 * ������ ���������� ��� ��������. ���������������� ������ � ������
	 * ������������� ��������
	 */
	public static String[][] exportColumns = null;
	
	/**
	 * ������������ ��� �������� ������ ��������� �������������
	 */
	public Map()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"Map()");
		created = System.currentTimeMillis();

		transferable = new Map_Transferable();
	}

	/**
	 * ������������ ��� �������� �������� ��� ��������� �� ���� ������
	 */
	public Map(Map_Transferable transferable)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"Map(" + transferable + ")");
		this.transferable = transferable;
		this.setLocalFromTransferable();
	}

	/**
	 * ������������ ������� - ������������ ��� ���������� ��������� �����
	 * ��� ����� ������. ��� ���� ��� ���������� ������ �� ������ �����������
	 * (��� �������������) ������� ������������ ��������� 
	 * 		Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id
	 * � ������� �� ����� ������� Id �������� Id ������ �������������� �������
	 */
	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"clone(" + dataSource + ")");

		String clonedId = (String )Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);

		if (clonedId != null)
			return Pool.get(Map.typ, clonedId);

		Map mc = (Map )super.clone();

		mc.createdBy = mc.userId;
		mc.description = description;
		mc.domainId = domainId;
		mc.id = dataSource.GetUId(Map.typ);
		mc.modified = mc.created;
		mc.modifiedBy = mc.userId;
		mc.name = name + "(copy)";
		mc.userId = dataSource.getSession().getUserId();

		Pool.put(Map.typ, mc.getId(), mc);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mc.getId());

		mc.nodeLinks = new LinkedList();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
			mc.nodeLinks.add(((MapElement )it.next()).clone(dataSource));
			
		mc.nodes = new LinkedList();
		for(Iterator it = nodes.iterator(); it.hasNext();)
			mc.nodes.add(((MapElement)it.next()).clone(dataSource));
			
		mc.physicalLinks = new LinkedList();
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
			mc.physicalLinks.add((((MapElement)it.next()).clone(dataSource)));

		mc.collectors = new LinkedList();
		for(Iterator it = collectors.iterator(); it.hasNext();)
			mc.collectors.add(((MapElement)it.next()).clone(dataSource));
			
		mc.markIds = new LinkedList();
		for(Iterator it = markIds.iterator(); it.hasNext();)
			mc.markIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.nodelinkIds = new LinkedList();
		for(Iterator it = nodelinkIds.iterator(); it.hasNext();)
			mc.nodelinkIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.nodeIds = new LinkedList();
		for(Iterator it = nodeIds.iterator(); it.hasNext();)
			mc.nodeIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.siteIds = new LinkedList();
		for(Iterator it = siteIds.iterator(); it.hasNext();)
			mc.siteIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.linkIds = new LinkedList();
		for(Iterator it = linkIds.iterator(); it.hasNext();)
			mc.linkIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.collectorIds = new LinkedList();
		for(Iterator it = collectorIds.iterator(); it.hasNext();)
			mc.collectorIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		return mc;
	}
	
	/**
	 * �������������� ��������� ���������� ������ ��� ��������� �� ���� ������
	 */
	public void setLocalFromTransferable()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setLocalFromTransferable()");
		int i;
		int count;

		id = transferable.id;
		name = transferable.name;
		userId = transferable.userId;
		domainId = transferable.domainId;
		created = transferable.created;
		modified = transferable.modified;
		modifiedBy = transferable.modifiedBy;
		createdBy = transferable.createdBy;

		description = transferable.description;

		count = transferable.nodeIds.length;
		nodeIds = new LinkedList();
		for(i = 0; i < count; i++)
			nodeIds.add(transferable.nodeIds[i]);

		count = transferable.siteIds.length;
		siteIds = new LinkedList();
		for(i = 0; i < count; i++)
			siteIds.add(transferable.siteIds[i]);

		count = transferable.nodeLinkIds.length;
		nodelinkIds = new LinkedList();
		for(i = 0; i < count; i++)
			nodelinkIds.add(transferable.nodeLinkIds[i]);

		count = transferable.physicalLinkIds.length;
		linkIds = new LinkedList();
		for(i = 0; i < count; i++)
			linkIds.add(transferable.physicalLinkIds[i]);

		count = transferable.markIds.length;
		markIds = new LinkedList();
		for(i = 0; i < count; i++)
			markIds.add(transferable.markIds[i]);

		count = transferable.collectorIds.length;
		collectorIds = new LinkedList();
		for(i = 0; i < count; i++)
			collectorIds.add(transferable.collectorIds[i]);
	}

	/**
	 * ��������� ����� � transferable �� ��������� ��������� ����������
	 * ��� ���������� � ���� ������
	 */
	public void setTransferableFromLocal()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setTransferableFromLocal()");
		
		ObjectResource os;

		transferable.id = id;
		transferable.name = name;
		transferable.userId = userId;
		transferable.domainId = domainId;
		transferable.description = description;
		transferable.modified = System.currentTimeMillis();
		transferable.modifiedBy = userId;
		transferable.createdBy = userId;

		nodeIds = new LinkedList();
		siteIds = new LinkedList();
		markIds = new LinkedList();

		for(Iterator it = nodes.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			if(os.getTyp().equals(MapPhysicalNodeElement.typ))
				nodeIds.add(os.getId());
			if(os.getTyp().equals(MapSiteNodeElement.typ))
				siteIds.add(os.getId());
			if(os.getTyp().equals(MapMarkElement.typ))
				markIds.add(os.getId());
		}
		transferable.nodeIds = (String [])nodeIds.toArray(new String[nodeIds.size()]);
		transferable.siteIds = (String [])siteIds.toArray(new String[siteIds.size()]);
		transferable.markIds = (String [])markIds.toArray(new String[markIds.size()]);

		nodelinkIds = new LinkedList();
		
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			nodelinkIds.add(os.getId());
		}
		transferable.nodeLinkIds = (String [])nodelinkIds.toArray(
				new String[nodelinkIds.size()]);

		linkIds = new LinkedList();
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			linkIds.add(os.getId());
		}
		transferable.physicalLinkIds = (String [])linkIds.toArray(
				new String[linkIds.size()]);

		collectorIds = new LinkedList();
		for(Iterator it = collectors.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			collectorIds.add(os.getId());
		}
		transferable.collectorIds = (String [])collectorIds.toArray(
				new String[collectorIds.size()]);
	}

	/**
	 * ������������ ��� ���������� ����������� ��������� ���������� �� 
	 * ���������, ���������� �� transferable
	 */
	public void updateLocalFromTransferable()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"updateLocalFromTransferable()");
		
		nodes = new LinkedList();

		for(Iterator it = nodeIds.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapPhysicalNodeElement.typ, (String )it.next()));
		for(Iterator it = siteIds.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapSiteNodeElement.typ, (String )it.next()));
		for(Iterator it = markIds.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapMarkElement.typ, (String)it.next()));

		nodeLinks = new LinkedList();
		for(Iterator it = nodelinkIds.iterator(); it.hasNext();)
			nodeLinks.add(Pool.get(MapNodeLinkElement.typ, (String )it.next()));

		physicalLinks = new LinkedList();
		for(Iterator it = linkIds.iterator(); it.hasNext();)
			physicalLinks.add(Pool.get(MapPhysicalLinkElement.typ, (String)it.next()));

		collectors = new LinkedList();
		for(Iterator it = collectorIds.iterator(); it.hasNext();)
			collectors.add(Pool.get(MapPipePathElement.typ, (String)it.next()));
	}

	/**
	 * ���������� ���������� ����������� ��������, ������������ � �����.
	 * ������������ ��� �������� �� ����
	 */
	public void updateFromPool()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"updateFromPool()");

		for(Iterator it = nodes.iterator(); it.hasNext();)
		{
			ObjectResource os = (ObjectResource )it.next();
			os.updateLocalFromTransferable();
		}
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			ObjectResource os = (ObjectResource )it.next();
			os.updateLocalFromTransferable();
		}
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
		{
			ObjectResource os = (ObjectResource )it.next();
			os.updateLocalFromTransferable();
		}
		for(Iterator it = collectors.iterator(); it.hasNext();)
		{
			ObjectResource os = (ObjectResource )it.next();
			os.updateLocalFromTransferable();
		}
	}

	/**
	 * �������� ������ ��� ���������� � ���� ������
	 */
	public Object getTransferable()
	{
		return transferable;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapPanel";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public MapCoordinatesConverter getConverter()
	{
		return converter;
	}
	
	public void setConverter(MapCoordinatesConverter converter)
	{
		this.converter = converter;
	}
	
	/**
	 * ��������� ��������� - ����������� ������ Node
	 */
	public List getNodes()
	{
		return nodes;
	}

	/**
	 * ���������� ������ ��������� ����������� ������ Node
	 */
	public void setNodes(List _nodes)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setNodes(" + _nodes + ")");

		nodes.clear();
		for(Iterator it = _nodes.iterator(); it.hasNext();)
			nodes.add(it.next());
	}

	/**
	 * �������� ����� MapNodeElement
	 */
	public void addNode(MapNodeElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addNode(" + ob + ")");
		
		nodes.add(ob);
//		if(ob instanceof MapPhysicalNodeElement)
//			nodeIds.add(ob.getId());
//		else
//		if(ob instanceof MapSiteNodeElement)
//			siteIds.add(ob.getId());
//		else
//		if(ob instanceof MapMarkElement)
//			markIds.add(ob.getId());
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * ������� MapNodeElement
	 */
	public void removeNode(MapNodeElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeNode(" + ob + ")");
		
		ob.setSelected(false);
		nodes.remove(ob);
//		if(ob instanceof MapPhysicalNodeElement)
//			nodeIds.remove(ob.getId());
//		else
//		if(ob instanceof MapSiteNodeElement)
//			siteIds.remove(ob.getId());
//		else
//		if(ob instanceof MapMarkElement)
//			markIds.remove(ob.getId());
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	/**
	 * ��������� ������ ��������� ���� NodeLinks
	 */
	public List getNodeLinks()
	{
		return nodeLinks;
	}

	/**
	 * ���������� ������ ��������� ���� NodeLinks
	 */
	public void setNodeLinks(List _nodeLinks)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setNodeLinks(" + _nodeLinks + ")");

		nodeLinks.clear();
		for(Iterator it = _nodeLinks.iterator(); it.hasNext();)
			nodeLinks.add(it.next());
	}

	/**
	 * �������� ����� MapNodeLinkElement
	 */
	public void addNodeLink(MapNodeLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addNodeLink(" + ob + ")");
		
		nodeLinks.add(ob);
//		nodelinkIds.add(ob.getId());
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * ������� MapNodeLinkElement
	 */
	public void removeNodeLink(MapNodeLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeNodeLink(" + ob + ")");
		
		nodeLinks.remove(ob);
		ob.setSelected(false);
//		nodelinkIds.remove(ob.getId());
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	/**
	 * ��������� MapNodeLinkElement �� ��� ID
	 */
	public MapNodeLinkElement getNodeLink(String mapNodeLinkElementID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLink(" + mapNodeLinkElementID + ")");
		
		Iterator e = this.getNodeLinks().iterator();

		while (e.hasNext())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
			if ( nodeLink.getId().equals( mapNodeLinkElementID) )
			{
				return nodeLink;
			}
		}
		return null;
	}

	/**
	 * �������� NodeLink �� ���������� � ��������� �����
	 */
	public MapNodeLinkElement getNodeLink(MapNodeElement start_node, MapNodeElement end_node)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLink(" + start_node + ", " + end_node + ")");
		
		for(Iterator it = this.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement link = (MapNodeLinkElement )it.next();
			if (((link.startNode == start_node) && (link.endNode == end_node)) ||
				((link.startNode == end_node) && (link.endNode == start_node)) )
			{
				return link;
			}
		}
		return null;
	}

	/**
	 * �������� ������ ���������� �����
	 */
	public List getPhysicalLinks()
	{
		return physicalLinks;
	}

	/**
	 * �������� ������ ���������� �����, ������������ ��� ���������������
	 * � ���� node
	 */
	public List getPhysicalLinksAt(MapNodeElement node)
	{
		LinkedList returnNodeLink = new LinkedList();
		Iterator e = this.getPhysicalLinks().iterator();

		while (e.hasNext())
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
			if ( (link.getEndNode() == node) || (link.getStartNode() == node))
				returnNodeLink.add(link);
		}
		return returnNodeLink;
	}

	/**
	 * ����������� ������ ���������� �����
	 */
	public void setPhysicalLinks(List vec)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setPhysicalLinks(" + vec + ")");
		
		physicalLinks.clear();
		for(Iterator it = vec.iterator(); it.hasNext();)
			physicalLinks.add(it.next());
	}

	/**
	 * �������� ����� ���������� �����
	 */
	public void addPhysicalLink(MapPhysicalLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addPhysicalLink(" + ob + ")");
		
		physicalLinks.add(ob);
//		linkIds.add(ob.getId());
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * ������� ���������� �����
	 */
	public void removePhysicalLink(MapPhysicalLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removePhysicalLink(" + ob + ")");
		
		physicalLinks.remove(ob);
		ob.setSelected(false);
//		linkIds.remove(ob.getId());
		ob.setRemoved(true);
		removedElements.add(ob);
		
		MapPipePathElement coll = getCollector(ob);
		if(coll != null)
			coll.removeLink(ob);
	}

	/**
	 * ��������� MapPhysicalLinkElement �� ��� ID
	 */
	public MapPhysicalLinkElement getPhysicalLink(String mapPhysicalLinkElementID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLink(" + mapPhysicalLinkElementID + ")");
		
		Iterator e = this.getPhysicalLinks().iterator();

		while (e.hasNext())
		{
			MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement )e.next();
			if ( physicalLink.getId().equals( mapPhysicalLinkElementID) )
				return physicalLink;
		}
		return null;
	}

	/**
	 * �������� MapPhysicalLinkElement �� ���������� � ��������� �����
	 */
	public MapPhysicalLinkElement getPhysicalLink(MapNodeElement start_node, MapNodeElement end_node)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLink(" + start_node + ", " + end_node + ")");
		
		for(Iterator it = this.getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if (((link.startNode == start_node) && (link.endNode == end_node)) ||
				((link.startNode == end_node) && (link.endNode == start_node)) )
			{
				return link;
			}
		}
		return null;
	}

	/**
	 * ��������� ��������� - ����������� ������ Node
	 */
	public List getCollectors()
	{
		return collectors;
	}

	/**
	 * ���������� ������ ��������� ����������� ������ Node
	 */
	public void setCollectors(List _collectors)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCollectors(" + _collectors + ")");

		collectors.clear();
		for(Iterator it = _collectors.iterator(); it.hasNext();)
			collectors.add(it.next());
	}

	/**
	 * �������� ����� MapNodeElement
	 */
	public void addCollector(MapPipePathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addCollector(" + ob + ")");
		
		collectors.add(ob);
//		collectorIds.add(ob.getId());
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * ������� MapNodeElement
	 */
	public void removeCollector(MapPipePathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeCollector(" + ob + ")");
		
		collectors.remove(ob);
		ob.setSelected(false);
//		collectorIds.remove(ob.getId());
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	/**
	 * �������� ���������, � ������� �������� ���� ������� mple
	 */
	public MapPipePathElement getCollector(MapPhysicalLinkElement mple)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getCollector(" + mple + ")");
		
		for(Iterator it = this.getCollectors().iterator(); it.hasNext();)
		{
			MapPipePathElement cp = (MapPipePathElement )it.next();
			if(cp.getLinks().contains(mple))
				return cp;
		}
		return null;
	}

	/**
	 * �������� ������ �������������� �����
	 */
	public List getMapPhysicalNodeElements()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapPhysicalNodeElements()");
		
		List returnVector = new LinkedList();

		Iterator e = nodes.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			if ( mapElement instanceof MapPhysicalNodeElement)
				returnVector.add( mapElement);
		}
		return returnVector;
	}

	/**
	 * �������� ������ ������� �����
	 */
	public List getMapSiteNodeElements()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapSiteNodeElements()");
		
		LinkedList returnVector = new LinkedList();

		Iterator e = nodes.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			if ( mapElement instanceof MapSiteNodeElement)
				returnVector.add( mapElement);
		}
		return returnVector;
	}

	/**
	 * �������� �������� ������ �� ID
	 */
	public MapNodeElement getNode(String nodeID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNode(" + nodeID + ")");
		
		for(Iterator it = this.getNodes().iterator(); it.hasNext();)
		{
			MapNodeElement node = (MapNodeElement )it.next();
			if ( node.getId().equals(nodeID ) )
				return node;
		}
		return null;
	}

	/**
	 * �������� ������� �������� ���� �� ID
	 */
	public MapSiteNodeElement getMapSiteNodeElement(String nodeId)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapSiteNodeElement(" + nodeId + ")");
		
		Iterator e = this.getMapSiteNodeElements().iterator();
		while (e.hasNext())
		{
			MapSiteNodeElement msne = 
				(MapSiteNodeElement )e.next();

			if ( msne.getId().equals(nodeId))
				return msne;
		}
		return null;
	}

	/**
	 * �������� ������ �����
	 */
	public List getMapMarkElements()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapMarkElements()");
		
		LinkedList returnVector = new LinkedList();

		Iterator e = nodes.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			if ( mapElement instanceof MapMarkElement)
				returnVector.add( mapElement);
		}
		return returnVector;
	}

	/**
	 * �������� ������ ���� �������������� ��������� �����
	 */
	public List getAllElements()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getAllElements()");
		
		LinkedList returnVector = new LinkedList();

		Iterator e = nodes.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}

		e = nodeLinks.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}

		e = physicalLinks.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}

		e = this.collectors.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}
		return returnVector;
	}

	/**
	 * �������� ������ ��������� ���������.
	 * ������������ ��� ���������� � ��
	 */
	public LinkedList getRemovedElements()
	{
		return removedElements;
	}

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[3][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		
		return exportColumns;
	}

	public void setColumn(String field, String value)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setColumn(" + field + ", "+ value + ")");
		if(field.equals(COLUMN_ID))
			this.setId(value);
		else
		if(field.equals(COLUMN_NAME))
			this.setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			this.setDescription(value);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "writeObject(out)");
		
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(userId);
		out.writeObject(domainId);
		out.writeLong(created);
		out.writeObject(createdBy);
		out.writeLong(modified);
		out.writeObject(modifiedBy);
		out.writeObject(nodes);
		out.writeObject(nodeLinks);
		out.writeObject(physicalLinks);
		out.writeObject(collectors);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "readObject(in)");
		
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		userId = (String )in.readObject();
		domainId = (String )in.readObject();
		created = in.readLong();
		createdBy = (String )in.readObject();
		modified = in.readLong();
		modifiedBy = (String )in.readObject();
		nodes = (List )in.readObject();
		nodeLinks = (List )in.readObject();
		physicalLinks = (List )in.readObject();
		collectors = (List )in.readObject();

		transferable = new Map_Transferable();

		nodeIds = new LinkedList();
		siteIds = new LinkedList();
		nodelinkIds = new LinkedList();
		linkIds = new LinkedList();
		markIds = new LinkedList();
		collectorIds = new LinkedList();
	
		removedElements = new LinkedList();
	
		Pool.put(getTyp(), getId(), this);
//		Pool.put("serverimage", getId(), this);

		this.updateFromPool();
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getDomainId()
	{
		return domainId;
	}
	
	public void setDomainId(String domainId)
	{
		this.domainId = domainId;
	}
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public long getModified()
	{
		return modified;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}


	public String getDescription()
	{
		return description;
	}


	public String getUserId()
	{
		return userId;
	}


	public long getCreated()
	{
		return created;
	}
	
	public Set getSelectedElements()
	{
		return selectedElements;
	}
	
	public void clearSelection()
	{
		selectedElements.clear();
	}
	
	public void setSelected(MapElement me, boolean selected)
	{
		if(selected)
			selectedElements.add(me);
		else
			selectedElements.remove(me);
	}

}
