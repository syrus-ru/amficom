package com.syrus.AMFICOM.analysis;

import java.io.*;
import java.util.Properties;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;

public class AnalysisManager
{
	double[] minuitParams;
	double[] minuitInitialParams;
	private String propertiesFileName = "analysis.properties";

	private static native double[] gauss(
			double[] y,
		double center,
		double amplitude,
		double sigma);

	private static native double[] analyse(
			int waveletType,
			double []y,                //the refl. itself
		double d_x,                  //dx
		double formFactor,           // 1. форм-фактор
		double min_level,            // 2. мин уровень события
		double max_level_noise,      // 3. макс уровень шума
		double min_level_to_find_end,// 4. мин. уровень конца
		double min_weld,             // 5. минимальная сварка
		double min_connector,        // 6. минимальный коннектор
		int key,                    // 7. стратегия
		int reflectiveSize,
		int nonReflectiveSize);

	private static native double[] fit(
			double []y,                //the refl. itself
		double d_x,                  //dx
		double[] refl_events,        // 1. события, которые будут фитироваться
		int key,										 // 2. стратегия
		double meanAttenuation);		 // 3. среднее затухание

	static
	{
		try
		{
			System.loadLibrary("gauss");
			System.loadLibrary("dadara");
		}
		catch  (java.lang.UnsatisfiedLinkError ex)
		{
			ex.printStackTrace();
		}
	}

	public static double[] calcGaussian(double[] y, int max_index)
	{
		double[] gauss = new double[y.length];
		int width = 0;

		double max_value = y[max_index];

		max_value *= .2;
		for (int i = 0; i < y.length; i++)
			if (y[i] > max_value)
				width++;
		max_value /= .2;

		double[] d = AnalysisManager.gauss(y, max_index, max_value, width);
		double center = d[0];
		max_value = d[1];
		double sigma_squared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = max_value * Math.exp( -(i - center) * (i - center) / sigma_squared);

		return gauss;
	}

	public static double[] calcThresholdCurve(double[] y, int max_index)
	{
		double[] threshold = new double[y.length];
		double max = 0;

		for (int i = 0; i < threshold.length; i++)
		{
			threshold[i] = 0;
			for (int j = 0; j < y.length; j++)
				if (j <= max_index - i || j >= max_index + i)
					threshold[i] += y[j];

			if (max < threshold[i])
				max = threshold[i];
		}

		for (int i = 0; i < threshold.length; i++)
			threshold[i] /= max;

		return threshold;
	}

	public static ReflectogramEvent[] analyseTrace(
			double[] y,
			double delta_x,
			double[] pars,
			int reflectiveSize,
			int nonReflectiveSize)
	{
		double[] tmp = analyse(
				(int)pars[7],
				y,
				delta_x,
				pars[5],
				pars[0],
				pars[4],
				pars[3],
				pars[1],
				pars[2],
				(int)pars[6],
				reflectiveSize,
				nonReflectiveSize);

		int n_events = tmp.length / ReflectogramEvent.NUMBER_OF_PARAMETERS;
		ReflectogramEvent[] ep = new ReflectogramEvent[n_events];
		for(int i = 0; i < n_events; i++)
		{
			ep[i] = new ReflectogramEvent();
			ep[i].setParams(tmp, i * ReflectogramEvent.NUMBER_OF_PARAMETERS);
			ep[i].setDeltaX(delta_x);
		}
		return ep;
	}

	public static ReflectogramEvent[] fitTrace(double[] y, double delta_x, ReflectogramEvent[] events, int strategy, double meanAttenuation)
	{
		double[] revents = new double[events.length * ReflectogramEvent.NUMBER_OF_PARAMETERS];
		for (int i = 0; i < events.length; i++)
			events[i].toDoubleArray(revents, ReflectogramEvent.NUMBER_OF_PARAMETERS * i);

		double[] tmp = fit(y, delta_x, revents, strategy, meanAttenuation);

		int n_events = tmp.length / ReflectogramEvent.NUMBER_OF_PARAMETERS;
		ReflectogramEvent[] ep = new ReflectogramEvent[n_events];
		for(int i = 0; i < n_events; i++)
		{
			ep[i] = new ReflectogramEvent();
			ep[i].setParams(tmp, i * ReflectogramEvent.NUMBER_OF_PARAMETERS);
			ep[i].setDeltaX(delta_x);
		}
		return ep;
	}

	static double[] defaultMinuitParams =
	{
		0.04, //минимальный уровень события
		0.06, //минимальный уровень сварки
		0.2, //минимальный уровень коннектора
		3, //минимальный уровень конца
		.1, //максимальный уровень шума
		0.3, //скорость спадания (форм-фактор) коннектора (0..1)
		4, //стратегия (int [0..3])
		0, //номер вейвлета [0..8] (предпочтителен 0)
	};

	public AnalysisManager()
	{
		Pool.put("analysisparameters", "minuitdefaults", defaultMinuitParams);

		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(propertiesFileName));
			String temp = properties.getProperty("parameters");
			minuitParams = decompose (temp, defaultMinuitParams);
		}
		catch (IOException ex)
		{
			minuitParams = new double [defaultMinuitParams.length];
			for (int i = 0; i < defaultMinuitParams.length; i++)
				minuitParams[i] = defaultMinuitParams[i];
		}
		finally
		{
			Pool.put("analysisparameters", "minuitanalysis", minuitParams);
		}

		minuitInitialParams = new double [minuitParams.length];
		for (int i = 0; i < minuitParams.length; i++)
			minuitInitialParams[i] = minuitParams[i];
		Pool.put("analysisparameters", "minuitinitials", minuitInitialParams);
	}

	public void saveIni()
	{
		minuitParams = (double[])Pool.get("analysisparameters", "minuitanalysis");
		String type = (String)Pool.get("analysisparameters", "analysisdefaulttype");
		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(propertiesFileName));
			properties.setProperty("parameters", compose(minuitParams));
			properties.store(new FileOutputStream(propertiesFileName), null);
		}
		catch (IOException ex)
		{
		}
	}

	private double[] decompose (String val, double[] defaults)
	{
		int i=0;
		int bind = -1;
		int ind = val.indexOf(";");
		double d[] = new double[defaults.length];

		for (int j=0; j<defaults.length; j++)
			d[j] = defaults[j];
		while ((ind != -1) && (i<defaults.length))
		{
			d[i++] = Double.parseDouble(val.substring(bind+1, ind));
			bind = ind;
			ind = val.indexOf(";", bind+1);
		}
		return d;
	}

	private String compose (double[] d)
	{
		String str = "";
		for (int i=0; i<d.length; i++)
			str = str + String.valueOf(d[i]) + ";";
		return str;
	}
}