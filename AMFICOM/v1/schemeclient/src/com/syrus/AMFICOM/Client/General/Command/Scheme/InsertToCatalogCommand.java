package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.AccessPortType;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import com.syrus.AMFICOM.Client.Schematics.Elements.CatalogElementsDialog;

public class InsertToCatalogCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;
	SchemePanel panel;
	UgoPanel ugo_panel;
	boolean include_paths = false;

	public InsertToCatalogCommand(ApplicationContext aContext, SchemePanel panel, UgoPanel ugo_panel, boolean include_paths)
	{
		this.aContext = aContext;
		this.include_paths = include_paths;
		this.panel = panel;
		this.ugo_panel = ugo_panel;
		this.graph = panel.getGraph();
	}

//	public InsertToCatalogCommand(ApplicationContext aContext, SchemeGraph graph)
//	{
//		this(aContext, graph, false);
//	}

//	public InsertToCatalogCommand(ApplicationContext aContext, SchemeGraph graph, boolean include_paths)
//	{
//		this.aContext = aContext;
//		this.graph = graph;
//		this.include_paths = include_paths;
//	}

	public Object clone()
	{
		return new InsertToCatalogCommand(aContext, panel, ugo_panel, include_paths);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		//Save elements
		Object[] cells = graph.getSelectionCells();

		if (cells.length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Не выбраны элементы для связывания с каталогом", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		Map elements_to_save = new HashMap();
		Map links_to_save = new HashMap();
		Map cable_links_to_save = new HashMap();
		Map paths_to_save = new HashMap();

		findElementsToSave (cells,
												elements_to_save,
												links_to_save,
												cable_links_to_save,
												paths_to_save);


		if (!elements_to_save.isEmpty() ||
				!links_to_save.isEmpty() ||
				!cable_links_to_save.isEmpty() ||
				!paths_to_save.isEmpty())
		{
			if (save(dataSource, elements_to_save,links_to_save, cable_links_to_save, paths_to_save))
			{
				graph.setGraphChanged(true);
				if (ugo_panel != null)
					new SchemeSaveCommand(aContext, panel, ugo_panel).execute();
			}
		}
	}

	boolean save (DataSourceInterface dataSource,
						 Map elements_to_save,
						 Map links_to_save,
						 Map cable_links_to_save,
						 Map paths_to_save)
	{
		CatalogElementsDialog dialog = new CatalogElementsDialog(aContext);

//			DataSet d = new DataSet(elements_to_save);
//			d.add(new DataSet(links_to_save));
//			d.add(new DataSet(cable_links_to_save));
//			d.add(new DataSet(paths_to_save));

			Map d = new HashMap(elements_to_save);
			d.putAll(links_to_save);
			d.putAll(cable_links_to_save);
			d.putAll(paths_to_save);

			int res = dialog.init(d);
			if (res != dialog.OK)
				return false;

			Map mapping = dialog.getMapping();
			boolean status = true;

			for (Iterator it = elements_to_save.values().iterator(); it.hasNext();)
			{
				SchemeElement se = (SchemeElement)it.next();
				Object obj = mapping.get(se.getId());
				if (obj instanceof Equipment)
				{
					se.equipment_id = ((Equipment)obj).getId();
					status = saveEquipment(dataSource, se, "");
				}
				else if (obj.equals(""))
					se.equipment_id = "";
				else
					status = saveEquipment(dataSource, se, (String)obj);
			}
			if (!status)
				return false;

			for (Iterator it = links_to_save.values().iterator(); it.hasNext();)
			{
				SchemeLink sl = (SchemeLink)it.next();
				Object obj = mapping.get(sl.getId());
				if (obj instanceof Link)
				{
					sl.link_id = ((Link)obj).getId();
					status = saveLink(dataSource, sl, "");
				}
				else if (obj.equals(""))
					sl.link_id = "";
				else
					status = saveLink(dataSource, sl, (String)obj);
			}
			if (!status)
				return false;

			for (Iterator it = cable_links_to_save.values().iterator(); it.hasNext();)
			{
				SchemeCableLink scl = (SchemeCableLink)it.next();
				Object obj = mapping.get(scl.getId());
				if (obj instanceof CableLink)
				{
					scl.cable_link_id = ((CableLink)obj).getId();
					status = saveCableLink(dataSource, scl, "");
				}
				else if (obj.equals(""))
					scl.cable_link_id = "";
				else
					status = saveCableLink(dataSource, scl, (String)obj);
			}
			if (!status)
				return false;

			for (Iterator it = paths_to_save.values().iterator(); it.hasNext();)
			{
				SchemePath sp = (SchemePath)it.next();
				Object obj = mapping.get(sp.getId());
				if (obj instanceof TransmissionPath)
				{
					sp.path_id = ((TransmissionPath)obj).getId();
					status = savePath(dataSource, sp, "");
				}
				else if (obj.equals(""))
					sp.path_id = "";
				else
					status = savePath(dataSource, sp, (String)obj);
			}
			if (!status)
				return false;
			return true;
	}

	void findElementsToSave(Object[] cells,
													Map elements_to_save,
													Map links_to_save,
													Map cable_links_to_save,
													Map paths_to_save)
	{
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DeviceGroup)
			{
			SchemeElement element = ((DeviceGroup)cells[i]).getSchemeElement();
			//saveEquipment(dataSource, element);
			if (element.scheme_id.equals(""))
			{
				elements_to_save.put(element.getId(), element);

				/*
				разремарчено 04.03.04 потому как без внутренних элементов не вносятся
				порты и соответственно глючат пасы и линки
				*/

				for (Iterator it = element.getAllChilds().iterator(); it.hasNext();)
				{
					SchemeElement se = (SchemeElement)it.next();
					elements_to_save.put(se.getId(), se);
				}
				for (Iterator it = element.getAllElementsLinks().iterator(); it.hasNext();)
				{
					SchemeLink sl = (SchemeLink)it.next();
					links_to_save.put(sl.getId(), sl);
				}
				/**/
			}
			else
			{
				Scheme inner_scheme = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
				SchemePanel virtual_panel = new SchemePanel(aContext);
				virtual_panel.openScheme(inner_scheme);
				findElementsToSave(virtual_panel.getGraph().getAll(),
													elements_to_save,
													links_to_save,
													cable_links_to_save,
													paths_to_save);
				if (panel != null)
					panel.schemes_to_save.add(inner_scheme);
			}
		}
		if (cells[i] instanceof DefaultLink)
		{
			DefaultLink link = (DefaultLink)cells[i];
			SchemeLink scheme_link = link.getSchemeLink();
			//saveLink (dataSource, scheme_link);
			links_to_save.put(scheme_link.getId(), scheme_link);
			/*String path_id = link.getSchemePathId();
			if (include_paths && !path_id.equals("") && !paths_to_save.containsKey(path_id))
			{
				SchemePath path = link.getSchemePath();
				paths_to_save.put(path.getId(), path);
				for (Enumeration e = path.links.elements(); e.hasMoreElements();)
				{
					PathElement pe = (PathElement)e.nextElement();
					if (pe.is_cable)
					{
						SchemeCableLink cl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
						cable_links_to_save.put(cl.getId(), cl);
					}
					else
					{
						SchemeLink l = (SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
						links_to_save.put(l.getId(), l);
					}
				}
			}*/
		}
		if (cells[i] instanceof DefaultCableLink)
		{
			DefaultCableLink link = (DefaultCableLink)cells[i];
			SchemeCableLink scheme_link = link.getSchemeCableLink();
			//saveCableLink(dataSource, scheme_link);
			cable_links_to_save.put(scheme_link.getId(), scheme_link);
			/*String path_id = link.getSchemePathId();
			if (include_paths && !path_id.equals("") && !paths_to_save.containsKey(path_id))
			{
				SchemePath path = link.getSchemePath();
				paths_to_save.put(path.getId(), path);
				for (Enumeration e = path.links.elements(); e.hasMoreElements();)
				{
					PathElement pe = (PathElement)e.nextElement();
					if (pe.is_cable)
					{
						SchemeCableLink cl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
						cable_links_to_save.put(cl.getId(), cl);
					}
					else
					{
						SchemeLink l = (SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
						links_to_save.put(l.getId(), l);
					}
				}
			}*/
		}
		}
	}


	boolean savePath(DataSourceInterface dataSource, SchemePath scheme_path, String name)
	{
		try
		{
			TransmissionPath path = (TransmissionPath)Pool.get(TransmissionPath.typ, scheme_path.path_id);
			if (path == null)
			{
				path = new TransmissionPath();
				path.id = dataSource.GetUId(TransmissionPath.typ);
				path.name = name.equals("") ? scheme_path.getName() : name;
				path.description = "";
				path.domain_id = dataSource.getSession().getDomainId();
				SchemeElement element = (SchemeElement)Pool.get(SchemeElement.typ, scheme_path.start_device_id);
				path.KIS_id = element.equipment_id;

				scheme_path.path_id = path.getId();
				Pool.put(TransmissionPath.typ, path.getId(), path);
				System.out.println("Add to catalog TransmissionPath with id " + path.getId());

				path.local_address = "";
				path.monitored_element_id = "";
			}

			String access_port_id = "";
			for (Iterator it = scheme_path.links.iterator(); it.hasNext();)
			{
				PathElement pel = (PathElement)it.next();

				if (pel.is_cable)
				{
					SchemeCableLink link = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pel.link_id);
					SchemeCablePort sp = (SchemeCablePort)Pool.get(SchemeCablePort.typ, link.source_port_id);
					CablePort cp = (CablePort)Pool.get(CablePort.typ, sp.cable_port_id);
					if (cp.equipment_id.equals(path.KIS_id))
					{
						access_port_id = sp.access_port_id;
						break;
					}
					else
					{
						sp = (SchemeCablePort)Pool.get(SchemeCablePort.typ, link.target_port_id);
						cp = (CablePort)Pool.get(CablePort.typ, sp.cable_port_id);
						if (cp.equipment_id.equals(path.KIS_id))
						{
							access_port_id = sp.access_port_id;
							break;
						}
					}
				}
				else
				{
					SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, pel.link_id);
					SchemePort sp = (SchemePort)Pool.get(SchemePort.typ, link.source_port_id);
					Port cp = (Port)Pool.get(Port.typ, sp.port_id);
					if (cp != null && cp.equipment_id.equals(path.KIS_id))
					{
						access_port_id = sp.access_port_id;
						break;
					}
					else
					{
						sp = (SchemePort)Pool.get(SchemePort.typ, link.target_port_id);
						cp = (Port)Pool.get(Port.typ, sp.port_id);
						if (cp != null && cp.equipment_id.equals(path.KIS_id))
						{
							access_port_id = sp.access_port_id;
							break;
						}
					}
				}
			}

			path.access_port_id = access_port_id;

			path.links = new ArrayList();

			for (Iterator it = scheme_path.links.iterator(); it.hasNext();)
			{
				TransmissionPathElement tpe = new TransmissionPathElement();
				PathElement pe = (PathElement)it.next();
				tpe.n = pe.n;
				tpe.is_cable = pe.is_cable;
				tpe.link_id = (String)Pool.get("clonedids", pe.link_id);

			//      SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
			//      tpe.link_id = link.link_id;

				if (tpe.is_cable)
					tpe.thread_id = (String)Pool.get("clonedids", pe.thread_id);
				path.links.add(tpe);
			}

			dataSource.SavePath(path.getId());
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении маршрута " + name, "Ошибка", JOptionPane.OK_OPTION);
			return false;
		}
	}

	boolean saveLink(DataSourceInterface dataSource, SchemeLink scheme_link, String name)
	{
		try
		{
			Link link = (Link)Pool.get(Link.typ, scheme_link.link_id);
			if (link == null)
			{
				link = new Link();
				link.id = dataSource.GetUId(Link.typ);
				scheme_link.link_id = link.getId();
				Pool.put(Link.typ, link.getId(), link);
				System.out.println("Add to catalog Link with id " + link.getId());

				LinkType link_type = (LinkType)Pool.get(LinkType.typ, scheme_link.link_type_id);

				link.name = name.equals("") ? scheme_link.getName() : name;
				link.domain_id = dataSource.getSession().getDomainId();
				link.type_id = link_type.getId();
				link.description = link_type.description;
				link.inventory_nr = "";
				link.manufacturer = link_type.manufacturer;
				link.manufacturer_code = link_type.manufacturer_code;
				link.supplier = "";
				link.supplier_code = "";
				link.link_class = link_type.link_class;
				link.image_id = "";
				link.characteristics = ResourceUtil.copyCharacteristics(dataSource, link_type.characteristics);
			}
			link.optical_length = scheme_link.optical_length;
			link.physical_length = scheme_link.physical_length;

			SchemePort scheme_port = (SchemePort)Pool.get(SchemePort.typ, scheme_link.source_port_id);
			if (scheme_port == null || scheme_port.port_id.equals(""))
			{
				link.start_equipment_id = "";
				link.start_port_id = "";
			}
			else
			{
				Port port = (Port)Pool.get(Port.typ, scheme_port.port_id);
				link.start_port_id = (port == null ? "" : port.getId());
				link.start_equipment_id = (port == null ? "" : port.equipment_id);
			}
			scheme_port = (SchemePort)Pool.get(SchemePort.typ, scheme_link.target_port_id);
			if (scheme_port == null || scheme_port.port_id.equals(""))
			{
				link.end_equipment_id = "";
				link.end_port_id = "";
			}
			else
			{
				Port port = (Port)Pool.get(Port.typ, scheme_port.port_id);
				link.end_port_id = (port == null ? "" : port.getId());
				link.end_equipment_id = (port == null ? "" : port.equipment_id);
			}

			Pool.put("clonedids", scheme_link.getId(), link.getId());

			dataSource.SaveLink(scheme_link.link_id);
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении линии связи " + name, "Ошибка", JOptionPane.OK_OPTION);
			return false;
		}
	}

	boolean saveEquipment(DataSourceInterface dataSource, SchemeElement element, String name)
	{
		try
		{
			Equipment eq = (Equipment) Pool.get(Equipment.typ, element.equipment_id);
			KIS kis = null;
			if (eq == null) {
				kis = (KIS) Pool.get(KIS.typ, element.equipment_id);
				if (kis != null) {
					eq = kis;
					if (eq.is_kis)
						kis = (KIS) eq;
				}
			}

			ProtoElement proto = (ProtoElement) Pool.get(ProtoElement.typ,
					element.proto_element_id);
			EquipmentType eqt = (EquipmentType) Pool.get(EquipmentType.typ,
					proto.equipment_type_id);

			if (eq == null) {
				if (eqt.eq_class.equals("tester")) {
					kis = new KIS();
					eq = kis;
					eq.id = dataSource.GetUId(KIS.typ);
					Pool.put(KIS.typ, eq.getId(), eq);
					Pool.put("kisequipment", eq.getId(), eq);
					element.equipment_id = eq.getId();
					eq.is_kis = true;
					System.out.println("Add to catalog KIS with id " + kis.getId());
				}
				else {
					eq = new Equipment();
					eq.id = dataSource.GetUId(Equipment.typ);
					Pool.put(Equipment.typ, eq.getId(), eq);
					Pool.put("kisequipment", eq.getId(), eq);
					element.equipment_id = eq.getId();
					eq.is_kis = false;
					System.out.println("Add to catalog Equipment with id " + eq.getId());
				}
				eq.port_ids = new ArrayList();
				eq.ports = new ArrayList();
				eq.cport_ids = new ArrayList();
				eq.cports = new ArrayList();

				eq.name = name.equals("") ? element.getName() : name;
				eq.description = eqt.description;
				eq.type_id = eqt.getId();
				eq.longitude = "";
				eq.latitude = "";
				eq.hw_serial = "";
				eq.sw_serial = "";
				eq.hw_version = "";
				eq.sw_version = "";
				eq.inventory_nr = "";
				eq.manufacturer = eqt.manufacturer;
				eq.manufacturer_code = eqt.manufacturer_code;
				eq.supplier = "";
				eq.supplier_code = "";
				eq.eq_class = eqt.eq_class;
				eq.agent_id = "";
				eq.domain_id = "";
				eq.image_id = eqt.image_id;
				eq.domain_id = dataSource.getSession().getDomainId();

				eq.characteristics = ResourceUtil.copyCharacteristics(dataSource,
						eqt.characteristics);
			}

			for (Iterator dit = element.devices.iterator(); dit.hasNext();)
			{
				SchemeDevice dev = (SchemeDevice)dit.next();

				for (Iterator it = dev.ports.iterator(); it.hasNext();)
				{
					Port port = null;
					SchemePort scheme_port = (SchemePort)it.next();

					for (Iterator pit = eq.ports.iterator(); pit.hasNext();)
					{
						Port p = (Port)pit.next();
						if (p.getId().equals(scheme_port.port_id))
						{
							port = p;
							break;
						}
					}

					if (port == null)
					{
						port = new Port();
						port.id = dataSource.GetUId(Port.typ);
						Pool.put(Port.typ, port.getId(), port);
						scheme_port.port_id = port.getId();
						System.out.println("Add to catalog Port with id " + port.getId());
						eq.port_ids.add(port.getId());
						eq.ports.add(port);

						PortType port_type = (PortType)Pool.get(PortType.typ, scheme_port.port_type_id);

						port.name = scheme_port.getName();
						port.description = port_type.description;
						port.interface_id = "";
						port.address_id = "";
						port.local_id = "";
						port.type_id = port_type.getId();
						port.equipment_id = eq.getId();
						port.characteristics = ResourceUtil.copyCharacteristics(dataSource, port_type.characteristics);
					}

					if(scheme_port.is_access_port && eq.is_kis)
					{
						AccessPortType aport_type = (AccessPortType)Pool.get(AccessPortType.typ, scheme_port.access_port_type_id);
						AccessPort aport = null;

						for (Iterator pit = kis.access_ports.iterator(); pit.hasNext();)
						{
							AccessPort a = (AccessPort)pit.next();
							if (a.getId().equals(scheme_port.access_port_id))
							{
								aport = a;
								break;
							}
						}

						if (aport == null)
						{
							aport = new AccessPort();
							aport.id = dataSource.GetUId(AccessPort.typ);
							Pool.put(AccessPort.typ, aport.getId(), aport);
							System.out.println("Add to catalog AccessPort with id " + aport.getId());
							scheme_port.access_port_id = aport.getId();
							kis.access_ports.add(aport);

							aport.name = aport_type.getName();
							aport.type_id = aport_type.getId();
							aport.port_id = port.getId();
							aport.KIS_id = kis.getId();
							aport.local_id = "";
							aport.characteristics = ResourceUtil.copyCharacteristics(dataSource, aport_type.characteristics);
						}
					}
				}

				for (Iterator it = dev.cableports.iterator(); it.hasNext();)
				{
					CablePort port = null;
					SchemeCablePort scheme_port = (SchemeCablePort)it.next();

					for (Iterator pit = eq.cports.iterator(); pit.hasNext();)
					{
						CablePort p = (CablePort)pit.next();
						if (p.getId().equals(scheme_port.cable_port_id))
						{
							port = p;
							break;
						}
					}
					if (port == null)
					{
						port = new CablePort();
						port.id = dataSource.GetUId(CablePort.typ);
						Pool.put(CablePort.typ, port.getId(), port);
						System.out.println("Add to catalog CablePort with id " + port.getId());
						scheme_port.cable_port_id = port.getId();
						eq.cport_ids.add(port.getId());
						eq.cports.add(port);

						CablePortType port_type = (CablePortType)Pool.get(CablePortType.typ, scheme_port.cable_port_type_id);

						port.name = scheme_port.getName();
						port.description = port_type.description;
						port.interface_id = "";
						port.address_id = "";
						port.local_id = "";
						port.type_id = port_type.getId();
						port.equipment_id = eq.getId();
						port.characteristics = ResourceUtil.copyCharacteristics(dataSource, port_type.characteristics);
					}
				}
				eq.s_port_ids = new ArrayList();
				eq.test_ports = new ArrayList();

				if(eq.is_kis)
				{
					dataSource.SaveKIS(eq.getId());
				}
				else
				{
					dataSource.SaveEquipment(eq.getId());
				}
			}

			for (Iterator it = element.links.iterator(); it.hasNext();)
			{
				SchemeLink link = (SchemeLink)it.next();
				if (link.link_id.equals(""))
					saveLink(dataSource, link, link.getName());
			}
			//for (int i = 0; i < element.element_ids.size(); i++)
			for (Iterator it = element.getChildElements().iterator(); it.hasNext();)
			{
				SchemeElement el = (SchemeElement)it.next();
			//{
			//	String id = (String)element.element_ids.get(i);
			//	SchemeElement el = (SchemeElement)Pool.get(SchemeElement.typ, id);
				if (el.equipment_id.equals(""))
					saveEquipment(dataSource, el, el.getName());
			}
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении оборудования " + name, "Ошибка", JOptionPane.OK_OPTION);
			return false;
		}
	}

	boolean saveCableLink(DataSourceInterface dataSource, SchemeCableLink scheme_link, String name)
	{
		try
		{
			CableLink link = (CableLink)Pool.get(CableLink.typ, scheme_link.cable_link_id);
			if (link == null)//true)//
			{
				link = new CableLink();
				link.id = dataSource.GetUId(CableLink.typ);
				scheme_link.cable_link_id = link.getId();
				Pool.put(CableLink.typ, link.getId(), link);
				System.out.println("Add to catalog CableLink with id " + link.getId());
				link.threads = new ArrayList();

				CableLinkType link_type = (CableLinkType)Pool.get(CableLinkType.typ, scheme_link.cable_link_type_id);

				link.name = name.equals("") ? scheme_link.getName() : name;
				link.type_id = link_type.getId();
				link.description = link_type.description;
				link.inventory_nr = "";
				link.manufacturer = link_type.manufacturer;
				link.manufacturer_code = link_type.manufacturer_code;
				link.supplier = "";
				link.supplier_code = "";
				link.link_class = link_type.link_class;
				link.domain_id = dataSource.getSession().getDomainId();
				link.image_id = "";
				link.characteristics = ResourceUtil.copyCharacteristics(dataSource, link_type.characteristics);
			}
			link.optical_length = scheme_link.optical_length;
			link.physical_length = scheme_link.physical_length;

			SchemeCablePort scheme_port = (SchemeCablePort)Pool.get(SchemeCablePort.typ, scheme_link.source_port_id);
			if (scheme_port == null || scheme_port.cable_port_id.equals(""))
			{
				link.start_equipment_id = "";
				link.start_port_id = "";
			}
			else
			{
				CablePort port = (CablePort)Pool.get(CablePort.typ, scheme_port.cable_port_id);
				link.start_port_id = (port == null ? "" : port.getId());
				link.start_equipment_id = (port == null ? "" : port.equipment_id);
			}
			scheme_port = (SchemeCablePort)Pool.get(SchemeCablePort.typ, scheme_link.target_port_id);
			if (scheme_port == null || scheme_port.cable_port_id.equals(""))
			{
				link.end_equipment_id = "";
				link.end_port_id = "";
			}
			else
			{
				CablePort port = (CablePort)Pool.get(CablePort.typ, scheme_port.cable_port_id);
				link.end_port_id = (port == null ? "" : port.getId());
				link.end_equipment_id = (port == null ? "" : port.equipment_id);
			}

			//		link.end_equipment_id = "";
			//		link.end_port_id = "";

			for (Iterator it = scheme_link.cable_threads.iterator(); it.hasNext();)
			{
				CableLinkThread thread = null;
				SchemeCableThread scheme_thread = (SchemeCableThread)it.next();
				for (Iterator it2 = link.threads.iterator(); it2.hasNext();)
				{
					CableLinkThread t = (CableLinkThread)it2.next();
					if (t.getId().equals(scheme_thread.thread_id))
					{
						thread = t;
						break;
					}
				}
				if (thread == null)
				{
					thread = new CableLinkThread();
					thread.id = dataSource.GetUId(CableLinkThread.typ);
					thread.name = scheme_thread.getName();
					thread.link_type_id = scheme_thread.link_type_id;
					thread.mark = "";
					thread.color = "";

					Pool.put(CableLinkThread.typ, thread.getId(), thread);
					System.out.println("Add to catalog Thread with id " + thread.getId());
					scheme_thread.thread_id = thread.getId();
					link.threads.add(thread);
				}
				Pool.put("clonedids", scheme_thread.getId(), thread.getId());
			}
			Pool.put("clonedids", scheme_link.getId(), link.getId());

			dataSource.SaveCableLink(scheme_link.cable_link_id);
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при кабеля " + name, "Ошибка", JOptionPane.OK_OPTION);
			return false;
		}
	}

	SchemeElement getElementByPortId(SchemeElement element, String port_id)
	{
		for (Iterator it = element.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			for (Iterator pit = dev.cableports.iterator(); pit.hasNext();)
			{
				SchemeCablePort port = (SchemeCablePort)pit.next();
				if (port.getId().equals(port_id))
					return element;
			}
			for (Iterator pit = dev.ports.iterator(); pit.hasNext();)
			{
				SchemePort port = (SchemePort)pit.next();
				if (port.getId().equals(port_id))
					return element;
			}
		}
		//for (int i = 0; i < element.element_ids.size(); i++)
		//{
			//SchemeElement el = (SchemeElement)Pool.get(SchemeElement.typ, (String)element.element_ids.get(i));
		for (Iterator it = element.getChildElements().iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (getElementByPortId(el, port_id) != null)
				return el;
		}
		return null;
	}
}

