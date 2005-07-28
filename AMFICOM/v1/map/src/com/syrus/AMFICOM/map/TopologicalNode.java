/*-
 * $Id: TopologicalNode.java,v 1.56 2005/07/28 14:47:31 arseniy Exp $
 *
 * Copyright њ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNode;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNodeHelper;

/**
 * “опологический узел нв топологической схеме. “опологический узел может
 * быть концевым дл€ линии и дл€ фрагмента линии. ¬ физическом смысле
 * топологический узел соответствует точке изгиба линии и не требует
 * дополнительной описательной информации.
 * @author $Author: arseniy $
 * @version $Revision: 1.56 $, $Date: 2005/07/28 14:47:31 $
 * @module map_v1
 * @todo physicalLink should be transient
 */
public final class TopologicalNode extends AbstractNode implements XMLBeansTransferable{

	public static final String CLOSED_NODE = "node";
	public static final String OPEN_NODE = "void";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258130254244885554L;

	/**
	 * набор параметров дл€ экспорта. инициализируетс€ только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map<String, Object> exportMap = null;

	/**
	 * ‘лаг показывающий закрыт ли узел true значит что из узла выходит две линии,
	 * false одна
	 */
	private boolean active;

	private transient PhysicalLink physicalLink = null;

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private transient boolean canBind = false;

