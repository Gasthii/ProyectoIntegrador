package vistas;

import components.CustomImageButton;
import contenido.Contenido;
import storage.DatabaseHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class VistaPrincipal extends JFrame {

    public VistaPrincipal(DatabaseHandler db) {
        List<Contenido> contenidos = db.obtenerTodo();

        Font font = new Font("Arial", Font.BOLD, 22);
        UIManager.put("Label.font", font);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Proyecto Integrador");
        setIconImage(getLogo());
        setSize(1000, 350);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(contenidos.size(), 1));

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        Contenido endGameC = contenidos.get(0);
        Contenido StrangerC = contenidos.get(1);
        Contenido paqueteC = contenidos.get(2);

        // Botones
        JButton endGame = new CustomImageButton(loadImagen(endGameC.getUrl()));
        JButton Stranger = new CustomImageButton(loadImagen(StrangerC.getUrl()));
        JButton paquete = new CustomImageButton(loadImagen(paqueteC.getUrl()));

        endGame.addActionListener(e ->
                JOptionPane.showMessageDialog(VistaPrincipal.this, endGameC.getNombre() + "\n" + endGameC.getDescripcion())
        );

        Stranger.addActionListener(e ->
                JOptionPane.showMessageDialog(VistaPrincipal.this, StrangerC.getNombre() + "\n" + StrangerC.getDescripcion())
        );

        paquete.addActionListener(e ->
                JOptionPane.showMessageDialog(VistaPrincipal.this, paqueteC.getNombre() + "\n" + paqueteC.getDescripcion())
        );

        // Crear el contenedor para dividir los botones
        buttonPanel.add(endGame);
        buttonPanel.add(Stranger);
        buttonPanel.add(paquete);

        // Agregar el panel de botones al panel de contenido
        add(buttonPanel, BorderLayout.CENTER);
    }

    public static ImageIcon loadImagen(String urlString) {
        try {
            URL url = new URL(urlString);
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image getLogo() {
        Image image;

        try {
            image = ImageIO.read(new File("src/assets/icono.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return image;
    }

}
