package com.syrus.AMFICOM.Client.General.Scheme;

import java.io.Serializable;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.jgraph.graph.*;
import com.jgraph.pad.*;
import com.jgraph.pad.GPGraph.*;
import com.jgraph.plaf.GraphUI;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemeGraph extends GPGraph
{
	Color defaultBorderColor = Color.black;
	public Dimension actualSize = Constants.A4;
	Dispatcher dispatcher;
	ApplicationContext aContext;
	UgoPanel panel;
	public SchemePath currentPath;
	public String mode = Constants.linkMode;

	public boolean make_notifications = true;

	protected boolean show_grid_at_actual_size = false;
	protected boolean border_visible = false;
	protected static boolean skip_notify = false;
	boolean can_be_editable = false;

	public boolean is_debug = false;
	private boolean changed = false;

	public SchemeGraph(ApplicationContext aContext, UgoPanel panel)
	{
		this(null, aContext, panel);
	}

	public SchemeGraph(GraphModel model, ApplicationContext aContext, UgoPanel panel)
	{
		this(model, null, aContext, panel);
	}

	public SchemeGraph(GraphModel model, GraphLayoutCache view, ApplicationContext aContext, UgoPanel panel)
	{
		super(model, view);
		this.aContext = aContext;
		this.panel = panel;

		setMarqueeHandler(new ShemeMarqueeHandler());
		init_module();
	}

	void init_module()
	{
		dispatcher = aContext.getDispatcher();
	}

	public void setGraphChanged(boolean b)
	{
		changed = b;
	}

	public boolean isGraphChanged()
	{
		return changed;
	}

	public void setActualSize(Dimension d)
	{
		super.setPreferredSize(new Dimension(
				(int)(getScale()*d.width),
				(int)(getScale()*d.height)));
		actualSize = d;
	}

	public void updateUI()
	{
		setUI(new SchemeGraphUI());
		invalidate();
	}

	public void update()
	{
		updateUI();
		setGridColor(Color.lightGray);
	}

	public GraphUI getUI()
	{
		return (SchemeGraphUI) ui;
	}

	public void setBorderVisible (boolean b)
	{
		border_visible = b;
	}

	public boolean isBorderVisible ()
	{
		return border_visible;
	}

	public void setGridVisibleAtActualSize(boolean b)
	{
		show_grid_at_actual_size = b;
	}

	public boolean isGridVisibleAtActualSize()
	{
		return show_grid_at_actual_size;
	}

	public DefaultGraphCell addVertex(Object userObject, Rectangle bounds, boolean autosize, Color border)
	{
		return GraphActions.addVertex(SchemeGraph.this, userObject, bounds, autosize, false, true, border);
	}

	protected VertexView createVertexView(Object v, CellMapper cm)
	{
		if (v instanceof DeviceCell)
			return new DeviceView(v, this, cm);
		else if (v instanceof PortCell)
			return new DefaultPortView(v, this, cm);
		else if (v instanceof DeviceGroup)
			return new SchemeVertexView(v, this, cm);
		return super.createVertexView(v, cm);
	}

	public class SchemeVertexView extends VertexView {

		public SchemeVertexView(Object v, SchemeGraph graph, CellMapper cm) {
			super(v, graph, cm);
		}

		public CellViewRenderer getRenderer() {
			return DeviceView.renderer;
		}

		public Rectangle getPureBounds() {
			return bounds;
		}
	}

	protected EdgeView createEdgeView(Edge e, CellMapper cm)
	{
		return new LinkView(e, this, cm, 0);
	}


	public Point snap(Point p) {
		Point p2 = new Point(fromScreen(p));
		if (gridEnabled && p != null) {
			p2.x = p.x + gridSize / 2;
			p2.y = p.y + gridSize / 2;
			p2.x = Math.round(p2.x / gridSize) * gridSize;
			p2.y = Math.round(p2.y / gridSize) * gridSize;
		}
		return toScreen(p2);
	}

	public Dimension snap(Dimension d) {
		return super.snap(d);
	}

	public Point toScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x * scale);
		p.y = (int) Math.round(p.y * scale);
		return p;
	}

	public Point fromScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x / scale);
		p.y = (int) Math.round(p.y / scale);
		return p;
	}

	public Rectangle toScreen(Rectangle rect) {
		if (rect == null)
			return null;
		rect.x *= scale;
		rect.y *= scale;
		rect.width *= scale;
		rect.height *= scale;
		return rect;
	}

	public Rectangle fromScreen(Rectangle rect) {
		if (rect == null)
			return null;
		rect.x /= scale;
		rect.y /= scale;
		rect.width /= scale;
		rect.height /= scale;
		return rect;
	}

	public void selectionNotify()
	{
		if (skip_notify || !make_notifications)
			return;

		//skip_notify = true;

		Object[] cells = getSelectionCells();

		if (cells.length == 0 && panel.scheme != null)
		{
			Notifier.selectionNotify(dispatcher, new Scheme[] {panel.scheme}, can_be_editable);
			((ShemeMarqueeHandler)getMarqueeHandler()).enableButtons(cells);
		}
		else
		{
			Notifier.selectionNotify(dispatcher, cells, can_be_editable, mode, is_debug);
			((ShemeMarqueeHandler)getMarqueeHandler()).enableButtons(cells);
		}

		skip_notify = false;
	}

	public void addSelectionCell(Object cell)
	{
		super.addSelectionCell(cell);
		selectionNotify();
	}

	public void addSelectionCells(Object[] cells)
	{
		super.addSelectionCells(cells);
		selectionNotify();
	}

	public void setSelectionCell(Object cell)
	{
		super.setSelectionCell(cell);
		selectionNotify();
	}

	public void setSelectionCells(Object[] cells)
	{
		super.setSelectionCells(cells);
		selectionNotify();
	}

	public void removeSelectionCell(Object cell)
	{
		super.removeSelectionCell(cell);
		selectionNotify();
	}

	public void removeSelectionCells()
	{
		setSelectionCells(new Object[0]);
		selectionNotify();
	}

	public void removeAll()
	{
		getModel().remove(getDescendants(getAll()));
	}

	public Object[] getPathElements(SchemePath path)
	{
		Object[] cells = getAll();
		ArrayList new_cells = new ArrayList();
		ArrayList links = new ArrayList();
		for (Iterator it = path.links.iterator(); it.hasNext();)
			links.add(((PathElement)it.next()).link_id);

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultCableLink)
			{
			DefaultCableLink cable = (DefaultCableLink)cells[i];
			if (links.contains(cable.getSchemeCableLinkId()))
				new_cells.add(cable);
		}
		else if (cells[i] instanceof DefaultLink)
		{
			DefaultLink link = (DefaultLink)cells[i];
			if (links.contains(link.getSchemeLinkId()))
				new_cells.add(link);
		}
		}
		return new_cells.toArray();
	}

	public Object[] getPathElements(TransmissionPath path)
	{
		Object[] cells = getAll();
		ArrayList new_cells = new ArrayList();
		ArrayList links = new ArrayList();
		for (Iterator it = path.links.iterator(); it.hasNext();)
		{
			TransmissionPathElement tpe = (TransmissionPathElement)it.next();
//			Link link = (Link)Pool.get(Link.typ, tpe.link_id);
			links.add(tpe.link_id);//link.getId()
		}

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultCableLink)
			{
			DefaultCableLink cable = (DefaultCableLink)cells[i];
			if (!cable.getSchemeCableLinkId().equals(""))
				if (links.contains(cable.getSchemeCableLink().cable_link_id))
					new_cells.add(cable);
		}
		else if (cells[i] instanceof DefaultLink)
		{
			DefaultLink link = (DefaultLink)cells[i];
			if (!link.getSchemeLinkId().equals(""))
				if (links.contains(link.getSchemeLink().link_id))
					new_cells.add(link);
		}
		}
		return new_cells.toArray();
	}

