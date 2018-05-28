/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.*;
import java.sql.*;

/**
 *
 * @author victor
 */
public class Jugador {

    private int id;
    private String nombre;
    private String apellidos;
    private int edad;
    private int idEquipo;

    public Jugador() {
        
    }
    
    public Jugador(int id) {
        this.id = id;
    }

    public Jugador(String nombre, String apellidos, int edad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
    }

    public Jugador(int id, String nombre, String apellidos, int edad) {
        this(nombre, apellidos, edad);
        this.id = id;
    }

    public Jugador(String nombre, String apellidos, int edad, int idEquipo) {
        this(nombre, apellidos, edad);
        this.idEquipo = idEquipo;
    }

    public Jugador(int id, String nombre, String apellidos, int edad, int idEquipo) {
        this(id, nombre, apellidos, edad);
        this.idEquipo = idEquipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    // --------- OPERACIONES BD ----------------------------------------
    // ---------- CRUD BÁSICO
    public boolean create() {
         boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {
            String sql = "INSERT INTO jugador (nombre,apellidos,edad,idquipo) VALUES (?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getApellidos());
                stmt.setInt(3, getEdad());
                stmt.setInt(4, getIdEquipo());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ok = false;
            System.err.println("Método create: Error al insertar equipos /" + ex.getMessage());
        }
        return ok;
    }

    public boolean retrieve() {
         boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {
            String sql = "SELECT nombre,apellidos,edad,idequipo FROM jugador WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, getId());
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    setNombre(rs.getString("nombre"));
                    setApellidos(rs.getString("apellidos"));
                    setEdad(rs.getInt("edad"));
                    setIdEquipo(rs.getInt("idequipo"));

                }
            }
        } catch (SQLException ex) {
            ok = false;
            System.err.println("Método retrieve: Eror al localizar equipo /" + ex.getMessage());
        }
        return ok;
    }

    public boolean update() {
        boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {
            
            String sqlUpdate = "UPDATE jugador SET nombre= ? , apellidos = ? , edad = ?, idequipo = ? WHERE id= ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getApellidos());
                stmt.setInt(3, getEdad());
                stmt.setInt(4, getIdEquipo());
                stmt.setInt(5, getId());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.err.println("Método update: Eror al visualizar/" + ex.getMessage());
        }
        return ok;
    }

    public boolean delete() {
        boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {
            String sqlDelete = "DELETE FROM jugador WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
                stmt.setInt(1, getId());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ok = false;
            System.err.println("Método delete: Eror al borrar/" + ex.getMessage());
        }
        return ok;
    }

    // ----------- Otras, de clase, no relacionadas con ÉSTE (this) objeto
    public static List<Jugador> obtenerJugadores(String busqueda, boolean esJunior, boolean esClass, boolean esMaster) {
        /* Junior:  14 años o más y menos de 18.
        Class: 18 o más y menos de 26.
        Master: 26 años o más. */

        List<Jugador> resultado = new ArrayList<>();
        try (Connection conn = ConexionBd.obtener()) {
            String sql = "SELECT id,nombre,apellidos,edad,idequipo FROM jugador";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultado.add(new Jugador(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"),
                                rs.getInt("edad"), rs.getInt("idequipo")));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Método retrieve: Eror al localizar equipo /" + ex.getMessage());
        }
        return resultado;
    }
}
