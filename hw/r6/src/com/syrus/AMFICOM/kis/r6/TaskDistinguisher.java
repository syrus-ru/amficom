package com.syrus.AMFICOM.kis.r6;

import java.util.Hashtable;
import com.syrus.util.ByteArray;
import com.syrus.AMFICOM.OperationEvent;
import com.gnnettest.questfiber.work.WorkManager;
import com.gnnettest.questfiber.work.WorkRequest;
import com.gnnettest.questfiber.work.Characterization;
import com.gnnettest.questfiber.work.OTDRAcquisitionConstraints;
import com.gnnettest.questfiber.work.OTDRAnalysisConstraints;
import com.gnnettest.questfiber.work.OTDRValidationConstraints;
import com.gnnettest.questfiber.util.Identity;
import com.syrus.AMFICOM.kis.Task;

public class TaskDistinguisher {
  private String measurement_id;
  private byte taskType;
  private String monitoredElementIdentityStr;

  private int wvlen;
  private double trclen;
  private double res;
  private long pulswd;
  private double ior;
  private double scans;

  private double eventLossThreshold;
  private double endDetectThreshold;
  private double reflectanceThreshold;
  private double noiseFloorAdjustment;

  private String characterizationIdentityStr;
  private byte ifGetRawData;

  public TaskDistinguisher(Task task) {
    this.measurement_id = task.getMeasurementId();
    String str = task.getMeasurementTypeId();
    if(str.indexOf("trace_get_trace") >= 0)
      this.taskType = 0;
    else
      if(str.indexOf("trace_simple") >= 0)
        this.taskType = 1;
      else
        if(str.indexOf("reflectometry") >= 0 || str.indexOf("trace_and_analyse") >= 0)
          this.taskType = 2;
        else
          System.out.println("Unknown type id: " + str + " of measurement: " + this.measurement_id);
    this.monitoredElementIdentityStr = task.getLocalAddress();
    Hashtable parameters = task.getParameters();
    this.regardTaskType(parameters);
  }

  private void regardTaskType(Hashtable parameters) {
    switch (this.taskType) {
    case 0:
      this.setQueryPars(parameters);
      this.printPars();
      if(this.characterizationIdentityStr != null)
        Session.dispatcher.notify(new QueryEvent(this.measurement_id, this.monitoredElementIdentityStr, this.characterizationIdentityStr));
      break;
    case 1:
    case 2:
    case 3:
      this.setWorkPars(parameters);
      this.printPars();

      OTDRAcquisitionConstraints acqConstr = null;
      OTDRAnalysisConstraints anzConstr = null;
      OTDRValidationConstraints valConstr = null;

      if (this.monitoredElementIdentityStr != null) {
        if (this.wvlen * this.trclen * this.res * this.pulswd * this.ior * this.scans > 0) {
          try {
            acqConstr = new OTDRAcquisitionConstraints();
          }
          catch (Exception e) {
            System.out.println("TaskDistinguisher.regardTaskType | Failed constructor: new OTDRAcquisitionConstraints()");
            System.out.println(e.getMessage());
          }
          acqConstr.setAttributes(this.wvlen, this.trclen, this.res, this.pulswd, this.ior, this.scans);
          //acqConstr.setAttributes(1550, 65.536, 4.0, 1000, 1.467, 16000.0);
        }
        else {
          System.out.println("TaskDistinguisher.regardTaskType | One o more parameters of AcquisitionConstraints aren't set correctly, will try to use defaults");
        }
        if (this.endDetectThreshold * this.reflectanceThreshold >0 && this.eventLossThreshold >= 0 && this.noiseFloorAdjustment >= -10) {
          try {
            anzConstr = new OTDRAnalysisConstraints(this.eventLossThreshold, this.endDetectThreshold, this.reflectanceThreshold, this.noiseFloorAdjustment);
          }
          catch (Exception e) {
            System.out.println("TaskDistinguisher.regardTaskType | Failed constructor: new OTDRAnalysisConstraints()");
            System.out.println(e.getMessage());
          }
        }
        else {
          System.out.println("TaskDistinguisher.regardTaskType | One o more parameters of AnalysisConstraints aren't set correctly, will try to    use defaults");
        }
//************ ValidationConstraints -- return later! **********************
				boolean submitted = false;
				while (! submitted) {
	        try {
		        WorkRequestSubmitter wrSubmitter = new WorkRequestSubmitter(this.measurement_id, this.taskType, this.monitoredElementIdentityStr, acqConstr, anzConstr, valConstr);
						submitted = true;
			    }
					catch (java.rmi.NoSuchObjectException nsoe) {
						System.out.println("While submitting: " + nsoe.getMessage() + "; restarting");
						Session.dispatcher.notify(new OperationEvent(new Object(), 1, "RTUDisconnected"));
						submitted = false;
						try {
							Thread.currentThread().sleep(5000);
						}
						catch (InterruptedException ie) {}
					}
			    catch (Exception e) {
				    System.out.println(e.getMessage());
					  e.printStackTrace();
						submitted = false;
						try {
							Thread.currentThread().sleep(5000);
						}
						catch (InterruptedException ie) {}
	        }
				}
      }
      else{
        System.out.println("TaskDistinguisher.regardTaskType | MonitoredElement for taskType = " + this.taskType + "not defined. Can't submit work request");
      }

      break;
//**************** Other types of tasks - return later! ***********************
    default:
      System.out.println("TaskDistinguisher.regardTaskType | Unknown type of task: taskType = " + this.taskType);
    }
  }



