/*
 * $Id: Link.java,v 1.2 2004/10/25 09:44:20 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;


/**
 * @version $Revision: 1.2 $, $Date: 2004/10/25 09:44:20 $
 * @author $Author: bob $
 * @module config_v1
 */
public class Link extends DomainMember implements Characterized, TypedObject {

	private String name;
	private String description;		
	private LinkType type;		
	private String inventoryNo;
	private String supplier;
	private String supplierCode;
	private int sort;
	
	private Identifier linkId;
	private String mark;
	private String color;
	
	private List characteristics;
	
	private StorableObjectDatabase linkDatabase;

	public Link(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
		try {
			this.linkDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Link(Link_Transferable lt) throws CreateObjectException  {
		super(new Identifier(lt.id),
					new Date(lt.created),
					new Date(lt.modified),
					new Identifier(lt.creator_id),
					new Identifier(lt.modifier_id),
					new Identifier(lt.domain_id));
		
		this.name = lt.name;
		this.description = lt.description;
		this.inventoryNo = lt.inventoryNo;
		this.supplier = lt.supplier;
		this.supplierCode = lt.supplierCode;
		this.sort = lt.sort.value();
		
		switch(this.sort){
			case LinkSort._LINKSORT_CABLELINK_THREAD:
				this.linkId = new Identifier(lt.link_id);
				this.color = new String(lt.color);
				this.mark = new String(lt.mark);
				break;
			default:
				break;
		}
		
		try {
			this.type = (LinkType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(lt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}		

	}
	
	protected Link(Identifier id,
				  Identifier creatorId,
				  Identifier domainId,
				  String name,
				  String description,
				  LinkType type,
				  String inventoryNo,
				  String supplier,
				  String supplierCode,
				  int sort,
				  Identifier linkId,
				  String color,
				  String mark) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.inventoryNo = inventoryNo;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
	
		this.sort = sort;
		this.linkId = linkId;
		this.color = color;
		this.mark = mark;
		
		this.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
	}
	
	/**
	 * create new instance for client
	 */ 

	public static Link createInstance(Identifier id,
																	 Identifier creatorId,
																	 Identifier domainId,
																	 String name,
																	 String description,
																	 LinkType type,
																	 String inventoryNo,
																	 String supplier,
																	 String supplierCode,
																	 LinkSort sort,
																	 Identifier linkId,
																	 String color,
																	 String mark){
		return new Link(id,
									 creatorId,
									 domainId,
									 name,
									 description,
									 type,
									 inventoryNo,
									 supplier,
									 supplierCode,
									 sort.value(),
									 linkId,
									 color,
									 mark);
	}
	
	public static Link getInstance(Link_Transferable lt) throws CreateObjectException{
		
		Link link = new Link(lt);
		
		link.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
		try {
			if (link.linkDatabase != null)
				link.linkDatabase.insert(link);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return link;
	}
	
	public Object getTransferable() {		
		return new Link_Transferable((Identifier_Transferable)super.id.getTransferable(),
									 super.created.getTime(),
									 super.modified.getTime(),
									 (Identifier_Transferable)super.creatorId.getTransferable(),
									 (Identifier_Transferable)super.modifierId.getTransferable(),
									 (Identifier_Transferable)super.domainId.getTransferable(),
									 new String(this.name),
									 new String(this.description),
									 (Identifier_Transferable)this.type.getId().getTransferable(),
									 LinkSort.from_int(this.sort),
									 this.inventoryNo,
									 this.supplier,
									 this.supplierCode,				
									 (this.linkId != null) ? (Identifier_Transferable)this.linkId.getTransferable() : (new Identifier_Transferable("")),
									 (this.color != null) ? this.color : "",
									 (this.mark != null) ? this.mark : "");
	}
	
	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												Identifier domainId,
												String name,
												String description,
												LinkType type,
												String inventoryNo,						
												String supplier,
												String supplierCode,
												int sort,
												Identifier linkId,
												String color,
												String mark) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					domainId);
			this.name = name;
			this.description = description;
			this.type = type;
			this.inventoryNo = inventoryNo;
			this.supplier = supplier;
			this.supplierCode = supplierCode;
			this.sort = sort;
			this.linkId = linkId;
			this.color = color;
			this.mark = mark;
		}

	public String getDescription() {
		return this.description;
	}
	
	public String getInventoryNo() {
		return this.inventoryNo;
	}	

	public String getName() {
		return this.name;
	}
	
	public String getSupplier() {
		return this.supplier;
	}
	
	public String getSupplierCode() {
		return this.supplierCode;
	}
	
	public StorableObjectType getType() {		
		return this.type;
	}
	
	public List getCharacteristics() {
		return this.characteristics;
	}	
	
	public LinkSort getSort(){
		return LinkSort.from_int(this.sort);
	}
	
	/**
	 * @return paretn link identifier
	 */
	public Identifier getLinkId(){
		return this.linkId;
	}
	
	public String getColor(){
		return this.color;		
	}
	
	public String getMark(){
		return this.mark;
	}
	
	public void setCharacteristics(List characteristics) {
		this.characteristics = characteristics;
	}
}
