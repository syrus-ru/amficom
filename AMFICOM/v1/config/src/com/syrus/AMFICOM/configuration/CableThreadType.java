/*
 * $Id: CableThreadType.java,v 1.8 2004/12/09 16:12:48 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.8 $, $Date: 2004/12/09 16:12:48 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class CableThreadType extends AbstractLinkType {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3689355429075628086L;
	private String name;
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
        this.name = ctt.name;
        this.color = ctt.color;
        this.linkTypeId = new Identifier(ctt.linkTypeId);
	}

	protected CableThreadType(Identifier id,
			Identifier creatorId,
			String codename,
			String description,
			String color,
            String name,
			Identifier linkTypeId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
				codename, description);
		this.name = name;
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

													String name,
                                                    String color,                                                    
													Identifier linkTypeId) throws CreateObjectException {

		if (creatorId == null || codename == null || description == null || 
				name == null || color == null || linkTypeId == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			return new CableThreadType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE), creatorId, codename, description, name, color, linkTypeId);
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
                                         (this.color != null) ? this.color : "",                                         
										 (Identifier_Transferable) this.linkTypeId.getTransferable());
	}

	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												String codename,
												String description,
                                                String name,
												String color,			
												Identifier linkTypeId) {
		super.setAttributes(created, modified, creatorId, modifierId, codename, description);
		this.name = name;
        this.color = color;
		this.linkTypeId = linkTypeId;
	}

	public Identifier getLinkTypeId() {
		return this.linkTypeId;
	}
	
	public String getColor() {
		return this.color;
	}
    
    public String getName() {
        return this.name;
    }
	
	public List getDependencies() {
		return Collections.singletonList(this.linkTypeId);
	}

}
