/*
 * $Id: MCMImplementation.java,v 1.39 2005/06/19 14:00:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.mcm.corba.MCMPOA;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.39 $, $Date: 2005/06/19 14:00:50 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class MCMImplementation extends MCMPOA {

	private static final long serialVersionUID = -3362347139575001444L;


	public void receiveTests(Test_Transferable[] testsT) throws AMFICOMRemoteException {
		Log.debugMessage("Received " + testsT.length + " tests", Log.DEBUGLEVEL07);
		List tests = new LinkedList();
		for (int i = 0; i < testsT.length; i++) {
			try {
				Test test = new Test(testsT[i]);
				tests.add(test);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
			}
		}

		MeasurementControlModule.addTests(tests);
	}

	public void abortTests(IdlIdentifier[] testIdsT) throws AMFICOMRemoteException {
		Set ids = Identifier.fromTransferables(testIdsT);
		MeasurementControlModule.abortTests(ids);
	}





	public Measurement_Transferable[] transmitMeasurements(IdlIdentifier[] identifier_Transferables)
			throws AMFICOMRemoteException {
		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = StorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement measurement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurement = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) measurement.getTransferable();
		}
		return transferables;
	}

	public Analysis_Transferable[] transmitAnalyses(IdlIdentifier[] identifier_Transferables)
			throws AMFICOMRemoteException {
		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = StorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis analysis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysis = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) analysis.getTransferable();
		}
		return transferables;
	}

  public Evaluation_Transferable[] transmitEvaluations(IdlIdentifier[] identifier_Transferables)
			throws AMFICOMRemoteException {
		Set ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = StorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation evaluation;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluation = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
		}
		return transferables;
	}

	public Measurement_Transferable[] transmitMeasurementsButIdsByCondition(IdlIdentifier[] identifier_Transferables,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement measurement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurement = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) measurement.getTransferable();
		}
		return transferables;
	}

	public Analysis_Transferable[] transmitAnalysesButIdsByCondition(IdlIdentifier[] identifier_Transferables,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis analysis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysis = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) analysis.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsByCondition(IdlIdentifier[] identifier_Transferables,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		Set objects = this.getObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation evaluation;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluation = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
		}
		return transferables;
	}

  private Set getObjectsButIdsCondition(IdlIdentifier[] identifier_Transferables,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		try {

			StorableObjectCondition condition = null;
			try {
				condition = StorableObjectConditionBuilder.restoreCondition(storableObjectCondition_Transferable);
			}
			catch (IllegalDataException ide) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
						CompletionStatus.COMPLETED_NO,
						"Cannot restore condition -- " + ide.getMessage());
			}

			Set objects = null;
			try {
				Set ids = Identifier.fromTransferables(identifier_Transferables);

				/**
				 * NOTE: If it is impossible to load objects by Loader - return only those from Pool
				 */
				objects = StorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true, false);

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




	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}
	
	
}
