package com.syrus.AMFICOM.kis.r6;

import java.io.IOException;
import com.syrus.io.IniFile;
import com.syrus.AMFICOM.Dispatcher;
import com.syrus.AMFICOM.kis.TaskReceiver;
import com.syrus.AMFICOM.kis.TaskReporter;
import com.syrus.AMFICOM.kis.Task;
import com.syrus.AMFICOM.OperationEvent;
import com.syrus.AMFICOM.OperationListener;

public class R6 implements OperationListener /*???*/ {

  private static final String INIFILENAME = "AmficomR6.properties";
  private static final String USERNAME = "amficom";
  private static final String PASSWORD = "rakom";
  private static final String KIS_ID = "rtu-1";
  private static final String RMIHOST = "rtu-1";
  private static final String RMIPORT = "17770";
  private static final String KISTIMEWAIT = "1000";
  private ReportComposer reportComposer;

  public R6() {
    Session.dispatcher.register(this, "TaskReceived");
		Session.dispatcher.register(this, "RTUDisconnected");
    this.reportComposer = new ReportComposer();
  }

  public static void main(String[] args) {
    IniFile inifile = null;
    try {
      inifile = new IniFile(INIFILENAME); 
    }
    catch (IOException ioe) {
      System.out.println("Exception reading ini file " + INIFILENAME + ": " + ioe.getMessage() + "; will try to use default values");
    }
    String kis_id;
    String rmihost;
    String rmiport;
    String kistimewait;
    if (inifile == null) {
      System.out.println("Setting default values");
      kis_id = KIS_ID;
      rmihost = RMIHOST;
      rmiport = RMIPORT;
      kistimewait = KISTIMEWAIT;
    }
    else {
      System.out.println("Setting values from file " + INIFILENAME);
      kis_id = inifile.getValue("kis_id", KIS_ID);
      rmihost = inifile.getValue("RMIHost", RMIHOST);
      rmiport = inifile.getValue("RMIPort", RMIPORT);
      kistimewait = inifile.getValue("kistimewait", KISTIMEWAIT);
    }
    inifile = null;
    try {
      Session.open(rmihost, Integer.parseInt(rmiport), USERNAME, PASSWORD);
    }
    catch (Exception e) {
      System.out.println("R6.main | Cannot open session on RTU \n" + e.getMessage() + " -- exiting!");
      e.printStackTrace();
      System.exit(1);
    }

//Create dispatcher of events
    Session.dispatcher = new Dispatcher();
    
    R6 r6 = new R6();
    TaskReceiver taskReceiver = new TaskReceiver(kis_id, Session.dispatcher, Long.parseLong(kistimewait));
    taskReceiver.start();
    TaskReporter taskReporter = new TaskReporter(kis_id, Session.dispatcher, Long.parseLong(kistimewait));
    taskReporter.start();
  }

  public void operationPerformed(OperationEvent e) {
    if (e.getActionCommand() == "TaskReceived") {
      TaskDistinguisher td = new TaskDistinguisher((Task)e.getSource());
		}

		if (e.getActionCommand() == "RTUDisconnected") {
			Session.reopen();
			this.reportComposer.reRegister();
		}

    if (e.getActionCommand() == "shutdown") {
      System.out.println("Exiting...");
      Session.dispatcher.unregister(this, "TaskReceived");
      Session.dispatcher.unregister(this, "shutdown");
    }
  }
}