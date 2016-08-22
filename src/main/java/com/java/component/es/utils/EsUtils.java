package com.java.component.es.utils;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * ━━━━━━南无阿弥陀佛━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃stay hungry stay foolish
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━萌萌哒━━━━━━
 * Module Desc:com.java.component.es.utils
 * User: zjprevenge
 * Date: 2016/8/22
 * Time: 19:26
 */

public class EsUtils {

    public static final String DATE_REGEX = "^((\\\\d{2}(([02468][048])|([13579][26]))[\\\\-\\\\/\\\\s]?((((0?[13578])" +
            "|(1[02]))[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\\\-\\\\/\\\\s]?((0?[1-9])" +
            "|([1-2][0-9])|(30)))|(0?2[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])))))|(\\\\d{2}(([02468][1235679])" +
            "|([13579][01345789]))[\\\\-\\\\/\\\\s]?((((0?[13578])|(1[02]))[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])" +
            "|(3[01])))|(((0?[469])|(11))[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\\\-\\\\/\\\\s]?" +
            "((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\\\s(((0?[0-9])|([1-2][0-3]))\\\\:([0-5]?[0-9])((\\\\s)|(\\\\:([0-5]?[0-9])))))?$";

    /**
     * 根据对象构建XContentBuilder
     *
     * @param data 数据
     * @param <V>
     * @return
     */
    public static <V> XContentBuilder getXBuidler(V data) throws Exception {
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        Class<?> clazz = data.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            builder.field(field.getName(), field.get(data));
        }
        return builder.endObject();
    }

    /**
     * 数组值校验,数组每一个值不允许为空
     *
     * @param values
     * @return
     */
    public static boolean checkValue(Object[] values) {
        if (values == null || values.length == 0) {
            return false;
        }
        for (Object value : values) {
            if (value == null || value.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDateFromDate(Object date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 判断是否是日期
     *
     * @param date
     * @return
     */
    public static boolean isDate(Object date) {
        return date instanceof Date
                || Pattern.matches(DATE_REGEX, date.toString());
    }
}
