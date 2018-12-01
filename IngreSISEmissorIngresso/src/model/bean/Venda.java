
package model.bean;


public class Venda {
    
    private int idVenda;
    private String formaPagamentoVenda;
    private Double valorVenda;
    private int idEventoVenda;
    private int idIngressoVenda;
    private int idLoteVenda;
    
    public Venda(){

    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public String getFormaPagamentoVenda() {
        return formaPagamentoVenda;
    }

    public void setFormaPagamentoVenda(String formaPagamentoVenda) {
        this.formaPagamentoVenda = formaPagamentoVenda;
    }

    
    
    public int getIdEventoVenda() {
        return idEventoVenda;
    }

    public void setIdEventoVenda(int idEventoVenda) {
        this.idEventoVenda = idEventoVenda;
    }

    public int getIdIngressoVenda() {
        return idIngressoVenda;
    }

    public void setIdIngressoVenda(int idIngressoVenda) {
        this.idIngressoVenda = idIngressoVenda;
    }

    public int getIdLoteVenda() {
        return idLoteVenda;
    }

    public void setIdLoteVenda(int idLoteVenda) {
        this.idLoteVenda = idLoteVenda;
    }

   public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }
}
