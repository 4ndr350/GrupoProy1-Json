package Actores;

public abstract class Usuario {
    // Atributos
    private String id;
    private String login;
    private String passwordHash;
    private String nombre;
    private String correo;
    private double saldoVirtual;

    /**
     * Constructor de Usuario
     */
    public Usuario(String id, String login, String password, String nombre, String correo) {
        this.id = id;
        this.login = login;
        this.passwordHash = hashPassword(password);
        this.nombre = nombre;
        this.correo = correo;
        this.saldoVirtual = 0.0;
    }

    /**
     * Autentica al usuario verificando la contraseña
     * @param passwordPlano contraseña en texto plano
     * @return true si la autenticación es exitosa
     */
    public boolean autenticar(String passwordPlano) {
        return this.passwordHash.equals(hashPassword(passwordPlano));
    }

    /**
     * Acredita un monto al saldo virtual del usuario
     * Usado para reembolsos o recargas
     * @param monto cantidad a acreditar
     */
    public void acreditarSaldo(double monto) {
        if (monto > 0) {
            this.saldoVirtual += monto;
            System.out.println("Saldo acreditado: $" + monto + ". Nuevo saldo: $" + this.saldoVirtual);
        }
    }

    /**
     * Debita un monto del saldo virtual del usuario
     * Usado para pagos con saldo virtual
     * @param monto cantidad a debitar
     * @return true si el débito fue exitoso, false si no hay saldo suficiente
     */
    public boolean debitarSaldo(double monto) {
        if (monto > 0 && this.saldoVirtual >= monto) {
            this.saldoVirtual -= monto;
            System.out.println("Saldo debitado: $" + monto + ". Saldo restante: $" + this.saldoVirtual);
            return true;
        }
        System.out.println("Saldo insuficiente. Saldo actual: $" + this.saldoVirtual);
        return false;
    }

    /**
     * Método simple de hash para contraseñas
     * NOTA: En producción usar BCrypt, Argon2 o similar
     */
    private String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
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
        if (o == null || getClass() != o.getClass()) return false;
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