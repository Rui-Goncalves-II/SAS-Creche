package org.example.Apps;

import org.example.Aluno.Aluno;
import org.example.Aluno.AlunoDAO;
import org.example.BancoDados.Banco;
import org.example.Responsavel.Responsavel;
import org.example.Responsavel.ResponsavelDAO;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import static org.example.Estetica.Cores.*;
import static org.example.Estetica.Fontes.*;

public class AppFrame extends JFrame {

    // variáveis gerais
    private JButton btnDeslogar, btnRecarregar;
    public JTextField txtNomeResposavel, txtEmail, txtAuxilio, txtRestricaoAlimentar, txtProblemasSaude, txtEndereco,
            txtNumCasa, txtPontoReferencia, txtBairro, txtEmprego, txtLocalEmprego, txtNomeAluno;
    JFormattedTextField txtCpfResponsavel, txtTel, txtTel2, txtRGResponsavel, txtRGAluno, txtDataNascimentoAluno,
            txtDataNascimentoResponsavel, txtCpfAluno, txtSUS;
    JComboBox<String> cbEtnia, cbRenda, cbParentesco, cbMunicipio, cbUF;
    DefaultComboBoxModel<String> comboBoxModelAlunos;
    ComboBoxDinamico cbDeficiencias, cbAlergias, cbAlunos;
    JCheckBox chkGemeos, chkSexoMAluno, chkSexoFAluno, chkSexoMResponsavel, chkSexoFResponsavel,
            chkMobilidadeT, chkMobilidadeP, chkResponsavelLegal;
    Aluno alunoParente;

    public static JTable tabelaAlunos, tabelaResponsaveis;
    public static DefaultTableModel modeloAlunos, modeloResponsaveis;


    public AppFrame() {
        Banco.criarTabelas();
        carregarIcone();
        setTitle("Creche Estrela");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension tamanhoMinimo = new Dimension(1200, 800);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setMinimumSize(tamanhoMinimo);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BEGE_CLARO);
        Deslogar();

