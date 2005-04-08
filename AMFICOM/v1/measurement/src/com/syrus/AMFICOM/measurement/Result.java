/*
 * $Id: Result.java,v 1.49 2005/04/08 12:33:24 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;

import org.omg.CORBA.portable.IDLEntity;

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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.49 $, $Date: 2005/04/08 12:33:24 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class Result extends StorableObject {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256999964965286967L;
	private Action action;
	private int sort;
	private SetParameter[] parameters;

	public Result(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		ResultDatabase database = MeasurementDatabaseContext.getResultDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Result(Result_Transferable rt) throws CreateObjectException {
		try {
			this.fromTransferable(rt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	

	protected Result(Identifier id,
					 Identifier creatorId,
					 long version,
					 Action action,
					 int sort,
					 SetParameter[] parameters) {
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
	
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Result_Transferable rt = (Result_Transferable) transferable;
		super.fromTransferable(rt.header);

		this.sort = rt.sort.value();
		switch (this.sort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				this.action = (Measurement) MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.measurement_id), true);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				this.action = (Analysis) MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.analysis_id), true);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				this.action = (Evaluation) MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.evaluation_id), true);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				this.action = (Modeling) MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.evaluation_id), true);
				break;
			default:
				Log.errorMessage("Result.init | Illegal sort: " + this.sort + " of result '" + super.id.toString() + "'");
		}

		this.parameters = new SetParameter[rt.parameters.length];
		for (int i = 0; i < this.parameters.length; i++)
			this.parameters[i] = new SetParameter(rt.parameters[i]);
	}
	
	public IDLEntity getTransferable() {
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable) this.parameters[i].getTransferable();
		return new Result_Transferable(super.getHeaderTransferable(),
				(this.sort == ResultSort._RESULT_SORT_MEASUREMENT) ? (Identifier_Transferable) this.action.getId().getTransferable()
						: (new Identifier_Transferable("")),
				(this.sort == ResultSort._RESULT_SORT_ANALYSIS) ? (Identifier_Transferable) this.action.getId().getTransferable()
						: (new Identifier_Transferable("")),
				(this.sort == ResultSort._RESULT_SORT_EVALUATION) ? (Identifier_Transferable) this.action.getId().getTransferable()
						: (new Identifier_Transferable("")),
				(this.sort == ResultSort._RESULT_SORT_MODELING) ? (Identifier_Transferable) this.action.getId().getTransferable()
						: (new Identifier_Transferable("")),
				ResultSort.from_int(this.sort),
				pts);
	}

	public short getEntityCode() {
		return ObjectEntities.RESULT_ENTITY_CODE;
	}

	public Action getAction() {
		return this.action;
	}
	
	public void setAction(Action action) {
		this.action = action;
		super.changed = true;
	}

	public ResultSort getSort() {
		return ResultSort.from_int(this.sort);
	}
	
	public void setSort(ResultSort sort) {
		this.sort = sort.value();
		super.changed = true;
	}

	public SetParameter[] getParameters() {
		return this.parameters;
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  Action action,
											  int sort) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.action = action;
		this.sort = sort;
	}

	protected static Result createInstance(Identifier creatorId,
										   Action action,
										   ResultSort sort,
										   SetParameter[] parameters) throws CreateObjectException {
		if (creatorId == null || action == null || sort == null ||
				parameters == null || parameters.length == 0)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Result result = new Result(IdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE),
				creatorId,
				0L,
				action,
				sort.value(),
				parameters);
			result.changed = true;
			return result;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Result.createInstance | cannot generate identifier ", e);
		}
	}

	protected synchronized void setParameters0(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	public void setParameters(SetParameter[] parameters) {
		this.setParameters0(parameters);
		super.changed = true;
	}

	public java.util.Set getDependencies() {
		java.util.Set dependencies = new HashSet();

		for (int i = 0; i < this.parameters.length; i++)
			dependencies.add(this.parameters[i].getType());

		dependencies.add(this.action);

		return dependencies;
	}
}
