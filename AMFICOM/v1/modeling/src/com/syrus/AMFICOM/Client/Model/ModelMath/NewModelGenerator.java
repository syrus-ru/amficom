package com.syrus.AMFICOM.Client.Model.ModelMath;

import java.util.ArrayList;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;

/////////////////////////////////////////////////////////////////
//         All space dimensions are measured in METERS         //
//        Size of the connector = eventSize*2!!!               //
//        Size of the weld  =  eventSize !!!                   //
/////////////////////////////////////////////////////////////////

public class NewModelGenerator
{
	private static final double dDynamicDiapazon = 5.;
	// Variables to built connectors (actually, connectors' form factors.)
	public static  double s1 = .1;
	public static  double s2 = .7;
	public static  double sFit = 30;

	// Variables to add noise after weld or connector
	//private double addNoiseAfterWeld = 1.1;
	//private double addNoiseAfterConnector = 1.2;

	ModelingEvent []rmip;
	EventParams2 [] eventParams;
	//ReflectogramEvent[] eventParams;
	int eventSize;
	double delta_x;
	double dinamicDiapazon;
	double pulsWidth;
	double maxLength;
	double addNoise;
	double formFactor;
	double[] modelArray;

	public NewModelGenerator(ModelingEvent[] rm_ip,
												double delta_x,
												double dinamicDiapazon,
												double pulsWidth,
												double maxLength,
												double addNoise,
												double formFactor)
	{
		this.rmip = rm_ip;
		this.dinamicDiapazon = dinamicDiapazon;
		this.delta_x = delta_x;
		this.pulsWidth = pulsWidth;
		this.maxLength = maxLength;
		this.addNoise = Math.abs(addNoise);
		this.formFactor = formFactor;
		performModeling();
	}

	void performModeling()
	{
		correctRMIP();
		setEventSize();

		buildEventParams();
		correctDeadZoneAndEndingSplash();

		correctCoords();
		cleanEmptyEvents();
		siewEvents();

		setRefArray();

		improveLastSplash();
		addNoiseToArray___();
	}

	private void improveLastSplash()
	{
		int coord = (int)(eventParams[eventParams.length-1].center_connector +
											eventParams[eventParams.length-1].width_connector/2);

		double a1 = eventParams[eventParams.length-1].a1_connector +
								eventParams[eventParams.length-1].aLet_connector;
		double a2 = 0.;

		for(int i=0; i<eventSize && i+coord<modelArray.length; i++)
		{
			modelArray[i+coord] = -(i-eventSize/2.)*(i-eventSize/2.)*(i-eventSize/2.)*
														 a1/2./(eventSize*eventSize*eventSize/8.) + a1/2.;
		}
	}


	boolean isConnectorPeak(ReflectogramEvent []re, int x)
	{
		for(int i=0; i<re.length; i++)
		{
			if(re[i].getType() == ReflectogramEvent.CONNECTOR && Math.abs(re[i].center_connector-x)<re[i].width_connector/2 && x<re[i].end)
				return true;
		}
		return false;
	}

