/*
 * $Id: Modeling.java,v 1.63 2005/10/25 19:53:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlModeling;
import com.syrus.AMFICOM.measurement.corba.IdlModelingHelper;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;

/**
 * @version $Revision: 1.63 $, $Date: 2005/10/25 19:53:05 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class Modeling extends Action<Modeling> {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7265091663685378139L;

	private String name;
	private ParameterSet argumentSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Modeling(final IdlModeling mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Modeling(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final ModelingType type,
			final Identifier monitoredElementId,
			final String name,
			final ParameterSet argumentSet) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version,
			type,
			monitoredElementId,
			null);
		this.name = name;
		this.argumentSet = argumentSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlModeling mt = (IdlModeling) transferable;
		super.fromTransferable(mt, ModelingType.fromTransferable(mt.type), new Identifier(mt.monitoredElementId), null);

		this.argumentSet = (ParameterSet) StorableObjectPool.getStorableObject(new Identifier(mt.argumentSetId), true);

		this.name = mt.name;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlModeling getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return IdlModelingHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				(IdlModelingType) super.type.getTransferable(orb),
				super.monitoredElementId.getTransferable(),
				this.name,
				this.argumentSet.getId().getTransferable());
	}

	public short getEntityCode() {
		return ObjectEntities.MODELING_CODE;
	}

	public String getName() {
		return this.name;
	}

	public ParameterSet getArgumentSet() {
		return this.argumentSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final ModelingType type,
			final Identifier monitoredElementId,
			final String name,
			final ParameterSet argumentSet) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				type,
				monitoredElementId,
				null);
		this.name = name;
		this.argumentSet = argumentSet;
	}

	/**
	 * Create a new instance for client
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param name
	 * @param argumentSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Modeling createInstance(final Identifier creatorId,
			final ModelingType type,
			final Identifier monitoredElementId,
			final String name,
			final ParameterSet argumentSet) throws CreateObjectException{
		try {
			final Modeling modeling = new Modeling(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					type,
					monitoredElementId,
					name,
					argumentSet);

			assert modeling.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			modeling.markAsChanged();

			return modeling;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public Result createResult(final Identifier resultCreatorId, final Parameter[] resultParameters)
			throws CreateObjectException {
		return Result.createInstance(resultCreatorId, this, ResultSort.RESULT_SORT_MODELING, resultParameters);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		final Set<Identifiable> dependencies =  new HashSet<Identifiable>();
		dependencies.add(this.argumentSet);
		return dependencies;
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.measurement.Action#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.argumentSet != null;
	}

	/**
	 * @param argumentSet The argumentSet to set.
	 */
	public void setArgumentSet(final ParameterSet argumentSet) {
		this.argumentSet = argumentSet;
		super.markAsChanged();
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ModelingWrapper getWrapper() {
		return ModelingWrapper.getInstance();
	}
}
