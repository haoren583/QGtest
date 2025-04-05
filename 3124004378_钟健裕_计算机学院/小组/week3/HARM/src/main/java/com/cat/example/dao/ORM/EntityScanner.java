package com.cat.example.dao.ORM;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 扫描实体类，用于扫描出带有@Entity注解的类
 * @author 钟健裕
 */
public class EntityScanner {
    // 包名，用于扫描指定包下的带有@DataEntity注解的类
    private final String packageName;

    // 构造方法
    public EntityScanner(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 扫描实体类方法
     */
    public List<Class<?>> scan() {
        //存储所有实体类
        List<Class<?>> entityList=new ArrayList<>();
        //将包名转换为路径
        String packageDirName = packageName.replace('.', '/');
        //获取包下所有类
        //获取当前线程的上下文类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //查找 path 对应的资源，并返回该资源的 URL
        //URL 可以用来进一步读取该包下的类文件或其他资源文件
        URL url = classLoader.getResource(packageDirName);
        //如果 url 为 null，说明该包路径不存在，返回空列表
        if (url == null) {
            throw new IllegalArgumentException("包路径不存在：" + packageName);
        }
        //获取该路径下的所有文件的目录
        File directory = new File(url.getFile());
        //遍历该目录下的所有文件
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            //如果是类文件，则判断是否带有@Entity注解
            if ( file.isFile() && file.getName().endsWith(".class") ){
                //获取类名
                String className = file.getName().replace(".class", "");
                //将类名转换为类路径
                String classPath = packageName + "." + className;
                //加载类
                try {
                    Class<?> clazz = Class.forName(classPath);
                    //判断是否带有@Entity注解
                    if (clazz.isAnnotationPresent(Entity.class)) {
                        entityList.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return entityList;
    }
}