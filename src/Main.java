import contenido.Contenido;
import contenido.PaqueteEspecial;
import contenido.Pelicula;
import contenido.Serie;
import storage.DatabaseHandler;
import vistas.VistaPrincipal;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();
        db.crearTabla();

        Pelicula peliculaDef = new Pelicula("Avengers: Endgame", "EndGame", 3, 500, "https://lumiere-a.akamaihd.net/v1/images/p_avengersendgame_19751_e14a0104.jpeg");
        Serie serieDef = new Serie("Stranger Things", "Strange", 25, 200, "https://applications-media.feverup.com/image/upload/f_auto,w_550,h_550/fever2/plan/photo/1278fd74-b301-11ed-8334-52172309e0c0.jpg");
        PaqueteEspecial paqueteDef = new PaqueteEspecial("Fin de semana de superhéroes", "fin de semana", Arrays.asList(peliculaDef, serieDef), 15, "https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/Netflix_icon.svg/2048px-Netflix_icon.svg.png");

        db.guardarPelicula(peliculaDef);
        db.guardarSerie(serieDef);
        db.guardarPaquete(paqueteDef);

        SwingUtilities.invokeLater(() -> {
            VistaPrincipal vista = new VistaPrincipal(db);
            vista.setVisible(true);
        });
    }

}