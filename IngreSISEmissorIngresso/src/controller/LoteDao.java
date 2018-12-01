package controller;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.bean.Lote;
import model.conexao.Conexao;

public class LoteDao {

    Connection con = null; // Criando conexão com DB
    PreparedStatement ps = null; // Preparando uma ação
    ResultSet rs = null; // Retorna uma Informações do banco
    String sql = "";

    public void cadastrar(Lote lote) {

        con = Conexao.getConexao();
        sql = "insert into lote(numeroLote, quantidadeLote, valorLote, idIngressoLote, statusLote) values (?, ?, ? , ?, 'ATIVO')";
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, lote.getNumeroLote());
            ps.setInt(2, lote.getQuantidadeLote());
            ps.setDouble(3, lote.getValorLote());
            ps.setInt(4, lote.getIdIngressoLote());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Novo Lote cadastrado com sucesso.");
        } catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Não foi possivel cadastrar o Lotes.");
        } finally {
            Conexao.closeConexao(con);
        }
    }

    public List<Lote> consultar(String nomeEvento) {

        con = Conexao.getConexao();
        sql = "select l.idLote, e.nomeEvento, i.descricaoIngresso, l.numeroLote, l.valorLote, l.quantidadeLote, l.statusLote from lote l "
                + "inner join ingresso i on (l.idIngressoLote = i.idIngresso) "
                + "inner join evento e on (i.idEventoIngresso = e.idEvento) "
                + "where l.statusLote = 'ATIVO' "
                + "and e.nomeEvento like '" + nomeEvento + "'";

        List<Lote> lista = new ArrayList<Lote>();
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Lote lote = new Lote();
                lote.setIdLote(rs.getInt("idLote"));
                lote.setNomeEvento(rs.getString("nomeEvento"));
                lote.setDescricaoIngresso(rs.getString("descricaoIngresso"));
                lote.setNumeroLote(rs.getString("numeroLote"));
                lote.setQuantidadeLote(rs.getInt("quantidadeLote"));
                lote.setValorLote(rs.getDouble("valorLote"));
                lista.add(lote);
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Lotes!");
        } finally {
            Conexao.closeConexao(con);
        }
        return lista;
    }

    public void alterar(Lote lote) {

        con = Conexao.getConexao();
        sql = "update lote set numeroLote = ?, valorLote = ?, quantidadeLote = ? where idLote = ?";
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, lote.getNumeroLote());
            ps.setDouble(2, lote.getValorLote());
            ps.setInt(3, lote.getQuantidadeLote());
            ps.setInt(4, lote.getIdLote());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Dados do Lote alterado com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Altear dados!");
        } finally {
            Conexao.closeConexao(con);
        }
    }

    public void excluir(Lote lote) {

        con = Conexao.getConexao();
        sql = "update lote set statusLote = 'DELETADO' where idLote = ?";
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, lote.getIdLote());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Lote excluído com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel excluir os dados!");
        } finally {
            Conexao.closeConexao(con);
        }
    }

    public List<Lote> listarLotePorIngressoEvento(String descricaoIngresso, String nomeEvento) {

        con = Conexao.getConexao();
        sql = "select l.idLote, i.idIngresso, idEventoIngresso, l.numeroLote, l.quantidadeLote, l.valorLote, i.descricaoIngresso, e.nomeEvento from lote l\n"
                + "inner join ingresso i on (l.idIngressoLote = i.idIngresso)\n"
                + "inner join evento e on (i.idEventoIngresso = e.idEvento)\n"
                + "where i.descricaoIngresso = '" + descricaoIngresso + "'\n"
                + "and e.nomeEvento = '" + nomeEvento + "'";

        List<Lote> lista = new ArrayList<Lote>();
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Lote lote = new Lote();
                lote.setIdLote(rs.getInt("idLote"));
                lote.setIdIngressoLote(rs.getInt("idIngresso"));
                lote.setIdEvento(rs.getInt("idEventoIngresso"));
                lote.setNumeroLote(rs.getString("numeroLote"));
                lote.setQuantidadeLote(rs.getInt("quantidadeLote"));
                lote.setValorLote(rs.getDouble("valorLote"));
                lote.setNomeEvento(rs.getString("nomeEvento"));
                lote.setDescricaoIngresso(rs.getString("descricaoIngresso"));
                lista.add(lote);
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Lotes!");
        } finally {
            Conexao.closeConexao(con);
        }
        return lista;
    }

    public List<Lote> listaEventoIngressoLote(String nomeEvento, String descricaoIngresso, String numeroLote) {

        con = Conexao.getConexao();
        sql = "select l.idLote, i.idIngresso, idEventoIngresso, l.numeroLote, l.quantidadeLote, l.valorLote, i.descricaoIngresso, e.nomeEvento from lote l\n"
                + "inner join ingresso i on (l.idIngressoLote = i.idIngresso)\n"
                + "inner join evento e on (i.idEventoIngresso = e.idEvento)\n"
                + "where i.descricaoIngresso = '" + descricaoIngresso + "'\n"
                + "and e.nomeEvento = '" + nomeEvento + "'\n"
                + "and l.numeroLote = '" + numeroLote + "'";

        List<Lote> lista = new ArrayList<Lote>();
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Lote lote = new Lote();
                lote.setIdLote(rs.getInt("idLote"));
                lote.setIdIngressoLote(rs.getInt("idIngresso"));
                lote.setIdEvento(rs.getInt("idEventoIngresso"));
                lote.setNumeroLote(rs.getString("numeroLote"));
                lote.setQuantidadeLote(rs.getInt("quantidadeLote"));
                lote.setValorLote(rs.getDouble("valorLote"));
                lote.setNomeEvento(rs.getString("nomeEvento"));
                lote.setDescricaoIngresso(rs.getString("descricaoIngresso"));
                lista.add(lote);
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Lotes!");
        } finally {
            Conexao.closeConexao(con);
        }
        return lista;
    }

    public int verificaQuantidadeDisponivelLote(int idLote) {

        con = Conexao.getConexao();
        sql = "select quantidadeLote from lote\n"
                + "where idLote = '" + idLote + "'";

        int quantidadeLote = 0;
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                quantidadeLote = (rs.getInt("quantidadeLote"));
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel consultar a quantidade do lote!");
        } finally {
            Conexao.closeConexao(con);
        }
        return quantidadeLote;
    }

    public void baixaQuantidadeLote(int idLote) {

        Lote lote = new Lote();
        int novaQuantidade = (verificaQuantidadeDisponivelLote(idLote) - 1);

        con = Conexao.getConexao();
        sql = "update lote set quantidadeLote = ? where idLote = ?";
        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, novaQuantidade);
            ps.setInt(2, idLote);
            ps.execute();
            System.out.println("Baixa de Lote realizada com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel realizar a baixa de lote!");
        } finally {
            Conexao.closeConexao(con);
        }
    }
}
