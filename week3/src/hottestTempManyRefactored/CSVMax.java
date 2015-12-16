/**
 * Find the highest (hottest) temperature in any number of files of CSV weather data chosen by the user.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class CSVMax {
    public CSVRecord hottestHourInFile(CSVParser parser) {
        //start with largestSoFar as nothing
        CSVRecord largestSoFar = null;
        //For each row (currentRow) in the CSV File
        for (CSVRecord currentRow : parser) {
            // use method to compare two records
            largestSoFar = getLargestOfTwo(currentRow, largestSoFar);
        }
        //The largestSoFar is the answer
        return largestSoFar;
    }

    public void testHottestInDay () {
        FileResource fr = new FileResource("data/2015/weather-2015-01-01.csv");
        CSVRecord largest = hottestHourInFile(fr.getCSVParser());
        System.out.println("hottest temperature was " + largest.get("TemperatureF") +
                   " at " + largest.get("TimeEST"));
    }

    public CSVRecord hottestInManyDays() {
        CSVRecord largestSoFar = null;
        DirectoryResource dr = new DirectoryResource();
        // iterate over files
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            // use method to get largest in file.
            CSVRecord currentRow = hottestHourInFile(fr.getCSVParser());
            // use method to compare two records
            largestSoFar = getLargestOfTwo(currentRow, largestSoFar);
        }
        //The largestSoFar is the answer
        return largestSoFar;
    }

    public CSVRecord getLargestOfTwo (CSVRecord currentRow, CSVRecord largestSoFar) {
        //If largestSoFar is nothing
        if (largestSoFar == null) {
            largestSoFar = currentRow;
        }
        //Otherwise
        else {
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            double largestTemp = Double.parseDouble(largestSoFar.get("TemperatureF"));
            //Check if currentRow’s temperature > largestSoFar’s
            if (currentTemp > largestTemp) {
                //If so update largestSoFar to currentRow
                largestSoFar = currentRow;
            }
        }
        return largestSoFar;
    }
    
    private CSVRecord getSmallestOfTwo(CSVRecord currentRow, CSVRecord smallestSoFar, String columName, String nullString){
        //If largestSoFar is nothing
        if (smallestSoFar == null) {
            smallestSoFar = currentRow;
        } else {
            double currentData = Double.parseDouble(currentRow.get(columName));
            double smallestData = Double.parseDouble(smallestSoFar.get(columName));
            if(!currentRow.get(columName).equals(nullString)){
                //compare "TemperatureF" of currentRow with coldestSoFar transform it from String to Double
                if(currentData < smallestData){
                    //if currentRow < coldestSoFar set coldestSoFar = currentRow
                    smallestSoFar = currentRow;
                }
            }   
        }
        return smallestSoFar;
    }

    public void testHottestInManyDays () {
        CSVRecord largest = hottestInManyDays();
        System.out.println("hottest temperature was " + largest.get("TemperatureF") +
                   " at " + largest.get("DateUTC"));
    }
    
    private CSVRecord coldestHourInFile(CSVParser parser) {
        CSVRecord coldestSoFar = null;
        for (CSVRecord currentRow: parser){
            //Check if coldestSoFar is null
            if(coldestSoFar == null){
                //If yes then set coldestSoFar to currentRow
                coldestSoFar = currentRow;
            }    
            //find column with name "TemperatureF" 
            String temperature = currentRow.get("TemperatureF");
            if (!temperature.equals("-9999")){
                //compare "TemperatureF" of currentRow with coldestSoFar transform it from String to Double
                if(Double.parseDouble(temperature) < Double.parseDouble(coldestSoFar.get("TemperatureF"))){
                    //if currentRow < coldestSoFar set coldestSoFar = currentRow
                    coldestSoFar = currentRow;
                }
            }
        }
        return coldestSoFar;
    }
    
    public void testColdestHourInFile(){
    //make a FileResoure from the selectedFiles
    FileResource dataFile = new FileResource("nc_weather/2012/weather-2012-01-02.csv");
    //Parse the File to a Table
    CSVParser parser = dataFile.getCSVParser();
    //Call coldestHourInFile
    CSVRecord result = coldestHourInFile(parser);
    //Print the result
    System.out.println(result);
    }
    
    private String fileWithColdestTemperature(){
    // create an inital variable for the currentColdestTemp
    CSVRecord coldestSoFar = null;
    String coldestSoFarFileName = null;
    //Get a directory with he files 
    DirectoryResource dr = new DirectoryResource();
    //Make the selectedFiles iterable / FileResource and iterate over all files
    for(File file: dr.selectedFiles()){
        String fileName = file.getName();
        FileResource fr = new FileResource(file);
        CSVParser parser = fr.getCSVParser();        
        for (CSVRecord currentRow: parser){
            //Check if coldestSoFar is null
            if(coldestSoFar == null){
                //If yes then set coldestSoFar to currentRow
                coldestSoFar = currentRow;
                coldestSoFarFileName = fileName;
            }    
            //find column with name "TemperatureF" 
            String temperature = currentRow.get("TemperatureF");
            if (!temperature.equals("-9999")){
                //compare "TemperatureF" of currentRow with coldestSoFar transform it from String to Double
                if(Double.parseDouble(temperature) < Double.parseDouble(coldestSoFar.get("TemperatureF"))){
                    //if currentRow < coldestSoFar set coldestSoFar = currentRow
                    coldestSoFar = currentRow;
                    coldestSoFarFileName = fileName;
                }
            }
        }
    }  
    // at the end return currentColdestTempFileName
    return coldestSoFarFileName;
    }
    
    public void testFileWithColdestTemperature(){
        String coldestFileName = fileWithColdestTemperature();
        System.out.println("Coldest day was in file "+coldestFileName);
        String path = "nc_weather/2013/"+coldestFileName;
        FileResource coldestFile = new FileResource(path);
        CSVRecord coldestRecord = coldestHourInFile(coldestFile.getCSVParser());
        System.out.println("Coldest temperature on that day was "+coldestRecord.get("TemperatureF")); 
        System.out.println("All the Temperatures on the coldest day were:");
        for(CSVRecord currentRow: coldestFile.getCSVParser()){
            System.out.println(currentRow.get("DateUTC")+": "+currentRow.get("TemperatureF"));
        }
    }
    
    private CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord currentLowestSoFar = null;
        for(CSVRecord currentRow: parser){
            if(!currentRow.get("Humidity").equals("N/A")){
                currentLowestSoFar = getSmallestOfTwo(currentRow, currentLowestSoFar,"Humidity","N/A");
            }
        }
        return currentLowestSoFar;
    }
    
    public void testLowestHumidityInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord csv = lowestHumidityInFile(parser);
        System.out.println("Lowest Humidity was "+csv.get("Humidity")+" at "+csv.get("DateUTC"));
    }
    
    private CSVRecord lowestHumidityInManyFiles(){
        // create an inital variable for the currentColdestTemp
        CSVRecord lowestSoFar = null; 
        //Get a directory with he files 
        DirectoryResource dr = new DirectoryResource();
        //Make the selectedFiles iterable / FileResource and iterate over all files
        for(File file: dr.selectedFiles()){
            FileResource fr = new FileResource(file);
            CSVParser parser = fr.getCSVParser();
            lowestSoFar = lowestHumidityInFile(parser);
        }
        return lowestSoFar;
    }
    
    public void testLowestHumidityInManyFiles(){
    //Lowest Humidity was 24 at 2014-01-20 19:51:00
    CSVRecord result = lowestHumidityInManyFiles();
    System.out.println("Lowest Humidity was "+result.get("Humidity")+" at "+result.get("DateUTC"));
    }
    
    private double averageTemperatureInFile(CSVParser parser){
        double sum = 0;
        double entries = 0;
        //return average temperature in the file
        for(CSVRecord currentRow: parser){
            sum = sum + Double.parseDouble(currentRow.get("TemperatureF"));
            entries = entries + 1;
        }
        return sum/entries;
    }
    
    public void testAverageTemperatureInFile(){
    //get a file
    FileResource fr = new FileResource();
    //create a parser 
    CSVParser parser = fr.getCSVParser();
    //Call the method averageTemperatureInFile and print result
    System.out.println("Average temperature in file is "+averageTemperatureInFile(parser));
    }
    
    private double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
        //average temperature of only those temperatures when the humidity was greater than or equal to value.
        double sum = 0;
        double entries = 0;
        //return average temperature in the file
        for(CSVRecord currentRow: parser){
            if (Integer.parseInt(currentRow.get("Humidity")) >= value){
                sum = sum + Double.parseDouble(currentRow.get("TemperatureF"));
                entries = entries + 1;
            }
        }
        return sum/entries;
    }
    
    public void testAverageTemperatureWithHighHumidityInFile(){
        //get a file
        FileResource fr = new FileResource();
        //create a parser 
        CSVParser parser = fr.getCSVParser();
        double result = averageTemperatureWithHighHumidityInFile(parser,80);
        if(Double.isNaN(result)){
            System.out.println("No temperatures with that humidity");
        } else{
            System.out.println("Average Temp when high Humidity is " +result);
        }
    }
    
}
