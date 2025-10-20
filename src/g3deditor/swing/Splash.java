package g3deditor.swing;

import g3deditor.util.Util;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JWindow;

public final class Splash extends JWindow implements Runnable {

    private final long _time;
    private final Runnable _r1;
    private final CheckedRunnable _r2;
    private final Runnable _r3;

    private BufferedImage _image;
    private float _alpha = 0.0f; // Começa transparente

    public interface CheckedRunnable {
        boolean run();
    }

    public Splash(long time, Runnable r1, CheckedRunnable r2, Runnable r3) {
        setAlwaysOnTop(true);
        _time = System.currentTimeMillis() + time;
        _r1 = r1;
        _r2 = r2;
        _r3 = r3;

        _image = Util.loadImage("./data/icon/splash.png");
        if (_image != null) {
            setSize(_image.getWidth(), _image.getHeight());
            setLocationRelativeTo(null);
            setBackground(new Color(0, 0, 0, 0)); // fundo transparente
            setVisible(true);
            new Thread(this).start();
            _r1.run();
        } else {
            _r1.run();
            while (!_r2.run()) {
                try { Thread.sleep(1); } catch (InterruptedException e) {}
            }
            _r3.run();
        }
    }

    @Override
    public void paint(Graphics g) {
        if (_image == null) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _alpha));
        g2d.drawImage(_image, 0, 0, null);
        g2d.dispose();
    }

    @Override
    public void run() {
        try {
            // fade-in suave
            while (_alpha < 1.0f) {
                _alpha += 0.01f; // aumenta mais devagar para ficar visível
                if (_alpha > 1.0f) _alpha = 1.0f;
                repaint();
                Thread.sleep(20);
            }

            while (!_r2.run()) {
                try { Thread.sleep(1); } catch (InterruptedException e) {}
            }

            long sleep = _time - System.currentTimeMillis();
            if (sleep > 0) Thread.sleep(sleep);

            _r3.run();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(0);
        } finally {
            setVisible(false);
            dispose();
        }
    }
}