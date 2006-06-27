// NMI's Java Code Viewer 5.1 © 1997-2001 B. Lemaire
// http://njcv.htmlplanet.com - info@njcv.htmlplanet.com

// Copy registered to Evaluation Copy

// Source File Name:   ServerEventProcessor.java

package com.gnnettest.questfiber.gui.events;

import com.gnnettest.questfiber.*;
import com.gnnettest.questfiber.domain.*;
import com.gnnettest.questfiber.problem.ProblemEvent;
import com.gnnettest.questfiber.problem.QuestFiberProblemListener;
import com.gnnettest.questfiber.rmi.ServerEventPipe;
import com.gnnettest.questfiber.util.*;
import com.gnnettest.questfiber.work.QuestFiberWorkListener;
import com.gnnettest.questfiber.work.WorkEvent;
import java.util.*;
import javax.swing.Timer;
//-------------
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.syrus.AMFICOM.kis.r6.Session;
//-------------

// Referenced classes of package com.gnnettest.questfiber.gui.events:
//            QFLoginEvent, QFLoginListener, QFLoginListenerInterface

public class ServerEventProcessor
    implements QuestFiberServerListener, QFLoginListenerInterface, QuestFiberProblemListener, QuestFiberConfigurationListener, QuestFiberWorkListener {

    public static final int ONE_SECOND_INTERVAL = 1000;
    public static final int HALF_SECOND_INTERVAL = 500;
    public static final int QUARTER_SECOND_INTERVAL = 250;
    public static final int DEFAULT_DELAY_INTERVAL = 250;
    private Timer eventTimer;
    private ThreadRequestQueue eventQueue;
    private boolean registeredForEvents;
    private boolean registerForServerEvents;
    private boolean registerForConfigurationEvents;
    private boolean registerForProblemEvents;
    private boolean registerForWorkEvents;
    Vector serverListeners;
    Vector configurationListeners;
    Vector problemListeners;
    Vector workListeners;
    private QFLoginListener loginListener;

    class TimerHandler implements ActionListener {

      private final ServerEventProcessor servEvProc;

      TimerHandler(ServerEventProcessor servereventprocessor) {
          servEvProc = servereventprocessor;
      }

      public void actionPerformed(ActionEvent actionevent) {
          Log.systemLog.logDebugMessage(getClass().getName() + ": Timer Checking Queue", 200);
          if(!ServerEventProcessor.access$0(servEvProc).hasUpdated()) {
              Log.systemLog.logDebugMessage(getClass().getName() + ": Stopping Timer", 200);
              ServerEventProcessor.access$1(servEvProc).stop();
              ServerEventProcessor.access$2(servEvProc);
          }
      }
    }


    public ServerEventProcessor(int i) {
        eventTimer = null;
        eventQueue = new ThreadRequestQueue();
        registeredForEvents = false;
        registerForServerEvents = false;
        registerForConfigurationEvents = false;
        registerForProblemEvents = false;
        registerForWorkEvents = false;
        serverListeners = new Vector();
        configurationListeners = new Vector();
        problemListeners = new Vector();
        workListeners = new Vector();
        loginListener = new QFLoginListener();
        loginListener.addLoginListener(this);
        Log.systemLog.logDebugMessage(getClass().getName() + ": Creating Timer with delay = " + i, 200);
        eventTimer = new Timer(i, new TimerHandler(this));
        eventTimer.setInitialDelay(i);
        eventTimer.setRepeats(true);
    }

    public void LoginActionRequested(QFLoginEvent qfloginevent) {
        if(!qfloginevent.getStatus())
            performDeregistration();
        else
            performRegistration();
    }

    static ThreadRequestQueue access$0(ServerEventProcessor servereventprocessor) {
        return servereventprocessor.eventQueue;
    }

    static Timer access$1(ServerEventProcessor servereventprocessor) {
        return servereventprocessor.eventTimer;
    }

    static void access$2(ServerEventProcessor servereventprocessor) {
        servereventprocessor.processEventQueue();
    }

    private void fireConfigurationEvent(ConfigurationEvent configurationevent) {
        for(int i = 0; i < configurationListeners.size(); i++)
            ((QuestFiberConfigurationListener)configurationListeners.get(i)).processEvent(configurationevent);

    }

    private void fireProblemEvent(ProblemEvent problemevent) {
        for(int i = 0; i < problemListeners.size(); i++)
            ((QuestFiberProblemListener)problemListeners.get(i)).processEvent(problemevent);

    }

    private void fireServerEvent(ServerEvent serverevent) {
        for(int i = 0; i < serverListeners.size(); i++)
            ((QuestFiberServerListener)serverListeners.get(i)).processEvent(serverevent);

    }

    private void fireWorkEvent(WorkEvent workevent) {
        for(int i = 0; i < workListeners.size(); i++)
            ((QuestFiberWorkListener)workListeners.get(i)).processEvent(workevent);

    }

    public void performDeregistration() {
        Log.systemLog.logDebugMessage(getClass().getName() + ": Performing Deregistration", 200);
        if(registeredForEvents) {
            registeredForEvents = false;
            if(registerForServerEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Deregistering for Server Events", 200);
                Session.eventPipe.removeQuestFiberServerListener(this);
            }
            if(registerForConfigurationEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Deregistering for Configuration Events", 200);
                Session.eventPipe.removeQuestFiberConfigurationListener(this);
            }
            if(registerForProblemEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Deregistering for Problem Events", 200);
                Session.eventPipe.removeQuestFiberProblemListener(this);
            }
            if(registerForWorkEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Deregistering for Work Events", 200);
                Session.eventPipe.removeQuestFiberWorkListener(this);
            }
        }
    }

    public void performRegistration() {
        Log.systemLog.logDebugMessage(getClass().getName() + ": Performing Registration", 200);
        if(!registeredForEvents) {
            registeredForEvents = true;
            if(registerForServerEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Registering for Server Events", 200);
                Session.eventPipe.addQuestFiberServerListener(this);
            }
            if(registerForConfigurationEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Registering for Configuration Events", 200);
                Session.eventPipe.addQuestFiberConfigurationListener(this);
            }
            if(registerForProblemEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Registering for Problem Events", 200);
                Session.eventPipe.addQuestFiberProblemListener(this);
            }
            if(registerForWorkEvents) {
                Log.systemLog.logDebugMessage(getClass().getName() + ": Registering for Work Events", 200);
                Session.eventPipe.addQuestFiberWorkListener(this);
            }
        }
    }

    public void processEvent(ServerEvent serverevent) {
        if(serverevent instanceof ConfigurationEvent) {
            processEvent((ConfigurationEvent)serverevent);
        } else {
            eventQueue.enqueueRequest(serverevent);
            startTimer();
        }
    }

    public void processEvent(ConfigurationEvent configurationevent) {
        if(configurationevent instanceof CreateConfigurationEvent) {
            fireConfigurationEvent(configurationevent);
            fireServerEvent(configurationevent);
        } else
        if(configurationevent instanceof DeleteConfigurationEvent) {
            fireConfigurationEvent(configurationevent);
            fireServerEvent(configurationevent);
        } else {
            eventQueue.enqueueRequest(configurationevent);
            startTimer();
        }
    }

    public void processEvent(ProblemEvent problemevent) {
        eventQueue.enqueueRequest(problemevent);
        startTimer();
    }

    public void processEvent(WorkEvent workevent) {
        eventQueue.enqueueRequest(workevent);
        startTimer();
    }

    private void processEventQueue() {
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        Object obj3 = null;
        Object obj4 = null;
        HashMap hashmap = new HashMap();
        HashMap hashmap1 = new HashMap();
        HashMap hashmap2 = new HashMap();
        Object obj5 = null;
        Log.systemLog.logDebugMessage(getClass().getName() + ": Processing Event Queue, size = " + eventQueue.size(), 200);
        for(ServerEvent serverevent = (ServerEvent)eventQueue.dequeueRequest(); serverevent != null; serverevent = (ServerEvent)eventQueue.dequeueRequest())
            if(serverevent instanceof ConfigurationEvent) {
                com.gnnettest.questfiber.util.Identity identity = ((ConfigurationEvent)serverevent).getConfiguredIdentity();
                Log.systemLog.logDebugMessage(getClass().getName() + ": Processing Configuration Event for " + identity, 200);
                hashmap2.put(identity, serverevent);
            } else
            if(serverevent instanceof ProblemEvent) {
                com.gnnettest.questfiber.util.Identity identity1 = ((ProblemEvent)serverevent).getReportIdentity();
                Log.systemLog.logDebugMessage(getClass().getName() + ": Processing Problem Event for " + identity1, 200);
                hashmap1.put(identity1, serverevent);
            } else
            if(serverevent instanceof WorkEvent) {
                com.gnnettest.questfiber.util.Identity identity2 = ((WorkEvent)serverevent).getRequestIdentity();
                Log.systemLog.logDebugMessage(getClass().getName() + ": Processing Work Event for " + identity2, 200);
                hashmap.put(identity2, serverevent);
            }

        if(hashmap2.size() > 0) {
            Log.systemLog.logDebugMessage(getClass().getName() + ": Processing Configuration Events from Map, # of Events = " + hashmap2.size(), 200);
            ConfigurationEvent configurationevent;
            for(Iterator iterator = hashmap2.values().iterator(); iterator.hasNext(); fireServerEvent(configurationevent)) {
                configurationevent = (ConfigurationEvent)iterator.next();
                fireConfigurationEvent(configurationevent);
            }

        }
        if(hashmap.size() > 0) {
            Log.systemLog.logDebugMessage(getClass().getName() + ": Processing Work Events from Map, # of Events = " + hashmap.size(), 200);
            WorkEvent workevent;
            for(Iterator iterator1 = hashmap.values().iterator(); iterator1.hasNext(); fireServerEvent(workevent)) {
                workevent = (WorkEvent)iterator1.next();
                fireWorkEvent(workevent);
            }

        }
        if(hashmap1.size() > 0) {
            Log.systemLog.logDebugMessage(getClass().getName() + ": Processing Problem Events from Map, # of Events = " + hashmap1.size(), 200);
            ProblemEvent problemevent;
            for(Iterator iterator2 = hashmap1.values().iterator(); iterator2.hasNext(); fireServerEvent(problemevent)) {
                problemevent = (ProblemEvent)iterator2.next();
                fireProblemEvent(problemevent);
            }

        }
    }

    public void registerForConfigurationEvents(QuestFiberConfigurationListener questfiberconfigurationlistener) {
        configurationListeners.add(questfiberconfigurationlistener);
        registerForConfigurationEvents = true;
    }

    public void registerForProblemEvents(QuestFiberProblemListener questfiberproblemlistener) {
        problemListeners.add(questfiberproblemlistener);
        registerForProblemEvents = true;
    }

    public void registerForServerEvents(QuestFiberServerListener questfiberserverlistener) {
        serverListeners.add(questfiberserverlistener);
        registerForServerEvents = true;
    }

    public void registerForWorkEvents(QuestFiberWorkListener questfiberworklistener) {
        workListeners.add(questfiberworklistener);
        registerForWorkEvents = true;
    }

    private void startTimer() {
        if(eventTimer.isRunning()) {
            Log.systemLog.logDebugMessage(getClass().getName() + ": Restarting Timer", 200);
            eventTimer.restart();
        } else {
            Log.systemLog.logDebugMessage(getClass().getName() + ": Starting Timer", 200);
            eventTimer.start();
        }
    }
}
