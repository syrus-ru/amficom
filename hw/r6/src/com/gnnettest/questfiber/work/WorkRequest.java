// NMI's Java Code Viewer 5.1 © 1997-2001 B. Lemaire
// http://njcv.htmlplanet.com - info@njcv.htmlplanet.com

// Copy registered to Evaluation Copy                                   

// Source File Name:   WorkRequest.java

package com.gnnettest.questfiber.work;

import com.gnnettest.questfiber.util.Identity;
import com.gnnettest.questfiber.util.Schedule;
import java.util.Date;

// Referenced classes of package com.gnnettest.questfiber.work:
//            Request, AcquisitionConstraints, AnalysisConstraints, ValidationConstraints, 
//            WorkReport

public interface WorkRequest
    extends Request {

    public static final int UNDEFINED = 0;
    public static final int SUBMITTED = 1;
    public static final int ACTIVE = 2;
    public static final int COMPLETED = 3;
    public static final int SUSPENDED = 4;
    public static final int CANCELLED = 5;
    public static final int IMMEDIATE = 1;
    public static final int STANDING = 2;
    public static final int SCHEDULED = 3;
    public static final int MEASURE = 1;
    public static final int ANALYZE = 2;
    public static final int VALIDATE = 3;
    public static final int NONE_PENDING = 0;
    public static final int ACTIVE_PENDING = 1;
    public static final int ACTIVE_FAILED = 2;
    public static final int SUSPEND_PENDING = 3;
    public static final int SUSPEND_FAILED = 4;
    public static final int RESUME_PENDING = 5;
    public static final int RESUME_FAILED = 6;
    public static final int CANCEL_PENDING = 7;
    public static final int CANCEL_FAILED = 8;
    public static final int INDETERMINATE = 9;

    public abstract AcquisitionConstraints getAcquisitionConstraints();

    public abstract Identity getAcquisitionConstraintsIdentity();

    public abstract int getAcquisitionType();

    public abstract AnalysisConstraints getAnalysisConstraints();

    public abstract Identity getAnalysisConstraintsIdentity();

    public abstract Identity getConfirmatinAcquisitionConstraintsIdentity();

    public abstract AcquisitionConstraints getConfirmationAcquisitionConstraints();

    public abstract Date getDate();

    public abstract Identity getMonitoredElementIdentity();

    public abstract int getPendingStatus();

    public abstract int getScheduleType();

    public abstract int getStatus();

    public abstract int getTask();

    public abstract ValidationConstraints getValidationConstraints();

    public abstract Identity getValidationConstraintsIdentity();

    public abstract WorkReport[] getWorkReports();

    public abstract String pendingStatusToString();

    public abstract String scheduleTypeToString();

    public abstract void setAcquisitionConstraints(AcquisitionConstraints acquisitionconstraints);

    public abstract void setAcquisitionType(int i);

    public abstract void setAnalysisConstraints(AnalysisConstraints analysisconstraints);

    public abstract void setConfirmationAcquisitionConstraints(AcquisitionConstraints acquisitionconstraints);

    public abstract void setMonitoredElementIdentity(Identity identity);

    public abstract void setPendingStatus(int i);

    public abstract void setPort(int i);

    public abstract void setSchedule(Schedule schedule);

    public abstract void setScheduleType(int i);

    public abstract void setStatus(int i);

    public abstract void setTask(int i);

    public abstract void setValidationConstraints(ValidationConstraints validationconstraints);

    public abstract String statusToString();

    public abstract String taskToString();

//---------------
    public abstract String getAmficomMeasurementId();

    public abstract void setAmficomMeasurementId(String amficom_measurement_id);
//---------------
}
