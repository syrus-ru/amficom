/**
 * $Id: Map.java,v 1.6 2004/10/04 15:58:19 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;

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
 * @version $Revision: 1.6 $, $Date: 2004/10/04 15:58:19 $
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
	protected String name = "Без названия";
	protected String description = "";
	protected String userId = "";
	protected String domainId = "";

	protected long created = 0;
	protected String createdBy = "";
	protected long modified = 0;
	protected String modifiedBy = "";

	protected ArrayList nodeIds = new ArrayList();
	protected ArrayList siteIds = new ArrayList();
	protected ArrayList nodelinkIds = new ArrayList();
	protected ArrayList linkIds = new ArrayList();
	protected ArrayList markIds = new ArrayList();
	protected ArrayList collectorIds = new ArrayList();

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

	public static String[][] exportColumns = null;

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
		if(field.equals(COLUMN_ID))
			setId(value);
		else
		if(field.equals(COLUMN_NAME))
			setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			setDescription(value);
	}

	/**
	 * Клонирование объекта - оспользуется при сохранении контекста карты
	 * под новым именем
	 */
	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "clone(" + dataSource + ")");
		String clonedId = (String )Pool.get("mapclonedids", id);
		if (clonedId != null)
			return Pool.get(com.syrus.AMFICOM.Client.Resource.Map.Map.typ, clonedId);

		Map mc = new Map();

		mc.createdBy = mc.userId;
		mc.description = description;
		mc.domainId = domainId;
		mc.id = dataSource.GetUId(com.syrus.AMFICOM.Client.Resource.Map.Map.typ);
		mc.modified = mc.created;
		mc.modifiedBy = mc.userId;
		mc.name = name + "(copy)";
		mc.userId = dataSource.getSession().getUserId();

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
			
			
		mc.markIds = new ArrayList();
		for(Iterator it = markIds.iterator(); it.hasNext();)
			mc.markIds.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.nodelinkIds = new ArrayList();
		for(Iterator it = nodelinkIds.iterator(); it.hasNext();)
			mc.nodelinkIds.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.nodeIds = new ArrayList();
		for(Iterator it = nodeIds.iterator(); it.hasNext();)
			mc.nodeIds.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.siteIds = new ArrayList();
		for(Iterator it = siteIds.iterator(); it.hasNext();)
			mc.siteIds.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.linkIds = new ArrayList();
		for(Iterator it = linkIds.iterator(); it.hasNext();)
			mc.linkIds.add(Pool.get("mapclonedids", (String )it.next()));
			
		mc.collectorIds = new ArrayList();
		for(Iterator it = collectorIds.iterator(); it.hasNext();)
			mc.collectorIds.add(Pool.get("mapclonedids", (String )it.next()));
			
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
		userId = transferable.userId;
		domainId = transferable.domainId;
		created = transferable.created;
		modified = transferable.modified;
		modifiedBy = transferable.modifiedBy;
		createdBy = transferable.createdBy;

		description = transferable.description;

		count = transferable.nodeIds.length;
		nodeIds = new ArrayList(count);
		for(i = 0; i < count; i++)
			nodeIds.add(transferable.nodeIds[i]);

		count = transferable.siteIds.length;
		siteIds = new ArrayList(count);
		for(i = 0; i < count; i++)
			siteIds.add(transferable.siteIds[i]);

		count = transferable.nodeLinkIds.length;
		nodelinkIds = new ArrayList(count);
		for(i = 0; i < count; i++)
			nodelinkIds.add(transferable.nodeLinkIds[i]);

		count = transferable.physicalLinkIds.length;
		linkIds = new ArrayList(count);
		for(i = 0; i < count; i++)
			linkIds.add(transferable.physicalLinkIds[i]);

		count = transferable.markIds.length;
		markIds = new ArrayList(count);
		for(i = 0; i < count; i++)
			markIds.add(transferable.markIds[i]);

		count = transferable.collectorIds.length;
		collectorIds = new ArrayList(count);
		for(i = 0; i < count; i++)
			collectorIds.add(transferable.collectorIds[i]);
	}

	/**
	 * Установка полей в transferable по значениям локальных переменных
	 * для сохранения в базе данных
	 */
	public void setTransferableFromLocal()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setTransferableFromLocal()");
		
		ObjectResource os;

		transferable.id = id;
		transferable.name = name;
		transferable.userId = userId;
		transferable.domainId = domainId;
		transferable.description = description;
		transferable.modified = System.currentTimeMillis();
		transferable.modifiedBy = userId;
		transferable.createdBy = userId;

		nodeIds = new ArrayList();
		siteIds = new ArrayList();
		markIds = new ArrayList();

		for(Iterator it = nodes.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			if(os.getTyp().equals("mapnodeelement"))
				nodeIds.add(os.getId());
			if(os.getTyp().equals("mapequipmentelement"))
				siteIds.add(os.getId());
			if(os.getTyp().equals("mapmarkelement"))
				markIds.add(os.getId());
		}
		transferable.nodeIds = (String [])nodeIds.toArray(new String[nodeIds.size()]);
		transferable.siteIds = (String [])siteIds.toArray(new String[siteIds.size()]);
		transferable.markIds = (String [])markIds.toArray(new String[markIds.size()]);

		nodelinkIds = new ArrayList();
		
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			nodelinkIds.add(os.getId());
		}
		transferable.nodeLinkIds = (String [])nodelinkIds.toArray(new String[nodelinkIds.size()]);

		linkIds = new ArrayList();
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			linkIds.add(os.getId());
		}
		transferable.physicalLinkIds = (String [])linkIds.toArray(new String[linkIds.size()]);

		collectorIds = new ArrayList();
		for(Iterator it = collectors.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			collectorIds.add(os.getId());
		}
		transferable.collectorIds = (String [])collectorIds.toArray(new String[collectorIds.size()]);
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

		for(Iterator it = nodeIds.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapPhysicalNodeElement.typ, (String )it.next()));
		for(Iterator it = siteIds.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapSiteNodeElement.typ, (String )it.next()));
		for(Iterator it = markIds.iterator(); it.hasNext();)
			nodes.add(Pool.get(MapMarkElement.typ, (String)it.next()));

		nodeLinks = new ArrayList();
		for(Iterator it = nodelinkIds.iterator(); it.hasNext();)
			nodeLinks.add(Pool.get(MapNodeLinkElement.typ, (String )it.next()));

		physicalLinks = new ArrayList();
		for(Iterator it = linkIds.iterator(); it.hasNext();)
			physicalLinks.add(Pool.get(MapPhysicalLinkElement.typ, (String)it.next()));

		collectors = new ArrayList();
		for(Iterator it = collectorIds.iterator(); it.hasNext();)
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
		if(ob instanceof MapPhysicalNodeElement)
			nodeIds.add(ob.getId());
		else
		if(ob instanceof MapSiteNodeElement)
			siteIds.add(ob.getId());
		else
		if(ob instanceof MapMarkElement)
			markIds.add(ob.getId());
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
		if(ob instanceof MapPhysicalNodeElement)
			nodeIds.remove(ob.getId());
		else
		if(ob instanceof MapSiteNodeElement)
			siteIds.remove(ob.getId());
		else
		if(ob instanceof MapMarkElement)
			markIds.remove(ob.getId());
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
		nodelinkIds.add(ob.getId());
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
		nodelinkIds.remove(ob.getId());
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
		linkIds.add(ob.getId());
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
		linkIds.remove(ob.getId());
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
		collectorIds.add(ob.getId());
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
		collectorIds.remove(ob.getId());
		ob.setRemoved(true);
		removedElements.add(ob);
	}

	public MapPipePathElement getCollector(MapPhysicalLinkElement mple)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getCollector(" + mple + ")");
		
		for(Iterator it = getCollectors().iterator(); it.hasNext();)
		{
			MapPipePathElement cp = (MapPipePathElement )it.next();
			if(cp.getLinks().contains(mple))
				return cp;
		}
		return null;
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
		out.writeObject(userId);
		out.writeObject(domainId);
		out.writeLong(created);
		out.writeObject(createdBy);
		out.writeLong(modified);
		out.writeObject(modifiedBy);
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
		userId = (String )in.readObject();
		domainId = (String )in.readObject();
		created = in.readLong();
		createdBy = (String )in.readObject();
		modified = in.readLong();
		modifiedBy = (String )in.readObject();
		nodes = (ArrayList )in.readObject();
		nodeLinks = (ArrayList )in.readObject();
		physicalLinks = (ArrayList )in.readObject();

		transferable = new Map_Transferable();

		nodeIds = new ArrayList();
		siteIds = new ArrayList();
		nodelinkIds = new ArrayList();
		linkIds = new ArrayList();
		markIds = new ArrayList();
	
		removedElements = new LinkedList();
	
//		deleted_nodesIds = new LinkedList();
//		deleted_nodeLinksIds = new LinkedList();
//		deleted_physicalLinksIds = new LinkedList();
		updateFromPool();
//		Pool.put("serverimage", getId(), this);
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


	public String getDescription()
	{
		return description;
	}
	
}
