/*-
 * $Id: SchemeCableLink.java,v 1.36 2005/06/17 13:06:54 bass Exp $
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

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink_Transferable;
import com.syrus.util.Log;

/**
 * #11 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.36 $, $Date: 2005/06/17 13:06:54 $
 * @module scheme_v1
 */
public final class SchemeCableLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3760847878314274867L;

	private SchemeCableLinkDatabase schemeCableLinkDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCableLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		this.schemeCableLinkDatabase = (SchemeCableLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMECABLELINK_CODE);
		try {
			this.schemeCableLinkDatabase.retrieve(this);
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

		this.schemeCableLinkDatabase = (SchemeCableLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMECABLELINK_CODE);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeCableLink(final SchemeCableLink_Transferable transferable) throws CreateObjectException {
		this.schemeCableLinkDatabase = (SchemeCableLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMECABLELINK_CODE);
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

	public Object clone() {
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
	public Set getCableChannelingItems() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.CABLECHANNELINGITEM_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see AbstractSchemeLink#getLink()
	 */
	public Link getLink() {
		final Link link = super.getLink();
		assert link == null || link.getSort().value() == LinkSort._LINKSORT_CABLELINK: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return link;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	public AbstractLinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof CableLinkType;
		return abstractLinkType;
	}

	public CableLinkType getCableLinkType() {
		return (CableLinkType) this.getAbstractLinkType();
	}

	/**
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	public Scheme getParentScheme() {
		assert super.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		return super.getParentScheme();
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set getSchemeCableThreads() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, ObjectEntities.SCHEMECABLETHREAD_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	public AbstractSchemePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return sourceAbstractSchemePort;
	}

	public SchemeCablePort getSourceSchemeCablePort() {
		return (SchemeCablePort) getSourceAbstractSchemePort();
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	public AbstractSchemePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return targetAbstractSchemePort;
	}

	public SchemeCablePort getTargetSchemeCablePort() {
		return (SchemeCablePort) getTargetAbstractSchemePort();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeCableLink_Transferable(
				super.getHeaderTransferable(), super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				(IdlIdentifier) super.abstractLinkTypeId.getTransferable(),
				(IdlIdentifier) super.linkId.getTransferable(),
				(IdlIdentifier) super.sourceAbstractSchemePortId.getTransferable(),
				(IdlIdentifier) super.targetAbstractSchemePortId.getTransferable(),
				(IdlIdentifier) super.parentSchemeId.getTransferable(),
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

	public void setCableChannelingItems(final Set cableChannelingItems) {
		assert cableChannelingItems != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldCableChannelingItemIterator = getCableChannelingItems().iterator(); oldCableChannelingItemIterator.hasNext();) {
			final CableChannelingItem oldCableChannelingItem = (CableChannelingItem) oldCableChannelingItemIterator.next();
			/*
			 * Check is made to prevent CableChannelingItems from
			 * permanently losing their parents.
			 */
			assert !cableChannelingItems.contains(oldCableChannelingItem);
			removeCableChannelingItem(oldCableChannelingItem);
		}
		for (final Iterator cableChannelingItemIterator = cableChannelingItems.iterator(); cableChannelingItemIterator.hasNext();)
			addCableChannelingItem((CableChannelingItem) cableChannelingItemIterator.next());
	}

	/**
	 * @param link
	 * @see AbstractSchemeLink#setLink(Link)
	 */
	public void setLink(final Link link) {
		assert link == null || link.getSort().value() == LinkSort._LINKSORT_CABLELINK: ErrorMessages.NATURE_INVALID;
		super.setLink(link);
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert abstractLinkType instanceof CableLinkType;
		super.setAbstractLinkType(abstractLinkType);
	}

	/**
	 * @param cableLinkType
	 */
	public void setCableLinkType(final CableLinkType cableLinkType) {
		this.setAbstractLinkType(cableLinkType);
	}

	/**
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	public void setParentScheme(final Scheme parentScheme) {
		assert super.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		super.setParentScheme(parentScheme);
	}

	public void setSchemeCableThreads(final Set schemeCableThreads) {
		assert schemeCableThreads != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeCableThreadIterator = getSchemeCableThreads().iterator(); oldSchemeCableThreadIterator.hasNext();) {
			final SchemeCableThread oldSchemeCableThread = (SchemeCableThread) oldSchemeCableThreadIterator.next();
			/*
			 * Check is made to prevent SchemeCableThreads from
			 * permanently losing their parents.
			 */
			assert !schemeCableThreads.contains(oldSchemeCableThread);
			removeSchemeCableThread(oldSchemeCableThread);
		}
		for (final Iterator schemeCableThreadIterator = schemeCableThreads.iterator(); schemeCableThreadIterator.hasNext();)
			addSchemeCableThread((SchemeCableThread) schemeCableThreadIterator.next());
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.NATURE_INVALID;
		super.setSourceAbstractSchemePort(sourceAbstractSchemePort);
	}

	public void setSourceSchemeCablePort(final SchemeCablePort sourceSchemeCablePort) {
		this.setSourceAbstractSchemePort(sourceSchemeCablePort);
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: ErrorMessages.NATURE_INVALID;
		super.setTargetAbstractSchemePort(targetAbstractSchemePort);
	}

	public void setTargetSchemeCablePort(final SchemeCablePort targetSchemeCablePort) {
		this.setTargetAbstractSchemePort(targetSchemeCablePort);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemeCableLink_Transferable schemeCableLink = (SchemeCableLink_Transferable) transferable;
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
