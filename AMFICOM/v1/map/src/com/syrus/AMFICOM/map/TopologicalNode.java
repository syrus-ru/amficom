/*-
 * $Id: TopologicalNode.java,v 1.100 2006/03/15 14:47:33 bass Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNode;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNodeHelper;
import com.syrus.AMFICOM.map.xml.XmlTopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * ?????????????? ???? ?? ?????????????? ?????. ?????????????? ???? ?????
 * ???? ???????? ??? ????? ? ??? ????????? ?????. ? ?????????? ??????
 * ?????????????? ???? ????????????? ????? ?????? ????? ? ?? ???????
 * ?????????????? ???????????? ??????????.
 * @author $Author: bass $
 * @version $Revision: 1.100 $, $Date: 2006/03/15 14:47:33 $
 * @module map
 */
public final class TopologicalNode extends AbstractNode
		implements XmlTransferableObject<XmlTopologicalNode>,
		IdlTransferableObjectExt<IdlTopologicalNode> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258130254244885554L;

	/**
	 * ???? ???????????? ?????? ?? ???? true ?????? ??? ?? ???? ??????? ??? ?????,
	 * false ????
	 */
	private boolean active;

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private transient boolean canBind = false;

	public TopologicalNode(final IdlTopologicalNode tnt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(tnt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
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
					INITIAL_VERSION,
					name,
					description,
					location.getX(),
					location.getY(),
					false);

			assert topologicalNode.isValid() : OBJECT_BADLY_INITIALIZED;

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
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		return Collections.emptySet();
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTopologicalNode getIdlTransferable(final ORB orb) {
		return IdlTopologicalNodeHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.active);
	}
	
	public synchronized void fromIdlTransferable(final IdlTopologicalNode idlTopologicalNode)
	throws IdlConversionException {
		super.fromIdlTransferable(idlTopologicalNode);
		this.active = idlTopologicalNode.active;
	}

	public boolean isActive() {
		return this.active;
	}

	/**
	 * ?????????? ?????????? ??????????????? ????. ???? ???????, ???? ?? ?????????
	 * ? ???????? ?????, ? ?? ???????, ???? ?? ????????? ?? ????? ?????. ????????
	 * ? ?????????? ?????????????? ???? ???????????? ??????? ????????
	 *
	 * @param active
	 *          ???? ??????????
	 */
	public void setActive(final boolean active) {
		this.active = active;
		super.markAsChanged();
	}

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
	 * ?????????? ???? ??????????? ???????? ??????????????? ???? ? ???????? ?/???
	 * ?????????????? ????.
	 *
	 * @param canBind
	 *          ???? ??????????? ????????
	 */
	public void setCanBind(final boolean canBind) {
		this.canBind = canBind;
	}

	/**
	 * ???????? ???? ??????????? ???????? ??????????????? ???? ? ???????? ?/???
	 * ?????????????? ????.
	 *
	 * @return ???? ??????????? ????????
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

	/**
	 * @param topologicalNode
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlTopologicalNode topologicalNode,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		this.id.getXmlTransferable(topologicalNode.addNewId(), importType);
		topologicalNode.setX(this.location.getX());
		topologicalNode.setY(this.location.getY());
		topologicalNode.setActive(this.active);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private TopologicalNode(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, TOPOLOGICALNODE_CODE, created, creatorId);
		/**
		 * @todo Should go to #fromXmlTransferable(...) or
		 *       the corresponding complementor.
		 */
		this.name = "";
		this.description = "";
		this.location = new DoublePoint(.0, .0);
		this.selected = false;
	}

	public void fromXmlTransferable(
			final XmlTopologicalNode xmlTopologicalNode,
			final String importType)
	throws XmlConversionException {
		this.active = xmlTopologicalNode.getActive();
		super.location.setLocation(xmlTopologicalNode.getX(), xmlTopologicalNode.getY());
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlTopologicalNode
	 * @throws CreateObjectException
	 */
	public static TopologicalNode createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlTopologicalNode xmlTopologicalNode)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlTopologicalNode.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			TopologicalNode topologicalNode;
			if (id.isVoid()) {
				topologicalNode = new TopologicalNode(xmlId,
						importType,
						created,
						creatorId);
			} else {
				topologicalNode = StorableObjectPool.getStorableObject(id, true);
				if (topologicalNode == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					topologicalNode = new TopologicalNode(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			topologicalNode.fromXmlTransferable(xmlTopologicalNode, importType);
			assert topologicalNode.isValid() : OBJECT_BADLY_INITIALIZED;
			topologicalNode.markAsChanged();
			return topologicalNode;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	public Characterizable getCharacterizable() {
		return null;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected TopologicalNodeWrapper getWrapper() {
		return TopologicalNodeWrapper.getInstance();
	}
}
