package com.syrus.AMFICOM.Client.General.UI;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.event.*;

public class ColorComboBox extends AComboBox
		implements PropertyRenderer, PropertyEditor
{
	public Vector vec = new Vector();
	public Color returnColor ;
	int prevComboBox = 0;

    MyColor[] col = {
            new MyColor(Color.black, "black"),
            new MyColor(Color.blue, "blue"),
            new MyColor(Color.cyan, "cyan"),
            new MyColor(Color.darkGray, "darkGray"),
            new MyColor(Color.gray, "gray"),
            new MyColor(Color.green, "green"),
            new MyColor(Color.lightGray, "lightGray"),
            new MyColor(Color.magenta, "magenta"),
            new MyColor(Color.orange, "orange"),
            new MyColor(Color.pink, "pink"),
            new MyColor(Color.red, "red" ),
            new MyColor(Color.white, "white"),
            new MyColor(Color.yellow, "yellow") };

	MyColor[] colors;
	public MyColor returnMyColor = new MyColor();

	public class MyColor
	{
		public Color color;
		public String text;

		public MyColor (Color col, String tx)
		{
			color = col;
			text = tx;
		}

		public MyColor ()
		{
			color = Color.black;
			text = "";
		}

		public void setMyColor(Color col, String tx)
		{
			color = col;
			text = tx;
		}
	}

    class ComboBoxRenderer extends JLabel implements ListCellRenderer
    {
		JPanel p1 = new JPanel();
		JLabel lbl = new JLabel();
		JButton jButton = new JButton("");

		JComboBox jComboBox = new JComboBox();

		public ComboBoxRenderer(JComboBox Box)
		{
		    setOpaque(true);
		    jComboBox = Box;
			lbl.setFont(Box.getFont());
		}

		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			if (isSelected)
			{
			    setBackground(list.getSelectionBackground());
			    setForeground(list.getSelectionForeground());
			}
			else
			{
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			}
			MyColor myColor = (MyColor )value;
//               returnMyColor.setMyColor( ((MyColor)value).color, ((MyColor)value).text );

			if (isSelected)
				lbl.setForeground(Color.white);
			else
				lbl.setForeground(Color.black);

            p1.setBounds(new Rectangle(
					(int)(jComboBox.getHeight() / 4),
					(int)(jComboBox.getHeight() / 4),
                    (int)(jComboBox.getHeight() * 3 / 4),
					(int)(jComboBox.getHeight() * 2 / 4)));

            lbl.setBounds(new Rectangle( 
					(int)(jComboBox.getHeight() * 5 / 4), 
					0, 
					100, 
					jComboBox.getHeight() ));

            this.setLayout(null);

            if (myColor.color == null)
            {
				jButton.setBounds(new Rectangle( 
						(int)(jComboBox.getHeight() / 5), 
						(int)(jComboBox.getHeight() / 5),
                        (int)(jComboBox.getHeight()), 
						(int)(jComboBox.getHeight() * 4 / 5)));

                this.add(jButton);
                this.remove(lbl);
                this.remove(p1);
            }
            else
            {
				p1.setBackground(myColor.color);
				lbl.setText(myColor.text);
				this.add(lbl);
				this.add(p1);
				this.remove(jButton);
            }
			returnColor = myColor.color;

            return this;
        }
    }

	public ColorComboBox(Vector vector)
	{
		super(AComboBox.SMALL_FONT);

		colors = col;
        vec = vector;

        vec.addElement(new MyColor(null, ""));

        returnColor = new Color( ((MyColor)vec.get(0)).color.getRGB() );
//        returnMyColor.setMyColor( ((MyColor)vec.get(0)).color, ((MyColor)vec.get(0)).text );

        setModel(new DefaultComboBoxModel(vec));

		this.setBounds(new Rectangle(0, 0, 20, 25));

		this.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				this_actionPerformed(e);
			}
		});

        ComboBoxRenderer renderer = new ComboBoxRenderer(this);
        renderer.setSize(this.getWidth(), this.getHeight());
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setRenderer(renderer);
        this.setMaximumRowCount(5);
	}

	public ColorComboBox()
	{
		super(AComboBox.SMALL_FONT);

		colors = col;

		for(int i = 0; i < colors.length; i++)
			vec.addElement(colors[i]);
/*
		vec.addElement(colors[1]);
		vec.addElement(colors[2]);
		vec.addElement(colors[6]);
		vec.addElement(colors[7]);
		vec.addElement(colors[8]);
*/
        vec.addElement(new MyColor(null, ""));

//        returnMyColor.setMyColor( ((MyColor)vec.get(0)).color, ((MyColor)vec.get(0)).text );
        returnColor = new Color( ((MyColor)vec.get(0)).color.getRGB() );

        setModel(new DefaultComboBoxModel(vec));

		this.setBounds(new Rectangle(0, 0, 20, 20));

		this.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				this_itemStateChanged(e);
			}
		});
