/*-
 * $Id: SchemeLink.java,v 1.14 2005/04/15 19:22:55 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/04/15 19:22:55 $
 * @module scheme_v1
 */
public final class SchemeLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3834587703751947064L;

	private Identifier parentSchemeElementId;

	private Identifier parentSchemeProtoElementId;

	private Identifier siteNodeId;

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
	 */
	SchemeLink(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeLink(final SchemeLink_Transferable transferable) throws CreateObjectException {
		this.schemeLinkDatabase = SchemeDatabaseContext.getSchemeLinkDatabase();
		fromTransferable(transferable);
	}

	public static SchemeLink createInstance(final Identifier creatorId) throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SCHEME_LINK_ENTITY_CODE),
					created,
					created,
					creatorId,
					creatorId,
					0L);
			schemeLink.changed = true;
			return schemeLink;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
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
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	public AbstractLinkType getAbstractLinkType() {
		return getLinkType();
	}

	/**
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMELINK;
	}

	/**
	 * @see StorableObject#getDependencies()
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

	public LinkType getLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	public Scheme getParentScheme() {
		assert this.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeId.isVoid()) {
			assert !this.parentSchemeElementId.isVoid() || !this.parentSchemeProtoElementId.isVoid(): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			Log.debugMessage("SchemeLink.getParentScheme() | Parent Scheme was requested, while parent is either a SchemeElement or a SchemeProtoElement; returning null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		assert this.parentSchemeElementId.isVoid() && this.parentSchemeProtoElementId.isVoid(): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
		return super.getParentScheme();
	}

	public SchemeElement getParentSchemeElement() {
		assert this.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeElementId.isVoid()) {
			assert !this.parentSchemeId.isVoid() || !this.parentSchemeProtoElementId.isVoid(): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			Log.debugMessage("SchemeLink.getParentSchemeElement() | Parent SchemeElement was requested, while parent is either a Scheme or a SchemeProtoElement; returning null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		try {
			assert this.parentSchemeId.isVoid() && this.parentSchemeProtoElementId.isVoid(): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
			return (SchemeElement) SchemeStorableObjectPool.getStorableObject(this.parentSchemeElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		assert this.parentSchemeId != null && this.parentSchemeElementId != null && this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeProtoElementId.isVoid()) {
			assert !this.parentSchemeId.isVoid() || !this.parentSchemeElementId.isVoid(): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			Log.debugMessage("SchemeLink.getParentSchemeProtoElement() | Parent SchemeProtoElement was requested, while parent is either a Scheme or a SchemeElement; returning null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		try {
			assert this.parentSchemeId.isVoid() && this.parentSchemeElementId.isVoid(): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
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

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		setLinkType((LinkType) abstractLinkType);
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
	 * @param newLinkTypeImpl
	 */
	public void setLinkType(LinkType newLinkTypeImpl) {
		throw new UnsupportedOperationException();
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		throw new UnsupportedOperationException();
	}

	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		throw new UnsupportedOperationException();
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
	 * @see StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
