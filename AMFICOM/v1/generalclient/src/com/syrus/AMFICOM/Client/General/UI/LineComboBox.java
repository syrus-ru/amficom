package com.syrus.AMFICOM.Client.General.UI;


import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
//import java.awt.ca
import java.awt.event.*;


public class LineComboBox extends AComboBox
		implements PropertyRenderer, PropertyEditor
{
	public static Vector vec = new Vector();
	static int lineWidth = 3;

	public static boolean do_init = true;

	{
		if(do_init)
		{
			vec.addElement( new MyLine(new BasicStroke(lineWidth), "Solid line") );
			vec.addElement( new MyLine(
				new BasicStroke(
					lineWidth,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					(float)0.0,
					new float[] {10, 3},
					(float)0.0), 
				"Dash line 1") );
			vec.addElement( new MyLine(
				new BasicStroke(
					lineWidth,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					(float)0.0,
					new float[] {3, 10},
					(float)0.0),
				"Dash line 2") );
			vec.addElement( new MyLine(
				new BasicStroke(
					lineWidth,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					(float)0.0,
					new float[] {10, 3, 10},
					(float)0.0),
				"Dash line 3") );
			vec.addElement( new MyLine(
				new BasicStroke(
					lineWidth,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					(float)0.0,
					new float[] {10, 10, 3, 10},
					(float)0.0),
				"Dash line 4") );
			do_init = false;
		}
	}

	public BasicStroke returnStroke;
	int prevComboBox = 0;

	public MyLine returnMyLine = new MyLine();
	MyLine[] lines;

	public class MyLine
	{
		public BasicStroke basicStroke;
		public String text;

		public MyLine (BasicStroke col, String tx)
		{
			basicStroke = col;
			text = tx;
		}

		public MyLine ()
		{
			basicStroke = new BasicStroke(2);
			text = "Solid line";
		}

		public void setMyLine (BasicStroke col, String tx)
		{
			basicStroke = col;
			text = tx;
		}

		public String toString()
		{
			return text;
		}
	}
	
    class ComboBoxRenderer extends JPanel implements ListCellRenderer
    {
        public JLabel lbl = new JLabel();
        public Drawfield a1 = new Drawfield();

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
            MyLine myLine = (MyLine )value;
            returnMyLine.setMyLine( ((MyLine)value).basicStroke, ((MyLine)value).text );

			if (isSelected)
			{
				lbl.setForeground(Color.white);
			}
			else
			{
				lbl.setForeground(Color.black);
			}

			a1.initDrawfield(
					0, 
					(int)(jComboBox.getHeight()/2 ),
                    (int)(jComboBox.getWidth()/2 ), 
					(int)(jComboBox.getHeight()/2 ),
                    Color.red, 
					myLine.basicStroke );

			a1.setBounds(new Rectangle( 
					0 , 
					0, 
					(int)(jComboBox.getWidth()/2 ), 
					jComboBox.getHeight() ));

			lbl.setBounds(new Rectangle( 
					(int)(jComboBox.getWidth()/2 ), 
					0 , 
					jComboBox.getWidth(), 
					jComboBox.getHeight() ));

            this.setLayout(null);

            returnStroke = myLine.basicStroke;
            lbl.setText(myLine.text);
            this.add(a1);
            this.add(lbl);

            return this;
        }

		class Drawfield extends JLabel
		{
			int startX;
			int startY;
			int endX;
			int endY;
			Color color;
			BasicStroke basicStroke;

			public Drawfield()
			{
				super();
			}

			public void initDrawfield(int startx, int starty, int endx, int endy, Color col, BasicStroke bas)
			{
				startX = startx;
				startY = starty;
				endX = endx;
				endY = endy;
				color = col;
				basicStroke = bas;
			}

			public void paint(Graphics g)
			{
				Graphics2D p = ( Graphics2D)g;

				Stroke bs = p.getStroke();
				p.setStroke(basicStroke);

				p.setColor(color);
				p.drawLine(startX, startY, endX, endY);
				p.setStroke(bs);
			}
		}
    }

	public LineComboBox()
	{
		super(AComboBox.SMALL_FONT);

        returnMyLine.setMyLine( ((MyLine)vec.get(0)).basicStroke, ((MyLine)vec.get(0)).text );
		returnStroke = ((MyLine)vec.get(0)).basicStroke;

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
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setRenderer(renderer);
        this.setMaximumRowCount(5);
/*
		renderer.lbl.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				this_mousePressed(e);
			}
			public void mouseReleased(MouseEvent e)
			{
				this_mousePressed(e);
			}
		});
		renderer.a1.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				this_mousePressed(e);
			}
			public void mouseReleased(MouseEvent e)
			{
				this_mousePressed(e);
			}
		});
*/
	}

	void this_itemStateChanged(ItemEvent e)
	{
		if(e.getStateChange() == ItemEvent.SELECTED)
        if (this.getSelectedItem() instanceof MyLine)
        {
			MyLine ml = (MyLine )this.getSelectedItem();
			returnMyLine.setMyLine( 
					ml.basicStroke,
					ml.text );
			returnStroke = ml.basicStroke;
			System.out.println("select " + ml.text);
		}
	}

	void this_actionPerformed(ActionEvent e)
	{
		returnMyLine.setMyLine( 
				((MyLine)this.getSelectedItem()).basicStroke,
				((MyLine)this.getSelectedItem()).text );
		returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
		System.out.println("select " + ((MyLine)this.getSelectedItem()).text);
	}

	void this_mousePressed(MouseEvent e)
	{
		returnMyLine.setMyLine( 
				((MyLine)this.getSelectedItem()).basicStroke,
				((MyLine)this.getSelectedItem()).text );
		returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
	}

	public BasicStroke getSelectedStroke()
	{
		returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
		return returnStroke;
	}

	public void setLineSize(int i)
	{
		lineWidth = i;
	}

	public int getLineSize()
	{
		return lineWidth;
	}

	public MyLine getReturnMyLine()
	{
		returnMyLine.setMyLine( 
				((MyLine)this.getSelectedItem()).basicStroke,
				((MyLine)this.getSelectedItem()).text );
		returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
		return returnMyLine;
	}

	public void setMyLine( MyLine myLine)
	{
		returnMyLine = myLine;
	}

	public Object getSelected()
	{
		return getReturnMyLine();//.text;
//		return getSelectedStroke();
	}

	public void setSelected(Object obj)
	{
		String str = (String )obj;
		for(int i = 0; i < vec.size(); i++)
		{
			MyLine line = (MyLine)vec.get(i);
			if(line.text.equals(str))
			{
				setSelectedItem(line);
				return;
			}
		}
	}

	public static BasicStroke getStrokeByType(String str)
	{
		for(int i = 0; i < vec.size(); i++)
		{
			MyLine line = (MyLine)vec.get(i);
			if(line.text.equals(str))
			{
				return line.basicStroke;
			}
		}
		return new BasicStroke(2);
	}


}
