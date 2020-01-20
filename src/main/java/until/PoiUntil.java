package until;

import org.apache.poi.hssf.usermodel.*;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.sql.*;
import java.util.*;

/**
 * @author by
 * @version 1.0
 * @date 2020/1/16 13:44
 */
public class PoiUntil {

    private static  String DRIVER ;
    private static  String URL ;
    private static  String USERNAME;
    private static  String PASSWORD ;
    private static String databaseName ;
    private static String AllDATABASE;


    private static final String SQL = "SELECT * FROM ";

    public PoiUntil(String url,String username,String password,String baseName,String baseUrl) {
        DRIVER="com.mysql.jdbc.Driver";
        URL=url;
        USERNAME=username;
        PASSWORD=password;
        databaseName=baseName;
        AllDATABASE=baseUrl;
        try {
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL,USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public  String getDatabaseName() {
        return databaseName;
    }


    /**
     * 获取数据库连接
     *
     * @return
     */
    public  Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL,USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public  void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据库下的所有数据库
     * @return
     */
    public List<String> getAllDataBase() throws SQLException {
        List<String> databaseNames =new ArrayList<String>();
        Connection conn =DriverManager.getConnection(AllDATABASE,USERNAME,PASSWORD);
        PreparedStatement pStemt = null;
        ResultSet rs = null;
        String tableSql = "SHOW DATABASES";
        try{
            pStemt = conn.prepareStatement(tableSql);
            rs =pStemt.executeQuery();
            while(rs.next()){
                String database=rs.getString("Database");
                databaseNames.add(database);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  databaseNames;
    }

    /**
     * 获取数据库下的所有表名
     */
    public  List<String> getTableNames() {
        List<String> tableNames = new ArrayList<String>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData db = conn.getMetaData();
            rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
           e.printStackTrace();
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
               e.printStackTrace();
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     * @param tableName 表名
     * @return
     */
    public  List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<String>();
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
           e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                  e.printStackTrace();
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     */
    public  List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<String>();
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
           e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                   e.printStackTrace();
                }
            }
        }
        return columnTypes;
    }

    /**
     * 获取表中所有内容
     * @param tableName
     */
    public  List<Map<String,Object>> getColumnDate(String tableName) {
        List<Map<String ,Object>> columnData = new ArrayList<Map<String, Object>>();
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            ResultSet rs = pStemt.executeQuery();
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();
            while(rs.next()){
                Map<String,Object> map =new LinkedHashMap<String, Object>();
                for (int i=1; i<=size;i++){
                    map.put(rsmd.getColumnName(i),rs.getObject(i));
                }
                columnData.add(map);
            }

        } catch (SQLException e) {
          e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                   e.printStackTrace();
                }
            }
        }
        return columnData;
    }

    /**
     * 获取表中表结构
     * @param tableName
     */

    public  List<Map<String,Object>> getColumnStruct(String tableName) {
        List<Map<String,Object>> columnStruct = new ArrayList<Map<String,Object>>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData db = conn.getMetaData();
            rs = db.getColumns(null, "%", tableName, "%");
            while(rs.next()) {
                Map<String,Object> map =new LinkedHashMap<String, Object>();
                String columnName =rs.getString("COLUMN_NAME");
                String columnType =rs.getString("TYPE_NAME");
                String dataSize  =rs.getString("COLUMN_SIZE");
                String nullable =rs.getString("NULLABLE");
                String def=rs.getString("COLUMN_DEF");
                String remarks =rs.getString("REMARKS");
                String TypeSize =columnType.toLowerCase()+"("+dataSize+")";
                String key =getColumnKey(tableName);
                map.put("columnName",columnName);
                map.put("columnType",TypeSize);
                if (nullable.equals(1)){
                    map.put("nullable","YES");
                }else {
                    map.put("nullable","NO");
                }
                map.put("def",def);
                if (key!=null&&key.equals(columnName)){
                    map.put("key","Y");
                }else {
                    map.put("key","");
                }
                map.put("remrk",remarks);
                columnStruct.add(map);
            }
        } catch (SQLException e) {
          e.printStackTrace();
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
               e.printStackTrace();
            }
        }
        return columnStruct;
    }

    /**
     * 获取表中表中主键
     * @param tableName
     */

    public  String getColumnKey(String tableName) {
        String PrimaryKey =null;
        List<Map<String,Object>> columnKey = new ArrayList<Map<String,Object>>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData db = conn.getMetaData();
            rs = db.getPrimaryKeys(null, "%", tableName);
            if (rs!=null){
                while(rs.next()) {
                  PrimaryKey =rs.getString("COLUMN_NAME");
                }
            }
        } catch (SQLException e) {
           e.printStackTrace();
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
              e.printStackTrace();
            }
        }
        return PrimaryKey;
    }

    /**
     * 设置表头样式及参数
     * @param
     */
    public HSSFCellStyle getHeaderStyle(HSSFWorkbook wb,HSSFCellStyle headStyle){
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //边框设置
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        //字体设置
        HSSFFont font =wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        headStyle.setFont(font);
        return headStyle;
    }

    /**
     * 设置普通样式及参数
     * @param
     */
    public HSSFCellStyle getStyle(HSSFWorkbook wb,HSSFCellStyle style){

        //边框设置
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        //背景设置
        HSSFFont font =wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        return style;
    }
}
