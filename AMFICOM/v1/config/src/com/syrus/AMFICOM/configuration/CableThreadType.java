/*
 * $Id: CableThreadType.java,v 1.24 2005/04/01 11:02:30 bass Exp $
 *
 * Copyright ø 2004 Syrus Systems.
 * Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“.
 * “œ≈À‘: ·ÌÊÈÎÔÌ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * <code>CableThreadType</code>, among other fields, contain references to
 * {@link LinkType} and {@link CableLinkType}. While the former is a type of
 * optical fiber (or an <i>abstract </i> optical fiber), the latter is a type of
 * cable (or an <i>abstract </i> cable containing this thread).
 *
 * @version $Revision: 1.24 $, $Date: 2005/04/01 11:02:30 $
 * @author $Author: bass $
 * @module config_v1
 */

public final class CableThreadType extends StorableObjectType {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long  serialVersionUID	= 3689355429075628086L;

	private String name;
	private int color;
	private LinkType linkType;
	private CableLinkType cableLinkType;

	private StorableObjectDatabase	cableThreadTypeDatabase;

	public CableThreadType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		try {
			this.cableThreadTypeDatabase.retrieve(this);
		}
		catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableThreadType(final CableThreadType_Transferable transferable)
			throws CreateObjectException {
		super(transferable.header, transferable.codename,
				transferable.description);
		this.name = transferable.name;
		this.color = transferable.color;
		try {
			this.linkType = (LinkType) ConfigurationStorableObjectPool
					.getStorableObject(
							new Identifier(
									transferable.linkTypeId),
							true);
			this.cableLinkType = (CableLinkType) ConfigurationStorableObjectPool
					.getStorableObject(
							new Identifier(
									transferable.cableLinkTypeId),
							true);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
	}

	protected CableThreadType(final Identifier id,
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

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
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
		assert creatorId != null && codename != null
				&& description != null && name != null
				&& linkType != null && cableLinkType != null;
//		assert color != java.awt.Color.ÛÂÚÔ_‚ıÚÔ_Ì·ÏÈÓÔ˜˘Í;
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
			cableThreadType.changed = true;
			return cableThreadType;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"CableThreadType.createInstance | cannot generate identifier ",
					ioee);
		}
	}

	public Object getTransferable() {
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
