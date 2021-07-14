package com.desafio.zup.service;
import com.desafio.zup.feign.ComicFeignInterface;
import com.desafio.zup.model.Comic;
import com.desafio.zup.model.Usuario;
import com.desafio.zup.repository.ComicRepository;
import com.desafio.zup.model.UsuarioComics;
import com.desafio.zup.repository.UsuarioRepository;
import com.desafio.zup.util.ClasseUtil;
import feign.Feign;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Import(FeignClientsConfiguration.class)
public class DesafioService {
    //Variavel para armazenar os Autores do livro
    private String creators;
    private final ComicFeignInterface comicFeignInterface;
    private final ComicRepository comicRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClasseUtil classeUtil;

    @Autowired
    public DesafioService(Decoder decoder, Encoder encoder, ComicRepository comicRepository, UsuarioRepository usuarioRepository, ClasseUtil classeUtil) {
        this.comicRepository = comicRepository;
        this.usuarioRepository = usuarioRepository;
        this.classeUtil = classeUtil;
        comicFeignInterface = Feign.builder().encoder(encoder).decoder(decoder)
                .target(Target.EmptyTarget.create(ComicFeignInterface.class));
    }

    //método que acessa a API da marvel utilizando o Feign
    public Comic getComicFeign(Comic comic) throws Exception {

        String apikey = "apikey=9d3d16d3e9fc07c9596e152880cb15ac";
        String ts = "ts=1";
        String hash = "hash=e90e670779f2bf5ca87d48e692e8e309";
        String baseURL = "https://gateway.marvel.com";
        String pathURLcomics = "/v1/public/comics/";
        String fullURLcomics = baseURL + pathURLcomics + comic.getComicId().toString() + "?" + apikey + "&" + ts + "&" + hash;
        String fullURLcreators = baseURL + pathURLcomics + comic.getComicId().toString() + "/creators?" + apikey + "&" + ts + "&" + hash;

        //Salva o retorno da API comics em uma string
        String comicStringJson = comicFeignInterface.getComic(new URI(fullURLcomics));

        //Salva o retorno da API creators em uma string
        String creatorsStringJson = comicFeignInterface.getCreators(new URI(fullURLcreators));

        //Chama o método pegarChaveValorDoJson
        comic = popularComic(comic, comicStringJson);
        comic = popularCreators(comic, creatorsStringJson);

        return comic;

    }

    //método para popular o objeto comic com os valores da API//Recebe como parametro um objeto comic e uma jsonString
    public Comic popularComic(Comic comic, String jsonString) throws Exception {

        //Converte a jsonString recebida para um jsonObject
        JSONObject objetoJSON = new JSONObject(jsonString);
        try {
            //Recuperando os valores de cada chave enviada

            String title = pegarChaveValorDoJson("title", objetoJSON);
            if (title != null && title.length() > 0)
                comic.setTitle(title);

            String descricao = pegarChaveValorDoJson("description", objetoJSON);
            if (descricao != null && descricao.length() > 0)
                comic.setDescription(descricao);

            String isbn = pegarChaveValorDoJson("isbn", objetoJSON);
            DayOfWeek diaDesconto = null;
            if (isbn != null && isbn.length() > 0) {
                comic.setIsbn(isbn);
                diaDesconto = verificaDiaDesconto(isbn);
            }
            if(diaDesconto!=null)
                comic.setDiaDesconto(diaDesconto);

            List<JSONObject> precos = new ArrayList<>();

            String precosString = pegarChaveValorDoJson("prices", objetoJSON);
            if (precosString != null && precosString.length() > 0) {
                JSONArray jsonArray = new JSONArray(precosString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    precos.add(jsonArray.getJSONObject(i));
                }
            }
            String precoDigital;
            String precoImpresso;

            if (precos.size() == 2) {
                precoDigital = String.valueOf(precos.get(1).get("price"));
                precoImpresso = String.valueOf(precos.get(0).get("price"));
            } else if (precos.size() == 1){
                precoImpresso = String.valueOf(precos.get(0).get("price"));
                precoDigital = "0";
            }else
            {
                precoImpresso = "0";
                precoDigital = "0";
            }

            comic.setPriceDigital(precoDigital);
            comic.setPriceImpresso(precoImpresso);

        } catch (Exception e) {
            throw new Exception("erro ao popular o livro: " + e.getMessage());
        }
        return comic;
    }

    //método para popular os autores do objeto comic com os valores da API CREATORS//Recebe como parametro um objeto comic e uma jsonString
    public Comic popularCreators(Comic comic, String jsonString) throws Exception {
        try {
            //Converte a jsonString recebida para um jsonObject
            JSONObject objetoJSON = new JSONObject(jsonString);

            //Recuperando o valor dos nomes dos Autores e armazenando na variavel Creators
            pegarChaveValorDoJson("fullName", objetoJSON);

            //Remove os 2 ultimos caracteres da String de autores (',' e 'espaço')
            if (this.creators != null && this.creators.length() > 0)
                comic.setCreators(this.creators.substring(0, this.creators.length() - 2));
        } catch (Exception e) {
            throw new Exception("erro ao popular autores: " + e.getMessage());
        }
        //limpando a variavel creators para a proxima chamada.
        this.creators = "";

        return comic;
    }

