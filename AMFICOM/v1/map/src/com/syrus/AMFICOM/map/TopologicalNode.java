/**
 * $Id: TopologicalNode.java,v 1.26 2005/04/04 13:15:58 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.omg.CORBA.portable.IDLEntity;

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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;

/**
 * Топологический узел нв топологической схеме. Топологический узел может
 * быть концевым для линии и для фрагмента линии. В физическом смысле
 * топологический узел соответствует точке изгиба линии и не требует 
 * дополнительной описательной информации.
 * @author $Author: bass $
 * @version $Revision: 1.26 $, $Date: 2005/04/04 13:15:58 $
 * @module map_v1
 * @todo physicalLink should be transient
 */
public class TopologicalNode extends AbstractNode {

	public static final String CLOSED_NODE = "node";
	public static final String OPEN_NODE = "void";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258130254244885554L;

	/**
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	/**
	 * Флаг показывающий закрыт ли узел true значит что из узла выходит две линии,
	 * false одна
	 */
	private boolean active;
	private StorableObjectDatabase topologicalNodeDatabase;

	private transient PhysicalLink physicalLink = null;

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private transient boolean canBind = false;

	public TopologicalNode(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			this.topologicalNodeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public TopologicalNode(TopologicalNode_Transferable tnt) throws CreateObjectException {
		super(tnt.header);
		super.name = tnt.name;
		super.description = tnt.description;
		super.location = new DoublePoint(tnt.longitude, tnt.latitude);
		this.active = tnt.active;

		try {
			this.characteristics = new HashSet(tnt.characteristicIds.length);
			Set characteristicIds = new HashSet(tnt.characteristicIds.length);
			for (int i = 0; i < tnt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(tnt.characteristicIds[i]));

			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected TopologicalNode(final Identifier id,
			final Identifier creatorId,
			final long version,
			String name,
			String description,
			double longitude,
			double latitude,
			boolean active) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				name,
				description,
				new DoublePoint(longitude, latitude));
		this.active = active;

		this.characteristics = new HashSet();

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
	}

	protected TopologicalNode(final Identifier id,
			final Identifier creatorId,
			final long version,
			String name,
			String description,
			PhysicalLink physicalLink,
			double longitude,
			double latitude,
			boolean active) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				name,
				description,
				new DoublePoint(longitude, latitude));
		this.physicalLink = physicalLink;
		this.active = active;

		this.characteristics = new HashSet();

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();

