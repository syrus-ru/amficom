/*-
 * $Id: NodeLink.java,v 1.41 2005/05/23 18:45:18 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;

/**
 * Фрагмент линии на топологической схеме. Фрагмент представляет собой линейный
 * отрезок, соединяющий два концевых узла ({@link AbstractNode}). Фрагменты
 * не живут сами по себе, а входят в состав одной и только одной линии
 * ({@link PhysicalLink}).
 * @author $Author: bass $
 * @version $Revision: 1.41 $, $Date: 2005/05/23 18:45:18 $
 * @module map_v1
 */
public class NodeLink extends StorableObject implements MapElement {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257290240262617393L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LENGTH = "length";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_START_NODE_ID = "start_node_id";
	public static final String COLUMN_END_NODE_ID = "end_node_id";

	/**
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	private String name;
	private PhysicalLink physicalLink;
	private AbstractNode startNode;
	private AbstractNode endNode;
	private double length;

	private Set characteristics;

	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;

	NodeLink(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getDatabase(ObjectEntities.NODE_LINK_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	NodeLink(NodeLink_Transferable nlt) throws CreateObjectException {
		try {
			this.fromTransferable(nlt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	NodeLink(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String name,
			final PhysicalLink physicalLink,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final double length) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.name = name;
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
		this.length = length;

		this.characteristics = new HashSet();

		this.selected = false;
	}

	public static NodeLink createInstance(final Identifier creatorId,
			final PhysicalLink physicalLink,
			final AbstractNode stNode,
			final AbstractNode eNode) throws CreateObjectException {
		return NodeLink.createInstance(creatorId,
				"",
				physicalLink,
				stNode,
				eNode,
				0.0D);
	}

	public static NodeLink createInstance(Identifier creatorId,
			String name,
			PhysicalLink physicalLink,
			AbstractNode starNode,
			AbstractNode endNode,
			double length) throws CreateObjectException {
		if (name == null || physicalLink == null || starNode == null || endNode == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			NodeLink nodeLink = new NodeLink(IdentifierPool.getGeneratedIdentifier(ObjectEntities.NODE_LINK_ENTITY_CODE),
					creatorId,
					0L,
					name,
					physicalLink,
					starNode,
					endNode,
					length);
			nodeLink.changed = true;
			return nodeLink;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		NodeLink_Transferable nlt = (NodeLink_Transferable) transferable;
		super.fromTransferable(nlt.header);

		this.name = nlt.name;
		this.length = nlt.length;

		this.physicalLink = (PhysicalLink) StorableObjectPool.getStorableObject(new Identifier(nlt.physicalLinkId), true);

		this.startNode = (AbstractNode) StorableObjectPool.getStorableObject(new Identifier(nlt.startNodeId), true);
		this.endNode = (AbstractNode) StorableObjectPool.getStorableObject(new Identifier(nlt.endNodeId), true);

		Set characteristicIds = Identifier.fromTransferables(nlt.characteristicIds);
		this.characteristics.addAll(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);
		return new NodeLink_Transferable(super.getHeaderTransferable(),
				this.name,
				(Identifier_Transferable) this.physicalLink.getId().getTransferable(),
				(Identifier_Transferable) this.startNode.getId().getTransferable(),
				(Identifier_Transferable) this.endNode.getId().getTransferable(),
				this.length,
				charIds);
	}

	public AbstractNode getEndNode() {
		return this.endNode;
	}

	protected void setEndNode0(AbstractNode endNode) {
		this.endNode = endNode;
	}

	public void setEndNode(AbstractNode endNode) {
		this.setEndNode0(endNode);
		this.changed = true;
	}

	public double getLength() {
		return this.length;
	}

	protected void setLength0(double length) {
		this.length = length;
	}

	public void setLength(double length) {
		this.setLength0(length);
		this.changed = true;
	}

	public String getName() {
		return this.name;
	}

	protected void setName0(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.setName0(name);
		this.changed = true;
	}

	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}

	protected void setPhysicalLink0(PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		if (getStartNode() instanceof TopologicalNode)
			((TopologicalNode) getStartNode()).setPhysicalLink(physicalLink);
		if (getEndNode() instanceof TopologicalNode)
			((TopologicalNode) getEndNode()).setPhysicalLink(physicalLink);
	}

	public void setPhysicalLink(PhysicalLink physicalLink) {
		this.setPhysicalLink0(physicalLink);
		this.changed = true;
	}

	public AbstractNode getStartNode() {
		return this.startNode;
	}

	protected void setStartNode0(AbstractNode startNode) {
		this.startNode = startNode;
	}

	public void setStartNode(AbstractNode startNode) {
		this.setStartNode0(startNode);
		this.changed = true;
	}

	synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String name,
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode,
			double length) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
		this.length = length;
	}

	/**
	 * Получить другой концевой узел фрагмента.
	 *
	 * @param node
	 *          концевой узел
	 * @return другой концевой узел. В случае, если node не является концевым для
	 *         данного фрагмента, возвращается <code>null</code>.
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
		return getPhysicalLink().getAlarmState();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		return new DoublePoint((getStartNode().getLocation().getX() + getEndNode().getLocation().getX()) / 2,
				(getStartNode().getLocation().getY() + getEndNode().getLocation().getY()) / 2);
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		return new NodeLinkState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(MapElementState state) {
		NodeLinkState mnles = (NodeLinkState) state;

		this.setName(mnles.name);
		this.setStartNode(mnles.startNode);
		this.setEndNode(mnles.endNode);

		try {
			setPhysicalLink((PhysicalLink) StorableObjectPool.getStorableObject(mnles.physicalLinkId, false));
		}
		catch (ApplicationException e) {
			e.printStackTrace();
		}
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
	 * Получить топологическую длинну фрагмента.
	 *
	 * @return топологическая длина
	 */
	public double getLengthLt() {
		return getLength();
	}

