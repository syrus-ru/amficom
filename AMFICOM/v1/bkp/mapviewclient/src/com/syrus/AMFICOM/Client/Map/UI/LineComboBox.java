package com.syrus.AMFICOM.Client.Map.UI;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.syrus.AMFICOM.client.UI.AComboBox;


public class LineComboBox extends AComboBox
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
			this.basicStroke = col;
			this.text = tx;
		}

		public MyLine ()
		{
			this.basicStroke = new BasicStroke(2);
			this.text = "Solid line";
		}

		public void setMyLine (BasicStroke col, String tx)
		{
			this.basicStroke = col;
			this.text = tx;
		}

		public String toString()
		{
			return this.text;
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
            this.jComboBox = Box;
			this.lbl.setFont(Box.getFont());
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
            LineComboBox.this.returnMyLine.setMyLine( ((MyLine)value).basicStroke, ((MyLine)value).text );

			if (isSelected)
			{
				this.lbl.setForeground(Color.white);
			}
			else
			{
				this.lbl.setForeground(Color.black);
			}

			this.a1.initDrawfield(
					0, 
					this.jComboBox.getHeight() / 2,
                    this.jComboBox.getWidth() / 2, 
					this.jComboBox.getHeight() / 2,
                    Color.red, 
					myLine.basicStroke );

			this.a1.setBounds(new Rectangle( 
					0, 
					0, 
					this.jComboBox.getWidth() / 2, 
					this.jComboBox.getHeight() ));

			this.lbl.setBounds(new Rectangle( 
					this.jComboBox.getWidth() / 2, 
					0, 
					this.jComboBox.getWidth(), 
					this.jComboBox.getHeight() ));

            this.setLayout(null);

            LineComboBox.this.returnStroke = myLine.basicStroke;
            this.lbl.setText(myLine.text);
            this.add(this.a1);
            this.add(this.lbl);

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
				this.startX = startx;
				this.startY = starty;
				this.endX = endx;
				this.endY = endy;
				this.color = col;
				this.basicStroke = bas;
			}

			public void paint(Graphics g)
			{
				Graphics2D p = ( Graphics2D)g;

				Stroke bs = p.getStroke();
				p.setStroke(this.basicStroke);

				p.setColor(this.color);
				p.drawLine(this.startX, this.startY, this.endX, this.endY);
				p.setStroke(bs);
			}
		}
    }

	public LineComboBox()
	{
		super(AComboBox.SMALL_FONT);

        this.returnMyLine.setMyLine( ((MyLine)vec.get(0)).basicStroke, ((MyLine)vec.get(0)).text );
		this.returnStroke = ((MyLine)vec.get(0)).basicStroke;

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
			this.returnMyLine.setMyLine( 
					ml.basicStroke,
					ml.text );
			this.returnStroke = ml.basicStroke;
			System.out.println("select " + ml.text);
		}
	}

	void this_actionPerformed(ActionEvent e)
	{
		this.returnMyLine.setMyLine( 
				((MyLine)this.getSelectedItem()).basicStroke,
				((MyLine)this.getSelectedItem()).text );
		this.returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
		System.out.println("select " + ((MyLine)this.getSelectedItem()).text);
	}

	void this_mousePressed(MouseEvent e)
	{
		this.returnMyLine.setMyLine( 
				((MyLine)this.getSelectedItem()).basicStroke,
				((MyLine)this.getSelectedItem()).text );
		this.returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
	}

	public BasicStroke getSelectedStroke()
	{
		this.returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
		return this.returnStroke;
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
		this.returnMyLine.setMyLine( 
				((MyLine)this.getSelectedItem()).basicStroke,
				((MyLine)this.getSelectedItem()).text );
		this.returnStroke = ((MyLine)this.getSelectedItem()).basicStroke;
		return this.returnMyLine;
	}

	public void setMyLine( MyLine myLine)
	{
		this.returnMyLine = myLine;
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
