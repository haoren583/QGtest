package model;

import DataBaseConnectPool.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class NameOfSchool {
    private static ArrayList<String> CollegeName;
    private static ArrayList<String> majorName;
    static {
        Dao dao=new Dao();
        CollegeName = new ArrayList<String>(Collections.nCopies(50, ""));
        majorName = new ArrayList<String>(Collections.nCopies(1000, ""));

        //从数据库中读取数据并填充到数组中
        ResultSet re=null;
        //读取学院信息
        try{
            re= dao.executeQuery("SELECT * FROM colleges where is_del=0");
            //读取学院名称
            while(re.next()){
                CollegeName.set(re.getInt("id"), re.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //读取专业信息
        try{
            re= dao.executeQuery("SELECT * FROM majors where is_del=0");
            //读取专业名称
            while(re.next()){
                majorName.set(re.getInt("id"), re.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCollegeName(int index) {
        return CollegeName.get(index);
    }

    public static String getMajorName(int index) {
        return majorName.get(index);
    }
}
