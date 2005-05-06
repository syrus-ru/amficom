/*
 * $Id: MServerMeasurementTransmit.java,v 1.3 2005/05/06 11:02:02 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.Iterator;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.CronTemporalPattern;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.CronTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.PeriodicalTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/06 11:02:02 $
 * @author $Author: bob $
 * @module mserver_v1
 */
abstract class MServerMeasurementTransmit extends MServerConfigurationTransmit {

	private static final long serialVersionUID = -3608597706693444950L;

  /* Transmit multiple objects*/
 
	public MeasurementType_Transferable[] transmitMeasurementTypes(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		MeasurementType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypes(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		AnalysisType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		EvaluationType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) object.getTransferable();
		}
		return transferables;
	}



	public Measurement_Transferable[] transmitMeasurements(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Analysis_Transferable[] transmitAnalyses(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluations(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		MeasurementSetup object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) object.getTransferable();
		}
		return transferables;
		} catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		} 
	}

	public Set_Transferable[] transmitSets(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		Set object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Set) it.next();
			transferables[i] = (Set_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatterns(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		IntervalsTemporalPattern_Transferable[] transferables = new IntervalsTemporalPattern_Transferable[objects.size()];
		int i = 0;
		IntervalsTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (IntervalsTemporalPattern) it.next();
			transferables[i] = (IntervalsTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatterns(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		PeriodicalTemporalPattern_Transferable[] transferables = new PeriodicalTemporalPattern_Transferable[objects.size()];
		int i = 0;
		PeriodicalTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (PeriodicalTemporalPattern) it.next();
			transferables[i] = (PeriodicalTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatterns(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjects(idsT);

		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[objects.size()];
		int i = 0;
		CronTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CronTemporalPattern) it.next();
			transferables[i] = (CronTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getMeasurementObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(idsT);
			java.util.Set objects = MeasurementStorableObjectPool.getStorableObjects(ids, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/* Transmit multiple objects but ids by condition*/

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsByCondition(Identifier_Transferable[] ids_Transferable,
			StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjectsButIdsCondition(ids_Transferable, condition_Transferable);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			MeasurementType measurementType = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
		}
		return transferables;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIdsByCondition(Identifier_Transferable[] ids_Transferable,
			StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjectsButIdsCondition(ids_Transferable, condition_Transferable);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			AnalysisType analysisType = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIdsByCondition(Identifier_Transferable[] ids_Transferable,
			StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjectsButIdsCondition(ids_Transferable, condition_Transferable);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			EvaluationType evaluationType = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
		}
		return transferables;
	}



	public Measurement_Transferable[] transmitMeasurementsButIdsByCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement measurement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurement = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) measurement.getTransferable();
		}
		return transferables;
	}

	public Analysis_Transferable[] transmitAnalysesButIdsByCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis analysis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysis = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) analysis.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsByCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		java.util.Set objects = this.getMeasurementObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation evaluation;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluation = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getMeasurementObjectsButIdsCondition(Identifier_Transferable[] idsT, StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		try {

			StorableObjectCondition condition = null;
			try {
				condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			}
			catch (IllegalDataException ide) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
						CompletionStatus.COMPLETED_NO,
						"Cannot restore condition -- " + ide.getMessage());
			}

			try {
				java.util.Set ids = Identifier.fromTransferables(idsT);
				java.util.Set objects = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
				return objects;
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
			}
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
