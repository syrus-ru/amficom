package com.syrus.AMFICOM.kis.r6;

import com.gnnettest.questfiber.util.Identity;
import com.gnnettest.questfiber.work.OTDRAcquisitionConstraints;
import com.gnnettest.questfiber.work.OTDRAnalysisConstraints;
import com.gnnettest.questfiber.work.OTDRValidationConstraints;
import com.gnnettest.questfiber.work.Characterization;
import com.gnnettest.questfiber.work.WorkManager;
import com.gnnettest.questfiber.work.WorkRequest;
import com.gnnettest.questfiber.domain.MonitoredElement;
import com.gnnettest.questfiber.probe.Module;
import com.gnnettest.questfiber.probe.OTDRModule;
import com.gnnettest.questfiber.probe.OTAUModule;


public class WorkRequestSubmitter {
  private String amficom_measurement_id;
  private byte taskType;
  private MonitoredElement monelem;
  private OTDRAcquisitionConstraints acqConstr;
  private OTDRAnalysisConstraints anzConstr;
//************ ValidationConstraints -- return later! ***********************
  private OTDRValidationConstraints vldConstr;

  public WorkRequestSubmitter(String amficom_measurement_id,
															byte taskType,
															String monitoredElementIdentityStr,
															OTDRAcquisitionConstraints acqConstr,
															OTDRAnalysisConstraints anzConstr,
															OTDRValidationConstraints vldConstr) throws Exception {
    this.amficom_measurement_id = amficom_measurement_id;
    this.taskType = taskType;

		this.monelem = Session.dommgr.getMonitoredElement(Session.user, new Identity(monitoredElementIdentityStr));

    if (this.monelem != null) {
      Characterization basCharact;
      if (acqConstr != null) {
        this.acqConstr = acqConstr;
      }
      else {
        try {
          basCharact = this.monelem.getBaselineCharacterizations(Session.userdomain, Session.user, 1)[0];
          System.out.println("WorkRequestSubmitter.<init>| Setting default AcquisitionConstraints from BaselineCharacterization");
          this.acqConstr = (OTDRAcquisitionConstraints)basCharact.getMeasurement().getAcquisitionConstraint();
        }
        catch (NullPointerException e) {
          System.out.println("WorkRequestSubmitter.<init>| Setting default AcquisitionConstraints from DomainManager");
          switch (this.taskType) {
          case 3:
            this.acqConstr = (OTDRAcquisitionConstraints)Session.dommgr.getDefaultOTDRAcquisitionConstraints(Session.user, 1).clone();
            break;
          case 2:
            this.acqConstr = (OTDRAcquisitionConstraints)Session.dommgr.getDefaultOTDRAcquisitionConstraints(Session.user, 0).clone();
            break;
          default:
            System.out.println("WorkRequestSubmitter.<init>| Cannot assign default AcquisitionConstraints from DomainManager - the taskType = " + this.taskType + " is not \"for work\"");
          }
        }
      }
      if (anzConstr != null) {
        this.anzConstr = anzConstr;
      }
      else {
        try {
          basCharact = this.monelem.getBaselineCharacterizations(Session.userdomain, Session.user, 1)[0];
          System.out.println("WorkRequestSubmitter.<init>| Setting default AnalysisConstraints from BaselineCharacterization");
          this.anzConstr = (OTDRAnalysisConstraints)basCharact.getAnalysisConstraints();
        }
        catch (NullPointerException e) {
          System.out.println("WorkRequestSubmitter.<init>| Setting default AnalysisConstraints from DomainManager");
          this.anzConstr = (OTDRAnalysisConstraints)Session.dommgr.getDefaultOTDRAnalysisConstraints(Session.user);
        }
      }
//************ ValidationConstraints -- return later! ***********************
      if (this.taskType == 3) {
        if (vldConstr != null) {
          this.vldConstr = vldConstr;
        }
        else {
          System.out.println("WorkRequestSubmitter.<init>| There are NULL ValidationConstraints - cannot submit surveillance");
        }
      }
      this.submitWorkRequest();
    }
    else {
      System.out.println("WorkRequestSubmitter.<init>| MonitoredElement " + monitoredElementIdentityStr + " not found!");
    }
  }

