/*
 * $Id: ClientMeasurementObjectLoader.java,v 1.22 2005/02/15 10:32:33 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.TypicalCondition_Transferable;
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
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;

/**
 * @version $Revision: 1.22 $, $Date: 2005/02/15 10:32:33 $
 * @author $Author: max $
 * @module generalclient_v1
 */

public final class ClientMeasurementObjectLoader implements MeasurementObjectLoader {

	private CMServer								server;

	public ClientMeasurementObjectLoader(CMServer server) {
		this.server = server;
	}
	
	private AccessIdentifier_Transferable getAccessIdentifierTransferable() {
		return  (AccessIdentifier_Transferable) SessionContext.getAccessIdentity().getTransferable();
	}
	
	private StorableObjectCondition_Transferable getConditionTransferable(StorableObjectCondition condition) {
		StorableObjectCondition_Transferable condition_Transferable = new StorableObjectCondition_Transferable();
		Object transferable = condition.getTransferable();
		if (condition instanceof LinkedIdsCondition) {
			condition_Transferable.linkedIdsCondition((LinkedIdsCondition_Transferable) transferable);
		} else if (condition instanceof CompoundCondition) {
			condition_Transferable.compoundCondition((CompoundCondition_Transferable) transferable);
		} else if (condition instanceof TypicalCondition) {
			condition_Transferable.typicalCondition((TypicalCondition_Transferable) transferable);
		} else if (condition instanceof EquivalentCondition) {
			condition_Transferable.equialentCondition((EquivalentCondition_Transferable) transferable);
		} 
		return condition_Transferable;
	}

