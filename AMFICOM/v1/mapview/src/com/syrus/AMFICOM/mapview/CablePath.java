/**
 * $Id: CablePath.java,v 1.3 2005/02/02 15:17:30 krupenn Exp $
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
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Элемент кабельного пути. Описывает привязку кабеля к топологическим линиям.
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/02 15:17:30 $
 * @module mapviewclient_v1
 */
public class CablePath implements MapElement
{
	private static final long serialVersionUID = 02L;

	/**
	 * Идентификатор.
	 */
	protected Identifier id;

	/**
	 * Название.
	 */
	protected String name;

	/**
	 * Описание.
	 */
	protected String description;

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
	 * Узел карты, к которому привязан начальный узел кабеля.
	 */
	private AbstractNode startNode;

	/**
	 * Узел карты, к которому привязан конечный узел кабеля.
	 */
	private AbstractNode endNode;

	/**
	 * Сортированный список узлов, по которым проходит кабель.
	 */
	protected List sortedNodes = new LinkedList();
	/**
	 * Сортированный список фрагментов линий, по которым проходит кабель.
	 */
	protected List sortedNodeLinks = new LinkedList();
	/**
	 * Флаг сортировки фрагментов.
	 */
	protected boolean nodeLinksSorted = false;

	/**
	 * Список линий, по которым проходит кабель.
	 */
	protected List links = new LinkedList();
	/**
	 * Флаг сортировки линий.
	 */
	protected boolean linksSorted = false;
	
	/**
	 * Схемный кабель.
	 */
	protected SchemeCableLink schemeCableLink;

	/**
	 * Вид.
	 */
	protected MapView mapView;
	
	/**
	 * Объект привязки кабеля к линиям.
	 */
	protected CablePathBinding binding;

	/**
	 * Конструктор.
	 * @param schemeCableLink схемный кабель
	 * @param id идентификатор
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param mapView вид
	 */
	protected CablePath(
			SchemeCableLink schemeCableLink,
			Identifier id, 
			AbstractNode stNode, 
			AbstractNode eNode, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;

		this.schemeCableLink = schemeCableLink;
		this.name = schemeCableLink.name();

		if(mapView != null)
		{
			this.map = mapView.getMap();
		}

		this.startNode = stNode;
		this.endNode = eNode;
		this.nodeLinksSorted = false;

		this.binding = new CablePathBinding(this);
	}

