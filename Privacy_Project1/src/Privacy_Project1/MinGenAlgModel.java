/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Privacy_Project1;

import java.io.*;
import java.util.*;
/**
 *
 * @author w057dxk
 * @author Dineshkasu
 */
public class MinGenAlgModel
{
     private String DataFile,DataFileString;
     private int ColumnCount,MinK,InputK ; float MaxPrec;
     private String Age,Gender,Worktype,Comma,QID_String,Dataline;
     private HashMap<String, Integer> QidHashMap = new HashMap<>();
     private HashMap<String,Float> PrecHashMap = new HashMap<>(); 
     private HashMap<String,Float> FinalHashMap = new HashMap<>(); 
     private int age,gender,worktype;
     private float age_float,gender_float,worktype_float,numerator,denominator,precision_val;
     
    
     public MinGenAlgModel()
     {
        DataFile = "./adults.txt";
        ColumnCount = 0;
        Age = null;
        Gender = null;
        Worktype = null;
        Comma = ",";
        QID_String =null;
        MinK =0;
        Dataline = null;
     }
 
     //It stores the User entered K value in the InputK.
     public void setInputK(int InputK)
     {
         this.InputK = InputK;
     }
     
     // It returns the Minimum Count (i.e K value) from the created HashMap.
     public int getMinK(HashMap<String,Integer> Hashmap)
     {
         MinK = Collections.min(Hashmap.values());
         return MinK;
     }
     
     
     //It returns the Maximum Precison (i.e Best Precision value) among the K-anonymous Tables.
      public float getMaxPrec(HashMap<String,Float> Hashmap)
     {
         MaxPrec = Collections.max(Hashmap.values());
         return MaxPrec;
     }
     
     //It checks whether the dataset is K-anonmous or not 
     public boolean isAnonmous(HashMap<String,Integer> Hashmap)
     {   
         if(this.getMinK(Hashmap) < this.InputK)
         {
             return false;
         }
         else
         {
             return true;
         }
     }
     
      //It process the given adults.txt file and separates the QID values.
      //It also stores the QID values in the Hashmap and Data file i.e QIDData.txt.
     public HashMap<String,Integer> ProcDataFile() throws FileNotFoundException, IOException
     {
         
             BufferedReader ReadDataFile = new BufferedReader(new FileReader(DataFile));
             PrintStream QIDDataFile = new PrintStream(new FileOutputStream("./QIDData.txt"));
             
             while((DataFileString = ReadDataFile.readLine())!= null )
             {
                 
                 StringTokenizer DataFileContent = new StringTokenizer(DataFileString, ",");
                                    ColumnCount = 0;
                      while(DataFileContent.hasMoreTokens())
                      {
                          String CurrentContent = DataFileContent.nextToken();
                                    ColumnCount = ColumnCount + 1;
                          
                          if(ColumnCount == 1)
                          {
                             Age = CurrentContent;  
                          }                        
                          if(ColumnCount == 2)
                          {
                             Worktype = CurrentContent; 
                          }
                          if(ColumnCount == 10)
                          {
                             Gender = CurrentContent;  
                          } 
                      }
                      
                    QID_String = Age + Comma + Worktype + Comma + Gender;   
                     if ( !QidHashMap.containsKey(QID_String))
                      {
                          QidHashMap.put(QID_String, 1);
                      }
                      else
                      {
                          Integer count = QidHashMap.get(QID_String);
                          count = count + 1;
                          QidHashMap.put(QID_String,count); 
                      }
               }
             
               for( String HashmapString : QidHashMap.keySet())
               {
                      QIDDataFile.println(HashmapString+" "+QidHashMap.get(HashmapString));
                      // Writing into file after the count
               }
         
               return QidHashMap;  
     }
     
     
     
