package jp.co.csj.tools.utils.poi.word;



public class FileExtractor {
		public static String getSuffix(String fileName){
		String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
		return suffix;
	}
//	public static String wordExtractor(String fileName){
//		try{
//		InputStream in = new FileInputStream(fileName);
//		String text = "";
//		if(getSuffix(fileName).equals("doc")){
//			WordExtractor wordExtractor = new WordExtractor(in);
//			text = wordExtractor.getText();
//		}else if(getSuffix(fileName).equals("docx")){
//			OPCPackage opcPackage =  POIXMLDocument.openPackage(fileName);
//			POIXMLTextExtractor ex = new XWPFWordExtractor(opcPackage);
//			text = ex.getText();
//		}else if(getSuffix(fileName).equals("pdf")){
//			PDFParser p = new PDFParser(in);
//			p.parse();
//			PDFTextStripper ts = new PDFTextStripper();
//			text = ts.getText(p.getPDDocument());
//		}else{
//			text = "";
//		}
//		in.close();
//		return text;
//		}catch(IOException e){
//			e.printStackTrace();
//			return null;
//		} catch (XmlException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (OpenXML4JException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//	public static void main(String[] args) {
//		List<File> fileList = CsjDirectory.getFilesList("F:\\vss\\SY1201PJ\\03_プロジェクト開発[密]\\0301_入手資料\\個別設計書", true);
//		try {
//			CsjLog5j.initLog5j("d:\\", "log.txt", CsjFileConst.ENCODE_UTF_8);
//
//
//			for (File f : fileList) {
//				try {
//					CsjFileUtils.writeWithbBlank(CsjLog5j.s_log5j, f.getAbsolutePath(), 0);
//					CsjFileUtils.writeWithbBlank(CsjLog5j.s_log5j, FileExtractor.wordExtractor(f.getAbsolutePath()), 0);
//				} catch (Throwable e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			}
//			CsjLog5j.closeLog5j();
//
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
