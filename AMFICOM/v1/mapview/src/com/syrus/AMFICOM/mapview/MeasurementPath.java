/**
 * $Id: MeasurementPath.java,v 1.5 2005/02/02 15:17:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElement;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Элемент пути.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/02/02 15:17:30 $
 * @module mapviewclient_v1
 */
public class MeasurementPath implements MapElement
{
	private static final long serialVersionUID = 02L;

	/**
	 * Идентификатор.
	 */
	protected Identifier id;

	/**
	 * Название.
	 */
	protected String	name;

	/**
	 * Описание.
	 */
	protected String	description;

	/**
	 * Флаг выделения.
	 */
	protected transient boolean selected = false;

	/**
	 * Флаг наличия сигнала тревоги.
	 */
	protected transient boolean alarmState = false;

	/**
	 * Флаг удаления.
	 */
	protected transient boolean removed = false;

	/**
	 * Топологическая схема.
	 */
	protected transient Map map = null;

	/**
	 * Схемный путь.
	 */
	protected SchemePath schemePath;
	/**
	 * Схема.
	 */
	protected Scheme scheme;

	/**
	 * Вид карты.
	 */
	protected MapView mapView;

	/**
	 * Конструктор.
	 * @param schemePath схемный путь
	 * @param id идентификатор
	 * @param mapView вид
	 */
	protected MeasurementPath(
			SchemePath schemePath,
			Identifier id, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;
		this.schemePath = schemePath;
		this.name = schemePath.name();
		if(mapView != null)
		{
			this.map = mapView.getMap();
		}
	}

