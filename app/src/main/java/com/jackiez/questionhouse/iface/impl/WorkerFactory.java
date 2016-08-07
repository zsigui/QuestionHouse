package com.jackiez.questionhouse.iface.impl;

import com.jackiez.questionhouse.iface.IWorker;
import com.jackiez.questionhouse.model.Exam;
import com.jackiez.questionhouse.utils.FileUtil;
import com.jackiez.questionhouse.utils.ToastUtil;
import com.jackiez.questionhouse.utils.Utils;
import com.jackiez.questionhouse.utils.log.AppDebugConfig;

/**
 * 简单工厂模式，根据文件名后缀生成指定处理器并进行处理返回处理结果
 *
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/4
 */
public class WorkerFactory implements IWorker {

    private WorkerFactory(){}

    private static final class SingletonHolder {
        static final WorkerFactory instance = new WorkerFactory();
    }

    public static WorkerFactory getInstance() {
        return SingletonHolder.instance;
    }

    public Exam process(String path) {
        Exam result = null;
        AppDebugConfig.d("处理文件路径：" + path);
        try {
            if (Utils.isEmpty(path)) {
                ToastUtil.showShort("传入空文件路径");
                return null;
            }
            String ext = FileUtil.getExtension(path);
            if ("xls".equalsIgnoreCase(ext)) {
                result = new XLSWorker().process(path);
            } else if ("xlsx".equalsIgnoreCase(ext)) {
                result = new XLSXWorker().process(path);
            } else {
                ToastUtil.showShort("传入文件路径后缀错误：" + path);
                return null;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }
}
