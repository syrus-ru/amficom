/*
 * $Id: Modeling.java,v 1.68.2.2 2006/02/13 19:37:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlModeling;
import com.syrus.AMFICOM.measurement.corba.IdlModelingHelper;

/**
 * @version $Revision: 1.68.2.2 $, $Date: 2006/02/13 19:37:59 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class Modeling extends Action<Modeling> {

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
			final Modeling modeling = new Modeling(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					typeId,
					monitoredElementId,
					actionTemplateId,
					name,
					startTime,
					duration,
					ActionStatus.ACTION_STATUS_NEW);

			assert modeling.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

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
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
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
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlModeling idlModeling = (IdlModeling) transferable;
		super.fromTransferable(idlModeling);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.getTypeId().getMajor() == ObjectEntities.MODELING_TYPE_CODE;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ModelingWrapper getWrapper() {
		return ModelingWrapper.getInstance();
	}
}
