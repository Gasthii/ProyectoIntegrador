package contenido;

public class Serie extends Contenido {

    private final double duracion;
    private final double precioHora;

    public Serie(String name, String description, double duracion, double precioHora, String url) {
        super(name, description, url);
        this.duracion = duracion;
        this.precioHora = precioHora;
    }

    @Override
    public double calcularPrecio() {
        return duracion * precioHora;
    }

    public double getDuracion() {
        return duracion;
    }

    public double getPrecioHora() {
        return precioHora;
    }

}
