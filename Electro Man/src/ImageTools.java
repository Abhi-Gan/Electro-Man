import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;

public class ImageTools {

    public static int HORIZONTAL_FLIP = 1, VERTICAL_FLIP = 2, DOUBLE_FLIP = 3;

    /**
     * Loads an image.
     *
     * @param fileName The name of a file with an image
     * @return Returns the loaded image. null is returned if the image cannot be loaded.
     */
    public static BufferedImage load(String fileName)
    {
        try
        {
            return ImageIO.read((new File(fileName)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Copies and returns an image.
     *
     * @param img Receives a buffered image
     * @return Returns a copy of the received image. null is returned if the received image is null.
     */
    public static BufferedImage copy(BufferedImage img) {
        if(img == null)
        {
            return null;
        }
        else
        {
            BufferedImage copiedImage=new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
            copiedImage.getGraphics().drawImage(img, 0,0, null);
            return copiedImage;
        }
    }

    public static BufferedImage recolor(BufferedImage img, Color endColor)
    {
        if(img == null)
        {
            return null;
        }
        BufferedImage copy=copy(img);

        for (int r=0;r<img.getHeight();r++)
        {
            for (int c=0;c<img.getWidth();c++)
            {
                int colorNumb=img.getRGB(c,r);
                Color color=new Color(colorNumb, true);
                int red=color.getRed();
                int green=color.getGreen();
                int blue=color.getBlue();
                //target grey has 156 for all
                if(red ==156 &&
                        green == 156 &&
                        blue ==156)
                {
                    copy.setRGB(c,r,endColor.getRGB());
                }
            }
        }
        return copy;
    }

    /**
     * Returns a new image with transparency enabled.
     *
     * @param img Receives a buffered image
     * @return returns a copy of the received image with a color mode that allows transparency.
     * null is returned if the received image is null.
     */
    public static BufferedImage copyWithTransparency(BufferedImage img)
    {
        if(img == null)
        {
            return null;
        }
        else
        {
            BufferedImage copiedImage=new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            copiedImage.getGraphics().drawImage(img, 0,0, null);
            return copiedImage;
        }
    }

    /**
     * Checks if the provided image has transparency.
     *
     * @param img Receives a buffered image
     * @return returns true if the image has a transparent mode and false otherwise.
     */
    public static boolean hasTransparency(BufferedImage img)
    {
        return (img.getTransparency()==BufferedImage.TYPE_4BYTE_ABGR);
    }

    /**
     * Scales an image.
     *
     * @param img Receives a buffered image and two positive double scale values (percentages)
     * @param horizontalScale Value to scale horizontal by.
     * @param verticalScale Value to scale vertical by.
     * @return creates and returns a scaled copy of the received image,
     * null is returned if the received image is null or if non-positive scales are provided
     */
    public static BufferedImage scale(BufferedImage img, double horizontalScale,
                                      double verticalScale)
    {
        /*System.out.println("Img width original: "+img.getWidth());
        System.out.println("Img height original: "+img.getHeight());
        System.out.println("Horizontal scale: "+horizontalScale);
        System.out.println("Vert scale: "+verticalScale);

        System.out.println("Img width: "+(int) (horizontalScale*img.getWidth()));
        System.out.println("Img height: "+(int) (verticalScale*img.getHeight()));*/
        if(img == null || (int) (horizontalScale*img.getWidth())<=0 || (int) (verticalScale*img.getHeight()) <=0)
        {
            System.out.println("null");
            return null;
        }
        else
        {
            BufferedImage copiedImage
                    = new BufferedImage((int) (horizontalScale*img.getWidth()), (int) (verticalScale*img.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);
            //10 parameters
            copiedImage.getGraphics().drawImage(img,
                    0,0,
                    (int) (horizontalScale*img.getWidth()), (int) (verticalScale*img.getHeight()),
                    0,0,
                    img.getWidth(), img.getHeight(),
                    null);
            return copiedImage;
        }
    }

    /**
     * Scales an image.
     *
     * @param img Receives a buffered image
     * @param newWidth New width to scale to.
     * @param newHeight New height to scale to.
     * @return creates and returns a scaled copy of the received image,
     * null is returned if the received image is null or if non-positive dimensions are provided
     */
    public static BufferedImage scale(BufferedImage img, int newWidth,
                                      int newHeight) {
        if (img == null || newWidth ==0 || newHeight == 0)
        {
            return null;
        }
        else
        {
            BufferedImage copiedImage
                    = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR);
            //10 parameters
            copiedImage.getGraphics().drawImage(img,
                    0,0,
                    newWidth, newHeight,
                    0,0,
                    img.getWidth(), img.getHeight(),
                    null);
            return copiedImage;
        }
    }

    /**
     * Rotates an image.
     *
     * @param img Receives a buffered image
     * @param angle The angle to rotate to.
     * @return The rotated image. null is returned if the received image is null.
     */
    public static BufferedImage rotate(BufferedImage img, double angle)
    {
        if(img == null)
        {
            return null;
        }
        else
        {
            /**For some reason angles > 90 deg dont work*/
            //for this lab, workaround is  use + angle and then horiz flip
            boolean horizFlip = false;
            angle = angle%360;
            if(angle < 0)
            {
                angle = -1 * angle;
                //System.out.println("angle"+angle);
                horizFlip = true;
            }
            else if(angle > 270)
            {
                angle = angle - 360;
                //System.out.println("angle"+angle);
                horizFlip = true;
            }
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.setToTranslation(0,0);
            affineTransform.rotate(Math.toRadians(angle), img.getWidth()/2, img.getHeight()/2);

            // Stores the transparency of the original image
            int transparency = img.getColorModel().getTransparency();

            //new code
            double radians = Math.toRadians(angle);
            int newWidth = (int) (Math.abs(Math.cos(radians)*img.getWidth()) + Math.abs(Math.sin(radians)*img.getHeight()));
            int newHeight = (int) (Math.abs(Math.sin(radians)*img.getWidth()) + Math.abs(Math.cos(radians)*img.getHeight()));
            /*System.out.println("cos: "+ Math.cos(radians));
            System.out.println("sin: "+ Math.sin(radians));
            System.out.println("width: "+ newWidth);
            System.out.println("height: "+newHeight);*/
            // Creates an image to store the rotated version of the original image
            BufferedImage rotated =
                    new BufferedImage( newWidth, newHeight, transparency);
            double xFix=-(newWidth/2 - img.getWidth()/2);
            double yFix=(newHeight/2 - img.getHeight()/2);
            /*System.out.println("xTranslate: "+xFix);
            System.out.println("yTranslate: "+yFix);*/
            affineTransform.translate( xFix, yFix);
            //new code done
            /*BufferedImage rotated =
                    new BufferedImage( img.getWidth(), img.getHeight(), transparency);*/
            // gets the graphics of the destination image.
            Graphics2D  g = (Graphics2D)  (rotated.getGraphics());
            // draws the original image onto the destination image, with the correct rotation
            g.drawImage(img, affineTransform,null);
            if(horizFlip)
            {
                rotated=flip(rotated, VERTICAL_FLIP);
            }
            return rotated;
        }
    }

    /**
     * Flips an image.
     *
     * @param img Receives a buffered image
     * @param type Type of flip (int)
     * @return Creates and returns a flipped copy of the received image.
     * null is returned if the received image is null or if an invalid flipping value is provided
     */
    public static BufferedImage flip(BufferedImage img, int type)
    {
        if(img==null)
        {
            return null;
        }
        //HORIZONTAL_FLIP = 1, VERTICAL_FLIP = 2, DOUBLE_FLIP = 3;
        BufferedImage flipped = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
        if(type == HORIZONTAL_FLIP)
        {
            flipped.getGraphics().drawImage(
                    img,
                    img.getWidth(),0,
                    0, img.getHeight(),
                    0,0,
                    img.getWidth(), img.getHeight(),
                    null
            );
        }
        else if(type == VERTICAL_FLIP)
        {
            flipped.getGraphics().drawImage(
                    img,
                    0,img.getHeight(),
                    img.getWidth(), 0,
                    0,0,
                    img.getWidth(), img.getHeight(),
                    null
            );
        }
        else if(type == DOUBLE_FLIP)
        {
            flipped.getGraphics().drawImage(
                    img,
                    img.getWidth(), img.getHeight(),
                    0,0,
                    0,0,
                    img.getWidth(), img.getHeight(),
                    null
            );
        }
        else
        {
            return null;
        }

        return flipped;
    }

    /**
     * Blurs an image.
     *
     * @param img Receives a buffered image
     * @return creates and returns a blurred copy of the received image,
     * the blurring is created by blending each cell with its 8 neighbors.
     * Null is returned if the received image is null.
     */
    public static BufferedImage blur(BufferedImage img)
    {
        if(img == null)
        {
            return null;
        }

        BufferedImage blurredImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());

        for(int x=0;x<blurredImage.getWidth();x++)
        {
            for (int y=0;y<blurredImage.getHeight();y++)
            {
                int colorNumb=img.getRGB(x,y);
                Color c=new Color(colorNumb, true);

                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;
                int num = 0;

                for(int surrX=x-1;
                    surrX>=0 && surrX<img.getWidth() && surrX<=x+1;
                    surrX++)
                {
                    for(int surrY=y-1;
                        surrY>=0 && surrY<img.getHeight() && surrY<=y+1;
                        surrY++)
                    {
                        //if not transparent, count it
                        if(img.getRGB(surrX, surrY) != 0)
                        {
                            Color color=new Color(img.getRGB(surrX, surrY), true);
                            sumRed += color.getRed();
                            sumGreen += color.getGreen();
                            sumBlue += color.getBlue();
                            num++;
                        }
                    }
                }

                if(num > 0)
                {
                    //System.out.println("num: "+num);
                    int red=(int) (sumRed/num);
                    int green=(int) (sumGreen/num);
                    int blue=(int) (sumBlue/num);
                    Color color=new Color(red,green,blue, c.getAlpha());
                    blurredImage.setRGB(x, y , color.getRGB());
                }
            }
        }

        return blurredImage;
    }

    /**
     * Inverts an image's colors.
     *
     * @param img Receives a buffered image
     * @return Image with inverted colors. null is returned if the received image is null.
     */
    public static BufferedImage invertColor(BufferedImage img)
    {
        if(img == null)
        {
            return null;
        }
        BufferedImage invert=copy(img);

        for (int r=0;r<img.getHeight();r++)
        {
            for (int c=0;c<img.getWidth();c++)
            {
                int colorNumb=img.getRGB(c,r);
                Color color=new Color(colorNumb, true);
                int red=255-color.getRed();
                int green=255-color.getGreen();
                int blue=255-color.getBlue();
                Color newColor=new Color(red,green,blue, color.getAlpha());
                invert.setRGB(c,r,newColor.getRGB());
            }
        }
        return invert;
    }

    /**
     * Removes a certain percentage of an image's pixels.
     *
     * @param img Receives a buffered image
     * @param percentToRemove Percent to remove of the image.
     * @return creates and returns a copy of the received image with the given
     * percentage in decimal form of the images remaining non-fully transparent
     * pixels changed to be completely transparent. null is returned if the
     * received image is null or if non-positive percentage is provided.
     */
    public static BufferedImage removePixels(BufferedImage img, double percentToRemove)
    {
        if(img == null
                || percentToRemove<=0)
        {
            return null;
        }
        int visiblePixs=visiblePixels(img);

        //System.out.print("Total pixels: "+img.getWidth()*img);

        int pixToRemove = (int) (percentToRemove*visiblePixs);
        //System.out.println("pixToRemove: "+pixToRemove);
        return removePixels(img, pixToRemove);
    }

    /**
     * Removes a certain amount of pixels from an image.
     *
     * @param img Receives a buffered image
     * @param numToRemove number of pixels to remove
     * @return creates and returns a copy of the received image with the given
     * number of the images remaining pixels removed.
     * Non-fully transparent pixels are changed to be completely transparent.
     * null is returned if the received image is null or if non-positive number
     * is provided. Note: is there are not enough pixels in the image it will
     * remove as many as it can.
     */
    public static BufferedImage removePixels(BufferedImage img, int numToRemove)
    {
        if(img == null
                || numToRemove<=0)
        {
            return null;
        }
        BufferedImage removedPixels=copy(img);
        int visiblePixs=visiblePixels(img);

        //if remove all pixels,
        if(numToRemove >= visiblePixs)
        {
            for(int x=0;x<img.getWidth();x++)
            {
                for(int y=0;y<img.getHeight();y++)
                {
                    int colorNumb=removedPixels.getRGB(x, y);
                    Color color=new Color(colorNumb, true);
                    Color newColor=new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
                    removedPixels.setRGB(x, y, newColor.getRGB());
                }
            }

            return removedPixels;
        }

        while(numToRemove >=0 &&
                visiblePixs >=0)
        {
            //find random place
            int r= (int) (Math.random()*img.getHeight());
            int c= (int) (Math.random()*img.getWidth());
            int colorNumb=removedPixels.getRGB(c,r);
            Color color=new Color(colorNumb, true);
            if(color.getAlpha() != 0)
            {
                //System.out.println("VisiblePix: "+visiblePixs);
                //System.out.println("numtoremove: "+numToRemove);
                Color newColor=new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);

                removedPixels.setRGB(c, r, newColor.getRGB());
                numToRemove--;
                visiblePixs--;

            }
        }
        return removedPixels;
    }

    /**
     * Fades an image.
     *
     * @param img Receives a buffered image
     * @param fade Double percentage to fade
     * @return Creates and returns a copy of the received image that has been
     * faded the given percentage. Fading is done by multiply the alpha value by (1-fade)
     * Null is returned if the received image is null or if non-positive fade value is provided
     * Note: any fade greater than 1 will be reduced to 1
     */
    public static BufferedImage fade(BufferedImage img, double fade)
    {
        if(img == null
                || fade<=0)
        {
            return null;
        }
        if(fade>1)
        {
            fade=1;
        }

        BufferedImage faded = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());

        for (int r=0;r<img.getHeight();r++)
        {
            for (int c=0;c<img.getWidth();c++)
            {
                int colorNumb=img.getRGB(c,r);
                Color color=new Color(colorNumb, true);
                int red=color.getRed();
                int green=color.getGreen();
                int blue=color.getBlue();
                Color newColor=new Color(red,green,blue, (int) (color.getAlpha()*(1-fade)));
                faded.setRGB(c,r,newColor.getRGB());
            }
        }
        return faded;
    }

    /**
     * Lightens an image.
     *
     * @param img Receives a buffered image
     * @param lightenFactor double percentage to lighten
     * @return creates and returns a copy of the received image that has been
     * lightened by the given percentage + 1.
     * Null is returned if the received image is null or if non-positive lighten.
     * Factor value is provided.
     * Note: any colors that end up being larger than 255 will be changed to 255.
     */
    public static BufferedImage lighten(BufferedImage img, double lightenFactor)
    {
        if(img == null
                || lightenFactor<=0)
        {
            return null;
        }

        BufferedImage lightened = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
        for (int r=0;r<img.getHeight();r++)
        {
            for (int c=0;c<img.getWidth();c++)
            {
                int colorNumb=img.getRGB(c,r);
                Color color=new Color(colorNumb, true);
                int red=(int) (color.getRed()*(1+lightenFactor));
                int green=(int) (color.getGreen()*(1+lightenFactor));
                int blue=(int) (color.getBlue()*(1+lightenFactor));
                if(red>255)
                {
                    red=255;
                }
                if(green>255)
                {
                    green=255;
                }
                if(blue>255)
                {
                    blue=255;
                }
                Color newColor=new Color(red,green,blue, color.getAlpha());
                lightened.setRGB(c,r,newColor.getRGB());
            }
        }
        return lightened;
    }

    /**
     * Darkens an image.
     *
     * @param img Receives a buffered image
     * @param darkenFactor double percentage to darken
     * @return creates and returns a copy of the received image that has been
     * darkened by 1 minus the given percentage.
     * null is returned if the received image is null or if non-positive.
     * Note: any colors that end up being larger than 255 will be
     * changed to 255.
     */
    public static BufferedImage darken(BufferedImage img, double darkenFactor)
    {
        if(img == null
                || darkenFactor<=0
                || darkenFactor>1)
        {
            return null;
        }

        BufferedImage darkened = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
        for (int r=0;r<img.getHeight();r++)
        {
            for (int c=0;c<img.getWidth();c++)
            {
                int colorNumb=img.getRGB(c,r);
                Color color=new Color(colorNumb, true);
                int red=(int) (color.getRed()*(1-darkenFactor));
                int green=(int) (color.getGreen()*(1-darkenFactor));
                int blue=(int) (color.getBlue()*(1-darkenFactor));
                if(red>255)
                {
                    red=255;
                }
                if(green>255)
                {
                    green=255;
                }
                if(blue>255)
                {
                    blue=255;
                }
                Color newColor=new Color(red,green,blue, color.getAlpha());
                darkened.setRGB(c,r,newColor.getRGB());
            }
        }
        return darkened;
    }

    private static int visiblePixels(BufferedImage img)
    {
        int visiblePixs=0;
        for (int r=0;r<img.getHeight();r++)
        {
            for (int c=0;c<img.getWidth();c++)
            {
                int colorNumb=img.getRGB(c,r);
                Color color=new Color(colorNumb, true);
                if(color.getAlpha() != 0)
                {
                    visiblePixs++;
                }
            }
        }

        return visiblePixs;
    }

    private static int totalPixels(BufferedImage img)
    {
        return img.getWidth()*img.getHeight();
    }
}