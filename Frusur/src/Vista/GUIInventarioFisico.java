package Vista;

import Controlador.ControladorFrusur;
import Modelo.*;

import javax.swing.*;
import java.awt.*;

public class GUIInventarioFisico extends JDialog {

    private JComboBox<TipoBerrie> cmbTipo;
    private JComboBox<ClasificacionProducto> cmbClasif;
    private JTextField txtKilosPorCaja;
    private JTextArea txtSalida;
    private JTextField txtCodigoScan;

    public GUIInventarioFisico() {
        setTitle("Inventario Físico - Stock / Palets / Escaneo");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- DISEÑO DE LA INTERFAZ ---
        JPanel top = new JPanel(new GridLayout(2, 5, 8, 8));
        top.setBorder(BorderFactory.createTitledBorder("Crear Palet"));

        cmbTipo = new JComboBox<>(TipoBerrie.values());
        cmbClasif = new JComboBox<>(ClasificacionProducto.values());
        txtKilosPorCaja = new JTextField("1.0");

        JButton btnVerStock = new JButton("Ver stock");
        JButton btnCrear80 = new JButton("Crear palet 80");
        JButton btnVerPalets = new JButton("Ver palets");

        top.add(new JLabel("Tipo berrie:"));
        top.add(new JLabel("Clasificación:"));
        top.add(new JLabel("Kilos por caja:"));
        top.add(new JLabel(""));
        top.add(new JLabel(""));

        top.add(cmbTipo);
        top.add(cmbClasif);
        top.add(txtKilosPorCaja);
        top.add(btnCrear80);
        top.add(btnVerStock);

        add(top, BorderLayout.NORTH);

        txtSalida = new JTextArea();
        txtSalida.setEditable(false);
        add(new JScrollPane(txtSalida), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBorder(BorderFactory.createTitledBorder("Escaneo (Despacho)"));

        txtCodigoScan = new JTextField(22);
        JButton btnScan = new JButton("Escanear");
        bottom.add(new JLabel("Código:"));
        bottom.add(txtCodigoScan);
        bottom.add(btnScan);
        bottom.add(btnVerPalets);

        JButton btnCerrar = new JButton("Cerrar");
        bottom.add(btnCerrar);

        add(bottom, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnVerStock.addActionListener(e -> verStock());
        btnCrear80.addActionListener(e -> crearPalet80());
        btnVerPalets.addActionListener(e -> verPalets());
        btnScan.addActionListener(e -> escanear());
        btnCerrar.addActionListener(e -> dispose());

        pack();
        setSize(900, 520);
        setLocationRelativeTo(null);

        // --- CARGA AUTOMÁTICA AL INICIAR ---
        // Estas dos líneas leen lo que está en el ControladorFrusur
        // y lo muestran en la tabla/área de texto apenas se abre la GUI.
        verStock();
        verPalets();
    }

    private Inventario inv() {
        return ControladorFrusur.getInstance().getInventario();
    }

    private void verStock() {
        StringBuilder sb = new StringBuilder();
        sb.append("STOCK IQF (kg):\n");
        for (TipoBerrie t : TipoBerrie.values()) {
            sb.append(" - ").append(t).append(": ").append(inv().getStock(t, ClasificacionProducto.IQF)).append("\n");
        }
        sb.append("\nSTOCK SUBPRODUCTO (kg):\n");
        for (TipoBerrie t : TipoBerrie.values()) {
            sb.append(" - ").append(t).append(": ").append(inv().getStock(t, ClasificacionProducto.SUBPRODUCTO)).append("\n");
        }
        sb.append("\nCajas registradas: ").append(inv().getCantidadCajasRegistradas()).append("\n");
        txtSalida.setText(sb.toString());
    }

    private void crearPalet80() {
        try {
            TipoBerrie tipo = (TipoBerrie) cmbTipo.getSelectedItem();
            ClasificacionProducto clasif = (ClasificacionProducto) cmbClasif.getSelectedItem();
            double kilosPorCaja = Double.parseDouble(txtKilosPorCaja.getText().trim().replace(",", "."));

            // Descuenta del stock por berrie y crea el palet de 80 cajas
            Palet p = inv().crearPalet80DesdeStock(tipo, clasif, kilosPorCaja);
            txtSalida.setText("Palet creado:\n" + p + "\n\nPrimeros 5 códigos:\n" +
                    p.obtenerCodigosDelPalet().stream().limit(5).map(c -> " - " + c.getCodigoBarras()).reduce("", (a,b)->a+b+"\n"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verPalets() {
        StringBuilder sb = new StringBuilder();
        sb.append("PALETS EN STOCK:\n");
        for (Palet p : inv().getPalets()) {
            sb.append(" - ").append(p).append("\n");
        }
        txtSalida.setText(sb.toString());
    }

    private void escanear() {
        String codigo = txtCodigoScan.getText().trim();
        // Simula el "pistoleo" de códigos por el departamento de cámara
        String r = inv().escanearCodigoParaDespacho(codigo);
        txtSalida.setText(r);
    }

    public static void display() {
        GUIInventarioFisico d = new GUIInventarioFisico();
        d.setVisible(true);
    }
}
