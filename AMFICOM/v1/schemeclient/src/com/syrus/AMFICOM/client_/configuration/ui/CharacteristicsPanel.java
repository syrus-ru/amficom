/*
 * $Id: CharacteristicsPanel.java,v 1.5 2005/03/17 14:45:35 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.Configure.UI.AddPropFrame;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/03/17 14:45:35 $
 * @module schemeclient_v1
 */

public abstract class CharacteristicsPanel implements
		OperationListener, StorableObjectEditor {

	ApplicationContext aContext;
	private Dispatcher dispatcher = new Dispatcher();
	protected CharacteristicTypeSort selectedTypeSort;
	Map characteristics = new HashMap();
	Set editableSorts = new HashSet();
	Map typeSortsCharacterizedIds = new HashMap();
	Map addedCharacteristics = new HashMap();
	Map removedCharacteristics = new HashMap();

	JPanel pnPanel0 = new JPanel(); 
	PropsADToolBar toolBar;
	Tree utp;
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
		scrollPane.setPreferredSize(new Dimension(100, 50));

		PropsTreeModel model = new PropsTreeModel();
		utp = new Tree(this.dispatcher, new SOMutableNode(model, "root"));
		utp.setRootVisible(false);
		utp.setBorder(BorderFactory.createLoweredBevelBorder());

		JPanel n_panel = new JPanel();
		n_panel.setLayout(new BorderLayout());
		n_panel.add(toolBar, BorderLayout.NORTH);
		n_panel.add(utp, BorderLayout.CENTER);

		pnPanel0.setLayout(new BorderLayout());
		pnPanel0.add(n_panel, BorderLayout.NORTH);
		pnPanel0.add(scrollPane, BorderLayout.CENTER);
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
			List removedIds = new LinkedList();
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
		} 
		catch (ApplicationException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public void addCharacteristics(final Collection chars, Identifier characterizedId) {
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

	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent) ae;
			if (ev.getSelectedObject() instanceof CharacteristicTypeSort) {
				selectedTypeSort = (CharacteristicTypeSort) ev.getSelectedObject();
				setPropsEditable(editableSorts.contains(selectedTypeSort));
			} 
			else
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
		CharacterizableObject obj = (CharacterizableObject) typeSortsCharacterizedIds
				.get(selectedTypeSort);
		if (obj == null) {
			System.err
					.println("CharacterizedObject not set for CharacteristicTypeSort "
							+ selectedTypeSort);
			return null;
		}
		Identifier characterizedId = obj.characterizableId;
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
					Identifier characterizedId = ((CharacterizableObject) obj).characterizableId;
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

class PropsTreeModel implements SOTreeDataModel {
	public Icon getNodeIcon(SONode node) {
		return null;
	}

	public Color getNodeColor(SONode node) {
		return Color.BLACK;
	}
	
	public String getNodeName(SONode node) {
		if (node.getUserObject() instanceof String) {
			return Constants.ROOT;
		}
		if (node.getUserObject() instanceof CharacteristicTypeSort) {
			CharacteristicTypeSort sort = (CharacteristicTypeSort)node.getUserObject();
			switch (sort.value()) {
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_OPTICAL:
					return Constants.CHARACTERISTICTYPESORT_OPTICAL;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_ELECTRICAL:
					return Constants.CHARACTERISTICTYPESORT_ELECTRICAL;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_OPERATIONAL:
					return Constants.CHARACTERISTICTYPESORT_OPERATIONAL;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_INTERFACE:
					return Constants.CHARACTERISTICTYPESORT_INTERFACE;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL:
					return Constants.CHARACTERISTICTYPESORT_VISUAL;
				default:
					throw new UnsupportedOperationException("Unsupported CharacteristicTypeSort"); //$NON-NLS-1$
			}
		}
		throw new UnsupportedOperationException("Unsupported object"); //$NON-NLS-1$
	}

	public ObjectResourceController getNodeController(SONode node) {
		return CharacteristicTypeController.getInstance();
	}

	public void updateChildNodes(SONode node) {
		if(!node.isExpanded())
			return;
		List contents = node.getChildrenUserObjects();

		if (node.getUserObject() instanceof String) {
			String s = (String) node.getUserObject();

			if (s.equals(Constants.ROOT)) {
				if(!contents.contains(CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL))
					node.add(new SOMutableNode(this,
							CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL, false));
				if(!contents.contains(CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL))
					node.add(new SOMutableNode(this,
							CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL, false));
				if(!contents.contains(CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL))
					node.add(new SOMutableNode(this,
							CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL, false));
				if(!contents.contains(CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE))
					node.add(new SOMutableNode(this,
							CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE, false));
				if(!contents.contains(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL))
					node.add(new SOMutableNode(this,
							CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL, false));
			}
		}
	}
}
