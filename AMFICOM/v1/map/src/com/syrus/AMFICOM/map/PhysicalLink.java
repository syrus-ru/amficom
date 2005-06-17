/*-
 * $Id: PhysicalLink.java,v 1.63 2005/06/17 11:01:12 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;

/**
 * Линия топологический схемы. Линия имеет начальный и конечный узлы,
 * внутренние топологические узлы (список не хранится явно) и состоит из
 * фрагментов. Фрагменты образуют цепочку. Последовательность фрагментов
 * не хранится. Линия характеризуется типом (<code>{@link PhysicalLinkType}</code>).
 * Предуствновленными являются  два типа -
 * тоннель (<code>{@link PhysicalLinkType#DEFAULT_TUNNEL}</code>)
 * и коллектор (<code>{@link PhysicalLinkType#DEFAULT_COLLECTOR}</code>).
 * @author $Author: bass $
 * @version $Revision: 1.63 $, $Date: 2005/06/17 11:01:12 $
 * @module map_v1
 * @todo make binding.dimension persistent (just as bindingDimension for PhysicalLinkType)
 * @todo nodeLinks should be transient
 */
public class PhysicalLink extends StorableObject implements TypedObject, MapElement, XMLBeansTransferable {

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

	private transient List nodeLinks = null;
	protected transient boolean selected = false;
	protected transient boolean selectionVisible = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;
	protected transient PhysicalLinkBinding binding = null;
	protected transient List sortedNodes = new LinkedList();
	protected transient boolean nodeLinksSorted = false;

	PhysicalLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		PhysicalLinkDatabase database = (PhysicalLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICALLINK_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	PhysicalLink(final PhysicalLink_Transferable plt) throws CreateObjectException {
		try {
			this.fromTransferable(plt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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
		this.nodeLinks = new ArrayList();

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
			PhysicalLink physicalLink = new PhysicalLink(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICALLINK_CODE),
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

			assert physicalLink.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			physicalLink.markAsChanged();

			return physicalLink;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		PhysicalLink_Transferable plt = (PhysicalLink_Transferable) transferable;
		super.fromTransferable(plt.header);

		this.name = plt.name;
		this.description = plt.description;

		this.city = plt.city;
		this.street = plt.street;
		this.building = plt.building;
		this.dimensionX = plt.dimensionX;
		this.dimensionY = plt.dimensionY;
		this.leftToRight = plt.leftToRight;
		this.topToBottom = plt.topToBottom;

		this.physicalLinkType = (PhysicalLinkType) StorableObjectPool.getStorableObject(new Identifier(plt.physicalLinkTypeId), true);

		this.startNode = (AbstractNode) StorableObjectPool.getStorableObject(new Identifier(plt.startNodeId), true);
		this.endNode = (AbstractNode) StorableObjectPool.getStorableObject(new Identifier(plt.endNodeId), true);

		Set ids = Identifier.fromTransferables(plt.characteristicIds);
		this.characteristics = StorableObjectPool.getStorableObjects(ids, true);

		this.selected = false;

		this.binding = new PhysicalLinkBinding(new IntDimension(this.dimensionX, this.dimensionY));
	}

	public Set getDependencies() {
		return Collections.singleton(this.physicalLinkType);
	}

	public IDLEntity getTransferable() {
		Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);
		Identifier_Transferable[] nodeLinkIds = new Identifier_Transferable[0];

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

	public void setType(final StorableObjectType type) {
		this.physicalLinkType = (PhysicalLinkType) type;
		super.markAsChanged();
	}

	public String getBuilding() {
		return this.building;
	}

	protected void setBuilding0(String building) {
		this.building = building;
	}

	public void setBuilding(final String building) {
		this.setBuilding0(building);
		super.markAsChanged();
	}

	public String getCity() {
		return this.city;
	}

	protected void setCity0(final String city) {
		this.city = city;
	}

	public void setCity(final String city) {
		this.setCity0(city);
		super.markAsChanged();
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription0(final String description) {
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}

	public int getDimensionX() {
		return this.dimensionX;
	}

	public void setDimensionX(final int dimensionX) {
		this.dimensionX = dimensionX;
		super.markAsChanged();
	}

	public int getDimensionY() {
		return this.dimensionY;
	}

	public void setDimensionY(final int dimensionY) {
		this.dimensionY = dimensionY;
		super.markAsChanged();
	}

	public AbstractNode getEndNode() {
		return this.endNode;
	}

	protected void setEndNode0(final AbstractNode endNode) {
		this.endNode = endNode;
		this.nodeLinksSorted = false;
	}

	public void setEndNode(final AbstractNode endNode) {
		this.setEndNode0(endNode);
		super.markAsChanged();
	}

	public boolean isLeftToRight() {
		return this.leftToRight;
	}

	protected void setLeftToRight0(final boolean leftToRight) {
		this.leftToRight = leftToRight;
	}

	public void setLeftToRight(final boolean leftToRight) {
		this.setLeftToRight0(leftToRight);
		super.markAsChanged();
	}

	public String getName() {
		if (this.name.length() == 0)
			return this.startNode.getName() + " -- " + this.endNode.getName();
		return this.name;
	}

	public void setPhysicalLinkType(final PhysicalLinkType physicalLinkType) {
		this.physicalLinkType = physicalLinkType;
		super.markAsChanged();
	}

	protected void setName0(final String name) {
		this.name = name;
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}

	public List getNodeLinks() {
		if (this.nodeLinks == null || this.nodeLinks.isEmpty())
			this.nodeLinks = findNodeLinks();
		return Collections.unmodifiableList(this.nodeLinks);
	}

	private List findNodeLinks() {
		try {
			StorableObjectCondition condition = new LinkedIdsCondition(this.getId(), ObjectEntities.NODELINK_CODE);
			Set nlinks;

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			nlinks = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			List nlinkslist = new ArrayList(nlinks.size());
			for(Iterator iter = nlinks.iterator(); iter.hasNext();) {
				nlinkslist.add(iter.next());
			}
			return nlinkslist;
		} catch(ApplicationException e) {
			// TODO how to work it over?!
			e.printStackTrace();
		}
		return new LinkedList();
	}

	public void setNodeLinks(final List nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null)
			this.nodeLinks.addAll(nodeLinks);
		this.nodeLinksSorted = false;
		super.markAsChanged();
	}

	public AbstractNode getStartNode() {
		return this.startNode;
	}

	protected void setStartNode0(final AbstractNode startNode) {
		this.startNode = startNode;
		this.nodeLinksSorted = false;

	}

	public void setStartNode(final AbstractNode startNode) {
		this.setStartNode0(startNode);
		super.markAsChanged();
	}

	public String getStreet() {
		return this.street;
	}	

	protected void setStreet0(final String street) {
		this.street = street;
	}

	public void setStreet(final String street) {
		this.setStreet0(street);
		super.markAsChanged();
	}

	public boolean isTopToBottom() {
		return this.topToBottom;
	}

	protected void setTopToBottom0(final boolean topToBottom) {
		this.topToBottom = topToBottom;
	}

	public void setTopToBottom(final boolean topToBottom) {
		this.setTopToBottom0(topToBottom);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String name,
			final String description,
			final PhysicalLinkType physicalLinkType,
			final String city,
			final String street,
			final String building,
			final int dimensionX,
			final int dimensionY,
			final boolean leftToRight,
			final boolean topToBottom,
			final AbstractNode startNode,
			final AbstractNode endNode) {
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
	public void removeNodeLink(final NodeLink nodeLink) {
		this.nodeLinks.remove(nodeLink);
		this.nodeLinksSorted = false;
		super.markAsChanged();
	}

	/**
	 * Добавить фрагмент в состав линии. Внимание! концевые точки линии не
	 * обновляются.
	 *
	 * @param addNodeLink
	 *          фрагмент линии
	 */
	public void addNodeLink(final NodeLink addNodeLink) {
		this.nodeLinks.add(addNodeLink);
		this.nodeLinksSorted = false;
		super.markAsChanged();
	}

	/**
	 * Убрать все фрагмента из состава линии.
	 */
	public void clearNodeLinks() {
		this.nodeLinks.clear();
		this.nodeLinksSorted = false;
		super.markAsChanged();
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
	public java.util.List getNodeLinksAt(final AbstractNode node) {
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
	 */
	public void sortNodeLinks() {
		if (!this.nodeLinksSorted) {
			AbstractNode smne = this.getStartNode();
			NodeLink currentNodeLink = null;
			LinkedList list = new LinkedList();
			List nodeList = new LinkedList();

			List origNodeLinks = new LinkedList();
			origNodeLinks.addAll(getNodeLinks());

			int count = origNodeLinks.size();
			for (int i = 0; i < count; i++) {
				nodeList.add(smne);

				for (Iterator it = origNodeLinks.iterator(); it.hasNext();) {
					NodeLink nodeLink = (NodeLink) it.next();

					if (!nodeLink.equals(currentNodeLink)) {
						if (nodeLink.getStartNode().equals(smne)) {
							list.add(nodeLink);
							it.remove();
							smne = nodeLink.getEndNode();
							currentNodeLink = nodeLink;
							break;
						}
						else
							if (nodeLink.getEndNode().equals(smne)) {
								list.add(nodeLink);
								it.remove();
								smne = nodeLink.getStartNode();
								currentNodeLink = nodeLink;
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
	public NodeLink nextNodeLink(final NodeLink nodeLink) {
		this.sortNodeLinks();
		for(Iterator iter = this.nodeLinks.iterator(); iter.hasNext();) {
			NodeLink bufNodeLink = (NodeLink )iter.next();
			if(bufNodeLink.equals(nodeLink)) {
				if(iter.hasNext())
					return (NodeLink )iter.next();
				return null;
			}
		}
		return null;
	}

	/**
	 * Получить предыдущий фрагмент по цепочке сортированных фрагментов.
	 *
	 * @param nodeLink
	 *          фрагмент
	 * @return предыдущий фрагмент, или <code>null</code>, если nl - первый в
	 *         списке
	 */
	public NodeLink previousNodeLink(final NodeLink nodeLink) {
		this.sortNodeLinks();
		NodeLink prevNodeLink = null;
		for(Iterator iter = this.nodeLinks.iterator(); iter.hasNext();) {
			NodeLink bufNodeLink = (NodeLink )iter.next();
			if(bufNodeLink.equals(nodeLink))
				return prevNodeLink;
			prevNodeLink = bufNodeLink;
		}
		return null;
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
	public void setAlarmState(final boolean alarmState) {
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
	public void setRemoved(final boolean removed) {
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
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	/**
	 * Получить другой конечный узел линии для заданного конечного узла.
	 *
	 * @param node
	 *          узел
	 * @return другой конечный узел
	 */
	public AbstractNode getOtherNode(final AbstractNode node) {
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
	public void revert(final MapElementState state) {
		PhysicalLinkState mples = (PhysicalLinkState) state;

		this.setName(mples.name);
		this.setDescription(mples.description);
		this.setStartNode(mples.startNode);
		this.setEndNode(mples.endNode);

		this.nodeLinks = new ArrayList(mples.nodeLinks.size());
		for (Iterator it = mples.nodeLinks.iterator(); it.hasNext();) {
			NodeLink mnle = (NodeLink) it.next();
			mnle.setPhysicalLink(this);
			this.nodeLinks.add(mnle);
		}
		try {
			setType((PhysicalLinkType) (StorableObjectPool.getStorableObject(mples.mapProtoId, true)));
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
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static PhysicalLink createInstance(final Identifier creatorId, final java.util.Map exportMap1)
			throws CreateObjectException {
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
					ObjectEntities.PHYSICALLINK_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			Collection collection = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
			if (collection == null || collection.size() == 0) {
				typeCodeName1 = PhysicalLinkType.DEFAULT_TUNNEL;

				condition.setValue(typeCodeName1);

				//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
				collection = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
				if (collection == null || collection.size() == 0) {
					throw new CreateObjectException("PhysicalLinkType \'" + PhysicalLinkType.DEFAULT_TUNNEL + "\' not found");
				}
			}
			physicalLinkType1 = (PhysicalLinkType) collection.iterator().next();

			AbstractNode startNode1 = (AbstractNode) StorableObjectPool.getStorableObject(startNodeId1, true);
			AbstractNode endNode1 = (AbstractNode) StorableObjectPool.getStorableObject(endNodeId1, true);
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

			assert link1.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			link1.markAsChanged();

			return link1;
		}
		catch (ApplicationException e) {
			throw new CreateObjectException("PhysicalLink.createInstance |  ", e);
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void addCharacteristic(final Characteristic characteristic) {
		this.characteristics.add(characteristic);
		super.markAsChanged();
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		this.characteristics.remove(characteristic);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
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

	public XmlObject getXMLTransferable() {
		com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = com.syrus.amficom.map.xml.PhysicalLink.Factory.newInstance();
		fillXMLTransferable(xmlPhysicalLink);
		return xmlPhysicalLink;
	}

	public void fillXMLTransferable(XmlObject xmlObject) {
		com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = (com.syrus.amficom.map.xml.PhysicalLink )xmlObject; 

		PhysicalLinkType type = (PhysicalLinkType )this.getType(); 

		com.syrus.amficom.general.xml.UID uid = xmlPhysicalLink.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlPhysicalLink.setName(this.name);
		xmlPhysicalLink.setDescription(this.description);
		xmlPhysicalLink.setPhysicallinktypeuid(com.syrus.amficom.map.xml.PhysicalLinkTypeSort.Enum.forString(type.getSort().value()));

		uid = xmlPhysicalLink.addNewStartnodeuid();
		uid.setStringValue(this.startNode.getId().toString());

		uid = xmlPhysicalLink.addNewEndnodeuid();
		uid.setStringValue(this.endNode.getId().toString());

		xmlPhysicalLink.setCity(this.city);
		xmlPhysicalLink.setStreet(this.street);
		xmlPhysicalLink.setBuilding(this.building);
	}

	PhysicalLink(
			Identifier creatorId, 
			com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink, 
			ClonedIdsPool clonedIdsPool) 
		throws CreateObjectException, ApplicationException {

		super(
				clonedIdsPool.getClonedId(
						ObjectEntities.PHYSICALLINK_CODE, 
						xmlPhysicalLink.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				0);
		this.characteristics = new HashSet();
		this.nodeLinks = new ArrayList();
		this.selected = false;
		this.fromXMLTransferable(xmlPhysicalLink, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = (com.syrus.amficom.map.xml.PhysicalLink )xmlObject; 

		this.name = xmlPhysicalLink.getName();
		this.description = xmlPhysicalLink.getDescription();
		this.city = xmlPhysicalLink.getCity();
		this.street = xmlPhysicalLink.getStreet();
		this.building = xmlPhysicalLink.getBuilding();

		Identifier startNodeId1 = clonedIdsPool.getClonedId(
				ObjectEntities.SITENODE_CODE, 
				xmlPhysicalLink.getStartnodeuid().getStringValue());
		Identifier endNodeId1 = clonedIdsPool.getClonedId(
				ObjectEntities.SITENODE_CODE, 
				xmlPhysicalLink.getEndnodeuid().getStringValue());

		if(xmlPhysicalLink.getStartnodeuid().getStringValue().equals("507133")) {
			System.out.println("Start node 507133 id " + startNodeId1.toString());
		}
		if(xmlPhysicalLink.getEndnodeuid().getStringValue().equals("507133")) {
			System.out.println("End node 507133 id " + endNodeId1.toString());
		}
		
		this.startNode = (AbstractNode) StorableObjectPool.getStorableObject(startNodeId1, true);
		this.endNode = (AbstractNode) StorableObjectPool.getStorableObject(endNodeId1, true);

		String typeCodeName1 = xmlPhysicalLink.getPhysicallinktypeuid().toString();
		TypicalCondition condition = new TypicalCondition(typeCodeName1,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PHYSICALLINK_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
		Collection collection = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
		if (collection == null || collection.size() == 0) {
			typeCodeName1 = PhysicalLinkType.DEFAULT_TUNNEL;

			condition.setValue(typeCodeName1);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			collection = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
			if (collection == null || collection.size() == 0) {
				throw new CreateObjectException("PhysicalLinkType \'" + PhysicalLinkType.DEFAULT_TUNNEL + "\' not found");
			}
		}
		
		this.physicalLinkType = (PhysicalLinkType) collection.iterator().next();

		this.dimensionX = this.physicalLinkType.getBindingDimension().getWidth();
		this.dimensionY = this.physicalLinkType.getBindingDimension().getHeight();
		this.leftToRight = true;
		this.topToBottom = true;
		this.binding = new PhysicalLinkBinding(this.physicalLinkType.getBindingDimension());
	}

	public static PhysicalLink createInstance(final Identifier creatorId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = (com.syrus.amficom.map.xml.PhysicalLink )xmlObject;

		try {
			PhysicalLink physicalLink = new PhysicalLink(creatorId, xmlPhysicalLink, clonedIdsPool);
			assert physicalLink.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			physicalLink.markAsChanged();
			return physicalLink;
		}
		catch (Exception e) {
			System.out.println(xmlPhysicalLink);
			throw new CreateObjectException("PhysicalLink.createInstance |  ", e);
		}
	}
}
