package com.melinkr.ice.annotation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * <pre>
 *    在服务类中标该类，以便确定服务方法所属的组及相关信息。由于ApiMethodGroup已经标注了
 * Spring的{@link Component}注解，因此标注了{@link WebService}的类自动成为Spring的Bean.
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface WebService {
}
