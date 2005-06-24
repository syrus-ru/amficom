/*-
 * $Id: CableChannelingItem.java,v 1.31 2005/06/24 14:13:38 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.IdlCableChannelingItem;
import com.syrus.util.Log;

/**
 * #13 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.31 $, $Date: 2005/06/24 14:13:38 $
 * @module scheme_v1
 */
public final class CableChannelingItem extends AbstractCloneableStorableObject {
	private static final long serialVersionUID = 3256437027796038705L;

	private double startSpare;

	private double endSpare;

	private int rowX;

	private int placeY;

	private int sequentialNumber;

	private Identifier physicalLinkId;

	private Identifier startSiteNodeId;

	private Identifier endSiteNodeId;

	private Identifier parentSchemeCableLinkId;

	private CableChannelingItemDatabase cableChannelingItemDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	CableChannelingItem(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		this.cableChannelingItemDatabase = (CableChannelingItemDatabase) DatabaseContext.getDatabase(ObjectEntities.CABLECHANNELINGITEM_CODE);
		try {
			this.cableChannelingItemDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param sequentialNumber
	 * @param physicalLink
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 */
	CableChannelingItem(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final double startSpare, final double endSpare,
			final int rowX, final int placeY,
			final int sequentialNumber,
			final PhysicalLink physicalLink,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink) {
		super(id, created, modified, creatorId, modifierId, version);

		this.startSpare = startSpare;
		this.endSpare = endSpare;
		this.rowX = rowX;
		this.placeY = placeY;
		this.sequentialNumber = sequentialNumber;
		this.physicalLinkId = Identifier.possiblyVoid(physicalLink);
		this.startSiteNodeId = Identifier.possiblyVoid(startSiteNode);
		this.endSiteNodeId = Identifier.possiblyVoid(endSiteNode);
		this.parentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);
	}

	/**
	 * @param transferable
	 */
	CableChannelingItem(final IdlCableChannelingItem transferable) {
		this.cableChannelingItemDatabase = (CableChannelingItemDatabase) DatabaseContext.getDatabase(ObjectEntities.CABLECHANNELINGITEM_CODE);
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, double, double, int, int, int, PhysicalLink, SiteNode, SiteNode, SchemeCableLink)}.
	 *
	 * @param creatorId
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static CableChannelingItem createInstance(
			final Identifier creatorId,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink)
			throws CreateObjectException {
		return createInstance(creatorId, 0, 0, 0, 0, 0, null,
				startSiteNode, endSiteNode,
				parentSchemeCableLink);
	}

