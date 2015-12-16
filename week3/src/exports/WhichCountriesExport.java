/**
 * Reads a chosen CSV file of country exports and prints each country that exports coffee.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;

public class WhichCountriesExport {
    public void listExporters(CSVParser parser, String exportOfInterest) {
        //for each row in the CSV File
        for(CSVRecord record: parser){
            //Look at the "Exports" column
            String exports = record.get("Exports");
            //Check if it contains exportOfInterest
            if(exports.contains(exportOfInterest)){
                //If so, write down the "Country" from that row
                String countryName = record.get("Country");
                System.out.println(countryName);
            }
        
        }
    }

    public void whoExportsCoffee() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        listExporters(parser, "coffee");
    }
    /*Write a method named tester that will create your CSVParser and call each of the methods below 
     *in parts 2, 3, 4, and 5. 
     */
    public void tester() {
        //read excel from disk
        FileResource countryExcel = new FileResource();
        //cerate parser from excel file
        CSVParser parser = countryExcel.getCSVParser();
        String result = countryInfo(parser, "Nauru");
        System.out.println(result);
        System.out.println("----------------------------- cotton and flowers");
        parser = countryExcel.getCSVParser();
        listExportersTwoProducts(parser, "cotton", "flowers");
        System.out.println("----------------------------- cocoa");
        parser = countryExcel.getCSVParser();
        System.out.println("Number of Exporters: "+numberOfExporters(parser, "cocoa"));
        System.out.println("-----------------------------");
        parser = countryExcel.getCSVParser();
        bigExporters(parser, "$999,999,999,999");
        
    }
    /*Write a method named countryInfo that has two parameters, parser is a CSVParser and country 
     * is a String. This method returns a string of information about the country or returns “NOT FOUND”
     * if there is no information about the country. 
     * The format of the string returned is 
     * the country, followed by “: “, followed by a list of the countries’ exports, 
     * followed by “: “, followed by the countries export value
     */
    private String countryInfo(CSVParser parser, String country){
        String result = "";
        for (CSVRecord record: parser){
            String countryName = record.get("Country");
            if(countryName.equals(country)){
              result = countryName+": "+record.get("Exports")+": "+record.get("Value (dollars)");
            }   
        }
        if (result.equals("")){
            result = "NOT FOUND";
        }
        return result;
    }
    /*Write a void method named listExportersTwoProducts that has three parameters, parser is a
     * CSVParser, exportItem1 is a String and exportItem2 is a String. 
     * This method prints the names of all the countries that have both exportItem1 and exportItem2 
     * as export items. For example, using the file exports_small.csv, this method called with the items
     * “gold” and “diamonds” would print the countries
     */ 
    private void listExportersTwoProducts(CSVParser parser, String exportItem1, String exportItem2){
        for (CSVRecord record: parser){
            String exports = record.get("Exports");
            if (exports.contains(exportItem1) && exports.contains(exportItem2)){
                System.out.println(record.get("Country"));
            } 
        }
    }
    
    /*4. Write a method named numberOfExporters, which has two parameters, parser is a CSVParser, and 
     * exportItem is a String. This method returns the number of countries that export exportItem. 
     * For example, using the file exports_small.csv, this method called with the item “gold” would 
     * return 3.
     */
    private int numberOfExporters(CSVParser parser, String exportItem){
        int numberOfExporters = 0;
        for(CSVRecord record: parser){
            String exports = record.get("Exports");
            if(exports.contains(exportItem)){
                numberOfExporters = numberOfExporters + 1;
            }
        }
        return numberOfExporters;
    }
    
    /*
     * 5. Write a void method named bigExporters that has two parameters, parser is a CSVParser, 
     * and amount is a String in the format of a dollar sign, followed by an integer number with a 
     * comma separator every three digits from the right. An example of such a string might 
     * be “$400,000,000”. This method prints the names of countries and their Value amount for all 
     * countries whose Value (dollars) string is longer than the amount string. You do not need to 
     * parse either string value as an integer, just compare the lengths of the strings. 
     * For example, if bigExporters is called with the file exports_small.csv and amount with 
     * the string $999,999,999, then this method would print eight countries and their export 
     * values shown here:
     */
    private void bigExporters(CSVParser parser, String amount){
        int amountLength = amount.length();
        for (CSVRecord record: parser){
            String exportAmount = record.get("Value (dollars)");
            int exportAmountLength = exportAmount.length();
            if( exportAmountLength > amountLength){
                System.out.println(record.get("Country")+" "+exportAmount);
            }
        }
    }
}
