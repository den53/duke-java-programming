
/**
 * Write a description of BatchInversions here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import java.io.File;

public class BatchInversions {
    public ImageResource makeInversion(ImageResource inImage){
        ImageResource outImage = new ImageResource(inImage.getWidth(), inImage.getHeight());
        for (Pixel pixel: outImage.pixels()){
            Pixel inPixel = inImage.getPixel(pixel.getX(),pixel.getY());
            pixel.setRed(invertColor(inPixel.getRed()));
            pixel.setGreen(invertColor(inPixel.getGreen()));
            pixel.setBlue(invertColor(inPixel.getBlue()));
        }
        return outImage;
    }
    
    private int invertColor(int color){
        return 250-color;
    }
    
    public void selectAndConvert(){
        DirectoryResource directory = new DirectoryResource();
        for(File file: directory.selectedFiles()){
            //transform file to Image
            ImageResource inImage = new ImageResource(file);
            ImageResource inverseImage = makeInversion(inImage);
            String newFileName = "inverted-" + file.getName();
            inverseImage.setFileName(newFileName);
            inverseImage.save();
        }
    }
}
