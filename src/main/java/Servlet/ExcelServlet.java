package Servlet;

import until.PoiUntil;
import until.PoiUntils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author by
 * @version 1.0
 * @date 2020/1/19 15:15
 */
public class ExcelServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String address = request.getParameter("address");
        String point =request.getParameter("point");
        String dataName =request.getParameter("dataName");
        String username =request.getParameter("username");
        String password =request.getParameter("password");
        String url ="jdbc:mysql://"+address+":"+point+"/"+dataName+"?useUnicode=true&characterEncoding=utf8";
        String baseUrl ="jdbc:mysql://"+address+":"+point+"/mysql"+"?useUnicode=true&characterEncoding=utf8";
        String filepath="d:/";

        PoiUntil poi = new PoiUntil(url,username,password,dataName,baseUrl);
        try {
            List<String> list =poi.getAllDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PoiUntils pois =new PoiUntils();
        Boolean  IsSuccess = pois.creatExcel(poi.getDatabaseName(),filepath,poi);
        if (IsSuccess) {
            request.setAttribute("message", "success");
        }else {
            request.setAttribute("message", "false");
        }
        request.getRequestDispatcher("/message.jsp").forward(request,response);


    }
}
