/*
 * $Id: MapContext.java,v 1.6 2004/08/17 11:09:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Map.UI.Display.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import java.awt.*;

import java.io.*;

import java.util.*;

import javax.swing.*;

/**
 * Класс $RCSfile: MapContext.java,v $ используется для описания контекста карты (её элементов и свойств)
 * Контекст содержит в себе информацию об узлах, колодцах, развязках, местах
 * пролегания кабелей. Контекст карты является каркасом для прокладки
 * топологической схемы сети (MapScheme)
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/08/17 11:09:02 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapContext extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public MapContext_Transferable transferable;
	static final public String typ = "mapcontext";
	
	// В maptoolbar есть есть кнопки которые устанавливают значения этих полей
	// какие элементы показываьб на карте
	static final public int SHOW_NODE_LINK = 1;//Показывать nodeLink
	static final public int SHOW_PHYSICALLINK = 2;//Показывать physicalLink
	static final public int SHOW_TRANSMISSION_PATH = 3;//Показывать transmissionPath

	public String id = "";
	public String name = "";
	public String description = "";
	public String codename = "";
	public String user_id = "";
	public String domain_id = "";

	public double longitude = 0.0;
	public double latitude = 0.0;

	public long created = 0;
	public String created_by = "";
	public long modified = 0;
	public String modified_by = "";

	public String scheme_id = "";

	ArrayList node_ids = new ArrayList();
	ArrayList equipment_ids = new ArrayList();
	ArrayList nodelink_ids = new ArrayList();
	ArrayList link_ids = new ArrayList();
	ArrayList path_ids = new ArrayList();
	ArrayList mark_ids = new ArrayList();

	protected ArrayList nodes = new ArrayList();//Вектор элементов наследников класса Node
	public ArrayList markers = new ArrayList();//Вектор маркеров
	protected ArrayList nodeLinks = new ArrayList();//Вектор элементов типа NodeLinks
	protected ArrayList physicalLinks = new ArrayList();//Вектор элементов типа physicalLinks
	protected ArrayList transmissionPath = new ArrayList();//Вектор элементов типа physicalLinks

	LinkedList removedElements = new LinkedList();

	LinkedList deleted_nodes_ids = new LinkedList();
	LinkedList deleted_nodeLinks_ids = new LinkedList();
	LinkedList deleted_physicalLinks_ids = new LinkedList();
	LinkedList deleted_transmissionPath_ids = new LinkedList();

	protected boolean showPhysicalNodeElement;//Флаг видимости PhysicalNodeElement
	public int linkState = SHOW_PHYSICALLINK;//Перменная показывающая что сейчас отображать

	public double defaultScale = 0.00001;
	public double currentScale = 0.00001;

	public double defaultZoomFactor = 0.00001;

//	public double zoomFactor = 1;
//	public double standartScale = 2.5E-5;
//	public Rectangle standartMapElementSize = new Rectangle(MapNodeElement.defaultBounds);
//	public Rectangle curentStandartMapElementSize = new Rectangle(MapNodeElement.defaultBounds);

	protected MapElement curMapElement = null;//Текущий элемент
	LogicalNetLayer logicalNetLayer = null;

	/**
	 * 
	 * @param logical comments
	 */
	public MapContext(LogicalNetLayer logical)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "MapContext(" + logical + ")");
		setLogicalNetLayer(logical);
		showPhysicalNodeElement = true;
		created = System.currentTimeMillis();

		transferable = new MapContext_Transferable();
	}

	/**
	 * constructor
	 */
	public MapContext()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "MapContext()");
		setLogicalNetLayer(null);
		showPhysicalNodeElement = true;
		created = System.currentTimeMillis();

		transferable = new MapContext_Transferable();
	}

	/**
	 * Используется для создания элемента при подгрузке из базы данных
	 */
	public MapContext(MapContext_Transferable transferable)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "MapContext(" + transferable + ")");
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * Клонирование объекта - оспользуется при сохранении контекста карты
	 * под новым именем
	 */
	public Object clone(DataSourceInterface dataSource)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "clone(" + dataSource + ")");
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapContext.typ, cloned_id);

		MapContext mc = new MapContext();

		mc.codename = codename;
		mc.created_by = mc.user_id;
		mc.currentScale = currentScale;
		mc.description = description;
		mc.domain_id = domain_id;
		mc.id = dataSource.GetUId(MapContext.typ);
		mc.latitude = latitude;
		mc.linkState = linkState;
		mc.longitude = longitude;
		mc.modified = mc.created;
		mc.modified_by = mc.user_id;
		mc.name = name + "(copy)";
		mc.scheme_id = (String )Pool.get("schemeclonedids", scheme_id);
		if(mc.scheme_id == null)
			mc.scheme_id = "";
		mc.showPhysicalNodeElement = showPhysicalNodeElement;
		mc.user_id = dataSource.getSession().getUserId();

		Pool.put(MapContext.typ, mc.getId(), mc);
		Pool.put("mapclonedids", id, mc.getId());

		mc.markers = new ArrayList();
