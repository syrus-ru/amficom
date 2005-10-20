/**
 * $Id: MapRemoveWrapper.java,v 1.3 2005/10/20 09:16:55 krupenn Exp $
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map;

import java.util.Set;

import com.syrus.AMFICOM.client.UI.dialogs.DefaultStorableObjectRemoveWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;

public class MapRemoveWrapper extends DefaultStorableObjectRemoveWrapper {

	static MapRemoveWrapper instance;
	
	protected MapRemoveWrapper() {
		super();
	}
	
	public static MapRemoveWrapper getInstance() {
		if(instance == null) {
			instance = new MapRemoveWrapper();
		}
		return instance;
	}
	
	@Override
	public boolean remove(Object object) {
//		try {
/* TODO uncomment when conditions are working at CB server!!!
			Map map = (Map) object;
			LinkedIdsCondition condition = new LinkedIdsCondition(map.getId(), ObjectEntities.MAP_CODE);
			Set<StorableObject> maps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!maps.isEmpty()) {
//				this.aContext.getDispatcher().firePropertyChange(
//						new StatusMessageEvent(
//								this,
//								StatusMessageEvent.STATUS_MESSAGE,
//								I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE)));
				return false;
			}
			condition = new LinkedIdsCondition(map.getId(), ObjectEntities.MAPVIEW_CODE);
			Set<StorableObject> mapViews = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!mapViews.isEmpty()) {
				return false;
			}
			condition = new LinkedIdsCondition(map.getId(), ObjectEntities.SCHEME_CODE);
			Set<StorableObject> schemes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!schemes.isEmpty()) {
				return false;
			}
			condition = new LinkedIdsCondition(Identifier.createIdentifiers(map.getSiteNodes()), ObjectEntities.MAP_CODE);
			maps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!maps.isEmpty()) {
				return false;
			}
*/
			return super.remove(object);
//		} catch(ApplicationException e) {
//			e.printStackTrace();
//		}
//		return false;
	}

}
