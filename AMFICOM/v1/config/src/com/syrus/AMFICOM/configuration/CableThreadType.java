/*
 * $Id: CableThreadType.java,v 1.37 2005/06/02 14:27:03 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * <code>CableThreadType</code>, among other fields, contain references to
 * {@link LinkType} and {@link CableLinkType}. While the former is a type of
 * optical fiber (or an <i>abstract </i> optical fiber), the latter is a type of
 * cable (or an <i>abstract </i> cable containing this thread).
 *
 * @version $Revision: 1.37 $, $Date: 2005/06/02 14:27:03 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class CableThreadType extends StorableObjectType implements Namable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long  serialVersionUID	= 3689355429075628086L;

	private String name;
	private int color;
	private LinkType linkType;
	private CableLinkType cableLinkType;

	CableThreadType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		CableThreadTypeDatabase database = (CableThreadTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	CableThreadType(final CableThreadType_Transferable cttt)
			throws CreateObjectException {
		try {
			this.fromTransferable(cttt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	CableThreadType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final int color,
			final LinkType linkType,
			final CableLinkType cableLinkType) {
		super(id,
			new Date(),
			new Date(),
			creatorId,
			creatorId,
			version,
			codename,
			description);

		this.name = name;
		this.color = color;
		this.linkType = linkType;
		this.cableLinkType = cableLinkType;
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final int color,
			final LinkType linkType,
			final CableLinkType cableLinkType) throws CreateObjectException {
		assert creatorId != null
				&& codename != null
				&& description != null
				&& name != null
				&& linkType != null
				&& cableLinkType != null;
		try {
			CableThreadType cableThreadType = new CableThreadType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					name,
					color,
					linkType,
					cableLinkType);

			assert cableThreadType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			cableThreadType.changed = true;
			try {
				StorableObjectPool.putStorableObject(cableThreadType);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

			return cableThreadType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		CableThreadType_Transferable cttt = (CableThreadType_Transferable) transferable;
		super.fromTransferable(cttt.header, cttt.codename, cttt.description);
		this.name = cttt.name;
		this.color = cttt.color;
		this.linkType = (LinkType) StorableObjectPool.getStorableObject(new Identifier(cttt.linkTypeId), true);
		this.cableLinkType = (CableLinkType) StorableObjectPool.getStorableObject(new Identifier(cttt.cableLinkTypeId),
				true);
	}

	public IDLEntity getTransferable() {
		return new CableThreadType_Transferable(super.getHeaderTransferable(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				this.color,
				(Identifier_Transferable) this.linkType.getId().getTransferable(),
				(Identifier_Transferable) this.cableLinkType.getId().getTransferable());
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final int color,
			final LinkType linkType,
			final CableLinkType cableLinkType) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
		this.name = name;
		this.color = color;
		this.linkType = linkType;
		this.cableLinkType = cableLinkType;
	}

	public LinkType getLinkType() {
		return this.linkType;
	}

	public CableLinkType getCableLinkType() {
		return this.cableLinkType;
	}

	public void setColor(final int color) {
		this.color = color;
		super.changed = true;
	}

	public int getColor() {
		return this.color;
	}	

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		assert name != null;
		this.name = name;
		super.changed = true;
	}

	public void setLinkType(final LinkType linkType) {
		assert linkType != null;
		this.linkType = linkType;
		super.changed = true;
	}

	public void setCableLinkType(final CableLinkType cableLinkType) {
		assert cableLinkType != null;
		this.cableLinkType = cableLinkType;
		super.changed = true;
	}

	public Set getDependencies() {
		final Set dependencies = new HashSet(2);
		dependencies.add(this.linkType);
		dependencies.add(this.cableLinkType);
		return Collections.unmodifiableSet(dependencies);
	}
}
