/*
 * $Id: Modeling.java,v 1.68.2.7 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELINGRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlModeling;
import com.syrus.AMFICOM.measurement.corba.IdlModelingHelper;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @version $Revision: 1.68.2.7 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class Modeling extends Action<ModelingResultParameter> {
	private static final long serialVersionUID = 622710280466286589L;

	Modeling(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status) {
		super(id,
				creatorId,
				version,
				typeId,
				monitoredElementId,
				actionTemplateId,
				name,
				startTime,
				duration,
				status);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Modeling(final IdlModeling idlModeling) throws CreateObjectException {
		super(idlModeling);
	}

	public static Modeling createInstance(final Identifier creatorId,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration) throws CreateObjectException{
		try {
			final Modeling modeling = new Modeling(IdentifierPool.getGeneratedIdentifier(MODELING_CODE),
					creatorId,
					INITIAL_VERSION,
					typeId,
					monitoredElementId,
					actionTemplateId,
					name,
					startTime,
					duration,
					ActionStatus.ACTION_STATUS_NEW);

			assert modeling.isValid() : OBJECT_STATE_ILLEGAL;

			modeling.markAsChanged();

			return modeling;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlModeling getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
		
		return IdlModelingHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getTypeId().getIdlTransferable(orb),
				super.getMonitoredElementId().getIdlTransferable(orb),
				super.getActionTemplateId().getIdlTransferable(orb),
				super.getName(),
				super.getStartTime().getTime(),
				super.getDuration(),
				super.getStatus().getIdlTransferable());
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromIdlTransferable(final IdlStorableObject transferable) throws IdlConversionException {
		final IdlModeling idlModeling = (IdlModeling) transferable;
		super.fromIdlTransferable(idlModeling);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	@Override
	void ensureActionResultParametersConditionIsCreated() {
		if (super.actionResultParametersCondition == null) {
			super.actionResultParametersCondition = new LinkedIdsCondition(this.id, MODELINGRESULTPARAMETER_CODE);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.getTypeId().getMajor() == MODELING_TYPE_CODE;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ModelingWrapper getWrapper() {
		return ModelingWrapper.getInstance();
	}

	@Override
	public ModelingResultParameter createActionResultParameter(final Identifier resultParameterCreatorId,
			final byte[] resultParameterValue,
			final Identifier resultParameterTypeId) throws CreateObjectException {
		return ModelingResultParameter.createInstance(resultParameterCreatorId,
				resultParameterValue,
				resultParameterTypeId,
				this.id);
	}
}
