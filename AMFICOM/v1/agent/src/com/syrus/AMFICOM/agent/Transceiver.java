package com.syrus.AMFICOM.agent;

public class Transceiver  {
  private static String measurement_id;
  private static String[] par_names;
  private static byte[][] par_values;

  static {
    System.loadLibrary("agenttransceiver");
  }

  public static native int call(String fileName);
  public static native boolean read1(int fileHandle, String fileName);
  public static native boolean push1(int fileHandle, String fileName,
                                      String measurement_id,
                                      String measurement_type_id,
                                      String local_address,
                                      String[] par_names,
                                      byte[][] par_values);

  public static String getMeasurementId() {
    return measurement_id;
  }

  public static String[] getParameterNames() {
    return par_names;
  }

  public static byte[][] getParameterValues() {
    return par_values;
  }
}