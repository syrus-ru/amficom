package com.syrus.AMFICOM.kis;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.LinkedList;

public class Report  {
  private String measurement_id;
  private String[] par_names;
  private byte[][] par_values;

  public Report(String measurement_id,
                Hashtable parameters) {
    this.measurement_id = measurement_id;
    Enumeration names = parameters.keys();
    Enumeration values = parameters.elements();
    LinkedList lln = new LinkedList();
    LinkedList llv = new LinkedList();
    while (names.hasMoreElements()) {
      lln.add((String)names.nextElement());
      llv.add((byte[])values.nextElement());
    }
    this.par_names = new String[lln.size()];
    this.par_names = (String[])lln.toArray(this.par_names);
    this.par_values = new byte[llv.size()][];
    this.par_values = (byte[][])llv.toArray(this.par_values);
  }

  public String getMeasurementId() {
    return this.measurement_id;
  }

  public String[] getParameterNames() {
    return this.par_names;
  }

  public byte[][] getParameterValues() {
    return this.par_values;
  }
}