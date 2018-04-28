import java.util.*;
import java.sql.*;
import java.io.*;

public class DBConvert3
{
  public static void main(String args[])
  {
    try
    {
       LoadProperties loadprop = new LoadProperties(100, 2000);
       loadprop.loadSP();
       loadprop.loadDP();
    }catch(Exception e)
    {
      System.out.println("Error : DBConvert3 - "+e);
    }
  }
}
