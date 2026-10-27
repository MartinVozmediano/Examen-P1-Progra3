package ec.edu.taller;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

/**
 * Ventana del centro de impresion con dos pestañas (REGISTRAR TRABAJO y GESTIONAR TRABAJOS).
 * El diseño visual esta en InterfazImpresiones.form (Swing UI Designer de IntelliJ);
 * esta clase solo enlaza los componentes con GestorImpresiones.
 * Los componentes NO se inicializan con "new": el diseñador los crea a partir del .form.
 */
public class InterfazImpresiones {
    private static final int CAPACIDAD = 10;
    private static final String ESTADO_VACIO = "-";

    // Unico gestor compartido por las dos pestañas durante toda la ejecucion.
    private final GestorImpresiones gestor = new GestorImpresiones(CAPACIDAD);

    // ---- Raiz y pestañas ----
    private JPanel panelPrincipal;
    private JTabbedPane tabbedPane;
    private JPanel panelTabRegistrar;
    private JPanel panelTabGestionar;

    // ---- Pestaña 1: REGISTRAR TRABAJO ----
    private JPanel panelEncabezadoRegistro;
    private JLabel lblTituloRegistrar;
    private JLabel lblVacioRegistro;
    private JPanel panelFormularioRegistro;
    private JLabel lblCodigo;
    private JTextField txtCodigo;
    private JLabel lblSolicitante;
    private JComboBox<String> cmbSolicitante;
    private JLabel lblDocumento;
    private JTextField txtDocumento;
    private JLabel lblPrioridad;
    private JSpinner spnPrioridad;
    private JPanel panelBotonesRegistro;
    private JButton btnRegistrar;
    private JButton btnLimpiar;

    // ---- Pestaña 2: GESTIONAR TRABAJOS ----
    private JPanel panelEncabezadoGestion;
    private JLabel lblVacioGestion;
    private JLabel lblTituloGestionar;
    private JPanel panelDatosTrabajo;
    private JLabel lblGCodigo;
    private JLabel lblValCodigo;
    private JLabel lblGSolicitante;
    private JLabel lblValSolicitante;
    private JLabel lblGDocumento;
    private JLabel lblValDocumento;
    private JLabel lblGPrioridad;
    private JLabel lblValPrioridad;
    private JLabel lblGEstado;
    private JLabel lblValEstado;
    private JPanel panelLista;
    private JLabel lblTituloLista;
    private JScrollPane scrollResultados;
    private JTextArea txtResultados;
    private JPanel panelInferior;
    private JPanel panelContadores;
    private JLabel lblPendientes;
    private JLabel lblDisponibles;
    private JLabel lblCapacidad;
    private JPanel panelBotonesGestion;
    private JButton btnProcesar;
    private JButton btnConsultar;
    private JButton btnListar;

    public InterfazImpresiones() {
        configurarComponentes();
        configurarEventos();
        mostrarDatosTrabajo(null);
        actualizarContadores();
        mostrarListado();
    }

    // ---------------------------------------------------------------- configuracion

    private void configurarComponentes() {
        cmbSolicitante.setModel(new DefaultComboBoxModel<>(
                new String[] {"ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO"}));
        spnPrioridad.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        // Marcos negros de cada pestaña y de los encabezados, como en los bocetos.
        for (JPanel tab : new JPanel[] {panelTabRegistrar, panelTabGestionar}) {
            tab.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.BLACK, 3), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        }
        for (JPanel encabezado : new JPanel[] {panelEncabezadoRegistro, panelEncabezadoGestion}) {
            encabezado.setBorder(new LineBorder(Color.BLACK, 2));
        }
        for (JLabel celda : new JLabel[] {lblTituloRegistrar, lblVacioRegistro,
                lblVacioGestion, lblTituloGestionar}) {
            celda.setBorder(new LineBorder(Color.BLACK, 2));
            celda.setFont(celda.getFont().deriveFont(Font.BOLD, 15f));
        }
        scrollResultados.setBorder(new LineBorder(Color.BLACK, 3));

