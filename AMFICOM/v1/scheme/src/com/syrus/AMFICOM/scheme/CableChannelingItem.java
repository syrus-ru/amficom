/*-
 * $Id: CableChannelingItem.java,v 1.47 2005/07/26 12:52:23 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NO_COMMON_PARENT;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.IdlCableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.IdlCableChannelingItemHelper;
import com.syrus.util.Log;

/**
 * #15 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.47 $, $Date: 2005/07/26 12:52:23 $
 * @module scheme
 */
public final class CableChannelingItem
		extends StorableObject
		implements Comparable<CableChannelingItem>, PathMember<SchemeCableLink, CableChannelingItem> {
	private static final long serialVersionUID = 3256437027796038705L;

	private double startSpare;

	private double endSpare;

	private int rowX;

	private int placeY;

	int sequentialNumber;

	private Identifier physicalLinkId;

	private Identifier startSiteNodeId;

	private Identifier endSiteNodeId;

	Identifier parentSchemeCableLinkId;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	CableChannelingItem(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		try {
			DatabaseContext.getDatabase(CABLECHANNELINGITEM_CODE).retrieve(this);
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
	CableChannelingItem(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final double startSpare,
			final double endSpare,
			final int rowX,
			final int placeY,
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
	public CableChannelingItem(final IdlCableChannelingItem transferable) {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, double, double, int, int, PhysicalLink, SiteNode, SiteNode, SchemeCableLink)}.
	 *
	 * @param creatorId
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static CableChannelingItem createInstance(final Identifier creatorId,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink) throws CreateObjectException {
		return createInstance(creatorId, 0, 0, 0, 0, null, startSiteNode, endSiteNode, parentSchemeCableLink);
	}

	/**
	 * @param creatorId
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param physicalLink
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static CableChannelingItem createInstance(final Identifier creatorId,
			final double startSpare,
			final double endSpare,
			final int rowX,
			final int placeY,
			final PhysicalLink physicalLink,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert startSiteNode != null : NON_NULL_EXPECTED;
		assert endSiteNode != null : NON_NULL_EXPECTED;
		assert parentSchemeCableLink != null : NON_NULL_EXPECTED;
		
		try {
			final Date created = new Date();
			/*
			 * This will work since current object is not
			 * yet put to the pool.
			 */
			final int sequentialNumber = parentSchemeCableLink.getPathMembers().size();
			final CableChannelingItem cableChannelingItem = new CableChannelingItem(IdentifierPool.getGeneratedIdentifier(CABLECHANNELINGITEM_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					startSpare,
					endSpare,
					rowX,
					placeY,
					sequentialNumber,
					physicalLink,
					startSiteNode,
					endSiteNode,
					parentSchemeCableLink);
			cableChannelingItem.markAsChanged();
			return cableChannelingItem;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"CableChanelingItem.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.physicalLinkId != null
				&& this.startSiteNodeId != null
				&& this.endSiteNodeId != null
				&& this.parentSchemeCableLinkId != null : OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.physicalLinkId);
		dependencies.add(this.startSiteNodeId);
		dependencies.add(this.endSiteNodeId);
		dependencies.add(this.parentSchemeCableLinkId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	Identifier getEndSiteNodeId() {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid(): OBJECT_NOT_INITIALIZED;
		assert !this.endSiteNodeId.equals(this.startSiteNodeId): CIRCULAR_DEPS_PROHIBITED;
		assert this.endSiteNodeId.getMajor() == SITENODE_CODE;
		return this.endSiteNodeId;
	}

	/**
	 * A wrapper around {@link #getEndSiteNodeId()}.
	 */
	public SiteNode getEndSiteNode() {
		try {
			return (SiteNode) StorableObjectPool.getStorableObject(this.getEndSiteNodeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public double getEndSpare() {
		return this.endSpare;
	}

	Identifier getParentSchemeCableLinkId() {
		assert this.parentSchemeCableLinkId != null && !this.parentSchemeCableLinkId.isVoid(): OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeCableLinkId.getMajor() == SCHEMECABLELINK_CODE;
		return this.parentSchemeCableLinkId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeCableLinkId()}.
	 * 
	 * @see #getParentSchemeCableLinkId()
	 * @see PathMember#getParentPathOwner()
	 */
	public SchemeCableLink getParentPathOwner() {
		try {
			return (SchemeCableLink) StorableObjectPool.getStorableObject(this.getParentSchemeCableLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getPhysicalLinkId() {
		assert this.physicalLinkId != null: OBJECT_NOT_INITIALIZED;
		assert this.physicalLinkId.isVoid() || this.physicalLinkId.getMajor() == PHYSICALLINK_CODE;
		return this.physicalLinkId;
	}

	/**
	 * A wrapper around {@link #getPhysicalLinkId()}.
	 */
	public PhysicalLink getPhysicalLink() {
		try {
			return (PhysicalLink) StorableObjectPool.getStorableObject(this.getPhysicalLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public int getPlaceY() {
		return this.placeY;
	}

	public int getRowX() {
		return this.rowX;
	}

	/**
	 * @return this <code>CableChannelingItem</code>&apos;s sequential number
	 *         (starting with 0).
	 * @see PathMember#getSequentialNumber()
	 */
	public int getSequentialNumber() {
		assert this.getParentPathOwner().assertContains(this);
		return this.sequentialNumber;
	}

	Identifier getStartSiteNodeId() {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid(): OBJECT_NOT_INITIALIZED;
		assert !this.startSiteNodeId.equals(this.endSiteNodeId): CIRCULAR_DEPS_PROHIBITED;
		assert this.startSiteNodeId.getMajor() == SITENODE_CODE;
		return this.startSiteNodeId;
	}

	/**
	 * A wrapper around {@link #getStartSiteNodeId()}.
	 */
	public SiteNode getStartSiteNode() {
		try {
			return (SiteNode) StorableObjectPool.getStorableObject(this.getStartSiteNodeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public double getStartSpare() {
		return this.startSpare;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableChannelingItem getTransferable(final ORB orb) {
		return IdlCableChannelingItemHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.startSpare,
				this.endSpare,
				this.rowX,
				this.placeY,
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
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final double startSpare,
			final double endSpare,
			final int rowX,
			final int placeY,
			final int sequentialNumber,
			final Identifier physicalLinkId,
			final Identifier startSiteNodeId,
			final Identifier endSiteNodeId,
			final Identifier parentSchemeCableLinkId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert physicalLinkId != null: NON_NULL_EXPECTED;
		assert startSiteNodeId != null && !startSiteNodeId.isVoid(): NON_VOID_EXPECTED;
		assert endSiteNodeId != null && !endSiteNodeId.isVoid(): NON_VOID_EXPECTED;
		assert parentSchemeCableLinkId != null && !parentSchemeCableLinkId.isVoid(): NON_VOID_EXPECTED;

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
				&& !this.endSiteNodeId.isVoid(): OBJECT_NOT_INITIALIZED;
		assert !this.endSiteNodeId.equals(this.startSiteNodeId): CIRCULAR_DEPS_PROHIBITED;
		assert endSiteNode != null: NON_NULL_EXPECTED;
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
	 * <p><em>Removes</em> itself from the old {@code SchemeCableLink}
	 * and <em>adds</em> to the end of the new {@code SchemeCableLink}
	 * if it&apos;s non-{@code null} (accordingly adjusting own
	 * {@link #sequentialNumber sequential number}), otherwise
	 * <em>deletes</em> itself from the pool.</p>
	 *
	 * <p>If {@code processSubsequentSiblings} is {@code true}, the same
	 * operation is undertaken with respect to {@code CableChannelingItem}s
	 * following this one within the old {@code SchemeCableLink}, in order;
	 * otherwise subsequent {@code CableChannelingItem}s are only shifted by
	 * {@code -1}, their parent {@code SchemeCableLink} remaining
	 * unchanged.</p>
	 *
	 * @param parentSchemeCableLink
	 * @param processSubsequentSiblings
	 */
	public void setParentPathOwner(final SchemeCableLink parentSchemeCableLink,
			final boolean processSubsequentSiblings) {
		assert this.parentSchemeCableLinkId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeCableLinkId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		final Identifier newParentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);

		if (this.parentSchemeCableLinkId.equals(newParentSchemeCableLinkId)) {
			return;
		}

		int newSequentialNumber = parentSchemeCableLink == null
				? -1
				: parentSchemeCableLink.getPathMembers().size();
		final Iterator<CableChannelingItem> cableChannelingItemIterator =
				this.getParentPathOwner().getPathMembers().tailSet(this).iterator();
		if (processSubsequentSiblings) {
			while (cableChannelingItemIterator.hasNext()) {
				cableChannelingItemIterator.next().setParentPathOwner(newParentSchemeCableLinkId,
						newSequentialNumber++);
			}
		} else {
			assert cableChannelingItemIterator.hasNext();
			final CableChannelingItem cableChannelingItem = cableChannelingItemIterator.next();
			assert cableChannelingItem == this;

			cableChannelingItem.setParentPathOwner(newParentSchemeCableLinkId,
					newSequentialNumber++);

			while (cableChannelingItemIterator.hasNext()) {
				cableChannelingItemIterator.next().shiftLeft();
			}
		}
	}

	private void setParentPathOwner(final Identifier newParentSchemeCableLinkId,
			final int newSequentialNumber) {
		this.parentSchemeCableLinkId = newParentSchemeCableLinkId;
		super.markAsChanged();
		if (newParentSchemeCableLinkId.isVoid()) {
			this.sequentialNumber = -1;
			StorableObjectPool.delete(super.id);
		} else {
			this.sequentialNumber = newSequentialNumber;
		}
	}

	private void shiftLeft() {
		this.sequentialNumber--;
		super.markAsChanged();
	}

	/**
	 * @todo Remove {@code SuppressWarnings} annotation.
	 */
	@SuppressWarnings("unused")
	private void shiftRight() {
		this.sequentialNumber++;
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

	/**
	 * @param startSiteNode
	 */
	public void setStartSiteNode(final SiteNode startSiteNode) {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid(): OBJECT_NOT_INITIALIZED;
		assert !this.startSiteNodeId.equals(this.endSiteNodeId): CIRCULAR_DEPS_PROHIBITED;
		assert startSiteNode != null: NON_NULL_EXPECTED;
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
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		final IdlCableChannelingItem cableChannelingItem = (IdlCableChannelingItem) transferable;
		try {
			super.fromTransferable(cableChannelingItem);
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

	/**
	 * @param that
	 */
	public int compareTo(final CableChannelingItem that) {
		assert this.parentSchemeCableLinkId.equals(that.parentSchemeCableLinkId) : NO_COMMON_PARENT;
		return this.sequentialNumber <= that.sequentialNumber ? this.sequentialNumber < that.sequentialNumber ? -1 : 0 : 1;
	}

	/**
	 * @todo Remove {@code SuppressWarnings} annotation.
	 */
	public void insertSelfBefore(@SuppressWarnings("unused") final CableChannelingItem sibling) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @todo Remove {@code SuppressWarnings} annotation.
	 */
	public void insertSelfAfter(@SuppressWarnings("unused") final CableChannelingItem sibling) {
		throw new UnsupportedOperationException();
	}
}
