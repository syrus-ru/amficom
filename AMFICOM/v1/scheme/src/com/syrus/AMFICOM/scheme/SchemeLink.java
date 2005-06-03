/*-
 * $Id: SchemeLink.java,v 1.32 2005/06/03 20:39:06 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.SchemeLink_Transferable;
import com.syrus.util.Log;

/**
 * #10 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.32 $, $Date: 2005/06/03 20:39:06 $
 * @module scheme_v1
 */
public final class SchemeLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3834587703751947064L;

	private Identifier siteNodeId;

	private Identifier parentSchemeElementId;

	private Identifier parentSchemeProtoElementId;

	private SchemeLinkDatabase schemeLinkDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemeLinkDatabase = (SchemeLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_LINK_ENTITY_CODE);
		try {
			this.schemeLinkDatabase.retrieve(this);
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
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentScheme
	 * @param parentSchemeElement
	 * @param parentSchemeProtoElement
	 */
	SchemeLink(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength, final LinkType linkType,
			final Link link, final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final Scheme parentScheme,
			final SchemeElement parentSchemeElement,
			final SchemeProtoElement parentSchemeProtoElement) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, physicalLength,
				opticalLength, linkType, link,
				sourceSchemePort, targetSchemePort,
				parentScheme);
		this.siteNodeId = Identifier.possiblyVoid(siteNode);

		assert (parentScheme == null ? 0 : 1)
				+ (parentSchemeElement == null ? 0 : 1)
				+ (parentSchemeProtoElement == null ? 0 : 1) <= 1: ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);

		this.schemeLinkDatabase = (SchemeLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_LINK_ENTITY_CODE);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeLink(final SchemeLink_Transferable transferable) throws CreateObjectException {
		this.schemeLinkDatabase = (SchemeLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_LINK_ENTITY_CODE);
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort)}.
	 *
	 * @param creatorId
	 * @param name
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name) throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null, parentScheme);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null, parentSchemeElement);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort, SchemeProtoElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null, parentSchemeProtoElement);
	}

        /**
         * This method breaks some assertions, so clients should consider using
         * other ones to create a new instance.
         *
         * @param creatorId
         * @param name
         * @param description
         * @param physicalLength
         * @param opticalLength
         * @param linkType
         * @param link
         * @param siteNode
         * @param sourceSchemePort
         * @param targetSchemePort
         * @throws CreateObjectException
         */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength, final LinkType linkType,
			final Link link, final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, physicalLength,
					opticalLength, linkType, link,
					siteNode, sourceSchemePort,
					targetSchemePort, null, null, null);
			schemeLink.markAsChanged();
			if (link != null || linkType != null)
				schemeLink.abstractLinkTypeSet = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength, final LinkType linkType,
			final Link link, final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final Scheme parentScheme)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentScheme != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, physicalLength,
					opticalLength, linkType, link,
					siteNode, sourceSchemePort,
					targetSchemePort, parentScheme, null,
					null);
			schemeLink.markAsChanged();
			if (link != null || linkType != null)
				schemeLink.abstractLinkTypeSet = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength, final LinkType linkType,
			final Link link, final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeElement != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, physicalLength,
					opticalLength, linkType, link,
					siteNode, sourceSchemePort,
					targetSchemePort, null, parentSchemeElement,
					null);
			schemeLink.markAsChanged();
			if (link != null || linkType != null)
				schemeLink.abstractLinkTypeSet = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength, final LinkType linkType,
			final Link link, final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, physicalLength,
					opticalLength, linkType, link,
					siteNode, sourceSchemePort,
					targetSchemePort, null, null,
					parentSchemeProtoElement);
			schemeLink.markAsChanged();
			if (link != null || linkType != null)
				schemeLink.abstractLinkTypeSet = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	public Object clone() {
		final SchemeLink schemeLink = (SchemeLink) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeLink;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.siteNodeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.siteNodeId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see AbstractSchemeLink#getLink()
	 */
	public Link getLink() {
		final Link link = super.getLink();
		assert link == null || link.getSort().value() == LinkSort._LINKSORT_LINK: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return link;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	public AbstractLinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof LinkType;
		return abstractLinkType;
	}

	public LinkType getLinkType() {
		return (LinkType) this.getAbstractLinkType();
	}

	/**
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	public Scheme getParentScheme() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (super.parentSchemeId.isVoid()) {
			assert !this.parentSchemeElementId.isVoid() || !this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			Log.debugMessage("SchemeLink.getParentScheme() | Parent Scheme was requested, while parent is either a SchemeElement or a SchemeProtoElement; returning null",
					Log.FINE);
			return null;
		}

		assert this.parentSchemeElementId.isVoid() && this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		return super.getParentScheme();
	}

	public SchemeElement getParentSchemeElement() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeElementId.isVoid()) {
			assert !super.parentSchemeId.isVoid() || !this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			Log.debugMessage("SchemeLink.getParentSchemeElement() | Parent SchemeElement was requested, while parent is either a Scheme or a SchemeProtoElement; returning null",
					Log.FINE);
			return null;
		}

		try {
			assert super.parentSchemeId.isVoid() && this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			return (SchemeElement) StorableObjectPool.getStorableObject(this.parentSchemeElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeProtoElementId.isVoid()) {
			assert !super.parentSchemeId.isVoid() || !this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			Log.debugMessage("SchemeLink.getParentSchemeProtoElement() | Parent SchemeProtoElement was requested, while parent is either a Scheme or a SchemeElement; returning null",
					Log.FINE);
			return null;
		}

		try {
			assert super.parentSchemeId.isVoid() && this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			return (SchemeProtoElement) StorableObjectPool.getStorableObject(this.parentSchemeProtoElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SiteNode getSiteNode() {
		assert this.siteNodeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (SiteNode) StorableObjectPool.getStorableObject(this.siteNodeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	public AbstractSchemePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return sourceAbstractSchemePort;
	}

	public SchemePort getSourceSchemePort() {
		return (SchemePort) this.getSourceAbstractSchemePort();
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	public AbstractSchemePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return targetAbstractSchemePort;
	}

	public SchemePort getTargetSchemePort() {
		return (SchemePort) this.getTargetAbstractSchemePort();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeLink_Transferable(
				super.getHeaderTransferable(), super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				(Identifier_Transferable) super.abstractLinkTypeId.getTransferable(),
				(Identifier_Transferable) super.linkId.getTransferable(),
				(Identifier_Transferable) this.siteNodeId.getTransferable(),
				(Identifier_Transferable) super.sourceAbstractSchemePortId.getTransferable(),
				(Identifier_Transferable) super.targetAbstractSchemePortId.getTransferable(),
				(Identifier_Transferable) super.parentSchemeId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeElementId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeProtoElementId.getTransferable(),
				Identifier.createTransferables(super.getCharacteristics()));
	}

	synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier linkTypeId,
			final Identifier linkId, final Identifier siteNodeId,
			final Identifier sourceSchemePortId,
			final Identifier targetSchemePortId,
			final Identifier parentSchemeId,
			final Identifier parentSchemeElementId,
			final Identifier parentSchemeProtoElementId) {
		super.setAttributes(created, modified, creatorId, modifierId,
				version, name, description, physicalLength,
				opticalLength, linkTypeId, linkId,
				sourceSchemePortId,
				targetSchemePortId, parentSchemeId);

		assert siteNodeId != null: ErrorMessages.NON_NULL_EXPECTED;

		assert parentSchemeElementId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert (parentSchemeId.isVoid() ? 0 : 1)
				+ (parentSchemeElementId.isVoid() ? 0 : 1)
				+ (parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1;

		this.siteNodeId = siteNodeId;
		this.parentSchemeElementId = parentSchemeElementId;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
	}

	/**
	 * @param link
	 * @see AbstractSchemeLink#setLink(Link)
	 */
	public void setLink(final Link link) {
		assert link == null || link.getSort().value() == LinkSort._LINKSORT_LINK: ErrorMessages.NATURE_INVALID;
		super.setLink(link);
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert abstractLinkType instanceof LinkType;
		super.setAbstractLinkType(abstractLinkType);
	}

	/**
	 * @param linkType
	 */
	public void setLinkType(final LinkType linkType) {
		this.setAbstractLinkType(linkType);
	}

	/**
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	public void setParentScheme(final Scheme parentScheme) {
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to another scheme.
			 */
			assert this.parentSchemeElementId.isVoid()
					&& this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			super.setParentScheme(parentScheme);
		} else {
			if (!this.parentSchemeElementId.isVoid()) {
				/*
				 * Moving from a scheme element to a scheme.
				 */
				assert this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
				if (parentScheme == null) {
					Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
					return;
				}
				this.parentSchemeElementId = Identifier.VOID_IDENTIFIER;
			} else {
				/*
				 * Moving from a scheme protoelement to a scheme.
				 */
				assert !this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
				if (parentScheme == null) {
					Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
					return;
				}
				this.parentSchemeProtoElementId = Identifier.VOID_IDENTIFIER;
			}
			super.parentSchemeId = parentScheme.getId();
			super.markAsChanged();
		}
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		Identifier newParentSchemeElementId;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to a scheme element.
			 */
			assert this.parentSchemeElementId.isVoid()
					&& this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			super.parentSchemeId = Identifier.VOID_IDENTIFIER;
		} else if (!this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a scheme element to another scheme element.
			 */
			assert this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeElement == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				StorableObjectPool.delete(this.id);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			if (this.parentSchemeElementId.equals(newParentSchemeElementId))
				return;
		} else {
			/*
			 * Moving from a scheme protoelement to a scheme element.
			 */
			assert !this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			this.parentSchemeProtoElementId = Identifier.VOID_IDENTIFIER;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		Identifier newParentSchemeProtoElementId;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to a scheme protoelement.
			 */
			assert this.parentSchemeElementId.isVoid()
					&& this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			super.parentSchemeId = Identifier.VOID_IDENTIFIER;
		} else if (!this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a scheme element to a scheme protoelement.
			 */
			assert this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			this.parentSchemeElementId = Identifier.VOID_IDENTIFIER;
		} else {
			/*
			 * Moving from a scheme protoelement to another scheme protoelement.
			 */
			assert !this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				StorableObjectPool.delete(this.id);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId))
				return;
		}
		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		super.markAsChanged();
	}

	public void setSiteNode(final SiteNode siteNode) {
		final Identifier newSiteNodeId = Identifier.possiblyVoid(siteNode);
		if (this.siteNodeId.equals(newSiteNodeId))
			return;
		this.siteNodeId = newSiteNodeId;
		super.markAsChanged();
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: ErrorMessages.NATURE_INVALID;
		super.setSourceAbstractSchemePort(sourceAbstractSchemePort);
	}

	public void setSourceSchemePort(final SchemePort sourceSchemePort) {
		this.setSourceAbstractSchemePort(sourceSchemePort);
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: ErrorMessages.NATURE_INVALID;
		super.setTargetAbstractSchemePort(targetAbstractSchemePort);
	}

	public void setTargetSchemePort(final SchemePort targetSchemePort) {
		this.setTargetAbstractSchemePort(targetSchemePort);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemeLink_Transferable schemeLink = (SchemeLink_Transferable) transferable;
		super.fromTransferable(schemeLink.header, schemeLink.name,
				schemeLink.description,
				schemeLink.physicalLength,
				schemeLink.opticalLength, schemeLink.linkTypeId,
				schemeLink.linkId,
				schemeLink.sourceSchemePortId,
				schemeLink.targetSchemePortId,
				schemeLink.parentSchemeId,
				schemeLink.characteristicIds);
		this.siteNodeId = new Identifier(schemeLink.siteNodeId);
		this.parentSchemeElementId = new Identifier(schemeLink.parentSchemeElementId);
		this.parentSchemeProtoElementId = new Identifier(schemeLink.parentSchemeProtoElementId);
	}
}
