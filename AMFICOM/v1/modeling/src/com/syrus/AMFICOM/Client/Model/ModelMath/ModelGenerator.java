package com.syrus.AMFICOM.Client.Model.ModelMath;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ATableFrame;
import com.syrus.AMFICOM.Client.Model.ModelMath.*;
import com.syrus.io.BellcoreStructure;

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

		pathelements = new DataSet(path.links);
		ObjectResourceSorter sorter = PathElement.getSorter();
		sorter.setDataSet(pathelements);
		pathelements = sorter.sort("num", ObjectResourceSorter.SORT_ASCENDING);
		Pool.put("activecontext", "activepathid", path.getId());
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
			if(pe.is_cable) // CABLE LINK
			{
				SchemeCableLink scl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				if(scl == null)
				{
					System.out.println("SchemeCableLink " + pe.link_id + " not found");
					return null;
				}
				SchemeCableThread thread = null;
				for(Enumeration en2 = scl.cable_threads.elements(); en2.hasMoreElements();)
				{
					SchemeCableThread sct = (SchemeCableThread)en2.nextElement();
					if(sct.getId().equals(pe.thread_id))
					{
						thread = sct;
						break;
					}
				}
				if(thread == null)
				{
					System.out.println("SchemeCableThread " + pe.thread_id + " not found");
					return null;
				}
				ModelingEvent event = addCableLink(scl, thread);
				if (event == null)
				{
					System.out.println("Fail creating modeling element from cable " + scl.getName() + "(" + scl.getId() + ")");
					return null;
				}
				reflectoElements.add(event);
			}
			else // pe.isCable() == false
			{
				SchemeLink sl =	(SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
				if(sl == null)
				{
					System.out.println("SchemeLink " + pe.link_id + " not found");
					return null;
				}
				SchemePort sourcePort =	(SchemePort)Pool.get(SchemePort.typ, sl.source_port_id);
				if(sourcePort == null)
				{
					System.out.println("SchemePort (source) " + sl.source_port_id + " not found");
					return null;
				}
				ModelingEvent event = addPort(sourcePort);
				if (event == null)
				{
					System.out.println("Fail creating modeling element from port " + sourcePort.getName() + "(" + sourcePort.getId() + ")");
					return null;
				}
				reflectoElements.add(event);

				event = addLink(sl);
				if (event == null)
				{
					System.out.println("Fail creating modeling element from link " + sl.getName() + "(" + sl.getId() + ")");
					return null;
				}
				reflectoElements.add(event);

				SchemePort targetPort = (SchemePort)Pool.get(SchemePort.typ, sl.target_port_id);
				if(targetPort == null)
				{
					System.out.println("Something is wrong... - Target port is null");
					return null;
				}
				event = addPort(targetPort);
				if (event == null)
				{
					System.out.println("Fail creating modeling element from port " + targetPort.getName() + "(" + targetPort.getId() + ")");
					return null;
				}
				reflectoElements.add(event);
			}
		}
		return reflectoElements;
	}

	private ModelingEvent addCableLink(SchemeCableLink scl, SchemeCableThread thread)
	{
		Map ht = null;
		Characteristic c;
		double length;
		double attenuation;

		if (!scl.cable_link_id.equals(""))
		{
			CableLink link = (CableLink)Pool.get(CableLink.typ, scl.cable_link_id);
			if (link != null)
				ht = link.characteristics;
		}
		if (ht == null)
		{
			LinkType linkType =	(LinkType)Pool.get(LinkType.typ, thread.link_type_id);
			if(linkType == null)
			{
				System.out.println("LinkType " + thread.link_type_id + " not found");
				return null;
			}
				else
					ht = linkType.characteristics;
		}
		if(ht == null)
		{
			System.out.println("Something wrong...  - linkType.characteristics == null");
			return null;
		}

		c = (Characteristic)ht.get(attenuationString);
		try
		{
			attenuation = Double.parseDouble(c.value);
		}
		catch (Exception ex)
		{
			attenuation = defAttenuation; //defauld dE/dx value
		}
		length = scl.optical_length;
		if(length < 0)
			length = 0;

		ModelingEvent event = new ModelingEvent();
		event.setLinear(attenuation, length);
		return event;
	}

	private ModelingEvent addPort(SchemePort sp)
	{
		Map ht;
		Characteristic c;
		double reflectance;
		double attenuation;

		PortType portType = (PortType)Pool.get(PortType.typ, sp.port_type_id);
		if(portType == null)
		{
			System.out.println("PortType " + sp.port_type_id + " not found");
			return null;
		}
		if (!sp.port_id.equals(""))
		{
			Port port = (Port)Pool.get(Port.typ, sp.port_id);
			if (port != null)
				ht = port.characteristics;
			else
				ht = portType.characteristics;
		}
		else
			ht = portType.characteristics;
		ModelingEvent event = new ModelingEvent();
		if(portType.p_class.equals("optical")) // optical connector
		{
			c = (Characteristic)ht.get("Attenuation_nom");
			try
			{
				attenuation = Double.parseDouble(c.value);
			}
			catch (Exception ex)
			{
				attenuation = defConnectorLoss;
			}
			c = (Characteristic)ht.get("Reflectance_nom");
			try
			{
				reflectance = Math.abs(Double.parseDouble(c.value));
			}
			catch (Exception ex)
			{
				reflectance = defReflect;
			}
			event.setConnector(Math.abs(reflectance), attenuation);
		}
		else // termo connection
		{
			c = (Characteristic)ht.get("Attenuation");
			try
			{
				attenuation = Double.parseDouble(c.value);
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
		Map ht = null;
		Characteristic c;
		double length;
		double attenuation;

		if (!sl.link_id.equals(""))
		{
			Link link = (Link)Pool.get(Link.typ, sl.link_id);
			if (link != null)
				ht = link.characteristics;
		}
		if (ht == null)
		{
			LinkType linkType =	(LinkType)Pool.get(LinkType.typ, sl.link_type_id);
			if(linkType == null)
			{
				System.out.println("LinkType " + sl.link_type_id + " not found");
				return null;
			}
			else
				ht = linkType.characteristics;
		}
		if(ht == null)
		{
			System.out.println("Something is wrong... - characteristics of link type is null");
			return null;
		}

		c = (Characteristic)ht.get(attenuationString);
		try
		{
			attenuation = Double.parseDouble(c.value);
		}
		catch (Exception ex)
		{
			attenuation = defAttenuation;
		}
		length = sl.optical_length;
		if(length < 0)
			length = 0;

		ModelingEvent event = new ModelingEvent();
		event.setLinear(attenuation, length);
		return event;
	}
}