/**
 * $Id: PhysicalLink.java,v 1.38 2005/04/01 11:40:53 bob Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;

/**
 * Линия топологический схемы. Линия имеет начальный и конечный узлы, 
 * внутренние топологические узлы (список не хранится явно) и состоит из
 * фрагментов. Фрагменты образуют цепочку. Последовательность фрагментов 
 * не хранится. Линия характеризуется типом (<code>{@link PhysicalLinkType}</code>). 
 * Предуствновленными являются  два типа - 
 * тоннель (<code>{@link PhysicalLinkType#TUNNEL}</code>) 
 * и коллектор (<code>{@link PhysicalLinkType#COLLECTOR}</code>).
 * @author $Author: bob $
 * @version $Revision: 1.38 $, $Date: 2005/04/01 11:40:53 $
 * @module map_v1
 * @todo make binding.dimension persistent (just as bindingDimension for PhysicalLinkType)
 * @todo nodeLinks should be transient
 */
public class PhysicalLink extends StorableObject implements TypedObject, MapElement {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4121409622671570743L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PROTO_ID = "proto_id";
	public static final String COLUMN_START_NODE_ID = "start_node_id";
	public static final String COLUMN_END_NODE_ID = "end_node_id";
	public static final String COLUMN_NODE_LINKS = "node_links";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_STREET = "street";
	public static final String COLUMN_BUILDING = "building";	

