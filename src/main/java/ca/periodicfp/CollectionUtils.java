package ca.periodicfp;

import java.util.List;

/**
 * Collection相关的工具
 * @author mjc
 */
public class CollectionUtils {

    /**
     * 将List对象转换为用"[]"包围、以", "间隔的字符串
     * @param list 待转换的List对象
     * @return 转换后的字符串
     */
    public static String toString(List<?> list) {
        StringBuilder ret = new StringBuilder("[");
        list.forEach(item -> ret.append(item).append(", "));
        if (ret.length() > 1) {
            ret.delete(ret.length() - 2, ret.length());
        }
        ret.append("]");
        return ret.toString();
    }
}
