/*
 * $Id: LinkType.java,v 1.61 2005/07/04 13:00:53 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlLinkTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.61 $, $Date: 2005/07/04 13:00:53 $
 * @author $Author: bass $
 * @module config_v1
 */

public final class LinkType extends AbstractLinkType implements Characterizable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	private Set<Characteristic> characteristics;

	LinkType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();
		final LinkTypeDatabase database = (LinkTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.LINK_TYPE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	LinkType(final IdlLinkType ltt) throws CreateObjectException {
		try {
			this.fromTransferable(ltt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	LinkType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.sort = sort;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;
		this.characteristics = new HashSet<Characteristic>();
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static LinkType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final LinkTypeSort sort,
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) throws CreateObjectException {
		if (creatorId == null
				|| codename == null
				|| description == null
				|| name == null
				|| sort == null
				|| manufacturer == null
				|| manufacturerCode == null
				|| imageId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			LinkType linkType = new LinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.LINK_TYPE_CODE),
						creatorId,
						0L,
						codename,
						description,
						name,
						sort.value(),
						manufacturer,
						manufacturerCode,
						imageId);

			assert linkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			linkType.markAsChanged();

			return linkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlLinkType ltt = (IdlLinkType) transferable;
		super.fromTransferable(ltt, ltt.codename, ltt.description);

		this.sort = ltt.sort.value();
		this.manufacturer = ltt.manufacturer;
		this.manufacturerCode = ltt.manufacturerCode;
		this.imageId = new Identifier(ltt.imageId);
		this.name = ltt.name;

		final Set<Identifier> characteristicIds = Identifier.fromTransferables(ltt.characteristicIds);
		this.characteristics = new HashSet<Characteristic>(ltt.characteristicIds.length);
		final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjects(characteristicIds, true);
		this.setCharacteristics0(characteristics0);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlLinkType getTransferable(final ORB orb) {
		final IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return IdlLinkTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version,
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				LinkTypeSort.from_int(this.sort), this.manufacturer, this.manufacturerCode,
				this.imageId.getTransferable(),
				charIds);
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null
				&& this.manufacturer != null
				&& this.manufacturerCode != null
				&& this.imageId != null;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.sort = sort;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
		super.markAsChanged();
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(final String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		super.markAsChanged();
	}

	public LinkTypeSort getSort() {
		return LinkTypeSort.from_int(this.sort);
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name= name;
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
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

	public Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set<Characteristic> characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set<Characteristic> characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param imageId The imageId to set.
	 */
	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(final LinkTypeSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}
}
