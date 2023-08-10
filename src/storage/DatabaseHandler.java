package storage;

import contenido.Contenido;
import contenido.PaqueteEspecial;
import contenido.Pelicula;
import contenido.Serie;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseHandler {

    private static final String JDBC_URL = "jdbc:oracle:thin:@//localhost:1521/yourSID";
    private static final String USUARIO = "usuario";
    private static final String CONTRA = "contraseña";

    private static final String TABLE_NAME = "Cartelera";

    public DatabaseHandler() {
        /*try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/
    }

    private static final String DB_NAME = "data/mydatabase.db"; // Ruta de la base de datos en la carpeta 'data'
    private static Connection connection;

    public static Connection obtenerConexion() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
                // System.out.println("Conexión a SQLite establecida.");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /*public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USUARIO, CONTRA);
    }*/

    public void crearTabla() {
        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "nombre VARCHAR2(255) PRIMARY KEY," +
                    "desc VARCHAR2(1000)," +
                    "imagen VARCHAR2(1000)," +
                    "tipo VARCHAR2(20)," +
                    "duracion NUMBER," +
                    "precioHora NUMBER," +
                    "descuento NUMBER," +
                    "contenido VARCHAR2(1000)" +
                    ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contenido> obtenerTodo() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        List<String> contenidos = new ArrayList<>();

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                contenidos.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contenidos.stream().map(DatabaseHandler::getContenido).collect(Collectors.toList());
    }

    public static Contenido getContenido(String nombre) {
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM " + TABLE_NAME + " WHERE nombre = ?")) {

            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");

                switch (tipo.toLowerCase()) {
                    case "pelicula": {
                        return new Pelicula(
                                rs.getString("nombre"),
                                rs.getString("desc"),
                                rs.getDouble("duracion"),
                                rs.getDouble("precioHora"),
                                rs.getString("imagen"));
                    }
                    case "serie": {
                        return new Serie(
                                rs.getString("nombre"),
                                rs.getString("desc"),
                                rs.getDouble("duracion"),
                                rs.getDouble("precioHora"),
                                rs.getString("imagen"));
                    }
                    case "paquete": {
                        List<String> contenidos = Arrays.stream(rs.getString("contenido").split("\\|\\|"))
                                .collect(Collectors.toList());

                        return new PaqueteEspecial(
                                rs.getString("nombre"),
                                rs.getString("desc"),
                                contenidos,
                                rs.getDouble("descuento"),
                                rs.getString("imagen"),
                                false
                        );
                    }
                    default:
                        return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void guardarPelicula(Pelicula contenido) {
        if(yaExiste(contenido))
            return;

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO " + TABLE_NAME + " (nombre, desc, tipo, imagen, duracion, precioHora, descuento, contenido) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, contenido.getNombre());
            pstmt.setString(2, contenido.getDescripcion());
            pstmt.setString(3, "pelicula");
            pstmt.setString(4, contenido.getUrl());
            pstmt.setDouble(5, contenido.getDuracion());
            pstmt.setDouble(6, contenido.getPrecioHora());
            pstmt.setDouble(7, 0);
            pstmt.setString(8, "");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardarSerie(Serie contenido) {
        if(yaExiste(contenido))
            return;

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO " + TABLE_NAME + " (nombre, desc, tipo, imagen, duracion, precioHora, descuento, contenido) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, contenido.getNombre());
            pstmt.setString(2, contenido.getDescripcion());
            pstmt.setString(3, "serie");
            pstmt.setString(4, contenido.getUrl());
            pstmt.setDouble(5, contenido.getDuracion());
            pstmt.setDouble(6, contenido.getPrecioHora());
            pstmt.setDouble(7, 0);
            pstmt.setString(8, "");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardarPaquete(PaqueteEspecial contenido) {
        if(yaExiste(contenido))
            return;

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO " + TABLE_NAME + " (nombre, desc, tipo, imagen, duracion, precioHora, descuento, contenido) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, contenido.getNombre());
            pstmt.setString(2, contenido.getDescripcion());
            pstmt.setString(3, "paquete");
            pstmt.setString(4, contenido.getUrl());
            pstmt.setDouble(5, 0);
            pstmt.setDouble(6, 0);
            pstmt.setDouble(7, contenido.getDescuento());
            pstmt.setString(8, contenido.getContenido().stream().map(Contenido::getNombre).collect(Collectors.joining("||")));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean yaExiste(Contenido contenido) {
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE nombre = ?")) {
            pstmt.setString(1, contenido.getNombre());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
