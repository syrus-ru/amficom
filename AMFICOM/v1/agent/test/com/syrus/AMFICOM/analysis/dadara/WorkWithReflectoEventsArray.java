package com.syrus.AMFICOM.analysis.dadara;

public class WorkWithReflectoEventsArray
{
	public WorkWithReflectoEventsArray()
	{
	}

	public static ReflectogramEvent getReflectogrammEventByCoord(int coord, ReflectogramEvent []re)
	{
		if(re == null)
			return null;

		for(int i=0; i<re.length; i++)
		{
			if(re[i].begin <= coord && re[i].end>=coord)
			{
				return re[i];
			}
		}
		return null;
	}


	public static int getEventTypeByNumber(int nEvent, ReflectogramEvent []re)
	{
		return re[nEvent].getType();
	}

	public static int getEventType(ReflectogramEvent re)
	{
		return re.getType();
	}


	public static int getEventType(int coord, ReflectogramEvent []re)
	{
		for(int i=0; i<re.length; i++)
		{
			if(re[i].begin<= coord && re[i].end>=coord)
				return re[i].getType();
		}
		return 0;
	}




	public static  int getEventNumber(int coord, ReflectogramEvent []re)
	{
		for(int i=0; i<re.length; i++)
		{
			if(re[i].begin<= coord && re[i].end>=coord)
				return i;
		}
		return -1;
	}


	public static int getEventCoord(ReflectogramEvent re)
	{
		int coord;
		if(re.type == ReflectogramEvent.CONNECTOR)
		{
			coord = (int)(re.center_connector);
		}
		else if(re.type == ReflectogramEvent.WELD)
		{
			coord = (int)(re.center_weld);
		}
		else
		{
			coord = (re.begin+re.end)/2;
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
			if(re[i].begin<=coord && re[i].end>=coord)
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
		double []ret = new double[Math.max(re[re.length-1].end, arrayLength)];

		for(int i=0; i<re.length; i++)
		{
			for(int j=re[i].begin; j<=re[i].end && j<ret.length; j++)
			{
				ret[j] = re[i].refAmplitude(j);
			}
		}

		for(int i=re[re.length-1].end; i<ret.length; i++)
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
			if(re[i].type == ReflectogramEvent.CONNECTOR)
			{
				return re[i].begin;
			}
		}
		return 0;
	}


	public static void shiftDataToEtalon(ReflectogramEvent []data, ReflectogramEvent []etalon)
	{
		if(etalon.length<1 || data.length<1)
			return;

		double maxData = -1000.;
		double maxEtalon = -1000.;

		for(int i=etalon[0].begin; i<etalon[0].end; i++)
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
			for(int j=re[i].begin; j<re[i].end; j++)
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
			if(re[i].type == ReflectogramEvent.CONNECTOR)
			{
				if(re[i].begin<0)
				{
					re[i].begin = 0;
				}

				if(i>0)
				{
					re[i-1].end =	re[i].begin;
				}
				if(i<re.length-1)
				{
					re[i+1].begin =	re[i].end;
				}
			}
		}
	}
}