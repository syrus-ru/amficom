/*
 * $Id: Link.java,v 1.15 2004/12/09 12:01:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;


/**
 * @version $Revision: 1.15 $, $Date: 2004/12/09 12:01:41 $
 * @author $Author: bob $
 * @module config_v1
 */
public class Link extends DomainMember implements Characterized, TypedObject {
	static final long serialVersionUID = -4235048398372768515L;

	private String name;
	private String description;		
	private AbstractLinkType type;		
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

		this.characteristics = new LinkedList();
		this.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
		try {
			this.linkDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Link(Link_Transferable lt) throws CreateObjectException  {
		super(lt.header,
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
            this.characteristics = new ArrayList(lt.characteristic_ids.length);
            for (int i = 0; i < lt.characteristic_ids.length; i++)
                this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(lt.characteristic_ids[i]), true));
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae);
        }
		
		try {
			this.type = (AbstractLinkType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(lt.type_id), true);
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
				  AbstractLinkType type,
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
		this.characteristics = new LinkedList();
		this.linkDatabase = ConfigurationDatabaseContext.linkDatabase;
	}
	
	/**
	 * create new instance for client
	 */ 

	public static Link createInstance(Identifier creatorId,
									  Identifier domainId,
									  String name,
									  String description,
									  AbstractLinkType type,
									  String inventoryNo,
									  String supplier,
									  String supplierCode,
									  LinkSort sort,
									  Identifier linkId,
									  String color,
									  String mark){
		if (creatorId == null || domainId == null || name == null || description == null || 
				type == null || inventoryNo == null || supplier == null || supplierCode == null ||
				sort == null || linkId == null || color == null || mark == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return new Link(IdentifierPool.getGeneratedIdentifier(ObjectEntities.LINK_ENTITY_CODE),
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
		int i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        
        return new Link_Transferable(super.getHeaderTransferable(),
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
									 (this.mark != null) ? this.mark : "",
                                     charIds);
	}
	
	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												Identifier domainId,
												String name,
												String description,
												AbstractLinkType type,
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
	
	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
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
	 * 
	 * @return paretn link identifier if sort is _LINKSORT_CABLELINK_THREAD, throws {@link UnsupportedOperationException} otherwise 
	 */
	public Identifier getLinkId(){
		if (this.sort == LinkSort._LINKSORT_CABLELINK_THREAD)
			return this.linkId;
		throw new UnsupportedOperationException("LinkSort isn't LINKSORT_CABLELINK_THREAD");
	}
	
	public String getColor(){
		return this.color;		
	}
	
	public String getMark(){
		return this.mark;
	}
	
	public void setCharacteristics(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
        super.currentVersion = super.getNextVersion();
    }
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		if (this.linkId != null)
			dependencies.add(this.linkId);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}
}
