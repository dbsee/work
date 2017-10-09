/**
 *
 */
package jp.co.csj.tools.utils.xml.dbtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnStrUtils;
import org.mydbsee.common.IConstFile;

import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.core.DbInfo;

/**
 * @author Think
 *
 */
public class XmlDbXlsInfoAll {

	private String defLanguage = "";
	private String defSelectIni = "";
	private XmlInfoXls xmlInfoXls = new XmlInfoXls();
	private XmlInfoSql xmlInfoSql = new XmlInfoSql();
	private XmlInfoActiveKey xmlInfoActiveKey = new XmlInfoActiveKey();
	private String tableSheet ="";
	private TreeMap<String, XmlDbConfig> xmlDbConfigMap = new TreeMap<String, XmlDbConfig>();
	private String[] viewConfig;
	private DbInfo dbInfo = null;
	private HashSet<String> delSqlSet = new HashSet<String>();
	private PicInfo picInfo = new PicInfo();
	/**
	 * @return the picInfo
	 */
	public PicInfo getPicInfo() {
		return picInfo;
	}
	/**
	 * @param picInfo the picInfo to set
	 */
	public void setPicInfo(PicInfo picInfo) {
		this.picInfo = picInfo;
	}
	public static void main(String[] args) {
		try {
			XmlDbXlsInfoAll xmlDbXlsInfoAll = new XmlDbXlsInfoAll();
			xmlDbXlsInfoAll.reform();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	/**
	 * @throws Throwable
	 *
	 */
	public void delete(String delNm) throws Throwable {

		CmnLog.logger.info("XML save begin");

		SAXReader reader = new SAXReader();

		// 得到文档对象
		Document doc = null;

		InputStream isr = new FileInputStream(new File(
				CsjPath.s_file_db_info_config));
		doc = reader.read(new InputStreamReader(isr,
				IConstFile.ENCODE_UTF_8));
		isr.close();

		// 获取根节点
		Element eleRoot = doc.getRootElement();
		String root = eleRoot.getName();
		System.out.println("RootNode:" + eleRoot.getName());
		List<Element> dbConfigList = doc.selectNodes("//"+root+"/dbConfig");
		for (Element dbConfigE: dbConfigList) {
			if (delNm.equals(dbConfigE.attributeValue("name"))) {
				dbConfigE.detach();
				xmlDbConfigMap.remove(delNm);
			}
		}
		String defSelete = "defaultIni";
		for (Entry<String, XmlDbConfig> entry : xmlDbConfigMap.entrySet()) {
			if ("defaultIni".equals( entry.getKey())) {
				continue;
			}
			defSelete = entry.getKey();
			
		}

		eleRoot.setAttributeValue("defSelectIni", defSelete);

		//保存(指定编码)
		FileWriter out1 = new FileWriter(CsjPath.s_file_db_info_config);

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(IConstFile.ENCODE_UTF_8);

		XMLWriter  out2 = new XMLWriter(out1,format);//指定格式

		out2.write(doc);
		out2.close();

		CmnLog.logger.info("XML save end");

	}

	public void saveDefSelect(String defSelect) throws Throwable  {

		CmnLog.logger.info("XML save begin");

		SAXReader reader = new SAXReader();

		// 得到文档对象
		Document doc = null;

		InputStream isr = new FileInputStream(new File(
				CsjPath.s_file_db_info_config));
		doc = reader.read(new InputStreamReader(isr,
				IConstFile.ENCODE_UTF_8));
		isr.close();

		// 获取根节点
		Element eleRoot = doc.getRootElement();
		String root = eleRoot.getName();
		System.out.println("RootNode:" + eleRoot.getName());
		List<Element> configList = doc.selectNodes("//"+root+"/dbConfig");

		for (Element configE : configList) {

			if (!defSelectIni.equals(configE.attributeValue("name"))) {
				continue;
			}
			configE.setAttributeValue("defualtSelect",defSelect);
		}

		//保存(指定编码)
		FileWriter out1 = new FileWriter(CsjPath.s_file_db_info_config);

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(IConstFile.ENCODE_UTF_8);

		XMLWriter  out2 = new XMLWriter(out1,format);//指定格式

		out2.write(doc);
		out2.close();

		CmnLog.logger.info("XML save end");

	}


	public void saveDefDbConfig(String defSelectConfig) throws Throwable {

		CmnLog.logger.info("XML save begin");

		SAXReader reader = new SAXReader();

		// 得到文档对象
		Document doc = null;

		InputStream isr = new FileInputStream(new File(
				CsjPath.s_file_db_info_config));
		doc = reader.read(new InputStreamReader(isr,
				IConstFile.ENCODE_UTF_8));
		isr.close();

		// 获取根节点
		Element eleRoot = doc.getRootElement();
		String root = eleRoot.getName();
		System.out.println("RootNode:" + root);

		eleRoot.setAttributeValue("defSelectIni", defSelectConfig);

		//保存(指定编码)
		FileWriter out1 = new FileWriter(CsjPath.s_file_db_info_config);

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(IConstFile.ENCODE_UTF_8);

		XMLWriter  out2 = new XMLWriter(out1,format);//指定格式

		out2.write(doc);
		out2.close();

		CmnLog.logger.info("XML save end");
	}
	/**
	 * @throws Throwable
	 *
	 */
	public void saveNew(String newNm) throws Throwable {

		CmnLog.logger.info("XML save begin");

		SAXReader reader = new SAXReader();

		// 得到文档对象
		Document doc = null;

		InputStream isr = new FileInputStream(new File(
				CsjPath.s_file_db_info_config));
		doc = reader.read(new InputStreamReader(isr,
				IConstFile.ENCODE_UTF_8));
		isr.close();

		// 获取根节点
		Element eleRoot = doc.getRootElement();
		String root = eleRoot.getName();
		System.out.println("RootNode:" + eleRoot.getName());
		List<Element> dbConfigList = doc.selectNodes("//"+root+"/dbConfig");
		for (Element dbConfigE: dbConfigList) {
			if (defSelectIni.equals(dbConfigE.attributeValue("name"))) {
				Element newE = eleRoot.addElement("dbConfig");
				newE.addAttribute("name", newNm);
				newE.addAttribute("defualtSelect", getCurrentXmlDbConfig().getDefualtSelect());
				newE.appendContent(dbConfigE);
			}
		}
		eleRoot.setAttributeValue("defSelectIni", newNm);

		//保存(指定编码)
		FileWriter out1 = new FileWriter(CsjPath.s_file_db_info_config);

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(IConstFile.ENCODE_UTF_8);

		XMLWriter  out2 = new XMLWriter(out1,format);//指定格式

		out2.write(doc);
		out2.close();

		CmnLog.logger.info("XML save end");

	}

	/**
	 * @throws Throwable
	 *
	 */
	public XmlDbXlsInfoAll() throws Throwable  {
		read();
	}

	public void save() throws Throwable  {

		CmnLog.logger.info("XML save begin");

		SAXReader reader = new SAXReader();

		// 得到文档对象
		Document doc = null;

		InputStream isr = new FileInputStream(new File(
				CsjPath.s_file_db_info_config));
		doc = reader.read(new InputStreamReader(isr,
				IConstFile.ENCODE_UTF_8));
		isr.close();

		// 获取根节点
		Element eleRoot = doc.getRootElement();
		String root = eleRoot.getName();
		System.out.println("RootNode:" + eleRoot.getName());

		// 获取指定路径中的(多个)节点
		List<Element> defLanguageElList = doc.selectNodes("//"+root+"/defLanguage");
		List<Element> defSelectIniElList = doc.selectNodes("//"+root+"/defSelectIni");
		List<Element> infoXlsElList = doc.selectNodes("//"+root+"/infoXls");
		List<Element> infoSqlElList = doc.selectNodes("//"+root+"/infoSql");
		List<Element> infoActiveKeyElList = doc.selectNodes("//"+root+"/infoActiveKey");
		List<Element> dbConfigElList = doc.selectNodes("//"+root+"/dbConfig");


		eleRoot.setAttributeValue("defLanguage", defLanguage);
		eleRoot.setAttributeValue("defSelectIni", defSelectIni);
		eleRoot.setAttributeValue("tableSheet", tableSheet);

		Element infoXlsEl =infoXlsElList.get(0);
		infoXlsEl.setAttributeValue("tblSign", xmlInfoXls.getTblSign());
		infoXlsEl.setAttributeValue("enNmCol", xmlInfoXls.getEnNmCol());
		infoXlsEl.setAttributeValue("jpNmCol", xmlInfoXls.getJpNmCol());
		infoXlsEl.setAttributeValue("colInfo", xmlInfoXls.getColInfo());
		infoXlsEl.setAttributeValue("maxRow", xmlInfoXls.getMaxRow());
		infoXlsEl.setAttributeValue("dateFormat", xmlInfoXls.getDateFormat());
		infoXlsEl.setAttributeValue("strFormat", xmlInfoXls.getStrFormat());
		infoXlsEl.setAttributeValue("numberFormat", xmlInfoXls.getNumberFormat());
		infoXlsEl.setAttributeValue("exceltype", xmlInfoXls.getExcelType());

		Element infoSqlEl =infoSqlElList.get(0);
		infoSqlEl.setAttributeValue("checkType", xmlInfoSql.getCheckType());
		infoSqlEl.setAttributeValue("encode", xmlInfoSql.getEncode());
		infoSqlEl.setAttributeValue("exeAbled", xmlInfoSql.getExeAbled());
		infoSqlEl.setAttributeValue("exeType", xmlInfoSql.getExeType());
		infoSqlEl.setAttributeValue("haveInsert", xmlInfoSql.getHaveInsert());
		infoSqlEl.setAttributeValue("isCheckWithLogic", xmlInfoSql.getIsCheckWithLogic());
		infoSqlEl.setAttributeValue("isDeleteTable", xmlInfoSql.getIsDeleteTable());
		infoSqlEl.setAttributeValue("isErrorContinue", xmlInfoSql.getIsErrorContinue());
		infoSqlEl.setAttributeValue("isGetTableCnt", xmlInfoSql.getIsGetTableCnt());
		infoSqlEl.setAttributeValue("isLogAbled", xmlInfoSql.getIsLogAbled());
		infoSqlEl.setAttributeValue("isLogTxtAbled", xmlInfoSql.getIsLogTxtAbled());
		infoSqlEl.setAttributeValue("isBatch", xmlInfoSql.getIsBatch());
		infoSqlEl.setAttributeValue("isUpdateWithOutNull", xmlInfoSql.getIsUpdateWithOutNull());
		Element infoActiveKeyEl =infoActiveKeyElList.get(0);
		infoActiveKeyEl.setAttributeValue("activeChar", xmlInfoActiveKey.getActiveChar());
		infoActiveKeyEl.setAttributeValue("isAlt", xmlInfoActiveKey.getIsAlt());
		infoActiveKeyEl.setAttributeValue("isCtrl", xmlInfoActiveKey.getIsCtrl());

		List<Element> linkList = doc.selectNodes("//"+root+"/dbConfig/db/link");

		for (Element linkE : linkList) {
			Element dbE = linkE.getParent();
			Element dbConfig = dbE.getParent();

			if (!defSelectIni.equals(dbConfig.attributeValue("name"))) {
				continue;
			}
			if (!getCurrentXmlDbConfig().getDefualtSelect().equals(dbE.attributeValue("dbType"))) {
				continue;
			}

			linkE.setAttributeValue("dbUrl",getCurrentXmlDb().getDbUrl());
			linkE.setAttributeValue("ip",getCurrentXmlDb().getIp());
			linkE.setAttributeValue("port",getCurrentXmlDb().getPort());
			linkE.setAttributeValue("database",getCurrentXmlDb().getDatabase());
			linkE.setAttributeValue("dbUserId",getCurrentXmlDb().getDbUserId());
			linkE.setAttributeValue("dbPassword",getCurrentXmlDb().getDbPassword());
			linkE.setAttributeValue("schema",getCurrentXmlDb().getSchema());
		}

		//保存(指定编码)
		FileWriter out1 = new FileWriter(CsjPath.s_file_db_info_config);

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(IConstFile.ENCODE_UTF_8);

		XMLWriter  out2 = new XMLWriter(out1,format);//指定格式

		out2.write(doc);
		out2.close();

		setStaticMaxRow();
		CmnLog.logger.info("XML save end");

	}
	/**
	 * 
	 */
	public void setStaticMaxRow() {
		String maxRow = this.getXmlInfoXls().getMaxRow();
		if (CmnStrUtils.isNotEmpty(maxRow)) {
			XmlInfoXls.S_WIDTH = maxRow.length();
		}
	}


	public void read() throws Throwable  {

		CmnLog.logger.info("XML read begin");
		SAXReader reader = new SAXReader();

		// 得到文档对象
		Document doc = null;

		InputStream isr = new FileInputStream(new File(
				CsjPath.s_file_db_info_config));
		doc = reader.read(new InputStreamReader(isr,
				IConstFile.ENCODE_UTF_8));
		isr.close();

		// 获取根节点
		Element eleRoot = doc.getRootElement();
		String root = eleRoot.getName();
		System.out.println("RootNode:" + eleRoot.getName());

		// 获取指定路径中的(多个)节点
		List<Element> defLanguageElList = doc.selectNodes("//"+root+"/defLanguage");
		List<Element> defSelectIniElList = doc.selectNodes("//"+root+"/defSelectIni");
		List<Element> infoXlsElList = doc.selectNodes("//"+root+"/infoXls");
		List<Element> infoSqlElList = doc.selectNodes("//"+root+"/infoSql");
		List<Element> infoActiveKeyElList = doc.selectNodes("//"+root+"/infoActiveKey");
		List<Element> dbConfigElList = doc.selectNodes("//"+root+"/dbConfig");
		defLanguage = eleRoot.attributeValue("defLanguage");
		defSelectIni = eleRoot.attributeValue("defSelectIni");
		tableSheet = eleRoot.attributeValue("tableSheet");

		Element infoXlsEl =infoXlsElList.get(0);
		xmlInfoXls.setTblSign(infoXlsEl.attributeValue("tblSign"));
		xmlInfoXls.setEnNmCol(infoXlsEl.attributeValue("enNmCol"));
		xmlInfoXls.setJpNmCol(infoXlsEl.attributeValue("jpNmCol"));
		xmlInfoXls.setColInfo(infoXlsEl.attributeValue("colInfo"));
		xmlInfoXls.setMaxRow(infoXlsEl.attributeValue("maxRow"));
		xmlInfoXls.setDateFormat(infoXlsEl.attributeValue("dateFormat"));
		xmlInfoXls.setStrFormat(infoXlsEl.attributeValue("strFormat"));
		xmlInfoXls.setNumberFormat(infoXlsEl.attributeValue("numberFormat"));
		xmlInfoXls.setExcelType(infoXlsEl.attributeValue("exceltype"));

		if (CsjConst.EXCEL_DOT_XLS_1997.equals(xmlInfoXls.getExcelType())) {
			xmlInfoXls.setMaxCol(CsjConst.EXCEL_DOT_XLSX_1997_MAX_COL);
		} else {
			xmlInfoXls.setMaxCol(CsjConst.EXCEL_DOT_XLSX_2007_MAX_COL);
		}
		
		Element infoSqlEl =infoSqlElList.get(0);
		xmlInfoSql.setCheckType(infoSqlEl.attributeValue("checkType"));
		xmlInfoSql.setEncode(infoSqlEl.attributeValue("encode"));
		xmlInfoSql.setExeAbled(infoSqlEl.attributeValue("exeAbled"));
		xmlInfoSql.setExeType(infoSqlEl.attributeValue("exeType"));
		xmlInfoSql.setHaveInsert(infoSqlEl.attributeValue("haveInsert"));
		xmlInfoSql.setIsCheckWithLogic(infoSqlEl.attributeValue("isCheckWithLogic"));
		xmlInfoSql.setIsDeleteTable(infoSqlEl.attributeValue("isDeleteTable"));
		xmlInfoSql.setIsErrorContinue(infoSqlEl.attributeValue("isErrorContinue"));
		xmlInfoSql.setIsGetTableCnt(infoSqlEl.attributeValue("isGetTableCnt"));
		xmlInfoSql.setIsLogAbled(infoSqlEl.attributeValue("isLogAbled"));
		xmlInfoSql.setIsLogTxtAbled(infoSqlEl.attributeValue("isLogTxtAbled"));
		xmlInfoSql.setIsBatch(infoSqlEl.attributeValue("isBatch"));
		xmlInfoSql.setIsUpdateWithOutNull(infoSqlEl.attributeValue("isUpdateWithOutNull"));

		Element infoActiveKeyEl =infoActiveKeyElList.get(0);
		xmlInfoActiveKey.setActiveChar(infoActiveKeyEl.attributeValue("activeChar"));
		xmlInfoActiveKey.setIsAlt(infoActiveKeyEl.attributeValue("isAlt"));
		xmlInfoActiveKey.setIsCtrl(infoActiveKeyEl.attributeValue("isCtrl"));

		for (Element e : dbConfigElList) {
			XmlDbConfig xmlDbConfig = new XmlDbConfig();
			String name = e.attributeValue("name");
			String defualtSelect = e.attributeValue("defualtSelect");
			xmlDbConfig.setName(name);
			xmlDbConfig.setDefualtSelect(defualtSelect);
			xmlDbConfigMap.put(name, xmlDbConfig);
		}

		List<Element> linkList = doc.selectNodes("//"+root+"/dbConfig/db/link");
		for (Element linkE : linkList) {
			Element dbE = linkE.getParent();
			Element dbConfigE = dbE.getParent();
			XmlDbConfig xmlDbConfig = xmlDbConfigMap.get(dbConfigE.attributeValue("name"));

			TreeMap<String,XmlDb> xmlDbMap = xmlDbConfig.getXmlDbMap();

			XmlDb xmlDb = new XmlDb();
			xmlDb.setDbUrl(linkE.attributeValue("dbUrl"));
			xmlDb.setIp(linkE.attributeValue("ip"));
			xmlDb.setPort(linkE.attributeValue("port"));
			xmlDb.setDatabase(linkE.attributeValue("database"));
			xmlDb.setDbUserId(linkE.attributeValue("dbUserId"));
			xmlDb.setDbPassword(linkE.attributeValue("dbPassword"));
			
			


			String dbType = dbE.attributeValue("dbType");
			
			if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
				
				if (CmnStrUtils.isEmpty(linkE.attributeValue("schema"))||linkE.attributeValue("schema").trim().length()==0) {
					xmlDb.setSchema("public");
				} else {
					xmlDb.setSchema(linkE.attributeValue("schema"));
				}
				
			} else {
				xmlDb.setSchema(CsjConst.EMPTY);
			}
			
			xmlDbMap.put(dbType, xmlDb);
		}


		List<Element> pathList = doc.selectNodes("//"+root+"/dbConfig/db/path");
		for (Element pathE : pathList) {
			Element dbE = pathE.getParent();
			Element dbConfigE = dbE.getParent();
			XmlDbConfig xmlDbConfig = xmlDbConfigMap.get(dbConfigE.attributeValue("name"));

			TreeMap<String,XmlDb> xmlDbMap = xmlDbConfig.getXmlDbMap();
			String dbType = dbE.attributeValue("dbType");
			xmlDbMap.get(dbType).setXlsPath(pathE.attributeValue("xlsPath"));
			xmlDbMap.get(dbType).setTxtPath(pathE.attributeValue("txtPath"));
		}

		reform();

		dbInfo = new DbInfo(this);
		
		setStaticMaxRow();
		
		CmnLog.logger.info("XML read end");
	}
	/**
	 *
	 */
	private void reform() {
		List<String> lstConfig = new ArrayList<String>();
		for (Entry<String, XmlDbConfig> entry : xmlDbConfigMap.entrySet()) {
			String key = entry.getKey();
			if (!"defaultIni".equals(key)) {
				lstConfig.add(key);

				XmlDbConfig val = entry.getValue();
				String s[] = new String[val.getXmlDbMap().size()];
				int i = 0;
				for (String str : val.getXmlDbMap().keySet()) {
					s[i++] = str;
				}
				val.setViewDbType(s);
			}
		}
		viewConfig = new String[lstConfig.size()];
		for (int i = 0; i < lstConfig.size(); i++) {
			viewConfig[i] = lstConfig.get(i);
		}



	}
	/**
	 * @return the defLanguage
	 */
	public String getDefLanguage() {
		return defLanguage;
	}

	/**
	 * @param defLanguage the defLanguage to set
	 */
	public void setDefLanguage(String defLanguage) {
		this.defLanguage = defLanguage;
	}

	/**
	 * @return the defSelectIni
	 */
	public String getDefSelectIni() {
		return defSelectIni;
	}

	/**
	 * @param defSelectIni the defSelectIni to set
	 */
	public void setDefSelectIni(String defSelectIni) {
		this.defSelectIni = defSelectIni;
	}
	/**
	 * @return the xmlInfoXls
	 */
	public XmlInfoXls getXmlInfoXls() {
		return xmlInfoXls;
	}
	/**
	 * @param xmlInfoXls the xmlInfoXls to set
	 */
	public void setXmlInfoXls(XmlInfoXls xmlInfoXls) {
		this.xmlInfoXls = xmlInfoXls;
	}
	/**
	 * @return the infoSql
	 */
	public XmlInfoSql getInfoSql() {
		return xmlInfoSql;
	}
	/**
	 * @param xmlInfoSql the infoSql to set
	 */
	public void setInfoSql(XmlInfoSql xmlInfoSql) {
		this.xmlInfoSql = xmlInfoSql;
	}
	/**
	 * @return the xmlInfoActiveKey
	 */
	public XmlInfoActiveKey getXmlInfoActiveKey() {
		return xmlInfoActiveKey;
	}
	/**
	 * @param xmlInfoActiveKey the xmlInfoActiveKey to set
	 */
	public void setXmlInfoActiveKey(XmlInfoActiveKey xmlInfoActiveKey) {
		this.xmlInfoActiveKey = xmlInfoActiveKey;
	}
	/**
	 * @return the xmlDbConfigMap
	 */
	public TreeMap<String, XmlDbConfig> getXmlDbConfigMap() {
		return xmlDbConfigMap;
	}
	/**
	 * @param xmlDbConfigMap the xmlDbConfigMap to set
	 */
	public void setXmlDbConfigMap(TreeMap<String, XmlDbConfig> xmlDbConfigMap) {
		this.xmlDbConfigMap = xmlDbConfigMap;
	}


	/**
	 * @return the xmlInfoSql
	 */
	public XmlInfoSql getXmlInfoSql() {
		return xmlInfoSql;
	}

	/**
	 * @param xmlInfoSql the xmlInfoSql to set
	 */
	public void setXmlInfoSql(XmlInfoSql xmlInfoSql) {
		this.xmlInfoSql = xmlInfoSql;
	}

	/**
	 * @return the viewConfig
	 */
	public String[] getViewConfig() {
		return viewConfig;
	}

	/**
	 * @param viewConfig the viewConfig to set
	 */
	public void setViewConfig(String[] viewConfig) {
		this.viewConfig = viewConfig;
	}

	public XmlDb getCurrentXmlDb() {
		XmlDbConfig xmlDbConfig = getCurrentXmlDbConfig();
		XmlDb xmlDb = xmlDbConfig.getXmlDbMap().get(xmlDbConfig.getDefualtSelect());
		return xmlDb;
	}
	public XmlDbConfig getCurrentXmlDbConfig() {
		XmlDbConfig xmlDbConfig = this.getXmlDbConfigMap().get(this.getDefSelectIni());
		return xmlDbConfig;
	}

	/**
	 * @return the dbInfo
	 */
	public DbInfo getDbInfo() {
		return dbInfo;
	}

	/**
	 * @param dbInfo the dbInfo to set
	 */
	public void setDbInfo(DbInfo dbInfo) {
		this.dbInfo = dbInfo;
	}
	/**
	 * @return the tableSheet
	 */
	public String getTableSheet() {
		return tableSheet;
	}
	/**
	 * @param tableSheet the tableSheet to set
	 */
	public void setTableSheet(String tableSheet) {
		this.tableSheet = tableSheet;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "XmlDbXlsInfoAll [defLanguage=" + defLanguage
				+ ", defSelectIni=" + defSelectIni + ", xmlInfoXls="
				+ xmlInfoXls + ", xmlInfoSql=" + xmlInfoSql
				+ ", xmlInfoActiveKey=" + xmlInfoActiveKey + ", tableSheet="
				+ tableSheet + ", xmlDbConfigMap=" + xmlDbConfigMap
				+ ", viewConfig=" + Arrays.toString(viewConfig) + ", dbInfo="
				+ dbInfo + "]";
	}
	/**
	 * @return the delSqlSet
	 */
	public HashSet<String> getDelSqlSet() {
		return delSqlSet;
	}
	/**
	 * @param delSqlSet the delSqlSet to set
	 */
	public void setDelSqlSet(HashSet<String> delSqlSet) {
		this.delSqlSet = delSqlSet;
	}

}
