package bmphandlers.bmpkernelfilters;

import java.io.*;
import java.util.*;
import bmphandlers.*;

public class BMPKernelFilter extends ImageHandler{
  protected byte[] filebytes;
  protected float[][] kernel;
  protected PixelTable pixeltable;
  protected String kernelfilename;
  protected String outputfilename;
  private int height;
  private int width;


  public BMPKernelFilter(String imagename, String kernelfilename){
    super(imagename);
    this.kernelfilename = kernelfilename;
    this.outputfilename = "Kernel-" + imagename;
  }
  public void readFile() throws Exception{
    FileInputStream input = new FileInputStream(this.handledFileName);
		this.filebytes = new byte[input.available()];
		input.read(filebytes);
    this.width =  (filebytes[21]<<24) & 0xff000000|
                  (filebytes[20]<<16) & 0x00ff0000|
                  (filebytes[19]<<8)  & 0x0000ff00|
                  (filebytes[18]<<0)  & 0x000000ff;
    this.height = (filebytes[25]<<24) & 0xff000000|
                  (filebytes[24]<<16) & 0x00ff0000|
                  (filebytes[23]<<8)  & 0x0000ff00|
                  (filebytes[22]<<0)  & 0x000000ff;
    int bits =  (filebytes[29]<<8)  & 0x0000ff00|
                (filebytes[28]<<0)  & 0x000000ff;
		input.close();
		System.out.println("Imagen leida: " + this.handledFileName);
    System.out.println("Width: " + this.width);
    System.out.println("Height: " + this.height);
    System.out.println("Pixel bits: " + bits);

    createPixelTable(this.filebytes, this.width, this.height);
    readKernel();

  }

  public void generateFiles() throws Exception{
    
  }

  private void readKernel() throws Exception{
    Scanner scan = new Scanner(new File(this.kernelfilename));

    //reads the length of the kernel matrix
    int len = 0;
    String[] aux = scan.nextLine().trim().split(" ");
    for (int i = 0; i < aux.length; i++){
      len++;
      System.out.println(aux[i]);
    }
    scan.close();
    //prints kernel matrix to internal Array
    scan = new Scanner(this.kernelfilename);
    this.kernel = new float[len][len];
    for (int i = 0; i < len; i++){
      for (int j = 0; j < len; j++){
        this.kernel[i][j] = Float.valueOf(scan.nextInt());
      }
    }
    System.out.println(this.kernel);

  }

  private void createPixelTable(byte[] filebytes, int width, int height) throws Exception{
    int x = 0;
    int y = height - 1;
    pixeltable = new PixelTable(height, width);
    for (int i = 54; i < filebytes.length - 1; i = i + 3){
      if (i%3 == 0){
        int red = filebytes[i+2] & 0xff;
        int green = filebytes[i+1] & 0xff;
        int blue = filebytes[i] & 0xff;
        Pixel pixel = new Pixel(red,green,blue);
        pixeltable.addPixel(pixel,y,x);
        x++;
      }
      if ((x)%width == 0){
        x = 0;
        if (y!=0 && x == 0) { y = y-1; }
      }
    }
  }


}
