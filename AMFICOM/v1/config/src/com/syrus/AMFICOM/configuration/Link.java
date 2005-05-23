/*
 * $Id: Link.java,v 1.50 2005/05/23 18:45:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.50 $, $Date: 2005/05/23 18:45:19 $
 * @author $Author: bass $
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

	public Link(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.LINK_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Link(Link_Transferable lt) throws CreateObjectException  {
		try {
			this.fromTransferable(lt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Link(Identifier id,
					Identifier creatorId,
					long version,
					Identifier domainId,
					String name,
					String description,
					AbstractLinkType type,
					String inventoryNo,
					String supplier,
					String supplierCode,
					int sort,
					int color,
					String mark) {
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

	public static Link createInstance(Identifier creatorId,
			Identifier domainId,
			String name,
			String description,
			AbstractLinkType type,
			String inventoryNo,
			String supplier,
			String supplierCode,
			LinkSort sort,
			int color,
			String mark) throws CreateObjectException {
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
			link.changed = true;
			return link;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
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
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Link_Transferable(super.getHeaderTransferable(),
									 (Identifier_Transferable)this.getDomainId().getTransferable(),
									 this.name,
									 this.description,
									 (Identifier_Transferable)this.type.getId().getTransferable(),
									 LinkSort.from_int(this.sort),
									 this.inventoryNo,
									 this.supplier,
									 this.supplierCode,
									 this.color,
									 (this.mark != null) ? this.mark : "",
									 charIds);
	}

	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												Identifier domainId,
												String name,
												String description,
												AbstractLinkType type,
												String inventoryNo,
												String supplier,
												String supplierCode,
												int sort,
												int color,
												String mark) {
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

	public void setDescription(String description) {
		this.description = description;
		super.changed = true;
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

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.changed = true;
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
		super.changed = true;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_LINK;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(LinkSort sort) {
		this.sort = sort.value();
		super.changed = true;
	}
	/**
	 * @param supplier The supplier to set.
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
		super.changed = true;
	}
	/**
	 * @param supplierCode The supplierCode to set.
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
		super.changed = true;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(AbstractLinkType type) {
		this.type = type;
		super.changed = true;
	}
	/**
	 * @param color The color to set.
	 */
	public void setColor(int color) {
		this.color = color;
		super.changed = true;
	}
	/**
	 * @param mark The mark to set.
	 */
	public void setMark(String mark) {
		this.mark = mark;
		super.changed = true;
	}
}
