package com.syrus.AMFICOM.client.report;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;

import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.model.Environment;

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
	private static final long serialVersionUID = 629260310026654841L;
	
	public static Font selectedFont = null;
	private static FontChooserDialog dialog = null;

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

	private FontChooserDialog()
	{
		jbInit();
		setComboData();
	}

	public static FontChooserDialog getInstance()
	{
		if (FontChooserDialog.dialog == null)
			FontChooserDialog.dialog = new FontChooserDialog();
		
		return FontChooserDialog.dialog;
	}	
	public static FontChooserDialog getInstance(Font font)
	{
		FontChooserDialog fontDialog = getInstance();
		fontDialog.fontNameComboBox.setSelectedItem(font.getName());
		fontDialog.fontNameComboBox.setSelectedItem(Integer.toString(font.getSize()));
		if (font.isBold())
			fontDialog.boldCheckBox.setSelected(true);
		if (font.isItalic())
			fontDialog.italicCheckBox.setSelected(true);
		
		return fontDialog;
	}

	private void jbInit()
	{
		this.border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(103, 101, 98),new Color(148, 145, 140));
		this.setTitle(LangModelReport.getString("label_chooseFont"));
		this.setResizable(false);
		this.setSize(new Dimension(299, 287));
		this.setModal(true);

		JFrame actW = Environment.getActiveWindow();
		this.setLocation(
			actW.getX() + (actW.getWidth() - this.getWidth()) / 2,
			actW.getY() + (actW.getHeight() - this.getHeight()) / 2);

		this.getContentPane().setLayout(new GridBagLayout());
		this.fontParamsPanel.setLayout(this.gridBagLayout1);
		this.fontNameLabel.setText(LangModelReport.getString("label_fontName"));
		this.fontSizeLabel.setText(LangModelReport.getString("label_fontSize"));
		this.examplePanel.setLayout(this.borderLayout1);
		this.exampTitleLabel.setText(LangModelReport.getString("label_fontExample"));
		this.exampLabel.setBorder(this.border1);
		this.exampLabel.setText("aAbBcC123");
		this.exampLabelPanel.setLayout(this.borderLayout2);
		this.applyButton.setPreferredSize(new Dimension(99, 24));
		this.applyButton.setText(LangModelReport.getString("label_apply"));
		this.applyButton.addActionListener(new FontChooserDialog1_applyButton_actionAdapter(this));
		this.cancelButton.setPreferredSize(new Dimension(79, 24));
		this.cancelButton.setText(LangModelReport.getString("label_cancel"));
		this.cancelButton.addActionListener(new FontChooserDialog1_cancelButton_actionAdapter(this));
		this.boldCheckBox.setFont(new java.awt.Font("Dialog", 1, 13));
		this.boldCheckBox.setText(LangModelReport.getString("label_fontBold"));
		this.boldCheckBox.addActionListener(new FontChooserDialog1_boldCheckBox_actionAdapter(this));
		this.italicCheckBox.setFont(new java.awt.Font("Dialog", 2, 13));
		this.italicCheckBox.setText(LangModelReport.getString("label_fontItalic"));
		this.italicCheckBox.addActionListener(new FontChooserDialog1_italicCheckBox_actionAdapter(this));
		this.fontNameComboBox.addActionListener(new FontChooserDialog1_fontNameComboBox_actionAdapter(this));
		this.fontSizeComboBox.addActionListener(new FontChooserDialog1_fontSizeComboBox_actionAdapter(this));
		this.getContentPane().add(this.fontParamsPanel,     new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.fontParamsPanel.add(this.fontNameLabel,       new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.fontParamsPanel.add(this.fontSizeLabel,       new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.fontParamsPanel.add(this.fontNameComboBox,        new GridBagConstraints(0, 1, 1, 1, 5.0, 1.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
		this.fontParamsPanel.add(this.fontSizeComboBox,                   new GridBagConstraints(1, 1, 1, 2, 1.0, 1.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 23, 0));

		this.getContentPane().add(this.examplePanel,       new GridBagConstraints(0, 1, 2, 1, 1.0, 5.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.examplePanel.add(this.paneTitlePanel, BorderLayout.NORTH);
		this.paneTitlePanel.add(this.exampTitleLabel, null);
		this.examplePanel.add(this.exampLabelPanel, BorderLayout.CENTER);
		this.exampLabelPanel.add(this.exampLabel,  BorderLayout.CENTER);


		this.getContentPane().add(this.applyButton,     new GridBagConstraints(0, 2, 1, 1, 1.0, 0.5
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(this.cancelButton,    new GridBagConstraints(1, 2, 1, 1, 1.0, 0.5
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.fontParamsPanel.add(this.boldCheckBox,   new GridBagConstraints(0, 2, 1, 2, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));
		this.fontParamsPanel.add(this.italicCheckBox,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
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
			this.fontNameComboBox.addItem(avFontNames[i]);

		int[] avFontSizes = {8,9,10,11,12,14,16,18,20,24,28,32,36,42,48,54,60,66,72};
		for (int i = 0; i < avFontSizes.length; i++)
			this.fontSizeComboBox.addItem(Integer.toString(avFontSizes[i]));

		this.fontSizeComboBox.setSelectedIndex(6);

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
			if (this.boldCheckBox.isSelected())
				fontStyle += Font.BOLD;
			if (this.italicCheckBox.isSelected())
				fontStyle += Font.ITALIC;

			selectedFont = new Font(
				(String)this.fontNameComboBox.getSelectedItem(),
				fontStyle,
				Integer.parseInt((String)this.fontSizeComboBox.getSelectedItem()));

			this.exampLabel.setFont(selectedFont);
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
		this.adaptee.cancelButton_actionPerformed(e);
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
		this.adaptee.applyButton_actionPerformed(e);
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
		this.adaptee.fontNameComboBox_actionPerformed(e);
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
		this.adaptee.fontSizeComboBox_actionPerformed(e);
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
		this.adaptee.boldCheckBox_actionPerformed(e);
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
		this.adaptee.italicCheckBox_actionPerformed(e);
	}
}
