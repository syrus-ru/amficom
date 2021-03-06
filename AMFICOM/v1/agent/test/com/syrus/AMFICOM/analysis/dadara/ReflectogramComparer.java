// The class is created to compare reflectogramms in using DADARA technology.
// Author: Levchenko Alexander S.
// mailto: levchenko@syrus.ru
// date: 4.11.2003
/*
package com.syrus.AMFICOM.analysis.dadara;

import java.util.ArrayList;

public class ReflectogramComparer
{
	boolean performShifting = false;

	private double []data;
	private ReflectogramEvent []reData;
	private ReflectogramEvent []etalon;

	private double []thUp1;
	private double []thUp2;
	private double []thDown1;
	private double []thDown2;

	private int minimalEventSize;
	private int length; //minimal length among all arrays;

	private ReflectogramAlarm []hardAlarms;
	private ReflectogramAlarm []softAlarms;


	public ReflectogramComparer(double []data, ReflectogramEvent []etalon, Threshold []thresholds, boolean performShifting)
	{
		this(data, etalon, performShifting);
		WorkWithReflectoEventsArray.setThresholds(this.etalon, thresholds);

		doIt();
	}

	public ReflectogramComparer(ReflectogramEvent []reData, ReflectogramEvent []etalon, Threshold []thresholds, boolean performShifting)
	{
		this(reData, etalon, performShifting);
		WorkWithReflectoEventsArray.setThresholds(this.etalon, thresholds);

		doIt();
	}

	public ReflectogramComparer(double []data, ReflectogramEvent []etalon, boolean performShifting)
	{
		this.reData = null;
		this.data = ReflectogramMath.correctReflectoArraySavingNoise(data);
		this.etalon = etalon;
		this.performShifting = performShifting;

		doIt();
	}

	public ReflectogramComparer(ReflectogramEvent []reData, ReflectogramEvent []etalon, boolean performShifting)
	{
		this.reData = reData;
		this.data = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(reData, etalon[etalon.length-1].endLinear);
		this.etalon = etalon;
		this.performShifting = performShifting;

		doIt();
	}



	private void doIt()
	{
		shiftDataToEtalon();// -->> NECESSARY, VERY IMPORTANT part of comparting!!!
		iniciateThresholds();
		iniciateMinimalEventSize();
		iniciateAlarms();
		iniciateAlarmsProperties();
	}

	private void shiftDataToEtalon()
	{
		if(data == null || etalon == null || etalon.length<1)
			return;

		double maxEtalon = -1000.;
		double maxData   = -1000.;

		for(int i=etalon[0].beginLinear; i<=etalon[0].endLinear && i<data.length; i++)
		{
			if(maxEtalon<etalon[0].refAmplitude(i))
				maxEtalon=etalon[0].refAmplitude(i);
			if(maxData<data[i])
				maxData = data[i];
		}

		//Here, we must shift data to the etalon;
		double dA = maxEtalon-maxData;

		for(int i=0; i<data.length; i++) //shifting of the data array;
		{
			data[i]+=dA;
		}

		if(reData != null) // shifting of the reflectogramm events;
		{
			for(int i=0; i<reData.length; i++)
			{
				reData[i].a1_connector+=dA;
				reData[i].a2_connector+=dA;
				reData[i].a_linear +=dA;
				reData[i].a_weld+=dA;
			}
		}


	}

	private void iniciateAlarmsProperties()
	{
		double []etalonArray = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(etalon, etalon[etalon.length-1].endLinear);

		for(int i=0; i<hardAlarms.length; i++) // iniciating of the hard alarms properties;
		{
			ReflectogramAlarm ra = hardAlarms[i];
			ra.setEventType(WorkWithReflectoEventsArray.getEventType(ra.alarmPointCoord, etalon));
			ra.refAmplChangeValue = ReflectogramMath.getMaximalDifference(etalonArray, data, ra.alarmPointCoord, ra.alarmEndPointCoord);

//      if(reData == null)
//      {
				ra.leftReflectoEventCoord = 0;
				ra.rightReflectoEventCoord = etalon[etalon.length-1].beginLinear;

				int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, etalon);
				for(int j=number+1; j<etalon.length; j++)
				{
					if(etalon[j].connectorEvent == 1)
					{
						ra.rightReflectoEventCoord = etalon[j].beginLinear;
					}
				}
				for(int j=number-1; j>=0; j--)
				{
					if(etalon[j].connectorEvent == 1)
					{
						ra.leftReflectoEventCoord = etalon[j].beginLinear;
					}
				}
//      }
//      else
//      {
//        ra.leftReflectoEventCoord = 0;
//        ra.rightReflectoEventCoord = reData[reData.length-1].beginLinear;
//
//        int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, reData);
//        for(int j=number+1; j<reData.length; j++)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.rightReflectoEventCoord = reData[j].beginLinear;
//          }
//        }
//        for(int j=number-1; j>=0; j--)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.leftReflectoEventCoord = reData[j].beginLinear;
//          }
//        }
//      }

			if(Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord)<Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord))
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord);
			}
			else
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord);
			}
		}



		for(int i=0; i<softAlarms.length; i++) // iniciating of the soft alarms properties;
		{
			ReflectogramAlarm ra = softAlarms[i];
			ra.setEventType(WorkWithReflectoEventsArray.getEventType(ra.alarmPointCoord, etalon));
			ra.refAmplChangeValue = ReflectogramMath.getMaximalDifference(etalonArray, data, ra.alarmPointCoord, ra.alarmEndPointCoord);

//      if(reData == null)
//      {
				ra.leftReflectoEventCoord = 0;
				ra.rightReflectoEventCoord = etalon[etalon.length-1].beginLinear;

				int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, etalon);
				for(int j=number+1; j<etalon.length; j++)
				{
					if(etalon[j].connectorEvent == 1)
					{
						ra.rightReflectoEventCoord = etalon[j].beginLinear;
					}
				}
				for(int j=number-1; j>=0; j--)
				{
					if(etalon[j].connectorEvent == 1)
					{
						ra.leftReflectoEventCoord = etalon[j].beginLinear;
					}
				}
//      }
//      else
//      {
//        ra.leftReflectoEventCoord = 0;
//        ra.rightReflectoEventCoord = reData[reData.length-1].beginLinear;
//
//        int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, reData);
//        for(int j=number+1; j<reData.length; j++)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.rightReflectoEventCoord = reData[j].beginLinear;
//          }
//        }
//        for(int j=number-1; j>=0; j--)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.leftReflectoEventCoord = reData[j].beginLinear;
//          }
//        }
//      }
//
			if(Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord)<Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord))
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord);
			}
			else
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord);
			}
		}

	}


	private void iniciateAlarms()
	{
		length = min(new int[] {data.length, thDown1.length, thDown2.length, thUp1.length, thUp2.length});
		ArrayList hard_Alarms = new ArrayList();
		ArrayList soft_Alarms = new ArrayList();

		int begin;
		int end;

		for(int i=0; i<length; i++) // iniciating of the hard alarms;
		{
			begin = 0;
			end = 0;
			if(data[i]>thUp2[i])
			{
				begin = i;
				while(data[i]>thUp2[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}
			else
			if(data[i]<thDown2[i])
			{
				begin = i;
				while(data[i]<thDown2[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}

			if(end-begin>minimalEventSize)
			{
				hard_Alarms.add(new ReflectogramAlarm(begin, ReflectogramAlarm.LEVEL_HARD, end));
			}
		}


		for(int i=0; i<length; i++) // iniciating of the soft alarms;
		{
			begin = 0;
			end = 0;
			if(data[i]>thUp1[i])
			{
				begin = i;
				while(data[i]>thUp1[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}
			else
			if(data[i]<thDown1[i])
			{
				begin = i;
				while(data[i]<thDown1[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}

			if(end-begin>minimalEventSize)
			{
				soft_Alarms.add(new ReflectogramAlarm(begin, ReflectogramAlarm.LEVEL_HARD, end));
			}
		}

		this.hardAlarms = (ReflectogramAlarm [])hard_Alarms.toArray(new ReflectogramAlarm [hard_Alarms.size()]);
		this.softAlarms = (ReflectogramAlarm [])soft_Alarms.toArray(new ReflectogramAlarm [soft_Alarms.size()]);
	}


	private void iniciateMinimalEventSize()
	{
		if(etalon[0].connectorEvent == 1)
		{
			minimalEventSize = (int)(etalon[0].width_connector/2);
		}
		else
		{
			minimalEventSize = ReflectogramMath.getEventSize(data, 0.5)/2;
		}

		if(minimalEventSize<3)
		{
			minimalEventSize = 3;
		}
	}


	private void iniciateThresholds()
	{
		thUp1 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.UP1),
				etalon[etalon.length-1].endLinear);

		thUp2 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.UP2),
				etalon[etalon.length-1].endLinear);

		thDown1 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.DOWN1),
				etalon[etalon.length-1].endLinear);

		thDown2 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.DOWN2),
				etalon[etalon.length-1].endLinear);
	}



	private int min(int []array)
	{
		int m = array[0];
		for(int i=0; i<array.length; i++)
		{
			if(m>array[i])
				m = array[i];
		}
		return m;
	}


	public ReflectogramAlarm []getHardAlarms()
	{
		return hardAlarms;
	}

	public ReflectogramAlarm []getSoftAlarms()
	{
		return softAlarms;
	}

	 public ReflectogramAlarm[] getAlarms()
	 {
		 ReflectogramAlarm []res = new ReflectogramAlarm[hardAlarms.length + softAlarms.length];
		 int n=0;
		 for(int i=0; i<hardAlarms.length; i++)
		 {
			 res[n] = hardAlarms[i];
			 n++;
		 }
		 for(int i=0; i<softAlarms.length; i++)
		 {
			 res[n] = softAlarms[i];
			 n++;
		 }
		 return res;
	 }







// Statical methods definition
//-----------------------------------------------------------------------------
	 public static int []getNewEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon)
	 {
		 if(data == null || etalon == null)
			 return new int[0];

		 int []eventsList = null;
		 ArrayList v = new ArrayList();

		 for(int i=0; i<data.length; i++)
		 {
			 int coord = WorkWithReflectoEventsArray.getEventCoord(data[i]);
			 int type = WorkWithReflectoEventsArray.getEventTypeByNumber(i, data);
			 if(WorkWithReflectoEventsArray.getEventType(coord, etalon) != type)
			 {
				 v.add(new Integer(i));
			 }
		 }

		 eventsList = new int[v.size()];

		 for(int i=0; i<eventsList.length; i++)
		 {
			 eventsList[i] = ((Integer)v.get(i)).intValue();
		 }
		 return eventsList;
	 }


//-----------------------------------------------------------------------------
	 public static int []getLostEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon)
	 {
		 return getNewEventsList(etalon, data);
	 }


//-----------------------------------------------------------------------------
	 public static int []getChangedLossEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon, double changeThreshold)
	 {
		 if(data == null || etalon == null)
			 return new int[0];

		 int []eventsList = null;
		 ArrayList v = new ArrayList();

		 for(int i=0; i<data.length; i++)
		 {
			 int coord = WorkWithReflectoEventsArray.getEventCoord(data[i]);
			 int type = WorkWithReflectoEventsArray.getEventType(data[i]);
			 ReflectogramEvent et = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
			 if(WorkWithReflectoEventsArray.getEventType(et) == type) // Event is the same!;
			 {
				 if(type == ReflectogramEvent.CONNECTOR)
				 {
					 if(Math.abs( (data[i].a1_connector-data[i].a2_connector) -
												(et.a1_connector-et.a2_connector) )>changeThreshold
												 && i>0 && i<data.length-1)
					 {
						 v.add(new Integer(i));
					 }
				 }
				 else if(type == ReflectogramEvent.WELD)
				 {
					 if(Math.abs(data[i].boost_weld - et.boost_weld)>changeThreshold)
					 {
						 v.add(new Integer(i));
					 }
				 }
			 }
		 }

		 eventsList = new int[v.size()];

		 for(int i=0; i<eventsList.length; i++)
		 {
			 eventsList[i] = ((Integer)v.get(i)).intValue();
		 }
		 return eventsList;
	 }



//-----------------------------------------------------------------------------
	 public static int []getChangedAmplitudeEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon, double changeThreshold)
	 {
		 if(data == null || etalon == null)
			 return new int[0];

		 int []eventsList = null;
		 ArrayList v = new ArrayList();

		 for(int i=0; i<data.length; i++)
		 {
			 int coord = WorkWithReflectoEventsArray.getEventCoord(data[i]);
			 int type = WorkWithReflectoEventsArray.getEventType(data[i]);
			 ReflectogramEvent et = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
			 if(WorkWithReflectoEventsArray.getEventType(et) == type) // Event is the same!;
			 {
				 if(type == ReflectogramEvent.CONNECTOR)
				 {
					 if(Math.abs(data[i].a1_connector-et.a1_connector)>changeThreshold && i>0)
					 {
						 v.add(new Integer(i));
					 }
					 else if(Math.abs(data[i].a2_connector-et.a2_connector)>changeThreshold && i<data.length-1)
					 {
						 v.add(new Integer(i));
					 }
				 }
				 else if(type == ReflectogramEvent.WELD)
				 {
					 if(Math.abs(data[i].a_weld - et.a_weld)>changeThreshold)
					 {
						 v.add(new Integer(i));
					 }
				 }
				 else if(type == ReflectogramEvent.LINEAR)
				 {
					 if(Math.abs(data[i].a_linear - et.a_linear)>changeThreshold)
					 {
						 v.add(new Integer(i));
					 }
				 }
			 }
		 }

		 eventsList = new int[v.size()];

		 for(int i=0; i<eventsList.length; i++)
		 {
			 eventsList[i] = ((Integer)v.get(i)).intValue();
		 }
		 return eventsList;
	 }



//-----------------------------------------------------------------------------
	 public static double getMaximalDeviation(ReflectogramEvent []etalon, ReflectogramEvent []data)
	 {
		 if(data == null || etalon == null)
			 return 0.;

		 double ret = 0.;
		 double a1;
		 double a2;

		 for(int i=0; i<data[data.length-1].beginLinear && i<etalon[etalon.length-1].beginLinear; i++)
		 {
			 a1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, i);
			 a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
			 if(Math.abs(ret) < Math.abs(a1-a2))
			 {
				 ret = a1-a2;
			 }
		 }
		 return ret;
	 }
//-----------------------------------------------------------------------------
	 public static double getLossDifference(ReflectogramEvent []etalon, ReflectogramEvent []data)
	 {
		 double a1, a2;
		 double b1, b2;

		 int length1 = WorkWithReflectoEventsArray.distanceToLastSplash(data);
		 int length2 = WorkWithReflectoEventsArray.distanceToLastSplash(etalon);

		 int  c = data[0].endLinear;
		 a1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, c);
		 a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, length1);
		 b1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, c);
		 b2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, length2);

		 double ret = (a1-a2) - (b1-b2);

		 return ret;
	 }


//-----------------------------------------------------------------------------
	 public static double getMeanDeviation(ReflectogramEvent []etalon, ReflectogramEvent []data)
	 {
		 if(data == null || etalon == null)
			 return 0.;

		 double ret = 0.;
		 double a1;
		 double a2;
		 int norma = 0;

		 for(int i=0; i<data[data.length-1].beginLinear && i<etalon[etalon.length-1].beginLinear; i++)
		 {
			 a1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, i);
			 a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
				 ret += (a1-a2);
				 norma ++;
		 }

		 if(norma>0)
			 ret/=norma;

		 return ret;
	 }



//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
	 public static double getDeviation(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 if(data == null || etalon == null)
			 return 0.;

		 double ret = 0.;

		 for(int i=data[nEvent].beginLinear; i<data[nEvent].endLinear; i++)
		 {
			 if(i<etalon[etalon.length-1].endLinear)
			 {
				 double a1 = data[nEvent].refAmplitude(i);
				 double a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
				 if(Math.abs(ret)<Math.abs(a1-a2))
				 {
					 ret = a1-a2;
				 }
			 }
		 }

		 return ret;
	 }

//-----------------------------------------------------------------------------
	 public static double getMeanDeviation(ReflectogramEvent []data, ReflectogramEvent []etalon, int nEvent)
	 {
		 double ret = 0.;
		 int norma = 0;
		 for(int i=data[nEvent].beginLinear; i<data[nEvent].endLinear; i++)
		 {
			 if(i<etalon[etalon.length-1].endLinear)
			 {
				 double a1 = data[nEvent].refAmplitude(i);
				 double a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
				 ret += (a1-a2);
				 norma ++;
			 }
		 }
		 if(norma>0)
			 ret = ret/norma;

		 return ret;
	 }

//-----------------------------------------------------------------------------
	 public static double getLoss(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 if(data == null || etalon == null)
			 return 0.;


		 ReflectogramEvent d = data[nEvent];
		 int coord = WorkWithReflectoEventsArray.getEventCoord(d);
		 ReflectogramEvent e = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
		 if(e == null)
			 return 0.;

		 int type = WorkWithReflectoEventsArray.getEventType(d);
		 if(WorkWithReflectoEventsArray.getEventType(e)!=type)
			 return 0.;

		 if(type == ReflectogramEvent.LINEAR)
		 {
			 double a1 = d.b_linear*(d.endLinear-d.beginLinear);
			 double a2 = e.b_linear*(e.endLinear-e.beginLinear);
			 return a1-a2;
		 }
		 if(type == ReflectogramEvent.CONNECTOR && nEvent>0)
		 {
			 double a1 = d.a1_connector-d.a2_connector;
			 double a2 = e.a1_connector-e.a2_connector;
			 return a1-a2;
		 }
		 if(type == ReflectogramEvent.WELD)
		 {
			 double a1 = d.boost_weld;
			 double a2 = e.boost_weld;
			 return a1-a2;
		 }
		 return 0.;
	 }


//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
	 public static double getLocationDifference(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 int coord = WorkWithReflectoEventsArray.getEventCoord(data[nEvent]);
		 ReflectogramEvent e = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
		 if(e == null)
			 return 0.;

		 int coord2 = WorkWithReflectoEventsArray.getEventCoord(e);

		 return coord-coord2;
	 }


//-----------------------------------------------------------------------------
	 public static double getWidthDifference(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 int coord = WorkWithReflectoEventsArray.getEventCoord(data[nEvent]);
		 ReflectogramEvent e = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
		 if(e == null)
			 return 0.;

		 int type = WorkWithReflectoEventsArray.getEventType(data[nEvent]);
		 if(type != WorkWithReflectoEventsArray.getEventType(e))
			 return 0.;

		 if(type == ReflectogramEvent.LINEAR)
		 {
			 double w1 = data[nEvent].endLinear-data[nEvent].beginLinear;
			 double w2 = e.endLinear-e.beginLinear;
			 return w1-w2;
		 }
		 if(type == ReflectogramEvent.CONNECTOR)
		 {
			 double w1 = data[nEvent].width_connector;
			 double w2 = e.width_connector;
			 return w1-w2;
		 }
		 if(type == ReflectogramEvent.WELD)
		 {
			 double w1 = data[nEvent].width_weld;
			 double w2 = e.width_weld;
			 return w1-w2;
		 }
		 return 0.;
	 }


}
*/