	/**
	 * Сохдать новый элемент пути.
	 * @param schemePath схемный путь
	 * @param mapView вид карты
	 * @return новый элемент пути
	 * @throws com.syrus.AMFICOM.general.CreateObjectException нельзя создать
	 */
	public static MeasurementPath createInstance(
			SchemePath schemePath,
			MapView mapView)
		throws CreateObjectException 
	{
		if (mapView == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new MeasurementPath(
				schemePath,
				ide,
				mapView);
		}
		catch (IllegalObjectEntityException e)
		{
			throw new CreateObjectException("MapMeasurementPathElement.createInstance | cannot generate identifier ", e);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapMeasurementPathElement.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List getCharacteristics() 
	{
		return Collections.unmodifiableList(this.schemePath.characteristicsImpl().getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCharacteristic(Characteristic ch)
	{
		this.schemePath.addCharacteristic(ch);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		this.schemePath.removeCharacteristic(ch);
	}
	
	/**
	 * Установить идентификатор
	 * @param id идентификатор
	 */
	public void setId(Identifier id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Identifier getId()
	{
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() 
	{
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) 
	{
		this.name = name;
	}

	/**
	 * Получить описание.
	 * @return описание
	 */
	public String getDescription() 
	{
		return this.description;
	}

	/**
	 * Установить описание.
	 * @param description описание
	 */
	public void setDescription(String description) 
	{
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map getMap()
	{
		return this.map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState()
	{
		return this.alarmState;
	}

	protected DoublePoint location = new DoublePoint(0.0, 0.0);

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation()
	{
		int count = 0;
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			DoublePoint an = cpath.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}
		this.location.setLocation(x /= count, y /= count);
		
		return this.location;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved()
	{
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	/**
	 * Установить вид карты.
	 * @param mapView вид карты
	 */
	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	/**
	 * получить вид карты.
	 * @return вид карты
	 */
	public MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * Установить схемный путь.
	 * @param schemePath схемный путь.
	 */
	public void setSchemePath(SchemePath schemePath)
	{
		this.schemePath = schemePath;
		this.name = schemePath.name();
		this.scheme = schemePath.scheme();
	}

	/**
	 * получить схемный путь.
	 * @return схемный путь
	 */
	public SchemePath getSchemePath()
	{
		return this.schemePath;
	}

	/**
	 * @deprecated
	 */
	public boolean isSelectionVisible()
	{
		return isSelected();
	}

	/**
	 * Возвращает топологическую длинну в метрах
	 * @return топологическая длина
	 */
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getSortedCablePaths().iterator();
		while( e.hasNext())
		{
			CablePath cpath = (CablePath)e.next();
			length = length + cpath.getLengthLt();
		}
		return length;
	}

	/**
	 * Возвращает физическую длину в метрах.
	 * @return физическая длина
	 */
	public double getLengthLf()
	{
		return SchemeUtils.getPhysicalLength(this.schemePath);
	}

	/**
	 * Возвращает оптическую длину в метрах.
	 * @return оптическая длина
	 */
	public double getLengthLo()
	{
		return SchemeUtils.getOpticalLength(this.schemePath);
	}

	/**
	 * Несортированный список кабельных путей, из которых строится 
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	protected List unsortedCablePaths = new LinkedList();

	/**
	 * Получить список топологических кабелей, которые входят в состав пути.
	 * Список строится динамически.
	 * @return список кабельных путей
	 */
	protected List getCablePaths()
	{
		synchronized(this.unsortedCablePaths)
		{
			this.unsortedCablePaths.clear();
			for(int i = 0; i < this.schemePath.links().length; i++)
			{
				PathElement pe = this.schemePath.links()[i];
				switch(pe.type().value())
				{
					case Type._SCHEME_ELEMENT:
						SchemeElement se = (SchemeElement )pe.abstractSchemeElement();
						SiteNode site = this.mapView.findElement(se);
						if(site != null)
						{
							//TODO think if link to 'site' is needed for mPath
		//					mPath.addCablePath(site);
						}
						break;
					case Type._SCHEME_LINK:
						SchemeLink link = (SchemeLink )pe.abstractSchemeElement();
						SchemeElement sse = SchemeUtils.getSchemeElementByDevice(this.scheme, link.sourceSchemePort().schemeDevice());
						SchemeElement ese = SchemeUtils.getSchemeElementByDevice(this.scheme, link.targetSchemePort().schemeDevice());
						SiteNode ssite = this.mapView.findElement(sse);
						SiteNode esite = this.mapView.findElement(ese);
						if(ssite == esite)
						{
							//TODO think if link to 'link' is needed for mPath
		//					mPath.addCablePath(ssite);
						}
						break;
					case Type._SCHEME_CABLE_LINK:
						SchemeCableLink clink = (SchemeCableLink )pe.abstractSchemeElement();
						CablePath cp = this.mapView.findCablePath(clink);
						if(cp != null)
						{
							this.unsortedCablePaths.add(cp);
						}
						break;
					default:
						throw new UnsupportedOperationException();
				}
			}
		}
		return this.unsortedCablePaths;
	}

	/**
	 * Сортированный список кабельных путей, из которых строится 
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	protected List sortedCablePaths = new LinkedList();
	/**
	 * Сортированный список фрагментов линий, из которых строится 
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	protected List sortedNodeLinks = new LinkedList();
	/**
	 * Сортированный список узлов, по которым проходит 
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	protected List sortedNodes = new LinkedList();

	/**
	 * Get {@link #sortedNodeLinks}.
	 * @return this.sortedNodeLinks
	 */
	public List getSortedNodeLinks()
	{
		return this.sortedNodeLinks;
	}
	
	/**
	 * Get {@link #sortedNodes}.
	 * @return this.sortedNodes
	 */
	public List getSortedNodes()
	{
		return this.sortedNodes;
	}
	
	/**
	 * Get {@link #sortedCablePaths}.
	 * @return this.sortedCablePaths
	 */
	public List getSortedCablePaths()
	{
		return this.sortedCablePaths;
	}
	
	/**
	 * Получить начальный узел пути. Определяется динамически.
	 * @return узел
	 */
	public AbstractNode getStartNode()
	{
		return getMapView().getStartNode(this.getSchemePath());
	}
	
	/**
	 * Получить конечный узел пути. Определяется динамически.
	 * @return узел
	 */
	public AbstractNode getEndNode()
	{
		return getMapView().getEndNode(this.getSchemePath());
	}
	
	/**
	 * Сортировать элементы пути. Включает сортировку кабельных путей,
	 * фрагментов линий и узлов.
	 */
	public void sortPathElements()
	{
		this.sortedCablePaths.clear();
		this.sortedNodeLinks.clear();
		this.sortedNodes.clear();
		
		AbstractNode node = getStartNode();

		this.sortedCablePaths.addAll(getCablePaths());
		
		for(Iterator it = this.sortedCablePaths.iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			cpath.sortNodeLinks();
			if(cpath.getStartNode().equals(node))
			{
				this.sortedNodeLinks.addAll(cpath.getSortedNodeLinks());
				this.sortedNodes.addAll(cpath.getSortedNodes());
			}
			else
			{
				for(ListIterator lit = cpath.getSortedNodeLinks().listIterator(cpath.getSortedNodeLinks().size()); lit.hasPrevious();)
				{
					this.sortedNodeLinks.add(lit.previous());
				}
				for(ListIterator lit = cpath.getSortedNodes().listIterator(cpath.getSortedNodes().size()); lit.hasPrevious();)
				{
					this.sortedNodes.add(lit.previous());
				}
			}
			node = cpath.getOtherNode(node);

			// to avoid duplicate entry
			this.sortedNodes.remove(node);
		}
		this.sortedNodes.add(node);
	}

	/**
	 * Получить следующий фрагмент по цепочке сортированных фрагментов.
	 * @param nodeLink фрагмент
	 * @return следующий фрагмент, или <code>null</code>, если nl - последний 
	 * в списке
	 */
	public NodeLink nextNodeLink(NodeLink nodeLink)
	{
		int index = getSortedNodeLinks().indexOf(nodeLink);
		if(index == getSortedNodeLinks().size() - 1)
			return null;

		return (NodeLink)getSortedNodeLinks().get(index + 1);
	}

	/**
	 * Получить предыдущий фрагмент по цепочке сортированных фрагментов.
	 * @param nodeLink фрагмент
	 * @return предыдущий фрагмент, или <code>null</code>, если nl - первый 
	 * в списке
	 */
	public NodeLink previousNodeLink(NodeLink nodeLink)
	{
		int index = getSortedNodeLinks().indexOf(nodeLink);
		if(index == 0)
			return null;

		return (NodeLink)getSortedNodeLinks().get(index - 1);
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}
}
