/*
 * $Id: ReportReceiver.java,v 1.2 2004/06/21 14:56:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

import com.syrus.AMFICOM.CORBA.General.AlarmLevel;
import com.syrus.AMFICOM.CORBA.KIS.*;
import com.syrus.AMFICOM.server.measurement.Result;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/21 14:56:29 $
 * @author $Author: bass $
 * @module agent_v1
 */
public class ReportReceiver extends Agent {
	private final String reportFileName;
	private boolean reportFileConnected;
	private int reportFileHandle;

	public ReportReceiver(String kis_id) {
		this.reportFileHandle = 0;
		this.reportFileConnected = false;
		this.reportFileName = "reportChannel" + kis_id;
	}

  public void run() {
    String[] par_names;
    byte[][] par_values;
    String measurement_id, measurement_type_id, test_id;
		long start_time;
		TestContainer testContainer;
    Parameter_Transferable[] parameters;
    Result_Transferable result;
    while (true) {
      if(!this.reportFileConnected) {
        this.reportFileHandle = Transceiver.call(this.reportFileName);
        if(this.reportFileHandle != 0) {
          this.reportFileConnected = true;
          Log.debugMessage("Report channel connected", Log.DEBUGLEVEL05);
        }
        else {
          Log.debugMessage("Report channel NOT connected", Log.DEBUGLEVEL05);
          this.reportFileConnected = false;
          this.reportFileHandle = 0;
        }
      }//if !reportFileConnected
      else {
        if (Transceiver.read1(this.reportFileHandle, this.reportFileName)) {
					Log.debugMessage("Report is read", Log.DEBUGLEVEL03);
          try {
						par_names = Transceiver.getParameterNames();
						par_values = Transceiver.getParameterValues();
	          measurement_id = Transceiver.getMeasurementId();
						test_id = measurement_id.substring(0, measurement_id.indexOf("*"));
						start_time = Long.parseLong(measurement_id.substring(measurement_id.indexOf("*") + 1));
						synchronized (testContainers) {
							testContainer = (TestContainer)testContainers.get(test_id);
						}
						if (testContainer == null)
							throw new Exception("ReportReceiver | ERROR: Cannot find Test Container for test id: '" + test_id + "'");
						measurement_type_id = testContainer.getMeasurementTypeId();
            parameters = new Parameter_Transferable[par_names.length];
            for (int i = 0; i < par_names.length; i++)
							parameters[i] = new Parameter_Transferable(par_names[i], ParametersDatabase.getTestParameterTypeId(par_names[i], measurement_type_id), par_values[i]);
            result = new Result_Transferable(null,
                                             test_id,
																						 test_id,
                                             Result.RESULT_TYPE_TEST,
                                             start_time,
                                             parameters,
																						 AlarmLevel.ALARM_LEVEL_NONE);
//------------------------------ Stub
/*
            System.out.println("ReportReceiver | test_id == " + Transceiver.getTestId());
            System.out.println("ReportReceiver | start_time == " + Transceiver.getStartTime());
            System.out.println("ReportReceiver | parameters.length == " + parameters.length);
            for (int i = 0; i < parameters.length; i++) {
              System.out.println("ReportReceiver | parameter[" + i + "].name == " + parameters[i].name + ", parameter[" + i + "].value.length == " + parameters[i].value.length);
							if (parameters[i].name.equals("reflectogramm")) {
								com.syrus.io.BellcoreReader br = new com.syrus.io.BellcoreReader();
								com.syrus.io.BellcoreStructure bs = br.getData(parameters[i].value);
								double[] data = new double[bs.dataPts.DSF[0].length];
								for (int k = 0; k < 101; k++ ) {
									data[k] = (double)(65535 - bs.dataPts.DSF[0][k])/1000d;
									System.out.println("[" + k + "] == " + data[k]);
								}
							}
						}*/
//------------------------------ End stub
            testContainer.addResult(result);
          }
          catch (Exception e) {
            Log.errorMessage("ReportReceiver | Exception reading report");
						Log.errorException(e);
          }
        }
        else {
          Log.errorMessage("No reports or disconnected");
          this.reportFileConnected = false;
          this.reportFileHandle = 0;
        }
      }//else if !reportFileConnected

			try {
				sleep(kistimewait);
			} catch (InterruptedException ie) {
				;
			}
		}
	}
}