//		mc.markers = new Vector(markers.size());
//		for (int i = 0; i < markers.size(); i++)
//			mc.markers.add(((MapElement)markers.get(i)).clone(dataSource));
		mc.nodeLinks = new ArrayList(nodeLinks.size());
		for (int i = 0; i < nodeLinks.size(); i++)
			mc.nodeLinks.add(((MapElement)nodeLinks.get(i)).clone(dataSource));
		mc.nodes = new ArrayList(nodes.size());
		for (int i = 0; i < nodes.size(); i++)
			mc.nodes.add(((MapElement)nodes.get(i)).clone(dataSource));
		mc.physicalLinks = new ArrayList(physicalLinks.size());
		for (int i = 0; i < physicalLinks.size(); i++)
			mc.physicalLinks.add(((MapElement)physicalLinks.get(i)).clone(dataSource));
		mc.transmissionPath = new ArrayList(transmissionPath.size());
		for (int i = 0; i < transmissionPath.size(); i++)
			mc.transmissionPath.add(((MapElement)transmissionPath.get(i)).clone(dataSource));
		
		mc.mark_ids = new ArrayList(mark_ids.size());
		for (int i = 0; i < mark_ids.size(); i++)
			mc.mark_ids.add(Pool.get("mapclonedids", (String )mark_ids.get(i)));
		mc.nodelink_ids = new ArrayList(nodelink_ids.size());
		for (int i = 0; i < nodelink_ids.size(); i++)
			mc.nodelink_ids.add(Pool.get("mapclonedids", (String )nodelink_ids.get(i)));
		mc.node_ids = new ArrayList(node_ids.size());
		for (int i = 0; i < node_ids.size(); i++)
			mc.node_ids.add(Pool.get("mapclonedids", (String )node_ids.get(i)));
		mc.equipment_ids = new ArrayList(equipment_ids.size());
		for (int i = 0; i < equipment_ids.size(); i++)
			mc.equipment_ids.add(Pool.get("mapclonedids", (String )equipment_ids.get(i)));
		mc.link_ids = new ArrayList(link_ids.size());
		for (int i = 0; i < link_ids.size(); i++)
			mc.link_ids.add(Pool.get("mapclonedids", (String )link_ids.get(i)));
		mc.path_ids = new ArrayList(path_ids.size());
		for (int i = 0; i < path_ids.size(); i++)
			mc.path_ids.add(Pool.get("mapclonedids", (String )path_ids.get(i)));

		String ni = curMapElement.getId();
		if(ni != null)
			ni = (String )Pool.get("mapclonedids", curMapElement.getId());
		MapElement me = null;
		if(ni != null)
			me = (MapElement )Pool.get(((ObjectResource )curMapElement).getTyp(), ni);
		if(me != null)
			mc.curMapElement = me;

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
		codename = transferable.codename;
		user_id = transferable.user_id;
		domain_id = transferable.domain_id;
		created = transferable.created;
		modified = transferable.modified;
		modified_by = transferable.modified_by;
		created_by = transferable.created_by;
		longitude = Double.parseDouble( transferable.longitude);
		latitude = Double.parseDouble( transferable.latitude);

		scheme_id = transferable.scheme_id;

		description = transferable.description;
		showPhysicalNodeElement = transferable.showPhysicalNodeElement;
