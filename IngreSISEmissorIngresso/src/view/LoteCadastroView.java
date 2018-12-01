package view;

import controller.IngressoDao;
import controller.LoteDao;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.bean.Ingresso;
import model.bean.Lote;


public class LoteCadastroView extends javax.swing.JFrame {

    public LoteCadastroView() {
        initComponents();
        this.setLocationRelativeTo(null);
        //Retirando a Mximização da tela
        this.setResizable(false);
        iniciarTela();
    }

    int statusAcao = 0; //1 - Novo / 2 - Alterar 

    private void habilitaBotoesCrud() {
        btAterar.setEnabled(true);
        btExcluir.setEnabled(true);
        btConsultar.setEnabled(true);
        btExcluir.setEnabled(true);
        btNovo.setEnabled(true);
    }

    private void desabilitaBotoesCrud() {
        btAterar.setEnabled(false);
        btExcluir.setEnabled(false);
        btConsultar.setEnabled(false);
        btExcluir.setEnabled(false);
        btNovo.setEnabled(false);
    }

    private void habilitaBotoesManterCrud() {
        btCancelar.setEnabled(true);
        btSalvar.setEnabled(true);
    }

    private void desabilitaBotoesManterCrud() {
        btCancelar.setEnabled(false);
        btSalvar.setEnabled(false);
    }

    private void limparCampos() {
        txtPesquisa.setText("");
        spNumLote.setValue(0);
        txtValor.setText("");
        spQuantidade.setValue(0);
        txtId.setText("");
        cbEventoIngresso.removeAllItems();
    }

    private void habilitaCampos() {
        txtPesquisa.setEnabled(true);
        spNumLote.setEnabled(true);
        txtValor.setEnabled(true);
        spQuantidade.setEnabled(true);
        cbEventoIngresso.setEnabled(true);
    }

    private void desabilitaCampos() {
        txtPesquisa.setEnabled(false);
        spNumLote.setEnabled(false);
        txtValor.setEnabled(false);
        spQuantidade.setEnabled(false);
        txtId.setEnabled(false);
        cbEventoIngresso.setEnabled(false);
    }

    private void desabilitaGridListagem() {
        tbListagem.setEnabled(false);
        tbListagem.setRowSelectionAllowed(false);
    }

    private void habilitaGridListagem() {
        tbListagem.setEnabled(true);
        tbListagem.setRowSelectionAllowed(true);
    }

     private void tamanhoColunasGridListagem() {
        tbListagem.setAutoResizeMode(0); //Desabilita tamanho automatico coluna
        tbListagem.getColumnModel().getColumn(0).setPreferredWidth(50);  //id
        tbListagem.getColumnModel().getColumn(1).setPreferredWidth(200); //nome evento 
        tbListagem.getColumnModel().getColumn(2).setPreferredWidth(200); //descrição ingresso
        tbListagem.getColumnModel().getColumn(3).setPreferredWidth(50); //numero lote
        tbListagem.getColumnModel().getColumn(4).setPreferredWidth(100); //quantidade lote
        tbListagem.getColumnModel().getColumn(5).setPreferredWidth(100); //valor
    }
     
    private void iniciarTela() {
        limparCampos();
        habilitaBotoesCrud();
        desabilitaBotoesManterCrud();
        desabilitaCampos();
        desabilitaGridListagem();
        txtPesquisa.setEnabled(true);
        txtPesquisa.setRequestFocusEnabled(true);
        tamanhoColunasGridListagem();
    }

    private void consultar() {
        if (txtPesquisa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum registro localizado.");
            txtPesquisa.requestFocus();
        } else {
            habilitaGridListagem();
            carregaListagem(txtPesquisa.getText().toUpperCase());
        }
    }

    private void Novo() {
        desabilitaBotoesCrud();
        desabilitaGridListagem();
        habilitaBotoesManterCrud();
        limparCampos();
        limparListagem(tbListagem);
        habilitaCampos();
        txtPesquisa.setEnabled(false);
        listarIngresso();
    }

