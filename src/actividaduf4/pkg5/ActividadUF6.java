/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package actividaduf4.pkg5;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 *
 * @author Gabriel
 */
public class ActividadUF6 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Banco banco = new Banco("GutierrezBank");
        menu(banco); 
    }
    
    public static void menu(Banco banco){
        Scanner sn = new Scanner(System.in);
        boolean exit = false;
        int opcion; //Guardaremos la opcion del usuario
        do {
            System.out.println("1. Crear Cuenta");
            System.out.println("2. Buscar Cuenta");
            System.out.println("3. Importar fichero de cuentas");
            System.out.println("4. Exportar fichero de cuentas");
            System.out.println("5  Listar Cuentas");
            System.out.println("9. Salir");
            System.out.println("Escribe una de las opciones");
            try{
                opcion = sn.nextInt();
                switch (opcion) {
                    case 1 -> {
                        Scanner ss = new Scanner(System.in);
                        System.out.println("Escribe el DNI");
                        String DNI = ss.nextLine();
                        System.out.println("Escribe el nombre del titular");
                        String nombreCliente = ss.nextLine();
                        Cuenta cuenta = new Cuenta(DNI,nombreCliente,"", "");
                        if (banco.crearCuenta(cuenta))
                            System.out.println("Cuenta creada Satisfactoriamente!");
                        else System.out.println("Error al crear la cuenta, ya existe la cuenta!");
                    }
                    case 2 -> subMenu(banco);
                    case 3 -> {
                        Scanner sr = new Scanner(System.in);
                        System.out.println("Especifique la ruta de donde se encuentra el fichero csv a importar");
                        String fileRouteRead = sr.nextLine();
                        banco.importarListadoCuentas(fileRouteRead); //C:\\Users\\Gabriel\\Desktop\\DAW\\Programacion\\UF4\\cuentas.csv
                    }
                    case 4 -> {
                        Scanner se = new Scanner(System.in);
                        System.out.println("Especifique la ruta en donde quiere alojar el fichero csv");
                        String fileRouteWrite = se.nextLine();
                        banco.exportarListadoCuentas(fileRouteWrite); //C:\\Users\\Gabriel\\Desktop\\DAW\\Programacion\\UF4\\cuentasExportar.csv
                    }
                    case 5 -> {
                        Scanner sl = new Scanner(System.in);
                        System.out.println("Escribe el DNI");
                        String numeroCuenta = sl.nextLine();
                        banco.getCuentas(numeroCuenta);
                        banco.listarCuentas();
                        System.out.println("");
                    }
                    case 9 -> exit = true;
                    default -> System.out.println("Solo números entre 1 y 4, y 9 para salir");
                }
            }
            catch(InputMismatchException | SQLException | ParseException e){
                System.out.println("Error, escoja una de las opciones del menú");
                menu(banco);
            }
        }
        while (!exit);
    }
    
    public static void subMenu(Banco banco) throws SQLException, ParseException {
        System.out.println("Escriba el DNI o el numero de cuenta a buscar");
        Scanner ss = new Scanner(System.in);
        String DNI = ss.nextLine();
        Cuenta cuenta = banco.getCuenta(DNI);
        if(cuenta != null){
            boolean exit = false;
            int opcion; //Guardaremos la opcion del usuario
            do {
                System.out.println("1. Modificar nombre del titular");
                System.out.println("2. Ingresar dinero");
                System.out.println("3. Retirar dinero");
                System.out.println("4. Detalles de la cuenta");
                System.out.println("9. Atrás");
                System.out.println("Escribe una de las opciones");
                try{
                    opcion = ss.nextInt();
                    switch (opcion) {
                        case 1:
                            System.out.println("Ingresa el nombre de titular a modificar: ");
                            Scanner sm = new Scanner(System.in);
                            String nombreClienteNuevo = sm.nextLine();
                            cuenta.setNombreCliente(nombreClienteNuevo);
                            if(banco.modificarNombreCuenta(cuenta)) System.out.println("Nombre del titular de la cuenta modficado Satisfactoriamente!");
                            else System.out.println("La cuenta no existe o hubo algún problema");
                            break;
                        case 2:
                            try{
                                System.out.println("Especifique el dinero que quiere ingresar: ");
                                Scanner si = new Scanner(System.in);
                                Double dinero = si.nextDouble();
                                if(cuenta.ingreso(dinero)) System.out.println("Ingreso de " + dinero + " euros correctamente en la cuenta " + cuenta.getNumeroCuenta());
                                else System.out.println("Error al ingresar el dinero, inténtelo más tarde");
                            }
                            catch(Exception e){
                                System.out.println("Error al ingresar el dinero, inténtelo más tarde");
                            }
                            break;
                        case 3:
                            try{
                                System.out.println("Especifique el dinero que quiere retirar: ");
                                Scanner si = new Scanner(System.in);
                                Double dinero = si.nextDouble();
                                if(cuenta.reintegro(dinero)) System.out.println("Retiro de " + dinero + " euros correctamente en la cuenta " + cuenta.getNumeroCuenta());
                                else System.out.println("Error al retirar, monto negativo o no posee saldo en la cuenta");
                            }
                            catch(Exception e){
                                System.out.println("Error al retirar el dinero, inténtelo más tarde");
                            }
                            break;
                        case 4:
                            System.out.println("Detalle de la cuenta:");
                            System.out.println("DNI / Numero de cuenta: " + cuenta.getNumeroCuenta());
                            System.out.println("Nombre del cliente: " + cuenta.getNombreCliente());
                            System.out.println("Fecha de creación: " + cuenta.getFechaCreacion());
                            System.out.println("Saldo actual: " + cuenta.getSaldo());
                            break;
                        case 9:
                            exit = true;
                            break;
                        default:
                            System.out.println("Solo números entre 1 y 4, y 9 para salir");
                    }
                }
                catch(InputMismatchException e){
                    System.out.println("Error, escoja correctamente una de las opciones del menú");
                    subMenu(banco);
                }
            }
            while (!exit);
        }
        else{
            System.out.println("No se ha encontrado nada con ese DNI");
        }
    }
    
}
