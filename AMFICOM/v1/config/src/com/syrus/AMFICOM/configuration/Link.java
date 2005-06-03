/*
 * $Id: Link.java,v 1.55 2005/06/03 20:37:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
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
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.55 $, $Date: 2005/06/03 20:37:53 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class Link extends DomainMember implements Characterizable, TypedObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257283626012783672L;
	private AbstractLinkType type;
	private String name;
	private String description;
	private String inventoryNo;
	private String supplier;
	private String supplierCode;
	private int sort;
	private String mark;
	private int color;

	private Set characteristics;

	Link(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		LinkDatabase database = (LinkDatabase) DatabaseContext.getDatabase(ObjectEntities.LINK_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	Link(final Link_Transferable lt) throws CreateObjectException  {
		try {
			this.fromTransferable(lt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Link(final Identifier id,
			final Identifier creatorId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final AbstractLinkType type,
			final String inventoryNo,
			final String supplier,
			final String supplierCode,
			final int sort,
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
		this.type = type;
		this.inventoryNo = inventoryNo;
		this.supplier = supplier;
		this.supplierCode = supplierCode;

		this.sort = sort;
		this.color = color;
		this.mark = mark;
		this.characteristics = new HashSet();
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */

	public static Link createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final AbstractLinkType type,
			final String inventoryNo,
			final String supplier,
			final String supplierCode,
			final LinkSort sort,
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
				|| sort == null
				|| mark == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Link link = new Link(IdentifierPool.getGeneratedIdentifier(ObjectEntities.LINK_ENTITY_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description,
					type,
					inventoryNo,
					supplier,
					supplierCode,
					sort.value(),
					color,
					mark);

			assert link.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			link.markAsChanged();

			return link;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		Link_Transferable lt = (Link_Transferable) transferable;
		super.fromTransferable(lt.header, new Identifier(lt.domain_id));

		this.name = lt.name;
		this.description = lt.description;
		this.inventoryNo = lt.inventoryNo;
		this.supplier = lt.supplier;
		this.supplierCode = lt.supplierCode;
		this.sort = lt.sort.value();

		Set characteristicIds = Identifier.fromTransferables(lt.characteristic_ids);
		this.characteristics = new HashSet(lt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));

		this.type = (AbstractLinkType) StorableObjectPool.getStorableObject(new Identifier(lt.type_id), true);
	}

	public IDLEntity getTransferable() {
		Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);

		return new Link_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				this.name,
				this.description,
				(Identifier_Transferable) this.type.getId().getTransferable(),
				LinkSort.from_int(this.sort),
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
			final AbstractLinkType type,
			final String inventoryNo,
			final String supplier,
			final String supplierCode,
			final int sort,
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
			this.type = type;
			this.inventoryNo = inventoryNo;
			this.supplier = supplier;
			this.supplierCode = supplierCode;
			this.sort = sort;
			this.color = color;
			this.mark = mark;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public String getInventoryNo() {
		return this.inventoryNo;
	}

	public String getName() {
		return this.name;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public LinkSort getSort() {
		return LinkSort.from_int(this.sort);
	}

	public int getColor() {
		return this.color;
	}

	public String getMark() {
		return this.mark;
	}

	public Set getDependencies() {
		return Collections.singleton(this.type);
	}

	public void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.markAsChanged();
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(final LinkSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}
	/**
	 * @param supplier The supplier to set.
	 */
	public void setSupplier(final String supplier) {
		this.supplier = supplier;
		super.markAsChanged();
	}
	/**
	 * @param supplierCode The supplierCode to set.
	 */
	public void setSupplierCode(final String supplierCode) {
		this.supplierCode = supplierCode;
		super.markAsChanged();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(final AbstractLinkType type) {
		this.type = type;
		super.markAsChanged();
	}
	/**
	 * @param color The color to set.
	 */
	public void setColor(final int color) {
		this.color = color;
		super.markAsChanged();
	}
	/**
	 * @param mark The mark to set.
	 */
	public void setMark(final String mark) {
		this.mark = mark;
		super.markAsChanged();
	}
}
