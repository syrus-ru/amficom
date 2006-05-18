/*-
 * $Id: CableLink.java,v 1.22 2006/04/19 13:22:15 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlCableLink;
import com.syrus.AMFICOM.configuration.corba.IdlCableLinkHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2006/04/19 13:22:15 $
 * @module config
 */
public final class CableLink extends AbstractLink implements IdlTransferableObjectExt<IdlCableLink> {
	private static final long serialVersionUID = 7733720151418798562L;

	public CableLink(final IdlCableLink idlCableLink) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlCableLink);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	CableLink(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final CableLinkType type,
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
	public static CableLink createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final CableLinkType type,
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
				|| mark == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final CableLink cableLink = new CableLink(IdentifierPool.getGeneratedIdentifier(CABLELINK_CODE),
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

			assert cableLink.isValid() : OBJECT_STATE_ILLEGAL;

			cableLink.markAsChanged();

			return cableLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public synchronized void fromIdlTransferable(final IdlCableLink idlCableLink) throws IdlConversionException {
		try {
			super.fromIdlTransferable(idlCableLink, new Identifier(idlCableLink.domainId));

			this.name = idlCableLink.name;
			this.description = idlCableLink.description;
			this.inventoryNo = idlCableLink.inventoryNo;
			this.supplier = idlCableLink.supplier;
			this.supplierCode = idlCableLink.supplierCode;

			super.type = (CableLinkType) StorableObjectPool.getStorableObject(new Identifier(idlCableLink._typeId), true);
		} catch (final ApplicationException ae) {
			throw new IdlConversionException(ae);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableLink getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlCableLinkHelper.init(orb,
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
			final CableLinkType type,
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
	public CableLinkType getType() {
		final AbstractLinkType type1 = super.getType();
		assert type1 == null || type1 instanceof CableLinkType : NATURE_INVALID;
		return (CableLinkType) type1;
	}

	@Override
	public void setType(final AbstractLinkType type) {
		assert type == null || type instanceof CableLinkType : NATURE_INVALID;
		this.setType((CableLinkType) type);
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final CableLinkType type) {
		super.setType(type);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CableLinkWrapper getWrapper() {
		return CableLinkWrapper.getInstance();
	}
}
