package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.analysis.dadara.Histogramm;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ShortReflectogramEvent;
import com.syrus.io.BellcoreStructure;
// Author: Alexandre S. Levchenko


public class ReflectoEventStatistics
{
	public static final int waveLength1310 = 1310;
	public static final int waveLength1550 = 1550;
	public static final int waveLength1625 = 1625;
	public int waveLength = 1550;
	private static final int nBins = 70;

	private final String tdd = "tdd";
	private final String dimensionName = "dimensionName";
	private final String linFit = "linFit";
	private final String histo = "histo";

	public ReflectoEventContainer []statData;
	public ReflectoEventContainer reference;
	public double []referenceArray;

	private double []meanReflectogramm;
	private int nReflectogrammsForAveraging=0;


	public double delta_x=0;
	public double maxRefValue=0;

	double []values; // Helping array;

	private Hashtable attenuationCache;
	private Hashtable splashAmplitudeCache;
	private Hashtable energyLossCache;
	private Hashtable amplitudeCache;
	private Hashtable reflectionCache;

	public long lowerTime;
	public long upperTime;
	public String pathID;


	public ReflectoEventStatistics(ReflectoEventContainer []statData,
																 ReflectoEventContainer reference,
																 long lowerTime, long upperTime,
																 String pathID)
	{
		this.lowerTime = lowerTime;
		this.upperTime = upperTime;
		this.pathID = pathID;
		setStatData(statData);
		setReference(reference);
		setRefArray(reference);

//    setMeanReflectogramm();
	}

	public ReflectoEventStatistics(ReflectoEventContainer []statData,
															 ReflectoEventContainer reference,
															 long lowerTime, long upperTime, String pathID,
															 int waveLength)
	{
		this(statData, reference, lowerTime, upperTime, pathID);
		this.waveLength = waveLength;
	}

	private void setMeanReflectogramm()
	{
		meanReflectogramm = new double[referenceArray.length];
		this.nReflectogrammsForAveraging = 0;
		for(int i=0; i<statData.length; i++)
		{
			if(statData[i].bs != null)
			{
				double []tmp = statData[i].bs.getTraceData();
				for(int j=0; j<meanReflectogramm.length && j<tmp.length; j++)
				{
					meanReflectogramm[j] += tmp[j];
				}
				nReflectogrammsForAveraging++;
			}
		}

		if(nReflectogrammsForAveraging>0)
		{
			for(int j=0; j<meanReflectogramm.length; j++)
			{
				meanReflectogramm[j]/=nReflectogrammsForAveraging;
			}
		}
	}

	public void setRefArray(ReflectoEventContainer reference)
	{
		BellcoreStructure bs = reference.bs;
		if(bs != null)
		{
			this.delta_x = bs.getDeltaX();
			referenceArray = bs.getTraceData();
		}
		else
		{
			delta_x = 1.;
			referenceArray = new double[reference.re[reference.re.length-1].end+1];
			for(int i=0; i<reference.re.length; i++)
			{
				for(int j=reference.re[i].begin; j<=reference.re[i].end; j++)
				{
					referenceArray[j] = reference.re[i].refAmpl(j)[0];
				}
			}
		}
	}

	public void setStatData(ReflectoEventContainer []statData)
	{
		if(statData == null) return;
		this.statData = statData;

		this.values = new double[statData.length]; //inicializing of the helping array;

		this.attenuationCache = new Hashtable();
		this.splashAmplitudeCache = new Hashtable();
		this.energyLossCache = new Hashtable();
		this.amplitudeCache = new Hashtable();
		this.reflectionCache = new Hashtable();
	}


	public ReflectoEventContainer []getStatData()
	{
		return this.statData;
	}


	public void setReference(ReflectoEventContainer reference)
	{
		if(reference == null) return;
		this.reference = reference;

		if(reference.sre!= null && reference.sre[0].getType() == ReflectogramEvent.CONNECTOR) //connector;
		{
			maxRefValue = reference.sre[0].aLet + reference.sre[0].A1;
		}
		else if(reference.re != null && reference.re[0].getType() == ReflectogramEvent.CONNECTOR)
		{
			maxRefValue = reference.re[0].aLet_connector + reference.re[0].a1_connector;
		}
	}