	/**
	 * @param creatorId
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param sequentialNumber
	 * @param physicalLink
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static CableChannelingItem createInstance(
			final Identifier creatorId, final double startSpare,
			final double endSpare, final int rowX,
			final int placeY, final int sequentialNumber,
			final PhysicalLink physicalLink,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert startSiteNode != null: ErrorMessages.NON_NULL_EXPECTED;
		assert endSiteNode != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeCableLink != null: ErrorMessages.NON_NULL_EXPECTED;
		
		try {
			final Date created = new Date();
			final CableChannelingItem cableChannelingItem = new CableChannelingItem(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.CABLECHANNELINGITEM_CODE),
					created, created, creatorId, creatorId,
					0L, startSpare, endSpare, rowX, placeY,
					sequentialNumber, physicalLink,
					startSiteNode, endSiteNode,
					parentSchemeCableLink);
			cableChannelingItem.markAsChanged();
			return cableChannelingItem;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"CableChanelingItem.createInstance | cannot generate identifier ", ige);
		}
	}

	@Override
	public CableChannelingItem clone() {
		final CableChannelingItem cableChannelingItem = (CableChannelingItem) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return cableChannelingItem;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.physicalLinkId != null
				&& this.startSiteNodeId != null
				&& this.endSiteNodeId != null
				&& this.parentSchemeCableLinkId != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.add(this.physicalLinkId);
		dependencies.add(this.startSiteNodeId);
		dependencies.add(this.endSiteNodeId);
		dependencies.add(this.parentSchemeCableLinkId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	public SiteNode getEndSiteNode() {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid(): ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.endSiteNodeId.equals(this.startSiteNodeId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;

		try {
			return (SiteNode) StorableObjectPool.getStorableObject(this.endSiteNodeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public double getEndSpare() {
		return this.endSpare;
	}

	public SchemeCableLink getParentSchemeCableLink() {
		assert this.parentSchemeCableLinkId != null: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert !this.parentSchemeCableLinkId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		try {
			return (SchemeCableLink) StorableObjectPool.getStorableObject(this.parentSchemeCableLinkId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public PhysicalLink getPhysicalLink() {
		assert this.physicalLinkId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (PhysicalLink) StorableObjectPool.getStorableObject(this.physicalLinkId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public int getPlaceY() {
		return this.placeY;
	}

	public int getRowX() {
		return this.rowX;
	}

	public int getSequentialNumber() {
		return this.sequentialNumber;
	}

	public SiteNode getStartSiteNode() {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid(): ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.startSiteNodeId.equals(this.endSiteNodeId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;

		try {
			return (SiteNode) StorableObjectPool.getStorableObject(this.startSiteNodeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public double getStartSpare() {
		return this.startSpare;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IdlCableChannelingItem getTransferable() {
		return new IdlCableChannelingItem(
				super.getHeaderTransferable(), this.startSpare,
				this.endSpare, this.rowX, this.placeY,
				this.sequentialNumber,
				this.physicalLinkId.getTransferable(),
				this.startSiteNodeId.getTransferable(),
				this.endSiteNodeId.getTransferable(),
				this.parentSchemeCableLinkId.getTransferable());
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param sequentialNumber
	 * @param physicalLinkId
	 * @param startSiteNodeId
	 * @param endSiteNodeId
	 * @param parentSchemeCableLinkId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final double startSpare, final double endSpare,
			final int rowX, final int placeY,
			final int sequentialNumber,
			final Identifier physicalLinkId,
			final Identifier startSiteNodeId,
			final Identifier endSiteNodeId,
			final Identifier parentSchemeCableLinkId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert physicalLinkId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert startSiteNodeId != null && !startSiteNodeId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert endSiteNodeId != null && !endSiteNodeId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert parentSchemeCableLinkId != null && !parentSchemeCableLinkId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;

		this.startSpare = startSpare;
		this.endSpare = endSpare;
		this.rowX = rowX;
		this.placeY = placeY;
		this.sequentialNumber = sequentialNumber;
		this.physicalLinkId = physicalLinkId;
		this.startSiteNodeId = startSiteNodeId;
		this.endSiteNodeId = endSiteNodeId;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
	}

	/**
	 * @param endSiteNode
	 */
	public void setEndSiteNode(final SiteNode endSiteNode) {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid(): ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.endSiteNodeId.equals(this.startSiteNodeId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		assert endSiteNode != null: ErrorMessages.NON_NULL_EXPECTED;
		final Identifier newEndSiteNodeId = endSiteNode.getId();
		if (this.endSiteNodeId.equals(newEndSiteNodeId))
			return;
		this.endSiteNodeId = newEndSiteNodeId;
		super.markAsChanged();
	}

	public void setEndSpare(final double endSpare) {
		if (this.endSpare == endSpare)
			return;
		this.endSpare = endSpare;
		super.markAsChanged();
	}

	/**
	 * @param parentSchemeCableLink
	 */
	public void setParentSchemeCableLink(final SchemeCableLink parentSchemeCableLink) {
		assert this.parentSchemeCableLinkId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeCableLinkId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeCableLink == null) {
			Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		final Identifier newParentSchemeCableLinkId = parentSchemeCableLink.getId();
		if (this.parentSchemeCableLinkId.equals(newParentSchemeCableLinkId))
			return;
		this.parentSchemeCableLinkId = newParentSchemeCableLinkId;
		super.markAsChanged();
	}

	/**
	 * @param physicalLink
	 */
	public void setPhysicalLink(final PhysicalLink physicalLink) {
		final Identifier newPhysicalLinkId = Identifier.possiblyVoid(physicalLink);
		if (this.physicalLinkId.equals(newPhysicalLinkId))
			return;
		this.physicalLinkId = newPhysicalLinkId;
		super.markAsChanged();
	}

	public void setPlaceY(final int placeY) {
		if (this.placeY == placeY)
			return;
		this.placeY = placeY;
		super.markAsChanged();
	}

	public void setRowX(final int rowX) {
		if (this.rowX == rowX)
			return;
		this.rowX = rowX;
		super.markAsChanged();
	}

	public void setSequentialNumber(final int sequentialNumber) {
		if (this.sequentialNumber == sequentialNumber)
			return;
		this.sequentialNumber = sequentialNumber;
		super.markAsChanged();
	}

	/**
	 * @param startSiteNode
	 */
	public void setStartSiteNode(final SiteNode startSiteNode) {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid(): ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.startSiteNodeId.equals(this.endSiteNodeId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		assert startSiteNode != null: ErrorMessages.NON_NULL_EXPECTED;
		final Identifier newStartSiteNodeId = startSiteNode.getId();
		if (this.startSiteNodeId.equals(newStartSiteNodeId))
			return;
		this.startSiteNodeId = newStartSiteNodeId;
		super.markAsChanged();
	}

	public void setStartSpare(final double startSpare) {
		if (this.startSpare == startSpare)
			return;
		this.startSpare = startSpare;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) {
		final IdlCableChannelingItem cableChannelingItem = (IdlCableChannelingItem) transferable;
		try {
			super.fromTransferable(cableChannelingItem.header);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false;
		}
		this.startSpare = cableChannelingItem.startSpare;
		this.endSpare = cableChannelingItem.endSpare;
		this.rowX = cableChannelingItem.rowX;
		this.placeY = cableChannelingItem.placeY;
		this.sequentialNumber = cableChannelingItem.sequentialNumber;
		this.physicalLinkId = new Identifier(cableChannelingItem.physicalLinkId);
		this.startSiteNodeId = new Identifier(cableChannelingItem.startSiteNodeId);
		this.endSiteNodeId = new Identifier(cableChannelingItem.endSiteNodeId);
		this.parentSchemeCableLinkId = new Identifier(cableChannelingItem.parentSchemeCableLinkId);
	}
}
