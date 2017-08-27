package org.mydbsee.pic.common.sys;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class CacheImage {
	private final Map<String, Image> imageMap = new HashMap<String, Image>();

	private static CacheImage INSTANCE;

	private CacheImage() {
	}

	// ����ģʽ�����CacheImageʵ��
	public static CacheImage getINSTANCE() {
		if (INSTANCE == null)
			INSTANCE = new CacheImage();
		return INSTANCE;
	}

	// ���ͼ��
	public Image getImage(String applicationID,String imageName) {
		if (imageName == null)
			return null;
		Image image = (Image) imageMap.get(imageName);
		if (image == null) {
			image =AbstractUIPlugin.imageDescriptorFromPlugin(
					applicationID,imageName).createImage();
			imageMap.put(imageName, image);
		}
		return image;
	}

	// �ͷ�ͼ����Դ
	public void dispose() {
		Iterator iterator = imageMap.values().iterator();
		while (iterator.hasNext())
			((Image) iterator.next()).dispose();
		imageMap.clear();
	}
}