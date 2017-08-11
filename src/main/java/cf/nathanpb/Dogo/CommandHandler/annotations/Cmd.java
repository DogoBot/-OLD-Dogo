package cf.nathanpb.Dogo.CommandHandler.annotations;

import cf.nathanpb.Dogo.CommandHandler.enums.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nathanpb on 7/26/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    String cmd();

    Permission[] allow() default Permission.COMMON;
    Permission[] disallow() default {};

    String description();
    String usage();

    int argslengh() default 0;
    int cooldown() default 10;

    boolean freeArgs() default false;
}