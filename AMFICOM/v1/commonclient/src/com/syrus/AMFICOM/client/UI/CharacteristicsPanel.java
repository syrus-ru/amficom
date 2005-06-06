/*-
 * $Id: CharacteristicsPanel.java,v 1.6 2005/06/06 11:02:26 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicWrapper;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.CharacteristicType_TransferablePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/06/06 11:02:26 $
 * @module commonclient_v1
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
	WrapperedTable wTable;
	WrapperedTableModel wtModel;

	private class CharacterizableObject {
		Identifier characterizableId;

		Characterizable characterizable;

		CharacterizableObject(
				Characterizable characterizable, Identifier characterizedId) {
			this.characterizable = characterizable;
			this.characterizableId = characterizedId;
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
		this.toolBar = new PropsADToolBar();

		this.wtModel = new WrapperedTableModel(CharacteristicWrapper.getInstance(), new String[] {
			StorableObjectWrapper.COLUMN_NAME, CharacteristicWrapper.COLUMN_VALUE });

		this.wTable = new WrapperedTable(this.wtModel);
		this.wTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting())
							return;
						CharacteristicsPanel.this.toolBar.setCancelButtonEnabled(!CharacteristicsPanel.this.wTable.getSelectionModel().isSelectionEmpty());
					}
				});
		this.wTable.getColumnModel().getColumn(0).setPreferredWidth(180);
		this.wTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.treeUI = new IconedTreeUI(createRoot());
		JTree tree = this.treeUI.getTree();
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				tree_valueChanged(e);
			}
		});
		JComponent treePanel = this.treeUI.getTree();
		treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		treePanel.setPreferredSize(null);

		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 1;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.toolBar, gbcPanel0);
		this.pnPanel0.add(this.toolBar);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 1;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(treePanel, gbcPanel0);
		this.pnPanel0.add(treePanel);

		JScrollPane tablePane = new JScrollPane(this.wTable);
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
		this.pnPanel0.add(tablePane);
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public void clear() {
		this.characteristics.clear();
		this.editableSorts.clear();
		this.typeSortsCharacterizedIds.clear();
		this.addedCharacteristics.clear();
		this.removedCharacteristics.clear();
	}

	public void commitChanges() {
		for (Iterator tits = this.typeSortsCharacterizedIds.values().iterator(); tits
				.hasNext();) {
			Object obj = tits.next();
			if (obj instanceof CharacterizableObject) {
				Characterizable characterizable = ((CharacterizableObject) obj).characterizable;
				List added = (List) this.addedCharacteristics.get(obj);
				if (added != null) {
					for (Iterator it = added.iterator(); it.hasNext();) {
						characterizable.addCharacteristic((Characteristic) it.next());
					}
				}
				List removed = (List) this.removedCharacteristics.get(obj);
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
			Set removedIds = new HashSet();
			for (Iterator it = this.removedCharacteristics.values().iterator(); it.hasNext();) {
				List removed = (List) it.next();
				for (Iterator it2 = removed.iterator(); it2.hasNext();) {
					Characteristic ch = (Characteristic) it2.next();
					removedIds.add(ch.getId());
				}
			}
			StorableObjectPool.delete(removedIds);
			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, true);
		} catch (ApplicationException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public void addCharacteristics(final Collection chars,
			Identifier characterizedId) {
		this.characteristics.put(characterizedId, chars);
		elementSelected(this.selectedTypeSort);
	}

	public void addCharacteristics(Characteristic[] chars,
			Identifier characterizedId) {
		this.characteristics.put(characterizedId, Arrays.asList(chars));
		elementSelected(this.selectedTypeSort);
	}

	public void setTypeSortMapping(CharacteristicTypeSort typeSort,
			Characterizable characterizable,
			Identifier characterizableId, boolean isEditable) {
		this.typeSortsCharacterizedIds.put(typeSort, new CharacterizableObject(
				characterizable, characterizableId));
		if (isEditable)
			this.editableSorts.add(typeSort);
		else
			this.editableSorts.remove(typeSort);
	}

	void setPropsEditable(boolean b) {
		this.toolBar.setAddButtonEnabled(b && this.selectedTypeSort != null);
		this.toolBar.setCancelButtonEnabled(!this.wTable.getSelectionModel()
				.isSelectionEmpty() && b);
		
		// TODO editable columns
		/*if (b)
			wtModel.setEditableColumns(new int[] { 1 });
		else
			tModel.setEditableColumns(new int[0]);*/
	}

	void elementSelected(CharacteristicTypeSort selected_type) {
		if (selected_type == null) {
			showNoSelection();
			return;
		}

		this.wtModel.clear();
		Collection chars2add = new LinkedList();
		for (Iterator it = this.characteristics.values().iterator(); it.hasNext();) {
			Collection chars = (Collection) it.next();
			if (chars != null)
				for (Iterator it2 = chars.iterator(); it2.hasNext();) {
					Characteristic ch = (Characteristic) it2.next();
					if (((CharacteristicType) ch.getType()).getSort().equals(
							selected_type)) {
						chars2add.add(ch);
					}
				}
		}
		this.wtModel.setValues(chars2add);
		setPropsEditable(this.editableSorts.contains(this.selectedTypeSort));
	}

	public void showNoSelection() {
		this.wtModel.clear();
		setPropsEditable(false);
	}

	void tree_valueChanged(TreeSelectionEvent e) {
		Item node = (Item) e.getPath().getLastPathComponent();
		if (node == null)
			return;

		this.selectedTypeSort = (CharacteristicTypeSort) node.getObject();
		setPropsEditable(this.editableSorts.contains(this.selectedTypeSort));
		elementSelected(this.selectedTypeSort);
	}

	private Item createRoot() {
		Item root = new IconedNode(ResourceKeys.I18N_ROOT, LangModelGeneral.getString(ResourceKeys.I18N_ROOT));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL,
				LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_OPTICAL), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
				LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_ELECTRICAL), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
				LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_OPERATIONAL), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
				LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_INTERFACE), false));
		root.addChild(new IconedNode(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
				LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_VISUAL), false));
		return root;
	}

	void setCharacteristicValue(Collection characteristics, String name,
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
		CharacterizableObject obj = (CharacterizableObject) this.typeSortsCharacterizedIds
				.get(this.selectedTypeSort);
		if (obj == null) {
			Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort " + this.selectedTypeSort, Log.FINER); //$NON-NLS-1$
			return null;
		}
		Identifier characterizedId = obj.characterizableId;
		Collection chars = (Collection) this.characteristics.get(characterizedId);
		Collection newChars = (Collection) this.addedCharacteristics.get(obj);
		if (newChars == null)
			return chars;
		Collection allChars = new ArrayList(chars.size() + newChars.size());
		allChars.addAll(chars);
		allChars.addAll(newChars);
		return allChars;
	}
	
	class PropsADToolBar extends JPanel {
		private static final long serialVersionUID = 3544392491714752818L;
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
			this.addButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_ADD_CHARACTERISTIC));
			this.addButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
			// addButton.setFocusable(false);
			this.addButton.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			this.addButton.setFocusPainted(false);
			this.addButton.setEnabled(false);
			this.addButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					"images/newprop.gif")));
			this.addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (CharacteristicsPanel.this.selectedTypeSort == null) {
						return;
					}
					Object object = CharacteristicsPanel.this.typeSortsCharacterizedIds.get(CharacteristicsPanel.this.selectedTypeSort);
					if (object == null) {
						Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort "  //$NON-NLS-1$
										+ CharacteristicsPanel.this.selectedTypeSort, Log.FINER);
						return;
					}

					CharacteristicAddDialog frame = new CharacteristicAddDialog(Environment.getActiveWindow(), "Add characteristic");
					if (frame.showDialog(CharacteristicsPanel.this.selectedTypeSort, CharacteristicsPanel.this.wtModel.getValues()) == JOptionPane.OK_OPTION) {
						CharacteristicType type = frame.getCharacteristicType();
						Identifier userId = LoginManager.getUserId();

						if (object instanceof CharacterizableObject) {
							final Characterizable characterizable = ((CharacterizableObject) object).characterizable;

							try {
								Characteristic ch = Characteristic.createInstance(userId, type,
										type.getDescription(), "", "", characterizable, true,
										true);
								List added = (List) CharacteristicsPanel.this.addedCharacteristics.get(object);
								if (added == null) {
									added = new LinkedList();
									CharacteristicsPanel.this.addedCharacteristics.put(object, added);
								}
								added.add(ch);

								int n = CharacteristicsPanel.this.wtModel.addObject(ch);
								CharacteristicsPanel.this.wTable.setRowSelectionInterval(n, n);
							} catch (CreateObjectException ex) {
								ex.printStackTrace();
							}
						}
					}
					CharacteristicsPanel.this.wTable.revalidate();
				}
			});

			this.deleteButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_REMOVE_CHARACTERISTIC));
			this.deleteButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
			// deleteButton.setFocusable(false);
			this.deleteButton.setEnabled(false);
			this.deleteButton.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			this.deleteButton.setFocusPainted(false);
			this.deleteButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_DELETE));
			this.deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					int n = CharacteristicsPanel.this.wTable.getSelectedRow();
					if (n == -1)
						return;

					if (CharacteristicsPanel.this.selectedTypeSort == null) {
						return;
					}
					Object obj = CharacteristicsPanel.this.typeSortsCharacterizedIds.get(CharacteristicsPanel.this.selectedTypeSort);
					if (obj == null) {
						Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort " //$NON-NLS-1$
										+ CharacteristicsPanel.this.selectedTypeSort, Log.FINER);
						return;
					}

					Characteristic characteristic = (Characteristic)CharacteristicsPanel.this.wtModel.getObject(n);

						List added = (List) CharacteristicsPanel.this.addedCharacteristics.get(obj);
						if (added != null && added.contains(characteristic)) {
							added.remove(characteristic);
							if (added.isEmpty()) {
								CharacteristicsPanel.this.addedCharacteristics.remove(obj);
							}
						} else {
							List removed = (List) CharacteristicsPanel.this.removedCharacteristics.get(obj);
							if (removed == null) {
								removed = new LinkedList();
								CharacteristicsPanel.this.removedCharacteristics.put(obj, removed);
							}
							removed.add(characteristic);
						}

						CharacteristicsPanel.this.wtModel.removeObject(n);
						CharacteristicsPanel.this.wTable.revalidate();
				
				}
			});
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(this.addButton);
			add(this.deleteButton);
		}

		public void setAddButtonEnabled(boolean b) {
			this.addButton.setEnabled(b);
		}

		public void setCancelButtonEnabled(boolean b) {
			this.deleteButton.setEnabled(b);
		}
	}
}