	/**
	 * Установить топологическую длинну фрагмента. Высчитывается в месте, в
	 * котором осуществляется управление рисованием фрагментов линий.
	 *
	 * @param length
	 *          топологическая длина
	 */
	public void setLengthLt(double length) {
		this.setLength(length);
	}

	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_LENGTH, String.valueOf(this.length));
			exportMap.put(COLUMN_PHYSICAL_LINK_ID, this.physicalLink.getId());
			exportMap.put(COLUMN_START_NODE_ID, this.startNode.getId());
			exportMap.put(COLUMN_END_NODE_ID, this.endNode.getId());
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static NodeLink createInstance(Identifier creatorId, java.util.Map exportMap1) throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		double length1 = Double.parseDouble((String) exportMap1.get(COLUMN_LENGTH));
		Identifier physicalLinkId1 = (Identifier) exportMap1.get(COLUMN_PHYSICAL_LINK_ID);
		Identifier startNodeId1 = (Identifier) exportMap1.get(COLUMN_START_NODE_ID);
		Identifier endNodeId1 = (Identifier) exportMap1.get(COLUMN_END_NODE_ID);

		if (id1 == null
				|| creatorId == null
				|| name1 == null
				|| physicalLinkId1 == null
				|| startNodeId1 == null
				|| endNodeId1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLink physicalLink1 = (PhysicalLink) StorableObjectPool.getStorableObject(physicalLinkId1, false);
			AbstractNode startNode1 = (AbstractNode) StorableObjectPool.getStorableObject(startNodeId1, true);
			AbstractNode endNode1 = (AbstractNode) StorableObjectPool.getStorableObject(endNodeId1, true);
			NodeLink nodeLink1 = new NodeLink(id1, creatorId, 0L, name1, physicalLink1, startNode1, endNode1, length1);
			nodeLink1.changed = true;
			physicalLink1.addNodeLink(nodeLink1);

			return nodeLink1;
		}
		catch (ApplicationException e) {
			throw new CreateObjectException("NodeLink.createInstance |  ", e);
		}
	}

	public Set getCharacteristics() {
		return  Collections.unmodifiableSet(this.characteristics);
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
		return CharacteristicSort.CHARACTERISTIC_SORT_NODE_LINK;
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

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.add(this.physicalLink);
		dependencies.add(this.startNode);
		dependencies.add(this.endNode);
		return dependencies;
	}

}
