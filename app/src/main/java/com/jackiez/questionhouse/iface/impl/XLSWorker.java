package com.jackiez.questionhouse.iface.impl;

import com.jackiez.questionhouse.iface.AbsWorker;
import com.jackiez.questionhouse.utils.log.AppDebugConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public class XLSWorker extends AbsWorker {

    @Override
    protected List<List<Object>> extractTableToLists(String path) {
        List<List<Object>> dataList = new ArrayList<>();
        try {
            Workbook book = Workbook.getWorkbook(new File(path));
            // book.getNumberOfSheets();  //获取sheet页的数目
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            AppDebugConfig.d("当前工作表的名字:" + sheet.getName());
            AppDebugConfig.d("总行数:" + Rows + ", 总列数:" + Cols);

            List<Object> objList = new ArrayList<Object>();
            String val = null;
            boolean null_row = true;
            for (int i = 0; i < Rows; i++) {
                for (int j = 0; j < Cols; j++) {
                    // getCell(Col,Row)获得单元格的值，注意getCell格式是先列后行，不是常见的先行后列
                    val = (sheet.getCell(j, i)).getContents();
                    if (val == null || val.equals("")) {
                        val = "null";
                    } else {
                        null_row = false;
                    }
                    objList.add(val);
                }
                if (!null_row) {
                    dataList.add(objList);
                    null_row = true;
                }
                objList = new ArrayList<Object>();
            }
            book.close();
        } catch (Exception e) {
            AppDebugConfig.d(e.getMessage());
        }
        return dataList;
    }
}
