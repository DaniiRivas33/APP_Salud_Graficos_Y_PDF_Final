/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoAppsalud;

/**
 *
 * @author danie
 */

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImagenEscalada extends JPanel {

    private Image imagen;

   public ImagenEscalada(String ruta) {
    try {
        URL url = getClass().getResource(ruta);
        if (url != null) {
            imagen = new ImageIcon(url).getImage();
        } else if (ruta.startsWith("http")) {
            url = new URL(ruta);
            imagen = new ImageIcon(url).getImage();
        } else {
            imagen = new ImageIcon(ruta).getImage();
        }
    } catch (Exception e) {
        imagen = null;
        
    }
}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
