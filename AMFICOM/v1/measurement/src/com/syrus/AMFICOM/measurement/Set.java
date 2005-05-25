/*
 * $Id: Set.java,v 1.69 2005/05/25 13:01:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.69 $, $Date: 2005/05/25 13:01:05 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class Set extends StorableObject {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3977303222014457399L;

	private int sort;
	private String description;
	private SetParameter[] parameters;

	private java.util.Set monitoredElementIds;

	protected static final String ID_MONITORED_ELEMENTS_IDS = "monitoredElementId"+KEY_VALUE_SEPERATOR;
	protected static final String ID_SORT = "sort"+KEY_VALUE_SEPERATOR;
	protected static final String ID_PARAMETERS = "parameter"+KEY_VALUE_SEPERATOR;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Set(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredElementIds = new HashSet();
		
		SetDatabase database = (SetDatabase) DatabaseContext.getDatabase(ObjectEntities.SET_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set(Set_Transferable st) throws CreateObjectException {
		try {
			this.fromTransferable(st);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Set(Identifier id,
				  Identifier creatorId,
				  long version,
				  int sort,
				  String description,
				  SetParameter[] parameters,
				  java.util.Set monitoredElementIds) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.sort = sort;
		this.description = description;
		this.parameters = parameters;

		this.monitoredElementIds = new HashSet();
		this.setMonitoredElementIds0(monitoredElementIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param sort
	 * @param description
	 * @param parameters
	 * @param monitoredElementIds
	 * @throws CreateObjectException
	 */
	public static Set createInstance(Identifier creatorId,
			SetSort sort,
			String description,
			SetParameter[] parameters,
			java.util.Set monitoredElementIds) throws CreateObjectException {

		try {
			Set set = new Set(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SET_ENTITY_CODE),
					creatorId,
					0L,
					sort.value(),
					description,
					parameters,
					monitoredElementIds);

			assert set.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			set.changed = true;
			return set;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Set_Transferable st = (Set_Transferable)transferable;
		super.fromTransferable(st.header);
		this.sort = st.sort.value();
		this.description = st.description;

		this.parameters = new SetParameter[st.parameters.length];
		for (int i = 0; i < this.parameters.length; i++) {
			this.parameters[i] = new SetParameter(st.parameters[i]);
		}

		this.monitoredElementIds = Identifier.fromTransferables(st.monitored_element_ids);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable) this.parameters[i].getTransferable();

		Identifier_Transferable[] meIds = Identifier.createTransferables(this.monitoredElementIds);
		return new Set_Transferable(super.getHeaderTransferable(),
				SetSort.from_int(this.sort),
				this.description,
				pts,
				meIds);
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		boolean valid = super.isValid() && this.description != null && this.parameters != null && this.monitoredElementIds != null && !this.monitoredElementIds.isEmpty();
		if (!valid)
			return valid;
		
		for(int i=0;i<this.parameters.length;i++) {
			valid &= this.parameters[i] != null && this.parameters[i].isValid();
			if (!valid)
				break;
		}		
		return valid;
	}

	public short getEntityCode() {
		return ObjectEntities.SET_ENTITY_CODE;
	}

	public SetSort getSort() {
		return SetSort.from_int(this.sort);
	}

	public String getDescription() {
		return this.description;
	}

	public SetParameter[] getParameters() {
		return this.parameters;
	}

	public java.util.Set getMonitoredElementIds() {
		return Collections.unmodifiableSet(this.monitoredElementIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  int sort,
											  String description) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.sort = sort;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setParameters0(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	public void setParameters(SetParameter[] parameters) {
		this.setParameters0(parameters);
		super.changed = true;
	}

	public boolean isAttachedToMonitoredElement(Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(Identifier monitoredElementId) {
		if (monitoredElementId != null && !this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.add(monitoredElementId);
			super.changed = true;
		}
	}

	public void detachFromMonitoredElement(Identifier monitoredElementId) {
		if (monitoredElementId != null && this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.remove(monitoredElementId);
			super.changed = true;
		}
	}

	protected synchronized void setMonitoredElementIds0(java.util.Set monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
	     	this.monitoredElementIds.addAll(monitoredElementIds);
	}
	
	protected synchronized void setMonitoredElementIds(java.util.Set monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
	}
	
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		super.changed = true;
		this.description = description;
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(SetSort sort) {
		super.changed = true;
		this.sort = sort.value();
	}
	
	public boolean equals(Object obj) {
		boolean equals = (obj==this);
		if ((!equals)&&(obj instanceof Set)){
			Set set = (Set)obj;
			if ((this.id.equals(set.id))&&
				 HashCodeGenerator.equalsDate(this.created,set.created) &&
				 (this.creatorId.equals(set.creatorId))&&
				 HashCodeGenerator.equalsDate(this.modified,set.modified) &&
				 (this.modifierId.equals(set.modifierId))&&
				 (this.monitoredElementIds.equals(set.monitoredElementIds))&&
				 (this.description.equals(set.description))&&
				 (this.sort==set.sort)&&
				 (HashCodeGenerator.equalsArray(this.parameters,set.parameters)))
				 equals = true;
		}
		return equals;
	}
	
	
	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.created);
		hashCodeGenerator.addObject(this.creatorId);
		hashCodeGenerator.addObject(this.modified);
		hashCodeGenerator.addObject(this.modifierId);
		hashCodeGenerator.addObject(this.monitoredElementIds);
		hashCodeGenerator.addObject(this.description);
		hashCodeGenerator.addInt(this.sort);
		hashCodeGenerator.addObjectArray(this.parameters);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;

	}
	
	
	public String toString() {
		StringBuffer buffer = new StringBuffer(getClass().getName());
		buffer.append(EOSL);
		buffer.append(ID+this.id+EOSL
					 + ID_CREATED + this.created.toString()+EOSL	
					 + ID_CREATOR_ID + this.creatorId.toString()+EOSL
					 + ID_MODIFIED + this.modified.toString()+EOSL		
					 + ID_MODIFIER_ID + this.modifierId.toString()+EOSL);
		if (this.monitoredElementIds==null){
			buffer.append(ID_MONITORED_ELEMENTS_IDS);
			buffer.append(NULL);
			buffer.append(EOSL);
		}else{
			for(Iterator it=this.monitoredElementIds.iterator();it.hasNext();){
				Identifier id1 = (Identifier)it.next();
				buffer.append(ID_MONITORED_ELEMENTS_IDS);
				buffer.append(id1.toString());
				buffer.append(EOSL);
			}
		}
		buffer.append(ID_SORT);
		buffer.append(this.sort);
		buffer.append(EOSL);
		if (this.parameters==null){
			buffer.append(ID_PARAMETERS);
			buffer.append(NULL);
			buffer.append(EOSL);
		}else{
			for(int i=0;i<this.parameters.length;i++){
				SetParameter param = this.parameters[i];
				buffer.append(ID_PARAMETERS);
				buffer.append(OPEN_BLOCK);
				buffer.append(param.toString());
				buffer.append(CLOSE_BLOCK);			
			}
		}
					 			
		return buffer.toString();
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();

		if (this.monitoredElementIds != null)
			dependencies.addAll(this.monitoredElementIds);

		for (int i = 0; i < this.parameters.length; i++)
			dependencies.add(this.parameters[i].getType());

		return dependencies;
	}
	
}
