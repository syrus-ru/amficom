package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;


/**
 * <p>Title: </p>
 * <p>Description: Диалоговое окно для выбора шрифта</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class FontChooserDialog extends JDialog
{
	public static Font selectedFont = null;

	JPanel fontParamsPanel = new JPanel();
	JPanel examplePanel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel fontNameLabel = new JLabel();
	JLabel fontSizeLabel = new JLabel();
	AComboBox fontNameComboBox = new AComboBox();
	AComboBox fontSizeComboBox = new AComboBox();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel paneTitlePanel = new JPanel();
	JPanel exampLabelPanel = new JPanel();
	JLabel exampTitleLabel = new JLabel();
	JLabel exampLabel = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();
	JButton applyButton = new JButton();
	JButton cancelButton = new JButton();
	Border border1;
	JCheckBox boldCheckBox = new JCheckBox();
	JCheckBox italicCheckBox = new JCheckBox();

	public FontChooserDialog()
	{
		jbInit();
		setComboData();
	}

	public FontChooserDialog(Font font)
	{
		this();
		fontNameComboBox.setSelectedItem(font.getName());
		fontNameComboBox.setSelectedItem(Integer.toString(font.getSize()));
		if (font.isBold())
			boldCheckBox.setSelected(true);
		if (font.isItalic())
			italicCheckBox.setSelected(true);
	}

	private void jbInit()
	{
		border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(103, 101, 98),new Color(148, 145, 140));
		this.setTitle(LangModelReport.String("label_chooseFont"));
		this.setResizable(false);
		this.setSize(new Dimension(299, 287));
		this.setModal(true);

		JFrame actW = Environment.getActiveWindow();
		this.setLocation(
			actW.getX() + (actW.getWidth() - this.getWidth()) / 2,
			actW.getY() + (actW.getHeight() - this.getHeight()) / 2);

		this.getContentPane().setLayout(new GridBagLayout());
		fontParamsPanel.setLayout(gridBagLayout1);
		fontNameLabel.setText(LangModelReport.String("label_fontName"));
		fontSizeLabel.setText(LangModelReport.String("label_fontSize"));
		examplePanel.setLayout(borderLayout1);
		exampTitleLabel.setText(LangModelReport.String("label_fontExample"));
		exampLabel.setBorder(border1);
		exampLabel.setText("aAbBcC123");
		exampLabelPanel.setLayout(borderLayout2);
		applyButton.setPreferredSize(new Dimension(99, 24));
		applyButton.setText(LangModelReport.String("label_apply"));
		applyButton.addActionListener(new FontChooserDialog1_applyButton_actionAdapter(this));
		cancelButton.setPreferredSize(new Dimension(79, 24));
		cancelButton.setText(LangModelReport.String("label_cancel"));
		cancelButton.addActionListener(new FontChooserDialog1_cancelButton_actionAdapter(this));
		boldCheckBox.setFont(new java.awt.Font("Dialog", 1, 13));
		boldCheckBox.setText(LangModelReport.String("label_fontBold"));
		boldCheckBox.addActionListener(new FontChooserDialog1_boldCheckBox_actionAdapter(this));
		italicCheckBox.setFont(new java.awt.Font("Dialog", 2, 13));
		italicCheckBox.setText(LangModelReport.String("label_fontItalic"));
		italicCheckBox.addActionListener(new FontChooserDialog1_italicCheckBox_actionAdapter(this));
		fontNameComboBox.addActionListener(new FontChooserDialog1_fontNameComboBox_actionAdapter(this));
		fontSizeComboBox.addActionListener(new FontChooserDialog1_fontSizeComboBox_actionAdapter(this));
		this.getContentPane().add(fontParamsPanel,     new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		fontParamsPanel.add(fontNameLabel,       new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		fontParamsPanel.add(fontSizeLabel,       new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		fontParamsPanel.add(fontNameComboBox,        new GridBagConstraints(0, 1, 1, 1, 5.0, 1.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
		fontParamsPanel.add(fontSizeComboBox,                   new GridBagConstraints(1, 1, 1, 2, 1.0, 1.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 23, 0));

		this.getContentPane().add(examplePanel,       new GridBagConstraints(0, 1, 2, 1, 1.0, 5.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		examplePanel.add(paneTitlePanel, BorderLayout.NORTH);
		paneTitlePanel.add(exampTitleLabel, null);
		examplePanel.add(exampLabelPanel, BorderLayout.CENTER);
		exampLabelPanel.add(exampLabel,  BorderLayout.CENTER);


		this.getContentPane().add(applyButton,     new GridBagConstraints(0, 2, 1, 1, 1.0, 0.5
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(cancelButton,    new GridBagConstraints(1, 2, 1, 1, 1.0, 0.5
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		fontParamsPanel.add(boldCheckBox,   new GridBagConstraints(0, 2, 1, 2, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));
		fontParamsPanel.add(italicCheckBox,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Заполняет комбобоксы названиями и размерами шрифтов
	 */
	private void setComboData()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] avFontNames = ge.getAvailableFontFamilyNames();
		for (int i = 0; i < avFontNames.length; i++)
			fontNameComboBox.addItem(avFontNames[i]);

		int[] avFontSizes = {8,9,10,11,12,14,16,18,20,24,28,32,36,42,48,54,60,66,72};
		for (int i = 0; i < avFontSizes.length; i++)
			fontSizeComboBox.addItem(Integer.toString(avFontSizes[i]));

		fontSizeComboBox.setSelectedIndex(6);

		updateFont();
	}

	/**
	 * Обновляет значение selectedFont и прорисовывает пример
	 * новым шрифтом
	 */
	private void updateFont()
	{
		try
		{
			int fontStyle = 0;
			if (boldCheckBox.isSelected())
				fontStyle += Font.BOLD;
			if (italicCheckBox.isSelected())
				fontStyle += Font.ITALIC;

			selectedFont = new Font(
				(String)fontNameComboBox.getSelectedItem(),
				fontStyle,
				Integer.parseInt((String)fontSizeComboBox.getSelectedItem()));

			exampLabel.setFont(selectedFont);
		}
		catch (Exception exc)
		{
		}
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		selectedFont = null;
		dispose();
	}

	void applyButton_actionPerformed(ActionEvent e)
	{
		updateFont();
		dispose();
	}

	void fontNameComboBox_actionPerformed(ActionEvent e)
	{
		updateFont();
	}

	void fontSizeComboBox_actionPerformed(ActionEvent e)
	{
		updateFont();
	}

	void boldCheckBox_actionPerformed(ActionEvent e)
	{
		updateFont();
	}

	void italicCheckBox_actionPerformed(ActionEvent e)
	{
		updateFont();
	}
}

