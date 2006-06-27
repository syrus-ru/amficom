package com.syrus.AMFICOM.kis;

import java.util.Vector;
import com.syrus.AMFICOM.Dispatcher;
import com.syrus.AMFICOM.OperationListener;
import com.syrus.AMFICOM.OperationEvent;
import com.syrus.AMFICOM.kis.Report;

public class TaskReporter extends Thread implements OperationListener {
  private final long KISTIMEWAIT;
  private final String reportFileName;
  private boolean reportFileConnected;
  private int reportFileHandle;
  private Vector reportSegments;

  public TaskReporter(String kis_id, Dispatcher dispatcher, long kistimewait) {
    this.reportFileName = "report" + kis_id;
    this.reportFileConnected = false;
    this.reportFileHandle = 0;
    this.reportSegments = new Vector(0, 1);
    this.KISTIMEWAIT = kistimewait;
    dispatcher.register(this, "ReportReady");
  }

  public void run() {
    Report report;
    while (true) {
      if(!this.reportFileConnected) {
        this.reportFileHandle = Transceiver.create(this.reportFileName);
        if(this.reportFileHandle != 0) {
          System.out.println("Report channel created, waiting for client...");
          if(Transceiver.open(this.reportFileHandle)) {
            System.out.println("Report channel opened");
            this.reportFileConnected = true;
          }
          else
            System.out.println("Report channel NOT opened");
        }
        else
          System.out.println("Report channel NOT created");
      }
      else {
        if (!this.reportSegments.isEmpty()) {
          report = (Report)this.reportSegments.get(0);
          if (Transceiver.push1(this.reportFileHandle, 
                                this.reportFileName,
                                report.getMeasurementId(),
                                report.getParameterNames(),
                                report.getParameterValues())) {
            System.out.println("Successfully pushed report");
            this.reportSegments.remove(0);
          }
          else {
            System.out.println("Can't push report");
            Transceiver.close(this.reportFileHandle);
            this.reportFileHandle = 0;
            this.reportFileConnected = false;
          }
        }
      }
      try {
        sleep(this.KISTIMEWAIT);
      }
      catch(InterruptedException ie) {
        ie.printStackTrace();
      }
    }
  }

  public void operationPerformed(OperationEvent e) {
    if(e.getActionCommand() == "ReportReady") {
      this.reportSegments.add((Report)e.getSource());
    }
  }
}