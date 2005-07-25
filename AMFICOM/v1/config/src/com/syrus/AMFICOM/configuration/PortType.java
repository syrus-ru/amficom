/*
 * $Id: PortType.java,v 1.72 2005/07/25 20:49:45 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.corba.IdlPortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
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
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.72 $, $Date: 2005/07/25 20:49:45 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class PortType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = -115251480084275101L;

	private String name;
	private int sort;
	private int kind;

	PortType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		final PortTypeDatabase database = (PortTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PORT_TYPE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public PortType(final IdlPortType ptt) throws CreateObjectException {
		try {
			this.fromTransferable(ptt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	PortType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final int kind) {
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
		this.kind = kind;
	}


	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param sort
	 * @param kind
	 * @throws CreateObjectException
	 */
	public static PortType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final PortTypeSort sort,
			final PortTypeKind kind) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null ||
				sort == null || kind == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final PortType portType = new PortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PORT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					sort.value(),
					kind.value());

			assert portType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			portType.markAsChanged();

			return portType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPortType ptt = (IdlPortType) transferable;
		super.fromTransferable(ptt, ptt.codename, ptt.description);
		this.name = ptt.name;
		this.sort = ptt.sort.value();
		this.kind = ptt.kind.value();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPortType getTransferable(final ORB orb) {
		return IdlPortTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				PortTypeSort.from_int(this.sort),
				PortTypeKind.from_int(this.kind));
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
			final int kind) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.name = name;
		this.sort = sort;
		this.kind = kind;
	}

	public String getName() {
		return this.name;
	}

	public PortTypeSort getSort() {
		return PortTypeSort.from_int(this.sort);
	}

	public void setSort(final PortTypeSort sort) {
		this.sort = sort.value();
	}

	public PortTypeKind getKind() {
		return PortTypeKind.from_int(this.kind);
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

}