	/**
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	private String name;
	private String description;

	private PhysicalLinkType physicalLinkType;

	private AbstractNode startNode;
	private AbstractNode endNode;
	private String city;
	private String street;
	private String building;

	private int dimensionX;
	private int dimensionY;

	private boolean leftToRight;
	private boolean topToBottom;

	private Set characteristics;

	private StorableObjectDatabase physicalLinkDatabase;

	private transient SortedSet nodeLinks = null;
	protected transient Map map;
	protected transient boolean selected = false;
	protected transient boolean selectionVisible = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;
	protected transient PhysicalLinkBinding binding;
	protected transient List sortedNodes = new LinkedList();
	protected transient boolean nodeLinksSorted = false;

	public PhysicalLink(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.physicalLinkDatabase = MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			this.physicalLinkDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLink(PhysicalLink_Transferable plt) throws CreateObjectException {
		super(plt.header);
		this.name = plt.name;
		this.description = plt.description;

		this.city = plt.city;
		this.street = plt.street;
		this.building = plt.building;
		this.dimensionX = plt.dimensionX;
		this.dimensionY = plt.dimensionY;
		this.leftToRight = plt.leftToRight;
		this.topToBottom = plt.topToBottom;

		try {
			this.physicalLinkType = (PhysicalLinkType) MapStorableObjectPool.getStorableObject(new Identifier(plt.physicalLinkTypeId),
					true);

			this.startNode = (AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier(plt.startNodeId), true);
			this.endNode = (AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier(plt.endNodeId), true);

			this.nodeLinks = new TreeSet();
			Set nodeLinksIds = new HashSet(plt.nodeLinkIds.length);
			for (int i = 0; i < plt.nodeLinkIds.length; i++)
				nodeLinksIds.add(new Identifier(plt.nodeLinkIds[i]));
			this.nodeLinks.addAll(MapStorableObjectPool.getStorableObjects(nodeLinksIds, true));

			this.characteristics = new HashSet(plt.characteristicIds.length);
			Set characteristicIds = new HashSet(plt.characteristicIds.length);
			for (int i = 0; i < plt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(plt.characteristicIds[i]));
			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.selected = false;

		this.binding = new PhysicalLinkBinding(new IntDimension(this.dimensionX, this.dimensionY));
	}

	protected PhysicalLink(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String name,
			final String description,
			final PhysicalLinkType physicalLinkType,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final String city,
			final String street,
			final String building,
			final int dimensionX,
			final int dimensionY,
			final boolean leftToRight,
			final boolean topToBottom) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.name = name;
		this.description = description;
		this.physicalLinkType = physicalLinkType;
		this.startNode = startNode;
		this.endNode = endNode;
		this.city = city;
		this.street = street;
		this.building = building;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;

		this.characteristics = new HashSet();
		this.nodeLinks = new TreeSet();

		this.physicalLinkDatabase = MapDatabaseContext.getPhysicalLinkDatabase();

		this.selected = false;

		if (physicalLinkType == null)
			this.binding = new PhysicalLinkBinding(new IntDimension(0, 0));
		else
			this.binding = new PhysicalLinkBinding(physicalLinkType.getBindingDimension());
	}

	public static PhysicalLink createInstance(final Identifier creatorId,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final PhysicalLinkType type) throws CreateObjectException {

		return PhysicalLink.createInstance(creatorId,
				"",
				"",
				type,
				stNode,
				eNode,
				"",
				"",
				"",
				type.getBindingDimension().getWidth(),
				type.getBindingDimension().getHeight(),
				true,
				true);
	}

	public static PhysicalLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final PhysicalLinkType physicalLinkType,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final String city,
			final String street,
			final String building,
			final int dimensionX,
			final int dimensionY,
			final boolean leftToRight,
			final boolean topToBottom) throws CreateObjectException {

		if (creatorId == null
				|| name == null
				|| description == null
				|| physicalLinkType == null
				|| startNode == null
				|| endNode == null
				|| city == null
				|| street == null
				|| building == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLink physicalLink = new PhysicalLink(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE),
					creatorId,
					0L,
					name,
					description,
					physicalLinkType,
					startNode,
					endNode,
					city,
					street,
					building,
					dimensionX,
					dimensionY,
					leftToRight,
					topToBottom);
			physicalLink.changed = true;
			return physicalLink;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("PhysicalLink.createInstance | cannot generate identifier ", e);
		}
	}

	public Set getDependencies() {
		return Collections.singleton(this.physicalLinkType);
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		i = 0;
		Identifier_Transferable[] nodeLinkIds = new Identifier_Transferable[this.nodeLinks.size()];
		for (Iterator iterator = this.nodeLinks.iterator(); iterator.hasNext();)
			nodeLinkIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new PhysicalLink_Transferable(super.getHeaderTransferable(),
				this.name,
				this.description,
				(Identifier_Transferable) this.physicalLinkType.getId().getTransferable(),
				(Identifier_Transferable) this.startNode.getId().getTransferable(),
				(Identifier_Transferable) this.endNode.getId().getTransferable(),
				this.city,
				this.street,
				this.building,
				this.dimensionX,
				this.dimensionY,
				this.leftToRight,
				this.topToBottom,
				nodeLinkIds,
				charIds);
	}

	public StorableObjectType getType() {
		return this.physicalLinkType;
	}

	public void setType(StorableObjectType type) {
		this.physicalLinkType = (PhysicalLinkType) type;
		this.changed = true;
	}

	public String getBuilding() {
		return this.building;
	}

	protected void setBuilding0(String building) {
		this.building = building;
	}

	public void setBuilding(String building) {
		this.setBuilding0(building);
		this.changed = true;
	}

	public String getCity() {
		return this.city;
	}

	protected void setCity0(String city) {
		this.city = city;
	}

	public void setCity(String city) {
		this.setCity0(city);
		this.changed = true;
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription0(String description) {
		this.description = description;
	}

	public void setDescription(String description) {
		this.setDescription0(description);
		this.changed = true;
	}

	public int getDimensionX() {
		return this.dimensionX;
	}

	public void setDimensionX(int dimensionX) {
		this.dimensionX = dimensionX;
		this.changed = true;
	}

	public int getDimensionY() {
		return this.dimensionY;
	}

	public void setDimensionY(int dimensionY) {
		this.dimensionY = dimensionY;
		this.changed = true;
	}

	public AbstractNode getEndNode() {
		return this.endNode;
	}

	protected void setEndNode0(AbstractNode endNode) {
		this.endNode = endNode;
		this.nodeLinksSorted = false;
	}

	public void setEndNode(AbstractNode endNode) {
		this.setEndNode0(endNode);
		this.changed = true;
	}

	public boolean isLeftToRight() {
		return this.leftToRight;
	}

	protected void setLeftToRight0(boolean leftToRight) {
		this.leftToRight = leftToRight;
	}

	public void setLeftToRight(boolean leftToRight) {
		this.setLeftToRight0(leftToRight);
		this.changed = true;
	}

	public String getName() {
		if (this.name.length() == 0)
			return this.startNode.getName() + " -- " + this.endNode.getName();
		return this.name;
	}

	public void setPhysicalLinkType(PhysicalLinkType physicalLinkType) {
		this.physicalLinkType = physicalLinkType;
		this.changed = true;
	}

	protected void setName0(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.setName0(name);
		this.changed = true;
	}

	public SortedSet getNodeLinks() {
		if (this.nodeLinks == null || this.nodeLinks.isEmpty())
			this.nodeLinks = findNodeLinks();
		return (SortedSet) Collections.unmodifiableSet(this.nodeLinks);
	}

	public void setNodeLinks(final List nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null)
			this.nodeLinks.addAll(nodeLinks);
		this.nodeLinksSorted = false;
		this.changed = true;
	}

	public AbstractNode getStartNode() {
		return this.startNode;
	}

	protected void setStartNode0(AbstractNode startNode) {
		this.startNode = startNode;
		this.nodeLinksSorted = false;

	}

	public void setStartNode(AbstractNode startNode) {
		this.setStartNode0(startNode);
		this.changed = true;
	}

	public String getStreet() {
		return this.street;
	}	

	protected void setStreet0(String street) {
		this.street = street;
	}

	public void setStreet(String street) {
		this.setStreet0(street);
		this.changed = true;
	}

	public boolean isTopToBottom() {
		return this.topToBottom;
	}

	protected void setTopToBottom0(boolean topToBottom) {
		this.topToBottom = topToBottom;
	}

	public void setTopToBottom(boolean topToBottom) {
		this.setTopToBottom0(topToBottom);
		this.changed = true;
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String name,
			String description,
			PhysicalLinkType physicalLinkType,
			String city,
			String street,
			String building,
			int dimensionX,
			int dimensionY,
			boolean leftToRight,
			boolean topToBottom,
			AbstractNode startNode,
			AbstractNode endNode) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.name = name;
		this.description = description;
		this.physicalLinkType = physicalLinkType;
		this.city = city;
		this.street = street;
		this.building = building;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	/**
	 * Получить объект, описывающий привязку кабелей
	 * (com.syrus.AMFICOM.scheme.corba.SchemeCableLink) к линии.
	 * 
	 * @return привязка
	 */
	public PhysicalLinkBinding getBinding() {
		return this.binding;
	}

