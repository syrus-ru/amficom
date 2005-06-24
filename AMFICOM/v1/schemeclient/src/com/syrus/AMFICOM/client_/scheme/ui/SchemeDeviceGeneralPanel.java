/*-
 * $Id: SchemeDeviceGeneralPanel.java,v 1.3 2005/06/24 14:13:36 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/24 14:13:36 $
 * @module schemeclient_v1
 */

public class SchemeDeviceGeneralPanel extends DefaultStorableObjectEditor {
	protected SchemeDevice schemeDevice;
	ApplicationContext aContext;

	JTable table;
	JComboBox combo;
	
	final String NULL_PORT = "---"; //$NON-NLS-1$
	Object[][] data;
	int size = 0;
	int portDirection;
	
	protected SchemeDeviceGeneralPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		table = new JTable();
		combo = new ColorChooserComboBox();
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	/**
	 * @return JTable
	 * @see StorableObjectEditor#getGUI()
	 */
	public JComponent getGUI() {
	  return table;
	}

	/**
	 * @param or SchemeDevice
	 * @see StorableObjectEditor#setObject(java.lang.Object)
	 */
	public void setObject(Object or) {
		this.schemeDevice = (SchemeDevice)or;
		combo.removeAllItems();
		
		if (schemeDevice != null) {
			List sPorts = new ArrayList(schemeDevice.getSchemePorts());
			WrapperComparator sorter = new WrapperComparator(
					SchemePortWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME, true);
			Collections.sort(sPorts, sorter);
			Set schemeCableLinks = new HashSet();
			combo.addItem(NULL_PORT);
			for (int i = 0; i < sPorts.size(); i++) {
				SchemePort port = (SchemePort) sPorts.get(i);
				combo.addItem(port);
				SchemeCableThread thread = port.getSchemeCableThread();
				if (thread != null)
					schemeCableLinks.add(thread.getParentSchemeCableLink());
			}

			List scThreads = new ArrayList();
			for (Iterator it = schemeCableLinks.iterator(); it.hasNext();) {
				scThreads.add(it.next());
			}
			sorter = new WrapperComparator(SchemeCableThreadWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME, true);
			Collections.sort(scThreads, sorter);
			size = scThreads.size();
			data = new Object[size][2];
			for (int i = 0; i < scThreads.size(); i++) {
				SchemeCableThread thread = (SchemeCableThread) scThreads.get(i);
				data[i][0] = thread;
				SchemePort port = thread.getSchemePort(schemeDevice);
				if (port != null) {
					data[i][1] = port;
					if (port.equals(thread.getSourceSchemePort()))
						portDirection = DirectionType.__OUT;
					else
						portDirection = DirectionType.__IN;
				} else
					data[i][1] = NULL_PORT;
			}
		} else {
			size = 0;
			data = new Object[0][2];
		}
		TableModel model = new AbstractTableModel() {
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex == 0)
					return false;
				return true;
			}

			public int getColumnCount() {
				return 2;
			}

			public int getRowCount() {
				return size;
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				return data[rowIndex][columnIndex];
			}
			
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				data[rowIndex][columnIndex] = aValue;
			}
		};
		table.setModel(model);			
		DefaultCellEditor editor = new DefaultCellEditor(combo);
		table.getColumnModel().getColumn(1).setCellEditor(editor);
	}

	/**
	 * @return SchemeDevice
	 * @see StorableObjectEditor#getObject()
	 */
	public Object getObject() {
		return schemeDevice;
	}

	/**
	 * 
	 * @see StorableObjectEditor#commitChanges()
	 */
	public void commitChanges() {
		for (int i = 0; i < size; i++) {
			if (!data[i][1].equals(NULL_PORT)) {
				if (portDirection == DirectionType.__OUT)
					((SchemeCableThread)data[i][0]).setSourceSchemePort((SchemePort)data[i][1]);
				else
					((SchemeCableThread)data[i][0]).setTargetSchemePort((SchemePort)data[i][1]);
			}
		}
		
	}
}