package com.syrus.AMFICOM.analysis.dadara;

import java.util.ArrayList;

public class ReflectogramComparer
{
	boolean performShifting = false;

	private double []data;
	private ReflectogramEvent []reData;
	private ReflectogramEvent []etalon;

	private double []thUp1;
	private double []thUp2;
	private double []thDown1;
	private double []thDown2;

	private int minimalEventSize;
	private int length; //minimal length among all arrays;

	private ReflectogramAlarm []hardAlarms;
	private ReflectogramAlarm []softAlarms;


	public ReflectogramComparer(double []data, ReflectogramEvent []etalon, Threshold []thresholds, boolean performShifting)
	{
		this(data, etalon, performShifting);
		WorkWithReflectoEventsArray.setThresholds(this.etalon, thresholds);

		doIt();
	}

	public ReflectogramComparer(ReflectogramEvent []reData, ReflectogramEvent []etalon, Threshold []thresholds, boolean performShifting)
	{
		this(reData, etalon, performShifting);
		WorkWithReflectoEventsArray.setThresholds(this.etalon, thresholds);

		doIt();
	}

	public ReflectogramComparer(double []data, ReflectogramEvent []etalon, boolean performShifting)
	{
		this.reData = null;
		this.data = ReflectogramMath.correctReflectoArraySavingNoise(data);
		this.etalon = etalon;
		this.performShifting = performShifting;

		doIt();
	}

