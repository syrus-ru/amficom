package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.MapContext_Transferable;
import com.syrus.AMFICOM.Client.Configure.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Configure.Map.UI.Display.MapContextDisplayModel;
import com.syrus.AMFICOM.Client.Configure.Map.UI.MapContextPane;
import com.syrus.AMFICOM.Client.General.Event.CatalogNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.*;

import java.awt.Point;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTable;

//Данный класс используется для описания содержимого карты (её элементов и свойств)

public class MapContext extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
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

	Vector node_ids = new Vector();
	Vector equipment_ids = new Vector();
	Vector kis_ids = new Vector();
	Vector nodelink_ids = new Vector();
	Vector link_ids = new Vector();
	Vector path_ids = new Vector();
	Vector mark_ids = new Vector();

	protected Vector nodes = new Vector();//Вектор элементов наследников класса Node
	public Vector markers = new Vector();//Вектор маркеров
	protected Vector nodeLinks = new Vector();//Вектор элементов типа NodeLinks
	protected Vector physicalLinks = new Vector();//Вектор элементов типа physicalLinks
	protected Vector transmissionPath = new Vector();//Вектор элементов типа physicalLinks

	Vector removedElements = new Vector();

	Vector deleted_nodes_ids = new Vector();
	Vector deleted_nodeLinks_ids = new Vector();
	Vector deleted_physicalLinks_ids = new Vector();
	Vector deleted_transmissionPath_ids = new Vector();

	protected boolean showPhysicalNodeElement;//Флаг видимости PhysicalNodeElement
	public int linkState = SHOW_PHYSICALLINK;//Перменная показывающая что сейчас отображать

//A0A
	public double defaultScale = 0.00001;
	public double currentScale = 0.00001;

	public double defaultZoomFactor = 0.00001;

//	public double zoomFactor = 1;
//	public double standartScale = 2.5E-5;
//	public Rectangle standartMapElementSize = new Rectangle(MapNodeElement.defaultBounds);
//	public Rectangle curentStandartMapElementSize = new Rectangle(MapNodeElement.defaultBounds);

	protected MapElement curMapElement = null;//Текущий элемент
	LogicalNetLayer logicalNetLayer = null;

	public MapContext(LogicalNetLayer logical)
	{
		setLogicalNetLayer(logical);
		showPhysicalNodeElement = true;
		created = System.currentTimeMillis();

		transferable = new MapContext_Transferable();
	}

	public MapContext()
	{
		setLogicalNetLayer(null);
		showPhysicalNodeElement = true;
		created = System.currentTimeMillis();

		transferable = new MapContext_Transferable();
	}