class FontChooserDialog1_cancelButton_actionAdapter implements java.awt.event.ActionListener
{
	FontChooserDialog adaptee;

	FontChooserDialog1_cancelButton_actionAdapter(FontChooserDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.cancelButton_actionPerformed(e);
	}
}

class FontChooserDialog1_applyButton_actionAdapter implements java.awt.event.ActionListener
{
	FontChooserDialog adaptee;

	FontChooserDialog1_applyButton_actionAdapter(FontChooserDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.applyButton_actionPerformed(e);
	}
}

class FontChooserDialog1_fontNameComboBox_actionAdapter implements java.awt.event.ActionListener
{
	FontChooserDialog adaptee;

	FontChooserDialog1_fontNameComboBox_actionAdapter(FontChooserDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.fontNameComboBox_actionPerformed(e);
	}
}

class FontChooserDialog1_fontSizeComboBox_actionAdapter implements java.awt.event.ActionListener
{
	FontChooserDialog adaptee;

	FontChooserDialog1_fontSizeComboBox_actionAdapter(FontChooserDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.fontSizeComboBox_actionPerformed(e);
	}
}

class FontChooserDialog1_boldCheckBox_actionAdapter implements java.awt.event.ActionListener
{
	FontChooserDialog adaptee;

	FontChooserDialog1_boldCheckBox_actionAdapter(FontChooserDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.boldCheckBox_actionPerformed(e);
	}
}

class FontChooserDialog1_italicCheckBox_actionAdapter implements java.awt.event.ActionListener
{
	FontChooserDialog adaptee;

	FontChooserDialog1_italicCheckBox_actionAdapter(FontChooserDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.italicCheckBox_actionPerformed(e);
	}
}