	public ReflectoEventContainer getReference()
	{
		return this.reference;
	}

//-----------------------------------------------------------------------------------------------
// Getting of the information about amplitude of the initial event point (defined for any event);
//-----------------------------------------------------------------------------------------------
	public void getAmplitudeInformation(int nEvent)
	{
		if(reference.re == null || nEvent<0 || nEvent>reference.re.length-1)
			return;

		if(amplitudeCache.get(String.valueOf(nEvent)+tdd)!= null)
		{
			setDataFromCache(amplitudeCache, nEvent);
			return;
		}

		ArrayList td = new ArrayList();
		int counter = 0;
		int n;

		int coord = getEventCoord(reference.re[nEvent]);

		int type = ReflectogramEvent.LINEAR;
		String dim= "";

		if(reference.re[nEvent].getType() == ReflectogramEvent.CONNECTOR) //Connector
		{
			dim = "connector_db";
			type = ReflectogramEvent.CONNECTOR;
		}
		else if(reference.re[nEvent].getType() == ReflectogramEvent.WELD) //Weld
		{
			dim = "weld_db";
			type = ReflectogramEvent.WELD;
		}
		else //Linear
		{
			dim = "linear_db";
			type = ReflectogramEvent.LINEAR;
		}

		double ampl;
		for(int i=0; i<statData.length; i++)
		{
			n = this.getEventNumber(statData[i].sre, coord);

			if(n>0 && statData[i].sre[n].getType() == type) // if not dead zone;
			{
				if(statData[i].sre[0].getType() == ReflectogramEvent.CONNECTOR)
				{
					ampl = statData[i].sre[0].aLet + statData[i].sre[0].A1;
				}
				else
				{
					ampl = maxRefValue;
				}

				values[counter] = ampl - statData[i].sre[n].A1;
				if(type == ReflectogramEvent.WELD) //Weld;
				{
					values[counter] = values[counter]+statData[i].sre[n].attenuation/2.;
				}
				td.add(new TimeDependenceData(statData[i].date, values[counter]));
				counter++;
			}
			else if(n==0 && statData[i].sre[0].getType() == type && type == ReflectogramEvent.CONNECTOR) // case of dead zone;
			{
				if(statData[i].sre[0].getType() == ReflectogramEvent.CONNECTOR)
				{
					ampl = statData[i].sre[0].aLet + statData[i].sre[0].A1;
				}
				else
				{
					ampl = maxRefValue;
				}

				double tmp = (statData[i].sre[0].end - statData[i].sre[0].begin);
				if(statData[i].sre.length>1 && statData[i].sre[1].getType() == ReflectogramEvent.LINEAR) //linear
					tmp = statData[i].sre[1].A1 - tmp*statData[i].sre[1].attenuation;
				else
					tmp = 0;
				values[counter] = ampl - tmp;
				td.add(new TimeDependenceData(statData[i].date, values[counter]));
				counter++;
			}
		}

		setData(counter, amplitudeCache, td, nEvent, dim);
	}


//----------------------------------------------------------------
// Getting information about attenuation (for linear events only);
//----------------------------------------------------------------
	public void getAttenuationInformation(int nEvent)
	{
		if(reference.re == null || nEvent<0 || nEvent>reference.re.length-1)
		{
			return;
		}
		if(attenuationCache.get(String.valueOf(nEvent)+tdd)!= null)
		{
			setDataFromCache(attenuationCache, nEvent);
			return;
		}

		ArrayList td = new ArrayList();
		int counter = 0;
		int n;

		int coord = getEventCoord(reference.re[nEvent]);

		int type = ReflectogramEvent.LINEAR;
		double C;
		String dim = "";

		if(reference.re[nEvent].getType() == ReflectogramEvent.LINEAR) //linear
		{
			dim = "linear_db/km";
			C = -1000./this.delta_x;
			type = ReflectogramEvent.LINEAR;
		}
		else
		{
			return;
		}

		for(int i=0; i<statData.length; i++)
		{
			n = this.getEventNumber(statData[i].sre, coord);
			if(n>=0 && statData[i].sre[n].getType() == type)
			{
				values[counter] = statData[i].sre[n].attenuation*C;
				td.add(new TimeDependenceData(statData[i].date, values[counter]));
				counter++;
			}
		}

		setData(counter, attenuationCache, td, nEvent, dim);

	}


//-------------------------------------------------------------------------------------------------
// Getting information about energy loss at the event (defined for any event);
//-------------------------------------------------------------------------------------------------
	public void getEnergyLossInformation(int nEvent)
	{
		if(reference.re == null || nEvent<0 || nEvent>reference.re.length-1)
		{
			return;
		}

		if(energyLossCache.get(String.valueOf(nEvent)+tdd)!= null)
		{
			setDataFromCache(energyLossCache, nEvent);
			return;
		}

		ArrayList td = new ArrayList();
		int counter = 0;
		int n;

		int coord = getEventCoord(reference.re[nEvent]);

		int type = ReflectogramEvent.LINEAR;
		String dim = "";

		int coeff = 1;
		if(reference.re[nEvent].getType() == ReflectogramEvent.CONNECTOR) //Connector
		{
			dim = "connector_db";
			type = ReflectogramEvent.CONNECTOR;
		}
		else if(reference.re[nEvent].getType() == ReflectogramEvent.WELD) //Weld
		{
			dim = "weld_db";
			type = ReflectogramEvent.WELD;
			coeff = -1;
		}
		else //Linear
		{
			dim = "linear_db";
			type = ReflectogramEvent.LINEAR;
			coeff = -1;
		}

		int C = 1;
		for(int i=0; i<statData.length; i++)
		{
			n = this.getEventNumber(statData[i].sre, coord);
			if(n>0 && statData[i].sre[n].getType() == type)
			{
				if(type == ReflectogramEvent.LINEAR)
				{
					C = statData[i].sre[n].end - statData[i].sre[n].begin;
				}
				else
				{
					C = 1;
				}
				values[counter] = statData[i].sre[n].attenuation*C*coeff;
				td.add(new TimeDependenceData(statData[i].date, values[counter]));
				counter++;
			}
			else if(n==0 && statData[i].sre[0].getType() == type && type == ReflectogramEvent.CONNECTOR) // case of dead zone;
			{
				double tmp = (statData[i].sre[0].end - statData[i].sre[0].begin);
				if(statData[i].sre.length>1 && statData[i].sre[1].getType() == ReflectogramEvent.LINEAR) //linear
					tmp =  - tmp*statData[i].sre[1].attenuation;
				else
					tmp = 0;
				values[counter] = tmp;
				td.add(new TimeDependenceData(statData[i].date, values[counter]));
				counter++;
			}

		}

		setData(counter, energyLossCache, td, nEvent, dim);

	}

//---------------------------------------------------------------------------------------------------------
// Getting of the information about reflectance (defined for reflecting events only, except for dead zone);
//---------------------------------------------------------------------------------------------------------
	public void getReflectanceInformation(int nEvent)
	{
		if(reference.re == null || nEvent<=0 || nEvent>reference.re.length-1)
		{
			return;
		}

		if(reflectionCache.get(String.valueOf(nEvent)+tdd)!= null)
		{
			setDataFromCache(reflectionCache, nEvent);
			return;
		}

		ArrayList td = new ArrayList();
		int counter = 0;
		int n;

		int coord = getEventCoord(reference.re[nEvent]);

		String dim = "";

		if(reference.re[nEvent].getType() == ReflectogramEvent.CONNECTOR && nEvent>0) //Connector
		{
			dim = "connector_db";
			for(int i=0; i<statData.length; i++)
			{
				n = this.getEventNumber(statData[i].sre, coord);
				if(n>0 && statData[i].sre[n].getType() == ReflectogramEvent.CONNECTOR)
				{
					values[counter] = getR(statData[i].sre[n].aLet);
					td.add(new TimeDependenceData(statData[i].date, values[counter]));
					counter++;
				}
			}
		}
		else
		{
			return;
		}

		setData(counter, reflectionCache, td, nEvent, dim);

	}

//------------------------------------------------------------------------------------------------
// Getting information about the amplitude of the splash (defined for the reflecting events only);
//------------------------------------------------------------------------------------------------
	public void getSplashAmplitudeInformation(int nEvent)
	{
		if(reference.re == null || nEvent<0 || nEvent>reference.re.length-1)
		{
			return;
		}

		if(splashAmplitudeCache.get(String.valueOf(nEvent)+tdd)!= null)
		{
			setDataFromCache(splashAmplitudeCache, nEvent);
			return;
		}

		ArrayList td = new ArrayList();
		int counter = 0;
		int n;

		int coord = getEventCoord(reference.re[nEvent]);

		String dim = "";

		if(reference.re[nEvent].getType() == ReflectogramEvent.CONNECTOR) //Connector
		{
			dim = "connector_db";
			for(int i=0; i<statData.length; i++)
			{
				n = this.getEventNumber(statData[i].sre, coord);
				if(n>0 && statData[i].sre[n].getType() == ReflectogramEvent.CONNECTOR)
				{
					values[counter] = statData[i].sre[n].aLet;
					td.add(new TimeDependenceData(statData[i].date, values[counter]));
					counter++;
				}
				else if(n == 0 && statData[i].sre[n].getType() == ReflectogramEvent.CONNECTOR)/**//**//**//**/
				{
					values[counter] = statData[i].sre[n].aLet +
														statData[i].sre[n].attenuation;
					td.add(new TimeDependenceData(statData[i].date, values[counter]));
					counter++;
				}
			}
		}
		else
		{
			return;
		}

		setData(counter, splashAmplitudeCache, td, nEvent, dim);
	}



