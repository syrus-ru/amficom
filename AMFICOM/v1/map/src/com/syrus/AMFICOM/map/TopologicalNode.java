/*-
 * $Id: TopologicalNode.java,v 1.74 2005/09/05 13:41:20 krupenn Exp $
 *
 * Copyright њ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNode;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNodeHelper;
import com.syrus.AMFICOM.map.xml.XmlTopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * “опологический узел нв топологической схеме. “опологический узел может
 * быть концевым дл€ линии и дл€ фрагмента линии. ¬ физическом смысле
 * топологический узел соответствует точке изгиба линии и не требует
 * дополнительной описательной информации.
 * @author $Author: krupenn $
 * @version $Revision: 1.74 $, $Date: 2005/09/05 13:41:20 $
 * @module map
 */
public final class TopologicalNode extends AbstractNode implements XmlBeansTransferable<XmlTopologicalNode> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258130254244885554L;

	/**
	 * ‘лаг показывающий закрыт ли узел true значит что из узла выходит две линии,
	 * false одна
	 */
	private boolean active;

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private transient boolean canBind = false;

	TopologicalNode(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(TOPOLOGICALNODE_CODE).retrieve(this);
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
		this.selected = false;
	}

	protected static TopologicalNode createInstance0(final Identifier creatorId,
			final String name,
			final String description,
			final DoublePoint location) throws CreateObjectException {

		if (creatorId == null || name == null || description == null || location == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final TopologicalNode topologicalNode = new TopologicalNode(IdentifierPool.getGeneratedIdentifier(TOPOLOGICALNODE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					location.getX(),
					location.getY(),
					false);

			assert topologicalNode.isValid() : OBJECT_STATE_ILLEGAL;

			topologicalNode.markAsChanged();

			return topologicalNode;

		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final DoublePoint location) throws CreateObjectException {
		return TopologicalNode.createInstance0(creatorId, name, description, location);
	}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final DoublePoint location) throws CreateObjectException {
		return TopologicalNode.createInstance0(creatorId, "", "", location);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
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
		try {
			final StorableObjectCondition condition = new LinkedIdsCondition(this.getId(), NODELINK_CODE);

			// NOTE: This call never results in using loader, so it doesn't matter
			// what to pass as 3-d argument
			final Set<NodeLink> nlinks = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			if(nlinks ==null || nlinks.size() == 0) {
				return null;
			}
			final NodeLink nodeLink = nlinks.iterator().next();
			return nodeLink.getPhysicalLink();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return null;
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
	}

	public XmlTopologicalNode getXmlTransferable() {
		final XmlTopologicalNode xmlTopologicalNode = XmlTopologicalNode.Factory.newInstance();
		xmlTopologicalNode.setId(this.id.getXmlTransferable());
		xmlTopologicalNode.setX(this.location.getX());
		xmlTopologicalNode.setY(this.location.getY());
		xmlTopologicalNode.setActive(this.active);
		return xmlTopologicalNode;
	}

	TopologicalNode(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlTopologicalNode xmlTopologicalNode,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(TOPOLOGICALNODE_CODE, xmlTopologicalNode.getId()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"",
				new DoublePoint(0, 0));
		this.selected = false;
		this.fromXmlTransferable(xmlTopologicalNode, clonedIdsPool, importType);
	}

	public void fromXmlTransferable(final XmlTopologicalNode xmlTopologicalNode, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException {
		this.active = xmlTopologicalNode.getActive();
		super.location.setLocation(xmlTopologicalNode.getX(), xmlTopologicalNode.getY());
	}

	public static TopologicalNode createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlTopologicalNode xmlTopologicalNode,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		try {
			final XmlIdentifier xmlId = xmlTopologicalNode.getId();
			Identifier existingIdentifier = Identifier.fromXmlTransferable(xmlId, importType);
			TopologicalNode topologicalNode = null;
			if(existingIdentifier != null) {
				topologicalNode = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(topologicalNode != null) {
					clonedIdsPool.setExistingId(xmlId, existingIdentifier);
					topologicalNode.fromXmlTransferable(xmlTopologicalNode, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, xmlId);
				}
			}
			if(topologicalNode == null) {
				topologicalNode = new TopologicalNode(creatorId,
						StorableObjectVersion.createInitial(),
						xmlTopologicalNode,
						clonedIdsPool, 
						importType);
				ImportUidMapDatabase.insert(importType, xmlId, topologicalNode.id);
			}
			assert topologicalNode.isValid() : OBJECT_STATE_ILLEGAL;
			topologicalNode.markAsChanged();
			return topologicalNode;
		} catch (Exception e) {
			throw new CreateObjectException("TopologicalNode.createInstance |  ", e);
		}
	}
}
