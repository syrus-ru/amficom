/*-
 * $Id: ModelChildrenFactory.java,v 1.1 2006/03/22 14:07:51 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.UI;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.filterclient.MonitoredElementConditionWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.MonitoredElementWrapper;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

public class ModelChildrenFactory extends AbstractChildrenFactory {
	private static final String	ROOT	= "Tree.models.root";
	private static final String	MONITOREDELEMENTS	= "monitoredElements";
	
	private PopulatableIconedNode root;
	
	public static String getRootObject() {
		return ROOT;
	}
	
	public PopulatableIconedNode getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(this, ROOT, I18N.getString(ROOT), true);
		}
		return this.root;
	}
	
	public void populate(Item item) {
		Object nodeObject = item.getObject();
		Collection<Item> items = item.getChildren();
		Collection<Object> objects = super.getChildObjects(item);
		
		if (nodeObject instanceof String) {
			String s = (String) nodeObject;
			if (s.equals(ROOT)) {
				if (item.getChildren().isEmpty()) { // add only if no children as they are constant
					FiltrableIconedNode item2 = new FiltrableIconedNode();
					item2.setObject(MONITOREDELEMENTS);
					item2.setName(LangModelAnalyse.getString(MONITOREDELEMENTS));
					item2.setChildrenFactory(this);
					item2.setDefaultCondition(new LinkedIdsCondition(LoginManager.getDomainId(), ObjectEntities.MONITOREDELEMENT_CODE));
					item2.setFilter(new Filter(new MonitoredElementConditionWrapper()));
					item.addChild(item2);
				}
			} else if (s.equals(MONITOREDELEMENTS)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					Set<StorableObject> meSet = StorableObjectPool.getStorableObjectsByCondition(condition, true);
										
					List<Item> toRemove = super.getItemsToRemove(meSet, items);
					List<StorableObject> toAdd = super.getObjectsToAdd(meSet, objects);
					for (Item child : toRemove) {
						child.setParent(null);
					}

					Collections.sort(toAdd, new WrapperComparator(MonitoredElementWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
					
					int i = 0;
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						MonitoredElement me = (MonitoredElement) it.next();
						FiltrableIconedNode item2 = new FiltrableIconedNode();
						item2.setObject(me);
						item2.setName(me.getName());
						item2.setChildrenFactory(this);
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PATHMODE));

						LinkedIdsCondition condition2 = new LinkedIdsCondition(me.getId(), ObjectEntities.MODELING_CODE);
						item2.setDefaultCondition(condition2);
						//XXX add possibility to insert item in arbitrary location
//							item.addChildAt(item2, i);
						item.addChild(item2);
						i++;
					}
				} catch (IllegalObjectEntityException ex) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), ex.getMessage());
					Log.errorMessage(ex);
				} catch (ApplicationException ex) {
					Log.errorMessage(ex);
				}
			}
		} else if (nodeObject instanceof MonitoredElement) {
			try {
				StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
				Set<StorableObject> meSet = StorableObjectPool.getStorableObjectsByCondition(condition, true);
									
				List<Item> toRemove = super.getItemsToRemove(meSet, items);
				List<StorableObject> toAdd = super.getObjectsToAdd(meSet, objects);
				for (Item child : toRemove) {
					child.setParent(null);
				}

				Collections.sort(toAdd, new WrapperComparator(ModelingWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
				
				for (Iterator it = toAdd.iterator(); it.hasNext();) {
					Modeling modeling = (Modeling) it.next();
					IconedNode item2 = new IconedNode();
					item2.setObject(modeling);
					item2.setName(modeling.getName());
					item2.setCanHaveChildren(false);
					item.addChild(item2);
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}
}