        JTabbedPane mainTabs = criarTabbedPanePrincipal();
        add(mainTabs);

    }

    private void carregarIcone() {
        try {
            URL url = getClass().getResource("/icones/logo-creche.png");
            if (url == null) {
                throw new FileNotFoundException("Ícone não encontrado: logo-creche.png");
            }

            Image icone = Toolkit.getDefaultToolkit().getImage(url);
            setIconImage(icone);
        } catch (Exception e) {
            System.out.println("Erro ao carregar ícone: " + e.getMessage());

        }
    }

    public void Deslogar() {
        ImageIcon imgDeslogar = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icones/saida.png")));
        btnDeslogar = criarBotaoEstilizado("Logout", imgDeslogar);
        btnDeslogar.setBounds(getWidth() - 120, 10, 110, 30);
        add(btnDeslogar);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                btnDeslogar.setBounds(getWidth() - 120, 10, 110, 30);
            }
        });

        btnDeslogar.addActionListener(ae -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            dispose();
        });

    }

    private JTabbedPane criarTabbedPanePrincipal() {
        JTabbedPane guia = new JTabbedPane();
        guia.setFont(FONT_LABEL);
        guia.setBackground(BEGE_CLARO);
        guia.setForeground(TEXTO_ESCURO);
        guia.setUI(new CustomTabbedPaneUI(AMARELO_DOURADO, TEXTO_ESCURO));

        guia.addTab("Alunos", criarPainelPessoas("Alunos"));
        guia.addTab("Responsáveis", criarPainelPessoas("Responsáveis"));
        //guia.addTab("Funcionários", criarPainelPessoas("Funcionários"));
        //guia.addTab("Documentos", criarPainelDocumentos());
        //guia.addTab("Estoque", criarPainelEstoque());


        return guia;
    }

    private JPanel criarPainelPessoas(String tipoPessoa) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BEGE_CLARO);

        JTabbedPane subguia = new JTabbedPane();
        subguia.setFont(FONT_LABEL);
        subguia.setBackground(BEGE_CLARO);
        subguia.setForeground(TEXTO_ESCURO);
        subguia.setUI(new CustomTabbedPaneUI(AMARELO_DOURADO, TEXTO_ESCURO));


        switch (tipoPessoa) {
            case ("Alunos") -> subguia.addTab("Adicionar Aluno", criarPainelCriarAluno());
            case ("Responsáveis") -> subguia.addTab("Adicionar Responsável", criarPainelCriarResponsavel());
        }
        subguia.addTab("Lista de " + tipoPessoa, criarPainelTabelaComBotoes(tipoPessoa));


        painel.add(subguia);

        return painel;
    }

    private JPanel criarPainelCriarResponsavel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BEGE_CLARO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE_SECUNDARIO, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        setLayout(new BorderLayout());
        setBackground(BEGE_CLARO);

        // Título
        JLabel lblTitulo = new JLabel("Cadastro de Responsáveis");
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(TEXTO_ESCURO);
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        painel.add(lblTitulo, BorderLayout.NORTH);

        // Painel central com os campos
        JPanel pnlCampos = new JPanel();
        pnlCampos.setLayout(new GridBagLayout());
        pnlCampos.setBackground(BEGE_CLARO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(16, 16, 16, 16);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuração dos campos
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlCampos.add(criarLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNomeResposavel = new JTextField();
        estilizarTextField(txtNomeResposavel, null);
        pnlCampos.add(txtNomeResposavel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("CPF:"), gbc);
        gbc.gridx = 1;
        txtCpfResponsavel = new JFormattedTextField();
        estilizarTextField(txtCpfResponsavel, null);
        try {
            MaskFormatter mask = new MaskFormatter("###.###.###-##");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.setValueContainsLiteralCharacters(false);
            mask.install(txtCpfResponsavel);
            txtCpfResponsavel.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });

        } catch (ParseException e) {
            System.out.println("Erro ao configurar CPF: " + e);
        }

        pnlCampos.add(txtCpfResponsavel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("RG:"), gbc);
        gbc.gridx = 1;
        txtRGResponsavel = new JFormattedTextField();
        estilizarTextField(txtRGResponsavel, null);
        try {
            MaskFormatter mask = new MaskFormatter("##########-#");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.setValueContainsLiteralCharacters(false);
            mask.install(txtRGResponsavel);
            txtRGResponsavel.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });

        } catch (ParseException e) {
            System.out.println("Erro ao configurar RG: " + e);
        }

        pnlCampos.add(txtRGResponsavel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        chkSexoMResponsavel = new JCheckBox("Masculino");
        chkSexoFResponsavel = new JCheckBox("Feminino");
        estilizarCheckBox(chkSexoMResponsavel);
        estilizarCheckBox(chkSexoFResponsavel);

        pnlCampos.add(chkSexoMResponsavel, gbc);
        gbc.gridx = 2;
        pnlCampos.add(chkSexoFResponsavel, gbc);

        chkSexoMResponsavel.addActionListener(e -> {
            if (chkSexoMResponsavel.isSelected()) {
                chkSexoFResponsavel.setSelected(false);
                chkSexoFResponsavel.setForeground(TEXTO_CLARO);
            }
        });

        chkSexoFResponsavel.addActionListener(e -> {
            if (chkSexoFResponsavel.isSelected()) {
                chkSexoMResponsavel.setSelected(false);
                chkSexoMResponsavel.setForeground(TEXTO_CLARO);
            }
        });

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Data de Nacimento:"), gbc);
        gbc.gridx = 1;
        txtDataNascimentoResponsavel = new JFormattedTextField();
        estilizarTextField(txtDataNascimentoResponsavel, null);

        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.install(txtDataNascimentoResponsavel);
            txtDataNascimentoResponsavel.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });
        } catch (ParseException e) {
            System.out.println("Erro ao configurar Data de Nascimento: " + e);
        }

        pnlCampos.add(txtDataNascimentoResponsavel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Etnia: "), gbc);
        gbc.gridx = 1;
        String[] Etnias = {"Branco", "Pardo", "Negro", "Indígena"};
        cbEtnia = new JComboBox<>(Etnias);
        estilizarComboBox(cbEtnia);
        pnlCampos.add(cbEtnia, gbc);

        // Painel de botões
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBotoes.setBackground(BEGE_CLARO);

        ImageIcon iconeSalvar = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icones/salvar.png")));

        JButton btnSalvar = criarBotaoEstilizado("Salvar", iconeSalvar);
        JButton btnCancelar = criarBotaoEstilizado("Limpar", null);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Email: "), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField();
        estilizarTextField(txtEmail, null);
        pnlCampos.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Celular: "), gbc);
        gbc.gridx = 1;
        txtTel = new JFormattedTextField();
        estilizarTextField(txtTel, null);

        try {
            MaskFormatter mask = new MaskFormatter("(##) # ####-####");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.install(txtTel);
            txtTel.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });

        } catch (ParseException e) {
            System.out.println("Erro ao configurar Telefone: " + e);
        }
        pnlCampos.add(txtTel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Celular 2: "), gbc);
        gbc.gridx = 1;
        txtTel2 = new JFormattedTextField();
        estilizarTextField(txtTel2, null);

        try {
            MaskFormatter mask = new MaskFormatter("(##) # ####-####");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.install(txtTel2);
            txtTel2.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });

        } catch (ParseException e) {
            System.out.println("Erro ao configurar Telefone: " + e);
        }
        pnlCampos.add(txtTel2, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Parentesco: "), gbc);
        gbc.gridx = 1;
        String[] parentescos = {"Pai", "Mãe", "Avô", "Avó", "Tio", "Tia", "Responsável Legal"};
        cbParentesco = new JComboBox<>(parentescos);
        estilizarComboBox(cbParentesco);
        pnlCampos.add(cbParentesco, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("É o Responsável Legal?: "), gbc);
        gbc.gridx = 1;
        chkResponsavelLegal = new JCheckBox();
        estilizarCheckBox(chkResponsavelLegal);

        pnlCampos.add(chkResponsavelLegal, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Renda: "), gbc);
        gbc.gridx = 1;
        String[] rendas = {"Até 1 salário mínimo", "1-2 salários mínimos",
                "2-3 salários mínimos", "3-5 salários mínimos",
                "Acima de 5 salários mínimos"};
        cbRenda = new JComboBox<>(rendas);
        estilizarComboBox(cbRenda);
        pnlCampos.add(cbRenda, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Trabalho: "), gbc);
        gbc.gridx = 1;
        txtEmprego = new JTextField();
        estilizarTextField(txtEmprego, null);
        pnlCampos.add(txtEmprego, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Local de Trabalho: "), gbc);
        gbc.gridx = 1;
        txtLocalEmprego = new JTextField();
        estilizarTextField(txtLocalEmprego, null);
        pnlCampos.add(txtLocalEmprego, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Auxilio Governamental (se houver): "), gbc);
        gbc.gridx = 1;
        txtAuxilio = new JTextField();
        estilizarTextField(txtAuxilio, null);
        pnlCampos.add(txtAuxilio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Parente do(s) Aluno(s): "), gbc);
        gbc.gridx = 1;
        cbAlunos = new ComboBoxDinamico("Alunos");
        pnlCampos.add(cbAlunos, gbc);


        btnSalvar.addActionListener(ae -> {
            Banco.criarTabelas();
            List<String> nomesAlunosSelecionados = cbAlunos.getSelecoes();
            List<Aluno> dependentes = new ArrayList<>();

            ResponsavelDAO responsavelDAO = new ResponsavelDAO();
            Responsavel responsavel = new Responsavel();
            responsavel.setNome(txtNomeResposavel.getText());
            responsavel.setCpf(txtCpfResponsavel.getText());
            responsavel.setRg(txtRGResponsavel.getText());
            if (chkSexoFResponsavel.isSelected()) {
                responsavel.setSexo("Feminino");
            } else {
                responsavel.setSexo("Masculino");
            }
            responsavel.setDataNascimento(txtDataNascimentoResponsavel.getText());
            responsavel.setEtnia(cbEtnia.getItemAt(cbEtnia.getSelectedIndex()));
            responsavel.setEmail(txtEmail.getText());
            responsavel.setContato(txtTel.getText());
            responsavel.setContato2(txtTel2.getText());
            responsavel.setParentesco(cbParentesco.getItemAt(cbParentesco.getSelectedIndex()));
            responsavel.setResponsavelLegal(chkResponsavelLegal.isSelected());
            responsavel.setRendaBruta(cbRenda.getItemAt(cbRenda.getSelectedIndex()));
            responsavel.setAuxilioGoverno(txtAuxilio.getText());
            responsavel.setEmprego(txtEmprego.getText());
            responsavel.setLocalTrabalho(txtLocalEmprego.getText());

            try {
                for (String nome: nomesAlunosSelecionados){
                    System.out.println("procurando aluno com nome: " + nome);
                    alunoParente = AlunoDAO.buscarPorNome(nome);
                    System.out.println("Aluno encontrado com nome: " + nome + " : " + alunoParente);
                    dependentes.add(alunoParente);
                }
                responsavel.setAlunos(dependentes);
                responsavelDAO.salvarResponsavel(responsavel);
                System.out.println(dependentes);
                for (Aluno alunoAtual: dependentes){
                    alunoAtual.setIdResponsavel(responsavel.getIdResponsavel());
                    AlunoDAO.atualizarAluno(alunoParente);
                }
                JOptionPane.showMessageDialog(this, "Responsável Criado Com Sucesso!");
                limparCamposResponsavel();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao Salvar Responsável: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        });

        btnCancelar.addActionListener(ae -> {
            limparCamposResponsavel();

        });

        JScrollPane scrollPrincipal = new JScrollPane(pnlCampos);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.getVerticalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));
        scrollPrincipal.getHorizontalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));

        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getViewport().setBackground(BEGE_CLARO);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(25);
        scrollPrincipal.getVerticalScrollBar().setBlockIncrement(150);

        scrollPrincipal.setPreferredSize(new Dimension(600, 400));

        pnlCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painel.add(scrollPrincipal, BorderLayout.CENTER);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnCancelar);
        painel.add(pnlBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelCriarAluno() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BEGE_CLARO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE_SECUNDARIO, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        setLayout(new BorderLayout());
        setBackground(BEGE_CLARO);

        // Título
        JLabel lblTitulo = new JLabel("Cadastro de Alunos");
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(TEXTO_ESCURO);
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        painel.add(lblTitulo, BorderLayout.NORTH);

        // Painel central com os campos
        JPanel pnlCampos = new JPanel();
        pnlCampos.setLayout(new GridBagLayout());
        pnlCampos.setBackground(BEGE_CLARO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(16, 16, 16, 16);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuração dos campos
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlCampos.add(criarLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNomeAluno = new JTextField();
        estilizarTextField(txtNomeAluno, null);
        pnlCampos.add(txtNomeAluno, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("CPF:"), gbc);
        gbc.gridx = 1;
        txtCpfAluno = new JFormattedTextField();
        estilizarTextField(txtCpfAluno, null);
        try {
            MaskFormatter mask = new MaskFormatter("###.###.###-##");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.setValueContainsLiteralCharacters(false);
            mask.install(txtCpfAluno);
            txtCpfAluno.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });

        } catch (ParseException e) {
            System.out.println("Erro ao configurar CPF: " + e);
        }

        pnlCampos.add(txtCpfAluno, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("RG:"), gbc);
        gbc.gridx = 1;
        txtRGAluno = new JFormattedTextField();
        estilizarTextField(txtRGAluno, null);
        try {
            MaskFormatter mask = new MaskFormatter("##########-#");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.setValueContainsLiteralCharacters(false);
            mask.install(txtRGAluno);
            txtRGAluno.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });

        } catch (ParseException e) {
            System.out.println("Erro ao configurar RG: " + e);
        }

        pnlCampos.add(txtRGAluno, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        chkSexoMAluno = new JCheckBox("Masculino");
        chkSexoFAluno = new JCheckBox("Feminino");
        estilizarCheckBox(chkSexoMAluno);
        estilizarCheckBox(chkSexoFAluno);

        pnlCampos.add(chkSexoMAluno, gbc);
        gbc.gridx = 2;
        pnlCampos.add(chkSexoFAluno, gbc);

        chkSexoMAluno.addActionListener(e -> {
            if (chkSexoMAluno.isSelected()) {
                chkSexoFAluno.setSelected(false);
                chkSexoFAluno.setForeground(TEXTO_CLARO);
            }
        });

        chkSexoFAluno.addActionListener(e -> {
            if (chkSexoFAluno.isSelected()) {
                chkSexoMAluno.setSelected(false);
                chkSexoMAluno.setForeground(TEXTO_CLARO);
            }
        });

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Data de Nacimento:"), gbc);
        gbc.gridx = 1;
        txtDataNascimentoAluno = new JFormattedTextField();
        estilizarTextField(txtDataNascimentoAluno, null);

        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.install(txtDataNascimentoAluno);
            txtDataNascimentoAluno.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });
        } catch (ParseException e) {
            System.out.println("Erro ao configurar Data de Nascimento: " + e);
        }

        pnlCampos.add(txtDataNascimentoAluno, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Etnia: "), gbc);
        gbc.gridx = 1;
        String[] Etnias = {"Branco", "Pardo", "Negro", "Indígena"};
        cbEtnia = new JComboBox<>(Etnias);
        estilizarComboBox(cbEtnia);
        pnlCampos.add(cbEtnia, gbc);

        // Painel de botões
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBotoes.setBackground(BEGE_CLARO);

        ImageIcon iconeSalvar = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icones/salvar.png")));

        JButton btnSalvar = criarBotaoEstilizado("Salvar", iconeSalvar);
        JButton btnCancelar = criarBotaoEstilizado("Limpar", null);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Endereço do Aluno: "), gbc);
        gbc.gridx = 1;
        txtEndereco = new JTextField();
        estilizarTextField(txtEndereco, null);
        pnlCampos.add(txtEndereco, gbc);
        gbc.gridx = 2;
        pnlCampos.add(criarLabel("Nº: "), gbc);
        gbc.gridx = 3;
        txtNumCasa = new JTextField();
        estilizarTextField(txtNumCasa, new Dimension(100, 40));
        pnlCampos.add(txtNumCasa, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Ponto de referência: "), gbc);
        gbc.gridx = 1;
        txtPontoReferencia = new JTextField();
        estilizarTextField(txtPontoReferencia, null);
        pnlCampos.add(txtPontoReferencia, gbc);
        gbc.gridx = 2;
        pnlCampos.add(criarLabel("Bairro: "), gbc);
        gbc.gridx = 3;
        txtBairro = new JTextField();
        estilizarTextField(txtBairro, new Dimension(200, 40));
        pnlCampos.add(txtBairro, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("UF: "), gbc);
        gbc.gridx = 1;
        String[] estados = {"AC", "AL", "AP", "AM", "BA", "CE",
                "DF", "ES", "GO", "MA",
                "MT", "MS", "MG", "PA",
                "PB", "PR", "PE", "PI", "RJ",
                "RN", "RS", "RO",
                "RR", "SC", "SP", "SE", "TO"};
        cbUF = new JComboBox<>(estados);
        cbMunicipio = new JComboBox<>();
        estilizarComboBox(cbUF);
        estilizarComboBox(cbMunicipio);
        cbUF.setSelectedIndex(9);
        configurarComboBox(cbUF, cbMunicipio);
        cbUF.addActionListener(_ -> {
            configurarComboBox(cbUF, cbMunicipio);
        });
        pnlCampos.add(cbUF, gbc);

        gbc.gridx = 2;
        pnlCampos.add(criarLabel("Município: "), gbc);
        gbc.gridx = 3;
        pnlCampos.add(cbMunicipio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Cadastro Nacional de Saúde (SUS): "), gbc);
        gbc.gridx = 1;
        txtSUS = new JFormattedTextField();
        estilizarTextField(txtSUS, null);
        try{
            MaskFormatter mask = new MaskFormatter("### #### #### ####");
            mask.setPlaceholderCharacter('_');
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setCommitsOnValidEdit(true);
            mask.setValueContainsLiteralCharacters(false);
            mask.install(txtSUS);
            txtSUS.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JFormattedTextField field = (JFormattedTextField) e.getSource();
                    String texto = field.getText();
                    int firstPlaceHolder = texto.indexOf('_');
                    if (firstPlaceHolder == -1) {
                        field.setCaretPosition(texto.length());
                    } else {
                        field.setCaretPosition(firstPlaceHolder);
                    }
                }
            });

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        pnlCampos.add(txtSUS, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Deficiências (se houver): "), gbc);
        gbc.gridx = 1;

        cbDeficiencias = new ComboBoxDinamico("Deficiencias");
        pnlCampos.add(cbDeficiencias, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(16, 16, 16, 16);
        pnlCampos.add(criarLabel("Alergias (se houver): "), gbc);
        gbc.gridx = 1;

        cbAlergias = new ComboBoxDinamico("Alergias");
        pnlCampos.add(cbAlergias, gbc);

        gbc.insets = new Insets(16, 16, 16, 16);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Mobilidade reduzida?"), gbc);

        chkMobilidadeT = new JCheckBox("Temporária");
        chkMobilidadeP = new JCheckBox("Permanente");
        estilizarCheckBox(chkMobilidadeT);
        estilizarCheckBox(chkMobilidadeP);

        chkMobilidadeT.addActionListener(e -> {
            if (chkMobilidadeT.isSelected()) {
                chkMobilidadeP.setSelected(false);
                chkMobilidadeP.setForeground(TEXTO_CLARO);
            }
        });

        chkMobilidadeP.addActionListener(_ -> {
            if (chkMobilidadeP.isSelected()) {
                chkMobilidadeT.setSelected(false);
                chkMobilidadeT.setForeground(TEXTO_CLARO);
            }
        });

        gbc.gridx = 1;
        pnlCampos.add(chkMobilidadeT, gbc);
        gbc.gridx = 2;
        pnlCampos.add(chkMobilidadeP, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        pnlCampos.add(criarLabel("Problemas de Saúde (se houver): "), gbc);
        gbc.gridx = 1;
        txtProblemasSaude = new JTextField();
        estilizarTextField(txtProblemasSaude, null);
        pnlCampos.add(txtProblemasSaude, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        pnlCampos.add(criarLabel("Restrição Alimentar (se houver): "), gbc);
        txtRestricaoAlimentar = new JTextField();
        estilizarTextField(txtRestricaoAlimentar, null);
        gbc.gridx = 1;
        pnlCampos.add(txtRestricaoAlimentar, gbc);


        btnSalvar.addActionListener(_ -> {
            try {
                Aluno aluno = new Aluno();
                aluno.setNome(txtNomeAluno.getText());
                aluno.setCpf(txtCpfAluno.getText());
                aluno.setRg(txtRGAluno.getText());
                if (chkSexoFAluno.isSelected()) {
                    aluno.setSexo("Feminino");
                } else {
                    aluno.setSexo("Masculino");
                }
                aluno.setDataNascimento(txtDataNascimentoAluno.getText());
                aluno.setEtnia(Objects.requireNonNull(cbEtnia.getSelectedItem()).toString());
                aluno.setEndereco(txtEndereco.getText());
                aluno.setNumCasa(txtNumCasa.getText());
                aluno.setPontoReferencia(txtPontoReferencia.getText());
                aluno.setBairro(txtBairro.getText());
                aluno.setUf(cbUF.getItemAt(cbUF.getSelectedIndex()));
                aluno.setMunicipio(cbMunicipio.getItemAt(cbMunicipio.getSelectedIndex()));
                aluno.setSus(txtSUS.getText());
                aluno.setDeficiencias(cbDeficiencias.getSelecoes());
                aluno.setAlergias(cbAlergias.getSelecoes());
                if (!chkMobilidadeT.isSelected() && !chkMobilidadeP.isSelected()) {
                    aluno.setMobilidade("Nenhuma deficiência de Mobilidade");
                } else {
                    if (chkMobilidadeT.isSelected()) {
                        aluno.setMobilidade("Mobilidade reduzida Temporária");
                    } else {
                        aluno.setMobilidade("Mobilidade reduzida Permanente");
                    }
                }
                aluno.setProblemaSaude(txtProblemasSaude.getText());
                aluno.setRestricaoAlimentar(txtRestricaoAlimentar.getText());

                AlunoDAO alunoDAO = new AlunoDAO();
                alunoDAO.salvarAluno(aluno);

                JOptionPane.showMessageDialog(this, "Aluno Criado Com Sucesso!");
                limparCamposAluno();
                cbAlunos = new ComboBoxDinamico("Alunos");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(_ -> {
            limparCamposAluno();
        });


        JScrollPane scrollPrincipal = new JScrollPane(pnlCampos);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.getVerticalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));
        scrollPrincipal.getHorizontalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getViewport().setBackground(BEGE_CLARO);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(25);
        scrollPrincipal.getVerticalScrollBar().setBlockIncrement(150);

        scrollPrincipal.setPreferredSize(new Dimension(600, 400));

        pnlCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painel.add(scrollPrincipal, BorderLayout.CENTER);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnCancelar);
        painel.add(pnlBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelTabelaComBotoes(String categoria) {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(BEGE_CLARO);

        JLabel label = new JLabel("Lista de " + categoria);
        label.setFont(FONT_TITLE);
        label.setForeground(VERDE_PRINCIPAL);
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.add(label, BorderLayout.NORTH);

        DefaultTableModel modeloTabela = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        JTable tabela = new JTable(modeloTabela);

        configurarTabela(tabela);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(VERDE_PRINCIPAL);
        header.setForeground(TEXTO_ESCURO);
        header.setFont(FONT_TITLE.deriveFont(Font.BOLD));

        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(tabela,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));
        painel.add(scrollPane, BorderLayout.CENTER);

        switch (label.getText()) {
            case ("Lista de Alunos") -> {
                modeloTabela.setColumnIdentifiers(new String[]{"ID", "NOME", "CPF", "SEXO",
                        "DATA NASCIMENTO", "DEFICIÊNCIAS", "ALERGIAS", "PROBLEMAS DE SAÚDE",
                        "RESTRIÇÃO ALIMENTAR", "ENDEREÇO", "BAIRRO", "RESPONSÁVEL", "IDB"});
                sorter.setComparator(0, Comparator.comparingLong(o -> Long.parseLong(o.toString())));
                sorter.setComparator(5, Comparator.comparingLong(o -> Long.parseLong(o.toString())));
                tabelaAlunos = tabela;
                modeloAlunos = modeloTabela;

                pegarValores("Alunos");
                configurarLargurasColunasAlunos(tabela);
            }

            case ("Lista de Responsáveis") -> {
                modeloTabela.setColumnIdentifiers(new String[]{"ID", "NOME", "CPF", "SEXO", "DATA NASCIMENTO",
                        "EMAIL", "CONTATO", "PARENTESCO", "RENDA", "EMPREGO", "ALUNOS", "IDB"});
                sorter.setComparator(0, Comparator.comparingLong(o -> Long.parseLong(o.toString())));
                sorter.setComparator(5, Comparator.comparingLong(o -> Long.parseLong(o.toString())));
                tabelaResponsaveis = tabela;
                modeloResponsaveis = modeloTabela;

                pegarValores("Responsáveis");
                configurarLargurasColunasResponsaveis(tabela);
            }

        }

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        painelBotoes.setBackground(BEGE_CLARO);


        ImageIcon iconeExcluir = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icones/lixeira.png")));
        JButton btnExcluir = criarBotaoEstilizado("Excluir", iconeExcluir);

        btnExcluir.addActionListener(ae -> {
            int[] linhasSelecionadas = tabela.getSelectedRows();
            for (int linha : linhasSelecionadas) {
                Object id = tabela.getValueAt(linha, 5);


            }


        });

        ImageIcon iconeAtualizar = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icones/atualizar.png")));
        JButton btnRecarregar = criarBotaoEstilizado("Atualizar", iconeAtualizar);

        btnRecarregar.addActionListener(ae -> {
            switch (label.getText()) {
                case ("Lista de Alunos") -> {
                    pegarValores("Alunos");
                    configurarLargurasColunasAlunos(tabela);
                }
                case ("Lista de Responsáveis") -> {
                    pegarValores("Responsáveis");
                    configurarLargurasColunasResponsaveis(tabela);
                }
            }

        });

        JButton btnEditar = criarBotaoEstilizado("Editar", null);

        btnEditar.addActionListener(ae -> {
            try {
                int linhaSelecionada = tabela.getSelectedRow();


            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Selecione um item para editar"
                        , "Erro Item faltando", JOptionPane.ERROR_MESSAGE);
            }

        });

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRecarregar);
        painelBotoes.add(btnExcluir);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE_PRINCIPAL, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        painel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tabela.clearSelection();
            }
        });

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tabela.clearSelection();
            }
        });

        SwingUtilities.invokeLater(() -> {
            ajustarLargurasColunasDinamicamente(tabela);
        });
        return painel;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONT_LABEL);
        return label;
    }

    // Funções Gerais

    private void pegarValores(String classeAlvo) {
        if (classeAlvo.equals("Alunos")) {
            int i = 0;
            List<Aluno> alunos = AlunoDAO.buscarTodos();
            modeloAlunos.setRowCount(0);

            for (Aluno aluno : alunos) {
                String nomeResponsavel;
                Responsavel responsavelAtual = ResponsavelDAO.buscarPorId(aluno.getIdResponsavel());
                if (responsavelAtual != null) {
                    nomeResponsavel = responsavelAtual.getNome();
                } else {
                    nomeResponsavel = "";
                }
                modeloAlunos.addRow(new Object[]{
                        ++i,
                        aluno.getNome(),
                        aluno.getCpf(),
                        aluno.getSexo(),
                        aluno.getDataNascimento(),
                        aluno.getDeficiencias(),
                        aluno.getAlergias(),
                        aluno.getProblemaSaude(),
                        aluno.getRestricaoAlimentar(),
                        aluno.getEndereco(),
                        aluno.getBairro(),
                        nomeResponsavel,
                        aluno.getIdAluno()
                });
            }

            configurarTabela(tabelaAlunos);

        }

        if (classeAlvo.equals("Responsáveis")) {
            int i = 0;
            List<Responsavel> responsaveis = ResponsavelDAO.buscarTodos();
            modeloResponsaveis.setRowCount(0);

            for (Responsavel responsavel : responsaveis) {
                List<String> nomeDependente = new ArrayList<>();
                List<Aluno> alunos = AlunoDAO.buscarTodos();

                for (Aluno aluno: alunos){
                    Responsavel parente = ResponsavelDAO.buscarPorId(aluno.getIdResponsavel());
                    if (parente != null){
                        if (parente.getNome().equals(responsavel.getNome())){
                            nomeDependente.add(aluno.getNome());
                        }
                    }
                }

                modeloResponsaveis.addRow(new Object[]{
                        ++i,
                        responsavel.getNome(),
                        responsavel.getCpf(),
                        responsavel.getSexo(),
                        responsavel.getDataNascimento(),
                        responsavel.getEmail(),
                        responsavel.getContato(),
                        responsavel.getParentesco(),
                        responsavel.getRendaBruta(),
                        responsavel.getEmprego(),
                        nomeDependente,
                        responsavel.getIdResponsavel()
                });
            }
            configurarTabela(tabelaResponsaveis);
        }
    }

    private void limparCamposAluno() {
        txtNomeAluno.setText("");
        txtCpfAluno.setText("");
        txtRGAluno.setText("");
        txtDataNascimentoAluno.setText("");
        txtSUS.setText("");
        cbDeficiencias.limparSelecoes();
        cbAlergias.limparSelecoes();
        chkMobilidadeT.setSelected(false);
        chkMobilidadeP.setSelected(false);
        chkSexoFAluno.setSelected(false);
        chkSexoMAluno.setSelected(false);
        chkSexoFAluno.setForeground(TEXTO_CLARO);
        chkSexoMAluno.setForeground(TEXTO_CLARO);
        chkMobilidadeP.setForeground(TEXTO_CLARO);
        chkMobilidadeT.setForeground(TEXTO_CLARO);
        cbEtnia.setSelectedIndex(0);
        txtProblemasSaude.setText("");
        txtRestricaoAlimentar.setText("");
        txtEndereco.setText("");
        txtPontoReferencia.setText("");
        txtNumCasa.setText("");
        txtBairro.setText("");
        cbUF.setSelectedIndex(9);
    }

    private void limparCamposResponsavel() {
        txtNomeResposavel.setText("");
        txtCpfResponsavel.setText("");
        txtRGResponsavel.setText("");
        txtDataNascimentoResponsavel.setText("");
        chkSexoFResponsavel.setSelected(false);
        chkSexoMResponsavel.setSelected(false);
        chkSexoFResponsavel.setForeground(TEXTO_CLARO);
        chkSexoMResponsavel.setForeground(TEXTO_CLARO);
        txtEmprego.setText("");
        txtLocalEmprego.setText("");
        cbEtnia.setSelectedIndex(0);
        txtEmail.setText("");
        txtTel.setText("");
        txtTel2.setText("");
        chkResponsavelLegal.setSelected(false);
        cbParentesco.setSelectedIndex(0);
        cbRenda.setSelectedIndex(0);
        txtAuxilio.setText("");
        cbAlunos.limparSelecoes();
    }

    private void configurarLargurasColunasAlunos(JTable tabela) {
        TableColumnModel columnModel = tabela.getColumnModel();

        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(1).setMinWidth(150);
        columnModel.getColumn(2).setMinWidth(120);
        columnModel.getColumn(3).setMinWidth(80);
        columnModel.getColumn(4).setMinWidth(120);
        columnModel.getColumn(5).setMinWidth(150);
        columnModel.getColumn(6).setMinWidth(150);
        columnModel.getColumn(7).setMinWidth(180);
        columnModel.getColumn(8).setMinWidth(150);
        columnModel.getColumn(9).setMinWidth(100);
        columnModel.getColumn(10).setMinWidth(100);
        columnModel.getColumn(11).setMinWidth(150);
        columnModel.getColumn(12).setMinWidth(50);

        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(120);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(200);
        columnModel.getColumn(7).setPreferredWidth(250);
        columnModel.getColumn(8).setPreferredWidth(200);
        columnModel.getColumn(9).setPreferredWidth(120);
        columnModel.getColumn(10).setPreferredWidth(120);
        columnModel.getColumn(11).setPreferredWidth(200);
        columnModel.getColumn(12).setPreferredWidth(60);
    }

    private void configurarLargurasColunasResponsaveis(JTable tabela) {
        TableColumnModel columnModel = tabela.getColumnModel();

        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(1).setMinWidth(150);
        columnModel.getColumn(2).setMinWidth(120);
        columnModel.getColumn(3).setMinWidth(80);
        columnModel.getColumn(4).setMinWidth(120);
        columnModel.getColumn(5).setMinWidth(150);
        columnModel.getColumn(6).setMinWidth(120);
        columnModel.getColumn(7).setMinWidth(100);
        columnModel.getColumn(8).setMinWidth(100);
        columnModel.getColumn(9).setMinWidth(150);
        columnModel.getColumn(10).setMinWidth(150);
        columnModel.getColumn(11).setMinWidth(50);

        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(120);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(120);
        columnModel.getColumn(7).setPreferredWidth(120);
        columnModel.getColumn(8).setPreferredWidth(120);
        columnModel.getColumn(9).setPreferredWidth(200);
        columnModel.getColumn(10).setPreferredWidth(200);
        columnModel.getColumn(11).setPreferredWidth(60);
    }

    private void ajustarLargurasColunasDinamicamente(JTable tabela) {
        TableColumnModel columnModel = tabela.getColumnModel();

        for (int column = 0; column < tabela.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = tabela.getTableHeader().getDefaultRenderer();
            }
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tabela, tableColumn.getHeaderValue(), false, false, 0, column);
            int headerWidth = headerComp.getPreferredSize().width;

            for (int row = 0; row < tabela.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tabela.getCellRenderer(row, column);
                Component cellComp = tabela.prepareRenderer(cellRenderer, row, column);
                int cellWidth = cellComp.getPreferredSize().width + tabela.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, cellWidth);
            }

            preferredWidth = Math.max(preferredWidth, headerWidth);

            if (maxWidth > 0 && preferredWidth > maxWidth) {
                preferredWidth = maxWidth;
            }

            preferredWidth += 10;

            tableColumn.setPreferredWidth(preferredWidth);
        }

        tabela.revalidate();
        tabela.repaint();
    }

    // Estilização dos componentes

    private void estilizarTextField(JTextField tf, Dimension dimensao) {
        if (dimensao == null) {
            tf.setBackground(BRANCO_ACINZENTADO);
            tf.setForeground(TEXTO_ESCURO);
            tf.setPreferredSize(new Dimension(330, 40));
            tf.setCaretColor(TEXTO_ESCURO);
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(VERDE_SECUNDARIO, 1, true),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            tf.setFont(FONT_LABEL);
        } else {
            tf.setBackground(BRANCO_ACINZENTADO);
            tf.setForeground(TEXTO_ESCURO);
            tf.setPreferredSize(dimensao);
            tf.setCaretColor(TEXTO_ESCURO);
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(VERDE_SECUNDARIO, 1, true),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            tf.setFont(FONT_LABEL);
        }

    }

    private void estilizarComboBox(JComboBox<String> cb) {
        cb.setFont(FONT_LABEL);
        cb.setBackground(BRANCO_ACINZENTADO);
        cb.setPreferredSize(new Dimension(330, 50));
        cb.setBorder(BorderFactory.createLineBorder(VERDE_SECUNDARIO, 1, true));
    }

    private void estilizarCheckBox(JCheckBox checkBox) {
        checkBox.setBackground(BEGE_CLARO);
        checkBox.setFont(FONT_LABEL);
        checkBox.setForeground(TEXTO_CLARO);

        checkBox.addActionListener(e -> {
            if (checkBox.isSelected()) {
                checkBox.setForeground(TEXTO_ESCURO);
            } else {
                checkBox.setForeground(TEXTO_CLARO);
            }
        });
    }

    private JButton criarBotaoEstilizado(String texto, ImageIcon icone) {
        JButton btn = new JButton(texto) {
            final int marginHoriz = 10;
            final int marginVert = 5;

            @Override
            public Dimension getPreferredSize() {
                Dimension pref = super.getPreferredSize();
                return new Dimension(
                        Math.max(pref.width + 2 * marginHoriz, 30),
                        Math.max(pref.height + 2 * marginVert, 10)
                );
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);

            }
        };

        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setFont(FONT_LABEL);
        btn.setForeground(TEXTO_ESCURO);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (texto.equals("Excluir") || texto.equals("Cancelar") || texto.equals("Logout")) {
            btn.setBackground(ERRO);
            btn.setBorder(new RoundedBorder(20, ERRO));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btn.setBackground(ERRO.darker());
                    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(MouseEvent evt) {
                    btn.setBackground(ERRO);
                }

                public void mousePressed(MouseEvent evt) {
                    btn.setBackground(ERRO.darker().darker());
                }

                public void mouseReleased(MouseEvent evt) {
                    btn.setBackground(ERRO);
                }
            });
        } else {
            btn.setBackground(SALMAO_SUAVE);
            btn.setBorder(new RoundedBorder(20, VERDE_SECUNDARIO));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btn.setBackground(HOVER_SALMAO);
                    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(MouseEvent evt) {
                    btn.setBackground(SALMAO_SUAVE);
                }

                public void mousePressed(MouseEvent evt) {
                    btn.setBackground(HOVER_SALMAO);
                }

                public void mouseReleased(MouseEvent evt) {
                    btn.setBackground(SALMAO_SUAVE);
                }
            });
        }

        if (icone != null) {
            Image img = icone.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
            btn.setHorizontalTextPosition(SwingConstants.TRAILING);
            btn.setVerticalTextPosition(SwingConstants.CENTER);
            btn.setIconTextGap(5);
        }

        return btn;

    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(FONT_LABEL);
        tabela.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabela.setBackground(BRANCO_ACINZENTADO);
        tabela.setForeground(TEXTO_ESCURO);
        tabela.setRowHeight(40);
        tabela.setBorder(BorderFactory.createLineBorder(TEXTO_ESCURO, 1, true));
        tabela.setShowGrid(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for (int column = 0; column < tabela.getColumnCount(); column++) {
            TableColumn tableColumn = tabela.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < tabela.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tabela.getCellRenderer(row, column);
                Component c = tabela.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tabela.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            tableColumn.setPreferredWidth(preferredWidth + 10);
        }
    }

    private void configurarComboBox(JComboBox comboBox, JComboBox cbMunicipio) {
        String estado = comboBox.getItemAt(comboBox.getSelectedIndex()).toString();
        String[] municipios = {};
        switch (estado) {
            case ("AC") ->
                    municipios = new String[]{"Rio Branco", "Cruzeiro do Sul", "Sena Madureira", "Tarauacá", "Feijó",
                            "Brasiléia", "Senador Guiomard", "Plácido de Castro", "Xapuri", "Mâncio Lima",
                            "Rodrigues Alves", "Epitaciolândia", "Porto Acre", "Acrelândia", "Capixaba",
                            "Bujari", "Manoel Urbano", "Assis Brasil", "Porto Walter", "Marechal Thaumaturgo"};
            case ("AL") ->
                    municipios = new String[]{"Maceió", "Arapiraca", "Palmeira dos Índios", "Rio Largo", "União dos Palmares",
                            "Penedo", "São Miguel dos Campos", "Santana do Ipanema", "Delmiro Gouveia", "Campo Alegre",
                            "Coruripe", "Murici", "Marechal Deodoro", "Atalaia", "Teotônio Vilela",
                            "Pilar", "São Luís do Quitunde", "Matriz de Camaragibe", "Porto Calvo", "Maragogi"};
            case ("AP") ->
                    municipios = new String[]{"Macapá", "Santana", "Laranjal do Jari", "Oiapoque", "Porto Grande",
                            "Mazagão", "Tartarugalzinho", "Vitória do Jari", "Pedra Branca do Amapari", "Serra do Navio",
                            "Amapá", "Ferreira Gomes", "Cutias", "Itaubal", "Pracuúba",
                            "Calçoene", "Augusto Corrêa", "Cachoeira do Arari", "Chaves", "Curralinho"};
            case ("AM") -> municipios = new String[]{"Manaus", "Parintins", "Itacoatiara", "Manacapuru", "Coari",
                    "Tefé", "Maués", "Humaitá", "Iranduba", "Rio Preto da Eva",
                    "Benjamin Constant", "Boca do Acre", "Eirunepé", "Lábrea", "Novo Aripuanã",
                    "São Gabriel da Cachoeira", "Tabatinga", "Autazes", "Careiro", "Borba"};
            case ("BA") ->
                    municipios = new String[]{"Salvador", "Feira de Santana", "Vitória da Conquista", "Camaçari", "Itabuna",
                            "Juazeiro", "Ilhéus", "Lauro de Freitas", "Jequié", "Teixeira de Freitas",
                            "Alagoinhas", "Barreiras", "Porto Seguro", "Simões Filho", "Paulo Afonso",
                            "Eunápolis", "Santo Antônio de Jesus", "Valença", "Candeias", "Guanambi"};
            case ("CE") -> municipios = new String[]{"Fortaleza", "Caucaia", "Juazeiro do Norte", "Maracanaú", "Sobral",
                    "Crato", "Itapipoca", "Maranguape", "Iguatu", "Quixadá",
                    "Pacatuba", "Quixeramobim", "Aracati", "Canindé", "Crateús",
                    "Russas", "Horizonte", "Tauá", "Barbalha", "Camocim"};
            case ("DF") -> municipios = new String[]{"Brasília", "Ceilândia", "Taguatinga", "Samambaia", "Planaltina",
                    "Sobradinho", "Gama", "Santa Maria", "São Sebastião", "Paranoá",
                    "Recanto das Emas", "Guará", "Cruzeiro", "Arniqueira", "Riacho Fundo",
                    "Lago Sul", "Lago Norte", "Sudoeste/Octogonal", "Varjão", "Park Way"};
            case ("ES") -> municipios = new String[]{"Vitória", "Vila Velha", "Serra", "Cariacica", "Linhares",
                    "São Mateus", "Guarapari", "Colatina", "Aracruz", "Viana",
                    "Nova Venécia", "Barra de São Francisco", "Santa Maria de Jetibá", "Domingos Martins", "Afonso Cláudio",
                    "Itapemirim", "Conceição da Barra", "Marataízes", "Piúma", "Anchieta"};
            case ("GO") ->
                    municipios = new String[]{"Goiânia", "Aparecida de Goiânia", "Anápolis", "Rio Verde", "Luziânia",
                            "Águas Lindas de Goiás", "Valparaíso de Goiás", "Trindade", "Formosa", "Novo Gama",
                            "Itumbiara", "Jataí", "Catalão", "Senador Canedo", "Planaltina",
                            "Caldas Novas", "Santo Antônio do Descoberto", "Goianésia", "Mineiros", "Inhumas"};
            case ("MA") -> municipios = new String[]{"São Luís", "Imperatriz", "São José de Ribamar", "Timon", "Caxias",
                    "Codó", "Paço do Lumiar", "Açailândia", "Bacabal", "Balsas",
                    "Barra do Corda", "Santa Inês", "Chapadinha", "Pinheiro", "Buriticupu",
                    "Coroatá", "Tutóia", "Zé Doca", "Grajaú", "Viana"};
            case ("MT") ->
                    municipios = new String[]{"Cuiabá", "Várzea Grande", "Rondonópolis", "Sinop", "Tangará da Serra",
                            "Cáceres", "Sorriso", "Lucas do Rio Verde", "Primavera do Leste", "Barra do Garças",
                            "Alta Floresta", "Pontes e Lacerda", "Juína", "Campo Verde", "Nova Mutum",
                            "Mirassol d'Oeste", "Jaciara", "Diamantino", "Santo Antônio do Leverger", "Poconé"};
            case ("MS") -> municipios = new String[]{"Campo Grande", "Dourados", "Três Lagoas", "Corumbá", "Ponta Porã",
                    "Naviraí", "Nova Andradina", "Aquidauana", "Sidrolândia", "Paranaíba",
                    "Maracaju", "Amambai", "Ribas do Rio Pardo", "Coxim", "Caarapó",
                    "Miranda", "Bandeirantes", "Jardim", "São Gabriel do Oeste", "Terenos"};
            case ("MG") ->
                    municipios = new String[]{"Belo Horizonte", "Uberlândia", "Contagem", "Juiz de Fora", "Betim",
                            "Montes Claros", "Ribeirão das Neves", "Uberaba", "Governador Valadares", "Ipatinga",
                            "Sete Lagoas", "Divinópolis", "Santa Luzia", "Ibirité", "Poços de Caldas",
                            "Patos de Minas", "Teófilo Otoni", "Pouso Alegre", "Barbacena", "Sabará"};
            case ("PA") -> municipios = new String[]{"Belém", "Ananindeua", "Santarém", "Marabá", "Castanhal",
                    "Parauapebas", "Abaetetuba", "Cametá", "Marituba", "Bragança",
                    "Barcarena", "Altamira", "Tucuruí", "Tailândia", "Paragominas",
                    "Redenção", "Moju", "Breves", "Capanema", "Vigia"};
            case ("PB") -> municipios = new String[]{"João Pessoa", "Campina Grande", "Santa Rita", "Patos", "Bayeux",
                    "Sousa", "Cajazeiras", "Guarabira", "Cabedelo", "Sapé",
                    "Mamanguape", "Monteiro", "Solânea", "Esperança", "Pombal",
                    "Catolé do Rocha", "Alagoa Grande", "Lagoa Seca", "Itabaiana", "Conde"};
            case ("PR") -> municipios = new String[]{"Curitiba", "Londrina", "Maringá", "Ponta Grossa", "Cascavel",
                    "São José dos Pinhais", "Foz do Iguaçu", "Colombo", "Guarapuava", "Paranaguá",
                    "Araucária", "Toledo", "Apucarana", "Pinhais", "Campo Largo",
                    "Arapongas", "Almirante Tamandaré", "Umuarama", "Cambé", "Piraquara"};
            case ("PE") ->
                    municipios = new String[]{"Recife", "Jaboatão dos Guararapes", "Olinda", "Caruaru", "Petrolina",
                            "Paulista", "Cabo de Santo Agostinho", "Camaragibe", "Garanhuns", "Vitória de Santo Antão",
                            "Ipojuca", "São Lourenço da Mata", "Serra Talhada", "Araripina", "Gravatá",
                            "Santa Cruz do Capibaribe", "Belo Jardim", "Goiana", "Palmares", "Surubim"};
            case ("PI") -> municipios = new String[]{"Teresina", "Parnaíba", "Picos", "Piripiri", "Floriano",
                    "Barras", "Campo Maior", "União", "Altos", "José de Freitas",
                    "Oeiras", "São Raimundo Nonato", "Esperantina", "Cocal", "Batalha",
                    "Luís Correia", "Paulistana", "Pedro II", "Uruçuí", "Amarante"};
            case ("RJ") ->
                    municipios = new String[]{"Rio de Janeiro", "São Gonçalo", "Duque de Caxias", "Nova Iguaçu", "Niterói",
                            "Belford Roxo", "São João de Meriti", "Petrópolis", "Campos dos Goytacazes", "Volta Redonda",
                            "Magé", "Itaboraí", "Macaé", "Cabo Frio", "Angra dos Reis",
                            "Nova Friburgo", "Barra Mansa", "Teresópolis", "Mesquita", "Araruama"};
            case ("RN") ->
                    municipios = new String[]{"Natal", "Mossoró", "Parnamirim", "São Gonçalo do Amarante", "Macaíba",
                            "Ceará-Mirim", "Caicó", "Açu", "Currais Novos", "São José de Mipibu",
                            "Santa Cruz", "Nova Cruz", "Touros", "João Câmara", "Pau dos Ferros",
                            "Extremoz", "Baraúna", "Areia Branca", "Goianinha", "Monte Alegre"};
            case ("RS") ->
                    municipios = new String[]{"Porto Alegre", "Caxias do Sul", "Pelotas", "Canoas", "Santa Maria",
                            "Gravataí", "Viamão", "Novo Hamburgo", "São Leopoldo", "Rio Grande",
                            "Alvorada", "Passo Fundo", "Sapucaia do Sul", "Uruguaiana", "Santa Cruz do Sul",
                            "Cachoeirinha", "Bagé", "Bento Gonçalves", "Erechim", "Guaíba"};
            case ("RO") -> municipios = new String[]{"Porto Velho", "Ji-Paraná", "Ariquemes", "Vilhena", "Cacoal",
                    "Rolim de Moura", "Jaru", "Guajará-Mirim", "Ouro Preto do Oeste", "Pimenta Bueno",
                    "Buritis", "Machadinho d'Oeste", "Espigão d'Oeste", "Presidente Médici", "Nova Mamoré",
                    "Alta Floresta d'Oeste", "Candeias do Jamari", "Colorado do Oeste", "São Miguel do Guaporé", "Cerejeiras"};
            case ("RR") -> municipios = new String[]{"Boa Vista", "Rorainópolis", "Caracaraí", "Alto Alegre", "Mucajaí",
                    "Cantá", "Bonfim", "Pacaraima", "Amajari", "Normandia",
                    "Uiramutã", "Caroebe", "Iracema", "São João da Baliza", "São Luiz",
                    "Cristiano Otoni", "Espigão do Oeste", "Governador Jorge Teixeira", "Ministro Andreazza", "Mirante da Serra"};
            case ("SC") -> municipios = new String[]{"Florianópolis", "Joinville", "Blumenau", "São José", "Criciúma",
                    "Chapecó", "Itajaí", "Lages", "Jaraguá do Sul", "Palhoça",
                    "Balneário Camboriú", "Brusque", "Tubarão", "Camboriú", "Caçador",
                    "Concórdia", "Araranguá", "Rio do Sul", "Navegantes", "São Bento do Sul"};
            case ("SP") ->
                    municipios = new String[]{"São Paulo", "Guarulhos", "Campinas", "São Bernardo do Campo", "Santo André",
                            "Osasco", "São José dos Campos", "Ribeirão Preto", "Sorocaba", "Mauá",
                            "São José do Rio Preto", "Mogi das Cruzes", "Santos", "Diadema", "Jundiaí",
                            "Piracicaba", "Carapicuíba", "Bauru", "Itaquaquecetuba", "São Vicente"};
            case ("SE") ->
                    municipios = new String[]{"Aracaju", "Nossa Senhora do Socorro", "Lagarto", "Itabaiana", "São Cristóvão",
                            "Estância", "Tobias Barreto", "Simão Dias", "Poço Redondo", "Propriá",
                            "Capela", "Porto da Folha", "Nossa Senhora da Glória", "Itabaianinha", "Japaratuba",
                            "Laranjeiras", "Boquim", "Umbaúba", "Canindé de São Francisco", "Riachão do Dantas"};
            case ("TO") ->
                    municipios = new String[]{"Palmas", "Araguaína", "Gurupi", "Porto Nacional", "Paraíso do Tocantins",
                            "Araguatins", "Colinas do Tocantins", "Guaraí", "Tocantinópolis", "Miracema do Tocantins",
                            "Dianópolis", "Formoso do Araguaia", "Almas", "Augustinópolis", "Nova Olinda",
                            "Wanderlândia", "Xambioá", "Arraias", "Natividade", "Pedro Afonso"};
        }
        cbMunicipio.setModel(new DefaultComboBoxModel<>(municipios));
    }

    private static class ComboBoxDinamico extends JPanel {
        private JPanel pnlComboBoxes;
        private JScrollPane scrollPane;
        private final List<LinhaComboBox> todasLinhas = new ArrayList<>();
        List<Aluno> classeAlunos = AlunoDAO.buscarTodos();
        List<String> nomes = classeAlunos.stream()
                .map(Aluno::getNome)
                .toList();

        private final String[] deficiencias = {"Nenhuma", "Altas Habilidades (superdotação)", "Cegueira",
                "Deficiência Auditiva (processamento central)", "Deficiência Auditiva (surdez leve ou moderada)",
                "Deficiência Auditiva (surdez severa ou profunda)", "Deficiência Física (cadeirante) - permanente",
                "Deficiência Física (outros)", "Deficiência Física (paralisia cerebral)",
                "Deficiência Física (paraplegia ou monoplegia)", "Deficiência intelectual", "Deficiência mental",
                "Deficiência Visual (baixa visão)", "Disfemia (gagueira)", "Espectro Autista Nível I",
                "Espectro Autista Nível II", "Espectro Autista Nível III", "Estrabismo",
                "Sensorial Alta (sensibilidade)", "Sensorial Baixa (sensibilidade)", "Síndrome de Down", "Surdo",
                "TDAH", "TEA", "TOD", "Outra"};

        private final String[] alergias = {"Nenhuma","Ácaros", "Adoçantes artificiais", "Amendoim", "Água (urticária aquagênica)",
                "Anti-inflamatórios", "Anticonvulsivantes", "Calor", "Castanhas", "Chocolate", "Conservantes",
                "Corantes artificiais", "Detergentes", "Exercício físico", "Frio", "Frutas cítricas", "Frutos do mar",
                "Fungos/mofo", "Glúten", "Iodo", "Insulina", "Lactose", "Látex", "Leite de vaca", "Mariscos", "Ovo",
                "Pelos de animais", "Penicilina e outros antibióticos", "Perfumes/fragrâncias", "Peixe", "Picada de abelha",
                "Picada de formiga", "Pólen", "Produtos de beleza", "Sol (fotossensibilidade)", "Soja", "Tomate",
                "Trigo", "Outra"};

        private final String[] alunos = nomes.toArray(new String[0]);
        private final String tipoCB;

        public ComboBoxDinamico(String tipoCB) {
            this.tipoCB = tipoCB;
            setLayout(new BorderLayout());
            setBackground(BEGE_CLARO);
            criarInterface();
        }

        public void criarInterface() {
            pnlComboBoxes = new JPanel();
            pnlComboBoxes.setLayout(new BoxLayout(pnlComboBoxes, BoxLayout.Y_AXIS));
            pnlComboBoxes.setBackground(BEGE_CLARO);

            scrollPane = new JScrollPane(pnlComboBoxes);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));
            scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI(VERDE_PRINCIPAL));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setPreferredSize(new Dimension(530, 120));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            adicionarNovaLinha();

            add(scrollPane, BorderLayout.CENTER);
        }

        private void adicionarNovaLinha() {
            LinhaComboBox novaLinha = new LinhaComboBox();
            todasLinhas.add(novaLinha);
            pnlComboBoxes.add(novaLinha);

            atualizarTodosComboBoxes();
            atualizarBotoesLinhas();

            pnlComboBoxes.revalidate();
            pnlComboBoxes.repaint();

            SwingUtilities.invokeLater(() -> {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getMaximum());
            });
        }

        private void removerLinha(LinhaComboBox linha) {
            todasLinhas.remove(linha);
            pnlComboBoxes.remove(linha);

            atualizarBotoesLinhas();
            atualizarTodosComboBoxes();

            pnlComboBoxes.revalidate();
            pnlComboBoxes.repaint();
        }

        private void atualizarBotoesLinhas() {
            for (int i = 0; i < todasLinhas.size(); i++) {
                LinhaComboBox linha = todasLinhas.get(i);
                boolean ehPrimeira = (i == 0);
                boolean ehUltima = (i == todasLinhas.size() - 1);

                linha.atualizarBotoes(!ehPrimeira, ehUltima);
            }
        }

        private String[] getOpcoesDisponiveis(JComboBox<String> comboBoxAtual) {
            List<String> disponiveis = new ArrayList<>();
            switch (tipoCB){
                    case ("Deficiencias") -> {
                        disponiveis = new ArrayList<>(Arrays.asList(deficiencias));
                    }
                    case ("Alergias") -> {
                        disponiveis = new ArrayList<>(Arrays.asList(alergias));
                    }
                    case ("Alunos") -> {
                        disponiveis = new ArrayList<>(Arrays.asList(alunos));
                    }


            }
            if ("Deficiencias".equals(tipoCB)) {

            } else if ("Alergias".equals(tipoCB)) {

            }

            if (todasLinhas.size() > 1) {
                disponiveis.remove("Nenhuma");
            }

            for (LinhaComboBox linha : todasLinhas) {
                String selecionado = linha.getSelecaoAtual();
                if (selecionado != null && !selecionado.equals("Nenhuma") &&
                        (comboBoxAtual == null || linha.comboBox != comboBoxAtual)) {
                    disponiveis.remove(selecionado);
                }
            }

            return disponiveis.toArray(new String[0]);
        }

        private void atualizarTodosComboBoxes() {
            Map<JComboBox<String>, String> selecoesAtuais = new HashMap<>();
            for (LinhaComboBox linha : todasLinhas) {
                selecoesAtuais.put(linha.comboBox, linha.getSelecaoAtual());
            }

            for (LinhaComboBox linha : todasLinhas) {
                linha.atualizarOpcoesComRestricao(selecoesAtuais.get(linha.comboBox));
            }
        }

        private class LinhaComboBox extends JPanel {
            private final JComboBox<String> comboBox;
            private final JButton btnRemover;
            private final JButton btnAdicionar;
            private final ActionListener comboBoxListener;

            public LinhaComboBox() {
                setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
                setBackground(BEGE_CLARO);
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                comboBox = new JComboBox<>(getOpcoesDisponiveis(null));
                comboBox.setPreferredSize(new Dimension(330, 40));
                estilizarComboBox(comboBox);

                comboBoxListener = e -> {
                    SwingUtilities.invokeLater(() -> {
                        atualizarTodosComboBoxes();
                        atualizarBotoesLinhas();
                    });
                };
                comboBox.addActionListener(comboBoxListener);

                btnRemover = criarBotaoRemover();
                btnRemover.addActionListener(e -> removerLinha(this));

                btnAdicionar = criarBotaoAdicionar();
                btnAdicionar.addActionListener(e -> adicionarNovaLinha());

                add(comboBox);
                add(btnRemover);
                add(btnAdicionar);
            }

            public String getSelecaoAtual() {
                return (String) comboBox.getSelectedItem();
            }

            public void atualizarOpcoesComRestricao(String selecaoParaManter) {
                comboBox.removeActionListener(comboBoxListener);

                String[] novasOpcoes = getOpcoesDisponiveis(comboBox);
                comboBox.setModel(new DefaultComboBoxModel<>(novasOpcoes));

                boolean selecaoMantida = false;
                if (selecaoParaManter != null) {
                    for (String opcao : novasOpcoes) {
                        if (opcao.equals(selecaoParaManter)) {
                            comboBox.setSelectedItem(selecaoParaManter);
                            selecaoMantida = true;
                            break;
                        }
                    }
                }

                if (!selecaoMantida && novasOpcoes.length > 0) {
                    comboBox.setSelectedIndex(0);
                }

                comboBox.addActionListener(comboBoxListener);
            }

            public void atualizarBotoes(boolean mostrarRemover, boolean mostrarAdicionar) {
                btnRemover.setVisible(mostrarRemover);
                btnAdicionar.setVisible(mostrarAdicionar);
            }

            private JButton criarBotaoRemover() {
                JButton btn = new JButton("−");
                btn.setPreferredSize(new Dimension(49, 45));
                btn.setFont(FONT_LABEL);
                btn.setBackground(ERRO);
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                switch (tipoCB){
                    case ("Deficiencias") -> {
                        String tooltip = "Remover esta deficiência";
                        btn.setToolTipText(tooltip);
                    }
                    case ("Alergias") -> {
                        String tooltip = "Remover esta alergia";
                        btn.setToolTipText(tooltip);
                    }
                    case ("Alunos") -> {
                        String tooltip = "Remover este aluno";
                        btn.setToolTipText(tooltip);
                    }

                }
                return btn;
            }

            private JButton criarBotaoAdicionar() {
                JButton btn = new JButton("+");
                btn.setPreferredSize(new Dimension(45, 45));
                btn.setFont(FONT_LABEL);
                btn.setBackground(VERDE_SECUNDARIO);
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                switch (tipoCB){
                    case ("Deficiencias") -> {
                        String tooltip = "Adicionar esta deficiência";
                        btn.setToolTipText(tooltip);
                    }
                    case ("Alergias") -> {
                        String tooltip = "Adicionar esta alergia";
                        btn.setToolTipText(tooltip);
                    }
                    case ("Alunos") -> {
                        String tooltip = "Adicionar este aluno";
                        btn.setToolTipText(tooltip);
                    }

                }
                return btn;
            }
        }

        private void estilizarComboBox(JComboBox<String> comboBox) {
            comboBox.setFont(FONT_LABEL);
            comboBox.setBackground(BRANCO_ACINZENTADO);
            comboBox.setForeground(TEXTO_ESCURO);
            comboBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(VERDE_SECUNDARIO, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            comboBox.setPreferredSize(new Dimension(455,43));
        }

        public List<String> getSelecoes() {
            List<String> selecionadas = new ArrayList<>();
            for (LinhaComboBox linha : todasLinhas) {
                String selecionado = linha.getSelecaoAtual();
                if (selecionado != null && !selecionado.equals("Nenhuma")) {
                    selecionadas.add(selecionado);
                }
            }
            return selecionadas;
        }

        public void limparSelecoes() {
            while (todasLinhas.size() > 1) {
                LinhaComboBox linha = todasLinhas.removeLast();
                pnlComboBoxes.remove(linha);
            }

            if (!todasLinhas.isEmpty()) {
                todasLinhas.getFirst().atualizarOpcoesComRestricao("Nenhuma");
                atualizarBotoesLinhas();
            }

            pnlComboBoxes.revalidate();
            pnlComboBoxes.repaint();
        }


    }

    static class RoundedBorder extends AbstractBorder {

        private final int radius;
        private final Color color;
        private final BasicStroke stroke;

        public RoundedBorder(int radius, Color color) {
            this(radius, color, 1.0f); // Espessura padrão de 1px
        }

        public RoundedBorder(int radius, Color color, float thickness) {
            this.radius = radius;
            this.color = color;
            this.stroke = new BasicStroke(thickness);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(stroke);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = radius / 2;
            insets.top = insets.bottom = radius / 2;
            return insets;
        }

    }

    static class CustomScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {

        private final Color thumbColor;

        public CustomScrollBarUI(Color thumbColor) {
            this.thumbColor = thumbColor;
        }

        @Override
        protected void configureScrollBarColors() {
            this.trackColor = BRANCO_ACINZENTADO;
            this.thumbHighlightColor = thumbColor.brighter();
            this.thumbDarkShadowColor = thumbColor.darker();
            this.thumbLightShadowColor = thumbColor.brighter();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(BRANCO_ACINZENTADO);
            g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 10, 10);
            g2.dispose();
        }
    }

    static class CustomTabbedPaneUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {

        private final Color selectedColor;
        private final Color foregroundColor;

        public CustomTabbedPaneUI(Color selectedColor, Color foregroundColor) {
            this.selectedColor = selectedColor;
            this.foregroundColor = foregroundColor;
        }

        @Override
        protected void installDefaults() {
            super.installDefaults();
            tabAreaInsets.left = 10;
            tabInsets = new Insets(10, 15, 10, 15);
            selectedTabPadInsets = new Insets(2, 2, 2, 2);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement,
                                          int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (isSelected) {
                g2.setColor(selectedColor);
                g2.fillRoundRect(x + 2, y + 2, w - 5, h - 4, 2, 2);
            } else {
                g2.setColor(TEXTO_MEDIO);
                g2.fillRoundRect(x + 2, y + 2, w - 4, h - 4, 12, 12);
            }
            g2.dispose();
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font,
                                 FontMetrics metrics, int tabIndex, String title,
                                 Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            g.setColor(isSelected ? foregroundColor : new Color(180, 180, 180));
            g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement,
                                           Rectangle[] rects, int tabIndex,
                                           Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        }
    }
}