	public void delete(Identifier id) throws CommunicationException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public void delete(Collection ids) throws CommunicationException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.delete | AMFICOMRemoteException ";
			throw new CommunicationException(msg, e);
		}
	}

	public MeasurementType loadMeasurementType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new MeasurementType(this.server.transmitMeasurementType((Identifier_Transferable) id
					.getTransferable(), getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
				(Identifier_Transferable) id.getTransferable(), getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
					.getTransferable(), getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
				getAccessIdentifierTransferable()));
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
					.getTransferable(), getAccessIdentifierTransferable()));
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

	public Collection loadAnalyses(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Analysis_Transferable[] transferables = this.server.transmitAnalyses(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadAnalysesButIds(StorableObjectCondition storableObjectCondition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Analysis_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitAnalysesButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), (StorableObjectCondition_Transferable) storableObjectCondition
						.getTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadAnalysisTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AnalysisType_Transferable[] transferables = this.server.transmitAnalysisTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadAnalysisTypesButIds(StorableObjectCondition storableObjectCondition, Collection ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			AnalysisType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitAnalysisTypesButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), (StorableObjectCondition_Transferable) storableObjectCondition
						.getTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadEvaluations(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Evaluation_Transferable[] transferables = this.server.transmitEvaluations(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadEvaluationsButIds(StorableObjectCondition storableObjectCondition, Collection ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Evaluation_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitEvaluationsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), (StorableObjectCondition_Transferable) storableObjectCondition
						.getTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadEvaluationTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EvaluationType_Transferable[] transferables = this.server.transmitEvaluationTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadEvaluationTypesButIds(StorableObjectCondition storableObjectCondition, Collection ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			EvaluationType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitEvaluationTypesButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), (StorableObjectCondition_Transferable) storableObjectCondition
						.getTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadMeasurements(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}

			Measurement_Transferable[] transferables = this.server.transmitMeasurements(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadMeasurementsButIds(StorableObjectCondition storableObjectCondition, Collection ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Measurement_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}

			transferables = this.server.transmitMeasurementsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), (StorableObjectCondition_Transferable) storableObjectCondition
						.getTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadModelings(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Modeling_Transferable[] transferables = this.server.transmitModelings(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadModelingTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			ModelingType_Transferable[] transferables = this.server.transmitModelingTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadModelingsButIds(StorableObjectCondition storableObjectCondition, Collection ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Modeling_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitModelingsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), (StorableObjectCondition_Transferable) storableObjectCondition
						.getTransferable());

			Collection list = new ArrayList(transferables.length);
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

	public Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
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
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));

			Collection list = new ArrayList(transferables.length);
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

	public Collection loadMeasurementSetups(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementSetup_Transferable[] transferables = this.server.transmitMeasurementSetups(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MeasurementSetup_Transferable[] transferables = null;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitMeasurementSetupsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));

			Collection list = null;
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

	public Collection loadMeasurementTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementType_Transferable[] transferables = this.server.transmitMeasurementTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementType_Transferable[] transferables;
			transferables = this.server.transmitMeasurementTypesButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadResults(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Result_Transferable[] transferables = this.server.transmitResults(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadResultsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Result_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitResultsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));

			Collection list = new ArrayList(transferables.length);
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

	public Collection loadSets(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Set_Transferable[] transferables = this.server.transmitSets(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadSetsButIds(StorableObjectCondition storableObjectCondition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Set_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitSetsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), (StorableObjectCondition_Transferable) storableObjectCondition
						.getTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadTemporalPatterns(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TemporalPattern_Transferable[] transferables = this.server.transmitTemporalPatterns(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadTemporalPatternsButIds(StorableObjectCondition storableObjectCondition, Collection ids)
			throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TemporalPattern_Transferable[] transferables = this.server.transmitTemporalPatternsButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadTests(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Test_Transferable[] transferables = this.server.transmitTests(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
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

	public Collection loadTestsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Test_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitTestsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), this.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
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
	
	private void updateStorableObjectHeader(Collection storableObjects, StorableObject_Transferable[] transferables) {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			Identifier_Transferable id = (Identifier_Transferable) storableObject.getId().getTransferable();
			for (int i = 0; i < transferables.length; i++) {
				if (transferables[i].id.equals(id)) {
					storableObject.updateFromHeaderTransferable(transferables[i]);
				}
			}
		}
	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		MeasurementType_Transferable transferables = (MeasurementType_Transferable) measurementType.getTransferable();
		try {
			measurementType.updateFromHeaderTransferable(this.server.receiveMeasurementType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementType| receiveMeasurementTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {

		AnalysisType_Transferable transferables = (AnalysisType_Transferable) analysisType.getTransferable();

		try {
			analysisType.updateFromHeaderTransferable(this.server.receiveAnalysisType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysisType | receiveAnalysisTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		EvaluationType_Transferable transferables = (EvaluationType_Transferable) evaluationType.getTransferable();
		try {
			evaluationType.updateFromHeaderTransferable(this.server.receiveEvaluationType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluationType | receiveEvaluationTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveSet(Set set, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Set_Transferable transferables = (Set_Transferable) set.getTransferable();
		try {
			set.updateFromHeaderTransferable(this.server.receiveSet(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveSet | receiveSets";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		MeasurementSetup_Transferable transferables = (MeasurementSetup_Transferable) measurementSetup
				.getTransferable();
		try {
			measurementSetup.updateFromHeaderTransferable(this.server.receiveMeasurementSetup(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementSetup | receiveMeasurementSetups";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Modeling_Transferable transferables = (Modeling_Transferable) modeling.getTransferable();
		try {
			modeling.updateFromHeaderTransferable(this.server.receiveModeling(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveModeling | receiveModelings";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		ModelingType_Transferable transferables = (ModelingType_Transferable) modelingType.getTransferable();
		try {
			modelingType.updateFromHeaderTransferable(this.server.receiveModelingType(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveModelingType | receiveModelingTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		Measurement_Transferable transferables = (Measurement_Transferable) measurement.getTransferable();
		try {
			measurement.updateFromHeaderTransferable(this.server.receiveMeasurement(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurement | receiveMeasurements";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Analysis_Transferable transferables = (Analysis_Transferable) analysis.getTransferable();
		try {
			analysis.updateFromHeaderTransferable(this.server.receiveAnalysis(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysis | receiveAnalysiss";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		Evaluation_Transferable transferables = (Evaluation_Transferable) evaluation.getTransferable();
		try {
			evaluation.updateFromHeaderTransferable(this.server.receiveEvaluation(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluation | receiveEvaluations";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTest(Test test, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Test_Transferable transferables = (Test_Transferable) test.getTransferable();
		try {
			test.updateFromHeaderTransferable(this.server.receiveTest(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTest | receiveTests";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveResult(Result result, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Result_Transferable transferables = (Result_Transferable) result.getTransferable();
		try {
			result.updateFromHeaderTransferable(this.server.receiveResult(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveResult | receiveResults";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TemporalPattern_Transferable transferables = (TemporalPattern_Transferable) temporalPattern.getTransferable();
		try {
			temporalPattern.updateFromHeaderTransferable(this.server.receiveTemporalPattern(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTemporalPattern | receiveTemporalPatterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementTypes(Collection measurementTypes, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[measurementTypes.size()];
		int i = 0;
		for (Iterator it = measurementTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementType_Transferable) ((MeasurementType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(measurementTypes, this.server.receiveMeasurementTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementType | receiveMeasurementTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);

		}
	}

	public void saveAnalysisTypes(Collection analysisTypes, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[analysisTypes.size()];
		int i = 0;
		for (Iterator it = analysisTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (AnalysisType_Transferable) ((AnalysisType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(analysisTypes, this.server.receiveAnalysisTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysisTypes | receiveAnalysisTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluationTypes(Collection evaluationTypes, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[evaluationTypes.size()];
		int i = 0;
		for (Iterator it = evaluationTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (EvaluationType_Transferable) ((EvaluationType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(evaluationTypes, this.server.receiveEvaluationTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluationType | receiveEvaluationTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveSets(Collection sets, boolean force) throws DatabaseException, CommunicationException,
			VersionCollisionException {
		Set_Transferable[] transferables = new Set_Transferable[sets.size()];
		int i = 0;
		for (Iterator it = sets.iterator(); it.hasNext(); i++) {
			transferables[i] = (Set_Transferable) ((Set) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(sets, this.server.receiveSets(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveSets | receiveSets";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModelings(Collection modelings, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Modeling_Transferable[] transferables = new Modeling_Transferable[modelings.size()];
		int i = 0;
		for (Iterator it = modelings.iterator(); it.hasNext(); i++) {
			transferables[i] = (Modeling_Transferable) ((Modeling) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(modelings, this.server.receiveModelings(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientModelingObjectLoader.saveModelings | receiveModelings";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveModelingTypes(Collection modelingTypes, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		ModelingType_Transferable[] transferables = new ModelingType_Transferable[modelingTypes.size()];
		int i = 0;
		for (Iterator it = modelingTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (ModelingType_Transferable) ((ModelingType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(modelingTypes, this.server.receiveModelingTypes(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientModelingTypeObjectLoader.saveModelingTypes | receiveModelingTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurementSetups(Collection measurementSetups, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[measurementSetups.size()];
		int i = 0;
		for (Iterator it = measurementSetups.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementSetup_Transferable) ((MeasurementSetup) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(measurementSetups, this.server.receiveMeasurementSetups(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementSetupObjectLoader.saveMeasurementSetups | receiveMeasurementSetups";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMeasurements(Collection measurements, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Measurement_Transferable[] transferables = new Measurement_Transferable[measurements.size()];
		int i = 0;
		for (Iterator it = measurements.iterator(); it.hasNext(); i++) {
			transferables[i] = (Measurement_Transferable) ((Measurement) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(measurements, this.server.receiveMeasurements(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurements | receiveMeasurements";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveAnalyses(Collection analyses, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Analysis_Transferable[] transferables = new Analysis_Transferable[analyses.size()];
		int i = 0;
		for (Iterator it = analyses.iterator(); it.hasNext(); i++) {
			transferables[i] = (Analysis_Transferable) ((Analysis) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(analyses, this.server.receiveAnalyses(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.receiveAnalyses | receiveAnalyses";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveEvaluations(Collection evaluations, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Evaluation_Transferable[] transferables = new Evaluation_Transferable[evaluations.size()];
		int i = 0;
		for (Iterator it = evaluations.iterator(); it.hasNext(); i++) {
			transferables[i] = (Evaluation_Transferable) ((Evaluation) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(evaluations, this.server.receiveEvaluations(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluations | receiveEvaluations";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTests(Collection tests, boolean force) throws DatabaseException, CommunicationException,
			VersionCollisionException {
		Test_Transferable[] transferables = new Test_Transferable[tests.size()];
		int i = 0;
		for (Iterator it = tests.iterator(); it.hasNext(); i++) {
			transferables[i] = (Test_Transferable) ((Test) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(tests, this.server.receiveTests(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTests | receiveTests";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveResults(Collection results, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Result_Transferable[] transferables = new Result_Transferable[results.size()];
		int i = 0;
		for (Iterator it = results.iterator(); it.hasNext(); i++) {
			transferables[i] = (Result_Transferable) ((Result) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(results, this.server.receiveResults(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientResultObjectLoader.saveResults | receiveResults";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveTemporalPatterns(Collection temporalPatterns, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[temporalPatterns.size()];
		int i = 0;
		for (Iterator it = temporalPatterns.iterator(); it.hasNext(); i++) {
			transferables[i] = (TemporalPattern_Transferable) ((TemporalPattern) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(temporalPatterns, this.server.receiveTemporalPatterns(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientTemporalPaternObjectLoader.saveTemporalPaterns | receiveTemporalPaterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

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
				getAccessIdentifierTransferable());

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}
}
