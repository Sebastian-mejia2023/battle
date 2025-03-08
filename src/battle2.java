import java.util.Random;
import java.util.Scanner;

public class battle2 {
    public static Scanner input = new Scanner(System.in);
    public static Random random = new Random();

    /**
     * Método principal que inicia el juego de batalla naval.
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        byte[] tableroUsuario = new byte[10]; // Tablero del usuario
        byte[] tableroMaquina = new byte[10]; // Tablero de la máquina

        System.out.println("\nTablero inicial del usuario: " + mostrarTablero(tableroUsuario) + "\n");

        System.out.println("Ingrese la cantidad de barcos que desea colocar (máximo 6): ");
        byte cantidadBarcos = input.nextByte();
        if (cantidadBarcos > 6) cantidadBarcos = 6;
        if (cantidadBarcos < 1) cantidadBarcos = 1;

        colocarBarcos(tableroUsuario, true, cantidadBarcos);
        colocarBarcos(tableroMaquina, false, cantidadBarcos);

        jugar(tableroUsuario, tableroMaquina);
    }

    /**
     * Convierte un tablero de bytes a una representación de cadena.
     * @param tablero El tablero a convertir.
     * @return Una cadena que representa el estado del tablero.
     */
    public static String mostrarTablero(byte[] tablero) {
        StringBuilder estado = new StringBuilder();
        for (byte casilla : tablero) {
            estado.append(casilla).append("|");
        }
        return estado.toString();
    }

    /**
     * Coloca los barcos en el tablero.
     * @param tablero El tablero en el que se colocarán los barcos.
     * @param esUsuario Indica si los barcos son para el usuario (true) o la máquina (false).
     * @param cantidad La cantidad de barcos a colocar.
     */
    public static void colocarBarcos(byte[] tablero, boolean esUsuario, byte cantidad) {
        byte[] tamanos = {1, 2, 3, 1, 2, 3};

        for (byte i = 0; i < cantidad; i++) {
            byte posicion;
            boolean colocado = false;

            while (!colocado) {
                if (esUsuario) {
                    System.out.println("\nElige una posición para el barco de " + tamanos[i] + " casillas (1-10): ");
                    posicion = input.nextByte();
                } else {
                    posicion = (byte) (random.nextInt(10) + 1);
                }

                if (sePuedeColocar(tablero, posicion, tamanos[i])) {
                    for (byte j = 0; j < tamanos[i]; j++) {
                        tablero[posicion - 1 + j] = (byte) (i + 1);
                    }
                    colocado = true;
                }
            }
        }
    }

    /**
     * Verifica si se puede colocar un barco en la posición dada.
     * @param tablero El tablero donde se intenta colocar el barco.
     * @param posicion La posición inicial.
     * @param tamano El tamaño del barco.
     * @return true si se puede colocar, false si no.
     */
    public static boolean sePuedeColocar(byte[] tablero, byte posicion, byte tamano) {
        if (posicion < 1 || posicion + tamano - 1 > 10) return false;
        for (byte i = 0; i < tamano; i++) {
            if (tablero[posicion - 1 + i] != 0) return false;
        }
        return true;
    }

    /**
     * Maneja la lógica del juego y los turnos.
     * @param usuarioTablero Tablero del usuario.
     * @param maquinaTablero Tablero de la máquina.
     */
    public static void jugar(byte[] usuarioTablero, byte[] maquinaTablero) {
        while (true) {
            System.out.println("\nIngresa la posición que deseas atacar (1-10, -1 para salir): ");
            byte ataqueUsuario = input.nextByte();

            if (ataqueUsuario == -1) return;

            procesarAtaque(maquinaTablero, ataqueUsuario, "Usuario");

            if (juegoTerminado(maquinaTablero)) {
                System.out.println("\n¡Ganaste! Has hundido todos los barcos de la máquina.\n");
                return;
            }

            byte ataqueMaquina = (byte) (random.nextInt(10) + 1);
            System.out.println("\nLa máquina ataca en la posición " + ataqueMaquina);
            procesarAtaque(usuarioTablero, ataqueMaquina, "Máquina");

            if (juegoTerminado(usuarioTablero)) {
                System.out.println("\n¡Perdiste! La máquina ha hundido todos tus barcos.\n");
                System.out.println("Tablero de la máquina al final: " + mostrarTablero(maquinaTablero));
                return;
            }
        }
    }

    /**
     * Procesa un ataque en el tablero.
     * @param tablero Tablero donde se ejecuta el ataque.
     * @param ataque Posición del ataque.
     * @param atacante Quién realiza el ataque.
     */
    public static void procesarAtaque(byte[] tablero, byte ataque, String atacante) {
        byte ataqueReal = (byte) (ataque - 1);

        if (tablero[ataqueReal] == 0) {
            System.out.println("\nAgua. No hay barco en esta posición.\n");
        } else {
            tablero[ataqueReal] = 4;
            System.out.println("\n¡Barco tocado por " + atacante + "!\n");
        }
    }

    /**
     * Verifica si el juego ha terminado.
     * @param tablero Tablero a analizar.
     * @return true si todos los barcos han sido hundidos, false si no.
     */
    public static boolean juegoTerminado(byte[] tablero) {
        for (byte casilla : tablero) {
            if (casilla > 0 && casilla < 4) {
                return false;
            }
        }
        return true;
    }
}
