/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package actividaduf4.pkg5;

import java.io.*;
import java.lang.String;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */
public class Banco {
    
    protected String nombreBanco;
    private Database db;
    private ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>();
    
    Banco(String _nombreBanco){
        nombreBanco = _nombreBanco;
        String filePath = new File("").getAbsolutePath();
        db = new Database("jdbc:sqlite:"+filePath+"\\sqllite\\ifp.db");
    }
    
    protected Boolean crearCuenta(Cuenta cuenta){
        if(this.db.connection()){
            String query = String.format("INSERT INTO cuenta (numero, nombre, fechaCreacion, saldo) VALUES ('%s','%s','%s','%f');",cuenta.getDNI(),cuenta.getNombreCliente(),cuenta.getFechaCreacion(),cuenta.getSaldo());
            return db.crud(query);
        }
        else{
            System.out.println("Ha ocurrido un error:" + this.db.getErrorMessage());
        }
        return false;
    }
    
    protected ArrayList<Cuenta> getCuentas(String numeroCuenta) throws SQLException, ParseException {
        if(this.db.connection()){
            String query = numeroCuenta.isEmpty() ? "SELECT * FROM cuenta" : "SELECT * FROM cuenta WHERE numero LIKE '%" +numeroCuenta+ "%';";
            ResultSet rs = db.select(query);
            while (rs.next()){
                Cuenta cuenta = Cuenta._Cuenta(rs);
                cuentas.add(cuenta);
            }
            rs.close();
            return cuentas;
        }
        else{
            System.out.println("Ha ocurrido un error:" + this.db.getErrorMessage());
        }
        return null;
    }

    protected Cuenta getCuenta(String numeroCuenta) throws SQLException, ParseException {
        if(this.db.connection()){
            String query = String.format("SELECT * FROM cuenta WHERE numero = '%s'",numeroCuenta.toUpperCase());
            ResultSet rs = db.select(query);
            Cuenta cuenta = Cuenta._Cuenta(rs);
            rs.close();
            return cuenta;
        }
        else{
            System.out.println("Ha ocurrido un error:" + this.db.getErrorMessage());
        }
        return null;
    }

    protected Boolean modificarNombreCuenta(Cuenta cuenta){
        if(this.db.connection()){
            String query = String.format("UPDATE cuenta SET nombre = '%s' WHERE numero = '%s'",cuenta.getNombreCliente(),cuenta.getDNI());
            return db.crud(query);
        }
        else{
            System.out.println("Ha ocurrido un error:" + this.db.getErrorMessage());
        }
        return false;
    }

    protected void listarCuentas(){
        if(cuentas != null) System.out.println("Listado de cuentas: ");
        for(Cuenta cuenta: cuentas){
            System.out.println("Numero de cuenta: " + cuenta.getNumeroCuenta() + " | Titular: " +  cuenta.getNombreCliente());
        }
    }

    protected void importarListadoCuentas(String fileRoute){
        try {
            FileReader reader = new FileReader(fileRoute);
            BufferedReader buffer = new BufferedReader(reader);
            
            String linea;
            
            while((linea = buffer.readLine()) != null){
                String[] col = linea.split(";");
                if(col.length == 4){
                    String numeroCuenta = col[0];
                    String nombreCliente = col[1];
                    String fechaCreacion = col[2];
                    String saldo = col[3];
                    Cuenta cuenta = new Cuenta(numeroCuenta,nombreCliente,fechaCreacion,saldo);
                    crearCuenta(cuenta);
                }
                else if(col.length == 3){
                    String numeroCuenta = col[0];
                    String nombreCliente = col[1];
                    String fechaCreacion = col[2];
                    String saldo = "";
                    Cuenta cuenta = new Cuenta(numeroCuenta,nombreCliente,fechaCreacion,saldo);
                    crearCuenta(cuenta);
                }
            } 
            reader.close();
            
        } catch (IOException | ParseException e) {
            System.out.println("No se pudo leer el fichero de la ruta: " + fileRoute);
        } finally {
            System.out.println("Ha culminado la importacion de las cuentas. Total de cuentas:");
            listarCuentas();
        }       
    }
    
    protected void exportarListadoCuentas(String fileRoute){
        try {
            FileWriter writer = new FileWriter(fileRoute);
            BufferedWriter buffer = new BufferedWriter(writer);
            String linea;
            cuentas = getCuentas("");
            for(Cuenta cuenta: cuentas){
                linea = cuenta.getDNI() + ";" + cuenta.getNombreCliente() + ";" + cuenta.getFechaCreacion() + ";" + cuenta.getSaldo();
                buffer.write(linea);
                buffer.newLine();
                buffer.flush();
            }
            writer.close();
            
        } catch (IOException | SQLException | ParseException e) {
            System.out.println("No se pudo escribir en la ruta especificada: " + fileRoute);
        } finally {
            System.out.println("Ha culminado la exportacion de las cuentas. Total de cuentas:");
        }       
    }
    

    
}