	void addNoiseToArray___()
	{
		int nEvents = 0;
		for(int i=0; i<eventParams.length; i++)
		{
			if(!eventParams[i].emptyEvent)
				nEvents++;
		}

		// Noise = A*(exp(s*x)-1); // s = 1/2000;
//		double maximalNoise = 125./pulsWidth;
		double maximalNoise = 50./pulsWidth;
		double arg = 0.;
		for(int i=0; i<modelArray.length; i++)
		{
			arg = i*delta_x/20000.;
			arg = maximalNoise*(Math.exp(arg)-1.);
			if(i<eventParams[eventParams.length-1].end)
			{
				if(!isConnectorPeak(eventParams, i))
				{
					if(rnd(0., 1.)>0.1)
					{
						arg *=1.;
					}
					else if(rnd(0., 1.)>0.6)
					{
						arg *=0.6;
					}
					else
					{
						arg *=0.3;
					}
					modelArray[i] += rnd(-arg, 0.05*arg)*
													 (1.+0.02*Math.sin(i*3.14/eventSize) +
													 0.03*Math.cos(i*3.14/(eventSize*1.1)));
					modelArray[i] += rnd(addNoise);
				}
			}

			if((modelArray[i]-dDynamicDiapazon)/dinamicDiapazon<0.2)
			{
				if(rnd(0., 1.)>0.1)
					modelArray[i] = 0.;
				else
					modelArray[i] -=rnd(0, dDynamicDiapazon*0.8);
			}
			else if((modelArray[i]-dDynamicDiapazon)/dinamicDiapazon<0.1)
			{
				if(rnd(0., 1.)>0.6)
					modelArray[i] = 0.;
				else
					modelArray[i] -=rnd(0, dDynamicDiapazon*0.9);
			}
			else if((modelArray[i]-dDynamicDiapazon)/dinamicDiapazon<0.05)
			{
				if(rnd(0., 1.)>0.3)
					modelArray[i] = 0.;
				else
					modelArray[i] -=rnd(0, dDynamicDiapazon);
			}
		}
		for(int i=0; i<modelArray.length; i++)
		{
			if(modelArray[i]<0.)
				modelArray[i] = 0.;
		}

		for(int i=eventParams[eventParams.length-1].end; i<modelArray.length; i++)
		{
			if(Math.random()<0.3)
			{
				modelArray[i] = Math.random()*dDynamicDiapazon;
			}
			else
			{
				modelArray[i] = 0.;
			}
		}
	}

	void siewEvents()
	{
		double Ampl;
		double ownAmpl;

		eventParams[0].a1_connector += dinamicDiapazon+dDynamicDiapazon;
		eventParams[0].a2_connector += dinamicDiapazon+dDynamicDiapazon;

		for(int i=1; i<eventParams.length; i++)
		{
			Ampl = eventParams[i-1].refAmpl(eventParams[i-1].end)[0];
			ownAmpl = eventParams[i].refAmpl(eventParams[i].begin)[0];

			eventParams[i].a_linear += (Ampl - ownAmpl);
			eventParams[i].a1_connector += (Ampl - ownAmpl);
			eventParams[i].a2_connector += (Ampl - ownAmpl);
			eventParams[i].a_weld += (Ampl - ownAmpl);
		}

		eventParams[eventParams.length-1].a2_connector = 0.;
	}

	void correctRMIP()
	{
		ModelingEvent dz;
		ModelingEvent end;
		if(rmip[0].type != ModelingEvent.CONNECTOR)
		{
			dz = new ModelingEvent();
			dz.setConnector(-40, .1);
			ModelingEvent []newRmip = new ModelingEvent[rmip.length+1];
			newRmip[0] = dz;
			for(int i=0; i<rmip.length; i++)
			{
				newRmip[i+1] = rmip[i];
			}
			this.rmip = newRmip;
		}
		if(rmip[rmip.length-1].type != ModelingEvent.CONNECTOR)
		{
			end = new ModelingEvent();
			end.setConnector(-40, .1);
			ModelingEvent []newRmip = new ModelingEvent[rmip.length+1];
			for(int i=0; i<rmip.length; i++)
			{
				newRmip[i] = rmip[i];
			}
			newRmip[newRmip.length-1] = end;
			this.rmip = newRmip;
		}
	}

	private void correctDeadZoneAndEndingSplash()
	{
		ReflectogramEvent ep = eventParams[0];
		ep.begin = 0;
		ep.width_connector = eventSize*1.2+0.1;
		ep.center_connector = eventSize*1.2/2.;
		ep.aLet_connector /= 2;

		ep = eventParams[eventParams.length-1];
		ep.sigma2_connector = 0.001;
		ep.sigmaFit_connector = 0.001;
	}

	void setRefArray()
	{
		modelArray = new double[(int)(maxLength/delta_x)];
		double noise_ = 0.;

		for(int i=0; i<eventParams.length; i++)
		{
			if(eventParams[i].getType() == ReflectogramEvent.CONNECTOR && i!=0)
			{
				noise_ += Math.abs(eventParams[i].a1_connector-eventParams[i].a2_connector)/50.;
			}
//      else if(eventParams[i].weldEvent ==1)
//      {
//        noise_ += Math.abs(eventParams[i].boost_weld)/10.;
//      }

			for(int j=eventParams[i].begin; j<=eventParams[i].end && j<modelArray.length; j++)
			{
				if(!isConnectorPeak(eventParams, j))
				{
					modelArray[j] = eventParams[i].refAmpl(j)[0]+rnd(noise_);
				}
				else
				{
					modelArray[j] = eventParams[i].refAmpl(j)[0];
				}
			}
		}
	}