    //método para pegar as chaves e valores desejados de um objeto json//Recebe como parametro um nome de chave e o objeto JSON
    private String pegarChaveValorDoJson(String nomeChave, JSONObject objetoJSON) throws Exception {
        String valorChave = null;
        //Verifica se há uma chave com o nome recebido no parametro
        boolean chaveExiste = objetoJSON.has(nomeChave);
        Iterator<?> chaves;
        String proximaChave;

        if (!chaveExiste) {//se a chave não existir faça:

            //recupera todas as chaves do objetoJson para o Iterator
            chaves = objetoJSON.keys();

            //enquanto houverem chaves no Iterator faça:
            while (chaves.hasNext()) {

                //salva a proxima chave na variável
                proximaChave = (String) chaves.next();

                //Verifica se tem outro JSON object, ou um JSON Array pegar a chave que queremos
                try {
                    if (objetoJSON.get(proximaChave) instanceof JSONObject) {
                        //se a proxima chave for um objeto json chama o metodo de forma recursiva e passa o novo objeto para procurar a chave que queremos
                        valorChave = pegarChaveValorDoJson(nomeChave, objetoJSON.getJSONObject(proximaChave));

                    } else if (objetoJSON.get(proximaChave) instanceof JSONArray) {
                        //se a proxima chave for um Array json pega cada string do array e converte em objeto para chamar o metodo e procurar a chave
                        JSONArray jsonArray = objetoJSON.getJSONArray(proximaChave);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //pega a string no index i
                            String stringDoArrayJSON = jsonArray.get(i).toString();
                            //cria o objeto a partir da string
                            JSONObject objDoArrayJSON = new JSONObject(stringDoArrayJSON);
                            //chama o metodo de novo
                            valorChave = pegarChaveValorDoJson(nomeChave, objDoArrayJSON);
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("erro ao procurar chave no objeto JSON: " + e.getMessage());
                }
            }
        } else {//se a chave existir faça:

            //Atribiu o valor encontrado ao retorno do método no caso dos Autores
            if (nomeChave.equals("fullName"))
                this.creators += objetoJSON.get(nomeChave) + ", ";
            else
                //Atribiu o valor encontrado ao retorno do método
                valorChave = String.valueOf(objetoJSON.get(nomeChave));
        }

        if (nomeChave.equals("fullName"))
            return this.creators;

        return valorChave;
    }

    //método para verificar se hoje é dia de desconto para o comic de acordo com o dia cadastrado
    private boolean verificaDescontoAtivo(DayOfWeek diaDesconto) {
        boolean descontoAtivo = false;
        if (diaDesconto != null) {
         descontoAtivo = diaDesconto == LocalDate.now().getDayOfWeek();
        }
        return descontoAtivo;
    }
    //método para verificar o dia de desconto para o comic de acordo com o último digito do isbn
    private DayOfWeek verificaDiaDesconto(String isbn) {

        DayOfWeek diaDesconto = null;

        if (isbn != null && isbn.length() > 0) {
            switch (isbn.substring(isbn.length() - 1)) {
                case "0":
                case "1":
                    diaDesconto = DayOfWeek.MONDAY;

                    break;
                case "2":
                case "3":
                    diaDesconto = DayOfWeek.TUESDAY;
                    break;
                case "4":
                case "5":
                    diaDesconto = DayOfWeek.WEDNESDAY;

                    break;
                case "6":
                case "7":
                    diaDesconto = DayOfWeek.THURSDAY;

                    break;
                case "8":
                case "9":
                    diaDesconto = DayOfWeek.FRIDAY;

                    break;
                default:
                    diaDesconto = null;
            }
        }
        return diaDesconto;
    }

    public void addComic(Comic comic) throws Exception {

        if (!classeUtil.isCPF(comic.getUsuario().getCpf()))
            throw new IllegalArgumentException("CPF inválido!");

        Optional<Comic> optionalComic = comicRepository.findByIdAndCpf(comic.getComicId(),comic.getUsuario());

        if(optionalComic.isPresent())
            throw new IllegalArgumentException("comic já cadastrado para este usuário!");

        try {
            comic = getComicFeign(comic);
        } catch (Exception e) {
            throw new Exception("erro na busca: " + e.getMessage());
        }
        try {
            comicRepository.save(comic);
        } catch (Exception e) {
            throw new Exception("erro ao salvar comic: " + e.getMessage());
        }
    }

    public void addUsuario(Usuario usuario) throws Exception {

        if (!classeUtil.isCPF(usuario.getCpf()))
            throw new IllegalArgumentException("CPF inválido!");

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioOptional.isPresent())
            throw new IllegalStateException("Email já utilizado!");

        usuarioOptional = usuarioRepository.ChecarCpfJaCadastrado(usuario.getCpf());

        if (usuarioOptional.isPresent())
            throw new IllegalStateException("CPF já cadastrado!");

        try {
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new Exception("erro ao salvar usuário: " + e.getMessage());
        }

    }

    public UsuarioComics getComicUsuario(String cpf) throws Exception {

        if (!classeUtil.isCPF(cpf))
            throw new IllegalArgumentException("CPF inválido!");

        Usuario usuario;
        UsuarioComics usuarioComics = new UsuarioComics();
        try {
            usuario = usuarioRepository.findByCpf(cpf);

            usuarioComics.setUsuario(usuario);
            List<Comic> lista = comicRepository.findAllByCpf(usuario);

            for (Comic c : lista) {
                c.setDescontoAtivo(verificaDescontoAtivo(c.getDiaDesconto()));
            }
            usuarioComics.setComics(lista);
        } catch (Exception e) {
            throw new Exception("erro na busca: " + e.getMessage());
        }
        return usuarioComics;
    }

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }
}
