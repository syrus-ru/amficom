package com.syrus.AMFICOM.kis;

import java.util.Hashtable;

public class Task  {
  private String measurement_id;
  private String measurement_type_id;
  private String local_address;
  private Hashtable parameters;

  public Task(String measurement_id,
              String measurement_type_id,
              String local_address,
              String[] par_names,
              byte[][] par_values) {
    this.measurement_id = measurement_id;
    this.measurement_type_id = measurement_type_id;
    this.local_address = local_address;
    this.parameters = new Hashtable(par_names.length);
    for (int i = 0; i < par_names.length; i++)
      this.parameters.put(par_names[i], par_values[i]);
  }

  public String getMeasurementId() {
    return this.measurement_id;
  }

  public String getMeasurementTypeId() {
    return this.measurement_type_id;
  }

  public String getLocalAddress() {
    return this.local_address;
  }

  public Hashtable getParameters() {
    return this.parameters;
  }
}