	public ReflectogramComparer(ReflectogramEvent []reData, ReflectogramEvent []etalon, boolean performShifting)
	{
		this.reData = reData;
		this.data = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(reData, etalon[etalon.length-1].end);
		this.etalon = etalon;
		this.performShifting = performShifting;

		doIt();
	}



	private void doIt()
	{
		shiftDataToEtalon();// -->> NECESSARY, VERY IMPORTANT part of comparting!!!
		iniciateThresholds();
		iniciateMinimalEventSize();
		iniciateAlarms();
		iniciateAlarmsProperties();
	}

	private void shiftDataToEtalon()
	{
		if(data == null || etalon == null || etalon.length<1)
			return;

		double maxEtalon = -1000.;
		double maxData   = -1000.;

		for(int i=etalon[0].begin; i<=etalon[0].end && i<data.length; i++)
		{
			if(maxEtalon<etalon[0].refAmplitude(i))
				maxEtalon=etalon[0].refAmplitude(i);
			if(maxData<data[i])
				maxData = data[i];
		}

		//Here, we must shift data to the etalon;
		double dA = maxEtalon-maxData;

		for(int i=0; i<data.length; i++) //shifting of the data array;
		{
			data[i]+=dA;
		}

		if(reData != null) // shifting of the reflectogramm events;
		{
			for(int i=0; i<reData.length; i++)
			{
				reData[i].a1_connector+=dA;
				reData[i].a2_connector+=dA;
				reData[i].a_linear +=dA;
				reData[i].a_weld+=dA;
			}
		}


	}

