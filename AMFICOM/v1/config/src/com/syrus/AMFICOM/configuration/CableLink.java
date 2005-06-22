/*-
 * $Id: CableLink.java,v 1.1 2005/06/22 15:05:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.IdlCableLink;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/22 15:05:18 $
 * @module config_v1
 */
public final class CableLink extends AbstractLink {
	private static final long serialVersionUID = 7733720151418798562L;

	CableLink(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();

		try {
			DatabaseContext.getDatabase(ObjectEntities.CABLELINK_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	CableLink(final IdlCableLink idlCableLink) throws CreateObjectException {
		try {
			this.fromTransferable(idlCableLink);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	CableLink(final Identifier id,
			final Identifier creatorId,
			final long version,
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
		this.characteristics = new HashSet<Characteristic>();
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
				|| mark == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			CableLink cableLink = new CableLink(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLELINK_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description,
					type,
					inventoryNo,
					supplier,
					supplierCode,
					color,
					mark);

			assert cableLink.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			cableLink.markAsChanged();

			return cableLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlCableLink idlCableLink = (IdlCableLink) transferable;
		super.fromTransferable(idlCableLink.header, new Identifier(idlCableLink.domainId));

		this.name = idlCableLink.name;
		this.description = idlCableLink.description;
		this.inventoryNo = idlCableLink.inventoryNo;
		this.supplier = idlCableLink.supplier;
		this.supplierCode = idlCableLink.supplierCode;

		Set characteristicIds = Identifier.fromTransferables(idlCableLink.characteristicIds);
		this.characteristics = new HashSet<Characteristic>(idlCableLink.characteristicIds.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));

		super.type = (CableLinkType) StorableObjectPool.getStorableObject(new Identifier(idlCableLink._typeId), true);
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IdlCableLink getTransferable() {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return new IdlCableLink(super.getHeaderTransferable(),
				this.getDomainId().getTransferable(),
				this.name,
				this.description,
				super.type.getId().getTransferable(),
				this.inventoryNo,
				this.supplier,
				this.supplierCode,
				this.color,
				(this.mark != null) ? this.mark : "",
				charIds);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
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
		assert type1 == null || type1 instanceof CableLinkType : ErrorMessages.NATURE_INVALID;
		return (CableLinkType) type1;
	}

	@Override
	public void setType(final AbstractLinkType type) {
		assert type == null || type instanceof CableLinkType : ErrorMessages.NATURE_INVALID;
		this.setType((CableLinkType) type);
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final CableLinkType type) {
		super.setType(type);
	}
}
