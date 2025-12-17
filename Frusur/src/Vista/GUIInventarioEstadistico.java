package Vista;

import Modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUIInventarioEstadistico extends JDialog {

    // Estado local de la GUI (tu planilla del turno)
    private Planilla planillaActual;

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

        btnIniciarPlanilla = new JButton("Iniciar planilla");
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

        btnCerrarPlanilla = new JButton("Cerrar planilla (Resumen)");
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
        btnVolver.addActionListener(e -> dispose());
    }

    private void iniciarPlanilla() {
        String linea = txtLinea.getText().trim();
        if (linea.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar la línea de proceso.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        planillaActual = new Planilla();
        planillaActual.setLineaProceso(linea);

        modelTarjas.setRowCount(0);
        setFormEnabled(true);

        JOptionPane.showMessageDialog(this, "Planilla iniciada en: " + linea);
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

        try {
            // intenta descontar del stock global del controlador
            Controlador.ControladorFrusur.getInstance().consumirMateriaPrima(kilos);
        } catch (Exception ex) {
            // si falla (no hay stock), muestra el error y no crea la tarja
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TipoBerrie tipo = (TipoBerrie) cmbTipo.getSelectedItem();
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

        ResumenProduccion resumen = planillaActual.generarResumen();
        try {
            Controlador.ControladorFrusur.getInstance().registrarProduccion(resumen);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Mensaje resumen (simple y claro)
        StringBuilder sb = new StringBuilder();
        sb.append("Línea: ").append(resumen.getLineaProceso()).append("\n\n");
        sb.append("Total IQF: ").append(resumen.getTotalIQF()).append(" kg\n");
        sb.append("Total Subproducto: ").append(resumen.getTotalSubproducto()).append(" kg\n");
        sb.append("TOTAL GENERAL: ").append(resumen.getTotalGeneral()).append(" kg\n\n");

        sb.append("Detalle por tipo (IQF):\n");
        for (TipoBerrie t : TipoBerrie.values()) {
            sb.append(" - ").append(t.name()).append(": ").append(resumen.getKilosIQF().get(t)).append(" kg\n");
        }

        sb.append("\nDetalle por tipo (Subproducto):\n");
        for (TipoBerrie t : TipoBerrie.values()) {
            sb.append(" - ").append(t.name()).append(": ").append(resumen.getKilosSubproducto().get(t)).append(" kg\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Resumen de Producción", JOptionPane.INFORMATION_MESSAGE);

        // Aquí es donde se lo pasarías al inventario de tu compañero:
        // inventario.agregarProduccion(resumen);

        // opcional: dejar lista para un nuevo turno
        // planillaActual = null; setFormEnabled(false);
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