	public double []getModelArray()
	{
		return modelArray;
	}

	void buildEventParams()
	{
		eventParams = new EventParams2[rmip.length];
		setEventCoords(); // Setting of the events coords;

		double meanAtt = getMeanAttenuation();
		for(int i=0; i<rmip.length; i++)
		{
			if(rmip[i].type == ModelingEvent.CONNECTOR)
			{
				eventParams[i].a1_connector = 0.;
				eventParams[i].a2_connector = -rmip[i].loss;
				eventParams[i].aLet_connector= rmip[i].splash;
				eventParams[i].sigma1_connector = s1;
				eventParams[i].sigma2_connector = s2;
				eventParams[i].sigmaFit_connector = sFit;
				eventParams[i].k_connector = formFactor;
				eventParams[i].center_connector = eventParams[i].begin+(double)eventSize/2.+0.1;
				eventParams[i].width_connector = eventSize;
				eventParams[i].K_connector = -meanAtt/1000.*delta_x;
				eventParams[i].setType(ReflectogramEvent.CONNECTOR);
			}
			else if(rmip[i].type == ModelingEvent.SPLICE)
			{
				eventParams[i].a_weld = 0.;
				eventParams[i].b_weld = -meanAtt/1000.*delta_x;
				eventParams[i].width_weld = eventSize;
				eventParams[i].center_weld = eventParams[i].begin + (double)eventSize/2.;
				eventParams[i].boost_weld = -rmip[i].loss;
				eventParams[i].setType(ReflectogramEvent.WELD);
			}
			else
			{
				eventParams[i].a_linear = 0.;
				eventParams[i].b_linear = -rmip[i].attenuation*delta_x/1000.;
				eventParams[i].setType(ReflectogramEvent.LINEAR);
			}
		}

	}

	void correctCoords()
	{
		for(int i=0; i<eventParams.length; i++)
		{
			if(eventParams[i].getType() == ReflectogramEvent.CONNECTOR)
			{
				for(int j=i+1; j<eventParams.length; j++)
				{
					if(eventParams[i].end>eventParams[j].begin)
					{
						eventParams[j].begin =
						eventParams[j].begin =
						eventParams[j].begin = eventParams[i].end;
						if(eventParams[j].getType() == ReflectogramEvent.CONNECTOR)
						{
							eventParams[j].emptyEvent = true;
						}
						eventParams[i].sigma2_connector*=1.2;
						eventParams[i].sigmaFit_connector*=1.2;
					}
					else
					{
						break;
					}
				}
			}
		}

		for(int i=1; i<eventParams.length; i++)
		{
			if(eventParams[i].getType() == ReflectogramEvent.WELD)
			{
				for(int j=i+1; j<eventParams.length; j++)
				{
					if(eventParams[i].end>eventParams[j].begin)
					{
						if(eventParams[j].getType() == ReflectogramEvent.CONNECTOR && !eventParams[j].emptyEvent)
						{
							eventParams[i].end =
							eventParams[i].end =
							eventParams[i].end = eventParams[j].begin;
							break;
						}
						else
						{
							eventParams[j].begin =
							eventParams[j].begin =
							eventParams[j].begin = eventParams[i].end;
						}
					}
				}
			}
		}

		for(int i=0; i<eventParams.length; i++)
		{
			if(eventParams[i].getType() == ReflectogramEvent.LINEAR)
			{
				for(int j=i+1; j<eventParams.length; j++)
				{
					if(eventParams[i].end>eventParams[j].begin)
					{
						eventParams[i].end =
						eventParams[i].end =
						eventParams[i].end = eventParams[j].begin;
					}
				}
			}
		}

		ArrayList v = new ArrayList();
		for(int i=0; i<eventParams.length; i++)
		{
			if(eventParams[i].begin>eventParams[i].end ||
				 eventParams[i].begin>eventParams[i].end ||
				 eventParams[i].begin>eventParams[i].end)
			{
			//	v.add(eventParams[i]);
				// do nothing;
			}
			else
			{
				v.add(eventParams[i]);
			}
		}

		//Correcting of the ending splash;
		EventParams2 epp = (EventParams2)v.get(v.size()-1);
		if(epp.begin>epp.end-eventSize*2)
		{
			epp.end =
					epp.end =
					epp.end = epp.begin+eventSize*2;
			epp.center_connector = epp.begin+(double)eventSize/2.+0.1;
			epp.emptyEvent = false;
		}

		eventParams = (EventParams2 [])v.toArray(new EventParams2[v.size()]);
	}

