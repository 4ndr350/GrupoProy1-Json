package Actores;

import PaquetesTiquetes.Tiquete;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el ROL de Comprador en el sistema
 * Cualquier usuario puede actuar como Comprador para adquirir tiquetes
 */
public class Comprador extends Usuario {
    // Atributos adicionales
    private List<Tiquete> misTiquetes;

    /**
     * Constructor de Comprador
     */
    public Comprador(String id, String login, String password, String nombre, String correo) {
        super(id, login, password, nombre, correo);
        this.misTiquetes = new ArrayList<>();
    }

    /**
     * Compra tiquetes para eventos
     * @param items lista de ItemVenta a comprar
     * @param medioPago medio de pago ("SALDO_VIRTUAL", "TARJETA", etc.)
     * @return Recibo con la informaci�n de la compra
     */
    public Object comprarTiquetes(List<Object> items, String medioPago) {
        // TODO: Implementar l�gica completa de compra
        // 1. Validar stock disponible
        // 2. Calcular precio total (base + cargos + emisi�n)
        // 3. Aplicar descuentos si hay ofertas activas
        // 4. Validar m�ximo de tiquetes por transacci�n
        // 5. Procesar pago seg�n medioPago
        // 6. Generar tiquetes y agregarlos a misTiquetes
        // 7. Retornar Recibo
        
        System.out.println(getNombre() + " comprando " + items.size() + " items con " + medioPago);
        return null; // Placeholder
    }

    /**
     * Transfiere un tiquete a otro usuario
     * @param tiquete el tiquete a transferir
     * @param destinatario usuario que recibir� el tiquete
     * @param password contrase�a para verificaci�n
     * @return true si la transferencia fue exitosa
     */
    public boolean transferirTiquete(Tiquete tiquete, Usuario destinatario, String password) {
        // Verificar autenticaci�n
        if (!autenticar(password)) {
            System.out.println("Contrase�a incorrecta");
            return false;
        }

        // Verificar propiedad del tiquete
        if (!misTiquetes.contains(tiquete)) {
            System.out.println("Este tiquete no te pertenece");
            return false;
        }

        // TODO: Verificar restricciones adicionales
        // - No transferir PaqueteDeluxe
        // - Verificar que no est� vencido
        // - Si es paquete, verificar restricciones especiales

        // Ejecutar transferencia
        misTiquetes.remove(tiquete);
        if (destinatario instanceof Comprador) {
            ((Comprador) destinatario).agregarTiquete(tiquete);
        }
        
        System.out.println("Tiquete transferido a: " + destinatario.getNombre());
        return true;
    }

    /**
     * Lista todos los tiquetes del comprador
     * @return lista de tiquetes
     */
    public List<Tiquete> listarMisTiquetes() {
        return new ArrayList<>(misTiquetes);
    }

    /**
     * Agrega un tiquete a la colecci�n
     */
    public void agregarTiquete(Tiquete tiquete) {
        if (tiquete != null && !misTiquetes.contains(tiquete)) {
            misTiquetes.add(tiquete);
        }
    }

    /**
     * Remueve un tiquete de la colecci�n
     */
    public void removerTiquete(Tiquete tiquete) {
        misTiquetes.remove(tiquete);
    }

    @Override
    public String toString() {
        return "Comprador{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", numTiquetes=" + misTiquetes.size() +
                ", saldoVirtual=$" + getSaldoVirtual() +
                '}';
    }
}
