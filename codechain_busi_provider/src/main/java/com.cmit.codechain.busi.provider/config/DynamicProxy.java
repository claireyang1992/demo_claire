package com.cmit.codechain.busi.provider.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;

//被代理的类必须实现接口，未实现接口则没办法完成动态代理
public class DynamicProxy implements InvocationHandler {
    Object object;


    public Object getProxyObject(Object object){
        this.object=object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), //类加载器,一个ClassLoader对象，定义了由哪个ClassLoader对象来生成代理对象进行加载
                object.getClass().getInterfaces(),//获得被代理对象的所有接口,一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
                this);//InvocationHandler对象,一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上，间接通过invoke来执行
    }

    /**
     * 当用户调用对象中的每个方法时都通过下面的方法执行，方法必须在接口
     * proxy 被代理后的对象
     * method 将要被执行的方法信息（反射）
     * args 执行方法时需要的参数
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable{
        long start=System.currentTimeMillis();
        lazy();
        Object result=method.invoke(object,args);
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
