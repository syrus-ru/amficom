/*
 * $Id: ParameterSet.java,v 1.6 2005/06/23 18:45:08 bass Exp $
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
import java.util.Set;

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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlParameter;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSet;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/23 18:45:08 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class ParameterSet extends StorableObject {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3977303222014457399L;

	private int sort;
	private String description;
	private Parameter[] parameters;

	private Set<Identifier> monitoredElementIds;

	protected static final String ID_MONITORED_ELEMENTS_IDS = "monitoredElementId"+KEY_VALUE_SEPERATOR;
	protected static final String ID_SORT = "sort"+KEY_VALUE_SEPERATOR;
	protected static final String ID_PARAMETERS = "parameter"+KEY_VALUE_SEPERATOR;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ParameterSet(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredElementIds = new HashSet();
		
		ParameterSetDatabase database = (ParameterSetDatabase) DatabaseContext.getDatabase(ObjectEntities.PARAMETERSET_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ParameterSet(final IdlParameterSet st) throws CreateObjectException {
		try {
			this.fromTransferable(st);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ParameterSet(final Identifier id,
			final Identifier creatorId,
			final long version,
			final int sort,
			final String description,
			final Parameter[] parameters,
			final java.util.Set monitoredElementIds) {
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
	public static ParameterSet createInstance(final Identifier creatorId,
			final ParameterSetSort sort,
			final String description,
			final Parameter[] parameters,
			final java.util.Set monitoredElementIds) throws CreateObjectException {

		try {
			ParameterSet set = new ParameterSet(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETERSET_CODE),
					creatorId,
					0L,
					sort.value(),
					description,
					parameters,
					monitoredElementIds);

			assert set.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			set.markAsChanged();

			return set;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlParameterSet st = (IdlParameterSet)transferable;
		super.fromTransferable(st.header);
		this.sort = st.sort.value();
		this.description = st.description;

		this.parameters = new Parameter[st.parameters.length];
		for (int i = 0; i < this.parameters.length; i++) {
			this.parameters[i] = new Parameter(st.parameters[i]);
		}

		this.monitoredElementIds = Identifier.fromTransferables(st.monitoredElementIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IdlParameterSet getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		IdlParameter[] pts = new IdlParameter[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = this.parameters[i].getTransferable();

		IdlIdentifier[] meIds = Identifier.createTransferables(this.monitoredElementIds);
		return new IdlParameterSet(super.getHeaderTransferable(),
				ParameterSetSort.from_int(this.sort),
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
		boolean valid = super.isValid()
				&& this.description != null
				&& this.parameters != null
				&& this.monitoredElementIds != null
				&& !this.monitoredElementIds.isEmpty();
		if (!valid)
			return valid;

		for (int i = 0; i < this.parameters.length; i++) {
			valid &= this.parameters[i] != null && this.parameters[i].isValid();
			if (!valid)
				break;
		}
		return valid;
	}

	public short getEntityCode() {
		return ObjectEntities.PARAMETERSET_CODE;
	}

	public ParameterSetSort getSort() {
		return ParameterSetSort.from_int(this.sort);
	}

	public String getDescription() {
		return this.description;
	}

	public Parameter[] getParameters() {
		return this.parameters;
	}

	public java.util.Set getMonitoredElementIds() {
		return Collections.unmodifiableSet(this.monitoredElementIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final int sort,
			final String description) {
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
	protected synchronized void setParameters0(final Parameter[] parameters) {
		this.parameters = parameters;
	}

	public void setParameters(final Parameter[] parameters) {
		this.setParameters0(parameters);
		super.markAsChanged();
	}

	public boolean isAttachedToMonitoredElement(final Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(final Identifier monitoredElementId) {
		if (monitoredElementId != null && !this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.add(monitoredElementId);
			super.markAsChanged();
		}
	}

	public void detachFromMonitoredElement(final Identifier monitoredElementId) {
		if (monitoredElementId != null && this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.remove(monitoredElementId);
			super.markAsChanged();
		}
	}

	protected synchronized void setMonitoredElementIds0(final java.util.Set monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
	     	this.monitoredElementIds.addAll(monitoredElementIds);
	}

	protected synchronized void setMonitoredElementIds(final java.util.Set monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @param sort The sort to set.
	 */
	public void setSort(final ParameterSetSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}

	public boolean equals(Object obj) {
		boolean equals = (obj==this);
		if ((!equals)&&(obj instanceof ParameterSet)){
			ParameterSet set = (ParameterSet)obj;
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
		buffer.append(ID + this.id + EOSL
				+ ID_CREATED + this.created.toString() + EOSL
				+ ID_CREATOR_ID + this.creatorId.toString() + EOSL
				+ ID_MODIFIED + this.modified.toString() + EOSL
				+ ID_MODIFIER_ID + this.modifierId.toString() + EOSL);
		if (this.monitoredElementIds == null) {
			buffer.append(ID_MONITORED_ELEMENTS_IDS);
			buffer.append(NULL);
			buffer.append(EOSL);
		} else {
			for (Iterator it = this.monitoredElementIds.iterator(); it.hasNext();) {
				Identifier id1 = (Identifier) it.next();
				buffer.append(ID_MONITORED_ELEMENTS_IDS);
				buffer.append(id1.toString());
				buffer.append(EOSL);
			}
		}
		buffer.append(ID_SORT);
		buffer.append(this.sort);
		buffer.append(EOSL);
		if (this.parameters == null) {
			buffer.append(ID_PARAMETERS);
			buffer.append(NULL);
			buffer.append(EOSL);
		} else {
			for (int i = 0; i < this.parameters.length; i++) {
				Parameter param = this.parameters[i];
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

		final java.util.Set dependencies = new HashSet();

		if (this.monitoredElementIds != null)
			dependencies.addAll(this.monitoredElementIds);

		for (int i = 0; i < this.parameters.length; i++)
			dependencies.add(this.parameters[i].getType());

		return dependencies;
	}

}
