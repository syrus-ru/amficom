/**
 * $Id: MapNodeElement.java,v 1.7 2004/09/21 14:56:16 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/09/21 14:56:16 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapNodeElement extends StubResource
    implements MapElement 
{
	/** Размер пиктограммы поумолчанию */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(14, 14);
	/** минимальный размер элемента */
	public static final Rectangle MIN_BOUNDS = new Rectangle(6, 6);
	/** максимальный размер элемента */
	public static final Rectangle MAX_BOUNDS = new Rectangle(40, 40);

	/** Размер пиктограммы в экранных координатах */
	protected Rectangle bounds = new Rectangle(DEFAULT_BOUNDS);

	/** коэффициент масштабирования пиктограммы по умолчанию */
	public static final double DEFAULT_SCALE_COEFFICIENT = 1.0;

	/** коэффициент масштабирования пиктограммы */
	double scaleCoefficient = DEFAULT_SCALE_COEFFICIENT;

	/** пиктограмма по умолчанию */	
	public static final String DEFAULT_IMAGE = "images/pc.gif";
	
	protected Map map;

	/** флаг выделения элемента */
	protected boolean selected = false;

	/** пиктограмма */
	protected Image icon;

	protected String id = "";
	protected String name = "";
	protected String description = "";
	protected String mapId = "";
	protected String imageId = "";
	
	/** координаты элемента (топографические) */
	protected Point2D.Double anchor = new Point2D.Double( 0, 0);

	public String optimizerAttribute = "optional";// "mandatory", "restricted"

	/** атрибуты отображения */
	public java.util.HashMap attributes = new HashMap();
	
	/** флаг наличия сигнала тревоги */
	protected boolean alarmState = false;
	
	/** объекты, необходимые для отрисовки пиктограмм */
    private static final Component component = new Component() {};
    private static final MediaTracker tracker = new MediaTracker(component);
    private static int mediaTrackerID = 0;

	/** флаг того, что элемент удален */
	protected boolean removed = false;

	public boolean isRemoved()
	{
		return removed;
	}
	
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}
	
	public Rectangle getMinBounds()
	{
		return MIN_BOUNDS;
	}
	
	public Rectangle getMaxBounds()
	{
		return MAX_BOUNDS;
	}

	/**
	 * установить коэффициент масштабирования пиктограммы.
	 * при этом обновляются границы элемента
	 */
	public void setScaleCoefficient(double ss)
	{
		scaleCoefficient = ss;
		int w = (int )((double )getDefaultBounds().width * scaleCoefficient);
		int h = (int )((double )getDefaultBounds().height * scaleCoefficient);
		
		if (w >= getMaxBounds().width || h >= getMaxBounds().height )
		{
			w = getMaxBounds().width;
			h = getMaxBounds().height;
		}
		else
		if (w <= getMinBounds().width || h <= getMinBounds().height )
		{
			w = getMinBounds().width;
			h = getMinBounds().height;
		}
		setBounds(new Rectangle(w, h));

		icon = getImage().getScaledInstance(
			w,
			h,
			Image.SCALE_SMOOTH);
		loadImage(icon);
	}
	
	/**
	 * получить границы элемента
	 */
	public Rectangle getBounds()
	{
		return bounds;
	}

	/**
	 * установить границы элемента
	 */
	public void setBounds(Rectangle rec)
	{
		bounds = rec;
	}

	/**
	 * получить пиктограмму элемента
	 */
	public Image getImage()
	{
		ImageResource ir = ImageCatalogue.get(imageId);
		if(ir == null)
		{
			ImageIcon imageIcon = new ImageIcon(DEFAULT_IMAGE);
			return imageIcon.getImage();
		}
		return ir.getImage();
	}

	/**
	 * установить идентификатор изображения, по которому определяется 
	 * пиктограмма
	 */
	public void setImageId(String iconPath)
	{
		imageId = iconPath;

		int width = (int )Math.round(getBounds().getWidth());
		int height = (int )Math.round(getBounds().getHeight());
		icon = getImage().getScaledInstance(
			width,
			height,
			Image.SCALE_SMOOTH);
		loadImage(icon);
	}

	/**
	 * получить идентификатор пиктограммы
	 */
	public String getImageId()
	{
		return imageId;
	}

	/**
	 * получить копию центра элемента (для избежания его случайной модификации)
	 */
	public Point2D.Double getAnchor()
	{
		return new Point2D.Double(anchor.x, anchor.y);
	}

	/**
	 * установить новые координаты центра элемента
	 */
	public void setAnchor(Point2D.Double aAnchor)
	{
		anchor.x = aAnchor.x;
		anchor.y = aAnchor.y;
	}

	public boolean isSelected ()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDomainId()
	{
		return getMap().getDomainId();
	}
	
	public void paint (Graphics g)
	{
		MapCoordinatesConverter converter = getMap().getConverter();
		
		Point p = converter.convertMapToScreen(getAnchor());

		Graphics2D pg = (Graphics2D )g;
//		pg.setStroke(MapPropertiesManager.getSelectionStroke());
		
		int width = getBounds().width;
		int height = getBounds().height;

		pg.drawImage(
				icon,
                p.x - width / 2,
                p.y - height / 2,
                null);

		// если на элементе есть сигнал тревоги, то анимация
		// в зависимости от флага getShowAlarmed()
		if (this.getAlarmState())
		{
			if ( MapPropertiesManager.isShowAlarmState() )
			{
				pg.setStroke(MapPropertiesManager.getAlarmedStroke());
				pg.setColor(MapPropertiesManager.getAlarmedColor());
				pg.drawRect( 
						p.x - width / 2,
						p.y - height / 2,
						width,
						height);
			}
		}

		//Если выбрано то рисовать рамку
		if (isSelected())
		{
			pg.setStroke(new BasicStroke(3));
			pg.setColor(MapPropertiesManager.getSelectionColor());
			pg.drawRect( 
					p.x - width / 2,
					p.y - height / 2,
					width,
					height);
		}
	}

	public void setAlarmState(boolean i)
	{
		alarmState = i;
	}

	public boolean getAlarmState()
	{
		return alarmState;
	}

	public boolean isMouseOnThisObject (Point currentMousePoint)
	{
		MapCoordinatesConverter converter = getMap().getConverter();
		
		//Проверка того что курсор находиться в прямоугольнике
		int width = (int )getBounds().getWidth();
		int height = (int )getBounds().getHeight();
		Point p = converter.convertMapToScreen(getAnchor());
		Rectangle imageBounds = new Rectangle( 
				p.x - width / 2 , 
				p.y - height / 2 , 
				width, 
				height);
		if (imageBounds.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	public void move (double deltaX, double deltaY)
	{
		setAnchor(new Point2D.Double(getAnchor().x + deltaX, getAnchor().y + deltaY));
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap( Map map)
	{
		this.map = map;
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

	/**
	 * Получить список NodeLinks, содержащих заданный Node
	 */
	public List getNodeLinks()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getNodeLinks()");
		
		LinkedList returnList = new LinkedList();
		for(Iterator it = map.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			
			//Если один из концов является данным node то добавляем его в вектор
			if ( (nodeLink.endNode == this) || (nodeLink.startNode == this))
			{
				returnList.add(nodeLink);
			}
		}

		return returnList;
	}

	/**
	 * Получить список PhysicalLink, содержащих заданный Node
	 */
	public List getPhysicalLinks()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPhysicalLinks()");
		
		LinkedList returnList = new LinkedList();

		for(Iterator it = map.getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement )it.next();
			
			//Если один из концов является данным node то добавляем его в вектор
			if ( (physicalLink.endNode == this) || (physicalLink.startNode == this) )
				returnList.add( physicalLink);
		}

		return returnList;
	}

	/**
	 * Получить вектор Node противоположных у элемета NodeLink по заданному Node
	 */
	public List getOppositeNodes()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getOppositeNodes()");
		
		Iterator e = getNodeLinks().iterator();
		LinkedList returnList = new LinkedList();

		while (e.hasNext())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();

			if ( nodeLink.getEndNode() == this )
				returnList.add(nodeLink.getStartNode());
			else
				returnList.add(nodeLink.getEndNode());
		}

		return returnList;
	}
	
	/**
	 * получить текущее состояние
	 */
	public MapElementState getState()
	{
		return new MapNodeElementState(this);
	}

	/**
	 * восстановить состояние
	 */
	public void revert(MapElementState state)
	{
		MapNodeElementState mnes = (MapNodeElementState )state;
		setBounds(mnes.bounds);
		setScaleCoefficient(mnes.scaleCoefficient);
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		icon = mnes.icon;
		setAnchor(mnes.anchor);
		optimizerAttribute = mnes.optimizerAttribute;
		attributes = new HashMap(mnes.attributes);
	}

	/**
	 * обеспечивает подгрузку пиктограммы для мгновенного ее отображения
	 */
    protected final void loadImage(Image image) 
	{
		synchronized(tracker) 
		{
            int id = getNextID();

			tracker.addImage(image, id);
			try 
			{
				tracker.waitForID(id, 0);
			} 
			catch (InterruptedException e) 
			{
				System.out.println("INTERRUPTED while loading Image");
			}
			tracker.removeImage(image, id);
		}
    }

	/**
	 * получить идентификатор для подгрузки пиктограммы
	 */
    private int getNextID() 
	{
        synchronized(tracker) 
		{
            return ++mediaTrackerID;
        }
    }

}