  private void submitWorkRequest() throws Exception {
    WorkManager wrkmgr = Session.dommgr.getWorkManager(Session.user);
    WorkRequest wrkrqst = wrkmgr.newWorkRequest(Session.userdomain, Session.user);
    wrkrqst.setAmficomMeasurementId(this.amficom_measurement_id);
    wrkrqst.setTask(this.taskType);
    wrkrqst.setAcquisitionType(1);
    wrkrqst.setMonitoredElementIdentity(this.monelem.getIdentity());

    Module[] amodule = monelem.getModules(Session.userdomain, Session.user, 1);
    OTDRModule otdrmodule = null;
    OTAUModule otaumodule = null;
    if(amodule.length == 0) {
      amodule = monelem.getModules(Session.userdomain, Session.user, 2);
      otdrmodule = null;
      for(int i = 0; otdrmodule == null && i < amodule.length; i++) {
        otaumodule = (OTAUModule)amodule[i];
        otdrmodule = otaumodule.getOTDRModule(Session.userdomain, Session.user);
      }
    }
    else {
      otdrmodule = (OTDRModule)amodule[0];
    }
    if(otaumodule != null) {
      wrkrqst.setModuleIdentity(otaumodule.getIdentity());
      wrkrqst.setPort(monelem.getModulePort(Session.userdomain, Session.user, otaumodule));
    }
    else {
      wrkrqst.setModuleIdentity(otdrmodule.getIdentity());
      wrkrqst.setPort(monelem.getModulePort(Session.userdomain, Session.user, otdrmodule));
    }

    switch (this.taskType) {
    case 1:
      System.out.println("WorkRequestSubmitter.submitWorkRequest| 'only_measure'(taskType = " + this.taskType + ") not implemented yet");
      break;
    case 2:
      if (this.acqConstr != null && this.anzConstr != null) {
        wrkrqst.setAnalysisConstraints(this.anzConstr);
        wrkrqst.setAcquisitionConstraints(this.acqConstr);
        wrkrqst.setScheduleType(1);
        this.printWorkRequestParameters(wrkrqst);
        wrkmgr.submit(Session.userdomain, Session.user, wrkrqst);
      }
      else
        System.out.println("WorkRequestSubmitter.submitWorkRequest| AcquisitionConstraints and/or AnalysisConstraints are NULL. Unable to submit work request.");
    break;
    case 3:
      if (this.acqConstr != null && this.anzConstr != null && this.vldConstr != null) {
        wrkrqst.setAnalysisConstraints(this.anzConstr);
        wrkrqst.setAcquisitionConstraints(this.acqConstr);
//************ ValidationConstraints -- return later! ***********************
        wrkrqst.setValidationConstraints(this.vldConstr);
        wrkrqst.setScheduleType(2);
        this.printWorkRequestParameters(wrkrqst);
        wrkmgr.submit(Session.userdomain, Session.user, wrkrqst);
      }
      else
        System.out.println("WorkRequestSubmitter.submitWorkRequest| AcquisitionConstraints and/or AnalysisConstraints and/or ValidationConstraints are NULL. Unable to submit work request.");
    break;
    default:
      System.out.println("WorkRequestSubmitter.submitWorkRequest| Failed to submit work request - the taskType = " + this.taskType + " is not \"for work\"");
    }
  }

  private void printWorkRequestParameters (WorkRequest wrkrqst) throws Exception {
    System.out.println("\nWorkRequestSubmitter| ------------- Listing of WorkRequest parameters --------------");
    System.out.println("MonitoredElement: " + this.monelem.getUserLabel(Session.userdomain, Session.user));
    System.out.println("Task: " + wrkrqst.getTask());
    System.out.println("ScheduleType: " + wrkrqst.getScheduleType());
    System.out.println("PendingStatus: " + wrkrqst.getPendingStatus());
    System.out.println("Status: " + wrkrqst.getStatus());
    System.out.println("AcquisitionType: " + wrkrqst.getAcquisitionType());

    System.out.println("\nAcquisitionConstraints:");
    try {
      System.out.println(wrkrqst.getAcquisitionConstraints().toString());
    }
    catch (NullPointerException e) {
      System.out.println("NULL");
    }

    System.out.println("AnalysisConstraints:");
    try{
      System.out.println("        eventLossThreshold: " + this.anzConstr.getEventLossThreshold());
      System.out.println("        endDetectThreshold: " + this.anzConstr.getEndDetectThreshold());
      System.out.println("        reflectanceThreshold: " + this.anzConstr.getReflectanceThreshold());
      System.out.println("        noiseFloorAdjustment: " + this.anzConstr.getNoiseFloorAdjustment());
    }
    catch (NullPointerException e) {
      System.out.println("NULL");
    }

    System.out.println("\nValidationConstraints:");
    try {
      System.out.println(wrkrqst.getValidationConstraints().toString());
    }
    catch (NullPointerException e) {
      System.out.println("NULL");
    }
    System.out.println("\n");
  }
}