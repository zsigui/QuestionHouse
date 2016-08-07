package com.jackiez.questionhouse.iface;

import com.jackiez.questionhouse.model.Exam;

/**
 * 操作文本的处理接口，通过实现该接口，完成对不同文件类型的处理
 *
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public interface IWorker {

    /**
     * 执行操作过程接口，需要实现该方法指定具体操作流程
     * @param path 待操作文件路径
     * @return
     */
    Exam process(String path);
}
