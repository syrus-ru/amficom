package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeModel;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeNode;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;

public class SchemeTreeModel extends ObjectResourceTreeModel
{
	ApplicationContext aContext;

	private static Type[] schemeTypes = new Type[] {
		Type.NETWORK,
		Type.BUILDING,
		Type.CABLE_SUBNETWORK
	};

	public SchemeTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
				"root",
				"Сеть",
				true,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	public ObjectResourceCatalogActionModel getNodeActionModel(ObjectResourceTreeNode node)
	{
	/*	if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals(LinkType.typ) ||
							s.equals(CableLinkType.typ) ||
							s.equals(PortType.typ) ||
							s.equals(CablePortType.typ) ||
							s.equals(MeasurementPortType.typ) ||
							s.equals(TransmissionPathType.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
			else if(s.equals(SchemeCableLink.typ) ||
							s.equals(SchemeLink.typ) ||
							s.equals(SchemePath.typ) ||
							s.equals(SchemePort.typ) ||
							s.equals(SchemeCablePort.typ) ||
							s.equals(MeasurementPort.typ) ||
							s.equals(Scheme.typ) ||
							s.equals(SchemeElement.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			}*/
		return new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.NO_PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("SchemeProtoGroup"))
				return SchemeProtoGroup.class;
			if(s.equals("Scheme"))
				return Scheme.class;
			if(s.equals("SchemeElement"))
				return SchemeElement.class;
			if(s.equals("SchemeLink"))
				return SchemeLink.class;
			if(s.equals("SchemeCableLink"))
				return SchemeCableLink.class;
			if(s.equals("SchemePath"))
				return SchemePath.class;
			if(s.equals("SchemeProtoGroup"))
				return SchemeProtoGroup.class;
//			if(s.equals(EquipmentType.typ))
//				return EquipmentType.class;
			if(s.equals("LinkType"))
				return LinkType.class;
			if(s.equals("CableLinkType"))
				return CableLinkType.class;
			if(s.equals("PortType"))
				return PortType.class;
			if(s.equals("TransmissionPathType"))
				return TransmissionPathType.class;
			if(s.equals("MeasurementPortType"))
				return MeasurementPortType.class;
			if(s.equals("schemeTypes"))
				return Type.class;
		}
		else if (node.getObject() instanceof Type)
			return Scheme.class;
		else if (node.getObject() instanceof SchemeProtoGroup)
		{
			if (((SchemeProtoGroup)node.getObject()).schemeProtoGroups().length != 0)
				return SchemeProtoGroup.class;
			else
				return SchemeProtoElement.class;
		}
		else if (node.getObject() instanceof Scheme)
			return Scheme.class;
		else if (node.getObject() instanceof SchemeElement)
			return SchemeElement.class;
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				vec.add(new ObjectResourceTreeNode("configure", LangModelConfig.getString("label_configuration"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode ("SchemeProtoGroup", "Компоненты сети", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				ObjectResourceTreeNode sch = new ObjectResourceTreeNode ("schemeTypes", "Схемы", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
				vec.add(sch);
//				registerSearchableNode(Scheme.typ, sch);
			}
			else if(s.equals("configure"))
			{
				vec.add(new ObjectResourceTreeNode("netdirectory", LangModelConfig.getString("menuNetDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode("jdirectory", LangModelConfig.getString("menuJDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
			}
			else if(s.equals("netdirectory"))
			{
				vec.add(new ObjectResourceTreeNode("LinkType", LangModelConfig.getString("menuNetDirLinkText"), true));
				vec.add(new ObjectResourceTreeNode("CableLinkType", LangModelConfig.getString("menuNetDirCableText"), true));
				vec.add(new ObjectResourceTreeNode("PortType", LangModelConfig.getString("menuNetDirPortText"), true));
			}
			else if(s.equals("jdirectory"))
			{
				vec.add(new ObjectResourceTreeNode("MeasurementPortType", LangModelConfig.getString("menuJDirAccessPointText"), true));
				vec.add(new ObjectResourceTreeNode("TransmissionPathType", LangModelConfig.getString("menuJDirPathText"), true));
			}
			else if(s.equals("schemeTypes"))
			{
				for (int i = 0; i < schemeTypes.length; i++)
				{
					String type;
					switch (schemeTypes[i].value()) {
						case Type._NETWORK:
							type = LangModelSchematics.getString("NETWORK");
							break;
						case Type._CABLE_SUBNETWORK:
							type = LangModelSchematics.getString("CABLE_SUBNETWORK");
							break;
						default:
							type = LangModelSchematics.getString("BUILDING");
					}

					vec.add(new ObjectResourceTreeNode(schemeTypes[i], type, true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				}
			}
			else if (s.equals("LinkType")) {
				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					DomainCondition condition = new DomainCondition(domain,
							ObjectEntities.LINKTYPE_ENTITY_CODE);
					List linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = linkTypes.iterator(); it.hasNext(); ) {
						LinkType type = (LinkType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("CableLinkType"))
			{
			}
			else if (s.equals("PortType")) {
				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					DomainCondition condition = new DomainCondition(domain,
							ObjectEntities.PORTTYPE_ENTITY_CODE);
					List portTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = portTypes.iterator(); it.hasNext(); ) {
						PortType type = (PortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("TransmissionPathType")) {
				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					DomainCondition condition = new DomainCondition(domain,
							ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
					List pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						TransmissionPathType type = (TransmissionPathType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("MeasurementPortType")) {
				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					DomainCondition condition = new DomainCondition(domain,
							ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
					List pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						MeasurementPortType type = (MeasurementPortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("SchemeProtoGroup"))
			{
				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					DomainCondition condition = new DomainCondition(domain,
							ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
					List groups = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = groups.iterator(); it.hasNext(); ) {
						SchemeProtoGroup group = (SchemeProtoGroup)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(group, group.name(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("Scheme"))
			{
				Scheme parent = (Scheme)((ObjectResourceTreeNode)node.getParent()).getObject();
				List ds = new LinkedList();
				for (int i = 0; i < parent.schemeElements().length; i++)
				{
					SchemeElement el = (SchemeElement)parent.schemeElements()[i];
					if (el.internalScheme() != null)
						ds.add(el.internalScheme());
				}
				if (ds.size() > 0)
				{
					for(Iterator it = ds.iterator(); it.hasNext();)
					{
						Scheme sch = (Scheme)it.next();
						vec.add(new ObjectResourceTreeNode(sch, sch.name(), true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
					}
				}
			}
			else if (s.equals("SchemeElement"))
			{
				Object parent = ((ObjectResourceTreeNode)node.getParent()).getObject();
				List ds = new LinkedList();
				if (parent instanceof Scheme)
				{
					Scheme scheme = (Scheme)parent;
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = scheme.schemeElements()[i];
						if (element.internalScheme() == null)
							ds.add(element);
					}
				}
				else if (parent instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement)parent;
					ds.addAll(Arrays.asList(el.schemeElements()));
				}
				if (ds.size() > 0)
				{
					for(Iterator it = ds.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						if (element.schemeLinks().length != 0 || element.schemeElements().length != 0)
							vec.add(new ObjectResourceTreeNode(element, element.name(), true, false));
						else
							vec.add(new ObjectResourceTreeNode(element, element.name(), true, true));
					}
				}
			}
			else if (s.equals("SchemeLink"))
			{
				Object parent = ((ObjectResourceTreeNode)node.getParent()).getObject();
				if (parent instanceof Scheme)
				{
					Scheme scheme = (Scheme)parent;
					for(int i = 0; i < scheme.schemeLinks().length; i++)
					{
						SchemeLink link = scheme.schemeLinks()[i];
						vec.add(new ObjectResourceTreeNode(link, link.name(), true, true));
					}
				}
				else if (parent instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement)parent;
					for(int i = 0; i < el.schemeLinks().length; i++)
					{
						SchemeLink link = el.schemeLinks()[i];
						vec.add(new ObjectResourceTreeNode(link, link.name(), true, true));
					}
				}
			}
			else if (s.equals("SchemeCableLink"))
			{
				Scheme parent = (Scheme)((ObjectResourceTreeNode)node.getParent()).getObject();
				for(int i = 0; i < parent.schemeCableLinks().length; i++)
				{
					SchemeCableLink link = parent.schemeCableLinks()[i];
					vec.add(new ObjectResourceTreeNode(link, link.name(), true, true));
				}
			}
			else if (s.equals("SchemePath"))
			{
				Scheme parent = (Scheme)((ObjectResourceTreeNode)node.getParent()).getObject();
				for(int i = 0; i < parent.schemeMonitoringSolution().schemePaths().length; i++)
				{
					SchemePath path = parent.schemeMonitoringSolution().schemePaths()[i];
					vec.add(new ObjectResourceTreeNode(path, path.name(), true, true));
				}
			}
		}
		else
		{
			if(node.getObject() instanceof Type)
			{
				Type type = (Type)node.getObject();
				StringFieldCondition condition = new StringFieldCondition(
						String.valueOf(type.value()),
						ObjectEntities.SCHEME_ENTITY_CODE,
						StringFieldSort.STRINGSORT_INTEGER);
				try {
					List schemes = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext(); ) {
						Scheme sc = (Scheme)it.next();
						vec.add(new ObjectResourceTreeNode(sc, sc.name(), true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif"))));
					}
				}
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}


			if(node.getObject() instanceof SchemeProtoGroup)
			{
				SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
				for (int i = 0; i < parent_group.schemeProtoGroups().length; i++)
				{
					SchemeProtoGroup map_group = parent_group.schemeProtoGroups()[i];
					ImageIcon icon;
					if (map_group.symbol() == null)
						icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
					else
						icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(map_group.symbolImpl().getImage())
																 .getScaledInstance(16, 16, Image.SCALE_SMOOTH));

					vec.add(new ObjectResourceTreeNode(map_group, map_group.name(), true, icon,
							map_group.schemeProtoGroups().length == 0 && map_group.schemeProtoElements().length == 0));
				}
				if (vec.isEmpty())
				{
					for (int i = 0; i < parent_group.schemeProtoElements().length; i++)
					{
						SchemeProtoElement proto = parent_group.schemeProtoElements()[i];
//						proto.scheme_proto_group = parent_group;
						vec.add(new ObjectResourceTreeNode(proto, proto.name().length() == 0 ? "Без названия" : proto.name(), true, true));
					}
				}
			}
			else if(node.getObject() instanceof Scheme)
			{
				Scheme s = (Scheme)node.getObject();
				if (s.schemeElements().length != 0)
				{
					boolean has_schemes = false;
					boolean has_elements = false;
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = (SchemeElement)s.schemeElements()[i];
						if (el.internalScheme() == null)
						{
							has_elements = true;
							break;
						}
					}
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = (SchemeElement)s.schemeElements()[i];
						if (el.internalScheme() != null)
						{
							has_schemes = true;
							break;
						}
					}
					if (has_schemes)
						vec.add(new ObjectResourceTreeNode("Scheme", "Вложенные схемы", true));
					if (has_elements)
						vec.add(new ObjectResourceTreeNode("SchemeElement", "Узлы", true));
				}
				if (s.schemeLinks().length != 0)
					vec.add(new ObjectResourceTreeNode("SchemeLink", "Линии", true));
				if (s.schemeCableLinks().length != 0)
					vec.add(new ObjectResourceTreeNode("SchemeCableLink", "Кабели", true));
				if (s.schemeMonitoringSolution().schemePaths().length != 0)
					vec.add(new ObjectResourceTreeNode("SchemePath", "Пути", true));
			}
			else if(node.getObject() instanceof SchemeElement)
			{
				SchemeElement schel = (SchemeElement)node.getObject();
				if (schel.internalScheme() != null)
				{
					Scheme scheme = schel.internalScheme();
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = scheme.schemeElements()[i];
						if (element.internalScheme() == null)
						{
							if (element.schemeLinks().length != 0 || element.schemeElements().length != 0)
								vec.add(new ObjectResourceTreeNode(element, element.name(), true, false));
							else
								vec.add(new ObjectResourceTreeNode(element, element.name(), true, true));
						}
						else
							vec.add(new ObjectResourceTreeNode(element, element.internalScheme().name(), true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
					}
				}
				else
				{
					if (schel.schemeElements().length != 0)
						vec.add(new ObjectResourceTreeNode("SchemeElement", "Вложенные элементы", true));
				 if (schel.schemeLinks().length != 0)
						vec.add(new ObjectResourceTreeNode("SchemeLink", "Линии", true));
				}
			}
		}
		return vec;
	}
}
