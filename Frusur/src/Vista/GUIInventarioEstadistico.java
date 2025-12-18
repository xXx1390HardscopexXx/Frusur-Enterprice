package Vista;

import Controlador.ControladorFrusur;
import Excepciones.CFException;
import Modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUIInventarioEstadistico extends JDialog {

    private Planilla planillaActual;
    private JButton btnInventarioFisico;


    // UI
    private JTextField txtLinea;
    private JComboBox<TipoBerrie> cmbTipo;
    private JComboBox<ClasificacionProducto> cmbClasif;
    private JTextField txtKilos;
    private JTextField txtDetalle;

    private JButton btnIniciarPlanilla;
    private JButton btnAgregarTarja;
    private JButton btnCerrarPlanilla;
    private JButton btnVolver;

    private JTable tablaTarjas;
    private DefaultTableModel modelTarjas;

    public GUIInventarioEstadistico() {
        setTitle("Estadístico - Inventario (Planilla y Tarjas)");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUI();
        wireEvents();

        pack();
        setSize(900, 520);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        // ------------------ TOP: Planilla (línea) ------------------
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setBorder(BorderFactory.createTitledBorder("Planilla del turno"));

        JPanel panelLinea = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLinea.add(new JLabel("Línea de proceso:"));
        txtLinea = new JTextField(25);
        panelLinea.add(txtLinea);

        btnIniciarPlanilla = new JButton("Iniciar/Recuperar planilla");
        panelLinea.add(btnIniciarPlanilla);

        panelTop.add(panelLinea, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);

        // ------------------ CENTER: Form Tarja + Tabla ------------------
        JPanel panelCenter = new JPanel(new BorderLayout(10, 10));

        // Form Tarja
        JPanel panelForm = new JPanel(new GridLayout(2, 6, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar tarja"));

        cmbTipo = new JComboBox<>(TipoBerrie.values());
        cmbClasif = new JComboBox<>(ClasificacionProducto.values());
        txtKilos = new JTextField();
        txtDetalle = new JTextField();

        panelForm.add(new JLabel("Tipo berrie:"));
        panelForm.add(new JLabel("Clasificación:"));
        panelForm.add(new JLabel("Kilos:"));
        panelForm.add(new JLabel("Detalle (opcional):"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));

        panelForm.add(cmbTipo);
        panelForm.add(cmbClasif);
        panelForm.add(txtKilos);
        panelForm.add(txtDetalle);

        btnAgregarTarja = new JButton("Agregar tarja");
        panelForm.add(btnAgregarTarja);

        btnCerrarPlanilla = new JButton("Finalizar Proceso");
        panelForm.add(btnCerrarPlanilla);

        panelCenter.add(panelForm, BorderLayout.NORTH);

        // Tabla
        String[] cols = {"Tipo berrie", "Clasificación", "Kilos", "Detalle"};
        modelTarjas = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaTarjas = new JTable(modelTarjas);

        JScrollPane scroll = new JScrollPane(tablaTarjas);
        scroll.setBorder(BorderFactory.createTitledBorder("Tarjas registradas"));
        panelCenter.add(scroll, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);

        // ------------------ BOTTOM: Volver ------------------
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnInventarioFisico = new JButton("Inventario Físico");
        panelBottom.add(btnInventarioFisico);

        btnVolver = new JButton("Volver");
        panelBottom.add(btnVolver);

        add(panelBottom, BorderLayout.SOUTH);



        // Estado inicial
        setFormEnabled(false);
    }

    private void wireEvents() {
        btnIniciarPlanilla.addActionListener(e -> iniciarPlanilla());
        btnAgregarTarja.addActionListener(e -> agregarTarja());
        btnCerrarPlanilla.addActionListener(e -> cerrarPlanilla());
        btnInventarioFisico.addActionListener(e -> GUIInventarioFisico.display());
        btnVolver.addActionListener(e -> dispose());
    }


    private void iniciarPlanilla() {
        String linea = txtLinea.getText().trim();
        if (linea.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar la línea de proceso.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        planillaActual = ControladorFrusur.getInstance().iniciarORecuperarPlanilla(linea);

        // Limpiar y cargar datos existentes en la tabla visual
        modelTarjas.setRowCount(0);
        for (Tarja t : planillaActual.getTarjas()) {
            modelTarjas.addRow(new Object[]{
                    t.getTipoBerrie(),
                    t.getClasificacion(),
                    t.getKilos(),
                    (t.getDetalle() == null || t.getDetalle().isEmpty()) ? "-" : t.getDetalle()
            });
        }

        setFormEnabled(true);
        JOptionPane.showMessageDialog(this, "Planilla activa para: " + linea);
    }

    private void agregarTarja() {
        if (planillaActual == null) {
            JOptionPane.showMessageDialog(this, "Primero inicia la planilla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double kilos;
        try {
            kilos = Double.parseDouble(txtKilos.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kilos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (kilos <= 0) {
            JOptionPane.showMessageDialog(this, "Los kilos deben ser > 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoBerrie tipo = (TipoBerrie) cmbTipo.getSelectedItem();

        try {
            ControladorFrusur.getInstance().consumirMateriaPrimaPorTipo(tipo, kilos);
        } catch (CFException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ClasificacionProducto clasif = (ClasificacionProducto) cmbClasif.getSelectedItem();
        String detalle = txtDetalle.getText().trim();

        Tarja t = new Tarja(tipo, clasif, kilos, detalle);
        planillaActual.agregarTarja(t);

        modelTarjas.addRow(new Object[]{
                tipo.name(),
                clasif.name(),
                kilos,
                detalle.isEmpty() ? "-" : detalle
        });

        txtKilos.setText("");
        txtDetalle.setText("");
        txtKilos.requestFocus();
    }

    private void cerrarPlanilla() {
        if (planillaActual == null) {
            JOptionPane.showMessageDialog(this, "No hay planilla activa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (planillaActual.getTarjas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tarjas registradas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ResumenProduccion resumen = planillaActual.generarResumen();
            ControladorFrusur.getInstance().finalizarPlanillaActual(resumen);

            // Mostrar Resumen
            StringBuilder sb = new StringBuilder();
            sb.append("Línea: ").append(resumen.getLineaProceso()).append("\n\n");
            sb.append("TOTAL GENERAL: ").append(resumen.getTotalGeneral()).append(" kg\n");
            sb.append("Total IQF: ").append(resumen.getTotalIQF()).append(" kg\n");
            sb.append("Total Subproducto: ").append(resumen.getTotalSubproducto()).append(" kg\n");

            JOptionPane.showMessageDialog(this, sb.toString(), "Resumen de Producción Guardado", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar Interfaz
            planillaActual = null;
            modelTarjas.setRowCount(0);
            setFormEnabled(false);
            txtLinea.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFormEnabled(boolean enabled) {
        cmbTipo.setEnabled(enabled);
        cmbClasif.setEnabled(enabled);
        txtKilos.setEnabled(enabled);
        txtDetalle.setEnabled(enabled);
        btnAgregarTarja.setEnabled(enabled);
        btnCerrarPlanilla.setEnabled(enabled);
    }

    public static void display() {
        GUIInventarioEstadistico dialog = new GUIInventarioEstadistico();
        dialog.setVisible(true);
    }
}
