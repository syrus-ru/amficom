/*-
 * $Id: SchemeLink.java,v 1.39 2005/06/25 17:07:43 bass Exp $
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

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLink;
import com.syrus.util.Log;

/**
 * #10 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.39 $, $Date: 2005/06/25 17:07:43 $
 * @module scheme_v1
 */
public final class SchemeLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3834587703751947064L;

	private Identifier siteNodeId;

	private Identifier parentSchemeElementId;

	private Identifier parentSchemeProtoElementId;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.SCHEMELINK_CODE).retrieve(this);
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
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeLink(final IdlSchemeLink transferable) throws CreateObjectException {
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
							.getGeneratedIdentifier(ObjectEntities.SCHEMELINK_CODE),
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
							.getGeneratedIdentifier(ObjectEntities.SCHEMELINK_CODE),
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
							.getGeneratedIdentifier(ObjectEntities.SCHEMELINK_CODE),
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
							.getGeneratedIdentifier(ObjectEntities.SCHEMELINK_CODE),
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

	@Override
	public SchemeLink clone() {
		final SchemeLink schemeLink = (SchemeLink) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeLink;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.siteNodeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.siteNodeId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLink()
	 */
	@Override
	public Link getAbstractLink() {
		final AbstractLink abstractLink = super.getAbstractLink();
		assert abstractLink == null || abstractLink instanceof Link : ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return (Link) abstractLink;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	@Override
	public LinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof LinkType;
		return (LinkType) abstractLinkType;
	}

	/**
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	@Override
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
	@Override
	public SchemePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return (SchemePort) sourceAbstractSchemePort;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	@Override
	public SchemePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return (SchemePort) targetAbstractSchemePort;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeLink getTransferable(final ORB orb) {
		return new IdlSchemeLink(
				super.getHeaderTransferable(orb), super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				super.abstractLinkTypeId.getTransferable(),
				super.linkId.getTransferable(),
				this.siteNodeId.getTransferable(),
				super.sourceAbstractSchemePortId.getTransferable(),
				super.targetAbstractSchemePortId.getTransferable(),
				super.parentSchemeId.getTransferable(),
				this.parentSchemeElementId.getTransferable(),
				this.parentSchemeProtoElementId.getTransferable(),
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
	 * @param abstractLink
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	@Override
	public void setAbstractLink(final AbstractLink abstractLink) {
		assert abstractLink == null || abstractLink instanceof Link : ErrorMessages.NATURE_INVALID;
		this.setAbstractLink((Link) abstractLink);
	}

	/**
	 * @param link
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	public void setAbstractLink(final Link link) {
		super.setAbstractLink(link);
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	@Override
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert abstractLinkType instanceof LinkType : ErrorMessages.NATURE_INVALID;
		this.setAbstractLinkType((LinkType) abstractLinkType);
	}

	/**
	 * @param linkType
	 */
	public void setAbstractLinkType(final LinkType linkType) {
		super.setAbstractLinkType(linkType);
	}

	/**
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	@Override
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
	@Override
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: ErrorMessages.NATURE_INVALID;
		this.setSourceAbstractSchemePort((SchemePort) sourceAbstractSchemePort);
	}

	public void setSourceAbstractSchemePort(final SchemePort sourceSchemePort) {
		super.setSourceAbstractSchemePort(sourceSchemePort);
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: ErrorMessages.NATURE_INVALID;
		this.setTargetAbstractSchemePort((SchemePort) targetAbstractSchemePort);
	}

	public void setTargetAbstractSchemePort(final SchemePort targetSchemePort) {
		super.setTargetAbstractSchemePort(targetSchemePort);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final IdlSchemeLink schemeLink = (IdlSchemeLink) transferable;
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
