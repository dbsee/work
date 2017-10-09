/**
 * 
 */
package jp.co.csj.tools.utils.log.constant;

/**
 * @author Think
 *
 */
public class CsjLogConst {
    public static final String SIGN_FILE_PATH = "ファイル位置:";
    public static final String SIGN_SHEET_NM = "シート名称:";
    public static final String SIGN_ROW = "行数:";
    public static final String SIGN_COL = "列数:";
    public static final String SIGN_CONTENT = "内容:";


    /** IFプロパティファイル */
    public final static String IF_PROPERTY_FILE = "IFProj.properties";

    /** IFプロパティSqlファイル */
    public final static String IF_PROPERTY_SQL_FILE = "IFSql.properties";

    /** IFプロパティMessageファイル */
    public final static String IF_PROPERTY_MESSAGE_FILE = "IFMessage.properties";

    /** IFプロパティSqlファイル */
    public final static String IF_SET_DATE_FILE = "SetData.properties";

    /** メッセージランク:INFO */
    public final static String MSG_LEVEL_I = "I";

    /** メッセージランク:ERROR */
    public final static String MSG_LEVEL_E = "E";

    /** メッセージランク:DEBUG */
    public final static String MSG_LEVEL_D = "D";

    /** 補足タイプ:半角 */
    public final static String FULL_TYPE_HANKAKU = "1";

    /** 補足タイプ:全角 */
    public final static String FULL_TYPE_ZENKAKU = "2";

    /** 補足タイプ:P */
    public final static String FULL_TYPE_SOP = "3";

    /** 補足タイプ:H */
    public final static String FULL_TYPE_HEX = "4";

    /** 補足タイプ:H */
    public final static String FULL_HEX_START = "0a42";

    /** 補足タイプ:H */
    public final static String FULL_HEX_END = "0a41";

    /** 補足揃える */
    public final static String FULL_LEFT = "1";

    /** 補足揃える */
    public final static String FULL_RIGHT = "2";

    /** 補足内容 */
    public final static String FULL_SPACE_ZENKAKU = "　";

    /** 補足内容 */
    public final static String FULL_SPACE_HANKAKU = " ";

    /** 補足内容 */
    public final static String FULL_SPACE_ZERO = "0";

    /** String.Empty:"" */
    public final static String STRING_EMPTY = "";

    /** 符号:"/" */
    public final static String MASK_SLASH = "/";

    /** 符号:"." */
    public final static String MASK_POINT = ".";

    /** 符号:"\" */
    public final static String MASK_BLACKLASH = "\\";

    /** 符号:":" */
    public final static String MASK_COLON = ":";

    /** 符号:"," */
    public final static String MASK_COMMA = ",";

    /** 符号:"'" */
    public final static String MASK_SINGLE_QUOTE = "'";

    /** Pタイプの符号+:" " */
    public final static String MASK_PLUS = " ";

    /** 符号:"-" */
    public final static String MASK_MINUS = "-";

    /** 符号:"''" */
    public final static String MASK_DOUBLE_QUOTE = "''";

    /** 換行符 */
    public final static String MASK_LINE_FEED = "\r\n";

    /** 有効性OK */
    public final static String VALIDITY_OK = "0";

    /** 有効性NG */
    public final static String VALIDITY_NG = "1";

    /** 照合エラー */
    public final static String CHECK_ERROR = "2";

    /** 会員番号更新 */
    public final static String MEMBER_UPDATE = "1";

    /** 有効期限更新 */
    public final static String VALID_PERIOD_UPDATE = "1";

    /** 通常チェック */
    public final static int COMMON_CHECK = 0;

    /** 契約状態チェック */
    public final static int ADMINISTRATIVE_STATUS_CHECK = 1;

    /** 契約商品番号チェック */
    public final static int ADMINISTRATIVE_ITEM_NO_CHECK = 2;

