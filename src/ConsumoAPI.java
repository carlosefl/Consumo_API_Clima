import org.json.JSONObject;

import  java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
public class ConsumoAPI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome da Cidade: ");
        String cidade = scanner.nextLine();
        try{

            String dadosClimaticos = getDadosClimaticos(cidade); //retorna um JSON
            // CODIGO 1006 é localização não é encontrada.
            if(dadosClimaticos.contains(("\"code\":1006"))){
                System.out.println("Localização não encontrada. Por favor, tente novamento");

            }else {
                imprimirDadosClimaticos(dadosClimaticos);
            }
        } catch (Exception e) {
            System.out.println("ocorreu um erro: "+e.getMessage());
        }

    }

    public static String getDadosClimaticos(String localizacao)throws Exception {
            String apiKey = Files.readString(Paths.get("src/codigo.txt")).trim();
            String formataNomeCidade = URLEncoder.encode(localizacao, StandardCharsets.UTF_8);
            String apiURL = "http://api.weatherapi.com/v1/current.json?key="+ apiKey+"&q="+ formataNomeCidade;

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiURL)).build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
    }
    public static void imprimirDadosClimaticos(String dados){


        JSONObject dadosJson = new JSONObject(dados);
        JSONObject informacoesMeterologicas = dadosJson.getJSONObject("current");

        //extrai os dados da localização
        String cidade = dadosJson.getJSONObject("location").getString("name");
        String pais = dadosJson.getJSONObject("location").getString("country");

        // extrai informações do tempo

        String condicaoTempo = informacoesMeterologicas.getJSONObject("condition").getString("text");
        int umidade = informacoesMeterologicas.getInt("humidity");
        float velocidadeVento = informacoesMeterologicas.getFloat("wind_kph");
        float pressaoAtmosferica = informacoesMeterologicas.getFloat("pressure_mb");
        float senssaoTermica = informacoesMeterologicas.getFloat("feelslike_c");
        float temperaturaAtual = informacoesMeterologicas.getFloat("temp_c");





        String dataHora = informacoesMeterologicas.getString("last_updated");

        System.out.println("Informações Meteorológicas para "+cidade+", "+pais);
        System.out.println("Data e Hora: "+dataHora);
        System.out.println("Temperatura Atual: "+temperaturaAtual+"°c");
        System.out.println("Sensação Termica: "+ senssaoTermica+"°c");
        System.out.println("Condições do Tempo: "+condicaoTempo);
        System.out.println("Umidade: "+umidade+"%");
        System.out.println("Velocidade do Vento: "+velocidadeVento +" km/h");
        System.out.println("Pressão Atmosférica: "+pressaoAtmosferica+" mb");




    }
}