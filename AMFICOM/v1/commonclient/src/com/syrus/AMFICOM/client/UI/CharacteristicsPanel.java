/*-
 * $Id: CharacteristicsPanel.java,v 1.14 2005/07/20 06:04:43 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.util.Log;

/**
 * @author $Author: bob $
 * @version $Revision: 1.14 $, $Date: 2005/07/20 06:04:43 $
 * @module commonclient_v1
 */

public abstract class CharacteristicsPanel extends DefaultStorableObjectEditor {
	protected static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL,	
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
	};

	protected ApplicationContext aContext;
	protected CharacteristicTypeSort selectedTypeSort;
	protected Map characteristics = new HashMap();
	protected Set editableSorts = new HashSet();
	protected Map typeSortsCharacterizedIds = new HashMap();
	protected Map addedCharacteristics = new HashMap();
	protected Map removedCharacteristics = new HashMap();

	JPanel pnPanel0 = new JPanel();
	PropsADToolBar toolBar;
	AComboBox characteristicTypeSortCombo;
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

	private class CharacteristicTypeSortRenderer extends JLabel implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			CharacteristicTypeSort sort = (CharacteristicTypeSort)value;
			String name;
			switch (sort.value()) {
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_OPTICAL:
					name = LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_OPTICAL);
					break;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_ELECTRICAL:
					name = LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_ELECTRICAL);
					break;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_OPERATIONAL:
					name = LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_OPERATIONAL);
					break;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_INTERFACE:
					name = LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_INTERFACE);
					break;
				case CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL:
					name = LangModelGeneral.getString(ResourceKeys.I18N_CHARACTERISTICTYPESORT_VISUAL);
					break;
				default:
					throw new UnsupportedOperationException("CharacteristicTypeSortRenderer: unknown CharacteristicTypeSort " + sort.value()); //$NON-NLS-1$
			}
			this.setText(name);
			return this;
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

		this.characteristicTypeSortCombo = new AComboBox(sorts);
		this.characteristicTypeSortCombo.addItem(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);
		this.characteristicTypeSortCombo.setRenderer(new CharacteristicTypeSortRenderer());
		this.characteristicTypeSortCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				item_stateChanged(e);
			}
		});

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
		gbcPanel0.insets = new Insets(2, 2, 0, 2);
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
		gbPanel0.setConstraints(this.characteristicTypeSortCombo, gbcPanel0);
		this.pnPanel0.add(this.characteristicTypeSortCombo);

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
	
	public void setEditable(boolean editable) {
		this.wtModel.setColumnEditable(1, editable);
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
/*@todo Посоветовать Владимиру Александровичу это всё снести 
 * 
		for (Iterator tits = this.typeSortsCharacterizedIds.values().iterator(); tits.hasNext();) {
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
		*/
		try {
			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
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
			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, true);
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
		this.toolBar.setCommitButtonEnabled(b);
		this.wtModel.setColumnEditable(1, b);
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

	void item_stateChanged(ItemEvent e) {
		this.selectedTypeSort = (CharacteristicTypeSort) e.getItem();
		setPropsEditable(this.editableSorts.contains(this.selectedTypeSort));
		elementSelected(this.selectedTypeSort);
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
			Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort " + this.selectedTypeSort, Level.FINER); //$NON-NLS-1$
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
		JButton commitButton = new JButton();

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
			this.addButton.setFocusPainted(false);
			this.addButton.setEnabled(false);
			this.addButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_ADD));
			this.addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (CharacteristicsPanel.this.selectedTypeSort == null) {
						return;
					}
					Object object = CharacteristicsPanel.this.typeSortsCharacterizedIds.get(CharacteristicsPanel.this.selectedTypeSort);
					if (object == null) {
						Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort "  //$NON-NLS-1$
										+ CharacteristicsPanel.this.selectedTypeSort, Level.FINER);
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
										type.getName(), type.getDescription(), "", characterizable, true,
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
			this.deleteButton.setEnabled(false);
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
										+ CharacteristicsPanel.this.selectedTypeSort, Level.FINER);
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
			
			this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
			this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
			this.commitButton.setFocusPainted(false);
			this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
			this.commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commitChanges();
				}
			});
			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(this.addButton);
			this.add(this.deleteButton);
			this.add(Box.createHorizontalGlue());
			this.add(this.commitButton);
		}

		public void setAddButtonEnabled(boolean b) {
			this.addButton.setEnabled(b);
		}

		public void setCancelButtonEnabled(boolean b) {
			this.deleteButton.setEnabled(b);
		}
		
		public void setCommitButtonEnabled(boolean b) {
			this.commitButton.setEnabled(b);
		}
	}
}
