package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;

public class TextFieldEditor extends JTextField
		implements PropertyRenderer, PropertyEditor
{
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1); 

	public TextFieldEditor()
	{
		super();
		setOpaque(true);
        setBorder(noFocusBorder);

		Font font = this.getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));

		setBackground(SystemColor.window);
	}

	public TextFieldEditor(String text)
	{
		super(text);
		setOpaque(true);
        setBorder(noFocusBorder);

		Font font = this.getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));

		setBackground(SystemColor.window);
	}

    public TextFieldEditor(int columns) 
	{
        super(columns);
		setOpaque(true);
        setBorder(noFocusBorder);

		Font font = this.getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));

		setBackground(SystemColor.window);
    }

    public TextFieldEditor(String text, int columns) 
	{
        super(text, columns);
		setOpaque(true);
        setBorder(noFocusBorder);

		Font font = this.getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));

		setBackground(SystemColor.window);
    }

    public TextFieldEditor(Document doc, String text, int columns) 
	{
        super(doc, text, columns);
		setOpaque(true);
        setBorder(noFocusBorder);

		Font font = this.getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));

		setBackground(SystemColor.window);
	}

	public Object getSelected()
	{
		return getText();
	}
	
	public void setSelected(Object obj)
	{
		setText((String )obj);
	}
	
}