/**
 * $Id: MapView.java,v 1.32 2005/01/31 12:19:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс используется для хранения объектов, отображаемых на 
 * топологической карте. Объекты включают в себя:
 * <br>&#9;- объект карты Map, то есть прокладку канализационную
 * <br>&#9;- набор физических схем {@link Scheme}, которые проложены по данному
 *        контексту
 * <br>Объект <code>MapView</code> являтся вроппером для класса 
 * <code>{@link com.syrus.AMFICOM.mapview.MapView}</code>
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.32 $, $Date: 2005/01/31 12:19:19 $
 * @module mapviewclient_v1
 * @deprecated use {@link LogicalNetLayer#getMapViewController()}
 */
public final class MapView extends com.syrus.AMFICOM.mapview.MapView
{
	private static final long serialVersionUID = 01L;

	/** Список кабелей. */
	protected List cablePaths = new LinkedList();
	
	/** Список измерительных путей. */
	protected List measurementPaths = new LinkedList();
	
	/** Список маркеров. */
	protected List markers = new LinkedList();

	/**
	 * Установить топологическую схему.
	 * @param map топологическая схема
	 */
	public void setMap(Map map)
	{
		super.setMap(map);
//		scanSchemes();
	}
	
	public MapView(Identifier id)
		throws RetrieveObjectException, ObjectNotFoundException
	{
		super(id);
	}
	
	/**
	 * Коррекция начального и конечного узлов топологической прокладки кабеля
	 * по элементам карты, в которых размещены начальный и конечный элемент
	 * схемного кабеля.
	 * 
	 * @param cable топологический кабель
	 * @param scl схемный кабель
	 */
	public void correctStartEndNodes(CablePath cable, SchemeCableLink scl)
	{
		SiteNode[] node = getSideNodes(scl);
		if(node[0] != null && node[1] != null)
		{
			cable.setStartNode(node[0]);
			cable.setEndNode(node[1]);
		}
	}
	
	/**
	 * Поле сделано статическим, чтобы каждый раз при вызове метода 
	 * getSideNodes не создавался новый массив ссылок
	 */
	private static SiteNode[] linkSideNodes = new SiteNode[2];
	
