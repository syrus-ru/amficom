/*
 * $Id: Result.java,v 1.78 2005/09/08 18:26:30 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlParameter;
import com.syrus.AMFICOM.measurement.corba.IdlResult;
import com.syrus.AMFICOM.measurement.corba.IdlResultHelper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.78 $, $Date: 2005/09/08 18:26:30 $
 * @author $Author: bass $
 * @module measurement
 */

public final class Result extends StorableObject {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256999964965286967L;
	private Action action;
	private int sort;
	private Parameter[] parameters;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Result(final IdlResult rt) throws CreateObjectException {
		try {
			this.fromTransferable(rt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Result(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Action action,
			final int sort,
			final Parameter[] parameters) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.action = action;
		this.sort = sort;
		this.parameters = parameters;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlResult rt = (IdlResult) transferable;
		super.fromTransferable(rt);

		this.sort = rt.sort.value();
		Identifier actionId = null;
		switch (this.sort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				actionId = new Identifier(rt.measurementId);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				actionId = new Identifier(rt.analysisId);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				actionId = new Identifier(rt.modelingId);
				break;
			default:
				Log.errorMessage("Result.init | Illegal sort: " + this.sort + " of result '" + super.id + "'");
		}
		this.action = (Action) StorableObjectPool.getStorableObject(actionId, true);

		this.parameters = new Parameter[rt.parameters.length];
		for (int i = 0; i < this.parameters.length; i++)
			this.parameters[i] = new Parameter(rt.parameters[i]);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlResult getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final IdlParameter[] pts = new IdlParameter[this.parameters.length];
		for (int i = 0; i < pts.length; i++) {
			pts[i] = this.parameters[i].getTransferable(orb);
		}

		final IdlIdentifier voidIdlIdentifier = Identifier.VOID_IDENTIFIER.getTransferable();
		final IdlIdentifier nonVoidIdlIdentifier = this.action.getId().getTransferable();
		return IdlResultHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				(this.sort == ResultSort._RESULT_SORT_MEASUREMENT)
						? nonVoidIdlIdentifier
						: voidIdlIdentifier,
				(this.sort == ResultSort._RESULT_SORT_ANALYSIS)
						? nonVoidIdlIdentifier
						: voidIdlIdentifier,
				(this.sort == ResultSort._RESULT_SORT_MODELING)
						? nonVoidIdlIdentifier
						: voidIdlIdentifier,
				ResultSort.from_int(this.sort),
				pts);
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		boolean valid = super.isValid() && this.action != null && this.parameters != null;
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
		return ObjectEntities.RESULT_CODE;
	}

	public Action getAction() {
		return this.action;
	}
	
	public void setAction(final Action action) {
		this.action = action;
		super.markAsChanged();
	}

	public ResultSort getSort() {
		return ResultSort.from_int(this.sort);
	}
	
	public void setSort(final ResultSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}

	public Parameter[] getParameters() {
		return this.parameters;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Action action,
			final int sort) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.action = action;
		this.sort = sort;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected static Result createInstance(final Identifier creatorId,
			final Action action,
			final ResultSort sort,
			final Parameter[] parameters) throws CreateObjectException {
		try {
			final Result result = new Result(IdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_CODE),
				creatorId,
				StorableObjectVersion.createInitial(),
				action,
				sort.value(),
				parameters);

			assert result.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			result.markAsChanged();

			return result;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
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

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();

		dependencies.add(this.action);

		return dependencies;
	}
}