/*
		this.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				this_actionPerformed(e);
			}
		});
*/
        ComboBoxRenderer renderer = new ComboBoxRenderer(this);
        renderer.setSize(this.getWidth(), this.getHeight());
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setRenderer(renderer);
        this.setMaximumRowCount(5);
	}

	void this_actionPerformed(ActionEvent e)
	{
        if (this.getSelectedItem() instanceof MyColor)
        {
            MyColor myColor = (MyColor)this.getSelectedItem();

            if (myColor.color == null)
            {
//                JColorChooser tcc = new JColorChooser();
                Color col = JColorChooser.showDialog(this, "Select color", Color.red);
                if ( col != null)
                {
                    myColor = new MyColor(col, "Custom color");
                    vec.insertElementAt(myColor, vec.size() - 1);
                    this.setSelectedIndex(vec.size() - 2);
                }
                else
                {
                    this.setSelectedIndex(prevComboBox);
                }
            }
            else
            {
				prevComboBox = this.getSelectedIndex();
            }
/*
            returnMyColor.setMyColor( 
					((MyColor)this.getSelectedItem()).color,
					((MyColor)this.getSelectedItem()).text );
*/
			returnColor = ((MyColor )this.getSelectedItem()).color;
        }
	}

	void this_itemStateChanged(ItemEvent e)
	{
		if(e.getStateChange() == ItemEvent.SELECTED)
        if (this.getSelectedItem() instanceof MyColor)
        {
            MyColor myColor = (MyColor)this.getSelectedItem();

            if (myColor.color == null)
            {
                Color col = JColorChooser.showDialog(this, "Select color", Color.red);
                if ( col != null)
                {
                    myColor = new MyColor(col, "Custom color");
                    vec.insertElementAt(myColor, vec.size() - 1);
                    this.setSelectedIndex(vec.size() - 2);
                }
                else
                {
                    this.setSelectedIndex(prevComboBox);
                }
            }
            else
            {
				prevComboBox = this.getSelectedIndex();
            }
			returnColor = ((MyColor )this.getSelectedItem()).color;
        }
	}

	public Color getSelectedColor()
	{
		returnColor = ((MyColor )this.getSelectedItem()).color;
		return returnColor;
	}
/*
	public MyColor getReturnMyColor()
	{
		return returnMyColor;
	}

	public void setMyColor(Color color, String text)
	{
		MyColor myColor = new MyColor(color, text);
   // vec.addElement(myColor);
   // setModel(new DefaultComboBoxModel(vec));
		this.setSelected(myColor.color);
		returnMyColor = myColor;
	}

	public void setMyColor(MyColor myColor)
	{
  //  vec.addElement(myColor);
  //  setModel(new DefaultComboBoxModel(vec));
		this.setSelected(myColor.color);
		returnMyColor = myColor;
	}
*/
	public Object getSelected()
	{
//		return getReturnMyColor();//.color;
		return getSelectedColor();
	}

	public void setSelected(Object obj)
	{
		MyColor mycolor;
		Color cor = (Color )obj;
		for(int i = 0; i < vec.size(); i++)
		{
			mycolor = (MyColor )vec.get(i);
			if(mycolor.color != null)
				if(mycolor.color.getRGB() == cor.getRGB())
				{
//					System.out.println("set selected " + color.text);
//					setSelectedItem(mycolor);
			        this.setSelectedIndex(i);
					return;
				}
		}
		mycolor = new MyColor(cor, "Custom color");
        vec.insertElementAt(mycolor, vec.size() - 1);
        this.setSelectedIndex(vec.size() - 2);
	}
}