	/**
	 * Возвращает массив из двух топологических элементов, в которых 
	 * расположены концевые элементы кабеля. Если элемент не найден (не
	 * нанесен на карту), соответствующий элемент массива равен null.
	 * 
	 * @param scl кабель
	 * @return массив концевых узлов:
	 * 	linkSideNodes[0] - начальный узел
	 *  linkSideNodes[1] - конечный узел
	 * @deprecated use {@link #getStartNode(SchemeCableLink)} & 
	 * {@link #getEndNode(SchemeCableLink)}
	 */
	public SiteNode[] getSideNodes(SchemeCableLink scl)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalCableLinks(sch).contains(scl))
				{
					SchemeElement se = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(sch, scl.sourceAbstractSchemePort().schemeDevice()));
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(sch, scl.targetAbstractSchemePort().schemeDevice()));
					linkSideNodes[1] = findElement(se2);
					break;
				}
			}
		}
		catch(Exception ex)
		{
			linkSideNodes[0] = null;
			linkSideNodes[1] = null;
		}
		return linkSideNodes;
	}

	public SiteNode getStartNode(SchemeCableLink scl)
	{
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalCableLinks(sch).contains(scl))
				{
					SchemeElement se = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(
								sch, 
								scl.sourceAbstractSchemePort().schemeDevice()));
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	public SiteNode getEndNode(SchemeCableLink scl)
	{
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalCableLinks(sch).contains(scl))
				{
					SchemeElement se = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(
								sch, 
								scl.targetAbstractSchemePort().schemeDevice()));
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Возвращает массив из двух топологических элементов, в которых 
	 * расположены концевые элементы схемного пути. Если элемент не найден (не
	 * нанесен на карту), соответствующий элемент массива равен null.
	 * @param path путь
	 * @return массив концевых узлов:
	 * 	linkSideNodes[0] - начальный узел
	 *  linkSideNodes[1] - конечный узел
	 * @deprecated use {@link #getStartNode(SchemePath)} & 
	 * {@link #getEndNode(SchemePath)}
	 */
	public SiteNode[] getSideNodes(SchemePath path)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalPaths(sch).contains(path))
				{
					SchemeElement se = SchemeUtils.getTopologicalElement(
							sch,
							path.startDevice());
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = SchemeUtils.getTopologicalElement(
							sch,
							path.endDevice());
					linkSideNodes[1] = findElement(se2);
					break;
				}
			}
		}
		catch(Exception ex)
		{
			linkSideNodes[0] = null;
			linkSideNodes[1] = null;
		}
		return linkSideNodes;
	}
	
	public SiteNode getStartNode(SchemePath path)
	{
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalPaths(sch).contains(path))
				{
					SchemeElement se = SchemeUtils.getTopologicalElement(
							sch,
							path.startDevice());
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	public SiteNode getEndNode(SchemePath path)
	{
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalPaths(sch).contains(path))
				{
					SchemeElement se = SchemeUtils.getTopologicalElement(
							sch,
							path.endDevice());
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Найти элемент карты, к которому привязан данный схемный элемент.
	 * @param se элемент схемы
	 * @return узел.
	 * 	null если элемент не найден
	 */
	public SiteNode findElement(SchemeElement se)
	{
		if(se == null)
			return null;
		for(Iterator it = getMap().getSiteNodes().iterator(); it.hasNext();)
		{
			SiteNode node = (SiteNode )it.next();
			if(node instanceof UnboundNode)
				if(((UnboundNode)node).getSchemeElement().equals(se))
					return node;
			if(se.siteNodeImpl() != null
				&& se.siteNodeImpl().equals(node))
						return node;
		}
		return null;
	}

	/**
	 * Найти элемент кабеля на карте.
	 * @param scl кабель
	 * @return топологический кабель
	 */
	public CablePath findCablePath(SchemeCableLink scl)
	{

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getSchemeCableLink().equals(scl))
					return cp;
		}
		return null;
	}

	/**
	 * Найти топологический путь, соответствующий схемному пути.
	 * @param path схемный путь
	 * @return топологический путь
	 */
	public MeasurementPath findMeasurementPath(SchemePath path)
	{
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			if(mp.getSchemePath().equals(path))
				return mp;
		}
		return null;
	}

	/**
	 * Получить список топологических кабелей.
	 * @return список топологических кабелей
	 */
	public List getCablePaths()
	{
		return cablePaths;
	}

	/**
	 * Добавить новый топологический кабель.
	 * @param cable топологический кабель
	 */
	public void addCablePath(CablePath cable)
	{
		cablePaths.add(cable);
	}

	/**
	 * Удалить топологический кабель.
	 * @param cable топологический кабель
	 */
	public void removeCablePath(CablePath cable)
	{
		cablePaths.remove(cable);
		cable.setSelected(false);
	}

	/**
	 * Получить список топологических кабелей, проложенных по указанной
	 * линии.
	 * @param link линия
	 * @return список топологических кабелей
	 */
	public List getCablePaths(PhysicalLink link)
	{
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getLinks().contains(link))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * Получить список топологических кабелей, проходящих через указанный узел.
	 * @param node узел
	 * @return список топологических кабелей
	 */
	public List getCablePaths(AbstractNode node)
	{
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodes();
			if(cp.getSortedNodes().contains(node))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * Получить список топологических кабелей, проходящих через указанный 
	 * фрагмент линии.
	 * @param nodeLink фрагмент линии
	 * @return список топологических кабелей
	 */
	public List getCablePaths(NodeLink nodeLink)
	{
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodeLinks();
			if(cp.getSortedNodeLinks().contains(nodeLink))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * Получить список путей тестирования.
	 * @return список путей тестирования
	 */
	public List getMeasurementPaths()
	{
		return measurementPaths;
	}

	/**
	 * Добавить новый путь тестирования.
	 * @param path новый путь тестирования
	 */
	public void addMeasurementPath(MeasurementPath path)
	{
		measurementPaths.add(path);
	}

	/**
	 * Удалить путь тестирования.
	 * @param path путь тестирования
	 */
	public void removeMeasurementPath(MeasurementPath path)
	{
		measurementPaths.remove(path);
		path.setSelected(false);
	}

	/**
	 * Получить список топологических путей, проходящих через указанный 
	 * топологический кабель.
	 * @param cpath топологический кабель
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(CablePath cpath)
	{
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			if(mp.getSortedCablePaths().contains(cpath))
				returnVector.add(mp);
		}
		return returnVector;
	}

	/**
	 * Получить список топологических путей, проходящих через указанную линию.
	 * @param link линия
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(PhysicalLink link)
	{
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				if(cp.getLinks().contains(link))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * Получить список топологических путей, проходящих через указанный узел.
	 * @param node узел
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(AbstractNode node)
	{
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodes();
				if(cp.getSortedNodes().contains(node))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * Получить список топологических путей, проходящих через указанный 
	 * фрагмент линии.
	 * @param nodeLink фрагмент линии
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(NodeLink nodeLink)
	{
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodeLinks();
				if(cp.getSortedNodeLinks().contains(nodeLink))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * Получить топологический путь по идентификатору измерительного элемента.
	 * @param meId идентификатор измерительного элемента
	 * @return топологический путь
	 * @throws com.syrus.AMFICOM.general.CommunicationException 
	 *  см. {@link ConfigurationStorableObjectPool#getStorableObject(Identifier, boolean)}
	 * @throws com.syrus.AMFICOM.general.DatabaseException
	 *  см. {@link ConfigurationStorableObjectPool#getStorableObject(Identifier, boolean)}
	 */
	public MeasurementPath getMeasurementPathByMonitoredElementId(Identifier meId)
		throws CommunicationException, DatabaseException
	{
		MeasurementPath path = null;
		MonitoredElement me = (MonitoredElement )
			ConfigurationStorableObjectPool.getStorableObject(meId, true);
		if(me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH))
		{
			Identifier tpId = (Identifier )(me.getMonitoredDomainMemberIds().get(0));
			TransmissionPath tp = (TransmissionPath )
				ConfigurationStorableObjectPool.getStorableObject(tpId, true);
			if(tp != null)
			{
				for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
				{
					MeasurementPath mp = (MeasurementPath)it.next();
					if(mp.getSchemePath().pathImpl().equals(tp))
					{
						path = mp;
						break;
					}
				}
			}
		}

		return path;
	}

	/**
	 * Удалить все маркеры.
	 */
	public void removeMarkers()
	{
		getMap().getNodes().removeAll(markers);
		markers.clear();
	}

	/**
	 * Удалить путь тестирования.
	 * @param marker маркер
	 */
	public void removeMarker(Marker marker)
	{
		markers.remove(marker);
		getMap().removeNode(marker);
		marker.setSelected(false);
	}

	/**
	 * Получить все маркеры.
	 * @return список маркеров
	 */
	public List getMarkers()
	{
		return markers;
	}

	/**
	 * Добавить новый маркер.
	 * @param marker маркер
	 */
	public void addMarker(Marker marker)
	{
		markers.add(marker);
		getMap().addNode(marker);
	}

	/**
	 * Получить маркер по идентификатору.
	 * @param markerId идентификатор
	 * @return маркер
	 */
	public Marker getMarker(Identifier markerId)
	{
		Iterator e = markers.iterator();
		while( e.hasNext())
		{
			Marker marker = (Marker)e.next();
			if ( marker.getId().equals(markerId))
				return marker;
		}
		return null;
	}

	/**
	 * Получить список всех олементов контекста карты.
	 * @return список всех топологических элементов
	 */
	public List getAllElements()
	{
		List returnVector = getMap().getAllElements();
		
		Iterator e;

		e = getCablePaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		e = getMeasurementPaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		e = markers.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		return returnVector;
	}

	/**
	 * Отменить выбор всем элементам.
	 */
	public void deselectAll()
	{
		Iterator e = getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			mapElement.setSelected(false);
		}
		getMap().clearSelection();
	}

	/**
	 * Remove all temporary objects on mapview when mapview was edited and
	 * closed without saving.
	 */
	public void revert()
	{
		removeMarkers();
	}

