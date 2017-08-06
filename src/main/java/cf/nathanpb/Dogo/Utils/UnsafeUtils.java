package cf.nathanpb.Dogo.Utils;

import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * Created by nathanpb on 7/2/17.
 */
public class UnsafeUtils {
    private static final Method defineClass;
    private static final Field modifiersField;
    private static final Unsafe unsafe;

    static {
        Unsafe u;
        try {
            Constructor<Unsafe> ctor = Unsafe.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            u = ctor.newInstance();
        } catch (Exception e) {
            u = null;
        }
        unsafe = u;
        Method d;
        try {
            d = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
            d.setAccessible(true);
        } catch (NoSuchMethodException e) {
            d = null;
        }
        defineClass = d;
        Field f;
        try {
            f = Field.class.getDeclaredField("modifiers");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            f = null;
        }
        modifiersField = f;
    }
    @CallerSensitive
    public static Class<?> defineClass(String name, byte[] bytes, ClassLoader loader) {
        return defineClass(name, bytes, 0, bytes.length, loader, getCallingClass().getProtectionDomain());
    }

    public static Class<?> getCallingClass() {
        return getCallingClass(3);
    }

    public static Class<?> getCallingClass(int depth) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(stacktrace[depth].getClassName());
        } catch (ClassNotFoundException e) {
            throw new AssertionError();
        }
    }
    public static Class<?> defineClass(String name, byte[] bytes, int offset, int length, ClassLoader loader, ProtectionDomain domain) {
        if (unsafe == null) {
            if (defineClass == null)
                return null;
            try {
                return (Class<?>) defineClass.invoke(loader, name, bytes, offset, length, domain);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof ClassFormatError)
                    throw (ClassFormatError) cause;
                throw new InternalError(cause);
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            }
        }
        return unsafe.defineClass(name, bytes, offset, length, loader, domain);
    }
}