package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.util.Log;

public class ReflectogramMath
{


  public ReflectogramMath()
  {
  }


  public static double[] getDerivative(double []data)
  {
    double []ret = new double[data.length];

    for(int i=0; i<data.length-1; i++)
    {
      ret[i] = data[i+1]-data[i];
    }
    ret[data.length-1]=0.;
    return ret;
  }



  public static int getEventSize(double []data, double widthLevel)
  {
    int maxWidth = 300;
    int eventSize=0;
    double max=data[0];
    int j=2;
    for(int i=0; i<maxWidth; i++)
    {
      if(max<data[i])
      {
        max = data[i];
        j=i;
      }
    }
    for(int i=j; i<maxWidth; i++)
    {
      if(data[i]+widthLevel<max)
      {
        eventSize=i;
        break;
      }
    }
    if(eventSize<2) eventSize=2;
    return eventSize;
  }

  public static double getMaximalDifference(double []etalon, double []data, int begin, int end)
  {
    double shift = 0.;

    for(int i=begin; i<=end && i<data.length && i<etalon.length; i++)
    {
      if(Math.abs(shift)<Math.abs(data[i]-etalon[i]))
      {
        shift = data[i]-etalon[i];
      }
    }

    return shift;
  }

  public static double []correctReflectoArraySavingNoise(double []data)
  {
    int i;
    int begin=300;
    if(begin>data.length/2) begin=data.length/2;
    double min=data[begin];

    for(i=begin; i<data.length; i++)
    {
      if(min>data[i]) min = data[i];
    }
    for(i=0; i<data.length; i++)
    {
      data[i] = data[i]-min;
    }
    for(i=0; i<=begin; i++)
    {
      if(data[i]<0.) data[i]=0.;
    }

    if(data[0]>0.001) data[0] = 0.;
    if(data[1]<0.001) data[1] = data[2]/2.;
    return data;
  }

  public static double getRefAbsoluteShift(double []reference, double []data)
  {
    // The function returns difference <reference> - <data>
    // In case of error, the function will return <1000>
    double ret;
    int eventSizeRef = getEventSize(reference, 0.2);
    int eventSizeDat = getEventSize(data, 0.2);
    int eventSize = Math.max(eventSizeRef, eventSizeDat);

    eventSize = (int)(eventSize*1.8);

    double shift = 0.;
    double norma = 0.;

    double previousValue = reference[eventSize];
    for(int i=eventSize;
        i<eventSize+300 && i<reference.length && i<data.length;
        i++)
    {
      if(Math.abs(reference[i] - previousValue)<0.1)
      {
      previousValue = reference[i];
      shift = shift + (reference[i]-data[i]);
      norma = norma+1.;
    }
    else
    {
      i+=eventSize;
      if(i>reference.length-2) break;
      previousValue = reference[i];
    }

    }

    if(norma>10.)
    {
      ret = shift/norma;
    }
    else
    {
      Log.errorMessage("Error getting reflectogramm absolute shift");
      ret = 1000.;
    }

    return ret;
  }


}