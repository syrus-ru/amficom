/*
 * $Id: CableThreadType.java,v 1.6 2004/12/09 12:23:55 bob Exp $
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
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2004/12/09 12:23:55 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class CableThreadType extends AbstractLinkType {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3689355429075628086L;
	private String					mark;
	private String					color;
	private Identifier				linkTypeId;

	private StorableObjectDatabase	cableThreadTypeDatabase;

	public CableThreadType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;
		try {
			this.cableThreadTypeDatabase.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableThreadType(CableThreadType_Transferable ctt) {
		super(ctt.header, new String(ctt.codename), new String(ctt.description));
		this.mark = ctt.mark;
		this.color = ctt.color;
		this.linkTypeId = new Identifier(ctt.link_type_id);
	}

	protected CableThreadType(Identifier id,
			Identifier creatorId,
			String codename,
			String description,
			String mark,
			String color,			
			Identifier linkTypeId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
				codename, description);
		this.mark = mark;
		this.color = color;
		this.linkTypeId = linkTypeId;

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;

	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(	Identifier creatorId,
													String codename,
													String description,
													String mark,
													String color,			
													Identifier linkTypeId) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null || 
				mark == null || color == null || linkTypeId == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new CableThreadType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE), creatorId, codename, description, mark, color, linkTypeId);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CableThreadType.createInstance | cannot generate identifier ", e);
		}
	}

	public static CableThreadType getInstance(CableThreadType_Transferable ctt) throws CreateObjectException {
		CableThreadType cableThreadType = new CableThreadType(ctt);

		cableThreadType.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;
		try {
			if (cableThreadType.cableThreadTypeDatabase != null)
				cableThreadType.cableThreadTypeDatabase.insert(cableThreadType);
		} catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}

		return cableThreadType;
	}

	public Object getTransferable() {
		return new CableThreadType_Transferable(super.getHeaderTransferable(), 
										 super.codename,
										 (super.description != null) ? super.description : "",										  
										 (this.mark != null) ? this.mark : "",
										 (this.color != null) ? this.color : "",		
										 (Identifier_Transferable) this.linkTypeId.getTransferable());
	}

	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												String codename,
												String description,
												String mark,
												String color,			
												Identifier linkTypeId) {
		super.setAttributes(created, modified, creatorId, modifierId, codename, description);
		this.mark = mark;
		this.color = color;
		this.linkTypeId = linkTypeId;
	}

	public Identifier getLinkTypeId() {
		return this.linkTypeId;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public String getMark() {
		return this.mark;
	}
	

	public List getDependencies() {
		return Collections.singletonList(this.linkTypeId);
	}

}