     public void Generalization() throws FileNotFoundException
     {
            System.out.println("-----------------------------------------------");
            System.out.println("The below generation data tables are K-anonmous");
            System.out.println("-----------------------------------------------");
             for(gender=0; gender<=1; gender++)
              {
                     for(age =0; age <=6;age++)
                    {
                         for(worktype=0;worktype <=2;worktype++)
                         {
                                HashMap<String, Integer> GenHashMap = new HashMap<>();
                                
                                 for( String HashmapString : QidHashMap.keySet())
                                 {
                                    
                                        Integer Oldval = QidHashMap.get(HashmapString);
                                        StringTokenizer  HashmapKey = new StringTokenizer(HashmapString,",");
                                        ColumnCount = 0;
                                        while(HashmapKey.hasMoreTokens())
                                       {      
                                           ColumnCount = ColumnCount + 1;
                                           String Currenttext = HashmapKey.nextToken();
                                           
                                           if(ColumnCount == 1){ Age = GeneralizeAge(age,Currenttext);}                        
                                           if(ColumnCount == 2){Worktype = GeneralizeWorktype(worktype,Currenttext);}
                                           if(ColumnCount == 3){Gender = GeneralizeGender(gender,Currenttext);} 
                                       }
                                         
                                           String Gen_string = Age + Comma + Worktype + Comma + Gender;
                                           
                                           if ( !GenHashMap.containsKey(Gen_string))
                                            {
                                                 GenHashMap.put(Gen_string, Oldval);
                                            }
                                            else
                                            {
                                                 Integer count = GenHashMap.get(Gen_string);
                                                 count = count + Oldval;
                                                 GenHashMap.put(Gen_string,count); 
                                            }
                                 }
                                 
                                 
                                 if(this.isAnonmous(GenHashMap)) //Calculating the precision value for the K-anonmous Data tables. 
                                 {     
                                   age_float = age;
                                   gender_float=gender;
                                   worktype_float=worktype;
                                   numerator = ((gender_float/1) + (age_float/6) + (worktype_float/2));
                                   denominator = 3;
                                   precision_val = 1-(numerator/denominator);
                                   String PrecString = gender+","+age+","+worktype; 
                                   PrecHashMap.put(PrecString, precision_val);
                                   
                                    System.out.println("Gender="+gender+" Age="+age+" Worktype="+worktype+" Precsion="+precision_val);
                                 }      
                                 
                                 
                              /* PrintStream QIDData2 = new PrintStream(new FileOutputStream("./G"+gender+"A"+age+"WT"+worktype+".txt")); 
                               for( String HashmapString : GenHashMap.keySet())
                               {
                                     QIDData2.println(HashmapString+" "+GenHashMap.get(HashmapString));
                               }
                                      */
               
                         } 
                    }
              }   
     }
     
     //Find the Best precision value among the K-anonmous tables and find the generations having the same best precision value.
     public void FindBestPrec()
     {
         MaxPrec = this.getMaxPrec(PrecHashMap);
         System.out.println("-----------------------------------------------");
         System.out.println("The best precision value is = "+MaxPrec);
         System.out.println("-----------------------------------------------");
         System.out.println("The below generations have the same best precision");
          for( String PrecHashmapString : PrecHashMap.keySet())
          {         
               if ( MaxPrec == PrecHashMap.get(PrecHashmapString)){FinalHashMap.put(PrecHashmapString, MaxPrec);}
          }
     }
     
