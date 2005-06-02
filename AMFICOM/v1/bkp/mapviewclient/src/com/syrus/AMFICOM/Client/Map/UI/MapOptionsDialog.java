package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.ColorComboBox;
import com.syrus.AMFICOM.Client.General.UI.LineThickComboBox;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;

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
	private LineThickComboBox thicknessComboBox = new LineThickComboBox();
	private JLabel colorLabel = new JLabel();
	private ColorComboBox colorComboBox = new ColorComboBox();
	private JLabel styleLabel = new JLabel();
	private JComboBox styleComboBox = new JComboBox();

	private JLabel selectionThicknessLabel = new JLabel();
	private LineThickComboBox selectionThicknessComboBox = new LineThickComboBox();
	private JLabel selectionColorLabel = new JLabel();
	private ColorComboBox selectionColorComboBox = new ColorComboBox();
	private JLabel selectionStyleLabel = new JLabel();
	private JComboBox selectionStyleComboBox = new JComboBox();
	private JLabel firstSelectionColorLabel = new JLabel();
	private ColorComboBox firstSelectionColorComboBox = new ColorComboBox();
	private JLabel secondSelectionColorLabel = new JLabel();
	private ColorComboBox secondSelectionColorComboBox = new ColorComboBox();

	private JLabel alarmedThicknessLabel = new JLabel();
	private LineThickComboBox alarmedThicknessComboBox = new LineThickComboBox();
	private JLabel alarmedColorLabel = new JLabel();
	private ColorComboBox alarmedColorComboBox = new ColorComboBox();
	private JLabel alarmedStyleLabel = new JLabel();
	private JComboBox alarmedStyleComboBox = new JComboBox();

	private JLabel borderThicknessLanel = new JLabel();
	private LineThickComboBox borderThicknessComboBox = new LineThickComboBox();
	private JLabel borderColorLabel = new JLabel();
	private ColorComboBox borderColorComboBox = new ColorComboBox();
	private JLabel textColorLabel = new JLabel();
	private ColorComboBox textColorComboBox = new ColorComboBox();
	private JLabel backgroundColorLabel = new JLabel();
	private ColorComboBox backgroundColorComboBox = new ColorComboBox();
	private JLabel fontLabel = new JLabel();
	private JComboBox fontComboBox = new JComboBox();
	private JLabel metricLabel = new JLabel();
	private JTextField metricTextField = new JTextField();

	private JLabel unboundThicknessLanel = new JLabel();
	private LineThickComboBox unboundThicknessComboBox = new LineThickComboBox();
	private JLabel unboundLinkColorLabel = new JLabel();
	private ColorComboBox unboundLinkColorComboBox = new ColorComboBox();
	private JLabel unboundLinkPositionColorLabel = new JLabel();
	private ColorComboBox unboundLinkPositionColorComboBox = new ColorComboBox();
	private JLabel unboundElementColorLabel = new JLabel();
	private ColorComboBox unboundElementColorComboBox = new ColorComboBox();
	private JLabel canBindColorLabel = new JLabel();
	private ColorComboBox canBindColorComboBox = new ColorComboBox();
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

		this.visualPropsPanel.add(this.thicknessLanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.visualPropsPanel.add(this.thicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.visualPropsPanel.add(this.colorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.visualPropsPanel.add(this.colorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.visualPropsPanel.add(this.styleLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.visualPropsPanel.add(this.styleComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		this.selectionPropsPanel.add(this.selectionThicknessLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.selectionPropsPanel.add(this.selectionThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.selectionPropsPanel.add(this.selectionColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.selectionPropsPanel.add(this.selectionColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.selectionPropsPanel.add(this.selectionStyleLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.selectionPropsPanel.add(this.selectionStyleComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.selectionPropsPanel.add(this.firstSelectionColorLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.selectionPropsPanel.add(this.firstSelectionColorComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.selectionPropsPanel.add(this.secondSelectionColorLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.selectionPropsPanel.add(this.secondSelectionColorComboBox, ReusedGridBagConstraints.get(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		this.alarmedPropsPanel.add(this.alarmedThicknessLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.alarmedPropsPanel.add(this.alarmedThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.alarmedPropsPanel.add(this.alarmedColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.alarmedPropsPanel.add(this.alarmedColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.alarmedPropsPanel.add(this.alarmedStyleLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.alarmedPropsPanel.add(this.alarmedStyleComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		this.textPropsPanel.add(this.borderThicknessLanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.textPropsPanel.add(this.borderThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.textPropsPanel.add(this.borderColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.textPropsPanel.add(this.borderColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.textPropsPanel.add(this.textColorLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.textPropsPanel.add(this.textColorComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.textPropsPanel.add(this.backgroundColorLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.textPropsPanel.add(this.backgroundColorComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.textPropsPanel.add(this.fontLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.textPropsPanel.add(this.fontComboBox, ReusedGridBagConstraints.get(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.textPropsPanel.add(this.metricLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.textPropsPanel.add(this.metricTextField, ReusedGridBagConstraints.get(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		this.unboundPropsPanel.add(this.unboundThicknessLanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.unboundPropsPanel.add(this.unboundThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.unboundPropsPanel.add(this.unboundLinkColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.unboundPropsPanel.add(this.unboundLinkColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.unboundPropsPanel.add(this.unboundLinkPositionColorLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.unboundPropsPanel.add(this.unboundLinkPositionColorComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.unboundPropsPanel.add(this.unboundElementColorLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.unboundPropsPanel.add(this.unboundElementColorComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.unboundPropsPanel.add(this.canBindColorLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.unboundPropsPanel.add(this.canBindColorComboBox, ReusedGridBagConstraints.get(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		this.unboundPropsPanel.add(this.spareLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.unboundPropsPanel.add(this.spareTextField, ReusedGridBagConstraints.get(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		this.showModesPanel.add(this.showLengthCheckBox, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.showModesPanel.add(this.showLinkNamesCheckBox, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.showModesPanel.add(this.showNodeNamesCheckBox, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.showModesPanel.add(this.showPhysicalNodesCheckBox, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.buttonsPanel.add(this.okButton, null);
		this.buttonsPanel.add(this.cancelButton, null);

		this.getContentPane().add(this.visualPropsPanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(this.jSeparator1, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(this.selectionPropsPanel, ReusedGridBagConstraints.get(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(this.jSeparator2, ReusedGridBagConstraints.get(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(this.alarmedPropsPanel, ReusedGridBagConstraints.get(4, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(this.jSeparator3, ReusedGridBagConstraints.get(0, 1, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(this.textPropsPanel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(this.jSeparator4, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(this.unboundPropsPanel, ReusedGridBagConstraints.get(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(this.jSeparator5, ReusedGridBagConstraints.get(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(this.showModesPanel, ReusedGridBagConstraints.get(4, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(this.jSeparator6, ReusedGridBagConstraints.get(0, 3, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(this.buttonsPanel, ReusedGridBagConstraints.get(0, 4, 5, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

	}
	
	private void init()
	{
		this.showLengthCheckBox.setSelected(MapPropertiesManager.isShowLength());
		this.showLinkNamesCheckBox.setSelected(MapPropertiesManager.isShowLinkNames());
		this.showNodeNamesCheckBox.setSelected(MapPropertiesManager.isShowNodesNames());
		this.showPhysicalNodesCheckBox.setSelected(MapPropertiesManager.isShowPhysicalNodes());
		
		this.metricTextField.setText(MapPropertiesManager.getMetric());
		this.spareTextField.setText(String.valueOf(MapPropertiesManager.getSpareLength()));

		this.thicknessComboBox.setSelected(new Integer(MapPropertiesManager.getThickness()));
		this.colorComboBox.setSelected(MapPropertiesManager.getColor());
//		this.styleComboBox.setSelected(MapPropertiesManager);
	
		this.selectionThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getSelectionThickness()));
		this.selectionColorComboBox.setSelected(MapPropertiesManager.getSelectionColor());
//		this.selectionStyleComboBox.setSelected(MapPropertiesManager);
		this.firstSelectionColorComboBox.setSelected(MapPropertiesManager.getFirstSelectionColor());
		this.secondSelectionColorComboBox.setSelected(MapPropertiesManager.getSecondSelectionColor());
	
		this.alarmedThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getAlarmedThickness()));
		this.alarmedColorComboBox.setSelected(MapPropertiesManager.getAlarmedColor());
//		this.alarmedStyleComboBox.setSelected(MapPropertiesManager);
	
		this.borderThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getBorderThickness()));
		this.borderColorComboBox.setSelected(MapPropertiesManager.getBorderColor());
		this.textColorComboBox.setSelected(MapPropertiesManager.getTextColor());
		this.backgroundColorComboBox.setSelected(MapPropertiesManager.getTextBackground());
//		this.fontComboBox.setSelected(MapPropertiesManager);
	
		this.unboundThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getUnboundThickness()));
		this.unboundLinkColorComboBox.setSelected(MapPropertiesManager.getUnboundLinkColor());
		this.unboundLinkPositionColorComboBox.setSelected(MapPropertiesManager.getUnboundLinkPositionColor());
		this.unboundElementColorComboBox.setSelected(MapPropertiesManager.getUnboundElementColor());
		this.canBindColorComboBox.setSelected(MapPropertiesManager.getCanBindColor());
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

		MapPropertiesManager.setThickness(((Integer )this.thicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setColor(this.colorComboBox.getSelectedColor());
//		this.styleComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setSelectionThickness(((Integer )this.selectionThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setSelectionColor(this.selectionColorComboBox.getSelectedColor());
//		this.selectionStyleComboBox.setSelected(MapPropertiesManager);
		MapPropertiesManager.setFirstSelectionColor(this.firstSelectionColorComboBox.getSelectedColor());
		MapPropertiesManager.setSecondSelectionColor(this.secondSelectionColorComboBox.getSelectedColor());
	
		MapPropertiesManager.setAlarmedThickness(((Integer )this.alarmedThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setAlarmedColor(this.alarmedColorComboBox.getSelectedColor());
//		this.alarmedStyleComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setBorderThickness(((Integer )this.borderThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setBorderColor(this.borderColorComboBox.getSelectedColor());
		MapPropertiesManager.setTextColor(this.textColorComboBox.getSelectedColor());
		MapPropertiesManager.setTextBackground(this.backgroundColorComboBox.getSelectedColor());
//		this.fontComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setUnboundThickness(((Integer )this.unboundThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setUnboundLinkColor(this.unboundLinkColorComboBox.getSelectedColor());
		MapPropertiesManager.setUnboundLinkPositionColor(this.unboundLinkPositionColorComboBox.getSelectedColor());
		MapPropertiesManager.setUnboundElementColor(this.unboundElementColorComboBox.getSelectedColor());
		MapPropertiesManager.setCanBindColor(this.canBindColorComboBox.getSelectedColor());
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
