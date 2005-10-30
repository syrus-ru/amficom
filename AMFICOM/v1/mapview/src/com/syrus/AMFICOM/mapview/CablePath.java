/*-
 * $Id: CablePath.java,v 1.40 2005/10/30 14:49:09 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * Элемент кабельного пути. Описывает привязку кабеля к топологическим линиям.
 * 
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.40 $, $Date: 2005/10/30 14:49:09 $
 * @module mapview
 */
public final class CablePath implements MapElement {
	/**
	 * Флаг выделения.
	 */
	private transient boolean selected = false;

	/**
	 * Флаг наличия сигнала тревоги.
	 */
	private transient boolean alarmState = false;

	/**
	 * Флаг удаления.
	 */
	private transient boolean removed = false;

	/**
	 * Узел карты, к которому привязан начальный узел кабеля.
	 */
	private transient AbstractNode startNode = null;

	/**
	 * Узел карты, к которому привязан конечный узел кабеля.
	 */
	private transient AbstractNode endNode = null;

	/**
	 * Сортированный список узлов, по которым проходит кабель.
	 */
	private transient List<AbstractNode> sortedNodes = new LinkedList<AbstractNode>();
	/**
	 * Сортированный список фрагментов линий, по которым проходит кабель.
	 */
	private transient List<NodeLink> sortedNodeLinks = new LinkedList<NodeLink>();
	/**
	 * Флаг сортировки фрагментов.
	 */
	private transient boolean nodeLinksSorted = false;

	/**
	 * Список линий, по которым проходит кабель.
	 */
	private transient List<PhysicalLink> links = new LinkedList<PhysicalLink>();

	/**
	 * Схемный кабель.
	 */
	private transient SchemeCableLink schemeCableLink = null;

	/**
	 * Объект привязки кабеля к линиям.
	 */
	private transient HashMap<CableChannelingItem, PhysicalLink> binding = null;

	/**
	 * Конструктор.
	 * 
	 * @param schemeCableLink схемный кабель
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 */
	protected CablePath(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode,
			AbstractNode eNode) {
		this.schemeCableLink = schemeCableLink;

		this.startNode = stNode;
		this.endNode = eNode;
		this.nodeLinksSorted = false;

		this.binding = new HashMap<CableChannelingItem, PhysicalLink>();
	}

	/**
	 * Создать новый элемент кабеля.
	 * 
	 * @param schemeCableLink схемный кабель
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @return новый топологический кабель создания объекта.
	 */
	public static CablePath createInstance(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode,
			AbstractNode eNode) {
		if(stNode == null || eNode == null || schemeCableLink == null)
			throw new IllegalArgumentException("Argument is 'null'");

		return new CablePath(schemeCableLink, stNode, eNode);
	}

	/**
	 * Get this.endNode.
	 * 
	 * @return this.endNode
	 */
	public AbstractNode getEndNode() {
		return this.endNode;
	}

	/**
	 * Set {@link #endNode}.
	 * 
	 * @param endNode new endNode
	 */
	public void setEndNode(AbstractNode endNode) {
		this.endNode = endNode;
		this.nodeLinksSorted = false;
	}

	/**
	 * Get {@link #startNode}.
	 * 
	 * @return this.startNode
	 */
	public AbstractNode getStartNode() {
		return this.startNode;
	}

