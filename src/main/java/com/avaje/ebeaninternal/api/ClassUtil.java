package com.avaje.ebeaninternal.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper to find classes taking into account the context class loader.
 */
public class ClassUtil {

  private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

  private static boolean preferContext = true;

  /**
   * Load a class taking into account a context class loader (if present).
   */
  public static Class<?> forName(String name) throws ClassNotFoundException {
    return forName(name, null);
  }

  /**
   * Load a class taking into account a context class loader (if present).
   */
  public static Class<?> forName(String name, Class<?> caller) throws ClassNotFoundException {

    if (caller == null) {
      caller = ClassUtil.class;
    }
    ClassLoadContext ctx = ClassLoadContext.of(caller, preferContext);

    return ctx.forName(name);
  }


  public static ClassLoader getClassLoader(Class<?> caller, boolean preferContext) {

    if (caller == null) {
      caller = ClassUtil.class;
    }
    ClassLoadContext ctx = ClassLoadContext.of(caller, preferContext);
    ClassLoader classLoader = ctx.getDefault(preferContext);
    if (ctx.isAmbiguous()) {
      logger.info("Ambigous ClassLoader (Context vs Caller) chosen " + classLoader);
    }
    return classLoader;
  }

  /**
   * Return true if javax validation annotations like Size and NotNull are present.
   */
  public static boolean isJavaxValidationAnnotationsPresent() {
    return isPresent("javax.validation.constraints.NotNull", null);
  }

  /**
   * Return true if Jackson annotations like JsonIgnore are present.
   */
  public static boolean isJacksonAnnotationsPresent() {
    return isPresent("com.fasterxml.jackson.annotation.JsonIgnore", null);
  }

  /**
   * Return true if Jackson ObjectMapper is present.
   */
  public static boolean isJacksonObjectMapperPresent() {
    return isPresent("com.fasterxml.jackson.databind.ObjectMapper", null);
  }


  /**
   * Return true if the given class is present.
   */
  public static boolean isPresent(String className) {
    return isPresent(className, null);
  }

  /**
   * Return true if the given class is present.
   */
  public static boolean isPresent(String className, Class<?> caller) {
    try {
      forName(className, caller);
      return true;
    } catch (Throwable ex) {
      // Class or one of its dependencies is not present...
      return false;
    }
  }

  /**
   * Return a new instance of the class using the default constructor.
   */
  public static Object newInstance(String className) {
    return newInstance(className, null);
  }

  /**
   * Return a new instance of the class using the default constructor.
   */
  public static Object newInstance(String className, Class<?> caller) {

    try {
      Class<?> cls = forName(className, caller);
      return cls.newInstance();
    } catch (Exception e) {
      String msg = "Error constructing " + className;
      throw new IllegalArgumentException(msg, e);
    }
  }
}

