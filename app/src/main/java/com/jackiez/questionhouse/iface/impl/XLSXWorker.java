package com.jackiez.questionhouse.iface.impl;

import android.util.Xml;

import com.jackiez.questionhouse.iface.AbsWorker;
import com.jackiez.questionhouse.utils.log.AppDebugConfig;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public class XLSXWorker extends AbsWorker {

    @Override
    protected List<List<Object>> extractTableToLists(String path) {
        List<List<Object>> dataList = new ArrayList<>();
        String str_c = "";
        String v = null;
        boolean flat = false;
        List<String> ls = new ArrayList<String>();
        try {
            ZipFile xlsxFile = new ZipFile(new File(path));
            ZipEntry sharedStringXML = xlsxFile.getEntry("xl/sharedStrings.xml");
            if (sharedStringXML == null) {
                AppDebugConfig.d("空文件:" + path);
                return dataList;
            }
            InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        if (tag.equalsIgnoreCase("t")) {
                            ls.add(xmlParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                evtType = xmlParser.next();
            }
            ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/sheet1.xml");
            InputStream inputStreamSheet = xlsxFile.getInputStream(sheetXML);
            XmlPullParser xmlParserSheet = Xml.newPullParser();
            xmlParserSheet.setInput(inputStreamSheet, "utf-8");
            int evtTypesheet = xmlParserSheet.getEventType();
            List<Object> objList = new ArrayList<Object>();
            String val = null;
            boolean null_row = true;

            while (evtTypesheet != XmlPullParser.END_DOCUMENT) {
                switch (evtTypesheet) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParserSheet.getName();
                        if (tag.equalsIgnoreCase("row")) {
                        } else if (tag.equalsIgnoreCase("c")) {
                            String t = xmlParserSheet.getAttributeValue(null, "t");
                            if (t != null) {
                                flat = true; // 字符串型
                                // Log.d(TAG, flat + "有");
                            } else { // 非字符串型，可能是整型
                                // Log.d(TAG, flat + "没有");
                                flat = false;
                            }
                        } else if (tag.equalsIgnoreCase("v")) {
                            v = xmlParserSheet.nextText();
                            if (v != null) {
                                if (flat) {
                                    str_c += ls.get(Integer.parseInt(v)) + "  ";
                                    val = ls.get(Integer.parseInt(v));
                                    null_row = false;
                                } else {
                                    str_c += v + "  ";
                                    val = v;
                                }
                                objList.add(val);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlParserSheet.getName().equalsIgnoreCase("row") && v != null) {
                            str_c += "\n";
                            if (!null_row) {
                                dataList.add(objList);
                                null_row = true;
                            }
                            objList = new ArrayList<Object>();
                        }
                        break;
                }
                evtTypesheet = xmlParserSheet.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        if (str_c.isEmpty()) {
            AppDebugConfig.d("解析文件出现问题");
        }

        return dataList;
    }
}
