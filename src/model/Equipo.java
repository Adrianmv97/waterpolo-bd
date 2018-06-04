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
        boolean ok = true; // Supongo que la operación va a ir ok;
        try (Connection conn = ConexionBd.obtener()) {

            String sqlUpdate = "UPDATE equipo SET nombre= ? , ciudad = ? , pais = ? WHERE id= ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getCiudad());
                stmt.setString(3, getPais());
                stmt.setInt(4, getId());
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
        try (Connection conn = ConexionBd.obtener()) {
            String sql = "SELECT id,nombre,apellidos,edad,idequipo FROM jugador WHERE idequipo = ? ";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, getId());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultado.add(new Jugador(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"),
                                rs.getInt("edad"), rs.getInt("idequipo")));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Método getJugadores: Eror al localizar equipo /" + ex.getMessage());
        }
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
        String sql;
        if (busqueda.equals("")) {
            sql = "SELECT id,nombre,ciudad,pais FROM equipo";
        } else {
            sql = "SELECT id, nombre ,ciudad ,pais FROM equipo WHERE nombre LIKE ? OR ciudad LIKE ? OR pais LIKE ?";
        }

        if (orden == ORDEN_NOMBRE) {
            sql = sql + " ORDER BY nombre";
        } else {
            sql = sql + " ORDER BY pais";
        }

        List<Equipo> resultado = new ArrayList<>();

        try (Connection conn = ConexionBd.obtener()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (!busqueda.equals("")) {
                    stmt.setString(1, '%' + busqueda + '%');
                    stmt.setString(2, '%' + busqueda + '%');
                    stmt.setString(3, '%' + busqueda + '%');
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultado.add(new Equipo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                    }
                }

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }
        return resultado;

    }

}
