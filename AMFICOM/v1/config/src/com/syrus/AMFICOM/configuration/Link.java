/*
 * $Id: Link.java,v 1.31 2005/01/26 15:09:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;

/**
 * @version $Revision: 1.31 $, $Date: 2005/01/26 15:09:22 $
 * @author $Author: bob $
 * @module config_v1
 */
public class Link extends DomainMember implements Characterized, TypedObject {

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

	private List characteristics;

	private StorableObjectDatabase linkDatabase;

	public Link(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new LinkedList();
		this.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
		try {
			this.linkDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Link(Link_Transferable lt) throws CreateObjectException  {
		super(lt.header,
				new Identifier(lt.domain_id));

		this.name = lt.name;
		this.description = lt.description;
		this.inventoryNo = lt.inventoryNo;
		this.supplier = lt.supplier;
		this.supplierCode = lt.supplierCode;
		this.sort = lt.sort.value();

		try {
			this.characteristics = new ArrayList(lt.characteristic_ids.length);
			for (int i = 0; i < lt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(lt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		try {
			this.type = (AbstractLinkType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(lt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
	}

	protected Link(Identifier id,
					Identifier creatorId,
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
		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
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
										String mark) throws CreateObjectException{
		if (creatorId == null || domainId == null || name == null || description == null ||
				type == null || inventoryNo == null || supplier == null || supplierCode == null ||
				sort == null || mark == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Link(IdentifierPool.getGeneratedIdentifier(ObjectEntities.LINK_ENTITY_CODE),
					creatorId,
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
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Link.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.linkDatabase != null)
				this.linkDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Link_Transferable(super.getHeaderTransferable(),
									 (Identifier_Transferable)this.getDomainId().getTransferable(),
									 new String(this.name),
									 new String(this.description),
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
		super.currentVersion = super.getNextVersion();
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

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}

	protected void setCharacteristics0(final List characteristics) {
		if (characteristics != null)
			this.characteristics.clear();
		else
			this.characteristics = new LinkedList();
		this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(LinkSort sort) {
		this.sort = sort.value();
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param supplier The supplier to set.
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param supplierCode The supplierCode to set.
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(AbstractLinkType type) {
		this.type = type;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param color The color to set.
	 */
	public void setColor(int color) {
		this.color = color;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param mark The mark to set.
	 */
	public void setMark(String mark) {
		this.mark = mark;
		super.currentVersion = super.getNextVersion();
	}
}
