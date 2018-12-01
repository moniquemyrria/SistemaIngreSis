package model.bean;

public class Evento {
    
    private Integer idEvento;
    private String nomeEvento;
    private String localEvento;
    private String dataEvento;
    private String horarioEvento;
    private String stausEvento;
    
    public Evento(){

    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getLocalEvento() {
        return localEvento;
    }

    public void setLocalEvento(String localEvento) {
        this.localEvento = localEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getHorarioEvento() {
        return horarioEvento;
    }

    public void setHorarioEvento(String horarioEvento) {
        this.horarioEvento = horarioEvento;
    }

    public String getStausEvento() {
        return stausEvento;
    }

    public void setStausEvento(String stausEvento) {
        this.stausEvento = stausEvento;
    }

}
