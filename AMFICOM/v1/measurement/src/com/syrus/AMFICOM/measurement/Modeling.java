/*
 * $Id: Modeling.java,v 1.72 2006/06/06 11:31:16 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

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
import com.syrus.AMFICOM.measurement.corba.IdlModeling;
import com.syrus.AMFICOM.measurement.corba.IdlModelingHelper;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.72 $, $Date: 2006/06/06 11:31:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class Modeling extends Action
		implements IdlTransferableObjectExt<IdlModeling> {
	
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
			this.fromIdlTransferable(mt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
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
			VOID_IDENTIFIER);
		this.name = name;
		this.argumentSet = argumentSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void fromIdlTransferable(final IdlModeling mt)
	throws IdlConversionException {
		try {
			super.fromIdlTransferable(mt, ModelingType.fromTransferable(mt.type), Identifier.valueOf(mt.monitoredElementId), VOID_IDENTIFIER);
	
			this.argumentSet = (ParameterSet) StorableObjectPool.getStorableObject(Identifier.valueOf(mt.argumentSetId), true);
	
			this.name = mt.name;
			
			assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		} catch (final ApplicationException ae) {
			throw new IdlConversionException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlModeling getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return IdlModelingHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				(IdlModelingType) super.type.getIdlTransferable(orb),
				super.monitoredElementId.getIdlTransferable(),
				this.name,
				this.argumentSet.getId().getIdlTransferable());
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
				VOID_IDENTIFIER);
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
					StorableObjectVersion.INITIAL_VERSION,
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
		return Result.createInstance(resultCreatorId, this.id, ResultSort.RESULT_SORT_MODELING, resultParameters);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
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
