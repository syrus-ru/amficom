/**
 * $Id: MeasurementPath.java,v 1.23 2005/04/28 09:08:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;

/**
 * Элемент пути.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.23 $, $Date: 2005/04/28 09:08:03 $
 * @module mapviewclient_v1
 */
public class MeasurementPath implements MapElement
{
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
	 * Узел карты, к которому привязан начальный узел кабеля.
	 */
	private transient AbstractNode startNode = null;

	/**
	 * Узел карты, к которому привязан конечный узел кабеля.
	 */
	private transient AbstractNode endNode = null;

	/**
	 * Схемный путь.
	 */
	protected SchemePath schemePath;

	/**
	 * Вид карты.
	 */
	protected MapView mapView;

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
	 * Конструктор.
	 * @param schemePath схемный путь
	 * @param id идентификатор
	 * @param mapView вид
	 */
	protected MeasurementPath(
			SchemePath schemePath,
			AbstractNode stNode, 
			AbstractNode eNode,
			MapView mapView) 
	{
		this.startNode = stNode;
		this.endNode = eNode;
		this.mapView = mapView;

		this.schemePath = schemePath;
	}

	/**
	 * Сохдать новый элемент пути.
	 * @param schemePath схемный путь
	 * @param mapView вид карты
	 * @return новый элемент пути
	 */
	public static MeasurementPath createInstance(
			SchemePath schemePath,
			AbstractNode stNode, 
			AbstractNode eNode,
			MapView mapView) 
	{
		if (mapView == null || stNode == null || eNode == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return new MeasurementPath(
			schemePath,
			stNode, 
			eNode,
			mapView);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set getCharacteristics() 
	{
		return this.schemePath.getCharacteristics();
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
	 * {@inheritDoc}
	 */
	public Identifier getId()
	{
		return this.schemePath.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() 
	{
		return this.schemePath.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) 
	{
		this.schemePath.setName(name);
	}

	/**
	 * Получить описание.
	 * @return описание
	 */
	public String getDescription() 
	{
		return this.schemePath.getDescription();
	}

	/**
	 * Установить описание.
	 * @param description описание
	 */
	public void setDescription(String description) 
	{
		this.schemePath.setDescription(description);
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
	 * Get this.endNode.
	 * @return this.endNode
	 */
	public AbstractNode getEndNode() 
	{
		return this.endNode;
//		return getMapView().getEndNode(this.getSchemePath());
	}
	
	/**
	 * Set {@link #endNode}.
	 * @param endNode new endNode
	 */
	public void setEndNode(AbstractNode endNode) 
	{
		this.endNode = endNode;
	}
	
	/**
	 * Get {@link #startNode}.
	 * @return this.startNode
	 */
	public AbstractNode getStartNode() 
	{
		return this.startNode;
//		return getMapView().getStartNode(this.getSchemePath());
	}
	
	/**
	 * Set {@link #startNode}.
	 * @param startNode new startNode
	 */
	public void setStartNode(AbstractNode startNode) 
	{
		this.startNode = startNode;
	}
	
	/**
	 * Установить схемный путь.
	 * @param schemePath схемный путь.
	 */
	public void setSchemePath(SchemePath schemePath)
	{
		this.schemePath = schemePath;
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
			Scheme scheme = this.schemePath.getScheme();

			this.unsortedCablePaths.clear();
			for(Iterator iter = this.schemePath.getPathElements().iterator(); iter.hasNext();) {
				PathElement pathElement = (PathElement )iter.next();
				switch(pathElement.getKind().value())
				{
					case Kind._SCHEME_ELEMENT:
						SchemeElement schemeElement = (SchemeElement )pathElement.getAbstractSchemeElement();
						SiteNode site = this.mapView.findElement(schemeElement);
						if(site != null)
						{
							//TODO think if link to 'site' is needed for mPath
		//					mPath.addCablePath(site);
						}
						break;
					case Kind._SCHEME_LINK:
						SchemeLink schemeLink = (SchemeLink )pathElement.getAbstractSchemeElement();
						SchemeElement startSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.getSourceSchemePort().getParentSchemeDevice());
						SchemeElement endSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme, schemeLink.getTargetSchemePort().getParentSchemeDevice());
						SiteNode startSiteNode = this.mapView.findElement(startSchemeElement);
						SiteNode endSiteNode = this.mapView.findElement(endSchemeElement);
						if(startSiteNode == endSiteNode)
						{
							//TODO think if link to 'link' is needed for mPath
		//					mPath.addCablePath(startSiteNode);
						}
						break;
					case Kind._SCHEME_CABLE_LINK:
						SchemeCableLink schemeCableLink = (SchemeCableLink )pathElement.getAbstractSchemeElement();
						CablePath cablePath = this.mapView.findCablePath(schemeCableLink);
						if(cablePath != null)
						{
							this.unsortedCablePaths.add(cablePath);
						}
						break;
					default:
						throw new UnsupportedOperationException("MeasurementPath.getCablePaths: Unknown path element kind: " + pathElement.getKind());
				}
			}
		}
		return Collections.unmodifiableList(this.unsortedCablePaths);
	}

	/**
	 * Get {@link #sortedNodeLinks}.
	 * @return this.sortedNodeLinks
	 */
	public List getSortedNodeLinks()
	{
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}
	
	/**
	 * Get {@link #sortedNodes}.
	 * @return this.sortedNodes
	 */
	public List getSortedNodes()
	{
		return Collections.unmodifiableList(this.sortedNodes);
	}
	
	/**
	 * Get {@link #sortedCablePaths}.
	 * @return this.sortedCablePaths
	 */
	public List getSortedCablePaths()
	{
		return Collections.unmodifiableList(this.sortedCablePaths);
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
				List reversedSortedNodeLinks = new ArrayList(cpath.getSortedNodeLinks());
				Collections.reverse(reversedSortedNodeLinks);
				for (int i = 0; i < reversedSortedNodeLinks.size(); i++)
					this.sortedNodeLinks.add(reversedSortedNodeLinks.get(i));
				List reversedSortedNodes = new ArrayList(cpath.getSortedNodes());
				Collections.reverse(reversedSortedNodes);
				for (int i = 0; i < reversedSortedNodes.size(); i++)
					this.sortedNodes.add(reversedSortedNodes.get(i));
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

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(Set characteristics) {
		this.schemePath.setCharacteristics(characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return this.schemePath.getCharacteristicSort();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(Set characteristics) {
		this.schemePath.setCharacteristics0(characteristics);
	}
}
