package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.ColorComboBox;
import com.syrus.AMFICOM.Client.General.UI.LineThickComboBox;
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
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;

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

		this.getContentPane().setLayout(gridBagLayout1);
		visualPropsPanel.setLayout(gridBagLayout2);
		selectionPropsPanel.setLayout(gridBagLayout3);
		alarmedPropsPanel.setLayout(gridBagLayout4);
		textPropsPanel.setLayout(gridBagLayout5);
		unboundPropsPanel.setLayout(gridBagLayout6);
		showModesPanel.setLayout(gridBagLayout7);

		jSeparator1.setOrientation(SwingConstants.VERTICAL);
//		jSeparator1.setBounds(new Rectangle(95, 116, 10, 10));
		jSeparator1.setPreferredSize(verticalSeparatorDimension);
		jSeparator2.setOrientation(SwingConstants.VERTICAL);
		jSeparator2.setPreferredSize(verticalSeparatorDimension);
		jSeparator3.setOrientation(SwingConstants.HORIZONTAL);
		jSeparator3.setPreferredSize(horizontalSeparatorDimension);
		jSeparator4.setOrientation(SwingConstants.VERTICAL);
		jSeparator4.setPreferredSize(verticalSeparatorDimension);
		jSeparator5.setOrientation(SwingConstants.VERTICAL);
		jSeparator5.setPreferredSize(verticalSeparatorDimension);
		jSeparator6.setOrientation(SwingConstants.HORIZONTAL);
		jSeparator6.setPreferredSize(horizontalSeparatorDimension);

		thicknessComboBox.setPreferredSize(fieldDimension);
		colorComboBox.setPreferredSize(fieldDimension);
		styleComboBox.setPreferredSize(fieldDimension);

		selectionThicknessComboBox.setPreferredSize(fieldDimension);
		selectionColorComboBox.setPreferredSize(fieldDimension);
		selectionStyleComboBox.setPreferredSize(fieldDimension);
		firstSelectionColorComboBox.setPreferredSize(fieldDimension);
		secondSelectionColorComboBox.setPreferredSize(fieldDimension);

		alarmedThicknessComboBox.setPreferredSize(fieldDimension);
		alarmedColorComboBox.setPreferredSize(fieldDimension);
		alarmedStyleComboBox.setPreferredSize(fieldDimension);

		borderThicknessComboBox.setPreferredSize(fieldDimension);
		borderColorComboBox.setPreferredSize(fieldDimension);
		textColorComboBox.setPreferredSize(fieldDimension);
		backgroundColorComboBox.setPreferredSize(fieldDimension);
		fontComboBox.setPreferredSize(fieldDimension);
		metricTextField.setPreferredSize(fieldDimension);

		unboundThicknessComboBox.setPreferredSize(fieldDimension);
		unboundLinkColorComboBox.setPreferredSize(fieldDimension);
		unboundLinkPositionColorComboBox.setPreferredSize(fieldDimension);
		unboundElementColorComboBox.setPreferredSize(fieldDimension);
		canBindColorComboBox.setPreferredSize(fieldDimension);
		spareTextField.setPreferredSize(fieldDimension);

		thicknessLanel.setText("Толщина линии");
		colorLabel.setText("Цвет линии");
		selectionThicknessLabel.setText("Толщина линии выделения");
		selectionColorLabel.setText("Цвет линии выделения");
		selectionStyleLabel.setText("Стиль линии выделения");
		styleLabel.setText("Стиль линии");
		alarmedThicknessLabel.setText("Толщина линии (тревога)");
		alarmedColorLabel.setText("Цвет линии (тревога)");
		alarmedStyleLabel.setText("Стиль линии (тревога)");
		borderThicknessLanel.setText("Толщина рамки");
		borderColorLabel.setText("Цвет рамки");
		textColorLabel.setText("Цвет текста");
		backgroundColorLabel.setText("Цвет под текстом");
		fontLabel.setText("Шрифт");
		firstSelectionColorLabel.setText("Первый цвет выделения линии");
		secondSelectionColorLabel.setText("Второй цвет выделения линии");
		metricLabel.setText("Метрика");
		unboundThicknessLanel.setText("Толщина непроложенного кабеля");
		unboundLinkColorLabel.setText("Цвет непроложенного кабеля");
		unboundLinkPositionColorLabel.setText("Цвет линии с непривязанным кабелем");
		unboundElementColorLabel.setText("Цвет непривязанного элемента");
		canBindColorLabel.setText("Цвет режимя привязки элемента");
		spareLabel.setText("Запас по умолчанию при привязке, м");
		showLengthCheckBox.setText("Отображать длины");
		showLinkNamesCheckBox.setText("Отображать названия линий");
		showNodeNamesCheckBox.setText("Отображать названия узлов");
		showNodeNamesCheckBox.setActionCommand("showNodeNamesCheckBox");
		showPhysicalNodesCheckBox.setText("Отображать топологические узлы");

		okButton.setText("Применить");
		okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ok();
				}
			});
		cancelButton.setText("Отменить");
		cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					cancel();
				}
			});

		visualPropsPanel.add(thicknessLanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		visualPropsPanel.add(thicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		visualPropsPanel.add(colorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		visualPropsPanel.add(colorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		visualPropsPanel.add(styleLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		visualPropsPanel.add(styleComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		selectionPropsPanel.add(selectionThicknessLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		selectionPropsPanel.add(selectionThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		selectionPropsPanel.add(selectionColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		selectionPropsPanel.add(selectionColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		selectionPropsPanel.add(selectionStyleLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		selectionPropsPanel.add(selectionStyleComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		selectionPropsPanel.add(firstSelectionColorLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		selectionPropsPanel.add(firstSelectionColorComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		selectionPropsPanel.add(secondSelectionColorLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		selectionPropsPanel.add(secondSelectionColorComboBox, ReusedGridBagConstraints.get(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		alarmedPropsPanel.add(alarmedThicknessLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		alarmedPropsPanel.add(alarmedThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		alarmedPropsPanel.add(alarmedColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		alarmedPropsPanel.add(alarmedColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		alarmedPropsPanel.add(alarmedStyleLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		alarmedPropsPanel.add(alarmedStyleComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		textPropsPanel.add(borderThicknessLanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		textPropsPanel.add(borderThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		textPropsPanel.add(borderColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		textPropsPanel.add(borderColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		textPropsPanel.add(textColorLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		textPropsPanel.add(textColorComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		textPropsPanel.add(backgroundColorLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		textPropsPanel.add(backgroundColorComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		textPropsPanel.add(fontLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		textPropsPanel.add(fontComboBox, ReusedGridBagConstraints.get(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		textPropsPanel.add(metricLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		textPropsPanel.add(metricTextField, ReusedGridBagConstraints.get(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		unboundPropsPanel.add(unboundThicknessLanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		unboundPropsPanel.add(unboundThicknessComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		unboundPropsPanel.add(unboundLinkColorLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		unboundPropsPanel.add(unboundLinkColorComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		unboundPropsPanel.add(unboundLinkPositionColorLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		unboundPropsPanel.add(unboundLinkPositionColorComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		unboundPropsPanel.add(unboundElementColorLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		unboundPropsPanel.add(unboundElementColorComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		unboundPropsPanel.add(canBindColorLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		unboundPropsPanel.add(canBindColorComboBox, ReusedGridBagConstraints.get(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));
		unboundPropsPanel.add(spareLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		unboundPropsPanel.add(spareTextField, ReusedGridBagConstraints.get(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 50, 0));

		showModesPanel.add(showLengthCheckBox, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		showModesPanel.add(showLinkNamesCheckBox, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		showModesPanel.add(showNodeNamesCheckBox, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		showModesPanel.add(showPhysicalNodesCheckBox, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		buttonsPanel.add(okButton, null);
		buttonsPanel.add(cancelButton, null);

		this.getContentPane().add(visualPropsPanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(jSeparator1, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(selectionPropsPanel, ReusedGridBagConstraints.get(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(jSeparator2, ReusedGridBagConstraints.get(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(alarmedPropsPanel, ReusedGridBagConstraints.get(4, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(jSeparator3, ReusedGridBagConstraints.get(0, 1, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(textPropsPanel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(jSeparator4, ReusedGridBagConstraints.get(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(unboundPropsPanel, ReusedGridBagConstraints.get(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(jSeparator5, ReusedGridBagConstraints.get(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(showModesPanel, ReusedGridBagConstraints.get(4, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.getContentPane().add(jSeparator6, ReusedGridBagConstraints.get(0, 3, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.getContentPane().add(buttonsPanel, ReusedGridBagConstraints.get(0, 4, 5, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

	}
	
	private void init()
	{
		showLengthCheckBox.setSelected(MapPropertiesManager.isShowLength());
		showLinkNamesCheckBox.setSelected(MapPropertiesManager.isShowLinkNames());
		showNodeNamesCheckBox.setSelected(MapPropertiesManager.isShowNodesNames());
		showPhysicalNodesCheckBox.setSelected(MapPropertiesManager.isShowPhysicalNodes());
		
		metricTextField.setText(MapPropertiesManager.getMetric());
		spareTextField.setText(String.valueOf(MapPropertiesManager.getSpareLength()));

		thicknessComboBox.setSelected(new Integer(MapPropertiesManager.getThickness()));
		colorComboBox.setSelected(MapPropertiesManager.getColor());
//		styleComboBox.setSelected(MapPropertiesManager);
	
		selectionThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getSelectionThickness()));
		selectionColorComboBox.setSelected(MapPropertiesManager.getSelectionColor());
//		selectionStyleComboBox.setSelected(MapPropertiesManager);
		firstSelectionColorComboBox.setSelected(MapPropertiesManager.getFirstSelectionColor());
		secondSelectionColorComboBox.setSelected(MapPropertiesManager.getSecondSelectionColor());
	
		alarmedThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getAlarmedThickness()));
		alarmedColorComboBox.setSelected(MapPropertiesManager.getAlarmedColor());
//		alarmedStyleComboBox.setSelected(MapPropertiesManager);
	
		borderThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getBorderThickness()));
		borderColorComboBox.setSelected(MapPropertiesManager.getBorderColor());
		textColorComboBox.setSelected(MapPropertiesManager.getTextColor());
		backgroundColorComboBox.setSelected(MapPropertiesManager.getTextBackground());
//		fontComboBox.setSelected(MapPropertiesManager);
	
		unboundThicknessComboBox.setSelected(new Integer(MapPropertiesManager.getUnboundThickness()));
		unboundLinkColorComboBox.setSelected(MapPropertiesManager.getUnboundLinkColor());
		unboundLinkPositionColorComboBox.setSelected(MapPropertiesManager.getUnboundLinkPositionColor());
		unboundElementColorComboBox.setSelected(MapPropertiesManager.getUnboundElementColor());
		canBindColorComboBox.setSelected(MapPropertiesManager.getCanBindColor());
	}

	private void commit()
	{
		MapPropertiesManager.setShowLength(showLengthCheckBox.isSelected());
		MapPropertiesManager.setShowLinkNames(showLinkNamesCheckBox.isSelected());
		MapPropertiesManager.setShowNodesNames(showNodeNamesCheckBox.isSelected());
		MapPropertiesManager.setShowPhysicalNodes(showPhysicalNodesCheckBox.isSelected());

		MapPropertiesManager.setMetric(metricTextField.getText());
		
		try
		{
			MapPropertiesManager.setSpareLength(Double.parseDouble(spareTextField.getText()));
		}
		catch(Exception e)
		{
			// cannot parse
		}

		MapPropertiesManager.setThickness(((Integer )thicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setColor(colorComboBox.getSelectedColor());
//		styleComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setSelectionThickness(((Integer )selectionThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setSelectionColor(selectionColorComboBox.getSelectedColor());
//		selectionStyleComboBox.setSelected(MapPropertiesManager);
		MapPropertiesManager.setFirstSelectionColor(firstSelectionColorComboBox.getSelectedColor());
		MapPropertiesManager.setSecondSelectionColor(secondSelectionColorComboBox.getSelectedColor());
	
		MapPropertiesManager.setAlarmedThickness(((Integer )alarmedThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setAlarmedColor(alarmedColorComboBox.getSelectedColor());
//		alarmedStyleComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setBorderThickness(((Integer )borderThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setBorderColor(borderColorComboBox.getSelectedColor());
		MapPropertiesManager.setTextColor(textColorComboBox.getSelectedColor());
		MapPropertiesManager.setTextBackground(backgroundColorComboBox.getSelectedColor());
//		fontComboBox.setSelected(MapPropertiesManager);
	
		MapPropertiesManager.setUnboundThickness(((Integer )unboundThicknessComboBox.getSelected()).intValue());
		MapPropertiesManager.setUnboundLinkColor(unboundLinkColorComboBox.getSelectedColor());
		MapPropertiesManager.setUnboundLinkPositionColor(unboundLinkPositionColorComboBox.getSelectedColor());
		MapPropertiesManager.setUnboundElementColor(unboundElementColorComboBox.getSelectedColor());
		MapPropertiesManager.setCanBindColor(canBindColorComboBox.getSelectedColor());
	}

	private void ok()
	{
		commit();
		retCode = RET_OK;
		dispose();
	}

	private void cancel()
	{
		dispose();
	}
	
	public int getReturnCode()
	{
		return retCode;
	}
}
