/*
 * $Id: ClientMeasurementObjectLoader.java,v 1.14 2004/10/04 05:40:11 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;

/**
 * @version $Revision: 1.14 $, $Date: 2004/10/04 05:40:11 $
 * @author $Author: max $
 * @module cmserver_v1
 */

public final class ClientMeasurementObjectLoader implements MeasurementObjectLoader {

	private CMServer				server;

	private static AccessIdentifier_Transferable	accessIdentifierTransferable;

	public ClientMeasurementObjectLoader(CMServer server) {
		this.server = server;
	}

	public static void setAccessIdentifierTransferable(AccessIdentifier_Transferable accessIdentifier_Transferable) {
		accessIdentifierTransferable = accessIdentifier_Transferable;
	}

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new ParameterType(this.server.transmitParameterType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.transmitParameterType | new ParameterType("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.transmitParameterType | server.transmitParameterType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public MeasurementType loadMeasurementType(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		try {
			return new MeasurementType(this.server.transmitMeasurementType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementType | new MeasurementType("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementType | server.transmitMeasurementType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new AnalysisType(this.server.transmitAnalysisType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysisType | new AnalysisType("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysisType | server.transmitAnalysisType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public EvaluationType loadEvaluationType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new EvaluationType(this.server.transmitEvaluationType((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadEvaluationType | new EvaluationType("
					+ id.toString() + ")";
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
			String msg = "ClientMeasurementObjectLoader.loadSet | server.transmitSet(" + id.toString()
					+ ")";
			throw new CommunicationException(msg, e);
		}
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		try {
			return new MeasurementSetup(this.server.transmitMeasurementSetup((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementSetup | new MeasurementSetup("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementSetup | server.transmitMeasurementSetup("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}
	
	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		try {
			return new Modeling(this.server.transmitModeling((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | new Measurement(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | server.transmitMeasurement("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}
	
	public Measurement loadMeasurement(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Measurement(this.server.transmitMeasurement((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | new Measurement(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | server.transmitMeasurement("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException {
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
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

	public Result loadResult(Identifier id) throws RetrieveObjectException,
	CommunicationException {
		try {
			return new Result(this.server.transmitResult((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadResult | new Result("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadResult | server.transmitResult("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		try {
			return new TemporalPattern(this.server.transmitTemporalPattern((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadTemporalPattern | new TemporalPattern("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadTemporalPattern | server.transmitTemporalPattern("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Analysis_Transferable[] transferables = this.server
                    .transmitAnalyses(identifier_Transferables, accessIdentifierTransferable);
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
    
    public List loadAnalysesButIds(List ids) throws DatabaseException,
            CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Analysis_Transferable[] transferables = this.server
                    .transmitAnalysesButIds(identifier_Transferables, accessIdentifierTransferable);
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
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AnalysisType_Transferable[] transferables = this.server
					.transmitAnalysisTypes(identifier_Transferables, accessIdentifierTransferable);
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
    
    public List loadAnalysisTypesButIds(List ids) throws DatabaseException,
            CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            AnalysisType_Transferable[] transferables = this.server
                    .transmitAnalysisTypesButIds(identifier_Transferables, accessIdentifierTransferable);
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
		/**
		 * FIXME method is not complete !
		 */
		throw new UnsupportedOperationException();
	}
    
    public List loadEvaluationsButIds(List ids) throws DatabaseException, CommunicationException {
        /**
         * FIXME method is not complete !
         */
        throw new UnsupportedOperationException();
    }

	public List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EvaluationType_Transferable[] transferables = this.server
					.transmitEvaluationTypes(identifier_Transferables, accessIdentifierTransferable);
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
    
    public List loadEvaluationTypesButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            EvaluationType_Transferable[] transferables = this.server
                    .transmitEvaluationTypesButIds(identifier_Transferables, accessIdentifierTransferable);
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
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Measurement_Transferable[] transferables = this.server
					.transmitMeasurements(identifier_Transferables, accessIdentifierTransferable);
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
    
    public List loadMeasurementsButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Measurement_Transferable[] transferables = this.server
                    .transmitMeasurementsButIds(identifier_Transferables, accessIdentifierTransferable);
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
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Modeling_Transferable[] transferables = this.server
					.transmitModelings(identifier_Transferables,
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
    
    public List loadModelingsButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Modeling_Transferable[] transferables = this.server
                    .transmitModelingsButIds(identifier_Transferables,
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
	
	public List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementSetup_Transferable[] transferables = this.server
					.transmitMeasurementSetups(identifier_Transferables,
									accessIdentifierTransferable);
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
    
    public List loadMeasurementSetupsButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementSetup_Transferable[] transferables = this.server
                    .transmitMeasurementSetupsButIds(identifier_Transferables,
                                    accessIdentifierTransferable);
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

	public List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementType_Transferable[] transferables = this.server
					.transmitMeasurementTypes(identifier_Transferables,
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
    
    public List loadMeasurementTypesButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            MeasurementType_Transferable[] transferables = this.server
                    .transmitMeasurementTypesButIds(identifier_Transferables,
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

	public List loadParameterTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			ParameterType_Transferable[] transferables = this.server
					.transmitParameterTypes(identifier_Transferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new ParameterType(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}
    
    public List loadParameterTypesButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            ParameterType_Transferable[] transferables = this.server
                    .transmitParameterTypesButIds(identifier_Transferables, accessIdentifierTransferable);
            List list = new ArrayList(transferables.length);
            for (int j = 0; j < transferables.length; j++) {
                list.add(new ParameterType(transferables[j]));
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
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Result_Transferable[] transferables = this.server.transmitResults(identifier_Transferables,
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
    
    public List loadResultsButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Result_Transferable[] transferables = this.server.transmitResultsButIds(identifier_Transferables,
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

	public List loadSets(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Set_Transferable[] transferables = this.server.transmitSets(identifier_Transferables,
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
    
    public List loadSetsButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Set_Transferable[] transferables = this.server.transmitSetsButIds(identifier_Transferables,
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

	public List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			TemporalPattern_Transferable[] transferables = this.server
					.transmitTemporalPatterns(identifier_Transferables,
									accessIdentifierTransferable);
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
    
    public List loadTemporalPatternsButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            TemporalPattern_Transferable[] transferables = this.server
                    .transmitTemporalPatternsButIds(identifier_Transferables,
                                    accessIdentifierTransferable);
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
			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Test_Transferable[] transferables = this.server.transmitTests(identifier_Transferables,
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
    
    public List loadTestsButIds(List ids) throws DatabaseException, CommunicationException {
        try {
            Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
            int i = 0;
            for (Iterator it = ids.iterator(); it.hasNext(); i++) {
                Identifier id = (Identifier) it.next();
                identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
            }
            Test_Transferable[] transferables = this.server.transmitTestsButIds(identifier_Transferables,
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
    
    public void saveMeasurementType(MeasurementType measurementType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub        
    }

     public void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
                  
         AnalysisType_Transferable transferables = (AnalysisType_Transferable)analysisType.getTransferable();                        
         
         try {
             this.server.receiveAnalysisType(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientMeasurementObjectLoader.saveAnalysisType | receiveAnalysisTypes";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
    }

     public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveSet(Set set, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveTest(Test test, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveResult(Result result, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
//    TODO auto generated stub
     }

     public void saveParameterTypes(List parameterTypes, boolean force) throws DatabaseException, CommunicationException, VersionCollisionException{
         ParameterType_Transferable[] transferables = new ParameterType_Transferable[parameterTypes.size()];
         int i=0;
         for (Iterator it = parameterTypes.iterator(); it.hasNext();i++) {
             transferables[i] = (ParameterType_Transferable)( (ParameterType)it.next() ).getTransferable();                        
         }
         try {
             this.server.receiveParameterTypes(transferables, force, accessIdentifierTransferable);         
         } catch (AMFICOMRemoteException e) {
             String msg = "ClientMeasurementObjectLoader.saveParameterTypes | receiveParameterTypes";
             
             if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
                 throw new VersionCollisionException(msg, e);
             
             throw new CommunicationException(msg, e);       
         }
     }
     
     public void saveMeasurementTypes(List measurementTypes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
         MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[measurementTypes.size()];
         int i=0;
         for (Iterator it = measurementTypes.iterator(); it.hasNext();i++) {
             transferables[i] = (MeasurementType_Transferable)( (MeasurementType)it.next() ).getTransferable();                        
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

     public void saveAnalysisTypes(List analysisTypes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[analysisTypes.size()];
         int i=0;
         for (Iterator it = analysisTypes.iterator(); it.hasNext();i++) {
             transferables[i] = (AnalysisType_Transferable)( (AnalysisType)it.next() ).getTransferable();                        
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

     public void saveEvaluationTypes(List evaluationTypes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[evaluationTypes.size()];
         int i=0;
         for (Iterator it = evaluationTypes.iterator(); it.hasNext();i++) {
             transferables[i] = (EvaluationType_Transferable)( (EvaluationType)it.next() ).getTransferable();                        
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

     public void saveSets(List sets, boolean force) throws DatabaseException, CommunicationException, VersionCollisionException{
         Set_Transferable[] transferables = new Set_Transferable[sets.size()];
         int i=0;
         for (Iterator it = sets.iterator(); it.hasNext();i++) {
             transferables[i] = (Set_Transferable)( (Set)it.next() ).getTransferable();                        
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

     public void saveModelings(List modelings, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Modeling_Transferable[] transferables = new Modeling_Transferable[modelings.size()];
         int i=0;
         for (Iterator it = modelings.iterator(); it.hasNext();i++) {
             transferables[i] = (Modeling_Transferable)( (Modeling)it.next() ).getTransferable();                        
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

     public void saveMeasurementSetups(List measurementSetups, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[measurementSetups.size()];
         int i=0;
         for (Iterator it = measurementSetups.iterator(); it.hasNext();i++) {
             transferables[i] = (MeasurementSetup_Transferable)( (MeasurementSetup)it.next() ).getTransferable();                        
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

     public void saveMeasurements(List measurements, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Measurement_Transferable[] transferables = new Measurement_Transferable[measurements.size()];
         int i=0;
         for (Iterator it = measurements.iterator(); it.hasNext();i++) {
             transferables[i] = (Measurement_Transferable)( (Measurement)it.next() ).getTransferable();                        
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

     public void saveAnalyses(List analyses, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Analysis_Transferable[] transferables = new Analysis_Transferable[analyses.size()];
         int i=0;
         for (Iterator it = analyses.iterator(); it.hasNext();i++) {
             transferables[i] = (Analysis_Transferable)( (Analysis)it.next() ).getTransferable();                        
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

     public void saveEvaluations(List evaluations, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Evaluation_Transferable[] transferables = new Evaluation_Transferable[evaluations.size()];
         int i=0;
         for (Iterator it = evaluations.iterator(); it.hasNext();i++) {
             transferables[i] = (Evaluation_Transferable)( (Evaluation)it.next() ).getTransferable();                        
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
     
     public void saveTests(List tests, boolean force) throws DatabaseException, CommunicationException, VersionCollisionException{
         Test_Transferable[] transferables = new Test_Transferable[tests.size()];
         int i=0;
         for (Iterator it = tests.iterator(); it.hasNext();i++) {
             transferables[i] = (Test_Transferable)( (Test)it.next() ).getTransferable();                        
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

     public void saveResults(List results, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         Result_Transferable[] transferables = new Result_Transferable[results.size()];
         int i=0;
         for (Iterator it = results.iterator(); it.hasNext();i++) {
             transferables[i] = (Result_Transferable)( (Result)it.next() ).getTransferable();                        
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

     public void saveTemporalPatterns(List temporalPatterns, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
         TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[temporalPatterns.size()];
         int i=0;
         for (Iterator it = temporalPatterns.iterator(); it.hasNext();i++) {
             transferables[i] = (TemporalPattern_Transferable)( (TemporalPattern)it.next() ).getTransferable();                        
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
     
    public void saveParameterType(ParameterType parameterType, boolean force)
            throws VersionCollisionException, DatabaseException, CommunicationException {
        // TODO Auto-generated method stub

    }
     


}