/* from SiteNode

	//Возвращяет длинну линий внутри данного узла,
	//пересчитанную на коэффициент топологической привязки
	public PathElement countPhysicalLength(SchemePath sp, PathElement pe, Enumeration pathelements)
	{
		physical_length = 0.0;

		if(this.elementId == null || this.elementId.equals(""))
			return pe;
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, this.elementId);

		Vector vec = new Vector();
		Enumeration e = se.getAllSchemesLinks();
		for(;e.hasMoreElements();)
			vec.add(e.nextElement());

		for(;;)
		{
			if(pe.is_cable)
			{
				SchemeCableLink schemeCableLink =
						(SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.linkId);
				if(schemeCableLink == null)
				{
					System.out.println("Something wrong... - schemeCableLink == null");
					return pe;
				}
				if(!vec.contains(schemeCableLink))
					return pe;
				physical_length += schemeCableLink.getPhysicalLength();
			}
			else
			{
				SchemeLink schemeLink =
						(SchemeLink )Pool.get(SchemeLink.typ, pe.linkId);
				if(schemeLink == null)
				{
					System.out.println("Something wrong... - schemeLink == null");
					return pe;
				}
				if(!vec.contains(schemeLink))
					return pe;
				physical_length += schemeLink.getPhysicalLength();
			}
			if(pathelements.hasMoreElements())
				pe = (PathElement )pathelements.nextElement();
			else
				return null;
		}

		return pe;//stub
	}
*/	


}