	private void iniciateAlarmsProperties()
	{
		double []etalonArray = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(etalon, etalon[etalon.length-1].end);

		for(int i=0; i<hardAlarms.length; i++) // iniciating of the hard alarms properties;
		{
			ReflectogramAlarm ra = hardAlarms[i];
			ra.setEventType(WorkWithReflectoEventsArray.getEventType(ra.alarmPointCoord, etalon));
			ra.refAmplChangeValue = ReflectogramMath.getMaximalDifference(etalonArray, data, ra.alarmPointCoord, ra.alarmEndPointCoord);

//      if(reData == null)
//      {
				ra.leftReflectoEventCoord = 0;
				ra.rightReflectoEventCoord = etalon[etalon.length-1].begin;

				int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, etalon);
				for(int j=number+1; j<etalon.length; j++)
				{
					if(etalon[j].type == ReflectogramEvent.CONNECTOR)
					{
						ra.rightReflectoEventCoord = etalon[j].begin;
					}
				}
				for(int j=number-1; j>=0; j--)
				{
					if(etalon[j].type == ReflectogramEvent.CONNECTOR)
					{
						ra.leftReflectoEventCoord = etalon[j].begin;
					}
				}
//      }
//      else
//      {
//        ra.leftReflectoEventCoord = 0;
//        ra.rightReflectoEventCoord = reData[reData.length-1].begin;
//
//        int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, reData);
//        for(int j=number+1; j<reData.length; j++)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.rightReflectoEventCoord = reData[j].begin;
//          }
//        }
//        for(int j=number-1; j>=0; j--)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.leftReflectoEventCoord = reData[j].begin;
//          }
//        }
//      }

			if(Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord)<Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord))
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord);
			}
			else
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord);
			}
		}



		for(int i=0; i<softAlarms.length; i++) // iniciating of the soft alarms properties;
		{
			ReflectogramAlarm ra = softAlarms[i];
			ra.setEventType(WorkWithReflectoEventsArray.getEventType(ra.alarmPointCoord, etalon));
			ra.refAmplChangeValue = ReflectogramMath.getMaximalDifference(etalonArray, data, ra.alarmPointCoord, ra.alarmEndPointCoord);

//      if(reData == null)
//      {
				ra.leftReflectoEventCoord = 0;
				ra.rightReflectoEventCoord = etalon[etalon.length-1].begin;

				int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, etalon);
				for(int j=number+1; j<etalon.length; j++)
				{
					if(etalon[j].type == ReflectogramEvent.CONNECTOR)
					{
						ra.rightReflectoEventCoord = etalon[j].begin;
					}
				}
				for(int j=number-1; j>=0; j--)
				{
					if(etalon[j].type == ReflectogramEvent.CONNECTOR)
					{
						ra.leftReflectoEventCoord = etalon[j].begin;
					}
				}