  private void setWorkPars(Hashtable parameters) {
    ByteArray bAr;
//wvlen - int
    this.wvlen = -1;
    try {
      bAr = new ByteArray((byte[])parameters.get("ref_wvlen"));
      this.wvlen = (short)bAr.toInt();
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'wvlen' from Agent");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
//trclen - double
    this.trclen = -1;
    try {
      bAr = new ByteArray((byte[])parameters.get("ref_trclen"));
      this.trclen = bAr.toDouble() / 1000;
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'trclen' from Agent\n");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

//res - double
    this.res = -1;
    try{
      bAr = new ByteArray((byte[])parameters.get("ref_res"));
      this.res = bAr.toDouble();
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'res' from Agent");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

//pulswd - long
    this.pulswd = -1;
    try{
      bAr = new ByteArray((byte[])parameters.get("ref_pulswd"));
      this.pulswd = bAr.toLong();
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'pulswd' from Agent");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

//ior - double
    this.ior = -1;
    try{
      bAr = new ByteArray((byte[])parameters.get("ref_ior"));
      /*for (int i = 0; i < bAr.getLength(); i++)
        System.out.println("bAr[" + i + "] == " + bAr.getBytes()[i]);*/
      this.ior = bAr.toDouble();
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'ior' from Agent");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

//scans - double
    this.scans = -1;
    try{
      bAr = new ByteArray((byte[])parameters.get("ref_scans"));
      this.scans = bAr.toDouble();
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'scans' from Agent");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

//eventLossThreshold - double
    this.eventLossThreshold = 0;
    
//endDetectThreshold - double
    this.endDetectThreshold = 0;
      
//reflectanceThreshold - double
    this.reflectanceThreshold = 0;
      
//noiseFloorAdjustment - double
    this.noiseFloorAdjustment = 0;
      
//************ ValidationConstraints -- return later! ***********************
  }

  private void setQueryPars(Hashtable parameters) {
    ByteArray bAr;
//characterizationIdentityStr - String
    this.characterizationIdentityStr = null;
    try {
      bAr = new ByteArray((byte[])parameters.get("ref_characterizationidentity"));
      this.characterizationIdentityStr = bAr.toUTFString();
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'characterizationIdentityStr' from Agent");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
/*
//ifGetRawData - byte;    
    this.ifGetRawData = 0;
    try {
      bAr = new ByteArray((byte[])parameters.get("ref_ifgetrawdata"));
      this.ifGetRawData = bAr.getBytes()[0];
      //this.ifGetRawData = 0;
    }
    catch (Exception e) {
      System.out.println("TaskDistinguisher.regardTaskType | Failed to obtain parameter 'ifGetRawData' from Agent, using default value: 0");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }*/
  }

  private void printPars() {
    System.out.println("\nTaskDistinguisher | Parameters of task, received from Agent:");
    System.out.println("taskType = " + this.taskType);
    System.out.println("monitoredElementIdentityStr = " + this.monitoredElementIdentityStr);
    System.out.println("wvlen = " + this.wvlen);
    System.out.println("trclen = " + this.trclen);
    System.out.println("res = " + this.res);
    System.out.println("pulswd = " + this.pulswd);
    System.out.println("ior = " + this.ior);
    System.out.println("scans = " + this.scans);
    System.out.println("eventLossThreshold = " + this.eventLossThreshold);
    System.out.println("endDetectThreshold = " + this.endDetectThreshold);
    System.out.println("reflectanceThreshold = " + this.reflectanceThreshold);
    System.out.println("noiseFloorAdjustment = " + this.noiseFloorAdjustment);
    System.out.println("characterizationIdentityStr = " + this.characterizationIdentityStr);
    System.out.println("ifGetRawData = " + this.ifGetRawData);
    System.out.println("\n");
  }
}