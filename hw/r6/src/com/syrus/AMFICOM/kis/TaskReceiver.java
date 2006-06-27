package com.syrus.AMFICOM.kis;

import com.syrus.AMFICOM.Dispatcher;
import com.syrus.AMFICOM.OperationEvent;

public class TaskReceiver extends Thread  {
  private final long KISTIMEWAIT;
  private final String taskFileName;
  private boolean taskFileConnected;
  private int taskFileHandle;
  private Dispatcher dispatcher;

  public TaskReceiver(String kis_id, Dispatcher dispatcher, long kistimewait) {
    this.taskFileName = "task" + kis_id;
    this.taskFileHandle = 0;
    this.taskFileConnected = false;
    this.dispatcher = dispatcher;
    this.KISTIMEWAIT = kistimewait;
  }

  public void run() {
    byte[] bar;
    while(true) {
      if(!this.taskFileConnected) {
        this.taskFileHandle = Transceiver.create(this.taskFileName);
        if(this.taskFileHandle != 0) {
          System.out.println("Task channel created, waiting for client...");
          if(Transceiver.open(this.taskFileHandle)) {
            System.out.println("Task channel opened");
            this.taskFileConnected = true;
          }
          else
            System.out.println("Task channel NOT opened");
        }
        else
          System.out.println("Task channel NOT created");
      }
      else {
        if (Transceiver.read1(this.taskFileHandle, this.taskFileName)) {
          System.out.println("Successfully read task");
          Transceiver.close(this.taskFileHandle);
          this.taskFileHandle = 0;
          this.taskFileConnected = false;
          this.dispatcher.notify(new OperationEvent(new Task(Transceiver.getMeasurementId(),
                                                             Transceiver.getMeasurementTypeId(),
                                                             Transceiver.getLocalAddress(),
                                                             Transceiver.getParameterNames(),
                                                             Transceiver.getParameterValues()), 1, "TaskReceived"));
        }
        else {
          System.out.println("Can't read task");
          Transceiver.close(this.taskFileHandle);
          this.taskFileHandle = 0;
          this.taskFileConnected = false;
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
}