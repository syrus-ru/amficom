/*-
 * $Id: SchemeDeviceGeneralPanel.java,v 1.6 2005/08/05 12:39:59 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemePortWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
 */

public class SchemeDeviceGeneralPanel extends DefaultStorableObjectEditor {
	protected SchemeDevice schemeDevice;
	ApplicationContext aContext;

	JTable table;
	JComboBox combo;
	
	private static final String NULL_PORT = "---"; //$NON-NLS-1$
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
		this.table = new JTable();
		this.combo = new ColorChooserComboBox();
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	/**
	 * @return JTable
	 * @see StorableObjectEditor#getGUI()
	 */
	public JComponent getGUI() {
	  return this.table;
	}

	/**
	 * @param or SchemeDevice
	 * @see StorableObjectEditor#setObject(java.lang.Object)
	 */
	public void setObject(Object or) {
		this.schemeDevice = (SchemeDevice)or;
		this.combo.removeAllItems();
		
		if (this.schemeDevice != null) {
			List<SchemePort> sPorts = new ArrayList<SchemePort>(this.schemeDevice.getSchemePorts());
			WrapperComparator sorter = new WrapperComparator(
					SchemePortWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME, true);
			Collections.sort(sPorts, sorter);
			Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>();
			this.combo.addItem(NULL_PORT);
			for (int i = 0; i < sPorts.size(); i++) {
				SchemePort port = sPorts.get(i);
				this.combo.addItem(port);
				SchemeCableThread thread = port.getSchemeCableThread();
				if (thread != null)
					schemeCableLinks.add(thread.getParentSchemeCableLink());
			}

			List<SchemeCableThread> scThreads = new ArrayList<SchemeCableThread>();
			for (SchemeCableLink scl : schemeCableLinks) {
				scThreads.addAll(scl.getSchemeCableThreads());
			}
			sorter = new WrapperComparator(SchemeCableThreadWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME, true);
			Collections.sort(scThreads, sorter);
			this.size = scThreads.size();
			this.data = new Object[this.size][2];
			for (int i = 0; i < scThreads.size(); i++) {
				SchemeCableThread thread = scThreads.get(i);
				this.data[i][0] = thread;
				SchemePort port = thread.getSchemePort(this.schemeDevice);
				if (port != null) {
					this.data[i][1] = port;
					if (port.equals(thread.getSourceSchemePort()))
						this.portDirection = IdlDirectionType.__OUT;
					else
						this.portDirection = IdlDirectionType.__IN;
				} else
					this.data[i][1] = NULL_PORT;
			}
		} else {
			this.size = 0;
			this.data = new Object[0][2];
		}
		TableModel model = new AbstractTableModel() {
			private static final long serialVersionUID = -3385987775156183367L;

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex == 0)
					return false;
				return true;
			}

			public int getColumnCount() {
				return 2;
			}

			public int getRowCount() {
				return SchemeDeviceGeneralPanel.this.size;
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				return SchemeDeviceGeneralPanel.this.data[rowIndex][columnIndex];
			}
			
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				SchemeDeviceGeneralPanel.this.data[rowIndex][columnIndex] = aValue;
			}
		};
		this.table.setModel(model);			
		DefaultCellEditor editor = new DefaultCellEditor(this.combo);
		this.table.getColumnModel().getColumn(1).setCellEditor(editor);
	}

	/**
	 * @return SchemeDevice
	 * @see StorableObjectEditor#getObject()
	 */
	public Object getObject() {
		return this.schemeDevice;
	}

	/**
	 * 
	 * @see StorableObjectEditor#commitChanges()
	 */
	public void commitChanges() {
		for (int i = 0; i < this.size; i++) {
			if (!this.data[i][1].equals(NULL_PORT)) {
				if (this.portDirection == IdlDirectionType.__OUT) {
					((SchemeCableThread)this.data[i][0]).setSourceSchemePort((SchemePort)this.data[i][1]);
				} else {
					((SchemeCableThread)this.data[i][0]).setTargetSchemePort((SchemePort)this.data[i][1]);
				}
			}
		}
	}
}
