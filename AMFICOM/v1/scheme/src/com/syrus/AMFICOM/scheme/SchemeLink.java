/*-
 * $Id: SchemeLink.java,v 1.48 2005/07/22 15:09:40 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLinkHelper;
import com.syrus.util.Log;

/**
 * #10 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.48 $, $Date: 2005/07/22 15:09:40 $
 * @module scheme_v1
 */
public final class SchemeLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3834587703751947064L;

	private Identifier siteNodeId;

	Identifier parentSchemeElementId;

	Identifier parentSchemeProtoElementId;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(SCHEMELINK_CODE).retrieve(this);
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
				+ (parentSchemeProtoElement == null ? 0 : 1) <= 1: EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeLink(final IdlSchemeLink transferable) throws CreateObjectException {
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMELINK_CODE),
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert parentScheme != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMELINK_CODE),
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert parentSchemeElement != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMELINK_CODE),
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMELINK_CODE),
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
	public SchemeLink clone() throws CloneNotSupportedException {
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
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.siteNodeId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkId()
	 */
	@Override
	Identifier getAbstractLinkId() {
		final Identifier linkId = super.getAbstractLinkId();
		assert linkId.isVoid() || linkId.getMajor() == LINK_CODE;
		return linkId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLink()
	 */
	@Override
	public Link getAbstractLink() {
		final AbstractLink abstractLink = super.getAbstractLink();
		assert abstractLink == null || abstractLink instanceof Link : OBJECT_BADLY_INITIALIZED;
		return (Link) abstractLink;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkTypeId()
	 */
	@Override
	Identifier getAbstractLinkTypeId() {
		final Identifier linkTypeId = super.getAbstractLinkTypeId();
		assert linkTypeId.isVoid() || linkTypeId.getMajor() == LINK_TYPE_CODE;
		return linkTypeId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	@Override
	public LinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof LinkType : OBJECT_BADLY_INITIALIZED;
		return (LinkType) abstractLinkType;
	}

	/**
	 * @see AbstractSchemeElement#getParentSchemeId()
	 */
	@Override
	Identifier getParentSchemeId() {
		final Identifier parentSchemeId1 = super.getParentSchemeId();
		assert this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final boolean parentSchemeIdVoid = parentSchemeId1.isVoid();
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert (parentSchemeIdVoid ? 1 : 0)
				+ (parentSchemeElementIdVoid ? 1 : 0)
				+ (parentSchemeProtoElementIdVoid ? 1 : 0) == 2 : EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeIdVoid) {
			Log.debugMessage("SchemeLink.getParentSchemeId() | Parent Scheme was requested, while parent is either a SchemeElement or a SchemeProtoElement; returning null",
					FINE);
		}
		return parentSchemeId1;
	}

	Identifier getParentSchemeElementId() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final boolean parentSchemeIdVoid = this.parentSchemeId.isVoid();
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeElementIdVoid || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		assert (parentSchemeIdVoid ? 1 : 0)
				+ (parentSchemeElementIdVoid ? 1 : 0)
				+ (parentSchemeProtoElementIdVoid ? 1 : 0) == 2 : EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeElementIdVoid) {
			Log.debugMessage("SchemeLink.getParentSchemeElementId() | Parent SchemeElement was requested, while parent is either a Scheme or a SchemeProtoElement; returning null",
					FINE);
		}
		return this.parentSchemeElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeElementId()}.
	 */
	public SchemeElement getParentSchemeElement() {
		try {
			return (SchemeElement) StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getParentSchemeProtoElementId() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final boolean parentSchemeIdVoid = this.parentSchemeId.isVoid();
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		assert (parentSchemeIdVoid ? 1 : 0)
				+ (parentSchemeElementIdVoid ? 1 : 0)
				+ (parentSchemeProtoElementIdVoid ? 1 : 0) == 2 : EXACTLY_ONE_PARENT_REQUIRED;
		if (this.parentSchemeProtoElementId.isVoid()) {
			Log.debugMessage("SchemeLink.getParentSchemeProtoElementId() | Parent SchemeProtoElement was requested, while parent is either a Scheme or a SchemeElement; returning null",
					FINE);
		}
		return this.parentSchemeProtoElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoElementId()}.
	 */
	public SchemeProtoElement getParentSchemeProtoElement() {
		try {
			return (SchemeProtoElement) StorableObjectPool.getStorableObject(this.getParentSchemeProtoElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getSiteNodeId() {
		assert this.siteNodeId != null: OBJECT_NOT_INITIALIZED;
		assert this.siteNodeId.isVoid() || this.siteNodeId.getMajor() == SITENODE_CODE;
		return this.siteNodeId;
	}

	/**
	 * A wrapper around {@link #getSiteNodeId()}.
	 */
	public SiteNode getSiteNode() {
		try {
			return (SiteNode) StorableObjectPool.getStorableObject(this.getSiteNodeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePortId()
	 */
	@Override
	Identifier getSourceAbstractSchemePortId() {
		final Identifier sourceSchemePortId = super.getSourceAbstractSchemePortId();
		assert sourceSchemePortId.isVoid() || sourceSchemePortId.getMajor() == SCHEMEPORT_CODE;
		return sourceSchemePortId;
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	@Override
	public SchemePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: OBJECT_BADLY_INITIALIZED;
		return (SchemePort) sourceAbstractSchemePort;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePortId()
	 */
	@Override
	Identifier getTargetAbstractSchemePortId() {
		final Identifier targetSchemePortId = super.getTargetAbstractSchemePortId();
		assert targetSchemePortId.isVoid() || targetSchemePortId.getMajor() == SCHEMEPORT_CODE;
		return targetSchemePortId;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	@Override
	public SchemePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: OBJECT_BADLY_INITIALIZED;
		return (SchemePort) targetAbstractSchemePort;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeLink getTransferable(final ORB orb) {
		return IdlSchemeLinkHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version, super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				this.getAbstractLinkTypeId().getTransferable(),
				this.getAbstractLinkId().getTransferable(),
				this.getSiteNodeId().getTransferable(),
				this.getSourceAbstractSchemePortId().getTransferable(),
				this.getTargetAbstractSchemePortId().getTransferable(),
				this.getParentSchemeId().getTransferable(),
				this.getParentSchemeElementId().getTransferable(),
				this.getParentSchemeProtoElementId().getTransferable());
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

		assert siteNodeId != null: NON_NULL_EXPECTED;

		assert parentSchemeElementId != null: NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId != null: NON_NULL_EXPECTED;
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
		assert abstractLink == null || abstractLink instanceof Link : NATURE_INVALID;
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
		assert abstractLinkType instanceof LinkType : NATURE_INVALID;
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
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to another scheme.
			 */
			assert this.parentSchemeElementId.isVoid()
					&& this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
			super.setParentScheme(parentScheme);
		} else {
			if (!this.parentSchemeElementId.isVoid()) {
				/*
				 * Moving from a scheme element to a scheme.
				 */
				assert this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
				if (parentScheme == null) {
					Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
					return;
				}
				this.parentSchemeElementId = VOID_IDENTIFIER;
			} else {
				/*
				 * Moving from a scheme protoelement to a scheme.
				 */
				assert !this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
				if (parentScheme == null) {
					Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
					return;
				}
				this.parentSchemeProtoElementId = VOID_IDENTIFIER;
			}
			super.parentSchemeId = parentScheme.getId();
			super.markAsChanged();
		}
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		Identifier newParentSchemeElementId;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to a scheme element.
			 */
			assert this.parentSchemeElementId.isVoid()
					&& this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			super.parentSchemeId = VOID_IDENTIFIER;
		} else if (!this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a scheme element to another scheme element.
			 */
			assert this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeElement == null) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
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
			assert !this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		Identifier newParentSchemeProtoElementId;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to a scheme protoelement.
			 */
			assert this.parentSchemeElementId.isVoid()
					&& this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			super.parentSchemeId = VOID_IDENTIFIER;
		} else if (!this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a scheme element to a scheme protoelement.
			 */
			assert this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			this.parentSchemeElementId = VOID_IDENTIFIER;
		} else {
			/*
			 * Moving from a scheme protoelement to another scheme protoelement.
			 */
			assert !this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
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
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: NATURE_INVALID;
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
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: NATURE_INVALID;
		this.setTargetAbstractSchemePort((SchemePort) targetAbstractSchemePort);
	}

	public void setTargetAbstractSchemePort(final SchemePort targetSchemePort) {
		super.setTargetAbstractSchemePort(targetSchemePort);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlSchemeLink schemeLink = (IdlSchemeLink) transferable;
		super.fromTransferable(schemeLink, schemeLink.name,
				schemeLink.description,
				schemeLink.physicalLength,
				schemeLink.opticalLength, schemeLink.linkTypeId,
				schemeLink.linkId,
				schemeLink.sourceSchemePortId,
				schemeLink.targetSchemePortId,
				schemeLink.parentSchemeId);
		this.siteNodeId = new Identifier(schemeLink.siteNodeId);
		this.parentSchemeElementId = new Identifier(schemeLink.parentSchemeElementId);
		this.parentSchemeProtoElementId = new Identifier(schemeLink.parentSchemeProtoElementId);
	}

	/**
	 * @todo Implement.
	 */
	Map<Identifier, Identifier> getIdMap() {
		return Collections.emptyMap();
	}
}
