package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import com.syrus.AMFICOM.Client.Resource.StubResource;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;

public abstract class MapNodeElement extends StubResource
    implements MapElement 
{
	protected MapContext mapContext;

//Размер пиктограммы поумолчанию
	public final static Rectangle defaultBounds = new Rectangle(14, 14);
	public final static Rectangle minBounds = new Rectangle(6, 6);
	public final static Rectangle maxBounds = new Rectangle(40, 40);
	protected int selectedNodeLineSize = 2;

	protected boolean selected = false;
	protected Image icon;

//Размер пиктограммы
	protected Rectangle bounds = new Rectangle(defaultBounds);

	public String id = "";
	public String name = "";
	public String type_id = "";
	public String description = "";
	protected Point2D.Double anchor = new Point2D.Double( 0, 0);	//Координаты
//	protected SxDoublePoint anchor = new SxDoublePoint( 0, 0);	//Координаты
	
	public String owner_id = "";
	public String mapContextID = "";
	protected String imageID = "";

	public String optimizerAttribute = "optional";// "mandatory", "restricted"

	public Hashtable attributes = new Hashtable();

	public double scaleCoefficient = 1.0;

	boolean alarmState = false;
	public boolean showAlarmState = false;

	public void setScaleCoefficient(double ss)
	{
		scaleCoefficient = ss;
		int w = (int )((double )defaultBounds.width * scaleCoefficient);
		int h = (int )((double )defaultBounds.height * scaleCoefficient);
		
		if (w >= maxBounds.width || h >= maxBounds.height )
			setBounds(maxBounds);
		else
		if (w <= minBounds.width || h <= minBounds.height )
			setBounds(minBounds);
		else
			setBounds(new Rectangle(w, h));
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}

	public void setBounds(Rectangle rec)
	{
		bounds = rec;
	}

	public int getSelectedNodeLineSize()
	{
		return selectedNodeLineSize;
	}

	public void setSelectedNodeLineSize(int size)
	{
		selectedNodeLineSize = size;
	}

	public Image get_image()
	{
		ImageResource ir = ImageCatalogue.get(imageID);
		if(ir == null)
		{
			ImageIcon myImageIcon = new ImageIcon("images/pc.gif");
			return myImageIcon.getImage();
		}
		return ir.getImage();
	}

//A0A
	public void setImageID(String iconPath)
	{
		imageID = iconPath;

		int width = (int)Math.round(getBounds().getWidth());
		int height = (int)Math.round(getBounds().getHeight());

//Рамер не должен равняться 0
		if ( width == 0 || height == 0)
		{
			icon = get_image().getScaledInstance(1, 1, Image.SCALE_SMOOTH);
		}
		else
		{
			icon = get_image().getScaledInstance((int)Math.round(getBounds().getWidth()),
				(int)Math.round(getBounds().getHeight()),Image.SCALE_SMOOTH);
		}
	}

	public String getImageID()
	{
		return imageID;
	}

	public SxDoublePoint getAnchor()
	{
		return new SxDoublePoint(anchor.x, anchor.y);
	}

	public void setAnchor(SxDoublePoint aAnchor)
	{
		anchor.x = aAnchor.x;
		anchor.y = aAnchor.y;
	}

	public boolean isSelected ()
	{
		return selected;
	}

	public void select()
	{
		selected = true;
	}

	public void deselect()
	{
		selected = false;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}
	
	public void setId(String myID)
	{
		id = myID;
	}

	public void paint (Graphics g)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();
		
		Point p = lnl.convertMapToScreen( new SxDoublePoint(
				getAnchor().x, 
				getAnchor().y) );

		Graphics2D pg = ( Graphics2D)g;
		pg.setStroke(new BasicStroke(getSelectedNodeLineSize()));

		pg.drawImage(
				icon,
                p.x - icon.getWidth(lnl) / 2,
                p.y - icon.getHeight(lnl) / 2,
                lnl);

		if (this.getAlarmState())
		{
			if ( this.getShowAlarmed() )
			{
				pg.setStroke(new BasicStroke(getSelectedNodeLineSize() + 2));
				pg.setColor( Color.red);
				pg.drawRect( 
						p.x - icon.getWidth(lnl) / 2,
						p.y - icon.getHeight(lnl) / 2,
						icon.getWidth( lnl),
						icon.getHeight( lnl));
			}
		}

//Если выбрано то рисовать рамку
		if (isSelected())
		{
			pg.setStroke(new BasicStroke(getSelectedNodeLineSize()));
			pg.setColor( Color.green);
			pg.drawRect( 
					p.x-icon.getWidth( lnl)/2,
					p.y-icon.getHeight( lnl)/2,
					icon.getWidth( lnl),
					icon.getHeight( lnl));
		}
	}

	public void paint (Graphics g, Point myPoint)
	{
	}

	public void setAlarmState(boolean i)
	{
		alarmState = i;
	}

	public boolean getAlarmState()
	{
		return alarmState;
	}

	public void setShowAlarmed(boolean bool)
	{
		showAlarmState = bool;
	}

	public boolean getShowAlarmed()
	{
		return showAlarmState;
	}

	public boolean isMouseOnThisObject (Point currentMousePoint)
	{
		//Проверка того что курсор находиться в прямоугольнике
		int width = icon.getWidth( getLogicalNetLayer());
		int height = icon.getHeight( getLogicalNetLayer());
		Point p = getLogicalNetLayer().convertMapToScreen(new SxDoublePoint(
				getAnchor().x, 
				getAnchor().y) );
		Rectangle imageBounds = new Rectangle( p.x - width / 2 , p.y - height / 2 , width, height);
		if (imageBounds.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	public void move (double deltaX, double deltaY)
	{
		double x = anchor.x;
		double y = anchor.y;
		this.anchor = new Point2D.Double(x + deltaX, y + deltaY);
	}

//Обработка событии связпнный с Node
	public MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		return new VoidStrategy();
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return mapContext.getLogicalNetLayer();
	}

	public MapContext getMapContext()
	{
		return mapContext;
	}

	public void setMapContext( MapContext myMapContext)
	{
		mapContext = myMapContext;
	}

//Можно ли перемещать
   public boolean isMovable()
   {
		return true;
   }

	public String getToolTipText()
	{
		String s1 = getName();

		return s1 + " [" + LangModel.getString("node" + getTyp()) + "]";
	}

//Возвращяет длинну линий внутри данного узла,
//пересчитанную на коэффициент топологической привязки
	public double getPhysicalLength()
	{
		return 0.0;
	}

	public PathElement countPhysicalLength(SchemePath sp, PathElement pe, Enumeration pathelements)
	{
		return pe;
	}
}