//      }
//      else
//      {
//        ra.leftReflectoEventCoord = 0;
//        ra.rightReflectoEventCoord = reData[reData.length-1].begin;
//
//        int number = WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord, reData);
//        for(int j=number+1; j<reData.length; j++)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.rightReflectoEventCoord = reData[j].begin;
//          }
//        }
//        for(int j=number-1; j>=0; j--)
//        {
//          if(reData[j].connectorEvent == 1)
//          {
//            ra.leftReflectoEventCoord = reData[j].begin;
//          }
//        }
//      }
//
			if(Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord)<Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord))
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.leftReflectoEventCoord);
			}
			else
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord-ra.rightReflectoEventCoord);
			}
		}

	}


	private void iniciateAlarms()
	{
		length = min(new int[] {data.length, thDown1.length, thDown2.length, thUp1.length, thUp2.length});
		ArrayList hard_Alarms = new ArrayList();
		ArrayList soft_Alarms = new ArrayList();

		int begin;
		int end;

		for(int i=0; i<length; i++) // iniciating of the hard alarms;
		{
			begin = 0;
			end = 0;
			if(data[i]>thUp2[i])
			{
				begin = i;
				while(data[i]>thUp2[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}
			else
			if(data[i]<thDown2[i])
			{
				begin = i;
				while(data[i]<thDown2[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}

			if(end-begin>minimalEventSize)
			{
				hard_Alarms.add(new ReflectogramAlarm(begin, ReflectogramAlarm.LEVEL_HARD, end));
			}
		}


		for(int i=0; i<length; i++) // iniciating of the soft alarms;
		{
			begin = 0;
			end = 0;
			if(data[i]>thUp1[i])
			{
				begin = i;
				while(data[i]>thUp1[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}
			else
			if(data[i]<thDown1[i])
			{
				begin = i;
				while(data[i]<thDown1[i] && i<length-1)
				{
					i++;
				}
				end = i;
			}

			if(end-begin>minimalEventSize)
			{
				soft_Alarms.add(new ReflectogramAlarm(begin, ReflectogramAlarm.LEVEL_HARD, end));
			}
		}

		this.hardAlarms = (ReflectogramAlarm [])hard_Alarms.toArray(new ReflectogramAlarm [hard_Alarms.size()]);
		this.softAlarms = (ReflectogramAlarm [])soft_Alarms.toArray(new ReflectogramAlarm [soft_Alarms.size()]);
	}


	private void iniciateMinimalEventSize()
	{
		if(etalon[0].type == ReflectogramEvent.CONNECTOR)
		{
			minimalEventSize = (int)(etalon[0].width_connector/2);
		}
		else
		{
			minimalEventSize = ReflectogramMath.getEventSize(data, 0.5)/2;
		}

		if(minimalEventSize<3)
		{
			minimalEventSize = 3;
		}
	}


	private void iniciateThresholds()
	{
		thUp1 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.UP1),
				etalon[etalon.length-1].end);

		thUp2 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.UP2),
				etalon[etalon.length-1].end);

		thDown1 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.DOWN1),
				etalon[etalon.length-1].end);

		thDown2 = WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(
				WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(etalon, Threshold.DOWN2),
				etalon[etalon.length-1].end);
	}



	private int min(int []array)
	{
		int m = array[0];
		for(int i=0; i<array.length; i++)
		{
			if(m>array[i])
				m = array[i];
		}
		return m;
	}


	public ReflectogramAlarm []getHardAlarms()
	{
		return hardAlarms;
	}

	public ReflectogramAlarm []getSoftAlarms()
	{
		return softAlarms;
	}

	 public ReflectogramAlarm[] getAlarms()
	 {
		 ReflectogramAlarm []res = new ReflectogramAlarm[hardAlarms.length + softAlarms.length];
		 int n=0;
		 for(int i=0; i<hardAlarms.length; i++)
		 {
			 res[n] = hardAlarms[i];
			 n++;
		 }
		 for(int i=0; i<softAlarms.length; i++)
		 {
			 res[n] = softAlarms[i];
			 n++;
		 }
		 return res;
	 }







