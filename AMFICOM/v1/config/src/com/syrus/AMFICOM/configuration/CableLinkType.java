/*
 * $Id: CableLinkType.java,v 1.59 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlCableLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlCableLinkTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.59 $, $Date: 2005/07/27 15:59:22 $
 * @author $Author: bass $
 * @module config
 */
public final class CableLinkType extends AbstractLinkType {

	private static final long serialVersionUID = 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	CableLinkType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.CABLELINK_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableLinkType(final IdlCableLinkType cltt) throws CreateObjectException {
		try {
			this.fromTransferable(cltt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	CableLinkType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
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
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static CableLinkType createInstance(final Identifier creatorId,
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
				|| manufacturerCode == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final CableLinkType cableLinkType = new CableLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLELINK_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					sort.value(),
					manufacturer,
					manufacturerCode,
					imageId);

			assert cableLinkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			cableLinkType.markAsChanged();

			return cableLinkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlCableLinkType cltt = (IdlCableLinkType) transferable;
		super.fromTransferable(cltt, cltt.codename, cltt.description);
		this.sort = cltt.sort.value();
		this.manufacturer = cltt.manufacturer;
		this.manufacturerCode = cltt.manufacturerCode;
		this.imageId = new Identifier(cltt.imageId);
		this.name = cltt.name;
	}
	
	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableLinkType getTransferable(final ORB orb) {

		return IdlCableLinkTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				LinkTypeSort.from_int(this.sort),
				this.manufacturer,
				this.manufacturerCode,
				this.imageId.getTransferable());
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.manufacturer != null && this.manufacturerCode != null;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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

	@Override
	public Identifier getImageId() {
		return this.imageId;
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}

	
	@Override
	public String getManufacturer() {
		return this.manufacturer;
	}

	@Override
	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
		super.markAsChanged();
	}

	@Override
	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	@Override
	public void setManufacturerCode(final String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		super.markAsChanged();
	}

	@Override
	public LinkTypeSort getSort() {
		return LinkTypeSort.from_int(this.sort);
	}
	
	public void setSort(final LinkTypeSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public Set getCableThreadTypes(final boolean breakOnLoadError) {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CABLETHREAD_TYPE_CODE);
		Set cableThreadTypes;
		try {
			cableThreadTypes = StorableObjectPool.getStorableObjectsByCondition(lic, true, breakOnLoadError);
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
			cableThreadTypes = Collections.EMPTY_SET;
		}
		return cableThreadTypes;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

}
