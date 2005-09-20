/*-
 * $Id: PhysicalLink.java,v 1.116 2005/09/20 10:42:01 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLink;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkHelper;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * Линия топологический схемы. Линия имеет начальный и конечный узлы,
 * внутренние топологические узлы (список не хранится явно) и состоит из
 * фрагментов. Фрагменты образуют цепочку. Последовательность фрагментов
 * не хранится. Линия характеризуется типом (<code>{@link PhysicalLinkType}</code>).
 * Предуствновленными являются  два типа -
 * тоннель (<code>{@link PhysicalLinkType#DEFAULT_TUNNEL}</code>)
 * и коллектор (<code>{@link PhysicalLinkType#DEFAULT_COLLECTOR}</code>).
 * @author $Author: bass $
 * @version $Revision: 1.116 $, $Date: 2005/09/20 10:42:01 $
 * @module map
 */
public class PhysicalLink extends StorableObject
		implements TypedObject<PhysicalLinkType>, MapElement,
		XmlBeansTransferable<XmlPhysicalLink> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4121409622671570743L;

	private String name;
	private String description;

	private PhysicalLinkType physicalLinkType;

	private Identifier startNodeId;
	private Identifier endNodeId;
	private String city;
	private String street;
	private String building;

	private boolean leftToRight;
	private boolean topToBottom;

	protected PhysicalLinkBinding binding = null;

	protected List<NodeLink> nodeLinks;
	protected List<AbstractNode> sortedNodes;
	protected AbstractNode startNode = null;
	protected AbstractNode endNode = null;
	private Set<Characteristic> characteristics;

	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;
	protected transient boolean nodeLinksSorted = false;

	private boolean transientFieldsInitialized = false;
	
	private void initialize() {
		if(!this.transientFieldsInitialized) {
			this.nodeLinks = findNodeLinks();
			this.sortedNodes = new LinkedList<AbstractNode>();
			if (this.characteristics == null) {
				try {
					final Set<Characteristic> chs = StorableObjectPool.getStorableObjectsByCondition(
							new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE), 
							false);
					this.characteristics = new HashSet<Characteristic>(chs);
				} catch(ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.transientFieldsInitialized = true;
		}		
	}

	private List<NodeLink> findNodeLinks() {
		try {
			final StorableObjectCondition condition = new LinkedIdsCondition(this.getId(), ObjectEntities.NODELINK_CODE);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			final Set<NodeLink> nlinks = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			final List<NodeLink> nlinkslist = new ArrayList<NodeLink>(nlinks.size());
			for(NodeLink nodeLink : nlinks) {
				if(!nodeLink.isRemoved()) {
					nlinkslist.add(nodeLink);
				}
			}
			return nlinkslist;
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
		return new LinkedList<NodeLink>();
	}
	
	public PhysicalLink(final IdlPhysicalLink plt) throws CreateObjectException {
		try {
			this.fromTransferable(plt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected PhysicalLink(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final PhysicalLinkType physicalLinkType,
			final Identifier startNodeId,
			final Identifier endNodeId,
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
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
		this.city = city;
		this.street = street;
		this.building = building;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;

//		initialize();

		this.selected = false;

		if (physicalLinkType == null) {
			this.binding = new PhysicalLinkBinding(new IntDimension(dimensionX, dimensionY));
		} else {
			this.binding = new PhysicalLinkBinding(physicalLinkType.getBindingDimension());
		}
	}
	

	public static PhysicalLink createInstance(final Identifier creatorId,
			final Identifier stNode,
			final Identifier eNode,
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
			final Identifier startNodeId,
			final Identifier endNodeId,
			final String city,
			final String street,
			final String building,
			final int dimensionX,
			final int dimensionY,
			final boolean leftToRight,
			final boolean topToBottom) throws CreateObjectException {

		assert creatorId != null
				&& name != null
				&& description != null
				&& physicalLinkType != null
				&& startNodeId != null
				&& endNodeId != null
				&& city != null
				&& street != null
				&& building != null : NON_NULL_EXPECTED;
		assert !startNodeId.isVoid() && !endNodeId.isVoid() : NON_NULL_EXPECTED;		
		
		try {
			final PhysicalLink physicalLink = new PhysicalLink(IdentifierPool.getGeneratedIdentifier(PHYSICALLINK_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLinkType,
					startNodeId,
					endNodeId,
					city,
					street,
					building,
					dimensionX,
					dimensionY,
					leftToRight,
					topToBottom);

			assert physicalLink.isValid() : OBJECT_BADLY_INITIALIZED;

			physicalLink.markAsChanged();

			return physicalLink;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPhysicalLink plt = (IdlPhysicalLink) transferable;
		super.fromTransferable(plt);

		this.name = plt.name;
		this.description = plt.description;

		this.city = plt.city;
		this.street = plt.street;
		this.building = plt.building;
		this.leftToRight = plt.leftToRight;
		this.topToBottom = plt.topToBottom;

		this.physicalLinkType = StorableObjectPool.getStorableObject(new Identifier(plt.physicalLinkTypeId), true);

		this.startNodeId = new Identifier(plt.startNodeId);
		this.endNodeId = new Identifier(plt.endNodeId);
		
		assert !this.startNodeId.isVoid() : NON_VOID_EXPECTED; 
		assert !this.endNodeId.isVoid() : NON_VOID_EXPECTED;
		
		this.selected = false;

		this.binding = new PhysicalLinkBinding(new IntDimension(plt.dimensionX, plt.dimensionY));

		this.transientFieldsInitialized = false;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>(3);
		dependencies.add(this.physicalLinkType);
		dependencies.add(this.startNodeId);
		dependencies.add(this.endNodeId);
		
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPhysicalLink getTransferable(final ORB orb) {
		IdlIdentifier[] nodeLinkIds = new IdlIdentifier[0];
		
		int dimensionX = this.binding.getDimension().getWidth();
		int dimensionY = this.binding.getDimension().getHeight();

		return IdlPhysicalLinkHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.physicalLinkType.getId().getTransferable(),
				this.startNodeId.getTransferable(),
				this.endNodeId.getTransferable(),
				this.city,
				this.street,
				this.building,
				dimensionX,
				dimensionY,
				this.leftToRight,
				this.topToBottom,
				nodeLinkIds);
	}

	public final PhysicalLinkType getType() {
		return this.physicalLinkType;
	}

	public void setType(final StorableObjectType type) {
		this.physicalLinkType = (PhysicalLinkType) type;
		super.markAsChanged();
	}

	public String getBuilding() {
		return this.building;
	}

	protected void setBuilding0(final String building) {
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
		return this.binding.getDimension().getWidth();
	}

	public void setDimensionX(final int dimensionX) {
		IntDimension dimension = this.binding.getDimension();
		dimension.setWidth(dimensionX);
		this.binding.setDimension(dimension);
		super.markAsChanged();
	}

	public int getDimensionY() {
		return this.binding.getDimension().getHeight();
	}

	public void setDimensionY(final int dimensionY) {
		IntDimension dimension = this.binding.getDimension();
		dimension.setHeight(dimensionY);
		this.binding.setDimension(dimension);
		super.markAsChanged();
	}

	public AbstractNode getEndNode() {
		if(this.endNode == null) {
			try {
				this.endNode = StorableObjectPool.<AbstractNode>getStorableObject(this.endNodeId, true);
			} catch(ApplicationException e) {
				Log.errorException(e);
			}
		}
		return this.endNode;
	}
	
	public Identifier getStartNodeId() {
		return this.startNodeId;
	}

	public void setStartNodeId(Identifier startNodeId) {
		this.startNodeId = startNodeId;
	}

	public Identifier getEndNodeId() {
		return this.endNodeId;
	}

	public void setEndNodeId(Identifier endNodeId) {
		this.endNodeId = endNodeId;
	}

	protected void setEndNode0(final AbstractNode endNode) {
		this.endNodeId = endNode.getId();
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
			return this.getStartNode().getName() + " -- " + this.getEndNode().getName();
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

	public List<NodeLink> getNodeLinks() {
		this.initialize();
		if(this.nodeLinks.isEmpty()) {
			System.out.println("no nodelinks for link " + this.id + " between \'" + this.getStartNode().getName() + "\' and " + this.getEndNode().getName() + "\'");
		}
		return Collections.unmodifiableList(this.nodeLinks);
	}

	public void setNodeLinks(final List<NodeLink> nodeLinks) {
		this.initialize();
		this.nodeLinks.clear();
		if (nodeLinks != null)
			this.nodeLinks.addAll(nodeLinks);
		this.nodeLinksSorted = false;
		super.markAsChanged();
	}

	public AbstractNode getStartNode() {
		if(this.startNode == null) {
			try {
				this.startNode = StorableObjectPool.<AbstractNode>getStorableObject(this.startNodeId, true);
			} catch(ApplicationException e) {
				Log.errorException(e);
			}
		}
		return this.startNode;
	}

	protected void setStartNode0(final AbstractNode startNode) {
		this.startNodeId = startNode.getId();
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
			final StorableObjectVersion version,
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
			final Identifier startNodeId,
			final Identifier endNodeId) {
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
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
		
		this.binding.setDimension(new IntDimension(dimensionX, dimensionY));
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
		for (final NodeLink nodeLink : this.getNodeLinks()) {
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
		this.initialize();
		this.nodeLinks.remove(nodeLink);
		Log.debugMessage("For physical link " + this.id.toString() + " remove nodeLink = " + nodeLink.getId().toString(), FINEST);
		this.nodeLinksSorted = false;
		// there is no need to modify object. NodeLink is transient.
	}

	/**
	 * Добавить фрагмент в состав линии. Внимание! концевые точки линии не
	 * обновляются.
	 *
	 * @param nodeLink
	 *          фрагмент линии
	 */
	public void addNodeLink(final NodeLink nodeLink) {
		this.initialize();
		if (!this.nodeLinks.contains(nodeLink)) {
			this.nodeLinks.add(nodeLink);
			Log.debugMessage("For physical link " + this.id.toString() + " add nodeLink = " + nodeLink.getId().toString(), FINEST);
			this.nodeLinksSorted = false;
			// there is no need to modify object. NodeLink is transient.
		}
	}

	/**
	 * Убрать все фрагмента из состава линии.
	 */
	public void clearNodeLinks() {
		this.initialize();
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
	public List<NodeLink> getNodeLinksAt(final AbstractNode node) {
		final LinkedList<NodeLink> returnNodeLink = new LinkedList<NodeLink>();
		for (final NodeLink nodeLink : this.getNodeLinks()) {
			if ((nodeLink.getEndNode().equals(node)) || (nodeLink.getStartNode().equals(node))) {
				returnNodeLink.add(nodeLink);
			}
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

		for (final NodeLink nodeLink : this.getNodeLinks()) {
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
	public List<AbstractNode> getSortedNodes() {
		this.initialize();
		if (!this.nodeLinksSorted) {
			return Collections.emptyList();
		}
		return this.sortedNodes;
	}

	/**
	 * Сортировать фрагменты линии по цепочке начиная от начального узла. При
	 * сортировке фрагментов сортируются также узлы
	 *
	 */
	public void sortNodeLinks() {
		this.initialize();
		if (!this.nodeLinksSorted) {
			AbstractNode smne = this.getStartNode();
			NodeLink currentNodeLink = null;
			final LinkedList<NodeLink> list = new LinkedList<NodeLink>();
			final List<AbstractNode> nodeList = new LinkedList<AbstractNode>();

			final List<NodeLink> origNodeLinks = new LinkedList<NodeLink>();
			origNodeLinks.addAll(this.getNodeLinks());

			int count = origNodeLinks.size();
			for (int i = 0; i < count; i++) {
				nodeList.add(smne);

				for (final Iterator<NodeLink> it = origNodeLinks.iterator(); it.hasNext();) {
					final NodeLink nodeLink = it.next();

					if (!nodeLink.equals(currentNodeLink)) {
						if (nodeLink.getStartNode().equals(smne)) {
							list.add(nodeLink);
							it.remove();
							smne = nodeLink.getEndNode();
							currentNodeLink = nodeLink;
							break;
						} else
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
		for(final Iterator<NodeLink> iter = this.nodeLinks.iterator(); iter.hasNext();) {
			final NodeLink bufNodeLink = iter.next();
			if(bufNodeLink.equals(nodeLink)) {
				if(iter.hasNext())
					return iter.next();
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
		for(final Iterator<NodeLink> iter = this.nodeLinks.iterator(); iter.hasNext();) {
			final NodeLink bufNodeLink = iter.next();
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

		for (NodeLink nodeLink : getNodeLinks()) {
			DoublePoint an = nodeLink.getLocation();
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
		final PhysicalLinkState mples = (PhysicalLinkState) state;

		this.setName(mples.name);
		this.setDescription(mples.description);
		this.setStartNode(mples.startNode);
		this.setEndNode(mples.endNode);

		this.setNodeLinks(mples.nodeLinks);
		try {
			setType(StorableObjectPool.<PhysicalLinkType>getStorableObject(mples.mapProtoId, true));
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		this.nodeLinksSorted = false;
	}

	public void addCharacteristic(Characteristic characteristic) {
		this.initialize();
		this.characteristics.add(characteristic);
	}
	
	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		this.initialize();
		return this.characteristics;
	}

	/**
	 * @param physicalLink
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void getXmlTransferable(
			final XmlPhysicalLink physicalLink,
			final String importType)
	throws ApplicationException {
		this.id.getXmlTransferable(physicalLink.addNewId(), importType);
		physicalLink.setName(this.name);
		physicalLink.setDescription(this.description);
		physicalLink.setPhysicalLinkTypeCodename(this.getType().getCodename());
		this.startNodeId.getXmlTransferable(physicalLink.addNewStartNodeId(), importType);
		this.endNodeId.getXmlTransferable(physicalLink.addNewEndNodeId(), importType);
		physicalLink.setCity(this.city);
		physicalLink.setStreet(this.street);
		physicalLink.setBuilding(this.building);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 */
	private PhysicalLink(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id,
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
		this.nodeLinks = new ArrayList<NodeLink>();
		this.selected = false;
	}

	public final void fromXmlTransferable(final XmlPhysicalLink xmlPhysicalLink,
			final String importType)
	throws ApplicationException {
		this.name = xmlPhysicalLink.getName();
		this.description = xmlPhysicalLink.getDescription();
		this.city = xmlPhysicalLink.getCity();
		this.street = xmlPhysicalLink.getStreet();
		this.building = xmlPhysicalLink.getBuilding();

		this.startNodeId = Identifier.fromXmlTransferable(xmlPhysicalLink.getStartNodeId(), importType, MODE_THROW_IF_ABSENT);
		this.endNodeId = Identifier.fromXmlTransferable(xmlPhysicalLink.getEndNodeId(), importType, MODE_THROW_IF_ABSENT);

		final TypicalCondition condition = new TypicalCondition(xmlPhysicalLink.getPhysicalLinkTypeCodename(),
				OperationSort.OPERATION_EQUALS,
				PHYSICALLINK_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
		Set<PhysicalLinkType> physicalLinkTypes = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
		if (physicalLinkTypes.isEmpty()) {
			condition.setValue(PhysicalLinkType.DEFAULT_TUNNEL);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			physicalLinkTypes = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			if (physicalLinkTypes.isEmpty()) {
				throw new CreateObjectException("PhysicalLinkType \'" + PhysicalLinkType.DEFAULT_TUNNEL + "\' not found");
			}
		}
		
		this.physicalLinkType = physicalLinkTypes.iterator().next();

		this.leftToRight = true;
		this.topToBottom = true;
		this.binding = new PhysicalLinkBinding(this.physicalLinkType.getBindingDimension());
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlPhysicalLink
	 * @throws CreateObjectException
	 */
	public static PhysicalLink createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlPhysicalLink xmlPhysicalLink)
	throws CreateObjectException {
		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlPhysicalLink.getId(), importType, PHYSICALLINK_CODE);
			PhysicalLink physicalLink = StorableObjectPool.getStorableObject(id, true);
			if (physicalLink == null) {
				physicalLink = new PhysicalLink(id, new Date(), creatorId);
			}
			physicalLink.fromXmlTransferable(xmlPhysicalLink, importType);
			assert physicalLink.isValid() : OBJECT_BADLY_INITIALIZED;
			physicalLink.markAsChanged();
			return physicalLink;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

}	