//		zoomFactor = transferable.zoomFactor;
//		defaultZoomFactor = transferable.defaultZoomFactor;
//                mouseTolerancy = transferable.mouseTolerancy;
		currentScale = transferable.zoomFactor;
		defaultScale = transferable.defaultZoomFactor;

		count = transferable.node_ids.length;
		node_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			node_ids.add(transferable.node_ids[i]);

		count = transferable.equipment_ids.length;
		equipment_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			equipment_ids.add(transferable.equipment_ids[i]);

		count = transferable.nodeLink_ids.length;
		nodelink_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			nodelink_ids.add(transferable.nodeLink_ids[i]);

		count = transferable.physicalLink_ids.length;
		link_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			link_ids.add(transferable.physicalLink_ids[i]);

		count = transferable.path_ids.length;
		path_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			path_ids.add(transferable.path_ids[i]);

		count = transferable.mark_ids.length;
		mark_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			mark_ids.add(transferable.mark_ids[i]);
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
		transferable.codename = codename;
		transferable.user_id = user_id;
		transferable.domain_id = domain_id;
		transferable.description = description;
		transferable.showPhysicalNodeElement = showPhysicalNodeElement;
		transferable.zoomFactor = currentScale;
		transferable.defaultZoomFactor = defaultScale;
		transferable.modified = System.currentTimeMillis();
		transferable.modified_by = user_id;
		transferable.created_by = user_id;

		transferable.scheme_id = scheme_id;

		transferable.longitude = String.valueOf(longitude);
		transferable.latitude = String.valueOf(latitude);

		node_ids = new ArrayList();
		equipment_ids = new ArrayList();
		mark_ids = new ArrayList();

		count = nodes.size();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)nodes.get(i);
			if(os.getTyp().equals("mapnodeelement"))
				node_ids.add(os.getId());
			if(os.getTyp().equals("mapequipmentelement"))
				equipment_ids.add(os.getId());
			if(os.getTyp().equals("mapmarkelement"))
				mark_ids.add(os.getId());
		}
		transferable.node_ids = (String [])node_ids.toArray(new String[node_ids.size()]);
		transferable.equipment_ids = (String [])equipment_ids.toArray(new String[equipment_ids.size()]);
		transferable.kis_ids = new String[0];
		transferable.mark_ids = (String [])mark_ids.toArray(new String[mark_ids.size()]);

		nodelink_ids = new ArrayList();
		
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			nodelink_ids.add(os.getId());
		}
		transferable.nodeLink_ids = (String [])nodelink_ids.toArray(new String[nodelink_ids.size()]);

		link_ids = new ArrayList();
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			link_ids.add(os.getId());
		}
		transferable.physicalLink_ids = (String [])link_ids.toArray(new String[link_ids.size()]);

		path_ids = new ArrayList();
		for(Iterator it = transmissionPath.iterator(); it.hasNext();)
		{
			os = (ObjectResource)it.next();
			path_ids.add(os.getId());
		}
		transferable.path_ids = (String [])path_ids.toArray(new String[path_ids.size()]);
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

	/**
	 * геттер
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * геттер
	 */
	public String getDomainId()
	{
		return domain_id;
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
		
		int l;
		int i;

		l = node_ids.size();
		nodes = new ArrayList();
		for(i = 0; i < l; i++)
			nodes.add(Pool.get("mapnodeelement", (String)node_ids.get(i)));

		l = equipment_ids.size();
		for(i = 0; i < l; i++)
			nodes.add(Pool.get("mapequipmentelement", (String)equipment_ids.get(i)));

		l = nodelink_ids.size();
		nodeLinks = new ArrayList();
		for(i = 0; i < l; i++)
			nodeLinks.add(Pool.get("mapnodelinkelement", (String)nodelink_ids.get(i)));

		l = link_ids.size();
		physicalLinks = new ArrayList();
		for(i = 0; i < l; i++)
			physicalLinks.add(Pool.get("maplinkelement", (String)link_ids.get(i)));

		l = path_ids.size();
		transmissionPath = new ArrayList();
		for(i = 0; i < l; i++)
			transmissionPath.add(Pool.get("mappathelement", (String)path_ids.get(i)));

		l = mark_ids.size();
		for(i = 0; i < l; i++)
			nodes.add(Pool.get("mapmarkelement", (String)mark_ids.get(i)));
	}

	/**
	 * обновление локального содержимого объектов, содержащихся в контексте
	 */
	public void updateFromPool()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateFromPool()");
		
		int i;
		
		for(i = 0; i < nodes.size(); i++)
		{
			ObjectResource os = (ObjectResource )nodes.get(i);
			os.updateLocalFromTransferable();
		}
		for(i = 0; i < nodeLinks.size(); i++)
		{
			ObjectResource os = (ObjectResource)nodeLinks.get(i);
			os.updateLocalFromTransferable();
		}
		for(i = 0; i < physicalLinks.size(); i++)
		{
			ObjectResource os = (ObjectResource )physicalLinks.get(i);
			os.updateLocalFromTransferable();
		}
		for(i = 0; i < transmissionPath.size(); i++)
		{
			ObjectResource os = (ObjectResource )transmissionPath.get(i);
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
		return new MapContextModel(this);
	}

	/**
	 * получить панель свойств
	 */
	public static PropertiesPanel getPropertyPane()
	{
		return new MapContextPane();
	}

	/**
	 * получить модель отображения в таблице
	 */
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapContextDisplayModel();
	}

	/**
	 * флаг того, что контекст открыт в окне карты
	 */
	public boolean isOpened()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "isOpened()");
		
		return (logicalNetLayer != null);
	}

	/**
	 * установить ссылку на класс логической схемы, что соответствует открытию 
	 * контекста в окне карты
	 */
	public void setLogicalNetLayer(LogicalNetLayer logical)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setLogicalNetLayer(" + logical + ")");
		
		logicalNetLayer = logical;

		//Поумолчанию текущий элемент Void
		curMapElement = new VoidMapElement(getLogicalNetLayer());

		if(logicalNetLayer != null)
		{
			longitude = logicalNetLayer.viewer.getCenter()[0];
			latitude = logicalNetLayer.viewer.getCenter()[1];
//			zoomFactor = logicalNetLayer.viewer.getScale();

			defaultScale = logicalNetLayer.viewer.getScale();
			currentScale = logicalNetLayer.viewer.getScale();
		}

	}
	
	/**
	 * Установить флаг видимости PhysicalNodeElement
	 */
	public void setPhysicalNodeElementVisibility(boolean visibility)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setPhysicalNodeElementVisibility(" + visibility + ")");
		
		showPhysicalNodeElement = visibility;
	}

	/**
	 * Получение флага видимости PhysicalNodeElement
	 */
	public boolean isPhysicalNodeElementVisible()
	{
		return showPhysicalNodeElement;
	}

	/**
	 * Получение элементов - наследников класса Node
	 */
	public ArrayList getNodes()
	{
		return nodes;
	}

	/**
	 * Установить список элементов наследников класса Node
	 */
	public void setNodes (ArrayList myNodes)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setNodes(" + myNodes + ")");
		nodes = myNodes;
	}

	/**
	 * Добавить новый MapNodeElement
	 */
	public void addNode(MapNodeElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addNode(" + ob + ")");
		
		nodes.add(ob);
	}

	/**
	 * Удалить MapNodeElement
	 */
	public void removeNode(MapNodeElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeNode(" + ob + ")");
		
		nodes.remove(ob);
		removedElements.add(ob);
	}

	/**
	 * Получение список элементов типа NodeLinks
	 */
	public ArrayList getNodeLinks()
	{
		return nodeLinks;
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
			MapNodeLinkElement nodeLink = (MapNodeLinkElement)e.next();
			if ( nodeLink.getId().equals( mapNodeLinkElementID) )
			{
				return nodeLink;
			}
		}
		return null;
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
			MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement)e.next();
			if ( physicalLink.getId().equals( mapPhysicalLinkElementID) )
			{
				return physicalLink;
			}
		}
		return null;
	}

	/**
	 * Установить список элементов типа NodeLinks
	 */
	public void setNodeLinks (ArrayList myNodeLinks)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setNodeLinks(" + myNodeLinks + ")");
		
		nodeLinks = myNodeLinks;
	}

	/**
	 * добавить новый MapNodeLinkElement
	 */
	public void addNodeLink(MapNodeLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addNodeLink(" + ob + ")");
		
		nodeLinks.add( ob);
	}

	/**
	 * Удалить MapNodeLinkElement
	 */
	public void removeNodeLink(MapNodeLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeNodeLink(" + ob + ")");
		
		nodeLinks.remove(ob);
		removedElements.add(ob);
	}

	/**
	 * Проверка того, что хотя бы один элемент выбран
	 */
	public boolean isSelectionEmpty()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "isSelectionEmpty()");
		
		Iterator e = this.getAllElements().iterator();

		while (e.hasNext())
		{
			MapElement curElement = (MapElement )e.next();
			if (curElement.isSelected())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Установить текущий элемент по координате на карте
	 */
	public void setCurrentMapElement (Point myPoint)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentMapElement(" + myPoint + ")");
		
		//Здесь пробегаемся по всем элементам и если на каком-нибудь из них курсор
		//то устанавливаем его текущим элементом
		for (int i = 0;i < markers.size(); i++)
		{
			MapMarker myMarer = (MapMarker )markers.get(i);
			if(myMarer.isMouseOnThisObject(myPoint))
			{
				curMapElement = myMarer;
				return;
			}
		}

		Iterator e = getAllElements().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			if (mapElement.isMouseOnThisObject(myPoint))
			{
				if ( mapElement instanceof MapNodeElement)
				{
					notifySchemeEvent(mapElement);
					notifyCatalogueEvent(mapElement);
				}
				if ( mapElement instanceof MapNodeLinkElement)
				{
					//Здесь смотрим по флагу linkState что делать
					if ( linkState == MapContext.SHOW_NODE_LINK)
					{
						curMapElement = mapElement;
						notifySchemeEvent(curMapElement);
						notifyCatalogueEvent(curMapElement);
						return;
					}

					if ( linkState == MapContext.SHOW_PHYSICALLINK)
					{
						curMapElement = getPhysicalLinkbyNodeLink(mapElement.getId());
						notifySchemeEvent(curMapElement);
						notifyCatalogueEvent(curMapElement);
						return;
					}

					if ( linkState == MapContext.SHOW_TRANSMISSION_PATH)
					{
						Dispatcher dispatcher = logicalNetLayer.mapMainFrame.aContext.getDispatcher();

						Iterator e1 = getTransmissionPathByNodeLink(mapElement.getId()).iterator();
						while( e1.hasNext())
						{
							MapTransmissionPathElement path = (MapTransmissionPathElement )e1.next();
							path.select();

							this.logicalNetLayer.perform_processing = false;
							MapNavigateEvent mne = new MapNavigateEvent(
									this, 
									MapNavigateEvent.MAP_PATH_SELECTED_EVENT);
							mne.mappathID = path.getId();
							dispatcher.notify(mne);
							this.logicalNetLayer.perform_processing = true;

							notifySchemeEvent(path);
							notifyCatalogueEvent(path);

							curMapElement = path;
							return;
						}

					}
				}
				curMapElement = mapElement;
				return;
			}
		}

		curMapElement = new VoidMapElement(this.getLogicalNetLayer());
	}

	/**
	 * Получить MapNodeLinkElement, для которого булет произволиться
	 * редактирование длины, по коордитате на карте
	 */
	public MapNodeLinkElement getEditedNodeLink(Point myPoint)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getEditedNodeLink(" + myPoint + ")");
		
		Iterator e = this.nodeLinks.iterator();
		while (e.hasNext())
		{
			MapNodeLinkElement mapElement = (MapNodeLinkElement )e.next();
			if (mapElement.isMouseOnThisObjectsLabel(myPoint))
			{
				return mapElement;
			}
		}
		return null;
	}

	/**
	 * Генерация сообщеия о выборке элемента карты
	 */
	public void notifySchemeEvent(MapElement mapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "notifySchemeEvent(" + mapElement + ")");
		
		SchemeNavigateEvent sne;
		Dispatcher dispatcher = logicalNetLayer.mapMainFrame.aContext.getDispatcher();
		try 
		{
			MapEquipmentNodeElement mapel = (MapEquipmentNodeElement )mapElement;

			if(mapel.element_id != null && !mapel.element_id.equals(""))
			{
				SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mapel.element_id);
//				System.out.println("notify SCHEME_ELEMENT_SELECTED_EVENT " + se.getId());
				this.logicalNetLayer.perform_processing = false;
				sne = new SchemeNavigateEvent(
						new SchemeElement[] { se },
						SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.perform_processing = true;
				return;
			}
		} 
		catch (Exception ex){ }

		try
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )mapElement;

			if(link.LINK_ID != null && !link.LINK_ID.equals(""))
			{
				SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
//				System.out.println("notify SCHEME_CABLE_LINK_SELECTED_EVENT " + scl.getId());
				this.logicalNetLayer.perform_processing = false;
				sne = new SchemeNavigateEvent(
						new SchemeCableLink[] { scl },
						SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.perform_processing = true;
				return;
			}
		} 
		catch (Exception ex){ }

		try 
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )mapElement;

			if(path.PATH_ID != null && !path.PATH_ID.equals(""))
			{
				SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
//				System.out.println("notify SCHEME_PATH_SELECTED_EVENT " + sp.getId());
				this.logicalNetLayer.perform_processing = false;
				sne = new SchemeNavigateEvent(
						new SchemePath[] { sp },
						SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.perform_processing = true;
				return;
			}
		} 
		catch (Exception ex){ } 
	}

	/**
	 * Генерация сообщения о выборке элемента каталога
	 */
	public void notifyCatalogueEvent(MapElement mapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "notifyCatalogueEvent(" + mapElement + ")");
		
		CatalogNavigateEvent cne;
		Dispatcher dispatcher = logicalNetLayer.mapMainFrame.aContext.getDispatcher();
		try 
		{
			MapEquipmentNodeElement mapel = (MapEquipmentNodeElement )mapElement;

			if(mapel.element_id != null && !mapel.element_id.equals(""))
			{
				SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mapel.element_id);
				
				if(se.equipment_id != null && !se.equipment_id.equals(""))
				{
					Equipment eq = (Equipment )Pool.get(Equipment.typ, se.equipment_id);
//					System.out.println("notify CATALOG_EQUIPMENT_SELECTED_EVENT " + eq.getId());
					cne = new CatalogNavigateEvent(
						new Equipment[] { eq },
						CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT);
					dispatcher.notify(cne);
					return;
				}
			}
		} 
		catch (Exception ex){ }

		try
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )mapElement;

			if(link.LINK_ID != null && !link.LINK_ID.equals(""))
			{
				SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
				
				if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
				{
					CableLink cl = (CableLink )Pool.get(CableLink.typ, scl.cable_link_id);
//					System.out.println("notify CATALOG_EQUIPMENT_SELECTED_EVENT " + cl.getId());
					cne = new CatalogNavigateEvent(
						new CableLink[] { cl },
						CatalogNavigateEvent.CATALOG_CABLE_LINK_SELECTED_EVENT);
					dispatcher.notify(cne);
					return;
				}
			}
		} 
		catch (Exception ex){ }

		try 
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )mapElement;

			if(path.PATH_ID != null && !path.PATH_ID.equals(""))
			{
				SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
				if(sp.path_id != null && !sp.path_id.equals(""))
				{
					TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
//					System.out.println("notify CATALOG_PATH_SELECTED_EVENT " + tp.getId());
					cne = new CatalogNavigateEvent(
						new TransmissionPath[] { tp }, 
						CatalogNavigateEvent.CATALOG_PATH_SELECTED_EVENT);
					dispatcher.notify(cne);
				}
			}
		} 
		catch (Exception ex){ } 
	}

	/**
	 * Получить текущий выбранный элемент
	 */
	public MapElement getCurrentMapElement()
	{
		return curMapElement;
	}

	/**
	 * Установить текущий выбранный элемент
	 */
	public void setCurrent(MapElement curMapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrent(" + curMapElement + ")");
		
		this.curMapElement = curMapElement;
	}

	/**
	 * Получить текущий элемент по коордитате на карте
	 */
	public MapElement getCurrentMapElement(Point myPoint)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getCurrentMapElement(" + myPoint + ")");
		
		MapElement curME = new VoidMapElement(this.getLogicalNetLayer());
		Iterator e = getAllElements().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			if ( mapElement.isMouseOnThisObject(myPoint))
			{
				if ( mapElement instanceof MapNodeLinkElement)
				{
					//Здесь смотрим по флагу linkState что делать
					if ( linkState == MapContext.SHOW_NODE_LINK)
					{
						curME = mapElement;
						break;
					}

					if ( linkState == MapContext.SHOW_PHYSICALLINK)
					{
						curME = getPhysicalLinkbyNodeLink( mapElement.getId());
						break;
					}

					if ( linkState == MapContext.SHOW_TRANSMISSION_PATH)
					{
						Iterator e1 = getTransmissionPathByNodeLink(mapElement.getId()).iterator();
						if( e1.hasNext())
						{
							MapTransmissionPathElement path = (MapTransmissionPathElement )e1.next();

							curME = path;
							break;
						}

					}
				}
				curME = mapElement;
				break;
			}
		}
		return curME;
	}

	/**
	 * Получить список NodeLinks, содержащих заданный Node
	 */
	public LinkedList getNodeLinksContainingNode(MapNodeElement myNode)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLinksContainingNode(" + myNode + ")");
		
		LinkedList returnNodeLink = new LinkedList();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement myNodeLink = (MapNodeLinkElement )it.next();
			
			//Если один из концов является данным myNode то добавляем его в вектор
			if ( (myNodeLink.endNode == myNode) || (myNodeLink.startNode == myNode))
			{
				returnNodeLink.add(myNodeLink);
			}
		}

		return returnNodeLink;
	}

