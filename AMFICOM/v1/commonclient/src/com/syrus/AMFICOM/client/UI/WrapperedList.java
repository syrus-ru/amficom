/*
 * $Id: WrapperedList.java,v 1.3 2005/06/15 12:04:44 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.UI;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;

import com.syrus.util.Wrapper;

/**
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/06/15 12:04:44 $
 * @module generalclient_v1
 */
public class WrapperedList extends JList {

	private static final long	serialVersionUID	= -1575309361246285747L;

	private WrapperedListModel	model;

	public WrapperedList(WrapperedListModel model) {
		LabelCheckBoxRenderer renderer = new LabelCheckBoxRenderer(model.wrapper, model.key);
		this.setCellRenderer(renderer);
		this.model = model;
		this.setModel(model);		
	}

	public WrapperedList(Wrapper controller, List objects, String key, String compareKey) {
		this(new WrapperedListModel(controller, objects, key, compareKey));
	}

	public WrapperedList(Wrapper controller, String key, String compareKey) {
		this(new WrapperedListModel(controller, new LinkedList(), key, compareKey));
	}	

	public void removeAll() {
		super.removeAll();
		this.model.removeAllElements();
	}

	public void addElements(Collection objects) {
		this.model.addElements(objects);
	}	
	


	public void setSelectedValue(	Object anObject,
									boolean shouldScroll) {

		Object anObjectValue = this.model.wrapper.getValue(anObject, this.model.compareKey);
//		Log.debugMessage("WrapperedList.setSelectedValue | anObject " + anObject, Log.FINEST);
//		Log.debugMessage("WrapperedList.setSelectedValue | this.model.compareKey: " + this.model.compareKey, Log.FINEST);
//		Log.debugMessage("WrapperedList.setSelectedValue | anObjectValue " + anObjectValue, Log.FINEST);
		if (anObjectValue == null) {
			setSelectedIndex(-1);
		} else {
			Object elementValue = this.model.wrapper.getValue(getSelectedValue(), this.model.compareKey);
//			Log.debugMessage("WrapperedList.setSelectedValue | elementValue " + elementValue, Log.FINEST);
			if (!anObjectValue.equals(elementValue)) {
				ListModel dm = this.getModel();
				int count = dm.getSize();
//				Log.debugMessage("WrapperedList.setSelectedValue | count " + count, Log.FINEST);
				for (int i = 0; i < count; i++) {
					elementValue = this.model.wrapper.getValue(dm.getElementAt(i), this.model.compareKey);
//					Log.debugMessage("WrapperedList.setSelectedValue | anObjectValue " + anObjectValue, Log.FINEST);
//					Log.debugMessage("WrapperedList.setSelectedValue | elementValue " + elementValue, Log.FINEST);
					if (anObjectValue.equals(elementValue)) {
						setSelectedIndex(i);
						if (shouldScroll) {
							ensureIndexIsVisible(i);
						}
						this.repaint();						
						return;
					}
				}
				this.setSelectedIndex(-1);
			}
			this.repaint();
		}
	}
}
