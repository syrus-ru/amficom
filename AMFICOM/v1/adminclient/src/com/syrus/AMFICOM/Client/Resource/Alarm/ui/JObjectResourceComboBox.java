package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import com.syrus.AMFICOM.corba.portable.reflect.*;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/24 10:53:57 $
 * @author $Author: bass $
 */
public final class JObjectResourceComboBox extends JComboBox {
	public JObjectResourceComboBox() {
		initComponents();
	}

	private void initComponents() {//GEN-BEGIN:initComponents
		
		addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				processItemEvent(e);
			}
		});
		
	}//GEN-END:initComponents

	private void processItemEvent(java.awt.event.ItemEvent e) {//GEN-FIRST:event_processItemEvent
		if ((e.getID() == ItemEvent.ITEM_STATE_CHANGED) && (e.getStateChange() == ItemEvent.SELECTED)) {
			Object selectedItem = getSelectedItem();
			if (selectedItem instanceof AlertingTypeImpl)
				setToolTipText(((AlertingTypeImpl) selectedItem).getToolTipText());
			else if (selectedItem instanceof MessageTypeImpl)
				setToolTipText(((MessageTypeImpl) selectedItem).getToolTipText());
			else if (selectedItem instanceof UserImpl)
				setToolTipText(((UserImpl) selectedItem).getToolTipText());
			else
				setToolTipText(null);
		}
	}//GEN-LAST:event_processItemEvent

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables
}
