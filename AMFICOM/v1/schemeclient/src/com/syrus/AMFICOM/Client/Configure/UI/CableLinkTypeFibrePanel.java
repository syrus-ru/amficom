package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Scheme.ThreadTypeCell;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;

public class CableLinkTypeFibrePanel extends GeneralPanel {
	protected CableLinkType clt;

	JPanel linksPanel = new JPanel();
	JLabel linksTypeLabel = new JLabel();
	JLabel linksMarkLabel = new JLabel();
	JLabel linksNameLabel = new JLabel();
	JLabel linksNumberLabel = new JLabel();
	JLabel threadColorLabel = new JLabel();
	JLabel linksIdLabel = new JLabel();
	ObjComboBox linksTypeBox;
	JTextArea linksMarkArea = new JTextArea();
	JTextField linksNameField = new JTextField();
	JTextField linksNumberField = new JTextField();
	JButton linksNumberButton = new JButton();
	JComboBox colorComboBox = new JComboBox();
	JButton colorButton = new JButton();
	JPanel listPanel = new JPanel();
	JScrollPane jScrollPane1 = new JScrollPane();
	ObjList threadsList = new ObjList(CableThreadTypeController.getInstance(),
			StorableObjectWrapper.COLUMN_NAME);
	static JColorChooser tcc;
	
	CableLinkTypeLayout layout = new CableLinkTypeLayout();

	Color[] defaultColors = new Color[] { Color.WHITE, Color.BLUE, Color.GREEN, Color.RED,
			Color.GRAY, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK,
			Color.YELLOW, Color.BLACK };

	protected CableLinkTypeFibrePanel() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected CableLinkTypeFibrePanel(LinkType clt) {
		this();
		setObject(clt);
	}

	private void jbInit() throws Exception {
		EquivalentCondition condition = new EquivalentCondition(
				ObjectEntities.LINKTYPE_ENTITY_CODE);
		List lTypes = new ArrayList(ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true)); 

		linksTypeBox = new ObjComboBox(LinkTypeController.getInstance(),
				lTypes,
				StorableObjectWrapper.COLUMN_NAME);
		setName(LangModelConfig.getString("label_fibers"));

		this.setLayout(new GridBagLayout());

		linksTypeLabel.setText(LangModelConfig.getString("label_type"));
		linksTypeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksMarkLabel.setText(LangModelConfig.getString("label_mark"));
		linksMarkLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNameLabel.setText(LangModelConfig.getString("label_name"));
		linksNameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNumberLabel.setText(LangModelConfig.getString("label_linksnumber"));
		linksNumberLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNumberButton.setText(LangModelConfig
				.getString("label_linksnumberbutton"));
		linksNumberButton.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNumberButton.addActionListener(new LinkNumberActionListener());
		threadColorLabel.setText(LangModelConfig.getString("label_threadColor"));
		threadColorLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		linksIdLabel.setText(LangModelConfig.getString("label_id"));
		linksIdLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		listPanel.setLayout(new BorderLayout());

