package webservice;

import entidade.Endereco;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.primefaces.json.JSONObject;
import util.EnvelopeEndereco;

public class CepWebService {

    private static final String URL_WEBSERVICE = "http://viacep.com.br/ws/";
    private static final String URL_END = "/json/";

    public static EnvelopeEndereco buscaEndereco(String cep) {

        HttpURLConnection connection = null;
           
        try {
            
            URL url = new URL(URL_WEBSERVICE + cep + URL_END);
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputstream = connection.getInputStream();
            String strContent = getStringFromInputStream(inputstream);
            JSONObject jsonObj = new JSONObject(strContent);
            
            if(jsonObj.optBoolean("erro")) return null;
           
            EnvelopeEndereco endereco = new EnvelopeEndereco(jsonObj.getString("bairro"),
                    jsonObj.getString("complemento"), jsonObj.getString("localidade"),
                    jsonObj.getString("logradouro"), jsonObj.getString("uf"));

            return endereco;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }
    
    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
