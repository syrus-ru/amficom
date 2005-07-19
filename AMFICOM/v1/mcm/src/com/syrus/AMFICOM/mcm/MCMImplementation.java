/*
 * $Id: MCMImplementation.java,v 1.47 2005/07/19 17:26:46 arseniy Exp $
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

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.mcm.corba.MCMPOA;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysis;
import com.syrus.AMFICOM.measurement.corba.IdlEvaluation;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurement;
import com.syrus.AMFICOM.measurement.corba.IdlResult;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.47 $, $Date: 2005/07/19 17:26:46 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class MCMImplementation extends MCMPOA {

	MCMImplementation() {
		//Nothing
	}

	private static final long serialVersionUID = -3362347139575001444L;


	public void receiveTests(final IdlTest[] testsT) throws AMFICOMRemoteException {
		Log.debugMessage("Received " + testsT.length + " tests", Log.DEBUGLEVEL07);
		final List<Test> tests = new LinkedList<Test>();
		for (int i = 0; i < testsT.length; i++) {
			try {
				final Test test = new Test(testsT[i]);
				tests.add(test);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
			}
		}

		MeasurementControlModule.addTests(tests);
	}

	public void abortTests(final IdlIdentifier[] testIdsT) throws AMFICOMRemoteException {
		final Set<Identifier> ids = Identifier.fromTransferables(testIdsT);
		MeasurementControlModule.abortTests(ids);
	}





	public IdlMeasurement[] transmitMeasurements(final IdlIdentifier[] identifier_Transferables)
			throws AMFICOMRemoteException {
		final Set<Identifier> ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = StorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlMeasurement[] transferables = new IdlMeasurement[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Measurement measurement = (Measurement) it.next();
			transferables[i] = measurement.getTransferable(orb);
		}
		return transferables;
	}

	public IdlAnalysis[] transmitAnalyses(final IdlIdentifier[] identifier_Transferables)
			throws AMFICOMRemoteException {
		final Set<Identifier> ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = StorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlAnalysis[] transferables = new IdlAnalysis[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Analysis analysis = (Analysis) it.next();
			transferables[i] = analysis.getTransferable(orb);
		}
		return transferables;
	}

  public IdlEvaluation[] transmitEvaluations(final IdlIdentifier[] identifier_Transferables)
			throws AMFICOMRemoteException {
		final Set<Identifier> ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = StorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlEvaluation[] transferables = new IdlEvaluation[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Evaluation evaluation = (Evaluation) it.next();
			transferables[i] = evaluation.getTransferable(orb);
		}
		return transferables;
	}

  public IdlResult[] transmitResults(final IdlIdentifier[] identifier_Transferables)
			throws AMFICOMRemoteException {
		final Set<Identifier> ids = Identifier.fromTransferables(identifier_Transferables);

		Set objects = null;
		try {
			objects = StorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlResult[] transferables = new IdlResult[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Result result = (Result) it.next();
			transferables[i] = result.getTransferable(orb);
		}
		return transferables;
	}



	public IdlMeasurement[] transmitMeasurementsButIdsByCondition(final IdlIdentifier[] identifier_Transferables,
			final IdlStorableObjectCondition storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		final Set objects = this.getObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlMeasurement[] transferables = new IdlMeasurement[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Measurement measurement = (Measurement) it.next();
			transferables[i] = measurement.getTransferable(orb);
		}
		return transferables;
	}

	public IdlAnalysis[] transmitAnalysesButIdsByCondition(IdlIdentifier[] identifier_Transferables,
			IdlStorableObjectCondition storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		final Set objects = this.getObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlAnalysis[] transferables = new IdlAnalysis[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Analysis analysis = (Analysis) it.next();
			transferables[i] = analysis.getTransferable(orb);
		}
		return transferables;
	}

	public IdlEvaluation[] transmitEvaluationsButIdsByCondition(final IdlIdentifier[] identifier_Transferables,
			final IdlStorableObjectCondition storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		final Set objects = this.getObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlEvaluation[] transferables = new IdlEvaluation[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Evaluation evaluation = (Evaluation) it.next();
			transferables[i] = evaluation.getTransferable(orb);
		}
		return transferables;
	}

	public IdlResult[] transmitResultsButIdsByCondition(final IdlIdentifier[] identifier_Transferables,
			final IdlStorableObjectCondition storableObjectCondition_Transferable) throws AMFICOMRemoteException {
		final Set objects = this.getObjectsButIdsCondition(identifier_Transferables, storableObjectCondition_Transferable);

		final ORB orb = MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final IdlResult[] transferables = new IdlResult[objects.size()];
		int i = 0;
		for (final Iterator it = objects.iterator(); it.hasNext(); i++) {
			final Result result = (Result) it.next();
			transferables[i] = result.getTransferable(orb);
		}
		return transferables;
	}



  private Set getObjectsButIdsCondition(final IdlIdentifier[] identifier_Transferables,
  		final IdlStorableObjectCondition storableObjectCondition_Transferable) throws AMFICOMRemoteException {
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
				final Set<Identifier> ids = Identifier.fromTransferables(identifier_Transferables);

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




	public void verify(final byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}
	
	
}
