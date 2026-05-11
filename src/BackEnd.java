import java.io.File;

public class BackEnd {

    String alg = "";

    File inputFile;
    BackEnd(){
        //this.alg = selectedAlg;



    }
    public void readArg(){}

    public void  launchC(String selectedAlg){
        System.out.println("backend launched a command");
        System.out.println("Wybrany alg:" + selectedAlg);
        //ProcessBuilder pb =
              //  new ProcessBuilder("./a.out ","-i", inputFile.getName(), "-t", "txt", "-a" );
    }
}
