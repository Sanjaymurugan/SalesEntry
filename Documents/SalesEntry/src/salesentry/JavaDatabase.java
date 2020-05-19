package salesentry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author sanjay murugan
 */
public class JavaDatabase {
    
    Connection conn;
    Statement stmt;
    public static String date;
    
    public JavaDatabase(){
        try{
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/sales","root","sanjay");
            stmt=conn.createStatement();
        }
        catch(Exception e){
            System.err.println("----->"+e);
        }
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now=LocalDateTime.now();
        date=dtf.format(now)+"";
    }
    
    void insertData(dbPojo pojo){
        try{
            stmt.executeUpdate("insert into sales values(null,'"+pojo.date+"','"+pojo.item+"','"+pojo.amount+"')");
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    
    String[] getItems(){
        ArrayList<String> items=new ArrayList<>();
        try{
            ResultSet rs=stmt.executeQuery("select * from items");
            while(rs.next()){
                items.add(rs.getString(1));
            }
        } catch(Exception e){
            System.err.println(e);
        }
        String[] itemsArray = items.toArray(new String[0]);
        return itemsArray;
    }
    
    String[] getListItem(){
        ArrayList<String> arrayList=new ArrayList<>();
        try{
            ResultSet rs=stmt.executeQuery("select * from sales where date = '"+date+"'");
            while(rs.next()){
                arrayList.add(rs.getString(2)+" : "+rs.getString(3)+" : "+rs.getInt(4));
            }
        } catch(Exception e){
            System.err.println(e);
        }
        String[] listArray = arrayList.toArray(new String[0]);
        return listArray;
    }
    
    int getTodayTotal(){
        int total=0;
        try{
            ResultSet rs=stmt.executeQuery("select * from sales where date = '"+date+"'");
            while(rs.next()){
                total=total+rs.getInt(4);
            }
        } catch(Exception e){
            System.err.println(e);
        }
        return total;
    }
    
    ArrayList<dbPojo> getSales(){
        ArrayList<dbPojo> arrayList=new ArrayList<dbPojo>();
        try{
            ResultSet rs=stmt.executeQuery("select * from sales where date = '"+date+"'");
            while(rs.next()){
                dbPojo pojo=new dbPojo();
                pojo.setSno(rs.getInt(1));
                pojo.setDate(rs.getString(2));
                pojo.setItem(rs.getString(3));
                pojo.setAmount(rs.getInt(4));
                arrayList.add(pojo);
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
        return arrayList;
    }
    
    void update(dbPojo pojo){
        try{
            stmt.executeUpdate("update sales set date='"+pojo.getDate()+"', item='"+pojo.getItem()+"', amount="+pojo.getAmount()+" where sno="+pojo.getSno()+"&& date='"+pojo.getEditDate()+"'");
        }
        catch(Exception e){
            System.err.println("---------->"+e);
        }
    }
    
    String[] getSalesInPeriod(String from, String to){
        ArrayList<String> arrayList=new ArrayList<>();
        try{
            Date fromDate=new SimpleDateFormat("dd/MM/yyyy").parse(from);
            Date toDate=new SimpleDateFormat("dd/MM/yyyy").parse(to);
            ResultSet rs=stmt.executeQuery("select * from sales");
            while(rs.next()){
                String salesDates=rs.getString(2);
                Date salesDate=new SimpleDateFormat("dd/MM/yyyy").parse(salesDates);
                if(salesDate.after(fromDate)&&salesDate.before(toDate))
                    arrayList.add(rs.getString(2)+" : "+rs.getString(3)+" : "+rs.getInt(4));
            }
        }
        catch(Exception e){
            System.err.println("--------->"+e);
        }
        String[] listArray=arrayList.toArray(new String[0]);
        return listArray;
    }
    
    int getTotalSales(String from,String to){
        int total=0;
        try{
            Date fromDate=new SimpleDateFormat("dd/MM/yyyy").parse(from);
            Date toDate=new SimpleDateFormat("dd/MM/yyyy").parse(to);
            ResultSet rs=stmt.executeQuery("select * from sales");
            while(rs.next()){
                String salesDates=rs.getString(2);
                Date salesDate=new SimpleDateFormat("dd/MM/yyyy").parse(salesDates);
                if(salesDate.after(fromDate)&&salesDate.before(toDate))
                    total+=rs.getInt(4);
            }
        }
        catch(Exception e){
            System.err.println("--------->"+e);
        }
        return total;
    }
    
}
