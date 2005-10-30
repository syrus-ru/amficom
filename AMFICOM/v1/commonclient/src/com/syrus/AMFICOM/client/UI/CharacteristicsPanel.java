/*-
 * $Id: CharacteristicsPanel.java,v 1.23 2005/10/30 15:20:24 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.23 $, $Date: 2005/10/30 15:20:24 $
 * @module commonclient
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
	protected Map<Identifier, Set<Characteristic>> characteristics = new HashMap<Identifier, Set<Characteristic>>();
	protected Set<CharacteristicTypeSort> editableSorts = new HashSet<CharacteristicTypeSort>();
	protected Map<CharacteristicTypeSort, CharacterizableObject> typeSortsCharacterizedIds = new HashMap<CharacteristicTypeSort, CharacterizableObject>();
	protected Map<CharacterizableObject, List<Characteristic>> addedCharacteristics = new HashMap<CharacterizableObject, List<Characteristic>>();
	protected Map<CharacterizableObject, List<Characteristic>> removedCharacteristics = new HashMap<CharacterizableObject, List<Characteristic>>();

	JPanel pnPanel0 = new JPanel();
	PropsADToolBar toolBar;
	AComboBox characteristicTypeSortCombo;
	WrapperedTable wTable;
	WrapperedTableModel<Characteristic> wtModel;

	private class CharacterizableObject {
		Identifier characterizableId;

		Characterizable characterizable;

		CharacterizableObject(final Characterizable characterizable, final Identifier characterizedId) {
			this.characterizable = characterizable;
			this.characterizableId = characterizedId;
		}
	}

	private class CharacteristicTypeSortRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 1811178338069743526L;

		public Component getListCellRendererComponent(final JList list,
				final Object value,
				final int index,
				final boolean isSelected,
				final boolean cellHasFocus) {
			final CharacteristicTypeSort sort = (CharacteristicTypeSort)value;
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
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CharacteristicsPanel(final Set<Characteristic> characteristics, final Identifier characterizedId) {
		this();
		this.addCharacteristics(characteristics, characterizedId);
	}

	public CharacteristicsPanel(final Characteristic[] characteristics, final Identifier characterizedId) {
		this();
		this.addCharacteristics(characteristics, characterizedId);
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	private void jbInit() throws Exception {
		this.toolBar = new PropsADToolBar();

		this.wtModel = new WrapperedTableModel<Characteristic>(CharacteristicWrapper.getInstance(),
				new String[] { StorableObjectWrapper.COLUMN_NAME, CharacteristicWrapper.COLUMN_VALUE });

		this.wTable = new WrapperedTable<Characteristic>(this.wtModel);
		this.wTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				CharacteristicsPanel.this.toolBar.setCancelButtonEnabled(!CharacteristicsPanel.this.wTable.getSelectionModel().isSelectionEmpty()
						&& CharacteristicsPanel.this.toolBar.addButton.isEnabled());
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
		this.selectedTypeSort = (CharacteristicTypeSort) this.characteristicTypeSortCombo.getSelectedItem();

		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
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

		final JScrollPane tablePane = new JScrollPane(this.wTable);
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
	
	public void setEditable(final boolean editable) {
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

//	public void commitChanges() {
		// flush must be performed while saving characterizable object object itself
//		try {
//			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, LoginManager.getUserId(), true);
//		} catch (ApplicationException e) {
//			Log.errorException(e);
//		}
//	}

	protected boolean save() {
		try {
			final Set<Identifier> charIds = new HashSet<Identifier>();
			for (final List<Characteristic> removed : this.removedCharacteristics.values()) {
				for (final Characteristic ch : removed) {
					charIds.add(ch.getId());
				}
			}
			StorableObjectPool.delete(charIds);

			for (final List<Characteristic> added : this.addedCharacteristics.values()) {
				for (final Characteristic ch : added) {
					charIds.add(ch.getId());
				}
			}
			for (final Set<Characteristic> existing : this.characteristics.values()) {
				for (final Characteristic ch : existing) {
					charIds.add(ch.getId());
				}
			}

			Identifier userId = LoginManager.getUserId();
			for (Identifier id : charIds) {
				StorableObjectPool.flush(id, userId, false);
			}
		} catch (ApplicationException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public void addCharacteristics(final Set<Characteristic> chars, final Identifier characterizedId) {
		this.characteristics.put(characterizedId, chars);
		this.elementSelected(this.selectedTypeSort);
	}

	public void addCharacteristics(final Characteristic[] chars, final Identifier characterizedId) {
		this.characteristics.put(characterizedId, new HashSet<Characteristic>(Arrays.asList(chars)));
		this.elementSelected(this.selectedTypeSort);
	}

	public void setTypeSortMapping(final CharacteristicTypeSort typeSort,
			final Characterizable characterizable,
			final Identifier characterizableId,
			final boolean isEditable) {
		this.typeSortsCharacterizedIds.put(typeSort, new CharacterizableObject(characterizable, characterizableId));
		if (isEditable) {
			this.editableSorts.add(typeSort);
		}
		else {
			this.editableSorts.remove(typeSort);
		}
	}

	void setPropsEditable(final boolean b) {
		this.toolBar.setAddButtonEnabled(b && this.selectedTypeSort != null);
		this.toolBar.setCancelButtonEnabled(!this.wTable.getSelectionModel().isSelectionEmpty() && b);
		this.toolBar.setCommitButtonEnabled(b);
		this.wtModel.setColumnEditable(1, b);
	}

	void elementSelected(final CharacteristicTypeSort selectedType) {
		if (selectedType == null) {
			this.showNoSelection();
			return;
		}

		this.wtModel.clear();
		final Collection<Characteristic> chars2add = new LinkedList<Characteristic>();
		for (final Set<Characteristic> chars : this.characteristics.values()) {
			if (chars != null) {
				for (final Characteristic ch : chars) {
					if (ch.getType().getSort().equals(selectedType)) {
						chars2add.add(ch);
					}
				}
			}
		}
		this.wtModel.setValues(chars2add);
		this.setPropsEditable(this.editableSorts.contains(this.selectedTypeSort));
	}

	public void showNoSelection() {
		this.wtModel.clear();
		this.setPropsEditable(false);
	}

	void item_stateChanged(final ItemEvent e) {
		this.selectedTypeSort = (CharacteristicTypeSort) e.getItem();
		this.setPropsEditable(this.editableSorts.contains(this.selectedTypeSort));
		this.elementSelected(this.selectedTypeSort);
	}

	void setCharacteristicValue(final Collection<Characteristic> characteristics, final String name, final String value) {
		for (final Characteristic ch : characteristics) {
			if (ch.getName().equals(name)) {
				ch.setValue(value);
				break;
			}
		}
	}

	Collection<Characteristic> getCharacteristics() {
		final CharacterizableObject obj = this.typeSortsCharacterizedIds.get(this.selectedTypeSort);
		if (obj == null) {
			assert Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort " + this.selectedTypeSort, Level.FINER); //$NON-NLS-1$
			return null;
		}
		final Identifier characterizedId = obj.characterizableId;
		final Collection<Characteristic> chars = this.characteristics.get(characterizedId);
		final Collection<Characteristic> newChars = this.addedCharacteristics.get(obj);
		if (newChars == null) {
			return chars;
		}
		final Collection<Characteristic> allChars = new ArrayList<Characteristic>(chars.size() + newChars.size());
		allChars.addAll(chars);
		allChars.addAll(newChars);
		return allChars;
	}

	class PropsADToolBar extends JPanel {
		private static final long serialVersionUID = 3544392491714752818L;
		final JButton addButton = new JButton();
		final JButton deleteButton = new JButton();
		final JButton commitButton = new JButton();

		public PropsADToolBar() {
			
			try {
				this.jbInit();
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
				public void actionPerformed(final ActionEvent e) {
					if (CharacteristicsPanel.this.selectedTypeSort == null) {
						return;
					}
					final CharacterizableObject object = CharacteristicsPanel.this.typeSortsCharacterizedIds.get(CharacteristicsPanel.this.selectedTypeSort);
					if (object == null) {
						assert Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort "  //$NON-NLS-1$
										+ CharacteristicsPanel.this.selectedTypeSort, Level.FINER);
						return;
					}

					final CharacteristicAddDialog frame = new CharacteristicAddDialog(Environment.getActiveWindow(), "Add characteristic");
					if (frame.showDialog(CharacteristicsPanel.this.selectedTypeSort, CharacteristicsPanel.this.wtModel.getValues()) == JOptionPane.OK_OPTION) {
						final CharacteristicType type = frame.getCharacteristicType();
						final Identifier userId = LoginManager.getUserId();

						if (object != null) {
							final Characterizable characterizable = object.characterizable;

							try {
								final Characteristic ch = Characteristic.createInstance(userId,
										type,
										type.getName(),
										type.getDescription(),
										"",
										characterizable,
										true,
										true);
								List<Characteristic> added = CharacteristicsPanel.this.addedCharacteristics.get(object);
								if (added == null) {
									added = new LinkedList<Characteristic>();
									CharacteristicsPanel.this.addedCharacteristics.put(object, added);
								}
								added.add(ch);

								final int n = CharacteristicsPanel.this.wtModel.addObject(ch);
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
				public void actionPerformed(final ActionEvent e) {

					final int n = CharacteristicsPanel.this.wTable.getSelectedRow();
					if (n == -1) {
						return;
					}

					if (CharacteristicsPanel.this.selectedTypeSort == null) {
						return;
					}
					final CharacterizableObject obj = CharacteristicsPanel.this.typeSortsCharacterizedIds.get(CharacteristicsPanel.this.selectedTypeSort);
					if (obj == null) {
						assert Log.debugMessage("CharacterizedObject not set for CharacteristicTypeSort " //$NON-NLS-1$
										+ CharacteristicsPanel.this.selectedTypeSort, Level.FINER);
						return;
					}

					final Characteristic characteristic = CharacteristicsPanel.this.wtModel.getObject(n);

					final List<Characteristic> added = CharacteristicsPanel.this.addedCharacteristics.get(obj);
					if (added != null && added.contains(characteristic)) {
						added.remove(characteristic);
						if (added.isEmpty()) {
							CharacteristicsPanel.this.addedCharacteristics.remove(obj);
						}
					} else {
						List<Characteristic> removed = CharacteristicsPanel.this.removedCharacteristics.get(obj);
						if (removed == null) {
							removed = new LinkedList<Characteristic>();
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
				public void actionPerformed(final ActionEvent e) {
					CharacteristicsPanel.this.commitChanges();
				}
			});

			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(this.addButton);
			this.add(this.deleteButton);
			this.add(Box.createHorizontalGlue());
			this.add(this.commitButton);
		}

		public void setAddButtonEnabled(final boolean b) {
			this.addButton.setEnabled(b);
		}

		public void setCancelButtonEnabled(final boolean b) {
			this.deleteButton.setEnabled(b);
		}

		public void setCommitButtonEnabled(final boolean b) {
			this.commitButton.setEnabled(b);
		}
	}
}
