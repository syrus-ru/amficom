package com.syrus.AMFICOM.agent;

import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

public class ParametersDatabase {
/*
  public static String getTestArgumentTypeId(String codename, String test_type_id) {
    if (test_type_id.equals("trace_and_analyse")) {
      if (codename.equals("ref_wvlen"))
        return "ttal6";
      if (codename.equals("ref_trclen"))
        return "ttal1";
      if (codename.equals("ref_res"))
        return "ttal4";
      if (codename.equals("ref_pulswd"))
        return "ttal9";
      if (codename.equals("ref_ior"))
        return "ttal5";
      if (codename.equals("ref_scans"))
        return "ttal2";
      Log.errorMessage("ParametersDatabase | Unknown codename '" + codename + "' for test type '" + test_type_id + "'");
      return null;
    }
    Log.errorMessage("ParametersDatabase | Unknown test type: '" + test_type_id + "'");
    return null;
  }*/

  public static String getTestParameterTypeId(String codename, String test_type_id) {
    if (test_type_id.equals("reflectometry") || test_type_id.equals("trace_and_analyse")) {
      if (codename.equals("ref_characterizationidentity"))
        return ApplicationProperties.getString("ref_characterizationidentity", null);
      if (codename.equals("reflectogramm"))
        return ApplicationProperties.getString("reflectogramm", null);
      Log.errorMessage("ParametersDatabase | Unknown codename '" + codename + "' for test type '" + test_type_id + "'");
      return null;
    }
    Log.errorMessage("ParametersDatabase | Unknown test type: '" + test_type_id + "'");
    return null;
  }

  public static String getAnalysisParameterTypeId(String codename, String analysis_type_id) {
    if (analysis_type_id.equals("dadara")) {
      if (codename.equals("dadara_event_array")) {
        return ApplicationProperties.getString("dadara_event_array", null);
      }
      Log.errorMessage("ParametersDatabase | Unknown codename '" + codename + "' for analysis type '" + analysis_type_id + "'");
      return null;
    }
    Log.errorMessage("ParametersDatabase | Unknown analysis type: '" + analysis_type_id + "'");
    return null;
  }

  public static String getEvaluationParameterTypeId(String codename, String evaluation_type_id) {
    if (evaluation_type_id.equals("dadara")) {
      if (codename.equals("dadara_alarm_array")) {
        return ApplicationProperties.getString("dadara_alarm_array", null);
      }
      Log.errorMessage("ParametersDatabase | Unknown codename '" + codename + "' for evaluation type '" + evaluation_type_id + "'");
      return null;
    }
    Log.errorMessage("ParametersDatabase | Unknown evaluation type: '" + evaluation_type_id + "'");
    return null;
  }
}