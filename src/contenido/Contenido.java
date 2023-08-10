package contenido;

public abstract class Contenido {

    private final String nombre;
    private final String descripcion;
    private final String url;

    public Contenido(String nombre, String desc, String url) {
        this.nombre = nombre;
        this.descripcion = desc;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl() {
        return url;
    }

    public abstract double calcularPrecio();

}
