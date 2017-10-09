package org.mydbsee.pic.sys.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.mydbsee.pic.MyDbSeeActivator;

public class MyCacheImage {
	private final Map<String, Image> imageMap = new HashMap<String, Image>();
	private final Map<String, ImageDescriptor> imageDescriptorMap = new HashMap<String, ImageDescriptor>();
	
	private static MyCacheImage INSTANCE;

	private MyCacheImage() {
	}

	// 单例模式，获得CacheImage实例
	public static MyCacheImage getINSTANCE() {
		if (INSTANCE == null)
			INSTANCE = new MyCacheImage();
		return INSTANCE;
	}

	// 获得图像
	public Image getImage(String imageName) {
		if (imageName == null)
			return null;
		Image image = (Image) imageMap.get(imageName);
		if (image == null) {
			
			ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
					MyDbSeeActivator.PLUGIN_ID,imageName);
			imageDescriptorMap.put(imageName, imageDescriptor);
			image = imageDescriptor.createImage();
			imageMap.put(imageName, image);
		}
		return image;
	}
	
	// 获得图像
	public ImageDescriptor getImageDescriptor(String imageName) {
		if (imageName == null)
			return null;
		ImageDescriptor imageDescriptor = (ImageDescriptor) imageDescriptorMap.get(imageName);
		if (imageDescriptor == null) {
			imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
					MyDbSeeActivator.PLUGIN_ID,imageName);
			imageDescriptorMap.put(imageName, imageDescriptor);
			imageMap.put(imageName, imageDescriptor.createImage());
		}
		return imageDescriptor;
	}
	
	// 释放图像资源
	public void dispose() {
		Iterator iterator = imageMap.values().iterator();
		while (iterator.hasNext())
			((Image) iterator.next()).dispose();
		imageMap.clear();
	}
}