//Испольуется для создания элимента при визове из базы данных
	public MapContext(MapContext_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
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

		mc.markers = new Vector();
//		mc.markers = new Vector(markers.size());
//		for (int i = 0; i < markers.size(); i++)
//			mc.markers.add(((MapElement)markers.get(i)).clone(dataSource));
		mc.nodeLinks = new Vector(nodeLinks.size());
		for (int i = 0; i < nodeLinks.size(); i++)
			mc.nodeLinks.add(((MapElement)nodeLinks.get(i)).clone(dataSource));
		mc.nodes = new Vector(nodes.size());
		for (int i = 0; i < nodes.size(); i++)
			mc.nodes.add(((MapElement)nodes.get(i)).clone(dataSource));
		mc.physicalLinks = new Vector(physicalLinks.size());
		for (int i = 0; i < physicalLinks.size(); i++)
			mc.physicalLinks.add(((MapElement)physicalLinks.get(i)).clone(dataSource));
		mc.transmissionPath = new Vector(transmissionPath.size());
		for (int i = 0; i < transmissionPath.size(); i++)
			mc.transmissionPath.add(((MapElement)transmissionPath.get(i)).clone(dataSource));
		
		mc.mark_ids = new Vector(mark_ids.size());
		for (int i = 0; i < mark_ids.size(); i++)
			mc.mark_ids.add(Pool.get("mapclonedids", (String )mark_ids.get(i)));
		mc.nodelink_ids = new Vector(nodelink_ids.size());
		for (int i = 0; i < nodelink_ids.size(); i++)
			mc.nodelink_ids.add(Pool.get("mapclonedids", (String )nodelink_ids.get(i)));
		mc.node_ids = new Vector(node_ids.size());
		for (int i = 0; i < node_ids.size(); i++)
			mc.node_ids.add(Pool.get("mapclonedids", (String )node_ids.get(i)));
		mc.equipment_ids = new Vector(equipment_ids.size());
		for (int i = 0; i < equipment_ids.size(); i++)
			mc.equipment_ids.add(Pool.get("mapclonedids", (String )equipment_ids.get(i)));
		mc.kis_ids = new Vector(kis_ids.size());
		for (int i = 0; i < kis_ids.size(); i++)
			mc.kis_ids.add(Pool.get("mapclonedids", (String )kis_ids.get(i)));
		mc.link_ids = new Vector(link_ids.size());
		for (int i = 0; i < link_ids.size(); i++)
			mc.link_ids.add(Pool.get("mapclonedids", (String )link_ids.get(i)));
		mc.path_ids = new Vector(path_ids.size());
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
	
//Устанавливаем переменные класса из базы данных
	public void setLocalFromTransferable()
	{
		int l;
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
		node_ids = new Vector(count);
		for(i = 0; i < count; i++)
		node_ids.add(transferable.node_ids[i]);

		count = transferable.equipment_ids.length;
		equipment_ids = new Vector(count);
		for(i = 0; i < count; i++)
			equipment_ids.add(transferable.equipment_ids[i]);

		count = transferable.kis_ids.length;
		kis_ids = new Vector(count);
		for(i = 0; i < count; i++)
			kis_ids.add(transferable.kis_ids[i]);

		count = transferable.nodeLink_ids.length;
		nodelink_ids = new Vector(count);
		for(i = 0; i < count; i++)
			nodelink_ids.add(transferable.nodeLink_ids[i]);

		count = transferable.physicalLink_ids.length;
		link_ids = new Vector(count);
		for(i = 0; i < count; i++)
			link_ids.add(transferable.physicalLink_ids[i]);

		count = transferable.path_ids.length;
		path_ids = new Vector(count);
		for(i = 0; i < count; i++)
			path_ids.add(transferable.path_ids[i]);

		count = transferable.mark_ids.length;
		mark_ids = new Vector(count);
		for(i = 0; i < count; i++)
			mark_ids.add(transferable.mark_ids[i]);
	}

//Передаём переменные в transferable которая используется для передачи их в базу данных
	public void setTransferableFromLocal()
	{
		int l;
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
//		transferable.zoomFactor = zoomFactor;
//		transferable.defaultZoomFactor = defaultZoomFactor;
		transferable.zoomFactor = currentScale;
		transferable.defaultZoomFactor = defaultScale;
		transferable.modified = System.currentTimeMillis();
		transferable.modified_by = user_id;
		transferable.created_by = user_id;

		transferable.scheme_id = scheme_id;

		transferable.longitude = String.valueOf(longitude);
		transferable.latitude = String.valueOf(latitude);
//		transferable.longitude = String.valueOf( logicalNetLayer.viewer.getCenter()[0]);
//		transferable.latitude = String.valueOf( logicalNetLayer.viewer.getCenter()[1]);

		node_ids = new Vector();
		kis_ids = new Vector();
		equipment_ids = new Vector();
		mark_ids = new Vector();

		count = nodes.size();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)nodes.get(i);
			if(os.getTyp().equals("mapnodeelement"))
				node_ids.add(os.getId());
			if(os.getTyp().equals("mapequipmentelement"))
				equipment_ids.add(os.getId());
			if(os.getTyp().equals("mapkiselement"))
				kis_ids.add(os.getId());
			if(os.getTyp().equals("mapmarkelement"))
				mark_ids.add(os.getId());
		}
		transferable.node_ids = new String[node_ids.size()];
		node_ids.copyInto(transferable.node_ids);
		transferable.equipment_ids = new String[equipment_ids.size()];
		equipment_ids.copyInto(transferable.equipment_ids);
		transferable.kis_ids = new String[kis_ids.size()];
		kis_ids.copyInto(transferable.kis_ids);
		transferable.mark_ids = new String[mark_ids.size()];
		mark_ids.copyInto(transferable.mark_ids);

		count = nodeLinks.size();
		nodelink_ids = new Vector();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)nodeLinks.get(i);
			nodelink_ids.add(os.getId());
		}
		transferable.nodeLink_ids = new String[nodelink_ids.size()];
		nodelink_ids.copyInto(transferable.nodeLink_ids);

		count = physicalLinks.size();
		link_ids = new Vector();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)physicalLinks.get(i);
			link_ids.add(os.getId());
		}
		transferable.physicalLink_ids = new String[link_ids.size()];
		link_ids.copyInto(transferable.physicalLink_ids);

		count = transmissionPath.size();
		path_ids = new Vector();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)transmissionPath.get(i);
			path_ids.add(os.getId());
		}
		transferable.path_ids = new String[path_ids.size()];
		path_ids.copyInto(transferable.path_ids);
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

	public String getDomainId()
	{
		return domain_id;
	}
	
	public long getModified()
	{
		return modified;
	}

