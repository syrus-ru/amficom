package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeModel;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeNode;
import com.syrus.AMFICOM.client_.general.ui_.tree.UniTreePanel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.*;
import oracle.jdeveloper.layout.*;

public class CharacteristicsPanel extends GeneralPanel implements
		OperationListener {
	private Dispatcher dispatcher = new Dispatcher();

	protected CharacteristicTypeSort selectedTypeSort;

	Map characteristics = new HashMap();
	Set editableSorts = new HashSet();
	Map typeSortsCharacterizedIds = new HashMap();
	Map addedCharacteristics = new HashMap();
	Map removedCharacteristics = new HashMap();
	PropsADToolBar toolBar;
	UniTreePanel utp;
	JTable jTable;
	PropsTableModel tModel;
	private class CharacterizedObject {
		CharacteristicSort sort;

		Identifier characterizedId;

		Characterized characterized;

		CharacterizedObject(CharacteristicSort sort, Characterized characterized,
				Identifier characterizedId) {
			this.characterized = characterized;
			this.characterizedId = characterizedId;
			this.sort = sort;
		}
	}
	private class CharacterizableObject {
		CharacteristicSort sort;

		Identifier characterizedId;

		Characterizable characterizable;

		CharacterizableObject(CharacteristicSort sort,
				Characterizable characterizable, Identifier characterizedId) {
			this.characterizable = characterizable;
			this.characterizedId = characterizedId;
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
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public CharacteristicsPanel(List characteristics, Identifier characterizedId) {
		this();
		addCharacteristics(characteristics, characterizedId);
	}

	public CharacteristicsPanel(ApplicationContext aContext,
			Characteristic[] characteristics, Identifier characterizedId) {
		this();
		addCharacteristics(characteristics, characterizedId);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = new ApplicationContext();
		this.aContext.setDispatcher(dispatcher);
		this.aContext.setSessionInterface(aContext.getSessionInterface());
		this.aContext.setApplicationModel(aContext.getApplicationModel());
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
		// jTable.setFocusable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(jTable);
		scrollPane.getViewport().setBackground(SystemColor.window);
		scrollPane.setAutoscrolls(true);

		PropsTreeModel model = new PropsTreeModel();
		utp = new UniTreePanel(this.dispatcher, aContext, model);
		utp.getTree().setRootVisible(false);
		utp.setBorder(BorderFactory.createLoweredBevelBorder());

		JPanel n_panel = new JPanel();
		n_panel.setLayout(new BorderLayout());
		n_panel.add(toolBar, BorderLayout.NORTH);
		n_panel.add(utp, BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add(n_panel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void clear() {
		this.characteristics.clear();
		this.editableSorts.clear();
		this.typeSortsCharacterizedIds.clear();
		this.addedCharacteristics.clear();
		this.removedCharacteristics.clear();
	}

	public boolean modify() {
		for (Iterator tit = typeSortsCharacterizedIds.values().iterator(); tit.hasNext();) {
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
			} else if (obj instanceof CharacterizedObject) {
				Characterized characterized = ((CharacterizedObject) obj).characterized;
				List added = (List) addedCharacteristics.get(obj);
				if (added != null) {
					for (Iterator it = added.iterator(); it.hasNext();) {
						characterized.addCharacteristic((Characteristic) it.next());
					}
				}
				List removed = (List) removedCharacteristics.get(obj);
				if (removed != null) {
					for (Iterator it = removed.iterator(); it.hasNext();) {
						characterized.removeCharacteristic((Characteristic) it.next());
					}
				}
			}
		}
		return true;
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
			List removedIds = new LinkedList();
			for (Iterator it = removedCharacteristics.values().iterator(); it.hasNext();) {
				List removed = (List) it.next();
				for (Iterator it2 = removed.iterator(); it2.hasNext();) {
					Characteristic ch = (Characteristic) it2.next();
					removedIds.add(ch.getId());
				}
			}
			GeneralStorableObjectPool.delete(removedIds);
			GeneralStorableObjectPool.flush(true);
			return true;
		} 
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public void addCharacteristics(List chars, Identifier characterizedId) {
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
			Identifier characterizedId, boolean isEditable) {
		typeSortsCharacterizedIds.put(typeSort, new CharacterizableObject(sort,
				characterizable, characterizedId));
		if (isEditable)
			editableSorts.add(typeSort);
		else
			editableSorts.remove(typeSort);
	}

	public void setTypeSortMapping(CharacteristicTypeSort typeSort,
			CharacteristicSort sort, Characterized characterized,
			Identifier characterizedId, boolean isEditable) {
		typeSortsCharacterizedIds.put(typeSort, new CharacterizedObject(sort,
				characterized, characterizedId));
		if (isEditable)
			editableSorts.add(typeSort);
		else
			editableSorts.remove(typeSort);
	}

	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent) ae;
			if (ev.getDataClass().equals(CharacteristicTypeSort.class)) {
				selectedTypeSort = (CharacteristicTypeSort) ev.getSelectedObject();
				setPropsEditable(editableSorts.contains(selectedTypeSort));
			} else
				showNoSelection();

			elementSelected(selectedTypeSort);
		}
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
			List chars = (List) it.next();
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

	void showNoSelection() {
		tModel.clearTable();
		setPropsEditable(false);
	}

	void tableUpdated(Object value, int row, int col) {
		String name = (String) tModel.getValueAt(row, 0);
		List chars = getCharacteristics();
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

	Characteristic getCharacterisric(List chars, String name) {
		for (Iterator it = chars.iterator(); it.hasNext();) {
			Characteristic ch = (Characteristic) it.next();
			if (ch.getName().equals(name)) {
				return ch;
			}
		}
		return null;
	}

	void setCharacteristicValue(List characteristics, String name, String value) {
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			Characteristic ch = (Characteristic) it.next();
			if (ch.getName().equals(name)) {
				ch.setValue(value);
				break;
			}
		}
	}

	List getCharacteristics() {
		CharacterizedObject obj = (CharacterizedObject) typeSortsCharacterizedIds
				.get(selectedTypeSort);
		if (obj == null) {
			System.err
					.println("CharacterizedObject not set for CharacteristicTypeSort "
							+ selectedTypeSort);
			return null;
		}
		Identifier characterizedId = obj.characterizedId;
		List chars = (List) characteristics.get(characterizedId);
		List newChars = (List) addedCharacteristics.get(obj);
		if (newChars == null)
			return chars;
		List allChars = new ArrayList(chars.size() + newChars.size());
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
			addButton.setToolTipText(LangModelConfig.getString("label_add_char"));
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

			deleteButton.setToolTipText(LangModelConfig
					.getString("label_delete_char"));
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
				System.err.println("CharacterizedObject not set for CharacteristicTypeSort " + selectedTypeSort);
				return;
			}

			AddPropFrame frame = new AddPropFrame(Environment.getActiveWindow(), "",
					aContext);
			if (frame.showDialog(selectedTypeSort, tModel.getData()) == AddPropFrame.OK) {
				CharacteristicType type = frame.getCharacteristicType();
				Identifier userId = new Identifier(((RISDSessionInfo) aContext
						.getSessionInterface()).getAccessIdentifier().user_id);

				if (obj instanceof CharacterizableObject) {
					CharacteristicSort sort = ((CharacterizableObject) obj).sort;
					Identifier characterizedId = ((CharacterizableObject) obj).characterizedId;
//					 Characterizable characterizable =
//					 ((CharacterizableObject)obj).characterizable;

					try {
						Characteristic ch = Characteristic.createInstance(userId, type,
								type.getDescription(), "", sort.value(), "", characterizedId,
								true, true);
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
				} else if (obj instanceof CharacterizedObject) {
					CharacteristicSort sort = ((CharacterizedObject) obj).sort;
					Identifier characterizedId = ((CharacterizedObject) obj).characterizedId;
//					 Characterized characterized =
//					 ((CharacterizedObject)obj).characterized;

					try {
						Characteristic ch = Characteristic.createInstance(userId, type,
								type.getDescription(), "", sort.value(), "", characterizedId,
								true, true);
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

			List chars = getCharacteristics();
			if (chars != null) {
				Characteristic ch = getCharacterisric(chars, name);
				
				List added = (List) addedCharacteristics.get(obj);
				if (added != null && added.contains(ch)) {
					added.remove(ch);
					if (added.isEmpty())
						addedCharacteristics.remove(obj);
				}
				else {
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

class PropsTreeModel extends ObjectResourceTreeModel {

	public PropsTreeModel() {
	}

	public ObjectResourceTreeNode getRoot() {
		return new ObjectResourceTreeNode("root", LangModelConfig
				.getString("label_comp_chars"), true, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/folder.gif")));
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node) {
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node) {
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node) {
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node) {
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node) {
		return CharacteristicTypeSort.class;
	}

	public ObjectResourceController getNodeChildController(
			ObjectResourceTreeNode node) {
		return CharacteristicTypeController.getInstance();
	}

	public List getChildNodes(ObjectResourceTreeNode node) {
		List vec = new ArrayList(4);

		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();

			if (s.equals("root")) {
				vec.add(new ObjectResourceTreeNode(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL,
						LangModelConfig.getString("label_opt_chars"), true, true));
				vec.add(new ObjectResourceTreeNode(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
						LangModelConfig.getString("label_el_chars"), true, true));
				vec.add(new ObjectResourceTreeNode(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
						LangModelConfig.getString("label_exp_chars"), true, true));
				vec.add(new ObjectResourceTreeNode(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
						LangModelConfig.getString("label_interface_chars"), true, true));
				vec.add(new ObjectResourceTreeNode(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						"Атрибуты отображения", true, true));
			}
		}
		return vec;
	}
}
