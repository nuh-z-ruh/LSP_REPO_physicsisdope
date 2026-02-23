package org.howard.edu.lsp.assignment2;

import java.io.*;
import java.math.*;
import java.util.function.*;
import java.util.regex.*;

public class ETLPipeline {
    public static void main(String[] args) throws IOException{
        String fileInput = "products.csv";
        
        File products = new File("transformed_products.csv");


        Boolean isNull = false;
        String[][] table = new String[12][4];
        int r = 0;//row traverse var
        int c = 0;//col traverse var
        int lines = 0;
        int prodId = 0;
        String name = "";
        BigDecimal newPrice = new BigDecimal("0");
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        Pattern digits = Pattern.compile("\\d");
        Predicate<String> match = digits.asPredicate();
        String cat = "";


        if(!fileInput.isEmpty()){
            BufferedReader reader = new BufferedReader(new FileReader(fileInput));
            BufferedWriter writer = new BufferedWriter(new FileWriter(products));
            String line = reader.readLine();//reads header line so not in array

            while(!isNull){//put all lines in array
            line = reader.readLine();
            if(line == null){
                isNull = true;
                break;
            }
            lines++;
            if(line.length() > 0 && line.split(",").length == 4){
                table[r] = line.split(",");
                for(c = 0; c < table[r].length; c++){
                    table[r][c] = table[r][c].trim();
                };//trim all
            }
            else{
                r--;
            }
            r++;
        }//end while lines to read
        reader.close();

        for(r = 0; r < table.length; r++){ 
            if((table[r][0] == null || !match.test(table[r][0])) && r+1 < table.length){
                for(c = 0; c < table[0].length; c++){
                    table[r][c] = table[r+1][c];
                    for(int ctr = r+1; ctr+1 < table.length; ctr++){
                        table[ctr][c] = table[ctr+1][c];
                    }
                    table[table.length-1][c] = null;
               }
            }

            if(table[r][1] != null){
                table[r][1] = table[r][1].toUpperCase();
                name = table[r][1];
            }
            if(table[r][2] != null && table[r][2].contains(".")){
                    BigDecimal price = new BigDecimal(table[r][2]);
                    newPrice = price;
            }
            else if(r+1 < table.length){
                for(c = 0; c < table[0].length; c++){
                    table[r][c] = table[r+1][c];
                    table[r+1][c] = null;
               }
            }
            else if(r + 1 < table.length){
                for(c = 0; c < table[0].length; c++){
                    table[r][c] = table[r+1][c];
                    for(int ctr = r+1; ctr+1 < table.length; ctr++){
                        table[ctr][c] = table[ctr+1][c];
                    }
                    table[table.length-1][c] = null;
               }
            }

            if(table[r][3] != null){
                cat = table[r][3];
            }
            if(cat.equals("Electronics")){
                BigDecimal num = new BigDecimal("0.90");
                newPrice = newPrice.multiply(num, mc);
                table[r][2] = "" + newPrice;
                if(newPrice.floatValue() > 500){
                    table[r][3] = "Premium Electronics";
                }
            }//end electronics sale
        }//end for transform table


        int n = 0;
        r = 0;
        while(table[r][0] != null){
            n++;
            r++;
        }//end while

        String[][] augTable = new String[n][5];
        
        
        for(r = 0; r < augTable.length; r=r+1){
            for(c = 0; c < table[0].length; c++){
                if(table[r][c] != null){
                    augTable[r][c] = table[r][c];
                }
            }
            float salePrice = 0;
            if(augTable[r][2] != null && match.test(augTable[r][2])){
                salePrice = Float.parseFloat(augTable[r][2]);
                if(salePrice > 500){
                augTable[r][4] = "Premium";
                }
                else if(salePrice > 100){
                    augTable[r][4] = "High";
                }
                else if(salePrice > 10){
                    augTable[r][4] = "Medium";
                }
                else if(salePrice > 0){
                    augTable[r][4] = "low";
                }
                else{
                    augTable[r][4] = null;
                }
                }
                
            }//end for fill new table

            line = "";
            writer.write("ProductID,Name,Price,Category,PriceRange\n");
            for(r = 0; r < augTable.length; r++){
                for(c = 0; c < table[0].length; c++){
                    if(augTable[r][c] != null){
                        line += augTable[r][c] + ", ";
                    }
                }
                line += augTable[r][4];
                line = line.trim();
                writer.write(line);
                writer.newLine();
                line = "";
            }
        writer.close();
        System.out.println("Summary:\n" + lines + " lines read\n" + (augTable.length) + " rows transformed\n"
                            + (lines - augTable.length) + " rows skipped\n" + products.getAbsolutePath());
        }//end if file name
        else{
            System.out.println("No file selected.");
        }
        

    }//end main
}//end class
