package com.melinkr.ice.annotation;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */

import java.lang.annotation.*;

/**
 * <pre>
 *     如果标注在请求类的属性上，则表示该属性无需进行签名
 * </pre>
 *
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoSign {
}
