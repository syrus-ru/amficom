package com.syrus.AMFICOM.kis.r6;

import java.util.Hashtable;
import java.util.Vector;
import java.io.IOException;
import com.gnnettest.questfiber.work.QuestFiberWorkListener;
import com.gnnettest.questfiber.work.WorkEvent;
import com.gnnettest.questfiber.work.Characterization;
import com.gnnettest.questfiber.work.WorkManager;
import com.gnnettest.questfiber.work.WorkRequest;
import com.gnnettest.questfiber.util.Identity;
import com.gnnettest.questfiber.domain.MonitoredElement;
import com.gnnettest.questfiber.gui.events.ServerEventProcessor;
import com.syrus.AMFICOM.OperationEvent;
import com.syrus.AMFICOM.OperationListener;
import com.syrus.AMFICOM.kis.Report;
import com.syrus.io.BellcoreStructureComposer;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.util.ByteArray;

public class ReportComposer implements OperationListener, QuestFiberWorkListener {
  private Vector workRequestIds;
  private ServerEventProcessor serverEventProcessor;

  public ReportComposer() {
    Session.dispatcher.register(this, "Query");
    this.workRequestIds = new Vector(0,1);
    this.serverEventProcessor = new ServerEventProcessor(ServerEventProcessor.QUARTER_SECOND_INTERVAL);
    this.serverEventProcessor.registerForWorkEvents(this);
    this.serverEventProcessor.performRegistration();
  }

	protected void reRegister() {
		this.serverEventProcessor = new ServerEventProcessor(ServerEventProcessor.QUARTER_SECOND_INTERVAL);
    this.serverEventProcessor.registerForWorkEvents(this);
    this.serverEventProcessor.performRegistration();
	}

  public void operationPerformed(OperationEvent e) {
    if (e.getActionCommand() == "Query") {
      QueryEvent qe = (QueryEvent)e;
      MonitoredElement monitoredElement = null;
      try {
        monitoredElement = Session.dommgr.getMonitoredElement(Session.user, qe.getMonitoredElementIdentity());
      }
      catch (Exception ex) {
        System.out.println("ReportComposer.operationPerformed | Failed to obtain reference to MonitoredElement from QueryEvent for identity: " + qe.getMonitoredElementIdentity().toString() + ",\n exception: " + ex.getMessage());
        ex.printStackTrace();
      }
      if (monitoredElement != null) {
        Characterization characterization = null;
        try {
          characterization = monitoredElement.getAssociatedCharacterization(Session.userdomain, Session.user, qe.getCharacterizationIdentity());
        }
        catch (Exception ex) {
          System.out.println("ReportComposer.operationPerformed | Failed to obtain reference to Characterization from QueryEvent for identity: " + qe.getCharacterizationIdentity().toString() + ",\n exception: " + ex.getMessage());
          ex.printStackTrace();
        }
        if (characterization != null) {
          Report report = composeQueryResultReport(qe.getAmficomMeasurementId(), characterization);
          Session.dispatcher.notify(new OperationEvent(report, 1, "ReportReady"));
        }
      }//(monitoredElement != null)
    }
  }

