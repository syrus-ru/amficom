package com.syrus.AMFICOM.Client.General.Scheme;

import java.io.Serializable;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.jgraph.graph.*;
import com.jgraph.pad.*;
import com.jgraph.graph.Port;
import com.jgraph.pad.GPGraph.*;
import com.jgraph.plaf.GraphUI;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;
import com.syrus.AMFICOM.scheme.SchemeUtils;

public class SchemeGraph extends GPGraph
{
	Color defaultBorderColor = Color.black;
	public Dimension actualSize = Constants.A4;
	Dispatcher dispatcher;
	ApplicationContext aContext;
	/**
	 * trigger between path selection modes
	 */
	public String mode = Constants.LINK_MODE;
	/**
	 * trigger between path creation modes
	 */

	private boolean topLevelSchemeMode = false;

	public static int path_creation_mode = Constants.NORMAL;

	protected boolean show_grid_at_actual_size = false;
	protected boolean border_visible = false;
	protected static boolean skip_notify = false;
	boolean can_be_editable = false;

	public boolean is_debug = false;
	private boolean changed = false;

	SchemeGraphResource graphResource;

	public SchemeGraph(ApplicationContext aContext)
	{
		this(null, aContext);
	}

	public SchemeGraph(GraphModel model, ApplicationContext aContext)
	{
		this(model, null, aContext);
	}

	public SchemeGraph(GraphModel model, GraphLayoutCache view, ApplicationContext aContext)
	{
		super(model, view);

		graphResource = new SchemeGraphResource(this);
		setMarqueeHandler(new ShemeMarqueeHandler());

		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext)
	{
		if (aContext != null)
		{
			this.aContext = aContext;
			init_module();
		}
	}

	void init_module()
	{
		dispatcher = aContext.getDispatcher();
	}

	public SchemeGraphResource getGraphResource()
	{
		return graphResource;
	}

	public Scheme getScheme()
	{
		return getGraphResource().scheme;
	}

	public void setScheme(Scheme scheme)
	{
		getGraphResource().scheme = scheme;
	}

	public void setSchemeElement(SchemeElement schemeelement)
	{
		getGraphResource().schemeelement = schemeelement;
	}

	public SchemeElement getSchemeElement()
	{
		return getGraphResource().schemeelement;
	}

	public void setCurrentPath(SchemePath path)
	{
		getGraphResource().setCurrentPath(path);
	}

	public SchemePath getCurrentPath()
	{
		return getGraphResource().getCurrentPath();
	}

	public void setTopLevelSchemeMode(boolean b)
	{
		topLevelSchemeMode = b;

		Scheme scheme = getScheme();
		if (scheme == null)
			return;

		if (b)
			generateTopLevelScheme();
	}

	public boolean isTopLevelSchemeMode()
	{
		return topLevelSchemeMode;
	}

