package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCablePort;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDevice;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Vector;

import javax.swing.JOptionPane;

public class EmptyMapViewDataSource
		extends EmptyMapDataSource
		implements DataSourceInterface
{
	protected EmptyMapViewDataSource()
	{
	}

	public EmptyMapViewDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadSchemes(Vector ids)
	{
		LoadSchemes();
	}
	
	public void LoadSchemes()
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		scheme1();
		scheme2();
	}
	
	private void scheme1()
	{
		Scheme scheme;
		SchemeElement el1;
		SchemeElement el2;
		SchemeElement el3;
		SchemeElement el4;
		SchemeElement el5;
		SchemeLink link;
		SchemePort port11;
		SchemePort port12;
		SchemeCablePort cport11;
		SchemeCablePort cport21;
		SchemeCablePort cport22;
		SchemeCablePort cport31;
		SchemeCablePort cport32;
		SchemeCablePort cport33;
		SchemeCablePort cport41;
		SchemeCablePort cport42;
		SchemeCablePort cport51;
		SchemeCablePort cport52;
		SchemeCableLink clink1;
		SchemeCableLink clink2;
		SchemeCableLink clink3;
		SchemeCableLink clink4;
		SchemeCableLink clink5;
		SchemeDevice dev1;
		SchemeDevice dev2;
		SchemeDevice dev3;
		SchemeDevice dev4;
		SchemeDevice dev5;
		SchemePath path;
		PathElement pel1;
		PathElement pel2;
		PathElement pel3;
		PathElement pel4;
		PathElement pel5;
		PathElement pel6;
		PathElement pel7;

		port11 = new SchemePort("port11");
		port11.deviceId = "dev1";
		port11.directionType = "out";
		port11.name = "Порт раз";
		port11.linkId = "link";
		Pool.put(SchemePort.typ, port11.getId(), port11);

		port12 = new SchemePort("port12");
		port12.deviceId = "dev1";
		port12.directionType = "in";
		port12.name = "Порт два";
		port12.linkId = "link";
		Pool.put(SchemePort.typ, port12.getId(), port12);

		cport11 = new SchemeCablePort("cport11");
		cport11.cableLinkId = "clink1";
		cport11.deviceId = "dev1";
		cport11.directionType = "in";
		cport11.name = "Каборт раз";
		Pool.put(SchemeCablePort.typ, cport11.getId(), cport11);

		cport21 = new SchemeCablePort("cport21");
		cport21.cableLinkId = "clink1";
		cport21.deviceId = "dev2";
		cport21.directionType = "out";
		cport21.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport21.getId(), cport21);

		cport22 = new SchemeCablePort("cport22");
		cport22.cableLinkId = "clink2";
		cport22.deviceId = "dev2";
		cport22.directionType = "out";
		cport22.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport22.getId(), cport22);

		cport31 = new SchemeCablePort("cport31");
		cport31.cableLinkId = "clink2";
		cport31.deviceId = "dev3";
		cport31.directionType = "out";
		cport31.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport31.getId(), cport31);

		cport32 = new SchemeCablePort("cport32");
		cport32.cableLinkId = "clink3";
		cport32.deviceId = "dev2";
		cport32.directionType = "out";
		cport32.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport32.getId(), cport32);

		cport33 = new SchemeCablePort("cport33");
		cport33.cableLinkId = "clink5";
		cport33.deviceId = "dev3";
		cport33.directionType = "out";
		cport33.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport33.getId(), cport33);

		cport41 = new SchemeCablePort("cport41");
		cport41.cableLinkId = "clink3";
		cport41.deviceId = "dev4";
		cport41.directionType = "out";
		cport41.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport41.getId(), cport41);

		cport42 = new SchemeCablePort("cport42");
		cport42.cableLinkId = "clink4";
		cport42.deviceId = "dev4";
		cport42.directionType = "out";
		cport42.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport42.getId(), cport42);

		cport51 = new SchemeCablePort("cport51");
		cport51.cableLinkId = "clink4";
		cport51.deviceId = "dev5";
		cport51.directionType = "out";
		cport51.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport51.getId(), cport51);

		cport52 = new SchemeCablePort("cport52");
		cport52.cableLinkId = "clink5";
		cport52.deviceId = "dev5";
		cport52.directionType = "out";
		cport52.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport52.getId(), cport52);

		dev1 = new SchemeDevice("dev1");
		dev1.cableports.add(cport11);
		dev1.name = "Девайс1";
		dev1.ports.add(port11);
		dev1.ports.add(port12);
		Pool.put(SchemeDevice.typ, dev1.getId(), dev1);

		dev2 = new SchemeDevice("dev2");
		dev2.cableports.add(cport21);
		dev2.cableports.add(cport22);
		dev2.name = "Девайс2";
		Pool.put(SchemeDevice.typ, dev2.getId(), dev2);
		
		dev3 = new SchemeDevice("dev3");
		dev3.cableports.add(cport31);
		dev3.cableports.add(cport32);
		dev3.cableports.add(cport33);
		dev3.name = "Девайс3";
		Pool.put(SchemeDevice.typ, dev3.getId(), dev3);
		
		dev4 = new SchemeDevice("dev4");
		dev4.cableports.add(cport41);
		dev4.cableports.add(cport42);
		dev4.name = "Девайс4";
		Pool.put(SchemeDevice.typ, dev4.getId(), dev4);
		
		dev5 = new SchemeDevice("dev5");
		dev5.cableports.add(cport51);
		dev5.cableports.add(cport52);
		dev5.name = "Девайс5";
		Pool.put(SchemeDevice.typ, dev5.getId(), dev5);
		
		el1 = new SchemeElement("el1");
		el1.description = "Описалово";
		el1.devices.add(dev1);
		el1.name = "Один элемент";
		el1.setSchemeId("scheme1");
		Pool.put(SchemeElement.typ, el1.getId(), el1);

		el2 = new SchemeElement("el2");
		el2.description = "Описалово";
		el2.devices.add(dev2);
		el2.name = "Другой элемент";
		el2.setSchemeId("scheme1");
		Pool.put(SchemeElement.typ, el2.getId(), el2);

		el3 = new SchemeElement("el3");
		el3.description = "Описалово";
		el3.devices.add(dev3);
		el3.name = "Другой элемент3";
		el3.setSchemeId("scheme1");
		Pool.put(SchemeElement.typ, el3.getId(), el3);

		el4 = new SchemeElement("el4");
		el4.description = "Описалово";
		el4.devices.add(dev4);
		el4.name = "Другой элемент4";
		el4.setSchemeId("scheme1");
		Pool.put(SchemeElement.typ, el4.getId(), el4);

		el5 = new SchemeElement("el5");
		el5.description = "Описалово";
		el5.devices.add(dev5);
		el5.name = "Другой элемент5";
		el5.setSchemeId("scheme1");
		Pool.put(SchemeElement.typ, el5.getId(), el5);

		link = new SchemeLink("link");
		link.name = "Патчкорд";
		link.opticalLength = 3.0D;
		link.physicalLength = 3.0D;
		link.sourcePortId = "port12";
		link.targetPortId = "port11";
		link.setSchemeId("scheme1");
		Pool.put(SchemeLink.typ, link.getId(), link);

		clink1 = new SchemeCableLink("clink1");
		clink1.setName("Кабелёк1");
		clink1.opticalLength = 1000.0D;
		clink1.physicalLength = 1000.0D;
		clink1.sourcePortId = "cport21";
		clink1.targetPortId = "cport11";
		clink1.setSchemeId("scheme1");
		Pool.put(SchemeCableLink.typ, clink1.getId(), clink1);

		clink2 = new SchemeCableLink("clink2");
		clink2.setName("Кабелёк2");
		clink2.opticalLength = 1000.0D;
		clink2.physicalLength = 1000.0D;
		clink2.sourcePortId = "cport22";
		clink2.targetPortId = "cport31";
		clink2.setSchemeId("scheme1");
		Pool.put(SchemeCableLink.typ, clink2.getId(), clink2);

		clink3 = new SchemeCableLink("clink3");
		clink3.setName("Кабелёк3");
		clink3.opticalLength = 1000.0D;
		clink3.physicalLength = 1000.0D;
		clink3.sourcePortId = "cport32";
		clink3.targetPortId = "cport41";
		clink3.setSchemeId("scheme1");
		Pool.put(SchemeCableLink.typ, clink3.getId(), clink3);

		clink4 = new SchemeCableLink("clink4");
		clink4.setName("Кабелёк4");
		clink4.opticalLength = 1000.0D;
		clink4.physicalLength = 1000.0D;
		clink4.sourcePortId = "cport42";
		clink4.targetPortId = "cport51";
		clink4.setSchemeId("scheme1");
		Pool.put(SchemeCableLink.typ, clink4.getId(), clink4);

		clink5 = new SchemeCableLink("clink5");
		clink5.setName("Кабелёк5");
		clink5.opticalLength = 1000.0D;
		clink5.physicalLength = 1000.0D;
		clink5.sourcePortId = "cport52";
		clink5.targetPortId = "cport33";
		clink5.setSchemeId("scheme1");
		Pool.put(SchemeCableLink.typ, clink5.getId(), clink5);

		pel1 = new PathElement();
		pel1.startPortId = "port12";
		pel1.endPortId = "port11";
		pel1.n = 0;
		pel1.schemeId = "scheme1";
		pel1.setSchemeLink(link);
		pel1.setOpticalLength(3.0);
		pel1.setPhysicalLength(3.0);
		pel1.setType(PathElement.LINK);
		
		pel2 = new PathElement();
		pel2.endPortId = "";
		pel2.n = 1;
		pel2.schemeId = "scheme1";
		pel2.setSchemeElement(el1);
		pel2.startPortId = "";
		pel2.setType(PathElement.SCHEME_ELEMENT);
		
		pel3 = new PathElement();
		pel3.startPortId = "cport11";
		pel3.endPortId = "cport21";
		pel3.n = 2;
		pel3.schemeId = "scheme1";
		pel3.setSchemeCableLink(clink1);
		pel3.setOpticalLength(1000.0);
		pel3.setPhysicalLength(1000.0);
		pel3.setType(PathElement.CABLE_LINK);
		
		pel4 = new PathElement();
		pel4.endPortId = "";
		pel4.n = 3;
		pel4.schemeId = "scheme1";
		pel4.setSchemeElement(el2);
		pel4.startPortId = "";
		pel4.setType(PathElement.SCHEME_ELEMENT);
		
		pel5 = new PathElement();
		pel5.startPortId = "cport22";
		pel5.endPortId = "cport31";
		pel5.n = 4;
		pel5.schemeId = "scheme1";
		pel5.setSchemeCableLink(clink2);
		pel5.setOpticalLength(1000.0);
		pel5.setPhysicalLength(1000.0);
		pel5.setType(PathElement.CABLE_LINK);
		
		pel6 = new PathElement();
		pel6.endPortId = "";
		pel6.n = 5;
		pel6.schemeId = "scheme1";
		pel6.setSchemeElement(el3);
		pel6.startPortId = "";
		pel6.setType(PathElement.SCHEME_ELEMENT);
		
		pel7 = new PathElement();
		pel7.startPortId = "cport32";
		pel7.endPortId = "cport41";
		pel7.n = 6;
		pel7.schemeId = "scheme1";
		pel7.setSchemeCableLink(clink3);
		pel7.setOpticalLength(1000.0);
		pel7.setPhysicalLength(1000.0);
		pel7.setType(PathElement.CABLE_LINK);
		
		path = new SchemePath("path");
		path.startDeviceId = "dev1";
		path.endDeviceId = "dev4";
		path.links.add(pel1);
		path.links.add(pel2);
		path.links.add(pel3);
		path.links.add(pel4);
		path.links.add(pel5);
		path.links.add(pel6);
		path.links.add(pel7);
		path.name = "Путяра измерений";
		path.setSchemeId("scheme1");
		Pool.put(SchemePath.typ, path.getId(), path);

		scheme = new Scheme();
		scheme.cablelinks.add(clink1);
		scheme.cablelinks.add(clink2);
		scheme.cablelinks.add(clink3);
		scheme.cablelinks.add(clink4);
		scheme.cablelinks.add(clink5);
		scheme.links.add(link);
		scheme.solution.paths.add(path);
		scheme.created = System.currentTimeMillis();
		scheme.modified = System.currentTimeMillis();
		scheme.createdBy = getSession().getUserId();
		scheme.modifiedBy = getSession().getUserId();
		scheme.description = "";
		scheme.domainId = getSession().getDomainId();
		scheme.elements.add(el1);
		scheme.elements.add(el2);
		scheme.elements.add(el3);
		scheme.elements.add(el4);
		scheme.elements.add(el5);
		scheme.height = 100;
		scheme.id = "scheme1";
		scheme.label = "lab1";
		scheme.name = "Схемка";
		scheme.ownerId = getSession().getUserId();
		scheme.schemeType = Scheme.NETWORK;
		scheme.width = 100;

		Pool.put(Scheme.typ, scheme.getId(), scheme);
	}

	private void scheme2()
	{
		Scheme scheme;
		SchemeLink link;
		SchemePort port1;
		SchemePort port2;
		SchemeCablePort cport1;
		SchemeCablePort cport2;
		SchemeCableLink clink;
		SchemeElement el1;
		SchemeElement el2;
		SchemeDevice dev1;
		SchemeDevice dev2;
		SchemePath path;
		PathElement pel1;
		PathElement pel2;
		PathElement pel3;

		port1 = new SchemePort("_port1");
		port1.deviceId = "_dev1";
		port1.directionType = "out";
		port1.name = "Порт раз";
		port1.linkId = "_link";
		Pool.put(SchemePort.typ, port1.getId(), port1);

		port2 = new SchemePort("_port2");
		port2.deviceId = "_dev2";
		port2.directionType = "in";
		port2.name = "Порт два";
		port2.linkId = "_link";
		Pool.put(SchemePort.typ, port2.getId(), port2);

		cport1 = new SchemeCablePort("_cport1");
		cport1.cableLinkId = "_clink";
		cport1.deviceId = "_dev1";
		cport1.directionType = "in";
		cport1.name = "Каборт раз";
		Pool.put(SchemeCablePort.typ, cport1.getId(), cport1);

		cport2 = new SchemeCablePort("_cport2");
		cport2.cableLinkId = "_clink";
		cport2.deviceId = "_dev2";
		cport2.directionType = "out";
		cport2.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport2.getId(), cport2);

		dev1 = new SchemeDevice("_dev1");
		dev1.cableports.add(cport1);
		dev1.name = "Девайс1";
		dev1.ports.add(port1);
		Pool.put(SchemeDevice.typ, dev1.getId(), dev1);

		dev2 = new SchemeDevice("_dev2");
		dev2.cableports.add(cport2);
		dev2.name = "Девайс2";
		dev2.ports.add(port2);
		Pool.put(SchemeDevice.typ, dev2.getId(), dev2);
		
		el1 = new SchemeElement("_el1");
		el1.description = "Описалово";
		el1.devices.add(dev1);
		el1.name = "Один элемент";
		el1.setSchemeId("_scheme1");
		Pool.put(SchemeElement.typ, el1.getId(), el1);

		el2 = new SchemeElement("_el2");
		el2.description = "Описалово";
		el2.devices.add(dev2);
		el2.name = "Другой элемент";
		el2.setSchemeId("_scheme1");
		Pool.put(SchemeElement.typ, el2.getId(), el2);

		link = new SchemeLink("_link");
		link.name = "Патчкорд";
		link.opticalLength = 1000.0D;
		link.physicalLength = 1000.0D;
		link.sourcePortId = "_port2";
		link.targetPortId = "_port1";
		link.setSchemeId("_scheme1");
		Pool.put(SchemeLink.typ, link.getId(), link);

		clink = new SchemeCableLink("_clink");
		clink.setName("Кабелёк");
		clink.opticalLength = 1000.0D;
		clink.physicalLength = 1000.0D;
		clink.sourcePortId = "_cport2";
		clink.targetPortId = "_cport1";
		clink.setSchemeId("_scheme1");
		Pool.put(SchemeCableLink.typ, clink.getId(), clink);

		pel1 = new PathElement();
		pel1.endPortId = "_port2";
		pel1.n = 0;
		pel1.schemeId = "_scheme1";
		pel1.setSchemeLink(link);
		pel1.startPortId = "_port1";
		pel1.setType(PathElement.LINK);
		
		pel2 = new PathElement();
		pel2.endPortId = "";
		pel2.n = 2;
		pel2.schemeId = "_scheme1";
		pel2.setSchemeElement(el2);
		pel2.startPortId = "";
		pel2.setType(PathElement.SCHEME_ELEMENT);
		
		pel3 = new PathElement();
		pel3.endPortId = "_cport1";
		pel3.n = 2;
		pel3.schemeId = "_scheme1";
		pel3.setSchemeCableLink(clink);
		pel3.startPortId = "_cport2";
		pel3.setType(PathElement.CABLE_LINK);
		
		path = new SchemePath("_path");
		path.endDeviceId = "_dev1";
		path.links.add(pel1);
		path.links.add(pel2);
		path.links.add(pel3);
		path.name = "Путяра измерений";
		path.startDeviceId = "_dev1";
		path.setSchemeId("_scheme1");
		Pool.put(SchemePath.typ, path.getId(), path);

		scheme = new Scheme();
		scheme.cablelinks.add(clink);
		scheme.links.add(link);
		scheme.solution.paths.add(path);
		scheme.created = System.currentTimeMillis();
		scheme.modified = System.currentTimeMillis();
		scheme.createdBy = getSession().getUserId();
		scheme.modifiedBy = getSession().getUserId();
		scheme.description = "";
		scheme.domainId = getSession().getDomainId();
		scheme.elements.add(el1);
		scheme.elements.add(el2);
		scheme.height = 100;
		scheme.id = "_scheme1";
		scheme.label = "lab1";
		scheme.name = "Схемочка";
		scheme.ownerId = getSession().getUserId();
		scheme.schemeType = Scheme.NETWORK;
		scheme.width = 100;

		Pool.put(Scheme.typ, scheme.getId(), scheme);
	}
	
	public void loadMapViews(String[] ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		String fileName = openFileForReading();
		if(fileName == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка чтения");
		}
		else
		{
			try
			{
				FileInputStream in = new FileInputStream(fileName);
				ObjectInputStream ias = new ObjectInputStream(in);
				MapView mv = (MapView )ias.readObject();
				ias.close();
				
//				Pool.put(MapView.typ, mv.getId(), mv);
			}
			catch(FileNotFoundException e)
			{
			}
			catch(ClassNotFoundException e)
			{
			}
			catch(IOException e)
			{
			}
		}
	}
	
	public void SaveMapView(String mvId)
	{
		saveMapViews(new String[] {mvId});
	}

	public void saveMapViews(String[] mv_ids)
	{
		MapView mv = (MapView )Pool.get(MapView.typ, mv_ids[0]);

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;
			
		String fileName = openFileForWriting();
		if(fileName == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка записи");
			return;
		}
		try
		{
			FileOutputStream out = new FileOutputStream(fileName);
			ObjectOutputStream oas = new ObjectOutputStream(out);
			oas.writeObject(mv);
			oas.flush();
			oas.close();
		}
		catch(FileNotFoundException e)
		{
		}
		catch(IOException e)
		{
		}
	}

}