// Statical methods definition
//-----------------------------------------------------------------------------
	 public static int []getNewEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon)
	 {
		 if(data == null || etalon == null)
			 return new int[0];

		 int []eventsList = null;
		 ArrayList v = new ArrayList();

		 for(int i=0; i<data.length; i++)
		 {
			 int coord = WorkWithReflectoEventsArray.getEventCoord(data[i]);
			 int type = WorkWithReflectoEventsArray.getEventTypeByNumber(i, data);
			 if(WorkWithReflectoEventsArray.getEventType(coord, etalon) != type)
			 {
				 v.add(new Integer(i));
			 }
		 }

		 eventsList = new int[v.size()];

		 for(int i=0; i<eventsList.length; i++)
		 {
			 eventsList[i] = ((Integer)v.get(i)).intValue();
		 }
		 return eventsList;
	 }


//-----------------------------------------------------------------------------
	 public static int []getLostEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon)
	 {
		 return getNewEventsList(etalon, data);
	 }


//-----------------------------------------------------------------------------
	 public static int []getChangedLossEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon, double changeThreshold)
	 {
		 if(data == null || etalon == null)
			 return new int[0];

		 int []eventsList = null;
		 ArrayList v = new ArrayList();

		 for(int i=0; i<data.length; i++)
		 {
			 int coord = WorkWithReflectoEventsArray.getEventCoord(data[i]);
			 int type = WorkWithReflectoEventsArray.getEventType(data[i]);
			 ReflectogramEvent et = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
			 if(WorkWithReflectoEventsArray.getEventType(et) == type) // Event is the same!;
			 {
				 if(type == ReflectogramEvent.CONNECTOR)
				 {
					 if(Math.abs( (data[i].a1_connector-data[i].a2_connector) -
												(et.a1_connector-et.a2_connector) )>changeThreshold
												 && i>0 && i<data.length-1)
					 {
						 v.add(new Integer(i));
					 }
				 }
				 else if(type == ReflectogramEvent.WELD)
				 {
					 if(Math.abs(data[i].boost_weld - et.boost_weld)>changeThreshold)
					 {
						 v.add(new Integer(i));
					 }
				 }
			 }
		 }

		 eventsList = new int[v.size()];

		 for(int i=0; i<eventsList.length; i++)
		 {
			 eventsList[i] = ((Integer)v.get(i)).intValue();
		 }
		 return eventsList;
	 }



