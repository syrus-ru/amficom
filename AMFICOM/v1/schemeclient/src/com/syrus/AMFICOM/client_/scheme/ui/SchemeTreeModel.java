/*
 * $Id: SchemeTreeModel.java,v 1.51 2006/02/08 14:09:48 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

/**
 * @author $Author: stas $
 * @version $Revision: 1.51 $, $Date: 2006/02/08 14:09:48 $
 * @module schemeclient
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.filterclient.SchemeCableLinkConditionWrapper;
import com.syrus.AMFICOM.filterclient.SchemeConditionWrapper;
import com.syrus.AMFICOM.filterclient.SchemeElementConditionWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

public class SchemeTreeModel extends AbstractChildrenFactory implements VisualManagerFactory {
	ApplicationContext aContext;
	
	PopulatableIconedNode root;
	
	private boolean forceSorting = false;
	
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
			if (s.equals(SchemeResourceKeys.SCHEME_TYPE))
				return SchemePropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.SCHEME))
				return SchemePropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.SCHEME_ELEMENT))
				return SchemeElementPropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.SCHEME_LINK))
				return SchemeLinkPropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.SCHEME_CABLELINK))
				return SchemeCableLinkPropertiesManager.getInstance(this.aContext);
//			if (s.equals(SchemeResourceKeys.SCHEME_PATH))
//				return SchemePathPController.getInstance();
			// for any other strings return null Manager
			return null;
		}
		if (object instanceof IdlKind)
				return SchemePropertiesManager.getInstance(this.aContext);
		if (object instanceof Scheme)
			return SchemePropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemeElement)
			return SchemeElementPropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemeLink)
			return SchemeLinkPropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemeCableLink)
			return SchemeCableLinkPropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemePath)
			return SchemePathPropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemePort)
			return SchemePortPropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemeCablePort)
			return SchemeCablePortPropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemeMonitoringSolution)
			return null;
		throw new UnsupportedOperationException("Unknown object " + object); //$NON-NLS-1$
	}
	
	public void populate(Item node) {
		Collection<Object> contents = super.getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(SchemeResourceKeys.SCHEME_TYPE)) {
				createSchemeKinds(node, contents);
			}
			else if (s.equals(SchemeResourceKeys.SCHEME)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
					Set<StorableObject> schemeElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection<StorableObject> toAdd = super.getObjectsToAdd(schemeElements, contents);
					Collection<Item> toRemove = super.getItemsToRemove(schemeElements, node.getChildren());
									
					for (Item child : toRemove) {
						child.setParent(null);
					}	
					if (this.forceSorting) {
						List<StorableObject> list = new ArrayList<StorableObject>(toAdd); 
						Collections.sort(list, StorableObjectComparator.getInstance());
						toAdd = list;
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						SchemeElement sc = (SchemeElement) it.next();
						if (sc.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
							Scheme scheme = sc.getScheme(false);
							node.addChild(new PopulatableIconedNode(this, scheme, UIManager.getIcon(SchemeResourceKeys.ICON_SCHEME)));
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} 
			else if (s.equals(SchemeResourceKeys.SCHEME_ELEMENT)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
					Set<StorableObject> schemeElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection<StorableObject> toAdd = super.getObjectsToAdd(schemeElements, contents);
					Collection<Item> toRemove = super.getItemsToRemove(schemeElements, node.getChildren());
									
					for (Item child : toRemove) {
						child.setParent(null);
					}
					if (this.forceSorting) {
						List<StorableObject> list = new ArrayList<StorableObject>(toAdd); 
						Collections.sort(list, StorableObjectComparator.getInstance());
						toAdd = list;
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						SchemeElement sc = (SchemeElement) it.next();
						if (sc.getKind() == IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER) {
							node.addChild(new PopulatableIconedNode(this, sc));
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} 
			else if (s.equals(SchemeResourceKeys.SCHEME_LINK) || s.equals(SchemeResourceKeys.SCHEME_CABLELINK)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
					Set<StorableObject> schemeLinks = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection<StorableObject> toAdd = super.getObjectsToAdd(schemeLinks, contents);
					Collection<Item> toRemove = super.getItemsToRemove(schemeLinks, node.getChildren());
									
					for (Item child : toRemove) {
						child.setParent(null);
					}
					if (this.forceSorting) {
						List<StorableObject> list = new ArrayList<StorableObject>(toAdd); 
						Collections.sort(list, StorableObjectComparator.getInstance());
						toAdd = list;
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						Namable so = (Namable) it.next();
						node.addChild(new PopulatableIconedNode(this, so, false));
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
			else if (s.equals(SchemeResourceKeys.SCHEME_MONITORING_SOLUTION)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
					Set<StorableObject> schemeMonitoringSolutions = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection<StorableObject> toAdd = super.getObjectsToAdd(schemeMonitoringSolutions, contents);
					Collection<Item> toRemove = super.getItemsToRemove(schemeMonitoringSolutions, node.getChildren());
									
					for (Item child : toRemove) {
						child.setParent(null);
					}
					if (this.forceSorting) {
						List<StorableObject> list = new ArrayList<StorableObject>(toAdd); 
						Collections.sort(list, StorableObjectComparator.getInstance());
						toAdd = list;
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						SchemeMonitoringSolution sms = (SchemeMonitoringSolution) it.next();
						LinkedIdsCondition condition1 = new LinkedIdsCondition(sms.getId(), ObjectEntities.SCHEMEPATH_CODE);
						FiltrableIconedNode child = new FiltrableIconedNode();
						child.setChildrenFactory(this);
						child.setObject(sms);
						child.setDefaultCondition(condition1);
						node.addChild(child);
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		} 
		else {
			if (node.getObject() instanceof IdlKind) {
				createSchemes(node, contents);
			}
			else if (node.getObject() instanceof Scheme) {
				Scheme s = (Scheme) node.getObject();
				try {
					Set<SchemeElement> innerSEs = s.getSchemeElements(false); 
					if (innerSEs.size() != 0) {
						boolean has_schemes = false;
						boolean has_elements = false;
						for (SchemeElement el : innerSEs) {
							if (el.getKind() == IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER) {
								has_elements = true;
								break;
							}
						}
						for (SchemeElement el : innerSEs) {
							if (el.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
								has_schemes = true;
								break;
							}
						}

						if (!contents.contains(SchemeResourceKeys.SCHEME) && has_schemes) {
							LinkedIdsCondition condition1 = new LinkedIdsCondition(s.getId(), ObjectEntities.SCHEMEELEMENT_CODE);
							// FIXME add TypicalCondition
//							TypicalCondition condition2 = new TypicalCondition(IdlSchemeElementKind._SCHEME_CONTAINER, 
//									OperationSort.OPERATION_EQUALS, ObjectEntities.SCHEMEELEMENT_CODE, SchemeElementWrapper.COLUMN_KIND);
//							CompoundCondition condition = new CompoundCondition(condition1, CompoundConditionSort.AND, condition2);
							
							FiltrableIconedNode child = new FiltrableIconedNode();
							child.setChildrenFactory(this);
							child.setObject(SchemeResourceKeys.SCHEME);
							child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME));
							child.setDefaultCondition(condition1);
							child.setFilter(new Filter(new SchemeElementConditionWrapper()));
							node.addChild(child);
						}
						if (!contents.contains(SchemeResourceKeys.SCHEME_ELEMENT) && has_elements) {
							LinkedIdsCondition condition1 = new LinkedIdsCondition(s.getId(), ObjectEntities.SCHEMEELEMENT_CODE);
							// FIXME add TypicalCondition
//							TypicalCondition condition2 = new TypicalCondition(IdlSchemeElementKind._SCHEME_ELEMENT_CONTAINER, 
//									OperationSort.OPERATION_EQUALS, ObjectEntities.SCHEMEELEMENT_CODE, SchemeElementWrapper.COLUMN_KIND);
//							CompoundCondition condition = new CompoundCondition(condition1, CompoundConditionSort.AND, condition2);

							FiltrableIconedNode child = new FiltrableIconedNode();
							child.setChildrenFactory(this);
							child.setObject(SchemeResourceKeys.SCHEME_ELEMENT);
							child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_ELEMENT));
							child.setDefaultCondition(condition1);
							child.setFilter(new Filter(new SchemeElementConditionWrapper()));
							node.addChild(child);
						}
						if (!contents.contains(SchemeResourceKeys.SCHEME_LINK) && !s.getSchemeLinks(false).isEmpty()) {
							LinkedIdsCondition condition = new LinkedIdsCondition(s.getId(), ObjectEntities.SCHEMELINK_CODE);
							FiltrableIconedNode child = new FiltrableIconedNode();
							child.setChildrenFactory(this);
							child.setObject(SchemeResourceKeys.SCHEME_LINK);
							child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_LINK));
							child.setDefaultCondition(condition);
							node.addChild(child);
						}
						if (!contents.contains(SchemeResourceKeys.SCHEME_CABLELINK) && !s.getSchemeCableLinks(false).isEmpty()) {
							LinkedIdsCondition condition = new LinkedIdsCondition(s.getId(), ObjectEntities.SCHEMECABLELINK_CODE);
							FiltrableIconedNode child = new FiltrableIconedNode();
							child.setChildrenFactory(this);
							child.setObject(SchemeResourceKeys.SCHEME_CABLELINK);
							child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_CABLELINK));
							child.setDefaultCondition(condition);
							child.setFilter(new Filter(new SchemeCableLinkConditionWrapper()));
							node.addChild(child);
						}
						if (!contents.contains(SchemeResourceKeys.SCHEME_MONITORING_SOLUTION) && !s.getSchemeMonitoringSolutions(false).isEmpty()) {
							LinkedIdsCondition condition = new LinkedIdsCondition(s.getId(), ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE);
							FiltrableIconedNode child = new FiltrableIconedNode();
							child.setChildrenFactory(this);
							child.setObject(SchemeResourceKeys.SCHEME_MONITORING_SOLUTION);
							child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_MONITORING_SOLUTION));
							child.setDefaultCondition(condition);
							node.addChild(child);
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
			else if (node.getObject() instanceof SchemeElement) {
				SchemeElement se = (SchemeElement) node.getObject();
				
				try {
					if (!contents.contains(SchemeResourceKeys.SCHEME_ELEMENT) && !se.getSchemeElements(false).isEmpty()) {
						LinkedIdsCondition condition1 = new LinkedIdsCondition(se.getId(), ObjectEntities.SCHEMEELEMENT_CODE);
						FiltrableIconedNode child = new FiltrableIconedNode();
						child.setChildrenFactory(this);
						child.setObject(SchemeResourceKeys.SCHEME_ELEMENT);
						child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_ELEMENT));
						child.setDefaultCondition(condition1);
						node.addChild(child);
					}
					if (!contents.contains(SchemeResourceKeys.SCHEME_LINK) && !se.getSchemeLinks(false).isEmpty()) {
						LinkedIdsCondition condition1 = new LinkedIdsCondition(se.getId(), ObjectEntities.SCHEMELINK_CODE);
						FiltrableIconedNode child = new FiltrableIconedNode();
						child.setChildrenFactory(this);
						child.setObject(SchemeResourceKeys.SCHEME_LINK);
						child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_LINK));
						child.setDefaultCondition(condition1);
						node.addChild(child);
					}
					if (!contents.contains(SchemeResourceKeys.SCHEME_CABLE_PORT)) {
						Collection<SchemeCablePort> cablePorts = Collections.emptySet();
						try {
							cablePorts = se.getSchemeCablePortsRecursively(false);
						} catch (ApplicationException e) {
							Log.errorMessage(e);
						}
						if (this.forceSorting) {
							List<SchemeCablePort> list = new ArrayList<SchemeCablePort>(cablePorts); 
							Collections.sort(list, StorableObjectComparator.getInstance());
							cablePorts = list;
						}
						if (!cablePorts.isEmpty()) {
							PopulatableIconedNode child = new PopulatableIconedNode(this, SchemeResourceKeys.SCHEME_CABLE_PORT, LangModelScheme.getString(SchemeResourceKeys.SCHEME_CABLE_PORT));
							node.addChild(child);
							for (SchemeCablePort cablePort : cablePorts) {
								child.addChild(new PopulatableIconedNode(this, cablePort, false));
							}
						}
					}
					if (!contents.contains(SchemeResourceKeys.SCHEME_PORT)) {
						Collection<SchemePort> ports = Collections.emptySet();
						try {
							ports = se.getSchemePortsRecursively(false);
						} catch (ApplicationException e) {
							Log.errorMessage(e);
						}
						if (this.forceSorting) {
							List<SchemePort> list = new ArrayList<SchemePort>(ports); 
							Collections.sort(list, StorableObjectComparator.getInstance());
							ports = list;
						}
						if (!ports.isEmpty()) {
							PopulatableIconedNode child = new PopulatableIconedNode(this, SchemeResourceKeys.SCHEME_PORT, LangModelScheme.getString(SchemeResourceKeys.SCHEME_PORT));
							node.addChild(child);
							for (SchemePort port : ports) {
								child.addChild(new PopulatableIconedNode(this, port, false));
							}
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} 
			else if (node.getObject() instanceof SchemeMonitoringSolution) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
					Set<StorableObject> schemePaths = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection<StorableObject> toAdd = super.getObjectsToAdd(schemePaths, contents);
					Collection<Item> toRemove = super.getItemsToRemove(schemePaths, node.getChildren());
									
					for (Item child : toRemove) {
						child.setParent(null);
					}
					if (this.forceSorting) {
						List<StorableObject> list = new ArrayList<StorableObject>(toAdd); 
						Collections.sort(list, StorableObjectComparator.getInstance());
						toAdd = list;
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						SchemePath sp = (SchemePath) it.next();
						node.addChild(new PopulatableIconedNode(this, sp, false));
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
	}
	
	public static final Object getRootObject() {
		return SchemeResourceKeys.SCHEME_TYPE;
	}
	
	
	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(this, SchemeResourceKeys.SCHEME_TYPE, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
		}
		return this.root;
	}
	
	private void createSchemeKinds(Item node, Collection contents) {
		for (int i = 0; i < schemeTypes.length; i++) {
			if (!contents.contains(schemeTypes[i])) {
				TypicalCondition condition1 = new TypicalCondition(String.valueOf(schemeTypes[i].value()), 
						OperationSort.OPERATION_EQUALS, ObjectEntities.SCHEME_CODE,
						SchemeWrapper.COLUMN_KIND);

				FiltrableIconedNode child = new FiltrableIconedNode();
				child.setChildrenFactory(this);
				child.setObject(schemeTypes[i]);
				child.setName(schemeTypeNames[i]);
				child.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
				child.setDefaultCondition(condition1);
				child.setFilter(new Filter(new SchemeConditionWrapper()));
				node.addChild(child);
			}
		}
	}
	
	private void createSchemes(Item node, Collection<Object> contents) {
		try {
			StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition(); 
			Set<StorableObject> schemes = new HashSet<StorableObject>(StorableObjectPool.getStorableObjectsByCondition(condition, true));

			for (Iterator it = schemes.iterator(); it.hasNext();) {
				Scheme sc = (Scheme) it.next();
				if (sc.getParentSchemeElement() != null) {
					it.remove();
				}
			}
			
			Collection<StorableObject> toAdd = super.getObjectsToAdd(schemes, contents);
			Collection<Item> toRemove = super.getItemsToRemove(schemes, node.getChildren());

			for (Item child : toRemove) {
				child.setParent(null);
			}
			if (this.forceSorting) {
				List<StorableObject> list = new ArrayList<StorableObject>(toAdd); 
				Collections.sort(list, StorableObjectComparator.getInstance());
				toAdd = list;
			}
			for (Iterator it = toAdd.iterator(); it.hasNext();) {
				Scheme sc = (Scheme) it.next();
				node.addChild(new PopulatableIconedNode(this, sc, UIManager.getIcon(SchemeResourceKeys.ICON_SCHEME)));
			}
		} 
		catch (ApplicationException ex1) {
			ex1.printStackTrace();
		}
	}
		
	public void setForceSorting(boolean forceSorting) {
		this.forceSorting = forceSorting;
	}
}

class StorableObjectComparator implements Comparator<StorableObject> {
	private static StorableObjectComparator instance;
	
	static StorableObjectComparator getInstance() {
		if (instance == null) {
			instance = new StorableObjectComparator();
		}
		return instance;
	}
	
	public int compare(final StorableObject object1, 
			final StorableObject object2) {
		final String string1 = object1 instanceof Namable ? ((Namable)object1).getName() : object1.getId().getIdentifierString();
		final String string2 = object2 instanceof Namable ? ((Namable)object2).getName() : object2.getId().getIdentifierString();
		return string1.compareTo(string2);
	}
}

