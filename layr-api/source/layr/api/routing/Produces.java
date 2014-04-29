package layr.api.routing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines which ContentType a method or class will produce when it returns
 * a non-Response object. This is almost a copy of {@link javax.ws.rs.Produces}
 * but, without default value, forcing the developer to specify one. 
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Produces {

    String value();

}