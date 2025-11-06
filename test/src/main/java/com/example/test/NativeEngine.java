package com.example.test;

/**
 * @author Zhenxi on 2023/10/30
 */
public class NativeEngine {
  /**
   * 跟踪全部的Java方法调用
   */
  public static native void startMethodTrace();

  public static native void stopMethodTrace();
}
