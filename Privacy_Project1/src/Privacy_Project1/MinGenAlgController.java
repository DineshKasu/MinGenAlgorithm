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
public class MinGenAlgController

{
    public static void main(String[] args) throws IOException 
    {
        
        //This module is the main entry for the MinGenAlgorithm
        //It creates the instance of MinGenAlgorithm Model which process the file data,
        //do the generalization steps and calculates the best precision amoung K-anonymous files.
                       boolean isRunning , StartALGorithm = false;
                       int InputK;
                       BufferedReader UserInput = new BufferedReader(new InputStreamReader(System.in));
                       HashMap<String, Integer> QidHashMap = new HashMap<>();
                       
                       do
                       {
                          if(StartALGorithm)
                          {
                                MinGenAlgModel mingen = new MinGenAlgModel();
                                QidHashMap = mingen.ProcDataFile(); 
                                System.out.println("-----------------------------------------------");
                                System.out.println("Data is read from the adults.txt file");
                                System.out.println("QID Data File with the name QIDData.txt is created ");
                                System.out.println("-----------------------------------------------");
                                System.out.println("Please Enter the K Value");
                                InputK = Integer.parseInt(UserInput.readLine());
                                mingen.setInputK(InputK);
                                if(mingen.isAnonmous(QidHashMap)){ System.out.println("The given adults.txt file is K-anonmous");}
                                else{System.out.println("The given adults.txt file is not K-anonmous");} 
                                
                                //It creates all possible generations and apply them to the data file.
                                mingen.Generalization();
                                mingen.FindBestPrec();
                                mingen.CreateDataFiles();
                          }
                          
                          //It takes the input from the console as User enters
                           System.out.println("Do you want to check the K-anonmous of the adults.txt file?");
                           System.out.println("Please enter (Y/N)?");
                           Scanner scanner = new Scanner(System.in);
                           String input = scanner.next();
                           char Response = input.charAt(0); 
                           isRunning = (Response == 'y') || (Response == 'Y');
                           StartALGorithm = false;
                           if(isRunning){StartALGorithm = true;}
                       }
                       while(isRunning);
    }   
}
