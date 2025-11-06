package com.zhenxi.hunter.stringfog;

import com.github.megatronking.stringfog.IStringFog;

import java.nio.charset.StandardCharsets;

/**
 * 自定义算法实现，此文件存储目录路径须和其包名一致
 *
 * @author Sundy
 * @since 2019/3/4 23:41
 */
public class CustomStringFogImpl implements IStringFog {
    /**
     * 主要目的为了防止mt这种自动解密字符串的操作。
     * 只有执行阶段他才会去解密
     */
    public static volatile boolean isEncrypt = false;

    @Override
    public byte[] encrypt(String data, byte[] key) {
        //暂时不进行加密,加速程序执行速度,干扰分析即可,也为了增加程序执行速度
        //自定义加密，这里直接返回原字符串
        return data.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String decrypt(byte[] data, byte[] key) {
        if (!isEncrypt) {
            return null;
        }
        //自定义解密
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public boolean shouldFog(String data) {
        // 控制指定字符串是否加密
        // 建议过滤掉不重要或者过长的字符串
        return true;
    }

}
