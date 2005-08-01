/*
 * $Id: SchemeTreeModel.java,v 1.28 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

/**
 * @author $Author: stas $
 * @version $Revision: 1.28 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.configuration.ui.ConfigurationTreeModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;

public class SchemeTreeModel implements ChildrenFactory, VisualManagerFactory {
	ApplicationContext aContext;
	
	PopulatableIconedNode root;

	private static IdlKind[] schemeTypes = new IdlKind[] { IdlKind.NETWORK, IdlKind.BUILDING,
			IdlKind.CABLE_SUBNETWORK };

	private static String[] schemeTypeNames = new String[] {
			LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_NETWORK),
			LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_BUILDING),
			LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_CABLE) };

	public SchemeTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public VisualManager getVisualManager(Item node) {
		Object object = node.getObject();
		if (object instanceof String) {
			String s = (String)object;
			/*if (s.equals(Constants.SCHEME))
				return SchemeController.getInstance();
			if (s.equals(Constants.SCHEME_ELEMENT))
				return SchemeElementController.getInstance();
			if (s.equals(Constants.SCHEME_LINK))
				return SchemeLinkController.getInstance();
			if (s.equals(Constants.SCHEME_CABLELINK))
				return SchemeCableLinkController.getInstance();
			if (s.equals(Constants.SCHEME_PATH))
				return SchemePathController.getInstance();
			if (s.equals(Constants.SCHEME_PROTO_GROUP))
				return null;*/
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
			if (s.equals(SchemeResourceKeys.SCHEME_TYPE))
				return null;
			// for any other strings return null Manager
			return null;
		}
		if (object instanceof IdlKind)
				return null;
//		if (object instanceof Kind)
//			return SchemeController.getInstance();
//		if (object instanceof SchemeProtoGroup) {
//			if (!((SchemeProtoGroup) object).getSchemeProtoGroups().isEmpty())
//				return null;
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
//			else
//				return null;
			/**
			 * @todo write SchemeProtoElementController return
			 *       SchemeProtoElementController.getInstance();
			 */
