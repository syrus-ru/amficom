/*-
* $Id: ActionTransferHandler.java,v 1.1 2005/11/28 14:47:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import org.jgraph.JGraph;

import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/28 14:47:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ActionTransferHandler extends TransferHandler {

    DataFlavor localActionFlavor;
    DataFlavor serialArrayListFlavor;
    
    private AbstractAction abstractAction;
    
    private String localActionType = DataFlavor.javaJVMLocalObjectMimeType +
                                ";class=" + AbstractAction.class.getName();

    private JTree source = null;
    
	private final ManagerMainFrame	managerMainFrame;
    
    public ActionTransferHandler(final ManagerMainFrame managerMainFrame) {
        this.managerMainFrame = managerMainFrame;
		try {
            this.localActionFlavor = new DataFlavor(this.localActionType);
        } catch (final ClassNotFoundException e) {
            System.err.println("ActionTransferHandler: unable to create data flavor");
        }
        this.serialArrayListFlavor = new DataFlavor(AbstractAction.class,
        	AbstractAction.class.getSimpleName());
    }
    

    @Override
    public boolean importData(	final JComponent component,
			final Transferable transferable) {
        if (!canImport(component, transferable.getTransferDataFlavors())) {
            return false;
        }

        try {
            if (!(component instanceof JGraph)) {
            	return false;
            }
            if (hasLocalArrayListFlavor(transferable.getTransferDataFlavors())) {
                this.abstractAction = (AbstractAction)transferable.getTransferData(this.localActionFlavor);
            } else if (hasSerialArrayListFlavor(transferable.getTransferDataFlavors())) {
                this.abstractAction = (AbstractAction)transferable.getTransferData(this.serialArrayListFlavor);
            } else {
                return false;
            }
        } catch (final UnsupportedFlavorException ufe) {
            System.err.println("importData: unsupported data flavor");
            return false;
        } catch (final IOException ioe) {
            System.err.println("importData: I/O exception");
            return false;
        }

        return true;
    }    
    
    public final AbstractAction getAction() {
    	return this.abstractAction;
    }
    
    private boolean hasLocalArrayListFlavor(final DataFlavor[] flavors) {
        if (this.localActionFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(this.localActionFlavor)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSerialArrayListFlavor(final DataFlavor[] flavors) {
        if (this.serialArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(this.serialArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    @Override
	public boolean canImport(final JComponent component, 
			final DataFlavor[] flavors) {
    	return hasLocalArrayListFlavor(flavors) ? true : hasSerialArrayListFlavor(flavors);
    }

    @Override
	protected Transferable createTransferable(final JComponent component) {
    	
    	if (component instanceof JTree) {        	
            this.source = (JTree)component;
            final TreePath[] values = this.source.getSelectionPaths();
            if (values == null || values.length != 1) {
                return null;
            }
            final Object lastPathComponent = values[0].getLastPathComponent();
            if (lastPathComponent instanceof ActionMutableTreeNode) {
            	final ActionMutableTreeNode actionNode = (ActionMutableTreeNode) lastPathComponent;
            	final Perspective perspective = actionNode.getPerspective();

            	// only for the same perspective available
            	if (this.managerMainFrame.getPerspective() == perspective) {
            		final AbstractAction action = actionNode.getAbstractAction();
    				return new ActionTransferable(action);
            	}
            }
        }
        return null;
    }

    @Override
	public int getSourceActions(final JComponent component) {
        return COPY;
    }
    
	public final class ActionTransferable implements Transferable {
        private final AbstractAction action;

        public ActionTransferable(final AbstractAction action) {
            this.action = action;
        }

        public final Object getTransferData(final DataFlavor flavor)
				throws UnsupportedFlavorException {
            if (!this.isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return this.action;
        }

        @SuppressWarnings("unqualified-field-access")
		public final DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { localActionFlavor,
                                      serialArrayListFlavor };
        }
        
        @SuppressWarnings("unqualified-field-access")
        public final boolean isDataFlavorSupported(final DataFlavor flavor) {
            if (localActionFlavor.equals(flavor)) {
                return true;
            }
            if (serialArrayListFlavor.equals(flavor)) {
                return true;
            }
            return false;
        }
    }
}

