// NMI's Java Code Viewer 5.1 © 1997-2001 B. Lemaire
// http://njcv.htmlplanet.com - info@njcv.htmlplanet.com

// Copy registered to Evaluation Copy                                   

// Source File Name:   WorkRequestImplementation.java

package com.gnnettest.questfiber.work;

import com.gnnettest.questfiber.util.*;
import com.gnnettest.questfiber.util.namedserver.ReceptacleAware;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.gnnettest.questfiber.work:
//            RequestImplementation, Constraints, OTDRAcquisitionConstraints, OTDRAcquisitionConstraintsDatabase, 
//            OTDRAnalysisConstraints, OTDRAnalysisConstraintsDatabase, OTDRValidationConstraints, OTDRValidationConstraintsDatabase, 
//            WorkReport, WorkRequest, AcquisitionConstraints, AnalysisConstraints, 
//            ValidationConstraints

public class WorkRequestImplementation extends RequestImplementation
    implements Serializable, WorkRequest, ReceptacleAware {

    private Set receptacles;
    private int task;
    private Identity monitoredElementIdentity;
    private int port;
    private int status;
    private int pendingStatus;
    private String statusString;
    private String serverName;
    private AcquisitionConstraints acquisitionConstraints;
    private Identity acquisitionConstraintsIdentity;
    private AcquisitionConstraints confirmationAcquisitionConstraints;
    private Identity confirmationAcquisitionConstraintsIdentity;
    private Identity analysisConstraintsIdentity;
    private AnalysisConstraints analysisConstraints;
    private Identity validationConstraintsIdentity;
    private ValidationConstraints validationConstraints;
    private Identity userIdentity;
    private int acquisitionType;
    private int scheduleType;
    private Schedule schedule;
    private boolean cancelled;
    private Date requestDate;
    Vector workReports;
    private String amficomMeasurementId;

    public WorkRequestImplementation() {
        receptacles = new HashSet();
        statusString = null;
        serverName = null;
        analysisConstraintsIdentity = null;
        analysisConstraints = null;
        validationConstraintsIdentity = null;
        validationConstraints = null;
        scheduleType = 1;
        cancelled = false;
        requestDate = null;
        workReports = new Vector();
        amficomMeasurementId = null;
    }

    public boolean Cancelled() {
        return status == 5;
    }

    public void addReceptacle(String s) {
        Log.systemLog.logDebugMessage("WorkRequest " + getIdentity() + " adding " + s, 190);
        serverName = s;
        receptacles.add(s);
    }

    void addWorkReport(WorkReport workreport) {
        if(workreport != null) {
            Log.systemLog.logDebugMessage(getClass().getName() + ": Adding report from " + workreport.getDate(), 200);
            workReports.addElement(workreport);
        }
    }

    public void cancel() {
        status = 5;
        pendingStatus = 0;
    }

    public void clearReceptacles() {
        receptacles.clear();
    }

    void createIdentity() {
        setIdentity(IdentityGenerator.generateIdentity(this));
    }

    public AcquisitionConstraints getAcquisitionConstraints() {
        return acquisitionConstraints;
    }

    public Identity getAcquisitionConstraintsIdentity() {
        Identity identity = null;
        identity = (Identity)acquisitionConstraintsIdentity.clone();
        return identity;
    }

    public int getAcquisitionType() {
        return acquisitionType;
    }

    public AnalysisConstraints getAnalysisConstraints() {
        return analysisConstraints;
    }

    public Identity getAnalysisConstraintsIdentity() {
        Identity identity = null;
        if(analysisConstraintsIdentity != null)
            identity = (Identity)analysisConstraintsIdentity.clone();
        return identity;
    }

    public Identity getConfirmatinAcquisitionConstraintsIdentity() {
        Identity identity = null;
        identity = (Identity)confirmationAcquisitionConstraintsIdentity.clone();
        return identity;
    }

    public AcquisitionConstraints getConfirmationAcquisitionConstraints() {
        return confirmationAcquisitionConstraints;
    }

    public Date getDate() {
        Date date = null;
        if(requestDate != null)
            date = (Date)requestDate.clone();
        return date;
    }

    public Identity getMonitoredElementIdentity() {
        return monitoredElementIdentity;
    }

    public int getPendingStatus() {
        return pendingStatus;
    }

    public int getPort() {
        return port;
    }

    public Set getReceptacles() {
        return new HashSet(receptacles);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * @deprecated Method getScheduleType is deprecated
     */

    public int getScheduleType() {
        return scheduleType;
    }

    public String getServerName() {
        if(serverName != null)
            return serverName;
        else
            return "NULL";
    }

    public int getStatus() {
        return status;
    }

    public String getStatusString() {
        return statusString;
    }

    public int getTask() {
        return task;
    }

    public Identity getUserIdentity() {
        Identity identity = null;
        identity = (Identity)userIdentity.clone();
        return identity;
    }

    public ValidationConstraints getValidationConstraints() {
        return validationConstraints;
    }

    public Identity getValidationConstraintsIdentity() {
        Identity identity = null;
        if(validationConstraintsIdentity != null)
            identity = (Identity)validationConstraintsIdentity.clone();
        return identity;
    }

    public WorkReport[] getWorkReports() {
        if(workReports.size() > 0) {
            WorkReport aworkreport[] = new WorkReport[workReports.size()];
            return (WorkReport[])workReports.toArray(aworkreport);
        } else {
            return null;
        }
    }

    public boolean isImmediate() {
        return scheduleType == 1;
    }

    public boolean isScheduled() {
        return scheduleType == 3;
    }

    public boolean isStanding() {
        return scheduleType == 2;
    }

    public void markAsImmediate() {
        scheduleType = 1;
    }

    public void markAsStanding() {
        scheduleType = 2;
    }

    public String pendingStatusToString() {
        return pendingStatusToString(pendingStatus);
    }

    public static String pendingStatusToString(int i) {
        String s;
        switch(i) {
        case 1: // '\001'
            s = InternationalStrings.getString("Active_Pending");
            break;

        case 2: // '\002'
            s = InternationalStrings.getString("Active_Failed");
            break;

        case 3: // '\003'
            s = InternationalStrings.getString("Suspend_Pending");
            break;

        case 4: // '\004'
            s = InternationalStrings.getString("Suspend_Failed");
            break;

        case 7: // '\007'
            s = InternationalStrings.getString("Cancel_Pending");
            break;

        case 8: // '\b'
            s = InternationalStrings.getString("Cancel_Failed");
            break;

        case 5: // '\005'
            s = InternationalStrings.getString("Resume_Pending");
            break;

        case 6: // '\006'
            s = InternationalStrings.getString("Resume_Failed");
            break;

        case 0: // '\0'
            s = InternationalStrings.getString("None");
            break;

        default:
            s = InternationalStrings.getString("Unknown");
            break;
        }
        return s;
    }

    public boolean persistant() {
        return true;
    }

    public void removeReceptacle(String s) {
        receptacles.remove(s);
    }

    void resolveIdentities(boolean flag) {
        System.out.println("WRI resolveIdentities type: " + acquisitionType);
        if(flag)
            createIdentity();
        if(acquisitionConstraints != null) {
            Identity identity = acquisitionConstraints.getIdentity();
            if(identity == null) {
                acquisitionConstraints.createIdentity();
                identity = acquisitionConstraints.getIdentity();
                if(acquisitionConstraints instanceof OTDRAcquisitionConstraints) {
                    OTDRAcquisitionConstraintsDatabase otdracquisitionconstraintsdatabase = new OTDRAcquisitionConstraintsDatabase();
                    try {
                        otdracquisitionconstraintsdatabase.create((OTDRAcquisitionConstraints)acquisitionConstraints);
                    }
                    catch(Exception exception) {
                        Log.systemLog.logDebugMessage("WorkRequestImplementation: Error (1) ", 100);
                        Log.systemLog.logDebugMessage(" " + exception, 100);
                        Log.systemLog.logErrorException(exception);
                    }
                }
            }
            acquisitionConstraintsIdentity = (Identity)identity.clone();
        }
        if(analysisConstraints != null) {
            Identity identity1 = analysisConstraints.getIdentity();
            if(identity1 == null) {
                analysisConstraints.createIdentity();
                identity1 = analysisConstraints.getIdentity();
                if(analysisConstraints instanceof OTDRAnalysisConstraints) {
                    OTDRAnalysisConstraintsDatabase otdranalysisconstraintsdatabase = new OTDRAnalysisConstraintsDatabase();
                    try {
                        otdranalysisconstraintsdatabase.create((OTDRAnalysisConstraints)analysisConstraints);
                    }
                    catch(Exception exception1) {
                        Log.systemLog.logDebugMessage("WorkRequestImplementation: Error (2) ", 100);
                        Log.systemLog.logDebugMessage(" " + exception1, 100);
                        Log.systemLog.logErrorException(exception1);
                    }
                }
            }
            analysisConstraintsIdentity = (Identity)identity1.clone();
        }
        if(validationConstraints != null) {
            Identity identity2 = validationConstraints.getIdentity();
            if(identity2 == null) {
                validationConstraints.createIdentity();
                identity2 = validationConstraints.getIdentity();
                if(validationConstraints instanceof OTDRValidationConstraints) {
                    OTDRValidationConstraintsDatabase otdrvalidationconstraintsdatabase = new OTDRValidationConstraintsDatabase();
                    try {
                        otdrvalidationconstraintsdatabase.create((OTDRValidationConstraints)validationConstraints);
                    }
                    catch(Exception exception2) {
                        Log.systemLog.logDebugMessage("WorkRequestImplementation: Error (3) ", 100);
                        Log.systemLog.logDebugMessage(" " + exception2, 100);
                        Log.systemLog.logErrorException(exception2);
                    }
                }
            }
            validationConstraintsIdentity = (Identity)identity2.clone();
        }
    }

    public void resume() {
        status = 2;
        pendingStatus = 0;
    }

    public String scheduleTypeToString() {
        return scheduleTypeToString(scheduleType);
    }

    public static String scheduleTypeToString(int i) {
        String s;
        switch(i) {
        case 1: // '\001'
            s = InternationalStrings.getString("Immediate");
            break;

        case 2: // '\002'
            s = InternationalStrings.getString("Standing");
            break;

        case 3: // '\003'
            s = InternationalStrings.getString("Scheduled");
            break;

        default:
            s = InternationalStrings.getString("Unknown");
            break;
        }
        return s;
    }

    public void setAcquisitionConstraints(AcquisitionConstraints acquisitionconstraints) {
        acquisitionConstraints = acquisitionconstraints;
    }

    public void setAcquisitionType(int i) {
        acquisitionType = i;
    }

    public void setAnalysisConstraints(AnalysisConstraints analysisconstraints) {
        analysisConstraints = analysisconstraints;
    }

    public void setAnalysisConstraintsIdentity(Identity identity) {
        analysisConstraintsIdentity = new Identity(identity.toString());
    }

    void setAttributes(int i, Identity identity, Identity identity1, int j, Identity identity2, int k, int l, 
            Schedule schedule1, int i1, Identity identity3, String s, Identity identity4, Identity identity5, Date date, 
            int j1, String s1, String amficomMeasurementId) {
        task = i;
        setModuleIdentity(identity);
        monitoredElementIdentity = (Identity)identity1.clone();
        port = j;
        acquisitionType = k;
        status = i1;
        pendingStatus = j1;
        userIdentity = (Identity)identity3.clone();
        statusString = s;
        serverName = s1;
        this.amficomMeasurementId = amficomMeasurementId;
        receptacles.add(s1);
        acquisitionConstraintsIdentity = (Identity)identity2.clone();
        if(identity4 != null)
            analysisConstraintsIdentity = (Identity)identity4.clone();
        else
            analysisConstraintsIdentity = null;
        if(identity5 != null)
            validationConstraintsIdentity = (Identity)identity5.clone();
        else
            validationConstraintsIdentity = null;
        requestDate = date;
        switch(l) {
        case 1: // '\001'
            markAsImmediate();
            break;

        case 2: // '\002'
            markAsStanding();
            break;

        default:
            markAsImmediate();
            break;

        case 3: // '\003'
            break;
        }
    }

    public void setConfirmationAcquisitionConstraints(AcquisitionConstraints acquisitionconstraints) {
        confirmationAcquisitionConstraints = acquisitionconstraints;
    }

    public void setMonitoredElementIdentity(Identity identity) {
        monitoredElementIdentity = new Identity(identity.toString());
    }

    public void setPendingStatus(int i) {
        pendingStatus = i;
    }

    public void setPort(int i) {
        port = i;
    }

    public void setSchedule(Schedule schedule1) {
        if(schedule1 != null) {
            schedule = schedule1;
            scheduleType = 3;
        }
    }

    /**
     * @deprecated Method setScheduleType is deprecated
     */

    public void setScheduleType(int i) {
        scheduleType = i;
    }

    public void setStatus(int i) {
        status = i;
    }

    public void setTask(int i) {
        task = i;
    }

    public void setUserIdentity(Identity identity) {
        userIdentity = new Identity(identity.toString());
    }

    public void setValidationConstraints(ValidationConstraints validationconstraints) {
        validationConstraints = validationconstraints;
    }

    public void setValidationConstraintsIdentity(Identity identity) {
        validationConstraintsIdentity = new Identity(identity.toString());
    }

    void stampDate() {
        requestDate = new Date();
    }

    public String statusToString() {
        return statusToString(status);
    }

    public static String statusToString(int i) {
        String s;
        switch(i) {
        case 1: // '\001'
            s = InternationalStrings.getString("Submitted");
            break;

        case 2: // '\002'
            s = InternationalStrings.getString("Active");
            break;

        case 3: // '\003'
            s = InternationalStrings.getString("Completed");
            break;

        case 4: // '\004'
            s = InternationalStrings.getString("Suspended");
            break;

        case 5: // '\005'
            s = InternationalStrings.getString("Cancelled");
            break;

        case 9: // '\t'
            s = InternationalStrings.getString("Indeterminate");
            break;

        case 0: // '\0'
            s = InternationalStrings.getString("Undefined");
            break;

        case 6: // '\006'
        case 7: // '\007'
        case 8: // '\b'
        default:
            s = InternationalStrings.getString("Unknown");
            break;
        }
        return s;
    }

    public void suspend() {
        status = 4;
        pendingStatus = 0;
    }

    public boolean suspended() {
        return status == 4;
    }

    public String taskToString() {
        return taskToString(task);
    }

    public static String taskToString(int i) {
        String s;
        switch(i) {
        case 1: // '\001'
            s = InternationalStrings.getString("Measure");
            break;

        case 2: // '\002'
            s = InternationalStrings.getString("Analyze");
            break;

        case 3: // '\003'
            s = InternationalStrings.getString("Validate");
            break;

        default:
            s = InternationalStrings.getString("Unknown");
            break;
        }
        return s;
    }

  public String getAmficomMeasurementId() {
    return this.amficomMeasurementId;
  }

  public void setAmficomMeasurementId(String amficomMeasurementId) {
    this.amficomMeasurementId = amficomMeasurementId;
  }
}
