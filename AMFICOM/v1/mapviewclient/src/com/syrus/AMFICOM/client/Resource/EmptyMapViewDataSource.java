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

		port1 = new SchemePort("port1");
		port1.deviceId = "dev1";
		port1.directionType = "out";
		port1.name = "Порт раз";
		port1.linkId = "link";
		Pool.put(SchemePort.typ, port1.getId(), port1);

		port2 = new SchemePort("port2");
		port2.deviceId = "dev2";
		port2.directionType = "in";
		port2.name = "Порт два";
		port2.linkId = "link";
		Pool.put(SchemePort.typ, port2.getId(), port2);

		cport1 = new SchemeCablePort("cport1");
		cport1.cableLinkId = "clink";
		cport1.deviceId = "dev1";
		cport1.directionType = "in";
		cport1.name = "Каборт раз";
		Pool.put(SchemeCablePort.typ, cport1.getId(), cport1);

		cport2 = new SchemeCablePort("cport2");
		cport2.cableLinkId = "clink";
		cport2.deviceId = "dev2";
		cport2.directionType = "out";
		cport2.name = "Каборт два";
		Pool.put(SchemeCablePort.typ, cport2.getId(), cport2);

		dev1 = new SchemeDevice("dev1");
		dev1.cableports.add(cport1);
		dev1.name = "Девайс1";
		dev1.ports.add(port1);
		Pool.put(SchemeDevice.typ, dev1.getId(), dev1);

		dev2 = new SchemeDevice("dev2");
		dev2.cableports.add(cport2);
		dev2.name = "Девайс2";
		dev2.ports.add(port2);
		Pool.put(SchemeDevice.typ, dev2.getId(), dev2);
		
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

		link = new SchemeLink("link");
		link.name = "Патчкорд";
		link.opticalLength = 1000.0D;
		link.physicalLength = 1000.0D;
		link.sourcePortId = "port2";
		link.targetPortId = "port1";
		link.setSchemeId("scheme1");
		Pool.put(SchemeLink.typ, link.getId(), link);

		clink = new SchemeCableLink("clink");
		clink.setName("Кабелёк");
		clink.opticalLength = 1000.0D;
		clink.physicalLength = 1000.0D;
		clink.sourcePortId = "cport2";
		clink.targetPortId = "cport1";
		clink.setSchemeId("scheme1");
		Pool.put(SchemeCableLink.typ, clink.getId(), clink);

		pel1 = new PathElement();
		pel1.endPortId = "port2";
		pel1.n = 0;
		pel1.schemeId = "scheme1";
		pel1.setSchemeLink(link);
		pel1.startPortId = "port1";
		pel1.setType(PathElement.LINK);
		
		pel2 = new PathElement();
		pel2.endPortId = "";
		pel2.n = 2;
		pel2.schemeId = "scheme1";
		pel2.setSchemeElement(el2);
		pel2.startPortId = "";
		pel2.setType(PathElement.SCHEME_ELEMENT);
		
		pel3 = new PathElement();
		pel3.endPortId = "cport1";
		pel3.n = 2;
		pel3.schemeId = "scheme1";
		pel3.setSchemeCableLink(clink);
		pel3.startPortId = "cport2";
		pel3.setType(PathElement.CABLE_LINK);
		
		path = new SchemePath("path");
		path.endDeviceId = "dev1";
		path.links.add(pel1);
		path.links.add(pel2);
		path.links.add(pel3);
		path.name = "Путяра измерений";
		path.startDeviceId = "dev1";
		path.setSchemeId("scheme1");
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
		scheme.id = "scheme1";
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
				
				Pool.put(MapView.typ, mv.getId(), mv);
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