    /** 延滞日数チェック */
    public final static int ARREARS_DAYS_CHECK = 3;

    /** キャラクターコード：SJIS */
    public final static String FILE_CODE_SJIS = "SJIS";

    /** キャラクターコード：MS932 */
    public final static String FILE_CODE_MS932 = "MS932";

    /** ページ件数：PAGE_COUNT */
    public final static String PAGE_COUNT = "PAGE_COUNT";

    /** DB接続用URL */
    public final static String DB_URL = "db_url";

    /** DB接続ユーザ名 */
    public final static String DB_USER_ID = "db_userid";

    /** DB接続パスワード */
    public final static String DB_PASSWORD = "db_password";

    /** 年 */
    public final static String DATE_YEAR = "年";

    /** 月 */
    public final static String DATE_MONTH = "月";

    /** 日 */
    public final static String DATE_DAY = "日";

    /** 時 */
    public final static String DATE_HOUR = "時";

    /** 分 */
    public final static String DATE_MINUTE = "分";

    /** 秒 */
    public final static String DATE_SECOND = "秒";

    /** 降順 */
    public final static String SORT_DESC = "DESC_";

    /** 日期格式 */
    public final static String DATE_FORMAT_YYYYMM = "yyyyMM";

    /** 日期格式 */
    public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    /** 日期格式 */
    public final static String DATE_FORMAT_YYYYMMDDHMS = "yyyyMMddHH24MISS";

    /** カード補位 */
    public final static String CARD_COMPLEMENT = "0000";

    /** ASCii:0 */
    public final static char ASCII_ZERO = '\u0030';

    /** ASCii:9 */
    public final static char ASCII_NINE = '\u0039';

    /** ASCii:, */
    public final static char ASCII_COMMA = '\u002C';

    /** ASCii:. */
    public final static char ASCII_PERIOD = '\u002E';

    /** テーブル存在フラグ： 1 */
    public final static String TABLE_IS_EXIST = "1";

    /** 数字： 0 */
    public final static String STRING_NUMBER_ZERO = "0";

    /** コミットカウント */
    public final static int COMMIT_COUNT = 10000;

    /** ユーザディレクトリ */
    public final static String USER_DIR = "user.dir";

    /** ファイル分割りフラグ */
    public final static String FILE_SEPARATOR = "file.separator";

    /** パラメータCYCLEID index */
    public final static int PARA_IDX_CYCLE_ID = 0;

    /** コミットカウント */
    public final static String INSERT_COMMIT_CNT = "INSERT_COMMIT_CNT";

    /** コミットカウント */
    public final static String ENCRYPT_PASSWORD = "SINSEIIFPROJ";

    /** INPUTENCODING */
    public final static String INPUT_ENCODE = "INPUT_ENCODE";

    /** OUTPUTENCODING */
    public final static String OUTPUT_ENCODE = "OUTPUT_ENCODE";

    /** DB_INFO_PATH */
    public final static String DB_INFO_PATH = "./../properties";

    /** NEW FILE INDES */
    public final static int FILE_IDX_1 = 1;

    /** NEW FILE INDES */
    public final static int FILE_IDX_2 = 2;
    
    /** 符号:"_" */
    public final static String MASK_UNDER_LINE = "_";
    
    /** 入力ファイルのメッセージ */
    public final static String MSG_IN_FILE = "Input file name:{0}";

    /** 入力ファイルのメッセージ */
    public final static String MSG_NO_PATH = "The path \"{0}\" is not exists.";
    
    /** LAST DAY */
    public final static String LAST_DAY_31 = "31";
    
    /** 送付方法区分:1(営業店送付) */
    public final static String SEND_METHOD_KIND = "1";
    
    /** 送付コメントコード:27 */
    public final static String SEND_COMMENT_CD_27 = "27";
    
    /** 2 */
    public final static String KBN_2 = "2";
    
    /** 1 */
    public final static String KBN_1 = "1";

}