	void generateTopLevelScheme()
	{
		Map oldToNewMap = new HashMap();
		Map map2 = new HashMap();
		Object[] cells = getRoots();

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DeviceGroup)
			{
				DeviceGroup group = (DeviceGroup)cells[i];
				if (!isCableGroup(group))
				{
					DeviceGroup newGroup = createGroup(group);
					if (newGroup != null)
						oldToNewMap.put(group, newGroup);
				}
			}
		}
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultCableLink)
			{
				DefaultCableLink link = (DefaultCableLink)cells[i];
				DeviceGroup start = null;
				DeviceGroup end = null;
				if (link.getSource() instanceof DefaultPort &&
						((DefaultPort)link.getSource()).getParent().getParent() instanceof DeviceGroup)
					start = (DeviceGroup)((DefaultPort)link.getSource()).getParent().getParent();
				if (link.getTarget() instanceof DefaultPort &&
						((DefaultPort)link.getTarget()).getParent().getParent() instanceof DeviceGroup)
					end = (DeviceGroup)((DefaultPort)link.getTarget()).getParent().getParent();

				boolean b1 = !isCableGroup(start);
				boolean b2 = !isCableGroup(end);
				if (b1 && b2)
				{
					DeviceGroup newStart = (DeviceGroup)oldToNewMap.get(start);
					DeviceGroup newEnd = (DeviceGroup)oldToNewMap.get(end);
					GraphActions.CreateTopLevelCableLinkAction(this,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							getCellBounds(newStart).getLocation(),
							getCellBounds(newEnd).getLocation());
				}
				else if (b1)
				{
					DeviceGroup newStart = (DeviceGroup)oldToNewMap.get(start);
					DeviceGroup newEnd = (DeviceGroup)map2.get(end);
					if (newEnd == null)
						map2.put(start, newStart);
					else
						GraphActions.CreateTopLevelCableLinkAction(this,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							getCellBounds(newStart).getLocation(),
							getCellBounds(newEnd).getLocation());
				}
				else if (b2)
				{
					DeviceGroup newEnd = (DeviceGroup)oldToNewMap.get(end);
					DeviceGroup newStart = (DeviceGroup)map2.get(start);
					if (newStart == null)
						map2.put(end, newEnd);
					else
						GraphActions.CreateTopLevelCableLinkAction(this,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							getCellBounds(newStart).getLocation(),
							getCellBounds(newEnd).getLocation());
				}
			}
		}
		getModel().remove(getDescendants(cells));
	}

	boolean isCableGroup(DeviceGroup group)
	{
		if (group.getScheme() == null)
			return false;
		if (group.getScheme().type().equals(Type.CABLE_SUBNETWORK))
			return true;
		return false;
	}

	DeviceGroup createGroup(DeviceGroup group)
	{
		DeviceGroup newGroup = null;
		Rectangle r = null;
		for (Iterator it = group.getChildren().iterator(); it.hasNext(); )
		{
			Object child = it.next();
			if (child instanceof DeviceCell)
			{
				DeviceCell dev = (DeviceCell) child;
				Rectangle cr = getCellBounds(dev);
				r = toScreen(new Rectangle(cr.getLocation(),
																	 new Dimension(4 * getGridSize(), 6 * getGridSize())));
				break;
			}
		}

		if (r != null)
		{
			newGroup = GraphActions.CreateTopLevelElementAction(this,
					group.getUserObject(),
					r,
					false,
					Color.BLACK);
			newGroup.setSchemeElementId(group.getSchemeElementId());
			newGroup.setSchemeId(group.getSchemeId());
			newGroup.setProtoElementId(group.getProtoElementId());
		}
		return newGroup;
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
		if (dispatcher == null || skip_notify)
			return;

		skip_notify = true;

		Object[] cells = getSelectionCells();

		if (cells.length == 0)
		{
			if (graphResource.scheme != null)
			{
				Notifier.selectionNotify(dispatcher, new Scheme[] {graphResource.scheme}, can_be_editable);
				((ShemeMarqueeHandler)getMarqueeHandler()).enableButtons(cells);
			}
			else if (graphResource.schemeelement != null)
			{
				Notifier.selectionNotify(dispatcher, new SchemeElement[] {graphResource.schemeelement}, can_be_editable);
				((ShemeMarqueeHandler)getMarqueeHandler()).enableButtons(cells);
			}
		}
		else
		{
			Notifier.selectionNotify(dispatcher, this, can_be_editable);
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
/*
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
*/
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

//	public void setSendEvents(boolean b)
//	{
//		((ShemeMarqueeHandler)getMarqueeHandler()).setSendEvents(b);
//	}

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
		public transient JButton gr2 = new JButton();
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
		public transient JButton topModeButt = new JButton();

		transient SchemeProtoElement settingProto = null;
		private transient boolean isEditable = true;
//		private transient boolean sendEvents = true;

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

//		void setSendEvents(boolean b)
//		{
//			sendEvents = b;
//		}

		void enableButtons(Object[] cells)
		{
			ugo.setEnabled((getAll().length == 0) ? false : true);
			del.setEnabled(cells.length != 0 && !GraphActions.hasGroupedParent(cells[0]));
			ugr.setEnabled(false);
			gr.setEnabled(false);
			gr2.setEnabled(false);
			addlib.setEnabled(false);
			p1.setEnabled(false);
			p2.setEnabled(false);
			bp.setEnabled(false);
			{
				int ports = 0;
				PortCell port = null;
				int cablePorts = 0;
				CablePortCell cablePort = null;
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
							{ cablePorts++; cablePort = (CablePortCell)cells[i]; }
				}
				if ((ports == 1 && cablePorts == 0 && devices == 0 && groups == 0) ||
						(ports == 0 && cablePorts == 1 && devices == 0 && groups == 0))
					bp.setEnabled(true);
				if (devices == 1 && groups == 0)
				{
					if (!GraphActions.hasGroupedParent(device))
					{
						p1.setEnabled(true);
						p2.setEnabled(true);
						if (device.getChildCount() > 1)
						{
							gr.setEnabled(true);
							gr2.setEnabled(true);
						}
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
				{
					gr.setEnabled(true);
					gr2.setEnabled(true);
				}
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
//			if (!sendEvents)
//				return;

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

						SchemeCableLink link = GraphActions.CreateCableLinkAction(SchemeGraph.this,
								firstPort,
								port,
								fromScreen(new Point(start)),
								fromScreen(new Point(current)));

						boolean inserted = false;
						Arrays.asList(getScheme().schemeCableLinks()).add(link);
						if (!getScheme().type().equals(Type.CABLE_SUBNETWORK))
						{
							if (link.sourceSchemeCablePort() != null)
							{
								SchemeElement se = SchemeUtils.getSchemeElementByDevice(
										getScheme(), link.sourceSchemeCablePort().schemeDevice());
								if (se.internalScheme() != null)
								{
									Scheme source_scheme = se.internalScheme();
									if (source_scheme.type().equals(Type.CABLE_SUBNETWORK))
									{
										link.scheme(source_scheme);
//										source_scheme.cablelinks.add(link);
										inserted = true;
									}
								}
							}
							if (!inserted && link.targetSchemeCablePort() != null)
							{
								SchemeElement se = SchemeUtils.getSchemeElementByDevice(
										getScheme(), link.targetSchemeCablePort().schemeDevice());
								if (se.internalScheme() != null)
								{
									Scheme target_scheme = se.internalScheme();
									if (target_scheme.type().equals(Type.CABLE_SUBNETWORK))
									{
										link.scheme(target_scheme);
//										target_scheme.cablelinks.add(link);
										inserted = true;
									}
								}
							}
						}
						if (!inserted)
						{
							link.scheme(getScheme());
//							getScheme().cablelinks.add(link);
						}

						event.consume();
						reinit();
					}
					else if (e.isSelected())
					{
						if (start == null || current == null)
							return;

						SchemeLink link = GraphActions.CreateLinkAction(SchemeGraph.this,
								firstPort,
								port,
								fromScreen(new Point(start)),
								fromScreen(new Point(current)));

						if (getScheme() != null)
						{
							Arrays.asList(getScheme().schemeLinks()).add(link);
							link.scheme(getScheme());
						}
						else if (getSchemeElement() != null)
						{
							Arrays.asList(getSchemeElement().schemeLinks()).add(link);
							link.scheme(getSchemeElement().scheme());
						}

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

			 if (!s.isSelected() && !event.isConsumed() || settingProto != null)
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

