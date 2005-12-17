/*-
 * $Id: CableChannelingItem.java,v 1.88 2005/12/17 12:11:19 arseniy Exp $
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
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PIPEBLOCK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.IdlCableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.IdlCableChannelingItemHelper;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItem;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #15 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.88 $, $Date: 2005/12/17 12:11:19 $
 * @module scheme
 */
public final class CableChannelingItem
		extends StorableObject<CableChannelingItem>
		implements Comparable<CableChannelingItem>,
		PathMember<SchemeCableLink, CableChannelingItem>,
		ReverseDependencyContainer,
		XmlTransferableObject<XmlCableChannelingItem>{
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
	 * @param pipeBlock
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
			final PipeBlock pipeBlock,
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

		this.pipeBlockId = Identifier.possiblyVoid(this.pipeBlock = pipeBlock);

		this.startSiteNodeId = Identifier.possiblyVoid(startSiteNode);
		this.endSiteNodeId = Identifier.possiblyVoid(endSiteNode);
		this.parentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);
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
	private CableChannelingItem(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, CABLECHANNELINGITEM_CODE, created, creatorId);

		this.pipeBlockId = VOID_IDENTIFIER;
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public CableChannelingItem(final IdlCableChannelingItem transferable)
	throws CreateObjectException {
		this.pipeBlockId = VOID_IDENTIFIER;

		try {
			this.fromTransferable(transferable);
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, double, double, int, int, PhysicalLink, PipeBlock, SiteNode, SiteNode, SchemeCableLink)}.
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
		return createInstance(creatorId, 0, 0, 0, 0, null, null, startSiteNode, endSiteNode, parentSchemeCableLink);
	}

	/**
	 * @param creatorId
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param physicalLink
	 * @param pipeBlock
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static CableChannelingItem createInstance(final Identifier creatorId,
			final double startSpare,
			final double endSpare,
			final int rowX,
			final int placeY,
			final PhysicalLink physicalLink,
			final PipeBlock pipeBlock,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink)
	throws CreateObjectException {
		final boolean usePool = false;

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
					StorableObjectVersion.INITIAL_VERSION,
					startSpare,
					endSpare,
					rowX,
					placeY,
					sequentialNumber,
					physicalLink,
					pipeBlock,
					startSiteNode,
					endSiteNode,
					parentSchemeCableLink);
			parentSchemeCableLink.getCableChannelingItemContainerWrappee().addToCache(cableChannelingItem, usePool);

			cableChannelingItem.markAsChanged();
			return cableChannelingItem;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("CableChannelingItem.createInstance() | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlCableChannelingItem
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static CableChannelingItem createInstance(
			final Identifier creatorId,
			final XmlCableChannelingItem xmlCableChannelingItem,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlCableChannelingItem.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			CableChannelingItem cableChannelingItem;
			if (id.isVoid()) {
				cableChannelingItem = new CableChannelingItem(xmlId,
						importType,
						created,
						creatorId);
			} else {
				cableChannelingItem = StorableObjectPool.getStorableObject(id, true);
				if (cableChannelingItem == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					cableChannelingItem = new CableChannelingItem(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			cableChannelingItem.fromXmlTransferable(xmlCableChannelingItem, importType);
			assert cableChannelingItem.isValid() : OBJECT_BADLY_INITIALIZED;
			cableChannelingItem.markAsChanged();
			return cableChannelingItem;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependenciesTmpl()
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.physicalLinkId != null
				&& this.pipeBlockId != null
				&& this.startSiteNodeId != null
				&& this.endSiteNodeId != null
				&& this.parentSchemeCableLinkId != null : OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.physicalLinkId);
		dependencies.add(this.pipeBlockId);
		dependencies.add(this.startSiteNodeId);
		dependencies.add(this.endSiteNodeId);
		dependencies.add(this.parentSchemeCableLinkId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) {
		return Collections.<Identifiable>singleton(super.id);
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
			return StorableObjectPool.getStorableObject(this.getEndSiteNodeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
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
			return StorableObjectPool.getStorableObject(this.getParentSchemeCableLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
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
			return StorableObjectPool.getStorableObject(this.getPhysicalLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
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

	/**
	 * @param sequentialNumber
	 */
	private void setSequentialNumber(final int sequentialNumber) {
		if (this.sequentialNumber == sequentialNumber) {
			return;
		}
		this.sequentialNumber = sequentialNumber;
		super.markAsChanged();
	}

	public Identifier getStartSiteNodeId() {
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
			return StorableObjectPool.getStorableObject(this.getStartSiteNodeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	public double getStartSpare() {
		return this.startSpare;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableChannelingItem getIdlTransferable(final ORB orb) {
		return IdlCableChannelingItemHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.startSpare,
				this.endSpare,
				this.rowX,
				this.placeY,
				this.sequentialNumber,
				this.physicalLinkId.getIdlTransferable(),
				this.pipeBlockId.getIdlTransferable(),
				this.startSiteNodeId.getIdlTransferable(),
				this.endSiteNodeId.getIdlTransferable(),
				this.parentSchemeCableLinkId.getIdlTransferable());
	}

	/**
	 * @param cableChannelingItem
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlCableChannelingItem cableChannelingItem,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			super.id.getXmlTransferable(cableChannelingItem.addNewId(), importType);
			cableChannelingItem.setStartSpare(this.startSpare);
			cableChannelingItem.setEndSpare(this.endSpare);
			cableChannelingItem.setRowX(this.rowX);
			cableChannelingItem.setPlaceY(this.placeY);
			cableChannelingItem.setSequentialNumber(this.sequentialNumber);
			if (cableChannelingItem.isSetPhysicalLinkId()) {
				cableChannelingItem.unsetPhysicalLinkId();
			}
			if (!this.physicalLinkId.isVoid()) {
				this.physicalLinkId.getXmlTransferable(cableChannelingItem.addNewPhysicalLinkId(), importType);
			}
			if (cableChannelingItem.isSetPipeBlockId()) {
				cableChannelingItem.unsetPipeBlockId();
			}
			if (!this.pipeBlockId.isVoid()) {
				this.pipeBlockId.getXmlTransferable(cableChannelingItem.addNewPipeBlockId(), importType);
			}
			this.startSiteNodeId.getXmlTransferable(cableChannelingItem.addNewStartSiteNodeId(), importType);
			this.endSiteNodeId.getXmlTransferable(cableChannelingItem.addNewEndSiteNodeId(), importType);
			this.parentSchemeCableLinkId.getXmlTransferable(cableChannelingItem.addNewParentSchemeCableLinkId(), importType);
			XmlComplementorRegistry.complementStorableObject(cableChannelingItem, CABLECHANNELINGITEM_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
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
	 * @param pipeBlockId
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
			final Identifier pipeBlockId,
			final Identifier startSiteNodeId,
			final Identifier endSiteNodeId,
			final Identifier parentSchemeCableLinkId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert physicalLinkId != null: NON_NULL_EXPECTED;
		assert pipeBlockId != null : NON_NULL_EXPECTED;
		assert startSiteNodeId != null && !startSiteNodeId.isVoid(): NON_VOID_EXPECTED;
		assert endSiteNodeId != null && !endSiteNodeId.isVoid(): NON_VOID_EXPECTED;
		assert parentSchemeCableLinkId != null && !parentSchemeCableLinkId.isVoid(): NON_VOID_EXPECTED;

		this.startSpare = startSpare;
		this.endSpare = endSpare;
		this.rowX = rowX;
		this.placeY = placeY;
		this.sequentialNumber = sequentialNumber;
		this.physicalLinkId = physicalLinkId;
		this.pipeBlockId = pipeBlockId;
		this.startSiteNodeId = startSiteNodeId;
		this.endSiteNodeId = endSiteNodeId;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
	}

	/**
	 * @param endSiteNodeId
	 */
	void setEndSiteNodeId(final Identifier endSiteNodeId) {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid() : OBJECT_NOT_INITIALIZED;
		assert !this.endSiteNodeId.equals(this.startSiteNodeId) : CIRCULAR_DEPS_PROHIBITED;

		assert endSiteNodeId != null : NON_NULL_EXPECTED;
		assert !endSiteNodeId.isVoid() : NON_VOID_EXPECTED;
		assert endSiteNodeId.getMajor() == SITENODE_CODE;

		if (this.endSiteNodeId.equals(endSiteNodeId)) {
			return;
		}
		this.endSiteNodeId = endSiteNodeId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setEndSiteNodeId(Identifier)}.
	 *
	 * @param endSiteNode
	 */
	public void setEndSiteNode(final SiteNode endSiteNode) {
		this.setEndSiteNodeId(Identifier.possiblyVoid(endSiteNode));
	}

	public void setEndSpare(final double endSpare) {
		if (this.endSpare == endSpare)
			return;
		this.endSpare = endSpare;
		super.markAsChanged();
	}

	/**
	 * @param parentSchemeCableLinkId
	 * @throws ApplicationException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	void setParentSchemeCableLinkId(final Identifier parentSchemeCableLinkId)
	throws ApplicationException {
		final boolean usePool = false;

		assert parentSchemeCableLinkId != null : NON_NULL_EXPECTED;
		assert parentSchemeCableLinkId.isVoid() || parentSchemeCableLinkId.getMajor() == SCHEMECABLELINK_CODE;

		if (this.parentSchemeCableLinkId.equals(parentSchemeCableLinkId)) {
			return;
		}

		this.getParentPathOwner().getCableChannelingItemContainerWrappee().removeFromCache(this, usePool);

		if (parentSchemeCableLinkId.isVoid()) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			StorableObjectPool.<SchemeCableLink>getStorableObject(parentSchemeCableLinkId, true).getCableChannelingItemContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
		this.markAsChanged();
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
	 * @throws ApplicationException
	 */
	public void setParentPathOwner(final SchemeCableLink parentSchemeCableLink,
			final boolean processSubsequentSiblings)
	throws ApplicationException {
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

	/**
	 * A wrapper around {@link #setParentSchemeCableLinkId(Identifier)}.
	 *
	 * @param newParentSchemeCableLinkId
	 * @param newSequentialNumber
	 * @throws ApplicationException
	 */
	private void setParentPathOwner(final Identifier newParentSchemeCableLinkId,
			final int newSequentialNumber)
	throws ApplicationException {
		this.setSequentialNumber(newParentSchemeCableLinkId.isVoid()
				? -1
				: newSequentialNumber);
		this.setParentSchemeCableLinkId(newParentSchemeCableLinkId);
	}

	private void shiftLeft() {
		this.sequentialNumber--;
		super.markAsChanged();
	}

	private void shiftRight() {
		this.sequentialNumber++;
		super.markAsChanged();
	}

	/**
	 * @param physicalLinkId
	 */
	void setPhysicalLinkId(final Identifier physicalLinkId) {
		assert physicalLinkId != null : NON_NULL_EXPECTED;
		assert physicalLinkId.isVoid() || physicalLinkId.getMajor() == PHYSICALLINK_CODE;

		if (this.physicalLinkId.equals(physicalLinkId)) {
			return;
		}
		this.physicalLinkId = physicalLinkId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setPhysicalLinkId(Identifier)}.
	 *
	 * @param physicalLink
	 */
	public void setPhysicalLink(final PhysicalLink physicalLink) {
		this.setPhysicalLinkId(Identifier.possiblyVoid(physicalLink));
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
	 * @param startSiteNodeId
	 */
	void setStartSiteNodeId(final Identifier startSiteNodeId) {
		assert this.startSiteNodeId != null
				&& !this.startSiteNodeId.isVoid()
				&& this.endSiteNodeId != null
				&& !this.endSiteNodeId.isVoid() : OBJECT_NOT_INITIALIZED;
		assert !this.startSiteNodeId.equals(this.endSiteNodeId) : CIRCULAR_DEPS_PROHIBITED;

		assert startSiteNodeId != null : NON_NULL_EXPECTED;
		assert !startSiteNodeId.isVoid() : NON_VOID_EXPECTED;
		assert startSiteNodeId.getMajor() == SITENODE_CODE;

		if (this.startSiteNodeId.equals(startSiteNodeId)) {
			return;
		}
		this.startSiteNodeId = startSiteNodeId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setStartSiteNodeId(Identifier)}
	 *
	 * @param startSiteNode
	 */
	public void setStartSiteNode(final SiteNode startSiteNode) {
		this.setStartSiteNodeId(Identifier.possiblyVoid(startSiteNode));
	}

	public void setStartSpare(final double startSpare) {
		if (this.startSpare == startSpare)
			return;
		this.startSpare = startSpare;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		synchronized (this) {
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
			this.setPipeBlockId0(Identifier.valueOf(cableChannelingItem.pipeBlockId));
			this.startSiteNodeId = new Identifier(cableChannelingItem.startSiteNodeId);
			this.endSiteNodeId = new Identifier(cableChannelingItem.endSiteNodeId);
			this.parentSchemeCableLinkId = new Identifier(cableChannelingItem.parentSchemeCableLinkId);
		}
	}

	/**
	 * @param cableChannelingItem
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlCableChannelingItem cableChannelingItem,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(cableChannelingItem, CABLECHANNELINGITEM_CODE, importType, PRE_IMPORT);
	
			this.startSpare = cableChannelingItem.getStartSpare();
			this.endSpare = cableChannelingItem.getEndSpare();
			this.rowX = cableChannelingItem.getRowX();
			this.placeY = cableChannelingItem.getPlaceY();
			this.sequentialNumber = cableChannelingItem.getSequentialNumber();
			this.physicalLinkId = cableChannelingItem.isSetPhysicalLinkId()
					? Identifier.fromXmlTransferable(cableChannelingItem.getPhysicalLinkId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			this.setPipeBlockId0(cableChannelingItem.isSetPipeBlockId()
					? Identifier.fromXmlTransferable(cableChannelingItem.getPipeBlockId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER);
			this.startSiteNodeId = Identifier.fromXmlTransferable(cableChannelingItem.getStartSiteNodeId(), importType, MODE_THROW_IF_ABSENT);
			this.endSiteNodeId = Identifier.fromXmlTransferable(cableChannelingItem.getEndSiteNodeId(), importType, MODE_THROW_IF_ABSENT);
			this.parentSchemeCableLinkId = Identifier.fromXmlTransferable(cableChannelingItem.getParentSchemeCableLinkId(), importType, MODE_THROW_IF_ABSENT);
	
			XmlComplementorRegistry.complementStorableObject(cableChannelingItem, CABLECHANNELINGITEM_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @param that
	 */
	public int compareTo(final CableChannelingItem that) {
		assert this.parentSchemeCableLinkId.equals(that.parentSchemeCableLinkId) : NO_COMMON_PARENT;
		return this.sequentialNumber <= that.sequentialNumber ? this.sequentialNumber < that.sequentialNumber ? -1 : 0 : 1;
	}

	/**
	 * @param that
	 * @throws ApplicationException
	 */
	public void insertSelfBefore(final CableChannelingItem that) throws ApplicationException {
		assert that != null : NON_NULL_EXPECTED;

		if (this == that || this.equals(that)) {
			return;
		}

		final SchemeCableLink parentSchemeCableLink = this.getParentPathOwner();
		assert parentSchemeCableLink.equals(that.getParentSchemeCableLinkId());

		final int thatSequentialNumber = that.getSequentialNumber();
		assert this.sequentialNumber != thatSequentialNumber;

		if (thatSequentialNumber - this.sequentialNumber == 1) {
			/*-
			 * This one is already situated immediately before that.
			 */
			return;
		}

		final SortedSet<CableChannelingItem> cableChannelingItems = parentSchemeCableLink.getPathMembers0();
		if (this.sequentialNumber < thatSequentialNumber) {
			final SortedSet<CableChannelingItem> toShiftLeft = cableChannelingItems.subSet(this, that);
			toShiftLeft.remove(this);
			for (final CableChannelingItem cableChannelingItem : toShiftLeft) {
				cableChannelingItem.shiftLeft();
			}
			this.sequentialNumber = thatSequentialNumber - 1;
		} else {
			final SortedSet<CableChannelingItem> toShiftRight = cableChannelingItems.subSet(that, this);
			for (final CableChannelingItem cableChannelingItem : toShiftRight) {
				cableChannelingItem.shiftRight();
			}
			this.sequentialNumber = thatSequentialNumber;
		}
		super.markAsChanged();
	}

	/**
	 * @param that
	 * @throws ApplicationException
	 */
	public void insertSelfAfter(final CableChannelingItem that) throws ApplicationException {
		assert that != null : NON_NULL_EXPECTED;

		if (this == that || this.equals(that)) {
			return;
		}

		final SchemeCableLink parentSchemeCableLink = this.getParentPathOwner();
		assert parentSchemeCableLink.equals(that.getParentSchemeCableLinkId());

		final int thatSequentialNumber = that.getSequentialNumber();
		assert this.sequentialNumber != thatSequentialNumber;

		if (this.sequentialNumber - thatSequentialNumber == 1) {
			/*-
			 * This one is already situated immediately after that.
			 */
			return;
		}

		final SortedSet<CableChannelingItem> cableChannelingItems = parentSchemeCableLink.getPathMembers0();
		if (this.sequentialNumber > thatSequentialNumber) {
			final SortedSet<CableChannelingItem> toShiftRight = cableChannelingItems.subSet(that, this);
			toShiftRight.remove(that);
			for (final CableChannelingItem cableChannelingItem : toShiftRight) {
				cableChannelingItem.shiftRight();
			}
			this.sequentialNumber = thatSequentialNumber + 1;
		} else {
			final SortedSet<CableChannelingItem> toShiftLeft = cableChannelingItems.subSet(this, that);
			toShiftLeft.remove(this);
			for (final CableChannelingItem cableChannelingItem : toShiftLeft) {
				cableChannelingItem.shiftLeft();
			}
			that.shiftLeft();
			this.sequentialNumber = thatSequentialNumber;
		}
		super.markAsChanged();
	}

	/*-********************************************************************
	 * pipeBlock                                                          *
	 **********************************************************************/

	private Identifier pipeBlockId;

	private transient PipeBlock pipeBlock;

	Identifier getPipeBlockId() {
		assert this.pipeBlockId != null : OBJECT_NOT_INITIALIZED;
		assert this.pipeBlockId.isVoid() || this.pipeBlockId.getMajor() == PIPEBLOCK_CODE;
		return this.pipeBlockId;
	}

	/**
	 * A wrapper around {@link #getPipeBlockId()}.
	 *
	 * @throws ApplicationException
	 */
	public PipeBlock getPipeBlock() throws ApplicationException {
		final Identifier thisPipeBlockId = this.getPipeBlockId();
		return thisPipeBlockId.equals(Identifier.possiblyVoid(this.pipeBlock))
				? this.pipeBlock
				: (this.pipeBlock = StorableObjectPool.getStorableObject(thisPipeBlockId, true));
	}

	/**
	 * A wrapper around {@link #setPipeBlockId0(Identifier)}.
	 *
	 * @param pipeBlockId
	 * @throws ApplicationException
	 */
	void setPipeBlockId(final Identifier pipeBlockId)
	throws ApplicationException {
		this.setPipeBlockId0(pipeBlockId);
		this.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setPipeBlock0(PipeBlock)}.
	 *
	 * @param pipeBlockId
	 * @throws ApplicationException
	 */
	private void setPipeBlockId0(final Identifier pipeBlockId)
	throws ApplicationException {
		assert pipeBlockId != null : NON_NULL_EXPECTED;
		assert pipeBlockId.isVoid() || pipeBlockId.getMajor() == PIPEBLOCK_CODE;

		if (this.pipeBlockId.equals(pipeBlockId)) {
			return;
		}

		if (buildCacheOnModification()) {
			this.setPipeBlock0(StorableObjectPool.<PipeBlock>getStorableObject(pipeBlockId, true));
		} else {
			this.pipeBlockId = pipeBlockId;
			this.pipeBlock = null;
		}
	}

	/**
	 * A wrapper around {@link #setPipeBlock0(PipeBlock)}.
	 *
	 * @param pipeBlock
	 */
	public void setPipeBlock(final PipeBlock pipeBlock) {
		this.setPipeBlock0(pipeBlock);
		this.markAsChanged();
	}

	private void setPipeBlock0(final PipeBlock pipeBlock) {
		final Identifier newPipeBlockId = Identifier.possiblyVoid(pipeBlock);

		if (this.pipeBlockId.equals(newPipeBlockId)) {
			return;
		}

		this.pipeBlockId = newPipeBlockId;
		this.pipeBlock = pipeBlock;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CableChannelingItemWrapper getWrapper() {
		return CableChannelingItemWrapper.getInstance();
	}
}
