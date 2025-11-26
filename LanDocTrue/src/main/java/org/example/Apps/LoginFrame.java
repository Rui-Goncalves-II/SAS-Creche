package org.example.Apps;

import org.example.Estetica.Cores.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.example.Estetica.Cores.*;
import static org.example.Estetica.Fontes.*;

class LoginFrame extends JFrame {

    private final JTextField txtUsuario;
    private final JPasswordField txtSenha;
    private final JButton btnEntrar;
    private JDialog loadingDialog;


    public LoginFrame() {
        criarTelaCarregamento();
        setTitle("SAS Creche");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 30, 30),
                        getWidth(), getHeight(), new Color(60, 60, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        painel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("FAÇA SEU LOGIN");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(TEXTO_CLARO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        JLabel lblUsuario = new JLabel("USUÁRIO:");
        lblUsuario.setFont(FONT_LABEL);
        lblUsuario.setForeground(TEXTO_CLARO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        painel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(20);
        estilizarCampo(txtUsuario, new Color(70, 70, 70));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        painel.add(txtUsuario, gbc);

        JLabel lblSenha = new JLabel("SENHA:");
        lblSenha.setFont(FONT_LABEL);
        lblSenha.setForeground(TEXTO_CLARO);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        painel.add(lblSenha, gbc);

        txtSenha = new JPasswordField(20);
        estilizarCampo(txtSenha, new Color(70, 70, 70));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        painel.add(txtSenha, gbc);

        btnEntrar = new BotaoCustom("ENTRAR");
        btnEntrar.setFocusPainted(false);
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFont(FONT_LABEL);
        btnEntrar.setForeground(TEXTO_ESCURO);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 15, 15, 15);
        painel.add(btnEntrar, gbc);

        JLabel lblFooter = new JLabel("© 2025 SAS CRECHE - Todos os direitos reservados");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblFooter.setForeground(new Color(150, 150, 150));
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 15, 0, 15);
        painel.add(lblFooter, gbc);

        //Listeners
        txtUsuario.addActionListener(e -> txtSenha.requestFocusInWindow());

        txtSenha.addActionListener(e -> fazerLogin());

        btnEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                corAnimada(SALMAO_SUAVE, HOVER_SALMAO);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                corAnimada(HOVER_SALMAO, SALMAO_SUAVE);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                fazerLogin();
            }
        });

        setContentPane(painel);
        getRootPane().setDefaultButton(btnEntrar);
    }

    private void criarTelaCarregamento() {
        loadingDialog = new JDialog(this, "");
        loadingDialog.setSize(400, 200);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setResizable(false);
        loadingDialog.setUndecorated(true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setModal(false);

        JPanel panel = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 30, 30),
                        getWidth(), getHeight(), new Color(60, 60, 60)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(VERDE_PRINCIPAL);
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblIcon = new JLabel("⏳", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        lblIcon.setForeground(TEXTO_ESCURO);

        JLabel lblLoading = new JLabel("AUTENTICANDO", SwingConstants.CENTER);
        lblLoading.setForeground(Color.WHITE);
        lblLoading.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.add(lblIcon, BorderLayout.CENTER);
        contentPanel.add(lblLoading, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);
        loadingDialog.add(panel);

    }

    private static class BotaoCustom extends JButton {

        private Color corAtual;

        public BotaoCustom(String text) {
            super(text);
            this.corAtual = SALMAO_SUAVE;
        }

        public void setCurrentColor(Color color) {
            this.corAtual = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(corAtual);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D textBounds = fm.getStringBounds(getText(), g2d);
            int x = (getWidth() - (int) textBounds.getWidth()) / 2;
            int y = (getHeight() - (int) textBounds.getHeight()) / 2 + fm.getAscent();
            g2d.setColor(getForeground());
            g2d.drawString(getText(), x, y);

            g2d.dispose();
        }
    }

    private void corAnimada(Color from, Color to) {
        final int duration = 200;
        final long startTime = System.currentTimeMillis();
        final Timer colorTimer = new Timer(10, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);

            int r = (int) (from.getRed() + progress * (to.getRed() - from.getRed()));
            int g = (int) (from.getGreen() + progress * (to.getGreen() - from.getGreen()));
            int b = (int) (from.getBlue() + progress * (to.getBlue() - from.getBlue()));

            ((BotaoCustom) btnEntrar).setCurrentColor(new Color(r, g, b));

            if (progress >= 1f) {
                ((Timer) e.getSource()).stop();
            }
        });
        colorTimer.start();
    }

    private void fazerLogin() {
        mostrarTelaCarregamento();

        new Thread(() -> {
            boolean sucesso = login();

            SwingUtilities.invokeLater(() -> {
                esconderTelaCarregamento();

                if (sucesso) {
                    AppFrame app = new AppFrame();
                    app.setVisible(true);
                    dispose();
                }
            });
        }).start();

    }

    private boolean login() {

    return true;
    }

    private void estilizarCampo(JTextField campo, Color bg) {
        campo.setBackground(bg);
        campo.setForeground(TEXTO_ESCURO);
        campo.setCaretColor(TEXTO_ESCURO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE_SECUNDARIO, 2, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
    }

    private void mostrarTelaCarregamento() {
        if (loadingDialog == null) {
            criarTelaCarregamento();
        }

        SwingUtilities.invokeLater(() -> {
            loadingDialog.setModal(true);
            loadingDialog.setVisible(true);
        });
    }

    private void esconderTelaCarregamento() {
        SwingUtilities.invokeLater(() -> {
            if (loadingDialog != null && loadingDialog.isVisible()) {
                loadingDialog.setVisible(false);
                loadingDialog.setModal(false);
            }
        });
    }

}