//		}
//		if (object instanceof Scheme)
//			return SchemeController.getInstance();
//		if (object instanceof SchemeElement)
//			return SchemeElementController.getInstance();
		throw new UnsupportedOperationException("Unknown object " + object); //$NON-NLS-1$
	}
	
	public void populate(Item node) {
		Collection contents = CommonUIUtilities.getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(SchemeResourceKeys.ROOT)) {
				createRootItems(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.SCHEME_TYPE)) {
				for (int i = 0; i < schemeTypes.length; i++) {
					if (!contents.contains(schemeTypes[i]))
						node.addChild(new PopulatableIconedNode(this, schemeTypes[i], schemeTypeNames[i], UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
				}
			}
			/*
			} 
			else if (s.equals(Constants.SCHEME)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				List ds = new LinkedList();
				for (int i = 0; i < parent.getSchemeElementsAsArray().length; i++) {
					SchemeElement el = parent.getSchemeElementsAsArray()[i];
					if (el.getScheme() != null)
						ds.add(el.getScheme());
				}
				if (ds.size() > 0) {
					for (Iterator it = ds.iterator(); it.hasNext();) {
						Scheme sch = (Scheme) it.next();
						if (!contents.contains(sch))
							node.addChild(new PopulatableIconedNode(this, sch));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_ELEMENT)) {
				Object parent = ((Item) node.getParent()).getObject();
				List ds = new LinkedList();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getScheme() == null)
							ds.add(element);
					}
				} 
				else if (parent instanceof SchemeElement) {
					SchemeElement el = (SchemeElement) parent;
					ds.addAll(Arrays.asList(el.getSchemeElementsAsArray()));
				}
				if (ds.size() > 0) {
					for (Iterator it = ds.iterator(); it.hasNext();) {
						SchemeElement element = (SchemeElement) it.next();
						if (element.getSchemeLinksAsArray().length != 0	|| element.getSchemeElementsAsArray().length != 0) {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), true));
						} else {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), false));
						}
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_LINK)) {
				Object parent = ((Item) node.getParent()).getObject();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = scheme.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
					}
				} 
				else if (parent instanceof SchemeElement) {
					SchemeElement el = (SchemeElement) parent;
					for (int i = 0; i < el.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = el.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_CABLELINK)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				for (int i = 0; i < parent.getSchemeCableLinksAsArray().length; i++) {
					SchemeCableLink link = parent.getSchemeCableLinksAsArray()[i];
					if (!contents.contains(link))
						node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
				}
			} 
			else if (s.equals(Constants.SCHEME_PATH)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				for (int i = 0; i < parent.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray().length; i++) {
					SchemePath path = parent.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray()[i];
					if (!contents.contains(path))
						node.addChild(new PopulatableIconedNode(this, path, path.getName(), false));
				}
			}*/
		} 
		else {
			if (node.getObject() instanceof IdlKind) {
				IdlKind type = (IdlKind) node.getObject();
				TypicalCondition condition = new TypicalCondition(String.valueOf(type.value()), 
						OperationSort.OPERATION_EQUALS, ObjectEntities.SCHEME_CODE,
						StorableObjectWrapper.COLUMN_TYPE_ID);
				try {
					Set schemes = StorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext();) {
						Scheme sc = (Scheme) it.next();
						if (!contents.contains(sc))
							node.addChild(new PopulatableIconedNode(this, sc));
					}
				} 
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}/*
			else if (node.getObject() instanceof Scheme) {
				Scheme s = (Scheme) node.getObject();
				if (s.getSchemeElementsAsArray().length != 0) {
					boolean has_schemes = false;
					boolean has_elements = false;
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getScheme() == null) {
							has_elements = true;
							break;
						}
					}
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getScheme() != null) {
							has_schemes = true;
							break;
						}
					}
					if (has_schemes) {
						if (!contents.contains(Constants.SCHEME))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME));
					}
					if (has_elements) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_ELEMENT));
					}
				}
				if (s.getSchemeLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_LINK))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_LINK));
				}
				if (s.getSchemeCableLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_CABLELINK))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_CABLELINK));
				}
				if (s.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_PATH))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_PATH));
				}
			} 
			else if (node.getObject() instanceof SchemeElement) {
				SchemeElement schel = (SchemeElement) node.getObject();
				if (schel.getScheme() != null) {
					Scheme scheme = schel.getScheme();
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getScheme() == null) {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), (element.getSchemeLinksAsArray().length != 0 || element.getSchemeElementsAsArray().length != 0)));
						} 
						else {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element));
						}
					}
				} 
				else {
					if (schel.getSchemeElementsAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_ELEMENT));
					}
					if (schel.getSchemeLinksAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_LINK))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_LINK));
					}
				}
			}*/
		}
	}
	
	public static final Object getRootObject() {
		return SchemeResourceKeys.ROOT;
	}
	
	public Item getRoot() {
		if (root == null) {
			root = new PopulatableIconedNode(this, SchemeResourceKeys.ROOT, LangModelScheme.getString(SchemeResourceKeys.ROOT),
					UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
		}
		return root;
	}
	
	private void createRootItems(Item node, Collection contents) {
		if (!contents.contains(ConfigurationTreeModel.getRootObject())) {
			ConfigurationTreeModel configurationTreeModel = new ConfigurationTreeModel(aContext);
			node.addChild(configurationTreeModel.getRoot());
		}
		if (!contents.contains(SchemeResourceKeys.SCHEME_TYPE))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.SCHEME_TYPE, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(ProtoGroupTreeModel.getRootObject())) {
			ProtoGroupTreeModel 	protoTreeModel = new ProtoGroupTreeModel(aContext);
			node.addChild(protoTreeModel.getRoot());
		}
	}
}