//-----------------------------------------------------------------------------
	 public static int []getChangedAmplitudeEventsList(ReflectogramEvent []data, ReflectogramEvent []etalon, double changeThreshold)
	 {
		 if(data == null || etalon == null)
			 return new int[0];

		 int []eventsList = null;
		 ArrayList v = new ArrayList();

		 for(int i=0; i<data.length; i++)
		 {
			 int coord = WorkWithReflectoEventsArray.getEventCoord(data[i]);
			 int type = WorkWithReflectoEventsArray.getEventType(data[i]);
			 ReflectogramEvent et = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
			 if(WorkWithReflectoEventsArray.getEventType(et) == type) // Event is the same!;
			 {
				 if(type == ReflectogramEvent.CONNECTOR)
				 {
					 if(Math.abs(data[i].a1_connector-et.a1_connector)>changeThreshold && i>0)
					 {
						 v.add(new Integer(i));
					 }
					 else if(Math.abs(data[i].a2_connector-et.a2_connector)>changeThreshold && i<data.length-1)
					 {
						 v.add(new Integer(i));
					 }
				 }
				 else if(type == ReflectogramEvent.WELD)
				 {
					 if(Math.abs(data[i].a_weld - et.a_weld)>changeThreshold)
					 {
						 v.add(new Integer(i));
					 }
				 }
				 else if(type == ReflectogramEvent.LINEAR)
				 {
					 if(Math.abs(data[i].a_linear - et.a_linear)>changeThreshold)
					 {
						 v.add(new Integer(i));
					 }
				 }
			 }
		 }

		 eventsList = new int[v.size()];

		 for(int i=0; i<eventsList.length; i++)
		 {
			 eventsList[i] = ((Integer)v.get(i)).intValue();
		 }
		 return eventsList;
	 }



//-----------------------------------------------------------------------------
	 public static double getMaximalDeviation(ReflectogramEvent []etalon, ReflectogramEvent []data)
	 {
		 if(data == null || etalon == null)
			 return 0.;

		 double ret = 0.;
		 double a1;
		 double a2;

		 for(int i=0; i<data[data.length-1].begin && i<etalon[etalon.length-1].begin; i++)
		 {
			 a1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, i);
			 a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
			 if(Math.abs(ret) < Math.abs(a1-a2))
			 {
				 ret = a1-a2;
			 }
		 }
		 return ret;
	 }
//-----------------------------------------------------------------------------
	 public static double getLossDifference(ReflectogramEvent []etalon, ReflectogramEvent []data)
	 {
		 double a1, a2;
		 double b1, b2;

		 int length1 = WorkWithReflectoEventsArray.distanceToLastSplash(data);
		 int length2 = WorkWithReflectoEventsArray.distanceToLastSplash(etalon);

		 int  c = data[0].end;
		 a1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, c);
		 a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, length1);
		 b1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, c);
		 b2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, length2);

		 double ret = (a1-a2) - (b1-b2);

		 return ret;
	 }


//-----------------------------------------------------------------------------
	 public static double getMeanDeviation(ReflectogramEvent []etalon, ReflectogramEvent []data)
	 {
		 if(data == null || etalon == null)
			 return 0.;

		 double ret = 0.;
		 double a1;
		 double a2;
		 int norma = 0;

		 for(int i=0; i<data[data.length-1].begin && i<etalon[etalon.length-1].begin; i++)
		 {
			 a1 = WorkWithReflectoEventsArray.getAmplitudeByCoord(data, i);
			 a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
				 ret += (a1-a2);
				 norma ++;
		 }

		 if(norma>0)
			 ret/=norma;

		 return ret;
	 }



