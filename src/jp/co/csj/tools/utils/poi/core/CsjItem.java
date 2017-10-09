/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author csj
 *
 */
public class CsjItem {

    private String content = CsjConst.EMPTY;
    private HSSFRichTextString richText = null;
    /**
     * @param content
     */
    public CsjItem(String content) {
        this.content = content;
    }
    /**
     * 
     */
    public CsjItem() {
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return the richText
     */
    public HSSFRichTextString getRichText() {
        return richText;
    }
    /**
     * @param richText the richText to set
     */
    public void setRichText(HSSFRichTextString richText) {
        this.richText = richText;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CsjItem [content=" + content + "]";
    }
    
    
}