	/**
	 * Возвращяет топологическую длинну линии, которая представляет собой
	 * суммарную длину фрагментов, составляющих линию
	 * 
	 * @return топологическая длинна линии
	 */
	public double getLengthLt() {
		double returnValue = 0;
		for (Iterator it = getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nodeLink = (NodeLink) it.next();
			returnValue += nodeLink.getLengthLt();
		}
		return returnValue;
	}

	/**
	 * Убрать фрагмент из состава линии. Внимание! концевые точки линии не
	 * обновляются.
	 * 
	 * @param nodeLink
	 *          фрагмент линии
	 */
	public void removeNodeLink(NodeLink nodeLink) {
		this.nodeLinks.remove(nodeLink);
		this.nodeLinksSorted = false;
		this.changed = true;
	}

	/**
	 * Добавить фрагмент в состав линии. Внимание! концевые точки линии не
	 * обновляются.
	 * 
	 * @param addNodeLink
	 *          фрагмент линии
	 */
	public void addNodeLink(NodeLink addNodeLink) {
		this.nodeLinks.add(addNodeLink);
		this.nodeLinksSorted = false;
		this.changed = true;
	}

	/**
	 * Убрать все фрагмента из состава линии.
	 */
	public void clearNodeLinks() {
		this.nodeLinks.clear();
		this.nodeLinksSorted = false;
		this.changed = true;
	}

	/**
	 * Получить список фрагментов этой линии, имеющие заданный узел концевым.
	 * Список включает не более двух фрагментов (поскольку фрагменты образуют
	 * цепочку).
	 * 
	 * @param node
	 *          узел
	 * @return список фрагментов
	 */
	public java.util.List getNodeLinksAt(AbstractNode node) {
		LinkedList returnNodeLink = new LinkedList();
		Iterator e = getNodeLinks().iterator();

		while (e.hasNext()) {
			NodeLink nodeLink = (NodeLink) e.next();
			if ((nodeLink.getEndNode().equals(node)) || (nodeLink.getStartNode().equals(node)))
				returnNodeLink.add(nodeLink);
		}
		return returnNodeLink;
	}

	/**
	 * Получить первый фрагмент в цепочке фрагментов, из которых строится линия.
	 * Первый фрагмент определяется начальным узлом линии.
	 * 
	 * @return первый фрагмент, или <code>null</code>, если в линии не найден
	 *         фрагмент, вклучающий начальный узел линии
	 */
	public NodeLink getStartNodeLink() {
		NodeLink startNodeLink = null;

		for (Iterator it = getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nodeLink = (NodeLink) it.next();
			if (nodeLink.getStartNode().equals(getStartNode()) || nodeLink.getEndNode().equals(getStartNode())) {
				startNodeLink = nodeLink;
				break;
			}
		}
		return startNodeLink;
	}