//Используется для для загрузки элементов в класс
//при востановлении класса из базы данных
	public void updateLocalFromTransferable()
	{
		int l;
		int i;

		l = node_ids.size();
		nodes = new Vector();
		for(i = 0; i < l; i++)
		nodes.add(Pool.get("mapnodeelement", (String)node_ids.get(i)));

		l = equipment_ids.size();
		for(i = 0; i	 < l; i++)
			nodes.add(Pool.get("mapequipmentelement", (String)equipment_ids.get(i)));

		l = kis_ids.size();
		for(i = 0; i	 < l; i++)
			nodes.add(Pool.get("mapkiselement", (String)kis_ids.get(i)));

		l = nodelink_ids.size();
		nodeLinks = new Vector();
		for(i = 0; i < l; i++)
			nodeLinks.add(Pool.get("mapnodelinkelement", (String)nodelink_ids.get(i)));

		l = link_ids.size();
		physicalLinks = new Vector();
		for(i = 0; i < l; i++)
			physicalLinks.add(Pool.get("maplinkelement", (String)link_ids.get(i)));

		l = path_ids.size();
		transmissionPath = new Vector();
		for(i = 0; i < l; i++)
			transmissionPath.add(Pool.get("mappathelement", (String)path_ids.get(i)));

		l = mark_ids.size();
		for(i = 0; i < l; i++)
			nodes.add(Pool.get("mapmarkelement", (String)mark_ids.get(i)));
	}

	public void updateFromPool()
	{
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

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new MapContextModel(this);
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new MapContextPane();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapContextDisplayModel();
	}

	public boolean isOpened()
	{
		return (logicalNetLayer != null);
	}

	public void setLogicalNetLayer(LogicalNetLayer logical)
	{
		logicalNetLayer = logical;
		curMapElement = new VoidMapElement(getLogicalNetLayer());//Поумолчанию текущий элемент Void

		if(logicalNetLayer != null)
		{
			longitude = logicalNetLayer.viewer.getCenter()[0];
			latitude = logicalNetLayer.viewer.getCenter()[1];
//			zoomFactor = logicalNetLayer.viewer.getScale();

			defaultScale = logicalNetLayer.viewer.getScale();
			currentScale = logicalNetLayer.viewer.getScale();
		}

	}
	
//Установить флаг видимости PhysicalNodeElement
	public void setPhysicalNodeElementVisibility(boolean visibility)
	{
		showPhysicalNodeElement = visibility;
	}

//Получение флага видимости PhysicalNodeElement
	public boolean isPhysicalNodeElementVisible()
	{
		return showPhysicalNodeElement;
	}

//Получение вектора элементов наследников класса Node
	public Vector getNodes()
	{
		return nodes;
	}

//Установить вектор элементов наследников класса Node
	public void setNodes (Vector myNodes)
	{
		nodes = myNodes;
	}

//Добавить MapNodeElement
	public void addNode(MapNodeElement ob)
	{
		nodes.add( ob);
	}

//Удалить MapNodeElement
	public void removeNode(MapNodeElement ob)
	{
		nodes.removeElement( ob);
//		deleted_nodes_ids.add(ob.getId());
		removedElements.add(ob);
	}

//Получение вектора элементов типа NodeLinks
	public Vector getNodeLinks()
	{
		return nodeLinks;
	}

//Получение MapNodeLinkElement по его ID
	public MapNodeLinkElement getNodeLink(String mapNodeLinkElementID)
	{
		Enumeration e = getNodeLinks().elements();

		while (e.hasMoreElements())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement)e.nextElement();
			if ( nodeLink.getId().equals( mapNodeLinkElementID) )
			{
				return nodeLink;
			}
		}
		return null;
	}

