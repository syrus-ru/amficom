/*
 * $Id: ParameterSet.java,v 1.22 2005/10/25 19:53:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlParameter;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSet;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetHelper;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;

/**
 * @version $Revision: 1.22 $, $Date: 2005/10/25 19:53:05 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class ParameterSet extends StorableObject<ParameterSet> {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3977303222014457399L;

	private int sort;
	private String description;
	private Parameter[] parameters;

	private Set<Identifier> monitoredElementIds;

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
			final StorableObjectVersion version,
			final int sort,
			final String description,
			final Parameter[] parameters,
			final Set<Identifier> monitoredElementIds) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.sort = sort;
		this.description = description;
		this.parameters = parameters;

		this.monitoredElementIds = new HashSet<Identifier>();
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
			final Set<Identifier> monitoredElementIds) throws CreateObjectException {

		try {
			final ParameterSet set = new ParameterSet(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETERSET_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
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
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlParameterSet st = (IdlParameterSet)transferable;
		super.fromTransferable(st);
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
	@Override
	public IdlParameterSet getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final IdlParameter[] pts = new IdlParameter[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = this.parameters[i].getTransferable(orb);

		final IdlIdentifier[] meIds = Identifier.createTransferables(this.monitoredElementIds);
		return IdlParameterSetHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
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
	@Override
	protected boolean isValid() {
		boolean valid = super.isValid()
				&& this.description != null
				&& this.parameters != null
				&& this.monitoredElementIds != null
				&& !this.monitoredElementIds.isEmpty();
		if (!valid) {
			return valid;
		}

		for (int i = 0; i < this.parameters.length; i++) {
			valid &= this.parameters[i] != null && this.parameters[i].isValid();
			if (!valid) {
				break;
			}
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

	public Set<Identifier> getMonitoredElementIds() {
		return Collections.unmodifiableSet(this.monitoredElementIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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

	protected synchronized void setMonitoredElementIds0(final Set<Identifier> monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
	     	this.monitoredElementIds.addAll(monitoredElementIds);
	}

	protected synchronized void setMonitoredElementIds(final Set<Identifier> monitoredElementIds) {
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

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();

		if (this.monitoredElementIds != null) {
			dependencies.addAll(this.monitoredElementIds);
		}

		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ParameterSetWrapper getWrapper() {
		return ParameterSetWrapper.getInstance();
	}
}
