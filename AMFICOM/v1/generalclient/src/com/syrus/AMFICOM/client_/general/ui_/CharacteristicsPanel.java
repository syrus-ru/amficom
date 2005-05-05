/*-
 * $Id: CharacteristicsPanel.java,v 1.4 2005/05/05 11:04:48 bob Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.client_.general.ui_.tree_.IconedNode;
import com.syrus.AMFICOM.client_.general.ui_.tree_.IconedTreeUI;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.Constants;
import com.syrus.AMFICOM.resource.LangModelGeneral;

/**
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2005/05/05 11:04:48 $
 * @module generalclient_v1
 */

public abstract class CharacteristicsPanel extends DefaultStorableObjectEditor {
	protected static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL };

	protected ApplicationContext aContext;
	protected CharacteristicTypeSort selectedTypeSort;
	protected Map characteristics = new HashMap();
	protected Set editableSorts = new HashSet();
	protected Map typeSortsCharacterizedIds = new HashMap();
	protected Map addedCharacteristics = new HashMap();
	protected Map removedCharacteristics = new HashMap();

	JPanel pnPanel0 = new JPanel();
	PropsADToolBar toolBar;
	IconedTreeUI treeUI;
	JTable jTable;
	PropsTableModel tModel;

	private class CharacterizableObject {
		CharacteristicSort sort;

		Identifier characterizableId;

		Characterizable characterizable;

		CharacterizableObject(CharacteristicSort sort,
				Characterizable characterizable, Identifier characterizedId) {
			this.characterizable = characterizable;
			this.characterizableId = characterizedId;
			this.sort = sort;
		}
	}

	public CharacteristicsPanel() {
		super();

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CharacteristicsPanel(List characteristics, Identifier characterizedId) {
		this();
		addCharacteristics(characteristics, characterizedId);
	}

	public CharacteristicsPanel(Characteristic[] characteristics, Identifier characterizedId) {
		this();
		addCharacteristics(characteristics, characterizedId);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	private void jbInit() throws Exception {
		toolBar = new PropsADToolBar();

		tModel = new PropsTableModel(new String[] { "", "" }, new Object[] { "" },
				null, new int[] { 1 });

		jTable = new JTable(tModel);
		jTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting())
							return;
						toolBar.setCancelButtonEnabled(!jTable.getSelectionModel()
								.isSelectionEmpty());
					}
				});
		jTable.getColumnModel().getColumn(0).setPreferredWidth(180);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		treeUI = new IconedTreeUI(createRoot());
		JTree tree = treeUI.getTree();
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				tree_valueChanged(e);
			}
		});
		JComponent treePanel = treeUI.getTree();
		treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		treePanel.setPreferredSize(null);

		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout(gbPanel0);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 1;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(toolBar, gbcPanel0);
		pnPanel0.add(toolBar);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 1;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(treePanel, gbcPanel0);
		pnPanel0.add(treePanel);

		JScrollPane tablePane = new JScrollPane(jTable);
		tablePane.getViewport().setBackground(SystemColor.window);
		tablePane.setAutoscrolls(true);
		tablePane.setPreferredSize(new Dimension(100, 50));

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 2;
		gbcPanel0.gridwidth = 1;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(tablePane, gbcPanel0);
		pnPanel0.add(tablePane);
	}

	public JComponent getGUI() {
		return pnPanel0;
	}

	public void clear() {
		this.characteristics.clear();
		this.editableSorts.clear();
		this.typeSortsCharacterizedIds.clear();
		this.addedCharacteristics.clear();
		this.removedCharacteristics.clear();
	}

	public void commitChanges() {
		for (Iterator tit = typeSortsCharacterizedIds.values().iterator(); tit
				.hasNext();) {
			Object obj = tit.next();
			if (obj instanceof CharacterizableObject) {
				Characterizable characterizable = ((CharacterizableObject) obj).characterizable;
				List added = (List) addedCharacteristics.get(obj);
				if (added != null) {
					for (Iterator it = added.iterator(); it.hasNext();) {
						characterizable.addCharacteristic((Characteristic) it.next());
					}
				}
				List removed = (List) removedCharacteristics.get(obj);
				if (removed != null) {
					for (Iterator it = removed.iterator(); it.hasNext();) {
						characterizable.removeCharacteristic((Characteristic) it.next());
					}
				}
			}
		}
	}

	public boolean save() {
		try {
			for (Iterator it = addedCharacteristics.values().iterator(); it.hasNext();) {
				List added = (List) it.next();
				for (Iterator it2 = added.iterator(); it2.hasNext();) {
					Characteristic ch = (Characteristic) it2.next();
					GeneralStorableObjectPool.putStorableObject(ch.getType());
					GeneralStorableObjectPool.putStorableObject(ch);
				}
			}
			Set removedIds = new HashSet();
			for (Iterator it = removedCharacteristics.values().iterator(); it
					.hasNext();) {
				List removed = (List) it.next();
				for (Iterator it2 = removed.iterator(); it2.hasNext();) {
					Characteristic ch = (Characteristic) it2.next();
					removedIds.add(ch.getId());
				}
			}
			GeneralStorableObjectPool.delete(removedIds);
			GeneralStorableObjectPool.flush(true);
		} catch (ApplicationException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public void addCharacteristics(final Collection chars,
			Identifier characterizedId) {
		this.characteristics.put(characterizedId, chars);
		elementSelected(selectedTypeSort);
	}

	public void addCharacteristics(Characteristic[] chars,
			Identifier characterizedId) {
		this.characteristics.put(characterizedId, Arrays.asList(chars));
		elementSelected(selectedTypeSort);
	}

	public void setTypeSortMapping(CharacteristicTypeSort typeSort,
			CharacteristicSort sort, Characterizable characterizable,
			Identifier characterizableId, boolean isEditable) {
		typeSortsCharacterizedIds.put(typeSort, new CharacterizableObject(sort,
				characterizable, characterizableId));
		if (isEditable)
			editableSorts.add(typeSort);
		else
			editableSorts.remove(typeSort);
	}

	void setPropsEditable(boolean b) {
		toolBar.setAddButtonEnabled(b && selectedTypeSort != null);
		toolBar.setCancelButtonEnabled(!jTable.getSelectionModel()
				.isSelectionEmpty()
				&& b);
		if (b)
			tModel.setEditableColumns(new int[] { 1 });
		else
			tModel.setEditableColumns(new int[0]);
	}

	void elementSelected(CharacteristicTypeSort selected_type) {
		if (selected_type == null) {
			showNoSelection();
			return;
		}

		tModel.clearTable();
		for (Iterator it = characteristics.values().iterator(); it.hasNext();) {
			Collection chars = (Collection) it.next();
			if (chars != null)
				for (Iterator it2 = chars.iterator(); it2.hasNext();) {
					Characteristic ch = (Characteristic) it2.next();
					if (((CharacteristicType) ch.getType()).getSort().equals(
							selected_type)) {
						tModel.addRow(ch.getName(), new Object[] { ch.getValue() });
					}
				}
		}
		setPropsEditable(editableSorts.contains(selectedTypeSort));
	}

	public void showNoSelection() {
		tModel.clearTable();
		setPropsEditable(false);
	}

	void tree_valueChanged(TreeSelectionEvent e) {
		Item node = (Item) e.getPath().getLastPathComponent();
		if (node == null)
			return;

		selectedTypeSort = (CharacteristicTypeSort) node.getObject();
		setPropsEditable(editableSorts.contains(selectedTypeSort));
		elementSelected(selectedTypeSort);
	}

	protected void tableUpdated(Object value, int row, int col) {
		String name = (String) tModel.getValueAt(row, 0);
		Collection chars = getCharacteristics();
		if (chars != null)
			setCharacteristicValue(chars, name, (String) value);
	}

	class PropsTableModel extends FixedSizeEditableTableModel {

		public PropsTableModel(String[] p_columns, // заголовки столбцов
				Object[] p_defaultv,// дефолтные значения
				String[] p_rows, // заголовки (0й столбец) строк
				int[] editable) {
			super(p_columns, p_defaultv, p_rows, editable);
		}

		public void setValueAt(Object value, int row, int col) {
			super.setValueAt(value, row, col);
			tableUpdated(value, row, col);
		}

		public List getData() {
			return rows;
		}
	}

	private Item createRoot() {
		Item root = new IconedNode(Constants.ROOT, LangModelGeneral.getString(Constants.ROOT));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL,
				LangModelGeneral.getString(Constants.CHARACTERISTICTYPESORT_OPTICAL), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
				LangModelGeneral.getString(Constants.CHARACTERISTICTYPESORT_ELECTRICAL), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
				LangModelGeneral.getString(Constants.CHARACTERISTICTYPESORT_OPERATIONAL), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
				LangModelGeneral.getString(Constants.CHARACTERISTICTYPESORT_INTERFACE), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
				LangModelGeneral.getString(Constants.CHARACTERISTICTYPESORT_VISUAL), false));
		return root;
	}

	Characteristic getCharacterisric(Collection chars, String name) {
		for (Iterator it = chars.iterator(); it.hasNext();) {
			Characteristic ch = (Characteristic) it.next();
			if (ch.getName().equals(name)) {
				return ch;
			}
		}
		return null;
	}

	protected void setCharacteristicValue(Collection characteristics, String name,
			String value) {
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			Characteristic ch = (Characteristic) it.next();
			if (ch.getName().equals(name)) {
				ch.setValue(value);
				break;
			}
		}
	}

	Collection getCharacteristics() {
		CharacterizableObject obj = (CharacterizableObject) typeSortsCharacterizedIds
				.get(selectedTypeSort);
		if (obj == null) {
			System.err
					.println("CharacterizedObject not set for CharacteristicTypeSort "
							+ selectedTypeSort);
			return null;
		}
		Identifier characterizedId = obj.characterizableId;
		Collection chars = (Collection) characteristics.get(characterizedId);
		Collection newChars = (Collection) addedCharacteristics.get(obj);
		if (newChars == null)
			return chars;
		Collection allChars = new ArrayList(chars.size() + newChars.size());
		allChars.addAll(chars);
		allChars.addAll(newChars);
		return allChars;
	}

	class PropsADToolBar extends JPanel {
		public final Dimension btn_size = new Dimension(24, 24);

		JButton addButton = new JButton();

		JButton deleteButton = new JButton();

		public PropsADToolBar() {
			try {
				jbInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void jbInit() throws Exception {
			addButton.setToolTipText(LangModelGeneral.getString(Constants.ADD_CHARACTERISTIC));
			addButton.setPreferredSize(btn_size);
			addButton.setMaximumSize(btn_size);
			// addButton.setFocusable(false);
			addButton.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			addButton.setFocusPainted(false);
			addButton.setEnabled(false);
			addButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					"images/newprop.gif")));
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addButton_actionPerformed(e);
				}
			});

			deleteButton.setToolTipText(LangModelGeneral.getString(Constants.REMOVE_CHARACTERISTIC));
			deleteButton.setPreferredSize(btn_size);
			deleteButton.setMaximumSize(btn_size);
			// deleteButton.setFocusable(false);
			deleteButton.setEnabled(false);
			deleteButton.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			deleteButton.setFocusPainted(false);
			deleteButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					"images/delete.gif")));
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteButton_actionPerformed(e);
				}
			});

			setLayout(new XYLayout());
			add(addButton, new XYConstraints(0, 0, -1, -1));
			add(deleteButton, new XYConstraints(btn_size.width, 0, -1, -1));
		}

		public void setAddButtonEnabled(boolean b) {
			addButton.setEnabled(b);
		}

		public void setCancelButtonEnabled(boolean b) {
			deleteButton.setEnabled(b);
		}

		void addButton_actionPerformed(ActionEvent e) {
			if (selectedTypeSort == null)
				return;
			Object obj = typeSortsCharacterizedIds.get(selectedTypeSort);
			if (obj == null) {
				System.err
						.println("CharacterizedObject not set for CharacteristicTypeSort "
								+ selectedTypeSort);
				return;
			}

			CharacteristicAddDialog frame = new CharacteristicAddDialog(Environment.getActiveWindow(), "",
					aContext);
			if (frame.showDialog(selectedTypeSort, tModel.getData()) == CharacteristicAddDialog.OK) {
				CharacteristicType type = frame.getCharacteristicType();
				Identifier userId = new Identifier(((RISDSessionInfo) aContext
						.getSessionInterface()).getAccessIdentifier().user_id);

				if (obj instanceof CharacterizableObject) {
					CharacteristicSort sort = ((CharacterizableObject) obj).sort;
					Identifier characterizedId = ((CharacterizableObject) obj).characterizableId;
					// Characterizable characterizable =
					// ((CharacterizableObject)obj).characterizable;

					try {
						Characteristic ch = Characteristic.createInstance(userId, type,
								type.getDescription(), "", sort, "", characterizedId, true,
								true);
						List added = (List) addedCharacteristics.get(obj);
						if (added == null) {
							added = new LinkedList();
							addedCharacteristics.put(obj, added);
						}
						added.add(ch);

						int n = tModel.addRow(ch.getName(), new String[] { "" });
						jTable.setRowSelectionInterval(n, n);
					} catch (CreateObjectException ex) {
						ex.printStackTrace();
					}
				}
			}
			jTable.updateUI();
		}

		void deleteButton_actionPerformed(ActionEvent e) {
			int n = jTable.getSelectedRow();
			if (n == -1)
				return;

			if (selectedTypeSort == null)
				return;
			Object obj = typeSortsCharacterizedIds.get(selectedTypeSort);
			if (obj == null) {
				System.err
						.println("CharacterizedObject not set for CharacteristicTypeSort "
								+ selectedTypeSort);
				return;
			}

			String name = (String) tModel.getValueAt(n, 0);

			Collection chars = getCharacteristics();
			if (chars != null) {
				Characteristic ch = getCharacterisric(chars, name);

				List added = (List) addedCharacteristics.get(obj);
				if (added != null && added.contains(ch)) {
					added.remove(ch);
					if (added.isEmpty())
						addedCharacteristics.remove(obj);
				} else {
					List removed = (List) removedCharacteristics.get(obj);
					if (removed == null) {
						removed = new LinkedList();
						removedCharacteristics.put(obj, removed);
					}
					removed.add(ch);
				}

				tModel.removeRow(n);
				jTable.updateUI();
			}
		}
	}
}
