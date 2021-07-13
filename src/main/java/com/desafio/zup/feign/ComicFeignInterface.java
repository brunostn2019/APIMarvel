package com.desafio.zup.feign;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import java.net.URI;

@FeignClient(name = "ComicsService")
public interface ComicFeignInterface {

    @RequestLine("GET")
    String getComic(URI uri);

    @RequestLine("GET")
    String getCreators(URI uri);

}
