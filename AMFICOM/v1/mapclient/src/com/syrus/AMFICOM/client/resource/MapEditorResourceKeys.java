/**
 * $Id: MapEditorResourceKeys.java,v 1.2 2005/09/25 15:53:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.resource;

public interface MapEditorResourceKeys extends ResourceKeys {
	String ICON_CATALOG = "icon.catalog";

	public static final String TITLE_SELECT_ELEMENT = "Title.SelectElement";

	public static final String LABEL_CABLE_LIST = "Label.CableList";

	public static final String TO_SITE_LOWERCASE = "Helper.ToSite_lowercase";

	public static final String FILE_CHOOSER_SELECT_FILE_TO_OPEN = "FileChooser.SelectFileToOpen";
	public static final String FILE_CHOOSER_FILE_EXISTS_OVERWRITE = "FileChooser.FileExists.Overwrite";
	public static final String FILE_CHOOSER_SELECT_FILE_TO_SAVE = "FileChooser.SelectFileToSave";
	public static final String FILE_CHOOSER_EXPORT_SAVE_FILE = "FileChooser.ExportSaveFile";
	public static final String FILE_CHOOSER_PICTURE = "FileChooser.Picture";

	public static final String MESSAGE_UNABLE_TO_PLACE_CABLE = "Message.UnableToPlaceCable";
	public static final String MESASGE_ERROR_OPENING = "Mesasge.ErrorOpening";
	public static final String MESSAGE_UNSAVED_ELEMENTS_PRESENT = "Message.UnsavedElementsPresent";
	public static final String MESSAGE_INFORMATION_IMPORTING_PLS_WAIT = "Message.Information.ImportingPlsWait";
	public static final String MESSAGE_ENTER_COLLECTOR_NAME = "Message.EnterCollectorName";
	public static final String MESSAGE_OPEN_MAP_FRAME_FIRST = "Message.OpenMapFrameFirst";

	public static final String ERROR_MAP_EXCEPTION_TOPOLOGICAL_IMAGE_TIMEOUT = "Error.MapException.TopologicalImageTimeout";
	public static final String ERROR_WRITE_ERROR = "Error.WriteError";
	public static final String ERROR_CABLE_END_ELEMENTS_NOT_PLACED = "Error.CableEndElementsNotPlaced";
	public static final String ERROR_NUMBER_FORMAT = "Error.NumberFormat";
	public static final String ERROR_XML_EXCEPTION = "Error.XmlException";
	public static final String ERROR_APPLICATION_EXCEPTION = "Error.ApplicationException";
	public static final String ERROR_MAP_EXCEPTION_SERVER_CONNECTION = "Error.MapException.ServerConnection";
	public static final String ERROR_MAP_EXCEPTION_DATA = "Error.MapException.Data";
	public static final String ERROR_MAP_VIEW_SAVED_BUT_FAILED_TO_OPEN = "Error.MapViewSavedButFailedToOpen";

	public static final String DOT = ".";
	public static final String EXTENSION_GIF = "gif";
	public static final String EXTENSION_XML = "xml";
	public static final String EXTENSION_ESF = "esf";
	public static final String EXTENSION_DOT_ESF = DOT + EXTENSION_ESF;
	public static final String EXTENSION_DOT_XML = DOT + EXTENSION_XML;

	public static final String EMPTY_STRING = "";
	public static final String IS_ACOPY_IN_PARENTHESIS = "Helper.IsACopyInParenthesis";
	public static final String COPY_OF = "Helper.CopyOf";
	public static final String NONAME = "Helper.NoName";
	public static final String IMPORTED = "Helper.Imported";
	public static final String OUT_OF_LOWERCASE = "Helper.OutOf_lowercase";
	public static final String TO_LOWERCASE = "Helper.To_lowercase";
	public static final String FROM_LOWERCASE = "Helper.From_lowercase";
	public static final String TO_NODE_LOWERCASE = "Helper.ToNode_lowercase";
	public static final String DISTANCE_LOWERCASE = "Helper.Distance_lowercase";
	public static final String PATH_LOWERCASE = "Helper.Path_lowercase";

	public static final String BUTTON_OK = "Button.OK";
	public static final String BUTTON_APPLY = "Button.Apply";
	public static final String BUTTON_CANCEL = "Button.Cancel";
	public static final String BUTTON_CHOOSE = "Button.Choose";
	public static final String BUTTON_CHANGE = "Button.Change";
	public static final String BUTTON_ADD = "Button.Add";
	public static final String BUTTON_REMOVE = "Button.Remove";
	public static final String BUTTON_SELECT_ELEMENT = "Button.SelectElement";
	public static final String BUTTON_UNBIND_CABLE = "Button.UnbindCable";
	public static final String BUTTON_UNBIND = "Button.Unbind";
	public static final String BUTTON_BIND_CHAIN = "Button.BindChain";
	public static final String BUTTON_BIND = "Button.Bind";
	public static final String BUTTON_NUMBERING_DIRECTION = "Button.NumberingDirection";
	public static final String BUTTON_VERTICAL_ORDER = "Button.VerticalOrder";
	public static final String BUTTON_HORIZONTAL_ORDER = "Button.HorizontalOrder";
	public static final String BUTTON_UNBIND_LINK_BINDING = "Button.UnbindLinkBinding";
	public static final String BUTTON_BIND_CABLE_TO_PIPE = "Button.BindCableToPipe";

	public static final String ENTITY_TOPOLOGICAL_NODE = "Entity.TopologicalNode";
	public static final String ENTITY_SITE_NODE = "Entity.SiteNode";
	public static final String ENTITY_ALARM = "Entity.Alarm";
	public static final String ENTITY_CABLEPATH = "Entity.CablePath";
	public static final String ENTITY_COLLECTOR = "Entity.Collector";
	public static final String ENTITY_EVENT = "Entity.Event";
	public static final String ENTITY_MARKER = "Entity.Marker";
	public static final String ENTITY_MARK = "Entity.Mark";
	public static final String ENTITY_MEASUREMENT_PATH = "Entity.MeasurementPath";
	public static final String ENTITY_NODE_LINK = "Entity.NodeLink";
	public static final String ENTITY_PHYSICAL_LINK_TYPE = "Entity.PhysicalLinkType";
	public static final String ENTITY_SITE_NODE_TYPE = "Entity.SiteNodeType";
	public static final String ENTITY_PHYSICAL_LINK = "Entity.PhysicalLink";
	public static final String ENTITY_PIQUET = "Entity.Piquet";

	public static final String LABEL_SCALE = "Label.Scale";
	public static final String LABEL_SITE_BINDING = "Label.SiteBinding";
	public static final String LABEL_SCHEME_ELEMENT = "Label.SchemeElement";
	public static final String LABEL_DISTANCES_TO_NEAREST_NODES = "Label.DistancesToNearestNodes";
	public static final String LABEL_CABLES_BINDING = "Label.CablesBinding";
	public static final String LABEL_CABLE_BINDING = "Label.CableBinding";
	public static final String LABEL_MAP = "Label.Map";
	public static final String LABEL_PLACE_IN_COLLECTOR = "Label.PlaceInCollector";
	public static final String LABEL_PLACE_IN_TUNNEL = "Label.PlaceInTunnel";
	public static final String LABEL_SCHEMES = "Label.Schemes";
	public static final String LABEL_LAYERS = "Label.Layers";
	public static final String LABEL_TOPOLOGICAL_LENGTH = "Label.TopologicalLength";
	public static final String LABEL_PHYSICAL_LENGTH = "Label.PhysicalLength";
	public static final String LABEL_OPTICAL_LENGTH = "Label.OpticalLength";
	public static final String LABEL_STYLE = "Label.Style";
	public static final String LABEL_THICKNESS = "Label.Thickness";
	public static final String LABEL_LABEL = "Label.Label";
	public static final String LABEL_IN_LIBRARY = "Label.InLibrary";
	public static final String LABEL_IMAGE = "Label.Image";
	public static final String LABEL_GEOGRAPHICAL_COORDINATES = "Label.GeographicalCoordinates";
	public static final String LABEL_START_SPARE = "Label.StartSpare";
	public static final String LABEL_END_SPARE = "Label.EndSpare";
	public static final String LABEL_FROM_END = "Label.FromEnd";
	public static final String LABEL_FROM_START = "Label.FromStart";
	public static final String LABEL_TUNNEL = "Label.Tunnel";
	public static final String LABEL_NODE = "Label.Node";
	public static final String LABEL_END_NODE = "Label.EndNode";
	public static final String LABEL_START_NODE = "Label.StartNode";
	public static final String LABEL_SELECTION_COUNT = "Label.SelectionCount";
	public static final String LABEL_ELEMENTS = "Label.Elements";
	public static final String LABEL_DISTANCE = "Label.Distance";
	public static final String LABEL_DIMENSION = "Label.Dimension";
	public static final String LABEL_DOMAIN = "Label.Domain";
	public static final String LABEL_USER = "Label.User";
	public static final String LABEL_MODIFIED = "Label.Modified";
	public static final String LABEL_CREATED = "Label.Created";
	public static final String LABEL_COMPONENTS = "Label.Components";
	public static final String LABEL_COLOR = "Label.Color";
	public static final String LABEL_STREET = "Label.Street";
	public static final String LABEL_ALL = "Label.All";
	public static final String LABEL_ADDRESS = "Label.Address";
	public static final String LABEL_CABLE = "Label.Cable";
	public static final String LABEL_DESCRIPTION = "Label.Description";
	public static final String LABEL_LONGITUDE = "Label.Longitude";
	public static final String LABEL_LATITUDE = "Label.Latitude";
	public static final String LABEL_TYPE = "Label.Type";
	public static final String LABEL_NAME = "Label.Name";
	public static final String LABEL_CITY_KURZ = "Label.CityKurz";
	public static final String LABEL_STREET_KURZ = "Label.StreetKurz";
	public static final String LABEL_BUILDING_KURZ = "Label.BuildingKurz";
	public static final String LABEL_MARKER_INFO = "Label.MarkerInfo";

	public static final String POPUP_GENERATE_CABLING = "Popup.GenerateCabling";
	public static final String POPUP_BIND = "Popup.Bind";
	public static final String POPUP_GENERATE_SITE = "Popup.GenerateSite";
	public static final String POPUP_ATTACH_CABLE_INLET = "Popup.AttachCableInlet";
	public static final String POPUP_ADD_SITE = "Popup.AddSite";
	public static final String POPUP_ADD_MARKER = "Popup.AddMarker";
	public static final String POPUP_CREATE_COLLECTOR = "Popup.CreateCollector";
	public static final String POPUP_ADD_MARK = "Popup.AddMark";
	public static final String POPUP_DELETE = "Popup.Delete";
	public static final String POPUP_REMOVE_FROM_COLLECTOR = "Popup.RemoveFromCollector";
	public static final String POPUP_REMOVE_COLLECTOR = "Popup.RemoveCollector";
	public static final String POPUP_ADD_TO_COLLECTOR = "Popup.AddToCollector";
	public static final String POPUP_PLACE_SITE = "Popup.PlaceSite";
	public static final String POPUP_COPY = "Popup.Copy";

	public static final String STATUS_MAP_OPENING = "Status.MapOpening";
	public static final String STATUS_ADDING_INTERNAL_MAP = "Status.AddingInternalMap";
	public static final String STATUS_ADDING_EXTERNAL_NODE = "Status.AddingExternalNode";
	public static final String STATUS_CREATING_NEW_MAP = "Status.CreatingNewMap";
	public static final String STATUS_MAP_SAVING = "Status.MapSaving";
	public static final String STATUS_REMOVING_SCHEME_FROM_MAP_VIEW = "Status.RemovingSchemeFromMapView";
	public static final String STATUS_MAP_VIEW_OPENING = "Status.MapViewOpening";
	public static final String STATUS_ADDING_SCHEME_TO_MAP_VIEW = "Status.AddingSchemeToMapView";
	public static final String STATUS_REMOVING_INTERNAL_MAP = "Status.RemovingInternalMap";
	public static final String STATUS_MAP_VIEW_SAVING = "Status.MapViewSaving";
	public static final String STATUS_CREATING_NEW_MAP_VIEW = "Status.CreatingNewMapView";

	public static final String TITLE_ADDITIONAL_PROPERTIES = "Title.AdditionalProperties";
	public static final String TITLE_PROPERTIES = "Title.Properties";
	public static final String TITLE_CHARACTERISTICS = "Title.Characteristics";
	public static final String TITLE_CHOOSE_MAP = "Title.ChooseMap";
	public static final String TITLE_CONFIGURE_TOPOLOGICAL_LAYERS = "Title.ConfigureTopologicalLayers";
	public static final String TITLE_IMAGES_DIALOG_IMAGE_SELECTION = "Title.ImagesDialog.ImageSelection";
	public static final String TITLE_MAP = "Title.Map";
	public static final String TITLE_MAP_EDITOR = "Title.MapEditor";
	public static final String TITLE_MAP_LIBRARY = "Title.MapLibrary";
	public static final String TITLE_MAP_VIEW = "Title.MapView";
	public static final String TITLE_MAP_PROPERTIES = "Title.MapProperties";
	public static final String TITLE_MAP_VIEW_PROPERTIES = "Title.MapViewProperties";
	public static final String TITLE_OBJECT_WAS_CHANGED = "Title.ObjectWasChanged";
	public static final String TITLE_NAVIGATOR = "Title.Navigator";
	public static final String TITLE_SCHEME = "Title.Scheme";

	public static final String TOOLTIP_ZOOM_IN = "Tooltip.ZoomIn";
	public static final String TOOLTIP_ZOOM_OUT = "Tooltip.ZoomOut";
	public static final String TOOLTIP_ZOOM_TO_POINT = "Tooltip.ZoomToPoint";
	public static final String TOOLTIP_ZOOM_BOX = "Tooltip.ZoomBox";
	public static final String TOOLTIP_VIEW_NODES = "Tooltip.ViewNodes";
	public static final String TOOLTIP_VIEW_INDICATION = "Tooltip.ViewIndication";
	public static final String TOOLTIP_UNDO = "Tooltip.Undo";
	public static final String TOOLTIP_REDO = "Tooltip.Redo";
	public static final String TOOLTIP_MAP_FRAME_OPTIONS = "Tooltip.MapFrameOptions";
	public static final String TOOLTIP_PHYSICAL_LINK_MODE = "Tooltip.PhysicalLinkMode";
	public static final String TOOLTIP_MEASUREMENT_PATH_MODE = "Tooltip.MeasurementPathMode";
	public static final String TOOLTIP_NODE_LINK_MODE = "Tooltip.NodeLinkMode";
	public static final String TOOLTIP_CABLE_MODE = "Tooltip.CableMode";
	public static final String TOOLTIP_CENTER_SELECTION = "Tooltip.CenterSelection";
	public static final String TOOLTIP_REDUCE_ICON = "Tooltip.ReduceIcon";
	public static final String TOOLTIP_ENLARGE_ICON = "Tooltip.EnlargeIcon";
	public static final String TOOLTIP_HAND_PAN = "Tooltip.HandPan";
	public static final String TOOLTIP_LAYERS = LABEL_LAYERS;
	public static final String TOOLTIP_MOVE_TO_CENTER = "Tooltip.MoveToCenter";
	public static final String TOOLTIP_MOVE_FIXED = "Tooltip.MoveFixed";
	public static final String TOOLTIP_MEASURE_DISTANCE = "Tooltip.MeasureDistance";
	public static final String TITLE_MEASURE_DISTANCE = TOOLTIP_MEASURE_DISTANCE;

	public static final String TREE_ALL_MAPS = "Tree.AllMaps";
	public static final String TREE_EXTERNAL_NODE = "Tree.ExternalNode";
	public static final String TREE_INNER_MAPS = "Tree.InnerMaps";
	public static final String TREE_SCHEME_PATHS = "Tree.SchemePaths";
	public static final String TREE_SCHEME_CABLE_LINKS = "Tree.SchemeCableLinks";
	public static final String TREE_SCHEME_LINKS = "Tree.SchemeLinks";
	public static final String TREE_SCHEME_ELEMENTS = "Tree.SchemeElements";
	public static final String TREE_INNER_SCHEMES = "Tree.InnerSchemes";
	public static final String TREE_ALL_MAP_VIEWS = "Tree.AllMapViews";
	public static final String TREE_SCHEMES = LABEL_SCHEMES;
	public static final String TREE_LIBRARIES = "Tree.Libraries";
	public static final String TREE_MAP = "Tree.Map";
	public static final String TREE_MAP_EDITOR_ROOT = "Tree.MapEditorRoot";
	public static final String TREE_ELEMENTS_IN_NODE = "Tree.ElementsInNode";
	public static final String TREE_TOPOLOGY = "Tree.Topology";

	public static final String VALUE_DEFAULT_COLLECTOR_NAME = "Value.DefaultCollectorName";
	public static final String VALUE_NEW_MAP_VIEW = "Value.NewMapView";
	public static final String VALUE_ALARMED_COLOR = "Value.AlarmedColor";
	public static final String VALUE_ALARMED_THICKNESS = "Value.AlarmedThickness";
	public static final String VALUE_COLOR = "Value.Color";
	public static final String VALUE_THICKNESS = "Value.Thickness";
	public static final String VALUE_STYLE = "Value.Style";
	public static final String VALUE_METRIC = "Value.Metric";
	public static final String VALUE_NO_DESCRIPTION = "Value.NoDescription";
	public static final String VALUE_NEW = "Value.New";
	public static final String VALUE_UPDATING = "Value.Updating";
}
