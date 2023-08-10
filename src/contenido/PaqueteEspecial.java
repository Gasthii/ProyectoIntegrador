package contenido;

import storage.DatabaseHandler;

import java.util.List;
import java.util.stream.Collectors;

public class PaqueteEspecial extends Contenido {

    private List<Contenido> contenido;
    private double descuento;

    public PaqueteEspecial(String name, String description, List<String> contenido, double descuento, String url, boolean fix) {
        super(name, description, url);
        this.contenido = contenido.stream().map(DatabaseHandler::getContenido).collect(Collectors.toList());
        this.descuento = descuento;
    }

    public PaqueteEspecial(String name, String description, List<Contenido> contenido, double descuento, String url) {
        super(name, description, url);
        this.contenido = contenido;
        this.descuento = descuento;
    }

    @Override
    public double calcularPrecio() {
        double totalPrice = 0;

        for (Contenido content : contenido) {
            totalPrice += content.calcularPrecio();
        }

        return totalPrice * (1 - descuento);
    }

    public List<Contenido> getContenido() {
        return contenido;
    }

    public double getDescuento() {
        return descuento;
    }

}
