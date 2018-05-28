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
public class Equipo {

    public static int ORDEN_NOMBRE = 0;
    public static int ORDEN_PAIS = 1;

    private int id;
    private String nombre;
    private String ciudad;
    private String pais;

    public Equipo() {
    }

    public Equipo(int id) {
        this.id = id;
    }

    public Equipo(String nombre, String ciudad, String pais) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public Equipo(int id, String nombre, String ciudad, String pais) {
        this(nombre, ciudad, pais);
        this.id = id;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    // --------- OPERACIONES BD ----------------------------------------

    // ---------- CRUD BÁSICO
    public boolean create() {
        boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {
            String sql = "INSERT INTO equipo (nombre,ciudad,pais) VALUES (?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getCiudad());
                stmt.setString(3, getPais());
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
            String sql = "SELECT nombre,ciudad,pais FROM equipo WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, getId());
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    setNombre(rs.getString("nombre"));
                    setCiudad(rs.getString("ciudad"));
                    setPais(rs.getString("pais"));

                }
            }
        } catch (SQLException ex) {
            ok = false;
            System.err.println("Método retrieve: Eror al localizar equipo /" + ex.getMessage());
        }
        return ok;
    }

    public boolean update() {
        return true;
    }

    public boolean delete() {
        boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {
            String sqlDelete = "DELETE FROM equipo WHERE id = ?";
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

    // ----------- Otras, de instancia, relacionadas con la fk
    public List<Jugador> getJugadores() {
        // POR HACER.
        List<Jugador> resultado = new ArrayList<>();
        resultado.add(new Jugador(1, "Paco", "López", 19));
        resultado.add(new Jugador(2, "Luisa", "Martínez", 21));
        return resultado;
    }

    // ----------- Otras, de clase, no relacionadas con ÉSTE (this) objeto
    public static List<Equipo> obtenerEquipos(String busqueda, int orden) {
        // Orden es una de las dos constantes de arriba: nombre o pais
        if (!(orden >= 0 && orden <= 1)) {
            throw new IllegalArgumentException("Parámetro de orden de equipos no admitido");
        }

        // Si la búsqueda es una cadena vacía lanzamos una select sin WHERE
        // y si tiene algo con WHERE y varios LIKEs
        // POR HACER
        List<Equipo> resultado = new ArrayList<>();
        boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {
            String sql = "SELECT id,nombre,ciudad,pais FROM equipo";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultado.add(new Equipo(rs.getInt("id"), rs.getString("nombre"), rs.getString("ciudad"), rs.getString("pais")));
                    }
                }
            }
        } catch (SQLException ex) {
            ok = false;
            System.err.println("Método retrieve: Eror al localizar equipo /" + ex.getMessage());
        }
        return resultado;

    }

}