	private int getEventNumber(ShortReflectogramEvent[]sre, int coord)
	{
		for(int i=0; i<sre.length; i++)
		{
			if(sre[i].begin<= coord && sre[i].end>=coord)
			{
				return i;
			}
		}
		return -1;
	}

	private void putHistoIntoPool(Histogramm histo)
	{
		Pool.put("myHisto", "Histogramm", histo);
	}

	private void putTimeDependenceDataIntoPool(TimeDependenceData []tdd)
	{
		Pool.put("timeDependentDataId", "timeDependentDataId", tdd);
	}

	private void putLinearCoeffsIntoPool(LinearCoeffs linearCoeffs)
	{
		Pool.put("linearCoeffs", "MyLinearCoeffs", linearCoeffs);
	}

	private void putDimensionIntoPool(String dimension)
	{
		Pool.put("dimension", "dimension", dimension);
	}

	private void setDataFromCache(Hashtable h, int n)
	{
		String s = String.valueOf(n);

		putDimensionIntoPool((String)h.get(s+dimensionName));
		putHistoIntoPool((Histogramm)h.get(s+histo));
		putLinearCoeffsIntoPool((LinearCoeffs)h.get(s+linFit));
		putTimeDependenceDataIntoPool((TimeDependenceData [])h.get(s+tdd));
	}

