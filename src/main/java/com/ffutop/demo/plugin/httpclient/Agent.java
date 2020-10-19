package com.ffutop.demo.plugin.httpclient;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author fangfeng
 * @since 2020/10/19
 */
public class Agent implements ClassFileTransformer {

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new Agent());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if ("org/apache/http/HttpHost".equals(className)) {
            ClassPool pool = ClassPool.getDefault();
            try {
                CtClass httpHost = pool.get("org.apache.http.HttpHost");
                CtClass string = pool.get("java.lang.String");
                CtConstructor constructor = httpHost.getDeclaredConstructor(new CtClass[]{string, CtClass.intType, string});
                constructor.insertBefore("hostname = \"www.163.com\";");
                byte[] bytes = httpHost.toBytecode();
                return bytes;
            } catch(NotFoundException | CannotCompileException | IOException e){
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }
}