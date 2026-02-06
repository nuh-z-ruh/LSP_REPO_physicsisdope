package org.howard.edu.lsp.assignment2;

import java.io;

public class ETLPipeline(){
    public static void main(String args[]){
            BufferedReader reader = new BufferedReader(new FileReader("products.csv"));
            String line = reader.readLine();

            while((line = reader.readLine()) != null){
                line = reader.readLine();
                line.toUpperCase();

            }//end while lines to read
    }//end main
}//end class
    
