 package controller;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.bean.Ingresso;
import model.conexao.Conexao;

public class IngressoDao {
    
    Connection con = null; // Criando conexão com DB
    PreparedStatement ps = null; // Preparando uma ação
    ResultSet rs = null; // Retorna uma Informações do banco
    String sql = "";
    
    public void cadastrar(Ingresso ingresso) {

        con = Conexao.getConexao();
	sql = "insert into ingresso(descricaoIngresso, idEventoIngresso, statusIngresso) values (?, ?, 'ATIVO')";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, ingresso.getDescricaoIngresso());
            ps.setInt(2, ingresso.getIdIngressoEvento());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Novo Ingresso cadastrado com sucesso.");
	} catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Não foi possivel cadastrar o Ingresso.");
	} finally {
            Conexao.closeConexao(con);
	}
    }
    
    public List<Ingresso> consultar(String nomeEvento){
		
	con = Conexao.getConexao();
	sql = "select i.idIngresso, e.nomeEvento, i.descricaoIngresso from ingresso i " +
              "inner join evento e on (e.idEvento = i.idEventoIngresso) " +
              "where i.statusIngresso = 'ATIVO' " +
              "and e.nomeEvento like '" + nomeEvento + "'";

	List<Ingresso> lista = new ArrayList<Ingresso>();
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
		rs = ps.executeQuery();
		while (rs.next()){
                    Ingresso ingresso = new Ingresso();
                    ingresso.setIdIngresso(rs.getInt("idIngresso"));
                    ingresso.setNomeEvento(rs.getString("nomeEvento"));
                    ingresso.setDescricaoIngresso(rs.getString("descricaoIngresso"));
                    lista.add(ingresso);
		}
	} catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Eventos!");
        } finally {
                Conexao.closeConexao(con);
        }
        return lista;
    }
     
    public void alterar(Ingresso ingresso) {

	con = Conexao.getConexao();
	sql = "update ingresso set descricaoIngresso = ?, idEventoIngresso = ? where idIngresso = ?";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, ingresso.getDescricaoIngresso());
            ps.setInt(2, ingresso.getIdIngressoEvento());
            ps.setInt(3, ingresso.getIdIngresso());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Dados do Ingresso alterado com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Altear dados!");
        } finally {
            Conexao.closeConexao(con);
	}
    }
    
    public void excluir(Ingresso ingresso) {

	con = Conexao.getConexao();
	sql = "update ingresso set statusIngresso = 'DELETADO' where idIngresso = ?";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, ingresso.getIdIngresso());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Ingresso excluído com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel excluir os dados!");
        } finally {
            Conexao.closeConexao(con);
	}
    }
    
    public List<Ingresso> listar(){
		
	con = Conexao.getConexao();
	sql = "select i.idIngresso, e.nomeEvento, i.descricaoIngresso from ingresso i " +
              "inner join evento e on (e.idEvento = i.idEventoIngresso) "+
              "where i.statusIngresso = 'ATIVO' ";
        
        List<Ingresso> lista = new ArrayList<Ingresso>();
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
		rs = ps.executeQuery();
		while (rs.next()){
                    Ingresso ingresso = new Ingresso();
                    ingresso.setIdIngresso(rs.getInt("idIngresso"));
                    ingresso.setNomeEvento(rs.getString("nomeEvento"));
                    ingresso.setDescricaoIngresso(rs.getString("descricaoIngresso"));
                    lista.add(ingresso);
		}
	} catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Eventos!");
        } finally {
            Conexao.closeConexao(con);
        }
        return lista;
    }
    
    public Ingresso consultaUnicoIngresso(String eventoIngresso){
		
	con = Conexao.getConexao();
	sql = "select i.idIngresso, concat(e.nomeEvento ,' - ', i.descricaoIngresso) as eventoIngresso from ingresso i " +
              "inner join evento e on (e.idEvento = i.idEventoIngresso) "+
              "where i.statusIngresso = 'ATIVO' "+
              "and concat(e.nomeEvento ,' - ', i.descricaoIngresso) = '" + eventoIngresso + "' ";
        Ingresso ingresso = new Ingresso();
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
		rs = ps.executeQuery();
		while (rs.next()){
                    ingresso.setIdIngresso(rs.getInt("idIngresso"));
                    ingresso.setDescricaoIngresso(rs.getString("eventoIngresso"));
		}
	} catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Eventos!");
        } finally {
               Conexao.closeConexao(con);
        }
        
        return ingresso;
    }
    
}
