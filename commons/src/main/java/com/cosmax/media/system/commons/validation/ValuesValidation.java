package com.cosmax.media.system.commons.validation;

import com.cosmax.media.system.commons.BaseResult;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @program: media-system
 * @description: 校验实体类参数
 * @author: Cosmax
 * @create: 2020/02/29 11:23
 */
public class ValuesValidation<T> implements Serializable {

    private static final long serialVersionUID = -6596881353732851296L;



    /**
     * 实体类字段判空
     * @param t 实体类T
     * @param keys 字段名
     * @return 响应结果 {@link BaseResult}
     */
    public BaseResult checkIsNull(T t, String... keys){
        Class<?> clzz = t.getClass();
        Field[] declaredFields = clzz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String name = declaredField.getName(); // 属性名
            String type = declaredField.getType().toString();

            // 判断字段是否在keys中
            int flag = 0; // 用来标记是否为判空字段
            for (String key : keys) {
                if (key.equals(name)){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0){
                continue;
            }

            Object value = null;
            try {
                value = declaredField.get(t);
                if (value == null){
                    return BaseResult.fail("字段["+ name +"]不能为空！");
                }
            } catch (IllegalAccessException e) {
                return BaseResult.fail("字段["+ name +"]值获取失败，请检查字段属性！");
            }


            if (type.equals(VALUETYPE.STRING_TYPE)
                    && StringUtils.isBlank((String)value)){
                return BaseResult.fail("字段["+ name +"]不能为空！");
            }
            if (type.equals(VALUETYPE.INTEGER_TYPE)){
                String lowerName = name.toLowerCase();
                // 状态可为0
                if (!(lowerName.contains("is") || lowerName.contains("status") || lowerName.contains("type"))
                        && (Integer)value == 0){
                    return BaseResult.fail("字段["+ name +"]不能为空！");
                }
            }
            if (type.equals(VALUETYPE.LONG_TYPE) && (Long)value == 0){
                return BaseResult.fail("字段["+ name +"]不能为空！");
            }
            if (type.equals(VALUETYPE.FLOAT_TYPE) && (Float)value == 0){
                return BaseResult.fail("字段["+ name +"]不能为空！");
            }
            if (type.equals(VALUETYPE.DOUBLE_TYPE) && (Double)value == 0){
                return BaseResult.fail("字段["+ name +"]不能为空！");
            }

        }
        return BaseResult.success("校验成功！");
    }

    private static class VALUETYPE {
        private static final String clazz = "class ";
        static final String STRING_TYPE = clazz + "java.lang.String";
        static final String BOOLEAN_TYPE = clazz + "java.lang.Boolean";
        static final String CHARACTER_TYPE = clazz + "java.lang.Character";
        static final String BYTE_TYPE = clazz + "java.lang.Byte";
        static final String FLOAT_TYPE = clazz + "java.lang.Float";
        static final String INTEGER_TYPE = clazz + "java.lang.Integer";
        static final String DOUBLE_TYPE = clazz + "java.lang.Double";
        static final String LONG_TYPE = clazz + "java.lang.Long";
        static final String SHORT_TYPE = clazz + "java.lang.Short";
    }
}