//Получение MapPhysicalLinkElement по его ID
	public MapPhysicalLinkElement getPhysicalLink(String mapPhysicalLinkElementID)
	{
		Enumeration e = this.getPhysicalLinks().elements();

		while (e.hasMoreElements())
		{
			MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement)e.nextElement();
			if ( physicalLink.getId().equals( mapPhysicalLinkElementID) )
			{
				return physicalLink;
			}
		}
		return null;
	}

//Установить вектор элементов типа NodeLinks
	public void setNodeLinks (Vector myNodeLinks)
	{
		nodeLinks = myNodeLinks;
	}

//добавить MapNodeLinkElement
	public void addNodeLink(MapNodeLinkElement ob)
	{
		nodeLinks.add( ob);
	}

//Удалить MapNodeLinkElement
	public void removeNodeLink(MapNodeLinkElement ob)
	{
		nodeLinks.removeElement( ob);
//		deleted_nodeLinks_ids.add(ob.getId());
		removedElements.add(ob);
	}

//Проверка того, что хотя бы один элемент выбран
	public boolean isSelectionEmpty()
	{
		Enumeration e = this.getAllElements().elements();

		while (e.hasMoreElements())
		{
			MapElement curElement = (MapElement)e.nextElement();
			if (curElement.isSelected())
			{
				return false;
			}
		}
		return true;
	}


