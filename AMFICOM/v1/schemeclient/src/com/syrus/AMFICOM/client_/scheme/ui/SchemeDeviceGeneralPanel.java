/*-
 * $Id: SchemeDeviceGeneralPanel.java,v 1.10 2005/09/06 12:45:57 stas Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.NumberedComparator;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemePortWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2005/09/06 12:45:57 $
 * @module schemeclient
 */

public class SchemeDeviceGeneralPanel extends DefaultStorableObjectEditor {
	protected SchemeDevice schemeDevice;
	ApplicationContext aContext;

	JPanel pnPanel0 = new JPanel();
	JTable table = new JTable();
	AComboBox combo = new AComboBox();
	JLabel titlelabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.THREAD_PORT_MAP));
	JButton commitBut = new JButton();
	
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

	@SuppressWarnings("unqualified-field-access")
	private void jbInit() throws Exception {
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 4;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( titlelabel, gbcPanel0 );
		pnPanel0.add( titlelabel );
		
		gbcPanel0.gridx = 5;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 1;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( commitBut, gbcPanel0 );
		pnPanel0.add( commitBut );
		
		JScrollPane pane = new JScrollPane(this.table);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 6;
		gbcPanel0.gridheight = 4;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints( pane, gbcPanel0 );
		pnPanel0.add( pane );
		
		this.commitBut.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.commitBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitBut.setFocusPainted(false);
		this.commitBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	/**
	 * @return JTable
	 * @see StorableObjectEditor#getGUI()
	 */
	public JComponent getGUI() {
	  return this.pnPanel0;
	}

	/**
	 * @param or SchemeDevice
	 * @see StorableObjectEditor#setObject(java.lang.Object)
	 */
	public void setObject(Object or) {
		this.schemeDevice = (SchemeDevice)or;
		this.combo.removeAllItems();
//	XXX check use of numbered comparator
		if (this.schemeDevice != null) {
			List<SchemePort> sPorts = new ArrayList<SchemePort>(this.schemeDevice.getSchemePorts());
			Comparator<SchemePort> sorter1 = new NumberedComparator<SchemePort>(
					SchemePortWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME);
			Collections.sort(sPorts, sorter1);
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
			NumberedComparator<SchemeCableThread> sorter2 = new NumberedComparator<SchemeCableThread>(SchemeCableThreadWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, true);
			Collections.sort(scThreads, sorter2);
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
				for (Object[] value : SchemeDeviceGeneralPanel.this.data) {
					if (value[columnIndex].equals(aValue)) {
						value[columnIndex] = NULL_PORT; 
					}
				}
				SchemeDeviceGeneralPanel.this.data[rowIndex][columnIndex] = aValue;
				fireTableDataChanged();
			}
		};
		this.table.setModel(model);			
		DefaultCellEditor editor = new NameableEditor(this.combo);
		this.table.getColumnModel().getColumn(1).setCellEditor(editor);
		TableCellRenderer cellRenderer = new NameableRenderer();
		this.table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		this.table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
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
		super.commitChanges();
		for (int i = 0; i < this.size; i++) {
			SchemePort port = this.data[i][1].equals(NULL_PORT) ? null : (SchemePort)this.data[i][1];
			if (this.portDirection == IdlDirectionType.__OUT) {
				((SchemeCableThread)this.data[i][0]).setSourceSchemePort(port);
			} else {
				((SchemeCableThread)this.data[i][0]).setTargetSchemePort(port);
			}
		}
	}
}

class NameableEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 7952972379949330113L;

	public NameableEditor(AComboBox comboBox) {
		super(comboBox);
		comboBox.setRenderer(new NameableListRenderer());
		comboBox.setFontSize(AComboBox.SMALL_FONT);
	}
	
}

class NameableListRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = -3477107347883967982L;

	public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof Namable) {
			this.setText(((Namable)value).getName());
		}
		return this;
	}
}


class NameableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 7251600604516400642L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof Namable) {
			this.setText(((Namable)value).getName());
		}
		return this;
	}
}
