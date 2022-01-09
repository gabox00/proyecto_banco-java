/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package actividaduf4.pkg5;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Gabriel
 */
public class Cuenta{
    
    protected Date fechaCreacion;
    private String nombreCliente;
    private String DNI;
    private String numeroCuenta;
    private Double saldo;
    private Database db;
    
    Cuenta(String _DNI, String _nombreCliente, String _fechaCreacion, String _saldo) throws ParseException, NumberFormatException{
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        numeroCuenta = _DNI.toUpperCase();
        DNI = _DNI.toUpperCase();
        nombreCliente = _nombreCliente;
        fechaCreacion = (_fechaCreacion.length() >= 1) ? formatter.parse(_fechaCreacion) : formatter.parse(formatter.format(new Date()));
        saldo = (_saldo.length() >= 1) ? Double.parseDouble(_saldo.replace(",",".")) : 0.00;
        String filePath = new File("").getAbsolutePath();
        db = new Database("jdbc:sqlite:"+filePath+"\\sqllite\\ifp.db");
    }

    //Hacemos uso de un patrón de diseño para utilizar el objeto en el menú
    protected static Cuenta _Cuenta(ResultSet rs) throws SQLException, ParseException {
        return new Cuenta(rs.getString("numero"),rs.getString("nombre"),rs.getString("fechaCreacion"),rs.getString("saldo"));
    }

    protected String getDNI() {
        return DNI.toUpperCase();
    }

    protected void setDNI(String DNI) {
        this.DNI = DNI;
    }

    protected String getFechaCreacion() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(fechaCreacion);
    }

    protected void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    protected String getNombreCliente() {
        return nombreCliente;
    }

    protected void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    protected String getNumeroCuenta() {
        return numeroCuenta;
    }

    protected void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    protected Double getSaldo() {
        return saldo;
    }

    protected void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    
    protected Boolean ingreso(Double dinero){
        if(dinero <= 0){
             return false;
        }
        else{
            this.saldo += dinero;
            if(this.db.connection()){
                String query = String.format("UPDATE cuenta SET saldo = %s WHERE numero = '%s'",this.saldo,this.DNI);
                return db.crud(query);
            }
            else{
                System.out.println("Ha ocurrido un error:" + this.db.getErrorMessage());
            }
            return false;
        }
    }
    
    protected Boolean reintegro(Double dinero){
        if(dinero <= 0 || this.saldo < dinero){
             return false; 
        }
        else{
            this.saldo -= dinero;
            if(this.db.connection()){
                String query = String.format("UPDATE cuenta SET saldo = %s WHERE numero = '%s'",this.saldo,this.DNI);
                return db.crud(query);
            }
            else{
                System.out.println("Ha ocurrido un error:" + this.db.getErrorMessage());
            }
            return false;
        }
    }
    
    protected Boolean transferencia(Cuenta cuentaDestino, Double dinero){
        if(this.reintegro(dinero)){
          if(cuentaDestino.ingreso(dinero)){
            return true;
          }          
        }
        return false;
    }
}
