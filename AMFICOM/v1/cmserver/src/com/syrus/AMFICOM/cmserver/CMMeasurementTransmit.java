/*
 * $Id: CMMeasurementTransmit.java,v 1.2 2005/01/28 12:16:25 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.measurement.ActionCondition;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.TemporalCondition;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.ActionCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/28 12:16:25 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMMeasurementTransmit extends CMConfigurationTransmit {

	private static final long serialVersionUID = 3410028455480782250L;

	public AnalysisType_Transferable transmitAnalysisType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysisType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			AnalysisType analysisType = (AnalysisType) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (AnalysisType_Transferable) analysisType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Evaluation_Transferable transmitEvaluation(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluation | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Evaluation evaluation = (Evaluation) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Evaluation_Transferable) evaluation.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Evaluation_Transferable[] transmitEvaluations(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitEvaluations | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.EVALUATION_ENTITY_CODE), true);

			Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Evaluation evaluation = (Evaluation) it.next();
				transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Evaluation_Transferable[] transmitEvaluationsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitEvaluationsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
								new DomainCondition(domain, ObjectEntities.EVALUATION_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.EVALUATION_ENTITY_CODE), true);

			Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Evaluation evaluation = (Evaluation) it.next();
				transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			DomainCondition_Transferable domainCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationsButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new DomainCondition(domainCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable),
						true);

			Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Evaluation evaluation = (Evaluation) it.next();
				transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public EvaluationType_Transferable transmitEvaluationType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			EvaluationType evaluationType = (EvaluationType) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (EvaluationType_Transferable) evaluationType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementType_Transferable transmitMeasurementType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (MeasurementType_Transferable) measurementType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}




	public Analysis_Transferable transmitAnalysis(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysis | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Analysis analysis = (Analysis) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Analysis_Transferable) analysis.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Modeling_Transferable transmitModeling(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitModeling | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Modeling modeling = (Modeling) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Modeling_Transferable) modeling.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Measurement_Transferable transmitMeasurement(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurement | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Measurement measurement = (Measurement) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Measurement_Transferable) measurement.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementSetup_Transferable transmitMeasurementSetup(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetup | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Result_Transferable transmitResult(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitResult | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Result result = (Result) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Result_Transferable) result.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Set_Transferable transmitSet(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitSet | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Set set = (Set) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Set_Transferable) set.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TemporalPattern_Transferable transmitTemporalPattern(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitTest | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			TemporalPattern temporalPattern = (TemporalPattern) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (TemporalPattern_Transferable) temporalPattern.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Test_Transferable transmitTest(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitTest | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Test test = (Test) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Test_Transferable) test.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}




	public AnalysisType_Transferable[] transmitAnalysisTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitAnalysisTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.ANALYSISTYPE_ENTITY_CODE), true);

			AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				AnalysisType analysisType = (AnalysisType) it.next();
				transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}

	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitAnalysisTypesButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.ANALYSISTYPE_ENTITY_CODE), true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.ANALYSISTYPE_ENTITY_CODE), true);

			AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				AnalysisType analysisType = (AnalysisType) it.next();
				transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysisTypesButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				AnalysisType analysisType = (AnalysisType) it.next();
				transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitEvaluationTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.EVALUATIONTYPE_ENTITY_CODE), true);

			EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				EvaluationType evaluationType = (EvaluationType) it.next();
				transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}

	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitEvaluationTypesButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.EVALUATIONTYPE_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.EVALUATIONTYPE_ENTITY_CODE), true);

			EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				EvaluationType evaluationType = (EvaluationType) it.next();
				transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationTypesButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				EvaluationType evaluationType = (EvaluationType) it.next();
				transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementType_Transferable[] transmitMeasurementTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

			MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementType measurementType = (MeasurementType) it.next();
				transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementType_Transferable[] transmitMeasurementTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementTypesButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

			MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementType measurementType = (MeasurementType) it.next();
				transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsLinkedIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementTypesButIdsLinkedIdsCondition | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) ", Log.DEBUGLEVEL07);
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementType measurementType = (MeasurementType) it.next();
				transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsStringFieldCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StringFieldCondition_Transferable stringFieldCondition_Transferable) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementTypesButIdsStringFieldCondition | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) ", Log.DEBUGLEVEL07);
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new StringFieldCondition(stringFieldCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new StringFieldCondition(stringFieldCondition_Transferable),
						true);

			MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementType measurementType = (MeasurementType) it.next();
				transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}




	public Analysis_Transferable[] transmitAnalyses(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitAnalyses | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.ANALYSIS_ENTITY_CODE), true);

			Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Analysis analysis = (Analysis) it.next();
				transferables[i] = (Analysis_Transferable) analysis.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Analysis_Transferable[] transmitAnalysesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitAnalysesButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.ANALYSIS_ENTITY_CODE), true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.ANALYSIS_ENTITY_CODE), true);

			Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Analysis analysis = (Analysis) it.next();
				transferables[i] = (Analysis_Transferable) analysis.getTransferable();
			}
			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Analysis_Transferable[] transmitAnalysesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			DomainCondition_Transferable domainCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysesButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new DomainCondition(domainCondition_Transferable),
						true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable),
						true);

			Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Analysis analysis = (Analysis) it.next();
				transferables[i] = (Analysis_Transferable) analysis.getTransferable();
			}
			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Modeling_Transferable[] transmitModelings(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitModelings | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MODELING_ENTITY_CODE), true);

			Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Modeling modeling = (Modeling) it.next();
				transferables[i] = (Modeling_Transferable) modeling.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}

	}

	public Modeling_Transferable[] transmitModelingsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitModelingsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.MODELING_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MODELING_ENTITY_CODE), true);

			Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Modeling modeling = (Modeling) it.next();
				transferables[i] = (Modeling_Transferable) modeling.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Modeling_Transferable[] transmitModelingsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			DomainCondition_Transferable domainCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitModelingsButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new DomainCondition(domainCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable),
						true);

			Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Modeling modeling = (Modeling) it.next();
				transferables[i] = (Modeling_Transferable) modeling.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Measurement_Transferable[] transmitMeasurements(Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurements | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

			Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Measurement measurement = (Measurement) it.next();
				transferables[i] = (Measurement_Transferable) measurement.getTransferable();
			}
			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Measurement_Transferable[] transmitMeasurementsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

			Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Measurement measurement = (Measurement) it.next();
				transferables[i] = (Measurement_Transferable) measurement.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Measurement_Transferable[] transmitMeasurementsButIdsLinkedCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementsButIdsLinkedCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Measurement measurement = (Measurement) it.next();
				transferables[i] = (Measurement_Transferable) measurement.getTransferable();
			}

			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementsButIdsLinkedCondition | transferables size "
					+ transferables.length
					+ ", count:"
					+ i, Log.DEBUGLEVEL07);

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Measurement_Transferable[] transmitMeasurementsButIdsDomainCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			DomainCondition_Transferable domainCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementsButIdsDomainCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new DomainCondition(domainCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable),
						true);

			Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Measurement measurement = (Measurement) it.next();
				transferables[i] = (Measurement_Transferable) measurement.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetups | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MS_ENTITY_CODE), true);

			MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
				transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
			}
			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetupsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.MS_ENTITY_CODE), true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.MS_ENTITY_CODE), true);

			MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
				transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
			}
			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsLinkedCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetupsButIdsMeasurementLinkedCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);
			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
				transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
			}
			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Result_Transferable[] transmitResults(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitResults | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.RESULT_ENTITY_CODE), true);

			Result_Transferable[] transferables = new Result_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Result result = (Result) it.next();
				transferables[i] = (Result_Transferable) result.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Result_Transferable[] transmitResultsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitResultsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.RESULT_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.RESULT_ENTITY_CODE), true);

			Result_Transferable[] transferables = new Result_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Result result = (Result) it.next();
				transferables[i] = (Result_Transferable) result.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Result_Transferable[] transmitResultsButIdsDomainCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			DomainCondition_Transferable domainCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitResultsButIdsDomainCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new DomainCondition(domainCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable),
						true);

			Result_Transferable[] transferables = new Result_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Result result = (Result) it.next();
				transferables[i] = (Result_Transferable) result.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Result_Transferable[] transmitResultsButIdsLinkedCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			LinkedIdsCondition_Transferable linkedIdsCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitResultsButIdsLinkedCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable),
						true);

			Result_Transferable[] transferables = new Result_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Result result = (Result) it.next();
				transferables[i] = (Result_Transferable) result.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.cmserver.corba.CMServerOperations#transmitResultsButIdsActionCondition(com.syrus.AMFICOM.general.corba.Identifier_Transferable[], com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable, com.syrus.AMFICOM.measurement.corba.ActionCondition_Transferable)
	 */
	public Result_Transferable[] transmitResultsButIdsActionCondition(Identifier_Transferable[] ids,
			AccessIdentifier_Transferable accessIdentifier,
			ActionCondition_Transferable actionCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitResultsButIdsActionCondition | requiere "
			+ (ids.length == 0 ? "all" : Integer.toString(ids.length))
			+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (ids.length > 0) {
				List idsList = new ArrayList(ids.length);
				for (int i = 0; i < ids.length; i++)
					idsList.add(new Identifier(ids[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new ActionCondition(actionCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new ActionCondition(actionCondition_Transferable),
						true);

			Result_Transferable[] transferables = new Result_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Result result = (Result) it.next();
				transferables[i] = (Result_Transferable) result.getTransferable();
			}

			return transferables;
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

//	public Result_Transferable[] transmitResultsButIdsResultCondition(Identifier_Transferable[] identifier_Transferables,
//			AccessIdentifier_Transferable accessIdentifier,
//			ResultCondition_Transferable resultCondition_Transferable) throws AMFICOMRemoteException {
//		Log.debugMessage("CMMeasurementTransmit.transmitResultsButIdsLinkedResultCondition | requiere "
//				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
//				+ " item(s) ", Log.DEBUGLEVEL07);
//		try {
//			List list;
//			if (identifier_Transferables.length > 0) {
//				List idsList = new ArrayList(identifier_Transferables.length);
//				for (int i = 0; i < identifier_Transferables.length; i++)
//					idsList.add(new Identifier(identifier_Transferables[i]));
//
//				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
//						new ResultCondition(resultCondition_Transferable),
//						true);
//
//			}
//			else
//				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new ResultCondition(resultCondition_Transferable),
//						true);
//
//			Result_Transferable[] transferables = new Result_Transferable[list.size()];
//			int i = 0;
//			for (Iterator it = list.iterator(); it.hasNext(); i++) {
//				Result result = (Result) it.next();
//				transferables[i] = (Result_Transferable) result.getTransferable();
//			}
//
//			return transferables;
//
//		}
//		catch (RetrieveObjectException roe) {
//			Log.errorException(roe);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
//		}
//		catch (IllegalDataException ide) {
//			Log.errorException(ide);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
//		}
//		catch (IllegalObjectEntityException ioee) {
//			Log.errorException(ioee);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
//		}
//		catch (ApplicationException e) {
//			Log.errorException(e);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
//		}
//		catch (Throwable t) {
//			Log.errorException(t);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
//		}
//	}
//
//	public Result_Transferable[] transmitResultsButIdsResultSortCondition(Identifier_Transferable[] identifier_Transferables,
//			AccessIdentifier_Transferable accessIdentifier,
//			ResultSortCondition_Transferable resultSortCondition_Transferable) throws AMFICOMRemoteException {
//		Log.debugMessage("CMMeasurementTransmit.transmitResultsButIdsLinkedResultSortCondition | requiere "
//				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
//				+ " item(s) ", Log.DEBUGLEVEL07);
//		try {
//			List list;
//			if (identifier_Transferables.length > 0) {
//				List idsList = new ArrayList(identifier_Transferables.length);
//				for (int i = 0; i < identifier_Transferables.length; i++)
//					idsList.add(new Identifier(identifier_Transferables[i]));
//
//				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
//						new ResultSortCondition(resultSortCondition_Transferable),
//						true);
//
//			}
//			else
//				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new ResultSortCondition(resultSortCondition_Transferable),
//						true);
//
//			Result_Transferable[] transferables = new Result_Transferable[list.size()];
//			int i = 0;
//			for (Iterator it = list.iterator(); it.hasNext(); i++) {
//				Result result = (Result) it.next();
//				transferables[i] = (Result_Transferable) result.getTransferable();
//			}
//
//			return transferables;
//
//		}
//		catch (RetrieveObjectException roe) {
//			Log.errorException(roe);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
//		}
//		catch (IllegalDataException ide) {
//			Log.errorException(ide);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
//		}
//		catch (IllegalObjectEntityException ioee) {
//			Log.errorException(ioee);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
//		}
//		catch (ApplicationException e) {
//			Log.errorException(e);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
//		}
//		catch (Throwable t) {
//			Log.errorException(t);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
//		}
//	}

	public Set_Transferable[] transmitSets(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitSets | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.SET_ENTITY_CODE), true);

			Set_Transferable[] transferables = new Set_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Set set = (Set) it.next();
				transferables[i] = (Set_Transferable) set.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Set_Transferable[] transmitSetsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitSetsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.SET_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.SET_ENTITY_CODE), true);

			Set_Transferable[] transferables = new Set_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Set set = (Set) it.next();
				transferables[i] = (Set_Transferable) set.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Set_Transferable[] transmitSetsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			DomainCondition_Transferable domainCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitSetsButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new DomainCondition(domainCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable),
						true);

			Set_Transferable[] transferables = new Set_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Set set = (Set) it.next();
				transferables[i] = (Set_Transferable) set.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TemporalPattern_Transferable[] transmitTemporalPatterns(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitTemporalPatterns | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

			TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				TemporalPattern temporalPattern = (TemporalPattern) it.next();
				transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TemporalPattern_Transferable[] transmitTemporalPatternsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitTemporalPatternsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

			TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				TemporalPattern temporalPattern = (TemporalPattern) it.next();
				transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Test_Transferable[] transmitTests(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitTests | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.TEST_ENTITY_CODE), true);

			Test_Transferable[] transferables = new Test_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Test test = (Test) it.next();
				transferables[i] = (Test_Transferable) test.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Test_Transferable[] transmitTestsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMMeasurementTransmit.transmitTestsButIds | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
					+ " item(s) in domain: "
					+ domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domain,
						ObjectEntities.TEST_ENTITY_CODE), true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domain,
						ObjectEntities.TEST_ENTITY_CODE), true);

			Test_Transferable[] transferables = new Test_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Test test = (Test) it.next();
				transferables[i] = (Test_Transferable) test.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Test_Transferable[] transmitTestsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			TemporalCondition_Transferable temporalCondition_Transferable) throws AMFICOMRemoteException {
		Log.debugMessage("CMMeasurementTransmit.transmitTestsButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
						new TemporalCondition(temporalCondition_Transferable),
						true);

			}
			else
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new TemporalCondition(temporalCondition_Transferable),
						true);

			Test_Transferable[] transferables = new Test_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Test test = (Test) it.next();
				transferables[i] = (Test_Transferable) test.getTransferable();
			}

			return transferables;

		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}




  //  Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedMeasurementObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		try {
			Map storableObjectMap = new HashMap();
			for (int i = 0; i < storableObjects_Transferables.length; i++) {
				storableObjectMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);
			}
			MeasurementStorableObjectPool.refresh();
			List storableObjects = MeasurementStorableObjectPool.getStorableObjects(new ArrayList(storableObjectMap.keySet()),
					true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject so = (StorableObject) it.next();
				StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectMap.get(so.getId());
				//  Checking for confomity
				Identifier sotCreatorId = new Identifier(sot.creator_id);
				Identifier sotModifierId = new Identifier(sot.modifier_id);
				if ((Math.abs(sot.created - so.getCreated().getTime()) < 1000)
						&& (Math.abs(sot.modified - so.getModified().getTime()) < 1000)
						&& sotCreatorId.equals(so.getCreatorId())
						&& sotModifierId.equals(so.getModifierId())) {
					it.remove();
				}
			}
			int i = 0;
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[storableObjects.size()];
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
				StorableObject so = (StorableObject) it.next();
				identifierTransferables[i] = (Identifier_Transferable) so.getId().getTransferable();
			}
			return identifierTransferables;
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
}
