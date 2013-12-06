package io.loli.jsr330;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Scope
@Target({ ElementType.TYPE })
public @interface SessionScoped {
}
