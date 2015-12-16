/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class BabyBirths {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        //StorageResource boyNames = new StorageResource();
        //StorageResource girlNames = new StorageResource();
        int totalNames = 0;
        int uniqueBoyNames = 0;
        int uniqueGirlNames = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            totalNames += 1;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                if(!checkForName(rec.get(0), "F",fr.getCSVParser(false))){
                    uniqueBoyNames += 1;
                }
            }
            else {
                totalGirls += numBorn;
                if(!checkForName(rec.get(0), "M",fr.getCSVParser(false))){
                    uniqueGirlNames += 1;
                }
            }
        }
        System.out.println("total births = "+totalBirths);
        System.out.println("female girls = "+totalGirls);
        System.out.println("male boys = "+totalBoys);
        System.out.println("total names = "+totalNames);
        System.out.println("unique boy names = "+uniqueBoyNames);
        System.out.println("unique girl names = "+uniqueGirlNames);
    }

    public void testTotalBirths () {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource("data/yob1900.csv");
        totalBirths(fr);
        System.out.println("======================");
        FileResource fr1 = new FileResource("data/yob1905.csv");
        totalBirths(fr1);
    }
    
    private boolean checkForName(String name, String gender, CSVParser parser){
        boolean result = false;
        for (CSVRecord rec: parser){
            
            if(rec.get(1).equals(gender)&&rec.get(0).equals(name)){
                result = true;
            }
        }
        return result;
    }
    
    private int getRank(int year, String name, String gender){
        //method returns the rank of the name in the file for the given gender
        String fileName = "data/yob"+year+".csv";
        FileResource fr = new FileResource(fileName);
        CSVParser parser = fr.getCSVParser(false);
        int tempRank = 0;
        int rank = 0;
        for (CSVRecord record: parser){
            if(record.get(1).equals(gender)){
                tempRank += 1;
                if(record.get(0).equals(name)){
                    rank = tempRank;
                    break;
                }
            }
        }
        if (rank==0){
            rank = -1;
        }
        return rank;
    }
    
    private int getRank(FileResource excelFile, String name, String gender){
        CSVParser parser = excelFile.getCSVParser(false);
        int tempRank = 0;
        int rank = 0;
        for (CSVRecord record: parser){
            if(record.get(1).equals(gender)){
                tempRank += 1;
                if(record.get(0).equals(name)){
                    rank = tempRank;
                    break;
                }
            }
        }
        if (rank==0){
            rank = -1;
        }
        return rank;
    }
    
    public void testGetRank(){
        //int result = getRank(2012,"Mason","M");
        int result = getRank(1960, "Emily", "F");
        System.out.println(result);
        int result2 = getRank(1971, "Frank", "M");
        System.out.println(result2);
    }
    
    private String getName(int year, int rank, String gender){
        String fileName = "data/yob"+year+".csv";
        FileResource fr = new FileResource(fileName);
        CSVParser parser = fr.getCSVParser(false);
        String name = "NO NAME";
        int currentRank = 0;
        for(CSVRecord record: parser){
            if(record.get(1).equals(gender)){
                currentRank += 1;
                if(currentRank == rank){
                    name = record.get(0);
                    break;
                }
            }
        }
        return name;
    }
    
    public void testGetName(){
        String result1 = getName(1980, 350, "F");
        System.out.println(result1);
        String result2 = getName(1982, 450, "M");
        System.out.println(result2);
    }
    
    private void whatIsNameInYear(String name, int year, int newYear, String gender){
        int rankYourName = getRank(year, name, gender);
        String newName = getName(newYear, rankYourName, gender);
        System.out.println(name+" born in "+year+" would be "+newName+" if she was born in "+newYear);
    }
    
    public void testWhatIsNameInYear(){
        whatIsNameInYear("Susan", 1972, 2014, "F");
        whatIsNameInYear("Owen", 1974, 2014, "M");
    }
    
    public int yearOfHighestRank(String name, String gender){
        int currentMaxRankYear = -1;
        int currentMaxRank = -1;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            String currentYearS = f.getName().substring(3,7);
            int currentYear = Integer.parseInt(currentYearS);
            int tempRank = 0;
            int rank = -1;
            for (CSVRecord record: parser){
                if(record.get(1).equals(gender)){
                    tempRank += 1;
                    if(record.get(0).equals(name)){
                        rank = tempRank;
                        break;
                    }
                }
            }
            if (currentMaxRank == -1 && rank != -1){
                currentMaxRank = rank;
                currentMaxRankYear = currentYear;
            } else if(rank<currentMaxRank && rank != -1){
                    currentMaxRank = rank;
                    currentMaxRankYear = currentYear;
            }    
        }
        return currentMaxRankYear;
    }
    
    public void testYearOfHighestRank(){
        System.out.println(yearOfHighestRank("Genevieve", "F"));
        System.out.println("---------------");
        System.out.println(yearOfHighestRank("Mich", "M"));
    }
    
    private double getAverageRank(String name, String gender){
        double sumOfRank = 0.0;
        double numberOfYears = 0.0;
        double averageRank = -1.0;
        DirectoryResource dr = new DirectoryResource();
        for(File f: dr.selectedFiles()){
            numberOfYears += 1.0;
            FileResource fr = new FileResource(f);
            int rank = getRank(fr, name, gender);
            if (rank != -1){
                sumOfRank += (double)rank;
            }            
        }
        if(sumOfRank != 0.0){
            averageRank = sumOfRank/numberOfYears;
        }
        return averageRank;
    }
    
    public void testGetAverageRank(){
        System.out.println(getAverageRank("Susan", "F"));
        System.out.println(getAverageRank("Robert", "M"));
    }
    
    private int getTotalBirthsRankedHigher(int year, String name, String gender){
        //total number of births of those names with the same gender and same year who 
        //are ranked higher than name
        //get FileName 
        String fileName = "data/yob"+year+".csv";
        FileResource excelFile = new FileResource(fileName);
        int rankOfName = getRank(excelFile, name, gender);
        int tempRank = 0;
        int totalBirths = 0;
        CSVParser parser = excelFile.getCSVParser(false);
        for(CSVRecord currentRow: parser){
            if(currentRow.get(1).equals(gender)){
                tempRank+=1;
                if(tempRank < rankOfName){
                    totalBirths += Integer.parseInt(currentRow.get(2));
                }
            }   
        }
        
        return totalBirths;
    }
    
    public void testGetTotalBirthsRankedHigher(){
        System.out.println(getTotalBirthsRankedHigher(1990,"Emily", "F"));
        System.out.println(getTotalBirthsRankedHigher(1990,"Drew", "M"));
    }
}
