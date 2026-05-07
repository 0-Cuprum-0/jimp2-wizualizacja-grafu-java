import java.io.File;

public class BackEnd {
    /*
    * alg = 0 Triangulacja
    * alg = 1 Fruchterman-Reingold / Eades - domyślnie
    */
    int alg = 0;

    File inputFile;
    BackEnd(){


    }
    public void readArg(){}

    public void  launchC(){
        System.out.println("backend launched a command");
        //ProcessBuilder pb =
              //  new ProcessBuilder("./a.out ","-i", inputFile.getName(), "-t", "txt", "-a" );
    }
}
