/*-
 * $$Id: CableBindingController.java,v 1.31 2005/10/30 15:20:32 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.31 $, $Date: 2005/10/30 15:20:32 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class CableBindingController implements Wrapper {
	public static final String KEY_START_NODE = "startnode"; //$NON-NLS-1$
	public static final String KEY_START_SPARE = "startspare"; //$NON-NLS-1$
	public static final String KEY_LINK = "link"; //$NON-NLS-1$
	public static final String KEY_END_SPARE = "endspare"; //$NON-NLS-1$
	public static final String KEY_END_NODE = "endnode"; //$NON-NLS-1$

	private static CableBindingController instance;

	private List keys;
	private String[] keysArray;
	
	CablePath cablePath;

	private CableBindingController() {
		// empty private constructor
		this.keysArray = new String[] { 
				KEY_START_NODE, 
				KEY_START_SPARE, 
				KEY_LINK, 
				KEY_END_SPARE, 
				KEY_END_NODE };
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(this.keysArray)));
	}

	public static CableBindingController getInstance() {
		if(instance == null)
			instance = new CableBindingController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String[] getKeysArray() {
		return this.keysArray;
	}

	public String getName(final String key) {
		String name = null;
		if(key.equals(KEY_START_NODE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_NODE);
		}
		else if(key.equals(KEY_START_SPARE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_START_SPARE);
		}
		else if(key.equals(KEY_LINK)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_TUNNEL);
		}
		else if(key.equals(KEY_END_SPARE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_END_SPARE);
		}
		else if(key.equals(KEY_END_NODE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_NODE);
		}
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;
		if (object instanceof PhysicalLink) {
			PhysicalLink link = (PhysicalLink)object;
			try {
				CableChannelingItem cci = this.cablePath.getFirstCCI(link);
				if(key.equals(KEY_START_NODE)) {
					AbstractNode mne = cci.getStartSiteNode();
					result = (mne == null) ? "" : mne.getName(); //$NON-NLS-1$
				} else if(key.equals(KEY_START_SPARE)) {
					result = (link instanceof UnboundLink) ? "" : String.valueOf(cci.getStartSpare()); //$NON-NLS-1$
				} else if(key.equals(KEY_LINK)) {
					result = (link instanceof UnboundLink) ? "" : link.getName(); //$NON-NLS-1$
				} else if(key.equals(KEY_END_SPARE)) {
					result = (link instanceof UnboundLink) ? "" : String.valueOf(cci.getEndSpare()); //$NON-NLS-1$
				} else if(key.equals(KEY_END_NODE)) {
					AbstractNode mne = cci.getEndSiteNode();
					result = (mne == null) ? "" : mne.getName(); //$NON-NLS-1$
				}
			} catch(Exception e) {
				assert Log.debugMessage(e, Level.SEVERE);
			}
		}
		return result;
	}

	public boolean isEditable(final String key) {
		boolean editable = false;
		if(key.equals(KEY_START_SPARE) || key.equals(KEY_END_SPARE)) {
			editable = true;
		}
		return editable;
	}

	public void setValue(Object object, final String key, final Object value) {
		if(object instanceof PhysicalLink) {
			PhysicalLink link = (PhysicalLink)object;
			try {
				CableChannelingItem cci = this.cablePath.getFirstCCI(link);
				if(key.equals(KEY_START_SPARE)) {
					if(cci.getPhysicalLink() != null)
						cci.setStartSpare(Double.parseDouble((String) value));
				} else if(key.equals(KEY_END_SPARE)) {
					if(cci.getPhysicalLink() != null)
						cci.setEndSpare(Double.parseDouble((String) value));
				}
			} catch(Exception e) {
				assert Log.debugMessage(e, Level.SEVERE);
			}
		}
	}

	public String getKey(final int index) {
		return (String )this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = ""; //$NON-NLS-1$
		return result;
	}

	public void setPropertyValue(
			String key,
			Object objectKey,
			Object objectValue) {
		//empty
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		return clazz;
	}

	public void setCablePath(CablePath cablePath) {
		this.cablePath = cablePath;
	}
}
