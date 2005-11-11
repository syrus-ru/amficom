/*-
* $Id: ImageResourceHandler.java,v 1.2 2005/11/11 12:46:16 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Image;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/11 12:46:16 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class ImageResourceHandler implements ConcreateResourceHandler<Image> {
	public void load(final Image image) {
		final String id = image.getId();
		final String filename = image.getFilename();
		final File file = new File(filename);
		final boolean useSize = image.isSetSize();
		final int width = useSize ? image.getSize().getWidth() : -1;
		final int height = useSize ? image.getSize().getHeight() : -1;
		
		if (useSize) {
			System.out.println(id + " required for scaling to : " + width + "x" + height);
		}
		
		if (file.exists()) {
			UIManager.put(id, new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table) {
					final java.awt.Image image2 =
						Toolkit.getDefaultToolkit().getImage(filename);
					final java.awt.Image image3 = useSize ? 
							image2.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH) : image2;
					return new ImageIcon(image3);
				}
			});
		} else {
			System.err.println("Image file " + filename + " for id '" + id + "' is not exists.");
		}
	}
}
