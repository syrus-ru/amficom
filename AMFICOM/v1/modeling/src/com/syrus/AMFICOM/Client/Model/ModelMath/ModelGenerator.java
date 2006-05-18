package com.syrus.AMFICOM.Client.Model.ModelMath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.modelling.ModelEvent;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

public class ModelGenerator {
	List<ModelEvent> reflectoElements;
	List<PathElement> pathelements;

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
		this.defConnectorLoss = defConnectorLoss;
		this.defWeldLoss = defWeldLoss;
		this.defReflect = defReflect;
		this.defAttenuation = defAttenuation;
		this.attenuationString = new StringBuffer("Attenuation_").append(waveLength).toString();

		try {
			this.pathelements = new ArrayList<PathElement>(path.getPathMembers());
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
//		ObjectResourceSorter sorter = SchemePath.getSorter();
//		sorter.setDataSet(pathelements);
//		path.links = sorter.sort("num", ObjectResourceSorter.SORT_ASCENDING);
	}

	public ReflectogramEvent[] model(
		int pulsWidth,
		double formFactor,
		double delta_x)
	{
/*
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
*/
		return null;
	}

	protected void correctModelingEvents()
	{
		ModelEvent event = this.reflectoElements.get(0);
		if(!event.isReflective())
		{
			ModelEvent dz = ModelEvent.createReflective(.1, -40);
			this.reflectoElements.add(0, dz);
		}
		event = this.reflectoElements.get(this.reflectoElements.size() - 1);
		if(!event.isReflective())
		{
			ModelEvent end = ModelEvent.createReflective(0.1, -40);
			this.reflectoElements.add(end);
		}
	}
/*
	private int getEventSize(int pulsWidth, double delta_x)
	{
		int eventSize = (int)((pulsWidth/5.)/delta_x);
		if(eventSize < 3)
			eventSize = 3;
		return eventSize;
	}*/

	double getMeanAttenuation() {
		double meanAtt = 0.;
		double length = 0.;
		for(ModelEvent ev : this.reflectoElements) {
			if(ev.isLinear()) {
				meanAtt += ev.getAttenuation() * ev.getLength();
				length += ev.getLength();
			}
		}
		if(length > 0) {
			meanAtt /= length;
		}
		return meanAtt;
	}

	public List<ModelEvent> createModelingEvents() throws ApplicationException {
		this.reflectoElements = new ArrayList<ModelEvent>();

		for(PathElement pe : this.pathelements) {
			if(pe.getKind() == IdlKind.SCHEME_CABLE_LINK) { // CABLE LINK

				SchemeCableLink scl = pe.getSchemeCableLink();
				SchemeCableThread thread = pe.getSchemeCableThread();

				ModelEvent event = addCableLink(scl, thread);
				if (event == null) {
					System.out.println("Fail creating modeling element from cable " + scl.getName() + "(" +
														 scl.getId().getIdentifierString() + ")");
					return null;
				}
				this.reflectoElements.add(event);
			}
			else if (pe.getKind() == IdlKind.SCHEME_LINK) {
				SchemeLink sl =	pe.getSchemeLink();
				SchemePort sourcePort =	sl.getSourceAbstractSchemePort();
				if(sourcePort == null) {
					return null;
				}

				ModelEvent event = addPort(sourcePort);
				if (event == null) {
					System.out.println("Fail creating modeling element from port " + sourcePort.getName() + "(" +
														 sourcePort.getId().getIdentifierString() + ")");
					return null;
				} 
				this.reflectoElements.add(event);

				event = addLink(sl);
				if (event == null) {
					System.out.println("Fail creating modeling element from link " + sl.getName() + "(" +
														 sl.getId().getIdentifierString() + ")");
					return null;
				}
				this.reflectoElements.add(event);

				SchemePort targetPort = sl.getTargetAbstractSchemePort();
				if(targetPort == null)
					return null;

				event = addPort(targetPort);
				if (event == null) {
					System.out.println("Fail creating modeling element from port " + targetPort.getName() +
														 "(" + targetPort.getId().getIdentifierString() + ")");
					return null;
				}
				this.reflectoElements.add(event);
			}
		}
		correctModelingEvents();
		return this.reflectoElements;
	}

	private ModelEvent addCableLink(SchemeCableLink scl, SchemeCableThread thread) throws ApplicationException {
		Set<Characteristic> ht = null;
		Characteristic c;
		double length;
		double attenuation;

		if (scl.getAbstractLink() != null)
			ht = scl.getAbstractLink().getCharacteristics(false);

		if (ht == null)
		{
			LinkType linkType =	thread.getLinkType();
			if(linkType == null)
				return null;
			else
				ht = linkType.getCharacteristics(false);
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
		length = scl.getOpticalLength();
		if(length < 0)
			length = 0;

		ModelEvent event = ModelEvent.createLinear(length, attenuation / 1000);
		return event;
	}

	private ModelEvent addPort(SchemePort sp) throws ApplicationException {
		Set<Characteristic> ht;
		Characteristic c;
		double reflectance;
		double attenuation;

		PortType portType = sp.getPortType();
		if (portType == null) {
			return null;
		}

		if (sp.getPort() != null)
			ht = sp.getPort().getCharacteristics(false);
		else
			ht = portType.getCharacteristics(false);

		ModelEvent event;
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
				reflectance = Double.parseDouble(c.getValue());
			}
			catch (Exception ex)
			{
				reflectance = defReflect;
			}
			event = ModelEvent.createReflective(attenuation, reflectance);
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
			event = ModelEvent.createSlice(attenuation);
		}
		return event;
	}

	private ModelEvent addLink(SchemeLink sl) throws ApplicationException {
		Set<Characteristic> ht = null;
		Characteristic c;
		double length;
		double attenuation;

		if (sl.getAbstractLink() != null)
				ht = sl.getAbstractLink().getCharacteristics(false);
		if (ht == null)
		{
			LinkType linkType =	sl.getAbstractLinkType();
			if(linkType == null) {
				return null;
			}	else {
				ht = linkType.getCharacteristics(false);
			}
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
		length = sl.getOpticalLength();
		if(length < 0)
			length = 0;

		ModelEvent event = ModelEvent.createLinear(length, attenuation / 1000);
		return event;
	}

	private Characteristic getCharacteristic(Set<Characteristic> characteristics, String codename)
	{
		for (Iterator it = characteristics.iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			if (ch.getType().getCodename().equals(codename))
				return ch;
		}
		return null;
	}
}