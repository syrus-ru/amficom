/*
 * $Id: SchemeTreeModel.java,v 1.3 2005/03/15 17:49:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/15 17:49:10 $
 * @module schemeclient_v1
 */

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schematics.UI.SchemeController;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;

public class SchemeTreeModel implements SOTreeDataModel
{
	static Icon catalogIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));

	ApplicationContext aContext;

	private static Type[] schemeTypes = new Type[] {
		Type.NETWORK,
		Type.BUILDING,
		Type.CABLE_SUBNETWORK
	};

	public SchemeTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Color getNodeColor(SONode node) {
		return Color.BLACK;
	}
	
	public Icon getNodeIcon(SONode node) {
		if(node.getUserObject() instanceof String)
			return catalogIcon;
		return null;
	}
	
	public String getNodeName(SONode node) {
		if(node.getUserObject() instanceof String) {
			String s = (String )node.getUserObject();
			if(s.equals("root"))
				return "Сеть";
			if(s.equals("configure"))
				return LangModelConfig.getString("label_configuration");
			if(s.equals("SchemeProtoGroup"))
				return "Компоненты сети";
			if(s.equals("schemeTypes"))
				return "Схемы";
			if(s.equals("netdirectory"))
				return LangModelConfig.getString("menuNetDirText");
			if(s.equals("jdirectory"))
				return LangModelConfig.getString("menuJDirText");
			if(s.equals("LinkType"))
				return LangModelConfig.getString("menuNetDirLinkText");
			if(s.equals("CableLinkType"))
				return LangModelConfig.getString("menuNetDirCableText");
			if(s.equals("PortType"))
				return LangModelConfig.getString("menuNetDirPortText");
//			if(s.equals("TransmissionPathType"))
//				return LangModelConfig.getString("menuJDirPathText");
			if(s.equals("MeasurementPortType"))
				return LangModelConfig.getString("menuJDirAccessPointText");
			if(s.equals("MeasurementType"))
				return LangModelConfig.getString("MeasurementType");
			if(s.equals(Type.NETWORK))
				return LangModelSchematics.getString("NETWORK");
			if(s.equals(Type.BUILDING))
				return LangModelSchematics.getString("BUILDING");
			if(s.equals(Type.CABLE_SUBNETWORK))
				return LangModelSchematics.getString("CABLE_SUBNETWORK");
		}
		if (node.getUserObject() instanceof LinkType)
			return ((LinkType)node.getUserObject()).getName();
		if (node.getUserObject() instanceof CableLinkType)
			return ((CableLinkType)node.getUserObject()).getName();
		if (node.getUserObject() instanceof PortType)
			return ((PortType)node.getUserObject()).getName();
		if (node.getUserObject() instanceof MeasurementPortType)
			return ((MeasurementPortType)node.getUserObject()).getName();
		if (node.getUserObject() instanceof MeasurementType)
			return ((MeasurementType)node.getUserObject()).getDescription();
		
		if (node.getUserObject() instanceof SchemeProtoGroup)
			return ((SchemeProtoGroup)node.getUserObject()).name();
		if (node.getUserObject() instanceof Scheme)
			return ((Scheme)node.getUserObject()).name();
		if (node.getUserObject() instanceof SchemeElement)
			return ((SchemeElement)node.getUserObject()).name();
		if (node.getUserObject() instanceof SchemeLink)
			return ((SchemeLink)node.getUserObject()).name();
		if (node.getUserObject() instanceof SchemeCableLink)
			return ((SchemeCableLink)node.getUserObject()).name();
		if (node.getUserObject() instanceof SchemePath)
			return ((SchemePath)node.getUserObject()).name();
		if (node.getUserObject() instanceof SchemePort)
			return ((SchemePort)node.getUserObject()).name();
		if (node.getUserObject() instanceof SchemeCablePort)
			return ((SchemeCablePort)node.getUserObject()).name();
		return "";
	}

	public ObjectResourceController getNodeController(SONode node)
	{
		if(node.getUserObject() instanceof String)
		{
			String s = (String )node.getUserObject();
			if(s.equals("Scheme"))
				return SchemeController.getInstance();
			if(s.equals("SchemeElement"))
				return SchemeElementController.getInstance();
			if(s.equals("SchemeLink"))
				return SchemeLinkController.getInstance();
			if(s.equals("SchemeCableLink"))
			  return SchemeCableLinkController.getInstance();
			if(s.equals("SchemePath"))
				return SchemePathController.getInstance();
			if(s.equals("SchemeProtoGroup"))
				return null;
			/**
			 * @todo write SchemeProtoGroupController
			 * return SchemeProtoGroupController.getInstance();
			 */
			if(s.equals("LinkType"))
				return LinkTypeController.getInstance();
			if(s.equals("CableLinkType"))
				return CableLinkTypeController.getInstance();
			if(s.equals("PortType"))
				return PortTypeController.getInstance();
			if(s.equals("TransmissionPathType"))
				return TransmissionPathTypeController.getInstance();
			if(s.equals("MeasurementPortType"))
				return MeasurementPortTypeController.getInstance();
			if(s.equals("MeasurementType"))
				return MeasurementTypeController.getInstance();
			if(s.equals("schemeTypes"))
				return null;
			return null;
		}
		if (node.getUserObject() instanceof Type)
			return SchemeController.getInstance();
		if (node.getUserObject() instanceof LinkType)
			return LinkTypeController.getInstance();
		if (node.getUserObject() instanceof CableLinkType)
			return CableLinkTypeController.getInstance();
		if (node.getUserObject() instanceof PortType)
			return PortTypeController.getInstance();
		if (node.getUserObject() instanceof TransmissionPathType)
			return TransmissionPathTypeController.getInstance();
		if (node.getUserObject() instanceof MeasurementPortType)
			return MeasurementPortTypeController.getInstance();
		if (node.getUserObject() instanceof MeasurementType)
			return MeasurementTypeController.getInstance();

		if (node.getUserObject() instanceof SchemeProtoGroup)
		{
			if (((SchemeProtoGroup)node.getUserObject()).schemeProtoGroups().length != 0)
				return null;
			/**
			 * @todo write SchemeProtoGroupController
			 * return SchemeProtoGroupController.getInstance();
			 */
			else
				return null;
			/**
			 * @todo write SchemeProtoElementController
			 * return SchemeProtoElementController.getInstance();
			 */
		}
		if (node.getUserObject() instanceof Scheme)
			return SchemeController.getInstance();
		if (node.getUserObject() instanceof SchemeElement)
			return SchemeElementController.getInstance();
		return null;
	}

	public void updateChildNodes(SONode node)
	{
		List vec = new LinkedList();
		if(node.getUserObject() instanceof String)
		{
			String s = (String )node.getUserObject();
			if(s.equals("root"))
			{
				vec.add(new SOMutableNode(this, "configure"));
				vec.add(new SOMutableNode(this, "SchemeProtoGroup"));
				vec.add(new SOMutableNode(this, "schemeTypes"));
			}
			else if(s.equals("configure"))
			{
				vec.add(new SOMutableNode(this, "netdirectory"));
				vec.add(new SOMutableNode(this, "jdirectory"));
			}
			else if(s.equals("netdirectory"))
			{
				vec.add(new SOMutableNode(this, "LinkType"));
				vec.add(new SOMutableNode(this, "CableLinkType"));
				vec.add(new SOMutableNode(this, "PortType"));
			}
			else if(s.equals("jdirectory"))
			{
				vec.add(new SOMutableNode(this, "MeasurementType"));
				vec.add(new SOMutableNode(this, "MeasurementPortType"));
//				vec.add(new SOMutableNode(this, "TransmissionPathType", LangModelConfig.getString("menuJDirPathText"), true));
			}
			else if(s.equals("schemeTypes"))
			{
				for (int i = 0; i < schemeTypes.length; i++) {
					vec.add(new SOMutableNode(this, schemeTypes[i]));
				}
			}
			else if (s.equals("LinkType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINKTYPE_ENTITY_CODE);

					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = linkTypes.iterator(); it.hasNext(); ) {
						LinkType type = (LinkType)it.next();
						vec.add(new SOMutableNode(this, type, false));
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("CableLinkType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);

					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = linkTypes.iterator(); it.hasNext(); ) {
						CableLinkType type = (CableLinkType)it.next();
						vec.add(new SOMutableNode(this, type, false));
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("PortType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PORTTYPE_ENTITY_CODE);
					Collection portTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = portTypes.iterator(); it.hasNext(); ) {
						PortType type = (PortType)it.next();
						vec.add(new SOMutableNode(this, type, false));
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("TransmissionPathType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
					Collection pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						TransmissionPathType type = (TransmissionPathType)it.next();
						vec.add(new SOMutableNode(this, type, false));
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("MeasurementPortType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
					Collection pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						MeasurementPortType type = (MeasurementPortType)it.next();
						vec.add(new SOMutableNode(this, type, false));
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if(s.equals("MeasurementType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					Collection measurementTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = measurementTypes.iterator(); it.hasNext(); ) {
						MeasurementType type = (MeasurementType)it.next();
						vec.add(new SOMutableNode(this, type, false));
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
					LinkedIdsCondition condition = new LinkedIdsCondition(domain_id,
							ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
					List groups = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = groups.iterator(); it.hasNext(); ) {
						SchemeProtoGroup group = (SchemeProtoGroup)it.next();
						vec.add(new SOMutableNode(this, group));
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("Scheme"))
			{
				Scheme parent = (Scheme)((SONode)node.getParent()).getUserObject();
				List ds = new LinkedList();
				for (int i = 0; i < parent.schemeElements().length; i++)
				{
					SchemeElement el = parent.schemeElements()[i];
					if (el.internalScheme() != null)
						ds.add(el.internalScheme());
				}
				if (ds.size() > 0)
				{
					for(Iterator it = ds.iterator(); it.hasNext();)
					{
						Scheme sch = (Scheme)it.next();
						vec.add(new SOMutableNode(this, sch));
					}
				}
			}
			else if (s.equals("SchemeElement"))
			{
				Object parent = ((SONode)node.getParent()).getUserObject();
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
							vec.add(new SOMutableNode(this, element, true));
						else
							vec.add(new SOMutableNode(this, element, false));
					}
				}
			}
			else if (s.equals("SchemeLink"))
			{
				Object parent = ((SONode)node.getParent()).getUserObject();
				if (parent instanceof Scheme)
				{
					Scheme scheme = (Scheme)parent;
					for(int i = 0; i < scheme.schemeLinks().length; i++)
					{
						SchemeLink link = scheme.schemeLinks()[i];
						vec.add(new SOMutableNode(this, link, false));
					}
				}
				else if (parent instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement)parent;
					for(int i = 0; i < el.schemeLinks().length; i++)
					{
						SchemeLink link = el.schemeLinks()[i];
						vec.add(new SOMutableNode(this, link, false));
					}
				}
			}
			else if (s.equals("SchemeCableLink"))
			{
				Scheme parent = (Scheme)((SONode)node.getParent()).getUserObject();
				for(int i = 0; i < parent.schemeCableLinks().length; i++)
				{
					SchemeCableLink link = parent.schemeCableLinks()[i];
					vec.add(new SOMutableNode(this, link, false));
				}
			}
			else if (s.equals("SchemePath"))
			{
				Scheme parent = (Scheme)((SONode)node.getParent()).getUserObject();
				for(int i = 0; i < parent.schemeMonitoringSolution().schemePaths().length; i++)
				{
					SchemePath path = parent.schemeMonitoringSolution().schemePaths()[i];
					vec.add(new SOMutableNode(this, path, false));
				}
			}
		}
		else
		{
			if(node.getUserObject() instanceof Type)
			{
				Type type = (Type)node.getUserObject();
				TypicalCondition condition = new TypicalCondition(
						String.valueOf(type.value()),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.SCHEME_ENTITY_CODE,
						com.syrus.AMFICOM.scheme.SchemeController.COLUMN_TYPE);
				try {
					List schemes = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext(); ) {
						Scheme sc = (Scheme)it.next();
						vec.add(new SOMutableNode(this, sc));
					}
				}
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}

			if(node.getUserObject() instanceof SchemeProtoGroup)
			{
				SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getUserObject();
				for (int i = 0; i < parent_group.schemeProtoGroups().length; i++)
				{
					SchemeProtoGroup map_group = parent_group.schemeProtoGroups()[i];
					ImageIcon icon;
					if (map_group.getSymbol() == null)
						icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
					else
						icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(map_group.getSymbol().getImage())
																 .getScaledInstance(16, 16, Image.SCALE_SMOOTH));

					vec.add(new SOMutableNode(this, map_group, map_group.schemeProtoGroups().length != 0 || map_group.schemeProtoElements().length != 0));
				}
				if (vec.isEmpty())
				{
					for (int i = 0; i < parent_group.schemeProtoElements().length; i++)
					{
						SchemeProtoElement proto = parent_group.schemeProtoElements()[i];
//						proto.parent(parent_group);
						vec.add(new SOMutableNode(this, proto, false));
					}
				}
			}
			else if(node.getUserObject() instanceof Scheme)
			{
				Scheme s = (Scheme)node.getUserObject();
				if (s.schemeElements().length != 0)
				{
					boolean has_schemes = false;
					boolean has_elements = false;
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = s.schemeElements()[i];
						if (el.internalScheme() == null)
						{
							has_elements = true;
							break;
						}
					}
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = s.schemeElements()[i];
						if (el.internalScheme() != null)
						{
							has_schemes = true;
							break;
						}
					}
					if (has_schemes)
						vec.add(new SOMutableNode(this, "Scheme"));
					if (has_elements)
						vec.add(new SOMutableNode(this, "SchemeElement"));
				}
				if (s.schemeLinks().length != 0)
					vec.add(new SOMutableNode(this, "SchemeLink"));
				if (s.schemeCableLinks().length != 0)
					vec.add(new SOMutableNode(this, "SchemeCableLink"));
				if (s.schemeMonitoringSolution().schemePaths().length != 0)
					vec.add(new SOMutableNode(this, "SchemePath"));
			}
			else if(node.getUserObject() instanceof SchemeElement)
			{
				SchemeElement schel = (SchemeElement)node.getUserObject();
				if (schel.internalScheme() != null)
				{
					Scheme scheme = schel.internalScheme();
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = scheme.schemeElements()[i];
						if (element.internalScheme() == null)
						{
							if (element.schemeLinks().length != 0 || element.schemeElements().length != 0)
								vec.add(new SOMutableNode(this, element, true));
							else
								vec.add(new SOMutableNode(this, element, false));
						}
						else
							vec.add(new SOMutableNode(this, element));
					}
				}
				else
				{
					if (schel.schemeElements().length != 0)
						vec.add(new SOMutableNode(this, "SchemeElement"));
				 if (schel.schemeLinks().length != 0)
						vec.add(new SOMutableNode(this, "SchemeLink"));
				}
			}
		}
		node.removeAllChildren();
		for (Iterator it = vec.iterator(); it.hasNext();) {
			node.add((SONode)it.next());
		}
	}
}
