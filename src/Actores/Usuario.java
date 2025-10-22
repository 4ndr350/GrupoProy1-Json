package Actores;

/**
 * Usuario base del sistema (abstracto).
 * Contiene credenciales, datos de contacto y saldo virtual.
 */
public abstract class Usuario {
    // Atributos
    private final String id;
    private String login;
    private String passwordHash;
    private String nombre;
    private String correo;
    private double saldoVirtual;

    /**
     * Constructor de Usuario.
     * Requiere id, login y password no nulos/ni vacíos.
     */
    public Usuario(String id, String login, String password, String nombre, String correo) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("id obligatorio");
        if (login == null || login.trim().isEmpty()) throw new IllegalArgumentException("login obligatorio");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("password obligatoria");

        this.id = id.trim();
        this.login = login.trim();
        this.passwordHash = hashPassword(password);
        this.nombre = nombre;
        this.correo = correo;
        this.saldoVirtual = 0.0;
    }

    /**
     * Autentica al usuario verificando la contraseña.
     */
    public boolean autenticar(String passwordPlano) {
        if (passwordPlano == null) return false;
        return this.passwordHash.equals(hashPassword(passwordPlano));
    }

    /**
     * Cambia la contraseña validando la actual.
     */
    public boolean cambiarPassword(String passwordActual, String passwordNueva) {
        if (!autenticar(passwordActual)) {
            System.out.println("Contraseña actual incorrecta");
            return false;
        }
        if (passwordNueva == null || passwordNueva.length() < 4) {
            System.out.println("La nueva contraseña debe tener al menos 4 caracteres");
            return false;
        }
        this.passwordHash = hashPassword(passwordNueva);
        System.out.println("Contraseña actualizada correctamente");
        return true;
    }

    /**
     * Acredita un monto al saldo virtual del usuario (reembolsos o recargas).
     */
    public void acreditarSaldo(double monto) {
        if (monto <= 0) {
            System.out.println("El monto a acreditar debe ser positivo");
            return;
        }
        this.saldoVirtual += monto;
        System.out.println("Saldo acreditado: $" + String.format("%.2f", monto) +
                ". Nuevo saldo: $" + String.format("%.2f", this.saldoVirtual));
    }

    /**
     * Debita un monto del saldo virtual del usuario (pagos con saldo virtual).
     */
    public boolean debitarSaldo(double monto) {
        if (monto <= 0) {
            System.out.println("El monto a debitar debe ser positivo");
            return false;
        }
        if (this.saldoVirtual < monto) {
            System.out.println("Saldo insuficiente. Saldo actual: $" + String.format("%.2f", this.saldoVirtual));
            return false;
        }
        this.saldoVirtual -= monto;
        System.out.println("Saldo debitado: $" + String.format("%.2f", monto) +
                ". Saldo restante: $" + String.format("%.2f", this.saldoVirtual));
        return true;
    }

    /**
     * Método simple de hash para contraseñas.
     * NOTA: En producción usar BCrypt, Argon2 o similar.
     */
    private String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }

    // Getters y Setters controlados
    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String nuevoLogin) {
        if (nuevoLogin == null || nuevoLogin.trim().isEmpty()) {
            System.out.println("Login inválido");
            return;
        }
        this.login = nuevoLogin.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getSaldoVirtual() {
        return saldoVirtual;
    }

    protected void setSaldoVirtual(double saldo) {
        this.saldoVirtual = saldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", saldoVirtual=$" + String.format("%.2f", saldoVirtual) +
                '}';
    }
}
