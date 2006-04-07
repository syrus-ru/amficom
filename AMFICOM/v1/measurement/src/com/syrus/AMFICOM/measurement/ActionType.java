/*-
 * $Id: ActionType.java,v 1.26.2.7 2006/04/07 10:55:23 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlActionType;

/**
 * @version $Revision: 1.26.2.7 $, $Date: 2006/04/07 10:55:23 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionType extends StorableObjectType implements Describable {

	ActionType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
	}

	ActionType() {
		//Empty
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 * 
	 * @param idlActionType
	 */
	final void fromIdlTransferable(final IdlActionType idlActionType) {
		super.fromIdlTransferable(idlActionType, idlActionType.codename, idlActionType.description);
	}

	public final String getName() {
		return super.getDescription();
	}

	public final void setName(final String name) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	/**
	 * Найти все КИС, на которых можно проводить действие данного типа.
	 * 
	 * @return Список подходящих КИС.
	 * @throws ApplicationException
	 */
	public final Set<KIS> getProperKISs() throws ApplicationException {
		final StorableObjectCondition bindingCondition = new LinkedIdsCondition(super.id, ACTIONPARAMETERTYPEBINDING_CODE);
		final Set<ActionParameterTypeBinding> bindings = StorableObjectPool.getStorableObjectsByCondition(bindingCondition, true);
		final Set<MeasurementPortType> measurementPortTypes = new HashSet<MeasurementPortType>();
		for (final ActionParameterTypeBinding binding : bindings) {
			measurementPortTypes.add(binding.getMeasurementPortType());
		}

		final StorableObjectCondition measurementPortCondition = new LinkedIdsCondition(measurementPortTypes, MEASUREMENTPORT_CODE);
		final Set<MeasurementPort> measurementPorts = StorableObjectPool.getStorableObjectsByCondition(measurementPortCondition, true);

		final StorableObjectCondition kisCondition = new LinkedIdsCondition(measurementPorts, KIS_CODE);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(kisCondition, true);
		return kiss;
	}

	/**
	 * If add additional fields to class,
	 * remove Override annotation.
	 */
	@Override
	protected synchronized final void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}
}
