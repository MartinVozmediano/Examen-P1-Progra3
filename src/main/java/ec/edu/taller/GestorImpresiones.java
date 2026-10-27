package ec.edu.taller;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Completar la logica respetando el contrato descrito en README.md.
 */
public class GestorImpresiones {
    private PriorityQueue<TrabajoImpresion> colaTrabajos;
    private int capacidadMaxima;

    public GestorImpresiones(int capacidadMaxima) {
        // TODO: guardar la capacidad e inicializar una cola con prioridad.
        colaTrabajos = new PriorityQueue<>();
        this.capacidadMaxima = capacidadMaxima;

    }

    public boolean registrarTrabajo(TrabajoImpresion trabajo) {
        // TODO: validar, comprobar duplicados y capacidad, y registrar.
        return false;
    }

    public boolean existeTrabajo(String codigo) {

        // TODO: buscar por codigo entre los trabajos pendientes.
        for(TrabajoImpresion tr : colaTrabajos){
            if(codigo.equals(tr.getCodigo())){
                return true;
            }
        }

        return false;
    }

    public TrabajoImpresion consultarSiguienteTrabajo() {
        // TODO: consultar el trabajo de mayor prioridad sin retirarlo.
        return null;
    }

    public TrabajoImpresion procesarSiguienteTrabajo() {
        // TODO: retirar el de mayor prioridad y cambiar su estado a PROCESADO.
        return null;
    }

    public int contarTrabajosPendientes() {
        // TODO: devolver la cantidad de trabajos en la cola.
        return 0;
    }

    public int consultarEspaciosDisponibles() {
        // TODO: devolver capacidad maxima menos trabajos pendientes.
        return 0;
    }

    public List<TrabajoImpresion> listarTrabajos() {
        // TODO: devolver una lista ordenada por prioridad sin alterar la cola.
        return new ArrayList<>();
    }
}
