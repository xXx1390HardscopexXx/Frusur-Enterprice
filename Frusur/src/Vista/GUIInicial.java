package Vista;

import Controlador.ControladorFrusur;
import Excepciones.CFException;

import javax.swing.*;
import java.awt.event.*;

public class GUIInicial extends JDialog {
    private JPanel contentPane;
    private JButton AgronomoButton;
    private JButton buttonCancel;
    private JButton EstadisticoButton;
    private JLabel titulo;
    private JButton crearCuentaButton;
    private JButton guardarButton;

    public GUIInicial() {
        try{
            ControladorFrusur.getInstance().cargar();
        }catch (CFException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        setTitle("Sistema de Gestión Frusur");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(AgronomoButton);

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

        AgronomoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUISesionAgro.display();
            }
        });
        EstadisticoButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               GUISesionEstadi.display();
           }
        });
        crearCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICrearCuenta.display();
            }
        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    ControladorFrusur.getInstance().guardar();
                    JOptionPane.showMessageDialog(null, "Se ha guardado con exito");
                }catch(CFException ex){
                    JOptionPane.showMessageDialog(GUIInicial.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        GUIInicial dialog = new GUIInicial();
        dialog.pack();
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
    }
}