	private TimeDependenceData[] resortData(TimeDependenceData []data)
	{
		long minTime;
		int minTimeIndex;
		int i;
		int j;

		TimeDependenceData []newData =
				new TimeDependenceData[data.length];

		for(i=0; i<data.length; i++)
		{
			minTime = Long.MAX_VALUE;
			minTimeIndex = 0;
			for(j=0; j<newData.length; j++)
			{
				if(minTime > data[j].date)
				{
					minTimeIndex = j;
					minTime = data[j].date;
				}
			}
			newData[i] = new TimeDependenceData(data[minTimeIndex].date,
																				 data[minTimeIndex].value);
			data[minTimeIndex].date = Long.MAX_VALUE;
			data[minTimeIndex].value = 0.;
		}
		return newData;
	}

	private double getR(double aLet)
	{
		double sigma;
		if(waveLength == waveLength1310)
		{
			sigma = 49.;
		}
		else if(waveLength == waveLength1625)
		{
			sigma = 52.8;
		}
		else if(waveLength == waveLength1550)
		{
			sigma = 52.;
		}
		else
		{
			sigma = 51.5;
		}

		double ret = -sigma + 10.*Math.log(Math.pow(10., aLet/5.)-1.)/Math.log(10.);

		return ret;
	}


	private void setData(int valueLength, Hashtable cache,
											 ArrayList timeDependence,
											 int nEvent,
											 String dimension)
	{
		double min = values[0];
		double max = values[0];

		for(int i=0; i<valueLength; i++)
		{
			if(min>values[i]) min = values[i];
			if(max<values[i]) max = values[i];
		}
		if(max-min<0.001) max = min+0.001;

		double delta = max-min;
		max = max+delta*.3;
		min = min-delta*.3;


		Histogramm histo = new Histogramm(min, max, nBins);
		histo.init(values, 0, valueLength-1);

		TimeDependenceData []tdd = (TimeDependenceData [])
															 timeDependence.toArray(new TimeDependenceData [timeDependence.size()]);
		tdd = this.resortData(tdd);

		LinearCoeffs lc = Fitting.performLinearFitting(tdd);

		putTimeDependenceDataIntoPool(tdd);
		putHistoIntoPool(histo);
		putLinearCoeffsIntoPool(lc);
		putDimensionIntoPool(dimension);

		String s = String.valueOf(nEvent);

		cache.put(s+this.dimensionName, dimension);
		cache.put(s+this.histo, histo);
		cache.put(s+this.tdd, tdd);
		cache.put(s+this.linFit, lc);
	}

	public String getPathID()
	{
		if(pathID != null)
			return pathID;
		return "Unknown";
	}


	public long getUpperTime()
	{
		return upperTime;
	}


	public long getLowerTime()
	{
		return lowerTime;
	}


	private int getEventCoord(ReflectogramEvent re)
	{
		int coord;
		if(re.getType() == ReflectogramEvent.CONNECTOR)
		{
			coord = (int)(re.center_connector);
		}
		else if(re.getType() == ReflectogramEvent.WELD)
		{
			coord = (int)(re.center_weld);
		}
		else
		{
			coord = (re.begin+
							 re.end)/2;
		}
		return coord;
	}

	public double []getMeanReflectogramm(boolean reCount)
	{
		if(reCount)
		{
			setMeanReflectogramm();
		}

		String message = "Усредненная рефлектограмма была получена\n";
		message += "на выборке из "+String.valueOf(nReflectogrammsForAveraging)+" рефлектограмм.\n";

		message += "Статистическая ошибка усреднения составляет " +
							 String.valueOf((int)(Math.sqrt(nReflectogrammsForAveraging)*100)) +
							 "%.";
		JOptionPane.showMessageDialog(Environment.getActiveWindow(), message, "", JOptionPane.OK_OPTION);

		return meanReflectogramm;
	}
}