		colorButton.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		colorButton.setText(LangModelConfig.getString("label_addColor"));
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorButton_actionPerformed(e);
			}
		});
		colorComboBox.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		colorComboBox.setFocusable(false);
		colorComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				colorComboBox_itemStateChanged(e);
			}
		});
		colorComboBox.setRenderer(new ListColorRenderer());
		for (int i = 0; i < defaultColors.length; i++)
			colorComboBox.addItem(defaultColors[i]);

		jScrollPane1.setPreferredSize(new Dimension(145, 125));
		jScrollPane1.setMinimumSize(new Dimension(145, 125));
		// jScrollPane1.setMaximumSize(new Dimension(145, 125));
		// jScrollPane1.setSize(new Dimension(145, 125));
		threadsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		threadsList.setBorder(BorderFactory.createLoweredBevelBorder());
		threadsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				threadsList_valueChanged(e);
			}
		});

		jScrollPane1.getViewport();
		jScrollPane1.getViewport().add(threadsList, null);
		listPanel.add(jScrollPane1, BorderLayout.CENTER);

		linksPanel.setLayout(new GridBagLayout());

		linksPanel.add(linksNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksMarkLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.4,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksTypeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(threadColorLabel, new GridBagConstraints(0, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0,
						0, 0), 0, 0));
		linksPanel.add(linksNumberLabel, new GridBagConstraints(0, 4, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0,
						0, 0), 0, 0));

		if (Environment.isDebugMode())
			linksPanel.add(linksIdLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0,
							0), 0, 0));

		linksPanel.add(linksNameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
						0, 0, 0), 0, 0));
		JScrollPane scrollPane = new JScrollPane(linksMarkArea);
		linksPanel.add(scrollPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.4,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0,
						0), 0, 0));
		linksPanel.add(linksTypeBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
						0, 0, 0), 0, 0));

		JPanel p0 = new JPanel(new BorderLayout());
		p0.add(colorComboBox, BorderLayout.CENTER);
		p0.add(colorButton, BorderLayout.EAST);
		linksPanel.add(p0, new GridBagConstraints(1, 3, 1, 1, 0.8, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
						0, 0, 0), 0, 0));

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(linksNumberField, BorderLayout.CENTER);
		p1.add(linksNumberButton, BorderLayout.EAST);
		linksPanel.add(p1, new GridBagConstraints(1, 4, 1, 1, 0.8, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
						0, 0, 0), 0, 0));

		this.add(listPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(linksPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0,
						0), 0, 0));

		linksPanel.add(layout.getPanel(), new GridBagConstraints(1, 5, 1, 1, 1.0,
				1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		SelectionListener selectionListener = new SelectionListener();
		layout.getInternalDispatcher().register(selectionListener,
				SchemeNavigateEvent.type);
	}

	class SelectionListener implements OperationListener {
		/**
		 * @param e
		 * @see com.syrus.AMFICOM.Client.General.Event.OperationListener#operationPerformed(com.syrus.AMFICOM.Client.General.Event.OperationEvent)
		 */
		public void operationPerformed(OperationEvent e) {
			if (e.getActionCommand().equals(SchemeNavigateEvent.type)) {
				SchemeNavigateEvent ev = (SchemeNavigateEvent) e;
				if (ev.OTHER_OBJECT_SELECTED) {
					Object[] objs = (Object[]) ev.getSource();
					for (int i = 0; i < objs.length; i++) {
						if (objs[i] instanceof ThreadTypeCell) {
							CableThreadType type = ((ThreadTypeCell) objs[i])
									.getCableThreadType();
							threadsList.setSelectedValue(type, true);
						}
					}
				}
			}
		}
	}

	class LinkNumberActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				int num = Integer.parseInt(linksNumberField.getText());
				if (num < 0)
					throw new NumberFormatException();

				int old_num = clt.getCableThreadTypes().size();
				if (num > old_num) {
					LinkType link_type = (LinkType) linksTypeBox.getSelectedItem();
					if (link_type == null)
						return;
					String codename = link_type.getCodename();
					String name = linksNameField.getText();
					if (name.equals("")) {
						if (old_num > 0) {
							CableThreadType ctt = (CableThreadType) clt.getCableThreadTypes()
									.get(0);
							name = ctt.getName();
						} else
							name = "fiber";
					}
					name = MiscUtil.removeDigitsFromString(name);

					Identifier user_id = new Identifier(((RISDSessionInfo) aContext
							.getSessionInterface()).getAccessIdentifier().user_id);
					try {
						List newCableThreadTypes = new ArrayList(num);
						newCableThreadTypes.addAll(clt.getCableThreadTypes());

						for (int i = old_num; i < num; i++) {
							CableThreadType newctt = CableThreadType
									.createInstance(user_id, codename, "", name + (i + 1),
											Color.BLACK.getRGB(), link_type);
							newCableThreadTypes.add(newctt);
							try {
								ConfigurationStorableObjectPool.putStorableObject(newctt);
							} catch (IllegalObjectEntityException e1) {
								throw new CreateObjectException(
										"Exception while creating CableLinkType by "
												+ e1.getMessage());
							}
						}
						clt.setCableThreadTypes(newCableThreadTypes);
					} catch (CreateObjectException ex) {
						ex.printStackTrace();
					}
				} else if (num < old_num) {
					List newCableThreadTypes = new ArrayList(num);
					newCableThreadTypes.addAll(clt.getCableThreadTypes());
					List toDelete = new LinkedList();

					for (int i = old_num - 1; i >= num; i--) {
						// CableTypeThread ctt = (CableTypeThread)clt.cable_threads.get(i);
						CableThreadType ctt = (CableThreadType) clt.getCableThreadTypes()
								.get(i);
						toDelete.add(ctt.getId());
						newCableThreadTypes.remove(ctt);
					}
					clt.setCableThreadTypes(newCableThreadTypes);
					try {
						ConfigurationStorableObjectPool.delete(toDelete);
					} catch (ApplicationException ex) {
						ex.printStackTrace();
					}
				} else
					modify();
				setObject(clt);
			} catch (NumberFormatException ex) {
				linksNumberField.setText(String.valueOf(clt.getCableThreadTypes()
						.size()));
			}
		}
		/*
		 * private int parseName(String name) { if (name.length() == 0) return 0;
		 * char[] ch = new char[name.length()]; name.getChars(0, name.length(), ch,
		 * 0); int i = ch.length - 1; while (Character.isDigit(ch[i])) i--; try {
		 * return Integer.parseInt(name.subSequence(i+1, name.length())); } catch
		 * (NumberFormatException ex) { return 0; } }
		 */
	}

	public Object getObject() {
		return clt;
	}

	public void setObject(Object or) {
		this.clt = (CableLinkType) or;

		this.threadsList.removeAll();
		this.linksNameField.setText("");
		this.linksNumberField.setText(String.valueOf(clt.getCableThreadTypes()
				.size()));
		this.linksMarkArea.setText("");
		// this.linksTypeBox.setSelectedItem(null);

		if (clt != null)
			threadsList.addElements(clt.getCableThreadTypes());
		layout.setObject(or);
	}

	public boolean modify() {
		try {
			CableThreadType ctt = (CableThreadType) threadsList.getSelectedValue();
			if (clt != null) {
				ctt.setName(linksNameField.getText());
				ctt.setDescription(this.linksMarkArea.getText());
				ctt.setLinkType((LinkType) this.linksTypeBox.getSelectedItem());
			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	void colorComboBox_itemStateChanged(ItemEvent e) {
		Color newColor = (Color)e.getItem();
		/**
		 * fix colored drop button in not Windows LAF 
		 */
		Color tmp = colorComboBox.getComponents()[0].getBackground();
		colorComboBox.setBackground(newColor);
		colorComboBox.getComponents()[0].setBackground(tmp);
		
		CableThreadType type = (CableThreadType)threadsList.getSelectedValue();
		if (type != null) {
			type.setColor(newColor.getRGB());
			layout.getInternalDispatcher().notify(
					new SchemeNavigateEvent(new Object[] { layout.getCell(type) },
							SchemeNavigateEvent.OTHER_OBJECT_SELECTED_EVENT));
		}
	}
	
	void colorButton_actionPerformed(ActionEvent e) {
		if (tcc == null)
			tcc = new JColorChooser((Color)colorComboBox.getSelectedItem());
		else 
			tcc.setColor((Color)colorComboBox.getSelectedItem());
		
		int res = JOptionPane.showOptionDialog(this, tcc, LangModelConfig.getString("label_chooseColor"), 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (res == JOptionPane.OK_OPTION) {
			Color newColor = tcc.getColor();
			if (isConatainsColor(newColor.getRGB())) {
					colorComboBox.setSelectedItem(newColor);
					return;
				}
			colorComboBox.addItem(newColor);
			colorComboBox.setSelectedItem(newColor);
		}
	}
	
	boolean isConatainsColor(int color) {
		for (int i = 0; i < colorComboBox.getItemCount(); i++)
			if (((Color)colorComboBox.getItemAt(i)).getRGB() == color)
				return true;
		return false;
	}
	
	
	void threadsList_valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;

		CableThreadType ctt = (CableThreadType) this.threadsList.getSelectedValue();
		if (ctt != null) {
			this.linksNameField.setText(ctt.getName());
			this.linksMarkArea.setText(ctt.getDescription());
			this.linksTypeBox.setSelectedItem(ctt.getLinkType());
			Color newColor = new Color(ctt.getColor());
			if (!isConatainsColor(ctt.getColor()))
				this.colorComboBox.addItem(newColor);
			this.colorComboBox.setSelectedItem(newColor);

			layout.getInternalDispatcher().notify(
					new SchemeNavigateEvent(new Object[] { layout.getCell(ctt) },
							SchemeNavigateEvent.OTHER_OBJECT_SELECTED_EVENT));
		}
	}

	static String EMPTY_STRING = " ";

	class ListColorRenderer extends JLabel implements ListCellRenderer {

		public ListColorRenderer() {
			setOpaque(true); // MUST do this for background to show up.
		}

		public Component getListCellRendererComponent(JList list, Object color,
				int index, boolean isSelected, boolean hasFocus) {
			Color newColor = (Color) color;
			super.setBackground(newColor);
			setText(EMPTY_STRING);

			setToolTipText("RGB value: " + newColor.getRed() + ", "
					+ newColor.getGreen() + ", " + newColor.getBlue());
			return this;
		}
	}
}