//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
	 public static double getDeviation(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 if(data == null || etalon == null)
			 return 0.;

		 double ret = 0.;

		 for(int i=data[nEvent].begin; i<data[nEvent].end; i++)
		 {
			 if(i<etalon[etalon.length-1].end)
			 {
				 double a1 = data[nEvent].refAmplitude(i);
				 double a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
				 if(Math.abs(ret)<Math.abs(a1-a2))
				 {
					 ret = a1-a2;
				 }
			 }
		 }

		 return ret;
	 }

//-----------------------------------------------------------------------------
	 public static double getMeanDeviation(ReflectogramEvent []data, ReflectogramEvent []etalon, int nEvent)
	 {
		 double ret = 0.;
		 int norma = 0;
		 for(int i=data[nEvent].begin; i<data[nEvent].end; i++)
		 {
			 if(i<etalon[etalon.length-1].end)
			 {
				 double a1 = data[nEvent].refAmplitude(i);
				 double a2 = WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i);
				 ret += (a1-a2);
				 norma ++;
			 }
		 }
		 if(norma>0)
			 ret = ret/norma;

		 return ret;
	 }

//-----------------------------------------------------------------------------
	 public static double getLoss(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 if(data == null || etalon == null)
			 return 0.;


		 ReflectogramEvent d = data[nEvent];
		 int coord = WorkWithReflectoEventsArray.getEventCoord(d);
		 ReflectogramEvent e = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
		 if(e == null)
			 return 0.;

		 int type = WorkWithReflectoEventsArray.getEventType(d);
		 if(WorkWithReflectoEventsArray.getEventType(e)!=type)
			 return 0.;

		 if(type == ReflectogramEvent.LINEAR)
		 {
			 double a1 = d.b_linear*(d.end-d.begin);
			 double a2 = e.b_linear*(e.end-e.begin);
			 return a1-a2;
		 }
		 if(type == ReflectogramEvent.CONNECTOR && nEvent>0)
		 {
			 double a1 = d.a1_connector-d.a2_connector;
			 double a2 = e.a1_connector-e.a2_connector;
			 return a1-a2;
		 }
		 if(type == ReflectogramEvent.WELD)
		 {
			 double a1 = d.boost_weld;
			 double a2 = e.boost_weld;
			 return a1-a2;
		 }
		 return 0.;
	 }


//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
	 public static double getLocationDifference(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 int coord = WorkWithReflectoEventsArray.getEventCoord(data[nEvent]);
		 ReflectogramEvent e = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
		 if(e == null)
			 return 0.;

		 int coord2 = WorkWithReflectoEventsArray.getEventCoord(e);

		 return coord-coord2;
	 }


//-----------------------------------------------------------------------------
	 public static double getWidthDifference(ReflectogramEvent []etalon, ReflectogramEvent []data, int nEvent)
	 {
		 int coord = WorkWithReflectoEventsArray.getEventCoord(data[nEvent]);
		 ReflectogramEvent e = WorkWithReflectoEventsArray.getReflectogrammEventByCoord(coord, etalon);
		 if(e == null)
			 return 0.;

		 int type = WorkWithReflectoEventsArray.getEventType(data[nEvent]);
		 if(type != WorkWithReflectoEventsArray.getEventType(e))
			 return 0.;

		 if(type == ReflectogramEvent.LINEAR)
		 {
			 double w1 = data[nEvent].end-data[nEvent].begin;
			 double w2 = e.end-e.begin;
			 return w1-w2;
		 }
		 if(type == ReflectogramEvent.CONNECTOR)
		 {
			 double w1 = data[nEvent].width_connector;
			 double w2 = e.width_connector;
			 return w1-w2;
		 }
		 if(type == ReflectogramEvent.WELD)
		 {
			 double w1 = data[nEvent].width_weld;
			 double w2 = e.width_weld;
			 return w1-w2;
		 }
		 return 0.;
	 }


}























// DO NOT DELETE THIS COMMENT!!!

/*
ReflectogramEvent linearEvent = null;

for(int i=0; i<etalon.length; i++)
{
	if(etalon[i].linearEvent == 1 && etalon[i].end-etalon[i].begin>3) //at list, three points needed to get total shift;
	{
		linearEvent = etalon[i];
		break;
	}
}

double meanShift = 0.;
int norma = 0;

if(linearEvent!=null)
{
	for(int i=linearEvent.begin; i<=linearEvent.end ; i++)
	{
		meanShift = meanShift+(data[i]-linearEvent.refAmplitude(i));
		norma++;
	}
}
else
{
	for(int i=minimalEventSize*3; i<=minimalEventSize*3+10; i++)
	{
		meanShift = meanShift+(data[i]-WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i));
		norma++;
	}
}

meanShift = meanShift/norma;// So, <data> = <etalon>+meanShift and
														// <threshold>  --> <threshold>+meanShift;

*/