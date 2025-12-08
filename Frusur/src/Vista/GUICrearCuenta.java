package Vista;

import Controlador.ControladorFrusur;
import Excepciones.CFException;

import javax.swing.*;
import java.awt.event.*;

public class GUICrearCuenta extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usuarioText;
    private JTextField contrasenaText;
    private JRadioButton agronomoButton;
    private JRadioButton estadisticoButton;

    public GUICrearCuenta() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(agronomoButton);
        buttonGroup.add(estadisticoButton);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try{
            if(agronomoButton.isSelected()){
                if(!validarCampos()){
                    return;
                }
                String usuario = usuarioText.getText();
                String contrasena = contrasenaText.getText();
                ControladorFrusur.getInstance().createCuentaAgronomo(usuario, contrasena);
                JOptionPane.showMessageDialog(this, "Cuenta de Agronomo creada correctamente");
                ControladorFrusur.getInstance().guardar();
            } else if(estadisticoButton.isSelected()){
                if(!validarCampos()){
                    return;
                }
                String usuario = usuarioText.getText();
                String contrasena = contrasenaText.getText();
                ControladorFrusur.getInstance().createCuentaEstadi(usuario, contrasena);
                JOptionPane.showMessageDialog(this, "Cuenta de Estadistico creada correctamente");
                ControladorFrusur.getInstance().guardar();
            }

        }catch (CFException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (usuarioText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (contrasenaText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        GUICrearCuenta dialog = new GUICrearCuenta();
        dialog.pack();
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
    }
}
