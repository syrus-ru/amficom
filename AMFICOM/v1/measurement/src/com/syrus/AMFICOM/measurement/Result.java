/*
 * $Id: Result.java,v 1.92 2006/06/06 11:31:16 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlParameter;
import com.syrus.AMFICOM.measurement.corba.IdlResult;
import com.syrus.AMFICOM.measurement.corba.IdlResultHelper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.92 $, $Date: 2006/06/06 11:31:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Result extends StorableObject
		implements IdlTransferableObjectExt<IdlResult> {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256999964965286967L;
	private Identifier actionId;
	private int sort;
	private Parameter[] parameters;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Result(final IdlResult rt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(rt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}	

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Result(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier actionId,
			final int sort,
			final Parameter[] parameters) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.actionId = actionId;
		this.sort = sort;
		this.parameters = parameters;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void fromIdlTransferable(final IdlResult rt)
	throws IdlConversionException {
		super.fromIdlTransferable(rt);

		this.sort = rt.sort.value();
		switch (this.sort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				this.actionId = Identifier.valueOf(rt.measurementId);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				this.actionId = Identifier.valueOf(rt.analysisId);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				this.actionId = Identifier.valueOf(rt.modelingId);
				break;
			default:
				Log.errorMessage("Illegal sort: " + this.sort + " of result '" + super.id + "'");
		}

		this.parameters = new Parameter[rt.parameters.length];
		for (int i = 0; i < this.parameters.length; i++) {
			this.parameters[i] = new Parameter(rt.parameters[i]);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlResult getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final IdlParameter[] pts = new IdlParameter[this.parameters.length];
		for (int i = 0; i < pts.length; i++) {
			pts[i] = this.parameters[i].getIdlTransferable(orb);
		}

		final IdlIdentifier voidIdlIdentifier = Identifier.VOID_IDENTIFIER.getIdlTransferable();
		final IdlIdentifier nonVoidIdlIdentifier = this.actionId.getIdlTransferable();
		return IdlResultHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
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
		boolean valid = super.isValid() && this.actionId != null && this.parameters != null;
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

	public Identifier getActionId() {
		return this.actionId;
	}

	/**
	 * A wrapper around {@link #getActionId()}.
	 */
	public Action getAction() {
		try {
			return StorableObjectPool.getStorableObject(this.actionId, true);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return null;
		}
	}

	public void setActionId(final Identifier actionId) {
		this.actionId = actionId;
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
			final Identifier actionId,
			final int sort) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.actionId = actionId;
		this.sort = sort;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected static Result createInstance(final Identifier creatorId,
			final Identifier actionId,
			final ResultSort sort,
			final Parameter[] parameters) throws CreateObjectException {
		try {
			final Result result = new Result(IdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_CODE),
				creatorId,
				StorableObjectVersion.INITIAL_VERSION,
				actionId,
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
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();

		dependencies.add(this.actionId);

		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ResultWrapper getWrapper() {
		return ResultWrapper.getInstance();
	}
}