	/**
	 * Сортировать узлы, входящие в состав линии, начиная от начального узла.
	 * Сортировка узлов неразрывно связана с сортировкой фрагментов
	 */
	public void sortNodes() {
		this.sortNodeLinks();
	}

	/**
	 * Получить список отсортированных узлов, входящих в состав линии, начиная от
	 * начального узла линии. Сортировка узлов должна производиться явно (
	 * {@link #sortNodes()}) до вызова этой функции
	 * 
	 * @return список отсортированных узлов, или <code>null</code>, если узлы
	 *         не отсортированы
	 */
	public List getSortedNodes() {
		if (!this.nodeLinksSorted)
			return Collections.EMPTY_LIST;
		return this.sortedNodes;
	}

	/**
	 * Сортировать фрагменты линии по цепочке начиная от начального узла. При
	 * сортировке фрагментов сортируются также узлы
	 * 
	 * @todo getNodeLinks() is unmodifiable so iterator.remove throws Ex
	 */
	public void sortNodeLinks() {
		if (!this.nodeLinksSorted) {
			AbstractNode smne = this.getStartNode();
			NodeLink nl = null;
			LinkedList list = new LinkedList();
			List nodeList = new LinkedList();
			int count = getNodeLinks().size();
			for (int i = 0; i < count; i++) {
				nodeList.add(smne);

				for (Iterator it = getNodeLinks().iterator(); it.hasNext();) {
					NodeLink nodeLink = (NodeLink) it.next();

					if (!nodeLink.equals(nl)) {
						if (nodeLink.getStartNode().equals(smne)) {
							list.add(nodeLink);
							it.remove();
							smne = nodeLink.getEndNode();
							nl = nodeLink;
							break;
						}
						else
							if (nodeLink.getEndNode().equals(smne)) {
								list.add(nodeLink);
								it.remove();
								smne = nodeLink.getStartNode();
								nl = nodeLink;
								break;
							}
					}
				}
			}
			nodeList.add(this.getEndNode());
			this.nodeLinks.clear();
			this.nodeLinks.addAll(list);
			this.nodeLinksSorted = true;
			this.sortedNodes = nodeList;
		}
	}

	/**
	 * Получить следующий фрагмент по цепочке сортированных фрагментов.
	 * 
	 * @param nodeLink
	 *          фрагмент
	 * @return следующий фрагмент, или <code>null</code>, если nl - последний в
	 *         списке
	 */
	public NodeLink nextNodeLink(NodeLink nodeLink) {
		this.sortNodeLinks();
		final SortedSet nodeLinksTailSet  = this.nodeLinks.tailSet(nodeLink);
		if (nodeLinksTailSet.size() == 1)
			return null;
		final Iterator nodeLinksIterator = nodeLinksTailSet.iterator();
		nodeLinksIterator.next();
		return (NodeLink) nodeLinksIterator.next();
	}

