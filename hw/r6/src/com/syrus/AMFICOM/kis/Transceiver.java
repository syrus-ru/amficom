package com.syrus.AMFICOM.kis;

public class Transceiver  {
  private static String measurement_id;
  private static String measurement_type_id;
  private static String local_address;
  private static String[] par_names;
  private static byte[][] par_values;

  static {
    System.loadLibrary("r6transceiver");
  }

  public static native int create(String fileName);
  public static native boolean open(int fileHandle);
  public static native boolean close(int fileHandle);
  public static native boolean read1(int fileHandle, String fileName);
  public static native boolean push1(int fileHandle, String fileName, 
                                     String measurement_id,
                                     String[] par_names,
                                     byte[][] par_values);

  public static String getMeasurementId() {
    return measurement_id;
  }

  public static String getMeasurementTypeId() {
    return measurement_type_id;
  }

  public static String getLocalAddress() {
    return local_address;
  }

  public static String[] getParameterNames() {
    return par_names;
  }

  public static byte[][] getParameterValues() {
    return par_values;
  }
}