package view;

import controller.EventoDao;
import controller.IngressoDao;
import controller.LoteDao;
import controller.VendaDao;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.bean.Evento;
import model.bean.Ingresso;
import model.bean.Lote;
import model.bean.Venda;

public class VendaIngressoView extends javax.swing.JFrame {

    public VendaIngressoView() {
        initComponents();
        this.setLocationRelativeTo(null);
        iniciarTela();
    }

    @SuppressWarnings("unchecked")

    private void iniciarTela() {
        limparCampos();
        listarEvento();
        desabilitaIngresso();
        desabilitaLote();
    }
    
    public void habilitaEvento() {
        cbEvento.setEnabled(true);
        btSelEvento.setEnabled(true);
    }
    
    public void habilitaIngresso() {
        cbIngresso.setEnabled(true);
        btSelIngresso.setEnabled(true);
    }

    public void habilitaLote() {
        cbLote.setEnabled(true);
        btAdcionar.setEnabled(true);
        btRemover.setEnabled(true);
    }

    public void desabilitaLote() {
        cbLote.setEnabled(false);
        btAdcionar.setEnabled(false);
        btRemover.setEnabled(false);
    }

    public void desabilitaIngresso() {
        cbIngresso.setEnabled(false);
        btSelIngresso.setEnabled(false);
    }

    public void desabilitaEvento() {
        cbEvento.setEnabled(false);
        btSelEvento.setEnabled(false);
    }

    public void limparListagem(JTable tbGridListagem) {
        DefaultTableModel linha = (DefaultTableModel) tbGridListagem.getModel();
        while (linha.getRowCount() > 0) {
            linha.removeRow(0);
        }
        lbTotalPagar.setText("0,00");
    }

    public void limparCampos() {
        limparListagem(tbListagem);
        cbEvento.removeAll();
        cbIngresso.removeAll();
        cbLote.removeAll();

        lbData.setText("--");
        lbHorario.setText("--");
        lbLocal.setText("--");

        lbQuantidadeLote.setText("00");
        lbValorLote.setText("0,00");

        lbTotalPagar.setText("0,00");

        optDinheiro.setSelected(false);
        optCartao.setSelected(false);
    }

    private void carregaDadosEvento() {
        if (cbEvento.getSelectedIndex() != -1) {
            List<Evento> evento = new ArrayList<Evento>();
            EventoDao eventoDao = new EventoDao();

            evento = eventoDao.consultar(cbEvento.getSelectedItem().toString());
            try {
                for (Evento event : evento) {
                    lbLocal.setText(event.getLocalEvento());
                    lbData.setText(event.getDataEvento());
                    lbHorario.setText(event.getHorarioEvento());
                }
            } catch (Exception e) {
                e.getMessage();
                System.out.println("Nao foi possivel carregar a Listagem n o grid");
            }
        }
    }

    private void carregaDadosLote() {
        if (cbLote.getSelectedIndex() != -1) {
            List<Lote> lote = new ArrayList<Lote>();
            LoteDao loteDao = new LoteDao();

            lote = loteDao.listaEventoIngressoLote(cbEvento.getSelectedItem().toString(), cbIngresso.getSelectedItem().toString(), cbLote.getSelectedItem().toString());
            try {
                for (Lote lot : lote) {
                    lbValorLote.setText(Double.toString(lot.getValorLote()));
                    lbQuantidadeLote.setText(Integer.toString(lot.getQuantidadeLote()));
                }
            } catch (Exception e) {
                e.getMessage();
                System.out.println("Nao foi possivel carregar a Listagem n o grid");
            }
        }
    }