  public void processEvent(WorkEvent workevent) {
    try {
      System.out.println("RequestIdentity: " + workevent.getRequestIdentity().toString());
      System.out.println("DataIdentity: " + workevent.getDataIdentity().toString());
    }
    catch(Exception e) {
      System.out.println("ReportComposer.processEvent | exception: " + e.getMessage());
    }

    if (workevent.getDataIdentity() == null && !this.workRequestIds.contains(workevent.getRequestIdentity()))
      this.workRequestIds.add(workevent.getRequestIdentity());

    if (workevent.getDataIdentity() != null && this.workRequestIds.contains(workevent.getRequestIdentity())) {
      this.workRequestIds.remove(workevent.getRequestIdentity());
      String amficom_measurement_id = null;
      Identity wrkrqstId = null;
      Characterization characterization = null;
      try {
        WorkManager wrkmgr = Session.dommgr.getWorkManager(Session.user);
        wrkrqstId = workevent.getRequestIdentity();
        WorkRequest wrkrqst = wrkmgr.getWorkRequest(Session.userdomain, Session.user, wrkrqstId);
        amficom_measurement_id = wrkrqst.getAmficomMeasurementId();
        
        Identity monitoredElementIdentity = wrkrqst.getMonitoredElementIdentity();
        MonitoredElement monitoredElement = Session.dommgr.getMonitoredElement(Session.user, monitoredElementIdentity);
        
        characterization = monitoredElement.getAssociatedCharacterization(Session.userdomain, Session.user, workevent.getDataIdentity());
      }
      catch (Exception e) {
        System.out.println("Exception while retrieving remote values for workevent: " + e.getMessage());
        e.printStackTrace();
      }
      Report report = composeWorkResultReport(amficom_measurement_id, characterization);
      Session.dispatcher.notify(new OperationEvent(report, 1, "ReportReady"));
    }
  }

  private Report composeQueryResultReport(String amficom_measurement_id, Characterization characterization) {
    Report report = null;
    byte[] rawDat = null;
    rawDat = bcToByteArray(BellcoreStructureComposer.createBellcoreStructure(characterization));
    if (rawDat != null) {
      System.out.println("---- Bellcore Structure size == " + rawDat.length);
      String rawDatStr = new String("reflectogramm");
      Hashtable parameters = new Hashtable(1);
      parameters.put(rawDatStr, rawDat);
      report = new Report(amficom_measurement_id, parameters);
    }
    return report;
  }

  private static Report composeWorkResultReport(String amficom_test_id, Characterization characterization) {
    Hashtable parameters = new Hashtable(2);
    try {
      parameters.put("ref_characterizationidentity", ByteArray.toByteArray(characterization.getIdentity().toString()));
    }
    catch (IOException ioe) {
      System.out.println("While converting string to byte array: " + ioe.getLocalizedMessage());
      ioe.printStackTrace();
    }
    byte[] rawDat = bcToByteArray(BellcoreStructureComposer.createBellcoreStructure(characterization));
    System.out.println("---- Bellcore Structure size == " + rawDat.length);
    parameters.put("reflectogramm", rawDat);
    return new Report(amficom_test_id, parameters); 
  }

	private static void dadaraCorrectData(double[] rawdata) {
//Got from WorkWithReflectoArray.correctReflectoArraySavingNoise()
		int i;
		int begin = 300;
		if(begin > rawdata.length/2)
			begin = rawdata.length/2;
		double max = rawdata[begin];
		for (i = begin; i < rawdata.length; i++)
			if (rawdata[i] > max)
				max = rawdata[i];
		for (i = 0; i < rawdata.length; i++)
			rawdata[i] = rawdata[i] + 65.535 - max;

		for(i = 0; i <= begin; i++)
      if(rawdata[i] > 65535.)
				rawdata[i] = 65535.;

		if(rawdata[0] < 65535. - 0.001)
			rawdata[0] = 65535.;
    if(rawdata[1] > 65535. - 0.001)
			rawdata[1] = (rawdata[0] - rawdata[2])/2.;

/*		int i;
    int begin = 300;
		double[] data = new double[rawdata.length];
    if(begin > data.length/2)
			begin = data.length/2;
    double min = rawdata[begin];
    for(i = begin; i < data.length; i++) {
      if(min > rawdata[i])
				min = rawdata[i];
    }
    for(i = 0; i < data.length; i++) {
      data[i] = rawdata[i] - min;
    }
    for(i = 0; i <= begin; i++) {
      if(data[i] < 0.)
				data[i] = 0.;
    }
    if(data[0] > 0.)
			data[0] = 0.;
    if(data[1] < 0.001)
			data[1] = data[2]/2.;
		return data;*/
	}

  private static byte[] bcToByteArray(BellcoreStructure bs) {
    BellcoreWriter bw = new BellcoreWriter();
    return bw.write(bs);
  }
}