     //Write the adults.txt file data by updating it with generation having the best precision value.
     public void CreateDataFiles() throws FileNotFoundException, IOException
     {
         BufferedReader ReadDataFile = new BufferedReader(new FileReader("./adults.txt"));
         int count =0;
         
          for( String HashString : FinalHashMap.keySet())
          {         
               count = count + 1;
                 StringTokenizer  Hashstring = new StringTokenizer(HashString,",");
                 ColumnCount = 0;
                 while(Hashstring.hasMoreTokens())
                  {      
                      ColumnCount = ColumnCount + 1;
                      String Currenttext = Hashstring.nextToken();
                      if(ColumnCount == 1){ gender = Integer.parseInt(Currenttext);}
                      if(ColumnCount == 2){ age = Integer.parseInt(Currenttext);}
                      if(ColumnCount == 3){ worktype = Integer.parseInt(Currenttext);}                       
                  }
                 
               System.out.println(" Gender="+gender+" Age="+age+" Worktype="+worktype);
               
               System.out.println("Please check the file Output"+count+".txt for data");
              
               PrintStream QIDData2 = new PrintStream(new FileOutputStream("./Output"+count+".txt")); 
              
              while((DataFileString = ReadDataFile.readLine())!= null )
              {
                 
                 StringTokenizer DataFileContent = new StringTokenizer(DataFileString, ",");
                                    ColumnCount = 0;
                                    
                     while(DataFileContent.hasMoreTokens())
                      {
                          String CurrentContent = DataFileContent.nextToken();
                                    ColumnCount = ColumnCount + 1;
                          
                          if(ColumnCount == 1)
                          {
                             CurrentContent = GeneralizeAge(age,CurrentContent);
                          }                        
                          if(ColumnCount == 2)
                          {
                             CurrentContent = GeneralizeWorktype(worktype,CurrentContent);
                          }
                          if(ColumnCount == 10)
                          {
                             CurrentContent = GeneralizeGender(gender,CurrentContent); 
                          } 
                          
                          if(ColumnCount == 1)
                          {
                              Dataline = CurrentContent;
                          }
                          else
                          {
                              Dataline = Dataline + Comma + CurrentContent;
                          }
                      }
                     
                    QIDData2.println(Dataline);  
              }                      
  
          }
          
          System.out.println("-----------------------------------------------");
     }
     
     
  // It is the possible generations of the Age 0-6   
     public String GeneralizeAge(int agelevel,String agevalue)
     {
         int Agevalue = Integer.parseInt(agevalue);                         
          if(agelevel == 1)
          {
              String[] agerange = {"[0-5)","[5-10)","[10-15)","[15-20)","[20-25)","[25-30)","[30-35)","[35-40)","[40-45)","[45-50)","[50-55)","[55-60)","[60-65)","[65-70)","[70-75)","[75-80)","[80-85)","[85-90)","[90-95)"};
                                         
              int val = Agevalue/5;                               
              return agerange[val];        
          }
         if(agelevel == 2)
         {
             String[] agerange = {"[0-10)","[10-20)","[20-30)","[30-40)","[40-50)","[50-60)","[60-70)","[70-80)","[80-90)","[90-100)"};                         
             int val = Agevalue/10;
             return agerange[val];        
         }
        if(agelevel == 3)
        {
            String[] agerange = {"[0-15)","[15-30)","[30-45)","[45-60)","[60-75)","[75-90)","[90-105)"};                           
             int val = Agevalue/15;
             return agerange[val];        
         } 
        if(agelevel == 4)
        {
           String[] agerange = {"[0-20)","[20-40)","[40-60)","[60-80)","[80-100)"};                             
           int val = Agevalue/20;
           return agerange[val];        
         }
        if(agelevel == 5)
        {
           String[] agerange = {"[0-25)","[25-50)","[50-75)","[75-100)"};                               
           int val = Agevalue/25;
           return agerange[val];        
         }
        if(agelevel == 6)
        {
           String[] agerange = {"[0-30)","[30-60)","[60-90)","[90-120)"};
           int val = Agevalue/30;
           return agerange[val];        
         }
        
        return agevalue;
     }
     
     // It is the possible generations of the Worktype 0-2   
     public String GeneralizeWorktype(int worktype,String WTvalue)
     {
         
         if(worktype == 2)
         {
             WTvalue = "Work-class";
             return WTvalue;
         }
                                     
         if(worktype == 1)
         {
            if((" Self-emp-inc".equals(WTvalue))|| (" Self-emp-not-inc".equals(WTvalue)))
             {
                 WTvalue = " Self-employed";
                 return WTvalue;
             }
            if((" Federal-gov".equals(WTvalue))||(" State-gov".equals(WTvalue))||(" Local-gov".equals(WTvalue)))
             {
                 WTvalue = " Government";
                 return WTvalue;
             }
            if((" Without-pay".equals(WTvalue)))
             {
                  WTvalue = " Unemployed";
                  return WTvalue;
             }   
        }
         
         return WTvalue;
     }
     
     // It is the possible generations of the Gender 0-1  
     public String GeneralizeGender(int gender,String GenderValue)
     {
         
         if(gender ==1)
         {
             GenderValue = "*";
         }
         
         return GenderValue;
     }
       
}
