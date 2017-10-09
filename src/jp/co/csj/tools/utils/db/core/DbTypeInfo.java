/**
 *
 */
package jp.co.csj.tools.utils.db.core;

import java.util.HashMap;

import jp.co.csj.tools.utils.db.oracle.CsjOracleColType;
import jp.co.csj.tools.utils.db.postgre.CsjPostgreColType;
import jp.co.csj.tools.utils.db.sqlserver.CsjSqlServerColType;


/**
 * @author Think
 *
 */
public class DbTypeInfo {

	public static HashMap<Integer, HashMap<String, Integer>> dbTypeInfoMap = new HashMap<Integer, HashMap<String, Integer>>();
	static {
		HashMap<String, Integer> oracleMap = new HashMap<String, Integer>();
		HashMap<String, Integer> sqlServerMap = new HashMap<String, Integer>();
		HashMap<String, Integer> mysqlMap = new HashMap<String, Integer>();
		HashMap<String, Integer> posgreMap = new HashMap<String, Integer>();

		/** ORACLE DB COLUMN TYPE BINARY_DOUBLE */
		oracleMap.put(CsjOracleColType.TYPE_BINARY_DOUBLE,DbInfo.TABLE_COL_TYPE_NUM);
		/** ORACLE DB COLUMN TYPE BINARY_FLOAT */
		oracleMap.put(CsjOracleColType.TYPE_BINARY_FLOAT,DbInfo.TABLE_COL_TYPE_NUM);

		/** ORACLE DB COLUMN TYPE BLOB */
		oracleMap.put(CsjOracleColType.TYPE_BLOB,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE CLOB */
		oracleMap.put(CsjOracleColType.TYPE_CLOB,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE CHAR */
		oracleMap.put(CsjOracleColType.TYPE_CHAR,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE DATE */
		oracleMap.put(CsjOracleColType.TYPE_DATE,DbInfo.TABLE_COL_TYPE_DATE);

		/** ORACLE DB COLUMN TYPE INTERVALDS */
		oracleMap.put(CsjOracleColType.TYPE_INTERVALDS,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE TYPE_INTERVAL_DAY_2_SECOND */
		oracleMap.put(CsjOracleColType.TYPE_INTERVAL_DAY_2_SECOND,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE INTERVALYM */
		oracleMap.put(CsjOracleColType.TYPE_INTERVALYM,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE INTERVAL YEAR(2) TO MONTH */
		oracleMap.put(CsjOracleColType.TYPE_INTERVAL_YEAR_2MONTH,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE LONG */
		oracleMap.put(CsjOracleColType.TYPE_LONG,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE NCLOB */
		oracleMap.put(CsjOracleColType.TYPE_NCLOB,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE NVARCHAR2 */
		oracleMap.put(CsjOracleColType.TYPE_NVARCHAR2,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE NUMBER */
		oracleMap.put(CsjOracleColType.TYPE_NUMBER,DbInfo.TABLE_COL_TYPE_NUM);

		/** ORACLE DB COLUMN TYPE RAW */
		oracleMap.put(CsjOracleColType.TYPE_RAW ,DbInfo.TABLE_COL_TYPE_STR);

		/** ORACLE DB COLUMN TYPE TIMESTAMP */
		oracleMap.put(CsjOracleColType.TYPE_TIMESTAMP,DbInfo.TABLE_COL_TYPE_DATE);

		/** ORACLE DB COLUMN TYPE TIMESTAMP(6) */
		oracleMap.put(CsjOracleColType.TYPE_TIMESTAMP_6,DbInfo.TABLE_COL_TYPE_DATE);

		/** ORACLE DB COLUMN TYPE TIMESTAMP WITH LOCAL TIME ZONE */
		oracleMap.put(CsjOracleColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE,DbInfo.TABLE_COL_TYPE_DATE);

		/** ORACLE DB COLUMN TYPE TIMESTAMP(6) WITH LOCAL TIME ZONE */
		oracleMap.put(CsjOracleColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE_6 ,DbInfo.TABLE_COL_TYPE_DATE);

		/** ORACLE DB COLUMN TYPE TIMESTAMP WITH TIME ZONE */
		oracleMap.put(CsjOracleColType.TYPE_TIMESTAMP_WITH_TIME_ZONE ,DbInfo.TABLE_COL_TYPE_DATE);

		/** ORACLE DB COLUMN TYPE TIMESTAMP(6) WITH TIME ZONE */
		oracleMap.put(CsjOracleColType.TYPE_TIMESTAMP_WITH_TIME_ZONE_6 ,DbInfo.TABLE_COL_TYPE_DATE);

		/** ORACLE DB COLUMN TYPE VARCHAR2 */
		oracleMap.put(CsjOracleColType.TYPE_VARCHAR2 ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE BIGINT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_BIGINT ,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE BINARY */
		sqlServerMap.put(CsjSqlServerColType.TYPE_BINARY ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE BIT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_BIT ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE CHAR */
		sqlServerMap.put(CsjSqlServerColType.TYPE_CHAR ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE DATE */
		sqlServerMap.put(CsjSqlServerColType.TYPE_DATE ,DbInfo.TABLE_COL_TYPE_DATE);

		/** SQL_SERVER DB COLUMN TYPE DATETIME */
		sqlServerMap.put(CsjSqlServerColType.TYPE_DATETIME ,DbInfo.TABLE_COL_TYPE_DATE);

		/** SQL_SERVER DB COLUMN TYPE DATETIME2 */
		sqlServerMap.put(CsjSqlServerColType.TYPE_DATETIME2 ,DbInfo.TABLE_COL_TYPE_DATE);

		/** SQL_SERVER DB COLUMN TYPE DATETIMEOFFSET */
		sqlServerMap.put(CsjSqlServerColType.TYPE_DATETIMEOFFSET ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE DECIMAL */
		sqlServerMap.put(CsjSqlServerColType.TYPE_DECIMAL ,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE FLOAT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_FLOAT ,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE GEOGRAPHY */
		sqlServerMap.put(CsjSqlServerColType.TYPE_GEOGRAPHY  ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE GEOMETRY */
		sqlServerMap.put(CsjSqlServerColType.TYPE_GEOMETRY  ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE HIERARCHYID */
		sqlServerMap.put(CsjSqlServerColType.TYPE_HIERARCHYID ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE IMAGE */
		sqlServerMap.put(CsjSqlServerColType.TYPE_IMAGE ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE SQL_VARIANT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_SQL_VARIANT  ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE INT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_INT ,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE MONEY */
		sqlServerMap.put(CsjSqlServerColType.TYPE_MONEY ,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE NCHAR */
		sqlServerMap.put(CsjSqlServerColType.TYPE_NCHAR ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE NTEXT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_NTEXT ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE NUMERIC */
		sqlServerMap.put(CsjSqlServerColType.TYPE_NUMERIC ,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE NVARCHAR */
		sqlServerMap.put(CsjSqlServerColType.TYPE_NVARCHAR ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE REAL */
		sqlServerMap.put(CsjSqlServerColType.TYPE_REAL ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE SMALLDATETIME */
		sqlServerMap.put(CsjSqlServerColType.TYPE_SMALLDATETIME ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE SMALLINT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_SMALLINT ,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE SMALLMONEY */
		sqlServerMap.put(CsjSqlServerColType.TYPE_SMALLMONEY,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE SMALLMONEY */
		sqlServerMap.put(CsjSqlServerColType.TYPE_SMALLMONEY1,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE TEXT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_TEXT,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE TIME */
		sqlServerMap.put(CsjSqlServerColType.TYPE_TIME,DbInfo.TABLE_COL_TYPE_DATE);

		/** SQL_SERVER DB COLUMN TYPE TIMESTAMP */
		sqlServerMap.put(CsjSqlServerColType.TYPE_TIMESTAMP,DbInfo.TABLE_COL_TYPE_DATE);

		/** SQL_SERVER DB COLUMN TYPE TINYINT */
		sqlServerMap.put(CsjSqlServerColType.TYPE_TINYINT,DbInfo.TABLE_COL_TYPE_NUM);

		/** SQL_SERVER DB COLUMN TYPE UNIQUEIDENTIFIER */
		sqlServerMap.put(CsjSqlServerColType.TYPE_UNIQUEIDENTIFIER ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE VARBINARY */
		sqlServerMap.put(CsjSqlServerColType.TYPE_VARBINARY ,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE VARCHAR */
		sqlServerMap.put(CsjSqlServerColType.TYPE_VARCHAR,DbInfo.TABLE_COL_TYPE_STR);

		/** SQL_SERVER DB COLUMN TYPE XML */
		sqlServerMap.put(CsjSqlServerColType.TYPE_XML,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE NUMERIC */
		posgreMap.put(CsjPostgreColType.TYPE_NUMERIC,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE VARCHAR */
		posgreMap.put(CsjPostgreColType.TYPE_VARCHAR,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE TIMESTAMP */
		posgreMap.put(CsjPostgreColType.TYPE_TIMESTAMP,DbInfo.TABLE_COL_TYPE_DATE);

		/** POSTGRE DB COLUMN TYPE BINARY_FLOAT */
		posgreMap.put(CsjPostgreColType.TYPE_BINARY_FLOAT,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE BLOB */
		posgreMap.put(CsjPostgreColType.TYPE_BLOB ,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE CLOB */
		posgreMap.put(CsjPostgreColType.TYPE_CLOB ,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE CHAR */
		posgreMap.put(CsjPostgreColType.TYPE_CHAR ,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE DATE */
		posgreMap.put(CsjPostgreColType.TYPE_DATE,DbInfo.TABLE_COL_TYPE_DATE);

		/** POSTGRE DB COLUMN TYPE INTERVALDS */
		posgreMap.put(CsjPostgreColType.TYPE_INTERVALDS ,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE TYPE_INTERVAL_DAY_2_SECOND */
		posgreMap.put(CsjPostgreColType.TYPE_INTERVAL_DAY_2_SECOND ,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE INTERVALYM */
		posgreMap.put(CsjPostgreColType.TYPE_INTERVALYM  ,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE INTERVAL YEAR(2) TO MONTH */
		posgreMap.put(CsjPostgreColType.TYPE_INTERVAL_YEAR_2MONTH ,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE LONG */
		posgreMap.put(CsjPostgreColType.TYPE_LONG  ,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE NCLOB */
		posgreMap.put(CsjPostgreColType.TYPE_NCLOB ,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE NVARCHAR2 */
		posgreMap.put(CsjPostgreColType.TYPE_NVARCHAR2  ,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE NUMBER */
		posgreMap.put(CsjPostgreColType.TYPE_NUMBER ,DbInfo.TABLE_COL_TYPE_NUM);

		/** POSTGRE DB COLUMN TYPE RAW */
		posgreMap.put(CsjPostgreColType.TYPE_RAW,DbInfo.TABLE_COL_TYPE_STR);

		/** POSTGRE DB COLUMN TYPE TIMESTAMP(6) */
		posgreMap.put(CsjPostgreColType.TYPE_TIMESTAMP_6,DbInfo.TABLE_COL_TYPE_DATE);

		/** POSTGRE DB COLUMN TYPE TIMESTAMP WITH LOCAL TIME ZONE */
		posgreMap.put(CsjPostgreColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE ,DbInfo.TABLE_COL_TYPE_DATE);

		/** POSTGRE DB COLUMN TYPE TIMESTAMP(6) WITH LOCAL TIME ZONE */
		posgreMap.put(CsjPostgreColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE_6 ,DbInfo.TABLE_COL_TYPE_DATE);

		/** POSTGRE DB COLUMN TYPE TIMESTAMP WITH TIME ZONE */
		posgreMap.put(CsjPostgreColType.TYPE_TIMESTAMP_WITH_TIME_ZONE,DbInfo.TABLE_COL_TYPE_DATE);

		/** POSTGRE DB COLUMN TYPE TIMESTAMP(6) WITH TIME ZONE */
		posgreMap.put(CsjPostgreColType.TYPE_TIMESTAMP_WITH_TIME_ZONE_6,DbInfo.TABLE_COL_TYPE_DATE);



	}

}
