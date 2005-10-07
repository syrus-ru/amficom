/*-
 * $Id: Link.java,v 1.73 2005/10/07 10:04:24 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlLink;
import com.syrus.AMFICOM.configuration.corba.IdlLinkHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @author $Author: bass $
 * @version $Revision: 1.73 $, $Date: 2005/10/07 10:04:24 $
 * @module config
 */
public final class Link extends AbstractLink {
	private static final long serialVersionUID = 3257283626012783672L;

	public Link(final IdlLink idlLink) throws CreateObjectException {
		try {
			this.fromTransferable(idlLink);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Link(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final LinkType type,
			final String inventoryNo,
			final String supplier,
			final String supplierCode,
			final int color,
			final String mark) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		super.type = type;
		this.inventoryNo = inventoryNo;
		this.supplier = supplier;
		this.supplierCode = supplierCode;

		this.color = color;
		this.mark = mark;
	}

	/**
	 * Creates a new instance for client.
	 *
	 * @throws CreateObjectException
	 */
	public static Link createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final LinkType type,
			final String inventoryNo,
			final String supplier,
			final String supplierCode,
			final int color,
			final String mark) throws CreateObjectException {
		if (creatorId == null
				|| domainId == null
				|| name == null
				|| description == null
				|| type == null
				|| inventoryNo == null
				|| supplier == null
				|| supplierCode == null
				|| mark == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Link link = new Link(IdentifierPool.getGeneratedIdentifier(LINK_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description,
					type,
					inventoryNo,
					supplier,
					supplierCode,
					color,
					mark);

			assert link.isValid() : OBJECT_STATE_ILLEGAL;

			link.markAsChanged();

			return link;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlLink idlLink = (IdlLink) transferable;
		super.fromTransferable(idlLink, new Identifier(idlLink.domainId));

		this.name = idlLink.name;
		this.description = idlLink.description;
		this.inventoryNo = idlLink.inventoryNo;
		this.supplier = idlLink.supplier;
		this.supplierCode = idlLink.supplierCode;

		super.type = (LinkType) StorableObjectPool.getStorableObject(new Identifier(idlLink._typeId), true);
	}

	/**
	 * @see com.syrus.util.TransferableObject#getTransferable(ORB)
	 */
	@Override
	public IdlLink getTransferable(final ORB orb) {
		return IdlLinkHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.getDomainId().getTransferable(),
				this.name,
				this.description,
				super.type.getId().getTransferable(),
				this.inventoryNo,
				this.supplier,
				this.supplierCode,
				this.color,
				(this.mark != null) ? this.mark : "");
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final LinkType type,
			final String inventoryNo,
			final String supplier,
			final String supplierCode,
			final int color,
			final String mark) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version,
					domainId);
			this.name = name;
			this.description = description;
			super.type = type;
			this.inventoryNo = inventoryNo;
			this.supplier = supplier;
			this.supplierCode = supplierCode;
			this.color = color;
			this.mark = mark;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TypedObject#getType()
	 */
	@Override
	public LinkType getType() {
		final AbstractLinkType type1 = super.getType();
		assert type1 == null || type1 instanceof LinkType : NATURE_INVALID;
		return (LinkType) type1;
	}

	@Override
	public void setType(final AbstractLinkType type) {
		assert type == null || type instanceof LinkType : NATURE_INVALID;
		this.setType((LinkType) type);
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final LinkType type) {
		super.setType(type);
	}
}