        // Etiquetas de los formularios
        for (JLabel etiqueta : new JLabel[] {lblCodigo, lblSolicitante, lblDocumento, lblPrioridad,
                lblGCodigo, lblGSolicitante, lblGDocumento, lblGPrioridad, lblGEstado,
                lblValCodigo, lblValSolicitante, lblValDocumento, lblValPrioridad, lblValEstado,
                lblPendientes, lblDisponibles, lblCapacidad}) {
            etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 15f));
        }
        lblTituloLista.setFont(lblTituloLista.getFont().deriveFont(Font.BOLD, 14f));

        txtResultados.setEditable(false);
        txtResultados.setLineWrap(true);
        txtResultados.setWrapStyleWord(true);
        txtResultados.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));

        // Botones con borde redondeado
        for (JButton boton : new JButton[] {btnRegistrar, btnLimpiar, btnProcesar, btnConsultar, btnListar}) {
            boton.setFont(boton.getFont().deriveFont(Font.BOLD, 14f));
            boton.setFocusPainted(false);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            boton.setBorder(new LineBorder(Color.BLACK, 3, true));
        }
        panelPrincipal.setPreferredSize(new Dimension(780, 660));
    }

    private void configurarEventos() {
        btnRegistrar.addActionListener(e -> registrar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnProcesar.addActionListener(e -> procesar());
        btnConsultar.addActionListener(e -> consultarSiguiente());
        btnListar.addActionListener(e -> {
            mostrarListado();
            mostrarMensaje("Listado actualizado: " + gestor.contarTrabajosPendientes() + " pendiente(s).");
        });
        // Al abrir la pestaña de gestion se refrescan contadores y listado.
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == panelTabGestionar) {
                actualizarContadores();
                mostrarListado();
            }
        });
    }

    // ---------------------------------------------------------------- pestaña REGISTRAR

    private void registrar() {
        String codigo = txtCodigo.getText();
        String documento = txtDocumento.getText().trim();
        String solicitante = (String) cmbSolicitante.getSelectedItem();
        int prioridad = (Integer) spnPrioridad.getValue();

        if (codigo.trim().isEmpty()) {
            avisar("Rechazado: el codigo del trabajo no puede estar vacio.");
            return;
        }
        if (documento.isEmpty()) {
            avisar("Rechazado: debe indicar el nombre del documento.");
            return;
        }
        if (gestor.existeTrabajo(codigo)) {
            avisar("Rechazado: ya existe un trabajo pendiente con el codigo " + codigo + ".");
            return;
        }
        if (gestor.consultarEspaciosDisponibles() <= 0) {
            avisar("Rechazado: la cola esta llena (capacidad " + CAPACIDAD + ").");
            return;
        }

        TrabajoImpresion trabajo = new TrabajoImpresion(codigo, solicitante, documento, prioridad);
        if (gestor.registrarTrabajo(trabajo)) {
            actualizarContadores();
            mostrarListado();
            mostrarMensaje("Registrado: " + trabajo);
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Trabajo registrado correctamente:\n" + trabajo,
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } else {
            avisar("Rechazado: no se pudo registrar el trabajo.");
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        cmbSolicitante.setSelectedIndex(0);
        txtDocumento.setText("");
        spnPrioridad.setValue(1);
        txtCodigo.requestFocusInWindow();
    }

    private void avisar(String mensaje) {
        JOptionPane.showMessageDialog(panelPrincipal, mensaje, "Registro rechazado",
                JOptionPane.WARNING_MESSAGE);
    }

    // ---------------------------------------------------------------- pestaña GESTIONAR

    private void consultarSiguiente() {
        TrabajoImpresion siguiente = gestor.consultarSiguienteTrabajo();
        mostrarDatosTrabajo(siguiente);
        if (siguiente == null) {
            mostrarMensaje("La cola esta vacia: no hay trabajos pendientes.");
        } else {
            mostrarMensaje("Siguiente (no se retira de la cola): " + siguiente);
        }
    }

    private void procesar() {
        TrabajoImpresion procesado = gestor.procesarSiguienteTrabajo();
        if (procesado == null) {
            mostrarDatosTrabajo(null);
            mostrarMensaje("La cola esta vacia: no hay trabajos para procesar.");
            return;
        }
        mostrarDatosTrabajo(procesado);
        actualizarContadores();
        mostrarListado();
        mostrarMensaje("Procesado: " + procesado);
    }

    /** Muestra en las etiquetas de la pestaña de gestion los datos del trabajo indicado. */
    private void mostrarDatosTrabajo(TrabajoImpresion trabajo) {
        if (trabajo == null) {
            lblValCodigo.setText(ESTADO_VACIO);
            lblValSolicitante.setText(ESTADO_VACIO);
            lblValDocumento.setText(ESTADO_VACIO);
            lblValPrioridad.setText(ESTADO_VACIO);
            lblValEstado.setText(ESTADO_VACIO);
        } else {
            lblValCodigo.setText(trabajo.getCodigo());
            lblValSolicitante.setText(trabajo.getSolicitante());
            lblValDocumento.setText(trabajo.getDocumento());
            lblValPrioridad.setText(String.valueOf(trabajo.getPrioridad()));
            lblValEstado.setText(trabajo.getEstado());
        }
    }

    // ---------------------------------------------------------------- auxiliares

    private void mostrarListado() {
        List<TrabajoImpresion> lista = gestor.listarTrabajos();
        StringBuilder sb = new StringBuilder();
        if (lista.isEmpty()) {
            sb.append("(no hay trabajos pendientes)");
        } else {
            for (TrabajoImpresion trabajo : lista) {
                sb.append(trabajo).append('\n');
            }
        }
        txtResultados.setText(sb.toString().trim());
        txtResultados.setCaretPosition(0);
    }

    private void mostrarMensaje(String mensaje) {
        txtResultados.append("\n>> " + mensaje);
        txtResultados.setCaretPosition(txtResultados.getDocument().getLength());
    }

    private void actualizarContadores() {
        lblPendientes.setText("PENDIENTES " + gestor.contarTrabajosPendientes());
        lblDisponibles.setText("DISPONIBLES " + gestor.consultarEspaciosDisponibles());
        lblCapacidad.setText("CAPACIDAD " + CAPACIDAD);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Centro de impresion");
            ventana.setContentPane(new InterfazImpresiones().panelPrincipal);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}