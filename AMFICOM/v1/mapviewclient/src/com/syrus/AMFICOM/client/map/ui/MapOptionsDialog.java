package com.syrus.AMFICOM.client.map.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.LineThicknessComboBox;

public class MapOptionsDialog extends JDialog 
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private GridBagLayout gridBagLayout4 = new GridBagLayout();
	private GridBagLayout gridBagLayout5 = new GridBagLayout();
	private GridBagLayout gridBagLayout7 = new GridBagLayout();
	private GridBagLayout gridBagLayout6 = new GridBagLayout();

	private JPanel visualPropsPanel = new JPanel();
	private JPanel selectionPropsPanel = new JPanel();
	private JPanel alarmedPropsPanel = new JPanel();
	private JPanel textPropsPanel = new JPanel();
	private JPanel unboundPropsPanel = new JPanel();
	private JPanel showModesPanel = new JPanel();
	private JPanel buttonsPanel = new JPanel();

	private JSeparator jSeparator1 = new JSeparator();
	private JSeparator jSeparator2 = new JSeparator();
	private JSeparator jSeparator3 = new JSeparator();
	private JSeparator jSeparator4 = new JSeparator();
	private JSeparator jSeparator5 = new JSeparator();
	private JSeparator jSeparator6 = new JSeparator();

	private JLabel thicknessLanel = new JLabel();
	private LineThicknessComboBox thicknessComboBox = new LineThicknessComboBox();
	private JLabel colorLabel = new JLabel();
	private ColorChooserComboBox colorComboBox = new ColorChooserComboBox();
	private JLabel styleLabel = new JLabel();
	private JComboBox styleComboBox = new JComboBox();

	private JLabel selectionThicknessLabel = new JLabel();
	private LineThicknessComboBox selectionThicknessComboBox = new LineThicknessComboBox();
	private JLabel selectionColorLabel = new JLabel();
	private ColorChooserComboBox selectionColorComboBox = new ColorChooserComboBox();
	private JLabel selectionStyleLabel = new JLabel();
	private JComboBox selectionStyleComboBox = new JComboBox();
	private JLabel firstSelectionColorLabel = new JLabel();
	private ColorChooserComboBox firstSelectionColorComboBox = new ColorChooserComboBox();
	private JLabel secondSelectionColorLabel = new JLabel();
	private ColorChooserComboBox secondSelectionColorComboBox = new ColorChooserComboBox();

	private JLabel alarmedThicknessLabel = new JLabel();
	private LineThicknessComboBox alarmedThicknessComboBox = new LineThicknessComboBox();
	private JLabel alarmedColorLabel = new JLabel();
	private ColorChooserComboBox alarmedColorComboBox = new ColorChooserComboBox();
	private JLabel alarmedStyleLabel = new JLabel();
	private JComboBox alarmedStyleComboBox = new JComboBox();

	private JLabel borderThicknessLanel = new JLabel();
	private LineThicknessComboBox borderThicknessComboBox = new LineThicknessComboBox();
	private JLabel borderColorLabel = new JLabel();
	private ColorChooserComboBox borderColorComboBox = new ColorChooserComboBox();
	private JLabel textColorLabel = new JLabel();
	private ColorChooserComboBox textColorComboBox = new ColorChooserComboBox();
	private JLabel backgroundColorLabel = new JLabel();
	private ColorChooserComboBox backgroundColorComboBox = new ColorChooserComboBox();
	private JLabel fontLabel = new JLabel();
	private JComboBox fontComboBox = new JComboBox();
	private JLabel metricLabel = new JLabel();
	private JTextField metricTextField = new JTextField();

	private JLabel unboundThicknessLanel = new JLabel();
	private LineThicknessComboBox unboundThicknessComboBox = new LineThicknessComboBox();
	private JLabel unboundLinkColorLabel = new JLabel();
	private ColorChooserComboBox unboundLinkColorComboBox = new ColorChooserComboBox();
	private JLabel unboundLinkPositionColorLabel = new JLabel();
	private ColorChooserComboBox unboundLinkPositionColorComboBox = new ColorChooserComboBox();
	private JLabel unboundElementColorLabel = new JLabel();
	private ColorChooserComboBox unboundElementColorComboBox = new ColorChooserComboBox();
	private JLabel canBindColorLabel = new JLabel();
	private ColorChooserComboBox canBindColorComboBox = new ColorChooserComboBox();
	private JLabel spareLabel = new JLabel();
	private JTextField spareTextField = new JTextField();

	private JCheckBox showLengthCheckBox = new JCheckBox();
	private JCheckBox showLinkNamesCheckBox = new JCheckBox();
	private JCheckBox showNodeNamesCheckBox = new JCheckBox();
	private JCheckBox showPhysicalNodesCheckBox = new JCheckBox();

	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();

	public static final int RET_OK = 1;
	
	protected int retCode = 0;

	public MapOptionsDialog()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		init();
	}

	private void jbInit()
	{
		this.setSize(new Dimension(850, 400));
		
		Dimension verticalSeparatorDimension = new Dimension(10, 1);
		Dimension horizontalSeparatorDimension = new Dimension(1, 10);
		Dimension fieldDimension = new Dimension(50, 24);

		this.getContentPane().setLayout(this.gridBagLayout1);
		this.visualPropsPanel.setLayout(this.gridBagLayout2);
		this.selectionPropsPanel.setLayout(this.gridBagLayout3);
		this.alarmedPropsPanel.setLayout(this.gridBagLayout4);
		this.textPropsPanel.setLayout(this.gridBagLayout5);
		this.unboundPropsPanel.setLayout(this.gridBagLayout6);
		this.showModesPanel.setLayout(this.gridBagLayout7);

		this.jSeparator1.setOrientation(SwingConstants.VERTICAL);
//		this.jSeparator1.setBounds(new Rectangle(95, 116, 10, 10));
		this.jSeparator1.setPreferredSize(verticalSeparatorDimension);
		this.jSeparator2.setOrientation(SwingConstants.VERTICAL);
		this.jSeparator2.setPreferredSize(verticalSeparatorDimension);
		this.jSeparator3.setOrientation(SwingConstants.HORIZONTAL);
		this.jSeparator3.setPreferredSize(horizontalSeparatorDimension);
		this.jSeparator4.setOrientation(SwingConstants.VERTICAL);
		this.jSeparator4.setPreferredSize(verticalSeparatorDimension);
		this.jSeparator5.setOrientation(SwingConstants.VERTICAL);
		this.jSeparator5.setPreferredSize(verticalSeparatorDimension);
		this.jSeparator6.setOrientation(SwingConstants.HORIZONTAL);
		this.jSeparator6.setPreferredSize(horizontalSeparatorDimension);

		this.thicknessComboBox.setPreferredSize(fieldDimension);
		this.colorComboBox.setPreferredSize(fieldDimension);
		this.styleComboBox.setPreferredSize(fieldDimension);

		this.selectionThicknessComboBox.setPreferredSize(fieldDimension);
		this.selectionColorComboBox.setPreferredSize(fieldDimension);
		this.selectionStyleComboBox.setPreferredSize(fieldDimension);
		this.firstSelectionColorComboBox.setPreferredSize(fieldDimension);
		this.secondSelectionColorComboBox.setPreferredSize(fieldDimension);

		this.alarmedThicknessComboBox.setPreferredSize(fieldDimension);
		this.alarmedColorComboBox.setPreferredSize(fieldDimension);
		this.alarmedStyleComboBox.setPreferredSize(fieldDimension);

		this.borderThicknessComboBox.setPreferredSize(fieldDimension);
		this.borderColorComboBox.setPreferredSize(fieldDimension);
		this.textColorComboBox.setPreferredSize(fieldDimension);
		this.backgroundColorComboBox.setPreferredSize(fieldDimension);
		this.fontComboBox.setPreferredSize(fieldDimension);
		this.metricTextField.setPreferredSize(fieldDimension);

		this.unboundThicknessComboBox.setPreferredSize(fieldDimension);
		this.unboundLinkColorComboBox.setPreferredSize(fieldDimension);
		this.unboundLinkPositionColorComboBox.setPreferredSize(fieldDimension);
		this.unboundElementColorComboBox.setPreferredSize(fieldDimension);
		this.canBindColorComboBox.setPreferredSize(fieldDimension);
		this.spareTextField.setPreferredSize(fieldDimension);

		this.thicknessLanel.setText("Толщина линии");
		this.colorLabel.setText("Цвет линии");
		this.selectionThicknessLabel.setText("Толщина линии выделения");
		this.selectionColorLabel.setText("Цвет линии выделения");
		this.selectionStyleLabel.setText("Стиль линии выделения");
		this.styleLabel.setText("Стиль линии");
		this.alarmedThicknessLabel.setText("Толщина линии (тревога)");
		this.alarmedColorLabel.setText("Цвет линии (тревога)");
		this.alarmedStyleLabel.setText("Стиль линии (тревога)");
		this.borderThicknessLanel.setText("Толщина рамки");
		this.borderColorLabel.setText("Цвет рамки");
		this.textColorLabel.setText("Цвет текста");
		this.backgroundColorLabel.setText("Цвет под текстом");
		this.fontLabel.setText("Шрифт");
		this.firstSelectionColorLabel.setText("Первый цвет выделения линии");
		this.secondSelectionColorLabel.setText("Второй цвет выделения линии");
		this.metricLabel.setText("Метрика");
		this.unboundThicknessLanel.setText("Толщина непроложенного кабеля");
		this.unboundLinkColorLabel.setText("Цвет непроложенного кабеля");
		this.unboundLinkPositionColorLabel.setText("Цвет линии с непривязанным кабелем");
		this.unboundElementColorLabel.setText("Цвет непривязанного элемента");
		this.canBindColorLabel.setText("Цвет режимя привязки элемента");
		this.spareLabel.setText("Запас по умолчанию при привязке, м");
		this.showLengthCheckBox.setText("Отображать длины");
		this.showLinkNamesCheckBox.setText("Отображать названия линий");
		this.showNodeNamesCheckBox.setText("Отображать названия узлов");
		this.showNodeNamesCheckBox.setActionCommand("showNodeNamesCheckBox");
		this.showPhysicalNodesCheckBox.setText("Отображать топологические узлы");

		this.okButton.setText("Применить");
		this.okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ok();
				}
			});
		this.cancelButton.setText("Отменить");
		this.cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					cancel();
				}
			});

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.visualPropsPanel.add(this.thicknessLanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.visualPropsPanel.add(this.thicknessComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.visualPropsPanel.add(this.colorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.visualPropsPanel.add(this.colorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.visualPropsPanel.add(this.styleLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.visualPropsPanel.add(this.styleComboBox, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.selectionThicknessLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.selectionThicknessComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.selectionColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.selectionColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.selectionStyleLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.selectionStyleComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.firstSelectionColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.firstSelectionColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.secondSelectionColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.selectionPropsPanel.add(this.secondSelectionColorComboBox, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.alarmedPropsPanel.add(this.alarmedThicknessLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.alarmedPropsPanel.add(this.alarmedThicknessComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.alarmedPropsPanel.add(this.alarmedColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.alarmedPropsPanel.add(this.alarmedColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.alarmedPropsPanel.add(this.alarmedStyleLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.alarmedPropsPanel.add(this.alarmedStyleComboBox, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.borderThicknessLanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.borderThicknessComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.borderColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.borderColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.textColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.textColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.backgroundColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.backgroundColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.fontLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.fontComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.metricLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.textPropsPanel.add(this.metricTextField, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundThicknessLanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundThicknessComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundLinkColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundLinkColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundLinkPositionColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundLinkPositionColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundElementColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.unboundElementColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.canBindColorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.canBindColorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.spareLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 50;
		constraints.ipady = 0;
		this.unboundPropsPanel.add(this.spareTextField, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.showModesPanel.add(this.showLengthCheckBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.showModesPanel.add(this.showLinkNamesCheckBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.showModesPanel.add(this.showNodeNamesCheckBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.showModesPanel.add(this.showPhysicalNodesCheckBox, constraints);

		this.buttonsPanel.add(this.okButton, null);
		this.buttonsPanel.add(this.cancelButton, null);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.visualPropsPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.jSeparator1, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.selectionPropsPanel, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.jSeparator2, constraints);

		constraints.gridx = 4;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.alarmedPropsPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 5;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.jSeparator3, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.textPropsPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.jSeparator4, constraints);

		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.unboundPropsPanel, constraints);

		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.jSeparator5, constraints);

		constraints.gridx = 4;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.showModesPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 5;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.jSeparator6, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 5;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.buttonsPanel, constraints);

	}
	
	private void init()
	{
		this.showLengthCheckBox.setSelected(MapPropertiesManager.isShowLength());
		this.showLinkNamesCheckBox.setSelected(MapPropertiesManager.isShowLinkNames());
		this.showNodeNamesCheckBox.setSelected(MapPropertiesManager.isShowNodesNames());
		this.showPhysicalNodesCheckBox.setSelected(MapPropertiesManager.isShowPhysicalNodes());
		
		this.metricTextField.setText(MapPropertiesManager.getMetric());
		this.spareTextField.setText(String.valueOf(MapPropertiesManager.getSpareLength()));

		this.thicknessComboBox.setSelectedValue(MapPropertiesManager.getThickness());
		this.colorComboBox.setSelectedItem(MapPropertiesManager.getColor());
//		this.styleComboBox.setSelected(MapPropertiesManager);
	
		this.selectionThicknessComboBox.setSelectedValue(MapPropertiesManager.getSelectionThickness());
		this.selectionColorComboBox.setSelectedItem(MapPropertiesManager.getSelectionColor());
//		this.selectionStyleComboBox.setSelected(MapPropertiesManager);
		this.firstSelectionColorComboBox.setSelectedItem(MapPropertiesManager.getFirstSelectionColor());
		this.secondSelectionColorComboBox.setSelectedItem(MapPropertiesManager.getSecondSelectionColor());
	
		this.alarmedThicknessComboBox.setSelectedValue(MapPropertiesManager.getAlarmedThickness());
		this.alarmedColorComboBox.setSelectedItem(MapPropertiesManager.getAlarmedColor());
//		this.alarmedStyleComboBox.setSelected(MapPropertiesManager);
	
		this.borderThicknessComboBox.setSelectedValue(MapPropertiesManager.getBorderThickness());
		this.borderColorComboBox.setSelectedItem(MapPropertiesManager.getBorderColor());
		this.textColorComboBox.setSelectedItem(MapPropertiesManager.getTextColor());
		this.backgroundColorComboBox.setSelectedItem(MapPropertiesManager.getTextBackground());
//		this.fontComboBox.setSelected(MapPropertiesManager);
	
		this.unboundThicknessComboBox.setSelectedValue(MapPropertiesManager.getUnboundThickness());
		this.unboundLinkColorComboBox.setSelectedItem(MapPropertiesManager.getUnboundLinkColor());
		this.unboundLinkPositionColorComboBox.setSelectedItem(MapPropertiesManager.getUnboundLinkPositionColor());
		this.unboundElementColorComboBox.setSelectedItem(MapPropertiesManager.getUnboundElementColor());
		this.canBindColorComboBox.setSelectedItem(MapPropertiesManager.getCanBindColor());
	}

	private void commit()
	{
		MapPropertiesManager.setShowLength(this.showLengthCheckBox.isSelected());
		MapPropertiesManager.setShowLinkNames(this.showLinkNamesCheckBox.isSelected());
		MapPropertiesManager.setShowNodesNames(this.showNodeNamesCheckBox.isSelected());
		MapPropertiesManager.setShowPhysicalNodes(this.showPhysicalNodesCheckBox.isSelected());

		MapPropertiesManager.setMetric(this.metricTextField.getText());
		
		try
		{
			MapPropertiesManager.setSpareLength(Double.parseDouble(this.spareTextField.getText()));
		}
		catch(Exception e)
		{
			System.out.println("Wrong number format");
			// cannot parse
		}

		MapPropertiesManager.setThickness(this.thicknessComboBox.getSelectedValue());
		MapPropertiesManager.setColor((Color )this.colorComboBox.getSelectedItem());
//		this.styleComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setSelectionThickness(this.selectionThicknessComboBox.getSelectedValue());
		MapPropertiesManager.setSelectionColor((Color )this.selectionColorComboBox.getSelectedItem());
//		this.selectionStyleComboBox.setSelected(MapPropertiesManager);
		MapPropertiesManager.setFirstSelectionColor((Color )this.firstSelectionColorComboBox.getSelectedItem());
		MapPropertiesManager.setSecondSelectionColor((Color )this.secondSelectionColorComboBox.getSelectedItem());
	
		MapPropertiesManager.setAlarmedThickness(this.alarmedThicknessComboBox.getSelectedValue());
		MapPropertiesManager.setAlarmedColor((Color )this.alarmedColorComboBox.getSelectedItem());
//		this.alarmedStyleComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setBorderThickness(this.borderThicknessComboBox.getSelectedValue());
		MapPropertiesManager.setBorderColor((Color )this.borderColorComboBox.getSelectedItem());
		MapPropertiesManager.setTextColor((Color )this.textColorComboBox.getSelectedItem());
		MapPropertiesManager.setTextBackground((Color )this.backgroundColorComboBox.getSelectedItem());
//		this.fontComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setUnboundThickness(this.unboundThicknessComboBox.getSelectedValue());
		MapPropertiesManager.setUnboundLinkColor((Color )this.unboundLinkColorComboBox.getSelectedItem());
		MapPropertiesManager.setUnboundLinkPositionColor((Color )this.unboundLinkPositionColorComboBox.getSelectedItem());
		MapPropertiesManager.setUnboundElementColor((Color )this.unboundElementColorComboBox.getSelectedItem());
		MapPropertiesManager.setCanBindColor((Color )this.canBindColorComboBox.getSelectedItem());
	}

	void ok()
	{
		commit();
		this.retCode = RET_OK;
		dispose();
	}

	void cancel()
	{
		dispose();
	}
	
	public int getReturnCode()
	{
		return this.retCode;
	}
}