/*
	public void setSelectionPath(Object cell)
	{
		if (cell instanceof DefaultLink)
		{
			DefaultLink link = (DefaultLink)cell;
			if (!link.getSchemePathId().equals(""))
			{
				currentPath = link.getSchemePath();
				Object[] cells = getPathElements(currentPath);
				setSelectionCells(cells);
				return;
			}
		}
		else if(cell instanceof DefaultCableLink)
		{
			DefaultCableLink link = (DefaultCableLink)cell;
			if (!link.getSchemePathId().equals(""))
			{
				currentPath = link.getSchemePath();
				Object[] cells = getPathElements(currentPath);
				setSelectionCells(cells);
				return;
			}
		}
	}
*/
	public Serializable getArchiveableState()
	{
		return getArchiveableState(getRoots());
	}

	public Serializable getArchiveableState(Object[] cells)
	{
		Object[] flat = DefaultGraphModel.getDescendants(getModel(), cells).toArray();
		ConnectionSet cs = ConnectionSet.create(getModel(), flat, false);
		Map viewAttributes = GraphConstants.createAttributes(cells, getGraphLayoutCache());
		ArrayList v = new ArrayList(3);
		v.add(cells);
		v.add(viewAttributes);
		v.add(cs);
		return v;
	}

	public Object[] setFromArchivedState(Object s)
	{
		if (s instanceof List)
		{
			List v = (List) s;
			Object[] cells = (Object[]) v.get(0);
			Map viewAttributes = (Map) v.get(1);
			ConnectionSet cs = (ConnectionSet) v.get(2);
			getGraphLayoutCache().insert(cells, viewAttributes, cs, null, null);

			return cells;
		}
		return null;
	}

	public Map copyFromArchivedState_virtual(Object s)
	{
		if (s instanceof List)
		{
			List v = (List) s;
			Object[] cells = (Object[]) v.get(0);
			Map viewAttributes = (Map) v.get(1);
			ConnectionSet cs = (ConnectionSet) v.get(2);

			ArrayList new_cells = new ArrayList();
			for (int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceGroup)
					new_cells.add(cells[i]);
			}
			DeviceGroup[] groups = (DeviceGroup[])new_cells.toArray(new DeviceGroup[new_cells.size()]);

			if (groups.length == 0)
			{
				getGraphLayoutCache().insert(cells, viewAttributes, cs, null, null);
				return new HashMap();
			}

			// клонируем селлы
			Map clones = cloneCells(cells);
			// клонируем аттрубуты
			Map cell_attr = GraphConstants.createAttributes(cells, getGraphLayoutCache());
			Map new_attributes = GraphConstants.cloneMap(cell_attr);
			// клонируем коннекшены
			Object[] flat = getDescendants(cells);
			//ConnectionSet cs = ConnectionSet.create(getGraphLayoutCache().getModel(), flat, false);
			cs = cs.clone(clones);
			// устанавливаем аттрибуты дл€ клона
			new_attributes = GraphConstants.replaceKeys(clones, new_attributes);
			// вставл€ем клонированные селлы
			Object[] cloned_cells = clones.values().toArray();
			return clones;
		}
		return null;
	}

	public Map copyFromArchivedState(Object s, Point p)
	{
		if (s instanceof List)
		{
			List v = (List) s;
			Object[] cells = (Object[]) v.get(0);
			Map viewAttributes = (Map) v.get(1);
			ConnectionSet cs = (ConnectionSet) v.get(2);

			ArrayList new_cells = new ArrayList();
			for (int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceGroup)
					new_cells.add(cells[i]);
			}
			DeviceGroup[] groups = (DeviceGroup[])new_cells.toArray(new DeviceGroup[new_cells.size()]);

			if (groups.length == 0)
			{
				getGraphLayoutCache().insert(cells, viewAttributes, cs, null, null);
				return new HashMap();
			}

			// клонируем селлы
			Map clones = cloneCells(cells);
			// клонируем аттрубуты
			Map cell_attr = GraphConstants.createAttributes(cells, getGraphLayoutCache());
			Map new_attributes = GraphConstants.cloneMap(cell_attr);
			// клонируем коннекшены
			Object[] flat = getDescendants(cells);
			//ConnectionSet cs = ConnectionSet.create(getGraphLayoutCache().getModel(), flat, false);
			cs = cs.clone(clones);
			// устанавливаем аттрибуты дл€ клона
			new_attributes = GraphConstants.replaceKeys(clones, new_attributes);
			// вставл€ем клонированные селлы
			Object[] cloned_cells = clones.values().toArray();
			getGraphLayoutCache().insert(cloned_cells, viewAttributes, cs, null, null);


			if (p != null)
			{
//				Point setpoint = snap(p);
//				Rectangle rect;
//				CellView[] cv = getGraphLayoutCache().getMapping(cloned_cells);
//				//CellView[] cv2 = getGraphLayoutCache().getAllDescendants(cv);
//				CellView topcv = cv[0];
//				for (int i = 0; i < cv.length; i++)
//					if (cv[i] instanceof DeviceView)
//					{
//						topcv = cv[i];
//						break;
//					}
//				if (topcv instanceof SchemeVertexView)
//					rect = ((SchemeVertexView)topcv).getPureBounds();
//				else
//					rect = topcv.getBounds();
//
//				p = snap(new Point((int)(p.x / (2 * getScale()) - rect.x / 2), (int)(p.y / (2 * getScale()) - rect.y / 2)));
//				//getGraphLayoutCache().update(getGraphLayoutCache().getAllDescendants(cv));
//				getGraphLayoutCache().setRememberCellViews(false);
//				getGraphLayoutCache().translateViews(cv, p.x, p.y);
//				getGraphLayoutCache().update(cv);
			}

			return clones;
		}
		return null;
	}

	public void setGraphEditable(boolean b)
	{
		//setBendable(b);
		setEditable(b);
//		setMoveable(b);
		setSizeable(b);
		((ShemeMarqueeHandler)getMarqueeHandler()).setGraphEditable(b);
	}

	public void setSendEvents(boolean b)
	{
		make_notifications = false;
		((ShemeMarqueeHandler)getMarqueeHandler()).setSendEvents(b);
	}

	/**
	 * MarqueeHandler that can insert cells.
	 */
	public class ShemeMarqueeHandler extends GPGraph.GPMarqueeHandler
	{
		public transient JToggleButton ce = new JToggleButton();
		public transient JToggleButton dev = new JToggleButton();
		public transient JToggleButton p1 = new JToggleButton();
		public transient JToggleButton p2 = new JToggleButton();
		public transient JToggleButton s_cell = new JToggleButton();
		public transient JButton gr = new JButton();
		public transient JButton ugr = new JButton();
		public transient JButton undo = new JButton();
		public transient JButton redo = new JButton();
		public transient JButton zi = new JButton();
		public transient JButton zo = new JButton();
		public transient JButton za = new JButton();
		public transient JButton del = new JButton();
		public transient JButton hup = new JButton();
		public transient JButton addlib = new JButton();
		public transient JButton ugo = new JButton();
		public transient JButton scheme_ugo = new JButton();
		public transient JButton bp = new JButton();
		public transient JButton bSize = new JButton();
		public transient JToggleButton pathButt = new JToggleButton();
		public transient JToggleButton linkButt = new JToggleButton();

		transient ProtoElement setting_proto = null;
		private transient boolean isEditable = true;
		private transient boolean sendEvents = true;

		// Update Undo/Redo Button State based on Undo Manager
		protected void updateHistoryButtons(GraphUndoManager undoManager)
		{
			undo.setEnabled(undoManager.canUndo(getGraphLayoutCache()));
			redo.setEnabled(undoManager.canRedo(getGraphLayoutCache()));
		}

		void setGraphEditable(boolean b)
		{
			isEditable = b;
		}

		void setSendEvents(boolean b)
		{
			sendEvents = b;
		}

		void enableButtons(Object[] cells)
		{
			ugo.setEnabled((getAll().length == 0) ? false : true);
			del.setEnabled(cells.length != 0 && !GraphActions.hasGroupedParent(cells[0]));
			ugr.setEnabled(false);
			gr.setEnabled(false);
			addlib.setEnabled(false);
			p1.setEnabled(false);
			p2.setEnabled(false);
			bp.setEnabled(false);
			{
				int ports = 0;
				PortCell port = null;
				int cable_ports = 0;
				CablePortCell cable_port = null;
				int devices = 0;
				DeviceCell device = null;
				int groups = 0;
				DeviceGroup group = null;
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof DeviceCell)
							{ devices++; device = (DeviceCell)cells[i]; }
					else if (cells[i] instanceof DeviceGroup)
							{ groups++; group = (DeviceGroup)cells[i]; }
					else if (cells[i] instanceof PortCell)
							{ ports++; port = (PortCell)cells[i]; }
					else if (cells[i] instanceof CablePortCell)
							{ cable_ports++; cable_port = (CablePortCell)cells[i]; }
				}
				if ((ports == 1 && cable_ports == 0 && devices == 0 && groups == 0) ||
						(ports == 0 && cable_ports == 1 && devices == 0 && groups == 0))
					bp.setEnabled(true);
				if (devices == 1 && groups == 0)
				{
					if (!GraphActions.hasGroupedParent(device))
					{
						p1.setEnabled(true);
						p2.setEnabled(true);
						if (device.getChildCount() > 1)
							gr.setEnabled(true);
					}
				}
				if (groups == 1)
				{
					//ugo.setEnabled(true);
					addlib.setEnabled(true);
				}
				if (groups > 0 )
					ugr.setEnabled(true);
				if (groups > 1)
					gr.setEnabled(true);
			}
		}

		public void mousePressed(MouseEvent event)
		{
			if (!isEditable)
			{
				super.mousePressed(event);
				return;
			}


			if (p1.isSelected() || p2.isSelected())
			{
				GraphActions.CreateVisualPortAction(SchemeGraph.this,
						fromScreen(snap (event.getPoint())),
						p1.isSelected(), "");

				repaint();
				event.consume();
			}
			if (ce.isSelected() && firstPort != null)
				start = toScreen(firstPort.getLocation(null));
			super.mousePressed(event);
		}

		public void mouseDragged(MouseEvent event)
		{
			if (!isEditable)
				return;

			if (p1.isSelected() || p2.isSelected())
				event.consume();
			else if (!event.isConsumed() && !s.isSelected())
			{
				Graphics g = getGraphics();
				Color bg = getBackground();
				Color fg = Color.black;
				g.setColor(fg);
				g.setXORMode(bg);
				overlay(g);
				current = snap(event.getPoint());
				if (e.isSelected() || l.isSelected() || ce.isSelected())
				{
					port = getPortViewAt(event.getX(), event.getY(), !event.isShiftDown());
					if (port != null)
					{
						Port p1 = (Port)port.getCell();
						Map map = getModel().getAttributes(p1);
						if (map != null)
						{
							if (!GraphConstants.isConnectable(map))
								port = null;
							else
								current = toScreen(port.getLocation(null));
						}
					}
				}
				bounds = new Rectangle(start).union(new Rectangle(current));
				g.setColor(bg);
				g.setXORMode(fg);
				overlay(g);
				event.consume();
			}
			super.mouseDragged(event);
		}

		public void mouseReleased(MouseEvent event)
		{
			/*if (!isEditable)
			{
				selectionNotify();
				return;
			}*/
			if (!sendEvents)
				return;

			if (!SwingUtilities.isRightMouseButton(event))
			{
				if (event != null && !event.isConsumed() && bounds != null && !s.isSelected())// && !r.isSelected() && !e.isSelected())
				{
					if (dev.isSelected())
					{
						fromScreen(bounds);
						bounds.width+=2;
						bounds.height+=2;
						DefaultGraphCell cell = GraphActions.CreateDeviceAction(SchemeGraph.this, "", bounds, false, Color.black);
						reinit();
					}
					else if (ce.isSelected())
					{
						if (start == null || current == null)
							return;

						if (panel instanceof SchemePanel)
						GraphActions.CreateCableLinkAction(SchemeGraph.this, (SchemePanel)panel,
								firstPort,
								port,
								fromScreen(new Point(start)),
								fromScreen(new Point(current)));
						event.consume();
						reinit();
					}
					else if (e.isSelected())
					{
						if (start == null || current == null)
							return;

						GraphActions.CreateLinkAction(SchemeGraph.this, panel,
								firstPort,
								port,
								fromScreen(new Point(start)),
								fromScreen(new Point(current)));
						event.consume();
						reinit();
					}
					else if (l.isSelected())
					{
						Point p = fromScreen(new Point(start));
						Point p2 = toScreen(new Point(current));
						List list = new ArrayList();
						list.add(p);
						list.add(p2);
						Map map = GraphConstants.createMap();
						GraphConstants.setPoints(map, list);
						GraphConstants.setLineEnd(map, GraphConstants.ARROW_NONE);
						GraphConstants.setEndFill(map, true);
						Map viewMap = new HashMap();
						DefaultEdge cell = new DefaultEdge("");
						viewMap.put(cell, map);
						Object[] insert = new Object[] { cell };
						ConnectionSet cs = new ConnectionSet();
						if (firstPort != null)
							cs.connect(cell, firstPort.getCell(), true);
						if (port != null)
							cs.connect(cell, port.getCell(), false);
						getModel().insert(insert, viewMap, cs, null, null);
						event.consume();
						reinit();
					}
					else if (t.isSelected())
					{
						DefaultGraphCell cell = GraphActions.addVertex(SchemeGraph.this, "“екст", bounds, true, false, false, null);
						startEditingAtCell(cell);
						event.consume();
					}
				}
				super.mouseReleased(event);
				//getUI().selectCellsForEvent(SchemeGraph.this, SchemeGraph.this.getSelectionCells(), event);
				//setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				//repaint();
				selectionNotify();
			}
			else // right mouse button pressed
			{
					if (!isSelectionEmpty())
					{
						Object cell = getSelectionCell();
						DeviceGroup group = null;

						if (cell instanceof DeviceGroup)
							group = (DeviceGroup)cell;
						else if (cell instanceof DeviceCell && ((DeviceCell)cell).getParent() instanceof DeviceGroup)
							group = (DeviceGroup)((DeviceCell)cell).getParent();

						if (group!= null)
						{
							JPopupMenu pop = SchemeActions.createElementPopup(aContext, SchemeGraph.this, group);
							if(pop.getSubElements().length != 0)
								pop.show(SchemeGraph.this, event.getX(), event.getY());
						}
					/*	if (cell instanceof DefaultLink)
						{
							JPopupMenu pop = SchemeActions.createLinkPopup(aContext, SchemeGraph.this, (DefaultLink)cell);
							if(pop.getSubElements().length != 0)
								pop.show(SchemeGraph.this, event.getX(), event.getY());
						}
						if (cell instanceof DefaultCableLink)
						{
							JPopupMenu pop = SchemeActions.createCableLinkPopup(aContext, SchemeGraph.this, (DefaultCableLink)cell);
							if(pop.getSubElements().length != 0)
								pop.show(SchemeGraph.this, event.getX(), event.getY());
						}*/
					}
					repaint();
			}
		}

		void reinit()
		{
			s.doClick();
			firstPort = null;
			port = null;
			start = null;
			current = null;
			bounds = null;
			repaint();
		}

		public void mouseMoved(MouseEvent event)
		{
			if (!isEditable)
				return;

			 if (!s.isSelected() && !event.isConsumed() || setting_proto != null)
			 {
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				event.consume();
				if (e.isSelected() || l.isSelected() || ce.isSelected())
				{
					PortView oldPort = port;
					PortView newPort = getPortViewAt(event.getX(), event.getY(), !event.isShiftDown());

					if (newPort != null)
					{
						Port p1 = (Port)newPort.getCell();
						Map map = getModel().getAttributes(p1);
						if (oldPort != newPort && GraphConstants.isConnectable(map))
						{
							Graphics g = getGraphics();
							Color bg = getBackground();
							Color fg = getMarqueeColor();
							g.setColor(fg);
							g.setXORMode(bg);
							overlay(g);
							port = newPort;
							g.setColor(bg);
							g.setXORMode(fg);
							overlay(g);
						}
					}
					else
					{
						Graphics g = getGraphics();
						Color bg = getBackground();
						Color fg = getMarqueeColor();
						g.setColor(fg);
						g.setXORMode(bg);
						overlay(g);
						port = newPort;
						g.setColor(bg);
						g.setXORMode(fg);
						overlay(g);
					}
				}
			}
			super.mouseMoved(event);
		}

		public void overlay(Graphics g)
		{
			if (marqueeBounds != null)
				g.drawRect(marqueeBounds.x, marqueeBounds.y, marqueeBounds.width,	marqueeBounds.height);
			paintPort(getGraphics());
			if (bounds != null && start != null)
			{
				if (i.isSelected() || z.isSelected())
					((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
				if (c.isSelected())
					g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
				else if ((l.isSelected() || e.isSelected() || ce.isSelected()) && current != null)
					g.drawLine(start.x, start.y, current.x, current.y);
				else if (!s.isSelected())
					g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}
	}
}

