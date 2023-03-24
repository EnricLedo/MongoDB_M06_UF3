/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package presentacio;

import java.util.Scanner;

/**
 *
 * @author Taufik
 */
public class M06UF3PracMG {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int opcio = 0;
        int opcioClase = 0;
        
        do {
            System.out.println("Selecciona una opció:\n"
                    + "1. Introduir Ruta\n"
                    + "2. Clone\n"
                    + "3. Sortir");
            opcio = scanner.nextInt();

            switch (opcio) {
                case 1:
                        // Demanem al usuari la classe i la cantitat que vol generar
                        System.out.print("Selecciona una opció: \n"
                                + "1. Create\n"
                                + "2. Drop\n"
                                + "3. Push\n"
                                + "4. Pull\n"
                                + "5. Compare\n"
                                + "6. Endarrere\n");
                        
                        opcioClase = scanner.nextInt();
                        
                    
                case 2:
                    
                    
                case 3:
                
                System.exit(0);
            }
            
            
        } while (opcio != 4);
    }
}
