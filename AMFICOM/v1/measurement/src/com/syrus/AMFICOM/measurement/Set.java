/*
 * $Id: Set.java,v 1.49 2005/03/01 07:34:12 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.49 $, $Date: 2005/03/01 07:34:12 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class Set extends StorableObject {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3977303222014457399L;
	/**
	 * @deprecated
	 */
	protected static final int UPDATE_ATTACH_ME = 1;
	/**
	 * @deprecated
	 */
	protected static final int UPDATE_DETACH_ME = 2;

	private int sort;
	private String description;
	private SetParameter[] parameters;
	private Collection monitoredElementIds;

	private StorableObjectDatabase setDatabase;
	
	protected static final String ID_MONITORED_ELEMENTS_IDS = "monitoredElementId"+KEY_VALUE_SEPERATOR;
	protected static final String ID_SORT = "sort"+KEY_VALUE_SEPERATOR;
	protected static final String ID_PARAMETERS = "parameter"+KEY_VALUE_SEPERATOR;

	public Set(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredElementIds = new HashSet();
		
		this.setDatabase = MeasurementDatabaseContext.setDatabase;
		try {
			this.setDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Set(Set_Transferable st) throws CreateObjectException {
		super(st.header);
		this.sort = st.sort.value();
		this.description = new String(st.description);

		try {
			this.parameters = new SetParameter[st.parameters.length];
			for (int i = 0; i < this.parameters.length; i++)
				this.parameters[i] = new SetParameter(st.parameters[i]);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.monitoredElementIds = new HashSet(st.monitored_element_ids.length);
		for (int i = 0; i < st.monitored_element_ids.length; i++)
			this.monitoredElementIds.add(new Identifier(st.monitored_element_ids[i]));
		
	}	
	
	protected Set(Identifier id,
				  Identifier creatorId,
				  long version,
				  int sort,
				  String description,
				  SetParameter[] parameters,
				  Collection monitoredElementIds) {
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
		
		this.setDatabase = MeasurementDatabaseContext.setDatabase;
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
									 Collection monitoredElementIds) throws CreateObjectException {
		if (creatorId == null || sort == null || description == null || parameters == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			Set set =  new Set(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SET_ENTITY_CODE),
				creatorId,
				0L,
				sort.value(),
				description,
				parameters,
				monitoredElementIds);
			set.changed = true;
			return set;
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Set.createInstance | cannot generate identifier ", e);
		}
	}

	public boolean isAttachedToMonitoredElement(Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(Identifier monitoredElementId,
																			 Identifier modifierId1) throws UpdateObjectException {
		if (this.isAttachedToMonitoredElement(monitoredElementId))
      return;
		super.modifierId = modifierId1;
		this.monitoredElementIds.add(monitoredElementId);
		try {
			this.setDatabase.update(this, modifierId1, StorableObjectDatabase.UPDATE_FORCE);
		}
		catch (VersionCollisionException vce){
			throw new UpdateObjectException(vce.getMessage(), vce);
		}
		this.monitoredElementIds.add(monitoredElementId);
	}

	public void detachFromMonitoredElement(Identifier monitoredElementId,
																				 Identifier modifierId1) throws UpdateObjectException {
    if (!this.isAttachedToMonitoredElement(monitoredElementId))
      return;
		super.modifierId = modifierId1;
		this.monitoredElementIds.remove(monitoredElementId);
		try {
	    this.setDatabase.update(this, modifierId1, StorableObjectDatabase.UPDATE_FORCE);
		}
		catch (VersionCollisionException vce) {
			throw new UpdateObjectException(vce.getMessage(), vce);
		}
		this.monitoredElementIds.remove(monitoredElementId);
  }

	public Object getTransferable() {
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable) this.parameters[i].getTransferable();

		Identifier_Transferable[] meIds = new Identifier_Transferable[this.monitoredElementIds.size()];
		int i = 0;
		for (Iterator iterator = this.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new Set_Transferable(super.getHeaderTransferable(),
				SetSort.from_int(this.sort),
				new String(this.description),
				pts,
				meIds);
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

	public Collection getMonitoredElementIds() {
		return Collections.unmodifiableCollection(this.monitoredElementIds);
	}

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

	protected synchronized void setParameters0(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	public void setParameters(SetParameter[] parameters) {
		this.setParameters0(parameters);
		super.changed = true;
	}

	protected synchronized void setMonitoredElementIds0(Collection monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
	     	this.monitoredElementIds.addAll(monitoredElementIds);
	}
	
	protected synchronized void setMonitoredElementIds(Collection monitoredElementIds) {
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
	
	public List getDependencies() {		
		List dependencies = new LinkedList();

		if (this.monitoredElementIds != null)
			dependencies.addAll(this.monitoredElementIds);

		for (int i = 0; i < this.parameters.length; i++)
			dependencies.add(this.parameters[i].getType());

		return dependencies;
	}
	
}
