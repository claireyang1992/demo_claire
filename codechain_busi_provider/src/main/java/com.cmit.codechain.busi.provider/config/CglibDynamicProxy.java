package com.cmit.codechain.busi.provider.config;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Random;

/*
    jdk和cglib动态代理实现的区别
        1、jdk动态代理生成的代理类和委托类实现了相同的接口；
        2、cglib动态代理中生成的字节码更加复杂，生成的代理类是委托类的子类，且不能处理被final关键字修饰的方法；
        3、jdk采用反射机制调用委托类的方法，cglib采用类似索引的方式直接调用委托类方法；
 */

//使用cglib可以实现动态代理，即使被代理的类没有实现接口，但被代理的类必须不是final类
public class CglibDynamicProxy implements MethodInterceptor {
    public Object targetObject;

    public Object getCglibProxyObject(Object object){
        this.targetObject=object;
        //增强器，动态代码生成器
        Enhancer enhancer=new Enhancer();
        //回调方法
        enhancer.setCallback(this);
        //设置生成类的父类类型
        enhancer.setSuperclass(targetObject.getClass());
        //动态生成字节码并返回代理对象
         return enhancer.create();

    }

    //Object为由CGLib动态生成的代理类实例，Method为上文中实体类所调用的被代理的方法引用，Object[]为参数值列表，MethodProxy为生成的代理类对方法的代理引用
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable{
        long start=System.currentTimeMillis();
        lazy();
        Object result=methodProxy.invoke(object,args);
        System.out.println(" 共用时："+ (System.currentTimeMillis()-start));
        return result;

    }

    public void lazy(){
        try{
            int n=new Random().nextInt(500);
            Thread.sleep(n);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
