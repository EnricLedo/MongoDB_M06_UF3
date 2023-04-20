/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package presentacio;

import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import static logica.Clone.clonarDirectoriRemot;
import logica.Create;
import static logica.DBConect.conexioMongoDB;
import logica.Push;
import java.util.logging.Level;
import java.util.logging.Logger;
import static logica.Drop.eliminarRepositoriRemot;
import logica.Pull;
import logica.Compare;

/**
 *
 * @author Taufik
 */
public class M06UF3PracMG {

    public static void main(String[] args) throws IOException, Exception {

        Scanner scanner = new Scanner(System.in);
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        int opcio = 0;
        int opcioClase = 0;
        String database;
        String rutaLocal = "";
        String rutaRemota = "";
        boolean enrere = false;

        MongoDatabase nomBD = conexioMongoDB();
        do {
            System.out.println(" \n"
                    + "**********************\n"
                    + "*   MENU PRINCIPAL   *\n"
                    + "**********************\n"
                    + "\n"
                    + "---------------------\n"
                    + "Selecciona una opció:\n"
                    + "---------------------\n"
                    + "---------------------\n"
                    + "1. GET\n"
                    + "2. Clone\n"
                    + "3. Sortir\n"
                    + "---------------------\n");
            opcio = scanner.nextInt();

            switch (opcio) {
                case 1 -> {
                    do {
                        // Demanem al usuari la classe i la cantitat que vol generar
                        System.out.print(" \n"
                                + "---------------------\n"
                                + "Selecciona una opció: \n"
                                + "---------------------\n"
                                + "---------------------\n"
                                + "1. Create\n"
                                + "2. Drop\n"
                                + "3. Push\n"
                                + "4. Pull\n"
                                + "5. Compare sense detall\n"
                                + "6. Compare amb detall\n"
                                + "7. Enrere\n"
                                + "---------------------\n");

                        opcioClase = scanner.nextInt();

                        switch (opcioClase) {
                            case 1:
                                System.out.println("-----------------------------------------------------");
                                System.out.println("S'ha selecionat Create");
                                System.out.println("-----------------------------------------------------");

                                System.out.println("Introdueix la ruta del repositori a Crear: ");
                                System.out.println("Exemple: C:\\Users\\Enric\\OneDrive\\Desktop\\Nom_Del_Repo");
                                rutaRemota = scanner.next();
                                System.out.println("-----------------------------------------------------");
                                
                                Create creator = new Create();
                                creator.crearRepositori(rutaRemota, nomBD);
                                
                                System.out.println("S'ha creat amb exit");
                                System.out.println("-----------------------------------------------------");
                                break;
                                
                            case 2:

                                //Eliminar repositori remot amb tots els seus documents, si no existeix s'informa a l'usuari
                                System.out.println("-----------------------------------------------------");
                                System.out.println("S'ha selecionat Drop");
                                System.out.println("-----------------------------------------------------");

                                System.out.println("Introdueix la ruta del repositori Remot: ");
                                rutaRemota = scanner.next();
                                System.out.println("-----------------------------------------------------");

                                eliminarRepositoriRemot(nomBD, rutaRemota);
                                break;

                            case 3:
                                System.out.println("-----------------------------------------------------");
                                System.out.println("S'ha selecionat Push");
                                System.out.println("-----------------------------------------------------");
                                do {
                                    System.out.print("\n"
                                            + "---------------------\n"
                                            + "Selecciona una opció:\n"
                                            + "---------------------\n"
                                            + "---------------------\n"
                                            + "1. Fer Push d'arxiu dintre del directori.\n"
                                            + "2. Fer Push de tot el directori.\n"
                                            + "3. Enrere.\n"
                                            + "---------------------\n");

                                    opcioClase = scanner.nextInt();

                                    System.out.println("");
                                    System.out.println("Introduexi la ruta del directori local: ");
                                    String rutaBase = scanner.next();
                                    String fichero = "";

                                    switch (opcioClase) {
                                        case 1:
                                            System.out.println("");
                                            System.out.println("Introduexi el nom del arxiu dintre del directori local: ");
                                            fichero = scanner.next();
                                            break;
                                        case 2:
                                            break;   
                                        default:
                                            System.out.println("Opción no válida");
                                            break;
                                    }

                                    Push push = new Push(rutaBase);

                                    try {
                                        Path rutaCompleta = Paths.get(rutaBase, fichero);
                                        System.out.println("");
                                        System.out.println("¿Desea forzar el Push? Introduzca 'true' o 'false':");
                                        boolean forzar = scanner.nextBoolean();
                                        push.push(rutaCompleta, forzar);
                                    } catch (Exception e) {
                                        System.err.println("Error al hacer push: " + e.getMessage());
                                    }

                                    break;
                                } while (opcioClase != 3);

                                break;
                            case 4:
                                System.out.println("-----------------------------------------------------");
                                System.out.println("S'ha selecionat Pull");
                                System.out.println("-----------------------------------------------------");

                                do {
                                    System.out.print("\n"
                                            + "---------------------\n"
                                            + "Selecciona una opció:\n"
                                            + "---------------------\n"
                                            + "---------------------\n"
                                            + "1. Fer Pull d'arxiu dintre del directori.\n"
                                            + "2. Fer Pull de tot el directori.\n"
                                            + "---------------------\n");

                                    opcioClase = scanner.nextInt();

                                    String dirBase = scanner.next();
                                    String fichero = "";

                                    switch (opcioClase) {
                                        case 1:
                                            fichero = scanner.next();
                                            break;
                                        case 2:
                                            break;
                                    }

                                    Pull pull = new Pull(dirBase);

                                    try {
                                        Path filePath = Paths.get(dirBase, fichero);
                                        System.out.println("");
                                        System.out.println("¿Desea forzar el Pull? Introduzca 'true' o 'false':");
                                        boolean forzar = scanner.nextBoolean();
                                        pull.pull(filePath, forzar);
                                    } catch (Exception e) {
                                        System.err.println("Error al hacer pull del archivo: " + e.getMessage());
                                    }
                                    
                                } while (opcioClase != 2);
                                break;


                            case 5:
                                System.out.println("-----------------------------------------------------");
                                System.out.println("Se ha selecionado la opcion de 'Compare sense detall'");
                                System.out.println("-----------------------------------------------------");
                                do {
                                    System.out.print("\n"
                                            + "---------------------\n"
                                            + "Selecciona una opció:\n"
                                            + "---------------------\n"
                                            + "---------------------\n"
                                            + "1. Comparar archivo.\n"
                                            + "2. Comparar directorio.\n"
                                            + "---------------------\n");

                                    opcioClase = scanner.nextInt();

                                    System.out.println("");
                                    System.out.println("Introduexi la ruta del directori local: ");
                                    String directorio = scanner.next();
                                    String fichero = "";

                                    switch (opcioClase) {
                                        case 1:
                                            System.out.println("");
                                            System.out.println("Introduexi el nom del arxiu dintre del directori local: ");
                                            fichero = scanner.next();
                                            break;
                                        case 2:
                                            break;
                                        default:
                                            System.out.println("Opción no válida");
                                            break;
                                    }

                                    Compare compare = new Compare(directorio);

                                    try {
                                        compare.compare(fichero);
                                    } catch (Exception e) {
                                        System.err.println("Error al comparar: " + e.getMessage());
                                    }

                                    break;
                                } while (opcioClase != 2);

                                break;

                            case 6:
                                System.out.println("-----------------------------------------------------");
                                System.out.println("Se ha selecionado la opcion de 'Compare amb detall'");
                                System.out.println("-----------------------------------------------------");

                                System.out.println("-----------------------------------------------------");
                                System.out.println("Introdueix la ruta del fixer o directori local: ");
                                rutaLocal = scanner.next();

                                System.out.println("Introdueix la ruta del fixer o directori Remot: ");
                                rutaRemota = scanner.next();
                                System.out.println("-----------------------------------------------------");

                                System.out.println("-----------------------------------------------------");
                                System.out.println("Aquí es es mostrarà el resultat.");
                                System.out.println("-----------------------------------------------------");
                                break;

                            case 7:
                                break;

                            default:
                                opcioClase = 0;
                                System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                        }

                    } while (opcioClase != 7);
                }

                case 2 -> {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Se ha selecionado la opcion de clonar");
                    System.out.println("-----------------------------------------------------");

                    System.out.println("Introdueix la ruta del directori remot: ");
                    rutaRemota = scanner.next();
                    clonarDirectoriRemot(rutaRemota, nomBD);
                }
                case 3 -> System.exit(0);
            }

        } while (opcio != 3);
    }

}
