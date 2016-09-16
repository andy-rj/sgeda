package util;


public class EnvelopeEndereco {
    private String bairro;
    private String complemento;
    private String localidade;
    private String logradouro;
    private String uf;
    
    public EnvelopeEndereco(String bairro, String complemento, String localidade, String logradouro, String uf){
        this.bairro = bairro;
        this.complemento = complemento;
        this.localidade = localidade;
        this.logradouro = logradouro;
        this.uf = uf;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
