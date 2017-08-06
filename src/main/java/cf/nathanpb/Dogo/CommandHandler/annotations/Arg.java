package cf.nathanpb.Dogo.CommandHandler.annotations;

import cf.nathanpb.Dogo.CommandHandler.enums.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nathanpb on 7/26/17.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {

    String arg();

    Permission[] allow() default Permission.COMMON;
    Permission[] disallow() default {};

    String description();
    String usage();

    int argsLenght() default 0;
}