		this.selected = false;
	}

	protected static TopologicalNode createInstance0(
			Identifier creatorId,
			String name,
			String description,
			PhysicalLink physicalLink, 
			DoublePoint location)
		throws CreateObjectException {

		// IMPORTANT!!
		// physicalLink validation should NOT be performad
		// as long as a known case of creation with null PhysicalLink exists.
		// It is programmer's responsibility to make sure that call to
		// 
		// topologicalNode.setPhysicalLink(PhysicalLink physicalLink)
		// 
		// is performed after creation of TopologicalNode with physicalLink
		// set to null.
		// 
		// Where needed physicalLink validation is performed in corresponding
		// public createInstance methods

		if (creatorId == null || name == null || description == null || location == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			TopologicalNode topologicalNode = new TopologicalNode(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE),
					creatorId,
					0L,
					name,
					description,
					physicalLink,
					location.getX(),
					location.getY(),
					false);
			topologicalNode.changed = true;
			return topologicalNode;

		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TopologicalNode.createInstance | cannot generate identifier ", e);
		}
	}

	public static TopologicalNode createInstance(
			Identifier creatorId,
			String name,
			String description,
			PhysicalLink physicalLink, 
			DoublePoint location)
		throws CreateObjectException {

		// validate physicalLink as long as validation is not performad
		// in createInstance0

		if (physicalLink == null)
			throw new IllegalArgumentException("Argument is 'null'");

		return TopologicalNode.createInstance0(
				creatorId,
				name,
				description,
				physicalLink,
				location);
		}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final PhysicalLink physicalLink,
			final DoublePoint location) throws CreateObjectException {

		// validate physicalLink as long as validation is not performad
		// in createInstance0

		if (physicalLink == null)
			throw new IllegalArgumentException("Argument is 'null'");

		return TopologicalNode.createInstance0(creatorId, "", "", physicalLink, location);
	}

	public static TopologicalNode createInstance(final Identifier creatorId, final DoublePoint location)
			throws CreateObjectException {

		return TopologicalNode.createInstance0(creatorId, "", "", null, location);
	}

	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		return new TopologicalNode_Transferable(super.getHeaderTransferable(),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				(Identifier_Transferable) this.physicalLink.getId().getTransferable(),
				this.active,
				charIds);
	}

	public boolean isActive() {
		return this.active;
	}

	/**
	 * установить активность топологического узла. узел активен, если он находится
	 * в середине связи, и не активен, если он находится на конце связи. активные
	 * и неактивные топологические узлы отображаются разными иконками
	 * 
	 * @param active
	 *          флаг активности
	 */
	public void setActive(boolean active) {
		this.active = active;
		this.changed = true;
	}

	public PhysicalLink getPhysicalLink() {
		if (this.physicalLink == null)
			this.physicalLink = findPhysicalLink();
		return this.physicalLink;
	}

	public void setPhysicalLink(PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		// do not change version due to physical link is not dependence object
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String name,
			String description,
			double longitude,
			double latitude,
			boolean active) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.name = name;
		this.description = description;
		this.location.setLocation(longitude, latitude);
		this.active = active;
	}

	/**
	 * Установить флаг возможности привязки топологического узла к сетевому и/или
	 * непривязанному узлу.
	 * 
	 * @param canBind
	 *          флаг овзможности привязки
	 */
	public void setCanBind(boolean canBind) {
		this.canBind = canBind;
	}

	/**
	 * Получить флаг возможности привязки топологического узла к сетевому и/или
	 * непривязанному узлу.
	 * 
	 * @return флаг овзможности привязки
	 */
	public boolean isCanBind() {
		return this.canBind;
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		return new TopologicalNodeState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(MapElementState state) {
		TopologicalNodeState mpnes = (TopologicalNodeState) state;

		setName(mpnes.name);
		setDescription(mpnes.description);
		setImageId(mpnes.imageId);
		setLocation(mpnes.location);
		setActive(mpnes.active);
		try {
			setPhysicalLink((PhysicalLink) MapStorableObjectPool.getStorableObject(mpnes.physicalLinkId, false));
		}
		catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			exportMap.put(StorableObjectWrapper.COLUMN_ID, this.id);
			exportMap.put(StorableObjectWrapper.COLUMN_NAME, this.name);
			exportMap.put(StorableObjectWrapper.COLUMN_DESCRIPTION, this.description);
			exportMap.put(TopologicalNodeWrapper.COLUMN_PHYSICAL_LINK_ID, this.physicalLink.getId());
			exportMap.put(TopologicalNodeWrapper.COLUMN_X, String.valueOf(this.location.getX()));
			exportMap.put(TopologicalNodeWrapper.COLUMN_Y, String.valueOf(this.location.getY()));
			exportMap.put(TopologicalNodeWrapper.COLUMN_ACTIVE, String.valueOf(this.active));
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static TopologicalNode createInstance(Identifier creatorId, java.util.Map exportMap1) throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(StorableObjectWrapper.COLUMN_ID);
		String name1 = (String) exportMap1.get(StorableObjectWrapper.COLUMN_NAME);
		String description1 = (String) exportMap1.get(StorableObjectWrapper.COLUMN_DESCRIPTION);
		Identifier physicalLinkId1 = (Identifier) exportMap1.get(TopologicalNodeWrapper.COLUMN_PHYSICAL_LINK_ID);
		double x1 = Double.parseDouble((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_X));
		double y1 = Double.parseDouble((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_Y));
		boolean active1 = Boolean.valueOf((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_ACTIVE)).booleanValue();

		if (id1 == null || creatorId == null || name1 == null || description1 == null || physicalLinkId1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLink physicalLink1 = (PhysicalLink) MapStorableObjectPool.getStorableObject(physicalLinkId1, false);
			TopologicalNode node1 = new TopologicalNode(id1, creatorId, 0L, name1, description1, x1, y1, active1);
			node1.changed = true;
			node1.setPhysicalLink(physicalLink1);

			return node1;
		}
		catch (ApplicationException e) {
			throw new CreateObjectException("Mark.createInstance |  ", e);
		}
	}

	private PhysicalLink findPhysicalLink() {
		return this.map.getNodeLink(this).getPhysicalLink();
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
		return CharacteristicSort.CHARACTERISTIC_SORT_TOPOLOGICAL_NODE;
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
