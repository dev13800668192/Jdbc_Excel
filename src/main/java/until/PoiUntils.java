package until;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;


import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author by
 * @version 1.0
 * @date 2020/1/16 14:01
 */
public class PoiUntils {

    public  Boolean creatExcel(String databaseName, String filepath, PoiUntil poi){
        boolean isSuccess =false;
        List<String>tableNames=poi.getTableNames();
        int tableNum = tableNames.size();
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int num =0;num<tableNum;num++) {
            String tableName =tableNames.get(num);
            Sheet sheet = wb.createSheet();
            sheet.setDefaultColumnWidth(20);
            sheet.setDefaultRowHeight((short) (100*5));

            //表头样式
            HSSFCellStyle headStyle =wb.createCellStyle();
            headStyle =poi.getHeaderStyle(wb,headStyle);

            //非表头样式
            HSSFCellStyle style  =wb.createCellStyle();
            style =poi.getStyle(wb,style);

            Row row = sheet.createRow(0);
            row.createCell(0).setCellStyle(headStyle);
            row.getCell(0).setCellValue("表名");
            row.createCell(1).setCellStyle(headStyle);
            row.getCell(1).setCellValue(tableName);

            row=sheet.createRow(1);
            row.createCell(0).setCellStyle(headStyle);
            row.getCell(0).setCellValue("列名");
            row.createCell(1).setCellStyle(headStyle);
            row.getCell(1).setCellValue("数据类型");
            row.createCell(2).setCellStyle(headStyle);
            row.getCell(2).setCellValue("是否为空");
            row.createCell(3).setCellStyle(headStyle);
            row.getCell(3).setCellValue("默认值");
            row.createCell(4).setCellStyle(headStyle);
            row.getCell(4).setCellValue("主键");
            row.createCell(5).setCellStyle(headStyle);
            row.getCell(5).setCellValue("备注");

            Iterator<Map<String,Object>> it =poi.getColumnStruct(tableName).iterator();
            int index =1;
            while(it.hasNext()){
                index++;
                row=sheet.createRow(index);
                Map<String,Object> data =it.next();
                int i=0;
                for (String key :data.keySet()){
                    HSSFCell cell = (HSSFCell) row.createCell(i);
                    cell.setCellStyle(style);
                    HSSFRichTextString text =new HSSFRichTextString(data.get(key)+"");
                    cell.setCellValue(text);
                    i++;
                }
            }
            wb.setSheetName(num,tableName);
        }

        try{
            FileOutputStream out = new FileOutputStream(new File(filepath+databaseName+".xls"));
            wb.write(out);
            out.close();
            isSuccess = true;
        }catch (IOException e){
            e.printStackTrace();
        }


        return  isSuccess;
    }

//    public static void main(String[] args) throws IOException {
//        String filepath ="d:/";
//       boolean isSuccess =false;
//       PoiUntil poi = new PoiUntil();
//       isSuccess =creatExcel(poi.getDatabaseName(),filepath,poi);
//        System.out.println(isSuccess);
//    }
}
