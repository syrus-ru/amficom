package com.syrus.AMFICOM.analysis.dadara;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class WorkWithReflectoEventsArray
{
  public static final int LINEAR = 0;
  public static final int CONNECTOR = 4;
  public static final int WELD = 3;
  public static final int UNKNOWN = 1;

  public WorkWithReflectoEventsArray()
  {
  }

  public static ReflectogramEvent getReflectogrammEventByCoord(int coord, ReflectogramEvent []re)
  {
    if(re == null)
      return null;

    for(int i=0; i<re.length; i++)
    {
      if(re[i].beginLinear<= coord && re[i].endLinear>=coord)
      {
        return re[i];
      }
    }
    return null;
  }


  public static int getEventTypeByNumber(int nEvent, ReflectogramEvent []re)
  {
    if(re == null)
      return UNKNOWN;
    if(nEvent <0 || nEvent>=re.length)
      return UNKNOWN;
    if(re[nEvent].linearEvent == 1)
      return LINEAR;
    else if(re[nEvent].weldEvent == 1)
      return WELD;
    else if(re[nEvent].connectorEvent == 1)
      return CONNECTOR;
    return UNKNOWN;
  }

  public static int getEventType(ReflectogramEvent re)
  {
    if(re == null)
      return UNKNOWN;
    if(re.linearEvent == 1)
      return LINEAR;
    else if(re.weldEvent == 1)
      return WELD;
    else if(re.connectorEvent == 1)
      return CONNECTOR;
    return UNKNOWN;
  }


  public static int getEventType(int coord, ReflectogramEvent []re)
  {
    if(re == null)
      return UNKNOWN;
    for(int i=0; i<re.length; i++)
    {
      if(re[i].beginLinear<= coord && re[i].endLinear>=coord)
      {
        if(re[i].linearEvent == 1)
          return LINEAR;
        else if(re[i].weldEvent == 1)
          return WELD;
        else if(re[i].connectorEvent == 1)
          return CONNECTOR;
        return UNKNOWN;
      }
    }
    return UNKNOWN;
  }




  public static  int getEventNumber(int coord, ReflectogramEvent []re)
  {
    if(re == null)
      return UNKNOWN;
    for(int i=0; i<re.length; i++)
    {
      if(re[i].beginLinear<= coord && re[i].endLinear>=coord)
        return i;
    }
    return -1;
  }


  public static int getEventCoord(ReflectogramEvent re)
  {
    int coord;
    if(re.connectorEvent == 1)
    {
      coord = (int)(re.center_connector);
    }
    else if(re.weldEvent == 1)
    {
      coord = (int)(re.center_weld);
    }
    else
    {
      coord = (re.beginLinear+
               re.endLinear)/2;
    }
    return coord;
  }

  public static double getDeadZoneDifference(double []y, ReflectogramEvent []re)
  {
    double maxY = y[0];
    for(int i=0; i<300; i++)
    {
      if(maxY<y[i])
        maxY = y[i];
    }
    double maxRE = re[0].refAmplitude(0);
    for(int i=0; i<300; i++)
    {
      if(maxRE<re[0].refAmplitude(i))
      {
        maxRE=re[0].refAmplitude(i);
      }
    }
    return (maxY - maxRE);
  }



  public static double getAmplitudeByCoord(ReflectogramEvent []re, int coord)
  {
    for(int i=0; i<re.length; i++)
    {
      if(re[i].beginLinear<=coord && re[i].endLinear>=coord)
      {
        return re[i].refAmplitude(coord);
      }
    }
    return 0.;
  }

  public static void setThresholds(ReflectogramEvent []etalon, Threshold []thresholds)
  {
    for(int i=0; i<etalon.length && i<thresholds.length; i++)
    {
      etalon[i].setThreshold(thresholds[i]);
    }
  }

  public static double []getArrayFromReflectogramEvents(ReflectogramEvent []re, int arrayLength)
  {
    correctEventsCoords(re);
    double []ret = new double[Math.max(re[re.length-1].endConnector, arrayLength)];

    for(int i=0; i<re.length; i++)
    {
      for(int j=re[i].beginLinear; j<=re[i].endConnector && j<ret.length; j++)
      {
        ret[j] = re[i].refAmplitude(j);
      }
    }

    for(int i=re[re.length-1].endConnector; i<ret.length; i++)
    {
      ret[i] = 0.;
    }

    return ret;
  }




  public static ReflectogramEvent []getThresholdReflectogramEventArray(ReflectogramEvent []etalon, int thresholdNumeral)
  {
    ReflectogramEvent []ret = new ReflectogramEvent[etalon.length];
    for(int i=0; i<ret.length; i++)
    {
      ret[i] = etalon[i].getThresholdReflectogramEvent(thresholdNumeral);
    }

    correctEventsCoords(ret);

    return ret;
  }





  public static int distanceToLastSplash(ReflectogramEvent []re)
  {
    for(int i=re.length-1; i>=0; i--)
    {
      if(re[i].connectorEvent == 1)
      {
        return re[i].beginConnector;
      }
    }
    return 0;
  }


  private static void shiftDataToEtalon(ReflectogramEvent []data, ReflectogramEvent []etalon)
  {
    if(etalon.length<1 || data.length<1)
      return;

    double maxData = -1000.;
    double maxEtalon = -1000.;

    for(int i=etalon[0].beginConnector; i<etalon[0].endConnector; i++)
    {
      if(maxEtalon<etalon[0].refAmplitude(i))
        maxEtalon=etalon[0].refAmplitude(i);
      if(maxData<data[0].refAmplitude(i))
        maxData=data[0].refAmplitude(i);
    }

    double dA = maxEtalon-maxData;

      for(int i=0; i<data.length; i++)
      {
        data[i].a1_connector+=dA;
        data[i].a2_connector+=dA;
        data[i].a_linear +=dA;
        data[i].a_weld+=dA;
      }
  }

  public static ReflectogramEvent []getDataShiftedToEtalon(ReflectogramEvent []data, ReflectogramEvent []etalon)
  {
    ReflectogramEvent []ret = new ReflectogramEvent[data.length];
    for(int i=0; i<data.length; i++)
    {
      ret[i] = data[i].copy();
    }
    shiftDataToEtalon(ret, etalon);
    return ret;
  }

  public static double getMaximumFromReflectogramEvents(ReflectogramEvent []re)
  {
    double ret = -1000.;

    if(re == null)
      return 0.;

    for(int i=0; i<re.length; i++)
    {
      for(int j=re[i].beginConnector; j<re[i].endConnector; j++)
      {
        if(ret<re[i].refAmplitude(j))
          ret = re[i].refAmplitude(j);
      }
    }

    return ret;
  }

  public static void correctEventsCoords(ReflectogramEvent []re)
  {
    if(re == null)
      return;

    for(int i=0; i<re.length; i++)
    {
      re[i].beginWeld = re[i].beginLinear = re[i].beginConnector;
      re[i].endWeld = re[i].endLinear = re[i].endConnector;
      if(re[i].connectorEvent == 1)
      {
        if(re[i].beginConnector<0)
        {
          re[i].beginConnector =
          re[i].beginLinear = re[i].beginWeld = 0;
        }

        if(i>0)
        {
          re[i-1].endConnector = re[i-1].endLinear = re[i-1].endWeld =
              re[i].beginConnector;
        }
        if(i<re.length-1)
        {
          re[i+1].beginConnector = re[i+1].beginLinear = re[i+1].beginWeld =
              re[i].endConnector;
        }
      }
    }
  }
}