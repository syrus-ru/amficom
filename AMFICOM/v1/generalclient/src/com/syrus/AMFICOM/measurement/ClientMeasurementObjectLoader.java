/*
 * $Id: ClientMeasurementObjectLoader.java,v 1.13 2005/02/02 09:14:25 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.administration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @version $Revision: 1.13 $, $Date: 2005/02/02 09:14:25 $
 * @author $Author: bob $
 * @module generalclient_v1
 */

public final class ClientMeasurementObjectLoader implements MeasurementObjectLoader {

	private CMServer								server;

	private static AccessIdentifier_Transferable	accessIdentifierTransferable;

	public ClientMeasurementObjectLoader(CMServer server) {
		this.server = server;
	}

	public static void setAccessIdentifierTransferable(AccessIdentifier_Transferable accessIdentifier_Transferable) {
		accessIdentifierTransferable = accessIdentifier_Transferable;
	}

	public void delete(Identifier id) throws CommunicationException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public void delete(List ids) throws CommunicationException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.delete | AMFICOMRemoteException ";
			throw new CommunicationException(msg, e);
		}
	}

	public MeasurementType loadMeasurementType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new MeasurementType(this.server.transmitMeasurementType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementType | new MeasurementType(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementType | server.transmitMeasurementType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new AnalysisType(this.server.transmitAnalysisType((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysisType | new AnalysisType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysisType | server.transmitAnalysisType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public EvaluationType loadEvaluationType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new EvaluationType(this.server.transmitEvaluationType(
				(Identifier_Transferable) id.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadEvaluationType | new EvaluationType(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadEvaluationType | server.transmitEvaluationType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadSet(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Set(this.server.transmitSet((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadSet | new Set(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadSet | server.transmitSet(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new MeasurementSetup(this.server.transmitMeasurementSetup((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementSetup | new MeasurementSetup(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementSetup | server.transmitMeasurementSetup("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new Modeling(this.server.transmitModeling((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | new Measurement(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | server.transmitMeasurement(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public ModelingType loadModelingType(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new ModelingType(this.server.transmitModelingType((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | new Measurement(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | server.transmitMeasurement(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Measurement loadMeasurement(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Measurement(this.server.transmitMeasurement((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | new Measurement(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | server.transmitMeasurement(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new Analysis(this.server.transmitAnalysis((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysis | new Analysis(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysis | server.transmitAnalysis(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new Evaluation(this.server.transmitEvaluation((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadEvaluation | new Evaluation(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadEvaluation | server.transmitEvaluation(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Test loadTest(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Test(this.server.transmitTest((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientTestObjectLoader.loadTest | new Test(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientTestObjectLoader.loadTest | server.transmitTest(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Result loadResult(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Result(this.server.transmitResult((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadResult | new Result(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadResult | server.transmitResult(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new TemporalPattern(this.server.transmitTemporalPattern((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadTemporalPattern | new TemporalPattern(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadTemporalPattern | server.transmitTemporalPattern("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Analysis_Transferable[] transferables = this.server.transmitAnalyses(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Analysis(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadAnalysesButIds(StorableObjectCondition storableObjectCondition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Analysis_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (storableObjectCondition instanceof DomainCondition) {
				transferables = this.server.transmitAnalysesButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) storableObjectCondition
							.getTransferable());
			} else {
				transferables = this.server.transmitAnalysesButIds(identifierTransferables,
					accessIdentifierTransferable);
				if (storableObjectCondition != null && !(storableObjectCondition instanceof DomainCondition)) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadAnalysesButIds | " + "Class '"
							+ storableObjectCondition.getClass().getName() + "' is not instanse of DomainCondition");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Analysis(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AnalysisType_Transferable[] transferables = this.server.transmitAnalysisTypes(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new AnalysisType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadAnalysisTypesButIds(StorableObjectCondition storableObjectCondition, List ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			AnalysisType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (storableObjectCondition instanceof LinkedIdsCondition) {

				transferables = this.server.transmitAnalysisTypesButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (LinkedIdsCondition_Transferable) storableObjectCondition
							.getTransferable());
			} else {
				transferables = this.server.transmitAnalysisTypesButIds(identifierTransferables,
					accessIdentifierTransferable);
				if (storableObjectCondition != null && !(storableObjectCondition instanceof LinkedIdsCondition)) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadAnalysisTypesButIds | " + "Class '"
							+ storableObjectCondition.getClass().getName() + "' is not instanse of LinkedIdsCondition");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new AnalysisType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadEvaluations(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Evaluation_Transferable[] transferables = this.server.transmitEvaluations(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Evaluation(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadEvaluationsButIds(StorableObjectCondition storableObjectCondition, List ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Evaluation_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (storableObjectCondition instanceof DomainCondition) {

				transferables = this.server.transmitEvaluationsButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) storableObjectCondition
							.getTransferable());

			} else {
				transferables = this.server.transmitEvaluationsButIds(identifierTransferables,
					accessIdentifierTransferable);
				if (storableObjectCondition != null && !(storableObjectCondition instanceof DomainCondition)) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadEvaluationsButIds | " + "Class '"
							+ storableObjectCondition.getClass().getName() + "' is not instanse of DomainCondition");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Evaluation(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EvaluationType_Transferable[] transferables = this.server.transmitEvaluationTypes(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new EvaluationType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadEvaluationTypesButIds(StorableObjectCondition storableObjectCondition, List ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			EvaluationType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (storableObjectCondition instanceof LinkedIdsCondition) {

				transferables = this.server.transmitEvaluationTypesButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (LinkedIdsCondition_Transferable) storableObjectCondition
							.getTransferable());

			} else {
				transferables = this.server.transmitEvaluationTypesButIds(identifierTransferables,
					accessIdentifierTransferable);
				if (storableObjectCondition != null && !(storableObjectCondition instanceof LinkedIdsCondition)) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadEvaluationTypesButIds | " + "Class '"
							+ storableObjectCondition.getClass().getName() + "' is not instanse of LinkedIdsCondition");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new EvaluationType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMeasurements(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}

			Measurement_Transferable[] transferables = this.server.transmitMeasurements(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Measurement(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMeasurementsButIds(StorableObjectCondition storableObjectCondition, List ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Measurement_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (storableObjectCondition instanceof DomainCondition) {
				transferables = this.server.transmitMeasurementsButIdsDomainCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) storableObjectCondition
							.getTransferable());
			} else if (storableObjectCondition instanceof LinkedIdsCondition) {
				transferables = this.server.transmitMeasurementsButIdsLinkedCondition(identifierTransferables,
					accessIdentifierTransferable, (LinkedIdsCondition_Transferable) storableObjectCondition
							.getTransferable());
			} else {
				transferables = this.server.transmitMeasurementsButIds(identifierTransferables,
					accessIdentifierTransferable);
				if (storableObjectCondition != null && !(storableObjectCondition instanceof DomainCondition)) {
					Log
							.errorMessage("ClientMeasurementObjectLoader.loadMeasurementsButIds | " + "Class '"
									+ storableObjectCondition.getClass().getName()
									+ "' is not instanse of LinkedIdsCondition ");
				}

			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Measurement(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadModelings(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Modeling_Transferable[] transferables = this.server.transmitModelings(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Modeling(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadModelingTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			ModelingType_Transferable[] transferables = this.server.transmitModelingTypes(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new ModelingType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadModelingsButIds(StorableObjectCondition storableObjectCondition, List ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Modeling_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (storableObjectCondition instanceof DomainCondition) {

				transferables = this.server.transmitModelingsButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) storableObjectCondition
							.getTransferable());

			} else {
				transferables = this.server.transmitModelingsButIds(identifierTransferables,
					accessIdentifierTransferable);
				if (storableObjectCondition != null && !(storableObjectCondition instanceof DomainCondition)) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadModelingsButIds | " + "Class '"
							+ storableObjectCondition.getClass().getName() + "' is not instanse of DomainCondition");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Modeling(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadModelingTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			ModelingType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitModelingTypesButIdsCondition(identifierTransferables,
				(StorableObjectCondition_Transferable) condition.getTransferable(), accessIdentifierTransferable);

			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new ModelingType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementSetup_Transferable[] transferables = this.server.transmitMeasurementSetups(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MeasurementSetup(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMeasurementSetupsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MeasurementSetup_Transferable[] transferables = null;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (condition instanceof LinkedIdsCondition) {
				LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) condition;
				transferables = this.server.transmitMeasurementSetupsButIdsLinkedCondition(identifierTransferables,
					accessIdentifierTransferable, (LinkedIdsCondition_Transferable) linkedIdsCondition
							.getTransferable());
			} else {
				transferables = this.server.transmitMeasurementSetupsButIds(identifierTransferables,
					accessIdentifierTransferable);
				if (condition != null) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadMeasurementsButIds | " + "Condition class '"
							+ condition.getClass().getName() + "' is not support.");
				}
			}

			List list = null;
			if (transferables != null) {
				list = new ArrayList(transferables.length);
				for (int j = 0; j < transferables.length; j++) {
					list.add(new MeasurementSetup(transferables[j]));
				}

			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementType_Transferable[] transferables = this.server.transmitMeasurementTypes(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MeasurementType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMeasurementTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementType_Transferable[] transferables;
			if (condition instanceof StringFieldCondition) {
				StringFieldCondition stringFieldCondition = (StringFieldCondition) condition;
				transferables = this.server.transmitMeasurementTypesButIdsStringFieldCondition(identifierTransferables,
					accessIdentifierTransferable, (StringFieldCondition_Transferable) stringFieldCondition
							.getTransferable());
			} else if (condition instanceof LinkedIdsCondition) {
				LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) condition;
				transferables = this.server.transmitMeasurementTypesButIdsLinkedIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (LinkedIdsCondition_Transferable) linkedIdsCondition
							.getTransferable());

			} else
				transferables = this.server.transmitMeasurementTypesButIds(identifierTransferables,
					accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MeasurementType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadResults(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Result_Transferable[] transferables = this.server.transmitResults(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Result(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadResultsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Result_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (condition instanceof DomainCondition) {

				transferables = this.server.transmitResultsButIdsDomainCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) condition.getTransferable());

			} else if (condition instanceof LinkedIdsCondition) {
				transferables = this.server.transmitResultsButIdsLinkedCondition(identifierTransferables,
					accessIdentifierTransferable, (LinkedIdsCondition_Transferable) condition.getTransferable());

			} else {
				transferables = this.server
						.transmitResultsButIds(identifierTransferables, accessIdentifierTransferable);

				if (condition != null && !(condition instanceof DomainCondition)) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadResultsButIds | " + "Class '"
							+ condition.getClass().getName() + "' is not instanse of DomainCondition");
				}
			}

			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Result(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadSets(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Set_Transferable[] transferables = this.server.transmitSets(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Set(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadSetsButIds(StorableObjectCondition storableObjectCondition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Set_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (storableObjectCondition instanceof DomainCondition) {
				transferables = this.server.transmitSetsButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) storableObjectCondition
							.getTransferable());
			} else {
				transferables = this.server.transmitSetsButIds(identifierTransferables, accessIdentifierTransferable);
				if (storableObjectCondition != null && !(storableObjectCondition instanceof DomainCondition)) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadSetsButIds | " + "Class '"
							+ storableObjectCondition.getClass().getName() + "' is not instanse of DomainCondition");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Set(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TemporalPattern_Transferable[] transferables = this.server.transmitTemporalPatterns(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new TemporalPattern(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadTemporalPatternsButIds(StorableObjectCondition storableObjectCondition, List ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TemporalPattern_Transferable[] transferables = this.server.transmitTemporalPatternsButIds(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new TemporalPattern(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadTests(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Test_Transferable[] transferables = this.server.transmitTests(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Test(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadTestsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Test_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (condition instanceof TemporalCondition) {
				transferables = this.server.transmitTestsButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (TemporalCondition_Transferable) condition.getTransferable());
			} else {
				transferables = this.server.transmitTestsButIds(identifierTransferables, accessIdentifierTransferable);
				if (condition != null) {
					Log.errorMessage("ClientMeasurementObjectLoader.loadTestsButIds | " + "Condition class '"
							+ condition.getClass().getName() + "' is not support.");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Test(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		MeasurementType_Transferable transferables = (MeasurementType_Transferable) measurementType.getTransferable();
		try {
			this.server.receiveMeasurementType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementType| receiveMeasurementTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {

		AnalysisType_Transferable transferables = (AnalysisType_Transferable) analysisType.getTransferable();

		try {
			this.server.receiveAnalysisType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysisType | receiveAnalysisTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		EvaluationType_Transferable transferables = (EvaluationType_Transferable) evaluationType.getTransferable();
		try {
			this.server.receiveEvaluationType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluationType | receiveEvaluationTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveSet(Set set, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Set_Transferable transferables = (Set_Transferable) set.getTransferable();
		try {
			this.server.receiveSet(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveSet | receiveSets";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		MeasurementSetup_Transferable transferables = (MeasurementSetup_Transferable) measurementSetup
				.getTransferable();
		try {
			this.server.receiveMeasurementSetup(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementSetup | receiveMeasurementSetups";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Modeling_Transferable transferables = (Modeling_Transferable) modeling.getTransferable();
		try {
			this.server.receiveModeling(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveModeling | receiveModelings";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModelingType(ModelingType modeling, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		ModelingType_Transferable transferables = (ModelingType_Transferable) modeling.getTransferable();
		try {
			this.server.receiveModelingType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveModelingType | receiveModelingTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		Measurement_Transferable transferables = (Measurement_Transferable) measurement.getTransferable();
		try {
			this.server.receiveMeasurement(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurement | receiveMeasurements";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Analysis_Transferable transferables = (Analysis_Transferable) analysis.getTransferable();
		try {
			this.server.receiveAnalysis(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysis | receiveAnalysiss";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		Evaluation_Transferable transferables = (Evaluation_Transferable) evaluation.getTransferable();
		try {
			this.server.receiveEvaluation(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluation | receiveEvaluations";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTest(Test test, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Test_Transferable transferables = (Test_Transferable) test.getTransferable();
		try {
			this.server.receiveTest(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTest | receiveTests";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveResult(Result result, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Result_Transferable transferables = (Result_Transferable) result.getTransferable();
		try {
			this.server.receiveResult(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveResult | receiveResults";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TemporalPattern_Transferable transferables = (TemporalPattern_Transferable) temporalPattern.getTransferable();
		try {
			this.server.receiveTemporalPattern(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTemporalPattern | receiveTemporalPatterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementTypes(List measurementTypes, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[measurementTypes.size()];
		int i = 0;
		for (Iterator it = measurementTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementType_Transferable) ((MeasurementType) it.next()).getTransferable();
		}
		try {
			this.server.receiveMeasurementTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementType | receiveMeasurementTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);

		}
	}

	public void saveAnalysisTypes(List analysisTypes, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[analysisTypes.size()];
		int i = 0;
		for (Iterator it = analysisTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (AnalysisType_Transferable) ((AnalysisType) it.next()).getTransferable();
		}
		try {
			this.server.receiveAnalysisTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysisTypes | receiveAnalysisTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluationTypes(List evaluationTypes, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[evaluationTypes.size()];
		int i = 0;
		for (Iterator it = evaluationTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (EvaluationType_Transferable) ((EvaluationType) it.next()).getTransferable();
		}
		try {
			this.server.receiveEvaluationTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluationType | receiveEvaluationTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveSets(List sets, boolean force) throws DatabaseException, CommunicationException,
			VersionCollisionException {
		Set_Transferable[] transferables = new Set_Transferable[sets.size()];
		int i = 0;
		for (Iterator it = sets.iterator(); it.hasNext(); i++) {
			transferables[i] = (Set_Transferable) ((Set) it.next()).getTransferable();
		}
		try {
			this.server.receiveSets(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveSets | receiveSets";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModelings(List modelings, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Modeling_Transferable[] transferables = new Modeling_Transferable[modelings.size()];
		int i = 0;
		for (Iterator it = modelings.iterator(); it.hasNext(); i++) {
			transferables[i] = (Modeling_Transferable) ((Modeling) it.next()).getTransferable();
		}
		try {
			this.server.receiveModelings(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientModelingObjectLoader.saveModelings | receiveModelings";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModelingTypes(List modelings, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		ModelingType_Transferable[] transferables = new ModelingType_Transferable[modelings.size()];
		int i = 0;
		for (Iterator it = modelings.iterator(); it.hasNext(); i++) {
			transferables[i] = (ModelingType_Transferable) ((ModelingType) it.next()).getTransferable();
		}
		try {
			this.server.receiveModelingTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientModelingTypeObjectLoader.saveModelingTypes | receiveModelingTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementSetups(List measurementSetups, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[measurementSetups.size()];
		int i = 0;
		for (Iterator it = measurementSetups.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementSetup_Transferable) ((MeasurementSetup) it.next()).getTransferable();
		}
		try {
			this.server.receiveMeasurementSetups(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementSetupObjectLoader.saveMeasurementSetups | receiveMeasurementSetups";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurements(List measurements, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Measurement_Transferable[] transferables = new Measurement_Transferable[measurements.size()];
		int i = 0;
		for (Iterator it = measurements.iterator(); it.hasNext(); i++) {
			transferables[i] = (Measurement_Transferable) ((Measurement) it.next()).getTransferable();
		}
		try {
			this.server.receiveMeasurements(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurements | receiveMeasurements";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveAnalyses(List analyses, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Analysis_Transferable[] transferables = new Analysis_Transferable[analyses.size()];
		int i = 0;
		for (Iterator it = analyses.iterator(); it.hasNext(); i++) {
			transferables[i] = (Analysis_Transferable) ((Analysis) it.next()).getTransferable();
		}
		try {
			this.server.receiveAnalyses(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.receiveAnalyses | receiveAnalyses";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluations(List evaluations, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Evaluation_Transferable[] transferables = new Evaluation_Transferable[evaluations.size()];
		int i = 0;
		for (Iterator it = evaluations.iterator(); it.hasNext(); i++) {
			transferables[i] = (Evaluation_Transferable) ((Evaluation) it.next()).getTransferable();
		}
		try {
			this.server.receiveEvaluations(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluations | receiveEvaluations";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTests(List tests, boolean force) throws DatabaseException, CommunicationException,
			VersionCollisionException {
		Test_Transferable[] transferables = new Test_Transferable[tests.size()];
		int i = 0;
		for (Iterator it = tests.iterator(); it.hasNext(); i++) {
			transferables[i] = (Test_Transferable) ((Test) it.next()).getTransferable();
		}
		try {
			this.server.receiveTests(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTests | receiveTests";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveResults(List results, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Result_Transferable[] transferables = new Result_Transferable[results.size()];
		int i = 0;
		for (Iterator it = results.iterator(); it.hasNext(); i++) {
			transferables[i] = (Result_Transferable) ((Result) it.next()).getTransferable();
		}
		try {
			this.server.receiveResults(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientResultObjectLoader.saveResults | receiveResults";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTemporalPatterns(List temporalPatterns, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[temporalPatterns.size()];
		int i = 0;
		for (Iterator it = temporalPatterns.iterator(); it.hasNext(); i++) {
			transferables[i] = (TemporalPattern_Transferable) ((TemporalPattern) it.next()).getTransferable();
		}
		try {
			this.server.receiveTemporalPatterns(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientTemporalPaternObjectLoader.saveTemporalPaterns | receiveTemporalPaterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException {
		try {
			java.util.Set refreshedIds = new HashSet();
			Identifier_Transferable[] identifier_Transferables;
			StorableObject_Transferable[] storableObject_Transferables = new StorableObject_Transferable[storableObjects
					.size()];
			int i = 0;
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObject_Transferables[i] = storableObject.getHeaderTransferable();
			}
			identifier_Transferables = this.server.transmitRefreshedMeasurementObjects(storableObject_Transferables,
				accessIdentifierTransferable);

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}
}
