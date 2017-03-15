package ru.bmstu.plasma.analysis.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ru.bmstu.plasma.analysis.channel.PointOfElement;
import ru.bmstu.plasma.analysis.element.SpectrLine;

public class PlasmaDB {
	 
	// JDBC URL, имя пользователя и пароль
    private static final String url = "jdbc:mysql://localhost:3306/test";
    private static final String user = "root";
    private static final String password = "11111";
 
    // JDBC переменные
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
 
    public static boolean checkAccount(String checkUserName, String checkPassword) {
    	boolean res = false;
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String query = "SELECT COUNT(*) FROM my_users WHERE name = '" + checkUserName + "' AND password = '" + checkPassword + "'";
 
        try {
            // Открываем соединение с базой данных mysql
            con = DriverManager.getConnection(url, user, password);
 
            // Создаем экземпляр объекта для запросов
            stmt = con.createStatement();
 
            // Выполняем запрос
            rs = stmt.executeQuery(query);
 
            while (rs.next()) {
                int count = rs.getInt(1);
                if (count >= 1)
                	res = true;
            }
 
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            // Закрываем соединение
            try { con.close(); } catch(SQLException se) { }
            try { stmt.close(); } catch(SQLException se) { }
            try { rs.close(); } catch(SQLException se) { }
        }
        return res;
    }
    
    public static ArrayList<PointOfElement> getExperimentData(int experimentId, int channel) {
    	ArrayList<PointOfElement> data = new ArrayList<PointOfElement>();
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String query = "SELECT Wavelength, Counts, Intensity FROM channel" + channel + " ORDER BY Wavelength";
 
        try {
            // Открываем соединение с базой данных mysql
            con = DriverManager.getConnection(url, user, password);
 
            // Создаем экземпляр объекта для запросов
            stmt = con.createStatement();
 
            // Выполняем запрос
            rs = stmt.executeQuery(query);
                       
            while (rs.next()) 
            	data.add(new PointOfElement(rs.getDouble(1), rs.getDouble(2), rs.getDouble(3)));
 
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            // Закрываем соединение
            try { con.close(); } catch(SQLException se) { }
            try { stmt.close(); } catch(SQLException se) { }
            try { rs.close(); } catch(SQLException se) { }
        }
        
        return data;
    }
    
    public static ArrayList<SpectrLine> getElementData(String elementId) throws Exception {
    	ArrayList<SpectrLine> data = new ArrayList<SpectrLine>();
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String elementTable;
    	switch (elementId) {
		case "Ar-I":
			elementTable = "ar_i";
			break;
		case "Ar-II":
			elementTable = "ar_ii";
			break;
		case "H-I":
			elementTable = "h_i";
			break;
		case "Hg-I":
			elementTable = "hg_i";
			break;	
		default:
			throw new Exception("Неизвестный элемент");
		}
    	
    	String query = "SELECT Wavelength, Intensity FROM " + elementTable + " ORDER BY Wavelength";
 
        try {
            // Открываем соединение с базой данных mysql
            con = DriverManager.getConnection(url, user, password);
 
            // Создаем экземпляр объекта для запросов
            stmt = con.createStatement();
 
            // Выполняем запрос
            rs = stmt.executeQuery(query);
                       
            while (rs.next()) 
            	data.add(new SpectrLine(rs.getDouble(1), rs.getDouble(2)));
 
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            // Закрываем соединение
            try { con.close(); } catch(SQLException se) { }
            try { stmt.close(); } catch(SQLException se) { }
            try { rs.close(); } catch(SQLException se) { }
        }
        
        return data;
    }
}
