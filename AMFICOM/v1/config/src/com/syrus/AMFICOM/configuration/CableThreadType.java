/*
 * $Id: CableThreadType.java,v 1.21 2005/02/14 09:15:45 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
 * @version $Revision: 1.21 $, $Date: 2005/02/14 09:15:45 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class CableThreadType extends StorableObjectType {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long  serialVersionUID	= 3689355429075628086L;

	private String name;
	private int color;
	private LinkType type;

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
	                          long version,
	                          String codename,
	                          String description,
	                          String name,
	                          int color,
	                          LinkType linkType) {
		super(id, 
			new Date(System.currentTimeMillis()), 
			new Date(System.currentTimeMillis()), 
			creatorId, 
			creatorId,
			version,
			codename, 
			description);
		this.name = name;
				this.color = color;
		this.type = linkType;

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
			CableThreadType cableThreadType = new CableThreadType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE), 
				creatorId, 
				0L,
				codename, 
				description, 
				name, 
				color, 
				linkType);
			cableThreadType.changed = true;
			return cableThreadType;
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CableThreadType.createInstance | cannot generate identifier ", e);
		}

	}

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
												long version,
												String codename,
												String description,
												String name,
												int color,
												LinkType linkType) {
		super.setAttributes(created, 
			modified, 
			creatorId, 
			modifierId,
			version,
			codename, 
			description);
		this.name = name;
				this.color = color;
		this.type = linkType;
	}

	public LinkType getLinkType() {
		return this.type;
	}

	public void setColor(int color) {
		this.color = color;
		super.changed = true;
	}

	public int getColor() {
		return this.color;
	}	

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}

	public void setLinkType(LinkType type) {
		this.type = type;
		super.changed = true;
	}
	public List getDependencies() {
		return Collections.singletonList(this.type);
	}

}
