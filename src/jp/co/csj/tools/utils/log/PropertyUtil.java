package jp.co.csj.tools.utils.log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.mydbsee.common.CmnLog;

import jp.co.csj.tools.utils.log.constant.CsjLogConst;

/**
 *
 * プロパティファイル処理作成
 *
 */
public class PropertyUtil {

	/** プロパティファイルディレクトリ */
	private String propDir = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "properties";

	/** プロパティマップ */
	private Properties prop = null;

	/** プロパティファイル名 */
	private String propFileName = "";

	/** エラーログ出力フラグ */
	private boolean bErrOutput = true;

	/**
	 * コンストラクタ
	 *
	 * @param strPropFileName プロパティファイル名
	 * @param proID 機能ID
	 */
	public PropertyUtil(String strPropFileName) {
		propFileName = strPropFileName;
	}

	/**
	 * コンストラクタ
	 *
	 * @param strPropDir プロパティファイルディレクトリ
	 * @param strPropFileName プロパティファイル名
	 * @param proID 機能ID
	 */
	public PropertyUtil(String strPropDir, String strPropFileName) {
		propDir = strPropDir;
		propFileName = strPropFileName;
	}

	/**
	 * @param bErrOutput the bErrOutput to set
	 */
	public void setbErrOutput(boolean bErrOutput) {
		this.bErrOutput = bErrOutput;
	}

	/**
	 * プロパティファイルからキーより該当値を取得処理
	 *
	 * @param key キー
	 * @return キーに対応する値
	 * @throws Throwable 例外
	 */
	public String getValueByKey(String key) throws IOException {

		// keyが未指定の場合
		if (key == null) {

			if (bErrOutput) {
				// エラーメッセージを出力する
				CmnLog.logger.error("Property File Error: key = null");
			}

			// Null例外を投げる
			throw new NullPointerException("key = null");
		}

		// プロパティマップがNULLの場合
		if (prop == null) {

			// プロパティファイルロード処理を行う
			loadPropertyFile();
		}

		// 該当値を取得する
		String value = prop.getProperty(key);

		// 該当値がNULLの場合
		if (value == null) {

			if (bErrOutput) {
				// エラーメッセージを出力する
				CmnLog.logger.info(CsjLogConst.MSG_LEVEL_E,
						"Property File Error: value = null [key]=" + key);
			}

			// Null例外を投げる
			throw new NullPointerException("value = null");
		}

		// 値を返却する
		return value;

	}

	/**
	 * プロパティファイルロード処理
	 *
	 * @throws Throwable 例外
	 */
	public void loadPropertyFile() throws IOException {

		// プロパティオブジェクトを初期化する
		prop = new Properties();

		// プロパティファイルを取得する
		String strPropFilePath = propDir + System.getProperty("file.separator")
				+ propFileName;
		// プロパティファイルストリームを初期化する
		FileInputStream fileInput = null;

		try {

			// プロパティファイルストリームを作成する
			fileInput = new FileInputStream(strPropFilePath);

			// プロパティファイルを読み込み
			prop.load(fileInput);

		} catch (IOException e) {
			CmnLog.logger.info(e.getMessage());
			e.printStackTrace();
			// 例外を投げる
			throw e;

		} finally {

			// プロパティファイルストリームがNULL以外の場合
			if (fileInput != null) {

				// ToJoプロパティファイルストリームをクローズ
				fileInput.close();
			}

		}

	}

	/**
	 * プロパティマップ取得処理
	 *
	 */
	public Properties getProp() throws IOException {
		loadPropertyFile();
		return prop;
	}

	/**
	 * プロパティファイルからキーと値リスト
	 *
	 * @return キーと値リスト
	 * @throws Throwable 例外
	 */
	public List<Entry<Object,Object>> getPropertiesList() throws IOException {

		List<Entry<Object,Object>> propertiesList = new ArrayList<Entry<Object,Object>>();

		// プロパティマップがNULLの場合
		if (prop == null) {

			// プロパティファイルロード処理を行う
			loadPropertyFile();
		}

		// 該当値を取得する
		Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();

		while(it.hasNext())
		{
			Entry<Object, Object> ent = it.next();
			propertiesList.add(ent);
		}

		// 値を返却する
		return propertiesList;

	}
}
