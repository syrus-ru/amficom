/*
 * $Id: CableThreadType.java,v 1.14 2005/01/17 11:49:37 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.14 $, $Date: 2005/01/17 11:49:37 $
 * @author $Author: stas $
 * @module config_v1
 */

public class CableThreadType extends StorableObjectType {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long  serialVersionUID	= 3689355429075628086L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_LINK_TYPE = "type";
	private static Object[][] exportColumns = null;

	private String             name;
	private int                color;
	private LinkType           type;

	private StorableObjectDatabase	cableThreadTypeDatabase;

	public CableThreadType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;
		try {
			this.cableThreadTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableThreadType(CableThreadType_Transferable ctt) throws CreateObjectException {
		super(ctt.header, new String(ctt.codename), new String(ctt.description));
		this.name = ctt.name;
		this.color = ctt.color;
		try {
			this.type = (LinkType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(ctt.linkTypeId), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;
	}

	protected CableThreadType(Identifier id,
			Identifier creatorId,
			String codename,
			String description,
			String name,
						int color,
						LinkType linkType) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
				codename, description);
		this.name = name;
				this.color = color;
		this.type = linkType;

		super.currentVersion = super.getNextVersion();

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;

	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(Identifier creatorId,
																		String codename,
																		String description,
																		String name,
																		int color,
																		LinkType linkType) throws CreateObjectException {

		if (creatorId == null || codename == null || description == null ||
				name == null || linkType == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			return new CableThreadType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE), creatorId, codename, description, name, color, linkType);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CableThreadType.createInstance | cannot generate identifier ", e);
		}

	}

	public void insert() throws CreateObjectException {
		try {
			if (this.cableThreadTypeDatabase != null)
				this.cableThreadTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

//	public static CableThreadType getInstance(CableThreadType_Transferable ctt) throws CreateObjectException {
//		CableThreadType cableThreadType = new CableThreadType(ctt);
//
//		cableThreadType.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;
//		try {
//			if (cableThreadType.cableThreadTypeDatabase != null)
//				cableThreadType.cableThreadTypeDatabase.insert(cableThreadType);
//		} catch (IllegalDataException ide) {
//			throw new CreateObjectException(ide.getMessage(), ide);
//		}
//
//		return cableThreadType;
//	}

	public Object getTransferable() {
		return new CableThreadType_Transferable(super.getHeaderTransferable(),
										 super.codename,
										 (super.description != null) ? super.description : "",
																				 (this.name != null) ? this.name : "",
																				 this.color,
										 (Identifier_Transferable) this.type.getId().getTransferable());
	}

	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												String codename,
												String description,
																								String name,
												int color,
												LinkType linkType) {
		super.setAttributes(created, modified, creatorId, modifierId, codename, description);
		this.name = name;
				this.color = color;
		this.type = linkType;
	}

	public LinkType getLinkType() {
		return this.type;
	}

	public int getColor() {
		return this.color;
	}

		public String getName() {
				return this.name;
		}

	public void setName(String name) {
		this.name = name;
	}

	public void setLinkType(LinkType type) {
		this.type = type;
	}
	public List getDependencies() {
		return Collections.singletonList(this.type);
	}

	public Object[][] exportColumns() {
		if (exportColumns == null) {
			exportColumns = new Object[5][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_COLOR;
			exportColumns[4][0] = COLUMN_LINK_TYPE;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[2][1] = String.valueOf(getColor());
		exportColumns[4][1] = getLinkType().getId();

		return exportColumns;
	}
}
