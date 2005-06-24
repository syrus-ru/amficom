/*-
 * $Id: SchemeCableLink.java,v 1.40 2005/06/24 14:13:38 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLink;
import com.syrus.util.Log;

/**
 * #11 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.40 $, $Date: 2005/06/24 14:13:38 $
 * @module scheme_v1
 */
public final class SchemeCableLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3760847878314274867L;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCableLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		try {
			DatabaseContext.getDatabase(ObjectEntities.SCHEMECABLELINK_CODE).retrieve(this);
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
	SchemeCableLink(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength,
			final CableLinkType cableLinkType,
			final Link link,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, physicalLength,
				opticalLength, cableLinkType, link,
				sourceSchemeCablePort, targetSchemeCablePort,
				parentScheme);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeCableLink(final IdlSchemeCableLink transferable) throws CreateObjectException {
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
	public static SchemeCableLink createInstance(final Identifier creatorId,
			final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, parentScheme);
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
			final String name, final String description,
			final double physicalLength, final double opticalLength,
			final CableLinkType cableLinkType, final Link link,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentScheme != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCableLink schemeCableLink = new SchemeCableLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEMECABLELINK_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, physicalLength,
					opticalLength, cableLinkType, link,
					sourceSchemeCablePort,
					targetSchemeCablePort, parentScheme);
			schemeCableLink.markAsChanged();
			if (link != null || cableLinkType != null)
				schemeCableLink.abstractLinkTypeSet = true;
			return schemeCableLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeCableLink.createInstance | cannot generate identifier ", ige);
		}
	}

	public void addCableChannelingItem(final CableChannelingItem cableChannelingItem) {
		assert cableChannelingItem != null: ErrorMessages.NON_NULL_EXPECTED;
		cableChannelingItem.setParentSchemeCableLink(this);
	}

	public void addSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeCableThread.setParentSchemeCableLink(this);
	}

	@Override
	public SchemeCableLink clone() {
		final SchemeCableLink schemeCableLink = (SchemeCableLink) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableLink;
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set<CableChannelingItem> getCableChannelingItems() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.CABLECHANNELINGITEM_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLink()
	 */
	@Override
	public CableLink getAbstractLink() {
		final AbstractLink abstractLink = super.getAbstractLink();
		assert abstractLink == null || abstractLink instanceof CableLink : ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return (CableLink) abstractLink;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	@Override
	public CableLinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof CableLinkType;
		return (CableLinkType) abstractLinkType;
	}

	/**
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	@Override
	public Scheme getParentScheme() {
		assert super.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		return super.getParentScheme();
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set<SchemeCableThread> getSchemeCableThreads() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, ObjectEntities.SCHEMECABLETHREAD_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	@Override
	public SchemeCablePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return (SchemeCablePort) sourceAbstractSchemePort;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	@Override
	public SchemeCablePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return (SchemeCablePort) targetAbstractSchemePort;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IdlSchemeCableLink getTransferable() {
		return new IdlSchemeCableLink(
				super.getHeaderTransferable(), super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				super.abstractLinkTypeId.getTransferable(),
				super.linkId.getTransferable(),
				super.sourceAbstractSchemePortId.getTransferable(),
				super.targetAbstractSchemePortId.getTransferable(),
				super.parentSchemeId.getTransferable(),
				Identifier.createTransferables(super.getCharacteristics()));
	}

	public void removeCableChannelingItem(final CableChannelingItem cableChannelingItem) {
		assert cableChannelingItem != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCableChannelingItems().contains(cableChannelingItem): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		cableChannelingItem.setParentSchemeCableLink(null);
	}

	public void removeSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeCableThreads().contains(schemeCableThread): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier cableLinkTypeId,
			final Identifier linkId,
			final Identifier sourceSchemeCablePortId,
			final Identifier targetSchemeCablePortId,			
			final Identifier parentSchemeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, name, description, physicalLength, opticalLength, cableLinkTypeId, linkId, sourceSchemeCablePortId, targetSchemeCablePortId, parentSchemeId);

		assert !parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
	}

	public void setCableChannelingItems(final Set<CableChannelingItem> cableChannelingItems) {
		assert cableChannelingItems != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator<CableChannelingItem> oldCableChannelingItemIterator = getCableChannelingItems().iterator(); oldCableChannelingItemIterator.hasNext();) {
			final CableChannelingItem oldCableChannelingItem = oldCableChannelingItemIterator.next();
			/*
			 * Check is made to prevent CableChannelingItems from
			 * permanently losing their parents.
			 */
			assert !cableChannelingItems.contains(oldCableChannelingItem);
			removeCableChannelingItem(oldCableChannelingItem);
		}
		for (final Iterator<CableChannelingItem> cableChannelingItemIterator = cableChannelingItems.iterator(); cableChannelingItemIterator.hasNext();)
			addCableChannelingItem(cableChannelingItemIterator.next());
	}

	/**
	 * @param abstractLink
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	@Override
	public void setAbstractLink(final AbstractLink abstractLink) {
		assert abstractLink == null || abstractLink instanceof CableLink : ErrorMessages.NATURE_INVALID;
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
		assert abstractLinkType instanceof CableLinkType : ErrorMessages.NATURE_INVALID;
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
		assert super.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		super.setParentScheme(parentScheme);
	}

	public void setSchemeCableThreads(final Set<SchemeCableThread> schemeCableThreads) {
		assert schemeCableThreads != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator<SchemeCableThread> oldSchemeCableThreadIterator = getSchemeCableThreads().iterator(); oldSchemeCableThreadIterator.hasNext();) {
			final SchemeCableThread oldSchemeCableThread = oldSchemeCableThreadIterator.next();
			/*
			 * Check is made to prevent SchemeCableThreads from
			 * permanently losing their parents.
			 */
			assert !schemeCableThreads.contains(oldSchemeCableThread);
			removeSchemeCableThread(oldSchemeCableThread);
		}
		for (final Iterator<SchemeCableThread> schemeCableThreadIterator = schemeCableThreads.iterator(); schemeCableThreadIterator.hasNext();)
			addSchemeCableThread(schemeCableThreadIterator.next());
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.NATURE_INVALID;
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
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.NATURE_INVALID;
		this.setTargetAbstractSchemePort((SchemeCablePort) targetAbstractSchemePort);
	}

	public void setTargetAbstractSchemePort(final SchemeCablePort targetSchemeCablePort) {
		super.setTargetAbstractSchemePort(targetSchemeCablePort);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final IdlSchemeCableLink schemeCableLink = (IdlSchemeCableLink) transferable;
		super.fromTransferable(schemeCableLink.header, schemeCableLink.name,
				schemeCableLink.description,
				schemeCableLink.physicalLength,
				schemeCableLink.opticalLength, schemeCableLink.cableLinkTypeId,
				schemeCableLink.cableLinkId,
				schemeCableLink.sourceSchemeCablePortId,
				schemeCableLink.targetSchemeCablePortId,
				schemeCableLink.parentSchemeId,
				schemeCableLink.characteristicIds);
	}
}
