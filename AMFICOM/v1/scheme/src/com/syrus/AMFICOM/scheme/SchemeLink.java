/*-
 * $Id: SchemeLink.java,v 1.12 2005/04/14 11:15:52 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.SchemeLink_Transferable;

import java.util.*;
import org.omg.CORBA.portable.IDLEntity;

/**
 * #10 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/04/14 11:15:52 $
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

	public static SchemeLink createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeLink.changed = true;
			return schemeLink;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
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

	public SchemeElement getParentSchemeElement() {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		throw new UnsupportedOperationException();
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
