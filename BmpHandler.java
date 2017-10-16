/* CC2 - 2017 - PJ1 */
import bmphandlers.*;
import bmphandlers.bmpimagehandlercopy.*;
import bmphandlers.bmpimagehandlercolors.*;
import bmphandlers.bmphandlerrotators.*;
import bmphandlers.bmpkernelfilters.*;

public class BmpHandler {

	public static void runHandler(ImageHandler imgh) throws Exception {
		/** NO CAMBIE ESTE CODIGO **/
		System.out.println("--------------------------------------");
		System.out.println(imgh.getClass().getTypeName().toUpperCase() + ": ");
		System.out.println("\nLeyendo imagen : " + imgh.getFileName());
		imgh.readFile();
		System.out.println("Proceso de lectura de imagen terminado!");
		System.out.println("\nGenerando imagenes : ");
		imgh.generateFiles();
		System.out.println("Proceso de generacion de archivos terminado!");
		System.out.println("\n--------------------------------------");
		/** --------------------- **/
	}


	public static void main(String[] args) throws Exception {
		String runoption = args[0];
		String filename = args[1];
		String filename2 = "";
		if (args.length > 2) {
			filename2 = args[2];
		}
		switch(runoption) {
			case "-copy": {
				BmpHandlerCopy bhc = new BmpHandlerCopy(filename);
				runHandler(bhc);
				break;
			}
			case "-colors": {
				BmpImageHandlerColors bhc = new BmpImageHandlerColors(filename);
				runHandler(bhc);
				break;
			}
			case "-rotate": {
				BmpImageHandlerRotator bhc = new BmpImageHandlerRotator(filename);
				runHandler(bhc);
				break;
			}
			case "-grayscale": {
				BmpImageToGrayscale bhc = new BmpImageToGrayscale(filename);
				runHandler(bhc);
				break;
			}
			case "-kernel": {
				BMPKernelFilter bhc = new BMPKernelFilter(filename2, filename);
				runHandler(bhc);
				break;
			}
			default: {
				System.out.println("Opcion de ejecucion invalida");
			}
		}
	}
}
