/**
 * $Id: Map.java,v 1.3 2004/09/17 11:38:44 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.Map_Transferable;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс используется для хранения и информации по канализационной
 * прокладке кабелей и положению узлов и других топологических объектов
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/09/17 11:38:44 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class Map extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	protected Map_Transferable transferable;

	static final public String typ = "map";

	static final public String COLUMN_ID = "id";
	static final public String COLUMN_NAME = "name";
	static final public String COLUMN_USER_ID = "user_id";
	static final public String COLUMN_SCHEME_ID = "scheme_id";
	static final public String COLUMN_CREATED = "created";
	static final public String COLUMN_MODIFIED = "modified";
	
	protected String id = "";
	protected String name = "Без названия";
	protected String description = "";
	protected String user_id = "";
	protected String domain_id = "";

	protected long created = 0;
	protected String created_by = "";
	protected long modified = 0;
	protected String modified_by = "";

	protected ArrayList node_ids = new ArrayList();
	protected ArrayList site_ids = new ArrayList();
	protected ArrayList nodelink_ids = new ArrayList();
	protected ArrayList link_ids = new ArrayList();
	protected ArrayList mark_ids = new ArrayList();
	protected ArrayList collector_ids = new ArrayList();

	/** Вектор элементов наследников класса Node */
	protected ArrayList nodes = new ArrayList();
	/** Вектор элементов типа NodeLinks */
	protected ArrayList nodeLinks = new ArrayList();
	/** Вектор элементов типа physicalLinks */
	protected ArrayList physicalLinks = new ArrayList();
	/** Вектор элементов типа PipePaths */
	protected ArrayList collectors = new ArrayList();

	/** список удаленных элементов */
	protected LinkedList removedElements = new LinkedList();
	
	protected boolean isOpened = false;
	
	/**
	 * объект, осуществляющий преобразование топологических/экранных координат
	 */
	protected MapCoordinatesConverter converter;

	/**
	 * Используется для создания нового контекста пользователем
	 */
	public Map()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "Map()");
		created = System.currentTimeMillis();

		transferable = new Map_Transferable();
	}

	/**
	 * Используется для создания элемента при подгрузке из базы данных
	 */
	public Map(Map_Transferable transferable)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "Map(" + transferable + ")");
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * Клонирование объекта - оспользуется при сохранении контекста карты
	 * под новым именем
	 */
	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "clone(" + dataSource + ")");
		String cloned_id = (String )Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(com.syrus.AMFICOM.Client.Resource.Map.Map.typ, cloned_id);

		Map mc = new Map();

		mc.created_by = mc.user_id;
		mc.description = description;
		mc.domain_id = domain_id;
		mc.id = dataSource.GetUId(com.syrus.AMFICOM.Client.Resource.Map.Map.typ);
		mc.modified = mc.created;
		mc.modified_by = mc.user_id;
		mc.name = name + "(copy)";
		mc.user_id = dataSource.getSession().getUserId();

		Pool.put(com.syrus.AMFICOM.Client.Resource.Map.Map.typ, mc.getId(), mc);
		Pool.put("mapclonedids", id, mc.getId());

		mc.nodeLinks = new ArrayList();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
			mc.nodeLinks.add(((MapElement )it.next()).clone(dataSource));
			
		mc.nodes = new ArrayList();
		for(Iterator it = nodes.iterator(); it.hasNext();)
			mc.nodes.add(((MapElement)it.next()).clone(dataSource));
			
		mc.physicalLinks = new ArrayList();
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
			mc.physicalLinks.add((((MapElement)it.next()).clone(dataSource)));

		mc.collectors = new ArrayList();
		for(Iterator it = collectors.iterator(); it.hasNext();)
			mc.collectors.add(((MapElement)it.next()).clone(dataSource));
			
			
		mc.mark_ids = new ArrayList();
		for(Iterator it = mark_ids.iterator(); it.hasNext();)
			mc.mark_ids.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.nodelink_ids = new ArrayList();
		for(Iterator it = nodelink_ids.iterator(); it.hasNext();)
			mc.nodelink_ids.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.node_ids = new ArrayList();
		for(Iterator it = node_ids.iterator(); it.hasNext();)
			mc.node_ids.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.site_ids = new ArrayList();
		for(Iterator it = site_ids.iterator(); it.hasNext();)
			mc.site_ids.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.link_ids = new ArrayList();
		for(Iterator it = link_ids.iterator(); it.hasNext();)
			mc.link_ids.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.collector_ids = new ArrayList();
		for(Iterator it = collector_ids.iterator(); it.hasNext();)
			mc.collector_ids.add(Pool.get("mapclonedids", (String )it.next()));
			
		return mc;
	}
	
	/**
	 * Восстановление локальных переменных класса при подгрузке из базы данных
	 */
	public void setLocalFromTransferable()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setLocalFromTransferable()");
		int i;
		int count;

		id = transferable.id;
		name = transferable.name;
		user_id = transferable.userId;
		domain_id = transferable.domainId;
		created = transferable.created;
		modified = transferable.modified;
		modified_by = transferable.modifiedBy;
		created_by = transferable.createdBy;

		description = transferable.description;

		count = transferable.nodeIds.length;
		node_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			node_ids.add(transferable.nodeIds[i]);

		count = transferable.siteIds.length;
		site_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			site_ids.add(transferable.siteIds[i]);

		count = transferable.nodeLinkIds.length;
		nodelink_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			nodelink_ids.add(transferable.nodeLinkIds[i]);

		count = transferable.physicalLinkIds.length;
		link_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			link_ids.add(transferable.physicalLinkIds[i]);

		count = transferable.markIds.length;
		mark_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			mark_ids.add(transferable.markIds[i]);

		count = transferable.collectorIds.length;
		collector_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			collector_ids.add(transferable.collectorIds[i]);
	}

	/**
	 * Установка полей в transferable по значениям локальных переменных
	 * для сохранения в базе данных
	 */
	public void setTransferableFromLocal()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setTransferableFromLocal()");
		
		int i;
		int count;
		ObjectResource os;

		transferable.id = id;
		transferable.name = name;
		transferable.userId = user_id;
		transferable.domainId = domain_id;
		transferable.description = description;
		transferable.modified = System.currentTimeMillis();
		transferable.modifiedBy = user_id;
		transferable.createdBy = user_id;

		node_ids = new ArrayList();
		site_ids = new ArrayList();
		mark_ids = new ArrayList();

		count = nodes.size();
		for(Iterator it = nodes.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			if(os.getTyp().equals("mapnodeelement"))
				node_ids.add(os.getId());
			if(os.getTyp().equals("mapequipmentelement"))
				site_ids.add(os.getId());
			if(os.getTyp().equals("mapmarkelement"))
				mark_ids.add(os.getId());
		}
		transferable.nodeIds = (String [])node_ids.toArray(new String[node_ids.size()]);
		transferable.siteIds = (String [])site_ids.toArray(new String[site_ids.size()]);
		transferable.markIds = (String [])mark_ids.toArray(new String[mark_ids.size()]);

		nodelink_ids = new ArrayList();
		
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			nodelink_ids.add(os.getId());
		}
		transferable.nodeLinkIds = (String [])nodelink_ids.toArray(new String[nodelink_ids.size()]);

		link_ids = new ArrayList();
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			link_ids.add(os.getId());
		}
		transferable.physicalLinkIds = (String [])link_ids.toArray(new String[link_ids.size()]);

		collector_ids = new ArrayList();
		for(Iterator it = collectors.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			collector_ids.add(os.getId());
		}
		transferable.collectorIds = (String [])collector_ids.toArray(new String[collector_ids.size()]);
	}

	/**
	 * 
	 */
	public String getTyp()
	{
		return typ;
	}

	/**
	 * геттер
	 */
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * геттер
	 */
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * геттер
	 */
	public String getDomainId()
	{
		return domain_id;
	}
	
	public void setDomainId(String domain_id)
	{
		this.domain_id = domain_id;
	}
	
	public void setUserId(String user_id)
	{
		this.user_id = user_id;
	}
	
	/**
	 * геттер
	 */
	public long getModified()
	{
		return modified;
	}

	/**
	 * Используется для обновления содержимого локальных переменных по 
	 * значениям, полученным из transferable
	 */
	public void updateLocalFromTransferable()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateLocalFromTransferable()");
		
		nodes = new ArrayList();

		for(Iterator it = node_ids.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapPhysicalNodeElement.typ, (String )it.next()));
		for(Iterator it = site_ids.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapSiteNodeElement.typ, (String )it.next()));
		for(Iterator it = mark_ids.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapMarkElement.typ, (String)it.next()));

		nodeLinks = new ArrayList();
		for(Iterator it = nodelink_ids.iterator(); it.hasNext();)
			nodeLinks.add(Pool.get(MapNodeLinkElement.typ, (String )it.next()));

		physicalLinks = new ArrayList();
		for(Iterator it = link_ids.iterator(); it.hasNext();)
			physicalLinks.add(Pool.get(MapPhysicalLinkElement.typ, (String)it.next()));

		collectors = new ArrayList();
		for(Iterator it = collector_ids.iterator(); it.hasNext();)
			collectors.add(Pool.get(MapPipePathElement.typ, (String)it.next()));
	}

	/**
	 * обновление локального содержимого объектов, содержащихся в контексте
	 */
	public void updateFromPool()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateFromPool()");
		
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
	 * получить объект для сохранения в базе данных
	 */
	public Object getTransferable()
	{
		return transferable;
	}

	/**
	 * получить модель объекта
	 */
	public ObjectResourceModel getModel()
	{
		return null;//new MapModel(this);
	}

	public static ObjectResourcePropertiesPane getPropertyPane1()
	{
		return null;
	}

	/**
	 * получить панель свойств
	 */
	public static PropertiesPanel getPropertyPane()
	{
		return null;//new MapPane();
	}

	/**
	 * получить модель отображения в таблице
	 */
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapDisplayModel();
	}

	/**
	 * флаг того, что контекст открыт в окне карты
	 */
	public boolean isOpened()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "isOpened()");
		
		return this.isOpened;
	}
	
	public void setOpened(boolean opened)
	{
		this.isOpened = opened;
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
	 * Получение элементов - наследников класса Node
	 */
	public List getNodes()
	{
		return nodes;
	}

	/**
	 * Установить список элементов наследников класса Node
	 */
	public void setNodes(List _nodes)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setNodes(" + _nodes + ")");

		nodes.clear();
		for(Iterator it = _nodes.iterator(); it.hasNext();)
			nodes.add(it.next());
	}

	/**
	 * Добавить новый MapNodeElement
	 */
	public void addNode(MapNodeElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addNode(" + ob + ")");
		
		nodes.add(ob);
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * Удалить MapNodeElement
	 */
	public void removeNode(MapNodeElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeNode(" + ob + ")");
		
		nodes.remove(ob);
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	/**
	 * Получение список элементов типа NodeLinks
	 */
	public List getNodeLinks()
	{
		return nodeLinks;
	}

	/**
	 * Установить список элементов типа NodeLinks
	 */
	public void setNodeLinks(List _nodeLinks)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setNodeLinks(" + _nodeLinks + ")");

		nodeLinks.clear();
		for(Iterator it = _nodeLinks.iterator(); it.hasNext();)
			nodeLinks.add(it.next());
	}

	/**
	 * добавить новый MapNodeLinkElement
	 */
	public void addNodeLink(MapNodeLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addNodeLink(" + ob + ")");
		
		nodeLinks.add(ob);
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * Удалить MapNodeLinkElement
	 */
	public void removeNodeLink(MapNodeLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeNodeLink(" + ob + ")");
		
		nodeLinks.remove(ob);
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	/**
	 * Получение MapNodeLinkElement по его ID
	 */
	public MapNodeLinkElement getNodeLink(String mapNodeLinkElementID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLink(" + mapNodeLinkElementID + ")");
		
		Iterator e = getNodeLinks().iterator();

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
	 * Получить NodeLink по начальному и конечному узлам
	 */
	public MapNodeLinkElement getNodeLink(MapNodeElement start_node, MapNodeElement end_node)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLink(" + start_node + ", " + end_node + ")");
		
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
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
	 * Получить список физических линий
	 */
	public List getPhysicalLinks()
	{
		return physicalLinks;
	}

	/**
	 * Получить список физических линий
	 */
	public List getPhysicalLinksAt(MapNodeElement node)
	{
		LinkedList returnNodeLink = new LinkedList();
		Iterator e = getPhysicalLinks().iterator();

		while (e.hasNext())
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
			if ( (link.getEndNode() == node) || (link.getStartNode() == node))
				returnNodeLink.add(link);
		}
		return returnNodeLink;
	}

	/**
	 * Установпить список физических линий
	 */
	public void setPhysicalLinks(List vec)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setPhysicalLinks(" + vec + ")");
		
		physicalLinks.clear();
		for(Iterator it = vec.iterator(); it.hasNext();)
			physicalLinks.add(it.next());
	}

	/**
	 * Добавить новую физическую линию
	 */
	public void addPhysicalLink(MapPhysicalLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addPhysicalLink(" + ob + ")");
		
		physicalLinks.add(ob);
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * Удалить физическую линию
	 */
	public void removePhysicalLink(MapPhysicalLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removePhysicalLink(" + ob + ")");
		
		physicalLinks.remove(ob);
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	/**
	 * Получение MapPhysicalLinkElement по его ID
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
	 * Получить MapPhysicalLinkElement по начальному и конечному узлам
	 */
	public MapPhysicalLinkElement getPhysicalLink(MapNodeElement start_node, MapNodeElement end_node)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLink(" + start_node + ", " + end_node + ")");
		
		for(Iterator it = getPhysicalLinks().iterator(); it.hasNext();)
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
	 * Получение элементов - наследников класса Node
	 */
	public List getCollectors()
	{
		return collectors;
	}

	/**
	 * Установить список элементов наследников класса Node
	 */
	public void setCollectors(List _collectors)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCollectors(" + _collectors + ")");

		collectors.clear();
		for(Iterator it = _collectors.iterator(); it.hasNext();)
			collectors.add(it.next());
	}

	/**
	 * Добавить новый MapNodeElement
	 */
	public void addCollector(MapPipePathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addCollector(" + ob + ")");
		
		collectors.add(ob);
		ob.setRemoved(false);
		removedElements.remove(ob);
	}

	/**
	 * Удалить MapNodeElement
	 */
	public void removeCollector(MapPipePathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeCollector(" + ob + ")");
		
		collectors.remove(ob);
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	/**
	 * Получить список топологических узлов
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
	 * Получить список узлов
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
	 * Получить точечный объект по ID
	 */
	public MapNodeElement getNode(String nodeID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNode(" + nodeID + ")");
		
		for(Iterator it = getNodes().iterator(); it.hasNext();)
		{
			MapNodeElement node = (MapNodeElement )it.next();
			if ( node.getId().equals(nodeID ) )
				return node;
		}
		return null;
	}

	/**
	 * Получить элемент узла по ID
	 */
	public MapSiteNodeElement getMapSiteNodeElement(String nodeId)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapSiteNodeElement(" + nodeId + ")");
		
		Iterator e = getMapSiteNodeElements().iterator();
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
	 * Получить список меток
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
	 * Получить список всех олементов контекста карты
	 */
	public List getAllElements1()
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
		return returnVector;
	}

	/**
	 * Получить список удаленных элементов
	 */
	public LinkedList getRemovedElements()
	{
		return removedElements;
	}

	/**
	 * Отменить выбор всем элементам
	 */
/*	 
	public void deselectAll1()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");
		
		Iterator e = getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			mapElement.setSelected(false);
		}
	}
*/
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "writeObject(out)");
		
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(user_id);
		out.writeObject(domain_id);
		out.writeLong(created);
		out.writeObject(created_by);
		out.writeLong(modified);
		out.writeObject(modified_by);
		out.writeObject(nodes);
		out.writeObject(nodeLinks);
		out.writeObject(physicalLinks);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "readObject(in)");
		
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		user_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		created = in.readLong();
		created_by = (String )in.readObject();
		modified = in.readLong();
		modified_by = (String )in.readObject();
		nodes = (ArrayList )in.readObject();
		nodeLinks = (ArrayList )in.readObject();
		physicalLinks = (ArrayList )in.readObject();

		transferable = new Map_Transferable();

		node_ids = new ArrayList();
		site_ids = new ArrayList();
		nodelink_ids = new ArrayList();
		link_ids = new ArrayList();
		mark_ids = new ArrayList();
	
		removedElements = new LinkedList();
	
//		deleted_nodes_ids = new LinkedList();
//		deleted_nodeLinks_ids = new LinkedList();
//		deleted_physicalLinks_ids = new LinkedList();
		updateFromPool();
//		Pool.put("serverimage", getId(), this);
	}
	
}
