/*-
 * $Id: SchemeCableLink.java,v 1.57 2005/07/26 12:52:23 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLinkHelper;
import com.syrus.util.Log;

/**
 * #13 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.57 $, $Date: 2005/07/26 12:52:23 $
 * @module scheme
 */
public final class SchemeCableLink extends AbstractSchemeLink implements PathOwner<CableChannelingItem> {
	private static final long serialVersionUID = 3760847878314274867L;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCableLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		try {
			DatabaseContext.getDatabase(SCHEMECABLELINK_CODE).retrieve(this);
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
	 */
	SchemeCableLink(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final CableLinkType cableLinkType,
			final Link link,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme) {
		super(id,
				created,
				modified,
				creatorId,
				modifierId,
				version,
				name,
				description,
				physicalLength,
				opticalLength,
				cableLinkType,
				link,
				sourceSchemeCablePort,
				targetSchemeCablePort,
				parentScheme);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeCableLink(final IdlSchemeCableLink transferable) throws CreateObjectException {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, CableLinkType, Link, SchemeCablePort, SchemeCablePort, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeCableLink createInstance(final Identifier creatorId, final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null, null, null, parentScheme);
	}

	
	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param cableLinkType
	 * @param link
	 * @param sourceSchemeCablePort
	 * @param targetSchemeCablePort
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeCableLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final CableLinkType cableLinkType,
			final Link link,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentScheme != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCableLink schemeCableLink = new SchemeCableLink(IdentifierPool.getGeneratedIdentifier(SCHEMECABLELINK_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLength,
					opticalLength,
					cableLinkType,
					link,
					sourceSchemeCablePort,
					targetSchemeCablePort,
					parentScheme);
			schemeCableLink.markAsChanged();
			if (link != null || cableLinkType != null)
				schemeCableLink.abstractLinkTypeSet = true;
			return schemeCableLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeCableLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * Adds <code>CableChannelingItem</code> to the end of this
	 * <code>SchemeCableLink</code>, adjusting its
	 * <code>sequentialNumber</code> accordingly.
	 *
	 * @param cableChannelingItem
	 * @param processSubsequentSiblings
	 */
	public void addPathMember(final CableChannelingItem cableChannelingItem, final boolean processSubsequentSiblings) {
		assert cableChannelingItem != null: NON_NULL_EXPECTED;
		cableChannelingItem.setParentPathOwner(this, processSubsequentSiblings);
	}

	public void addSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: NON_NULL_EXPECTED;
		schemeCableThread.setParentSchemeCableLink(this);
	}

	@Override
	public SchemeCableLink clone() throws CloneNotSupportedException {
		final SchemeCableLink schemeCableLink = (SchemeCableLink) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableLink;
	}

	public SortedSet<CableChannelingItem> getPathMembers() {
		return Collections.unmodifiableSortedSet(new TreeSet<CableChannelingItem>(this.getPathMembers0()));
	}

	/**
	 * @return child <code>CableChannelingItem</code>s in an unsorted manner.
	 */
	private Set<CableChannelingItem> getPathMembers0() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, CABLECHANNELINGITEM_CODE), true, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkId()
	 */
	@Override
	Identifier getAbstractLinkId() {
		final Identifier cableLinkId = super.getAbstractLinkId();
		assert cableLinkId.isVoid() || cableLinkId.getMajor() == CABLELINK_CODE;
		return cableLinkId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLink()
	 */
	@Override
	public CableLink getAbstractLink() {
		final AbstractLink abstractLink = super.getAbstractLink();
		assert abstractLink == null || abstractLink instanceof CableLink : OBJECT_BADLY_INITIALIZED;
		return (CableLink) abstractLink;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkTypeId()
	 */
	@Override
	Identifier getAbstractLinkTypeId() {
		final Identifier cableLinkTypeId = super.getAbstractLinkTypeId();
		assert cableLinkTypeId.isVoid() || cableLinkTypeId.getMajor() == CABLELINK_TYPE_CODE;
		return cableLinkTypeId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	@Override
	public CableLinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof CableLinkType: OBJECT_BADLY_INITIALIZED;
		return (CableLinkType) abstractLinkType;
	}

	/**
	 * @see AbstractSchemeElement#getParentSchemeId()
	 */
	@Override
	Identifier getParentSchemeId() {
		final Identifier parentSchemeId1 = super.getParentSchemeId();
		assert !parentSchemeId1.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		return parentSchemeId1;
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeCableThread> getSchemeCableThreads() {
		return Collections.unmodifiableSet(this.getSchemeCableThreads0());
	}

	private Set<SchemeCableThread> getSchemeCableThreads0() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMECABLETHREAD_CODE), true, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePortId()
	 */
	@Override
	Identifier getSourceAbstractSchemePortId() {
		final Identifier sourceSchemeCablePortId = super.getSourceAbstractSchemePortId();
		assert sourceSchemeCablePortId.isVoid() || sourceSchemeCablePortId.getMajor() == SCHEMECABLEPORT_CODE;
		return sourceSchemeCablePortId;
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	@Override
	public SchemeCablePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: OBJECT_BADLY_INITIALIZED;
		return (SchemeCablePort) sourceAbstractSchemePort;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePortId()
	 */
	@Override
	Identifier getTargetAbstractSchemePortId() {
		final Identifier targetSchemeCablePortId = super.getTargetAbstractSchemePortId();
		assert targetSchemeCablePortId.isVoid() || targetSchemeCablePortId.getMajor() == SCHEMECABLEPORT_CODE;
		return targetSchemeCablePortId;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	@Override
	public SchemeCablePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: OBJECT_BADLY_INITIALIZED;
		return (SchemeCablePort) targetAbstractSchemePort;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeCableLink getTransferable(final ORB orb) {
		return IdlSchemeCableLinkHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				this.getAbstractLinkTypeId().getTransferable(),
				this.getAbstractLinkId().getTransferable(),
				this.getSourceAbstractSchemePortId().getTransferable(),
				this.getTargetAbstractSchemePortId().getTransferable(),
				this.getParentSchemeId().getTransferable());
	}

	/**
	 * Removes the <code>CableChannelingItem</code> from this
	 * <code>SchemeCableLink</code>, changing its
	 * <code>sequentialNumber</code> to <code>-1</code> and removing all
	 * its subsequent <code>CableChannelingItem</code>s.
	 *
	 * @param cableChannelingItem
	 * @param processSubsequentSiblings
	 * @see SchemePath#removePathMember(PathElement, boolean)
	 */
	public void removePathMember(final CableChannelingItem cableChannelingItem, final boolean processSubsequentSiblings) {
		assert cableChannelingItem != null: NON_NULL_EXPECTED;
		assert cableChannelingItem.getParentSchemeCableLinkId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		cableChannelingItem.setParentPathOwner(null, processSubsequentSiblings);
	}

	public void removeSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: NON_NULL_EXPECTED;
		assert schemeCableThread.getParentSchemeCableLinkId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableThread.setParentSchemeCableLink(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param cableLinkTypeId
	 * @param linkId
	 * @param sourceSchemeCablePortId
	 * @param targetSchemeCablePortId
	 * @param parentSchemeId
	 * @see AbstractSchemeLink#setAttributes(Date, Date, Identifier, Identifier, long, String, String, double, double, Identifier, Identifier, Identifier, Identifier, Identifier)
	 */
	@Override
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier cableLinkTypeId,
			final Identifier linkId,
			final Identifier sourceSchemeCablePortId,
			final Identifier targetSchemeCablePortId,
			final Identifier parentSchemeId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				name,
				description,
				physicalLength,
				opticalLength,
				cableLinkTypeId,
				linkId,
				sourceSchemeCablePortId,
				targetSchemeCablePortId,
				parentSchemeId);

		assert !parentSchemeId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
	}

	/**
	 * @param abstractLink
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	@Override
	public void setAbstractLink(final AbstractLink abstractLink) {
		assert abstractLink == null || abstractLink instanceof CableLink : NATURE_INVALID;
		this.setAbstractLink((CableLink) abstractLink);
	}

	/**
	 * @param cableLink
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	public void setAbstractLink(final CableLink cableLink) {
		super.setAbstractLink(cableLink);
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	@Override
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert abstractLinkType instanceof CableLinkType : NATURE_INVALID;
		this.setAbstractLinkType((CableLinkType) abstractLinkType);
	}

	/**
	 * @param cableLinkType
	 */
	public void setAbstractLinkType(final CableLinkType cableLinkType) {
		super.setAbstractLinkType(cableLinkType);
	}

	/**
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	@Override
	public void setParentScheme(final Scheme parentScheme) {
		assert super.parentSchemeId != null: OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		super.setParentScheme(parentScheme);
	}

	public void setSchemeCableThreads(final Set<SchemeCableThread> schemeCableThreads) {
		assert schemeCableThreads != null: NON_NULL_EXPECTED;
		final Set<SchemeCableThread> oldSchemeCableThreads = this.getSchemeCableThreads0();
		/*
		 * Check is made to prevent SchemeCableThreads from
		 * permanently losing their parents.
		 */
		oldSchemeCableThreads.removeAll(schemeCableThreads);
		for (final SchemeCableThread oldSchemeCableThread : oldSchemeCableThreads) {
			this.removeSchemeCableThread(oldSchemeCableThread);
		}
		for (final SchemeCableThread schemeCableThread : schemeCableThreads) {
			this.addSchemeCableThread(schemeCableThread);
		}
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: NATURE_INVALID;
		this.setSourceAbstractSchemePort((SchemeCablePort) sourceAbstractSchemePort);
	}

	public void setSourceAbstractSchemePort(final SchemeCablePort sourceSchemeCablePort) {
		super.setSourceAbstractSchemePort(sourceSchemeCablePort);
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: NATURE_INVALID;
		this.setTargetAbstractSchemePort((SchemeCablePort) targetAbstractSchemePort);
	}

	public void setTargetAbstractSchemePort(final SchemeCablePort targetSchemeCablePort) {
		super.setTargetAbstractSchemePort(targetSchemeCablePort);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlSchemeCableLink schemeCableLink = (IdlSchemeCableLink) transferable;
		super.fromTransferable(schemeCableLink, schemeCableLink.name,
				schemeCableLink.description,
				schemeCableLink.physicalLength,
				schemeCableLink.opticalLength, schemeCableLink.cableLinkTypeId,
				schemeCableLink.cableLinkId,
				schemeCableLink.sourceSchemeCablePortId,
				schemeCableLink.targetSchemeCablePortId,
				schemeCableLink.parentSchemeId);
	}

	/**
	 * @param cableChannelingItem
	 * @see SchemePath#assertContains(PathElement)
	 */
	boolean assertContains(final CableChannelingItem cableChannelingItem) {
		final SortedSet<CableChannelingItem> cableChanelingItems = this.getPathMembers();
		return cableChannelingItem.getParentSchemeCableLinkId().equals(super.id)
				&& cableChanelingItems.headSet(cableChannelingItem).size() == cableChannelingItem.sequentialNumber;
	}
}
