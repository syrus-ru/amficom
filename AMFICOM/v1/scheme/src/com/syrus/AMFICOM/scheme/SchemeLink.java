/*-
 * $Id: SchemeLink.java,v 1.20 2005/04/25 15:07:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.SchemeLink_Transferable;
import com.syrus.util.Log;

/**
 * #10 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.20 $, $Date: 2005/04/25 15:07:11 $
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

		this.schemeLinkDatabase = SchemeDatabaseContext.getSchemeLinkDatabase();
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

		this.schemeLinkDatabase = SchemeDatabaseContext.getSchemeLinkDatabase();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeLink(final SchemeLink_Transferable transferable) throws CreateObjectException {
		this.schemeLinkDatabase = SchemeDatabaseContext.getSchemeLinkDatabase();
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
		return createInstance(creatorId, name, "", 0, 0, null, null, //$NON-NLS-1$
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
		return createInstance(creatorId, name, "", 0, 0, null, null, //$NON-NLS-1$
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
		return createInstance(creatorId, name, "", 0, 0, null, null, //$NON-NLS-1$
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
		return createInstance(creatorId, name, "", 0, 0, null, null, //$NON-NLS-1$
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
			schemeLink.changed = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
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
			schemeLink.changed = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
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
			schemeLink.changed = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
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
			schemeLink.changed = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
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
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMELINK;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
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
			Log.debugMessage("SchemeLink.getParentScheme() | Parent Scheme was requested, while parent is either a SchemeElement or a SchemeProtoElement; returning null", //$NON-NLS-1$
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
			Log.debugMessage("SchemeLink.getParentSchemeElement() | Parent SchemeElement was requested, while parent is either a Scheme or a SchemeProtoElement; returning null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		try {
			assert super.parentSchemeId.isVoid() && this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			return (SchemeElement) SchemeStorableObjectPool.getStorableObject(this.parentSchemeElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeProtoElementId.isVoid()) {
			assert !super.parentSchemeId.isVoid() || !this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			Log.debugMessage("SchemeLink.getParentSchemeProtoElement() | Parent SchemeProtoElement was requested, while parent is either a Scheme or a SchemeElement; returning null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		try {
			assert super.parentSchemeId.isVoid() && this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
			return (SchemeProtoElement) SchemeStorableObjectPool.getStorableObject(this.parentSchemeProtoElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SiteNode getSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	public AbstractSchemePort getSourceAbstractSchemePort() {
		return getSourceSchemePort();
	}

	public SchemePort getSourceSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	public AbstractSchemePort getTargetAbstractSchemePort() {
		return getTargetSchemePort();
	}

	public SchemePort getTargetSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
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
	 * @todo skip invariance checks.
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
	 * @todo skip invariance checks.
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
			this.changed = true;
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
				SchemeStorableObjectPool.delete(this.id);
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
		this.changed = true;
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
				SchemeStorableObjectPool.delete(this.id);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId))
				return;
		}
		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		this.changed = true;
	}

	public void setSiteNode(final SiteNode siteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		setSourceSchemePort((SchemePort) sourceAbstractSchemePort);
	}

	public void setSourceSchemePort(final SchemePort sourceSchemePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		setTargetSchemePort((SchemePort) targetAbstractSchemePort);
	}

	public void setTargetSchemePort(final SchemePort targetSchemePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemeLink_Transferable schemeLink = (SchemeLink_Transferable) transferable;
		fromTransferable(schemeLink.header, schemeLink.name, schemeLink.description, schemeLink.parentSchemeId, schemeLink.characteristicIds);
		throw new UnsupportedOperationException();
	}
}