    private void listarEvento() {
        cbEvento.removeAllItems();
        EventoDao eventoDao = new EventoDao();
        Evento evento = new Evento();
        List<Evento> listaEvento = new ArrayList<Evento>();
        listaEvento = eventoDao.listar();
        try {
            for (Evento event : listaEvento) {
                cbEvento.addItem(event.getNomeEvento());
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem n o grid");
        }
    }

    private void listarIngresso() {
        cbIngresso.removeAllItems();
        IngressoDao ingressoDao = new IngressoDao();
        Ingresso ingresso = new Ingresso();
        List<Ingresso> listaIngresso = new ArrayList<Ingresso>();
        listaIngresso = ingressoDao.consultar(cbEvento.getSelectedItem().toString());
        try {
            for (Ingresso ingres : listaIngresso) {
                cbIngresso.addItem(ingres.getDescricaoIngresso());
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem no cbEventoIngresso");
        }
    }

    private void listarLote() {
        cbLote.removeAllItems();
        LoteDao loteDao = new LoteDao();
        Lote lote = new Lote();
        List<Lote> listaLote = new ArrayList<Lote>();
        listaLote = loteDao.listarLotePorIngressoEvento(cbIngresso.getSelectedItem().toString(), cbEvento.getSelectedItem().toString());
        try {
            for (Lote lot : listaLote) {
                cbLote.addItem(lot.getNumeroLote());
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem no cbLote");
        }
    }

    private void validaEventoSelecionado() {
        if (cbEvento.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um Evento.");
        } else {
            habilitaIngresso();
            listarIngresso();
            desabilitaEvento();
        }
    }

    private void validaIngressoSelecionado() {
        if (cbIngresso.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um Ingresso.");
        } else {
            habilitaLote();
            listarLote();
            desabilitaEvento();
            desabilitaIngresso();
        }
    }

    private void validaLoteSelecionado() {
        if (cbLote.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um Lote.");
        } else if (Integer.parseInt(lbQuantidadeLote.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Não há quantidade disponível para a venda deste lote.");
        } else {
            addIngressoVenda();
        }
    }

    private boolean validaDadosVenda() {
        if (tbListagem.getRowCount() > 0) {
            if ((optDinheiro.isSelected()) || (optCartao.isSelected())) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma das Formas de Pagamento\nDINHEIRO ou CARTÃO.");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum Ingresso Selecionado para a realização da Venda.");
            return false;
        }

    }

    private void addIngressoVenda() {

        DefaultTableModel linha = (DefaultTableModel) tbListagem.getModel();
        limparListagem(tbListagem);
        Lote lote = new Lote();
        lote.setNomeEvento(cbEvento.getSelectedItem().toString());
        lote.setDescricaoIngresso(cbIngresso.getSelectedItem().toString());
        lote.setNumeroLote(cbLote.getSelectedItem().toString());
        try {
            linha.addRow(new Object[]{
                lote.getNomeEvento(),
                lote.getDescricaoIngresso(),
                lote.getNumeroLote(),});
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel inserir o Ingresso a venda");
        }
        lbTotalPagar.setText(lbValorLote.getText());
    }

    private void finalizarVenda() {
        if (validaDadosVenda()) {
            LoteDao loteDao = new LoteDao();
            List<Lote> lote = new ArrayList<Lote>();
            lote = loteDao.listaEventoIngressoLote(cbEvento.getSelectedItem().toString(), cbIngresso.getSelectedItem().toString(), cbLote.getSelectedItem().toString());

            //Verifica forma de pagamento selecionada
            String formaPagamento = "";
            if (optDinheiro.isSelected()) {
                formaPagamento = "DINHEIRO";
            }
            if (optCartao.isSelected()) {
                formaPagamento = "CARTÃO";
            }

            //Abastece o model Venda com dads da venda
            Venda venda = new Venda();
            venda.setFormaPagamentoVenda(formaPagamento);
            venda.setValorVenda(lote.get(0).getValorLote());
            venda.setIdEventoVenda(lote.get(0).geIdEvento());
            venda.setIdIngressoVenda(lote.get(0).getIdIngressoLote());
            venda.setIdLoteVenda(lote.get(0).getIdLote());

            //cria registro da venda para VendaDao
            VendaDao vendaDao = new VendaDao();
            vendaDao.gerarVenda(venda);

            //Baixa a quantidade do Lote vendido
            loteDao.baixaQuantidadeLote(venda.getIdLoteVenda());

            //Fechar tela
            this.setVisible(false);
            vendaDao.gerarIngresso();
        }
    }

    public void cancelar() {
        cbIngresso.removeAllItems();
        cbLote.removeAllItems();
        limparListagem(tbListagem);
        desabilitaIngresso();
        desabilitaLote();
        optCartao.setSelected(false);
        optDinheiro.setSelected(false);
        listarEvento();
        habilitaEvento();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpEvento = new javax.swing.JPanel();
        cbEvento = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbLocal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbData = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbHorario = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btSelEvento = new javax.swing.JButton();
        jpIngresso = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btSelIngresso = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        cbIngresso = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbListagem = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        btRemover = new javax.swing.JButton();
        lbTotalPagar = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btFinalizarVenda = new javax.swing.JButton();
        optDinheiro = new javax.swing.JRadioButton();
        optCartao = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        cbCancelar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        lbQuantidadeLote1 = new javax.swing.JLabel();
        lbQuantidadeLote2 = new javax.swing.JLabel();
        jpLote = new javax.swing.JPanel();
        cbLote = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        lbValorLote = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btAdcionar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        lbQuantidadeLote = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setTitle("Venda de Ingressos");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("INGRESIS");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Sistema Emissor de Ingressos");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jpEvento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(153, 153, 153))); // NOI18N

        cbEvento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbEventoMouseClicked(evt);
            }
        });
        cbEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEventoActionPerformed(evt);
            }
        });

        jLabel4.setText("Selecione o Evento");

        jLabel7.setText("Local");

        lbLocal.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lbLocal.setText("--");

        jLabel12.setText("Data");

        lbData.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lbData.setText("--");

        jLabel14.setText("Horário");

        lbHorario.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lbHorario.setText("--");
        lbHorario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Dados do Evento");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btSelEvento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Salvar.png"))); // NOI18N
        btSelEvento.setText("Selecionar");
        btSelEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelEventoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpEventoLayout = new javax.swing.GroupLayout(jpEvento);
        jpEvento.setLayout(jpEventoLayout);
        jpEventoLayout.setHorizontalGroup(
            jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEventoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpEventoLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpEventoLayout.createSequentialGroup()
                        .addGroup(jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbEvento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpEventoLayout.createSequentialGroup()
                                .addGroup(jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEventoLayout.createSequentialGroup()
                                .addGroup(jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpEventoLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jpEventoLayout.createSequentialGroup()
                                        .addComponent(lbData, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                                        .addGap(15, 15, 15)))
                                .addGroup(jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEventoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSelEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpEventoLayout.setVerticalGroup(
            jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEventoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbLocal)
                .addGap(18, 18, 18)
                .addGroup(jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbData)
                    .addComponent(lbHorario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btSelEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpIngresso.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(153, 153, 153))); // NOI18N

        jLabel5.setText("Selecione o Ingresso");

        btSelIngresso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Salvar.png"))); // NOI18N
        btSelIngresso.setText("Selecionar");
        btSelIngresso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelIngressoActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(153, 153, 153));
        jLabel21.setText("Dados do Ingresso e Lote");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        cbIngresso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbIngressoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpIngressoLayout = new javax.swing.GroupLayout(jpIngresso);
        jpIngresso.setLayout(jpIngressoLayout);
        jpIngressoLayout.setHorizontalGroup(
            jpIngressoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIngressoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpIngressoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpIngressoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btSelIngresso, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpIngressoLayout.createSequentialGroup()
                        .addGroup(jpIngressoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 372, Short.MAX_VALUE))
                    .addComponent(cbIngresso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpIngressoLayout.setVerticalGroup(
            jpIngressoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIngressoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbIngresso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSelIngresso, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(153, 153, 153))); // NOI18N

        tbListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EVENTO", "DESCRIÇÃO O INGRESSO", "LOTE"
            }
        ));
        jScrollPane1.setViewportView(tbListagem);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 153, 153));
        jLabel10.setText("Dados da Venda");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Excluir.png"))); // NOI18N
        btRemover.setText("Remover");
        btRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoverActionPerformed(evt);
            }
        });

        lbTotalPagar.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        lbTotalPagar.setForeground(new java.awt.Color(0, 0, 102));
        lbTotalPagar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar.setText("0,00");
        lbTotalPagar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("Valor Total a Pagar:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTotalPagar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btRemover, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTotalPagar)
                .addGap(18, 18, 18))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btFinalizarVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendaIngresso.png"))); // NOI18N
        btFinalizarVenda.setText("Finalizar Venda e Gerar Ingresso");
        btFinalizarVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFinalizarVendaActionPerformed(evt);
            }
        });

        optDinheiro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        optDinheiro.setText("Dinheiro");
        optDinheiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optDinheiroActionPerformed(evt);
            }
        });

        optCartao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        optCartao.setText("Cartão (Débito / Crédito)");
        optCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optCartaoActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(153, 153, 153));
        jLabel18.setText("Forma de Pagamento");
        jLabel18.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        cbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cancelar.png"))); // NOI18N
        cbCancelar.setText("Cancelar");
        cbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCancelarActionPerformed(evt);
            }
        });

        jLabel11.setText("Selecione a opções de Pagamento");

        lbQuantidadeLote1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbQuantidadeLote1.setForeground(new java.awt.Color(153, 153, 153));
        lbQuantidadeLote1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbQuantidadeLote1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/dinheiro2.png"))); // NOI18N
        lbQuantidadeLote1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lbQuantidadeLote2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbQuantidadeLote2.setForeground(new java.awt.Color(153, 153, 153));
        lbQuantidadeLote2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbQuantidadeLote2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cartao2.png"))); // NOI18N
        lbQuantidadeLote2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 191, Short.MAX_VALUE)
                        .addComponent(btFinalizarVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lbQuantidadeLote1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(optDinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lbQuantidadeLote2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(optCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel11)
                        .addGap(13, 13, 13)
                        .addComponent(optDinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbQuantidadeLote1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbQuantidadeLote2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFinalizarVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jpLote.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(153, 153, 153))); // NOI18N

        cbLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLoteActionPerformed(evt);
            }
        });

        jLabel6.setText("Selecione o Lote");

        lbValorLote.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbValorLote.setForeground(new java.awt.Color(153, 153, 153));
        lbValorLote.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValorLote.setText("0,00");
        lbValorLote.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel8.setText("Valor do Lote do Ingresso");

        btAdcionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Add2.png"))); // NOI18N
        btAdcionar.setText("Adcionar");
        btAdcionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAdcionarActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(153, 153, 153));
        jLabel19.setText("Dados do Ingresso e Lote");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lbQuantidadeLote.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbQuantidadeLote.setForeground(new java.awt.Color(153, 153, 153));
        lbQuantidadeLote.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbQuantidadeLote.setText("00");
        lbQuantidadeLote.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel9.setText("Quantidade");

        javax.swing.GroupLayout jpLoteLayout = new javax.swing.GroupLayout(jpLote);
        jpLote.setLayout(jpLoteLayout);
        jpLoteLayout.setHorizontalGroup(
            jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbLote, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpLoteLayout.createSequentialGroup()
                        .addGroup(jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbValorLote, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(47, 47, 47)
                        .addGroup(jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbQuantidadeLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpLoteLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btAdcionar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpLoteLayout.setVerticalGroup(
            jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel19)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbValorLote)
                    .addComponent(jLabel6)
                    .addComponent(lbQuantidadeLote))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLote, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdcionar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpIngresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpLote, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpIngresso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jpLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEventoActionPerformed
        carregaDadosEvento();
    }//GEN-LAST:event_cbEventoActionPerformed

    private void cbIngressoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbIngressoActionPerformed

    }//GEN-LAST:event_cbIngressoActionPerformed

    private void btSelEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelEventoActionPerformed
        validaEventoSelecionado();
    }//GEN-LAST:event_btSelEventoActionPerformed

    private void cbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCancelarActionPerformed
        //this.setVisible(false);
        cancelar();
    }//GEN-LAST:event_cbCancelarActionPerformed

    private void cbEventoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbEventoMouseClicked


    }//GEN-LAST:event_cbEventoMouseClicked

    private void btSelIngressoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelIngressoActionPerformed
        validaIngressoSelecionado();;
    }//GEN-LAST:event_btSelIngressoActionPerformed

    private void cbLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLoteActionPerformed
        carregaDadosLote();
    }//GEN-LAST:event_cbLoteActionPerformed

    private void btAdcionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAdcionarActionPerformed
        validaLoteSelecionado();
    }//GEN-LAST:event_btAdcionarActionPerformed

    private void btRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoverActionPerformed
        limparListagem(tbListagem);
    }//GEN-LAST:event_btRemoverActionPerformed

    private void optDinheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optDinheiroActionPerformed
        optCartao.setSelected(false);
    }//GEN-LAST:event_optDinheiroActionPerformed

    private void optCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optCartaoActionPerformed
        optDinheiro.setSelected(false);
    }//GEN-LAST:event_optCartaoActionPerformed

    private void btFinalizarVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFinalizarVendaActionPerformed
        finalizarVenda();
    }//GEN-LAST:event_btFinalizarVendaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VendaIngressoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendaIngressoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendaIngressoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendaIngressoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VendaIngressoView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdcionar;
    private javax.swing.JButton btFinalizarVenda;
    private javax.swing.JButton btRemover;
    private javax.swing.JButton btSelEvento;
    private javax.swing.JButton btSelIngresso;
    private javax.swing.JButton cbCancelar;
    private javax.swing.JComboBox<String> cbEvento;
    private javax.swing.JComboBox<String> cbIngresso;
    private javax.swing.JComboBox<String> cbLote;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpEvento;
    private javax.swing.JPanel jpIngresso;
    private javax.swing.JPanel jpLote;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbHorario;
    private javax.swing.JLabel lbLocal;
    private javax.swing.JLabel lbQuantidadeLote;
    private javax.swing.JLabel lbQuantidadeLote1;
    private javax.swing.JLabel lbQuantidadeLote2;
    private javax.swing.JLabel lbTotalPagar;
    private javax.swing.JLabel lbValorLote;
    private javax.swing.JRadioButton optCartao;
    private javax.swing.JRadioButton optDinheiro;
    private javax.swing.JTable tbListagem;
    // End of variables declaration//GEN-END:variables

    private void JOptionPane(Object object, String selecione_um_evento) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
