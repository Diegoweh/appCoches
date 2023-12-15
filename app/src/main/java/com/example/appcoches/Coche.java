package com.example.appcoches;

public class Coche {
    private int id;
    private String nombreP;
    private String descripcionP;
    private double precioP;
    private String imagen;
    private String nombreImagen;
    private String urlImagen;
    private int estadoP;

    // Constructor vacío requerido para Firebase
    public Coche() {

    }

    public Coche(int id, String nombreP, String descripcionP, double precioP, String imagen,
                 String nombreImagen, String urlImagen, int estadoP) {
        this.id = id;
        this.nombreP = nombreP;
        this.descripcionP = descripcionP;
        this.precioP = precioP;
        this.imagen = imagen;
        this.nombreImagen = nombreImagen;
        this.urlImagen = urlImagen;
        this.estadoP = estadoP;
    }

    // Métodos getter y setter...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public String getDescripcionP() {
        return descripcionP;
    }

    public void setDescripcionP(String descripcionP) {
        this.descripcionP = descripcionP;
    }

    public double getPrecioP() {
        return precioP;
    }

    public void setPrecioP(double precioP) {
        this.precioP = precioP;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public int getEstadoP() {
        return estadoP;
    }

    public void setEstadoP(int estadoP) {
        this.estadoP = estadoP;
    }
}
