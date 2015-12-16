
/**
 * Write a description of BatchGreyConverter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import java.io.File;

public class BatchGreyConverter {
    public ImageResource convertToGrey(ImageResource inImage){
        ImageResource outImage = new ImageResource(inImage.getWidth(),inImage.getHeight());
        for (Pixel pixel: outImage.pixels()){
            //System.out.println(pixel.getX());
            //System.out.println(pixel.getY());
            Pixel inPixel = inImage.getPixel(pixel.getX(),pixel.getY());
            int averageColorInPixel = (inPixel.getRed() + inPixel.getGreen() + inPixel.getRed())/3;
            pixel.setRed(averageColorInPixel);
            pixel.setGreen(averageColorInPixel);
            pixel.setBlue(averageColorInPixel);
            
        }
        return outImage;
    }
    
    public void convertFilesToGrey(){
        DirectoryResource dr = new DirectoryResource();
        for (File file: dr.selectedFiles()){
            //Convert File to Image Resource 
            ImageResource inImage = new ImageResource(file);
            System.out.println(inImage);
            //convert image to grey
            ImageResource greyImage = convertToGrey(inImage);
            //change file name to "grey-filename"
            String newFileName = "grey-"+ file.getName();
            //set new name to the greyImage
            greyImage.setFileName(newFileName);
            greyImage.save();
        }
    
    }

}
