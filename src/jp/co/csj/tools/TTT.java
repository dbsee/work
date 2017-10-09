package jp.co.csj.tools;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.mydbsee.common.CmnFileUtils;


public class TTT {
    
//    public void test1(){
//        try {
//        	System.out.println("test1");
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl","oracletest","shuang");
//            conn.setAutoCommit(false);
//            Statement stat=conn.createStatement();
//            stat.execute("delete from empbig");
//            conn.commit();
//            Long startTime=System.currentTimeMillis();
//            for(int i=0;i<10000;i++){
//            	String sql = "insert into \"EMPBIG\" (\"EMPNO\", \"ENAME\", \"JOB\", \"MGR\", \"HIREDATE\", \"SAL\", \"COMM\", \"DEPTNO\") values ("+i+",'T00000447','S00000447',7698, to_date('1912/11/28 00:00:00','YYYY/MM/DD HH24:MI:SS'),1500,0,30)";
//                stat.execute(sql);
//            }
//            conn.commit();
//            stat.close();
//            long endTime=System.currentTimeMillis();
//            System.out.println(endTime-startTime);
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//   
//    public void test2(){
//        try {
//        	System.out.println("test2");
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl","oracletest","shuang");
//            conn.setAutoCommit(false);
//            Statement state=conn.createStatement();
//            state.execute("delete from empbig");
//            conn.commit();
//            PreparedStatement stat = conn.prepareStatement("insert into \"EMPBIG\" (\"EMPNO\", \"ENAME\", \"JOB\", \"MGR\", \"HIREDATE\", \"SAL\", \"COMM\", \"DEPTNO\") values (?,'T00000447','S00000447',7698, to_date('1912/11/28 00:00:00','YYYY/MM/DD HH24:MI:SS'),1500,0,30)");
//            Long startTime=System.currentTimeMillis();
//                stat.setObject(1, 1);
//                stat.execute();
//            //conn.commit();
//            stat.close();
//            long endTime=System.currentTimeMillis();
//            System.out.println(endTime-startTime);
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//   
//    public void test3(){
//        try {
//        	System.out.println("test3");
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl","oracletest","shuang");
//            conn.setAutoCommit(false);
//            Statement state=conn.createStatement();
//            state.execute("delete from empbig");
//            conn.commit();
//            PreparedStatement stat = conn.prepareStatement("insert into \"EMPBIGCOMP\" (\"EMPNO\", \"ENAME\", \"JOB\", \"MGR\") values (?,?,?,?)");
//            Long startTime=System.currentTimeMillis();
//            for(int i=0;i<1000000;i++){
//                stat.setObject(1, i);
//                stat.setObject(2, "1");
//                stat.setObject(3, "1");
//                stat.setObject(4, 106);
//                
//                stat.addBatch();
//            }
//            //stat.clearBatch();
//            int[] a = stat.executeBatch();
//            conn.commit();
//            stat.clearBatch();
//            stat.close();
//            long endTime=System.currentTimeMillis();
//            System.out.println("run time：" + CsjDateUtil.getMsHour(endTime - startTime));
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
   
	public static void testMysql() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("load jdbc successfully");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysqltest", "root", "shuang");
			Statement stat=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			stat.setFetchSize(Integer.MIN_VALUE);
				ResultSet rs = stat.executeQuery("SELECT * FROM emp");
				while (rs.next()) {
					System.out.println(rs.getString("EMPNO"));
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//    public static void main(String [] aaa) throws Throwable{
////        new TTT().test1();//
////        new TTT().test2();//22637
//        //new TTT().test3();//63
//    	
////    	testMysql();
//    }
	public static void main(String[] args) {
		
		try {
			List<File> fLst = CmnFileUtils.getFilesList("D:\\IDE\\eclipse-rcp-luna-SR2-win32\\eclipse\\workspace\\CsjToolsPic\\AutoDb\\", true);
			for (File f :fLst) {
				f.delete();
			}
			
			
//			FileInputStream fileInputStream = new FileInputStream(new File("D:\\javaTools\\eclipse\\workspace\\CsjToolsPic\\dbInfo\\template\\CellStyle.xlsx"));
//			XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}