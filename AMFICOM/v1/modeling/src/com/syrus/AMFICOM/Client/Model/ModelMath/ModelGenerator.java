package com.syrus.AMFICOM.Client.Model.ModelMath;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

public class ModelGenerator
{
	SchemePath path;
	ArrayList reflectoElements;
	List pathelements;

	double defConnectorLoss;
	double defWeldLoss;
	double defReflect;
	double defAttenuation;
	String attenuationString;

	public ModelGenerator(SchemePath path,
		int waveLength,
		double defConnectorLoss,
		double defWeldLoss,
		double defReflect,
		double defAttenuation)
	{
		this.path = path;
		this.defConnectorLoss = defConnectorLoss;
		this.defWeldLoss = defWeldLoss;
		this.defReflect = defReflect;
		this.defAttenuation = defAttenuation;
		this.attenuationString = new StringBuffer("Attenuation_").append(waveLength).toString();

		pathelements = Arrays.asList(path.links());
//		ObjectResourceSorter sorter = SchemePath.getSorter();
//		sorter.setDataSet(pathelements);
//		path.links = sorter.sort("num", ObjectResourceSorter.SORT_ASCENDING);
		Pool.put("activecontext", "activepathid", path.id());
	}

	public ReflectogramEvent[] model(
		int pulsWidth,
		double formFactor,
		double delta_x)
	{

		reflectoElements = createModelingEvents();
		if (reflectoElements == null)
			return null;

		correctModelingEvents();
		int eventSize = getEventSize(pulsWidth, delta_x);

		double s1 = .5/eventSize;
		double s2 = .5/eventSize;
		double sFit = 10./eventSize;
		double meanAtt = getMeanAttenuation();
		int coord = 0;
		int delta = 0;

		ArrayList events = new ArrayList(reflectoElements.size());
		for (int i = 0; i < reflectoElements.size(); i++)
		{
			ModelingEvent ev = (ModelingEvent)reflectoElements.get(i);
			ReflectogramEvent re = new ReflectogramEvent();

			switch (ev.type)
			{
				case ModelingEvent.CONNECTOR:
					re.begin = coord;
					delta = eventSize*2;
					re.end = coord + delta;
					if (!events.isEmpty())
					{
						ReflectogramEvent last = (ReflectogramEvent)events.get(events.size() - 1);
						if (last.end >= re.end)
							continue;
						re.begin = last.end;
						coord = re.end;
					}

					re.a1_connector = 0.;
					re.a2_connector = -ev.loss;
					re.aLet_connector = ev.splash;
					re.sigma1_connector = s1;
					re.sigma2_connector = s2;
					re.sigmaFit_connector = sFit;
					re.k_connector = formFactor; // -meanAtt/1000.*delta_x
					re.center_connector = re.begin + (double)eventSize / 2. + 0.1;
					re.width_connector = eventSize;
					re.setType(ReflectogramEvent.CONNECTOR);
					break;
				case ModelingEvent.SPLICE:
					re.begin = coord;
					delta = eventSize;
					re.end = coord + delta;
					if (!events.isEmpty())
					{
						ReflectogramEvent last = (ReflectogramEvent)events.get(events.size() - 1);
						if (last.end >= re.end)
						 continue;
						re.begin = last.end;
						coord = re.end;
					}

					re.a_weld = 0.;
					re.b_weld = -meanAtt/1000.*delta_x;
					re.width_weld = eventSize;
					re.center_weld = re.begin + (double)eventSize/2.;
					re.boost_weld = -ev.loss;
					re.setType(ReflectogramEvent.WELD);
					break;
				case ModelingEvent.LINEAR:
					re.begin = coord;
					re.end = (int)Math.round(ev.length / delta_x) - delta;
					delta = 0;
					if (!events.isEmpty())
					{
						ReflectogramEvent last = (ReflectogramEvent)events.get(events.size() - 1);
						if (last.end >= re.end)
						 continue;
						 re.begin = last.end;
						 coord = re.end;
					}

					re.a_linear = 0.;
					re.b_linear = -ev.attenuation*delta_x/1000.;
					re.setType(ReflectogramEvent.LINEAR);
			}
			events.add(re);
		}
		return (ReflectogramEvent[])events.toArray(new ReflectogramEvent[events.size()]);
	}

	protected void correctModelingEvents()
	{
		ModelingEvent event = (ModelingEvent)reflectoElements.get(0);
		if(event.type != ModelingEvent.CONNECTOR)
		{
			ModelingEvent dz = new ModelingEvent();
			dz.setConnector(-40, .1);
			reflectoElements.add(0, dz);
		}
		event = (ModelingEvent)reflectoElements.get(reflectoElements.size() - 1);
		if(event.type != ModelingEvent.CONNECTOR)
		{
			ModelingEvent end = new ModelingEvent();
			end.setConnector(-40, .1);
			reflectoElements.add(end);
		}
	}

	private int getEventSize(int pulsWidth, double delta_x)
	{
		int eventSize = (int)((pulsWidth/5.)/delta_x);
		if(eventSize < 3)
			eventSize = 3;
		return eventSize;
	}

	double getMeanAttenuation()
	{
		double meanAtt = 0.;
		double length = 0.;
		for(Iterator it = reflectoElements.iterator(); it.hasNext();)
		{
			ModelingEvent ev = (ModelingEvent)it.next();
			if(ev.type == ModelingEvent.LINEAR)
			{
				meanAtt += ev.attenuation * ev.length;
				length += ev.length;
			}
		}
		if(length > 0)
			meanAtt /= length;

		return meanAtt;
	}