//
	/**
	 * Получить список PhysicalLink, содержащих заданный Node
	 */
	public LinkedList getPhysicalLinksContainingNode(MapNodeElement myNode)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLinksContainingNode(" + myNode + ")");
		
		LinkedList returnPhysicalLink = new LinkedList();

		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement myPhysicalLink = (MapPhysicalLinkElement )it.next();
			
			//Если один из концов является данным myNode то добавляем его в вектор
			if ( (myPhysicalLink.endNode == myNode) || (myPhysicalLink.startNode == myNode) )
			{
				returnPhysicalLink.add( myPhysicalLink);
			}
		}

		return returnPhysicalLink;
	}

	/**
	 * Получить список NodeLinks, содержащихся в PhysicalLink по physicalLinkID
	 */
	public LinkedList getNodeLinksInPhysicalLink(String physicalLinkID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLinksInPhysicalLink(" + physicalLinkID + ")");
		
		LinkedList returnNodeLink = new LinkedList();

		Iterator e = physicalLinks.iterator();
		while (e.hasNext())
		{
			MapPhysicalLinkElement myPhysicalLink = (MapPhysicalLinkElement)e.next();

			if ( myPhysicalLink.getId().equals(physicalLinkID) )
			{
				//Получаем PhysicalLink с заданным ID
				Enumeration ee = myPhysicalLink.nodeLink_ids.elements();
				while ( ee.hasMoreElements() )
				{
					String id = (String)ee.nextElement();
					returnNodeLink.add(  this.getNodeLink( id));
				}

				return returnNodeLink;
			}

		}
		return returnNodeLink;
	}

	/**
	 * Получить список NodeLinks, содержащиеся в TransmissionPath, по pathID
	 */
	public LinkedList getNodeLinksInTransmissionPath(String pathID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLinksInTransmissionPath(" + pathID + ")");
		
		LinkedList returnNodeLink = new LinkedList();
		MapTransmissionPathElement transmissionPath = getMapTransmissionPathElement(pathID);

		Iterator e = transmissionPath.physicalLink_ids.iterator();
		while( e.hasNext())
		{
			String physicalLinkID = (String )e.next();
			returnNodeLink.addAll(getNodeLinksInPhysicalLink(physicalLinkID));
		}
		return returnNodeLink;
	}

	/**
	 * Получить вектор Node противоположных у элемета NodeLink по заданному Node
	 */
	public LinkedList getOtherNodeOfNodeLinksContainingNode(MapNodeElement myNode)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getOtherNodeOfNodeLinksContainingNode(" + myNode + ")");
		
		Iterator e = getNodeLinksContainingNode(myNode).iterator();
		LinkedList returnNodeLink = new LinkedList();

		while (e.hasNext())
		{
			MapNodeLinkElement myNodeLink = (MapNodeLinkElement )e.next();

			if ( myNodeLink.endNode == myNode )
			{
				returnNodeLink.add(myNodeLink.startNode);
			}
			else
			{
				returnNodeLink.add(myNodeLink.endNode);
			}
		}

		return returnNodeLink;
	}

	/**
	 * Получить другой конец NodeLink по заданному NodeElement
	 */
	public MapNodeElement getOtherNodeOfNodeLink(MapNodeLinkElement myNodeLink, MapNodeElement myNode)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getOtherNodeOfNodeLink(" + myNodeLink + ", " + myNode + ")");
		

		if ( myNodeLink.endNode == myNode )
		{
			return myNodeLink.startNode;
		}
		if ( myNodeLink.startNode == myNode )
		{
			return myNodeLink.endNode;
		}
		return null;
	}

	/**
	 * Получить другой конец PhysicalLink по заданному NodeElement
	 */
	public MapNodeElement getOtherNodeOfPhysicalLink(MapPhysicalLinkElement myPhysicalLink, MapNodeElement myNode)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getOtherNodeOfPhysicalLink(" + myPhysicalLink + ", " + myNode + ")");
		

		if ( myPhysicalLink.endNode == myNode )
		{
			return myPhysicalLink.startNode;
		}
		if ( myPhysicalLink.startNode == myNode )
		{
			return myPhysicalLink.endNode;
		}
		return null;
	}

	/**
	 * Отменить выбор всем элементам
	 */
	public void deselectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");
		
		Iterator e = getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			mapElement.deselect();
		}
	}

