/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package presentacio;

import com.mongodb.client.MongoDatabase;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import static logica.Clone.clonarDirectoriRemot;
import logica.Create;
import static logica.DBConect.conexioMongoDB;
import logica.Push;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static logica.Drop.eliminarRepositori;

/**
 *
 * @author Taufik
 */
public class M06UF3PracMG {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        int opcio = 0;
        int opcioClase = 0;
        String database;
        String rutaLocal = "";
        String rutaRemota = "";

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
                    + "1. Introduir Ruta\n"
                    + "2. Clone\n"
                    + "3. Sortir\n"
                    + "---------------------\n");
            opcio = scanner.nextInt();

            switch (opcio) {
                case 1:

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

                            case 2:

                                //Eliminar repositori remot amb tots els seus documents, si no existeix s'informa a l'usuari
                                System.out.println("-----------------------------------------------------");
                                System.out.println("S'ha selecionat Drop");
                                System.out.println("-----------------------------------------------------");

                                System.out.println("Introdueix la ruta del repositori Remot: ");
                                rutaRemota = scanner.next();
                                System.out.println("-----------------------------------------------------");

                                //eliminarRepositori(nomBD, rutaRemota);
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
                                            + "---------------------\n");

                                    opcioClase = scanner.nextInt();

                                    System.out.println("");
                                    System.out.println("Introduexi la ruta del directori local: ");
                                    String dirBase = scanner.next();
                                    String fichero = "";

                                    switch (opcioClase) {
                                        case 1:
                                            System.out.println("");
                                            System.out.println("Introduexi el nom del arxiu dintre del directori local: ");
                                            fichero = scanner.next();
                                            break;
                                        case 2:
                                            break;
                                    }

                                    Push push = new Push(dirBase);

                                    try {
                                        Path filePath = Paths.get(dirBase, fichero);
                                        System.out.println("");
                                        System.out.println("¿Desea forzar el Push? Introduzca 'true' o 'false':");
                                        boolean forzar = scanner.nextBoolean();
                                        push.push(filePath, forzar);
                                    } catch (Exception e) {
                                        System.err.println("Error al hacer push del archivo: " + e.getMessage());
                                    }

                                } while (opcioClase != 2);

                                break;
                            case 4:
                                System.out.println("-----------------------------------------------------");
                                System.out.println("S'ha selecionat Pull");
                                System.out.println("-----------------------------------------------------");

                                System.out.println("Introdueix la ruta del fixer o directori Remot: ");
                                rutaRemota = scanner.next();
                                System.out.println("-----------------------------------------------------");

                                System.out.println("S'ha baixat amb exit");
                                System.out.println("-----------------------------------------------------");

                                break;

                            case 5:
                                System.out.println("-----------------------------------------------------");
                                System.out.println("Se ha selecionado la opcion de 'Compare sense detall'");
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

                    break;

                case 2:
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Se ha selecionado la opcion de clonar");
                    System.out.println("-----------------------------------------------------");

                    System.out.println("Introdueix la ruta del directori remot: ");
                    rutaRemota = scanner.next();
                    clonarDirectoriRemot(rutaRemota, nomBD);

                    break;
                case 3:

                    System.exit(0);
            }

        } while (opcio != 3);
    }

}