    private void alterar() {

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "É necessário selecionar um registro.");
            txtPesquisa.requestFocus();
        } else {
            desabilitaBotoesCrud();
            habilitaBotoesManterCrud();
            habilitaCampos();
            txtPesquisa.setEnabled(false);
            cbEventoIngresso.setEnabled(false);
        }
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "É necessário selecionar um registro.");
            txtPesquisa.requestFocus();
        } else {
            Lote lote = new Lote();
            LoteDao loteDao = new LoteDao();
            lote.setIdLote(Integer.parseInt(txtId.getText()));
            loteDao.excluir(lote);
            DefaultTableModel linha = (DefaultTableModel) tbListagem.getModel();
            linha.removeRow(tbListagem.getSelectedRow());
            limparCampos();
            iniciarTela();
            
            if (tbListagem.getRowCount() > 0) {
                habilitaGridListagem();
            } else {
                desabilitaGridListagem();
            }
        }
    }

    private void salvar() {
        Lote lote = new Lote();
        LoteDao loteDao = new LoteDao();
        IngressoDao ingressoDao = new IngressoDao();
        
        lote.setIdIngressoLote(ingressoDao.consultaUnicoIngresso(cbEventoIngresso.getSelectedItem().toString()).getIdIngresso());
        lote.setNumeroLote(spNumLote.getValue().toString());
        lote.setQuantidadeLote(Integer.parseInt(spQuantidade.getValue().toString()));
        lote.setValorLote(Double.parseDouble(txtValor.getText()));
        if (verificaCamposObrigatorios()) {
            if (statusAcao == 1) {
                loteDao.cadastrar(lote);
                limparCampos();
                iniciarTela();
                statusAcao = 0;
            }
            if (statusAcao == 2) {
                lote.setIdLote(Integer.parseInt(txtId.getText()));
                loteDao.alterar(lote);
                limparCampos();
                iniciarTela();
                limparListagem(tbListagem);
                habilitaGridListagem();
                statusAcao = 0;
            }
        }
    }

    private void carregaRegistroSelecionado() {
        if (tbListagem.getRowSelectionAllowed()) {
            try {
                cbEventoIngresso.removeAllItems();
                txtId.setText(tbListagem.getValueAt(tbListagem.getSelectedRow(), 0).toString());
                cbEventoIngresso.addItem(tbListagem.getValueAt(tbListagem.getSelectedRow(), 1).toString() + " - " + tbListagem.getValueAt(tbListagem.getSelectedRow(), 2).toString());
                spNumLote.setValue(Integer.parseInt(tbListagem.getValueAt(tbListagem.getSelectedRow(), 3).toString()));
                spQuantidade.setValue(Integer.parseInt(tbListagem.getValueAt(tbListagem.getSelectedRow(), 4).toString()));
                txtValor.setText(tbListagem.getValueAt(tbListagem.getSelectedRow(), 5).toString());
                
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void limparListagem(JTable tbGridListagem) {
        DefaultTableModel linha = (DefaultTableModel) tbGridListagem.getModel();
        while (linha.getRowCount() > 0) {
            linha.removeRow(0);
        }
    }

    private void carregaListagem(String consulta) {
        
        LoteDao loteDao = new LoteDao();
        List<Lote> listaLote = new ArrayList<Lote>();
        listaLote = loteDao.consultar(consulta);
        
        DefaultTableModel linha = (DefaultTableModel) tbListagem.getModel();
        limparListagem(tbListagem);
        try {
            for (Lote lote : listaLote) {
                linha.addRow(new Object[]{
                    lote.getIdLote(),
                    lote.getNomeEvento(),
                    lote.getDescricaoIngresso(),
                    lote.getNumeroLote(),
                    lote.getQuantidadeLote(),
                    lote.getValorLote(),
                });
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem no grid");
        }

        if ((listaLote.size() == 0) && (statusAcao != 3)) {
            JOptionPane.showMessageDialog(this, "Nenhum registro localizado.");
        }

    }

    private boolean verificaCamposObrigatorios() {
        if (cbEventoIngresso.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Há campos Obrigatórios não preenchidos");
            return false;
        }
        if (spNumLote.getValue().equals(0)) {
            JOptionPane.showMessageDialog(this, "O campo Numero do Lote não pode ser zerado");
            spNumLote.requestFocus();
            return false;
        } else if (txtValor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Há campos Obrigatórios não preenchidos");
            txtValor.requestFocus();
            return false;
        } else if (spQuantidade.getValue().equals(0)) {
            JOptionPane.showMessageDialog(this, "O campo Quantidade não pode ser zerado");
            spQuantidade.requestFocus();
            return false;
        } else {

            return true;
        }
    }
    
    private void listarIngresso() {
        cbEventoIngresso.removeAllItems();
        IngressoDao ingressoDao = new IngressoDao();
        Ingresso ingresso = new Ingresso();
        List<Ingresso> listaIngresso = new ArrayList<Ingresso>();
        listaIngresso = ingressoDao.listar();
        try {
            for (Ingresso ingres : listaIngresso) {
                cbEventoIngresso.addItem(ingres.getNomeEvento() + " - " + ingres.getDescricaoIngresso());
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel carregar a Listagem no cbEventoIngresso");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btExcluir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbEventoIngresso = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        spNumLote = new javax.swing.JSpinner();
        spQuantidade = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbListagem = new javax.swing.JTable();
        btConsultar = new javax.swing.JButton();
        btNovo = new javax.swing.JButton();
        btAterar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setTitle("Cadastro de Lote");

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Excluir.png"))); // NOI18N
        btExcluir.setText("Excluir");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        jLabel1.setText("Pesquisa de Eventos ... Informe o Nome do Evento");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Dados do Lote"));

        jLabel3.setText("Valor");

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cancelar.png"))); // NOI18N
        btCancelar.setText("Cancelar");
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Salvar.png"))); // NOI18N
        btSalvar.setText("Salvar");
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        jLabel4.setText("ID");

        jLabel5.setText("Numero do Lote");

        cbEventoIngresso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEventoIngressoActionPerformed(evt);
            }
        });

        jLabel2.setText("Evento - Descrição do Ingresso");

        jLabel6.setText("Quantidade");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("*");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("*");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("*");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("*");

        spNumLote.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        spQuantidade.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbEventoIngresso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(spNumLote, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spQuantidade, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtValor, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(160, 160, 160)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbEventoIngresso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spNumLote, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        tbListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "EVENTO", "DESCRIÇÃO DO INGRESSO", "LOTE", "QUANTIDADE", "VALOR (R$)"
            }
        ));
        tbListagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListagemMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbListagem);

        btConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/consultar.png"))); // NOI18N
        btConsultar.setText("Consultar");
        btConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultarActionPerformed(evt);
            }
        });

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Add2.png"))); // NOI18N
        btNovo.setText("Novo Lote");
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btAterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Alterar.png"))); // NOI18N
        btAterar.setText("Alterar");
        btAterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAterarActionPerformed(evt);
            }
        });

        jLabel7.setText("Listagem de Lotes");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btAterar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAterar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btConsultar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btExcluirActionPerformed

    private void cbEventoIngressoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEventoIngressoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEventoIngressoActionPerformed

    private void btConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultarActionPerformed
        consultar();
    }//GEN-LAST:event_btConsultarActionPerformed

    private void tbListagemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListagemMouseClicked
        carregaRegistroSelecionado();
    }//GEN-LAST:event_tbListagemMouseClicked

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed
         Novo();
        statusAcao = 1;
    }//GEN-LAST:event_btNovoActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btAterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAterarActionPerformed
        alterar();
        statusAcao = 2;
    }//GEN-LAST:event_btAterarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        iniciarTela();
    }//GEN-LAST:event_btCancelarActionPerformed

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
            java.util.logging.Logger.getLogger(LoteCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoteCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoteCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoteCadastroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoteCadastroView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAterar;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btConsultar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btSalvar;
    private javax.swing.JComboBox<String> cbEventoIngresso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spNumLote;
    private javax.swing.JSpinner spQuantidade;
    private javax.swing.JTable tbListagem;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