	/**
	 * Set {@link #startNode}.
	 * 
	 * @param startNode new startNode
	 */
	public void setStartNode(AbstractNode startNode) {
		this.startNode = startNode;
		this.nodeLinksSorted = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Identifier getId() {
		return this.schemeCableLink.getId();
	}

	/**
	 * получить название.
	 * 
	 * @return название
	 */
	public String getName() {
		return this.schemeCableLink.getName();
	}

	/**
	 * Установить название.
	 * 
	 * @param name новое название
	 */
	public void setName(String name) {
		this.schemeCableLink.setName(name);
	}

	/**
	 * Получить описание.
	 * 
	 * @return описание
	 */
	public String getDescription() {
		return this.schemeCableLink.getDescription();
	}

	/**
	 * Установить описание.
	 * 
	 * @param description новое описание
	 */
	public void setDescription(String description) {
		this.schemeCableLink.setDescription(description);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState) {
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState() {
		return this.alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		try {
			int count = 0;
			DoublePoint point = new DoublePoint(0.0, 0.0);
			double x = 0.0D;
			double y = 0.0D;
	
			for(Iterator it = this.getLinks().iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink) it.next();
				DoublePoint an = link.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
			x /= count;
			y /= count;
	
			point.setLocation(x, y);
	
			return point;
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			return null;
		}
	}

	/**
	 * Suppress since this class is transient
	 */
	public void setLocation(@SuppressWarnings("unused") DoublePoint location) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/**
	 * Получить узел на другом конце топологического кабеля.
	 * 
	 * @param node концевой узел
	 * @return другой концевой узел
	 */
	public AbstractNode getOtherNode(AbstractNode node) {
		if(this.endNode.equals(node))
			return this.startNode;
		if(this.startNode.equals(node))
			return this.endNode;
		return null;
	}

	/**
	 * Установить схемный кабель.
	 * 
	 * @param schemeCableLink схемный кабель
	 */
	public void setSchemeCableLink(SchemeCableLink schemeCableLink) {
		this.schemeCableLink = schemeCableLink;
	}

	/**
	 * получить схемный кабель.
	 * 
	 * @return схемный кабель
	 */
	public SchemeCableLink getSchemeCableLink() {
		return this.schemeCableLink;
	}

	/**
	 * Возвращает топологическую длину в метрах.
	 * 
	 * @return топологическая длина
	 * @throws ApplicationException
	 */
	public double getLengthLt() throws ApplicationException {
		double length = 0;
		Iterator e = this.getLinks().iterator();
		while(e.hasNext()) {
			PhysicalLink link = (PhysicalLink) e.next();
//			if(!(link instanceof UnboundLink))
				length = length + link.getLengthLt();
		}
		return length;
	}

	/**
	 * Возвращает физическую длину в метрах.
	 * 
	 * @return физическая длина
	 */
	public double getLengthLf() {
		return this.schemeCableLink.getPhysicalLength();
	}

	/**
	 * Возвращает оптическую длину в метрах.
	 * 
	 * @return оптическая длина
	 */
	public double getLengthLo() {
		return this.schemeCableLink.getOpticalLength();
	}

	/**
	 * Установить физическую длину в метрах.
	 * 
	 * @param len физическая длина
	 */
	public void setLengthLf(double len) {
		this.schemeCableLink.setPhysicalLength(len);
	}

	/**
	 * Установить оптическую длину в метрах.
	 * 
	 * @param len оптическая длина
	 */
	public void setLengthLo(double len) {
		this.schemeCableLink.setOpticalLength(len);
	}

	/**
	 * Возвращает коэффициент топологической привязки. Коэффициент связывает
	 * топологическую длину с физической.
	 * 
	 * @return коэффициент топологической привязки
	 * @throws ApplicationException
	 */
	public double getKd() throws ApplicationException {
		double phLen = this.schemeCableLink.getPhysicalLength();
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
	 * 
	 * @return список линий
	 * @throws ApplicationException
	 */
	public List<PhysicalLink> getLinks() throws ApplicationException {
		this.links.clear();
		for(CableChannelingItem cci : this.schemeCableLink.getPathMembers()) {
			this.links.add(this.binding.get(cci));
		}
		return Collections.unmodifiableList(this.links);
	}

	/**
	 * Очистить список линий, по которым проходит кабель.
	 */
	public void clearLinks() {
		this.links.clear();
		this.binding.clear();
	}

	/**
	 * Убрать линию из списка. <br>
	 * Внимание! концевые точки линии не обновляются.
	 */
	public void removeLink(final CableChannelingItem cci) {
		this.getBinding().remove(cci);
		this.nodeLinksSorted = false;
	}

	/**
	 * Добавить линию в список. <br>
	 * Внимание! концевые точки линии не обновляются
	 * 
	 * @param addLink добавляемая линия
	 * @param cci оюъект, описывающий привязку кабеля к линии
	 */
	public void addLink(
			final PhysicalLink addLink,
			final CableChannelingItem cci) {
		this.binding.put(cci, addLink);
		this.nodeLinksSorted = false;
	}

	private CableChannelingItem getElementAt(int index) throws ApplicationException {
		SortedSet<CableChannelingItem> cableChannelingItems = 
			this.schemeCableLink.getPathMembers();
		int count = cableChannelingItems.size();
		if(index < count && index >= 0) {
			int i = 0;
			for(CableChannelingItem nextCableChannelingItem : cableChannelingItems) {
				if(i == index)
					return nextCableChannelingItem;
				i++;
			}
		}
		return null;
	}

	/**
	 * Получить следующую линию по цепочке сортированных линий.
	 * 
	 * @return следующая линия, или <code>null</code>, если link - последняя
	 *         в списке
	 * @throws ApplicationException
	 */
	public CableChannelingItem nextLink(CableChannelingItem cableChannelingItem) throws ApplicationException {
		CableChannelingItem ret = null;
		if(cableChannelingItem == null) {
			SortedSet<CableChannelingItem> cableChannelingItems = 
				this.schemeCableLink.getPathMembers();
			ret = cableChannelingItems.first();
		}
		else {
			int index = cableChannelingItem.getSequentialNumber();
			ret = getElementAt(index + 1);
		}
		return ret;
	}

	/**
	 * Получить предыдущую линию по цепочке сортированных линий.
	 * 
	 * @param cableChannelingItem привязка линии
	 * @return предыдущая линия, или <code>null</code>, если link - первая в
	 *         списке
	 * @throws ApplicationException
	 */
	public CableChannelingItem previousLink(
			CableChannelingItem cableChannelingItem) throws ApplicationException {
		CableChannelingItem ret = null;
		if(cableChannelingItem == null) {
			SortedSet<CableChannelingItem> cableChannelingItems = 
				this.schemeCableLink.getPathMembers();
			ret = cableChannelingItems.last();
		}
		else {
			int index = cableChannelingItem.getSequentialNumber();
			ret = getElementAt(index - 1);
		}
		return ret;
	}

	/**
	 * Сортировать узлы, входящие в состав кабеля, начиная от начального узла.
	 * Сортировка узлов неразрывно связана с сортировкой фрагментов
	 * @throws ApplicationException
	 */
	public void sortNodes() throws ApplicationException {
		this.sortNodeLinks();
	}

	/**
	 * Получить список отсортированных узлов, входящих в состав кабеля, начиная
	 * от начального узла линии. Сортировка узлов должна производиться явно ({@link #sortNodes()})
	 * до вызова этой функции
	 * 
	 * @return список отсортированных узлов, или
	 *         <code>Collections.EMPTY_LIST</code>, если узлы не
	 *         отсортированы
	 */
	public List<AbstractNode> getSortedNodes() {
		if(!this.nodeLinksSorted) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.sortedNodes);
	}

	/**
	 * Получить список отсортированных фрагментов линий, входящих в состав
	 * кабеля, начиная от начального узла линии. Сортировка узлов должна
	 * производиться явно ({@link #sortNodes()}) до вызова этой функции
	 * 
	 * @return список отсортированных фрагментов линий, или
	 *         <code>Collections.EMPTY_LIST</code>, если узлы не
	 *         отсортированы
	 */
	public List<NodeLink> getSortedNodeLinks() {
		if(!this.nodeLinksSorted) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}

	/**
	 * Сортировать фрагменты линии по цепочке начиная от начального узла. При
	 * сортировке фрагментов сортируются также узлы
	 * @throws ApplicationException
	 */
	public void sortNodeLinks() throws ApplicationException {
		// if(!nodeLinksSorted)
		{
			List<NodeLink> list = new LinkedList<NodeLink>();
			List<AbstractNode> nodeList = new LinkedList<AbstractNode>();

			AbstractNode node = getStartNode();

			for(Iterator it = this.getLinks().iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink) it.next();

				link.sortNodeLinks();

				if(link.getStartNode().equals(node)) {
					list.addAll(link.getNodeLinks());
					nodeList.addAll(link.getSortedNodes());
				}
				else {
					final List<NodeLink> nodeLinks = link.getNodeLinks();
					final List<NodeLink> nodeLinksList = 
						new ArrayList<NodeLink>(nodeLinks);
					for(final ListIterator<NodeLink> listIterator = 
							nodeLinksList.listIterator(nodeLinksList.size()); 
							listIterator.hasPrevious();) {
						list.add(listIterator.previous());
					}
					List<AbstractNode> sortedNodes2 = link.getSortedNodes();
					for(final ListIterator<AbstractNode> listIterator = 
							sortedNodes2.listIterator(sortedNodes2.size()); 
							listIterator.hasPrevious();) {
						nodeList.add(listIterator.previous());
					}
				}
				node = link.getOtherNode(node);

				// to avoid duplicate entry
				nodeList.remove(node);
			}
			// add last node
			nodeList.add(getEndNode());

			this.sortedNodeLinks.clear();
			this.sortedNodeLinks.addAll(list);
			this.sortedNodes.clear();
			this.sortedNodes.addAll(nodeList);
			this.nodeLinksSorted = true;
		}
	}

	/**
	 * Получить объект, хранящий привязку кабеля к линиям.
	 * 
	 * @return привязка
	 */
	public HashMap<CableChannelingItem, PhysicalLink> getBinding() {
		return this.binding;
	}

	/**
	 * Получить из списка линий, входящих в кабельный путь, первый с начала узел
	 * первой по порядку непривязанной линии.
	 * 
	 * @return узел
	 * @throws ApplicationException
	 */
	public AbstractNode getStartUnboundNode() throws ApplicationException {
		AbstractNode bufferSite = getStartNode();
		for(Iterator it = getLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink) it.next();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}

	/**
	 * Получить из списка линий, входящих в кабельный путь, первый с конца узел
	 * последней по порядку непривязанной линии.
	 * 
	 * @return узел
	 * @throws ApplicationException
	 */
	public AbstractNode getEndUnboundNode() throws ApplicationException {
		AbstractNode bufferSite = getEndNode();
		List<PhysicalLink> thislinks = this.getLinks();
		for(ListIterator it = thislinks.listIterator(thislinks.size()); 
				it.hasPrevious();) {
			PhysicalLink link = (PhysicalLink) it.previous();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;

	}

	public CableChannelingItem getFirstCCI(PhysicalLink physicalLink) throws ApplicationException {
		for(CableChannelingItem cci2 : this.schemeCableLink.getPathMembers()) {
			if(this.binding.get(cci2).equals(physicalLink))
				return cci2;
		}
		return null;
	}

	/**
	 * Получить из списка линий, входящих в кабельный путь, последнюю с начала
	 * привязанную линию.
	 * 
	 * @return привязанная линия
	 * @throws ApplicationException
	 */
	public CableChannelingItem getStartLastBoundLink() throws ApplicationException {
		CableChannelingItem cci = null;
		for(Iterator it = this.schemeCableLink.getPathMembers().iterator(); 
				it.hasNext();) {
			CableChannelingItem cci2 = (CableChannelingItem) it.next();
			if(cci2.getPhysicalLink() == null)
				break;
			cci = cci2;
		}
		return cci;
	}

	/**
	 * Получить из списка линий, входящих в кабельный путь, последнюю с конца
	 * привязанную линию.
	 * 
	 * @return привязанная линия
	 * @throws ApplicationException
	 */
	public CableChannelingItem getEndLastBoundLink() throws ApplicationException {
		CableChannelingItem cci = null;
		for(Iterator it = this.schemeCableLink.getPathMembers().iterator(); 
				it.hasNext();) {
			CableChannelingItem cci2 = (CableChannelingItem) it.next();
			if(cci == null)
				cci = cci2;
			if(cci2.getPhysicalLink() == null)
				cci = null;
		}
		return cci;
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void revert(MapElementState state) {
		throw new UnsupportedOperationException();
	}

	public Characterizable getCharacterizable() {
		return this.schemeCableLink;
	}

}
