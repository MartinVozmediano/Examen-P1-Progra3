package ec.edu.taller;

import java.util.*;

/**
 * Completar la logica respetando el contrato descrito en README.md.
 */
public class GestorImpresiones {
    private Queue<TrabajoImpresion> colaTrabajos;
    private int capacidadMaxima;

    public GestorImpresiones(int capacidadMaxima) {
        // TODO: guardar la capacidad e inicializar una cola con prioridad.
        colaTrabajos = new PriorityQueue<>();
        this.capacidadMaxima = capacidadMaxima; //Deberia ser 10

    }

    public boolean registrarTrabajo(TrabajoImpresion trabajo) {
        // TODO: validar, comprobar duplicados y capacidad, y registrar.
        boolean valido = false;
        if(colaTrabajos.size() >= capacidadMaxima){
            return false;
        }else{
            valido = true;
        }

        if (trabajo == null) {
            return false;
        } else {
            valido = true;
        }

        if (trabajo.getCodigo() == null || trabajo.getCodigo().isEmpty() || trabajo.getCodigo().isBlank()) {
            return false;
        } else {
            valido = true;
        }

        if (existeTrabajo(trabajo.getCodigo())) {
            return false;
        } else {
            valido = true;
        }

        if (colaTrabajos.size() == 10) {
            return false;
        } else {
            valido = true;
        }

        if (valido == true) {
            colaTrabajos.offer(trabajo);
            return true;
        }

        return false;
    }


    public boolean existeTrabajo(String codigo) {

        // TODO: buscar por codigo entre los trabajos pendientes.
        if (codigo == null || codigo.isEmpty() || codigo.isBlank()) {
            return false;
        }

        for (TrabajoImpresion tr : colaTrabajos) {
            if (codigo.equals(tr.getCodigo())) {
                return true;
            }
        }

        return false;
    }

    public TrabajoImpresion consultarSiguienteTrabajo() {
        // TODO: consultar el trabajo de mayor prioridad sin retirarlo.
        if (colaTrabajos.isEmpty()) {
            return null;
        }
        return colaTrabajos.peek();
    }

    public TrabajoImpresion procesarSiguienteTrabajo() {
        // TODO: retirar el de mayor prioridad y cambiar su estado a PROCESADO.
        if (colaTrabajos.isEmpty()) {
            return null;
        }
        TrabajoImpresion retirado = colaTrabajos.poll();
        if (retirado != null) {
            retirado.setEstado("PROCESADO");
        }
        return retirado;
    }

    public int contarTrabajosPendientes() {
        // TODO: devolver la cantidad de trabajos en la cola.
        return colaTrabajos.size();
    }

    public int consultarEspaciosDisponibles() {
        // TODO: devolver capacidad maxima menos trabajos pendientes.
        return capacidadMaxima - colaTrabajos.size();
    }

    public List<TrabajoImpresion> listarTrabajos() {
        // TODO: devolver una lista ordenada por prioridad sin alterar la cola.
        ArrayList<TrabajoImpresion> listaNueva = new ArrayList<>(colaTrabajos);
        listaNueva.sort(Comparator.comparingInt(TrabajoImpresion::getPrioridad));
        return listaNueva;
    }
}
