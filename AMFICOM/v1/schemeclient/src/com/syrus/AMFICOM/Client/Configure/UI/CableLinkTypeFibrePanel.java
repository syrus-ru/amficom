package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Scheme.ThreadTypeCell;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;

public class CableLinkTypeFibrePanel extends GeneralPanel {
	protected CableLinkType clt;

	JPanel pnMainPanel = new JPanel();
	ObjList lsThreadsList  = new ObjList(CableThreadTypeController.getInstance(),
		StorableObjectWrapper.COLUMN_NAME);
	JLabel lbNameLabel = new JLabel(LangModelConfig.getString("label_name"));
	JTextField tfNameText = new JTextField();
	JLabel lbDescrLabel = new JLabel(LangModelConfig.getString("label_mark"));
	JTextArea taDescrArea = new JTextArea(2,10);
	JLabel lbTypeLabel = new JLabel(LangModelConfig.getString("label_type"));
	JComboBox cmbTypeCombo;
	JLabel lbColorLabel = new JLabel(LangModelConfig.getString("label_threadColor"));
	JComboBox cmbColorCombo = new JComboBox();
	JButton btColorBut = new JButton(LangModelConfig.getString("label_addColor"));
	JLabel lbNumberLabel = new JLabel(LangModelConfig.getString("label_linksnumber"));
	JTextField tfNumberText = new JTextField( );
	JButton btNumberBut = new JButton(LangModelConfig.getString("label_linksnumberbutton"));