//Установить текущий элемент по коордитате на карте
	public void setCurrentMapElement (Point myPoint)
	{
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
//A0A
		Enumeration e = getAllElements().elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
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
						curMapElement = getPhysicalLinkbyNodeLink( mapElement.getId());
						notifySchemeEvent(curMapElement);
						notifyCatalogueEvent(curMapElement);
						return;
					}

					if ( linkState == MapContext.SHOW_TRANSMISSION_PATH)
					{
						Dispatcher dispatcher = logicalNetLayer.mapMainFrame.aContext.getDispatcher();

						Enumeration e1 = getTransmissionPathByNodeLink( mapElement.getId()).elements();
						while( e1.hasMoreElements())
						{
							MapTransmissionPathElement path = (MapTransmissionPathElement)e1.nextElement();
							path.select();

							this.logicalNetLayer.perform_processing = false;
							MapNavigateEvent mne = new MapNavigateEvent(this, MapNavigateEvent.MAP_PATH_SELECTED_EVENT);
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

//Установить текущий элемент по коордитате на карте
	public MapNodeLinkElement getEditedNodeLink(Point myPoint)
	{

		Enumeration e = this.nodeLinks.elements();
		while (e.hasMoreElements())
		{
			MapNodeLinkElement mapElement = (MapNodeLinkElement)e.nextElement();
			if (mapElement.isMouseOnThisObjectsLabel(myPoint))
			{
				return mapElement;
			}
		}
		return null;
	}

	public void notifySchemeEvent(MapElement mapElement)
	{
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

	public void notifyCatalogueEvent(MapElement mapElement)
	{
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


//Получить текущий элемент
	public MapElement getCurrentMapElement()
	{
		return curMapElement;
	}

	public void setCurrent(MapElement curMapElement)
	{
		this.curMapElement = curMapElement;
	}

//Получить текущий элемент по коордитате на карте
	public MapElement getCurrentMapElement(Point myPoint)
	{
		MapElement curME = new VoidMapElement(this.getLogicalNetLayer());
		Enumeration e = getAllElements().elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
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
						Enumeration e1 = getTransmissionPathByNodeLink( mapElement.getId()).elements();
						if( e1.hasMoreElements())
						{
							MapTransmissionPathElement path = (MapTransmissionPathElement)e1.nextElement();

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

//Получить NodeLinks содержащие заданный Node
	public Vector getNodeLinksContainingNode(MapNodeElement myNode)
	{
		Vector returnNodeLink = new Vector();
		Enumeration e = nodeLinks.elements();

		while (e.hasMoreElements())
		{
			MapNodeLinkElement myNodeLink = (MapNodeLinkElement)e.nextElement();
//Если один из концов является данным myNode то добавляем его в вектор
			if ( (myNodeLink.endNode == myNode) || (myNodeLink.startNode == myNode))
			{
				returnNodeLink.add(myNodeLink);
			}

		}

		return returnNodeLink;
	}

//Получить PhysicalLink содержащие заданный Node
	public Vector getPhysicalLinksContainingNode(MapNodeElement myNode)
	{
		Vector returnPhysicalLink = new Vector();
		Enumeration e = this.physicalLinks.elements();

		while (e.hasMoreElements())
		{
			MapPhysicalLinkElement myPhysicalLink = (MapPhysicalLinkElement)e.nextElement();
//Если один из концов является данным myNode то добавляем его в вектор
			if ( (myPhysicalLink.endNode == myNode) || (myPhysicalLink.startNode == myNode) )
			{
				returnPhysicalLink.add( myPhysicalLink);
			}

		}

		return returnPhysicalLink;
	}

//Получить NodeLinks содержащиеся в PhysicalLink по physicalLinkID
	public Vector getNodeLinksInPhysicalLink(String physicalLinkID)
	{
		Vector returnNodeLink = new Vector();

		Enumeration e = physicalLinks.elements();
		while (e.hasMoreElements())
		{
			MapPhysicalLinkElement myPhysicalLink = (MapPhysicalLinkElement)e.nextElement();

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

//Получить NodeLinks содержащиеся в TransmissionPath по pathID
	public Vector getNodeLinksInTransmissionPath(String pathID)
	{
		Vector returnNodeLink = new Vector();
		MapTransmissionPathElement transmissionPath = getMapTransmissionPathElement(pathID);

		Enumeration e = transmissionPath.physicalLink_ids.elements();
		while( e.hasMoreElements())
		{
			String physicalLinkID = (String)e.nextElement();
			/// returnNodeLink.
			returnNodeLink.addAll(getNodeLinksInPhysicalLink(physicalLinkID));
		}
		return returnNodeLink;
	}

//Получить вектор Node противоположных у элемета NodeLink по заданному Node
	public Vector getOtherNodeOfNodeLinksContainingNode(MapNodeElement myNode)
	{
		Enumeration e = getNodeLinksContainingNode(myNode).elements();
		Vector returnNodeLink = new Vector();

		while (e.hasMoreElements())
		{
			MapNodeLinkElement myNodeLink = (MapNodeLinkElement)e.nextElement();

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

//Получить другой конец NodeLink по заданному NodeElement
	public MapNodeElement getOtherNodeOfNodeLink(MapNodeLinkElement myNodeLink, MapNodeElement myNode)
	{

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

//Получить другой конец PhysicalLink по заданному NodeElement
	public MapNodeElement getOtherNodeOfPhysicalLink(MapPhysicalLinkElement myPhysicalLink, MapNodeElement myNode)
	{

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


//Отменитьe выбор всем элементам
	public void deselectAll()
	{
		Enumeration e = getAllElements().elements();
		while ( e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			mapElement.deselect();
		}
	}

//Выбрать всё
	public void selectAll()
	{
		Enumeration e = this.getAllElements().elements();

		while(e.hasMoreElements())
		{
			MapElement curElement = (MapElement)e.nextElement();
			curElement.select();
		}
	}

	public void initColumnSizes(JTable table)
	{
	}

	public void setPhysicalLinks(Vector vec)
	{
		physicalLinks = vec;
	}

	public Vector getPhysicalLinks()
	{
		return physicalLinks;
	}

	public void addPhysicalLink(MapPhysicalLinkElement ob)
	{
		physicalLinks.add( ob);
	}

	public void removePhysicalLink(MapPhysicalLinkElement ob)
	{
		physicalLinks.removeElement( ob);
//		deleted_physicalLinks_ids.add( ob.getId());
		removedElements.add(ob);
	}

	public void setTransmissionPath(Vector vec)
	{
		transmissionPath = vec;
	}

	public Vector getTransmissionPath()
	{
		return transmissionPath;
	}

	public void addTransmissionPath(MapTransmissionPathElement ob)
	{
		transmissionPath.add( ob);
	}

	public void removeTransmissionPath(MapTransmissionPathElement ob)
	{
		transmissionPath.removeElement( ob);
//		deleted_transmissionPath_ids.add( ob.getId());
		removedElements.add(ob);
	}

//Получить MapPhysicalLinkElement по NodeLink который он содержит
	public MapPhysicalLinkElement getPhysicalLinkbyNodeLink(String nodeLinkElementID)
	{
		Enumeration e = getPhysicalLinks().elements();
		while (e.hasMoreElements())
		{
			MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement)e.nextElement();
			if ( physicalLink.nodeLink_ids.contains(nodeLinkElementID) )
			{
				return physicalLink;
			}
		}
		return null;
	}

//Получить вектор TransmissionPath элементов, которые содержат MapPhysicalLinkElement, по physicalLinkID
	public Vector getTransmissionPathByPhysicalLink(String physicalLinkID)
	{
		Vector returnVector = new Vector();;
		Enumeration e = getTransmissionPath().elements();
		while (e.hasMoreElements())
		{
			MapTransmissionPathElement transmissionPath = (MapTransmissionPathElement)e.nextElement();
			if ( transmissionPath.physicalLink_ids.contains( physicalLinkID) )
			{
				returnVector.add(transmissionPath);
			}
		}
		return returnVector;
	}

	//Получить вектор TransmissionPath элементов, которые содержат MapNodeLinkElement, по nodeLinkID

	public Vector getTransmissionPathByNodeLink(String nodeLinkID)
	{
		return getTransmissionPathByPhysicalLink(
				getPhysicalLinkbyNodeLink(nodeLinkID).getId());
	}

	public void setLongLat( double longit, double latit)
	{
		longitude = longit;
		latitude = latit;
	}

//A0A
	public void deleteTranmissionPath( String physicalLinkID)
	{

		Enumeration e = getTransmissionPath().elements();
		while (e.hasMoreElements())
		{
			MapTransmissionPathElement transmissionPath = (MapTransmissionPathElement)e.nextElement();
			if ( transmissionPath.physicalLink_ids.contains( physicalLinkID) )
			{
				this.removeTransmissionPath(transmissionPath);
			}
		}

	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

//"Это функция используется для востанивления класса из базы данных
	public void createFromPool( LogicalNetLayer logical)
	{
		logicalNetLayer = logical;
		curMapElement = new VoidMapElement(getLogicalNetLayer());
		showPhysicalNodeElement = true;
//		created = System.currentTimeMillis();
//    logicalNetLayer.getMapViewer().setZoomFactor( zoomFactor);
	    logicalNetLayer.getMapViewer().setScale( currentScale);
		zoom(currentScale);

		logicalNetLayer.viewer.setCenter(longitude, latitude);
	}

//A0A
	public Vector getMapKISNodeElements()
	{
		Vector returnVector = new Vector();

		Enumeration e = nodes.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			if ( mapElement instanceof MapKISNodeElement)
			{
				returnVector.add( mapElement);
			}
		}
		return returnVector;
	}

	public Vector getMapPhysicalNodeElements()
	{
		Vector returnVector = new Vector();

		Enumeration e = nodes.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			if ( mapElement instanceof MapPhysicalNodeElement)
			{
				returnVector.add( mapElement);
			}
		}
		return returnVector;
	}

//A0A
	public MapKISNodeElement getMapKISNodeElement(String myID)
	{
		Enumeration e = getMapKISNodeElements().elements();
		while (e.hasMoreElements())
		{
			MapKISNodeElement myMapKISNodeElement = (MapKISNodeElement)e.nextElement();

			if ( myMapKISNodeElement.getId().equals(myID))
			{
				return myMapKISNodeElement;
			}
		}
		return null;
	}

//A0A
	public MapTransmissionPathElement getMapTransmissionPathElement(String myID)
	{

		Enumeration e = getTransmissionPath().elements();
		while (e.hasMoreElements())
		{
			MapTransmissionPathElement myMapTransmissionPathElement = (MapTransmissionPathElement)e.nextElement();

			if ( myMapTransmissionPathElement.getId().equals(myID))
			{
				return myMapTransmissionPathElement;
			}
		}
		return null;
	}

//A0A
	public Vector getMapEquipmentNodeElements()
	{
		Vector returnVector = new Vector();

		Enumeration e = nodes.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			if ( mapElement instanceof MapEquipmentNodeElement)
			{
				returnVector.add( mapElement);
			}
		}
		return returnVector;
	}

	public Vector getMapMarkElements()
	{
		Vector returnVector = new Vector();

		Enumeration e = nodes.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			if ( mapElement instanceof MapMarkElement)
			{
				returnVector.add( mapElement);
			}
		}
		return returnVector;
	}

//A0A
	public MapEquipmentNodeElement getMapEquipmentNodeElement(String myID)
	{

		Enumeration e = getMapEquipmentNodeElements().elements();
		while (e.hasMoreElements())
		{
			MapEquipmentNodeElement myMapEquipmentNodeElement = (MapEquipmentNodeElement)e.nextElement();

			if ( myMapEquipmentNodeElement.getId().equals(myID))
			{
				return myMapEquipmentNodeElement;
			}
		}
		return null;
	}

//A0A
	public Vector getAllElements()
	{
		Vector returnVector = new Vector();

		Enumeration e = nodes.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			returnVector.add( mapElement);
		}

		e = nodeLinks.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			returnVector.add( mapElement);
		}

		e = physicalLinks.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			returnVector.add( mapElement);
		}

		e = transmissionPath.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			returnVector.add( mapElement);
		}

		e = markers.elements();
		while (e.hasMoreElements())
		{
			MapElement mapElement = (MapElement)e.nextElement();
			returnVector.add( mapElement);
		}
		return returnVector;
	}

//A0A
	public MapMarker getMarker(String markerID)
	{
		Enumeration e = markers.elements();
		while( e.hasMoreElements())
		{
			MapMarker marker = (MapMarker)e.nextElement();
			if ( marker.getId().equals(markerID))
		 return marker;

		}
		return null;
	}

	public void removeMarkers()
	{
		markers.removeAllElements();
	}

//A0A
	public Vector getRemovedElements()
	{
		return removedElements;
	}

	public void zoomIn()
	{
//		System.out.println("--- " + getLogicalNetLayer().getMapViewer().getScale());

		getLogicalNetLayer().viewer.zoomIn();
		currentScale = logicalNetLayer.viewer.getScale();

		updateZoom();
	}

	public void updateZoom()
	{
		double sF = defaultScale / currentScale;

		Enumeration en =  getNodes().elements();
		while (en.hasMoreElements())
		{
			MapNodeElement curNode = (MapNodeElement)en.nextElement();
			curNode.setScaleCoefficient(sF);
			curNode.setImageID(curNode.getImageID());
		}
	}

//A0A
	public void zoom(double scale)
	{
//		getLogicalNetLayer().getMapViewer().zoomIn(scale);
		currentScale = scale;

		updateZoom();
	}

//A0A
	public void zoomOut()
	{
//		System.out.println("--- " + getLogicalNetLayer().getMapViewer().getScale());

		getLogicalNetLayer().viewer.zoomOut();
		currentScale = logicalNetLayer.viewer.getScale();

		updateZoom();
	}

	public Vector getPhysicalLinksInTransmissiionPath(String pathID)
	{
		Vector returnLinkVector = new Vector();
		MapTransmissionPathElement transmissionPath = getMapTransmissionPathElement(pathID);
		Enumeration e = transmissionPath.physicalLink_ids.elements();
		while( e.hasMoreElements())
		{
			String physicalLinkID = (String)e.nextElement();
			MapPhysicalLinkElement bufferPhysLink = (MapPhysicalLinkElement)getPhysicalLink(physicalLinkID);

			returnLinkVector.add(bufferPhysLink);
		}
		return returnLinkVector;
	}

//Возвращает упорядоченный набор PhysicalLink в TransmissionPath начиная с начала
	public Vector getPhysicalLinksInTransmissiionPath1(String pathID)
	{
		Vector returnLinkVector = new Vector();

		MapTransmissionPathElement transPath = getMapTransmissionPathElement(pathID);
		MapNodeElement bufferNode = transPath.startNode;
		MapPhysicalLinkElement bufferPhysLink = (MapPhysicalLinkElement)getPhysicalLinksContainingNode(bufferNode).get(0);

		returnLinkVector.add(bufferPhysLink);
		bufferNode = getOtherNodeOfPhysicalLink(bufferPhysLink, bufferNode);

		while (bufferNode != transPath.endNode)
		{
			for (int i = 0; i < getPhysicalLinksContainingNode(bufferNode).size();i++)
			{
				MapPhysicalLinkElement buffer2Link =
						(MapPhysicalLinkElement)getPhysicalLinksContainingNode(bufferNode).get(i);

				if (transPath.physicalLink_ids.contains(buffer2Link.getId()) &&
					buffer2Link != bufferPhysLink)
				{
					bufferPhysLink = buffer2Link;
					returnLinkVector.add(bufferPhysLink);

					i = getPhysicalLinksContainingNode(bufferNode).size();//exit from while
				}
			}

			bufferNode = getOtherNodeOfPhysicalLink(bufferPhysLink, bufferNode);
		}
		return returnLinkVector;
	}

	public Vector getEquipKISElemenetsInTansPath(String pathID)
	{
		Vector returnNodeVector = new Vector();

		MapTransmissionPathElement transmissionPath = getMapTransmissionPathElement(pathID);
		MapNodeElement bufferNode = transmissionPath.startNode;
		returnNodeVector.add(bufferNode);

		Enumeration e = transmissionPath.physicalLink_ids.elements();
		while( e.hasMoreElements())
		{
			String physicalLinkID = (String)e.nextElement();
			MapPhysicalLinkElement bufferPhysLink = (MapPhysicalLinkElement)getPhysicalLink(physicalLinkID);
			bufferNode = bufferPhysLink.endNode;

			returnNodeVector.add(bufferNode);
		}
		return returnNodeVector;
	}

//Возвращает упорядоченный набор Equip & KIS в TransmissionPath начиная с начала
	public Vector getEquipKISElemenetsInTansPath1(String pathID)
	{
		Vector returnNodeVector = new Vector();

		MapTransmissionPathElement transPath = getMapTransmissionPathElement(pathID);
		MapNodeElement bufferNode = transPath.startNode;
		MapPhysicalLinkElement bufferPhysLink = (MapPhysicalLinkElement)getPhysicalLinksContainingNode(bufferNode).get(0);

		returnNodeVector.add(bufferNode);

		bufferNode = getOtherNodeOfPhysicalLink(bufferPhysLink, bufferNode);

		while (bufferNode != transPath.endNode)
		{
			for (int i = 0; i < getPhysicalLinksContainingNode(bufferNode).size();i++)
			{
				MapPhysicalLinkElement buffer2Link =
						(MapPhysicalLinkElement)getPhysicalLinksContainingNode(bufferNode).get(i);

				if (transPath.physicalLink_ids.contains(buffer2Link.getId()) &&
					buffer2Link != bufferPhysLink)
				{
					bufferPhysLink = buffer2Link;
					i = getPhysicalLinksContainingNode(bufferNode).size();//exit from while
				}
			}

			returnNodeVector.add(bufferNode);
			bufferNode = getOtherNodeOfPhysicalLink(bufferPhysLink, bufferNode);
		}

		returnNodeVector.add(bufferNode);
		return returnNodeVector;
	}

//A0A
	public MapNodeElement getNode(String nodeID)
	{
		for (int i = 0; i < getNodes().size();i++)
		{
			MapNodeElement node = (MapNodeElement)getNodes().get(i);
			if ( node.getId().equals(nodeID ) )
				return node;
		}
		return null;
	}

//A0A
	public MapNodeLinkElement getNodeLink(MapNodeElement start_node, MapNodeElement end_node)
	{
		for(int i = 0;i < getNodeLinks().size();i++)
		{
			MapNodeLinkElement link = (MapNodeLinkElement)getNodeLinks().get(i);
			if (((link.startNode == start_node) && (link.endNode == end_node)) ||
				((link.startNode == end_node) && (link.endNode == start_node)) )
			{
				return link;
			}
		}
		return null;
	}

//A0A
	public MapPhysicalLinkElement getPhysicalLink(MapNodeElement start_node, MapNodeElement end_node)
	{
		for(int i = 0;i < getNodeLinks().size();i++)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement)getPhysicalLinks().get(i);
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
//		out.writeDouble(standartScale);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
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
		nodes = (Vector )in.readObject();
		nodeLinks = (Vector )in.readObject();
		physicalLinks = (Vector )in.readObject();
		transmissionPath = (Vector )in.readObject();
		markers = (Vector )in.readObject();
		showPhysicalNodeElement = in.readBoolean();
		linkState = in.readInt();
		currentScale = in.readDouble();
		defaultScale = in.readDouble();
//		standartScale = in.readDouble();

		transferable = new MapContext_Transferable();

    node_ids = new Vector();
    equipment_ids = new Vector();
    kis_ids = new Vector();
    nodelink_ids = new Vector();
    link_ids = new Vector();
    path_ids = new Vector();
    mark_ids = new Vector();

    removedElements = new Vector();

    deleted_nodes_ids = new Vector();
    deleted_nodeLinks_ids = new Vector();
    deleted_physicalLinks_ids = new Vector();
    deleted_transmissionPath_ids = new Vector();

		curMapElement = new VoidMapElement();//Поумолчанию текущий элемент Void

		updateFromPool();
//		Pool.put("serverimage", getId(), this);
	}

}