	public ArrayList createModelingEvents()
	{
		ArrayList reflectoElements = new ArrayList();

		for(Iterator it = pathelements.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if(pe.type().value() == Type._SCHEME_CABLE_LINK) // CABLE LINK
			{
				SchemeCableLink scl = (SchemeCableLink)pe.abstractSchemeElement();
				SchemeCableThread thread = null;
				for(int j = 0; j < scl.schemeCableThreads().length; j++)
				{
					SchemeCableThread sct = scl.schemeCableThreads()[j];
					if(sct.equals(pe.schemeCableThread()))
					{
						thread = sct;
						break;
					}
				}
				if(thread == null)
				{
					System.out.println("SchemeCableThread " + pe.schemeCableThread().name() + " not found");
					return null;
				}
				ModelingEvent event = addCableLink(scl, thread);
				if (event == null) {
					System.out.println("Fail creating modeling element from cable " + scl.name() + "(" +
														 scl.id().identifierString() + ")");
					return null;
				}
				reflectoElements.add(event);
			}
			else if (pe.type().value() == Type._SCHEME_LINK)
			{
				SchemeLink sl =	(SchemeLink)pe.abstractSchemeElement();
				SchemePort sourcePort =	sl.sourceSchemePort();
				if(sourcePort == null)
					return null;

				ModelingEvent event = addPort(sourcePort);
				if (event == null) {
					System.out.println("Fail creating modeling element from port " + sourcePort.name() + "(" +
														 sourcePort.id().identifierString() + ")");
					return null;
				}
				reflectoElements.add(event);

				event = addLink(sl);
				if (event == null) {
					System.out.println("Fail creating modeling element from link " + sl.name() + "(" +
														 sl.id().identifierString() + ")");
					return null;
				}
				reflectoElements.add(event);

				SchemePort targetPort = sl.targetSchemePort();
				if(targetPort == null)
					return null;

				event = addPort(targetPort);
				if (event == null) {
					System.out.println("Fail creating modeling element from port " + targetPort.name() +
														 "(" + targetPort.id().identifierString() + ")");
					return null;
				}
				reflectoElements.add(event);
			}
		}
		return reflectoElements;
	}

	private ModelingEvent addCableLink(SchemeCableLink scl, SchemeCableThread thread)
	{
		List ht = null;
		Characteristic c;
		double length;
		double attenuation;

		if (scl.link() != null)
			ht = scl.linkImpl().getCharacteristics();

		if (ht == null)
		{
			LinkType linkType =	thread.cableThreadTypeImpl().getLinkType();
			if(linkType == null)
				return null;
			else
				ht = linkType.getCharacteristics();
		}

		c = getCharacteristic(ht, attenuationString);
		try
		{
			attenuation = Double.parseDouble(c.getValue());
		}
		catch (Exception ex)
		{
			attenuation = defAttenuation; //defauld dE/dx value
		}
		length = scl.opticalLength();
		if(length < 0)
			length = 0;

		ModelingEvent event = new ModelingEvent();
		event.setLinear(attenuation, length);
		return event;
	}

	private ModelingEvent addPort(SchemePort sp)
	{
		List ht;
		Characteristic c;
		double reflectance;
		double attenuation;

		PortType portType = sp.portTypeImpl();
		if (portType == null) {
			return null;
		}

		if (sp.port() != null)
			ht = sp.portImpl().getCharacteristics();
		else
			ht = portType.getCharacteristics();

		ModelingEvent event = new ModelingEvent();
		if(portType.getSort().value() == PortTypeSort._PORTTYPESORT_OPTICAL) // optical connector
		{
			c = getCharacteristic(ht, "Attenuation_nom");
			try
			{
				attenuation = Double.parseDouble(c.getValue());
			}
			catch (Exception ex)
			{
				attenuation = defConnectorLoss;
			}
			c = getCharacteristic(ht, "Reflectance_nom");
			try
			{
				reflectance = Math.abs(Double.parseDouble(c.getValue()));
			}
			catch (Exception ex)
			{
				reflectance = defReflect;
			}
			event.setConnector(Math.abs(reflectance), attenuation);
		}
		else // termo connection
		{
			c = getCharacteristic(ht, "Attenuation");
			try
			{
				attenuation = Double.parseDouble(c.getValue());
			}
			catch (Exception ex)
			{
				attenuation = defWeldLoss;
			}
			event.setWeld(attenuation);
		}
		return event;
	}

	private ModelingEvent addLink(SchemeLink sl)
	{
		List ht = null;
		Characteristic c;
		double length;
		double attenuation;

		if (sl.link() != null)
				ht = sl.linkImpl().getCharacteristics();
		if (ht == null)
		{
			LinkType linkType =	sl.linkTypeImpl();
			if(linkType == null)
				return null;
			else
				ht = linkType.getCharacteristics();
		}

		c = getCharacteristic(ht, attenuationString);
		try
		{
			attenuation = Double.parseDouble(c.getValue());
		}
		catch (Exception ex)
		{
			attenuation = defAttenuation;
		}
		length = sl.opticalLength();
		if(length < 0)
			length = 0;

		ModelingEvent event = new ModelingEvent();
		event.setLinear(attenuation, length);
		return event;
	}

	private Characteristic getCharacteristic(List characteristics, String codename)
	{
		for (Iterator it = characteristics.iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			if (((CharacteristicType)ch.getType()).getCodename().equals(codename))
				return ch;
		}
		return null;
	}
}