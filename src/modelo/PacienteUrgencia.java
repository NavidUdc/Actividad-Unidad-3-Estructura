import java.util.Objects;

public class PacienteUrgencia {

    private String codigoTriage;
    private String nombre;
    private String identificacion;
    private String contacto;
    private String urgencia;

    public PacienteUrgencia(String codigoTriage, String nombre, String identificacion, String urgencia,
            String contacto) {
        this.codigoTriage = codigoTriage;
        this.contacto = contacto;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.urgencia = urgencia;
    }

    // Gets
    public String getCodigoTriage() {
        return codigoTriage;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getContacto() {
        return contacto;
    }

    public String getUrgencia() {
        return urgencia;
    }

    // Sets
    public void setCodigoTriage(String codigoTriage) {
        this.codigoTriage = codigoTriage;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    // HASHCODE Y EQUALS
    @Override
    public int hashCode() {
        return Objects.hash(codigoTriage != null ? codigoTriage.toLowerCase() : null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PacienteUrgencia other = (PacienteUrgencia) obj;
        return Objects.equals(codigoTriage, other.codigoTriage);
    }

    // TOSTRING
    @Override
    public String toString() {
        return "PacienteUrgencia{" +
                "codigoTriage='" + codigoTriage + '\'' +
                ", nombre='" + nombre + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", urgencia='" + urgencia + '\'' +
                ", contacto='" + contacto + '\'' +
                '}';
    }

}