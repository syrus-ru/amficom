package com.syrus.AMFICOM.kis.r6;

import com.syrus.AMFICOM.OperationEvent;
import com.gnnettest.questfiber.util.Identity;

public class QueryEvent extends OperationEvent {
  Identity monitorEdelementIdentity;
  Identity characterizationIdentity;

  public QueryEvent(String amficomMeasurementId, String monElemIdStr, String charactIdStr) {
    super(amficomMeasurementId, 1, "Query");
    this.monitorEdelementIdentity = new Identity(monElemIdStr);//RTUSession.dommgr.getMonitoredElement(RTUSession.user, new Identity(monElemStr));
    this.characterizationIdentity = new Identity(charactIdStr);//this.monitoredelement.getAssociatedCharacterization(RTUSession.userdomain, RTUSession.user, new Identity(charactIdStr));
  }

  public String getAmficomMeasurementId() {
    return (String)(super.getSource());
  }

  public Identity getCharacterizationIdentity() {
    return this.characterizationIdentity;
  }

  public Identity getMonitoredElementIdentity() {
    return this.monitorEdelementIdentity;
  }
}