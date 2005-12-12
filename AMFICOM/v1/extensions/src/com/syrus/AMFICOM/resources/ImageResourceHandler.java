/*-
* $Id: ImageResourceHandler.java,v 1.5 2005/12/12 11:47:13 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Image;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.5 $, $Date: 2005/12/12 11:47:13 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class ImageResourceHandler implements ConcreateResourceHandler<Image> {
	
	public void load(final Image image) {
		final String id = image.getId();
		final String filename = image.getFilename();
		
		final URL resource = ImageResourceHandler.class.getClassLoader().getResource(filename);
		
		final boolean useSize = image.isSetSize();
		final int width = useSize ? image.getSize().getWidth() : -1;
		final int height = useSize ? image.getSize().getHeight() : -1;
		
		if (useSize) {
			assert Log.debugMessage(id 
					+ " required for scaling to : " 
					+ width 
					+ 'x' 
					+ height, 
				Log.DEBUGLEVEL08);
		}
		
		if (resource != null) {
			UIManager.put(id, new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table) {
					java.awt.Image image2 = Toolkit.getDefaultToolkit().createImage(resource);
					final java.awt.Image image3 = useSize ? 
							image2.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH) : image2;
					return new ImageIcon(image3);
				}
			});
		} else {
			final String message = "Image file " + filename + " for id '" + id + "' does not exist.";
			Log.errorMessage(message);
		}
	}
}
