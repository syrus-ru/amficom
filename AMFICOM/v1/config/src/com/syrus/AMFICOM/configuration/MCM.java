/*
 * $Id: MCM.java,v 1.35 2004/12/20 14:03:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;

/**
 * @version $Revision: 1.35 $, $Date: 2004/12/20 14:03:04 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MCM extends DomainMember implements Characterized {
	static final long serialVersionUID = 4622885259080741046L;

	private String name;
	private String description;
	private Identifier userId;
	private Identifier serverId;
	private short tcpPort;

	private List kiss;

	private List characteristics;

	private StorableObjectDatabase mcmDatabase;

	public MCM(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.kiss = new LinkedList();
		this.characteristics = new LinkedList();
		this.mcmDatabase = ConfigurationDatabaseContext.mcmDatabase;
		try {
			this.mcmDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MCM(MCM_Transferable mt) throws CreateObjectException {
		super(mt.header, new Identifier(mt.domain_id));

		this.name = new String(mt.name);
		this.description = new String(mt.description);
		this.userId = new Identifier(mt.user_id);
		this.serverId = new Identifier(mt.server_id);
		this.tcpPort = mt.tcp_port;

		try {
			this.kiss = new ArrayList(mt.kis_ids.length);
			for (int i = 0; i < mt.kis_ids.length; i++)
				this.kiss.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(mt.kis_ids[i]), true));			

			this.characteristics = new ArrayList(mt.characteristic_ids.length);
			for (int i = 0; i < mt.characteristic_ids.length; i++)
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(mt.characteristic_ids[i]), true));
			}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected MCM(Identifier id,
								Identifier creatorId,
								Identifier domainId,
								String name,
								String description,
								Identifier userId,
								Identifier serverId,
								short tcpPort) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.serverId = serverId;
		this.tcpPort = tcpPort;

		this.characteristics = new LinkedList();

		this.kiss = new LinkedList();
		
		this.mcmDatabase = ConfigurationDatabaseContext.mcmDatabase;
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.mcmDatabase != null)
				this.mcmDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

//	public static MCM getInstance(MCM_Transferable mt) throws CreateObjectException {
//		MCM mcm = new MCM(mt);
//		
//		mcm.mcmDatabase = ConfigurationDatabaseContext.mcmDatabase;
//		try {
//			if (mcm.mcmDatabase != null)
//				mcm.mcmDatabase.insert(mcm);
//		}
//		catch (IllegalDataException ide) {
//			throw new CreateObjectException(ide.getMessage(), ide);
//		}
//
//		return mcm;
//	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] kisIdsT = new Identifier_Transferable[this.kiss.size()];
		for (Iterator iterator = this.kiss.iterator(); iterator.hasNext();){
			KIS kis = (KIS)iterator.next();
			kisIdsT[i++] = (Identifier_Transferable)kis.getId().getTransferable();
		}

		return new MCM_Transferable(super.getHeaderTransferable(),
									(Identifier_Transferable)super.domainId.getTransferable(),
									new String(this.name),
									new String(this.description),
									(Identifier_Transferable)this.userId.getTransferable(),
									(Identifier_Transferable)this.serverId.getTransferable(),
									this.tcpPort,
									charIds,
									kisIdsT);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public Identifier getUserId() {
		return this.userId;
	}

	public Identifier getServerId() {
		return this.serverId;
	}

	public short getTCPPort() {
		return this.tcpPort;
	}

	public List getCharacteristics() {
        return Collections.unmodifiableList(this.characteristics);
    }
    
    protected void setCharacteristics0(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
    }
    
    public void setCharacteristics(final List characteristics) {
        this.setCharacteristics0(characteristics);
        super.currentVersion = super.getNextVersion();
    }

	public List getKISs() {
		return Collections.unmodifiableList(this.kiss);
	}

	public static MCM createInstance(Identifier creatorId,
									 Identifier domainId,
									 String name,
									 String description,
									 Identifier userId,
									 Identifier serverId,
									 short tcpPort) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null || 
				userId == null || serverId == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new MCM(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MCM_ENTITY_CODE),
								creatorId,
								domainId,
								name,
								description,
								userId,
								serverId,
								tcpPort);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MCM.createInstance | cannot generate identifier ", e);
		}
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  Identifier domainId,
											  String name,
											  String description,
											  Identifier userId,
											  Identifier serverId) {
		super.setAttributes(created,												
				modified,
				creatorId,
				modifierId,
				domainId);
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.serverId = serverId;
//		this.tcpPort = tcpPort;
	}

	protected synchronized void setKISs0(List kiss) {
		this.kiss.clear();
		if (kiss != null)
			this.kiss.addAll(kiss);		
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.addAll(this.kiss);
		dependencies.add(this.userId);
		dependencies.add(this.serverId);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}
}
