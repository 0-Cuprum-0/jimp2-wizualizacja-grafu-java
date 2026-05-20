import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class BackEnd {

    GraphVisualizerUI ui;

    String selectedAlgorithm = "Fruchterman-Reingold";

    private File inputFile;
    private File outputFile;
    
    List<Edge> edges;
    List<Vertex> vertices;
    BackEnd(){
        this.edges = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.inputFile = null;
        String outputFilePath = System.getProperty("user.dir") + File.separator + "src_c" + File.separator + "output.txt";
        this.outputFile = new File(outputFilePath);
    }

    public void setUI(GraphVisualizerUI ui) {
        this.ui = ui;
    }

    public void  launchC(){
        String alg = "";

        if("Fruchterman-Reingold".equals(selectedAlgorithm)){
            alg = "FRE";
        }
        else if("Triangulacja".equals(selectedAlgorithm)){
            alg = "TRI";
        }

        try{
            if (inputFile == null) {
                System.err.println("BŁĄD: Plik wejściowy nie został wybrany przed uruchomieniem algorytmu.");
                ui.showErrorMessage("BŁĄD: Plik wejściowy nie został wybrany przed uruchomieniem algorytmu.");
                return;
            }
            String executablePath = System.getProperty("user.dir") + File.separator + "src_c" + File.separator + "a.out";
            ProcessBuilder pb =
                new ProcessBuilder(executablePath,"-i", inputFile.getAbsolutePath(), "-t", "TXT", "-o", outputFile.getAbsolutePath(), "-a", alg);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Odczytaj wyjście z procesu C, aby zobaczyć ewentualne błędy i komunikaty
            try (InputStreamReader isr = new InputStreamReader(process.getInputStream());
                 java.io.BufferedReader reader = new java.io.BufferedReader(isr)) {
                String line;
                System.out.println("--- Wyjście z programu C ---");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("----------------------------");
            System.out.println("Program w C zakończył się z kodem: " + exitCode);

            if(exitCode != 0) {
                ui.showErrorMessage("Błąd: Nie udało się wykonać wizualizacji. Program obliczeniowy zakończył się z kodem: " + exitCode);
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setInputFile(File file) {
        this.inputFile = file;
    }
    public int readInputFile(File file) {
        edges.clear(); 

        try (Scanner scanner = new Scanner(file)) {
            scanner.useLocale(Locale.US);

            while (scanner.hasNext()) {
                String name = scanner.next();
                Integer u = scanner.nextInt();
                Integer v = scanner.nextInt();
                Double weight = scanner.nextDouble();
                Edge newEdge = new Edge(name, u, v, weight);
                edges.add(newEdge);
            }
            
            System.out.println("Pomyślnie wczytano " + edges.size() + " krawędzi.");
            return edges.size();

        } catch (FileNotFoundException e) {
            System.err.println("BŁĄD: Nie można znaleźć pliku - " + file.getAbsolutePath());
            ui.showErrorMessage("BŁĄD: Nie można znaleźć pliku - " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Błąd podczas parsowania pliku: " + e.getMessage());
            ui.showErrorMessage("Błąd podczas parsowania pliku: " + e.getMessage());
        }
        return 0;
    }
    public int readOutputFile() {
        vertices.clear(); 

        try (Scanner scanner = new Scanner(this.outputFile)) {
            scanner.useLocale(Locale.US);

            while (scanner.hasNext()) {
                Integer id = scanner.nextInt();
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                Vertex newVertex = new Vertex(id, x, y);
                vertices.add(newVertex);
            }
            
            System.out.println("Pomyślnie wczytano " + vertices.size() + " wierzchołków.");
            return vertices.size();

        } catch (FileNotFoundException e) {
            System.err.println("BŁĄD: Nie można znaleźć pliku - " + this.outputFile.getAbsolutePath());
            ui.showErrorMessage("BŁĄD: Nie można znaleźć pliku - " + this.outputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("BŁĄD: " + e.getMessage());
            ui.showErrorMessage("BŁĄD: " + e.getMessage());
        }
        return 0;
    }

    public boolean checkIfInputFileExists(){
        return inputFile != null;
    }
}
