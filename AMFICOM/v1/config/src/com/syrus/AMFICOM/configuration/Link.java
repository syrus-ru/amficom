/*-
 * $Id: Link.java,v 1.81 2006/03/15 15:18:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

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
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.81 $, $Date: 2006/03/15 15:18:30 $
 * @module config
 */
public final class Link extends AbstractLink {
	private static final long serialVersionUID = 3257283626012783672L;

	public Link(final IdlLink idlLink) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlLink);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
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
					INITIAL_VERSION,
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
	protected synchronized void fromIdlTransferable(final IdlStorableObject transferable) throws IdlConversionException {
		final IdlLink idlLink = (IdlLink) transferable;
		super.fromTransferable(idlLink, new Identifier(idlLink.domainId));

		try {
			super.type = StorableObjectPool.getStorableObject(new Identifier(idlLink._typeId), true);
		} catch (final ApplicationException ae) {
			throw new IdlConversionException(ae);
		}

		this.name = idlLink.name;
		this.description = idlLink.description;
		this.inventoryNo = idlLink.inventoryNo;
		this.supplier = idlLink.supplier;
		this.supplierCode = idlLink.supplierCode;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	@Override
	public IdlLink getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlLinkHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.getDomainId().getIdlTransferable(),
				this.name,
				this.description,
				super.type.getId().getIdlTransferable(),
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

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected LinkWrapper getWrapper() {
		return LinkWrapper.getInstance();
	}
}