	static JColorChooser tcc;
	CableLinkTypeLayout layout = new CableLinkTypeLayout();
	static Color[] defaultColors = new Color[] { Color.WHITE, Color.BLUE, Color.GREEN, Color.RED,
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
		cmbTypeCombo = new ObjComboBox(LinkTypeController.getInstance(),
				lTypes,
				StorableObjectWrapper.COLUMN_NAME);
		
		GridBagLayout gbMainPanel = new GridBagLayout();
		GridBagConstraints gbcMainPanel = new GridBagConstraints();
		pnMainPanel.setLayout( gbMainPanel );

		JScrollPane scpThreadsList = new JScrollPane( lsThreadsList );
		lsThreadsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsThreadsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				threadsList_valueChanged(e);
			}
		});
		scpThreadsList.setPreferredSize(new Dimension(100, 100));
		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 0;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 8;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 1;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( scpThreadsList, gbcMainPanel );
		pnMainPanel.add( scpThreadsList );

		lbNameLabel.setFocusable( false );
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 0;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( lbNameLabel, gbcMainPanel );
		pnMainPanel.add( lbNameLabel );

		gbcMainPanel.gridx = 4;
		gbcMainPanel.gridy = 0;
		gbcMainPanel.gridwidth = 3;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( tfNameText, gbcMainPanel );
		pnMainPanel.add( tfNameText );

		lbDescrLabel.setFocusable( false );
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 1;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( lbDescrLabel, gbcMainPanel );
		pnMainPanel.add( lbDescrLabel );

		JScrollPane scpDescrArea = new JScrollPane( taDescrArea );
		scpDescrArea.setPreferredSize(new Dimension(50, 100));
		gbcMainPanel.gridx = 4;
		gbcMainPanel.gridy = 1;
		gbcMainPanel.gridwidth = 3;
		gbcMainPanel.gridheight = 2;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( scpDescrArea, gbcMainPanel );
		pnMainPanel.add( scpDescrArea );

		lbTypeLabel.setFocusable( false );
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 3;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( lbTypeLabel, gbcMainPanel );
		pnMainPanel.add( lbTypeLabel );

		cmbTypeCombo.setPreferredSize(cmbColorCombo.getPreferredSize());
		gbcMainPanel.gridx = 4;
		gbcMainPanel.gridy = 3;
		gbcMainPanel.gridwidth = 3;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( cmbTypeCombo, gbcMainPanel );
		pnMainPanel.add( cmbTypeCombo );

		lbColorLabel.setFocusable( false );
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 4;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( lbColorLabel, gbcMainPanel );
		pnMainPanel.add( lbColorLabel );

		cmbColorCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				colorComboBox_itemStateChanged(e);
			}
		});
		cmbColorCombo.setRenderer(ColorListCellRenderer.getInstance());
		for (int i = 0; i < defaultColors.length; i++)
			cmbColorCombo.addItem(defaultColors[i]);
		gbcMainPanel.gridx = 4;
		gbcMainPanel.gridy = 4;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( cmbColorCombo, gbcMainPanel );
		pnMainPanel.add( cmbColorCombo );

		btColorBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorButton_actionPerformed(e);
			}
		});
		gbcMainPanel.gridx = 6;
		gbcMainPanel.gridy = 4;
		gbcMainPanel.gridwidth = 1;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( btColorBut, gbcMainPanel );
		pnMainPanel.add( btColorBut );

		lbNumberLabel.setFocusable( false );
		gbcMainPanel.gridx = 2;
		gbcMainPanel.gridy = 5;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( lbNumberLabel, gbcMainPanel );
		pnMainPanel.add( lbNumberLabel );

		gbcMainPanel.gridx = 4;
		gbcMainPanel.gridy = 5;
		gbcMainPanel.gridwidth = 2;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( tfNumberText, gbcMainPanel );
		pnMainPanel.add( tfNumberText );

		btNumberBut.addActionListener(new LinkNumberActionListener());
		gbcMainPanel.gridx = 6;
		gbcMainPanel.gridy = 5;
		gbcMainPanel.gridwidth = 1;
		gbcMainPanel.gridheight = 1;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 0;
		gbcMainPanel.weighty = 0;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( btNumberBut, gbcMainPanel );
		pnMainPanel.add( btNumberBut );

		JPanel pnLayoutPanel = layout.getPanel();
		JScrollPane scpLayoutPanel = new JScrollPane( pnLayoutPanel );
		scpLayoutPanel.setPreferredSize(new Dimension(100, 100));
		gbcMainPanel.gridx = 4;
		gbcMainPanel.gridy = 6;
		gbcMainPanel.gridwidth = 3;
		gbcMainPanel.gridheight = 2;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.weightx = 1;
		gbcMainPanel.weighty = 1;
		gbcMainPanel.anchor = GridBagConstraints.NORTH;
		gbMainPanel.setConstraints( scpLayoutPanel, gbcMainPanel );
		pnMainPanel.add( scpLayoutPanel );

		SelectionListener selectionListener = new SelectionListener();
		layout.getInternalDispatcher().register(selectionListener,
				SchemeNavigateEvent.type);
		
		this.setLayout(new BorderLayout());
		this.add(pnMainPanel, BorderLayout.CENTER);
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
							lsThreadsList.setSelectedValue(type, true);
						}
					}
				}
			}
		}
	}

	class LinkNumberActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				int num = Integer.parseInt(tfNumberText.getText());
				if (num < 0)
					throw new NumberFormatException();

				int old_num = clt.getCableThreadTypes().size();
				if (num > old_num) {
					LinkType link_type = (LinkType) cmbTypeCombo.getSelectedItem();
					if (link_type == null)
						return;
					String codename = link_type.getCodename();
					String name = tfNameText.getText();
					if (name.equals("")) {
						if (old_num > 0) {
							CableThreadType ctt = (CableThreadType) clt
									.getCableThreadTypes()
									.iterator()
									.next();
							name = ctt.getName();
						} 
						else
							name = "fiber";
					}
					name = MiscUtil.removeDigitsFromString(name);

					Identifier user_id = new Identifier(((RISDSessionInfo) aContext
							.getSessionInterface()).getAccessIdentifier().user_id);
					try {
						for (int i = old_num; i < num; i++) {
							CableThreadType newctt = CableThreadType
									.createInstance(user_id, codename, "", name + (i + 1),
											Color.BLACK.getRGB(), link_type, clt);
							try {
								ConfigurationStorableObjectPool.putStorableObject(newctt);
							} 
							catch (IllegalObjectEntityException e1) {
								throw new CreateObjectException(
										"Exception while creating CableLinkType by " + e1.getMessage());
							}
						}
					} 
					catch (CreateObjectException ex) {
						ex.printStackTrace();
					}
				} 
				else if (num < old_num) {
					Set toDelete = new HashSet();
					/**
					 * TODO! make sort by namr and remove from the end   
					 */
					Iterator it = clt.getCableThreadTypes().iterator();
					for (int i = old_num - 1; i >= num; i--) {
						CableThreadType ctt = (CableThreadType)it.next(); 
						toDelete.add(ctt.getId());
					}
					try {
						ConfigurationStorableObjectPool.delete(toDelete);
					} 
					catch (ApplicationException ex) {
						ex.printStackTrace();
					}
				} 
				else
					modify();
				setObject(clt);
			} 
			catch (NumberFormatException ex) {
				tfNumberText.setText(String.valueOf(clt.getCableThreadTypes().size()));
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

		this.lsThreadsList.removeAll();
		this.tfNameText.setText("");
		this.tfNumberText.setText(String.valueOf(clt.getCableThreadTypes()
				.size()));
		this.taDescrArea.setText("");
		// this.linksTypeBox.setSelectedItem(null);

		if (clt != null) {
			lsThreadsList.addElements(clt.getCableThreadTypes());
			if (!clt.getCableThreadTypes().isEmpty())
				lsThreadsList.setSelectedIndex(0);
		}
		layout.setObject(or);
	}

	public boolean modify() {
		try {
			CableThreadType ctt = (CableThreadType) lsThreadsList.getSelectedValue();
			if (ctt != null) {
				ctt.setName(tfNameText.getText());
				ctt.setDescription(this.taDescrArea.getText());
				ctt.setLinkType((LinkType) this.cmbTypeCombo.getSelectedItem());
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
		Color tmp = cmbColorCombo.getComponents()[0].getBackground();
		cmbColorCombo.setBackground(newColor);
		cmbColorCombo.getComponents()[0].setBackground(tmp);
		
		CableThreadType type = (CableThreadType)lsThreadsList.getSelectedValue();
		if (type != null) {
			type.setColor(newColor.getRGB());
			layout.getInternalDispatcher().notify(
					new SchemeNavigateEvent(new Object[] { layout.getCell(type) },
							SchemeNavigateEvent.OTHER_OBJECT_SELECTED_EVENT));
		}
	}
	
	void colorButton_actionPerformed(ActionEvent e) {
		if (tcc == null)
			tcc = new JColorChooser((Color)cmbColorCombo.getSelectedItem());
		else 
			tcc.setColor((Color)cmbColorCombo.getSelectedItem());
		
		int res = JOptionPane.showOptionDialog(this, tcc, LangModelConfig.getString("label_chooseColor"), 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (res == JOptionPane.OK_OPTION) {
			Color newColor = tcc.getColor();
			if (isConatainsColor(newColor.getRGB())) {
					cmbColorCombo.setSelectedItem(newColor);
					return;
				}
			cmbColorCombo.addItem(newColor);
			cmbColorCombo.setSelectedItem(newColor);
		}
	}
	
	boolean isConatainsColor(int color) {
		for (int i = 0; i < cmbColorCombo.getItemCount(); i++)
			if (((Color)cmbColorCombo.getItemAt(i)).getRGB() == color)
				return true;
		return false;
	}
	
	
	void threadsList_valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;

		CableThreadType ctt = (CableThreadType) this.lsThreadsList.getSelectedValue();
		if (ctt != null) {
			this.tfNameText.setText(ctt.getName());
			this.taDescrArea.setText(ctt.getDescription());
			this.cmbTypeCombo.setSelectedItem(ctt.getLinkType());
			Color newColor = new Color(ctt.getColor());
			if (!isConatainsColor(ctt.getColor()))
				this.cmbColorCombo.addItem(newColor);
			this.cmbColorCombo.setSelectedItem(newColor);

			layout.getInternalDispatcher().notify(
					new SchemeNavigateEvent(new Object[] { layout.getCell(ctt) },
							SchemeNavigateEvent.OTHER_OBJECT_SELECTED_EVENT));
		}
	}
}