	/**
	 * Получить предыдущий фрагмент по цепочке сортированных фрагментов.
	 * 
	 * @param nodeLink
	 *          фрагмент
	 * @return предыдущий фрагмент, или <code>null</code>, если nl - первый в
	 *         списке
	 */
	public NodeLink previousNodeLink(NodeLink nodeLink) {
		this.sortNodeLinks();
		final SortedSet nodeLinksHeadSet = this.nodeLinks.headSet(nodeLink); 
		return nodeLinksHeadSet.isEmpty() ? null : (NodeLink) nodeLinksHeadSet.last();
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
	public void setAlarmState(boolean alarmState) {
		this.alarmState = alarmState;
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
		getMap().setSelected(this, selected);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map getMap() {
		return this.map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * Получить другой конечный узел линии для заданного конечного узла.
	 * 
	 * @param node
	 *          узел
	 * @return другой конечный узел
	 */
	public AbstractNode getOtherNode(AbstractNode node) {
		if (this.getEndNode().equals(node))
			return getStartNode();
		if (this.getStartNode().equals(node))
			return getEndNode();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		int count = 0;
		double x = 0.0;
		double y = 0.0;
		DoublePoint point = new DoublePoint(0.0, 0.0);

		for (Iterator it = getNodeLinks().iterator(); it.hasNext();) {
			NodeLink mnle = (NodeLink) it.next();
			DoublePoint an = mnle.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}
		if (count > 0) {
			x /= count;
			y /= count;
			point.setLocation(x, y);
		}

		return point;
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		return new PhysicalLinkState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(MapElementState state) {
		PhysicalLinkState mples = (PhysicalLinkState) state;

		this.setName(mples.name);
		this.setDescription(mples.description);
		this.setStartNode(mples.startNode);
		this.setEndNode(mples.endNode);

		this.nodeLinks = new TreeSet();
		for (Iterator it = mples.nodeLinks.iterator(); it.hasNext();) {
			NodeLink mnle = (NodeLink) it.next();
			mnle.setPhysicalLink(this);
			this.nodeLinks.add(mnle);
		}
		try {
			setType((PhysicalLinkType) (MapStorableObjectPool.getStorableObject(mples.mapProtoId, true)));
		}
		catch (ApplicationException e) {
			e.printStackTrace();
		}

		this.nodeLinksSorted = false;
	}

	public void setSelectionVisible(boolean selectionVisible) {
		this.selectionVisible = selectionVisible;
	}

	public boolean isSelectionVisible() {
		return this.selectionVisible;
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			exportMap.put(COLUMN_PROTO_ID, this.physicalLinkType.getCodename());
			exportMap.put(COLUMN_START_NODE_ID, this.startNode.getId());
			exportMap.put(COLUMN_END_NODE_ID, this.endNode.getId());
			exportMap.put(COLUMN_CITY, this.city);
			exportMap.put(COLUMN_STREET, this.street);
			exportMap.put(COLUMN_BUILDING, this.building);
			List nodeLinkIds = new ArrayList(getNodeLinks().size());
			for (Iterator it = getNodeLinks().iterator(); it.hasNext();) {
				NodeLink nodeLink = (NodeLink) it.next();
				nodeLinkIds.add(nodeLink.getId());
			}
			exportMap.put(COLUMN_NODE_LINKS, nodeLinkIds);
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static PhysicalLink createInstance(Identifier creatorId, java.util.Map exportMap1) throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		String description1 = (String) exportMap1.get(COLUMN_DESCRIPTION);
		String typeCodeName1 = (String) exportMap1.get(COLUMN_PROTO_ID);
		Identifier startNodeId1 = (Identifier) exportMap1.get(COLUMN_START_NODE_ID);
		Identifier endNodeId1 = (Identifier) exportMap1.get(COLUMN_END_NODE_ID);
		String city1 = (String) exportMap1.get(COLUMN_CITY);
		String street1 = (String) exportMap1.get(COLUMN_STREET);
		String building1 = (String) exportMap1.get(COLUMN_BUILDING);

		if (id1 == null
				|| creatorId == null
				|| name1 == null
				|| description1 == null
				|| startNodeId1 == null
				|| endNodeId1 == null
				|| city1 == null
				|| street1 == null
				|| building1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLinkType physicalLinkType1;

			TypicalCondition condition = new TypicalCondition(typeCodeName1,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);

			Collection collection = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (collection == null || collection.size() == 0) {
				typeCodeName1 = PhysicalLinkType.TUNNEL;

				condition.setValue(typeCodeName1);

				collection = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (collection == null || collection.size() == 0) {
					throw new CreateObjectException("PhysicalLinkType \'" + PhysicalLinkType.TUNNEL + "\' not found");
				}
			}
			physicalLinkType1 = (PhysicalLinkType) collection.iterator().next();

			AbstractNode startNode1 = (AbstractNode) MapStorableObjectPool.getStorableObject(startNodeId1, true);
			AbstractNode endNode1 = (AbstractNode) MapStorableObjectPool.getStorableObject(endNodeId1, true);
			PhysicalLink link1 = new PhysicalLink(id1,
					creatorId,
					0L,
					name1,
					description1,
					physicalLinkType1,
					startNode1,
					endNode1,
					city1,
					street1,
					building1,
					physicalLinkType1.getBindingDimension().getWidth(),
					physicalLinkType1.getBindingDimension().getHeight(),
					true,
					true);

			return link1;
		}
		catch (ApplicationException e) {
			throw new CreateObjectException("PhysicalLink.createInstance |  ", e);
		}
	}

	private SortedSet findNodeLinks() {
		return this.map.getNodeLinks(this);
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void addCharacteristic(Characteristic characteristic) {
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	public void removeCharacteristic(Characteristic characteristic) {
		this.characteristics.remove(characteristic);
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		this.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_PHYSICAL_LINK;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
}