	/**
	 * Создать новый элемент кабеля.
	 * @param schemeCableLink схемный кабель
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param mapView вид
	 * @return новый топологический кабель
	 * @throws com.syrus.AMFICOM.general.CreateObjectException при невозможности
	 * создания объекта.
	 */
	public static CablePath createInstance(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode, 
			AbstractNode eNode, 
			MapView mapView)
		throws CreateObjectException 
	{
		if (stNode == null || mapView == null || eNode == null || schemeCableLink == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE);
			return new CablePath(
				schemeCableLink,
				ide,
				stNode, 
				eNode, 
				mapView);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapCablePathElement.createInstance | cannot generate identifier ", e);
		}
		catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("MapCablePathElement.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * Get this.endNode.
	 * @return this.endNode
	 */
	public AbstractNode getEndNode() 
	{
		return this.endNode;
	}
	
	/**
	 * Set {@link #endNode}.
	 * @param endNode new endNode
	 */
	public void setEndNode(AbstractNode endNode) 
	{
		this.endNode = endNode;
		this.nodeLinksSorted = false;
	}
	
	/**
	 * Get {@link #startNode}.
	 * @return this.startNode
	 */
	public AbstractNode getStartNode() 
	{
		return this.startNode;
	}
	
	/**
	 * Set {@link #startNode}.
	 * @param startNode new startNode
	 */
	public void setStartNode(AbstractNode startNode) 
	{
		this.startNode = startNode;
		this.nodeLinksSorted = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List getCharacteristics() 
	{
		return Collections.unmodifiableList(this.schemeCableLink.characteristicsImpl().getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCharacteristic(Characteristic ch)
	{
		this.schemeCableLink.addCharacteristic(ch);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		this.schemeCableLink.removeCharacteristic(ch);
	}
	
	/**
	 * Set id.
	 * @param id id
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
	 * получить название.
	 * @return название
	 */
	public String getName() 
	{
		return this.name;
	}

	/**
	 * Установить название.
	 * @param name новое название
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
	 * @param description новое описание
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

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation()
	{
		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = this.getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink )it.next();
			DoublePoint an = link.getLocation();
			x += an.getX();
			y += an.getY();
			count ++;
		}
		x /= count;
		y /= count;
		
		point.setLocation(x, y);
		
		return point;
	}


	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setLocation(DoublePoint location)
	{
		throw new UnsupportedOperationException();
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
	 * Получить узел на другом конце топологического кабеля.
	 * @param node концевой узел
	 * @return другой концевой узел
	 */
	public AbstractNode getOtherNode(AbstractNode node)
	{
		if ( this.getEndNode().equals(node) )
			return this.getStartNode();
		if ( this.getStartNode().equals(node) )
			return this.getEndNode();
		return null;
	}

	/**
	 * Установить схемный кабель.
	 * @param schemeCableLink схемный кабель
	 */
	public void setSchemeCableLink(SchemeCableLink schemeCableLink)
	{
		this.schemeCableLink = schemeCableLink;
		this.setName(schemeCableLink.name());
	}

	/**
	 * получить схемный кабель.
	 * @return схемный кабель
	 */
	public SchemeCableLink getSchemeCableLink()
	{
		return this.schemeCableLink;
	}

	/**
	 * Возвращает топологическую длину в метрах.
	 * @return топологическая длина
	 */
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = this.getLinks().iterator();
		while( e.hasNext())
		{
			PhysicalLink link = (PhysicalLink)e.next();
			if(! (link instanceof UnboundLink))
				length = length + link.getLengthLt();
		}
		return length;
	}

	/**
	 * Возвращает физическую длину в метрах.
	 * @return физическая длина
	 */
	public double getLengthLf()
	{
		return this.schemeCableLink.physicalLength();
	}

	/**
	 * Возвращает оптическую длину в метрах.
	 * @return оптическая длина
	 */
	public double getLengthLo()
	{
		return this.schemeCableLink.opticalLength();
	}

	/**
	 * Установить физическую длину в метрах.
	 * @param len физическая длина
	 */
	public void setLengthLf(double len)
	{
		this.schemeCableLink.physicalLength(len);
	}

	/**
	 * Установить оптическую длину в метрах.
	 * @param len оптическая длина
	 */
	public void setLengthLo(double len)
	{
		this.schemeCableLink.opticalLength(len);
	}

	/**
	 * Возвращает коэффициент топологической привязки. Коэффициент связывает
	 * топологическую длину с физической.
	 * @return коэффициент топологической привязки
	 */
	public double getKd()
	{
		double phLen = this.schemeCableLink.physicalLength();
		if(phLen == 0.0D)
			return 1.0;
		double topLen = this.getLengthLt();
		if(topLen == 0.0D)
			return 1.0;

		double kd = phLen / topLen;
		return kd;
	}

	/**
	 * Получить список линий, по которым проходит кабель.
	 * @return список линий
	 */
	public List getLinks()
	{	
		return Collections.unmodifiableList(this.links);
	}

	/**
	 * Очистить список линий, по которым проходит кабель.
	 */
	public void clearLinks()
	{	
		this.links.clear();
		this.binding.clear();
	}

	/**
	 * Убрать линию из списка.
	 * <br>Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void removeLink(PhysicalLink link)
	{
		this.links.remove(link);
		this.getBinding().remove(link);
		this.nodeLinksSorted = false;
	}

	/**
	 * Добавить линию в список.
	 * <br>Внимание! концевые точки линии не обновляются
	 * @param addLink добавляемая линия
	 * @param cci оюъект, описывающий привязку кабеля к линии
	 */
	public void addLink(PhysicalLink addLink, CableChannelingItem cci)
	{
		this.links.add(addLink);
		this.binding.put(addLink, cci);
		this.linksSorted = false;
		this.nodeLinksSorted = false;
		this.sortLinks();
	}

	/**
	 * Сортировать список линий, по которым проходит кабель, начиная с 
	 * начального узла кабеля до конечного.
	 */
	public void sortLinks()
	{
		if(!this.linksSorted)
		{
			AbstractNode smne = this.getStartNode();
			List origvec = new LinkedList();
			origvec.addAll(this.getLinks());
			List vec = new LinkedList();
			int count = this.getLinks().size();
			for (int i = 0; i < count; i++) 
//			while(!smne.equals(this.getEndNode()))
			{
				boolean canSort = false;
				for(Iterator it = origvec.iterator(); it.hasNext();)
				{
					PhysicalLink link = (PhysicalLink)it.next();

					if(link.getStartNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getEndNode();
						CableChannelingItem cci = this.getBinding().getCCI(link);
						cci.startSiteNodeImpl((SiteNode )link.getStartNode());
						cci.endSiteNodeImpl((SiteNode )link.getEndNode());
						canSort = true;
						break;
					}
					else
					if(link.getEndNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getStartNode();
						CableChannelingItem cci = this.getBinding().getCCI(link);
						cci.startSiteNodeImpl((SiteNode )link.getEndNode());
						cci.endSiteNodeImpl((SiteNode )link.getStartNode());
						canSort = true;
						break;
					}
				}
				if(!canSort)
					// unconsistent - cannot sort
					return;
			}
			this.links = vec;
			this.linksSorted = true;
			this.nodeLinksSorted = false;
		}
	}

	/**
	 * Получить следующую линию по цепочке сортированных линий.
	 * @param link линия
	 * @return следующая линия, или <code>null</code>, если link - последняя 
	 * в списке
	 */
	public PhysicalLink nextLink(PhysicalLink link)
	{
		PhysicalLink ret = null;
		if(link == null)
		{
			if(this.getLinks().size() != 0)
				ret = (PhysicalLink)this.getLinks().get(0);
		}
		else
		{
			int index = this.getLinks().indexOf(link);
			if(index != this.getLinks().size() - 1 && index != -1)
				ret = (PhysicalLink)this.getLinks().get(index + 1);
		}
		return ret;
	}

	/**
	 * Получить предыдущую линию по цепочке сортированных линий.
	 * @param link линия
	 * @return предыдущая линия, или <code>null</code>, если link - первая 
	 * в списке
	 */
	public PhysicalLink previousLink(PhysicalLink link)
	{
		PhysicalLink ret = null;
		if(link == null)
		{
			if(this.getLinks().size() != 0)
				ret = (PhysicalLink)this.getLinks().get(this.getLinks().size() - 1);
		}
		else
		{
			int index = this.getLinks().indexOf(link);
			if(index > 0)
				ret = (PhysicalLink)this.getLinks().get(index - 1);
		}
		return ret;
	}

	/**
	 * Сортировать узлы, входящие в состав кабеля, начиная от начального узла.
	 * Сортировка узлов неразрывно связана с сортировкой фрагментов
	 */
	public void sortNodes()
	{
		this.sortNodeLinks();
	}
	
	/**
	 * Получить список отсортированных узлов, входящих в состав кабеля, начиная
	 * от начального узла линии. Сортировка узлов должна производиться явно 
	 * ({@link #sortNodes()}) до вызова этой функции
	 * @return список отсортированных узлов, или <code>Collections.EMPTY_LIST</code>, если
	 * узлы не отсортированы
	 */
	public java.util.List getSortedNodes()
	{
		if(!this.nodeLinksSorted)
			return Collections.EMPTY_LIST;
		return this.sortedNodes;
	}

	/**
	 * Получить список отсортированных фрагментов линий, входящих в состав 
	 * кабеля, начиная от начального узла линии. Сортировка узлов должна 
	 * производиться явно ({@link #sortNodes()}) до вызова этой функции
	 * @return список отсортированных фрагментов линий, или 
	 * <code>Collections.EMPTY_LIST</code>, если узлы не отсортированы
	 */
	public java.util.List getSortedNodeLinks()
	{
		if(!this.nodeLinksSorted)
			return Collections.EMPTY_LIST;
		return this.sortedNodeLinks;
	}

	/**
	 * Сортировать фрагменты линии по цепочке начиная от начального узла.
	 * При сортировке фрагментов сортируются также узлы
	 */
	public void sortNodeLinks()
	{
		this.sortLinks();
//		if(!nodeLinksSorted)
		{
			java.util.List pl = getLinks();

			List vec = new LinkedList();
			List nodevec = new LinkedList();

			AbstractNode node = getStartNode();

			for(Iterator it = pl.iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				
				link.sortNodeLinks();
				
				if(link.getStartNode().equals(node))
				{
					vec.addAll(link.getNodeLinks());
					nodevec.addAll(link.getSortedNodes());
				}
				else
				{
					for(ListIterator lit = link.getNodeLinks().listIterator(link.getNodeLinks().size()); lit.hasPrevious();)
					{
						vec.add(lit.previous());
					}
					for(ListIterator lit = link.getSortedNodes().listIterator(link.getSortedNodes().size()); lit.hasPrevious();)
					{
						nodevec.add(lit.previous());
					}
				}
				node = link.getOtherNode(node);

				// to avoid duplicate entry
				nodevec.remove(node);
			}
			// add last node
			nodevec.add(getEndNode());
				
			this.sortedNodeLinks = vec;
			this.sortedNodes = nodevec;
			this.nodeLinksSorted = true;
		}
	}

	/**
	 * Получить координаты прокладки кабелья по линии <code>link</code>.
	 * @param link линия
	 * @return координаты прокладки в тоннеле
	 */
	public IntPoint getBindingPosition(PhysicalLink link)
	{
		CableChannelingItem cci = (CableChannelingItem )getBinding().get(link);
		return new IntPoint(cci.rowX(), cci.placeY());
	}

	/**
	 * Установить объект, хранящий привязку кабеля к линиям.
	 * @param binding привязка
	 */
	public void setBinding(CablePathBinding binding)
	{
		this.binding = binding;
	}

	/**
	 * Получить объект, хранящий привязку кабеля к линиям.
	 * @return привязка
	 */
	public CablePathBinding getBinding()
	{
		return this.binding;
	}

	/**
	 * Получить из списка линий, входящих в кабельный путь, первый с начала узел
	 * первой по порядку непривязанной линии.
	 * @return узел
	 */
	public AbstractNode getStartUnboundNode()
	{
		AbstractNode bufferSite = getStartNode();
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}
	
	/**
	 * Получить из списка линий, входящих в кабельный путь, первый с конца узел
	 * последней по порядку непривязанной линии.
	 * @return узел
	 */
	public AbstractNode getEndUnboundNode()
	{
		AbstractNode bufferSite = getEndNode();
		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			PhysicalLink link = (PhysicalLink)it.previous();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}
	
	/**
	 * Получить из списка линий, входящих в кабельный путь, последнюю с начала
	 * привязанную линию.
	 * @return привязанная линия
	 */
	public PhysicalLink getStartLastBoundLink()
	{
		PhysicalLink link = null;

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link2 = (PhysicalLink)it.next();
			if(link2 instanceof UnboundLink)
				break;
			link = link2;
		}
		
		return link;
	}

	/**
	 * Получить из списка линий, входящих в кабельный путь, последнюю с конца
	 * привязанную линию.
	 * @return привязанная линия
	 */
	public PhysicalLink getEndLastBoundLink()
	{
		PhysicalLink link = null;

		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			PhysicalLink link2 = (PhysicalLink)it.previous();
			if(link2 instanceof UnboundLink)
				break;
			link = link2;
		}
		
		return link;
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