	void setEventCoords()
	{
		int coord = 0;
		for(int i=0; i<rmip.length; i++)
		{
			if(eventParams[i] == null)
				eventParams[i] = new EventParams2();
			if(rmip[i].type == ModelingEvent.CONNECTOR)
			{
				eventParams[i].begin =
				eventParams[i].begin =
				eventParams[i].begin = coord;

				eventParams[i].end =
				eventParams[i].end =
				eventParams[i].end = coord + eventSize*2;
			}
			else if(rmip[i].type == ModelingEvent.SPLICE)
			{
				eventParams[i].begin =
				eventParams[i].begin =
				eventParams[i].begin = coord;

				eventParams[i].end =
				eventParams[i].end =
				eventParams[i].end = coord + eventSize;
			}
			else
			{
				eventParams[i].begin =
				eventParams[i].begin =
				eventParams[i].begin = coord;

				coord = coord + (int)(rmip[i].length/delta_x + 0.5);

				eventParams[i].end =
				eventParams[i].end =
				eventParams[i].end = coord;
			}

		}
	}

//--------------------------------------------------------------------------
	private void setEventSize()
	{
		this.eventSize = (int)((this.pulsWidth/5.)/delta_x);
//    this.eventSize = (int)(this.pulsWidth/6.67);

		if(this.eventSize<3)
			this.eventSize = 3;


		s1 = .5/eventSize;
		s2 = .5/eventSize;
		sFit = 10./eventSize;
	}


	void cleanEmptyEvents()
	{
		double meanAtt = getMeanAttenuation();

		for(int i=1; i<eventParams.length; i++)
		{
			if(eventParams[i].emptyEvent)
			{
				eventParams[i].setType(ReflectogramEvent.LINEAR);

				eventParams[i].a_linear = 0.;
				eventParams[i].b_linear = -meanAtt*delta_x/1000.;;
			}
		}
	}

	double getMeanAttenuation()
	{
			double meanAtt=0.;
			double length = 0.;
			for(int i=0; i<rmip.length; i++)
			{
				if(rmip[i].type == ModelingEvent.LINEAR)
				{
				meanAtt += rmip[i].attenuation*rmip[i].length;
				length += rmip[i].length;
			}
			}

			if(length>0.000001)
			{
				meanAtt /= length;
			}

			return meanAtt;
	}


	private double rnd(double arg)
	{
		double ret = Math.random()-0.5;

		ret = ret*2.*arg;

		return ret;
	}

	private double rnd(double from, double to)
	{
		double ret = Math.random();

		ret = ret*(to-from) +from;

		return ret;
	}

}

class EventParams2 extends ReflectogramEvent
{
	public boolean emptyEvent = false;
	double K_connector;

	public EventParams2()
	{
		super();
	}

	public double connectorF(int x)
	{
		double ret = 0.;
		double arg;
		double arg1;
		double arg2;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;
		double tmp;

		if(arg<-width_connector/2.)
		{
			ret = a1_connector;
		}
		else
			if(arg>=-width_connector/2. && arg<=width_connector/2.)
			{
				ret = aLet_connector*(1.-Math.exp(-arg1/sigma1_connector)) +
							a1_connector;
			}
			else
				if(arg>width_connector/2.)
				{
					tmp = a1_connector+aLet_connector*
								(1.-Math.exp(-width_connector/sigma1_connector));

					ret = tmp -
								(tmp-a2_connector)*(1. - expa(arg2, sigma2_connector,

								sigmaFit_connector, k_connector));
				}
				else
					ret = 0.;
				ret = ret + K_connector*arg1;
				return ret;
	}
}