	TopologicalNode(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.TOPOLOGICALNODE_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public TopologicalNode(final IdlTopologicalNode tnt) throws CreateObjectException {
		super(tnt);
		super.name = tnt.name;
		super.description = tnt.description;
		super.location = new DoublePoint(tnt.longitude, tnt.latitude);
		this.active = tnt.active;
	}

	TopologicalNode(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final boolean active) {
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
	}

	TopologicalNode(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final PhysicalLink physicalLink,
			final double longitude,
			final double latitude,
			final boolean active) {
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

		this.selected = false;
	}

	protected static TopologicalNode createInstance0(final Identifier creatorId,
			final String name,
			final String description,
			final PhysicalLink physicalLink,
			final DoublePoint location) throws CreateObjectException {

		if (creatorId == null || name == null || description == null || location == null || physicalLink == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final TopologicalNode topologicalNode = new TopologicalNode(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TOPOLOGICALNODE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLink,
					location.getX(),
					location.getY(),
					false);

			assert topologicalNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			topologicalNode.markAsChanged();

			return topologicalNode;

		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final PhysicalLink physicalLink,
			final DoublePoint location) throws CreateObjectException {
		return TopologicalNode.createInstance0(creatorId, name, description, physicalLink, location);
	}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final PhysicalLink physicalLink,
			final DoublePoint location) throws CreateObjectException {
		return TopologicalNode.createInstance0(creatorId, "", "", physicalLink, location);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTopologicalNode getTransferable(final ORB orb) {
		return IdlTopologicalNodeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.physicalLink.getId().getTransferable(),
				this.active);
	}

	public boolean isActive() {
		return this.active;
	}

	/**
	 * установить активность топологического узла. узел активен, если он находитс€
	 * в середине св€зи, и не активен, если он находитс€ на конце св€зи. активные
	 * и неактивные топологические узлы отображаютс€ разными иконками
	 *
	 * @param active
	 *          флаг активности
	 */
	public void setActive(final boolean active) {
		this.active = active;
		super.markAsChanged();
	}

	/**
	 * @todo initial physicalLink
	 */
	public PhysicalLink getPhysicalLink() {
		if (this.physicalLink == null)
			this.physicalLink = this.findPhysicalLink();
		return this.physicalLink;
	}

	private PhysicalLink findPhysicalLink() {
		try {
			final StorableObjectCondition condition = new LinkedIdsCondition(this.getId(), ObjectEntities.NODELINK_CODE);

			// NOTE: This call never results in using loader, so it doesn't matter
			// what to pass as 3-d argument
			final Set nlinks = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			final NodeLink nodeLink = (NodeLink) nlinks.iterator().next();
			return nodeLink.getPhysicalLink();
		} catch (ApplicationException e) {
			// TODO how to work it over?!
			e.printStackTrace();
		}
		return null;
		// return this.map.getNodeLink(this).getPhysicalLink();
	}

	public void setPhysicalLink(final PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		// do not change version due to physical link is not dependence object
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final boolean active) {
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
	 * ”становить флаг возможности прив€зки топологического узла к сетевому и/или
	 * неприв€занному узлу.
	 *
	 * @param canBind
	 *          флаг овзможности прив€зки
	 */
	public void setCanBind(final boolean canBind) {
		this.canBind = canBind;
	}

	/**
	 * ѕолучить флаг возможности прив€зки топологического узла к сетевому и/или
	 * неприв€занному узлу.
	 *
	 * @return флаг овзможности прив€зки
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
	public void revert(final MapElementState state) {
		final TopologicalNodeState mpnes = (TopologicalNodeState) state;

		this.setName(mpnes.name);
		this.setDescription(mpnes.description);
		this.setImageId(mpnes.imageId);
		this.setLocation(mpnes.location);
		this.setActive(mpnes.active);
		try {
			this.setPhysicalLink((PhysicalLink) StorableObjectPool.getStorableObject(mpnes.physicalLinkId, false));
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map<String, Object> getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap<String, Object>();
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

	public static TopologicalNode createInstance(final Identifier creatorId, final java.util.Map exportMap1)
			throws CreateObjectException {
		final Identifier id1 = (Identifier) exportMap1.get(StorableObjectWrapper.COLUMN_ID);
		final String name1 = (String) exportMap1.get(StorableObjectWrapper.COLUMN_NAME);
		final String description1 = (String) exportMap1.get(StorableObjectWrapper.COLUMN_DESCRIPTION);
		final Identifier physicalLinkId1 = (Identifier) exportMap1.get(TopologicalNodeWrapper.COLUMN_PHYSICAL_LINK_ID);
		final double x1 = Double.parseDouble((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_X));
		final double y1 = Double.parseDouble((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_Y));
		final boolean active1 = Boolean.valueOf((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_ACTIVE)).booleanValue();

		if (id1 == null || creatorId == null || name1 == null || description1 == null || physicalLinkId1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final PhysicalLink physicalLink1 = (PhysicalLink) StorableObjectPool.getStorableObject(physicalLinkId1, false);
			final TopologicalNode node1 = new TopologicalNode(id1,
					creatorId,
					StorableObjectVersion.createInitial(),
					name1,
					description1,
					x1,
					y1,
					active1);
			node1.setPhysicalLink(physicalLink1);

			assert node1.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			node1.markAsChanged();

			return node1;
		} catch (ApplicationException e) {
			throw new CreateObjectException("Mark.createInstance |  ", e);
		}
	}

	public XmlObject getXMLTransferable() {
		final com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = com.syrus.amficom.map.xml.TopologicalNode.Factory.newInstance();
		this.fillXMLTransferable(xmlTopologicalNode);
		return xmlTopologicalNode;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		final com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = (com.syrus.amficom.map.xml.TopologicalNode) xmlObject;

		final com.syrus.amficom.general.xml.UID uid = xmlTopologicalNode.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlTopologicalNode.setX(this.location.getX());
		xmlTopologicalNode.setY(this.location.getY());
		xmlTopologicalNode.setActive(this.active);
	}

	TopologicalNode(final Identifier creatorId,
			final StorableObjectVersion version,
			final com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(ObjectEntities.TOPOLOGICALNODE_CODE, xmlTopologicalNode.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"",
				new DoublePoint(0, 0));
		this.selected = false;
		this.fromXMLTransferable(xmlTopologicalNode, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		final com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = (com.syrus.amficom.map.xml.TopologicalNode) xmlObject;

		this.active = xmlTopologicalNode.getActive();
		super.location.setLocation(xmlTopologicalNode.getX(), xmlTopologicalNode.getY());
	}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		final com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = (com.syrus.amficom.map.xml.TopologicalNode) xmlObject;

		try {
			final TopologicalNode topologicalNode = new TopologicalNode(creatorId,
					StorableObjectVersion.createInitial(),
					xmlTopologicalNode,
					clonedIdsPool);
			assert topologicalNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			topologicalNode.markAsChanged();
			return topologicalNode;
		} catch (Exception e) {
			throw new CreateObjectException("TopologicalNode.createInstance |  ", e);
		}
	}
}