//
	/**
	 * Выбрать все элеметны
	 */
	public void selectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "selectAll()");
		
		Iterator e = this.getAllElements().iterator();

		while(e.hasNext())
		{
			MapElement curElement = (MapElement)e.next();
			curElement.select();
		}
	}

	/**
	 * Установпить список физических линий
	 */
	public void setPhysicalLinks(ArrayList vec)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setPhysicalLinks(" + vec + ")");
		
		physicalLinks = vec;
	}

	/**
	 * Получить список физических линий
	 */
	public ArrayList getPhysicalLinks()
	{
		return physicalLinks;
	}

	/**
	 * Добавить новую физическую линию
	 */
	public void addPhysicalLink(MapPhysicalLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addPhysicalLink(" + ob + ")");
		
		physicalLinks.add(ob);
	}

	/**
	 * Удалить физическую линию
	 */
	public void removePhysicalLink(MapPhysicalLinkElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removePhysicalLink(" + ob + ")");
		
		physicalLinks.remove(ob);
		removedElements.add(ob);
	}

	/**
	 * Установить 
	 */
	public void setTransmissionPath(ArrayList vec)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setTransmissionPath(" + vec + ")");
		
		transmissionPath = vec;
	}

	/**
	 * Получить список путей тестирования
	 */
	public ArrayList getTransmissionPath()
	{
		return transmissionPath;
	}

	/**
	 * Добавить новый путь тестирования
	 */
	public void addTransmissionPath(MapTransmissionPathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addTransmissionPath(" + ob + ")");
		
		transmissionPath.add(ob);
	}

	/**
	 * Удалить путь тестирования
	 */
	public void removeTransmissionPath(MapTransmissionPathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeTransmissionPath(" + ob + ")");
		
		transmissionPath.remove(ob);
		removedElements.add(ob);
	}

	/**
	 * Получить MapPhysicalLinkElement по NodeLink который он содержит
	 */
	public MapPhysicalLinkElement getPhysicalLinkbyNodeLink(String nodeLinkElementID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLinkbyNodeLink(" + nodeLinkElementID + ")");
		
		Iterator e = getPhysicalLinks().iterator();
		while (e.hasNext())
		{
			MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement )e.next();
			if ( physicalLink.nodeLink_ids.contains(nodeLinkElementID) )
			{
				return physicalLink;
			}
		}
		return null;
	}

	/**
	 * Получить вектор TransmissionPath элементов, которые содержат
	 * MapPhysicalLinkElement, по physicalLinkID
	 */
	public LinkedList getTransmissionPathByPhysicalLink(String physicalLinkID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getTransmissionPathByPhysicalLink(" + physicalLinkID + ")");
		
		LinkedList returnVector = new LinkedList();;
		Iterator e = getTransmissionPath().iterator();
		while (e.hasNext())
		{
			MapTransmissionPathElement transmissionPath = (MapTransmissionPathElement )e.next();
			if ( transmissionPath.physicalLink_ids.contains(physicalLinkID) )
			{
				returnVector.add(transmissionPath);
			}
		}
		return returnVector;
	}

	/**
	 * Получить список TransmissionPath элементов, которые содержат 
	 * MapNodeLinkElement, по nodeLinkID
	 */
	public LinkedList getTransmissionPathByNodeLink(String nodeLinkID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getTransmissionPathByNodeLink(" + nodeLinkID + ")");
		
		return getTransmissionPathByPhysicalLink(
				getPhysicalLinkbyNodeLink(nodeLinkID).getId());
	}

	/**
	 * Установить значения долготы, широты
	 */
	public void setLongLat( double longit, double latit)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setLongLat(" + longit + ", " + latit + ")");
		
		longitude = longit;
		latitude = latit;
	}

	/**
	 * Удалить путь тестирования, содержащий физическую линию
	 */
	public void deleteTranmissionPath( String physicalLinkID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deleteTranmissionPath(" + physicalLinkID + ")");
		
		Iterator e = getTransmissionPath().iterator();
		while (e.hasNext())
		{
			MapTransmissionPathElement transmissionPath = 
					(MapTransmissionPathElement )e.next();
			if (transmissionPath.physicalLink_ids.contains(physicalLinkID))
			{
				this.removeTransmissionPath(transmissionPath);
			}
		}

	}

	/**
	 * Получить ссылку на слой логической сети
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	/**
	 * используется для востанивления класса из базы данных
	 */
	public void createFromPool(LogicalNetLayer logical)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "createFromPool(" + logical + ")");
		
		logicalNetLayer = logical;
		curMapElement = new VoidMapElement(getLogicalNetLayer());
		showPhysicalNodeElement = true;

	    logicalNetLayer.getMapViewer().setScale( currentScale);
		zoom(currentScale);

		logicalNetLayer.viewer.setCenter(longitude, latitude);
	}

	/**
	 * Получить список топологических узлов
	 */
	public LinkedList getMapPhysicalNodeElements()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapPhysicalNodeElements()");
		
		LinkedList returnVector = new LinkedList();

		Iterator e = nodes.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			if ( mapElement instanceof MapPhysicalNodeElement)
			{
				returnVector.add( mapElement);
			}
		}
		return returnVector;
	}

	/**
	 * Получить пкуть тестирования по ID
	 */
	public MapTransmissionPathElement getMapTransmissionPathElement(String myID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapTransmissionPathElement(" + myID + ")");
		
		Iterator e = getTransmissionPath().iterator();
		while (e.hasNext())
		{
			MapTransmissionPathElement myMapTransmissionPathElement = 
				(MapTransmissionPathElement )e.next();

			if ( myMapTransmissionPathElement.getId().equals(myID))
			{
				return myMapTransmissionPathElement;
			}
		}
		return null;
	}

	/**
	 * Получить список узлов
	 */
	public LinkedList getMapEquipmentNodeElements()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapEquipmentNodeElements()");
		
		LinkedList returnVector = new LinkedList();

		Iterator e = nodes.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			if ( mapElement instanceof MapEquipmentNodeElement)
			{
				returnVector.add( mapElement);
			}
		}
		return returnVector;
	}

	/**
	 * Получить список меток
	 */
	public LinkedList getMapMarkElements()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapMarkElements()");
		
		LinkedList returnVector = new LinkedList();

		Iterator e = nodes.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			if ( mapElement instanceof MapMarkElement)
			{
				returnVector.add( mapElement);
			}
		}
		return returnVector;
	}

	/**
	 * Получить элемент узла по ID
	 */
	public MapEquipmentNodeElement getMapEquipmentNodeElement(String myID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapEquipmentNodeElement(" + myID + ")");
		
		Iterator e = getMapEquipmentNodeElements().iterator();
		while (e.hasNext())
		{
			MapEquipmentNodeElement myMapEquipmentNodeElement = 
				(MapEquipmentNodeElement )e.next();

			if ( myMapEquipmentNodeElement.getId().equals(myID))
			{
				return myMapEquipmentNodeElement;
			}
		}
		return null;
	}

	/**
	 * Получить список всех олементов контекста карты
	 */
	public LinkedList getAllElements()
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

		e = transmissionPath.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}

		e = markers.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}
		return returnVector;
	}

	/**
	 * Получить маркер по ID
	 */
	public MapMarker getMarker(String markerID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMarker(" + markerID + ")");
		
		Iterator e = markers.iterator();
		while( e.hasNext())
		{
			MapMarker marker = (MapMarker )e.next();
			if ( marker.getId().equals(markerID))
				return marker;
		}
		return null;
	}

	/**
	 * Удалить все маркеры
	 */
	public void removeMarkers()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeMarkers()");
		
		markers.clear();
	}

	/**
	 * Получить список удаленных элементов
	 */
	public LinkedList getRemovedElements()
	{
		return removedElements;
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public void zoomIn()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "zoomIn()");
		
		getLogicalNetLayer().viewer.zoomIn();
		currentScale = logicalNetLayer.viewer.getScale();

		updateZoom();
	}

	/**
	 * При изменении масштаба отображения карты необходимо обновить
	 * масштаб отображения всех объектов на карте
	 */
	public void updateZoom()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateZoom()");
		
		double sF = defaultScale / currentScale;

		Iterator en =  getNodes().iterator();
		while (en.hasNext())
		{
			MapNodeElement curNode = (MapNodeElement )en.next();
			curNode.setScaleCoefficient(sF);
			curNode.setImageID(curNode.getImageID());
		}
	}

	/**
	 * Установить заданный масштаб вида карты
	 */
	public void zoom(double scale)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "zoom(" + scale + ")");
		
		currentScale = scale;

		updateZoom();
	}

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public void zoomOut()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "zoomOut()");
		
		getLogicalNetLayer().viewer.zoomOut();
		currentScale = logicalNetLayer.viewer.getScale();

		updateZoom();
	}

	/**
	 * Полцчить список физических линий в составе пути тестирования начиная 
	 * с начала
	 */
	public LinkedList getPhysicalLinksInTransmissiionPath(String pathID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLinksInTransmissiionPath(" + pathID + ")");
		
		LinkedList returnLinkVector = new LinkedList();
		MapTransmissionPathElement transmissionPath = getMapTransmissionPathElement(pathID);
		Iterator e = transmissionPath.physicalLink_ids.iterator();
		while(e.hasNext())
		{
			String physicalLinkID = (String )e.next();
			MapPhysicalLinkElement bufferPhysLink = 
				(MapPhysicalLinkElement )getPhysicalLink(physicalLinkID);

			returnLinkVector.add(bufferPhysLink);
		}
		return returnLinkVector;
	}

	/**
	 * Возвращает упорядоченный набор Equipment в TransmissionPath начиная 
	 * с начала
	 */
	public LinkedList getEquipmentElemenetsInTansPath(String pathID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getEquipmentElemenetsInTansPath(" + pathID + ")");
		
		LinkedList returnNodeVector = new LinkedList();

		MapTransmissionPathElement transmissionPath = getMapTransmissionPathElement(pathID);
		MapNodeElement bufferNode = transmissionPath.startNode;
		returnNodeVector.add(bufferNode);

		Iterator e = transmissionPath.physicalLink_ids.iterator();
		while( e.hasNext())
		{
			String physicalLinkID = (String )e.next();
			MapPhysicalLinkElement bufferPhysLink = 
				(MapPhysicalLinkElement )getPhysicalLink(physicalLinkID);
			bufferNode = bufferPhysLink.endNode;

			returnNodeVector.add(bufferNode);
		}
		return returnNodeVector;
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
	 * Получить MapPhysicalLinkElement по начальному и конечному узлам
	 */
	public MapPhysicalLinkElement getPhysicalLink(MapNodeElement start_node, MapNodeElement end_node)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLink(" + start_node + ", " + end_node + ")");
		
		for(Iterator it = getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement)it.next();
			if (((link.startNode == start_node) && (link.endNode == end_node)) ||
				((link.startNode == end_node) && (link.endNode == start_node)) )
			{
				return link;
			}
		}
		return null;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "writeObject(out)");
		
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(codename);
		out.writeObject(user_id);
		out.writeObject(domain_id);
		out.writeDouble(longitude);
		out.writeDouble(latitude);
		out.writeLong(created);
		out.writeObject(created_by);
		out.writeLong(modified);
		out.writeObject(modified_by);
		out.writeObject(scheme_id);
		out.writeObject(nodes);
		out.writeObject(nodeLinks);
		out.writeObject(physicalLinks);
		out.writeObject(transmissionPath);
		out.writeObject(markers);
		out.writeBoolean(showPhysicalNodeElement);
		out.writeInt(linkState);
		out.writeDouble(currentScale);
		out.writeDouble(defaultScale);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "readObject(in)");
		
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		codename = (String )in.readObject();
		user_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		longitude = in.readDouble();
		latitude = in.readDouble();
		created = in.readLong();
		created_by = (String )in.readObject();
		modified = in.readLong();
		modified_by = (String )in.readObject();
		scheme_id = (String )in.readObject();
		nodes = (ArrayList )in.readObject();
		nodeLinks = (ArrayList )in.readObject();
		physicalLinks = (ArrayList )in.readObject();
		transmissionPath = (ArrayList )in.readObject();
		markers = (ArrayList )in.readObject();
		showPhysicalNodeElement = in.readBoolean();
		linkState = in.readInt();
		currentScale = in.readDouble();
		defaultScale = in.readDouble();

		transferable = new MapContext_Transferable();

		node_ids = new ArrayList();
		equipment_ids = new ArrayList();
		nodelink_ids = new ArrayList();
		link_ids = new ArrayList();
		path_ids = new ArrayList();
		mark_ids = new ArrayList();
	
		removedElements = new LinkedList();
	
		deleted_nodes_ids = new LinkedList();
		deleted_nodeLinks_ids = new LinkedList();
		deleted_physicalLinks_ids = new LinkedList();
		deleted_transmissionPath_ids = new LinkedList();

		curMapElement = new VoidMapElement();//Поумолчанию текущий элемент Void

		updateFromPool();
//		Pool.put("serverimage", getId(), this);
	}

}